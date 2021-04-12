package org.buildobjects.tasklet.metamodel;

import java.lang.reflect.Method;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 2:48:59 PM
 */
public class ActionInfo {
    String name;
    Method method;
    private final String description;

    public ActionInfo(String name, Method method, String description) {
        this.name = name;
        this.method = method;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }
}
