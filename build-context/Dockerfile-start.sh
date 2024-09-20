sudo docker build -t job-vacancies-app -f docker-compose-app/Dockerfile .
sudo docker network create external-net-app

sudo docker run -d --name tomcat-app --network external-net-app -p 9080:8080 -v /AppData_Java11/JobVacanciesApp:/AppData_Java11/JobVacanciesApp job-vacancies-app