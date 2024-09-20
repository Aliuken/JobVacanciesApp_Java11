sudo docker volume prune -f
sudo docker network create "external-net-elastic-stack"
sudo docker compose build
sudo docker compose up
sudo docker compose start
