package skd.test;

import skd.chalba.common.Task;
import skd.chalba.common.TaskParams;
import skd.chalba.interfaces.AsyncResponseCallback;
import skd.chalba.requests.ResponseData;
import skd.chalba.runner.ThreadCount;
import skd.chalba.runner.ThreadSpawnDelay;


/**
 * @author sapan.dang
 */
@ThreadCount(1)
@ThreadSpawnDelay(1)
public class Script1 extends Task {

    int _ThreadNumber;
    public Script1(TaskParams taskParams)
    {
        _ThreadNumber=taskParams._ThreadIndex;
        System.out.println("Started "+taskParams._ThreadIndex);
        System.out.println("ThreadName "+taskParams._TaskName);
    }

    @Override
    public void run() {
        super.run();

                while (true) {
                    test();
                }


    }

    public void test()
    {
        System.out.println("Thread Started "+_ThreadNumber);

        System.out.println("Sending request ...");

        ResponseData responseData = null;
        try {
         //   responseData = requests.get("https://google.com");
          //  requests.get("https://xwww.yahoo.com");

            requests.get("https://google.com/async", new AsyncResponseCallback() {

                @Override
                public void onResponse(ResponseData arg0) {
                    System.out.println(" ===>"+arg0.code);
                }

            });

          /*  requests.get("http://www.live-graph.org/help/dataPlot.html", new AsyncResponseCallback() {

                @Override
                public void onResponse(ResponseData arg0) {
                    System.out.println(" ===>"+arg0.code);
                }

            });
*/
            System.out.println("ENDED");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR MESSAGEDDDDD");
        }
        System.out.println("....... ");

      // _testCompleted();
    }

//    @Override
//    public void run() {
//        super.run();
//        System.out.println("Started thread x");
//
//    }
//
}
