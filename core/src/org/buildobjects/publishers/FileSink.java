package org.buildobjects.publishers;

import java.io.OutputStream;

/**
 * User: fleipold
 * Date: Oct 14, 2008
 * Time: 4:25:15 PM
 */
public interface FileSink {
    OutputStream getOutputStream(String fileName);

    FileSink getChild(String name);


}
