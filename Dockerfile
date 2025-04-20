FROM openjdk:17-jdk-slim

# Atualiza os pacotes e instala o FFmpeg
RUN apt-get update && apt-get install -y ffmpeg

# Define o volume temporário
VOLUME /tmp

# Define o nome do JAR gerado pelo Maven
ARG JAR_FILE=target/authservice-0.0.1-SNAPSHOT.jar

# Copia o JAR para a imagem com o nome app.jar
COPY ${JAR_FILE} app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
