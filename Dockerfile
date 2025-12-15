FROM eclipse-temurin:17-jdk-alpine

# 拷贝 Maven 构建好的 JAR
COPY Jcore-manage/target/Jcore-manage.jar /app/app.jar

WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
