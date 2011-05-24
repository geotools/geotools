/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.maven.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.maven.taglet.Source;

/**
 * This program adds the @source tag to class header javadocs. Optionally, it will also
 * replace an existing @source tag with a new one. The tag is used to generate the module
 * module listing in the class javadocs. For example, this tag:
 * <pre><code>
 * &#64source http://svn.osgeo.org/geotools/trunk/modules/library/api/src/main/java/org/geotools/data/DataStore.java
 * </code></pre>
 * Will result in this content at the end of the class header javadocs:
 * <p>
 * <b>Module:</b><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;modules/library/api (gt-api.jar)<br>
 * <b>Source repository:</b><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;http://svn.osgeo.org/geotools/trunk/modules/library/api/src/main/java/org/geotools/data/DataStore.java
 * <p>
 * To run this program on the command line using Maven:
 * <pre><code>
 * cd /topgtdir/build/maven/javadoc
 * mvn exec:java
 * </code></pre>
 * 
 * <p>
 * Adapted from the CommentUpdater class previously in this package that was written
 * by Martin Desruisseaux.
 *
 * @author Michael Bedward
 * @source $URL$
 * @version $Id$
 */
public class InsertSourceTag {

    private final Pattern findSVNLine = Pattern.compile(".+\\/(trunk|tags|branches)\\/.*\\.java");
    
    private final Pattern findJavadocStart = Pattern.compile("^\\s*\\Q/**\\E");
    
    private final Pattern findCommentStart = Pattern.compile("^\\s*\\Q/*\\E([^\\*]|$)");
    
    private final Pattern findCommentEnd = Pattern.compile("\\Q*/\\E");
    
    private final Pattern findSourceTag = Pattern.compile("^.*?\\Q@source\\E");
    
    private final Pattern findCompleteSourceTag = Pattern.compile(
            "^.*?\\Q@source\\E(.*?)\\Q.java\\E\\s*\\$?");
    
    private final Pattern findVersionTag = Pattern.compile("^(\\s|\\*)*\\Q@version\\E");
    
    private final Pattern findPublicClass = Pattern.compile(
            "\\s*public[a-zA-Z\\s]+(class|interface|enum)");
    
    private final Pattern findClass = Pattern.compile(".*?(class|interface|enum)");
    
    private final Pattern findAnnotation = Pattern.compile("^@[a-zA-Z]+");
    
    private final String lineSeparator = System.getProperty("line.separator", "\n");
    
    private static final String REPLACE_OPTION = "--replace";
    private boolean replaceExistingTag;
    
    private static final String SVN_OPTION = "--svn";
    private boolean addSVNKeyword;
    
    private static final String ANY_CLASS_OPTION = "--anyclass";
    private boolean processAnyClass;

