package org.buildobjects.artifacts.resources;

/**
 * User: fleipold
 * Date: Nov 3, 2008
 * Time: 11:04:42 AM
 */
public class ResourcesRenderer {
    private String string;


    public ResourcesRenderer(Resources toRender) {
        StringBuffer buffer = new StringBuffer();
                for(Resource res : toRender.getAll()){
                    buffer.append(res.getPath().toRelativePathString()+"\n");
                }
              this.string = buffer.toString();

    }


}
