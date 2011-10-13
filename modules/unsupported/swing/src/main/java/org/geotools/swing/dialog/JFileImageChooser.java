/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.geotools.util.logging.Logging;

/**
 * A file chooser dialog for common raster image format files. It provides 
 * static methods to display the dialog for opening or saving an image file.
 * The file formats offered by the dialog are a subset of those supported by
 * {@code ImageIO} on the host system.
 *
 * <pre><code>
 * // Prompting for an input image file
 * File file = JFileImageChooser.showOpenFile();
 * if (file != null) {
 *     ...
 * }
 *
 * // Prompting for a file name to save an image
 * File file = JFileImageChooser.showSaveFile();
 * if (file != null) {
 *     ...
 * }
 * </code></pre>
 * 
 * @author Michael Bedward
 * @since 2.6.1
 * @source $URL$
 * @version $Id$
 */
public class JFileImageChooser extends JFileChooser {
    
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");

    /**
     * Constants for (possibly) supported image file formats.
     */
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
            System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
        }
    };

    private static final EnumSet<FormatSpecifier> supportedReaders = 
            EnumSet.noneOf(FormatSpecifier.class);
    
    private static final EnumSet<FormatSpecifier> supportedWriters = 
            EnumSet.noneOf(FormatSpecifier.class);
    
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

    /**
     * A file filter which works with the {@code FormatSpecifier} constants.
     * It is package-private, rather than private, for unit tests purposes.
     */
    static class FormatFilter extends FileFilter {
        private FormatSpecifier format;

        public FormatFilter(FormatSpecifier format) {
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


    /**
     * Prompts for file name to save an image.
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showSaveFile() {
        return showSaveFile(null);
    }
    
    /**
     * Prompts for file name to save an image.
     *
     * @param parent parent component (may be {@code null})
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showSaveFile(Component parent) {
        return showSaveFile(parent, null);
    }

    /**
     * Prompts for file name to save an image.
     *
     * @param parent parent component (may be {@code null})
     * @param workingDir the initial directory
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showSaveFile(final Component parent, final File workingDir) {
        final File[] file = new File[1];
        
        if (SwingUtilities.isEventDispatchThread()) {
            file[0] = doShow(parent, workingDir, SAVE_DIALOG);
            
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        file[0] = doShow(parent, workingDir, SAVE_DIALOG);
                    }
                });
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "Thread interrupted while prompting for file", ex);
                
            } catch (InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, "Unexpected problem while prompting for file", ex);
            }
        }
        
        return file[0];
    }
    
    /**
     * Prompts for file name to read an image.
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showOpenFile() {
        return showOpenFile(null, null);
    }

    /**
     * Prompts for file name to read an image.
     *
     * @param parent parent component (may be {@code null})
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showOpenFile(Component parent) {
        return showOpenFile(parent, null);
    }

    /**
     * Prompts for file name to read an image.
     *
     * @param parent parent component (may be {@code null})
     * @param workingDir the initial directory
     *
     * @return the selected file or {@code null} if the dialog was cancelled
     */
    public static File showOpenFile(final Component parent, final File workingDir) {
        final File[] file = new File[1];
        
        if (SwingUtilities.isEventDispatchThread()) {
            file[0] = doShow(parent, workingDir, OPEN_DIALOG);
            
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        file[0] = doShow(parent, workingDir, OPEN_DIALOG);
                    }
                });
            } catch (InterruptedException ex) {
                LOGGER.log(Level.SEVERE, "Thread interrupted while prompting for file", ex);
                
            } catch (InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, "Unexpected problem while prompting for file", ex);
            }
        }
        
        return file[0];
    }
    
    /**
     * Helper method which creates and displays the chooser dialog on the event dispatch thread.
     * 
     * @param parent optional parent component
     * @param workingDir optional initial working directory
     * @param openOrSave either {@linkplain #OPEN_DIALOG} or {@linkplain #SAVE_DIALOG}
     * 
     * @return selected file or {@code null} if the dialog was cancelled
     */
    private static File doShow(Component parent, File workingDir, int openOrSave) {
        JFileImageChooser chooser = new JFileImageChooser(workingDir);
        File file = null;
        int dialogRtnValue;

        switch (openOrSave) {
            case OPEN_DIALOG:
                chooser.setFilter(supportedReaders);
                chooser.setDialogTitle("Open image");
                dialogRtnValue = chooser.showOpenDialog(parent);
                break;
                
            case SAVE_DIALOG:
                chooser.setFilter(supportedWriters);
                chooser.setDialogTitle("Save image");
                dialogRtnValue = chooser.showSaveDialog(parent);
                break;
                
            default:
                // just in case
                throw new IllegalArgumentException(
                        "Invalid value for openOrSave argument" + openOrSave);
        }

        if (dialogRtnValue == JFileImageChooser.APPROVE_OPTION) {
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
     * Private constructor.
     *
     * @param workingDir the initial directory
     */
    private JFileImageChooser(File workingDir) {
        super(workingDir);
    }


    /**
     * Validates the selected file name.
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
                        getParent(), sb.toString(), "Confirm file name",
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
     * Helper method called by {@linkplain #doShow(java.awt.Component, java.io.File, int)
     * to set file filters.
     *
     * @param supportedFormats formats to base filters on
     */
    private void setFilter(Set<FormatSpecifier> supportedFormats) {
        this.setAcceptAllFileFilterUsed(false);
        for (final FormatSpecifier format : supportedFormats) {
            addChoosableFileFilter(new FormatFilter(format));
        }
    }


}
