FROM sonarsource/sonar-scanner-cli:4.6

RUN \
    apk update && \
    apk add gradle maven

ADD coderadar-app/build/libs/coderadar-app-1.0.0.local.jar /coderadar.jar
ENTRYPOINT ["java","-jar","/coderadar.jar"]
