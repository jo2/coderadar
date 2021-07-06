FROM alpine:3.14

ARG SONAR_SCANNER_HOME=/opt/sonar-scanner
ARG SONAR_SCANNER_VERSION=4.6.2.2472
ARG UID=1000
ARG GID=1000
ENV HOME=/tmp \
    XDG_CONFIG_HOME=/tmp \
    SONAR_SCANNER_HOME=${SONAR_SCANNER_HOME} \
    SONAR_USER_HOME=${SONAR_SCANNER_HOME}/.sonar \
    PATH=${SONAR_SCANNER_HOME}/bin:${PATH} \
    SRC_PATH=/usr/src

RUN apk update \
    && addgroup -S -g ${GID} scanner-cli \
    && adduser -S -D -u ${UID} -G scanner-cli scanner-cli \
    && apk add --virtual \
        wget \
        unzip \
        gnupg \
    && apk add \
        git \
        bash \
        openjdk7 \
        openjdk8 \
        openjdk9 \
        openjdk10 \
        openjdk11 \
        maven \
        gradle \
    && wget -U "scannercli" -q -O /opt/sonar-scanner-cli.zip https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_SCANNER_VERSION}.zip \
    && unzip /opt/sonar-scanner-cli.zip \
    && mv sonar-scanner-${SONAR_SCANNER_VERSION} ${SONAR_SCANNER_HOME} \
    && mkdir -p "${SRC_PATH}" "${SONAR_USER_HOME}" "${SONAR_USER_HOME}/cache"\
    && chown -R scanner-cli:scanner-cli "${SONAR_SCANNER_HOME}" "${SRC_PATH}" \
    && chmod -R 777 "${SRC_PATH}" "${SONAR_USER_HOME}"

ADD coderadar-app/build/libs/coderadar-app-1.0.0.local.jar /coderadar.jar
ENTRYPOINT ["/usr/lib/jvm/java-11-openjdk/bin/java","-jar","/coderadar.jar"]
