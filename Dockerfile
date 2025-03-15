# ARM64 플랫폼으로 base image를 명시하여 가져옵니다.
FROM --platform=linux/arm64 openjdk:21-jdk-slim-buster

# 컨테이너 내부 작업 디렉토리 설정
WORKDIR /app

# 로컬 빌드 결과물(JAR 파일)을 컨테이너로 복사합니다.
COPY /build/libs/art-server-0.0.1-SNAPSHOT.jar .

# 컨테이너 시작 시 실행할 명령어를 JSON 배열 형식으로 지정합니다.
ENTRYPOINT ["java", "-jar", "art-server-0.0.1-SNAPSHOT.jar"]
