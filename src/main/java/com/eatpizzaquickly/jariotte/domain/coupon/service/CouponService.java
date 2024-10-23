package com.eatpizzaquickly.jariotte.domain.coupon.service;

import com.eatpizzaquickly.jariotte.domain.coupon.dto.CouponRequestDto;
import com.eatpizzaquickly.jariotte.domain.coupon.dto.CouponResponseDto;
import com.eatpizzaquickly.jariotte.domain.coupon.entity.Coupon;
import com.eatpizzaquickly.jariotte.domain.coupon.entity.DiscountType;
import com.eatpizzaquickly.jariotte.domain.coupon.entity.UserCoupon;
import com.eatpizzaquickly.jariotte.domain.coupon.exception.CouponActiveException;
import com.eatpizzaquickly.jariotte.domain.coupon.exception.CouponNotFoundException;
import com.eatpizzaquickly.jariotte.domain.coupon.exception.CouponOutOfStockException;
import com.eatpizzaquickly.jariotte.domain.coupon.exception.DuplicateCouponException;
import com.eatpizzaquickly.jariotte.domain.coupon.repository.CouponsRepository;
import com.eatpizzaquickly.jariotte.domain.coupon.repository.UserCouponRepository;
import com.eatpizzaquickly.jariotte.domain.user.entity.User;
import com.eatpizzaquickly.jariotte.domain.user.exception.UserNotFoundException;
import com.eatpizzaquickly.jariotte.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class CouponService {

    private final CouponsRepository couponsRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;

    //어드민이 쿠폰을 생성하는관리하는 로직
    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto) {
        Coupon coupon = Coupon.builder()
                .couponName(couponRequestDto.getCouponName())
                .couponCode(couponRequestDto.getCouponCode())
                .couponType(couponRequestDto.getCouponType())
                .discountType(couponRequestDto.getDiscountType())
                .discount(couponRequestDto.getDiscount())
                .price(couponRequestDto.getPrice())
                .quantity(couponRequestDto.getQuantity())
                .build();
        couponsRepository.save(coupon);
        return CouponResponseDto.from(coupon);
    }

    @Transactional
    public void issueCouponToUser(String email,String couponCode) {
        Coupon coupon = couponsRepository.findByCouponCode(couponCode)
                .orElseThrow(()->new CouponNotFoundException("쿠폰이 존재 하지 않습니다"));

        // 쿠폰 수량 체크
        if (coupon.getQuantity() <= 0) {
            throw new CouponOutOfStockException("쿠폰 수량이 모두 소진되었습니다.");
        }
        // 중복 발급 체크
        if (userCouponRepository.existsByCouponIdAndUserEmail(coupon.getId(), email)) {
            throw new DuplicateCouponException("이미 발급받은 쿠폰입니다.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("가입되지 않는 유저입니다."));
        UserCoupon userCoupon = UserCoupon.builder()
                .user(user)
                .coupon(coupon)
                .build();
        userCouponRepository.save(userCoupon);
        coupon.getUserCoupons().add(userCoupon);
        coupon.decreaseQuantity();
        couponsRepository.save(coupon);

    }

    @Transactional
    public void issueCouponToAllUsers(Long couponId) {
        Coupon coupon = couponsRepository.findById(couponId)
                .orElseThrow(()->new CouponNotFoundException("쿠폰이 존재 하지 않습니다"));

        List<User> allUsers = userRepository.findAll();
        allUsers.stream()
                .map(user -> UserCoupon.builder()
                        .user(user)
                        .coupon(coupon)
                        .build())
                .forEach(userCoupon ->  coupon.getUserCoupons().add(userCoupon));

        couponsRepository.save(coupon);
    }

    public List<CouponResponseDto> findAvailableCoupons(String email) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUserEmail(email);

        return userCoupons.stream()
                .filter(userCoupon -> userCoupon.getCoupon().getIsActive())
                .map(userCoupon -> CouponResponseDto.from(userCoupon.getCoupon()))
                .toList();
    }

    @Transactional
    public Long applyCoupon(Long couponId, Long originalPrice){
        Coupon coupon = couponsRepository.findById(couponId)
                .orElseThrow(()->new CouponNotFoundException("쿠폰이 존재 하지 않습니다."));
        if(!coupon.getIsActive()){
            throw new CouponActiveException("사용가능한 쿠폰이 아닙니다.");
        }
        Long discountedPrice = originalPrice;
        if(coupon.getDiscountType() == DiscountType.PERCENTAGE){
            discountedPrice -=(originalPrice * coupon.getDiscount() / 100);
        }
        else if(coupon.getDiscountType() == DiscountType.AMOUNT){
            discountedPrice -= coupon.getDiscount();
        }
        return  Math.max(discountedPrice, 0);
    }
}
