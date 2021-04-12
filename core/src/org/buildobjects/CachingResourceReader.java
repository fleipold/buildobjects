package org.buildobjects;

import org.apache.commons.codec.digest.DigestUtils;
import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.artifacts.Initializer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

public class CachingResourceReader implements ResourceReader {
    final private File baseDir;

    public CachingResourceReader(File baseDir) {
        this.baseDir = baseDir;
        if (!baseDir.exists()){
            baseDir.mkdirs();
        }
    }


    public File fetchFile(URL url) {
        String path = new Path(url.getPath()).getParent().toRelativePathString();
        String fileName = new Path(url.getPath()).getName();
        String dirName = DigestUtils.md5Hex((url.getHost() + "/" + path));
        File localDir = new File(baseDir, "cache/" + dirName);
        File file = new File(localDir, fileName);
        if (file.exists()) {
            return file;
        }
        localDir.mkdirs();
        try {
            downloadToFile(url, file);
            return file;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private  void downloadToFile(URL url, File file) throws IOException {
        FileOutputStream fout = null;
        InputStream inputStream = null;
        try {
            fout = new FileOutputStream(file);


            URLConnection connection = url.openConnection();
            connection.setReadTimeout(5000);
            inputStream = connection.getInputStream();

            IOUtils.copy(inputStream, fout);
        }
        finally {
        IOUtils.closeQuietly(fout);
        IOUtils.closeQuietly(inputStream);
        }

    }

    public File fetchFileInZip(URL url, String relativePath) {

        String path = url.getPath() + "/" + new Path(relativePath).getParent().toRelativePathString();
        String fileName = new Path(relativePath).getName();
            
        String dirName = DigestUtils.md5Hex((url.getHost() + "/" + path));
        File localDir = new File(baseDir, "cache/" + dirName);
        File file = new File(localDir, fileName);
        if (file.exists()) {
            return file;
        }
        localDir.mkdirs();
        try {
            File tempFile = File.createTempFile("somezip", ".zip");
            downloadToFile(url, tempFile);
            ZipFile zip = new ZipFile(tempFile);
            ZipEntry entry = zip.getEntry(relativePath);
            if (entry == null) {
                throw new RuntimeException("File " + relativePath + " does not exist in " + url);
            }

            InputStream inputStream = zip.getInputStream(entry);
            FileOutputStream fout = new FileOutputStream(file);
            IOUtils.copy(inputStream, fout);

            fout.close();
            inputStream.close();
            return file;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}