FROM postgres:latest
USER root
RUN service postgresql start
USER postgres
COPY init.sql /docker-entrypoint-initdb.d/