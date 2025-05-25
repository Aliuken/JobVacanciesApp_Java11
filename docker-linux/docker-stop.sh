clear

cd ./build-context-app
sudo ./docker-compose-stop.sh

cd ../build-context-elk
sudo ./docker-compose-stop.sh

sudo docker volume rm $(sudo docker volume ls -q)
sudo docker volume prune -f
sudo docker network rm "external-net-app"
sudo docker network rm "external-net-elk"

sudo docker kill $(sudo docker ps -q)
sudo docker rm -f -v $(sudo docker ps -a -q)
sudo docker rmi -f $(sudo docker images -q)

sudo docker system prune -a -f --volumes

sudo docker buildx history rm --all