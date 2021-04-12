package org.buildobjects.tasks;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * User: fleipold
 * Date: Oct 16, 2008
 * Time: 12:13:11 PM
 */
public class ManifestBuilder {
    String classPath;
    String mainClass;

    public ManifestBuilder classPath(String classPath){
        this.classPath = classPath;
        return this;
    }

    public ManifestBuilder mainClass(String mainClass){
        this.mainClass = mainClass;
        return this;
    }

    public Manifest toManifest(){
        Manifest mf = new Manifest();
        mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION,"1.0");
                

        setIfNotNull(mf, Attributes.Name.MAIN_CLASS, mainClass);
        setIfNotNull(mf, Attributes.Name.CLASS_PATH, classPath);
        return mf;
    }

    private void setIfNotNull(Manifest mf, Attributes.Name attributeName, String attributeValue) {
        if (attributeValue!=null){
            mf.getMainAttributes().put(attributeName, attributeValue);
        }
    }


}
