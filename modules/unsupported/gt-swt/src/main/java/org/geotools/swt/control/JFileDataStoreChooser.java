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
package org.geotools.swt.control;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
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
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class JFileDataStoreChooser {
    private static final long serialVersionUID = -7482109609487216939L;
    private FileDialog fileDialog;

    /**
     * Create a dialog that filters for files with the specified extension.
     *
     * @param extension the file extension, with or without the leading '.'
     */
    public JFileDataStoreChooser( Shell parent, int style, final String extension ) {
        this(parent, style, new String[]{extension});
    }

    static Map<String, String> associations( List<String> extensions ) {
        Map<String, String> fileAssociations = new TreeMap<String, String>();

        for( String extension : extensions ) {
            String ext = extension.toLowerCase().trim();
            if (!ext.startsWith("*.")) {
                if (ext.startsWith(".")) {
                    ext = "*" + ext;
                } else {
                    ext = "*." + ext;
                }
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
    public JFileDataStoreChooser( Shell parent, int style, final List<String> extensions ) {
        this(parent, style, associations(extensions));
    }

    /**
     * Create a dialog that filters for files with the specified extensions.
     *
     * @param extensions the file extensions, with or without the leading '.'
     */
    public JFileDataStoreChooser( Shell parent, int style, final String[] extensions ) {
        this(parent, style, associations(Arrays.asList(extensions)));
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
    public JFileDataStoreChooser( Shell parent, int style, final Map<String, String> fileAssociations ) {
        fileDialog = new FileDialog(parent, style);
        init(fileAssociations);
    }

    /**
     * Helper method for constructors that creates file filters.
     *
     * @param fileAssociations a {@code Map} where keys are extensions (with or
     *        wirhout the leading dot) and values are descriptions.
     */
    private void init( final Map<String, String> fileAssociations ) {
        String[] exts = new String[fileAssociations.size()];
        String[] descr = new String[fileAssociations.size()];
        int i = 0;
        for( final Entry<String, String> entry : fileAssociations.entrySet() ) {

            exts[i] = entry.getKey();
            descr[i] = entry.getValue();
            i++;
        }
        fileDialog.setFilterExtensions(exts);
        fileDialog.setFilterNames(descr);
    }

    /**
     * Creates a dialog that filters for files matching the specified
     * data format. 
     *
     * @param format data file format
     */
    public JFileDataStoreChooser( Shell parent, int style, final FileDataStoreFactorySpi format ) {
        fileDialog = new FileDialog(parent, style);
        fileDialog.setFilterExtensions(format.getFileExtensions());
    }

    public FileDialog getFileDialog() {
        return fileDialog;
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
    public static File showOpenFile( String extension, Shell parent ) {
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
    public static File showOpenFile( String extension, File initialDir, Shell parent ) {
        JFileDataStoreChooser dialog = new JFileDataStoreChooser(parent, SWT.OPEN, extension);
        FileDialog fileDialogInternal = dialog.getFileDialog();
        if (initialDir != null) {
            if (initialDir.isDirectory()) {
                fileDialogInternal.setFilterPath(initialDir.getAbsolutePath());
            } else {
                fileDialogInternal.setFilterPath(initialDir.getParentFile().getAbsolutePath());
            }
        }

        fileDialogInternal.open();
        String filePath = fileDialogInternal.getFileName();
        File file = new File(filePath);
        return file;
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
    public static File showOpenFile( String[] extensions, Shell parent ) {
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
    public static File showOpenFile( String[] extensions, File initialDir, Shell parent ) {

        JFileDataStoreChooser dialog = new JFileDataStoreChooser(parent, SWT.OPEN, extensions);
        FileDialog fileDialogInternal = dialog.getFileDialog();
        if (initialDir != null) {
            if (initialDir.isDirectory()) {
                fileDialogInternal.setFilterPath(initialDir.getAbsolutePath());
            } else {
                fileDialogInternal.setFilterPath(initialDir.getParentFile().getAbsolutePath());
            }
        }

        String filePath = fileDialogInternal.open();
        if (filePath != null) {
            File file = new File(filePath);
            return file;
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
    public static File showOpenFile( FileDataStoreFactorySpi format, Shell parent ) {
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
    public static File showOpenFile( FileDataStoreFactorySpi format, File initialDir, Shell parent ) {

        JFileDataStoreChooser dialog = new JFileDataStoreChooser(parent, SWT.OPEN, format);
        FileDialog fileDialogInternal = dialog.getFileDialog();
        if (initialDir != null) {
            if (initialDir.isDirectory()) {
                fileDialogInternal.setFilterPath(initialDir.getAbsolutePath());
            } else {
                fileDialogInternal.setFilterPath(initialDir.getParentFile().getAbsolutePath());
            }
        }

        fileDialogInternal.open();
        String filePath = fileDialogInternal.getFileName();
        File file = new File(filePath);
        return file;
    }

    /**
     * Demonstrates the file data store dialog by prompting for a shapefile
     *
     * @param arg ignored
     */
    public static void main( String arg[] ) {
        Display display = new Display();
        Shell shell = new Shell(display);
        File file = JFileDataStoreChooser.showOpenFile("shp", shell);
        if (file != null) {
            MessageDialog.openInformation(shell, "INFO", "Selected " + file.getAbsolutePath());
        } else {
            MessageDialog.openInformation(shell, "INFO", "Selection cancelled");
        }
    }
    /**
     * Consider the provided file as a candidate for a new filename. 
     * A number will be appended to the filename if there is a
     * conflict.
     * 
     * @param file the candidate file name
     */
    public void setSaveFile( File file ) {
        String path = file.getAbsolutePath();
        int split = path.lastIndexOf('.');
        String base;
        String extension;
        if (split == -1) {
            base = path;
            extension = "";
        } else {
            base = path.substring(0, split);
            extension = path.substring(split);
        }
        File saveFile = new File(path);
        int number = 0;
        while( saveFile.exists() ) {
            saveFile = new File(base + (number++) + extension);
        }
        fileDialog.setFileName(saveFile.getAbsolutePath());
    }
}
