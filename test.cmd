@REM don't have any options to test it. Hope it works

set SPRING_DATASOURCE_PASSWORD="template"
set SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/template"
set SPRING_DATASOURCE_USERNAME="template"

call .\mvnw clean verify