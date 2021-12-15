/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vsi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;
import org.gdal.gdal.Dataset;

/**
 * Abstraction of the VSI Virtual File System data type used to generate readable VRT virtual files
 *
 * @author Matthew Northcott <matthewnorthcott@catalyst.net.nz>
 */
public class VSIDataset {

    private final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(VSIDataset.class);

    private String location;
    private File file;

    /**
     * Constructor for /vsi-prefixed path to a VSI Virtual File System
     *
     * @param input source {@link String} path to a VSI Virtual File System
     */
    public VSIDataset(String input) {
        if (!VSIUtils.isVSILocation(input)) {
            throw new RuntimeException("could not parse invalid VSI location: " + input);
        }

        location = input;

        generate();
    }

    /**
     * Static method for creating a {@link VSIDataset} from a generic object type
     *
     * @param input source {@link Object} which is a {@link String} representing the path to a VSI
     *     Virtual File System
     * @return {@link VSIDataset} representation of the input
     */
    public static VSIDataset fromObject(Object input) {
        if (input instanceof String) {
            return new VSIDataset((String) input);
        }

        throw new RuntimeException("input object must be a String type");
    }

    /**
     * Get the VRT file representing the VSI Virtual File System
     *
     * @return {@link File} object of the VRT file
     */
    public File getFile() {
        return file;
    }

    /**
     * Generate a temporary VRT file based on the same name as the remote file. This file is deleted
     * on exit of the JVM
     *
     * @return {@link File} representing the temporary VRT file
     */
    private File getTempFile() {
        final String fileName = VSIUtils.getFileName(location) + ".vrt";
        final String tempDir = System.getProperty("java.io.tmpdir");
        final File temp = Paths.get(tempDir, fileName).toFile();

        temp.deleteOnExit();

        return temp;
    }

    /**
     * Copy the source dataset to a temporary VRT file, applying any GDAL configuration options
     * specified in vsi.properties before doing so
     *
     * @return generated VRT {@link File} object
     */
    public File generate() {
        // Apply any configuration in vsi.properties
        try {
            new VSIProperties().apply();
        } catch (IOException ex) {
            LOGGER.fine(ex.getMessage());
        }

        // Create temporary file to store VRT data
        final File temp = getTempFile();

        if (temp.exists()) {
            LOGGER.fine(temp.getAbsolutePath() + " already exists");
        } else {
            // Open virtual file system
            Dataset in = null;
            try {
                in = VSIUtils.openDataset(location);
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }

            LOGGER.fine("loaded data from " + location);

            // Convert virtual file system to VRT format and save to the temporary file created
            Dataset out = null;
            try {
                out = VSIUtils.datasetToVRT(in, temp);
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }

            LOGGER.fine("dataset exported to " + temp.getAbsolutePath());

            in.delete();
            out.delete();
        }

        file = temp;

        return temp;
    }
}
