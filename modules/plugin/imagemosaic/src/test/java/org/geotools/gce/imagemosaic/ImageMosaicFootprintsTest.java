/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.gce.imagemosaic.Utils.FF;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import it.geosolutions.rendered.viewer.RenderedImageBrowser;
import java.awt.*;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GranuleRemovalPolicy;
import org.geotools.coverage.grid.io.GranuleStore;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.coverage.grid.io.footprint.FootprintInsetPolicy;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProviderFactory;
import org.geotools.coverage.grid.io.footprint.WKBLoaderSPI;
import org.geotools.coverage.grid.io.footprint.WKTLoaderSPI;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.imagemosaic.catalog.MultiLevelROIGeometryOverviewsProvider;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.precision.EnhancedPrecisionOp;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.PropertyIsLike;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;

public class ImageMosaicFootprintsTest {

    private File testMosaic;

    private URL testMosaicUrl;

    private File footprintsSource;

    private Geometry geometryMask;

    // Flag for debug and tests during development
    private static boolean DEBUG = false;

    @Before
    public void cleanup() throws IOException, ParseException {
        // clean up
        testMosaic = new File(TestData.file(this, "."), "footprintMosaic");
        if (testMosaic.exists()) {
            FileUtils.deleteDirectory(testMosaic);
        }

        // create the base mosaic we are going to use
        File mosaicSource = TestData.file(this, "rgb");
        FileUtils.copyDirectory(mosaicSource, testMosaic);
        testMosaicUrl = URLs.fileToUrl(testMosaic);

        // footprint source
        footprintsSource = TestData.file(this, "rgb-footprints");

        WKTReader wktReader = new WKTReader();
        geometryMask = wktReader.read("POLYGON ((-170 -10, -72 80, 80 0, -170 -10))");
    }

