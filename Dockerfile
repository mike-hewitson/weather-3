FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/weather-3.jar /weather-3/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/weather-3/app.jar"]
