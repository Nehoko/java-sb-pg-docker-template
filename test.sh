#!/usr/bin/env bash

export SPRING_DATASOURCE_PASSWORD="template"
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/template"
export SPRING_DATASOURCE_USERNAME="template"

bash ./mvnw clean verify