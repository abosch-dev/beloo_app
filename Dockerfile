# =================================================================
# 1. ETAPA DE CONSTRUCCIÓN (BUILD STAGE)
# Utiliza una imagen con el JDK completo para compilar
# =================================================================
FROM eclipse-temurin:17-jdk-alpine AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Gradle y el wrapper
# Usamos 'gradlew' (wrapper) para evitar instalar Gradle en el contenedor
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Otorga permisos de ejecución al script gradlew ANTES de usarlo
RUN chmod +x gradlew

# Copia el código fuente
COPY src src

# Ejecuta la tarea 'bootJar' de Gradle para construir la aplicación
# La aplicación Spring Boot se empaquetará en /app/build/libs/app.jar
RUN ./gradlew bootJar -x test

# =================================================================
# 2. ETAPA DE EJECUCIÓN (RUNTIME STAGE)
# Utiliza solo la JRE (Java Runtime Environment), mucho más ligera y segura
# =================================================================
FROM eclipse-temurin:17-jre-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR generado en la etapa de construcción.
# El nombre del archivo JAR generado por 'bootJar' suele ser 'nombre-del-proyecto-version.jar'
# Usamos un comodín para simplificar.
COPY --from=build /app/build/libs/*.jar app.jar

# Expone el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando de inicio del microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]