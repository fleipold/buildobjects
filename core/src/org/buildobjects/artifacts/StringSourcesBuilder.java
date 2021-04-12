package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.InMemoryResource;
import org.buildobjects.artifacts.resources.InMemoryResources;
import org.buildobjects.artifacts.resources.Path;

/**
 * User: fleipold
 * Date: Nov 3, 2008
 * Time: 5:26:52 PM
 */
public class StringSourcesBuilder {
    InMemoryResources resources = new InMemoryResources();

    public class StringSourcesPackageBuilder {
        private Path path;


        public StringSourcesPackageBuilder(String name) {
            this.path = Path.forPacakgeName(name);
        }

        public StringSourcesPackageBuilder addClass(String name, String implementation) {
            resources.add(new InMemoryResource(path.child(name+".java"), implementation.getBytes()));
            return this;
        }

        public StringSourcesPackageBuilder addPackage(String name) {
            return new StringSourcesPackageBuilder(name);
        }

        public Sources toSources(){
            return new ResourcesSources(resources);
        }

    }


    public StringSourcesPackageBuilder addPackage(String name) {
        return new StringSourcesPackageBuilder(name);
    }

    public StringSourcesPackageBuilder addClass(String name, String implementation){
        StringSourcesPackageBuilder builder = new StringSourcesPackageBuilder("");
        builder.addClass(name, implementation);
        return builder;

    }

}



