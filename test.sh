#!/usr/bin/env bash

export SPRING_DATASOURCE_PASSWORD="drone"
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/drone"
export SPRING_DATASOURCE_USERNAME="drone"

bash ./mvnw clean verify