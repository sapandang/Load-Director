package  chalba;


import skd.chalba.common.Task;
import skd.chalba.runner.TaskName;
import skd.chalba.common.TaskParams;
import skd.chalba.runner.ThreadCount;
import skd.chalba.runner.ThreadSpawnDelay;

/**
 * @author sapan.dang
 */

@ThreadCount(40)
@ThreadSpawnDelay(100)
public class File2 extends Task {




    public File2(TaskParams taskParams)
    {
        System.out.println("Started "+taskParams._ThreadIndex);
        System.out.println("ThreadName "+taskParams._TaskName);
    }

    @Override
    public void run() {
        super.run();
        System.out.println("Started thread x");

    }



}
