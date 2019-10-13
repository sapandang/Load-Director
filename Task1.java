package chalba;


import org.sqlite.jdbc4.*;

import skd.chalba.common.*;
import skd.chalba.requests.*;
import skd.chalba.runner.*;
import skd.chalba.interfaces.*;



/**
 * @author sapan.dang
 */

@ThreadCount(10)
@ThreadSpawnDelay(100)
public class Task1 extends Task {




    public Task1(TaskParams taskParams)
    {
        System.out.println("Started "+taskParams._ThreadIndex);
        System.out.println("ThreadName "+taskParams._TaskName);

    }

    @Override
    public void run() {
        super.run();
        System.out.println("Started thread...");


//            ResponseData res = requests.get("https://www.buglens.com");
//            if (res != null) {
//                System.out.println("ResponseCode " + res.code);
//            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            requests.get("https://www.google.com", new AsyncResponseCallback() {

                @Override
                public void onResponse(ResponseData arg0) {
                    System.out.println(" "+arg0.code);
                }

            });


    }



}
