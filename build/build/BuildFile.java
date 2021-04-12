package build;

import org.buildobjects.*;
import org.buildobjects.projectmodel.Libraries;
import org.buildobjects.projectmodel.JavaModule;
import static org.buildobjects.util.MixedUtils.combine;
import org.buildobjects.publishers.*;
import org.buildobjects.classpath.ClassLoaderFactory;
import org.buildobjects.classpath.DelegationBlockingClassesLoader;
import org.buildobjects.tasks.*;
import org.buildobjects.artifacts.*;

import java.io.File;

/* Functional definition of build
*   Everything is passed into the constructor, hence no cycles!
*  But a bit of a smell with all those names moduleACompileTests etcpp*/
public class BuildFile extends BuildBase {
    private boolean fromSubversion;
    private PublishableCombiner buildResult;
    private JavaModule module;
    private Classes libs;
    private Classes testLibs;

    public BuildFile() {}

    public BuildFile(BuildEnvironment environment) {
        super(environment);
    }

    public void setFromSubversion(boolean fromSubversion) {
        this.fromSubversion = fromSubversion;
    }    


    private void initialize() {
        Location location = fromSubversion ?
                new SVNLocation(build, "http://buildobjects.googlecode.com/svn/trunk/")
                : new FileLocation(".");

        Libraries libraries = new Libraries(tasks);


        Classes  commons = combine(
                //libraries.commons.CLI(),
                tasks.fetchJar("https://repo1.maven.org/maven2/commons-cli/commons-cli/1.1/commons-cli-1.1.jar"),
                //libraries.commons.lang(),
                //libraries.commons.codec(),
                tasks.fetchJar("https://repo1.maven.org/maven2/commons-cli/commons-cli/1.1/commons-cli-1.1.jar"),
                tasks.fetchJar("https://repo1.maven.org/maven2/commons-io/commons-io/1.4/commons-io-1.4.jar"),
                tasks.fetchJar("https://repo1.maven.org/maven2/commons-codec/commons-codec/1.3/commons-codec-1.3.jar"),
                tasks.fetchJar("https://repo1.maven.org/maven2/commons-lang/commons-lang/2.4/commons-lang-2.4.jar")
                //,libraries.commons.IO()
        );

        libs = combine(
                commons,
            tasks.fetchJar("https://repo1.maven.org/maven2/com/thoughtworks/xstream/xstream/1.4.16/xstream-1.4.16.jar"),

            //libraries.xstream(),
               // libraries.junit(),
                tasks.fetchJar("https://repo1.maven.org/maven2/junit/junit/4.4/junit-4.4.jar"),
                tasks.fetchJar("https://repo1.maven.org/maven2/net/sourceforge/collections/collections-generic/4.01/collections-generic-4.01.jar")
                //libraries.commons.genericCollections()
        );

        testLibs = tasks.fetchJar("https://repo1.maven.org/maven2/org/mockito/mockito-all/1.6/mockito-all-1.6.jar");
                    //libraries.mockito();

        module = new JavaModule(tasks, location.child("core"), libs, testLibs);

        //block classloader delegation to test actual compiled code rather than the bootstrap version of buildobjects
        module.getInternals().getRunTests().setClassLoaderFactory(new ClassLoaderFactory() {
            public ClassLoader getClassLoader(Classes classes, ClassLoader parentLoader) {
                return new DelegationBlockingClassesLoader(classes, parentLoader, "org.buildobjects", build.getBuildNo());
            }
        });


        final JarFileOut jarFile = new JarFileOutBuild(module.outputDep(), "buildobjects-dep.jar",
                new ManifestBuilder().mainClass("org.buildobjects.tasklet.TaskletRunner").toManifest());

        
        final TestResultFileOut testResultFile = new TestResultFileOut(module.testResults(), "buildobjects_tests.txt");


        buildResult = new PublishableCombiner(
                        new PublishableCombiner("dist", jarFile),
                        new PublishableCombiner("test-results", testResultFile)

        );

    }


    public void build() {
        initialize();
        build.publish(buildResult);

    }


    public void fetch(){
        initialize();
        final JarFileOut libJar = new JarFileOutBuild(libs, "lib.jar");
        final JarFileOut testJar = new JarFileOutBuild(testLibs, "test-lib.jar");


        buildResult = new PublishableCombiner(
            new PublishableCombiner("dist", libJar),
            new PublishableCombiner("dist", testJar));

        build.publish(buildResult);


    }


    public void buildJar() {
        initialize();
        build.publish(tasks.jar(module.output(),"my.jar"));

    }

    public void test(){
        initialize();
        build.publish(new TestResultFileOut(module.testResults(),"test-results.txt"));
    }



    public static void main(String[] args) {

        new BuildFile().build();
    }
}
