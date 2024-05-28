FROM openjdk:17
COPY "./target/Veterinaria-1-1.jar" "app.jar"
EXPOSE 8088
ENTRYPOINT [ "java", "-jar", "app-jar" ]