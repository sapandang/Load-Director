package skd.chalba.listners;

import org.tinylog.Logger;
import skd.chalba.interfaces.RequestsListners;
import skd.chalba.requests.ResponseData;

/**
 * @author sapan.dang
 */
public class ResponseLogger implements RequestsListners {

    static boolean logOnce=false;
    @Override
    public void onResponse(ResponseData response) {



        if(!logOnce)
        {
            //print the headers
            Logger.tag("RESPONSE").debug("ThreadName"+"|"+
                    "ResponseCode"+"|"+
                    "StatusMessage"+"|"+
                    "RequestURL"+"|"+
                    "RequestSentAtMillis"+"|"+
                    "ResponseReceivedAtMillis"+"|"+
                    "ResponseSendTime"+"|"+
                    "ResponseReceivedTime"+"|"+
                    "ResponseTimeAtMillis");
            //header printed

            Logger.tag("RESPONSE2").debug("timeStamp," +
                    "elapsed," +
                    "label," +
                    "responseCode," +
                    "responseMessage," +
                    "threadName," +
                    "dataType," +
                    "success," +
                    "failureMessage," +
                    "bytes,sentBytes,grpThreads,allThreads,Latency,IdleTime,Connect");

            //new Headers


            logOnce=true;
        }



        long startTime = System.currentTimeMillis();

        Logger.tag("RESPONSE").debug(response.getThreadName()+"|"+
                response.code+"|"+
                response.message+"|"+
                response.url+"|"+
                response.sentRequestAtMillis+"|"+
                response.receivedResponseAtMillis+"|"+
                response.sendTimeStamp+"|"+
                response.receivedTimeStamp+"|"+
                response.responseTimeAtMillis);

        try{


            String label = escapeSpecialCharacters(response.url.split("/")[(response.url.split("/").length - 1)]);
            String success = "true";
            String failureMessage = "";
            if(response.code!=200)
            {
                success = "false";
                failureMessage= escapeSpecialCharacters(response.body);
            }
            Logger.tag("RESPONSE2").debug(response.receivedResponseAtMillis+","
                    +response.responseTimeAtMillis+","
                    +label+","+
                    response.code+","+
                    response.message+","+
                    response.getThreadName()+","+
                    "text,"+
                    success+","+
                    failureMessage+",0,0,0,0,"+
                    response.responseTimeAtMillis+",0,0"
                    );



        }catch (Exception e)
        {
            Logger.error("logger error");
        }


        //System.out.println("Time Taken to write in file -> "+(System.currentTimeMillis()-startTime ));

    }


    public static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
