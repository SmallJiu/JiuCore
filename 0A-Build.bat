@echo off
set y=%date:~0,4%
set month=%date:~5,2%
set day=%date:~8,2%
set hour=%time:~0,2%
set min=%time:~3,2%
set sec=%time:~6,2%

set time=%y%.%month%.%day%-%hour%.%min%.%sec%
java -jar Compress.jar build\libs\backups\%time%.zip libs src gradle build.gradle gradle.properties gradlew gradlew.bat settings.gradle

start build\libs

start 0A-Commit.bat

gradlew build
