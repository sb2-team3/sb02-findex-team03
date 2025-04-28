https://github.com/sb2-team3/sb02-findex-team03

# **{코드쥬}**

![](https://codeit-static.codeit.com/_main/production/_next/static/media/guru-trigger.798e15e9.png?w=64&q=75&t=cover)

## **팀원 구성**

이준엽 ([https://github.com/leejunnyeop](https://github.com/LeejunHyeok7170))
김태우 (https://github.com/kimtaewoo9)
이유나 (https://github.com/nayu-yuna)
이준혁 (https://github.com/LeejunHyeok7170)
김세은 ([https://github.com/Seeun126](https://github.com/LeejunHyeok7170))

---

## **프로젝트 소개**

- FINDEX 금융 지수 분석 웹사이트
- 프로젝트 기간: 2024.08.13 ~ 2024.09.03

---

## **기술 스택**

- Backend: Spring Boot, MapStruct, Spring Data JPA
- Database: PostgreSQL
- 공통 Tool: Git & Github, Discord, IntelliJ

## 맡은 작업

- **이준엽(팀장**)
    1. 깃허브 관리 
    2. 공공데이터 수집 api 코드 구현
    3. 자동연동 페이징 (QueryDels)
    4. 자동연동 활성화 구현
    5. 커서페이징 커서 직접 구현 시
- **이유나**
    - **지수 정보 등록 API**
        - `POST /api/index-infos` 엔드포인트에서 지수 정보 등록 기능 구현
        - 중복된 `indexClassification`과 `indexName` 체크 후 등록 처리
    - **지수 정보 수정 API**
        - `PATCH /api/index-infos/{id}` 엔드포인트에서 지수 정보 수정 기능 구현
        - 입력된 ID에 해당하는 지수 정보 수정 및 저장 처리
    - **지수 정보 삭제 API**
        - `DELETE /api/index-infos/{id}` 엔드포인트에서 지수 정보 삭제 기능 구현
        - 해당 ID의 지수 정보를 삭제하고 관련 데이터도 함께 삭제
    - **지수 상세 조회 API**
        - `GET /api/index-infos/{id}` 엔드포인트에서 특정 지수의 상세 정보 조회 기능 구현
        - ID에 해당하는 지수 정보를 반환
    - **지수 목록 조회 및 페이지네이션**
        - `GET /api/index-infos` 엔드포인트에서 지수 목록 조회 및 페이지네이션 기능 구현
        - 다양한 필터링(`indexClassification`, `indexName`, `favorite`)을 지원하고, 페이지네이션 처리
- **김태우**
    - **지수 차트 조회 API**
        - `POST /api/index-data/{id]/chart`  엔드포인트에서 지수 차트 조회 기능 구현
        - **{즐겨찾기}**된 지수의 성과 정보를 포함합니다.
    
    - **지수 성과 랭킹 조회 API**
        - `POST /api/index-data/performance/rank`  엔드포인트에서 지수 성과 랭킹 조회 기능 구현
        - 월/분기/년 단위 시계열 데이터
            - **{종가}**를 기준으로 합니다.
        - 이동평균선 데이터
            - 5일, 20일
            - 이동평균선이란 지난 n일간 가격의 평균을 의미합니다.
            - 예를 들어 3월 13일의 5일 이동평균선은 3월 9일부터 3월 13일까지의 **{종가}**의 평균값입니다.
    
    - **관심 지수 성과 조회 API**
        - `POST /api/index-data/performance/rank`  엔드포인트에서 관심 지수 성과 조회 기능 구현
        - 전일/전주/전월 대비 성과 랭킹
            - 성과는 **{종가}**를 기준으로 비교합니다.
- **이준혁**
    - 데이터 openAPi 연동 API
        - `POST /api/sync-jobs/index-data` 엔드포인트에서 Openapi 데이터를 지수 데이터 repo로 연동
        - Open API를 활용해 지수 데이터를 등록, 수정할 수 있습니다.
        - **{지수}, {대상 날짜}**로 연동할 데이터의 범위를 지정할 수 있습니다.
            - **{대상 날짜}**는 반드시 지정해야하며, 범위로 지정할 수 있습니다.
            - **{지수}**는 선택적으로 지정할 수 있습니다.
        - 지수 데이터 연동은 사용자가 직접 실행할 수 있습니다.
            - 실행 후 연동 작업 결과를 등록합니다.
            - 대상 지수, 대상 날짜가 여러 개인 경우 지수, 날짜 별로 이력을 등록합니다.
    - 데이터 목록 조회 API
        - `POST /api/index-data`  엔드포인트에서 지수 데이터 목록 조회 기능 구현
        - **{지수}, {날짜}**로 지수 데이터 목록을 조회할 수 있습니다.
            - **{지수}**는 완전 일치 조건입니다.
            - **{날짜}**는 범위 조건입니다.
            - 조회 조건이 여러 개인 경우 모든 조건을 만족한 결과로 조회합니다.
        - **{소스 타입}**을 제외한 모든 속성으로 정렬 및 페이지네이션을 구현합니다.
            - 여러 개의 정렬 조건 중 선택적으로 1개의 정렬 조건만 가질 수 있습니다.
            - 정확한 페이지네이션을 위해 **{이전 페이지의 마지막 요소 ID}**를 활용합니다.
            - 화면을 고려해 적절한 페이지네이션 전략을 선택합니다.
    - 데이터 정보 수정 API
        - **{지수}, {날짜}**를 제외한 모든 속성을 수정할 수 있습니다.
            - Open API를 활용해 자동으로 수정할 수 있습니다.
        
    - 데이터 정보 등록 API
        - **{지수}**, **{날짜}**부터 **{상장 시가 총액}**까지 모든 속성을 입력해 지수 데이터를 등록할 수 있습니다.
            - **{지수}, {날짜}** 조합값은 중복되면 안됩니다.
            
    - 데이터 정보 삭제 API
        - 지수 데이터를 삭제할 수 있습니다.
- **김세은**
    - **지수 정보 연동**
        - Open API를 활용해 지수 정보를 수정할 수 있습니다.
    - **연동 작업 목록 조회**
        - **{유형}, {지수}, {대상 날짜}, {작업자}, {결과}, {작업일시}**로 연동 작업 목록을 조회할 수 있습니다.
            - 조회 조건이 여러 개인 경우 모든 조건을 만족한 결과로 조회합니다.
        - **{대상 날짜}, {작업일시}**으로 정렬 및 페이지네이션을 구현합니다.
            - 여러 개의 정렬 조건 중 선택적으로 1개의 정렬 조건만 가질 수 있습니다.
            - 정확한 페이지네이션을 위해 {이전 페이지의 마지막 요소 ID}를 활용합니다.
            - 화면을 고려해 적절한 페이지네이션 전략을 선택합니다.

## 파일 구조
![스크린샷 2025-04-28 151531](https://github.com/user-attachments/assets/f5371587-dece-48e7-894a-f5e3f52d660f)


## 구현 홈페이지

https://www.codezooFindex.com/

## **프로젝트 회고록**

[https://www.notion.so/1c7987eef3c6808e94ffd1c23511ea8a?v=1c7987eef3c6806d906c000c22284eaf&p=1e0987eef3c68016a9f4ee169b9c816a&pm=s](https://www.notion.so/4L-1e0987eef3c68016a9f4ee169b9c816a?pvs=21)
