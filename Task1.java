package chalba;


import org.sqlite.jdbc4.*;

import skd.chalba.common.*;
import skd.chalba.requests.*;
import skd.chalba.runner.*;


/**
 * @author sapan.dang
 */

@ThreadCount(1)
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

        while (true) {
            ResponseData res = requests.get("https://qa.fareye.co");
            if (res != null) {
                System.out.println("ResponseCode " + res.code);
            }
        }
    }



}
