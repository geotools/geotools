/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.geotools.util.logging.Logging;

/**
 * A collector of supportFiles.
 *
 * <p>It will look for ancillary files having same name but different extension, trying from a set
 * of possible values.
 */
public class SupportFilesCollector {

    static final Logger LOGGER = Logging.getLogger(SupportFilesCollector.class);

    private static final String PRJ = ".PRJ";

    private static final String PRJ_ = ".prj";

    private static final String TFW = ".TFW";

    private static final String TFW_ = ".tfw";

    private static final String WLD = ".WLD";

    private static final String WLD_ = ".wld";

    private static final String JGW = ".JGW";

    private static final String JGW_ = ".jgw";

    private static final String PGW = ".PGW";

    private static final String PGW_ = ".pgw";

    private static final String MSK = ".TIF.MSK";

    private static final String MSK_ = ".tif.msk";

    private static final String OVR = ".TIF.OVR";

    private static final String OVR_ = ".tif.ovr";

    static final Map<String, SupportFilesCollector> COLLECTORS;

    static {
        // Improve that, to be pluggable in future versions
        COLLECTORS = new HashMap<String, SupportFilesCollector>();
        String[] PNG_ = new String[] {WLD_, PRJ_, PGW_, WLD, PGW, PRJ};
        String[] JPG_ = new String[] {WLD_, PRJ_, JGW_, WLD, JGW, PRJ};
        String[] GIF_ = new String[] {WLD_, PRJ_, WLD, PRJ};
        String[] TIF_ = new String[] {TFW_, PRJ_, WLD_, OVR_, MSK_, TFW, WLD, PRJ, OVR, MSK};
        SupportFilesCollector PNG = new SupportFilesCollector(PNG_);
        SupportFilesCollector JPG = new SupportFilesCollector(JPG_);
        SupportFilesCollector TIF = new SupportFilesCollector(TIF_);
        SupportFilesCollector GIF = new SupportFilesCollector(GIF_);
        COLLECTORS.put("PNG", PNG);
        COLLECTORS.put("JPG", JPG);
        COLLECTORS.put("JPEG", JPG);
        COLLECTORS.put("GIF", GIF);
        COLLECTORS.put("TIF", TIF);
        COLLECTORS.put("TIFF", TIF);
    }

    /**
     * Gets the support file collector for the given file, or <code>null</code> if the file type is
     * not known (the extension will be evaluated in a case insensitive way)
     */
    public static SupportFilesCollector getCollectorFor(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        return COLLECTORS.get(extension.toUpperCase());
    }

    private String[] supportingExtensions;

    public SupportFilesCollector(String[] supportingExtensions) {
        this.supportingExtensions = supportingExtensions;
    }

    /** Return supportFiles (if found) for the specified file */
    public List<File> getSupportFiles(String filePath) {
        LinkedHashSet<File> supportFiles = null;
        String parent = FilenameUtils.getFullPath(filePath);
        String mainName = FilenameUtils.getName(filePath);
        String baseName = FilenameUtils.removeExtension(mainName);
        for (String extension : supportingExtensions) {
            String newFilePath = parent + baseName + extension;
            File file = new File(newFilePath);
            if (file.exists()) {
                if (supportFiles == null) {
                    supportFiles = new LinkedHashSet<>();
                }

                try {
                    supportFiles.add(file.getCanonicalFile());
                } catch (IOException e) {
                    LOGGER.log(Level.FINE, "Failed to canonicalize file, will add as is: " + file);
                    supportFiles.add(file);
                }
            }
        }
        return supportFiles == null ? null : new ArrayList<>(supportFiles);
    }
}
