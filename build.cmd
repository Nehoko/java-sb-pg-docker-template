@REM don't have any options to test it. Hope it works

set SPRING_DATASOURCE_PASSWORD="drone"
set SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/drone"
set SPRING_DATASOURCE_USERNAME="drone"

call mvnw.cmd clean install