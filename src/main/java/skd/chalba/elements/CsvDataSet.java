package skd.chalba.elements;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.tinylog.Logger;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sapan.kumar
 */
public class CsvDataSet {

    String fileName="";
    List<CSVRecord> csvRecords;
    int totalRecords;
    public int rowCounter=0;

    //CONFIG PARAMS
    boolean SKIP_HEADERS=false;
    boolean RECYCLE_DATA=false;



    /**
     * Constructor 2
     * @param fileName
     * @param skipHeader
     * @param recycleData
     */
    public CsvDataSet(String fileName, boolean skipHeader, boolean recycleData )
    {

        this.SKIP_HEADERS=skipHeader;
        this.RECYCLE_DATA=recycleData;

        //no more need since with CSVFormat.newFormat(',').withHeader() it skips the data
        /*if(skipHeader)
        {
            rowCounter=1;
        }*/
        this.fileName=fileName;
        try {
            Logger.info("reading file "+fileName);
            Reader reader = Files.newBufferedReader(Paths.get(this.fileName));


            //handle the headers
            CSVParser csvParser;
            if(skipHeader) {
                csvParser = new CSVParser(reader, CSVFormat.newFormat(',').withHeader());
            }else {
                csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            }
            csvRecords = csvParser.getRecords();
            totalRecords = csvRecords.size();

            Logger.info("completed reading file "+fileName+"record count "+totalRecords);
        }catch (Exception e)
        {
            Logger.info("error reading file "+fileName);
            e.printStackTrace();
        }

    }




   /* public synchronized ArrayList<CSVRecord> getCsvRecordList(int batchSize)
    {
        ArrayList<CSVRecord> records = new ArrayList<>();
        if(rowCounter<totalRecords)
        {
            for(int i=0;i<batchSize;i++)
            {
                records.add(csvRecords.get(rowCounter));
                rowCounter++;
            }
        }else {

            //if recycle is enabled
            if(RECYCLE_DATA)
            {
                if(rowCounter>=totalRecords)
                {
                    if(SKIP_HEADERS)
                    {

                        rowCounter=1;
                        records.add(csvRecords.get(rowCounter));
                        rowCounter++;
                    }else {
                        rowCounter=0;
                        records.add(csvRecords.get(rowCounter));
                        rowCounter++;
                    }
                }
            }else {
                return null;
            }
        }

        return records;
    }
*/


    public synchronized ArrayList<Map<String,String>> getCsvRecordList(int batchSize)
    {
        ArrayList<Map<String,String>>  recordsMapList = new ArrayList<>();
        try{

            //loop trough the csvRecords to get the data
            for(int rowIndex=0;rowIndex<batchSize;rowIndex++){

                //check the rowCounter
                if(rowCounter>=totalRecords)
                {

                    //rowCounter has reached the end. so set it again
                    if(RECYCLE_DATA)
                    {
                        rowCounter=0;
//                        if(SKIP_HEADERS)
//                        {
//                            rowCounter=1;
//                        }else
//                        {
//                            rowCounter=0;
//                        }
                    }else {
                        break;
                    }

                }

                //get the data
                Map<String,String> tmpmap = new HashMap<>();
                //get the number of cloumns

                for(int coulmnIndex=0;coulmnIndex<csvRecords.get(rowCounter).size();coulmnIndex++)
                {
                    tmpmap.put(""+coulmnIndex,csvRecords.get(rowCounter).get(coulmnIndex));
                }
                recordsMapList.add(tmpmap);
                rowCounter++;

            }
            return recordsMapList;
        }catch (Exception e)
        {
            //Not needed exception
            Logger.error(e);
        }
        return null;
    }


    public String getFileName() {
        return fileName;
    }

    public List<CSVRecord> getAllCsvRecords() {
        return csvRecords;
    }

    public int getTotalRecords() {
        return totalRecords;
    }


}
