package skd.test;

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
    public void testwords()
    {

        String statement = "hi hoy hydy hozx";

        int count=0;
        char backup=' ';
        for(int i=0;i<statement.length();i++)
        {

            if(statement.charAt(i)== 'z' || statement.charAt(i)== 'y' )
            {
                if(i<statement.length()-1)
                {
                    if(statement.charAt(i+1)==' ')
                    {
                        count++;
                    }
                }else
                {
                    count++;
                }
            }
        }

        System.out.println("words are "+count);


    }
}
