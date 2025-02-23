cd C:\Programacion\git\JobVacanciesApp_Java11\build-context-elk-windows

docker volume prune -f
docker network create "external-net-elk"
docker compose build
docker compose up
docker compose start

pause