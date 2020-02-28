package skd.chalba.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Load the property file
 * @author sapan.dang
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadProp {

    String value() default "";

}
