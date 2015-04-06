/*    (C) 2014 Open Source Geospaital Foundation (OSGeo)
 *    (C) 2012, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package html;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JFileChooser;

/**
 * The open document to RST conversion is great - except for the generated image names.
 * <p>
 * This application takes a file describing how you want to fix the problem:
 * 
 * <pre>
 * 10000000000002010000018F5D64B4D5.png=installer_admin.png
 * 10000000000002010000018F5E22EE7A.png=installer_location
 * 1000000000000081000001924E854422.jpg=splash_screen
 * 10000000000001010000007AC9F16190.png
 * </pre>
 * Files will be renamed if a new name is defined, the rename will preserving their extension.
 * <p>
 * The renames will also be used to update the RST files (as long as the final file exists).
 * <p>
 * You need to do a bit of set up prior to running this program:
 * <ul>
 * <li>Open up the file, and break any links to referenced images</li>
 * <li>Run odt2sphinx to convert the open office document file</li>
 * <li>Create "rename.properties" using the format above</li>
 * <li>Run html.ImageRename index.rst rename.properties</li>
 * </ul>
 * I experimented with converting the images from PNG to jpeg to save space, but at least for my
 * docs it did not appear worthwhile.
 * 
 * @author jody
 */
public class ImageRename {
    private static final class TargetFileFilter extends javax.swing.filechooser.FileFilter {
        final String target;

        public TargetFileFilter(String target) {
            this.target = target;
        }

        public String getDescription() {
            return target + " file";
        }

        @Override
        public boolean accept(File f) {
            if( target.startsWith("*.")){
                return f.getName().endsWith( target.substring(1) );
            }
            return f.getName().equals(target);
        }
    }

    /**
     * Used to list html files for conversion.
     */
    private final class ExtensionFileFilter implements FileFilter {
        private String extension;

        public ExtensionFileFilter(String extension) {
            this.extension = extension;
        }

        public boolean accept(File file) {
            return file.getName().endsWith("." + extension);
        }
    }

    private File target;

    private Properties rename;

    public ImageRename(File rstDirectory, Properties rename) {
        this.target = rstDirectory;
        this.rename = rename;
    }

    private void process() {
        File imageDirectory = new File(target, "images");

        Map<String, String> moved = moveImages(imageDirectory, rename);

        File[] rstFileList = target.listFiles(new ExtensionFileFilter("rst"));
        for (File rstFile : rstFileList) {
            updateRstFile(rstFile, moved);
        }
    }

    private void updateRstFile(File rstFile, Map<String, String> moved) {
        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;

        String line;
        try {
            reader = new BufferedReader(new FileReader(rstFile));
            READ: while ((line = reader.readLine()) != null) {
                UPDATE: for (Entry<String, String> rename : moved.entrySet()) {
                    String search = rename.getKey();
                    String replace = rename.getValue();
                    if (line.contains(search)) {
                        String updated = line.replace(search, replace);
                        contents.append(updated);
                        contents.append("\n");
                        continue READ;
                    }
                }
                contents.append(line);
                contents.append("\n");
            }
        } catch (FileNotFoundException notFound) {
            System.err.println("Unable to locate " + rstFile + ":" + notFound);
            return;
        } catch (IOException io) {
            System.err.println("Unable to read " + rstFile + ":" + io);
            return;
        } finally {
            close(reader);
        }

        String text = contents.toString();
        replaceContents(rstFile, text);
    }

