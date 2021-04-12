package org.buildobjects;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.codec.binary.Base64;
import org.buildobjects.artifacts.Initializer;
import org.buildobjects.internal.BuildEnvironmentState;
import org.buildobjects.util.MixedUtils;
import org.buildobjects.util.Revision;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * User: fleipold
 * Date: Oct 15, 2008
 * Time: 4:32:55 PM
 */
public class MultipleBuildEnvironment implements BuildEnvironment {
    final File baseDir;
    BuildEnvironmentState state = new BuildEnvironmentState();
    private final File configFile;
    private final ResourceReader resourceReader;

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");

    public MultipleBuildEnvironment(File baseDir) {
        this.baseDir = baseDir;
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        resourceReader = new CachingResourceReader(new File(System.getProperty("user.home"),".buildobjects"));
        configFile = new File(baseDir, "environment.json");
        loadConfig();


    }

    public Build createBuild() {
        final Date timestamp = new Date();
        final int buildNo = getNextBuildNo();
        File buildDir =  new File(baseDir, TIMESTAMP_FORMAT.format(timestamp) + "-build." + buildNo);
        buildDir.mkdirs();
        return new Build(this, buildDir, buildNo, timestamp);
    }

    private int getNextBuildNo() {

        int buildNo = state.getNextBuildNo();
        state.setNextBuildNo(buildNo + 1);
        writeConfig();
        return buildNo;
    }

    private void loadConfig() {
        if (!configFile.exists())
            return;
        String configString = null;
        try {
            configString = IOUtils.toString(new FileInputStream(configFile));
            XStream xstream = getXStream();
            state = (BuildEnvironmentState) xstream.fromXML(configString);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private XStream getXStream() {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.alias("build", BuildResult.class);
        xstream.alias("environment", BuildEnvironmentState.class);
        return xstream;
    }

    private void writeConfig() {
        try {
            final FileOutputStream stream = new FileOutputStream(configFile);
            XStream xstream = getXStream();
            final String jsonString = xstream.toXML(state);

            IOUtils.write(jsonString, stream);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

     public File getCacheFolder(String key, Initializer initializer) {
        String folderName = new String(Base64.encodeBase64(key.getBytes()));
        final File directory = new File(baseDir, "cache/" + folderName);
        if (!directory.exists()) {
            directory.mkdirs();
            initializer.initDir(directory);
        }

        return directory;
    }

    public void reportResult(BuildResult result) {
        System.out.println("Build finished: " + result.getState());
        state.addResult(result);
        writeConfig();
        writeIndex();

    }

    private void writeIndex() {
        StringBuffer html = new StringBuffer();
        Formatter formatter = new Formatter(html);
        formatter.format("<html><body><ul>");


        List<BuildResult> results = new ArrayList<BuildResult>(state.getResults());
        Collections.reverse(results);


        for (BuildResult buildResult : results) {
            String style = buildResult.getState().succeeded() ? "color:green" : "color:red";
            String path = MixedUtils.relativePath(baseDir, buildResult.getFolder()) + "/index.html";

            formatter.format("<a href=\"%s\" style = \"%s\"><li >build.%d %s </li></a>",
                    path,
                    style,
                    buildResult.getBuildNumber(),
                    buildResult.getState());
        }

        formatter.format("</ul></body></html");
        try {
            FileUtils.writeStringToFile(new File(baseDir, "index.html"), html.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public List<BuildResult> getResults() {
        return Collections.unmodifiableList(state.getResults());
    }

    public Revision getLatestRevision() {
        if (state.getResults().isEmpty()) {
            return null;
        }
        return state.getResults().get(state.getResults().size() - 1).getRevision();

    }

    public File fetchFile(URL url) {

        return resourceReader.fetchFile(url);
    }


    public File fetchFileInZip(URL url, String relativePath) {

        return resourceReader.fetchFileInZip(url, relativePath);
    }
}
