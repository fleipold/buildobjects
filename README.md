# buildobjects



buildobjects is an object based general purpose build tool

buildobjects provides a number of java classes that allow you to code your build process in a truly object-oriented fashion. 
It is not forcing you into a certain way of structuring your code and projects. Instead it aims at providing useful building blocks.

The rationale for using java was the availability of the full tool support to th build script. So when using buildobjects 
you get completetion, search for usage, and a fully featured debugger.

This prototype was originally hosted in [google code](https://code.google.com/archive/p/buildobjects/) 
and it has been migrated here for archiving purposes.

Getting Started
===============

Download and install buildobjects
---------------------------------

1. Get the buildobjects-dep.jar from the downnloads section.
2. Put the file into the local file system and create and an alias or a script to run it:
   ` alias bo="java -jar /pathtobuildobjects/buildobjects-dep.jar"`
   Make sure that you are using java 6.

Getting the project set up
--------------------------

The project to be built looks somewhat like this:

~~~
.
|-- build
|   `-- Build.java
|-- lib
|   |-- commons-lang-2.3.jar
|   `-- junit-4.4.jar
|-- src
|   `-- example
|       |-- A.java
|       `-- B.java
`-- test
    `-- example
        |-- ATest.java
        `-- BTest.java

~~~

The build folder contains the actual build script. A minimal build script looks like this: 
~~~
import org.buildobjects.BuildBase; 
import org.buildobjects.artifacts.Classes; 
import org.buildobjects.artifacts.FileLocation;
import org.buildobjects.artifacts.Location;
import org.buildobjects.projectmodel.JavaModule;

public class Build extends BuildBase {

    private JavaModule module;

    public Build() {
        Location location = new FileLocation(".");
        Classes libraries = location.jarDir("lib");
        module = new JavaModule(tasks, location, libraries);
    }


    public void build() {        
        build.publish(module.publishable());
    }

    public static void main(String[] args) {
        new Build().build();
    }
~~~

It is a good practice to have a separate project in your IDE setup for the builds. This includes the
buildobjects-dep.jar on the classpath.

How to run?
-----------

To trigger your build form the IDE you can just execute the main method in the build file. To run a build from the
command line the buildfile has to be compiled. Fortunately the buildobjects Tasklets do the job for
you: ` bo -source build Build`

After running the build you will see the following content in the target diretory:
~~~
target 0 $ tree --charset unicode
.
|-- SUCCEEDED
|-- index.html
`-- testproject-simple
    |-- test-results.txt
    `-- testproject-simple.jar
~~~

To see what this build actually published have a look at the
[JavaModule](http://code.google.com/p/buildobjects/source/browse/trunk/core/src/org/buildobjects/projectmodel/JavaModule.java)
class. It is expected that projects will define their own `JavaModule`
class. If it is put in the build folder it will be compiled when the build is run.

[Full Example](example/testproject-simple).


## Project Structure 

buildobjects is structured into multiple packages.In the `artifacts` package there are a number of classes that define
different artifacts. Artifacts are mainly [Sources](core/src/org/buildobjects/artifacts/Sources.java)
and [Classes](core/src/org/buildobjects/artifacts/Classes.java)
, which are representing filesystem like tree structures. The common aspect of `Sources` and `Classes` are abstracted
in `Resources`. There are several implementations, e.g. classes in a directory structure, classes in a jar file or
classes in an in-memory structure.

The `compiler` package is providing a wrapper around the newly introduced java compiler API (hence it needs java6) using
the concepts from the `artifacts` package.

On top of that the `tasks` package has a collection of Tasks that are the basic abstraction for a functional defintion
of your build. A task being something like a function-object that can be fully wired up, before actually doing any work.

It is expected that projects build their own abstractions on top of these tasks pretty much
like [JavaModel](core/src/org/buildobjects/projectmodel/JavaModule.java)
, which provides a reusable default implementation for a module build. Another example of such an abstraction ist
the [Libraries](core/src/org/buildobjects/projectmodel/Libraries.java)
class which provides a type safe way of expressing dependencies to external libraries.

The `tasklet` package provides a [Tasklet] runner, that allows to run small java programs form the command line. This
way you don't have to compile your build file to build your project.
