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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * A file chooser dialog for common raster image format files.
 * It provides static methods to display the dialog for opening or
 * saving an image file with basic validation of user input.
 *
 * <pre><code>
 * // Prompting for an input image file
 * File file = JFileImageChooser.showOpenFile(null);
 * if (file != null) {
 *     ...
 * }
 *
 * // Prompting for a file name to save an image
 * File file = JFileImageChooser.showSaveFile(null);
 * if (file != null) {
 *     ...
 * }
 * </code></pre>
 *
 * The file formats offered by the dialog are a subset of those supported by
 * {@code ImageIO} on the host system.
 * <p>
 *
 * @see JFileDataStoreChooser
 * @see JParameterListWizard
 * @see ImageIO
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Michael Bedward
 *
 *
 *
 * @source $URL$
 */
public class JFileImageChooser {

    private static enum FormatSpecifier {
        TIF("tif", "TIFF image", "*.tif", "*.tiff"), //
        BMP("bmp", "BMP image", "*.bmp"), //
        GIF("gif", "GIF image", "*.gif"), //
        JPG("jpg", "JPEG image", "*.jpg", "*.jpeg"), //
        PNG("png", "PNG image", "*.png");

        private String id;
        private String[] suffixes;

        private FormatSpecifier( String id, String desc, String... suffixes ) {
            this.id = id;
            this.suffixes = new String[suffixes.length];
            for( int i = 0; i < suffixes.length; i++ ) {
                this.suffixes[i] = suffixes[i];
            }
        }
    };

    private static final Set<FormatSpecifier> supportedReaders = new TreeSet<FormatSpecifier>();
    private static final Set<FormatSpecifier> supportedWriters = new TreeSet<FormatSpecifier>();
    static {
        for( FormatSpecifier format : FormatSpecifier.values() ) {
            if (ImageIO.getImageReadersBySuffix(format.id).hasNext()) {
                supportedReaders.add(format);
            }

            if (ImageIO.getImageWritersBySuffix(format.id).hasNext()) {
                supportedWriters.add(format);
            }
        }
    }

    private FileDialog fileDialog;

    private List<String> extentionsList = new ArrayList<String>();

    /*
     * Create a new image file chooser
     */
    public JFileImageChooser( Shell parent, int style ) {
        this(parent, style, null);
    }

    /**
     * Create a new image file chooser
     *
     * @param workingDir the initial directory to display
     */
    public JFileImageChooser( Shell parent, int style, File workingDir ) {
        fileDialog = new FileDialog(parent, style);
        if (workingDir != null)
            fileDialog.setFilterPath(workingDir.getAbsolutePath());
    }

    /**
     * Set the file filters. This is a helper for the static showXXXXFile methods.
     *
     * @param supportedFormats the set of file formats that will be offered
     */
    private void setFilter( Set<FormatSpecifier> supportedFormats ) {
        for( final FormatSpecifier format : supportedFormats ) {
            String[] suffixes = format.suffixes;
            // tweak for swt filters
            for( int i = 0; i < suffixes.length; i++ ) {
                if (!suffixes[i].startsWith("*.")) {
                    if (suffixes[i].startsWith(".")) {
                        suffixes[i] = "*" + suffixes[i];
                    } else {
                        suffixes[i] = "*." + suffixes[i];
                    }
                }
                extentionsList.add(suffixes[i]);
            }
        }
        String[] extArray = (String[]) extentionsList.toArray(new String[extentionsList.size()]);
        fileDialog.setFilterExtensions(extArray);
    }

    public FileDialog getFileDialog() {
        return fileDialog;
    }

    /**
     * Display a dialog to choose a file name to save an image to
     *
     * @param parent parent component (may be {@code null})
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showSaveFile( Shell parent ) {
        return showSaveFile(parent, null);
    }

    /**
     * Display a dialog to choose a file name to save an image to
     *
     * @param parent parent component (may be {@code null})
     * @param workingDir the initial directory to display
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showSaveFile( Shell parent, File workingDir ) {
        JFileImageChooser chooser = new JFileImageChooser(parent, SWT.SAVE, workingDir);
        chooser.setFilter(supportedWriters);
        FileDialog dialog = chooser.getFileDialog();
        dialog.setText("Save image");

        String path = dialog.open();
        File file = null;
        if (path != null && path.length() >= 1) {
            file = new File(path);
        }
        return file;
    }

    /**
     * Display a dialog to choose an image file to open
     *
     * @param parent parent component (may be {@code null})
     * @param workingDir the initial directory to display
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showOpenFile( Shell parent ) {
        return showOpenFile(parent, null);
    }

    /**
     * Display a dialog to choose an image file to open
     *
     * @param parent parent component (may be {@code null})
     * @param workingDir the initial directory to display
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showOpenFile( Shell parent, File workingDir ) {
        JFileImageChooser chooser = new JFileImageChooser(parent, SWT.OPEN, workingDir);
        chooser.setFilter(supportedReaders);
        FileDialog dialog = chooser.getFileDialog();
        dialog.setText("Open image file");
        String path = dialog.open();
        File file = null;
        if (path != null && path.length() >= 1) {
            file = new File(path);
        }
        return file;
    }
}
