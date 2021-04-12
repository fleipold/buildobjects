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
public class SingleBuildEnvironment implements BuildEnvironment {
    final File baseDir;
    private final ResourceReader resourceReader;

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");

    public SingleBuildEnvironment(File baseDir) {
        this.baseDir = baseDir;

        if (baseDir.exists()) {
            try {
                FileUtils.deleteDirectory(baseDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        baseDir.mkdirs();

        resourceReader = new CachingResourceReader(new File(System.getProperty("user.home"),".buildobjects"));


    }

    public Build createBuild() {

        final Date timestamp = new Date();


        return new Build(this, baseDir, 999999, timestamp);
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
        

    }


    public File fetchFile(URL url) {

        return resourceReader.fetchFile(url);
    }


    public File fetchFileInZip(URL url, String relativePath) {

        return resourceReader.fetchFileInZip(url, relativePath);
    }
}