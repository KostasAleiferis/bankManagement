rem @echo off

set JAVA_HOME="C:\Program Files\Java\jdk-17"
set JAVA=%JAVA_HOME%\bin\java
set JAR_FILE=jar\bankManagement-0.0.1-SNAPSHOT.jar

rem -------LOGGING CONFIG-------
set LOG_DIR=logs
set NODE=Localhost
set LOG_LEVEL=info
set LOG_APPENDER=ROLLING_APPENDER
rem set idocs.log.level=debug
rem set idocs.log.appender=debuggingAppender

rem ------------------------
set JVM_OPTIONS=-Xms512m -Xmx2G
set JAVA_OPTS=%JVM_OPTIONS%
set REMOTE_DEBUG= -Xdebug -Xrunjdwp:transport=dt_socket,address=:5555,server=y,suspend=n

%JAVA% %JAVA_OPTS% %REMOTE_DEBUG% -jar %JAR_FILE%