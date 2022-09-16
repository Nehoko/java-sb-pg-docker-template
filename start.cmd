@REM don't have any options to test it. Hope it works

call .\mvnw spring-boot:build-image -DskipTests
call docker-compose up -d