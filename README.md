# Java-demo --> Release tracker task for neon

## Prerequisites:
- mysql server
- jdk 19
- docker for desktop (optional)

## Initial set up
- To start application with docker:
1.1 navigate to root folder and run <b>mvn clean install -DskipTests</b> for jar to be createad
1.2 from root enter command <b>docker-compose build</b>
1.3 after building is done type <b>docker-compose up</b> This will bring containers for mysql server and for the spring boot api.

- Without docker, just change mysql connection string(which is located to application.properties) to point to appropriate sql server, also username and password accordingly. After that start the app from IDEA, hibernate will create database on start up.

## Running the app

2.Swagger ui 
![swagger-ui](https://user-images.githubusercontent.com/2013682/219371027-08496ce5-d327-4ceb-9ce7-bae4c1855c26.PNG)

- This is the api definition done via swagger opn api. If application is running from docker choose from servers dropdown to point on <b>localhost:8090</b> otherwise leave it to localhost:8080

## Authorization
2.1 First thing first, we need to authorize in order to use other endpoints
- For authorization i used Bearer token which is obtained from Firebase, since i made test table for user authentication.
  navigate to user/authorize endpoint and execute request. Body data will be prepopulated with some test user.
    
![authorization-done](https://user-images.githubusercontent.com/2013682/219372721-7cafe562-80c2-4769-8e0b-d0cbb45de352.PNG)

- Copy response body, and navigate to authorize section and paste it into value field

![bearer value set](https://user-images.githubusercontent.com/2013682/219373732-71d9b734-4a20-4d0f-a87d-bc07ba2cec1d.PNG)

 - Hit authorize and then close the pop up. Everything should be set now for further testing.
 - <i> Additional info:</i> If you posses firebase  integration already, and have table of authenticated users, you could donwload your firebase admin sdk and 
replace with one in project, and try validating against your users location is /resource/static/firebase-authenticate.json. or you can just change value of google.credentials.json.location in application.properties to point on different json of your choice.


3. Hit some endpoint and you should see in curl section that Bearer is correctly set in header.

![get request](https://user-images.githubusercontent.com/2013682/219374168-f679e1f6-f4ee-4f56-bdd5-f566009c244e.PNG)

## Technologies and frameworks used:
- Spring boot application 
- mysql and hibernate ORM
- firebase admin
- log4j2 (logs are located  <b>/Logs<b> folder
- open api swagger 

