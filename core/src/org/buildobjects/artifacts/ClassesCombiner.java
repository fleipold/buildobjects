package org.buildobjects.artifacts;


import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.buildobjects.artifacts.resources.Resources;
import org.buildobjects.artifacts.resources.ResourcesCombiner;

import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: fleipold
 * Date: Oct 12, 2008
 * Time: 11:41:12 AM
 */
public class ClassesCombiner implements Classes {
    private final Set<Classes> classesSet;
    final private Resources resources;

    public ClassesCombiner(Collection<? extends Classes> combinedClasses) {
        this.classesSet = new HashSet<Classes>(combinedClasses.size()*2);
        resources = new ResourcesCombiner(CollectionUtils.collect(combinedClasses, new Transformer<Classes, Resources>() {
            public Resources transform(Classes o) {
                return o.getResources();
            }
        }));
        for (Classes classes : combinedClasses) {
            if (classes instanceof ClassesCombiner) {
                ClassesCombiner classesCombiner = (ClassesCombiner) classes;
                this.classesSet.addAll(classesCombiner.getCombinedClasses());
            } else {
                classesSet.add(classes);
            }

        }


    }

    
    public Set<Classes> getCombinedClasses() {
        return classesSet;
    }

    public ClassesCombiner(Classes... classesArray) {
        this(new HashSet<Classes>(asList(classesArray)));
    }


    public Resources getResources() {
        return resources;
    }
}
