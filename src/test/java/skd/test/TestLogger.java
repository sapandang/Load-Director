package skd.test;

import org.tinylog.Logger;

/**
 * @author sapan.dang
 */
public class TestLogger {

    public static void main(String[] a)
    {
        System.out.println("Logger started");
        Logger.tag("SYSTEMx").debug("hii");
        Logger.info("Byee");
    }
}
