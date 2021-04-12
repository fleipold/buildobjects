package org.buildobjects.internal;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import org.buildobjects.BuildResult;
import org.buildobjects.BuildState;
import org.buildobjects.util.SVNRevision;
import org.junit.Test;

import java.io.File;
import java.util.Date;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 11:33:49 PM
 */
public class BuildEnvironmentStateTest {
    BuildEnvironmentState state = new BuildEnvironmentState();

    @Test
    public void serialize(){
        BuildResult res = new BuildResult(BuildState.SUCCEEDED, new File(""), new Date(), 4, new SVNRevision(3), 100);
        state.addResult(res);

        //json.put("state", state);
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.alias("build", BuildResult.class);
        xstream.alias("environment", BuildEnvironmentState.class);
        xstream.setClassLoader(this.getClass().getClassLoader());
        final String jsonString = xstream.toXML(state);
        System.out.println(jsonString);


        BuildEnvironmentState anotherState = (BuildEnvironmentState) xstream.fromXML(jsonString);


    }

}
