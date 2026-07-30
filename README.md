# 보글보글 재료 마을 (Bogle-Bogle)

--> 다음 단계 api 다운 받고 테스트해보기 ambiente 전부 준비됨

**사용자가 선택한 식재료를 기반으로 최적의 레시피를 매칭해주는 백엔드 서비스**
---
##  Tech Stack (기술 스택)

###  Front-end (Client)
*   **Framework:** Flutter
*   **Language:** Dart
    

###  Back-end (Server)
*   **Framework:** Spring Boot 
*   **Language:** Java 17
      
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
 
##  전체 시스템 상호작용 흐름 (System Architecture & Flow)

사용자가 앱에서 재료를 선택하고 결과를 보기까지의 전체 흐름은 다음과 같이 상호작용합니다.

## 레시피를 위해 사용한 데이터 셋 링크들
https://github.com/hye1ns/datanalysis_recipe
https://github.com/josephrmartinez/recipe-dataset

```mermaid
flowchart TD
    A["📱 1. Flutter (Frontend)"] -->|"GET /api/recipes/match?ingredientIds=1,2"| B["🌐 2. Controller (요청 창구)"]
    B -->|"파라미터 전달"| C["🧠 3. Service (비즈니스 로직)"]
    C -->|"레시피 조회 명령"| D["🗄️ 4. Repository (DB 관리)"]
    D -->|"SQL / JPQL 실행"| E[("🏛️ 5. Domain / Entity")]
    E -->|"필요한 데이터만 추출"| F["📦 6. RecipeResponse DTO"]
    F -->|"JSON 응답"| A

    style A fill:#ffe0b2,stroke:#f57c00,stroke-width:2px
    style B fill:#e1f5fe,stroke:#0288d1,stroke-width:1px
    style C fill:#e8f5e9,stroke:#388e3c,stroke-width:1px
    style D fill:#fff3e0,stroke:#e65100,stroke-width:1px
    style E fill:#f3e5f5,stroke:#7b1fa2,stroke-width:1px
    style F fill:#eceff1,stroke:#455a64,stroke-width:1px



