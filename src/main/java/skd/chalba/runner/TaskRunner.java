package skd.chalba.runner;

import org.json.JSONObject;
import skd.chalba.common.TaskParams;
//import skd.utils.Config;
import org.tinylog.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import org.tinylog.Logger;

/**
 * @author sapan.dang
 */
public class TaskRunner {

    public Class taskClass;
    public int totalThreads;
    public String taskName;
    public long delayBetweenThreads=0;//not used use directely from config
    public String taskStatus="stop";
    public String taskClassName="";



    ArrayList<Object> threadTrackerArrayList = new ArrayList<>();

    public TaskRunner(Class taskClass, int totalThreads, String taskName, long delayBetweenThreads) {
        this.taskClass = taskClass;
        this.totalThreads = totalThreads;
        this.taskName = taskName;
        this.delayBetweenThreads = delayBetweenThreads;
        this.taskClassName=taskClass.getName();
    }

    public void startTasks() throws Exception {

        //check if task is already running or not
        if(taskStatus.toLowerCase().equals("running"))
        {
            return;
        }

        taskStatus="running";
        //start the threads
        for(int i=0;i<totalThreads;i++)
        {
            //if taskRunner stopped in middle
            if(taskStatus.toLowerCase().equals("stop"))
            {
                return;
            }

            Logger.info("Starting thread "+(i+1));

            TaskParams taskParams = new TaskParams();
            taskParams._ThreadCount=totalThreads;
            taskParams._ThreadIndex=(i+1);
            taskParams._TaskName=taskName;
            taskParams._ThreadSpwanDelay=delayBetweenThreads;

            //spwan 1
           // Constructor<?> tmpClassConstructor = taskClass.getConstructor(int.class, int.class);
           // Object tmpTaskObject = tmpClassConstructor.newInstance(1, 1);


            //TODO : First need to deprecate the above function
            //spwan 2
            Constructor<?> tmpClassConstructor = taskClass.getConstructor(TaskParams.class);
            Object tmpTaskObject = tmpClassConstructor.newInstance(taskParams);

            java.lang.reflect.Method startMethod = tmpTaskObject.getClass().getMethod("start");
            startMethod.invoke(tmpTaskObject);
            threadTrackerArrayList.add(tmpTaskObject);

            Thread.sleep(this.delayBetweenThreads);
            //Thread.sleep(Config.THREAD_SPAWN_DELAY); //HACK UNTIL  THREAD UI MADE
        }
    }

    public void startTasks(int numberOfThread) throws Exception {

        //check if task is already running or not
//        if(taskStatus.toLowerCase().equals("running"))
//        {
//            return;
//        }

        taskStatus="running";
        //start the threads
        for(int i=0;i<numberOfThread;i++)
        {
            //if taskRunner stopped in middle
            if(taskStatus.toLowerCase().equals("stop"))
            {
                return;
            }

            Logger.info("Starting thread "+(i+1));

            TaskParams taskParams = new TaskParams();
            taskParams._ThreadCount=totalThreads;
            taskParams._ThreadIndex=(i+1);
            taskParams._TaskName=taskName;
            taskParams._ThreadSpwanDelay=delayBetweenThreads;

            //spwan 1
            // Constructor<?> tmpClassConstructor = taskClass.getConstructor(int.class, int.class);
            // Object tmpTaskObject = tmpClassConstructor.newInstance(1, 1);


            //TODO : First need to deprecate the above function
            //spwan 2
            Constructor<?> tmpClassConstructor = taskClass.getConstructor(TaskParams.class);
            Object tmpTaskObject = tmpClassConstructor.newInstance(taskParams);

            java.lang.reflect.Method startMethod = tmpTaskObject.getClass().getMethod("start");
            startMethod.invoke(tmpTaskObject);
            threadTrackerArrayList.add(tmpTaskObject);

            Thread.sleep(this.delayBetweenThreads);
            //Thread.sleep(Config.THREAD_SPAWN_DELAY); //HACK UNTIL  THREAD UI MADE
        }
    }


    public int getRunningThreads()
    {
        return  threadTrackerArrayList.size();
    }

    public void resetThreadTrackerArrayList()
    {
        threadTrackerArrayList = new ArrayList<>();
    }

    public void stopTasks()
    {

        for (int i=0;i<threadTrackerArrayList.size();i++)
        {
            Thread th = (Thread) threadTrackerArrayList.get(i);
            th.stop(); //stop the thread
            Logger.info("Stopping task "+(i+1));
        }
        taskStatus="stop";
        resetThreadTrackerArrayList();
    }


    public JSONObject toJsonObject()
    {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totalThreads",""+totalThreads);
            jsonObject.put("taskName",""+taskName);
            jsonObject.put("taskClassName",""+taskClassName);
            jsonObject.put("taskStatus",""+taskStatus);
            jsonObject.put("delayBetweenThreads",""+delayBetweenThreads);
            jsonObject.put("runningThreads",""+getRunningThreads());
            return jsonObject;
    }


}
