package org.buildobjects.artifacts.resources;

import org.apache.commons.collections15.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 8:51:35 PM
 */
public class FilteringResources implements Resources{
    final Predicate<Path> predicate;
    final Resources subject;

    public FilteringResources(Predicate<Path> predicate, Resources subject) {
        this.predicate = predicate;
        this.subject = subject;
    }


    public boolean hasResource(Path path) {
        if (!predicate.evaluate(path)){
            return false;
        }
        return subject.hasResource(path);
    }

    public Resource getResource(Path path) {

        if (!predicate.evaluate(path)){
            return null;
        }
        return subject.getResource(path);
    }

    public Collection<Resource> getAll() {
        Collection<Resource> returnValue = new LinkedList<Resource>();
        for (Resource resource : subject.getAll()){
            if (predicate.evaluate(resource.getPath())){
                returnValue.add(resource);
            }
        }
        return returnValue;
    }



    public Collection<Resource> getResourcesInPath(Path path) {
        Collection<Resource> returnValue = new ArrayList<Resource>();
                for (Resource resource : subject.getResourcesInPath(path)){
                    if (predicate.evaluate(resource.getPath())){
                        returnValue.add(resource);
                    }
                }
        return returnValue;
    }

    public Collection<Resource> getResourcesInPathRescursively(Path path) {
        Collection<Resource> returnValue = new ArrayList<Resource>();
                for (Resource resource : subject.getResourcesInPathRescursively(path)){
                    if (predicate.evaluate(resource.getPath())){
                        returnValue.add(resource);
                    }
                }
        return returnValue;
    }
}
