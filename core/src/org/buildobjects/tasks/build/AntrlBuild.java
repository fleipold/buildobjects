package org.buildobjects.tasks.build;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang.StringUtils;
import org.buildobjects.Build;
import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.FileSources;
import org.buildobjects.artifacts.Sources;
import org.buildobjects.classpath.ClassesLoader;
import org.buildobjects.tasks.Antlr;
import org.buildobjects.util.DelegateProvider;
import org.buildobjects.util.DelegatingProxy;
import org.buildobjects.util.MainRunner;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * User: fleipold
 * Date: Oct 27, 2008
 * Time: 2:00:14 PM
 */
class AntrlBuild extends AbstractTask implements Antlr {
    final private Sources sources;
    private Sources actualGeneratedSources;
    private List<File> inputGrammars;
    private final Classes antlr;

    public AntrlBuild(Build build, Classes antlr, File... inputGrammars) {
        super(build);
        this.antlr = antlr;
        this.inputGrammars = Arrays.asList(inputGrammars);
        sources =  DelegatingProxy.create(Sources.class, new DelegateProvider(){

            public Object delegate() {
                build();
                return actualGeneratedSources;
            }
        }, getClass().getClassLoader());

    }

    protected void buildInternal() {
        File targetFolder = build.getTempFolder();
        MainRunner runner = new MainRunner("antlr.Tool", new ClassesLoader(antlr));

        for (File grammarFile : inputGrammars) {
            runner.run("-o",targetFolder.getPath(),grammarFile.getPath());

        }
        actualGeneratedSources = new FileSources(targetFolder);
    }

    public String getName() {
        Collection<String> sourceNames = CollectionUtils.collect(inputGrammars, new Transformer<File, String>() {
            public String transform(File file) {
                return file.getName();
            }
        });
        return "Antlr" + StringUtils.join(sourceNames, ", ");
    }

    public Sources sources() {
        return sources;
    }
}
