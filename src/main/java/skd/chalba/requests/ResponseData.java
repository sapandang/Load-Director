package skd.chalba.requests;

import okhttp3.Response;
import org.tinylog.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sapan.kumar
 */
public class ResponseData {

    static String timeFormat= "HH:mm:ss"; //yyyy-MM-dd HH:mm:ss:SSS
    public int code;
    public String url;
    public String body;
    public Response response;
    public long receivedResponseAtMillis;
    public long receivedResponseAtSec;
    public long sentRequestAtMillis;
    public String receivedTimeStamp;
    public String sendTimeStamp;
    public long responseTimeAtMillis;
    public long byteSent=0;
    public long byteReceived=0;
    public String message="";
    private String threadName="";
    public Headers headers;

    public Map<String, List<String>> multiMapHeader;


    protected void setThreadName(String name){

        //need to match the logger thread name with
        //With the executed thread -> So thread count -1

        name=name.replaceAll("Thread-","");
        int threadCount =2;

        //Leads to exception in unit test
        try {
            threadCount = Integer.parseInt(name);
        }catch (Exception e)
        {
            threadCount =2;
            Logger.error(e);
        }



        threadCount/=2; //Comment for the match the thread
        this.threadName="Thread-"+threadCount;


    }

    public String getThreadName(){
        return this.threadName;
    }

    public int getCode() {
        return code;
    }

    protected void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    protected void setBody(String body) {
        this.body = body;
    }

    public Response getResponse() {
        return response;
    }

    protected void setResponse(Response response) {
        this.response = response;
    }

    public long getReceivedResponseAtMillis() {
        return receivedResponseAtMillis;
    }

    protected  void setReceivedResponseAtMillis(long receivedResponseAtMillis) {
        this.receivedResponseAtMillis = receivedResponseAtMillis;
        this.receivedTimeStamp = new SimpleDateFormat(timeFormat).format(new Date(this.receivedResponseAtMillis));

    }
    protected void setResponseTimeAtMillis(long responseTimeAtMillis) {
        this.responseTimeAtMillis = responseTimeAtMillis;

    }


    public long getReceivedResponseAtSec() {
        return receivedResponseAtSec;
    }

    protected  void setReceivedResponseAtSec(long receivedResponseAtSec) {
        this.receivedResponseAtSec = receivedResponseAtSec;
    }

    public long getSentRequestAtMillis() {
        return sentRequestAtMillis;
    }

    protected  void setSentRequestAtMillis(long sentRequestAtMillis) {
        this.sentRequestAtMillis = sentRequestAtMillis;
        this.sendTimeStamp = new SimpleDateFormat(timeFormat).format(new Date(this.sentRequestAtMillis));

    }

    public String getReceivedTimeStamp() {
        return receivedTimeStamp;
    }

    protected  void setReceivedTimeStamp(String receivedTimeStamp) {
        this.receivedTimeStamp = receivedTimeStamp;
    }

    public String getSendTimeStamp() {
        return sendTimeStamp;
    }

    protected  void setSendTimeStamp(String sendTimeStamp) {
        this.sendTimeStamp = sendTimeStamp;
    }

    public long getResponseTimeAtMillis() {
        return responseTimeAtMillis;
    }



    public ResponseData() {

    }

    public ResponseData(Response response) {
        parseResponse(response);
    }

    //Parse the Response
    protected void parseResponse(Response response){
        try {
            this.response = response;
            this.code = response.code();
            this.url = response.networkResponse().request().url().toString();
            try {
                this.body = response.body().string();
            } catch (Exception e) {
                this.body = "HAHAHA got IOexception while parsing the body " + e.getMessage();

            }
            this.receivedResponseAtMillis = response.receivedResponseAtMillis();
            this.receivedResponseAtSec = (int) ((receivedResponseAtMillis / 1000) % 60);
            this.sentRequestAtMillis = response.sentRequestAtMillis();
            this.receivedTimeStamp = new SimpleDateFormat(timeFormat).format(new Date(this.receivedResponseAtMillis));
            this.sendTimeStamp = new SimpleDateFormat(timeFormat).format(new Date(this.sentRequestAtMillis));
            this.responseTimeAtMillis = this.receivedResponseAtMillis - this.sentRequestAtMillis;
            this.message=response.message();
            //TODO need to implement headers
            multiMapHeader = response.headers().toMultimap();

        } catch (Exception e) {
            Logger.error(e);

        }finally {
            this.response.close();
        }

    }


    //Parse the Request
    protected void parseRequest(okhttp3.Request request)
    {
        this.url = request.url().toString();

    }

    //HACK method to be called when error occurs and
    // okHttp response return null
    protected void calculateResponseTime()
    {
        this.responseTimeAtMillis = this.receivedResponseAtMillis - this.sentRequestAtMillis;

    }

}
