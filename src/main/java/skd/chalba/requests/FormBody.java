package skd.chalba.requests;

import java.util.HashMap;

/**
 * @author sapan.dang
 */
public class FormBody {

    HashMap<String,String> data = new HashMap<>();
    HashMap<String,String> encodedData = new HashMap<>();

    public FormBody add(String key,String value)
    {

        data.put(key,value);
        return this;
    }


    public FormBody addEncoded(String key,String value)
    {

        encodedData.put(key,value);
        return this;
    }

    public  HashMap<String,String> getData()
    {
        return data;
    }

    public  HashMap<String,String> getEncodedData()
    {
        return encodedData;
    }


}
