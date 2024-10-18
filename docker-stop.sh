cd build-context/docker-compose-app
sudo ./docker-compose-stop.sh

cd ../docker-compose-elk
sudo ./docker-compose-stop.sh

sudo docker volume rm $(docker volume ls -q)
sudo docker volume prune -f
sudo docker network rm "external-net-app"
sudo docker network rm "external-net-elk"

sudo docker kill $(docker ps -q)
sudo docker rm -f -v $(docker ps -a -q)
sudo docker rmi -f $(docker images -q)

sudo docker system prune -a -f --volumes
