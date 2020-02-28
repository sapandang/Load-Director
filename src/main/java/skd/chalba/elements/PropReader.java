package skd.chalba.elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author sapan.dang
 */
public class PropReader {

    String propFile;
    Properties props = new Properties();


    public PropReader(String propFile) throws Exception
    {
        this.propFile=propFile;
        loadConfigFile();
    }


    public void loadConfigFile() throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(propFile);
            props.load(fis);
        } catch (Exception e) {
           throw e;
        }
    }

    public String getProperty(String key) {
        try {
            return props.getProperty(key);
        } catch (Exception e) {
            return null;
        }
    }


}
