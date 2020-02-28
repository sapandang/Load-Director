package skd.test;

import skd.chalba.LoadDirector;
import skd.chalba.common.Task;
import skd.chalba.common.TaskParams;
import skd.chalba.interfaces.LoadProp;
import skd.chalba.interfaces.ThreadCount;
import skd.chalba.interfaces.ThreadSpawnDelay;
import skd.chalba.requests.ResponseData;

/**
 * @author sapan.dang
 */

@LoadProp("input/PropReader.prop")
@ThreadCount(fromProp = "THREAD_COUNT")
@ThreadSpawnDelay(100)
public class Task2 extends Task {




    public Task2(TaskParams taskParams)
    {
        System.out.println("Started "+taskParams._ThreadIndex);
        System.out.println("ThreadName "+taskParams._TaskName);

    }

    @Override
    public void run() {
        super.run();
        System.out.println("Started thread...");

        ResponseData res =  requests.get("http://google.com");
        if(res!=null)
        {
            System.out.println("ResponseCode " +res.code);
        }

    }

    public static void main(String[] args) throws Exception {

        skd.chalba.LoadDirector loadDirector = new LoadDirector();
        loadDirector.registerTask(Task2.class);
        loadDirector.startTask("skd.test.Task2");

    }



}