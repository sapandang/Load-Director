package skd.chalba.interfaces;

import okhttp3.Call;
import skd.chalba.requests.ResponseData;

import java.io.IOException;

/**
 * @author sapan.kumar
 */
public interface AsyncResponseCallback {

    void onResponse(ResponseData response);


}
