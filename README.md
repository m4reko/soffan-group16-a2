# Continuous Integration
Continuous Integration is a simple continous integration server that builds a Gradle project and reports the status of it for every push command done to the remote repository. It reports the build status to the commit status on Github.

## Travis-CI Status
[![Build Status](https://travis-ci.com/m4reko/soffan-group16-a2.svg?branch=main)](https://travis-ci.com/m4reko/soffan-group16-a2)


## How to Use It
### Requirements
Java `11+`, Gradle `6.8.2`, Ngrok `2.3.35`

### Building
`./gradlew build`
### Running
`./gradlew run`
### Testing
`./gradlew test`
### Getting ngrok url
`./ngrok http 8080`
### Visiting Local Server
eg. `http://localhost:8080`
### Github Settings
Settings >> Webhooks >> Add webhook.

Payload URL: ngrok URL (eg. `http://12345678.ngrok.io`)

Set the content type: application/json


## File Structure
We are using a common Java package folder structure.  
|Folder/File|Description|
| --- | --- |
|app/src/main/java/ciserver|Contains the source code of the CI server|
|app/src/main/java/ciserver/ContinuousIntegrationServer.java|CI Server object; main function to run the server|
|app/src/main/java/ciserver/Repository.java|Repository object: clone, build, report commit status|
|app/src/main/java/ciserver/Payload.java|Payload object: JSON parse|
|app/src/test/java/ciserver/ContinuousIntegrationServerTest.java|Contains unit test for the overall program|
|app/src/test/java/ciserver/PayloadTest.java|Contains unit test for the payload object.|
|app/src/test/java/ciserver/RepositoryTest.java|Contains unit test for the repository class.|
|app/src/test/resources|Contains hard coded simple resources for the unit test to use|
|app/build.gradle|Contains the dependencies of the project which is handled by Gradle|
|.travis.yml|Contains Travis CI configurations|

## Statement of Contributions
Markus Wesslén -  
Justin Arieltan -  
David Ek -  
Jiarui Tan -  

## Pass with distinction ##
* Most commits are linked to a related issue. Issues and commits have descriptive names and some contain additional relevant information.