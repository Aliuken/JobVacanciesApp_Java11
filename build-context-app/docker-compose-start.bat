cd C:\Programacion\git\JobVacanciesApp_Java11\build-context-app

docker volume prune -f
docker network create "external-net-app"
docker compose build
docker compose up
docker compose start

pause