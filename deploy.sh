./mvnw clean package -DskipTests
cp target/demo-0.0.1-SNAPSHOT.jar src/main/docker
cd src/main/docker || exit 1
docker-compose up -d