    @Test
    public void testSingleShapefileDefaults() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);

        assertItalyFootprints();
    }

    @Test
    public void testWkbSidecars() throws Exception {
        // create wkb sidecar files
        ShapefileDataStore ds =
                new ShapefileDataStore(
                        URLs.fileToUrl(new File(footprintsSource, "footprints.shp")));
        ds.getFeatureSource()
                .getFeatures()
                .accepts(
                        new FeatureVisitor() {

                            @Override
                            public void visit(Feature feature) {
                                try {
                                    SimpleFeature sf = (SimpleFeature) feature;
                                    String fileName = (String) sf.getAttribute("location");
                                    int idx = fileName.lastIndexOf(".");
                                    Geometry g = (Geometry) sf.getDefaultGeometry();
                                    File wkbFile =
                                            new File(
                                                    testMosaic,
                                                    fileName.substring(0, idx) + ".wkb");
                                    byte[] bytes = new WKBWriter().write(g);
                                    FileUtils.writeByteArrayToFile(wkbFile, bytes);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        null);
        ds.dispose();
        assertItalyFootprints();
    }

    @Test
    public void testWktSidecars() throws Exception {
        // create wkb sidecar files
        ShapefileDataStore ds =
                new ShapefileDataStore(
                        URLs.fileToUrl(new File(footprintsSource, "footprints.shp")));
        ds.getFeatureSource()
                .getFeatures()
                .accepts(
                        new FeatureVisitor() {

                            @Override
                            public void visit(Feature feature) {
                                try {
                                    SimpleFeature sf = (SimpleFeature) feature;
                                    String fileName = (String) sf.getAttribute("location");
                                    int idx = fileName.lastIndexOf(".");
                                    Geometry g = (Geometry) sf.getDefaultGeometry();
                                    File wkbFile =
                                            new File(
                                                    testMosaic,
                                                    fileName.substring(0, idx) + ".wkt");
                                    String wkt = new WKTWriter().write(g);
                                    FileUtils.writeStringToFile(wkbFile, wkt, "UTF-8");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        null);
        ds.dispose();
        assertItalyFootprints();
    }

    @Test
    public void testWkbMultipleSidecars() throws Exception {
        // For this test, we use multiple WKB overviews having polygons in raster space
        testMultipleSidecars("footprint_wkbs", WKBLoaderSPI.class.getName(), true, "_%d");
    }

    @Test
    public void testWktMultipleSidecars() throws Exception {
        // For this test, we use multiple WKT overviews having polygons in model space
        testMultipleSidecars("footprint_wkts", WKTLoaderSPI.class.getName(), false, "-%d");
    }

    @Test
    public void testWktMultipleSidecarsAutoDetect() throws Exception {
        testMultipleSidecars("footprint_wkts", null, false, "-%d");
    }

    private void testMultipleSidecars(
            String testFolder,
            String loaderClassName,
            boolean overviewsInRasterSpace,
            String overviewsSuffixFormat)
            throws Exception {

        ImageMosaicReader reader =
                getMultipleSidecarReader(
                        testFolder, loaderClassName, overviewsInRasterSpace, overviewsSuffixFormat);

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();

        // Apply some scaling and some crop
        final double scalingFactorX = 1 / 5d;
        final double scalingFactorY = 1 / 5d;
        final double spanRatioX = 8 / 9d;
        final double spanRatioY = 5 / 6d;
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) * (scalingFactorX * spanRatioX),
                reader.getOriginalGridRange().getSpan(1) * (scalingFactorY * spanRatioY));
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        double maxX = envelope.getMaximum(0);
        double minY = envelope.getMinimum(1);
        double minX = maxX - envelope.getSpan(0) * (spanRatioX);
        double maxY = minY + envelope.getSpan(1) * (spanRatioY);

        final GeneralEnvelope env2 =
                new GeneralEnvelope(new double[] {minX, minY}, new double[] {maxX, maxY});
        env2.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
        gg.setValue(new GridGeometry2D(range, env2));

        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        GridCoverage2D coverage =
                reader.read(new GeneralParameterValue[] {footprintManagement, gg});

        byte[] pixel = new byte[4];

        // Checking some pixels out of the footprint are transparent
        coverage.evaluate(new DirectPosition2D(-89, 34), pixel);
        assertEquals(0, pixel[3]);

        coverage.evaluate(new DirectPosition2D(43, -13), pixel);
        assertEquals(0, pixel[3]);

        coverage.evaluate(new DirectPosition2D(131, 10), pixel);
        assertEquals(0, pixel[3]);

        coverage.evaluate(new DirectPosition2D(145, 0), pixel);
        assertEquals(0, pixel[3]);
    }

    private ImageMosaicReader getMultipleSidecarReader(
            String testFolder,
            String loaderClassName,
            boolean overviewsInRasterSpace,
            String overviewsSuffixFormat)
            throws IOException {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File multiWkbs = folder.getRoot();
        FileUtils.copyDirectory(TestData.file(this, testFolder), multiWkbs);
        Properties p = new Properties();
        p.put(
                MultiLevelROIProviderFactory.SOURCE_PROPERTY,
                MultiLevelROIProviderFactory.TYPE_MULTIPLE_SIDECAR);
        p.put(
                MultiLevelROIGeometryOverviewsProvider.OVERVIEWS_SUFFIX_FORMAT_KEY,
                overviewsSuffixFormat);
        p.put(
                MultiLevelROIGeometryOverviewsProvider.OVERVIEWS_ROI_IN_RASTER_SPACE_KEY,
                Boolean.toString(overviewsInRasterSpace));
        if (loaderClassName != null) {
            p.put(MultiLevelROIGeometryOverviewsProvider.FOOTPRINT_LOADER_SPI, loaderClassName);
        }
        try (FileOutputStream fos =
                new FileOutputStream(new File(multiWkbs, "footprints.properties"))) {
            p.store(fos, null);
        }

        ImageMosaicFormat format = new ImageMosaicFormat();
        return format.getReader(multiWkbs);
    }

    @Test
    public void testShapefileSidecars() throws Exception {
        // create wkb sidecar files
        ShapefileDataStore ds =
                new ShapefileDataStore(
                        URLs.fileToUrl(new File(footprintsSource, "footprints.shp")));
        ds.getFeatureSource()
                .getFeatures()
                .accepts(
                        new FeatureVisitor() {

                            @Override
                            public void visit(Feature feature) {
                                try {
                                    SimpleFeature sf = (SimpleFeature) feature;
                                    String fileName = (String) sf.getAttribute("location");
                                    int idx = fileName.lastIndexOf(".");
                                    Geometry g = (Geometry) sf.getDefaultGeometry();
                                    String filename = fileName.substring(0, idx);
                                    File shpFile = new File(testMosaic, filename + ".shp");
                                    ShapefileDataStore sds =
                                            new ShapefileDataStore(URLs.fileToUrl(shpFile));
                                    SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
                                    tb.setName(filename);
                                    GeometryDescriptor gd =
                                            sf.getFeatureType().getGeometryDescriptor();
                                    tb.add(
                                            "the_geom",
                                            gd.getType().getBinding(),
                                            gd.getCoordinateReferenceSystem());
                                    SimpleFeatureType sft = tb.buildFeatureType();
                                    sds.createSchema(sft);

                                    SimpleFeatureBuilder fb = new SimpleFeatureBuilder(sft);
                                    fb.add(g);
                                    SimpleFeature footprintFeature = fb.buildFeature(null);
                                    SimpleFeatureStore fs =
                                            (SimpleFeatureStore) sds.getFeatureSource();
                                    fs.addFeatures(DataUtilities.collection(footprintFeature));
                                    sds.dispose();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        null);

        assertItalyFootprints();
    }

    /** Test the GeometryMask parameter */
    @Test
    public void testMasking() throws Exception {
        maskCoverage(false, Double.NaN, this.geometryMask);
    }

    /** Test the GeometryMask parameter combined with granules footprint */
    @Test
    @Ignore
    public void testMaskingWithFootprint() throws Exception {
        maskCoverage(true, Double.NaN, this.geometryMask);
    }

    /** Test the GeometryMask parameter with buffering */
    @Test
    public void testMaskingWithBuffer() throws Exception {
        maskCoverage(false, 10d, this.geometryMask);
    }

    /** Test the GeometryMask parameter with applied buffering, combined with granules footprint */
    @Test
    @Ignore
    //    java.lang.ClassCastException: javax.media.jai.ROI cannot be cast to
    // it.geosolutions.jaiext.vectorbin.ROIGeometry
    //
    //    at
    // org.geotools.gce.imagemosaic.ImageMosaicFootprintsTest.maskCoverage(ImageMosaicFootprintsTest.java:498)
    //    at
    // org.geotools.gce.imagemosaic.ImageMosaicFootprintsTest.testMaskingWithBufferAndFootprint(ImageMosaicFootprintsTest.java:399)
    //    at sun.reflect.NativeMethodAccessorImpl.invoke0(
    //    Native Method)
    //    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    //    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    //    at java.lang.reflect.Method.invoke(Method.java:498)
    //    at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
    //    at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
    //    at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
    //    at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
    //    at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
    //    at org.junit.rules.ExternalResource$1.evaluate(ExternalResource.java:48)
    //    at org.junit.rules.RunRules.evaluate(RunRules.java:20)
    //    at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
    //    at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
    //    at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
    //    at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
    //    at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
    //    at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
    //    at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
    //    at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
    //    at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
    //    at org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:27)
    //    at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
    //    at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
    //    at
    // com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
    //    at
    // com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
    //    at
    // com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
    //    at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
    public void testMaskingWithBufferAndFootprint() throws Exception {
        maskCoverage(true, 10d, this.geometryMask);
    }

    /**
     * Test the GeometryMask parameter with applied buffering, combined with granules footprint
     * involving decimation
     */
    @Test
    @Ignore
    public void testMaskingDecimationWithBufferAndFootprint() throws Exception {
        Geometry decimatingMask = Densifier.densify(this.geometryMask, 0.2);
        maskCoverage(true, 10d, decimatingMask);
    }

    private void maskCoverage(boolean footprint, double buffer, Geometry geometryMask)
            throws Exception {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File multiWkts = folder.getRoot();
        FileUtils.copyDirectory(TestData.file(this, "footprint_wkts"), multiWkts);

        // Setting up granules footprint properties
        Properties p = new Properties();
        p.put(
                MultiLevelROIProviderFactory.SOURCE_PROPERTY,
                MultiLevelROIProviderFactory.TYPE_MULTIPLE_SIDECAR);
        try (FileOutputStream fos =
                new FileOutputStream(new File(multiWkts, "footprints.properties"))) {
            p.store(fos, null);
        }

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = null;
        GridCoverage2D coverage = null;
        try {
            reader = format.getReader(multiWkts);
            AffineTransform2D g2w =
                    (AffineTransform2D) reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER);
            double xScale = g2w.getScaleX();

            boolean useBuffer = !Double.isNaN(buffer);

            // Setup the intersection mask
            Geometry intersectingMask =
                    useBuffer ? geometryMask.buffer(buffer * xScale) : geometryMask;
            Geometry unionGeometry = null;
            if (footprint) {
                Geometry leftGeometry = readWktGeometry("r1c1.wkt");
                Geometry rightGeometry = readWktGeometry("r1c2.wkt");
                unionGeometry = leftGeometry.union(rightGeometry);
            } else {
                unionGeometry =
                        JTS.toGeometry(new ReferencedEnvelope(reader.getOriginalEnvelope()));
            }
            Geometry maskedGeometry = unionGeometry.intersection(intersectingMask);
            double inputMaskArea = maskedGeometry.getArea();

            List<GeneralParameterValue> paramList = new ArrayList<GeneralParameterValue>();

            // Setup reading params
            // FOOTPRINT
            if (footprint) {
                ParameterValue<String> footprintManagement =
                        AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
                footprintManagement.setValue(FootprintBehavior.Transparent.name());
                paramList.add(footprintManagement);
            }

            // MASKING
            ParameterValue<Geometry> maskParam = ImageMosaicFormat.GEOMETRY_MASK.createValue();
            maskParam.setValue(geometryMask);
            paramList.add(maskParam);

            // SETROI
            ParameterValue<Boolean> setRoiParam = ImageMosaicFormat.SET_ROI_PROPERTY.createValue();
            setRoiParam.setValue(true);
            paramList.add(setRoiParam);

            // BUFFER
            if (useBuffer) {
                ParameterValue<Double> maskingBuffer =
                        ImageMosaicFormat.MASKING_BUFFER_PIXELS.createValue();
                maskingBuffer.setValue(buffer);
                paramList.add(maskingBuffer);
            }

            GeneralParameterValue[] params = new GeneralParameterValue[paramList.size()];
            params = paramList.toArray(params);

            coverage = reader.read(params);
            RenderedImage image = coverage.getRenderedImage();
            if (DEBUG) {
                RenderedImageBrowser.showChain(image);
                // wait for an input key so you can check the image
                System.in.read();
            }
            // Extract the resulting mask as ROI property of the resulting coverage
            ROIGeometry roi = (ROIGeometry) image.getProperty("ROI");

            // Transform the ROI to model space
            MathTransform2D tx =
                    coverage.getGridGeometry()
                            .getCRSToGrid2D(PixelOrientation.UPPER_LEFT)
                            .inverse();
            Geometry roiGeometry = roi.getAsGeometry();
            double tolerance = 0.1d;
            if (geometryMask != this.geometryMask) {
                final int numPoints = roiGeometry.getNumPoints();
                // Check that decimation occurred
                assertTrue(numPoints < 1000);

                // Allows some more area check tolerance due to densification + decimation
                tolerance = 2d;
            }

            Geometry coverageGeometry = JTS.transform(roiGeometry, tx);
            double coverageMaskArea = coverageGeometry.getArea();

            // Make sure the mask matches by doing area check and subtractions area check
            Geometry aMinusB = EnhancedPrecisionOp.difference(coverageGeometry, maskedGeometry);
            Geometry bMinusA = EnhancedPrecisionOp.difference(maskedGeometry, coverageGeometry);

            assertEquals(inputMaskArea, coverageMaskArea, tolerance);
            assertEquals(0, aMinusB.getArea(), tolerance);
            assertEquals(0, bMinusA.getArea(), tolerance);
        } finally {
            if (coverage != null) {
                coverage.dispose(true);
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Exception e) {
                    // Does Nothing
                }
            }
        }
    }

    @Test
    @Ignore
    public void testMaskWithBackground() throws Exception {
        TemporaryFolder folder = new TemporaryFolder();
        folder.create();
        File multiWkts = folder.getRoot();
        FileUtils.copyDirectory(TestData.file(this, "footprint_wkts"), multiWkts);

        // Setting up granules footprint properties
        Properties p = new Properties();
        p.put(
                MultiLevelROIProviderFactory.SOURCE_PROPERTY,
                MultiLevelROIProviderFactory.TYPE_MULTIPLE_SIDECAR);
        try (FileOutputStream fos =
                new FileOutputStream(new File(multiWkts, "footprints.properties"))) {
            p.store(fos, null);
        }

        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = null;
        GridCoverage2D coverage = null;
        try {
            reader = format.getReader(multiWkts);

            List<GeneralParameterValue> paramList = new ArrayList<GeneralParameterValue>();

            // Setup reading params
            // FOOTPRINT
            ParameterValue<String> footprintManagement =
                    AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
            footprintManagement.setValue(FootprintBehavior.Cut.name());
            paramList.add(footprintManagement);

            // MASKING
            ParameterValue<Geometry> maskParam = ImageMosaicFormat.GEOMETRY_MASK.createValue();
            maskParam.setValue(geometryMask);
            paramList.add(maskParam);

            // SETROI
            ParameterValue<Boolean> setRoiParam = ImageMosaicFormat.SET_ROI_PROPERTY.createValue();
            setRoiParam.setValue(true);
            paramList.add(setRoiParam);

            // Background Values
            ParameterValue<double[]> bg = ImageMosaicFormat.BACKGROUND_VALUES.createValue();
            double[] bgValues = new double[] {0, 255, 0};
            bg.setValue(bgValues);
            paramList.add(bg);

            // GRIDGEOMETRY (specify a chunk of the whole envelope)
            final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
            final GeneralEnvelope cropEnvelope =
                    new GeneralEnvelope(
                            new double[] {
                                oldEnvelope.getLowerCorner().getOrdinate(0)
                                        + oldEnvelope.getSpan(0) / 4,
                                oldEnvelope.getLowerCorner().getOrdinate(1)
                                        + oldEnvelope.getSpan(1) / 2
                            },
                            new double[] {
                                oldEnvelope.getUpperCorner().getOrdinate(0)
                                        - oldEnvelope.getSpan(0) / 2,
                                oldEnvelope.getUpperCorner().getOrdinate(1)
                            });
            cropEnvelope.setCoordinateReferenceSystem(reader.getCoordinateReferenceSystem());

            ParameterValue<GridGeometry2D> gg =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            gg.setValue(
                    new GridGeometry2D(
                            PixelInCell.CELL_CENTER,
                            reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
                            cropEnvelope,
                            null));
            paramList.add(gg);

            GeneralParameterValue[] params = new GeneralParameterValue[paramList.size()];
            params = paramList.toArray(params);

            coverage = reader.read(params);

            // Extract the resulting mask as ROI property of the resulting coverage
            RenderedImage image = coverage.getRenderedImage();
            ROIGeometry roi = (ROIGeometry) image.getProperty("ROI");

            // Transform the ROI to model space
            MathTransform2D tx =
                    coverage.getGridGeometry()
                            .getCRSToGrid2D(PixelOrientation.UPPER_LEFT)
                            .inverse();
            Geometry roiGeometry = roi.getAsGeometry();
            Geometry coverageGeometry = JTS.transform(roiGeometry, tx);
            double coverageMaskArea = coverageGeometry.getArea();

            Geometry intersectingMask = geometryMask;
            Geometry requestedAreaGeometry = JTS.toGeometry(new ReferencedEnvelope(cropEnvelope));

            // Combine the granules footprint
            Geometry leftGeometry = readWktGeometry("r1c1.wkt");
            Geometry rightGeometry = readWktGeometry("r1c2.wkt");
            Geometry unionGeometry = leftGeometry.union(rightGeometry);

            // intersect the requested area with the mask
            Geometry maskedGeometry = requestedAreaGeometry.intersection(intersectingMask);

            // intersect the footprint union with the resulting mask
            maskedGeometry = unionGeometry.intersection(maskedGeometry);
            maskedGeometry = maskedGeometry.getGeometryN(1);
            double inputMaskArea = maskedGeometry.getArea();

            // Make sure the mask matches by doing area check and subtractions area check
            Geometry aMinusB = EnhancedPrecisionOp.difference(coverageGeometry, maskedGeometry);
            Geometry bMinusA = EnhancedPrecisionOp.difference(maskedGeometry, coverageGeometry);

            if (DEBUG) {
                RenderedImageBrowser.showChain(image);
                // wait for an input key so you can check the image
                System.in.read();
            }
            assertEquals(inputMaskArea, coverageMaskArea, 1E-3);
            assertEquals(0, aMinusB.getArea(), 1E-3);
            assertEquals(0, bMinusA.getArea(), 1E-3);

            // Get a pixel living outside of the mask
            int[] pixel = new int[3];
            image.getData().getPixel(0, 0, pixel);
            for (int i = 0; i < 3; i++) {
                assertEquals(pixel[i], (int) bgValues[i]);
            }
        } finally {
            if (coverage != null) {
                coverage.dispose(true);
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Exception e) {
                    // Does Nothing
                }
            }
        }
    }

    private Geometry readWktGeometry(String fileName)
            throws FileNotFoundException, IOException, ParseException {
        WKTReader wktReader = new WKTReader();
        File file = TestData.file(this, "footprint_wkts" + File.separatorChar + fileName);
        try (FileReader fileReader = new FileReader(file)) {
            return wktReader.read(fileReader);
        }
    }

    private void assertItalyFootprints()
            throws NoSuchAuthorityCodeException, FactoryException, IOException {
        GridCoverage2D coverage = readCoverage();

        // RenderedImageBrowser.showChain(coverage.getRenderedImage());
        // System.in.read();

        // check the footprints have been applied by pocking the output image
        byte[] pixel = new byte[3];
        // Mar Ionio, should be black
        coverage.evaluate(new DirectPosition2D(16.87, 40.19), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Golfo di La Spezia, should be black
        coverage.evaluate(new DirectPosition2D(9.12, 44.25), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Sardinia, not black
        coverage.evaluate(new DirectPosition2D(9, 40), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Piedmont, not black
        coverage.evaluate(new DirectPosition2D(8, 45), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
    }

    private GridCoverage2D readCoverage()
            throws NoSuchAuthorityCodeException, FactoryException, IOException {
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicUrl);
        final ImageMosaicReader reader = TestUtils.getReader(testMosaicUrl, format);
        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[2];
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Cut.name());
        params[0] = footprintManagement;

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params[1] = jaiImageRead;
        GridCoverage2D coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
        return coverage;
    }

    @Test
    public void testAreaOutside() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1");
        saveFootprintProperties(p);
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicUrl);
        final ImageMosaicReader reader = TestUtils.getReader(testMosaicUrl, format);

        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params[1] = jaiImageRead;

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final Dimension dim = new Dimension();
        dim.setSize(4, 4);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        rasterArea.x = 0;
        rasterArea.y = (int) (rasterArea.getHeight() / 2);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(
                new GridGeometry2D(
                        range,
                        PixelInCell.CELL_CENTER,
                        reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
                        reader.getCoordinateReferenceSystem(),
                        null));
        params[2] = gg;

        GridCoverage2D coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);

        // check the ROI is there
        RenderedImage ri = coverage.getRenderedImage();
        Object roiCandidate = ri.getProperty("ROI");
        assertTrue(roiCandidate instanceof ROI);
        // empty ROI
        ROI roi = (ROI) roiCandidate;
        assertFalse(roi.intersects(ri.getMinX(), ri.getMinY(), ri.getWidth(), ri.getHeight()));
    }

    @Test
    public void testRequestHole() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1");
        saveFootprintProperties(p);
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicUrl);
        ImageMosaicReader reader = TestUtils.getReader(testMosaicUrl, format);
        reader.dispose();
        // get rid of the sample image
        File sampleImage = new File(testMosaic, Utils.SAMPLE_IMAGE_NAME);
        sampleImage.delete();
        // a new reader without the sample image, in normal conditions it can actually produce
        // output
        reader = TestUtils.getReader(testMosaicUrl, format);

        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params[1] = jaiImageRead;

        // limit yourself to reading just a bit of it
        MathTransform mt = reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER);
        GridEnvelope2D ge = new GridEnvelope2D(6, 44, 1, 1);
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        gg.setValue(new GridGeometry2D(ge, mt, DefaultGeographicCRS.WGS84));
        params[2] = gg;

        // read the first time, no sample_image yet present
        GridCoverage2D coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
        RenderedImage ri = coverage.getRenderedImage();
        assertNotEquals(Transparency.OPAQUE, ri.getColorModel().getTransparency());
        reader.dispose();

        // read a second time
        reader = TestUtils.getReader(testMosaicUrl, format);
        coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
        ri = coverage.getRenderedImage();
        assertNotEquals(Transparency.OPAQUE, ri.getColorModel().getTransparency());
        reader.dispose();

        // check the ROI is there
        Object roiCandidate = ri.getProperty("ROI");
        assertTrue(roiCandidate instanceof ROI);
        // empty ROI
        ROI roi = (ROI) roiCandidate;
        assertFalse(roi.intersects(ri.getMinX(), ri.getMinY(), ri.getWidth(), ri.getHeight()));
    }

    @Test
    public void testInsetsFull() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1");
        p.put(FootprintInsetPolicy.INSET_TYPE_PROPERTY, "full");
        saveFootprintProperties(p);

        GridCoverage2D coverage = readCoverage();

        // check the footprints have been applied by pocking the output image
        byte[] pixel = new byte[3];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(new DirectPosition2D(12.54, 44.03), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Inner BORDER gets black with FULL insets
        coverage.evaluate(new DirectPosition2D(11.52, 44.57), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Golfo di La Spezia, should be black
        coverage.evaluate(new DirectPosition2D(9.12, 44.25), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Sardinia, not black
        coverage.evaluate(new DirectPosition2D(9, 40), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Piedmont, not black
        coverage.evaluate(new DirectPosition2D(8, 45), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        disposeCoverage(coverage);

        final ImageMosaicReader reader =
                TestUtils.getReader(testMosaicUrl, new ImageMosaicFormat());
        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params[1] = jaiImageRead;

        // GridGeometry, small aread at the upper right corner
        final GridEnvelope2D ge2D =
                new GridEnvelope2D(
                        reader.getOriginalGridRange().getHigh(0) - 3,
                        reader.getOriginalGridRange().getLow(1),
                        3,
                        3);
        final GridGeometry2D gg2D =
                new GridGeometry2D(
                        ge2D,
                        reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
                        reader.getCoordinateReferenceSystem());
        ParameterValue<GridGeometry2D> gg2DParam =
                ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
        gg2DParam.setValue(gg2D);
        params[2] = gg2DParam;

        coverage = reader.read(params);
        MathTransform tr = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        reader.dispose();
        assertNotNull(coverage);

        // check the footprints have been applied by pocking the output image
        pixel = new byte[4];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(
                new DirectPosition2D(
                        coverage.getEnvelope().getMinimum(0) + 1e-3,
                        coverage.getEnvelope().getMinimum(1) + 1e-3),
                pixel);

        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        assertEquals(0, pixel[3]);

        disposeCoverage(coverage);
    }

    @Test
    public void testInsetsMargin() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1");
        p.put(FootprintInsetPolicy.INSET_TYPE_PROPERTY, "border");
        saveFootprintProperties(p);

        GridCoverage2D coverage = readCoverage();

        //        // check the footprints have been applied by pocking the output image
        byte[] pixel = new byte[3];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(new DirectPosition2D(12.54, 44.03), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Inner BORDER should not get black with border insets
        coverage.evaluate(new DirectPosition2D(11.52, 44.57), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Golfo di La Spezia, should be black
        coverage.evaluate(new DirectPosition2D(9.12, 44.25), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Sardinia, not black
        coverage.evaluate(new DirectPosition2D(9, 40), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Piedmont, not black
        coverage.evaluate(new DirectPosition2D(8, 45), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        disposeCoverage(coverage);

        final ImageMosaicReader reader =
                TestUtils.getReader(testMosaicUrl, new ImageMosaicFormat());
        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params[1] = jaiImageRead;

        // GridGeometry, small read at the upper right corner
        final GridEnvelope2D ge2D =
                new GridEnvelope2D(
                        reader.getOriginalGridRange().getHigh(0) - 3,
                        reader.getOriginalGridRange().getLow(1),
                        3,
                        3);
        final GridGeometry2D gg2D =
                new GridGeometry2D(
                        ge2D,
                        reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
                        reader.getCoordinateReferenceSystem());
        ParameterValue<GridGeometry2D> gg2DParam =
                ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
        gg2DParam.setValue(gg2D);
        params[2] = gg2DParam;

        coverage = reader.read(params);
        MathTransform tr = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        reader.dispose();
        assertNotNull(coverage);

        // check the footprints have been applied by pocking the output image
        pixel = new byte[4];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(
                new DirectPosition2D(
                        coverage.getEnvelope().getMinimum(0) + 1e-3,
                        coverage.getEnvelope().getMinimum(1) + 1e-3),
                pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        assertEquals(0, pixel[3]);

        disposeCoverage(coverage);
    }

    /** Dispose the provided coverage for good. */
    private void disposeCoverage(GridCoverage2D coverage) {
        if (coverage == null) {
            return;
        }
        final RenderedImage im = coverage.getRenderedImage();
        ImageUtilities.disposePlanarImageChain(PlanarImage.wrapRenderedImage(im));
        coverage.dispose(true);
    }

    private void saveFootprintProperties(Properties p) throws FileNotFoundException, IOException {
        try (FileOutputStream fos =
                new FileOutputStream(new File(testMosaic, "footprints.properties"))) {
            p.store(fos, null);
        }
    }

    @AfterClass
    public static void close() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @BeforeClass
    public static void init() {

        // make sure CRS ordering is correct
        CRS.reset("all");
        System.setProperty("org.geotools.referencing.forceXY", "true");
    }

    @Test
    public void testInsetsBorder() throws Exception {
        // copy the footprints mosaic over
        FileUtils.copyDirectory(footprintsSource, testMosaic);
        Properties p = new Properties();
        p.put(FootprintInsetPolicy.INSET_PROPERTY, "0.1");
        saveFootprintProperties(p);
        GridCoverage2D coverage = readCoverage();
        // check the footprints have been applied by pocking the output image
        byte[] pixel = new byte[3];
        // Close to San Marino, black if we have the insets
        coverage.evaluate(new DirectPosition2D(12.54, 44.03), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Golfo di La Spezia, should be black
        coverage.evaluate(new DirectPosition2D(9.12, 44.25), pixel);
        assertEquals(0, pixel[0]);
        assertEquals(0, pixel[1]);
        assertEquals(0, pixel[2]);
        // Sardinia, not black
        coverage.evaluate(new DirectPosition2D(9, 40), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        // Piedmont, not black
        coverage.evaluate(new DirectPosition2D(8, 45), pixel);
        assertTrue(pixel[0] + pixel[1] + pixel[2] > 0);
        disposeCoverage(coverage);
    }

    @Test
    public void testFootprintA() throws IOException {
        ImageMosaicReader reader =
                (ImageMosaicReader)
                        new ImageMosaicFormatFactory()
                                .createFormat()
                                .getReader(TestData.file(this, "footprint_a"));
        GeneralParameterValue[] params = new GeneralParameterValue[1];
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        GridCoverage2D coverage = reader.read(params);

        byte[] result = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        position.setLocation(1, 1);
        coverage.evaluate(position, result);

        // RGBA
        assertEquals(4, coverage.getSampleDimensions().length);

        // Transparent
        assertEquals(0, result[3]);

        position = new DirectPosition2D();
        position.setLocation(-1, -1);
        coverage.evaluate(position, result);

        // Blue
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertTrue(0 != result[2]);
        assertTrue(0 != result[3]);
        reader.dispose();
    }

    @Test
    public void testFootprintRGB() throws FileNotFoundException, IOException {
        testFootprint(TestData.file(this, "footprint_rgb"));
    }

    @Test
    public void testFootprintRGBA() throws FileNotFoundException, IOException {
        testFootprint(TestData.file(this, "footprint_rgba"));
    }

    public void testFootprint(File mosaic) throws IOException {
        ImageMosaicReader reader =
                (ImageMosaicReader) new ImageMosaicFormatFactory().createFormat().getReader(mosaic);

        GeneralParameterValue[] params = new GeneralParameterValue[1];
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        GridCoverage2D coverage = reader.read(params);

        byte[] result = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        position.setLocation(1, 1);
        coverage.evaluate(position, result);

        // Red
        assertTrue(0 != result[0]);
        assertEquals(0, result[1]);
        assertEquals(0, result[2]);
        assertTrue(0 != result[3]);

        position = new DirectPosition2D();
        position.setLocation(-1, -1);
        coverage.evaluate(position, result);

        // Blue
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertTrue(0 != result[2]);
        assertTrue(0 != result[3]);

        reader.dispose();
    }

    @Test
    public void testRasterFootprintExternal() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRaster");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }
        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("rastermask", testMosaicRaster, false);

        // Evaluate results
        byte[] results = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-86.724, 25.085);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-86.252, 27.7984);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-87.937, 26.144);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-89.084, 27.133);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-89.763, 25.167);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
    }

    @Test
    public void testRasterFootprintSubmsampling() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRasterSubsampling");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }
        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("masked2", testMosaicRaster, true);

        // check the ROI and the image are black in the same pixels
        ROI roi = CoverageUtilities.getROIProperty(coverage);
        Raster roiImage = roi.getAsImage().getData();
        Raster image = coverage.getRenderedImage().getData();

        int[] px = new int[4];
        int[] rpx = new int[1];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                image.getPixel(j, i, px);
                roiImage.getPixel(j, i, rpx);
                if (px[0] == 0 && px[1] == 0 && px[2] == 0) {
                    assertEquals("Difference at " + i + "," + j, 0, rpx[0]);
                } else {
                    assertEquals("Difference at " + i + "," + j, 1, rpx[0]);
                }
            }
        }
    }

    @Test
    public void testRasterFootprintInternal() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRaster");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }

        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("rastermask2", testMosaicRaster, false);

        // Evaluate results
        byte[] results = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-86.724, 25.085);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-86.252, 27.7984);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-87.937, 26.144);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-89.084, 27.133);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-89.763, 25.167);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
    }

    @Test
    public void testRasterFootprintExternalMask() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRaster");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }

        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("rastermask", testMosaicRaster, true);

        // Evaluate results
        byte[] results = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0 but since the mask is subsampled, it may happen that the
        // final pixel is not masked
        position.setLocation(-86.724, 25.085);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be > 0
        position.setLocation(-86.252, 27.7984);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be  0 but since the mask is subsampled, it may happen that the
        // final pixel is not masked
        position.setLocation(-87.937, 26.144);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be > 0
        position.setLocation(-89.084, 27.133);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-89.763, 25.167);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
    }

    @Test
    public void testRasterFootprintInternalMaskAndOverviews() throws Exception {
        // Raster
        File testMosaicRaster = new File(TestData.file(this, "."), "footprintRaster");
        if (testMosaicRaster.exists()) {
            FileUtils.deleteDirectory(testMosaicRaster);
        }

        // Reading Coverage with Raster footprint
        GridCoverage2D coverage = readRasterFootprint("rastermask2", testMosaicRaster, true);

        // Evaluate results
        byte[] results = new byte[4];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-86.724, 25.085);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-86.252, 27.7984);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be  0
        position.setLocation(-87.937, 26.144);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
        // Should be > 0
        position.setLocation(-89.084, 27.133);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        assertTrue(results[3] != 0);
        // Should be 0
        position.setLocation(-89.763, 25.167);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        assertEquals(results[3], 0);
    }

    private GridCoverage2D readRasterFootprint(
            String path, File testMosaicRaster, boolean testOverviews) throws Exception {
        // create the base mosaic we are going to use
        File mosaicSourceRaster = TestData.file(this, path);
        FileUtils.copyDirectory(mosaicSourceRaster, testMosaicRaster);
        URL testMosaicRasterUrl = URLs.fileToUrl(testMosaicRaster);
        // copy the footprints mosaic properties
        Properties p = new Properties();
        // Setting Raster property
        p.put(MultiLevelROIProviderFactory.SOURCE_PROPERTY, "raster");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testMosaicRaster, "footprints.properties"))) {
            p.store(fos, null);
        }
        final AbstractGridFormat format = TestUtils.getFormat(testMosaicRasterUrl);
        final ImageMosaicReader reader = TestUtils.getReader(testMosaicRasterUrl, format);

        // activate footprint management
        GeneralParameterValue[] params = new GeneralParameterValue[3];
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params[0] = footprintManagement;

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params[1] = jaiImageRead;

        // setup how much we are going to read
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        if (testOverviews) {
            Dimension dim = new Dimension();
            dim.setSize(8, 8);
            rasterArea.setSize(dim);
            final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
            gg.setValue(new GridGeometry2D(range, reader.getOriginalEnvelope()));
            params[2] = gg;
        } else {
            final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
            gg.setValue(
                    new GridGeometry2D(
                            range,
                            PixelInCell.CELL_CENTER,
                            reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
                            reader.getCoordinateReferenceSystem(),
                            null));
            params[2] = gg;
        }
        // Read the coverage
        GridCoverage2D coverage = reader.read(params);
        reader.dispose();
        assertNotNull(coverage);
        // Check if ROI is present
        ROI roi = CoverageUtilities.getROIProperty(coverage);
        assertNotNull(roi);

        // Checking ROI Bounds
        // Ensure has the same size of the input image
        Rectangle roiBounds = roi.getBounds();
        Rectangle imgBounds = coverage.getGridGeometry().getGridRange2D();
        assertEquals(imgBounds.x, roiBounds.x);
        assertEquals(imgBounds.y, roiBounds.y);
        assertEquals(imgBounds.width, roiBounds.width);
        assertEquals(imgBounds.height, roiBounds.height);

        return coverage;
    }

    @Rule public TemporaryFolder redFootprintFolder = new TemporaryFolder();

    /**
     * When the mosaic bounds don't match the requested image bounds, there's only one granule in
     * the requested bounds and FootprintBehavior is transparent a border is added to the image.
     * This actually only happens in very specific circumstances, like in the test data which is an
     * L shaped. In this case the footprint behavior was not being respected, resulting in a
     * background color even though the background should be transparent. Update: now the image
     * mosaic uses only the footprint of the intercepted tiles to build the result, so the bbox has
     * been moved to hit the internal corner of the L in order to still generate a transparent
     * corner (with the original bbox only the part intersecting the requested granule is returned)
     */
    @Test
    public void testFootprintWithBorderNeeded() throws IOException {
        File testFolder = redFootprintFolder.newFolder();
        File mosaic = TestData.file(this, "red_footprint_test");
        FileUtils.copyDirectory(mosaic, testFolder);
        ImageMosaicReader reader =
                (ImageMosaicReader)
                        new ImageMosaicFormatFactory().createFormat().getReader(testFolder);

        ParameterValue<String> footprintBehaviorParam =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintBehaviorParam.setValue(FootprintBehavior.Transparent.name());

        ParameterValue<GridGeometry2D> readGeom =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();

        CoordinateReferenceSystem coordinateReferenceSystem =
                reader.getOriginalEnvelope().getCoordinateReferenceSystem();

        GridEnvelope2D gridRange = new GridEnvelope2D(0, 0, 100, 100);
        Envelope requestEnvelope =
                new ReferencedEnvelope(989960, 990800, 217380, 219200, coordinateReferenceSystem);
        GridGeometry2D readGeometry = new GridGeometry2D(gridRange, requestEnvelope);
        readGeom.setValue(readGeometry);
        GeneralParameterValue[] readParams =
                new GeneralParameterValue[] {footprintBehaviorParam, readGeom};
        GridCoverage2D coverage = reader.read(readParams);

        int numComponents = coverage.getRenderedImage().getColorModel().getNumComponents();
        assertEquals(numComponents, 4);

        reader.dispose();
    }

    @Test
    public void testCleanUpWkbFootprints() throws Exception {
        assertFootprintsCleanup(
                "footprint_wkbs",
                WKBLoaderSPI.class.getName(),
                true,
                "_%d",
                f -> f.getName().startsWith("r1c1"),
                FF.like(FF.property("location"), "r1c1*"));
    }

    @Test
    public void testCleanUpWktFootprints() throws Exception {
        assertFootprintsCleanup(
                "footprint_wkts",
                WKTLoaderSPI.class.getName(),
                true,
                "-%d",
                f -> f.getName().startsWith("r1c1"),
                FF.like(FF.property("location"), "r1c1*"));
    }

    @Test
    public void testCleanUpWktFootprintsAutoDetect() throws Exception {
        assertFootprintsCleanup(
                "footprint_wkts",
                null,
                false,
                "-%d",
                f -> f.getName().startsWith("r1c1"),
                FF.like(FF.property("location"), "r1c1*"));
    }

    private void assertFootprintsCleanup(
            String footprint_wkbs,
            String name,
            boolean overviewsInRasterSpace,
            String overviewsSuffixFormat,
            FileFilter fileFilter,
            PropertyIsLike granuleFilter)
            throws IOException {
        ImageMosaicReader reader =
                getMultipleSidecarReader(
                        footprint_wkbs, name, overviewsInRasterSpace, overviewsSuffixFormat);
        File directory = (File) reader.getSource();

        // collect the existing files matching the removal criteria
        FileFilter notFileFilter = f -> !fileFilter.accept(f);
        File[] existingFiles = directory.listFiles(fileFilter);
        assertThat(existingFiles, arrayWithSize(6));
        int otherFilesCount = directory.listFiles(notFileFilter).length;

        GranuleStore store =
                (GranuleStore) reader.getGranules(reader.getGridCoverageNames()[0], false);
        Hints hints = new Hints(Hints.GRANULE_REMOVAL_POLICY, GranuleRemovalPolicy.ALL);
        int removed = store.removeGranules(granuleFilter, hints);
        assertEquals(1, removed);

        // collect again, files should have been removed
        File[] existingFilesPastCleanup = directory.listFiles(fileFilter);
        assertThat(existingFilesPastCleanup, Matchers.emptyArray());
        assertEquals(otherFilesCount, directory.listFiles(notFileFilter).length);
    }
}
