---
sidebarDepth: 2
---

# Api

## Headers
```java
Headers headers = new Headers();  
headers.put("Content-Type", "application/json" );
```
## Query parameters
```java
QueryParameters queryParameters = new QueryParameters();  
queryParameters.add("cityId","3757,3758")  
        .add("endDate","2019-10-07")                 
        .add("pageNo","0");  

```
## application/x-www-form-urlencoded form data
```java
FormBody formBody = new FormBody();  
formBody.add("j_username","admni");  
formBody.add("j_password","admin123");  
formBody.add("remember-me","false");  
formBody.add("submit","Login");
```
## multipart/form-data;
```java
MultiPartFormBody multiPartFormBody = new MultiPartFormBody();  
multiPartFormBody.add("multiPartData","multiData")  
                 .add("data1","data2");
 ```
## Send form data
For sending the formdata mulipart/x-www-form use ***postFormData*** method
```java
ResponseData responseData = requests.postFormData("https://postb.in/1570770392983-2035075568128",  
        formBody,queryParameters);  
System.out.println(responseData.body);
```
## Send synchronous GET request
```java
ResponseData responseData = requests.get("https://postb.in/1570770392983-2035075568128",headers,queryParameters);  
System.out.println("testGet "+responseData.body);
```
## Send synchronous POST request
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
## Send aSynchornous GET
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
## Send aSynchronous POST
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

