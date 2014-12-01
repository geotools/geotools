/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.Raster;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.test.TestData;
import org.geotools.util.Range;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Tests for the raster to vector FootprintExtractionProcess.
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * 
 */
public class FootprintExtractionProcessTest {

    private static final double TOLERANCE = 1.0e-12;

    private FootprintExtractionProcess process;

    /** A reference geometry being extracted from the cloud file by excluding only BLACK pixels */
    private Geometry referenceGeometry;

    private File cloudFile;

    @Before
    public void setup() throws IOException, ParseException {
        process = new FootprintExtractionProcess();
        cloudFile = TestData.file(this, "cloud.tif");
        final File geometryFile = TestData.file(this, "cloud.wkt");
        FileReader fileReader = null;

        try {
            WKTReader wktReader = new WKTReader();
            fileReader = new FileReader(geometryFile);
            referenceGeometry = wktReader.read(fileReader);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (Throwable t) {

                }
            }
        }
    }

    @Test
    public void cloudExtractionTest() throws Exception {
        GeoTiffReader reader = null;
        FeatureIterator iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);
            SimpleFeatureCollection fc = process.execute(cov, null, 10d, false, null, true, true,
                    null, null);
            assertEquals(1, fc.size());

            iter = fc.features();

            SimpleFeature feature = (SimpleFeature) iter.next();
            MultiPolygon poly = (MultiPolygon) feature.getDefaultGeometry();
            assertTrue(referenceGeometry.equalsExact(poly, TOLERANCE));
        } finally {
            if (iter != null) {
                iter.close();
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {

                }
            }
            if (cov != null) {
                try {
                    cov.dispose(true);
                } catch (Throwable t) {

                }
            }
        }
    }

    @Test
    public void cloudExtractionSimplified() throws Exception {
        GeoTiffReader reader = null;
        FeatureIterator iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);
            SimpleFeatureCollection fc = process.execute(cov, null, 10d, true, 4d, true, true,
                    null, null);
            assertEquals(2, fc.size());
            iter = fc.features();

            // Getting the main Footprint
            SimpleFeature feature = (SimpleFeature) iter.next();
            Geometry poly = (Geometry) feature.getDefaultGeometry();
            double fullArea = poly.getArea();

            // Getting to the simplified Footprint
            feature = (SimpleFeature) iter.next();
            poly = (Geometry) feature.getDefaultGeometry();
            double simplifiedArea = poly.getArea();

            // area are different and polygons are different too
            assertTrue(Math.abs(simplifiedArea - fullArea) > 0);
            assertFalse(referenceGeometry.equalsExact(poly, TOLERANCE));
        } finally {
            if (iter != null) {
                iter.close();
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {

                }
            }
            if (cov != null) {
                try {
                    cov.dispose(true);
                } catch (Throwable t) {

                }
            }
        }
    }

    @Test
    public void cloudExtractionNoRemoveCollinear() throws Exception {
        GeoTiffReader reader = null;
        FeatureIterator iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);
            SimpleFeatureCollection fc = process.execute(cov, null, 10d, false, null, false, true,
                    null, null);
            iter = fc.features();

            SimpleFeature feature = (SimpleFeature) iter.next();
            Geometry poly = (Geometry) feature.getDefaultGeometry();
            final int removeCollinearLength = referenceGeometry.getGeometryN(0).getCoordinates().length;
            assertEquals(133, removeCollinearLength);

            // The computed polygon should have more vertices due to collinear point not being removed
            final int length = poly.getGeometryN(0).getCoordinates().length;
            assertTrue(length > removeCollinearLength);
            assertFalse(referenceGeometry.equalsExact(poly, TOLERANCE));
        } finally {
            if (iter != null) {
                iter.close();
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {

                }
            }
            if (cov != null) {
                try {
                    cov.dispose(true);
                } catch (Throwable t) {

                }
            }
        }
    }

    @Test
    public void cloudExtractionWithoutDarkPixels() throws Exception {
        GeoTiffReader reader = null;
        FeatureIterator iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);

            // Exclude pixels with luminance less than 20.
            final int referenceLuminance = 10;
            Range<Integer> exclusionRange = new Range<Integer>(Integer.class, 0, referenceLuminance);
            SimpleFeatureCollection fc = process.execute(cov, exclusionRange, 10d, false, null,
                    true, true, null, null);
            iter = fc.features();

            SimpleFeature feature = (SimpleFeature) iter.next();
            Geometry poly = (Geometry) feature.getDefaultGeometry();

            Raster raster = cov.getRenderedImage().getData();
            int[] darkPixel = new int[3];

            // These positions identify a couple of dark pixels of the cloud edge
            raster.getPixel(9, 13, darkPixel);
            double luminance = ImageUtilities.RGB_TO_GRAY_MATRIX[0][0] * darkPixel[0]
                    + ImageUtilities.RGB_TO_GRAY_MATRIX[0][1] * darkPixel[1]
                    + ImageUtilities.RGB_TO_GRAY_MATRIX[0][2] * darkPixel[2];
            assertTrue(luminance < referenceLuminance);

            raster.getPixel(15, 7, darkPixel);
            luminance = ImageUtilities.RGB_TO_GRAY_MATRIX[0][0] * darkPixel[0]
                    + ImageUtilities.RGB_TO_GRAY_MATRIX[0][1] * darkPixel[1]
                    + ImageUtilities.RGB_TO_GRAY_MATRIX[0][2] * darkPixel[2];
            assertTrue(luminance < referenceLuminance);

            // The computed polygon should have different shape due to dark pixels being excluded
            assertFalse(referenceGeometry.equalsExact(poly, TOLERANCE));
        } finally {
            if (iter != null) {
                iter.close();
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {

                }
            }
            if (cov != null) {
                try {
                    cov.dispose(true);
                } catch (Throwable t) {

                }
            }
        }
    }
}