FROM jenkins/jenkins:lts-jdk17

# Establecer variables de entorno para Java que limitan el uso de memoria
ENV JAVA_OPTS="-Xmx2048m -Xms512m -Xss256k -XX:+UseSerialGC"

USER jenkins

# Copiar y asignar propiedad de la lista de plugins a instalar
COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

# Depuración: imprimir información del sistema antes de instalar plugins
USER root
RUN echo "Checking limits:" && ulimit -a && echo "Checking free memory:" && free -h

# Instalar plugins usando jenkins-plugin-cli
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

USER jenkins

VOLUME /var/jenkins_home
EXPOSE 8080
EXPOSE 50000
CMD ["jenkins.sh"]