    /**
     * Main method. Takes the name of the file or directory to process from the
     * first command line argument provided (only the first is examined). If a
     * directory, all child directories and java source files will be processed.
     * <p>
     * Note: local backup files are <b>not</b> saved by this program.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("usage: InsertSourceTag {options} fileOrDirName");
            System.out.println("options:");
            System.out.println("   " + REPLACE_OPTION
                    + ": Replaces existing source tags (default no replacement)");
            System.out.println("   " + SVN_OPTION
                    + ": Add the svn URL keyword (omitted by default)");
            System.out.println("   " + ANY_CLASS_OPTION
                    + ": Process any class (default is only public classes)");
            return;
        }

        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("Can't find " + file);
            return;
        }

        InsertSourceTag me = new InsertSourceTag();

        for (int i = 1; i < args.length; i++) {
            String s = args[i].trim();
            if (REPLACE_OPTION.equals(s)) {
                me.replaceExistingTag = true;
            } else if (SVN_OPTION.equals(s)) {
                me.addSVNKeyword = true;
            } else if (ANY_CLASS_OPTION.equals(s)) {
                me.processAnyClass = true;
            } else {
                System.out.println("Unrecognized option: " + s);
                return;
            }
        }

        me.process(file);
    }

    /**
     * Process the given file or directory. If a directory, this method will
     * be called recursively for all child directories and files.
     *
     * @param file the file or directory to be processed
     */
    private void process(File file) {

        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                process(child);
            }
        } else {
            if (file.getName().endsWith(".java")) {
                try {
                    System.out.println(file.getPath());
                    processFile(file);

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * This method performs the task of searching the file for a public class or interface
     * and its associated javadoc comment block. If found, the comment block is searched
     * for a source tag which, if absent, will be generated and inserted into the file.
     *
     * @param file file to process
     * @return true if the source tag was inserted into the file; false otherwise
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private boolean processFile(File file) throws FileNotFoundException, IOException {
        Matcher matcher = null;
        String sourceTagText;

        /*
         * Find the svn repo path: trunk, tags or branches
         */
        matcher = findSVNLine.matcher(file.getAbsolutePath());
        if (matcher.matches()) {
            int pos = matcher.start(1);

            String repoURL = Source.SVN_REPO_URL;
            StringBuilder sb = new StringBuilder(" * @source ");
            if (addSVNKeyword) {
                sb.append("$URL: ");
            }
            sb.append(Source.SVN_REPO_URL);
            sb.append(file.getAbsolutePath().substring(pos));
            if (addSVNKeyword) {
                sb.append(" $");
            }
            sourceTagText = sb.toString();

        } else {
            // don't process this file
            System.out.println("   --- skipped this file");
            System.out.println();
            return false;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> buffer = new ArrayList<String>();
        String line;

        while ((line = reader.readLine()) != null) {
            buffer.add(line);
        }
        reader.close();

        /*
         * Search the buffer for class header docs and, within that,
         * the @source tag
         */
        boolean inJavadocBlock = false;
        boolean inCommentBlock = false;
        boolean unknownPrecedingContent = false;
        boolean classFound = false;

        int javadocStartLine = -1;
        int javadocEndLine = -1;
        int sourceTagLine = -1;

        for (int lineNo = 0; sourceTagLine < 0 && lineNo < buffer.size(); lineNo++) {
            String text = buffer.get(lineNo);

            if (inJavadocBlock || inCommentBlock) {
                matcher = findCommentEnd.matcher(text);
                if (matcher.find()) {
                    if (inJavadocBlock) {
                        inJavadocBlock = false;
                        javadocEndLine = lineNo;
                    } else if (inCommentBlock) {
                        inCommentBlock = false;
                    } else {
                        System.out.println("   *** Mis-placed end marker for comment block "
                                + "- skipping this file ***");
                        System.out.println();
                        return false;
                    }
                }

            } else if (findJavadocStart.matcher(text).find()) {
                inJavadocBlock = true;
                unknownPrecedingContent = false;
                javadocStartLine = lineNo;

            } else if (findCommentStart.matcher(text).find()) {
                inCommentBlock = true;

            // Guard against nested or following classes and mention of classes in
            // comment blocks
            } else if (!inJavadocBlock && !inCommentBlock && !classFound) {
                if (processAnyClass) {
                    matcher = findClass.matcher(text);
                } else {
                    matcher = findPublicClass.matcher(text);
                }
                if (matcher.find()) {
                    classFound = true;
                    /*
                     * If no javadoc comment block preceded the class header
                     * there is nothing to do
                     */
                    if (javadocStartLine < 0) {
                        System.out.println("   *** No class javadocs - skipping file ***");
                        System.out.println();
                        return false;
                    }

                    /* If there were any non-blank lines between the comment and
                     * the class header we will act safely and not modify the file
                     */
                    if (unknownPrecedingContent) {
                        System.out.println("   *** Javadocs do not directly precede class"
                                + " - skipping file ***");
                        System.out.println();
                        return false;
                    }

                    /*
                     * Check if the source tag already exists. If it does, and
                     * the replace tag option is false, skip this file.
                     */
                    for (int i = javadocStartLine; i <= javadocEndLine; i++) {
                        String commentText = buffer.get(i);
                        matcher = findSourceTag.matcher(commentText);
                        if (matcher.find()) {
                            /* 
                             * Check that those pesky Eclipse users haven't
                             * split the source tag across multiple lines with
                             * their auto-format thing.
                             */
                            matcher = findCompleteSourceTag.matcher(commentText);
                            if (!matcher.find()) {
                                System.out.println("   *** Incomplete source tag detected"
                                        + "- skipping this file ***");
                                System.out.println();
                                return false;
                            }

                            if (replaceExistingTag) {
                                sourceTagLine = i;
                                // delete the original tag from the buffer
                                buffer.remove(i);
                                break;
                            } else {
                                return false;
                            }

                        }
                    }

                    if (sourceTagLine < 0) {
                        /*
                         * Check if the version tag exists. If it does we
                         * will place the source tag on the line before it
                         */
                        for (int i = javadocStartLine; i <= javadocEndLine; i++) {
                            matcher = findVersionTag.matcher(buffer.get(i));
                            if (matcher.find()) {
                                sourceTagLine = i;
                                break;
                            }
                        }
                    }

                    if (sourceTagLine < 0) {
                        sourceTagLine = javadocEndLine;
                    }

                } else {
                    /*
                     * Not a comment line or the class header. Check if it is
                     * a non-emptyLine
                     */
                    if (text.trim().length() > 0) {
                        // Annotations are OK
                        matcher = findAnnotation.matcher(text);
                        if (!matcher.find()) {
                            unknownPrecedingContent = true;
                        }
                    }
                }
            }
        }

        /*
         * If the search was successful write the results to file
         */
        if (sourceTagLine > 0) {
            return writeFile(file, buffer, sourceTagLine, sourceTagText);
        }

        return false;
    }

    /**
     * Writes the file with a newly generated source tag in the class header
     * javadocs
     *
     * @param file the file to write
     * @param buffer file contents
     * @param sourceTagLine line number for the new source tag
     * @param sourceTag text for the new source tag
     *
     * @return always returns true
     * 
     * @throws IOException
     */
    private boolean writeFile(File file, List<String> buffer,
            int sourceTagLine, String sourceTag)
            throws IOException {

        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < buffer.size(); i++) {
            if (i == sourceTagLine) {
                writer.write(" *" + lineSeparator);
                writer.write(sourceTag);
                writer.write(lineSeparator);
            }

            writer.write(buffer.get(i));
            writer.write(lineSeparator);
        }
        writer.close();

        return true;
    }
}
