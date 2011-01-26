/*
 *    GeoTools - The Open Source Java GIS Tookit
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Inserts the @source tag into class header javadocs. Only modifies source files that
 * meet the following criteria:
 * <ul>
 * <li> Contain a public class or interface
 * <li> Already have a class header javadoc comment block
 * <li> Do not already have the @source tag present
 * </ul>
 *
 * Adapted from the CommentUpdater class previously in this package that was written
 * by Martin Desruisseaux.
 *
 * @author Michael Bedward
 * @source $URL$
 * @version $Id$
 */
public class InsertSourceTag {

    private final Pattern findSVNLine = Pattern.compile(".+\\/(trunk|tags|branches)\\/.*\\.java");
    private final Pattern findCommentStart = Pattern.compile("^\\s*\\Q/**\\E");
    private final Pattern findCommentEnd = Pattern.compile("\\Q*/\\E");
    private final Pattern findSourceTag = Pattern.compile("^(\\s|\\*)*\\Q@source\\E");
    private final Pattern findVersionTag = Pattern.compile("^(\\s|\\*)*\\Q@version\\E");
    private final Pattern findClass = Pattern.compile("\\s*public[a-zA-Z\\s]+(class|interface)");

    private final String lineSeparator = System.getProperty("line.separator", "\n");

    /**
     * Main method. Takes the name of the file or directory to process from the
     * first command line argument provided (only the first is examined). If a
     * directory, all child directories and java source files will be processed.
     * <p>
     * Note: local backup files are <b>not</b> saved by this program.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("usage: InsertSourceTag fileOrDirName");
        }

        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("Can't find " + file);
            return;
        }

        InsertSourceTag me = new InsertSourceTag();
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
                    ex.printStackTrace();
                    System.exit(1);
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
        List<String> buffer = new ArrayList<String>();

        boolean inCommentBlock = false;
        boolean nonEmptyLines = false;
        boolean completedSearch = false;

        int commentStartLine = -1;
        int commentEndLine = -1;
        int sourceTagLine = -1;

        Matcher matcher = null;
        String text;
        String sourceTagText;

        /**
         * Find the svn repo path: trunk, tags or branches
         */
        matcher = findSVNLine.matcher(file.getAbsolutePath());
        if (matcher.matches()) {
            int pos = matcher.start(1);

            StringBuilder sb = new StringBuilder(" * @source $URL: ");
            sb.append("http://svn.somewhere.foo/org/geotools/");
            sb.append(file.getAbsolutePath().substring(pos));
            sb.append(" $");
            sourceTagText = sb.toString();

        } else {
            // don't process this file
            return false;
        }

        LineNumberReader reader = new LineNumberReader( new FileReader(file) );

        /**
         * We want the first line of text to be line 0 so
         * that line numbers match buffer indices
         */
        reader.setLineNumber(-1);
        
        while ( (text = reader.readLine()) != null ) {
            buffer.add(text);
            
            if (completedSearch) {
                continue;
            }

            if (inCommentBlock) {
                matcher = findCommentEnd.matcher(text);
                if (matcher.find()) {
                    inCommentBlock = false;
                    commentEndLine = reader.getLineNumber();
                }
                
            } else {
                matcher = findCommentStart.matcher(text);
                if (matcher.find()) {
                    inCommentBlock = true;
                    nonEmptyLines = false;
                    commentStartLine = reader.getLineNumber();

                } else {
                    matcher = findClass.matcher(text);
                    if (matcher.find()) {

                        /*
                         * If no javadoc comment block preceded the class header
                         * there is nothing to do
                         */
                        if (commentStartLine < 0) {
                            return false;
                        }

                        /* If there were any non-blank lines between the comment and
                         * the class header we will act safely and not modify the file
                         */
                        if (nonEmptyLines) {
                            return false;
                        }

                        /*
                         * Check if the source tag already exists
                         */
                        for (int i = commentStartLine; i <= commentEndLine; i++) {
                            matcher = findSourceTag.matcher(buffer.get(i));
                            if (matcher.find()) {
                                return false;
                            }
                        }

                        /*
                         * Check if the version tag exists. If it does we
                         * will place the source tag on the line before it
                         */
                        for (int i = commentStartLine; i <= commentEndLine; i++) {
                            matcher = findVersionTag.matcher(buffer.get(i));
                            if (matcher.find()) {
                                sourceTagLine = i;
                                break;
                            }
                        }

                        if (sourceTagLine < 0) {
                            sourceTagLine = commentEndLine;
                        }

                        completedSearch = true;

                    } else {
                        /**
                         * Not a comment line or the class header. Check if it is
                         * a non-emptyLine
                         */
                        if (text.trim().length() > 0) {
                            nonEmptyLines = true;
                        }
                    }
                }
            }
        }

        reader.close();

        /*
         * Close the input file and call the writing method
         * that will insert the source tag
         */
        if (completedSearch) {
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
    private boolean writeFile(File file, List<String> buffer, int sourceTagLine, String sourceTag)
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
