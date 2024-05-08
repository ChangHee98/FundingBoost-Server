# gradle:7.3.1-jdk17 이미지를 기반으로 함
FROM gradle:8.7.0-jdk17

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# Spring 소스 코드를 이미지에 복사
COPY . .

# gradle 초기화
RUN gradle init

# gradle wrapper를 프로젝트에 추가
RUN gradle wrapper

# gradlew를 이용한 프로젝트 필드
RUN chmod +x gradlew

# gradlew를 이용한 프로젝트 필드
RUN ./gradlew clean build 

# DATABASE_URL을 환경 변수로 삽입
ENV DATABASE_URL=jdbc:mysql://mysql/fundingboost

# 빌드 결과 jar 파일을 실행
CMD ["java", "-jar", "-Dspring.profiles.active=local", "/home/gradle/project/build/libs/fundingboost-1.0.jar"]
