package org.buildobjects.tasks.build;

import org.buildobjects.Build;
import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.ClassesCombiner;
import org.buildobjects.artifacts.NullClasses;
import org.buildobjects.artifacts.Sources;
import org.buildobjects.compiler.CompilationFailedException;
import org.buildobjects.compiler.CompilerWrapper;
import org.buildobjects.tasks.FailureException;
import org.buildobjects.tasks.JavaC;
import org.buildobjects.util.DelegateProvider;
import org.buildobjects.util.DelegatingProxy;

import java.io.IOException;

class JavaCBuild extends AbstractTask implements JavaC {
    private final Classes dependencies;
    private final Sources sources;
    private Classes result;
    private StackTraceElement[] stackTrace;
    private FailureException failureException;

    private Classes platformClassPath;

    public JavaCBuild(Build build, Classes dependencies, Sources sources) {
        super(build);
        this.dependencies = dependencies == null ? new NullClasses() : dependencies;
        this.sources = sources;
        this.stackTrace = new Exception().getStackTrace();
    }


    protected void buildInternal(){ 
        result = compile();
    }

    private Classes compile() {
        try {
            final CompilerWrapper compilerWrapper = new CompilerWrapper(sources, new ClassesCombiner(dependencies));
            compilerWrapper.setPlatformClassPath(platformClassPath);
            compilerWrapper.doit();
            return compilerWrapper.getResult();

        } catch (IOException e) {
            final FailureException failureException = new FailureException("Compilation failed", this, e);
            build.reportFailure(failureException);
            throw failureException;
        } catch (CompilationFailedException cf){
            cf.setStackTrace(stackTrace);
            final FailureException failureException = new FailureException(cf.getMessage(), this, cf);
            build.reportFailure(failureException);
            throw failureException;
        } catch (FailureException fex){
            failureException = new FailureException("Dependency failed", this, fex);
            build.reportFailure(failureException);
            throw failureException;
        }

    }

    public Classes output() {
        return DelegatingProxy.create(Classes.class, new DelegateProvider<Classes>() {
            public Classes delegate() {
                build();
                return result;
            }
        });

    }

    public Classes outputDep() {
            return new ClassesCombiner(output(), dependencies);
        }


    public String getName() {
        return "JavaC " + sources.getName();
    }


    public void setPlatformClassPath(Classes platformClassPath) {
        this.platformClassPath = platformClassPath;
    }
}