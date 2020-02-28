package skd.test;

import okhttp3.*;

import java.io.IOException;

/**
 * @author sapan.dang
 */
public class NetworkTest {

    public static void main(String[] args) throws IOException {


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"entry.1689263360\"\r\n\r\n 1234567\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"fvv\"\r\n\r\n 1\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"fbzx\"\r\n\r\n 4195642247604962669\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url("https://docs.google.com/forms/u/0/d/e/1FAIpQLSdgH0CUXpeLWXVPgHevsANiw-yfD7Bv7f8F-GdtdhPeYZyuOg/formResponse")
                .post(body)
                .addHeader("authority", "authority: docs.google.com")
                .addHeader("method", "method: POST")
                .addHeader("path", "path: /forms/u/0/d/e/1FAIpQLSdgH0CUXpeLWXVPgHevsANiw-yfD7Bv7f8F-GdtdhPeYZyuOg/formResponse")
                .addHeader("scheme", "scheme: https")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
                .addHeader("cache-control", "max-age=0,no-cache")
                .addHeader("content-length", "133")
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("origin", "https://docs.google.com")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                .addHeader("Postman-Token", "fafc2d89-5381-469c-96d9-33bec90cacf8,bb530c1d-d5e9-42d4-af3c-06de169e295c")
                .addHeader("Host", "docs.google.com")
               // .addHeader("Cookie", "S=spreadsheet_forms=EFcdy0WwBWeapqMSEAAY3E8gUerQ_V9L; NID=194=dFYKn5Vq-d-NySssce_BgrBSJExP8_nQ_fSpUU7plYCWnfFP6d2rgx7PQe93tEX89EAOzYov965OTX8vXdxmj4RVeKI0lqcjsSc1D6HYTfl_wA3GU0VxkdCA41AIVtlHlLgSvk5QSAMNhxioBsenh4QD6veviAkLpC8O0M4BNj8")
                .addHeader("Connection", "keep-alive")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().toString());
        System.out.println(response.code());

    }
}
