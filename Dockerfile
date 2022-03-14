FROM openjdk:17-oracle
EXPOSE 8181
ADD target/zinkworks-atm.jar zinkworks-atm.jar
ENTRYPOINT ["java","-jar","zinkworks-atm.jar"]