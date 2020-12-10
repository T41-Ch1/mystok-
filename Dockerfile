FROM tomcat:8.5.41-jre8-alpine
COPY ./webapps/mystok.war /usr/local/tomcat/webapps/
COPY ./conf/server.xml /usr/local/tomcat/conf/
COPY ./ROOT/index.jsp /usr/local/tomcat/webapps/ROOT/
EXPOSE 80
