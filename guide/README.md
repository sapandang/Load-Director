---
title: Chalba
sidebarDepth: 3
---
# Introduction
Chalba is open source hackable load generator born out of need to handle complex load scenrios.
Instead of using custom DSL to code the load script, chalba uses pure java code as the script, with this we can leverage the full power of java  and it's avaliable library to create the scripts.
Chalba api is designed to be simple like python so we can focus on logic insted of syntax.

Like to send a GET request use
```java
ResponseData googleResponse = requests.get("https://www.google.com/");
```
For asyc request use
```java
requests.get("https://www.google.com/", new AsyncResponseCallback() {
                @Override
                public void onResponse(ResponseData response) {
                    System.out.println(" "+response.body);
                }
            });
```
# Why chalba ?
Now the question arise why you should chalba when there are plenty of load generator avaliable.
Answers: It depends on you, every tool was developed to solved a problem. Similary chalba was developed since we need a load generator where scripts can be written in java so we can easily use any library, low resource consuming. don't want some build tool to write our scripts.

# Features
* **Open source**  chalba code is liscensed under MIT.So no worry use as you like.
* **Full power of java** with chalba you can leverage the full power of java.
* **No build tool** as chalba scripts are written in java, however you don't need any complex build tool.
* **low resource usage** chalba itself don't consume much ram and cpu. Since it was written to lightweight
* **easy to use** with simpler syntax chalba is easy to use
* **complex load scenerios** this is the main advantage of chalba since scripts are written in code. You can write complex load scenerios
* **Functional testing** you can also use this to create functional tests
* **CI enabled** you can easily embeed chalba in your CI framework 
