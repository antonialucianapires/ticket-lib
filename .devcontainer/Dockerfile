FROM mcr.microsoft.com/vscode/devcontainers/base:ubuntu

RUN apt-get update && apt-get install -y openjdk-17-jdk

RUN apt-get update && apt-get install -y maven

ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64

RUN mkdir -p /root/.m2

COPY settings.xml /root/.m2/settings.xml