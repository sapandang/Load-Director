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
                    "ResponseReceivedTime"+"|"+
                    "ResponseSendTime"+"|"+
                    "ResponseTimeAtMillis");
            //header printed
            logOnce=true;
        }



        long startTime = System.currentTimeMillis();

        Logger.tag("RESPONSE").debug(response.getThreadName()+"|"+
                response.code+"|"+
                response.message+"|"+
                response.url+"|"+
                response.sentRequestAtMillis+"|"+
                response.receivedResponseAtMillis+"|"+
                response.receivedTimeStamp+"|"+
                response.sendTimeStamp+"|"+
                response.responseTimeAtMillis);

        //System.out.println("Time Taken to write in file -> "+(System.currentTimeMillis()-startTime ));

    }
}
