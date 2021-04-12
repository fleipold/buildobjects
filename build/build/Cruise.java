package build;

import org.buildobjects.MultipleBuildEnvironment;
import org.buildobjects.util.Revision;
import org.buildobjects.Build;
import org.buildobjects.BuildEnvironment;
import org.buildobjects.classpath.DelegationBlockingClassesLoader;
import org.buildobjects.tasks.JavaC;
import org.buildobjects.tasks.build.BuildTaskFactory;
import org.buildobjects.util.SVNWrapper;
import org.buildobjects.artifacts.Location;
import org.buildobjects.artifacts.*;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 4:36:51 PM
 */
public class Cruise {
    private final MultipleBuildEnvironment environment;
    private final BuildEnvironment bootStrap;
    Project buildobjects;
    private int pollingInterval = 5000;
    private URLClassLoader parentLoader;


    public Cruise(){
        this.environment = new MultipleBuildEnvironment(new File("builds/continuous-builds"));

        this.bootStrap = new MultipleBuildEnvironment(new File("builds/bootstrap-builds"));

        try {
            buildobjects = new Project(new URL("http://buildobjects.googlecode.com/svn/trunk/build")
                                       );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        parentLoader = (URLClassLoader) Build.class.getClassLoader();
    }

    public void cruise(){
             do {
                 log("Checking if build needed");
                 if (buildNeeded()){
                     log("starting to build");
                     build();

                 } else {
                     log("No build needed");
                 }

                 try {
                     Thread.sleep(pollingInterval);
                 } catch (InterruptedException e) {
                     throw new RuntimeException(e);
                 }
             }   while (true);
    }

    private void log(String message) {
        System.out.println(message);
    }


    private void build() {
        final Classes compiledBuildScript = compileScript();
        ClassLoader myLoader = new DelegationBlockingClassesLoader(compiledBuildScript, parentLoader, "build");

        try {

            Class builderClass = myLoader.loadClass(BuildFile.class.getName());

            Constructor constructor = builderClass.getConstructor(MultipleBuildEnvironment.class);
            Object builder = constructor.newInstance(environment);
            Method setSubversion = builderClass.getMethod("setFromSubversion", boolean.class);
            setSubversion.invoke(builder, true);
            Method buildMethod = builderClass.getMethod("build");
            buildMethod.invoke(builder);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }


    }

    private Classes compileScript() {
        Build bootstrapBuild = bootStrap.createBuild();

        Location buildfileLocation = new SVNLocation(bootstrapBuild, buildobjects.getBootstrap().toExternalForm());
        Sources source = buildfileLocation.src(".");
        BuildTaskFactory tasks = new BuildTaskFactory(bootstrapBuild);

        URL url = getUrlForClass(parentLoader, "org/buildobjects/Build.class");
        Classes classpath = new ClassLoadableClasses(new File(url.getFile()));

        JavaC compile=tasks.javac(classpath, source);
        final Classes compiledBuildScript = compile.output();
        return compiledBuildScript;
    }

    private URL getUrlForClass(URLClassLoader parentLoader, String name) {

        String classURL = parentLoader.getResource(name).toExternalForm();
        if (classURL.startsWith("jar:")){
            classURL = classURL.replace("jar:","");
        }

        log("URL for the class  " + classURL);
        URL[] urls = parentLoader.getURLs();
            log ("Root URLs from classloader:");
        for (URL url:urls){
            log (" "+url.toExternalForm());

            if (classURL.startsWith(url.toExternalForm()))
                return url;
        }
        throw new RuntimeException("could not find the right url");
    }

    private boolean buildNeeded() {
        Revision lastBuildRevision = environment.getLatestRevision();
        Revision latestRevision = new SVNWrapper().getLatestRevision(buildobjects.getBootstrap());
        log ("Last build: "+lastBuildRevision);
        log ("Latest repository: "+latestRevision);

        if (lastBuildRevision == null) return
                true;
        return lastBuildRevision.compareTo(latestRevision) < 0;

    }

    public static void main(String[] args) {
        new Cruise().cruise();

    }

}
