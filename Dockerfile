FROM repo.backbase.com/backbase-docker-releases/arrangement-manager:DBS-2.19.2

ARG JAR_FILE
COPY target/${JAR_FILE} /app/WEB-INF/lib/
