AppEngine-Demo
=============================

Repository for CS263 class at UCSB

Prerequisite software
----------
- Java7 JDK 
    [http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html]
- Maven 
    [https://maven.apache.org/download.cgi or brew]
- Google App Engine SDK 
    [https://cloud.google.com/appengine/downloads?hl=en_US&_ga=1.109616773.1031383433.1450403374#Google_App_Engine_SDK_for_Java]

Getting started
----------
- set JAVA_HOME env variable to Java7 JDK
- add app engine sdk bin folder to PATH variable, verify by running "appcfg.sh"
- run "mvn clean install" to set-up and get dependencies
- run "mvn appengine:devserver" to deploy on local dev server
  - url: http://localhost:8080/datastore
- run "mvn appengine:update" to deploy on remote app engine server in google cloud
  - url: https://cs263-ae-demo-119421.appspot.com/datastore
