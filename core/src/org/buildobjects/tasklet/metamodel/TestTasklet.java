package org.buildobjects.tasklet.metamodel;

import org.buildobjects.tasklet.Description;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 2:41:15 PM
 */
public class TestTasklet {

    public static enum Language {
        GERMAN, ENGLISH
    }

    String name;
    boolean friendly;
    private int greetcount;
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
        greetcount++;

      if (friendly){
          System.out.println("Hello dear "+name+"!");
      } else {
          System.out.println("Hello "+name+".");
      }

    }

    @Description("Says goodbye")
     public void sayGoodbye(){
        greetcount++;

      if (friendly){
          System.out.println("Bye dear "+name+"!");
      } else {
          System.out.println("Bye "+name+".");
      }

    }


    // these are for testng only
    
    public String getName() {
        return name;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public int getGreetcount() {
        return greetcount;
    }

    public Language getLanguage() {
        return language;
    }
}
