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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.test.TestData;
import org.geotools.util.Range;
import org.geotools.util.URLs;
import org.geotools.util.Utilities;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.InStream;
import org.locationtech.jts.io.InputStreamInStream;
import org.locationtech.jts.io.OutStream;
import org.locationtech.jts.io.OutputStreamOutStream;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests for the raster to vector FootprintExtractionProcess.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class FootprintExtractionProcessTest {

    private static final String THE_GEOM = "the_geom";

    private static final String CREATE_SPATIAL_INDEX = "create spatial index";

    private static final double TOLERANCE = 1.0e-12;

    private FootprintExtractionProcess process;

    /** A reference geometry being extracted from the cloud file by excluding only BLACK pixels */
    private Geometry referenceGeometry;

    private File cloudFile;

    private File islandFile;

    static enum WritingFormat {
        WKB {
            @Override
            void write(Geometry geometry, File outputFile, CoordinateReferenceSystem crs)
                    throws IOException {
                final WKBWriter wkbWriter = new WKBWriter(2);
                final OutputStream outputStream = new FileOutputStream(outputFile);
                final BufferedOutputStream bufferedStream = new BufferedOutputStream(outputStream);
                final OutStream outStream = new OutputStreamOutStream(bufferedStream);
                try {
                    wkbWriter.write(geometry, outStream);
                } finally {
                    IOUtils.closeQuietly(bufferedStream);
                }
            }
        },
        WKT {
            @Override
            void write(Geometry geometry, File outputFile, CoordinateReferenceSystem crs)
                    throws IOException {
                final WKTWriter wktWriter = new WKTWriter(2);
                final StringWriter wkt = new StringWriter();
                BufferedWriter bufferedWriter = new BufferedWriter(wkt);
                try {
                    wktWriter.write(geometry, bufferedWriter);
                } finally {
                    IOUtils.closeQuietly(bufferedWriter);
                }

                // write to file
                if (outputFile != null) {

                    bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
                    try {
                        bufferedWriter.write(wkt.toString());
                    } finally {
                        IOUtils.closeQuietly(bufferedWriter);
                    }
                }
            }
        },
        SHAPEFILE {
            @Override
            void write(Geometry geometry, File outputFile, CoordinateReferenceSystem crs)
                    throws IOException {

                // create feature type
                final SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
                featureTypeBuilder.setName("raster2vector");
                featureTypeBuilder.setCRS(crs);
                featureTypeBuilder.add(THE_GEOM, Polygon.class);
                featureTypeBuilder.add("cat", Integer.class);
                SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();

                // Preparing the collection
                final ListFeatureCollection collection = new ListFeatureCollection(featureType);
                final String typeName = featureType.getTypeName();

                // Creating the feature
                final SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
                final Object[] values = new Object[] {geometry, 0};
                featureBuilder.addAll(values);
                final SimpleFeature feature = featureBuilder.buildFeature(typeName + '.' + 0);

                // adding the feature to the collection
                collection.add(feature);

                // create shapefile
                final DataStoreFactorySpi factory = new ShapefileDataStoreFactory();

                // Preparing creation param
                final Map<String, Serializable> params = new HashMap<String, Serializable>();
                params.put(CREATE_SPATIAL_INDEX, Boolean.TRUE);
                params.put("url", URLs.fileToUrl(outputFile));

                final ShapefileDataStore ds =
                        (ShapefileDataStore) factory.createNewDataStore(params);
                ds.createSchema(featureType);
                if (crs != null) {
                    ds.forceSchemaCRS(crs);
                }

                // Write the features to the shapefile
                Transaction transaction = new DefaultTransaction("create");

                FeatureSource source = ds.getFeatureSource(ds.getTypeNames()[0]);

                if (source instanceof FeatureStore) {
                    FeatureStore store = (FeatureStore) source;

                    store.setTransaction(transaction);
                    try {
                        store.addFeatures(collection);
                        transaction.commit();

                    } catch (Exception e) {
                        transaction.rollback();

                    } finally {
                        try {
                            transaction.close();
                        } catch (IOException ioe) {

                        }
                    }
                }
                ds.dispose();
            }
        };

        abstract void write(Geometry geometry, File outputFile, CoordinateReferenceSystem crs)
                throws IOException;
    }

    @Before
    public void setup() throws IOException, ParseException {
        process = new FootprintExtractionProcess();
        cloudFile = TestData.file(this, "cloud.tif");
        islandFile = TestData.file(this, "island.tif");
        final File geometryFile = TestData.file(this, "cloud.wkt");
        referenceGeometry = wktRead(geometryFile);
    }

    private static Geometry wktRead(File geometryFile)
            throws FileNotFoundException, ParseException {
        FileReader fileReader = null;
        try {
            WKTReader wktReader = new WKTReader();
            fileReader = new FileReader(geometryFile);
            return wktReader.read(fileReader);
        } finally {
            IOUtils.closeQuietly(fileReader);
        }
    }

    private static Geometry wkbRead(File geometryFile) throws ParseException, IOException {
        BufferedInputStream bufferedStream = null;
        try {
            WKBReader wkbReader = new WKBReader();
            final InputStream inputStream = new FileInputStream(geometryFile);
            bufferedStream = new BufferedInputStream(inputStream);
            final InStream inStream = new InputStreamInStream(bufferedStream);
            return wkbReader.read(inStream);
        } finally {
            IOUtils.closeQuietly(bufferedStream);
        }
    }

    @Test
    public void cloudExtractionTest() throws Exception {
        GeoTiffReader reader = null;
        FeatureIterator<SimpleFeature> iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);
            SimpleFeatureCollection fc =
                    process.execute(cov, null, 10d, false, null, true, true, null, null);
            assertEquals(1, fc.size());

            iter = fc.features();

            SimpleFeature feature = iter.next();
            MultiPolygon geometry = (MultiPolygon) feature.getDefaultGeometry();
            assertTrue(referenceGeometry.equalsExact(geometry, TOLERANCE));
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
        FeatureIterator<SimpleFeature> iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);
            SimpleFeatureCollection fc =
                    process.execute(cov, null, 10d, true, 4d, true, true, null, null);
            assertEquals(2, fc.size());
            iter = fc.features();

            // Getting the main Footprint
            SimpleFeature feature = iter.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            double fullArea = geometry.getArea();

            // Getting to the simplified Footprint
            feature = (SimpleFeature) iter.next();
            geometry = (Geometry) feature.getDefaultGeometry();
            double simplifiedArea = geometry.getArea();

            // area are different and polygons are different too
            assertTrue(Math.abs(simplifiedArea - fullArea) > 0);
            assertFalse(referenceGeometry.equalsExact(geometry, TOLERANCE));
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
        FeatureIterator<SimpleFeature> iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);
            SimpleFeatureCollection fc =
                    process.execute(cov, null, 10d, false, null, false, true, null, null);
            iter = fc.features();

            SimpleFeature feature = iter.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            final int removeCollinearLength =
                    referenceGeometry.getGeometryN(0).getCoordinates().length;
            assertEquals(133, removeCollinearLength);

            // The computed polygon should have more vertices due to collinear point not being
            // removed
            final int length = geometry.getGeometryN(0).getCoordinates().length;
            assertTrue(length > removeCollinearLength);
            assertFalse(referenceGeometry.equalsExact(geometry, TOLERANCE));
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
        FeatureIterator<SimpleFeature> iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);

            // Exclude pixels with luminance less than 20.
            final int referenceLuminance = 10;
            List<Range<Integer>> exclusionRanges =
                    Collections.singletonList(
                            new Range<Integer>(Integer.class, 0, referenceLuminance));
            SimpleFeatureCollection fc =
                    process.execute(cov, exclusionRanges, 10d, false, null, true, true, null, null);
            iter = fc.features();

            SimpleFeature feature = iter.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();

            Raster raster = cov.getRenderedImage().getData();
            int[] darkPixel = new int[3];

            // These positions identify a couple of dark pixels of the cloud edge
            raster.getPixel(9, 13, darkPixel);
            double luminance =
                    ImageUtilities.RGB_TO_GRAY_MATRIX[0][0] * darkPixel[0]
                            + ImageUtilities.RGB_TO_GRAY_MATRIX[0][1] * darkPixel[1]
                            + ImageUtilities.RGB_TO_GRAY_MATRIX[0][2] * darkPixel[2];
            assertTrue(luminance < referenceLuminance);

            raster.getPixel(15, 7, darkPixel);
            luminance =
                    ImageUtilities.RGB_TO_GRAY_MATRIX[0][0] * darkPixel[0]
                            + ImageUtilities.RGB_TO_GRAY_MATRIX[0][1] * darkPixel[1]
                            + ImageUtilities.RGB_TO_GRAY_MATRIX[0][2] * darkPixel[2];
            assertTrue(luminance < referenceLuminance);

            // The computed polygon should have different shape due to dark pixels being excluded
            assertFalse(referenceGeometry.equalsExact(geometry, TOLERANCE));
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
    public void islandPolygonExtractionWithoutDarkPixelsAndWhiteClouds() throws Exception {
        GeoTiffReader reader = null;
        FeatureIterator<SimpleFeature> iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(islandFile);
            cov = reader.read(null);

            // Test removing black areas and clouds
            List<Range<Integer>> exclusionRanges = new ArrayList<Range<Integer>>();
            exclusionRanges.add(new Range<Integer>(Integer.class, 0, 10));
            exclusionRanges.add(new Range<Integer>(Integer.class, 253, 255));
            SimpleFeatureCollection fc =
                    process.execute(cov, exclusionRanges, 10d, false, null, true, true, null, null);
            iter = fc.features();

            SimpleFeature feature = iter.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            Geometry islandWkt = wktRead(TestData.file(this, "island.wkt"));
            assertTrue(islandWkt.equalsExact(geometry, TOLERANCE));

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
    public void emptyPolygonExtractionTest() throws Exception {
        GeoTiffReader reader = null;
        FeatureIterator<SimpleFeature> iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);
            BandSelectProcess select = new BandSelectProcess();
            cov = select.execute(cov, new int[] {0}, null);
            List<Range<Integer>> exclusionRange = new ArrayList<Range<Integer>>();

            // Exclude any value so the process will not find anything
            exclusionRange.add(new Range<Integer>(Integer.class, 0, 255));
            SimpleFeatureCollection fc =
                    process.execute(cov, exclusionRange, 10d, false, null, true, true, null, null);
            assertEquals(1, fc.size());

            iter = fc.features();

            // Check no results are returned as an empty MultiPolygon
            SimpleFeature feature = iter.next();
            Object geom = feature.getDefaultGeometry();
            assertTrue(geom instanceof MultiPolygon);
            MultiPolygon geometry = (MultiPolygon) geom;
            assertTrue(geometry.isEmpty());

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
    public void valuesEqualityTest() throws Exception {
        double p1 = 0.2;
        double p2 = 2d / 10;
        double p3 = 0.21;
        assertTrue(MarchingSquaresVectorizer.areEqual(p1, p2));
        assertFalse(MarchingSquaresVectorizer.areEqual(p1, p3));
    }

    @Test
    public void cloudExtractionWriteToDisk() throws Exception {
        GeoTiffReader reader = null;
        FeatureIterator<SimpleFeature> iter = null;
        GridCoverage2D cov = null;
        try {
            reader = new GeoTiffReader(cloudFile);
            cov = reader.read(null);
            SimpleFeatureCollection fc =
                    process.execute(cov, null, 10d, false, null, true, true, null, null);
            assertEquals(1, fc.size());

            iter = fc.features();

            SimpleFeature feature = iter.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            assertTrue(referenceGeometry.equalsExact(geometry, TOLERANCE));
            final File wktFile = TestData.temp(this, "cloudWkt.wkt");
            final File wkbFile = TestData.temp(this, "cloudWkb.wkb");
            final File shapeFile = TestData.temp(this, "cloudShape.shp");
            CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();

            // Write geometries
            writeGeometry(WritingFormat.WKT, geometry, wktFile, crs);
            writeGeometry(WritingFormat.WKB, geometry, wkbFile, crs);
            writeGeometry(WritingFormat.SHAPEFILE, geometry, shapeFile, crs);

            // read geometries back
            assertTrue(referenceGeometry.equalsExact(wktRead(wktFile), TOLERANCE));
            assertTrue(referenceGeometry.equalsExact(wkbRead(wkbFile), TOLERANCE));

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

    public static void writeGeometry(
            final WritingFormat writingFormat,
            final Geometry geometry,
            final File outputFile,
            final CoordinateReferenceSystem crs)
            throws IOException {
        Utilities.ensureNonNull("writingFormat", writingFormat);
        Utilities.ensureNonNull("geometry", geometry);
        if ((outputFile != null) && outputFile.exists()) {
            FileUtils.deleteQuietly(outputFile);
        }

        writingFormat.write(geometry, outputFile, crs);
    }
}
