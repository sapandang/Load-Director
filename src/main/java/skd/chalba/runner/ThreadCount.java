package skd.chalba.runner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Set the thread count for the task
 * @author sapan.dang
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadCount {

    int value() default 1;
    String fromCsvWithHeaders() default "";
    String fromCsvWithoutHeaders() default "";
}
