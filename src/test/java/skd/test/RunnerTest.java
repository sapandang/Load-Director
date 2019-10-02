package skd.test;

import org.junit.Test;
import skd.chalba.LoadDirector;
import skd.chalba.requests.QueryParameters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertThat;

/**
 * @author sapan.dang
 */
public class RunnerTest {

    @Test
    public void startLoad() throws Exception
    {
//        System.out.println("load 1 start");
//        LoadDirector.registerTask(Script1.class);
//        LoadDirector.startTask(Script1.class.getName());

    }



    public static void main(String[] arg)
    {
        System.out.println("load 1 start");
        LoadDirector.registerTask(Script1.class);
        LoadDirector.startTask(Script1.class.getName());

    }


    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    @Test
    public void givenRequestParam_whenUTF8Scheme_thenEncode() throws Exception {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.add("key1", "value 1")
        .add("key2", "value@!$2")
        .add("key3", "value%3");

        System.out.println(queryParameters.getQueryParameter());

    }

}
