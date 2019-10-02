package skd.chalba.requests;

import okhttp3.*;
import okhttp3.FormBody;
import org.tinylog.Logger;
import skd.chalba.interfaces.AsyncResponseCallback;
import skd.chalba.interfaces.RequestsListners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author sapan.dang
 */
public class Requests {

    private OkHttpClient client;
    private  CookieHelper cookieHelper;
    private ArrayList<RequestsListners> mRequestsListnersArrayList = new ArrayList();
    public String  threadName="";

    public Requests() {
       /* //set the client
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Dispatcher customDispatcher = new Dispatcher(executorService);
*/

         cookieHelper = new CookieHelper();
        client = new OkHttpClient().newBuilder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .cookieJar(cookieHelper.cookieJar())

                .build();
        client.dispatcher().setMaxRequests(Integer.MAX_VALUE);
        client.dispatcher().setMaxRequestsPerHost(Integer.MAX_VALUE);

    }




    public void setReadTimeout(long timeout)
    {
        client = client.newBuilder()
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();
    }

    public void setConnectTimeout(long timeout)
    {
        client = client.newBuilder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();
    }

    public void closeDispatcher()
    {
        client.dispatcher().executorService().shutdown();
    }


    public CookieHelper getCookieHelper()
    {
        return cookieHelper;
    }

    public void setCookieeHelper(CookieHelper cookieeHelper)
    {
        client = client.newBuilder()
                .cookieJar(cookieeHelper.cookieJar())
                .build();
    }

    public CookieJar getCookieeJar()
    {
      return client.cookieJar();
    }

    public OkHttpClient getClient()
    {
        return client;
    }

    public void setClient(OkHttpClient client)
    {
        this.client = client;
    }





//===========================================================================================
//============ REQUEST SEND LIBRARY =========================================================
//===========================================================================================

    public ResponseData get(String URL, Object... objects)  {
        Request.Builder reqBuilder = new Request.Builder().url(URL).get();
        reqBuilder = parseObjects(reqBuilder, objects);

        Request request = reqBuilder.build();

        ResponseData responseData = null;
        try {

                //check if request is or async
                AsyncResponseCallback asyncResponseCallback = isAsyncRequest(objects);
                if(asyncResponseCallback==null) {
                    //send sync Response
                    responseData = sendRequest(request);
                }else {
                    //request is async
                    sendAsyncRequest(request,asyncResponseCallback);

                }


        } catch (Exception e) {
            Logger.error(e);
        }
        return responseData;
    }

    public ResponseData postRaw(String URL, String body, Object... objects)  {

        RequestBody requestBody = RequestBody.create(null, body);

        Request.Builder requestBuilder = new Request.Builder().url(URL);
        requestBuilder.post(requestBody);
        requestBuilder = parseObjects(requestBuilder, objects);

        Request request = requestBuilder.build();

        ResponseData responseData = null;
        try {
            responseData = sendRequest(request);
        } catch (Exception e) {
            Logger.error(e);
        }

        return responseData;
    }

    public ResponseData postFormData(String URL, Object... objects)  {

        RequestBody requestBody = null;

        //Parse the FormBody i.e : skd.chalba.requests.FormBody formBody;
        for (Object obj : objects) {

            //Create x-www-form-urlEncode request
            if (obj instanceof skd.chalba.requests.FormBody) {

                skd.chalba.requests.FormBody formBody = (skd.chalba.requests.FormBody) obj;
                okhttp3.FormBody.Builder tformBody = new FormBody.Builder();

                //get the data
                HashMap<String, String> data = formBody.getData();
                for (int i = 0; i < data.size(); i++) {

                    tformBody.add(data.keySet().toArray()[i].toString(),
                            data.get(data.keySet().toArray()[i].toString())
                    );
                }

                //get the encoded data
                HashMap<String, String> encodedData = formBody.getEncodedData();
                for (int i = 0; i < encodedData.size(); i++) {

                    tformBody.addEncoded(encodedData.keySet().toArray()[i].toString(),
                            encodedData.get(encodedData.keySet().toArray()[i].toString())
                    );
                }

                requestBody = tformBody.build();
            }

            //Create multipart form data
            if( obj instanceof skd.chalba.requests.MultiPartFormBody)
            {
                skd.chalba.requests.MultiPartFormBody multiPartFormBody = (skd.chalba.requests.MultiPartFormBody) obj;
                MultipartBody.Builder tmultiFormBodyBuilder = new MultipartBody.Builder();
                tmultiFormBodyBuilder.setType(MultipartBody.FORM);

                //parse the text data
                HashMap<String, String> textData = multiPartFormBody.getTextData();
                for (int i = 0; i < textData.size(); i++) {


                    tmultiFormBodyBuilder.addFormDataPart(textData.keySet().toArray()[i].toString(),
                            textData.get(textData.keySet().toArray()[i].toString())
                    );
                }
                //parse the file data
                HashMap<String, MultiPartFormBody.FileStruct> fileData = multiPartFormBody.getFileData();
                for (int i = 0; i < fileData.size(); i++) {

                    //file strut
                    MultiPartFormBody.FileStruct fileStruct = fileData.get(fileData.keySet().toArray()[i].toString());

                    tmultiFormBodyBuilder.addFormDataPart(fileStruct.key,fileStruct.fileName,
                            RequestBody.create(null, fileStruct.file) );
                }

                requestBody = tmultiFormBodyBuilder.build();

            }


        }

        Request.Builder requestBuilder = new Request.Builder().url(URL);
        if (requestBody != null) {
            requestBuilder.post(requestBody);
        }
        requestBuilder = parseObjects(requestBuilder, objects);

        Request request = requestBuilder.build();

        ResponseData responseData = null;
        try {
            responseData = sendRequest(request);
        } catch (Exception e) {
            Logger.error(e);
        }

        return responseData;
    }


