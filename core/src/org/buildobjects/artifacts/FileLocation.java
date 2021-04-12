package org.buildobjects.artifacts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: fleipold
 * Date: Oct 1, 2008
 * Time: 3:06:05 PM
 */
public class FileLocation implements Location {
    private final File baseDir;

    private Map<String, FileSources> sourcesMap = new HashMap<String, FileSources>();

    public FileLocation(File baseDir) {
        if (!baseDir.exists()){
            throw new IllegalArgumentException("Base dir "+baseDir.getPath()+ " does not exist");
        }
        this.baseDir = baseDir;
    }

    public FileLocation(String baseDir) {
        this(new File(baseDir));
    }

    public String getName(){
        try {
            return baseDir.getCanonicalFile().getName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Sources src(String relativePath){
        if (
            sourcesMap.containsKey(relativePath)){
            return sourcesMap.get(relativePath);
        }

        final FileSources sources = new FileSources(new File(baseDir, relativePath), relativePath);
        sourcesMap.put(relativePath, sources);
        return sources;
    }


    public Sources src(String relativePath, String sourceName){
            if (
                sourcesMap.containsKey(relativePath)){
                return sourcesMap.get(  relativePath);
            }

            final FileSources sources = new FileSources(new File(baseDir, relativePath), sourceName);
            sourcesMap.put(relativePath, sources);
            return sources;
        }


    public Classes jarFile(String relativePath){
        return new JarClasses(new File(baseDir,relativePath));
    }
    public Classes jarDir(String relativePath){
            return new JarDirClasses(new File(baseDir, relativePath));
    }

    

   public Location child(String relativePath){
       return new FileLocation(new File(baseDir,relativePath));
   }

    public File file(String relativePath) {
        final File file = new File(baseDir, relativePath);
        if (!file.exists()){
            throw new IllegalArgumentException("File "+file.getPath()+" does not exist");
        }
        return file;
    }

}
