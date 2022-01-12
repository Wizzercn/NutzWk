FROM openjdk:11.0.13-jdk
COPY ./target/mini.jar /app/
COPY ./target/classes/application.yaml /app/
ENV TZ Asia/Shanghai

WORKDIR /app
CMD ["java","-jar","-Dnutz.boot.configure.yaml.dir=/app","/app/mini.jar"]