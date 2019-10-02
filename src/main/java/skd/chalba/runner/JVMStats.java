package skd.chalba.runner;

import org.json.JSONObject;
import org.tinylog.Logger;

/**
 * @author sapan.dang
 */
public class JVMStats {

    public static void main(String[] a)
    {
        getJVMStats();
    }

    public static JSONObject getJVMStats()
    {
        JSONObject jsonObject = new JSONObject();
        int mb = 1024*1024;

        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        jsonObject.put("usedMemory",""+(runtime.totalMemory() - runtime.freeMemory()) / mb);
        //Print used memory
        Logger.info("Used Memory:"
                + (runtime.totalMemory() - runtime.freeMemory()) / mb);

        jsonObject.put("freeMemory",""+(runtime.freeMemory() / mb));
        //Print free memory
        Logger.info("Free Memory:"
                + runtime.freeMemory() / mb);

        jsonObject.put("totalMemory",""+(runtime.totalMemory() / mb));
        //Print total available memory
        Logger.info("Total Memory:" + runtime.totalMemory() / mb);

        jsonObject.put("maxMemory",""+(runtime.maxMemory() / mb));
        //Print Maximum available memory
        Logger.info("Max Memory:" + runtime.maxMemory() / mb);


        return jsonObject;
    }

}
