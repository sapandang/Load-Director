package skd.chalba.reporter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.tinylog.Logger;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class will parse the response.ctl file
 *
 * @author sapan.dang
 */
public class ResponseCtlParser {


    //store each csv list for compute : use RequestURL as the unique map
    public HashMap<String, Metric> summarymap = new HashMap<>();

    public void readResponseFile(String fileName) {

        try {
            Logger.info("reading file " + fileName);
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.newFormat('|').withHeader().withIgnoreSurroundingSpaces());

            List<CSVRecord> csvRecordList = csvParser.getRecords();

            //parse each records
            for (int recordCounter = 0; recordCounter < csvRecordList.size(); recordCounter++) {
                CSVRecord tmpCsvRecord = csvRecordList.get(recordCounter);
                CtlRecord ctlRecord = new CtlRecord(tmpCsvRecord);
                calculateMetric(ctlRecord);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void calculateMetric(CtlRecord ctlRecord)
    {
        if(!summarymap.containsKey(ctlRecord.RequestURL))
        {
            summarymap.put(ctlRecord.RequestURL,new Metric());
        }
        //now the map must containt the URL
        Metric metric = summarymap.get(ctlRecord.RequestURL);
        metric.totalSamples+=1;
        metric.totalResponseTime+=ctlRecord.ResponseTimeAtMillis;

        //set maxResponse
        if(ctlRecord.ResponseTimeAtMillis > metric.maxReponseTime)
        {
            metric.maxReponseTime=ctlRecord.ResponseTimeAtMillis;
        }
        //set minResponse
        if(metric.minResponseTime==0)
        {
            metric.minResponseTime=ctlRecord.ResponseTimeAtMillis;
        }
        if(ctlRecord.ResponseTimeAtMillis < metric.minResponseTime)
        {
            metric.minResponseTime=ctlRecord.ResponseTimeAtMillis;
        }

        //set average response time
        try {
            metric.averageReponseTime = metric.totalResponseTime / metric.totalSamples;
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if(ctlRecord.ResponseCode!=200)
        {
            metric.totalErrorSamples+=1;
        }

        //setup the total response codes
        if(!metric.responseCodemaps.containsKey(ctlRecord.ResponseCode))
        {
            metric.responseCodemaps.put(ctlRecord.ResponseCode,1);
        }else {

            int count = metric.responseCodemaps.get(ctlRecord.ResponseCode);
            metric.responseCodemaps.put(ctlRecord.ResponseCode,count+1);
        }

        //calculate percentile



    }



    class Metric {

        ArrayList<Long> responseTimeArrayList = new ArrayList<Long>();

        long totalResponseTime=0;
        long maxReponseTime, minResponseTime, averageReponseTime=0;
        long totalSamples = 0, totalErrorSamples = 0, percentile90, percentile95, percentile99;
        HashMap<Integer, Integer> responseCodemaps = new HashMap<>();

        public void addResponseList(long responseTime)
        {
            responseTimeArrayList.add(responseTime);

        }


        @Override
        public String toString() {
            return "totalResponseTime: "+totalResponseTime+
                    " maxReponseTime: "+maxReponseTime+
                    " minResponseTime: "+minResponseTime+
                    " averageReponseTime: "+averageReponseTime+
                    " totalSamples: "+totalSamples+
                    " totalErrorSamples: "+totalErrorSamples+
                    " responsecodes "+responseCodemaps;

        }
    }


    public static void main(String[] args) {
        System.out.println("Testing ReportGenerator");
        ResponseCtlParser responseCtlParser = new ResponseCtlParser();
        responseCtlParser.readResponseFile("/home/sapan/Documents/workspace/javaProject/chalba/input/records.csv");
        System.out.println(responseCtlParser.summarymap);

    }

}
