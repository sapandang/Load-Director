package skd.chalba.requests;

import java.io.File;
import java.util.HashMap;

/**
 * @author sapan.dang
 */
public class MultiPartFormBody {


   private HashMap<String,String> textData = new HashMap<>();
   private HashMap<String,FileStruct> fileData = new HashMap<>();

    public MultiPartFormBody add(String key,String value)
    {
            textData.put(key,value);
            return this;
    }

    public HashMap<String,String> getTextData()
    {
        return textData;
    }

    public MultiPartFormBody add(String key,String fileName,File file)
    {
        FileStruct fileStruct = new FileStruct();
        fileStruct.key=key;
        fileStruct.fileName=fileName;
        fileStruct.file=file;
        fileData.put(key,fileStruct);
        return this;
    }


    public HashMap<String,FileStruct> getFileData()
    {
        return fileData;
    }



    class FileStruct{

        public String key;
        public String fileName;
        public File file;

    }


}
