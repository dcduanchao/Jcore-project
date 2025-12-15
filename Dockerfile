FROM openjdk:17-jdk-slim

# 拷贝 Maven 构建好的 JAR
COPY default/Jcore-manage/target/Jcore-manage-1.0-SNAPSHOT.jar /app/app.jar

WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
