FROM tomcat:8.0.20-jre8
COPY MessageBoard-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
COPY javax.faces-2.2.13.jar /usr/local/tomcat/lib/
COPY weld-servlet-2.3.5.Final.jar /usr/local/tomcat/lib/
COPY log4j.xml /usr/local/tomcat/lib/
COPY hibernate-validator-5.2.4.Final.jar /usr/local/tomcat/lib/
COPY catalina.sh /usr/local/tomcat/bin/
