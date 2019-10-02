package skd.test;

import org.junit.Test;
import skd.chalba.LoadDirector;

/**
 * @author sapan.dang
 */
public class RunnerTest {

    @Test
    public void startLoad() throws Exception
    {
//        System.out.println("load 1 start");
//        LoadDirector.registerTask(Script1.class);
//        LoadDirector.startTask(Script1.class.getName());

    }



    public static void main(String[] arg)
    {
        System.out.println("load 1 start");
        LoadDirector.registerTask(Script1.class);
        LoadDirector.startTask(Script1.class.getName());

    }
}
