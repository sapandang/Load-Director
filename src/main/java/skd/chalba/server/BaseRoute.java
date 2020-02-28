package skd.chalba.server;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author sapan.dang
 */
public class BaseRoute {


    public Route get()
    {
        return new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                return null;
            }
        };
    }

}
