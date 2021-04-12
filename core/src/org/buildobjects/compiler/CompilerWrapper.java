package org.buildobjects.compiler;

import org.apache.commons.collections15.Predicate;
import org.buildobjects.artifacts.*;
import org.buildobjects.artifacts.resources.FilteringResources;
import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.util.MixedUtils;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * User: fleipold
 * Date: Sep 30, 2008
 * Time: 4:49:11 PM
 */
public class CompilerWrapper {

    final Sources sources;

    final Classes classes;

    private Classes result;
    private List<Diagnostic<? extends JavaFileObject>> diagnostics;
    private Iterable<String> options = Arrays.asList("-g");
    private Classes platformClassPath;



    public CompilerWrapper(Sources sources, Classes classes) throws IOException {
        this.sources = sources;
        this.classes = classes;
        //doit();
    }

    public void setPlatformClassPath(Classes platformClassPath){

        this.platformClassPath = platformClassPath;
    }




    public void doit() throws IOException {


        javax.tools.JavaCompiler jc = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<JavaFileObject>();
        CompilerFileManager myFileManager = new CompilerFileManager(jc.getStandardFileManager(collector, null, null));


        ResourcesReadingFileManagerAdapter sourceFileAdapter = sourceFileAdpater();

        Iterable<JavaFileObject> sourcesToCompile = sourceFileAdapter.listClassPathFiles("", MixedUtils.set(JavaFileObject.Kind.SOURCE), true);


        myFileManager.addAdapterForLocation("SOURCE_PATH", sourceFileAdapter);
        myFileManager.addAdapterForLocation("CLASS_PATH", classPathAdapter());
        if (platformClassPath!=null){
            myFileManager.addAdapterForLocation("PLATFORM_CLASS_PATH", platformClassPathAdapter());
        }


        jc.getTask(null, myFileManager, collector, options, null, sourcesToCompile).call();

        this.diagnostics = collector.getDiagnostics();
        checkForFailure();
        System.out.println(formatDiagnostics(diagnostics));

        myFileManager.close();
        result = new ClassesCombiner(myFileManager.getResult(), new ResourcesClasses(sources.getResources()));
    }

    private ResourcesReadingFileManagerAdapter platformClassPathAdapter() {
        return new ResourcesReadingFileManagerAdapter(new FilteringResources(new Predicate<Path>() {
                public boolean evaluate(Path path) {
                    return !path.isJava();
                }
            }, platformClassPath.getResources()));
        }

    private ResourcesReadingFileManagerAdapter classPathAdapter() {
        final FilteringResources classesWithoutSources = new FilteringResources(new Predicate<Path>() {
            public boolean evaluate(Path path) {
                return !path.isJava();
            }
        }, classes.getResources());
        return new ResourcesReadingFileManagerAdapter(classesWithoutSources);
    }

    private ResourcesReadingFileManagerAdapter sourceFileAdpater() {
        ResourcesReadingFileManagerAdapter sourceFileAdapter = new ResourcesReadingFileManagerAdapter(new FilteringResources(new Predicate<Path>() {
            public boolean evaluate(Path path) {
                return path.isJava();
            }
        }, sources.getResources()));
        return sourceFileAdapter;
    }

    public static String formatDiagnostics(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Diagnostic diagnostic : diagnostics) {
            stringBuilder.append("\n");
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR){
                stringBuilder.append("Error: ");
            } else if (diagnostic.getKind() == Diagnostic.Kind.MANDATORY_WARNING
                           || diagnostic.getKind() == Diagnostic.Kind.WARNING){
                stringBuilder.append(" Warning: ");
            } else stringBuilder.
                 append("Issue: ");

            stringBuilder.append(diagnostic.getMessage(Locale.getDefault()));
            //stringBuilder.append("\n");


        }
        return stringBuilder.toString();
    }

    private void checkForFailure() {
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                throw new CompilationFailedException(diagnostics);
            }
        }
    }


    public static void main(String[] args) throws IOException {
//        new CompilerWrapper(
//                Arrays.asList(
//                        new FileSources("/Users/fleipold/Prog/java-stuff/newbuild/example/src/module_a/src"),
//                        new FileSources("/Users/fleipold/Prog/java-stuff/newbuild/example/src/module_a/src2")
//                ),
//                new NullClassPath(),
//
//                new File("/Users/fleipold/Prog/java-stuff/newbuild/example/src/builds/out.jar")).doit();

        ClassesCombiner classes = new ClassesCombiner(

                        new JarClasses(new File("/Users/fleipold/Prog/java-stuff/newbuild/example/src/builds/out.jar"))
                );


        new CompilerWrapper(
                new FileSources("/Users/fleipold/Prog/java-stuff/newbuild/example/src/module_b/src", ""),
                classes
        ).doit();

    }


    public Classes getResult() {
        return result;
    }
}
