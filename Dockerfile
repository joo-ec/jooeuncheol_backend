
FROM openjdk:17-jdk-slim

WORKDIR /bank

COPY build/libs/*.jar bank.jar

# 애플리케이션 포트 설정
#EXPOSE 9091

ENTRYPOINT ["java", "-jar", "bank.jar"]