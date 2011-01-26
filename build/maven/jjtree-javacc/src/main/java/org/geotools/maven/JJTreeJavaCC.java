/*
 * Copyright 2001-2005 The Codehaus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geotools.maven;

// J2SE dependencies
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.io.IOException;

// JavaCC dependencies
import org.javacc.parser.Main;
import org.javacc.jjtree.JJTree;

// Maven and Plexus dependencies
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;
import org.codehaus.plexus.util.FileUtils;


// Note: javadoc in class and fields descriptions must be XHTML.
/**
 * Generates <code>.java</code> sources from <code>.jjt</code> files during Geotools build. This
 * <A HREF="http://maven.apache.org/maven2/">Maven 2</A> plugin executes <code>jjtree</code>
 * first, followed by <code>javacc</code>. Both of them are part of the
 * <A HREF="https://javacc.dev.java.net/">JavaCC</A> project.
 * <p/>
 * This code is a derived work from the Mojo
 * <code><A HREF="http://mojo.codehaus.org/maven-javacc-plugin/">maven-javacc-plugin</A></code>,
 * which explain why we retains the Apache copyright header. We didn't used The Mojo JavaCC plugin
 * because:
 * <p/>
 * <ul>
 *   <li>It seems easier to control execution order in a single plugin (obviously <code>jjtree</code>
 *       must be executed before <code>javacc</code>, but I don't know how to enforce this order if
 *       both of them are independent plugins registered in the <code>generate-sources</code> build
 *       phase).</li>
 *   <li><code>maven-javacc-plugin</code> overwrites the values specified in the <code>.jjt</code>
 *       file with its own default values, even if no such values were specified in the
 *       <code>pom.xml</code> file. This behavior conflicts with Geotools setting for the
 *       <code>STATIC</code> option.</li>
 * </ul>
 *
 * Note: The default directories in this plugin are Maven default, even if this plugin target
 *       Geotools build (which use a different directory structure).
 *
 * @goal generate
 * @phase generate-sources
 * @description Parses a JJT file and transform it to Java Files.
 * @source $URL$
 * @version $Id$
 *
 * @author jruiz
 * @author Jesse McConnell
 * @author Martin Desruisseaux
 */
public class JJTreeJavaCC extends AbstractMojo {
    /**
     * The package to generate the node classes into.
     *
     * @parameter expression=""
     * @required
     */
    private String nodePackage;

    /**
     * Directory where user-specified <code>Node.java</code> and <code>SimpleNode.java</code>
     * files are located. If no node exist, JJTree will create ones.
     *
     * @parameter expression="${basedir}/src/main/jjtree"
     * @required
     */
    private String nodeDirectory;

    /**
     * Directory where the JJT file(s) are located.
     *
     * @parameter expression="${basedir}/src/main/jjtree"
     * @required
     */
    private String sourceDirectory;

    /**
     * Directory where the output Java files will be located.
     *
     * @parameter expression="${project.build.directory}/generated-sources/jjtree-javacc"
     * @required
     */
    private String outputDirectory;

    /**
     * Concatenation of {@link #outputDirectory} with {@link #nodePackage}.
     * For internal use only.
     */
    private File outputPackageDirectory;

    /**
     * The directory to store the processed <code>.jjt</code> files.
     *
     * @parameter expression="${project.build.directory}/timestamp"
     */
    private String timestampDirectory;

    /**
     * The granularity in milliseconds of the last modification
     * date for testing whether a source needs recompilation
     *
     * @parameter expression="${lastModGranularityMs}" default-value="0"
     */
    private int staleMillis;

