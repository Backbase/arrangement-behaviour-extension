FROM harbor.backbase.eu/staging/arrangement-manager:DBS-2.19.2

ARG JAR_FILE
COPY target/${JAR_FILE} /app/WEB-INF/lib/
