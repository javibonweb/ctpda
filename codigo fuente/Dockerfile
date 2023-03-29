FROM registry.access.redhat.com/ubi8/openjdk-11-runtime
# Encoding
ENV JAVA_TOOL_OPTIONS -Dfile.encoding=UTF8
ENV TZ Europe/Madrid
# Variables de entorno
ENV SPRING_PROFILES_ACTIVE default
ENV SPRING_CONFIG_FILE /app/conf/application-integracion.properties
# Directorio aplicacion y de configuracion
RUN mkdir app && mkdir app/conf

USER root
# Se añade dependecia zona horaria
RUN microdnf update && \
    microdnf install -y tzdata
USER jboss

# Fichero de configuracion
#COPY gestor-pdt-web/src/main/resources/application.properties /app/conf/application.properties
COPY gestor-pdt-web/src/main/resources/applicatio*.properties /app/conf/
# Ejecutable con la aplicacion
COPY gestor-pdt-web/target/*exec.jar /app/app.jar


# Puerto que expone
EXPOSE 8080
# Comando que se ejecuta una vez ejecutemos el contenedor
# por ahora sin trustore, añadirlo mas adelante.
# ENTRYPOINT [ "sh", "-c", "java -Djavax.net.ssl.trustStore=$TRUSTSTORE_FILE -Djavax.net.ssl.trustStorePassword=$TRUSTSTORE_PASSWORD -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /app/app.jar --spring.config.location=$SPRING_CONFIG_FILE" ]
ENTRYPOINT [ "sh", "-c", "java -Dlog4j2.formatMsgNoLookups=true -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /app/app.jar --spring.config.location=$SPRING_CONFIG_FILE" ]
#ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -cp /app/app.jar es.juntadeandalucia.ctpda.gestionpdt.web.SpringSWFBootApplication --spring.config.location=/app/conf/application.properties" ]
