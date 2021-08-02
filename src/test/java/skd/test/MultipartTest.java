package skd.test;

import okhttp3.*;
import skd.chalba.common.Task;
import skd.chalba.common.TaskParams;
import skd.chalba.interfaces.ThreadCount;
import skd.chalba.interfaces.ThreadSpawnDelay;
import skd.chalba.requests.Headers;
import skd.chalba.requests.MultiPartFormBody;
import skd.chalba.requests.ResponseData;

import java.io.File;

@ThreadCount(1)
@ThreadSpawnDelay(100)
public class MultipartTest extends Task {

    public static String postdata = "{\"message\":\"Test Message\",\"type\":\"mobi\",\"priority\":\"LOW\",\"email\":{\"to_email_ids\":[\"vinod.kumar1@getfareye.com\"],\"cc_email_ids\":[],\"bcc_email_ids\":[],\"from_email_id\":\"automation@getfareye.com\",\"subject\":\"subject - test email\",\"attachment\":[]}}";

    // this constructor is required
    public MultipartTest(TaskParams taskParams) {

    }

    //This method is executed after constructor
    //script must implement this method
    @Override
    public void run() {
        super.run();

        //executable method
        //it is good practice to write your code in other method
        mainLoop();
        //call test completed when your test is finished
        _testCompleted();
    }


    //Main Loop write your logic here
    public void mainLoop() {

        //Write your code in the try-catch block
        //to avoid any unexpected closure of the script
        try {


            MultiPartFormBody multiPartFormBody = new MultiPartFormBody();
            multiPartFormBody.add("email_data","application/json",postdata);
            multiPartFormBody.add("attachment","a.txt","application/json",new File("/home/sapan/Fareye/Jmeter/nginxtest/a.txt"));


            Headers headers = new Headers();
            headers.put("Content-Type","application/json");
            headers.put("client","mobi");

            ResponseData responseData =  requests.postFormData("https://qa-load-notification-service.fareye-cloud.ml//rest/v1/companies/22/notifications/email-with-attachment?content_type=static",multiPartFormBody,headers);
            System.out.println(responseData.code +" -> "+responseData.body);




            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("email_data", null,
                            RequestBody.create(MediaType.parse("application/json"), "{\"message\":\"Test Message\",\"type\":\"mobi\",\"priority\":\"LOW\",\"email\":{\"to_email_ids\":[\"vinod.kumar1@getfareye.com\"],\"cc_email_ids\":[],\"bcc_email_ids\":[],\"from_email_id\":\"automation@getfareye.com\",\"subject\":\"subject - test email\",\"attachment\":[]}}".getBytes()))
                    .addFormDataPart("attachment","dbquery.txt",
                            RequestBody.create(MediaType.parse("application/json"),
                                    new File("/home/sapan/Documents/dbquery.txt")))
                    .build();
            Request request = new Request.Builder()
                    .url("https://qa-load-notification-service.fareye-cloud.ml/rest/v1/companies/20/notifications/email-with-attachment?content_type=static")
                    .method("POST", body)
                    .addHeader("client", "mobi")
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println("--> "+response.body().string());



        } catch (Exception e) {
            LOG(e);
            e.printStackTrace();
        }
    }

}
