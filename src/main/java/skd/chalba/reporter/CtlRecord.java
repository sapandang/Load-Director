package skd.chalba.reporter;

import org.apache.commons.csv.CSVRecord;

/**
 * Ctl file record model
 * @author sapan.dang
 */
public class CtlRecord {

  public   String ThreadName,StatusMessage,RequestURL;
  public   int ResponseCode;
  public   long RequestSentAtMillis,ResponseReceivedAtMillis,ResponseReceivedTime,
          ResponseSendTime,ResponseTimeAtMillis;


    public CtlRecord(CSVRecord csvRecord){

        ThreadName = csvRecord.get("ThreadName");
        StatusMessage = csvRecord.get("StatusMessage");
        RequestURL = csvRecord.get("RequestURL");

        ResponseCode = Integer.parseInt(csvRecord.get("ResponseCode"));

        RequestSentAtMillis = Long.parseLong(csvRecord.get("RequestSentAtMillis"));
        ResponseReceivedAtMillis = Long.parseLong(csvRecord.get("ResponseReceivedAtMillis"));
        ResponseReceivedTime = Long.parseLong(csvRecord.get("ResponseReceivedTime"));
        ResponseSendTime = Long.parseLong(csvRecord.get("ResponseSendTime"));
        ResponseTimeAtMillis = Long.parseLong(csvRecord.get("ResponseTimeAtMillis"));

    }

}
