# 보글보글 재료 마을 (Bogle-Bogle)

**사용자가 선택한 식재료를 기반으로 최적의 레시피를 매칭해주는 백엔드 서비스**
---
##  Tech Stack (기술 스택)

###  Front-end (Client)
*   **Framework:**Dart
    *   *역할: Android 및 iOS 통합 UI 구현, 사용자 재료 선택 인터페이스 제공 및 백엔드 API 연동*

###  Back-end (Server)
*   **Framework:** Spring Boot 
*   **Language:** Java 17
*   **Build Tool:** Gradle
    *   *역할: RESTful API 엔드포인트 제공, 재료 매칭 비즈니스 로직 처리 및 데이터 정제(DTO 변환)*
 
      
### ① Controller (컨트롤러)
   프론트엔드가 보낸 HTTP 요청을 가장 먼저 받는다
### ② Service (서비스)
   앱의 핵심 규칙과 알고리즘이 작동하는곳이다. -> Controller에게서 받은 재료 리스트를 가지고 만들수있는 조합을 찾아낸다. 
### ③ DTO (Data Transfer Object)
   DB에서 꺼낸 원본 데이터(Domain)에는  민감한 정보나 너무 많은 정보가 들어있을 수 있기때문에 프론트엔드 화면에 딱 필요한 정보만 정돈해서 json형식으로 전달하기 위해 만드는 데이터 전산 틀이다.
### ④ Entity & Repository (엔티티 & 리포지토리)
   인터베이스의 테이블 구조를 자바 객체로 1:1 매핑한 클래스
   Domain(Entity) 객체를 이용해 데이터베이스에 접근하여 데이터를 저장, 수정, 삭제, 조회

### Database & ORM
*   **Database:** MySQL 
*   **ORM:** Spring Data JPA 
    *   *역할: 레시피-재료 관계형 데이터 저장 및 관리, 다대다(N:M) 매핑 테이블 조인(JOIN) 연산 수행*


## 앱 실행하는 법
1. java를 실행한다
2. terminal로 가서 app 파일에 cd해서 들어간다
3. flutter run -d chrome
4. 
##  전체 시스템 상호작용 흐름 (System Architecture & Flow)

사용자가 앱에서 재료를 선택하고 결과를 보기까지의 전체 흐름은 다음과 같이 상호작용합니다.

```text
📱 [1. Flutter (Frontend)]
   - 화면에서 사용자가 선택한 재료 ID 목록(1, 2) 전달
   - GET /api/recipes/match?ingredientIds=1,2
                          ↓
🌐 [2. Controller (대문/창구)]
   - HTTP 요청을 받고, 파라미터(1, 2)를 추출하여 Service로 전달
                          ↓
🧠 [3. Service (비즈니스 로직/주방장)]
   - "재료 1, 2번으로 만들 수 있는 레시피를 가져와라"라고 Repository에 명령
                          ↓
🗄️ [4. Repository (DB 창고 관리인)]
   - SQL(JPQL)을 실행하여 DB 데이터 조회
                          ↓
🏛️ [5. Domain / Entity (DB 데이터 설계도)]
   - DB에서 읽어온 원본 데이터 레코드들을 자바 객체(Recipe Entity)로 변환
                          ↓
📦 [6. DTO (포장 상자)]
   - Recipe Entity에서 프론트엔드에 필요한 정보만 쏙 뽑아 RecipeResponse DTO로 변환
                          ↓
📱 [7. Flutter (Frontend)]
   - JSON 응답을 받아 화면에 카드로 출력한다.
