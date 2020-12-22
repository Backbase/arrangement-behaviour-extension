FROM harbor.backbase.eu/staging/arrangement-manager:DBS-2.20.0-cr.124

ARG JAR_FILE
COPY target/${JAR_FILE} /app/WEB-INF/lib/