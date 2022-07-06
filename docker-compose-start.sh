docker volume prune -f
docker network create "external-net"
docker-compose build
docker-compose up
docker-compose start