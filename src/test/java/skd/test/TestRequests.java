package skd.test;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Test;
import skd.chalba.interfaces.AsyncResponseCallback;
import skd.chalba.requests.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sapan.dang
 */
public class TestRequests {

    Requests requests = new Requests();
    String HOOK_URL = "https://postb.in/1570772006478-3730544417630";

    @Test
    public void testGet() throws Exception {
        Headers headers = new Headers();
        headers.put("user-agent", "Testagent");


        QueryParameters queryParameters = new QueryParameters();
        queryParameters.add("cityId", "3757,3758")
                .add("endDate", "2019-10-07")
                .add("pageNo", "0");

        ResponseData responseData = requests.get(HOOK_URL, headers, queryParameters);
        System.out.println("testGet " + responseData.body);

    }

    @Test
    public void AsynctestGet() throws Exception {
        Headers headers = new Headers();
        headers.put("user-agent", "Testagent");


        QueryParameters queryParameters = new QueryParameters();
        queryParameters.add("cityId", "3757,3758")
                .add("endDate", "2019-10-07")
                .add("pageNo", "0");

        requests.get(HOOK_URL, headers, queryParameters, new AsyncResponseCallback() {
            @Override
            public void onResponse(ResponseData response) {
                System.out.println("testGet " + response.body);

            }
        });

        Thread.sleep(500000); //hack for jnit

    }


    @Test
    public void testPost() throws Exception {

        Headers headers = new Headers();
        headers.put("user-agent", "Testagent");

        ResponseData responseData = requests.postRaw("https://postb.in/1570772006478-3730544417630",
                "Hii I am post",
                headers,
                ContentType.APPLICATION_JSON);
        System.out.println(responseData.body);

    }

    @Test
    public void AsynctestPost() throws Exception {

        Headers headers = new Headers();
        headers.put("user-agent", "Testagent");

        requests.postRaw("https://postb.in/1570772006478-3730544417630",
                "Hii I am post",
                headers,
                ContentType.APPLICATION_JSON, new AsyncResponseCallback() {
                    @Override
                    public void onResponse(ResponseData response) {

                        System.out.println(response.body);

                    }
                });

        Thread.sleep(130000);
    }

    @Test
    public void testPostForm() throws Exception {

        QueryParameters queryParameters = new QueryParameters();
        queryParameters.add("cityId", "3757,3758")
                .add("endDate", "2019-10-07")
                .add("pageNo", "0");


        FormBody formBody = new FormBody();
        formBody.add("j_username", "admni");
        formBody.add("j_password", "admin123");
        formBody.add("remember-me", "false");
        formBody.add("submit", "Login");
        formBody.addEncoded("encoded", " ris is encoded");

        ResponseData responseData = requests.postFormData("https://postb.in/1570770392983-2035075568128",
                formBody, queryParameters);
        System.out.println(responseData.body);


    }

    @Test
    public void testMultipartText() throws Exception {

        MultiPartFormBody multiPartFormBody = new MultiPartFormBody();
        multiPartFormBody.add("multiPartData", "multiData")
                .add("data1", "data2");

        ResponseData responseData = requests.postFormData("https://postb.in/1570770392983-2035075568128",
                multiPartFormBody);
        System.out.println(responseData.body);
    }

    @Test
    public void testMultiPartFileUpload() throws Exception {

        MultiPartFormBody multiPartFormBody = new MultiPartFormBody();
        multiPartFormBody.add("file", "fileName", new File("input/chalba.txt"));
        ResponseData responseData = requests.postFormData("https://postb.in/1568266949766-0641507897526",
                multiPartFormBody);
        System.out.println(responseData.body);
    }

    @Test
    public void testPostFormWithoutBody() throws Exception {
        ResponseData responseData = requests.postFormData("https://postb.in/1568261939218-9800930477213");
        System.out.println(responseData.body);
    }


    @Test
    public void testTime() {
        System.out.println("time " + System.currentTimeMillis());
        String receivedTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        System.out.println(receivedTimeStamp);
    }

    @Test
    public void testAsyncGet() {

        requests.get("https://google.com", new AsyncResponseCallback() {

            @Override
            public void onResponse(ResponseData arg0) {
                System.out.println(" " + arg0.body);
            }

        });
    }


}
