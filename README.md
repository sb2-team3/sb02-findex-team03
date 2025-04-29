# **팀명 : 코드쥬**
[GitHub Repository](https://github.com/sb2-team3/sb02-findex-team03)  
[Notion](https://ballistic-teller-d42.notion.site/1d9987eef3c680bea1cbeb1e529f6c91?pvs=4)

## **팀원 구성**

| <img src="https://github.com/user-attachments/assets/443c3687-bbb8-4e30-9928-b4e28cc90bd1?format=webp&width=80" style="border-radius:50%"/> | <img src="https://github.com/user-attachments/assets/82cd3287-ef07-415b-9dd8-9556cd0776a2?format=webp&width=80" style="border-radius:50%"/> | <img src="https://github.com/user-attachments/assets/9aaf9939-f81b-46fa-8f62-cb85403b589b?format=webp&width=80" style="border-radius:50%"/> | <img src="https://github.com/user-attachments/assets/f18f0576-a415-4cef-8d7a-f709178c5473?format=webp&width=80" style="border-radius:50%"/> | <img src="https://github.com/user-attachments/assets/53315c57-c06c-45d0-a392-b09ec1b8368c?format=webp&width=80" style="border-radius:50%"/> |
| :---: | :---: | :---: | :---: | :---: |
| 이준엽 (팀장) | 김세은 | 이유나 | 이준혁 | 김태우 |
| [GitHub](https://github.com/leejunnyeop) | [GitHub](https://github.com/Seeun126) | [GitHub](https://github.com/nayu-yuna) | [GitHub](https://github.com/LeejunHyeok7170) | [GitHub](https://github.com/kimtaewoo9) |




## **프로젝트 소개**
- **프로젝트명**: FINDEX 금융 지수 분석 웹사이트
- **프로젝트 기간**: 2025.04.21 ~ 2025.04.29



## **기술 스택**
- **Backend**: Spring Boot, Spring Data JPA, QueryDSL
- **Database**: PostgreSQL
- **공통 Tool**: Git & Github, Swagger, Discord, IntelliJ, Notion, Postman, Railway


## **팀원별 맡은 작업**
### **이준엽 (팀장)**
- **자동 연동 설정 수정**
    - PATHS `/api/auto-sync-configs/{indexInfoId}` **활성화** 속성 수정
- **자동 연동 설정 목록 조회**
    - `GET /api/auto-sync-configs`
    - {지수}, {활성화}로 자동 연동 설정 목록을 조회, 정렬 및 페이지네이션(QueryDSL) 구현
    - **afterId (이전 페이지의 마지막 요소 ID)** 활용
- **배치에 의한 지수 데이터 연동 자동화**
    - **지수 데이터 연동 프로세스**를 일정 주기(1일)마다 자동화

 ### **김세은**
- **지수 정보 연동**
    - `/api/sync-jobs/index-infos`에서 OpenAPI를 통해 지수 정보를 등록, 수정
- **연동 작업 목록 조회**
    - `/api/sync-jobs`에서 연동 작업 목록 조회 기능 구현
    - **{유형}, {지수}, {대상 날짜}, {작업자}, {결과}, {작업일시}** 조건으로 조회 및 페이지네이션 처리
      

### **이유나**
- **지수 정보 등록 API**
    - `POST /api/index-infos`에서 지수 정보 등록 기능 구현
    - 중복된 `indexClassification`과 `indexName` 체크 후 등록 처리
- **지수 정보 수정 API**
    - `PATCH /api/index-infos/{id}`에서 지수 정보 수정 기능 구현
- **지수 정보 삭제 API**
    - `DELETE /api/index-infos/{id}`에서 지수 정보 삭제 기능 구현
- **지수 상세 조회 API**
    - `GET /api/index-infos/{id}`에서 특정 지수의 상세 정보 조회 기능 구현
- **지수 목록 조회 및 페이지네이션**
    - `GET /api/index-infos`에서 지수 목록 조회 및 페이지네이션 기능 구현
    - 다양한 필터링 및 페이지네이션 처리
 
### **이준혁**
- **데이터 OpenAPI 연동 API**
    - `POST /api/sync-jobs/index-data`에서 OpenAPI를 통한 지수 데이터 연동
    - **{지수}, {대상 날짜}** 범위 지정 가능
    - 연동 작업 후 결과 등록
- **데이터 목록 조회 API**
    - `POST /api/index-data`에서 지수 데이터 목록 조회 기능 구현
    - **{지수}, {날짜}** 조건에 맞춰 조회
- **데이터 정보 수정 API**
    - `POST /api/index-data/{id}`에서 지수 데이터 정보 수정
- **데이터 정보 등록 API**
    - `POST /api/index-data/{id}`에서 지수 데이터 정보 등록
- **데이터 정보 삭제 API**
    - `POST /api/index-data/{id}`에서 지수 데이터 삭제

### **김태우**
- **지수 차트 조회 API**
    - `POST /api/index-data/{id}/chart`에서 지수 차트 조회 기능 구현
    - {즐겨찾기}된 지수의 성과 정보를 포함
- **지수 성과 랭킹 조회 API**
    - `POST /api/index-data/performance/rank`에서 지수 성과 랭킹 조회 기능 구현
    - 월/분기/년 단위 시계열 데이터
    - 이동평균선 (5일, 20일) 데이터 제공
- **관심 지수 성과 조회 API**
    - `POST /api/index-data/performance/rank`에서 관심 지수 성과 조회 기능 구현
    - 전일/전주/전월 대비 성과 랭킹 제공



## **파일 구조**
![프로젝트 파일 구조](https://github.com/user-attachments/assets/f5371587-dece-48e7-894a-f5e3f52d660f)


## **구현 홈페이지**
[CodeZoo](https://sb02-findex-team03-production-fefc.up.railway.app/)

## **프로젝트 회고록**
[회고록 보기](https://www.notion.so/4L-1e0987eef3c68016a9f4ee169b9c816a)
