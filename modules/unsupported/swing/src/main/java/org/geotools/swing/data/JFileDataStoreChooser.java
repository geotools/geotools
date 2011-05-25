/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swing.data;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.FileDataStoreFinder;

/**
 * A file chooser dialog to get user choices for data stores.
 * <p>
 * Examples of use:
 * <pre>{@code
 * // prompt the user for a shapefile
 * File file = JFileDataStoreChooser.showOpenFile("shp", parentFrame);
 * if (file != null) {
 *    ...
 * }
 *
 * // prompt the user for a given data format
 *
 * }</pre>
 *
 * @author Jody Garnett
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class JFileDataStoreChooser extends JFileChooser {
    private static final long serialVersionUID = -7482109609487216939L;

    /**
     * Create a dialog that filters for files with the specified extension.
     *
     * @param extension the file extension, with or without the leading '.'
     */
    public JFileDataStoreChooser(final String extension) {
        this(new String[]{extension});
    }

    static Map<String, String> associations( List<String> extensions ){
        Map<String, String> fileAssociations = new TreeMap<String, String>();

        for (String extension : extensions) {
            String ext = extension.toLowerCase().trim();
            if (!ext.startsWith(".")) {
                ext = "." + ext;
            }

            FileDataStoreFactorySpi factory = FileDataStoreFinder.getDataStoreFactory(ext);
            if (factory != null) {
                fileAssociations.put(ext, factory.getDescription());

            } else {
                // guess some common ones
                if (".csv".equals(ext)) {
                    fileAssociations.put(ext, "Comma-delimited files (*.csv)");

                } else if (ext.startsWith(".tif")) {
                    fileAssociations.put(ext, "GeoTIFF files (*.tif; *.tiff)");

                } else {
                    // fallback
                    fileAssociations.put(ext, ext.toUpperCase().substring(1) + "files (*" + ext + ")");
                }
            }
        }
        return fileAssociations;
    }
    
    /**
     * Create a dialog that filters for files with the specified extensions.
     *
     * @param extensions the file extensions, with or without the leading '.'
     */
    public JFileDataStoreChooser(final List<String> extensions) {
        this( associations( extensions ));
    }
    
    /**
     * Create a dialog that filters for files with the specified extensions.
     *
     * @param extensions the file extensions, with or without the leading '.'
     */
    public JFileDataStoreChooser(final String[] extensions) {
        this( associations( Arrays.asList(extensions)));
    }

    /**
     * Creates a dialog based on the given file associations.
     *
     * <pre><code>
     * Map<String, String> assoc = new HashMap<String, String>();
     * assoc.put(".foo", "Foo data files (*.foo)");
     * assoc.put(".bar", "Bar data files (*.bar)");
     * JFileDataStoreChooser chooser = new JFileDataStoreChooser(assoc);
     * </code></pre>
     *
     * @param fileAssociations a {@code Map} where keys are extensions (with or
     *        wirhout the leading dot) and values are descriptions.
     */
    public JFileDataStoreChooser(final Map<String, String> fileAssociations) {
        init( fileAssociations );
    }

    /**
     * Helper method for constructors that creates file filters.
     *
     * @param fileAssociations a {@code Map} where keys are extensions (with or
     *        wirhout the leading dot) and values are descriptions.
     */
    private void init(final Map<String, String> fileAssociations) {

        for (final String ext : fileAssociations.keySet()) {
            addChoosableFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }

                    for (String ext : fileAssociations.keySet()) {
                        if (f.getPath().endsWith(ext) ||
                                f.getPath().endsWith(ext.toUpperCase())) {
                            return true;
                        }
                    }

                    return false;
                }

                @Override
                public String getDescription() {
                    return fileAssociations.get(ext);
                }
            });
        }
    }

    /**
     * Creates a dialog that filters for files matching the specified
     * data format. 
     *
     * @param format data file format
     */
    public JFileDataStoreChooser(final FileDataStoreFactorySpi format) {

        setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                for (String ext : format.getFileExtensions()) {
                    if (f.getPath().endsWith(ext)) {
                        return true;
                    }
                    if (f.getPath().endsWith(ext.toUpperCase())) {
                        return true;
                    }
                }
                return false;
            }

            public String getDescription() {
                return format.getDescription();
            }
        });
    }

    /**
     * Show a file open dialog that filters for files with the given extension.
     *
     * @param extension file extension, with or without leading '.'
     * @param parent parent GUI component (may be {@code null})
     *
     * @return the selected file or null if the user cancelled the selection
     * @throws java.awt.HeadlessException if run in an unsupported environment
     */
    public static File showOpenFile(String extension, Component parent)
            throws HeadlessException {
        return showOpenFile(extension, null, parent);
    }

    /**
     * Show a file open dialog that filters for files with the given extension.
     *
     * @param extension file extension, with or without leading '.'
     * @param initialDir initial directory to display; if {@code null} the initial directory
     *        will be the user's default directory
     * @param parent parent GUI component (may be {@code null})
     *
     * @return the selected file or null if the user cancelled the selection
     * @throws java.awt.HeadlessException if run in an unsupported environment
     */
    public static File showOpenFile(String extension, File initialDir, Component parent)
            throws HeadlessException {
        JFileDataStoreChooser dialog = new JFileDataStoreChooser(extension);
        if (initialDir != null) {
            if (initialDir.isDirectory()) {
                dialog.setCurrentDirectory(initialDir);
            } else {
                dialog.setCurrentDirectory(initialDir.getParentFile());
            }
        }
        
        if (dialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return dialog.getSelectedFile();
        }
        
        return null;
    }

    /**
     * Show a file open dialog that filters for files with the given extensions.
     *
     * @param extensions array of file extension, with or without leading '.'
     * @param parent parent GUI component (may be null)
     *
     * @return the selected file or null if the user cancelled the selection
     * @throws java.awt.HeadlessException if run in an unsupported environment
     */
    public static File showOpenFile(String[] extensions, Component parent)
            throws HeadlessException {
        return showOpenFile(extensions, null, parent);
    }

    /**
     * Show a file open dialog that filters for files with the given extensions.
     *
     * @param extensions array of file extension, with or without leading '.'
     * @param initialDir initial directory to display; if {@code null} the initial directory
     *        will be the user's default directory
     * @param parent parent GUI component (may be null)
     *
     * @return the selected file or null if the user cancelled the selection
     * @throws java.awt.HeadlessException if run in an unsupported environment
     */
    public static File showOpenFile(String[] extensions, File initialDir, Component parent)
            throws HeadlessException {

        JFileDataStoreChooser dialog = new JFileDataStoreChooser(extensions);
        if (initialDir != null) {
            if (initialDir.isDirectory()) {
                dialog.setCurrentDirectory(initialDir);
            } else {
                dialog.setCurrentDirectory(initialDir.getParentFile());
            }
        }

        if (dialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return dialog.getSelectedFile();
        }

        return null;
    }

    /**
     * Show a file open dialog that filters for files that match a given file
     * data store format
     *
     * @param format the file data store format
     * @param parent parent GUI component (may be null)
     *
     * @return the selected file or null if the user cancelled the selection
     * @throws java.awt.HeadlessException if run in an unsupported environment
     */
    public static File showOpenFile(FileDataStoreFactorySpi format, Component parent)
            throws HeadlessException {
        return showOpenFile(format, null, parent);
    }

    /**
     * Show a file open dialog that filters for files that match a given file
     * data store format
     *
     * @param format the file data store format
     * @param initialDir initial directory to display; if {@code null} the initial directory
     *        will be the user's default directory
     * @param parent parent GUI component (may be null)
     *
     * @return the selected file or null if the user cancelled the selection
     * @throws java.awt.HeadlessException if run in an unsupported environment
     */
    public static File showOpenFile(FileDataStoreFactorySpi format, File initialDir, Component parent)
            throws HeadlessException {

        JFileDataStoreChooser dialog = new JFileDataStoreChooser(format);
        if (initialDir != null) {
            if (initialDir.isDirectory()) {
                dialog.setCurrentDirectory(initialDir);
            } else {
                dialog.setCurrentDirectory(initialDir.getParentFile());
            }
        }


        if (dialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return dialog.getSelectedFile();
        }
        return null;
    }

    /**
     * Demonstrates the file data store dialog by prompting for a shapefile
     *
     * @param arg ignored
     */
    public static void main(String arg[]) {
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file != null) {
            JOptionPane.showMessageDialog(null, "Selected " + file.getPath());
        } else {
            JOptionPane.showMessageDialog(null, "Selection cancelled");
        }
    }
    /**
     * Consider the provided file as a candidate for a new filename. 
     * A number will be appended to the filename if there is a
     * conflict.
     * 
     * @param file the candidate file name
     */
    public void setSaveFile(File file) {
        String path = file.getAbsolutePath();
        int split = path.lastIndexOf('.');
        String base;
        String extension;
        if( split == -1 ){
            base = path;
            extension = "";
        }
        else {
            base = path.substring(0, split);
            extension = path.substring(split);
        }
        File saveFile = new File( path );
        int number = 0;
        while( saveFile.exists() ){
            saveFile = new File( base+(number++)+extension );            
        }
        setSelectedFile( saveFile );        
    }
}
