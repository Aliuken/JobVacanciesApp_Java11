cd docker-compose-app
./docker-compose-stop.sh

cd ../docker-compose-elastic-stack
./docker-compose-stop.sh

sudo docker volume rm $(docker volume ls -q)
sudo docker volume prune -f
sudo docker network rm "external-net-app"
sudo docker network rm "external-net-elastic-stack"

sudo docker kill $(docker ps -q)
sudo docker rm $(docker ps -a -q)
sudo docker rmi $(docker images -q)