FROM openjdk:8-jre
COPY svc /svc
EXPOSE 9000 9443
USER root
RUN chmod -R +x /svc