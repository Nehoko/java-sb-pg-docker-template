#!/bin/bash
set -e
echo "creating database"
export PGPASSWORD=$POSTGRES_PASSWORD;
psql --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASS';
  CREATE DATABASE $APP_DB_NAME;
  CREATE SCHEMA $APP_DB_NAME;
  GRANT ALL PRIVILEGES ON DATABASE $APP_DB_NAME TO $APP_DB_USER;
EOSQL

# create tables (for some reason, docker do not use sql files when init container)
#echo "creating tables"
#export PGPASSWORD=$APP_DB_PASS;
#psql -U $APP_DB_USER -d $APP_DB_NAME -a -f ./create_drone.sql
#psql -U $APP_DB_USER -d $APP_DB_NAME -a -f ./create_medication.sql

echo "init done"