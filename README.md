# Chalba - Code the load
Chalba is open source hackable load testing tool inspired from jmeter, gatling, grinder to write complex load scenerio.

## Install
##### requirements
* java jdk 1.8.* must be installed.
* JAVA_HOME is set and it must point to jdk
##### Download the chalba binary
* link: https://github.com/sapandang/chalba/releases/

##### Extract the zip or tar
After extracting the following directory will be created.
```
chalba/
├── bin/
│   ├── chalba --executable for linux
│   └── chalba.bat --executable for windows
├── lib/
    ├── chalba.jar -- used in IDE
    ├── extlib -- put your jars here
    └── contains the libraries
```
##### Set environment to path
Add ***chalba/bin*** to the path of your os.
* In linux to PATH in ~/.profile
* In windows in the environment variable

##### Testing
To test if chalba has been registered in your system. Open terminal or command prompt , type chalba and press enter.
Chalba help menu should be visible

## Getting Started.
Create your working directory and enter the following command
```bash
chalba -newFile
```
It will generate file name ***Task1.java*** in the current directory. Use this file as template to write your scripts.
> Refactor this file name to yours because every time -newFile is executed it will generate file with name Task1.java

Code snippet.
```java
package chalba;


import skd.chalba.common.*;
import skd.chalba.requests.*;
import skd.chalba.runner.*;
import skd.chalba.interfaces.*;

/**
 * This is the template File for writing the load script
 * Please follow the structure as described in this file
 *
 * @author sapan.dang
 */
@ThreadCount(1)
@ThreadSpawnDelay(100)
public class Task1 extends Task {


    // this constructor is required
    public Task1(TaskParams taskParams) {

    }

    //This method is executed after constructor
    //script must implement this method
    @Override
    public void run() {
        super.run();

        //executable method
        //it is good practice to write your code in other method
        mainLoop();
        _testCompleted(); //finish when test complete
    }


    //Main Loop write your logic here
    public void mainLoop() {

        //Write your code in the try-catch block
        //to avoid any unexpected closure of the script
        try {


            //create GET request
            System.out.println("send get request");
            ResponseData googleResponse = requests.get("https://www.google.com/");
            System.out.println("response code " + googleResponse.code);

            //create async request
            requests.get("https://www.google.com/", new AsyncResponseCallback() {
                @Override
                public void onResponse(ResponseData arg0) {
                    System.out.println(" "+arg0.body);
                }
            });


        } catch (Exception e) {
            LOG(e);
        }
    }

}
```
Chalba scripts are plain java file use your favourite IDE to develop the script.
Here the execution flow is like.
```
Constructor() -> run()
```
Running scrtips
```bash
chalba -f Task1.java
```
## Setting up IDE
you can user your favourite to write the scripts just add ***skd.chalba-1.0-SNAPSHOT.jar*** to your IDE

##### Setting up project with Intellij and gradle
* Create java gradle project
* In ***scr -> main -> java***  create ***chalba*** package, now you can use this package as directory to develop your script.
* In ***build.gradle*** add chalba jar as dependency
```java 
//load test dependency  
compile fileTree(dir: "full path to chalba directory/chalba/lib/", includes: ['*.jar'])

```
* To run your script navigate to ***src->main->java->chalba*** directory 
* In this directory open terminal/command prompt and run the script.
```bash
chalba -f FileName.java
```

## Requests
#### Headers
```java
Headers headers = new Headers();  
headers.put("Content-Type", "application/json" );
```
#### Query parameters
```java
QueryParameters queryParameters = new QueryParameters();  
queryParameters.add("cityId","3757,3758")  
        .add("endDate","2019-10-07")                 
        .add("pageNo","0");  

```
#### application/x-www-form-urlencoded form data
```java
FormBody formBody = new FormBody();  
formBody.add("j_username","admni");  
formBody.add("j_password","admin123");  
formBody.add("remember-me","false");  
formBody.add("submit","Login");
```
#### multipart/form-data;
```java
MultiPartFormBody multiPartFormBody = new MultiPartFormBody();  
multiPartFormBody.add("multiPartData","multiData")  
                 .add("data1","data2");
 ```
#### Send form data
For sending the formdata mulipart/x-www-form use ***postFormData*** method
```java
ResponseData responseData = requests.postFormData("https://postb.in/1570770392983-2035075568128",  
        formBody,queryParameters);  
System.out.println(responseData.body);
```
#### Send synchronous GET request
```java
ResponseData responseData = requests.get("https://postb.in/1570770392983-2035075568128",headers,queryParameters);  
System.out.println("testGet "+responseData.body);
```
#### Send synchronous POST request
Posting JSON,XML and other text data.
```java
Headers headers = new Headers();  
headers.put("user-agent","Testagent");  
  
ResponseData responseData = requests.postRaw("https://postb.in/1570772006478-3730544417630",  
        "Hii I am post",  
        headers,  
        ContentType.APPLICATION_JSON);  
System.out.println(responseData.body);
```
#### Send aSynchornous GET
```java
Headers headers = new Headers();  
headers.put("user-agent","Testagent");  
  
  
QueryParameters queryParameters = new QueryParameters();  
queryParameters.add("cityId","3757,3758")  
        .add("endDate","2019-10-07")  
        .add("pageNo","0");  
  
 requests.get("https://postb.in/1570772006478-3730544417630", headers, queryParameters, new AsyncResponseCallback() {  
    @Override  
  public void onResponse(ResponseData response) {  
        System.out.println("testGet "+response.body);  
  
    }  
});
``` 
#### Send aSynchronous POST
```java
Headers headers = new Headers();  
headers.put("user-agent", "Testagent");  
  
requests.postRaw("https://postb.in/1570772006478-3730544417630",  
        "Hii I am post",  
        headers,  
        ContentType.APPLICATION_JSON, new AsyncResponseCallback() {  
            @Override  
  public void onResponse(ResponseData response) {  
  
                System.out.println(response.body);  
  
            }  
        });
```       


