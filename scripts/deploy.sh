#!/bin/bash

CONTAINER_ID=$(docker container ls -f "name=dailypet" -q)

echo "> 컨테이너 ID : ${CONTAINER_ID}"

if [ -z ${CONTAINER_ID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> docker stop ${CONTAINER_ID}"
  sudo docker stop ${CONTAINER_ID}
  echo "> docker rm ${CONTAINER_ID}"
  sudo docker rm ${CONTAINER_ID}
  sleep 5
fi

# 기존 이미지 삭제
sudo docker rmi miki308/dailypet:latest

# 도커허브 이미지 pull
sudo docker pull miki308/dailypet:latest

# 도커 run
docker run -d -p 8080:8080 -v /home/ec2-user:/config --name dailypet
