package skd.chalba.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sapan.dang
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadSpawnDelay {
    long value() default 0;

}
