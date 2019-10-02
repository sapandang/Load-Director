package skd.chalba.common;

import okhttp3.*;
import org.tinylog.ThreadContext;
import skd.chalba.interfaces.AsyncResponseCallback;
import skd.chalba.interfaces.RequestsListners;
import skd.chalba.listners.ResponseLogger;
import skd.chalba.requests.Requests;
import skd.chalba.requests.ResponseData;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CyclicBarrier;

/**
 * @author sapan.kumar
 */
public  class Task extends Thread {

   // public static final Logger LOG = LogManager.getLogger(Task.class);



    //public  int _ThreadNumber,_ThreadCount,_ThreadIteration;
    public  CyclicBarrier gate;
    public String _CurTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    public String _CurDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


    public OkHttpClient client;
    public Requests requests;

    public Task()
    {
        requests = new Requests();
        client = requests.getClient();
        requests.addRequestListners(new ResponseLogger());
        requests.threadName=this.getName();
    }

    /*public Task(int _ThreadNumber, int _ThreadCount, CyclicBarrier gate)
    {
        this._ThreadNumber=_ThreadNumber;
        this._ThreadCount=_ThreadCount;
        this.gate = gate;
    }

    public void setClient(OkHttpClient client)
    {
        this.client=client;
    }*/


    public void _testCompleted()
    {
        requests.closeDispatcher();
    }


}
