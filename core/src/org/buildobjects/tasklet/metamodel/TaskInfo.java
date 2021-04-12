package org.buildobjects.tasklet.metamodel;

import java.util.List;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 7:47:38 PM
 */
public class TaskInfo {
    final String name;
    final List<ParameterInfo> parameters;
    final List<ActionInfo> actions;

    public TaskInfo(String name, List<ParameterInfo> parameters, List<ActionInfo> actions) {
        this.name = name;
        this.parameters = parameters;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    public List<ActionInfo> getActions() {
        return actions;
    }
}
