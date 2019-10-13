
package skd.chalba.requests;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base Code : https://github.com/riversun/okhttp3-cookie-helper
 *
 * @author sapan.dang
 */

public class CookieHelper {

    private Map<String, List<Cookie>> mServerCookieStore = new HashMap<>();

    private Map<String, List<Cookie>> mClientCookieStore = new HashMap<>();





    private final CookieJar mCookieJar = new CookieJar() {
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {

            List<Cookie> serverCookieList = mServerCookieStore.get(url.host());

            if (serverCookieList == null) {
                serverCookieList = new ArrayList<Cookie>();
            }

            final List<Cookie> clientCookieStore = mClientCookieStore.get(url.host());

            if (clientCookieStore != null) {
                serverCookieList.addAll(clientCookieStore);
            }

            return serverCookieList != null ? serverCookieList : new ArrayList<Cookie>();
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> unmodifiableCookieList) {
            // Why 'new ArrayList<Cookie>'?
            // Since 'unmodifiableCookieList' can not be changed, create a new
            // one

            if (!mServerCookieStore.containsKey(url.host())) {
               mServerCookieStore.put(url.host(), new ArrayList<Cookie>(unmodifiableCookieList));
           }else {

                //cookiee exists need to patch it
                HashMap<String,Cookie> cookiemap = new HashMap<>();

                //add old cookiee to map
                List<Cookie> old_cookiee =  mServerCookieStore.get(url.host());

                for(int i=0;i<unmodifiableCookieList.size();i++)
                {
                  for(int j=0;j<old_cookiee.size();j++)
                  {
                        if(unmodifiableCookieList.get(i).name().equals(old_cookiee.get(j).name()))
                        {
                            //remove from the old one
                            old_cookiee.remove(j);
                            old_cookiee.add(unmodifiableCookieList.get(i));
                        }
                  }

                }

                mServerCookieStore.put(url.host(),old_cookiee);


                //=====================


            }

            //cookiee need to be updated every time
            //accept cookie once from the site
//            if (!mServerCookieStore.containsKey(url.host())) {
//                mServerCookieStore.put(url.host(), new ArrayList<Cookie>(unmodifiableCookieList));
//            }
            // The persistence code should be described here if u want.
        }


    };

    /**
     * Set cookie
     *
     * @param url
     * @param cookie
     */
    public void setCookie(String url, Cookie cookie) {

        final String host = HttpUrl.parse(url).host();

        List<Cookie> cookieListForUrl = mClientCookieStore.get(host);
        if (cookieListForUrl == null) {
            cookieListForUrl = new ArrayList<Cookie>();
            mClientCookieStore.put(host, cookieListForUrl);
        }
        putCookie(cookieListForUrl, cookie);

    }

    /**
     * Set cookie
     *
     * @param url
     * @param cookieName
     * @param cookieValue
     */
    public void setCookie(String url, String cookieName, String cookieValue) {
        final HttpUrl httpUrl = HttpUrl.parse(url);
        setCookie(url, Cookie.parse(httpUrl, cookieName + "=" + cookieValue));
    }

    /**
     * Set cookie
     *
     * @param httpUrl
     * @param cookieName
     * @param cookieValue
     */
    public void setCookie(HttpUrl httpUrl, String cookieName, String cookieValue) {
        setCookie(httpUrl.host(), Cookie.parse(httpUrl, cookieName + "=" + cookieValue));
    }

    /**
     * Returns CookieJar
     *
     * @return
     */
    public CookieJar cookieJar() {
        return mCookieJar;
    }

    private void putCookie(List<Cookie> storedCookieList, Cookie newCookie) {

        Cookie oldCookie = null;
        for (Cookie storedCookie : storedCookieList) {

            // create key for comparison
            final String oldCookieKey = storedCookie.name() + storedCookie.path();
            final String newCookieKey = newCookie.name() + newCookie.path();

            if (oldCookieKey.equals(newCookieKey)) {
                oldCookie = storedCookie;
                break;
            }
        }
        if (oldCookie != null) {
            storedCookieList.remove(oldCookie);
        }
        storedCookieList.add(newCookie);
    }