    public boolean replaceContents(File file, String text) {
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.err.println("Unable to replace " + file);
                return false;
            }
        }
        BufferedWriter writer = null;
        // write out modified copy
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
        } catch (IOException eek) {
            System.out.println("Trouble writing modified '" + file + "':" + eek);
            return false;
        } finally {
            close(writer);
        }
        return true;
    }

    String trimExtension(String path) {
        int split = path.lastIndexOf('.');
        if (split != -1) {
            return path.substring(0, split);
        }
        return path;
    }
    
    String extension(String path) {
        int split = path.lastIndexOf('.');
        if (split != -1) {
            return path.substring(split);
        }
        return "";
    }

    /**
     * Move the indicated images, returns a map of the search and replace to perform.
     * 
     * @param images
     * @param list
     * @return
     */
    private Map<String, String> moveImages(File images, Properties list) {
        Map<String, String> renamed = new HashMap<String, String>();
        for (Entry<Object, Object> entry : list.entrySet()) {
            String from = trimExtension((String) entry.getKey());
            String extension = extension( (String) entry.getKey() );
            
            String to = trimExtension((String) entry.getValue());

            if( to.isEmpty() ) continue; // skip
            
            File file = new File(images, from + extension);
            File move = new File(images, to + extension);
            
            if (!file.exists()) {
                // checking if it was already moved ...
                if (move.exists()) {
                    renamed.put(from, to);
                    continue; // fine then
                } else {
                    System.out.println("Unable to locate " + file + " to rename to " + to);
                    continue;
                }
            } else {
                boolean moved = file.renameTo(move);
                if (moved) {
                    renamed.put(from, to);
                }
            }
        }
        return renamed;
    }

    private void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        if (args.length == 1 && "?".equals(args[0])) {
            System.out.println(" Usage: java html.ImageRename [file.rst] [rename.properties]");
            System.out.println();
            System.out.println("Where:");
            System.out.println("  file.rst Used to locate your odt2sphinx files");
            System.out.println("  rename.properties used to rename files in your images folder");
            System.out.println();
            System.out
                    .println("If not provided the appication will prompt you for the above information");

            System.exit(0);
        }
        File rstFile = args.length > 0 ? new File(args[0]) : null;
        File renameFile = args.length > 1 ? new File(args[1]) : null;

        if (rstFile != null && rstFile.isDirectory()) {
            rstFile = new File(rstFile, "*.rst");
        }

        if (rstFile == null || !rstFile.exists()) {
            File cd = new File("user");

            JFileChooser dialog = new JFileChooser(cd);
            dialog.setFileFilter(new TargetFileFilter("index.rst"));
            dialog.setDialogTitle("Step 1: odt2sphinx RST file");

            int open = dialog.showDialog(null, "Rename Images");

            if (open == JFileChooser.CANCEL_OPTION) {
                System.out.println("Rename Canceled");
                System.exit(-1);
            }
            rstFile = dialog.getSelectedFile();
        }

        if (rstFile == null || !rstFile.exists() || rstFile.isDirectory()) {
            System.out
                    .println("Locate *.RST file generated from odt2sphinx: '" + rstFile + "'");
            System.exit(-1);
        }
        File rstDirectory = null;
        try {
            rstDirectory = rstFile.getParentFile().getCanonicalFile();
        } catch (IOException eek) {
            System.out.println("Could determine parent of " + rstFile + ":" + eek);
            System.exit(-1);
        }

        if (renameFile == null) {
            JFileChooser dialog = new JFileChooser(rstDirectory.getParentFile());
            dialog.setFileFilter(new TargetFileFilter("rename.properties"));
            dialog.setDialogTitle("Step 2: rename.properties");

            int open = dialog.showDialog(null, "Rename Images");

            if (open == JFileChooser.CANCEL_OPTION) {
                System.out.println("Rename images canceled");
                System.exit(-1);
            }
            renameFile = dialog.getSelectedFile();
        }
        Properties rename = properties(renameFile);
        if (rename == null) {
            System.out.println("No rename image information provided  " + renameFile);
            System.exit(-1);
        }
        ImageRename imageRename = new ImageRename(rstDirectory, rename);
        imageRename.process();
    }

    private static Properties properties(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            System.out.println("Warning: Unable to locate " + file);
            return null;
        }
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            properties.load(input);
        } catch (FileNotFoundException e) {
            System.out.println("Warning: Unable to locate " + file + ":" + e);
            return null;
        } catch (IOException e) {
            System.out.println("Warning: Unable to process " + file + ":" + e);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Warning: Trouble parsing " + file + ":" + e);
                }
            }
        }
        return properties;
    }

}
