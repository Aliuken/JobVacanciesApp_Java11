docker volume prune -f
docker network create "external-net-elastic-stack"
docker-compose build
docker-compose up
docker-compose start