package skd.test;

import skd.chalba.LoadDirector;

/**
 * @author sapan.dang
 */
public class DebugRunner {

    public static void main(String[] args) throws Exception {
        System.out.println("Debug started..");


        LoadDirector loadDirector = new LoadDirector();
        loadDirector.registerTask(CookieeTest.class);
        loadDirector.startTask(CookieeTest.class);



    }
}
