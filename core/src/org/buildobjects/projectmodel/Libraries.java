package org.buildobjects.projectmodel;

import org.buildobjects.artifacts.Classes;
import org.buildobjects.tasks.build.BuildTaskFactory;
import static org.buildobjects.util.MixedUtils.combine;


public class Libraries {
    final BuildTaskFactory tasks;

    public Libraries(BuildTaskFactory tasks) {
        this.tasks = tasks;
    }

    public class Commons {
        public Classes codec() {
            return classesFor("https://repo1.maven.org/maven2/commons-codec/commons-codec/1.3/commons-codec-1.3.jar");
        }

        public Classes lang() {
            return classesFor("https://repo1.maven.org/maven2/commons-lang/commons-lang/2.4/commons-lang-2.4.jar");
        }

        public Classes lang23() {
            return classesFor("https://repo1.maven.org/maven2/commons-lang/commons-lang/2.3/commons-lang-2.3.jar");
        }

        public Classes IO() {
            return classesFor("https://repo1.maven.org/maven2/commons-io/commons-io/1.4/commons-io-1.4.jar");
        }

        public Classes CLI() {
            return classesFor("https://repo1.maven.org/maven2/commons-cli/commons-cli/1.1/commons-cli-1.1.jar");
            }

        public Classes math(){
            return classesFor("https://mirrors.ibiblio.org/pub/mirrors/maven2/commons-math/commons-math/1.2/commons-math-1.2.jar");
        }
        
        public Classes genericCollections() {
            return classesFor("https://repo1.maven.org/maven2/net/sourceforge/collections/collections-generic/4.01/collections-generic-4.01.jar");
        }


    }

    public final Commons commons = new Commons();

    public Classes junit() {
         return classesFor("https://repo1.maven.org/maven2/junit/junit/4.4/junit-4.4.jar");
    }

    public Classes jtidy() {
        return classesFor("http://mirrors.ibiblio.org/pub/mirrors/maven2/jtidy/jtidy/4aug2000r7-dev/jtidy-4aug2000r7-dev.jar");
    }


    public Classes htmlcleaner() {
        return classesFor("https://htmlcleaner.sourceforge.net/download/htmlcleaner2_1.jar");
    }

    public Classes hypirinha() {
        return classesFor("https://hypirinha.googlecode.com/files/hypirinha-0.2.jar");
    }

    public Classes xstream() {
        return combine(
                classesFor("https://repo1.maven.org/maven2/com/thoughtworks/xstream/xstream/1.4.16/xstream-1.4.16.jar"),
                jettison()
        );
    }

    public Classes jettison() {
        return classesFor("https://repository.codehaus.org/org/codehaus/jettison/jettison/1.0-RC2/jettison-1.0-RC2.jar");
    }

    public Classes mockito() {
        return classesFor("https://repo1.maven.org/maven2/org/mockito/mockito-all/1.6/mockito-all-1.6.jar/files/mockito-all-1.6.jar");
    }
    
    public Classes dom4j() {
        return classesFor("https://mirrors.ibiblio.org/pub/mirrors/maven2/dom4j/dom4j-core/1.4-dev-8/dom4j-core-1.4-dev-8.jar");
    }

    public class JGoodies {

        public Classes binding() {
            return tasks.fetchJar("http://www.jgoodies.com/download/libraries/binding/binding-2_0_6.zip", "binding-2.0.6/binding-2.0.6.jar");
        }

        public Classes forms() {
            return tasks.fetchJar("http://www.jgoodies.com/download/libraries/forms/forms-1_2_1.zip", "forms-1.2.1/forms-1.2.1.jar");

        }

        public Classes looks() {
            return tasks.fetchJar("http://www.jgoodies.com/download/libraries/looks/looks-2_2_1.zip", "looks-2.2.1/looks-2.2.1.jar");


        }
    }

    public final JGoodies jgoodies = new JGoodies();

    private Classes classesFor(String url) {
        return tasks.fetchJar(url);
    }


}
