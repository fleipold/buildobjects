package org.buildobjects.util;

import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: fleipold
 * Date: Oct 20, 2008
 * Time: 4:04:59 PM
 */
public class SVNWrapper {
    /**
     * check out from url to workingCopy.
     * returns the revision
     */

    public static Pattern REVISION_PATTERN =  Pattern.compile(".*revision (\\d*)[^\\d]");
    public SVNRevision checkout(URL url, File workingCopy) {
        final String command = "svn co " + url.toExternalForm() + " " + workingCopy.getPath();
        String out = wrapProcess(command);

        final int revision = parseRevision(out);
        System.out.println("Updated to r"+revision);

        return new SVNRevision(revision);
    }

    public SVNRevision update(File workingCopy) {
        String output = wrapProcess("svn up  " + workingCopy.getPath());

        final int revision = parseRevision(output);

        System.out.println("Updated to r"+revision);
        return new SVNRevision(revision);
    }

    public SVNRevision getLatestRevision(URL repositoryUrl){
        return new SVNRevision(Integer.parseInt(getInfo(repositoryUrl).get("Revision")));
    }


    public Map<String, String> getInfo(URL repositoryUrl) {
        Map<String, String> map = new HashMap<String, String>();
        String output = wrapProcess("svn info  " + repositoryUrl.toExternalForm());
        LineIterator iter = new LineIterator(new StringReader(output));
        while (iter.hasNext()) {
            String line = (String) iter.next();
            if (line.indexOf(":")!=-1){
                String key = line.substring(0, line.indexOf(":"));
                String value = line.substring(line.indexOf(":")+2).trim();
                map.put(key, value);
            }

        }

        return map;
    }

    private String wrapProcess(String command) {
        Process2.StringOutputConsumer out = new Process2.StringOutputConsumer();
        Process2.StringOutputConsumer err = new Process2.StringOutputConsumer();

        Process2 process = new Process2(command, out, err);

        try {
            int ret = process.runBlocked();
            if (ret != 0) {
                throw new RuntimeException("SVN failed:\n" + err.toString());
            }
        } catch (Process2.StartupException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return out.toString();
    }

    private int parseRevision(String output) {
        String[] lines = output.split("\n");
        String lastLine = lines[lines.length - 1];
        Matcher matcher = REVISION_PATTERN.matcher(lastLine);
        matcher.matches();
        String revisionString = matcher.group(1);
        return Integer.parseInt(revisionString);
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println("Revision : "+new SVNWrapper().getLatestRevision(new URL("https://buildobjects.googlecode.com/svn/trunk/")));
    }

}
