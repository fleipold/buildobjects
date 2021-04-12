package org.buildobjects.compiler;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URI;

/**
 * User: fleipold
 * Date: Sep 30, 2008
 * Time: 7:59:05 PM
 */
public class InMemoryWriteFileObject implements JavaFileObject {
    private final String fullyQualifiedName;
    private final JavaFileObject delegate;
    ByteArrayOutputStream bout;


    public InMemoryWriteFileObject(String fullyQualifiedName, JavaFileObject delegate) {
        this.delegate = delegate;

        this.fullyQualifiedName = fullyQualifiedName;

    }


    public Kind getKind() {
        return delegate.getKind();
    }

    public boolean isNameCompatible(String s, Kind kind) {
        return delegate.isNameCompatible(s, kind);
    }

    public NestingKind getNestingKind() {
        return delegate.getNestingKind();
    }

    public Modifier getAccessLevel() {
        return delegate.getAccessLevel();
    }

    public URI toUri() {
        return delegate.toUri();
    }

    public String getName() {
        return delegate.getName();
    }

    public OutputStream openOutputStream() throws IOException {
        bout = new ByteArrayOutputStream();
        return bout;    
    }

    public Writer openWriter() throws IOException {
        return new OutputStreamWriter(openOutputStream());
    }


    public byte[] getContent(){
        return bout.toByteArray();
    }

    public CharSequence getCharContent(boolean b) throws IOException {
            return null;
        }


    public InputStream openInputStream() throws IOException {
        return null;
    }

    public Reader openReader(boolean b) throws IOException {
        return null;
    }


    public long getLastModified() {
        return 0;
    }

    public boolean delete() {
        return false;
    }
}
