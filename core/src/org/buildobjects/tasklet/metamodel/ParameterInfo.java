package org.buildobjects.tasklet.metamodel;

import java.lang.reflect.Method;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 2:49:07 PM
 */
public class ParameterInfo {
    Class type;
    String name;
    Method setter;
    private final String description;

    public ParameterInfo(Class type, String name, Method setter, String description) {
        this.type = type;
        this.name = name;
        this.setter = setter;
        this.description = description;
    }


    public Class getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Method getSetter() {
        return setter;
    }

    public String getDescription() {
        return description;
    }
}
