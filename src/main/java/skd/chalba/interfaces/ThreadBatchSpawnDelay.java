package skd.chalba.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sapan.dang
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadBatchSpawnDelay {

    long threadBatchSize() default 0;
    long delay() default 0;

}