    public ResponseData postfile(String URL , String key, String fileName, File file) throws Exception
    {
        MultiPartFormBody multiPartFormBody = new MultiPartFormBody();
        multiPartFormBody.add(key,fileName,file);
        return postFormData(URL,
                multiPartFormBody);

    }


//====================================================================================
//======================= PARSER =====================================================
//====================================================================================

    /**
     * This will parse the request builder objects
     *
     * @param reqBuilder
     * @param obj
     * @return
     */
    public Request.Builder parseObjects(Request.Builder reqBuilder, Object... obj) {

        //Parse the objects
        for (Object object : obj) {
            //headers
            if (object instanceof Headers) {

                Headers headers = (Headers) object;
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    reqBuilder.addHeader(key, value);
                }
            }
            //content type
            if (object instanceof ContentType) {
                ContentType contentType = (ContentType) object;
                reqBuilder.addHeader("Content-Type", contentType.getContentType());

            }


        }

        return reqBuilder;
    }

    public AsyncResponseCallback isAsyncRequest(Object... obj){
        for (Object object : obj) {

            if (object instanceof AsyncResponseCallback)
            {
                return (AsyncResponseCallback) object;
            }

        }
        return null;
    }

//=======================================================================================
//=========================== Listners ==================================================
//=======================================================================================
public void addRequestListners(RequestsListners requestsListners)
{
    mRequestsListnersArrayList.add(requestsListners);
}

public void publishToRequestListners(ResponseData responseData)
{
    for(int listnerIndex=0;listnerIndex<mRequestsListnersArrayList.size();listnerIndex++)
    {
        RequestsListners tRequestsListners  = mRequestsListnersArrayList.get(listnerIndex);
        tRequestsListners.onResponse(responseData);

    }
}




//========================================================================================
//==================== REQUEST SENDER ====================================================
//========================================================================================



    /**
     * Send Request and get the response
     *
     * @param request
     * @return
     * @throws Exception
     */
    public ResponseData sendRequest(okhttp3.Request request) throws Exception {
        ResponseData responseData= new ResponseData();
        responseData.setThreadName(threadName);
        responseData.parseRequest(request);
        Response response = null;
        try {

            responseData.setSentRequestAtMillis(System.currentTimeMillis());
            response = client.newCall(request).execute();
            responseData.parseResponse(response);
            //response.close(); //->moved to finally


        } catch (Exception e) {
            //if error
            if(responseData!= null) {
                responseData.code = 0;
                responseData.message=e.getMessage();
                responseData.setReceivedResponseAtMillis(System.currentTimeMillis());
                responseData.calculateResponseTime();
            }
            Logger.error(e);
            throw e;
        } finally {

            publishToRequestListners(responseData);

            if (response != null) {
                response.close();
            }
        }
        return responseData;
    }


    /**
     * send the async Response
     * TODO need to fix the wait when all the request has finished execution
     *  * it has been renamed to sendAsyncRequest2 as a backup
     * @param request
     * @param asyncResponseCallback
     */
    private void sendAsyncRequest(okhttp3.Request request, AsyncResponseCallback asyncResponseCallback) {



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                //if error
                ResponseData tResponseData = new ResponseData();
                tResponseData.setThreadName(threadName);
                tResponseData.parseRequest(call.request());
                tResponseData.message=e.getMessage();
                tResponseData.code=0;
                asyncResponseCallback.onResponse(tResponseData);
                publishToRequestListners(tResponseData);

                //TODO need to check the impcact of the executorservice
                //https://github.com/square/okhttp/issues/1739
             //   client.dispatcher().executorService().shutdown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseData tResponseData = new ResponseData(response);
                tResponseData.setThreadName(threadName);
                asyncResponseCallback.onResponse(tResponseData);
                publishToRequestListners(tResponseData);


                //TODO need to check the impcact of the executorservice
                //https://github.com/square/okhttp/issues/1739
               // client.dispatcher().executorService().shutdown();


            }
        });

    }

    /**
     * New version for the async requests
     * @param request
     * @param asyncResponseCallback
     */
    public void sendAsyncRequest2(okhttp3.Request request, AsyncResponseCallback asyncResponseCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                ResponseData responseData= new ResponseData();
                responseData.setThreadName(threadName);
                responseData.parseRequest(request);
                Response response = null;
                try {

                    responseData.setSentRequestAtMillis(System.currentTimeMillis());
                    response = client.newCall(request).execute();
                    responseData.parseResponse(response);
                    //response.close(); //->moved to finally


                } catch (Exception e) {
                    //if error
                    if(responseData!= null) {
                        responseData.code = 0;
                        responseData.message=e.getMessage();
                        responseData.setReceivedResponseAtMillis(System.currentTimeMillis());
                        responseData.calculateResponseTime();
                    }
                    Logger.error(e);

                } finally {

                    publishToRequestListners(responseData);
                    asyncResponseCallback.onResponse(responseData);
                    if (response != null) {
                        response.close();
                    }
                }


            }
        }).start();

    }



}
