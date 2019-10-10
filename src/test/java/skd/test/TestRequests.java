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

    @Test
    public void testGet() throws Exception
    {

        ResponseData responseData = requests.get("http://www.google.com");
        System.out.println("testGet "+responseData.body);

    }

    @Test
    public void testPost() throws  Exception
    {
        Headers headers = new Headers();
        headers.put("user-agent","Testagent");

        ResponseData responseData = requests.postRaw("https://qatest.fareye.co/requestdump/tesr",
                "Hii I am post",
                headers,
                ContentType.APPLICATION_JSON);
        System.out.println(responseData.body);

    }


    @Test
    public void testPostForm() throws Exception {


        FormBody formBody = new FormBody();
        formBody.add("j_username","dhlmy72_admin");
        formBody.add("j_password","Fareye@123");
        formBody.add("remember-me","false");
        formBody.add("submit","Login");
        formBody.addEncoded("encoded"," ris is encoded");

        ResponseData responseData = requests.postFormData("https://postb.in/1568266949766-0641507897526",
                formBody);
        System.out.println(responseData.body);


    }

    @Test
    public void testMultipartText() throws Exception {

        MultiPartFormBody multiPartFormBody = new MultiPartFormBody();
        multiPartFormBody.add("this is the file","fileData")
                .add("file2","file2");

        ResponseData responseData = requests.postFormData("https://postb.in/1568266949766-0641507897526",
                multiPartFormBody);
        System.out.println(responseData.body);
    }

    @Test
    public void testMultiPartFileUpload() throws Exception{

        MultiPartFormBody multiPartFormBody = new MultiPartFormBody();
        multiPartFormBody.add("file","fileName",new File("input/chalba.txt"));
        ResponseData responseData = requests.postFormData("https://postb.in/1568266949766-0641507897526",
                multiPartFormBody);
        System.out.println(responseData.body);
    }

    @Test
    public void testPostFormWithoutBody() throws Exception
    {
        ResponseData responseData = requests.postFormData("https://postb.in/1568261939218-9800930477213");
        System.out.println(responseData.body);
    }


    @Test
    public void testSequence() throws Exception
    {

        try {
            FormBody formBody = new FormBody();
            formBody.add("j_username", "dhlmy72_admin");
            formBody.add("j_password", "312c2dd0b8bc61d02e5539c650f5911488a844e2f8cecc0b237cd38c8400bff3");
            formBody.add("remember-me", "false");
            formBody.add("submit", "Login");


            ResponseData responseData = requests.postFormData("https://qa.fareye.co/app/authentication",
                    formBody);
            System.out.println(responseData.body);


            //send to accout
            ResponseData account = requests.get("https://qa.fareye.co/app/rest/account");
            System.out.println("account " + account.body);



        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    @Test
    public void testTime()
    {
        System.out.println("time "+System.currentTimeMillis());
        String receivedTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        System.out.println(receivedTimeStamp);
    }

    @Test
    public void testAsyncGet()
    {

        requests.get("https://google.com", new AsyncResponseCallback() {

            @Override
            public void onResponse(ResponseData arg0) {
                System.out.println(" "+arg0.body);
            }

        });
    }


    @Test
    public void testCookiee() throws Exception
    {
        requests = new Requests();
        //send request
        Headers headers = new Headers();
        headers.put("Content-Type", "application/json" );


        MultiPartFormBody authData = new MultiPartFormBody();
        authData.add("j_username", "dhlmy79_admin")
                .add("j_password", "312c2dd0b8bc61d02e5539c650f5911488a844e2f8cecc0b237cd38c8400bff3")
                .add("remember-me", "false")
                .add("submit", "Login");

        ResponseData auth = requests.postFormData("https://qa.fareye.co/app/authentication",authData);
        System.out.println("auth "+auth.code);

        String token = requests.getCookieeValue("https://qa.fareye.co/","XSRF-TOKEN");
        System.out.println("token: auth ==> "+token);

      //  System.out.println(requests.getCookieHelper().getCookieeJson());
        System.out.println(" authHeaderMap: "+auth.multiMapHeader.get("set-cookie"));


        //account
        ResponseData account =  requests.get("https://qa.fareye.co/app/rest/account");
        System.out.println("account "+account.code+"  -  " +account.body);

        //extract the cookiee

         token = requests.getCookieeValue("https://qa.fareye.co/","XSRF-TOKEN");
        System.out.println("token: account ==> "+token);
        headers.put("X-XSRF-TOKEN",token);


      //  System.out.println(requests.getCookieHelper().getCookieeJson());
        System.out.println(" accountHeaderMap:  "+account.multiMapHeader.get("set-cookie"));

        ResponseData assignData = requests.postRaw("https://qa.fareye.co/app/rest/order/assign_job","{}",headers);
        System.out.println(""+assignData.body);

        JSONObject jsonObject = new JSONObject(assignData.body);
        if (jsonObject.getInt("successCount") == 1 ) {
            System.out.println("response " + assignData.code +" status:PASS");
        }else {
            System.out.println("response " + assignData.code +" status:FAIL "+assignData.body);

        }

    }


}
