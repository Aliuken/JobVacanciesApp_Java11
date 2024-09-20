# Docker commands

## 1. Commands to start and stop JobVacanciesApp and the Elastic Stack

```cmd
sudo ./docker-stop.sh
sudo ./docker-compose-app-start.sh
sudo ./docker-compose-elastic-stack-start.sh
```

## 2. Commands to access the database

```cmd
docker exec -it app-db-container bash -l
/usr/bin/mysql -u root -padmin job-vacancies-app-db
select * from job_category;
```