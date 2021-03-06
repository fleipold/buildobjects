## The Benefit - Separation of Concerns

While it seems a lot of plumbing the task approach allows nice composition. So you might specify how a java module looks like for your project and reuse that for multiple modules:

```
public class JavaModule {
    
    private final JavaC compile;
    private final JavaC compileTests;
    private final JUnit runTests;
    private final Publishable publisher;

    public JavaModule(Location moduleLocation, StandardTaskBuilder tasks, Classes libs, Classes testlibs) {    
        compile = tasks.javac(
                libs,
                moduleLocation.src("src")
        );

        compileTests = tasks.javac(
                new ClassesCombiner(asList(testlibs, compile.outputDep())),
                moduleLocation.src("test")
        );



        runTests = tasks.junit(
                compileTests.outputDep(),
                moduleLocation.src("test")
        );


        publisher = new PublishableCombiner(
              moduleLocation.getName(),
                new JarFileOut(output(),"dist.jar"),
                new TestResultFileOut(getTestResults(),"test-results.txt")
             );


    }

    public Classes output() {
        return compile.output();
    }

    public Classes outputDep() {
        return compile.outputDep();
    }

    public List<SuiteResult> getTestResults() {
        return runTests.getResults();
    }

    public Publishable publishable() {
        return publisher;
    }
}

```

This collapses the build of the ExampleProject to something like this:
```
JavaModule moduleA = new JavaModule(location.child("module_a"), tasks, commonsLang, testLibs);

JavaModule moduleB = new JavaModule(location.child("module_b"), tasks, moduleA.outputDep(), junit);

//nothing happens until here:
PublishableCombiner buildResult = new PublishableCombiner(moduleA.publishable(), moduleB.publishable());

build.publish(buildResult);
```
