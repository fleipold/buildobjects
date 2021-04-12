package org.buildobjects.tasklet.metamodel;

import org.apache.commons.lang.StringUtils;
import org.buildobjects.tasklet.Description;
import org.buildobjects.util.MixedUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 2:54:17 PM
 */
public class TaskletReflector {

    private final Class taskletClass;
    
    private List<ParameterInfo> parameters;
    private TaskInfo taskletInfo;

    public TaskletReflector(Class taskletClass) {
        this.taskletClass = taskletClass;

        reflectParameters();

        this.taskletInfo = new TaskInfo(taskletClass.getName(), parameters, reflectActions());
    }

    private void reflectParameters()   {
        parameters = new ArrayList<ParameterInfo>();
        Method[] methods = taskletClass.getMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 1 & method.getName().startsWith("set")) {
                checkParameter(method);
            }
        }
    }


    private List<ActionInfo> reflectActions() {
        List<ActionInfo> actions = new ArrayList<ActionInfo>();

        Method[] methods = taskletClass.getMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 0
                    && !method.getName().startsWith("get")
                    && !method.getName().startsWith("set")
                    && !method.getName().startsWith("is")
                    && (method.getDeclaringClass() == taskletClass)
                && Modifier.isPublic(method.getModifiers())) {

                String description = method.getName();
                Description descriptionAnnotation = method.getAnnotation(Description.class);
                if (descriptionAnnotation != null) {
                    description = descriptionAnnotation.value();
                }

                actions.add(new ActionInfo(method.getName(), method, description));
            }
        }
        return actions;
    }


    private void checkParameter(Method method)  {
        String name = StringUtils.uncapitalize(method.getName().substring(3));
        final Class<?> type = method.getParameterTypes()[0];
        String valueSet = null;
        if (Enum.class.isAssignableFrom(type)){
                valueSet = StringUtils.join(MixedUtils.enumValues((Class<? extends Enum>) type),"|");
        }

        String description = name;

        Description descriptionAnnotation = method.getAnnotation(Description.class);
        if (descriptionAnnotation != null) {
            description = descriptionAnnotation.value();
        }

        if (valueSet!=null){
            description = valueSet + " " + description;
        }
        ParameterInfo info = new ParameterInfo(type, name, method, description);
        parameters.add(info);
    }


    public TaskInfo getTaskletInfo() {
        return taskletInfo;
    }

}