    /**
     * The Maven project running this plugin.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * Generates the source code from all {@code .jjt} and {@code .jj} files found in the source
     * directory. First, all {@code .jjt} files are processed using {@code jjtree}. Then, all
     * generated {@code .jj} files are processed.
     *
     * @throws MojoExecutionException if the plugin execution failed.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        // if not windows, don't rewrite file
        final boolean windowsOs = System.getProperty("os.name").indexOf("Windows") != -1;  
        
        outputPackageDirectory = createPackageDirectory(outputDirectory);
        if (!FileUtils.fileExists(timestampDirectory)) {
            FileUtils.mkdir(timestampDirectory);
        }
        /*
         * Copies the user-supplied Node.java files (if any) from the source directory  (by default
         * "src/main/jjtree") to the output directory (by default "target/generated-sources"). Only
         * java files found in the node package are processed.  NOTE: current version do not handle
         * properly subpackages.
         */
        final Set userNodes = searchNodeFiles();
        for (final Iterator it=userNodes.iterator(); it.hasNext();) {
            final File nodeFile = (File) it.next();
            try {
                FileUtils.copyFileToDirectory(nodeFile, outputPackageDirectory);
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to copy Node.java files for JJTree.", e);
            }
        }
        /*
         * Reprocess the .jjt files found in the source directory (by default "src/main/jjtree").
         * The default output directory is "generated-sources/jjtree-javacc" (it doesn't contains
         * javacc output yet, but it will).
         */
        final Set staleTrees = searchStaleGrammars(new File(sourceDirectory), ".jjt");
        for (final Iterator it=staleTrees.iterator(); it.hasNext();) {
            final File sourceFile = (File) it.next();
            final JJTree   parser = new JJTree();
            final String[]   args = generateJJTreeArgumentList(sourceFile.getPath());
            final int      status = parser.main(args);
            if (status != 0) {
                throw new MojoFailureException("JJTree failed with error code " + status + '.');
            }
            try {
                FileUtils.copyFileToDirectory(sourceFile, new File(timestampDirectory));
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to copy processed .jjt file.", e);
            }
        }
        /*
         * Reprocess the .jj files found in the generated-sources directory.
         */
        final Set staleGrammars = searchStaleGrammars(new File(outputDirectory), ".jj");
        for (final Iterator it=staleGrammars.iterator(); it.hasNext();) {
            final File sourceFile = (File) it.next();
            try {
                if (windowsOs) {
                    fixHeader(sourceFile);
                }
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to fix header for .jj file.", e);
            }
            final String[]   args = generateJavaCCArgumentList(sourceFile.getPath());
            final int      status;
            try {
                status = Main.mainProgram(args);
            } catch (Exception e) {
                throw new MojoExecutionException("Failed to run javacc.", e);
            }
            if (status != 0) {
                throw new MojoFailureException("JavaCC failed with error code " + status + '.');
            }
            try {
                FileUtils.copyFileToDirectory(sourceFile, new File(timestampDirectory));
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to copy processed .jj file.", e);
            }
        }
        /*
         * Reprocess generated java files so that they won't contain invalid escape characters
         */
        if (windowsOs) {
            try {
                String[] files = FileUtils.getFilesFromExtension(outputDirectory, new String[] {"java"});
                for (int i = 0; i < files.length; i++) {
                    System.out.println("Fixing " + files[i]);
                    fixHeader(new File(files[i]));
                }
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to fix header for java file.", e);
            }
        }
        /*
         * Add the generated-sources directory to the compilation root for the remaining
         * maven build.
         */
        if (project != null) {
            project.addCompileSourceRoot(outputDirectory);
        }
    }

    /**
     * Takes a file generated from javacc, and changes the first line so that it does not
     * contain escape characters on windows (the filename may contain things like \ u 
     * which are invalid escape chars)
     *
     * @param sourceFile the file to process.
     * @throws IOException if the file can't be read or the resutl can't be writen.
     */
    private void fixHeader(final File sourceFile) throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        File fixedFile = new File(sourceFile.getParentFile(), sourceFile.getName() + ".fix");
        try {
            reader = new BufferedReader(new FileReader(sourceFile));
            writer = new BufferedWriter(new FileWriter(fixedFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("/*@bgen(jjtree) Generated By:JJTree:") || 
                        line.startsWith("/* Generated By:JJTree:"))
                {
                    line = line.replace('\\', '/');
                }
                writer.write(line);
                writer.newLine();
            }
        } finally {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
        }
        sourceFile.delete();
        fixedFile.renameTo(sourceFile);
    }

    /**
     * Returns the concatenation of {@code directory} with {@link #nodePackage}. This is used in
     * order to construct a directory path which include the Java package. The directory will be
     * created if it doesn't exists.
     */
    private File createPackageDirectory(final String directory) throws MojoExecutionException {
        File packageDirectory = new File(directory);
        if (nodePackage!=null && nodePackage.trim().length()!=0) {
            packageDirectory = new File(packageDirectory, nodePackage.replace('.', '/'));
            if (!packageDirectory.exists()) {
                if (!packageDirectory.mkdirs()) {
                    throw new MojoExecutionException("Failed to create the destination directory.");
                }
            }
        }
        return packageDirectory;
    }

    /**
     * Gets the set of user-specified {@code Node.java} files. If none are found, {@code jjtree}
     * will generate automatically a default one. This method search only in the package defined
     * in the {@link #nodePackage} attribute.
     */
    private Set searchNodeFiles() throws MojoExecutionException {
        final SuffixMapping mapping    = new SuffixMapping(".java", ".java");
        final SuffixMapping mappingCAP = new SuffixMapping(".JAVA", ".JAVA");
        final SourceInclusionScanner scanner = new StaleSourceScanner(staleMillis);
        scanner.addSourceMapping(mapping);
        scanner.addSourceMapping(mappingCAP);
        File directory = new File(nodeDirectory);
        if (nodePackage!=null && nodePackage.trim().length()!=0) {
            directory = new File(directory, nodePackage.replace('.', '/'));
        }
        if (!directory.isDirectory()) {
            return Collections.EMPTY_SET;
        }
        final File outDir = new File(timestampDirectory);
        try {
            return scanner.getIncludedSources(directory, outDir);
        } catch (InclusionScanException e) {
            throw new MojoExecutionException("Error scanning \"" + directory.getPath() +
                                             "\" for Node.java to copy.", e);
        }
    }

    /**
     * Gets the set of {@code .jjt} or {@code .jj} files to reprocess.
     *
     * @param sourceDir The source directory.
     * @param ext The extension to search of ({@code .jjt} or {@code .jj}).
     */
    private Set searchStaleGrammars(final File sourceDir, final String ext)
            throws MojoExecutionException
    {
        final String        extCAP     = ext.toUpperCase();
        final SuffixMapping mapping    = new SuffixMapping(ext,    ext);
        final SuffixMapping mappingCAP = new SuffixMapping(extCAP, extCAP);
        final SourceInclusionScanner scanner = new StaleSourceScanner(staleMillis);
        scanner.addSourceMapping(mapping);
        scanner.addSourceMapping(mappingCAP);
        final File outDir = new File(timestampDirectory);
        try {
            return scanner.getIncludedSources(sourceDir, outDir);
        } catch (InclusionScanException e) {
            throw new MojoExecutionException("Error scanning source root \"" + sourceDir.getPath() +
                                             "\" for stale grammars to reprocess.", e);
        }
    }

    /**
     * Gets the arguments to pass to {@code jjtree}.
     *
     * @param  sourceFilename The {@code .jjt} file name (including the path).
     * @return The arguments to pass to {@code jjtree}.
     */
    private String[] generateJJTreeArgumentList(final String sourceFilename) {
        final List argsList = new ArrayList();
        if (nodePackage!=null && nodePackage.trim().length()!=0) {
            argsList.add("-NODE_PACKAGE:" + nodePackage);
        }
        argsList.add("-OUTPUT_DIRECTORY:" + outputPackageDirectory.getPath());
        argsList.add(sourceFilename);
        getLog().debug("jjtree arguments list: " + argsList.toString());
        return (String[]) argsList.toArray(new String[argsList.size()]);
    }

    /**
     * Gets the arguments to pass to {@code javacc}.
     *
     * @param  sourceFilename The {@code .jj} file name (including the path).
     * @return The arguments to pass to {@code javacc}.
     */
    private String[] generateJavaCCArgumentList(final String sourceInput) {
        final List argsList = new ArrayList();
        argsList.add("-OUTPUT_DIRECTORY:" + outputPackageDirectory.getPath());
        argsList.add(sourceInput);
        getLog().debug("javacc arguments list: " + argsList.toString());
        return (String[]) argsList.toArray(new String[argsList.size()]);
     }
}
