import org.buildobjects.BuildBase;
import org.buildobjects.SingleBuildEnvironment;
import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.FileLocation;
import org.buildobjects.artifacts.Location;
import org.buildobjects.projectmodel.JavaModule;

import java.io.File;

/* Functional definition of build
 *   Everything is passed into the constructor, hence no cycles!
 */
public class Build extends BuildBase {

    private JavaModule module;

    public Build() {
        super(new SingleBuildEnvironment(new File("target")));
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

}
