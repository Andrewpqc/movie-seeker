FROM tomcat:9
MAINTAINER andrewpqc <JamieOgburn1988@gmail.com>
RUN mkdir -p  /usr/local/tomcat/webapps/service
WORKDIR /usr/local/tomcat/webapps/service
ADD . .
EXPOSE 8081 8080



