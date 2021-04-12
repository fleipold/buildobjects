package org.buildobjects.artifacts;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang.StringUtils;
import org.buildobjects.artifacts.resources.Resources;
import org.buildobjects.artifacts.resources.ResourcesCombiner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 12:07:42 PM
 */
public class SourcesCombiner implements Sources {
    private final Set<Sources> sources;
    private Resources resources;


    public Set<Sources> getCombinedSources() {
        return sources;
    }


    public SourcesCombiner(Collection<Sources> combinedSources){
        resources = new ResourcesCombiner(CollectionUtils.collect(combinedSources, new Transformer<Sources, Resources>() {
            public Resources transform(Sources o) {
                return o.getResources();
            }
        }));
        sources = new HashSet<Sources>(combinedSources.size());

        for (Sources currentSources : combinedSources) {

            if (currentSources instanceof SourcesCombiner) {
                SourcesCombiner sourcesCombiner = (SourcesCombiner) currentSources;
                sources.addAll(sourcesCombiner.getCombinedSources());
            } else {
                sources.add(currentSources);
            }

        }

    }
    public SourcesCombiner(Sources... elements) {
        this(Arrays.asList(elements));
    }






    public String getName() {
        return "combiner(" + StringUtils.join(
                CollectionUtils.collect(sources, new Transformer<Sources, String>() {
                    public String transform(Sources o) {
                        return o.getName();
                    }
                }
                ), ", ") + ")";
    }

    public Resources getResources() {
        return resources;
    }


}
