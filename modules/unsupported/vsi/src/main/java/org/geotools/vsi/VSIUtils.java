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
import java.util.Arrays;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public final class VSIUtils {

    private static final String[] VSI_TYPES = {
        "zip",
        "gzip",
        "tar",
        // "stdin",
        // "stdout",
        "mem",
        "subfile",
        "sparse",
        "crypt",
        "curl",
        "curl_streaming",
        "s3",
        "s3_streaming",
        "gs",
        "gs_streaming",
        "az",
        "az_streaming",
        "adls",
        "oss",
        "oss_streaming",
        "swift",
        "swift_streaming",
        "hdfs",
        "webhdfs"
    };
    private static final String VSI_PATTERN =
            String.format(
                    "^(/vsi(%s)/)+[\\w\\d_\\-\\.]+/[\\w\\d_\\-\\./]+$",
                    String.join("|", Arrays.asList(VSI_TYPES)));

    /**
     * Determine if an input {@link String} is a valid VSI-prefixed Virtual File System location
     *
     * @param input {@link String} representation of a virtual location
     * @return true if the input is a valid VSI-prefixed Virtual File System location
     */
    public static boolean isVSILocation(String input) {
        return input == null ? false : input.matches(VSI_PATTERN);
    }

    /**
     * Get a filename from a source location
     *
     * @return {@String} of the filename with extension
     */
    public static String getFileName(String input) {
        if (isVSILocation(input)) {
            return input.substring(input.lastIndexOf('/') + 1);
        }

        throw new IllegalArgumentException("invalid VSI Virtual File System location");
    }

    /**
     * Open a {@link String} input as a GDAL {@link Dataset}, handling a null response as an {@link
     * IOException}
     *
     * @param input {@link String} representation of a virtual location
     * @return {@link Dataset} object containing the layer data
     * @throws IOException
     */
    public static Dataset openDataset(String input) throws IOException {
        final Dataset dataset = gdal.Open(input, gdalconstConstants.GA_ReadOnly);

        if (dataset == null) {
            throw new IOException("GDAL could not read source path: " + input);
        }

        return dataset;
    }

    /**
     * Add an alpha band to a VRT dataset in-place based on the dataset's mask.
     *
     * @param dataset The dataset object to add an alpha band to.
     */
    public static void addAlphaToVRT(Dataset dataset) {
        dataset.AddBand(gdalconstConstants.GDT_Byte);

        final Band alpha = dataset.GetRasterBand(dataset.getRasterCount());
        alpha.SetColorInterpretation(gdalconstConstants.GCI_AlphaBand);

        final String sourceData =
                (String) dataset.GetRasterBand(1).GetMetadata_Dict("vrt_sources").get("source_0");

        alpha.SetMetadataItem(
                "source_0",
                sourceData.replaceAll("<SourceBand>[0-9]+", "<SourceBand>mask"),
                "new_vrt_sources");

        dataset.FlushCache();
    }

    /**
     * Generate and create a VRT file that links any given GDAL {@link Dataset}, handling a null
     * response as a {@link IOException}
     *
     * @param source input GDAL {@link Dataset}
     * @param destination output VRT {@link File} object
     * @return output VRT file as a {@link Dataset}
     * @throws IOException
     */
    public static Dataset datasetToVRT(Dataset source, File destination) throws IOException {
        final String path = destination.getAbsolutePath();
        final Driver driver = gdal.GetDriverByName("VRT");
        final Dataset dataset = driver.CreateCopy(path, source);

        if (dataset == null) {
            throw new IOException("GDAL could not write dataset to file: " + path);
        }

        addAlphaToVRT(dataset);

        return dataset;
    }
}
