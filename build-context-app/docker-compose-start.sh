sudo docker volume prune -f
sudo docker network create "external-net-app"
sudo docker compose build
sudo docker compose up
sudo docker compose start
