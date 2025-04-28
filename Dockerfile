# 빌드 스테이지
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Java (Groovy DSL)용 복사
COPY build.gradle settings.gradle gradlew gradlew.bat /app/
COPY gradle /app/gradle
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# 실행 스테이지
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENV PORT=8080
EXPOSE 8080
CMD ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]

# 빌드 결과물만 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 환경변수 포트 설정 (Railway가 자동으로 $PORT를 할당해줌)
ENV PORT=8080

# 실행
EXPOSE 8080
CMD ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
