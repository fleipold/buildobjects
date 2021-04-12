package org.buildobjects.artifacts.resources;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.buildobjects.util.MixedUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:20:47 PM
 */
public class Path {
    final String[] components;

    public static Path path(String path){
        return new Path(path);
    }

    public static Path path(String... components){
        return new Path(components);
    }

    public Path(String... components) {
        this.components = components;
    }

    public Path(List<String> components){
        this.components = components.toArray(new String[components.size()]);
    }


    public Path(String path){
        if (path.equals("")){
            components = new String[0];
        } else {
            components = MixedUtils.relativePathCleaner(path).split("/");            
        }
    }

    public String[] getComponents() {
        return components;
    }

    public String getName(){
        return components[components.length -1];        
    }

    /** returns null if this is a toplevel path */
    public Path getParent(){
        if (components.length==1){
            return null;
        }
        return new Path(Arrays.copyOf(components, components.length-1));
    }


    public static Path forPacakgeName(String packageName){
        return new Path(packageName.split("\\."));
    }

    public String toRelativePathString() {
        return StringUtils.join(components, "/");
    }

    public String toString() {
        return toRelativePathString();
    }

    public Path child(String name) {
        String [] comps = Arrays.copyOf(components, components.length + 1);
        comps[components.length] = name;
        return new Path(comps);
    }

    public String toFullyQualifiedClassName() {

        String baseName = getName().replace(".java", "").replace(".class", "");
        if (getParent() != null)
            return getParent().toPackageName()+ "." + baseName;
        return baseName;
    }

    private String toPackageName() {
        return StringUtils.join(components, ".");
    }

    public boolean isJava() {
        return getName().endsWith(".java");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path path = (Path) o;

        if (!Arrays.equals(components, path.components)) return false;

        return true;
    }

    public int hashCode() {
        return (components != null ? Arrays.hashCode(components) : 0);
    }

    public boolean startsWith(Path prefix) {
        if (components.length < prefix.components.length){
            return false;
        }

        for (int i = 0; i < prefix.components.length; i++) {
            if (!components[i].equals(prefix.components[i])){
                return false;
            }
        }
        return true;
    }

    public Path removePrefix(Path prefix) {
        if (!startsWith(prefix)){
            throw new IllegalArgumentException("Path has to start with prefix that is to be removed");
        }
        List<String> newComponents = asList(components).subList(prefix.components.length, components.length);
        return new Path(newComponents);
    }

    public Path concatenate(Path path) {
        List<String> newPath = new ArrayList<String>(components.length + path.components.length);
        newPath.addAll(asList(components));
        newPath.addAll(asList(path.components));
        return new Path(newPath);
    }

    public File toFile(){
        return new File(toRelativePathString());
    }


}
