FROM openjdk:21-oracle
WORKDIR /app

COPY target/scrapper.jar /app/scrapper.jar
EXPOSE 8080
CMD ["java", "-jar", "scrapper.jar"]
