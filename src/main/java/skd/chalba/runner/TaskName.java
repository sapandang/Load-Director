package skd.chalba.runner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sapan.dang
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskName {

    String value();

}
