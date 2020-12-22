FROM harbor.backbase.eu/staging/arrangement-manager:DBS-2.19.0-cr.24

ARG JAR_FILE
COPY target/${JAR_FILE} /app/WEB-INF/lib/