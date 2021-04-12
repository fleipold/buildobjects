package org.buildobjects.tasklet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 5:10:35 PM
 */

@Retention(value = RetentionPolicy.RUNTIME)
public @interface Description {
    String value();
}
