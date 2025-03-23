cd C:\Programacion\git\JobVacanciesApp_Java11\docker-windows\build-context-elk

docker volume prune -f
docker network create "external-net-elk"
docker compose build
docker compose up
docker compose start

pause