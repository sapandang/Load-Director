package skd.test;

import skd.chalba.LoadDirector;

/**
 * @author sapan.dang
 */
public class DebugRunner {

    public static void main(String[] args) {
        System.out.println("Debug started..");
        LoadDirector.registerTask(Task1.class);
        String result =   LoadDirector.startTask("skd.test.Task1");


    }
}
