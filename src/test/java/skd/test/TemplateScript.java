package skd.test;

import org.tinylog.Logger;
import skd.chalba.common.*;
import skd.chalba.requests.*;
import skd.chalba.runner.*;

/**
 * This is the template File for writing the load script
 * Please follow the structure as described in this file
 *
 * @author sapan.dang
 */
@ThreadCount(1)
@ThreadSpawnDelay(100)
public class TemplateScript extends Task1 {


    // this constructor is required
    public TemplateScript(TaskParams taskParams) {
        super(taskParams);
    }

    //This method is executed after constructor
    //script must implement this method
    @Override
    public void run() {
        super.run();

        //executable method
        //it is good practice to write your code in other method
        mainLoop();
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


        } catch (Exception e) {
            Logger.error(e);
        }
    }

}
