FROM openjdk:17-slim as build

#Add Maintainer Info
#LABEL maintainer="your-email@example.com"

#Make port 8085 available to the world outside this container
#EXPOSE 9191

COPY . /app/bank-BANK-CHECKS

WORKDIR /app/bank-BANK-CHECKS

#Install Maven
RUN apt-get update && \
    apt-get install -y maven tzdata && \
    rm -rf /var/lib/apt/lists/*

# Set the timezone to Jordan
ENV TZ=Asia/Amman

#Use Maven to build the application
RUN mvn clean install

#Start the application
ENTRYPOINT ["java", "-jar", "target/BANK-CHECKS-0.0.1-SNAPSHOT.jar"]
