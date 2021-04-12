package org.buildobjects;

import java.io.File;
import java.net.URL;

/**
 * User: fleipold
 * Date: Oct 8, 2009
 * Time: 11:20:03 PM
 */
public interface ResourceReader {
    File fetchFile(URL url);

    File fetchFileInZip(URL url, String relativePath);
}
