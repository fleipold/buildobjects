package org.buildobjects;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;
import org.buildobjects.artifacts.Initializer;
import org.buildobjects.publishers.FSFileSink;
import org.buildobjects.publishers.PublicationInfo;
import org.buildobjects.publishers.PublicationListener;
import org.buildobjects.publishers.Publishable;
import org.buildobjects.tasks.FailureException;
import org.buildobjects.util.MixedUtils;
import org.buildobjects.util.Revision;

import java.io.*;
import java.util.*;


/**
 * The build object provides the context for a certain a build, like means to get temp folders and to publish
 * build results as well as getting a label and timestamp.
 */
public class
        Build {
    BuildState state = BuildState.IN_PROGRESS;

    File baseDir;
    Map built = new HashMap();
    int tempCount = 0;
    private Date timestamp;
    private final BuildEnvironment environment;
    private final int buildNo;
    
    private Revision revision;
    private boolean hasPublished = false;
    private List<FailureException> failures = new ArrayList<FailureException>();
    private List<PublicationInfo> publications = new ArrayList<PublicationInfo>();
    private long timeToBuild;


    public Build(BuildEnvironment environment, File basedir, int buildNo, final Date timestamp) {
        this.environment = environment;
        this.buildNo = buildNo;
        this.timestamp = timestamp;
        this.baseDir = basedir;

    }


    public File getTargetFolder() {
        return baseDir;
    }

    public File getTempFile(String prefix, String extension) {
        File tempDir = new File(baseDir, "temp");
        tempDir.mkdirs();
        File file = new File(tempDir, prefix + tempCount + "." + extension);
        tempCount++;
        return file;
    }

    public File getTempFolder() {
        File folderName = new File(baseDir, "temp/" + tempCount);
        folderName.mkdirs();

        tempCount++;
        return folderName;

    }


    public boolean isBuilt(Object task) {
        return built.containsKey(task);
    }

    public void setBuilt(Object task) {
        built.put(task, true);
    }

    public void log(String message) {
        System.out.println(message);
    }


    public void publish(Publishable publishable) {
        if (hasPublished) {
            throw new IllegalStateException("Cannot publish more than one publisher");
        }
        hasPublished = true;
        publishable.publish(new FSFileSink(baseDir, new PublicationListener() {

            public void published(PublicationInfo publication) {
                publications.add(publication);
            }
        }));

        if (state == BuildState.IN_PROGRESS) {
            state = BuildState.SUCCEEDED;
        }

        timeToBuild = System.currentTimeMillis() - timestamp.getTime();
        environment.reportResult(new BuildResult(this.state, this.baseDir, this.timestamp, this.buildNo, this.revision, timeToBuild));
        writeIndex();
        writeResultFile();
    }

    private void writeResultFile() {
        try {
            OutputStream outfile = new FileOutputStream(new File(baseDir,state.toString()));
            IOUtils.write(""+timestamp+"", outfile);
            outfile.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public File getCacheFolder(String key, Initializer initializer) {
        return environment.getCacheFolder(key, initializer);
    }

    //todo: this is a hack! The relationship between publish, location and revision has to be clarified...
    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    public int getBuildNo() {
        return buildNo;
    }

    public void reportFailure(FailureException failure) {
        state = BuildState.FAILED;
        failures.add(failure);
    }


    private void writeIndex() {
        StringBuffer html = new StringBuffer();
        Formatter formatter = new Formatter(html);
        formatter.format("<html><body>");

        String style = state.succeeded() ? "color:green" : "color:red";
        formatter.format("<h1 style=\"%s\">build.%d</h1>", style, buildNo);
        formatter.format("<table><tr><td>Date:</td><td>%s</td></tr><tr><td>Revision:</td><td>%s</td></tr>" +
                            "<tr><td>Time to build:</td><td>%ds</td></tr></table>",timestamp, revision, timeToBuild/1000);
        
        if (!this.failures.isEmpty()) {
            formatter.format("<h1>Failures</h1>");
            formatter.format("<ul>");
            for (FailureException failure : failures) {
                formatter.format("<li>%s : <pre>\n%s\n</pre> </li>", failure.getTask().getName(), failure.getMessage());
            }
            formatter.format("</ul>");
        }


        if (!this.publications.isEmpty()) {
            formatter.format("<h1>Publications</h1>");
            formatter.format("<ul>");

            for (PublicationInfo publication : publications){
                String path = MixedUtils.relativePath(baseDir, publication.getOutput());
                formatter.format("<li><a href=\"%s\">%s</a></li>", path, publication.getFileName() );
            }

            formatter.format("</ul>");
        }


        formatter.format("</body></html");
        try {
              FileUtils.writeStringToFile(new File(baseDir, "index.html"), html.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public BuildEnvironment getEnvironment() {
        return environment;
    }
}