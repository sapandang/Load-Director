package skd.chalba.common;

/**
 * @author sapan.dang
 */
public class ClassParams {

    public Class taskClass;
    public String taskName;
    public long delayBetweenThreads=0;//not used use directely from config
    public String taskStatus="stop";
    public String taskClassName="";

    public int totalThreads=0;
    public long runDuration=0;


}
