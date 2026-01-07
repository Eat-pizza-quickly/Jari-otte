<div align="center">
  
# Jari-Otte 티켓 예매 플랫폼
![로고 메이커 프로젝트](https://github.com/user-attachments/assets/1d90f74c-168a-4686-a5a7-553d2ea7c46c)<br>
### <Strong>[JARI-OTTE](https://www.jariotte.store/)<Strong>
</div>


## 목차

[1. 프로젝트 소개](#프로젝트-소개)

[2. 기술스택](#-기술-스택)

[3. 인프라 구성도](#-인프라-구성도)

[4. Flow Chart](#-FLOW-CHART)

[5. API](#-api)

[6. 주요 기술 및 특징](#-주요-기술-및-특징)

[7. 트러블 슈팅](#-트러블-슈팅)

[8. 성능 개선](#-성능-개선)

# 팀원 소개
<div align=center> 
<img src="https://github.com/user-attachments/assets/fc09bfec-299e-48ec-8a26-65dd89abdbfd">
</div>

<div align=center> 
  
| 주소       | 오강욱 (팀장)                 | 정이삭 (부팀장)                 | 김 현 (팀원)                  | 김우진 (팀원)                 |
|:------------:|:-----------------------------:|:-----------------------------:|:-----------------------------:|:-----------------------------:|
| 블로그 주소      | [블로그](https://velog.io/@kanguk_o/posts) | [블로그](https://velog.io/@isdev7057/posts) | [블로그](https://hyun-my-it-blog.tistory.com/) | [블로그](https://velog.io/@boom3652/posts) |
| GitHub 주소     | [GitHub](https://github.com/KangWookOh) | [GitHub](https://github.com/golden-hamster) | [GitHub](https://github.com/ican0422) | [GitHub](https://github.com/Woojin1123) |

</div>
<Br>

# 프로젝트 소개
Jari-Otte는 <Strong>마이크로서비스 아키텍처(MSA)</Strong>를 통해 특정 서비스의 장애가 발생하더라도

전체 서비스에는 영향을 미치지 않도록 설계된 안정적이고 확장 가능한 티켓 예매 플랫폼입니다.

이 플랫폼은 대규모 데이터를 효율적으로 처리하고, 사용자에게 빠르고 직관적인 검색 및 예매 경험을 제공하는 것을 목표로 합니다.

# ⚙ 기술 스택

<div align=center> 


  <br>
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> 
  <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"> 
  <br>



  <img src="https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">
  <img src="https://img.shields.io/badge/ELK-005571?style=for-the-badge&logo=elasticstack&logoColor=white">
  <img src="https://img.shields.io/badge/KAFKA-231F20?style=for-the-badge&logo=apachekafka&logoColor=white">
  <br>

  <img src="https://img.shields.io/badge/amazon s3-569A31?style=for-the-badge&logo=amazons3&logoColor=black"> 
  <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> 
  <img src="https://img.shields.io/badge/amazon ecs-FF9900?style=for-the-badge&logo=amazonecs&logoColor=white">
  <br>

  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <br>
</div>

# 💡 API
[API 문서 보기](https://documenter.getpostman.com/view/37572363/2sAYBSjDDo)

# 🎮 FlOW CHART
<h4>서비스 플로우</h4>

![image](https://github.com/user-attachments/assets/c46525e9-bc86-4c46-9d31-65c3d7c4cdb8)

<h4>배치 플로우</h4>

![image](https://github.com/user-attachments/assets/de1c6a2f-2c8c-494f-b55a-5103fbd4267c)

<h4>대기열 플로우</h4>

![대기열로직 drawio (2)](https://github.com/user-attachments/assets/6c278085-1464-4812-881f-f66e7f9c7a4f)



# 🏗 인프라 구성도
![인프라 구성도](https://github.com/user-attachments/assets/a90ce3a1-2b68-4c86-bdf6-3b7c2558fbf6)

# 🚀 주요 기술 및 특징
### 🍕 __마이크로서비스 아키텍처 (MSA)__
   - 서비스 간의 독립성을 확보하여 특정 서비스에 장애가 발생하더라도 나머지 서비스에 영향을 주지 않음.
   - 유연한 확장성과 유지 보수성 향상.

### 🍕 __Kafka 기반 대용량 데이터 처리__
   - Kafka를 활용해 실시간으로 대량의 데이터를 처리하고, 빠른 이벤트 드라이븐 환경을 제공.
   - 트래픽이 폭주하는 상황에서도 안정적인 데이터 처리.

### 🍕 __Elasticsearch 검색 엔진__
   - 다중 조건 검색 및 고속 데이터 처리를 통해 사용자에게 더 나은 검색 경험을 제공.
   - 방대한 데이터에서도 실시간으로 정확한 검색 결과를 반환.

### 🍕 __ELK Stack & Prometheus를 활용한 로그 관리 및 모니터링__
   - ELK Stack (Elasticsearch, Logstash, Kibana)을 통해 로그를 중앙에서 관리하며 고급 필터링과 로그 분석 기능 제공.
   - Prometheus를 활용하여 시스템 상태를 실시간으로 모니터링하고 장애를 빠르게 탐지 및 대응.
### 🍕 Spring Batch 활용 쿠폰만료 삭제 및 결제금액 정산

- 대량의 쿠폰 데이터를 효율적으로 처리하여 만료 기한을 일괄 정산
- 조건에 맞는 결제 데이터에 대한 정산 처리
- 안정적인 배치 작업으로 데이터 정확성과 성능 확보

# 👩‍💻 트러블 슈팅
[트러블 슈팅 & 기술 선택 문서](https://abalone-kicker-cfb.notion.site/bb89be9bc05b4618b46725fb2addce71?pvs=4)

<details> 
   <summary><font size=5>💥 검색 결과 정확도 문제</font></summary>
  
  ### 📌 요약
  - 검색 쿼리의 정확도를 높이기 위해 Elasticsearch에서 띄어쓰기 문제, 복합어 처리, 최소 Score 설정 등의 문제를 해결
  - 사용자가 의도한 검색어에 대해 정확한 결과를 반환하도록 최적화
  
  ### 📌배경
  - 사용자가 검색어를 입력할 때 띄어쓰기 없이 작성하거나 복합어 형태로 작성하는 경우가 많음
  - Elasticsearch는 기본 설정에서는 이러한 입력을 제대로 처리하지 못하므로, 결과 정확도와 성능을 개선할 필요가 있었음

  ### 🚨문제점 
  **검색 결과가 사용자의 의도와 맞지 않음**
  - 띄어쓰기가 없는 경우("여름공연" vs "여름 공연") Elasticsearch가 정확한 결과를 반환하지 못함.
  - 복합어("여름공연")와 분리된 단어("여름", "공연")를 모두 처리하지 못함.
    
  ![띄어쓰기 없는 경우 미 반환](https://github.com/user-attachments/assets/3c15641c-7400-4710-870a-a4e0becfdbc2)
    
  **검색 쿼리의 스코어링 문제**
  - Score 값이 낮거나 0으로 설정된 데이터가 검색 결과에 포함되거나 제외됨.
    
  ![스코어링 문제](https://github.com/user-attachments/assets/39cb710a-2073-4fa3-ad00-52b228001270)
    
  ### 🔧성능 개선 
  **최소 Score 설정**
  - minScore를 0.3으로 설정하여 낮은 점수의 결과를 제거.
  - 사용자 의도에 가까운 검색 결과만 반환.
    
  ![minScore 적용](https://github.com/user-attachments/assets/d62bdbf9-4e9f-4993-a141-c77e296ce2d3)

    
  **복합어와 띄어쓰기 처리**
  - Nori Tokenizer를 decompound_mode: mixed로 설정하여 복합어("여름공연")를 분리된 형태("여름", "공연")로 처리.
  - 띄어쓰기 없는 단어도 분석하여 검색이 가능하도록 설정.
    
  ![스크린샷 2024-11-22 002610](https://github.com/user-attachments/assets/73e23923-0a15-40d5-9eae-cf6c8d128adc)
  
  ![스크린샷 2024-11-21 222718](https://github.com/user-attachments/assets/0232db78-a27f-4156-9ba6-1962c89d5606)
  
  ![스크린샷 2024-11-21 222858](https://github.com/user-attachments/assets/31a983b0-e3fd-456e-b6c0-1e75914af6ce)
  
  ![Nori Tokenizer](https://github.com/user-attachments/assets/6e058fd4-0e40-447d-a955-eda325ce010f)

  ### 🔧결과
  **스코어링 도입 결과**
  ![스크린샷 2024-11-18 104432](https://github.com/user-attachments/assets/36ecf6a1-750a-4312-8843-5d9bac3ff5c3)

  **띄어쓰기 없이 검색한 결과**
  ![스크린샷 2024-11-20 065812](https://github.com/user-attachments/assets/174a50a1-5925-4662-9a76-a1f195eb327a)

</details>
<details> <summary><font size=5>💥 Batch Read & Delete </font></summary>
  
### 📌 요약
Spring Batch 실행 중 데이터 손실 문제 발생. 특정 Step에서 Read Data가 절반으로 감소하여 원인 분석 후 해결 방안을 적용함.


### 📌 배경
Spring Batch에서 Read & Delete 방식으로 데이터를 처리하는 동안 결과 값이 일치하지 않는 문제가 발생했습니다.

배치 메타테이블을 확인한 결과, 특정 Step에서 Read Data가 절반으로 감소된 것을 확인했습니다.

배치 실패를 의심하고 재시도했으나, 동일한 문제가 발생했습니다. 결과 상태는 Completed였기에 추가적으로 코드와 실행 방식을 검토했습니다.

문제는 특정 Step에서 Chunk 단위로 데이터를 Read & Write 후 삭제하는 과정에서 발생했습니다.

Spring Batch의 ItemPagingReader는 데이터를 페이지 단위로 읽어오는데, 아래와 같은 흐름이 문제의 원인이었습니다:

첫 번째로 읽어온 데이터를 처리하고 삭제한 후, 다음 Chunk로 넘어감.
이 과정에서 Page 0번 데이터가 갱신되어, ItemReader는 다음 페이지인 201~300 데이터를 읽어옴.
결과적으로 배치 실행 중 전체 데이터의 절반만 처리되고, 나머지는 누락됨.

### 🚨 문제점
Spring Batch에서 ItemPagingReader 사용 시:

데이터 삭제 후 다시 페이지를 읽을 경우, Page 0번 데이터가 변경됨.
데이터의 누락 문제로 인해 Read Data와 Write Data가 불일치.

### 🔧 성능 개선
이 문제를 해결하기 위해 다음과 같은 방법을 검토했습니다:

1️⃣ ItemReader를 항상 0번째 페이지로 고정하는 방식 적용

데이터 삭제 후에도 항상 0번째 페이지를 기준으로 데이터를 읽음.
수정된 데이터의 영향을 받지 않음.
단점: 페이징 처리가 고정되므로 성능이 저하될 가능성이 있음.

2️⃣ CursorReader 사용

DB 커넥션을 유지하며 처음 조회한 결과를 고정하고 데이터를 순차적으로 처리.
수정된 데이터의 영향을 받지 않는다는 장점이 있음.
단점: DB Connection Timeout 문제가 발생할 수 있음.
현재 해결 방법:
CursorReader는 Coupon 서비스에 API 요청을 보내 데이터를 받아오는 구조에서는 적합하지 않다고 판단했습니다.
따라서, 항상 0번째 페이지를 읽도록 ItemReader를 수정하여 문제를 해결했습니다.

### ✅ 추가 고려 사항(선택)
데이터 볼륨이 증가할 경우 CursorReader 적용 방안 검토 필요.
DB Connection Timeout 문제를 방지하기 위해 Connection Pool 또는 Batch 처리 시간을 줄이는 방식의 추가 최적화 고려.
데이터 처리 로직과 Batch Step 간의 의존성을 줄여 더 안정적인 실행 구조 마련.
</details>

<details> <summary><font size=5>💥 대기열 기능 자원 낭비 </font></summary>

### 📌 요약
공연별 대기열 처리를 위해 SingleThreadScheduledExecutor를 사용하던 기존 방식에서, 요청마다 새로운 스레드풀이 생성되어 자원 낭비 문제가 발생함. 이를 해결하기 위해 공용 스레드 풀을 활용한 방식으로 전환하여 효율성을 개선함.

### 📌 배경
대기열 진행 처리를 구현할 때, 각 공연 ID마다 독립적인 대기열 처리를 보장하기 위해 SingleThreadScheduledExecutor를 사용했습니다.
이를 통해 공연별로 별도의 스레드를 생성하여 대기열 처리를 실행하도록 설계했습니다.

운영 중 로그 분석 결과, 공연 요청마다 계속 새로운 스레드가 생성되고 있었음을 확인했습니다.
이로 인해 컨텍스트 스위칭 비용 증가와 같은 자원낭비가 발생했습니다:

### 🚨 문제점
대기열이 생길때마다 SingleThreadScheduledExecutor 를 사용하고 있었기에
공연마다 개별적인 스레드 생성으로 리소스 낭비

### 🔧 성능 개선
Executors.newScheduledThreadPool() 메서드를 활용하여 미리 정의된 스레드 수를 가진 공용 스레드 풀을 생성하여
사용하도록 하였습니다.
* 요청마다 새로운 스레드가 생성되지 않음.
* 미리 생성된 스레드를 재사용하여 자원 낭비 감소.
</details>

<details> <summary><font size=5>💥 카프카 에러 핸들링 </font></summary>

### 📌 요약
Kafka 이벤트 발행 및 소비 과정에서 발생하는 장애를 CompletableFuture와 DefaultErrorHandler를 활용하여 안정적으로 처리했습니다. 장애 발생 시 Slack 알림을 통해 신속히 대응하며, 데이터 정합성을 유지하는 신뢰성 높은 시스템을 구축했습니다.

![image](https://github.com/user-attachments/assets/613ae270-5dee-41d9-a12e-7deb624876a7)


### 📌 배경
Kafka를 활용해 이벤트를 발행 및 소비하는 시스템을 운영 중, 장애가 발생했을 때:

이벤트 발행 실패 또는 소비 실패로 인해 데이터 정합성 문제가 발생.
장애 원인을 발견하고 대응하는 데 시간이 소요되는 문제가 있었습니다.
운영 환경에서는 장애 상황에서도:

빠르게 문제를 인지하여 대응해야 하며,
데이터의 정합성을 유지하는 것이 필수적이었습니다.

### 🚨 문제점
Kafka 이벤트 발행/소비 실패 시:
장애가 발생했을 때 로그만 남고, 실시간 알림이 없어 대응이 지연됨.
소비 실패로 인해 메시지가 누락되거나 데이터 정합성이 깨질 위험이 있음.
장애 처리 코드의 복잡성으로 인해 시스템의 신뢰성이 저하됨.

### 🔧 성능 개선
문제를 해결하기 위해 다음과 같은 방식을 적용했습니다:

1. CompletableFuture를 활용한 비동기 처리
    * 이벤트 발행 및 소비 과정을 CompletableFuture로 처리.
    * 성공 및 실패에 따라 후속 작업을 분기 처리하여 안정적인 흐름 보장.
    
2. DefaultErrorHandler를 활용한 장애 관리
    * Kafka 소비 실패 시 DefaultErrorHandler를 설정하여, 재시도 및 복구 로직을 구현.
    * 장애 발생 시 Slack 알림을 통해 실시간 대응 체계 마련.

3. 데이터 정합성 유지
    * 이벤트 발행 실패 시 재시도를 포함한 복구 로직 적용.
    * 소비 실패 메시지를 DB에 저장하여 데이터 누락 방지.
</details>

<details> <summary><font size=5>💥 좌석 예매 및 쿠폰 발급 시스템 동시성 제어</font></summary>

  
  ### 📌 요약
공연 티켓팅 서비스의 좌석 예매 및 쿠폰 발급 과정에서 발생할 수 있는 동시성 문제를 해결하기 위해 Redis Lua script를 활용했습니다.
Redis Sentinel 환경에서 3개 노드 구성을 기반으로 원자성을 보장하며, 기존 방식 대비 약 80% 이상의 성능 향상을 달성했습니다.

### 📌 배경

공연 티켓팅 서비스와 쿠폰 발급 시스템은 대규모 트래픽이 몰릴 것으로 예상되었으며, 다음과 같은 문제가 발생했습니다:

**좌석 예매 동시성 문제**:
- 여러 사용자가 동일한 좌석을 동시에 예매하려고 시도할 때, 하나의 요청만 성공하고 나머지 요청에는 예외 처리가 필요.

**쿠폰 발급 성능 저하**:
- 기존 방식으로 처리 속도가 느려 대규모 트래픽 환경에서 병목 현상이 발생.

**운영 환경**:
- Redis Sentinel 클러스터(3개 노드)를 기반으로 분산 환경에서 동시성 제어 필요.
- 트래픽 증가에 따른 시스템 성능 최적화 요구.


### 🚨 문제점

**Redis 분산락 방식**:
  - 락 해제와 트랜잭션 범위 간 불일치로 인해 동시성 문제가 발생 가능.
  - 락 획득 및 해제 과정에서 성능 저하 발생.

1. 좌석 예매 시 동시성 문제
  **DB 락 방식**:
    - 동시성은 보장되지만, 높은 트래픽 시 DB 부하 증가 및 데드락 가능성.

2. 쿠폰 발급 성능 저하
  **Redisson 분산락 방식**:
    - 백분위 응답 시간이 32초로, 트래픽이 높아질수록 성능 병목.


### 🔧 성능 개선
**1️⃣ Redis Lua script로 좌석 예매 동시성 문제 해결**
- Lua script를 통해 좌석 상태 조회와 변경을 원자적으로 처리:
  - Redis 싱글 스레드 특성과 Lua script를 결합해 동시성 제어 보장.
  - 락을 사용하지 않아 락 해제 시간 지연 없이 빠른 응답 가능.

- Redis Set 자료형 활용:
  - 좌석 ID만 저장하여, O(1) 시간복잡도로 조회와 삭제를 처리.

- Redis Sentinel 환경:
  - 3개 노드 구성을 통해 고가용성과 장애 복구 대비.


**2️⃣ Lua script로 쿠폰 발급 성능 개선**
- Redisson 분산락에서 Lua script로 전환:
  - 백분위 응답시간: 7.3초.
  - 평균 응답시간: 406.5ms.
  - 기존 대비 80% 이상 성능 향상.

- Redis Sentinel 환경에서의 분산 처리:
  - 트래픽 증가에도 안정적인 쿠폰 발급 속도 유지.

</details>

# 📉 성능 개선


<details> 
   <summary><font size=5>🍕 [MSA] Monolith -> MSA 이관</font></summary>
  
  ### 📌 요약
### [Before]
![image](https://github.com/user-attachments/assets/9b6a1702-95c1-4a9e-bab1-7b3bc736c6c6)
### [AFTER]
![image](https://github.com/user-attachments/assets/1f182364-bcf0-4e97-a53d-740b3cb97629)

  ### 🚨문제점

  ### ☀️해결 방안 

  ### 🔧성능 개선 
</details>

<details> 
    <summary><font size=5>🍕 [Redis] 좌석 예매 동시성 제어</font></summary>

### 📌 요약
### 1. Before - 분산 락
  
<img src="https://github.com/user-attachments/assets/20f3773b-0af9-4f76-85f5-8fea0c3837a7" >

### 2. After - Lua Script
  
<img src = "https://github.com/user-attachments/assets/73b75348-1977-4646-a84b-bedf6d5832dc" >

### 성능비교
 
  <img src = https://github.com/user-attachments/assets/38ad2cc8-b2f7-4ab6-82d9-1371a1b9ff6f  height="300">
  
### 응답 속도 평균 40~50% 향상   /  처리량 약 55% 증가
  
### 🚨문제점

대용량 트래픽이 몰릴 것으로 예상되는 공연 티켓팅 서비스 프로젝트의 좌석 예매를 구현하는 중에 **동시성 문제**를 신경 써야 했습니다.<br>
거의 동시에 여러 사람이 같은 좌석을 예매할 때 **한 사람만 성공**하고 나머지 요청에는 **예외를 반환**해야 합니다.<br>

### ☀️해결 방안

### DB 락

DB 자체의 락을 활용해서 동시성을 제어하는 방식입니다. 구현이 간단하고 일관성을 보장합니다. 하지만 대규모 트래픽이 발생하면 **수평적으로 확장하기 힘든 DB에 큰 부하**를 주게 됩니다. 그리고 다중 트랜잭션에서 서로가 서로의 락을 기다리는 **데드락**이 발생할 수 있습니다.

### Redis 분산락

Redis에 저장된 특정 키를 사용하여 락을 구현하는 방식입니다. 여러 인스턴스의 서비스가 동시에 특정 좌석을 예매할 때, **Redis 분산락**을 이용해 여러 인스턴스에서 동시에 같은 좌석을 예매하지 못하게 합니다.

Redis를 사용하기 때문에 여러 서버에서 동시에 동작하는 분산 환경에서 유용합니다. 하지만 **락을 얻고 해제하는 과정에서 시간이 소요되기 때문에 Lua script를 사용하는 것에 비해 느릴 수 있습니다.**

그리고 **락의 범위와 트랜잭션의 범위를 잘 조율해야 합니다.** 락의 해제 시점이 트랜잭션의 커밋 시점보다 빠를 경우 동시성 문제가 발생할 수 있습니다. 그렇다고 락의 범위를 너무 크게 하면 락 해제까지 걸리는 시간도 길어지기에 조심해야 합니다.

### 락 없이 Redis Lua script로 원자적 처리

만약 **Lua script를 사용하지 않고 그냥 Redis 각각의 명령만 락 없이 사용한다면** 동시성 문제가 발생할 수 있습니다.

예를 들어서:

1. **Redis에서 좌석 상태를 조회**
2. **Redis에서 좌석 상태를 변경**

위와 같은 작업을 할 때 Redis는 싱글 스레드로 동작하지만 이처럼 명령이 여러 개로 분리되는 경우 **동시성 문제가 발생**할 수 있습니다.

하지만 **Lua script는 싱글 스레드로 동작하는 Redis 서버 내에서 한 번에 묶여서 실행되기 때문에 원자성을 보장합니다.**

좌석의 상태를 조회하고 상태를 변경하는 행위를 스크립트로 묶어서 원자적으로 처리하기 때문에 동시성 제어를 할 수 있게 된 것입니다.

- **Redis에서 한 번에 좌석 상태 조회 & 좌석 상태 변경**

그리고 **락을 사용하지 않기 때문에 동시간대에 처리할 수 있는 트래픽의 수도 늘어납니다.** <br>




### 🔧성능 개선

고민끝에 저는 좌석 예매 기능에 **락 없이 Redis Lua script를 사용하기로 결정했습니다.**

- **원자성 보장**
- **빠른 속도**

그리고 자료형은 Redis에 `예매 가능한 좌석 id`만 들어가기 때문에 조회와 삭제의 시간복잡도가 O(1)로 매우 빠른 **`Set` 자료형**을 사용하기로 했습니다.

  </details>
      <div>
</div>

<details>
<summary><font size=5>🍕 [Redis] 공연 단건 조회</font></summary>

### 📌 요약

공연정보와 잔여 좌석수를 함께 조회하지 않고 공연 정보는 캐시, 잔여 좌석 수는 Redis 에서 실시간으로 조회하게 해서 81.43%의 성능 향상을 달성했습니다.

### 🚨문제점

공연 정보와 잔여 좌석 수를 함께 조회할 때는 자주 변하는 값이면서 정확해야 하는 잔여 좌석 수 때문에 캐싱이 비효율적이었습니다.

### ☀️해결 방안

공연 정보의 조회와 잔여 좌석수의 조회를 별도로 분리했습니다.

### 🔧성능 개선

### [BEFORE] - 공연정보와 잔여 좌석 수를 함께 조회
![image (4)](https://github.com/user-attachments/assets/070de891-e0f8-4705-8f72-b6b3cee639ae)


### [AFTER] - 공연정보와 잔여 좌석 수를 별도 조회
![image (5)](https://github.com/user-attachments/assets/78096c22-eda2-4b10-837b-0faf43c395aa)


### 성능 향상: 81.43%
![output (2) (1)](https://github.com/user-attachments/assets/b5022402-eaf6-4c11-8736-3dfbf1230d59)



</details>

<details> 
    <summary><font size=5>🍕 [Spring Batch] No-Offset Reader 사용</font></summary>
<div>
  
### 📌 요약

- ItemReader를 No-Offset ItemReader로 변경
- ItemProcessor에서 발생하는 과도한 Api 통신 해결
- #### 수행시간 1시간 12분 → 14분 5배 감소
  
- #### 성능 개선율 약 80%
<img src="https://github.com/user-attachments/assets/fd4d1b50-ff50-48e4-b698-2e6e5c14c021" width="500">
<img src = "https://github.com/user-attachments/assets/0273fd56-0e5e-40d1-a8c8-83cf26c040b2" width="500">

  
### 🚨문제점

- <strong>27만건의 결제 데이터에 대해 CHUNK_SIZE 100 으로 수행</strong>
- <strong>27만건의 상대적으로 적은 데이터임에도 1시간 12분으로 오래걸림</strong>

### ☀️해결 방안

1. **ItemReader의 Offset 조회 방식**
  - Offset 기반 쿼리는 **OFFSET만큼의 데이터를 읽고 무시**한 후 결과를 반환.
  - 데이터가 많아질수록 **불필요한 읽기 작업**이 증가해 성능 저하.
   
-> **Offset을 사용하지 않는 No-Offset Reader로 교체**

2. **ItemWriter의 JPA saveAll**
  - **Chunk_size**만큼 반복적으로 **INSERT** 쿼리를 실행.
  - 개별 INSERT 쿼리가 많아 대량 쓰기 작업에서 비효율적.

-> **Jdbc batch Insert 사용**
**3 .  Processor의 Feign통신**

- ItemReader의 경우 read()메서드를 통해 데이터를 한건씩 반환.
- Processor에서 네트워크 통신을 진행할 경우 모든 데이터에 대해 네트워크 통신으로 인해 응답시간*데이터 개수 만큼의 처리시간 발생

-> **Api통신을 Processor가 아닌 Writer에서 수행**
### 🔧성능 개선

1. **Offset 대신 ID 기반 조회**
  - Offset 조회 대신, Primary Key (ID)를 기준으로 조건 조회.
  - 이전 페이지의 **최대 ID**를 기억하고 `WHERE id > ? LIMIT ?` 형태로 데이터를 읽음.
  - **불필요한 읽기 제거**로 조회 성능 대폭 개선.

    ```java
    //Redis에 이전에 조회한 ID 저장
    redisTemplate.opsForValue().set(OFFSET_KEY, "0", 60000, TimeUnit.MILLISECONDS);
    // QueryDsl Id를 기준으로 조회하도록 수
     List<PaymentResponseDto> results = queryFactory
                    .select(Projections.constructor(
                            PaymentResponseDto.class,
                            payment.id,
                            payment.settlementStatus,
                            payment.payStatus,
                            payment.amount,
                            payment.reservation.concertId))
                    .from(payment)
                    .where(
                            payment.settlementStatus.eq(settlementStatus),
                            payment.payStatus.eq(payStatus),
                            payment.paidAt.before(before),
                            payment.id.gt(currentOffset))
                    .orderBy(payment.id.asc())
                    .limit(chunk)
                    .fetch();
    ```

2. **JdbcTemplate로 대량 INSERT 처리**
  - JPA 대신 **JdbcTemplate**를 활용하여 **Batch Insert** 구현.
  - 하나의 쿼리로 여러 Row를 처리해 데이터 쓰기 성능 최적화.

    ```java
    
    jdbcTemplate.batchUpdate(
        "INSERT INTO table_name (col1, col2) VALUES (?, ?)",
        new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, dataList.get(i).getCol1());
                ps.setString(2, dataList.get(i).getCol2());
            }
    
            @Override
            public int getBatchSize() {
                return dataList.size();
            }
        });
    ```

3. **Processor → Writer로 이관**
  - Writer에서 아래 코드를 통해 concertId에 대한 hostId를 가져오도록 변경

    ```java
    //Writer
    ResponseEntity<ConcertHostResponseDto> concertResponse = concertClient.findHostIdsByConcertIds(requestDto);
    log.info("콘서트 feign 응답코드 : {}", concertResponse.getStatusCode());
    Map<String, Long> hostIds = concertResponse.getBody().getResult();
    ```
</div>
</details>


<details> 
   <summary><font size=5>🍕 [Elastic Search] 검색 속도</font></summary>
  
  ![약 89.84% 개선)](https://github.com/user-attachments/assets/81e5bc2a-69a5-46bc-8e99-575ce7bddd48)

  ### 📌 요약
  쿼리 최적화를 통해서 검색 속도 기능 약 89.84% 개선
  
### 🚨문제점
- **WildcardQuery로 인한 성능 저하**
  - WildcardQuery는 전체 문서 스캔(Full Table Scan)을 유발하여 검색 속도가 매우 느려짐.
  - 특히, query 형태의 쿼리는 대량 데이터에서 병목현상을 발생시킴.
- **WildcardQuery로 인한 성능 저하**
  - 필터 조건이 should 조건 아래 위치하여 후처리 단계에서 필터가 실행됨.
  - 결과적으로 불필요한 문서들이 검색되고, 리소스 낭비가 심각하게 발생.

### ☀️해결 방안 
  
**WildcardQuery 제거**
  - WildcardQuery를 삭제하고, 효율적인 Nori 분석기를 사용하여 띄어쓰기와 복합어 처리가 가능하도록 개선.
  - WildcardQuery 없이도 높은 검색 정확도를 유지하도록 새로운 쿼리 방식 적용.
**필터 선처리로 위치 변경**
  - 필터 조건을 should가 아닌 filter의 상단으로 이동.
  - 필터링을 검색의 선처리 단계에서 수행하여 불필요한 문서를 미리 제거.<br>

### 🔧성능 개선 

**사용자 경험**
  - 빠른 응답 속도로 검색 서비스 품질이 향상되어 사용자 만족도 증가.

**응답 속도**
  - 기존: 검색에 433ms 소요.
  ![개선 전](https://github.com/user-attachments/assets/cc775507-a073-445d-b147-02a99258be8f)
  - 개선 후: 검색 속도 44ms로 약 89.84% 개선
  ![개선 후](https://github.com/user-attachments/assets/2e494b5a-d3d1-4ec4-a5f7-7b5b85ab7cdc)

**변경 전 쿼리 코드**
```java
public static BoolQuery createConcertSearchQuery(String query, LocalDate startDate, LocalDate endDate) {
        // 다중 필드 검색, 오타 허용
        // title과 artists 필드에서 검색어가 포함된 문서를 찾고, 오타도 허용합니다.
        MultiMatchQuery matchQueryWithFuzziness = MultiMatchQuery.of(m -> m
                .query(query)                                  // 사용자가 입력한 검색어
                .fields("title^2", "artists")   // 검색할 필드 목록 (title과 artists), title에 가중치 2배 부여
                .fuzziness("AUTO")                      // 오타를 허용하여 유사한 검색어도 매칭
                .operator(Operator.Or)                        // 모든 검색어를 포함할 필요 없이 하나만 포함해도 매칭
        );

        // phrase_prefix
        // 검색어가 입력된 단어의 앞부분만 맞아도 매칭되도록 설정
        // 검색어의 접두사에 맞는 문서도 검색
        MultiMatchQuery matchQueryWithPhrasePrefix = MultiMatchQuery.of(m -> m
                .query(query)                                   // 사용자가 입력한 검색어
                .fields("title^2", "artists")    // 검색할 필드 목록 (title과 artists), title에 가중치 2배 부여
                .type(TextQueryType.PhrasePrefix)               // phrase_prefix 타입으로 설정하여 접두사 일치 허용
        );

        // Wildcard Query 추가: artists 필드에 대해 중간에 포함된 텍스트도 매칭
        WildcardQuery wildcardQuery = WildcardQuery.of(w -> w
                .field("artists")
                .value("*" + query + "*")  // 검색어가 포함된 부분 일치 허용
        );

        // BoolQuery에 추가할 필터 리스트 생성
        List<Query> filters = new ArrayList<>();

        // 날짜 필터 추가 (startDate와 endDate가 모두 존재할 경우에만 필터 적용)
        if (startDate != null && endDate != null) {
            // 시작일과 종료일을 UTC 시간의 ISO-8601 문자열로 변환
            String startDateTime = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toString();
            String endDateTime = endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toString();

            // DateRangeQuery 생성
            DateRangeQuery dateRangeQuery = new DateRangeQuery.Builder()
                    .field("startDate")        // 필드 지정
                    .gte(startDateTime)              // 시작 시간
                    .lte(endDateTime)                // 종료 시간
                    .build();

            // RangeQuery 생성 및 DateRangeQuery 추가
            RangeQuery rangeQuery = new RangeQuery.Builder()
                    .date(dateRangeQuery)      // DateRangeQuery를 RangeQuery에 추가
                    .build();

            // Query.Builder를 사용하여 RangeQuery 추가
            Query rangeQueryWrapper = new Query.Builder()
                    .range(rangeQuery)
                    .build();

            // 필터 리스트에 추가
            filters.add(rangeQueryWrapper);
        }
        // 삭제 여부 필터 추가 (deleted가 false인 문서만 반환)
        TermQuery deletedFilter = TermQuery.of(t -> t
                .field("deleted")
                .value(false) // deleted가 false인 문서만 포함
        );
        filters.add(new Query.Builder().term(deletedFilter).build());

        return BoolQuery.of(b -> b
                .should(Query.of(q -> q.multiMatch(matchQueryWithFuzziness)))        // 첫 번째 쿼리: 오타 허용
                .should(Query.of(q -> q.multiMatch(matchQueryWithPhrasePrefix)))    // 두 번째 쿼리: 접두사 일치
                .should(Query.of(q -> q.wildcard(wildcardQuery)))                   // Wildcard 쿼리 추가
                .filter(filters)                                                    // 필터 조건 추가 (필터가 있을 경우에만 적용)
        );
    }
```

**변경 후 쿼리 코드**
```java
public static BoolQuery createConcertSearchQuery(String query, LocalDate startDate, LocalDate endDate) {
        // 다중 필드 검색, 오타 허용
        // title과 artists 필드에서 검색어가 포함된 문서를 찾고, 오타도 허용합니다.
        MultiMatchQuery matchQueryWithFuzziness = MultiMatchQuery.of(m -> m
                .query(query)                                  // 사용자가 입력한 검색어
                .fields("title^2", "artists")   // 검색할 필드 목록 (title과 artists), title에 가중치 2배 부여
                .fuzziness("AUTO")                      // 오타를 허용하여 유사한 검색어도 매칭
                .operator(Operator.Or)                        // 모든 검색어를 포함할 필요 없이 하나만 포함해도 매칭
        );

        // phrase_prefix
        // 검색어가 입력된 단어의 앞부분만 맞아도 매칭되도록 설정
        // 검색어의 접두사에 맞는 문서도 검색
        MultiMatchQuery matchQueryWithPhrasePrefix = MultiMatchQuery.of(m -> m
                .query(query)                                   // 사용자가 입력한 검색어
                .fields("title^2", "artists")    // 검색할 필드 목록 (title과 artists), title에 가중치 2배 부여
                .type(TextQueryType.PhrasePrefix)               // phrase_prefix 타입으로 설정하여 접두사 일치 허용
        );

        // BoolQuery에 추가할 필터 리스트 생성
        List<Query> filters = new ArrayList<>();

        // 날짜 필터 추가 (startDate와 endDate가 모두 존재할 경우에만 필터 적용)
        if (startDate != null && endDate != null) {
            // 시작일과 종료일을 UTC 시간의 ISO-8601 문자열로 변환
            String startDateTime = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toString();
            String endDateTime = endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toString();

            // DateRangeQuery 생성
            DateRangeQuery dateRangeQuery = new DateRangeQuery.Builder()
                    .field("startDate")        // 필드 지정
                    .gte(startDateTime)              // 시작 시간
                    .lte(endDateTime)                // 종료 시간
                    .build();

            // RangeQuery 생성 및 DateRangeQuery 추가
            RangeQuery rangeQuery = new RangeQuery.Builder()
                    .date(dateRangeQuery)      // DateRangeQuery를 RangeQuery에 추가
                    .build();

            // Query.Builder를 사용하여 RangeQuery 추가
            Query rangeQueryWrapper = new Query.Builder()
                    .range(rangeQuery)
                    .build();

            // 필터 리스트에 추가
            filters.add(rangeQueryWrapper);
        }
        // 삭제 여부 필터 추가 (deleted가 false인 문서만 반환)
        TermQuery deletedFilter = TermQuery.of(t -> t
                .field("deleted")
                .value(false) // deleted가 false인 문서만 포함
        );
        filters.add(new Query.Builder().term(deletedFilter).build());

        return BoolQuery.of(b -> b
                .filter(filters)                                                    // 필터 조건 추가 (필터가 있을 경우에만 적용)
                .should(Query.of(q -> q.multiMatch(matchQueryWithFuzziness)))        // 첫 번째 쿼리: 오타 허용
                .should(Query.of(q -> q.multiMatch(matchQueryWithPhrasePrefix)))    // 두 번째 쿼리: 접두사 일치
        );
    }
```

**성능 개선 결과**

| **항목**               | **기존 성능** | **개선 후 성능** | **개선 효과**            |
|------------------------|---------------|------------------|--------------------------|
| **응답 속도**          | 433ms         | 44ms             | 약 **90% 개선**          |
| **리소스 소비**        | 높음          | 감소             | 불필요한 리소스 제거      |
| **검색 정확도**        | 낮음          | 높음             | Wildcard 없이도 검색 가능 |
| **사용자 요청 처리량** | 제한적        | 증가             | 동시 요청 처리 가능       |


### 추가 고려 사항 ✅

- **인덱스 무중단 배포**: 새로운 매핑이나 설정 변경 시, 별도의 임시 인덱스를 생성하고 데이터 리인덱싱을 수행하여 서비스 중단 없이 배포를 완료.
- **데이터 인덱스 최적화**: 데이터 크기와 요청 패턴에 맞는 Shard와 Replica 수를 설정하고, 불필요한 필드를 최소화하여 저장 공간과 검색 속도 최적화.
- **클러스터 구성 관리**: 노드 역할 분리 및 확장(예: 데이터 노드, 마스터 노드 분리)로 안정성과 성능 향상.
- **쿼리 복잡도 관리**: 불필요한 쿼리 제거 및 필터와 조건의 적절한 사용으로 검색 성능 최적화.
- **모니터링과 로그 관리**: Kibana와 Slow Log를 활용하여 검색 요청 병목 지점 파악 및 성능 문제 사전 대응.

</details>


