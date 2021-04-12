package org.buildobjects.compiler;

import org.apache.commons.io.IOUtils;
import org.buildobjects.artifacts.resources.Resource;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: fleipold
 * Date: Oct 12, 2008
 * Time: 7:57:14 PM
 */
public class InMemoryReadFileObject implements JavaFileObject {
    private final Resource resource;

    public InMemoryReadFileObject(Resource resource) {
        this.resource = resource;
    }

    public Kind getKind() {
        if (getName().endsWith(".class")) return Kind.CLASS;
        if (getName().endsWith(".java")) return Kind.SOURCE;
        if (getName().endsWith(".html")) return Kind.HTML;
        else return Kind.OTHER;
    }

    public boolean isNameCompatible(String simpleName, Kind kind) {
    String baseName = simpleName + kind.extension;
        return kind.equals(getKind())
            && (baseName.equals(toUri().getPath())
                || toUri().getPath().endsWith("/" + baseName));
    }

    public NestingKind getNestingKind() {
        return null;
    }

    public Modifier getAccessLevel() {
        throw new UnsupportedOperationException("Can't get access level"+getName());
    }

    public URI toUri() {
        try {
            return new URI("inmem://resource/"+getName());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return resource.getPath().toRelativePathString();
    }

    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream(resource.getBytes());
    }

    public OutputStream openOutputStream() throws IOException {
throw new UnsupportedOperationException("Can't write this file!");
    }

    public Reader openReader(boolean b) throws IOException {
        return new InputStreamReader(openInputStream());
    }

    public CharSequence getCharContent(boolean b) throws IOException {
        final StringWriter writer = new StringWriter();
        IOUtils.copy(openReader(b), writer);
        return writer.toString();
    }

    public Writer openWriter() throws IOException {
        throw new UnsupportedOperationException("Can't write this file!");
    }

    public long getLastModified() {
        return 0;
    }

    public boolean delete() {
        throw new UnsupportedOperationException("Can't write this file!");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InMemoryReadFileObject that = (InMemoryReadFileObject) o;


        return resource == that.resource;
    }

    public int hashCode() {
        return (resource != null ? resource.hashCode() : 0);
    }

    public String toString() {
        return toUri().toString();
    }
}
