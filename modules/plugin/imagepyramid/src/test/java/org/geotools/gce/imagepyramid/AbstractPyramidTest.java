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
package org.geotools.gce.imagepyramid;

import static org.apache.commons.io.filefilter.FileFilterUtils.and;
import static org.apache.commons.io.filefilter.FileFilterUtils.nameFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.or;
import static org.apache.commons.io.filefilter.FileFilterUtils.suffixFileFilter;

import java.io.File;
import java.io.FileFilter;
import org.geotools.referencing.CRS;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AbstractPyramidTest {
    protected static final Double DELTA = 1E-6;

    @BeforeClass
    public static void init() {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        CRS.reset("all");
    }

    @AfterClass
    public static void close() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    protected void cleanFiles(File mosaicFolder) {
        FileFilter filter =
                or(
                        suffixFileFilter("db"),
                        suffixFileFilter("sample_image"),
                        and(
                                suffixFileFilter(".properties"),
                                notFileFilter(
                                        or(
                                                nameFileFilter("indexer.properties"),
                                                nameFileFilter("datastore.properties")))));
        for (File configFile : mosaicFolder.listFiles(filter)) {
            configFile.delete();
        }
    }
}
