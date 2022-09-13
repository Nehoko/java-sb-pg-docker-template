#!/usr/bin/env bash

./mvnw spring-boot:build-image -DskipTests && docker-compose up -d