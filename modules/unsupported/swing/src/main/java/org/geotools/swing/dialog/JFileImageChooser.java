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

package org.geotools.swing.dialog;

import java.awt.Component;
import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.swing.data.JParameterListWizard;

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
 * @author Michael Bedward
 * @since 2.6.1
 *
 * @source $URL$
 * @version $Id$
 */
public class JFileImageChooser extends JFileChooser {

    private static enum FormatSpecifier {
        BMP("bmp", "BMP image", ".bmp"),
        GIF("gif", "GIF image", ".gif"),
        JPG("jpg", "JPEG image", ".jpg", ".jpeg"),
        PNG("png", "PNG image", "png"),
        TIF("tif", "TIFF image", ".tif", ".tiff");

        private String id;
        private String desc;
        private String[] suffixes;

        private FormatSpecifier(String id, String desc, String ...suffixes) {
            this.id = id;
            this.desc = desc;
            this.suffixes = new String[suffixes.length];
            for (int i = 0; i < suffixes.length; i++) {
                this.suffixes[i] = suffixes[i];
            }
        }
    };

    private static final Set<FormatSpecifier> supportedReaders = new TreeSet<FormatSpecifier>();
    private static final Set<FormatSpecifier> supportedWriters = new TreeSet<FormatSpecifier>();
    static {
        for (FormatSpecifier format : FormatSpecifier.values()) {
            if (ImageIO.getImageReadersBySuffix(format.id).hasNext()) {
                supportedReaders.add(format);
            }

            if (ImageIO.getImageWritersBySuffix(format.id).hasNext()) {
                supportedWriters.add(format);
            }
        }
    }

    private static class FormatFilter extends FileFilter {
        private FormatSpecifier format;

        FormatFilter(FormatSpecifier format) {
            this.format = format;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            for (String suffix : format.suffixes) {
                if (f.getPath().endsWith(suffix) ||
                    f.getPath().endsWith(suffix.toUpperCase())) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public String getDescription() {
            return format.desc;
        }

        public String getDefaultSuffix() {
            return format.suffixes[0];
        }
    }

    /*
     * Create a new image file chooser
     */
    public JFileImageChooser() {
        this(null);
    }

    /**
     * Create a new image file chooser
     *
     * @param workingDir the initial directory to display
     */
    public JFileImageChooser(File workingDir) {
        super(workingDir);
    }


    /**
     * Overridden method to validate the dialog content prior to closing.
     */
    @Override
    public void approveSelection() {
        FormatFilter filter = (FormatFilter) getFileFilter();
        File file = getSelectedFile();
        String name = file.getAbsolutePath();
        int dot = name.lastIndexOf('.');

        boolean ok = true;

        if (dot < 0) {
            /*
             * No file extension. Set the default one
             */
            name = name + filter.getDefaultSuffix();
            file = new File(name);
            setSelectedFile(file);
        }

        if (this.getDialogType() == JFileImageChooser.SAVE_DIALOG) {
            if (!filter.accept(getSelectedFile())) {
                StringBuilder sb = new StringBuilder();
                sb.append("'");
                sb.append(name.substring(dot + 1));
                sb.append("' ");
                sb.append("is not a standard suffix for a ");
                sb.append(filter.getDescription());
                sb.append(".");
                sb.append("\nDo you want to save with this name ?");

                int answer = JOptionPane.showConfirmDialog(
                        getParent(), sb.toString(), "Incompatible file suffix",
                        JOptionPane.YES_NO_OPTION);

                ok = answer == JOptionPane.YES_OPTION;
            }

            if (ok && file.exists()) {
                int answer = JOptionPane.showConfirmDialog(
                        this,
                        "Overwrite the existing file ?",
                        "File exists",
                        JOptionPane.YES_NO_OPTION);

                ok = answer == JOptionPane.YES_OPTION;
            }

        } else {
            if (!file.exists()) {
                JOptionPane.showMessageDialog(
                        this, "Can't file this file", "File not found", JOptionPane.WARNING_MESSAGE);
                ok = false;
            }
        }

        if (ok) {
            super.approveSelection();
        }
    }

    /**
     * Set the file filters. This is a helper for the static showXXXXFile methods.
     *
     * @param supportedFormats the set of file formats that will be offered
     */
    private void setFilter(Set<FormatSpecifier> supportedFormats) {
        this.setAcceptAllFileFilterUsed(false);
        for (final FormatSpecifier format : supportedFormats) {
            addChoosableFileFilter(new FormatFilter(format));
        }
    }


    /**
     * Display a dialog to choose a file name to save an image to
     *
     * @param parent parent component (may be {@code null})
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showSaveFile(Component parent) {
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
    public static File showSaveFile(Component parent, File workingDir) {
        JFileImageChooser chooser = new JFileImageChooser(workingDir);
        chooser.setFilter(supportedWriters);
        chooser.setDialogTitle("Save image");

        File file = null;

        if (chooser.showSaveDialog(parent) == JFileImageChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();

            String name = file.getAbsolutePath();
            int dot = name.lastIndexOf('.');

            FormatFilter filter = (FormatFilter) chooser.getFileFilter();
            if (dot < 0) {
                name = name + filter.getDefaultSuffix();
                file = new File(name);
            }
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
    public static File showOpenFile(Component parent) {
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
    public static File showOpenFile(Component parent, File workingDir) {
        JFileImageChooser chooser = new JFileImageChooser(workingDir);
        chooser.setFilter(supportedReaders);
        chooser.setDialogTitle("Open image file");

        File file = null;

        if (chooser.showOpenDialog(parent) == JFileImageChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        }

        return file;
    }
}
