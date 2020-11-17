#!/usr/bin/env bash

docker exec -it kafka-broker kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic data-input
docker exec -it kafka-broker kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic data-output