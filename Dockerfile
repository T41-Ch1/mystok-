FROM tomcat:8.5.41-jre8
COPY ./target/mystok.war /usr/local/tomcat/webapps/
COPY ./conf/server.xml /usr/local/tomcat/conf/
EXPOSE 80
