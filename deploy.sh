./mvnw clean package -DskipTests
cp target/demo-0.0.1-SNAPSHOT.jar src/main/docker
cd src/main/docker || exit 1
docker-compose stop app
echo y | docker-compose rm app
docker rmi demo
docker-compose up -d --build app