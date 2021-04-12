package org.buildobjects.artifacts.resources;

import java.util.*;
import static java.util.Arrays.asList;

/**
 * User: fleipold
 * Date: Nov 2, 2008
 * Time: 8:02:19 PM
 */
public class ResourcesCombiner implements Resources {
    final Set<Resources> resourcesSet;

       public ResourcesCombiner(Collection<? extends Resources> combinedResources) {
           this.resourcesSet = new HashSet<Resources>(combinedResources.size()*2);
           for (Resources resources : combinedResources) {
               if (resources instanceof ResourcesCombiner) {
                   ResourcesCombiner classesCombiner = (ResourcesCombiner) resources;
                   this.resourcesSet.addAll(classesCombiner.getCombinedResources());
               } else {
                   resourcesSet.add(resources);
               }

           }

       }

    


    public Set<Resources> getCombinedResources() {
           return resourcesSet;
       }

       public ResourcesCombiner(Resources... classesArray) {
           this(new HashSet<Resources>(asList(classesArray)));
       }

    public boolean hasResource(Path path) {
        for (Iterator<Resources> resourcesIterator = resourcesSet.iterator(); resourcesIterator.hasNext();) {
            Resources resources = resourcesIterator.next();
            if (resources.hasResource(path)){
                return true;
            }
        }
        return false;
    }


    public Resource getResource(Path path) {
        for (Resources resources : resourcesSet) {
            Resource resource = resources.getResource(path);
            if (resource != null)
                return resource;
        }
        return null;
    }

    public Collection<Resource> getAll() {
        Collection<Resource> returnValue = new ArrayList<Resource>();
                for (Resources resources : resourcesSet){
                        returnValue.addAll(resources.getAll());
                }
        return returnValue;
    }

    public Collection<Resource> getResourcesInPath(Path path) {
        Collection<Resource> returnValue = new ArrayList<Resource>();
                for (Resources resources : resourcesSet){
                        returnValue.addAll(resources.getResourcesInPath(path));
                }
        return returnValue;
    }

    public Collection<Resource> getResourcesInPathRescursively(Path path) {
        Collection<Resource> returnValue = new ArrayList<Resource>();
                for (Resources resources : resourcesSet){
                        returnValue.addAll(resources.getResourcesInPathRescursively(path));
                }
        return returnValue;
    }
}
