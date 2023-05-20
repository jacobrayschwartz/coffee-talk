FROM gradle:8.0.2-jdk17 AS build

WORKDIR /src
COPY . /src
RUN gradle installDist

FROM eclipse-temurin:17.0.4.1_1-jre
RUN mkdir /app/
WORKDIR /app/
COPY --from=build /src/build/install/coffee-talk .
USER 1001
CMD ["bin/coffee-talk"]
