package skd.chalba.requests;

/**
 * @author sapan.dang
 */
public class ContentType {


    private String currentContentType="";

    public static ContentType APPLICATION_JSON = new ContentType("application/json");
    public static ContentType TEXT_PLAIN = new ContentType("text/plain");


    public ContentType(String type)
    {
        this.currentContentType=type;
    }


    public void setContentType(String type)
    {
        currentContentType=type;
    }

    public String getContentType()
    {
        return currentContentType;
    }




}
