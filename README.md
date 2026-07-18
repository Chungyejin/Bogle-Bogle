# 보글보글 재료 마을 (Bogle-Bogle)

**사용자가 선택한 식재료를 기반으로 최적의 레시피를 매칭해주는 백엔드 서비스**
---
##  Tech Stack (기술 스택)

###  Front-end (Client)
*   **Framework:** Flutter 
    *   *역할: Android 및 iOS 통합 UI 구현, 사용자 재료 선택 인터페이스 제공 및 백엔드 API 연동*

###  Back-end (Server)
*   **Framework:** Spring Boot 
*   **Language:** Java 17
*   **Build Tool:** Gradle
    *   *역할: RESTful API 엔드포인트 제공, 재료 매칭 비즈니스 로직 처리 및 데이터 정제(DTO 변환)*

### Database & ORM
*   **Database:** MySQL 
*   **ORM:** Spring Data JPA 
    *   *역할: 레시피-재료 관계형 데이터 저장 및 관리, 다대다(N:M) 매핑 테이블 조인(JOIN) 연산 수행*

##  전체 시스템 상호작용 흐름 (System Architecture & Flow)

사용자가 앱에서 재료를 선택하고 결과를 보기까지 **Flutter(프론트엔드) ➡️ Spring Boot(백엔드) ➡️ MySQL(데이터베이스)**로 이어지는 전체 흐름은 다음과 같이 상호작용합니다.

```text
[  Flutter 앱 ] 
       │
       │ ① 재료 선택 후 API 요청 (GET /api/recipes/match?ingredientIds=1,2)
       ▼
[  Spring Boot (Java) ] 
       │
       │ ② JPA를 통해 효율적인 매칭 쿼리 실행 (JOIN & IN 절 활용)
       ▼
[ MySQL DB ] 
       │
       │ ③ 조건에 맞는 레시피 데이터 탐색 및 반환
       ▼
[  Spring Boot (Java) ] 
       │
       │ ④ 엔티티를 안전한 'RecipeResponse DTO'로 가공 및 JSON 변환
       ▼
[ Flutter 앱 ]
         ⑤ JSON 데이터를 받아 파싱 후, 사용자 화면에 예쁜 레시피 카드로 출력
