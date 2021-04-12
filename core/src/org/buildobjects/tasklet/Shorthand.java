package org.buildobjects.tasklet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 6:23:06 PM
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Shorthand {
    String value();
}
