FROM openjdk:11
EXPOSE 8088
ADD target/spring-boot-testing.jar spring-boot-testing.jar
ENTRYPOINT ["java","-jar","/spring-boot-testing.jar"]

