## Using the Wrapper for the Java 6 Compiler API

To compile module_a into a jar from our ExampleProject we write the following code:

```
Classes dependencies = new JarDirClasses("example/testproject/lib");
Sources sources =   new FileSources(new File("example/testproject/module_a/src"));
CompilerWrapper compiler = new CompilerWrapper(sources,dependencies);
compiler.doit();
new JarFileOut(compiler.getResult(), "module_a.jar").write();
```

Sometimes we are not actually interested in the jar file so we might as well new up classloader directly:

```
Classes dependencies = new JarDirClasses("example/testproject/lib");
Sources sources =   new FileSources(new File("example/testproject/module_a/src"));
CompilerWrapper compiler = new CompilerWrapper(sources,dependencies);
compiler.doit();
       
ClassesLoader loader = new ClassesLoader(compiler.getResult());
Class a = loader.loadClass("felix.A");
```
