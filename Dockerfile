FROM maven:latest

COPY . .

# RUN mvn clean install

EXPOSE 8080

# CMD [ "java -jar target/restapi-0.0.1.jar" ]
CMD [ "mvn", "spring-boot:run" ]
