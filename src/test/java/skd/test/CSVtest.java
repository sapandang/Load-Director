package skd.test;

import org.junit.Test;
import skd.chalba.elements.CsvDataSet;

/**
 * @author sapan.dang
 */
public class CSVtest {

    @Test
    public void Test1()
    {
        //case 1
        CsvDataSet csvDataSet = new CsvDataSet("input/dummycsvdata.csv",false,false);
        System.out.println("false false");
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));


        //case 2
        csvDataSet = new CsvDataSet("input/dummycsvdata.csv",true,false);
        System.out.println("true false");
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));

        //case 3
        csvDataSet = new CsvDataSet("input/dummycsvdata.csv",true,true);
        System.out.println("true true");
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));

        //case 4
        csvDataSet = new CsvDataSet("input/dummycsvdata.csv",false,true);
        System.out.println("false true");
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));
        System.out.println("==>"+csvDataSet.getCsvRecordList(1));

    }

}
