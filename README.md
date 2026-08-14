# 보글보글 재료 마을 (Bogle-Bogle)
-> 다음으로 할것 : 프론트 이쁘게 만들기 백엔드 기능 추가하기 ( 하나가없으면 부족하다고 알려주는 시스템 등 디벨롭하기)

**사용자가 선택한 식재료를 기반으로 최적의 레시피를 매칭해주는 백엔드 서비스**
---
##  Tech Stack (기술 스택)

###  Front-end (Client)
*    **Language:** HTML, CSS
    

###  Back-end (Server)
*   **Framework:** Spring Boot 
*   **Language:** Java 17
      
①  Controller (요청 창구): GET /api/recipes/match 엔드포인트를 통해 클라이언트의 식재료 ID 파라미터를 수신합니다.

②  Service (비즈니스 로직): 전달받은 파라미터를 바탕으로 레시피 매칭 및 매칭률 계산 로직을 처리합니다.

③  Repository & Domain: JPA/JPQL을 통해 DB에서 필요한 레시피 및 재료 엔티티 데이터를 조회합니다. 

④ DTO (Data Transfer Object): 엔티티를 직접 반환하지 않고, 화면에 필요한 데이터만 추려 RecipeResponse DTO로 변환하여 클라이언트에 응답합니다.회

### Database & ORM
*   **Database:** MySQL 
*   **ORM:** Spring Data JPA 
    *   *역할: 레시피-재료 관계형 데이터 저장 및 관리, 다대다(N:M) 매핑 테이블 조인(JOIN) 연산 수행*


## 앱 실행하는 법
1. java를 실행한다
2. http://localhost:8080/
 
##  전체 시스템 상호작용 흐름 (System Architecture & Flow)

사용자가 앱에서 재료를 선택하고 결과를 보기까지의 전체 흐름은 다음과 같이 상호작용합니다.

## 초기 ERD 시스템
<img width="1211" height="515" alt="image" src="https://github.com/user-attachments/assets/4252784d-6ba7-4445-afbb-6e4cf4e1e1af" />

### 예외처리
1. 같은 재료를 다시 입력할때, 이미 있는재료에서 수량만 추가하기
2. 0이나 음수를 입력할떄 불가능하다고 뜨기

```mermaid
flowchart TD
    A["📱 1. Frontend"] -->|"GET /api/recipes/match?ingredientIds=1,2"| B["🌐 2. Controller (요청 창구)"]
    B -->|"파라미터 전달"| C["🧠 3. Service (비즈니스 로직)"]
    C -->|"레시피 조회 명령"| D["🗄️ 4. Repository (DB 관리)"]
    D -->|"SQL / JPQL 실행"| E[("🏛️ 5. Domain / Entity")]
    E -->|"필요한 데이터만 추출"| F["📦 6. RecipeResponse DTO"]
    F -->|"응답"| A

    style A fill:#ffe0b2,stroke:#f57c00,stroke-width:2px
    style B fill:#e1f5fe,stroke:#0288d1,stroke-width:1px
    style C fill:#e8f5e9,stroke:#388e3c,stroke-width:1px
    style D fill:#fff3e0,stroke:#e65100,stroke-width:1px
    style E fill:#f3e5f5,stroke:#7b1fa2,stroke-width:1px
    style F fill:#eceff1,stroke:#455a64,stroke-width:1px



