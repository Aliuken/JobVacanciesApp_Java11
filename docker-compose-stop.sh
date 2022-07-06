docker-compose stop
docker-compose down
docker volume prune -f
docker network rm "external-net"