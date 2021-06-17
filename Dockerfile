FROM sonarsource/sonar-scanner-cli:4.6

ARG MAVEN_VERSION=3.8.1
ARG USER_HOME_DIR="/root"
ARG SHA=0ec48eb515d93f8515d4abe465570dfded6fa13a3ceb9aab8031428442d9912ec20f066b2afbf56964ffe1ceb56f80321b50db73cf77a0e2445ad0211fb8e38d
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN \
    apk update && \
    apk add gradle maven openjdk11

## Download and install Gradle
#RUN \
#    cd /usr/local && \
#    curl -L https://services.gradle.org/distributions/gradle-2.5-bin.zip -o gradle-2.5-bin.zip && \
#    unzip gradle-2.5-bin.zip && \
#    rm gradle-2.5-bin.zip
#
## Export some environment variables
#ENV GRADLE_HOME=/usr/local/gradle-2.5
#ENV PATH=$PATH:$GRADLE_HOME/bin JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
#
## Dowload and install Maven
#RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
#  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
#  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
#  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
#  && rm -f /tmp/apache-maven.tar.gz \
#  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
#
## Export some environment variables
#ENV MAVEN_HOME /usr/share/maven
#ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

ADD coderadar-app/build/libs/coderadar-app-1.0.0.local.jar /coderadar.jar
ENTRYPOINT ["java","-jar","/coderadar.jar"]