    public void clearCokiee() {
        mServerCookieStore = new ConcurrentHashMap<String, List<Cookie>>();
        mClientCookieStore = new ConcurrentHashMap<String, List<Cookie>>();
    }



//========================================================================================================
//========================= SET AND DELETE COOKIEE =======================================================
//========================================================================================================
/* cookiee format
    [{"cookiee":["{\"path\":\"/\",\"domain\":\"www.buglens.com\",\"hostOnly\":true,\"name\":\"XSRF-TOKEN\",\"httpOnly\":false,\"secure\":true,\"persistent\":false,\"value\":\"785296bd-2c35-414d-a3ab-7c6375714f47\",\"expiresAt\":253402300799999}","{\"path\":\"/\",\"domain\":\"www.buglens.com\",\"hostOnly\":true,\"name\":\"JSESSIONID\",\"httpOnly\":true,\"secure\":true,\"persistent\":false,\"value\":\"d4ed0049-f0f6-4475-b359-3725e78c891d\",\"expiresAt\":253402300799999}"],"name":"www.buglens.com"}]
*/
//========================================================================================================

    /**
     * This method will set the cookiee from the json
     * @param jsonData
     */
    public void setCookieeFromJson(String jsonData) {

        //first clear the cookiee
        clearCokiee();

        JSONArray root = new JSONArray(jsonData);

        for(int cookieeNodeIndex =0;cookieeNodeIndex<root.length();cookieeNodeIndex++)
        {

            JSONObject cookieeNode  = root.getJSONObject(cookieeNodeIndex);
            String hostName = cookieeNode.getString("name");

            JSONArray cookieeArrayNode = cookieeNode.getJSONArray("cookiee");
            List<Cookie> cookieList = new ArrayList<>();
            for(int cookieeListIndex = 0;cookieeListIndex<cookieeArrayNode.length();cookieeListIndex++)
            {
                cookieList.add(jsonStringToCookiee(cookieeArrayNode.getString(cookieeListIndex)));
            }

          mServerCookieStore.put(hostName,cookieList);

        }

    }

    /**
     * This will return the json representation of the cookiee
     * @return
     * @throws Exception
     */
    public String getCookieeJson() throws Exception {

        JSONArray root = new JSONArray();


        //First store the ServerCookiee
        for(int mServerCookieStoreIndex = 0;mServerCookieStoreIndex<mServerCookieStore.size();mServerCookieStoreIndex++)
        {
            List<Cookie> cok = mServerCookieStore.get(mServerCookieStore.keySet().toArray()[mServerCookieStoreIndex]);
            JSONArray serverCookieeJsonArray = new JSONArray();

            for (int j = 0; j < cok.size(); j++) {
              //  Logger.info("=>" + cookieeToJsonString(cok.get(j)));
                serverCookieeJsonArray.put(cookieeToJsonString(cok.get(j)));
            }

            JSONObject cookieeNode = new JSONObject();
            cookieeNode.put("name",mServerCookieStore.keySet().toArray()[mServerCookieStoreIndex]);
            cookieeNode.put("cookiee",serverCookieeJsonArray);

            root.put(cookieeNode);
        }

        return root.toString();

    }


//========================================================================================================
//============================= COOKIEE SERIALISER DEALSERIAL =======================================================
//========================================================================================================

    private String cookieeToJsonString(Cookie cookie)
    {
        JSONObject root = new JSONObject();

        root.put("name",cookie.name());
        root.put("value",cookie.value());
        root.put("expiresAt",cookie.expiresAt());
        root.put("domain",cookie.domain());
        root.put("path",cookie.path());
        root.put("secure",cookie.secure());
        root.put("httpOnly",cookie.httpOnly());
        root.put("hostOnly",cookie.hostOnly());
        root.put("persistent",cookie.persistent());

        return root.toString();
    }

    private Cookie jsonStringToCookiee(String jsonString)
    {

        JSONObject root = new JSONObject(jsonString);

        String name = (String) root.getString("name");
        String value = (String) root.getString("value");
        long expiresAt = root.getLong("expiresAt");
        String domain = (String) root.getString("domain");
        String path = (String) root.getString("path");
        boolean secure = root.getBoolean("secure");
        boolean httpOnly = root.getBoolean("httpOnly");
        boolean hostOnly = root.getBoolean("hostOnly");
        boolean persistent = root.getBoolean("persistent");
        Cookie.Builder builder = new Cookie.Builder();
        builder = builder.name(name);
        builder = builder.value(value);
        builder = builder.expiresAt(expiresAt);
        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        builder = builder.path(path);
        builder = secure ? builder.secure() : builder;
        builder = httpOnly ? builder.httpOnly() : builder;
        return builder.build();

    }


}
