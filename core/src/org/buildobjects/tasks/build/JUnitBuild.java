package org.buildobjects.tasks.build;

import org.buildobjects.Build;
import org.buildobjects.artifacts.*;
import org.buildobjects.artifacts.resources.Resource;
import org.buildobjects.classpath.ClassLoaderFactory;
import org.buildobjects.classpath.ClassesLoader;
import org.buildobjects.tasks.FailureException;
import org.buildobjects.tasks.JUnit;
import org.buildobjects.util.DelegateProvider;
import org.buildobjects.util.DelegatingProxy;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


class JUnitBuild extends AbstractTask implements JUnit {
    private int nFail;

    public JUnitBuild(Build build, Classes dependencies, Sources sources) {
        super(build);
        this.dependencies = dependencies;
        this.sources = sources;
    }

    private ClassLoaderFactory classLoaderFactory = new ClassLoaderFactory() {
        public ClassLoader getClassLoader(Classes classes, ClassLoader parentLoader) {
            return new ClassesLoader(classes, parentLoader);
        }
    };

    Classes dependencies;
    Sources sources;
    List<SuiteResult> results = new ArrayList<SuiteResult>();


    protected void buildInternal() {
        test();
    }

    /**
     * be careful!
     */
    public void setClassLoaderFactory(ClassLoaderFactory factory) {
        this.classLoaderFactory = factory;
    }

    private void test() {
        try {
            final List<String> testClasses = getTestClasses();

            final ClassLoader loader = classLoaderFactory.getClassLoader(
                    new ClassesCombiner(dependencies), this.getClass().getClassLoader());

            for (final String className : testClasses) {
                final Exception[] ex = new Exception[]{null};
                loadAndRunInSeparateThread(loader, className, ex);

            }
        } catch (FailureException fex) {
            FailureException wrapper = new FailureException("Dependency failed", this, fex);
            build.reportFailure(wrapper);
            throw wrapper;
        }

        if (nFail > 0) {
            final FailureException failureException = new FailureException("There are failed tests.", this, null);
            build.reportFailure(failureException);
            //don't throw...
        }
    }

    private void loadAndRunInSeparateThread(final ClassLoader loader, final String className, final Exception[] ex) {
        Runnable testRunner = new Runnable() {

            public void run() {
                try {

                    Result result = loadAndRunTestCase(loader, className);
                    if (result == null) {
                        return;
                    }

                    System.out.print(className);
                    System.out.print(" (run: " + result.getRunCount());
                    System.out.println(" failed: " + result.getFailureCount() + ")");
                    for (Failure failure : result.getFailures()) {
                        System.out.println(failure.getTrace());
                    }

                    nFail = nFail + result.getFailureCount();

                    results.add(new SuiteResult(className, result));
                } catch (Exception e) {
                    ex[0] = e;

                }
            }
        };

        ThreadGroup testGroup = new ThreadGroup("Test execution");
        Thread testThread = new Thread(testGroup, testRunner);
        testThread.setContextClassLoader(loader);
        testThread.start();
        try {
            testThread.join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (ex[0] != null) {
            if (ex[0] instanceof RuntimeException) {
                throw (RuntimeException) (ex[0]);
            } else {

                throw new RuntimeException(ex[0]);
            }
        }

        int active = testGroup.enumerate(new Thread[100]);
        if (active > 0) {
            System.out.println("##########  Active threads: " + active);
        }
        testGroup.stop();
    }

    private Result loadAndRunTestCase(ClassLoader loader, String className) {
        Class testClazz = null;
        try {
            testClazz = loader.loadClass(className);
            if (testClazz.isInterface() || Modifier.isAbstract(testClazz.getModifiers())) {
                return null;
            }

            if (testClazz == null) {
                throw new RuntimeException("Could not load testclass " + className);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new JUnitCore().run(testClazz);
    }

    private List<String> getTestClasses() {
        List<String> testClasses = new ArrayList<String>();
        for (Resource file : sources.getResources().getAll()) {
            if (file.getPath().getName().endsWith("Test.java")) {
                testClasses.add(file.getPath().toFullyQualifiedClassName());
            }

        }
        return testClasses;
    }

    public TestResults results() {
        return DelegatingProxy.create(TestResults.class, new DelegateProvider<TestResults>() {
            public TestResults delegate() {
                build();
                return new TestResultsImpl(results);
            }
        }, getClass().getClassLoader());

    }

    public String getName() {
        return "JUnit " + sources.getName();
    }
}