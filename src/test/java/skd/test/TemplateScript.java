package skd.test;


import skd.chalba.common.*;
import skd.chalba.requests.*;
import skd.chalba.interfaces.*;

/**
 * This is the template File for writing the load script
 * Please follow the structure as described in this file
 *
 * @author sapan.dang
 */
@ThreadCount(1)
@ThreadSpawnDelay(100)
public class TemplateScript extends Task {


    // this constructor is required
    public TemplateScript(TaskParams taskParams) {

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


            //add headers
            Headers headers = new Headers();
            headers.put("Content-Type", "application/json" );

            //add query parameters
            QueryParameters queryParameters = new QueryParameters();
            queryParameters.add("api_key","wzYz38mncxW4Q7AW3dr86X6qqsDzoBK8");

            //multiPart body
            MultiPartFormBody authData = new MultiPartFormBody();
            authData.add("j_username", "admin")
                    .add("j_password", "312c2dd0b8bc61d02e5539c650f5911488a844e2f8cecc0b237cd38c8400bff3")
                    .add("remember-me", "false")
                    .add("submit", "Login");


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
