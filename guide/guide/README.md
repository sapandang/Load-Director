---
title: Chalba
sidebarDepth: 2
---

# Introduction

Chalba is open source hackable load testing tool inspired from jmeter,gatling,grinder.

## Installiation

##### requirements
* java jdk 1.8.* must be installed.
* JAVA_HOME is set and it must point to jdk



##### Download the chalba binary
* link: 

##### Extract the zip or tar
After extracting the following directory will be created.
```
chalba/
├── bin/
│   ├── chalba --executable for linux
│   └── chalba.bat --executable for windows
├── lib/
    └── contains the libraries
```
##### Set environment to path
Add ***chalba/bin*** to the path of your os.
* In linux to PATH in ~/.profile
* In windows in the environment variable

##### Testing
To test if chalba has been registered in your system. Open terminal or command prompt , type chalba and press enter.
Chalba help menu should be visible

## Getting Started
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
compile files('full_path_to_jar/skd.chalba-1.0-SNAPSHOT.jar')
```
* To run your script navigate to ***src->main->java->chalba*** directory 
* In this directory open terminal/command prompt and run the script.
```bash
chalba -f FileName.java
```