# Functional style Build and Task Definition

buildobjects provides a higher level abstraction for tasks and builds. Each running build is represented by a build object
which in turn lives inside a BuildEnvironment. All tasks are defined in terms of that build and there is a TaskBuilder
which helps with creating tasks. The taskobjects provide handles to their outputs, that can be passed around without
triggering a build. The build is not triggered until you ask the build to publish results. Normally you don't have to
specify where to build stuff to. The compiler just compiles into memory. If you need the classes filed out (which you
probably don't for unit tests) you have to specify a publisher. Building module_a now looks like this:

~~~
BuildEnvironment environment = new BuildEnvironment(new File("example-builds"));
Build build = environment.createBuild();
StandardTaskBuilder tasks = new StandardTaskBuilder(build);

Location projectLocation = new FileLocation("example/testproject");

Classes junit = projectLocation.jarFile("lib/junit-4.4.jar");
Classes mockito = projectLocation.jarFile("lib/mockito-all-1.6.jar");

Classes testLibs = new ClassesCombiner(junit, mockito);
Classes commonsLang = projectLocation.jarFile("lib/commons-lang-2.3.jar");

Location locA = projectLocation.child("module_a");

JavaC moduleACompile = tasks.javac(
    commonsLang,
    locA.src("src")
);

JavaC moduleACompileTests = tasks.javac(
    asList(testLibs, moduleACompile.output()),
    locA.src("test")
);


JUnit moduleARunTests = tasks.junit(
    moduleACompileTests.outputDep(),
    locA.src("test")
);


Publishable result = new PublishableCombiner(
    "dist",
    new JarFileOut(moduleACompile.output(), "module_a.jar"),
    new TestResultFileOut(moduleARunTests.getResults(), "module_a_test.txt")
);

// the  steps so far did not trigger any action, so now we go
 build.publish(result);
~~~
