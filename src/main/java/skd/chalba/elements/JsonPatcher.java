package skd.chalba.elements;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author sapan.dang
 */
public class JsonPatcher {

    JSONObject sourceLogJsonObject;

    public JSONObject getJsonObject()
    {
        return sourceLogJsonObject;
    }
    public JsonPatcher(String josnString) {
        sourceLogJsonObject = new JSONObject(josnString);
    }


    public JSONObject patchStringNode(String nodeName, String _nodeValue) {
       return patchStringObjectNode(sourceLogJsonObject,  nodeName,  _nodeValue);
    }

    private JSONObject patchStringObjectNode(JSONObject jsonObject, String nodeName, String _nodeValue) {
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String keyName = keys.next();
            Object node = jsonObject.get(keyName);

            if (node instanceof JSONObject) {
                //System.out.println("node is object " + keyName);
                patchStringObjectNode((JSONObject) node, nodeName, _nodeValue);
            } else if (node instanceof JSONArray) {
                patchStringArrayNode(((JSONArray) node), nodeName, _nodeValue);
                //System.out.println("node is arrary " + keyName);
            } else {
              //  System.out.println("node is key " + keyName);
                if (keyName.equals(nodeName)) {
                   // System.out.println("patching key " + keyName);
                    jsonObject.put("" + keyName, _nodeValue);
                }
            }


        }


        return jsonObject;
    }

    private JSONArray patchStringArrayNode(JSONArray jsonArray, String nodeName, String _nodeValue) {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object node = jsonArray.get(i);


            if (node instanceof JSONObject) {
              //  System.out.println("node is object ");
                patchStringObjectNode((JSONObject) node, nodeName, _nodeValue);
            } else if (node instanceof JSONArray) {

                patchStringArrayNode(((JSONArray) node), nodeName, _nodeValue);

            }

        }

        return jsonArray;
    }





 //============================================================================================================
 //======================================= Long Patcher =======================================================
 //============================================================================================================
    public JSONObject patchLongNode(String nodeName, long nodeValue) {
        return patchLongObjectNode(sourceLogJsonObject, nodeName, nodeValue);
    }


    private JSONObject patchLongObjectNode(JSONObject jsonObject, String nodeName, long _nodeValue) {
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String keyName = keys.next();
            Object node = jsonObject.get(keyName);

            if (node instanceof JSONObject) {
              //  System.out.println("node is object " + keyName);
                patchLongObjectNode((JSONObject) node, nodeName, _nodeValue);
            } else if (node instanceof JSONArray) {
                patchLongArrayNode(((JSONArray) node), nodeName, _nodeValue);
               // System.out.println("node is arrary " + keyName);
            } else {
               // System.out.println("node is key " + keyName);
                if (keyName.equals(nodeName)) {
                 //   System.out.println("patching key " + keyName);
                    jsonObject.put("" + keyName, _nodeValue);
                }
            }


        }

        return jsonObject;
    }

    private JSONArray patchLongArrayNode(JSONArray jsonArray, String nodeName, long _nodeValue) {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object node = jsonArray.get(i);


            if (node instanceof JSONObject) {
                //System.out.println("node is object ");
                patchLongObjectNode((JSONObject) node, nodeName, _nodeValue);
            } else if (node instanceof JSONArray) {

                patchLongArrayNode(((JSONArray) node), nodeName, _nodeValue);

            }

        }

        return jsonArray;
    }


//======================================================================================================================
//==================== Double Patcher ==================================================================================
//======================================================================================================================
public JSONObject patchDoubleNode(String nodeName, double nodeValue) {
    return patchDoubleObjectNode(sourceLogJsonObject, nodeName, nodeValue);
}


    private JSONObject patchDoubleObjectNode(JSONObject jsonObject, String nodeName, double _nodeValue) {
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String keyName = keys.next();
            Object node = jsonObject.get(keyName);

            if (node instanceof JSONObject) {
                //  System.out.println("node is object " + keyName);
                patchDoubleObjectNode((JSONObject) node, nodeName, _nodeValue);
            } else if (node instanceof JSONArray) {
                patchDoubleArrayNode(((JSONArray) node), nodeName, _nodeValue);
                // System.out.println("node is arrary " + keyName);
            } else {
                // System.out.println("node is key " + keyName);
                if (keyName.equals(nodeName)) {
                    //   System.out.println("patching key " + keyName);
                    jsonObject.put("" + keyName, _nodeValue);
                }
            }


        }

        return jsonObject;
    }



    private JSONArray patchDoubleArrayNode(JSONArray jsonArray, String nodeName, double _nodeValue) {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object node = jsonArray.get(i);


            if (node instanceof JSONObject) {
                //System.out.println("node is object ");
                patchDoubleObjectNode((JSONObject) node, nodeName, _nodeValue);
            } else if (node instanceof JSONArray) {

                patchDoubleArrayNode(((JSONArray) node), nodeName, _nodeValue);

            }

        }

        return jsonArray;
    }



    public static void main(String[] ar) throws IOException {
        String tmp_deliverySuccessJson = FileUtils.readFileToString(new File("input/testJson.json"));
        JsonPatcher jsonPatcher = new JsonPatcher(tmp_deliverySuccessJson);
        //jsonPatcher.patchStringNode("as","");
        jsonPatcher.patchLongNode("userId", 213);
        jsonPatcher.patchLongNode("cityId", 213);
        jsonPatcher.patchLongNode("hubId", 213);
        JSONObject a = jsonPatcher.patchStringNode("hubCode", "sapamn");

       // System.out.println("==>" + a.toString());


    }
}
