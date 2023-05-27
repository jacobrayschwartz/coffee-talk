FROM node:18-alpine as frontend-build
COPY ./react-app /src
WORKDIR /src
RUN npm install && npm run build

FROM gradle:8.0.2-jdk17 AS build

WORKDIR /src
COPY . /src
RUN gradle installDist


FROM eclipse-temurin:17.0.4.1_1-jre
RUN mkdir -p /app/react-app
WORKDIR /app/
COPY --from=build /src/build/install/coffee-talk .
COPY --from=frontend-build /src/out ./react-app/out
USER 1001
CMD ["bin/coffee-talk"]
