package skd.test;

import okhttp3.*;
import org.junit.Test;

import java.io.IOException;

/**
 * @author sapan.dang
 */
public class NetworkLibraryTest {

    private final OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                    client.dispatcher().executorService().shutdown();

                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        NetworkLibraryTest networkLibraryTest = new NetworkLibraryTest();
        networkLibraryTest.run();
    }


    @Test
    public void urlBuilder()
    {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")

                .addQueryParameter("q", "polar bears")
                .build();
        System.out.println(url);
    }

}
