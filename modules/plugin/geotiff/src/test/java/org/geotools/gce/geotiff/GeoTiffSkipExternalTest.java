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
 *
 */
package org.geotools.gce.geotiff;

import it.geosolutions.imageio.maskband.DatasetLayout;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.PrjFileReader;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Testing the external files (prjs, masks, overviews) aren't retrieved when the skip flag is on.
 */
public class GeoTiffSkipExternalTest extends org.junit.Assert {

    private static void setStaticField(Class<?> clazz, String fieldName, Object value)
            throws ReflectiveOperationException {

        // Playing with System.Properties and Static boolean fields can raises issues
        // when running Junit tests via Maven, due to initialization orders.
        // So let's change the fields via reflections for these tests
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }

    @BeforeClass
    public static void setup() throws ReflectiveOperationException {
        setStaticField(ImageIOUtilities.class, "SKIP_EXTERNAL_FILES_LOOKUP", true);
    }

    @AfterClass
    public static void teardown() throws ReflectiveOperationException {
        setStaticField(ImageIOUtilities.class, "SKIP_EXTERNAL_FILES_LOOKUP", false);
    }

    /** Test that the reader didn't access the external prj */
    @Test
    public void prjIsSkippedTest() throws IllegalArgumentException, IOException, FactoryException {

        final File noCrs = TestData.file(GeoTiffSkipExternalTest.class, "override/sample.tif");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(noCrs));
        GeoTiffReader reader = (GeoTiffReader) format.getReader(noCrs);
        CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();

        final File prj = TestData.file(GeoTiffSkipExternalTest.class, "override/sample.prj");
        final FileInputStream fis = new FileInputStream(prj);
        final CoordinateReferenceSystem crs_ =
                new PrjFileReader(fis.getChannel()).getCoordinateReferenceSystem();
        // Check External PRJ isn't found due to SKIP external files flag
        assertFalse(CRS.equalsIgnoreMetadata(crs, crs_));
        GridCoverage2D coverage = reader.read(null);
        assertFalse(CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(), crs_));

        fis.close();
        coverage.dispose(true);
        reader.dispose();
    }

    /** Test that the reader didn't access the external overviews */
    @Test
    public void externalMasksAreSkippedTest() throws Exception {
        final File file = TestData.file(GeoTiffSkipExternalTest.class, "mask/external2.tif");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        AbstractGridCoverage2DReader reader = format.getReader(file);
        DatasetLayout layout = reader.getDatasetLayout();
        assertFalse(layout.getNumExternalOverviews() > 0);
        assertFalse(layout.getNumExternalMasks() > 0);
        assertFalse(layout.getNumExternalMaskOverviews() > 0);
        reader.dispose();
    }

    /** Test that the reader didn't access the external files (masks and overviews) */
    @Test
    public void externalOverviewsAreSkippedTest() throws Exception {
        final File file = TestData.file(GeoTiffSkipExternalTest.class, "ovr.tif");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        AbstractGridCoverage2DReader reader = format.getReader(file);
        DatasetLayout layout = reader.getDatasetLayout();
        assertFalse(layout.getNumExternalOverviews() > 0);
        reader.dispose();
    }
}
