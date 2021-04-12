package org.buildobjects.compiler;

import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.ResourcesClasses;
import org.buildobjects.artifacts.Sources;
import org.buildobjects.artifacts.resources.InMemoryResources;
import org.buildobjects.artifacts.resources.WritableResources;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: fleipold
 * Date: Oct 12, 2008
 * Time: 12:29:44 PM
 */
class CompilerFileManager extends ForwardingJavaFileManager {
    
    private final WritableResources outputClasses = new InMemoryResources();
    final Map<String, ResourcesReadingFileManagerAdapter> resourcesAdapter;

    final Map<String, InMemoryWriteFileObject> writeFileIdentity = new HashMap<String, InMemoryWriteFileObject>(); 
    private Sources sources;


    public CompilerFileManager(StandardJavaFileManager fileManager) throws IOException {
        super(fileManager);
        resourcesAdapter = new HashMap<String, ResourcesReadingFileManagerAdapter>();

    }

    public void addAdapterForLocation(String locationName, ResourcesReadingFileManagerAdapter adapter){
        resourcesAdapter.put(locationName, adapter);
    }

                                                  
    public JavaFileObject getJavaFileForOutput(Location location, String fullyQualifiedName, JavaFileObject.Kind kind, FileObject fileObject) throws IOException {
        if (writeFileIdentity.containsKey(fullyQualifiedName)){
            return writeFileIdentity.get(fullyQualifiedName);
        }

        final InMemoryWriteFileObject file = new InMemoryWriteFileObject(fullyQualifiedName, super.getJavaFileForOutput(location, fullyQualifiedName, kind, fileObject));
        writeFileIdentity.put(fullyQualifiedName, file);
        outputClasses.add(new InMemoryFileWrappingResource(fullyQualifiedName, file));
        return file; 
    }

    public JavaFileObject getJavaFileForInput(Location location, String s, JavaFileObject.Kind kind) throws IOException {
        throw new UnsupportedOperationException("");
        
    }

    public FileObject getFileForInput(Location location, String s, String s1) throws IOException {
        throw new UnsupportedOperationException("");
    }

    public Classes getResult() {
        return new ResourcesClasses(outputClasses);

    }



    public boolean hasLocation(Location location) {
        return resourcesAdapter.containsKey(location.getName()) || location.getName().equals("CLASS_OUTPUT");
    }


    @Override
    public Iterable list(Location location, String packageName, Set kinds, boolean recurse) throws IOException {

        if (this.resourcesAdapter.containsKey(location.getName())){
            return resourcesAdapter.get(location.getName()).listClassPathFiles(packageName,kinds,recurse);
        }

        if (location.getName().equals("PLATFORM_CLASS_PATH")){
            return super.list(location, packageName, kinds, recurse); 
        }
        throw new UnsupportedOperationException();

    }


    public boolean isSameFile(FileObject first, FileObject second) {
        if (first instanceof InMemoryReadFileObject && second instanceof InMemoryReadFileObject) {
            return first.equals(second);
        }
        throw new UnsupportedOperationException();

    }

    public String inferBinaryName(Location location, JavaFileObject javaFileObject) {
        if (javaFileObject instanceof InMemoryReadFileObject) {

            final String path = javaFileObject.getName();
            final String pathWithoutExtension = path.substring(0, path.lastIndexOf("."));
            String name = pathWithoutExtension.replace("/", ".");

            return name;
        }
        return super.inferBinaryName(location, javaFileObject);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public FileObject getFileForOutput(Location location, String s, String s1, FileObject fileObject) throws IOException {
        throw new UnsupportedOperationException();
    }

    
    public void close() throws IOException {
        super.close();
    }
}
