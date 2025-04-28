# 1단계: 빌드 전용 스테이지
FROM gradle:8.5-jdk17 AS build

# 필요에 따라 jdk17 → jdk21로 바꿀 수도 있음
WORKDIR /app

# 필요한 파일만 복사해서 캐시 최적화
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat /app/
COPY gradle /app/gradle

# dependencies만 먼저 다운
RUN ./gradlew dependencies

# 소스코드 복사
COPY . /app


# 실패하더라도 로그를 출력하게 수정
RUN chmod +x gradlew
RUN ./gradlew clean build -x check -x test || (cat /app/build/reports/*/* || true) && false


# --------------------------------------------------------

# 2단계: 런타임용 스테이지 (경량화)
FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app

# 빌드 결과물만 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 환경변수 포트 설정 (Railway가 자동으로 $PORT를 할당해줌)
ENV PORT=8080

# 실행
EXPOSE 8080
CMD ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
