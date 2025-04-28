#빌드 스테이지
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

#Gradle 관련 파일 복사
COPY build.gradle settings.gradle gradlew gradlew.bat /app/
COPY gradle /app/gradle

#Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

#소스코드 복사
COPY src /app/src

#프로젝트 빌드 (테스트는 제외)
RUN ./gradlew clean build -x test

#실행 스테이지
FROM eclipse-temurin:21-jre
WORKDIR /app

#빌드 결과물 복사
COPY --from=build /app/build/libs/*.jar app.jar

#Railway는 PORT 환경변수를 주입해줌
ENV PORT=8080

#포트 오픈
EXPOSE 8080

#실행 명령어
CMD ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]

ENV TZ=Asia/Seoul

RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && echo "Asia/Seoul" > /etc/timezone \
    && apk del tzdata
