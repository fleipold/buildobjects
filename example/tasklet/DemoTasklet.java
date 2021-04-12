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
              System.out.println("Hallo "+address()+"!");
        }

        @Description("Says goodbye")
         public void sayGoodbye(){
              System.out.println("Bye "+address()+"!");
        }

    private String address(){
        return (friendly?"dear ":"") + name;
    }
}
