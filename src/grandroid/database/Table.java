/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package grandroid.database;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Rovers
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value() default "app";//value=""
}
