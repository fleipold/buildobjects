## What is a Tasklet?

A tasklet is a small program that performs a certain task and possibly takes some arguments to do so. Normally you would use a scripting language to perform such tasks. 

buildobjects has a tasklet runner that allows to implement such programs without worryinng about parsing the command-line args and compiling your tasklet. It it compiles and introspects your tasklet and tries to parse arguments of the command line and then executes the tasklet.

## Example
~~~
import org.buildobjects.tasklet.Description;


public class DemoTasklet {
        public static enum Language {
            GERMAN, ENGLISH
        }

        String name;
        boolean friendly;
        Language language;

        public void setLanguage(Language language) {
            this.language = language;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Description("Greet a bit more friendly")
        public void setFriendly(boolean friendly) {
            this.friendly = friendly;
        }

        @Description("Says hello")
        public void sayHello(){
              System.out.println("Hello "+address()+"!");
        }

        @Description("Says goodbye")
         public void sayGoodbye(){
              System.out.println("Bye"+address()+"!");
        }

    private String address(){
        return (friendly?" dear ":"") + name;
    }
}
~~~

If you have this file on your current directory you can just kick it off with this bit of magic:

~~~
java -jar buildobjects-dep.jar DemoTasklet
~~~

This will produce output like this:

~~~
Tasklet has more than one action please specify action as commandline parameter
usage: DemoTasklet [options] action
 -friendly              Greet a bit more friendly
 -h                     Show help
 -language <language>   GERMAN|ENGLISH language
 -name <name>           name
Available actions:
 sayHello: Says hello
 sayGoodbye: Says goodbye
~~~

So obviously you need to tell the tasklet what to do:

~~~
java -jar buildobjects-dep.jar DemoTasklet -name Felix sayGoodbye
~~~
This produces:
~~~
Bye Felix!
~~~

## How does it work?
The [TaskletReflector](TaskletReflector) looks at all public setters and tries to get them from the commandline.
Currently it only supports Strings, enums, and booleans. It looks for all non-getter public zero argument
methods and treats them as Actions.
You can use the @Description annotation to get a more meaningful help message.

If your sources are not on the same folder, you might specify a source folder by passing it as command line parameter,
before the fully qualified classname, e.g.:

~~~
java -jar buildobjects-dep.jar -sources src/java DemoTasklet -name Felix sayGoodbye
~~~
