package skd.test;


import com.google.gson.Gson;
import okhttp3.Cookie;
import skd.chalba.common.*;
import skd.chalba.requests.*;
import skd.chalba.interfaces.*;

import java.util.List;

/**
 * This is the template File for writing the load script
 * Please follow the structure as described in this file
 *
 * @author sapan.dang
 */
@ThreadCount(1)
@ThreadSpawnDelay(100)
public class CookieeTest extends Task {


    // this constructor is required
    public CookieeTest(TaskParams taskParams) {

    }

    //This method is executed after constructor
    //script must implement this method
    @Override
    public void run() {
        super.run();

        //executable method
        //it is good practice to write your code in other method
        mainLoop();
        //call test completed when your test is finished
        _testCompleted();
    }


    //Main Loop write your logic here
    public void mainLoop() {

        //Write your code in the try-catch block
        //to avoid any unexpected closure of the script
        try {

            System.out.println("Started..");



            String userName = "camp4856_dhlmy101";
            //send request
            Headers headers = new Headers();
            headers.put("Content-Type", "application/json");


            MultiPartFormBody authData = new MultiPartFormBody();
            authData.add("j_username", userName)
                    .add("j_password", "312c2dd0b8bc61d02e5539c650f5911488a844e2f8cecc0b237cd38c8400bff3")
                    .add("remember-me", "false")
                    .add("submit", "Login");

            ResponseData auth = requests.postFormData("https://qa.fareye.co/app/authentication", authData);
            System.out.println("auth " + auth.code);

            if(auth.code != 200)
            {
                System.out.println("AUTH ERROR 200 "+userName);
                return;
            }

            //extract the cookiee
            String token = requests.getCookieeValue("https://qa.fareye.co/app/rest/account", "XSRF-TOKEN");
            System.out.println("token: " + token);

            String cookiex =  requests.getCookieeJson("https://qa.fareye.co/app/rest/account");
            requests.setCookieeFromJson(cookiex);


        } catch (Exception e) {
            LOG(e);
            e.printStackTrace();
        }
    }

}
