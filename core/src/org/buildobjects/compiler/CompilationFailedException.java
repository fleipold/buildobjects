package org.buildobjects.compiler;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;

/**
 * User: fleipold
 * Date: Oct 19, 2008
 * Time: 9:32:08 PM
 */
public class CompilationFailedException extends RuntimeException {
    private final List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public CompilationFailedException(List<Diagnostic<? extends JavaFileObject>> diagnostics) {

        this.diagnostics = diagnostics;
    }

    public String getMessage() {
        return CompilerWrapper.formatDiagnostics(diagnostics);

    }
}
