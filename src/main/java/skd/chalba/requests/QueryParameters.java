package skd.chalba.requests;

import org.tinylog.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sapan.dang
 */
public class QueryParameters {

    Map<String, String> encodedRequestParams = new HashMap<>();

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    public QueryParameters add(String key,String value)
    {
        try {
            encodedRequestParams.put(key,encodeValue(value));
        } catch (UnsupportedEncodingException e) {
            Logger.error(e);
            e.printStackTrace();
        }
        return this;
    }

    public String getQueryParameter()
    {
        String query =encodedRequestParams.toString().replaceAll(", ","&").replace("{","").replaceAll("}","");
        return "?"+query;
    }


}
