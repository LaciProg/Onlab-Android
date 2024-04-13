FROM gradle:latest
WORKDIR api
COPY . .
RUN ./gradlew build
CMD "./gradlew" "run"