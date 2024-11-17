package com.eatpizzaquickly.couponservice.service;

import com.eatpizzaquickly.couponservice.client.ApiResponse;
import com.eatpizzaquickly.couponservice.client.UserClient;
import com.eatpizzaquickly.couponservice.common.config.UserCouponsChangedEvent;
import com.eatpizzaquickly.couponservice.dto.CouponRequestDto;
import com.eatpizzaquickly.couponservice.dto.CouponResponseDto;
import com.eatpizzaquickly.couponservice.dto.UserResponseDto;
import com.eatpizzaquickly.couponservice.entity.Coupon;
import com.eatpizzaquickly.couponservice.entity.CouponType;
import com.eatpizzaquickly.couponservice.entity.DiscountType;
import com.eatpizzaquickly.couponservice.entity.UserCoupon;
import com.eatpizzaquickly.couponservice.exception.*;
import com.eatpizzaquickly.couponservice.kafka.CouponEvent;
import com.eatpizzaquickly.couponservice.kafka.CouponEventProducer;
import com.eatpizzaquickly.couponservice.kafka.SendCouponEmailProducer;
import com.eatpizzaquickly.couponservice.repository.CouponsRepository;
import com.eatpizzaquickly.couponservice.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private static final String COUPON_CACHE = "coupon";
    private static final String USER_COUPONS_CACHE = "userCoupons";
    private static final String LOCK_PREFIX = "coupon:lock:";
    private static final long WAIT_TIME = 3L;
    private static final long LEASE_TIME = 5L;

    private final CouponsRepository couponsRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserClient userClient;
    private final ApplicationEventPublisher eventPublisher;
    private final CouponCacheService couponCacheService;
    private final RedissonClient redissonClient;
    private final CouponEventProducer couponEventProducer;
    private final SendCouponEmailProducer sendCouponEmailProducer;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @CacheEvict(value = {COUPON_CACHE, USER_COUPONS_CACHE}, allEntries = true)
    public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto) {
        Coupon coupon = Coupon.builder()
                .couponName(couponRequestDto.getCouponName())
                .couponCode(couponRequestDto.getCouponCode())
                .couponType(couponRequestDto.getCouponType())
                .discountType(couponRequestDto.getDiscountType())
                .discount(couponRequestDto.getDiscount())
                .price(couponRequestDto.getPrice())
                .quantity(couponRequestDto.getQuantity())
                .expiryDate(couponRequestDto.getExpiryDate())
                .build();
        Coupon savedCoupon = couponsRepository.save(coupon);
        couponCacheService.saveCouponToCache(savedCoupon);
        return CouponResponseDto.from(savedCoupon);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Caching(evict = {
            @CacheEvict(value = COUPON_CACHE, key = "#couponCode"),
            @CacheEvict(value = USER_COUPONS_CACHE, key = "#userId")
    })
    public void issueCouponToUser(Long userId, String couponCode) {
        String lockKey = LOCK_PREFIX + couponCode;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new CouponLockException("쿠폰 발급 처리 중입니다. 잠시 후 다시 시도해주세요.");
            }

            UserResponseDto user = userClient.getUserById(userId).getData();
            if (user == null || user.getEmail() == null) {
                throw new UserNotFoundException("유효하지 않은 사용자입니다.");
            }

            Coupon coupon = couponCacheService.findCouponByCouponCode(couponCode);
            if (coupon.getCouponType() != CouponType.LIMIT || coupon.getQuantity() <= 0) {
                throw new CouponTypeMissMatched("쿠폰 타입 또는 수량을 다시 확인 해 주세요");
            }

            if (isCouponAlreadyIssued(userId, coupon.getId())) {
                throw new DuplicateCouponException("이미 발급받은 쿠폰입니다.");
            }

            UserCoupon userCoupon = UserCoupon.builder()
                    .userId(userId)
                    .couponId(coupon.getId())
                    .expiryDate(coupon.getExpiryDate())
                    .build();
            userCouponRepository.save(userCoupon);
            coupon.decreaseQuantity();
            couponsRepository.save(coupon);

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            try {
                                CouponEvent event = CouponEvent.builder()
                                        .eventType("SINGLE_ISSUE")
                                        .couponId(coupon.getId())
                                        .userId(userId)
                                        .email(user.getEmail())
                                        .couponCode(couponCode)
                                        .timestamp(LocalDateTime.now())
                                        .notificationMessage(String.format("%s님, [%s] 쿠폰이 발급되었습니다. %s까지 사용 가능합니다.",
                                                user.getNickname(), coupon.getCouponName(), coupon.getExpiryDate()))
                                        .build();
                                couponEventProducer.sendCouponEvent(event);
                                sendCouponEmailProducer.sendCouponEmail(event);
                                eventPublisher.publishEvent(new UserCouponsChangedEvent(this, userId));
                            } catch (Exception e) {
                                log.error("쿠폰 이벤트 발행 실패: ", e);
                            }
                        }
                    });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CouponLockException("쿠폰 발급 처리 중 인터럽트가 발생했습니다.");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    @Transactional
    @CacheEvict(value = COUPON_CACHE, key = "#couponId")
    public void issueCouponToAllUsers(Long couponId) {
        String lockKey = LOCK_PREFIX + "bulk:" + couponId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new CouponLockException("대량 쿠폰 발급 처리 중입니다. 잠시 후 다시 시도해주세요.");
            }

            Coupon originalCoupon = couponCacheService.findCouponById(couponId);
            if (originalCoupon.getCouponType() != CouponType.ALL) {
                throw new CouponTypeMissMatched("쿠폰 타입을 다시 확인 해 주세요");
            }

            // 모든 사용자 정보를 한 번에 가져옵니다.
            List<UserResponseDto> users = userClient.getAllUsers();

            if (originalCoupon.getQuantity() < users.size()) {
                throw new CouponOutOfStockException("모든 사용자에게 발급할 수량이 부족합니다.");
            }

            // 중복 발급 체크 후 필터링
            List<UserCoupon> userCoupons = users.stream()
                    .filter(user -> !isCouponAlreadyIssued(user.getId(), couponId))
                    .map(user -> UserCoupon.builder()
                            .userId(user.getId())
                            .couponId(originalCoupon.getId())
                            .expiryDate(originalCoupon.getExpiryDate())
                            .isUsed(false)
                            .build())
                    .toList();

            // 실제 발급될 쿠폰 수량 확인
            int issuableCount = userCoupons.size();
            if (issuableCount == 0) {
                throw new DuplicateCouponException("모든 사용자가 이미 쿠폰을 보유하고 있습니다.");
            }

            userCouponRepository.saveAll(userCoupons);

            // 실제 발급된 수량만큼만 감소
            originalCoupon.decreaseQuantity(issuableCount);
            couponsRepository.save(originalCoupon);

            // 쿠폰이 발급된 사용자에게만 이벤트 발행
            userCoupons.forEach(userCoupon -> {
                UserResponseDto user = users.stream()
                        .filter(u -> u.getId().equals(userCoupon.getUserId()))
                        .findFirst()
                        .orElseThrow();

                CouponEvent event = CouponEvent.builder()
                        .eventType("BULK_ISSUE")
                        .couponId(originalCoupon.getId())
                        .userId(user.getId())
                        .email(user.getEmail())
                        .couponCode(originalCoupon.getCouponCode())
                        .timestamp(LocalDateTime.now())
                        .notificationMessage(String.format("%s님, [%s] 쿠폰이 발급되었습니다. \n%s까지 사용 가능합니다.",
                                user.getNickname(),
                                originalCoupon.getCouponName(),
                                originalCoupon.getExpiryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                        .build();
                couponEventProducer.sendCouponEvent(event);
                sendCouponEmailProducer.sendCouponEmail(event);
            });

            // 캐시 및 이벤트 갱신
            couponCacheService.clearAllUserCouponsCache();
            userCoupons.forEach(userCoupon ->
                    eventPublisher.publishEvent(new UserCouponsChangedEvent(this, userCoupon.getUserId())));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CouponLockException("대량 쿠폰 발급 처리 중 인터럽트가 발생했습니다.", e);
        } catch (Exception e) {
            log.error("대량 쿠폰 발급 중 오류가 발생했습니다. 쿠폰 ID: {}. 예외: {}", couponId, e.getMessage(), e);
            throw e;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Cacheable(value = USER_COUPONS_CACHE, key = "#userId", unless = "#result.isEmpty()")
    public List<CouponResponseDto> findAvailableCoupons(Long userId) {
        List<UserCoupon> activeUserCoupons = userCouponRepository.findByUserIdAndIsUsedFalse(userId);
        if (activeUserCoupons.isEmpty()) {
            return Collections.emptyList();
        }

        return activeUserCoupons.stream()
                .map(userCoupon -> {
                    Coupon coupon = couponCacheService.findCouponById(userCoupon.getCouponId());
                    return CouponResponseDto.from(coupon);
                })
                .toList();
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = USER_COUPONS_CACHE, key = "#userCouponId"),
    })
    public Long applyCoupon(Long userCouponId, Long originalPrice) {
        String lockKey = LOCK_PREFIX + "apply:" + userCouponId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new CouponLockException("쿠폰 사용 처리 중입니다. 잠시 후 다시 시도해주세요.");
            }

            UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                    .orElseThrow(() -> new CouponNotFoundException("사용자 쿠폰이 존재하지 않습니다"));

            Coupon coupon = couponCacheService.findCouponById(userCoupon.getCouponId());

            if (!coupon.isCouponActive() || userCoupon.getIsUsed()) {
                throw new CouponActiveException("사용 가능한 쿠폰이 아닙니다.");
            }

            Long discountedPrice = calculateDiscountedPrice(originalPrice, coupon);

            userCoupon.markAsUsed();
            userCouponRepository.save(userCoupon);

            couponCacheService.clearUserCouponsCache(userCoupon.getUserId());
            eventPublisher.publishEvent(new UserCouponsChangedEvent(this, userCoupon.getUserId()));

            return discountedPrice;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CouponLockException("쿠폰 사용 처리 중 인터럽트가 발생했습니다.", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private Long calculateDiscountedPrice(Long originalPrice, Coupon coupon) {
        Long discountedPrice = originalPrice;
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            discountedPrice -= (originalPrice * coupon.getDiscount() / 100);
        } else if (coupon.getDiscountType() == DiscountType.AMOUNT) {
            discountedPrice -= coupon.getDiscount();
        }
        return Math.max(discountedPrice, 0);
    }

    // 중복 발급 방지 메서드
    public boolean isCouponAlreadyIssued(Long userId, Long couponId) {
        return userCouponRepository.existsByUserIdAndCouponId(userId, couponId);
    }
}
