/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import javax.imageio.stream.FileImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.operator.MosaicDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GroundControlPoints;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.coverage.grid.io.imageio.geotiff.TiePoint;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.Scale;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.DataSourceException;
import org.geotools.data.PrjFileReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.projection.Sinusoidal;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.test.TestData;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Projection;

/**
 * Testing {@link GeoTiffReader} as well as {@link IIOMetadataDumper}.
 *
 * @author Simone Giannecchini
 */
public class GeoTiffReaderTest extends org.junit.Assert {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GeoTiffReaderTest.class);

    static boolean oldOverrideInnerCRS;

    @Before
    public void saveGlobals() {
        oldOverrideInnerCRS = GeoTiffReader.OVERRIDE_INNER_CRS;
        GeoTiffReader.OVERRIDE_INNER_CRS = true;
    }

    @After
    public void cleanupGlobals() {
        System.clearProperty(GeoTiffReader.OVERRIDE_CRS_SWITCH);
        GeoTiffReader.OVERRIDE_INNER_CRS = oldOverrideInnerCRS;
    }

    /** Testing proper CRS override with PRJ. */
    @Test
    public void prjOverrideTesting1()
            throws IllegalArgumentException, IOException, FactoryException {

        //
        // PRJ override
        //
        final File noCrs = TestData.file(GeoTiffReaderTest.class, "override/sample.tif");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(noCrs));
        GeoTiffReader reader = (GeoTiffReader) format.getReader(noCrs);
        CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();

        final File prj = TestData.file(GeoTiffReaderTest.class, "override/sample.prj");
        final CoordinateReferenceSystem crs_ =
                new PrjFileReader(new FileInputStream(prj).getChannel())
                        .getCoordinateReferenceSystem();
        assertTrue(CRS.equalsIgnoreMetadata(crs, crs_));
        GridCoverage2D coverage = reader.read(null);
        assertTrue(CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(), crs_));

        coverage.dispose(true);
    }

    /** Testing proper CRS override with PRJ. */
    @Test
    public void prjOverrideTesting2()
            throws IllegalArgumentException, IOException, FactoryException {

        //
        // PRJ override
        //
        final File noCrs = TestData.file(GeoTiffReaderTest.class, "override/sample.tif");

        final File prj = TestData.file(GeoTiffReaderTest.class, "override/sample.prj");
        final CoordinateReferenceSystem crs_ =
                new PrjFileReader(new FileInputStream(prj).getChannel())
                        .getCoordinateReferenceSystem();

        // NO override
        GeoTiffReader.OVERRIDE_INNER_CRS = false;

        // getting a reader
        GeoTiffReader reader = new GeoTiffReader(noCrs);

        if (TestData.isInteractiveTest()) {
            IIOMetadataDumper iIOMetadataDumper =
                    new IIOMetadataDumper(reader.getMetadata().getRootNode());
            // System.out.println(iIOMetadataDumper.getMetadata());
        }
        // reading the coverage
        GridCoverage2D coverage1 = reader.read(null);

        // check coverage and crs
        assertNotNull(coverage1);
        assertNotNull(coverage1.getCoordinateReferenceSystem());
        assertNotSame(coverage1.getCoordinateReferenceSystem(), crs_);
        reader.dispose();

        coverage1.dispose(true);
    }
    /** Test for reading bad/strange geotiff files */
    @Test
    //    @Ignore
    public void testReaderBadGeotiff()
            throws IllegalArgumentException, IOException, FactoryException {

        //
        // no crs geotiff
        //
        final File noCrs = TestData.file(GeoTiffReaderTest.class, "no_crs.tif");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(noCrs));
        GeoTiffReader reader = (GeoTiffReader) format.getReader(noCrs);
        CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();
        assertTrue(CRS.equalsIgnoreMetadata(crs, AbstractGridFormat.getDefaultCRS()));
        GridCoverage2D coverage = reader.read(null);
        assertTrue(
                CRS.equalsIgnoreMetadata(
                        coverage.getCoordinateReferenceSystem(),
                        AbstractGridFormat.getDefaultCRS()));

        // hint for CRS
        crs = CRS.decode("EPSG:32632", true);
        final Hints hint = new Hints();
        hint.put(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, crs);

        // getting a reader
        reader = new GeoTiffReader(noCrs, hint);

        // reading the coverage
        GridCoverage2D coverage1 = reader.read(null);

        // check coverage and crs
        assertNotNull(coverage1);
        assertNotNull(coverage1.getCoordinateReferenceSystem());
        assertEquals(
                CRS.lookupIdentifier(coverage1.getCoordinateReferenceSystem(), true), "EPSG:32632");
        reader.dispose();

        //
        // use prj and wld
        //
        final File wldprjFile = TestData.file(GeoTiffReaderTest.class, "no_crs_no_envelope.tif");
        assertTrue(format.accepts(wldprjFile));

        // getting a reader
        reader = new GeoTiffReader(wldprjFile);

        // reading the coverage
        GridCoverage2D coverage2 = reader.read(null);

        // check coverage and crs
        assertNotNull(coverage2);
        assertNotNull(coverage2.getCoordinateReferenceSystem());
        assertEquals(
                CRS.lookupIdentifier(coverage2.getCoordinateReferenceSystem(), true), "EPSG:32632");
        reader.dispose();

        //
        // use prj and hint
        //
        final File wldFile = TestData.file(GeoTiffReaderTest.class, "no_crs_no_envelope2.tif");
        assertTrue(format.accepts(wldFile));

        // getting a reader
        reader = new GeoTiffReader(wldFile, hint);

        // reading the coverage
        GridCoverage2D coverage3 = reader.read(null);

        // check coverage and crs
        assertNotNull(coverage3);
        assertNotNull(coverage3.getCoordinateReferenceSystem());
        assertEquals(
                CRS.lookupIdentifier(coverage3.getCoordinateReferenceSystem(), true), "EPSG:32632");
        reader.dispose();

        coverage1.dispose(true);
        coverage2.dispose(true);
        coverage3.dispose(true);
    }

    /** Test for reading geotiff files */
    @Test
    public void testReader() throws Exception {

        final File baseDirectory = TestData.file(GeoTiffReaderTest.class, ".");
        final File writeDirectory =
                new File(baseDirectory, Long.toString(System.currentTimeMillis()));
        writeDirectory.mkdir();
        final File files[] = baseDirectory.listFiles();
        final int numFiles = files.length;
        final AbstractGridFormat format = new GeoTiffFormat();
        for (int i = 0; i < numFiles; i++) {
            StringBuilder buffer = new StringBuilder();
            final String path = files[i].getAbsolutePath().toLowerCase();
            if (!path.endsWith("tif") && !path.endsWith("tiff") || path.contains("no_crs"))
                continue;

            buffer.append(files[i].getAbsolutePath()).append("\n");
            Object o;
            if (i % 2 == 0)
                // testing file
                o = files[i];
            else
                // testing url
                o = files[i].toURI().toURL();
            if (format.accepts(o)) {
                buffer.append("ACCEPTED").append("\n");

                // getting a reader
                GeoTiffReader reader =
                        new GeoTiffReader(
                                o, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
                if (reader != null) {

                    // reading the coverage
                    final GridCoverage2D coverage = reader.read(null);

                    // Crs and envelope
                    if (TestData.isInteractiveTest()) {
                        buffer.append("CRS: ")
                                .append(coverage.getCoordinateReferenceSystem2D().toWKT())
                                .append("\n");
                        buffer.append("GG: ")
                                .append(coverage.getGridGeometry().toString())
                                .append("\n");
                    }
                    // display metadata
                    if (org.geotools.TestData.isExtensiveTest()) {
                        IIOMetadataDumper iIOMetadataDumper =
                                new IIOMetadataDumper(reader.getMetadata().getRootNode());
                        buffer.append("TIFF metadata: ")
                                .append(iIOMetadataDumper.getMetadata())
                                .append("\n");
                    }

                    // layout checks
                    final ImageLayout layout = reader.getImageLayout();
                    assertNotNull(layout);
                    assertNotNull(layout.getColorModel(null));
                    assertNotNull(layout.getSampleModel(null));
                    assertEquals(0, layout.getMinX(null));
                    assertEquals(0, layout.getMinY(null));
                    assertTrue(layout.getWidth(null) > 0);
                    assertTrue(layout.getHeight(null) > 0);
                    assertEquals(0, layout.getTileGridXOffset(null));
                    assertEquals(0, layout.getTileGridYOffset(null));
                    assertTrue(layout.getTileHeight(null) > 0);
                    assertTrue(layout.getTileWidth(null) > 0);

                    // showing it
                    if (TestData.isInteractiveTest()) {
                        coverage.show();
                    } else {
                        PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
                    }

                    if (reader.getGroundControlPoints() != null) {
                        // we cannot write GCPs yet
                        continue;
                    }

                    // write and read back
                    writeAndReadBackCheck(coverage, format, writeDirectory, o);
                }

            } else buffer.append("NOT ACCEPTED").append("\n");
            if (TestData.isInteractiveTest()) LOGGER.info(buffer.toString());
        }
    }

    private void writeAndReadBackCheck(
            GridCoverage2D coverage, AbstractGridFormat format, File writeDirectory, Object o)
            throws IOException, FactoryException {
        final File destFile = File.createTempFile("test", ".tif", writeDirectory);
        final GeoTiffWriter writer = new GeoTiffWriter(destFile);
        writer.write(coverage, null);
        writer.dispose();

        // read back
        assertTrue(format.accepts(destFile));
        GeoTiffReader reader =
                new GeoTiffReader(
                        destFile, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
        final GridCoverage2D destCoverage = reader.read(null);
        reader.dispose();

        final double eps =
                XAffineTransform.getScaleX0(
                                (AffineTransform) coverage.getGridGeometry().getGridToCRS())
                        * 1E-2;
        String toString = o.toString();
        assertTrue(
                "CRS comparison failed:" + toString,
                CRS.findMathTransform(
                                coverage.getCoordinateReferenceSystem(),
                                destCoverage.getCoordinateReferenceSystem(),
                                true)
                        .isIdentity());
        assertTrue(
                "CRS comparison failed:" + toString,
                CRS.equalsIgnoreMetadata(
                        coverage.getCoordinateReferenceSystem(),
                        destCoverage.getCoordinateReferenceSystem()));
        assertTrue(
                "GridRange comparison failed:" + toString,
                coverage.getGridGeometry()
                        .getGridRange()
                        .equals(destCoverage.getGridGeometry().getGridRange()));
        assertTrue(
                "Envelope comparison failed:" + toString,
                ((GeneralEnvelope) coverage.getGridGeometry().getEnvelope())
                        .equals(destCoverage.getGridGeometry().getEnvelope(), eps, false));
        coverage.dispose(true);
        destCoverage.dispose(true);
    }

    @Test
    public void testBandNames() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "wind.tiff");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        GridCoverage2D coverage = format.getReader(file).read(null);

        String band1Name = coverage.getSampleDimension(0).getDescription().toString();
        String band2Name = coverage.getSampleDimension(1).getDescription().toString();
        assertEquals("Band1", band1Name);
        assertEquals("Band2", band2Name);
    }

    @Test
    public void testThreadedTransformations() throws Exception {
        Callable<Void> callable =
                new Callable<Void>() {

                    @Override
                    public Void call() throws Exception {
                        final File baseDirectory = TestData.file(GeoTiffReaderTest.class, ".");
                        final File files[] =
                                baseDirectory.listFiles(
                                        new FilenameFilter() {

                                            @Override
                                            public boolean accept(File dir, String name) {
                                                String lcName = name.toLowerCase();
                                                return lcName.endsWith("tif")
                                                        || lcName.endsWith("tiff");
                                            }
                                        });
                        final AbstractGridFormat format = new GeoTiffFormat();
                        for (File file : files) {
                            AbstractGridCoverage2DReader reader = null;
                            try {
                                reader = format.getReader(file);
                                if (reader != null) {
                                    GridCoverage2D coverage = reader.read(null);
                                    ImageIOUtilities.disposeImage(coverage.getRenderedImage());
                                    coverage.dispose(true);
                                }
                            } finally {
                                if (reader != null) {
                                    reader.dispose();
                                }
                            }
                        }
                        return null;
                    }
                };

        // used to deadlock under load, check it does not now
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            List<Future<Void>> futures = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                Future<Void> f = executor.submit(callable);
                futures.add(f);
            }

            for (Future<Void> f : futures) {
                f.get();
            }

        } finally {
            executor.shutdown();
        }
    }

    /** Test that the reader is able to read NoData */
    @Test
    public void testNoData() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "nodata.tiff");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        GridCoverage2D coverage = format.getReader(file).read(null);
        // Ensure proper NoData is set
        NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(coverage);
        assertNotNull(noDataProperty);

        Range nd = noDataProperty.getAsRange();
        assertNotNull(nd);

        assertTrue(nd.isPoint());
        assertEquals(nd.getMin().doubleValue(), -9999, 0.001);
        assertEquals(nd.getMax().doubleValue(), -9999, 0.001);

        // check the image too
        RenderedImage image = coverage.getRenderedImage();
        Object property = image.getProperty(NoDataContainer.GC_NODATA);
        assertThat(property, CoreMatchers.instanceOf(NoDataContainer.class));
        NoDataContainer nc = (NoDataContainer) property;
        assertEquals(-9999, nc.getAsSingleValue(), 0.0001);
    }

    /**
     * Test that the reader sets a Meters as default UoM definition for CRS with an undefined UoM
     * definition
     */
    @Test
    public void testUoMDefault() throws Exception {
        // Reading file
        final File file = TestData.file(GeoTiffReaderTest.class, "no_uom.tif");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        GridCoverage2D coverage = format.getReader(file).read(null);
        // Getting CRS
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
        // Getting its string definition
        String crsDef = crs.toWKT();
        // Ensure the Unit of Measure define is Meter
        assertTrue(crsDef.contains("UNIT[\"m\", 1.0]"));
    }

    /** Test that the reader sets a ROI property based on the input internal masks */
    @Test
    public void testMasking() throws Exception {
        // Reading file
        final File file = TestData.file(GeoTiffReaderTest.class, "mask/masked.tif");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        AbstractGridCoverage2DReader reader = format.getReader(file);
        GridCoverage2D coverage = reader.read(null);
        // Checking if ROI is present
        checkCoverageROI(coverage);
        // Getting DatasetLayout and testing it
        DatasetLayout layout = reader.getDatasetLayout();
        Assert.assertEquals(5, layout.getNumInternalMasks());
        Assert.assertEquals(-1, layout.getNumExternalMasks());
        Assert.assertEquals(4, layout.getNumInternalOverviews());
        Assert.assertEquals(-1, layout.getNumExternalOverviews());
        Assert.assertEquals(-1, layout.getNumExternalMaskOverviews());
        Assert.assertNull(layout.getExternalMasks());
        Assert.assertNull(layout.getExternalOverviews());
        Assert.assertNull(layout.getExternalMaskOverviews());

        // Doing a minor Operation in order to make ROI available
        CoverageProcessor processor = CoverageProcessor.getInstance();
        Scale scaleOp = (Scale) processor.getOperation("Scale");
        // getting operation parameters
        ParameterValueGroup parameters = scaleOp.getParameters();
        // Setting the parameters
        parameters.parameter("Source").setValue(coverage);
        parameters.parameter("xScale").setValue(Float.valueOf(3f));
        parameters.parameter("yScale").setValue(Float.valueOf(3f));
        parameters.parameter("xTrans").setValue(Float.valueOf(0.0f));
        parameters.parameter("yTrans").setValue(Float.valueOf(0.0f));
        parameters
                .parameter("Interpolation")
                .setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        // Executing operation
        coverage = (GridCoverage2D) scaleOp.doOperation(parameters, null);
        // Checking if ROI is present
        checkCoverageROI(coverage);

        // Evaluate results
        byte[] results = new byte[3];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-87.517, 25.302);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        // Should be > 0
        position.setLocation(-87.005, 26.336);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        // Should be 0
        position.setLocation(-87.891, 26.159);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        // Should be > 0
        position.setLocation(-86.401, 26.297);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        // Should be 0
        position.setLocation(-87.411, 27.289);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
    }

    /**
     * Test that the reader sets a ROI property based on the input internal masks, and that the ROI
     * is the expected one, taking into account the same subsampling factors during the read as the
     * image
     */
    @Test
    public void testMaskSubsampling() throws Exception {
        // Reading file
        final File file = TestData.file(GeoTiffReaderTest.class, "mask/masked2.tif");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        AbstractGridCoverage2DReader reader = format.getReader(file);

        // prepare to read a sub.sampled image
        GeneralParameterValue[] params = new GeneralParameterValue[1];
        // Define a GridGeometry in order to reduce the output
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 4,
                reader.getOriginalGridRange().getSpan(1) / 4);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));
        params[0] = gg;

        GridCoverage2D coverage = reader.read(params);
        // Checking if ROI is present
        checkCoverageROI(coverage);

        // check the ROI and the image are black in the same pixels
        ROI roi = CoverageUtilities.getROIProperty(coverage);
        Raster roiImage = roi.getAsImage().getData();
        Raster image = coverage.getRenderedImage().getData();

        int[] px = new int[3];
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

    /** Test that the reader sets a ROI property based on the input external masks */
    @Test
    public void testMaskingExternal() throws Exception {
        // Reading file
        final File file = TestData.file(GeoTiffReaderTest.class, "mask/external.tif");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        AbstractGridCoverage2DReader reader = format.getReader(file);
        GridCoverage2D coverage = reader.read(null);
        // Checking if ROI is present
        checkCoverageROI(coverage);
        // Getting DatasetLayout and testing it
        DatasetLayout layout = reader.getDatasetLayout();
        Assert.assertEquals(0, layout.getNumInternalMasks());
        Assert.assertEquals(1, layout.getNumExternalMasks());
        Assert.assertEquals(4, layout.getNumInternalOverviews());
        Assert.assertEquals(0, layout.getNumExternalOverviews());
        Assert.assertEquals(0, layout.getNumExternalMaskOverviews());
        Assert.assertTrue(!layout.getExternalMasks().getAbsolutePath().isEmpty());
        Assert.assertNull(layout.getExternalOverviews());
        Assert.assertNull(layout.getExternalMaskOverviews());

        // Doing a minor Operation in order to make ROI available
        CoverageProcessor processor = CoverageProcessor.getInstance();
        Scale scaleOp = (Scale) processor.getOperation("Scale");
        // getting operation parameters
        ParameterValueGroup parameters = scaleOp.getParameters();
        // Setting the parameters
        parameters.parameter("Source").setValue(coverage);
        parameters.parameter("xScale").setValue(Float.valueOf(3f));
        parameters.parameter("yScale").setValue(Float.valueOf(3f));
        parameters.parameter("xTrans").setValue(Float.valueOf(0.0f));
        parameters.parameter("yTrans").setValue(Float.valueOf(0.0f));
        parameters
                .parameter("Interpolation")
                .setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        // Executing operation
        coverage = (GridCoverage2D) scaleOp.doOperation(parameters, null);
        // Checking if ROI is present
        checkCoverageROI(coverage);

        // Evaluate results
        byte[] results = new byte[3];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-87.517, 25.302);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        // Should be > 0
        position.setLocation(-87.005, 26.336);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        // Should be 0
        position.setLocation(-87.891, 26.159);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        // Should be > 0
        position.setLocation(-86.401, 26.297);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        // Should be 0
        position.setLocation(-87.411, 27.289);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
    }

    /** Test that the reader can read a GeoTIFF with GCPs (even if it cannot reference it) */
    @Test
    public void testGCPs() throws Exception {
        // Reading file
        final File file = TestData.file(GeoTiffReaderTest.class, "box_gcp.tif");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(file));
        AbstractGridCoverage2DReader reader = format.getReader(file);
        GridCoverage2D coverage = reader.read(null);

        // Get CRS and transform, they should be 404000 and
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
        assertEquals(AbstractGridFormat.getDefaultCRS(), crs);
        assertEquals(
                ProjectiveTransform.create(new AffineTransform()),
                coverage.getGridGeometry().getGridToCRS());
        // Getting its string definition
        String crsDef = crs.toWKT();
        // Ensure the Unit of Measure define is Meter
        assertTrue(crsDef.contains("UNIT[\"m\", 1.0]"));

        // Ground control points
        GroundControlPoints gcps = reader.getGroundControlPoints();
        assertNotNull(gcps);
        // the tie point CRS has the same size as WGS84)
        GeographicCRS gcrs = (GeographicCRS) gcps.getCoordinateReferenceSystem();
        Ellipsoid ellipsoid = gcrs.getDatum().getEllipsoid();
        assertEquals(
                ellipsoid.getSemiMajorAxis(),
                DefaultGeographicCRS.WGS84.getDatum().getEllipsoid().getSemiMajorAxis(),
                1e-6);
        assertEquals(
                ellipsoid.getInverseFlattening(),
                DefaultGeographicCRS.WGS84.getDatum().getEllipsoid().getInverseFlattening(),
                1e-6);
        // check the tie points
        final double EPS = 1e-9;
        List<TiePoint> tiePoints = gcps.getTiePoints();
        // t1
        assertEquals(49.5005, tiePoints.get(0).getValueAt(0), EPS);
        assertEquals(250.909, tiePoints.get(0).getValueAt(1), EPS);
        assertEquals(-84, tiePoints.get(0).getValueAt(3), EPS);
        assertEquals(33, tiePoints.get(0).getValueAt(4), EPS);
        // t2
        assertEquals(49.5005, tiePoints.get(1).getValueAt(0), EPS);
        assertEquals(51.8182, tiePoints.get(1).getValueAt(1), EPS);
        assertEquals(-84, tiePoints.get(1).getValueAt(3), EPS);
        assertEquals(34, tiePoints.get(1).getValueAt(4), EPS);
        // t3
        assertEquals(248.824, tiePoints.get(2).getValueAt(0), EPS);
        assertEquals(51.8182, tiePoints.get(2).getValueAt(1), EPS);
        assertEquals(-83, tiePoints.get(2).getValueAt(3), EPS);
        assertEquals(34, tiePoints.get(2).getValueAt(4), EPS);
    }

    /** Test that the reader can read a GeoTIFF with Sinusoidal projection */
    @Test
    public void testSinusoidalCRS() throws Exception {
        // Reading file
        final File file = TestData.file(GeoTiffReaderTest.class, "worldsinus.tif");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(file));
        AbstractGridCoverage2DReader reader = null;
        try {
            reader =
                    format.getReader(
                            file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            GridCoverage2D coverage = reader.read(null);

            CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem();
            assertTrue(crs instanceof ProjectedCRS);
            Projection conversion = ((ProjectedCRS) crs).getConversionFromBase();
            assertNotNull(conversion);

            MathTransform transform = conversion.getMathTransform();
            assertNotNull(transform);
            assertTrue(transform instanceof Sinusoidal);
            final File writeDirectory =
                    new File(
                            TestData.file(GeoTiffReaderTest.class, "."),
                            Long.toString(System.currentTimeMillis()));
            writeDirectory.mkdir();
            writeAndReadBackCheck(coverage, format, writeDirectory, file);

        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /**
     * Test that the reader sets a ROI property based on the input external masks with external
     * overviews
     */
    @Test
    public void testMaskingExternalOverviews() throws Exception {
        // Reading file
        final File file = TestData.file(GeoTiffReaderTest.class, "mask/external2.tif");
        assertNotNull(file);
        final AbstractGridFormat format = new GeoTiffFormat();
        AbstractGridCoverage2DReader reader = format.getReader(file);
        GeneralParameterValue[] params = new GeneralParameterValue[2];
        // Define a GridGeometry in order to reduce the output
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));
        params[0] = gg;
        // Define Overview Policy
        final ParameterValue<OverviewPolicy> policy =
                AbstractGridFormat.OVERVIEW_POLICY.createValue();
        policy.setValue(OverviewPolicy.NEAREST);
        params[1] = policy;
        GridCoverage2D coverage = reader.read(params);
        // Checking if ROI is present
        checkCoverageROI(coverage);
        // Getting DatasetLayout and testing it
        DatasetLayout layout = reader.getDatasetLayout();
        Assert.assertEquals(0, layout.getNumInternalMasks());
        Assert.assertEquals(1, layout.getNumExternalMasks());
        Assert.assertEquals(4, layout.getNumInternalOverviews());
        Assert.assertEquals(0, layout.getNumExternalOverviews());
        Assert.assertEquals(4, layout.getNumExternalMaskOverviews());
        Assert.assertTrue(!layout.getExternalMasks().getAbsolutePath().isEmpty());
        Assert.assertNull(layout.getExternalOverviews());
        Assert.assertTrue(!layout.getExternalMaskOverviews().getAbsolutePath().isEmpty());

        // Doing a minor Operation in order to make ROI available
        CoverageProcessor processor = CoverageProcessor.getInstance();
        Scale scaleOp = (Scale) processor.getOperation("Scale");
        // getting operation parameters
        ParameterValueGroup parameters = scaleOp.getParameters();
        // Setting the parameters
        parameters.parameter("Source").setValue(coverage);
        parameters.parameter("xScale").setValue(Float.valueOf(3f));
        parameters.parameter("yScale").setValue(Float.valueOf(3f));
        parameters.parameter("xTrans").setValue(Float.valueOf(0.0f));
        parameters.parameter("yTrans").setValue(Float.valueOf(0.0f));
        parameters
                .parameter("Interpolation")
                .setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        // Executing operation
        coverage = (GridCoverage2D) scaleOp.doOperation(parameters, null);
        // Checking if ROI is present
        checkCoverageROI(coverage);
        // Evaluate results
        byte[] results = new byte[3];
        DirectPosition2D position = new DirectPosition2D();
        // Should be 0
        position.setLocation(-87.517, 25.25);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        // Should be > 0
        position.setLocation(-87.005, 26.336);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        // Should be 0
        position.setLocation(-87.891, 26.159);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
        // Should be > 0
        position.setLocation(-86.401, 26.297);
        results = coverage.evaluate(position, results);
        assertTrue(results[0] != 0);
        assertTrue(results[1] != 0);
        assertTrue(results[2] != 0);
        // Should be 0
        position.setLocation(-87.411, 27.289);
        results = coverage.evaluate(position, results);
        assertEquals(results[0], 0);
        assertEquals(results[1], 0);
        assertEquals(results[2], 0);
    }

    /**
     * Private method for checking if ROI size and image size are equals
     *
     * @param coverage Input {@link GridCoverage2D} to test
     */
    private void checkCoverageROI(GridCoverage2D coverage) {
        ROI roi = CoverageUtilities.getROIProperty(coverage);
        assertNotNull(roi);
        // Ensure has the same size of the input image
        checkROI(coverage, roi);
        // make sure the ROI is also present at the image level
        Object imageRoi = coverage.getRenderedImage().getProperty("ROI");
        assertTrue(imageRoi instanceof ROI);
        checkROI(coverage, (ROI) imageRoi);
    }

    private void checkROI(GridCoverage2D coverage, ROI roi) {
        Rectangle roiBounds = roi.getBounds();
        Rectangle imgBounds = coverage.getGridGeometry().getGridRange2D();
        assertEquals(imgBounds.x, roiBounds.x);
        assertEquals(imgBounds.y, roiBounds.y);
        assertEquals(imgBounds.width, roiBounds.width);
        assertEquals(imgBounds.height, roiBounds.height);
    }

    /** Test what we can do and what not with */
    @Test
    //    @Ignore
    public void testTransparencySettings() throws Exception {

        final AbstractGridFormat format = new GeoTiffFormat();
        File file = TestData.file(GeoTiffReaderTest.class, "002025_0100_010722_l7_01_utm2.tiff");
        if (format.accepts(file)) {
            // getting a reader
            GeoTiffReader reader =
                    new GeoTiffReader(
                            file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            if (reader != null) {
                // reading the coverage
                GridCoverage2D coverage = reader.read(null);
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 1);
                final ParameterValue<Color> colorPV =
                        AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
                colorPV.setValue(Color.BLACK);
                coverage = reader.read(new GeneralParameterValue[] {colorPV});
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 2);

                // showing it
                if (TestData.isInteractiveTest()) coverage.show();
                else PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
            }

        } else assertFalse(true); // we should not get here

        file = TestData.file(GeoTiffReaderTest.class, "gaarc_subset.tiff");
        if (format.accepts(file)) {
            // getting a reader
            GeoTiffReader reader =
                    new GeoTiffReader(
                            file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            if (reader != null) {
                // reading the coverage
                GridCoverage2D coverage = reader.read(null);
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 3);
                final ParameterValue<Color> colorPV =
                        AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
                colorPV.setValue(new Color(34, 53, 87));
                coverage = reader.read(new GeneralParameterValue[] {colorPV});
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 4);

                // showing it
                if (TestData.isInteractiveTest()) coverage.show();
                else PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
            }

        } else assertFalse(true); // we should not get here

        // now we test that we cannot do colormasking on a non-rendered output
        file = TestData.file(GeoTiffReaderTest.class, "wind.tiff");
        if (format.accepts(file)) {
            // getting a reader
            GeoTiffReader reader =
                    new GeoTiffReader(
                            file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
            if (reader != null) {
                // reading the coverage
                GridCoverage2D coverage = reader.read(null);
                assertNotNull(coverage);
                assertTrue(coverage.getRenderedImage().getSampleModel().getNumBands() == 2);
                final ParameterValue<Color> colorPV =
                        AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
                colorPV.setValue(new Color(34, 53, 87));
                try {
                    coverage = reader.read(new GeneralParameterValue[] {colorPV});
                    assertFalse(true); // we should not get here
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }

    @Test
    public void testCoverageName() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "wind.tiff");
        assertNotNull(file);

        GeoTiffReader reader = new GeoTiffReader(file, GeoTools.getDefaultHints());
        assertTrue(reader.checkName("geotiff_coverage"));
    }

    @Test
    //    @Ignore
    public void testExternalOverviews() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "ovr.tif");
        assertNotNull(file);
        assertEquals(true, file.exists());
        GeoTiffReader reader = new GeoTiffReader(file);
        final int nOvrs = reader.getDatasetLayout().getNumExternalOverviews();
        LOGGER.info("Number of external overviews: " + nOvrs);
        assertEquals(4, nOvrs);
        double[][] availableResolutions = reader.getResolutionLevels();
        assertEquals(availableResolutions.length, 5);

        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 64.0,
                reader.getOriginalGridRange().getSpan(1) / 64.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        GridGeometry2D gridGeometry = new GridGeometry2D(range, envelope);
        gg.setValue(gridGeometry);

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {gg});
        assertEquals(reader.getOriginalEnvelope(), coverage.getEnvelope());
        RenderedImage image = coverage.getRenderedImage();
        assertEquals(image.getWidth(), 2);
        assertEquals(image.getHeight(), 2);

        final double delta = 0.00001;
        assertEquals(availableResolutions[0][0], 5, delta);
        assertEquals(availableResolutions[0][1], 5, delta);

        assertEquals(availableResolutions[1][0], 10, delta);
        assertEquals(availableResolutions[1][1], 10, delta);

        assertEquals(availableResolutions[2][0], 20, delta);
        assertEquals(availableResolutions[2][1], 20, delta);

        assertEquals(availableResolutions[3][0], 40, delta);
        assertEquals(availableResolutions[3][1], 40, delta);

        assertEquals(availableResolutions[4][0], 80, delta);
        assertEquals(availableResolutions[4][1], 80, delta);

        MathTransform transform = gridGeometry.getGridToCRS();
        AffineTransform affine = (AffineTransform) transform;
        double resX = XAffineTransform.getScaleX0(affine);
        double resY = XAffineTransform.getScaleY0(affine);

        // Using "poor" resolution (less than the worst available overview).
        double[] resolutions =
                reader.getReadingResolutions(OverviewPolicy.QUALITY, new double[] {resX, resY});
        // Checking that the reading resolution will be the one of the worst (last) overview
        assertEquals(resolutions[0], availableResolutions[nOvrs][0], delta);
        assertEquals(resolutions[1], availableResolutions[nOvrs][1], delta);

        // Using a middle resolution
        resolutions =
                reader.getReadingResolutions(
                        OverviewPolicy.QUALITY, new double[] {resX / 8, resY / 8});
        assertEquals(resolutions[0], 40, delta);
        assertEquals(resolutions[1], 40, delta);

        // Using native resolution
        resolutions = reader.getReadingResolutions(OverviewPolicy.QUALITY, availableResolutions[0]);
        assertEquals(resolutions[0], availableResolutions[0][0], delta);
        assertEquals(resolutions[1], availableResolutions[0][1], delta);
    }

    /**
     * The leak geotiff is a strange geotiff with PixelScale and TiePoints that are all 0 hence the
     * matrix we come up with is all 0 and not invertibile.
     *
     * <p>This is not acceptable as we need a transformation that allows us to go back and forth
     * between raster and model space.
     *
     * <p>Side effect of this, we leak an open file due to the exception thrown during a read
     * operation.
     */
    @Test
    // @Ignore
    public void testLeakedOpenFileFix() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "leak.tiff");
        assertNotNull(file);
        assertEquals(true, file.exists());

        try {

            @SuppressWarnings("unused")
            GeoTiffReader reader = new GeoTiffReader(file);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }

        // this files if things went wrong and the fix is not working (on Windows especially)
        assertTrue(file.delete());
    }

    /**
     * The GeoTiff reader should provide a useful error message when the user does not have
     * permissions to read the file.
     */
    @Test
    public void testPermissionDeniedExceptionFix() throws Exception {
        final File file = TestData.file(GeoTiffReaderTest.class, "t.tiff");

        file.setReadable(false);

        try {
            GeoTiffReader reader = new GeoTiffReader(file);
        } catch (DataSourceException e) {
            if (e.getCause() instanceof IOException) {
                IOException ioException = (IOException) e.getCause();
                assertTrue(ioException.getMessage().contains("can not be read"));
            } else {
                assertFalse(true);
            }
        } finally {
            file.setReadable(true);
        }
    }

    /**
     * The GeoTiff reader should provide a useful error message when the input file path does not
     * exist.
     */
    @Test(expected = FileNotFoundException.class)
    public void testFileDoesNotExist() throws Throwable {
        final File file = new File("/does/not/exist");

        file.setReadable(false);

        try {
            GeoTiffReader reader = new GeoTiffReader(file);
        } catch (DataSourceException e) {
            // Throw the inner exception
            throw e.getCause();
        } finally {
            file.setReadable(true);
        }
    }

    /** The GeoTiffReader should be able to read from an InputStream */
    @Test
    public void testCanReadInputStream() throws IOException {

        File rasterfile = TestData.file(GeoTiffReaderTest.class, "geo.tiff");
        GeoTiffReader reader = null;

        try (FileInputStream is = new FileInputStream(rasterfile)) {
            // Read coverage
            reader = new GeoTiffReader(is);
            GridCoverage2D gridCoverage = reader.read(null);

            assertTrue(gridCoverage != null && gridCoverage.getNumSampleDimensions() == 1);

            gridCoverage.dispose(true);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /** The GeoTiffReader should be able to read from an ImageInputStream */
    @Test
    public void testCanReadImageInputStream() throws IOException {
        File rasterfile = TestData.file(GeoTiffReaderTest.class, "geo.tiff");
        GeoTiffReader reader = null;

        try (FileImageInputStream is = new FileImageInputStream(rasterfile)) {
            // Read coverage
            reader = new GeoTiffReader(is);
            GridCoverage2D gridCoverage = reader.read(null);

            assertTrue(gridCoverage != null && gridCoverage.getNumSampleDimensions() == 1);

            gridCoverage.dispose(true);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    @Test
    public void testScaleOffset() throws IllegalArgumentException, IOException, FactoryException {
        // prepare reader
        final File scaleOffset = TestData.file(GeoTiffReaderTest.class, "scaleOffset.tif");
        GeoTiffReader reader = new GeoTiffReader(scaleOffset);

        // read with explicit request not to rescale
        GridCoverage2D coverage = null;
        try {
            ParameterValue<Boolean> rescalePixels = AbstractGridFormat.RESCALE_PIXELS.createValue();
            rescalePixels.setValue(false);
            coverage = reader.read(new GeneralParameterValue[] {rescalePixels});
            ImageWorker iw = new ImageWorker(coverage.getRenderedImage());
            double noData = iw.getNoData().getMin().doubleValue();
            double[] maximums = iw.getMaximums();
            double[] minimums = iw.getMinimums();
            // the max is 255
            assertArrayEquals(new double[] {6020, 6020, 6020, 1, 0, 10000}, maximums, 0d);
            assertArrayEquals(new double[] {-2473, -2473, -2473, 0, 0, 0}, minimums, 0d);

            Raster data = coverage.getRenderedImage().getData();
            // The pixel in the top right corner should be nodata
            double sample = data.getSampleDouble(data.getWidth() - 1, 0, 0);
            assertEquals(noData, sample, 0d);

        } finally {
            if (coverage != null) {
                ImageUtilities.disposeImage(coverage.getRenderedImage());
                coverage.dispose(true);
            }
        }

        // do the same with rescaling
        try {
            ParameterValue<Boolean> rescalePixels = AbstractGridFormat.RESCALE_PIXELS.createValue();
            rescalePixels.setValue(true);
            coverage = reader.read(new GeneralParameterValue[] {rescalePixels});
            ImageWorker iw = new ImageWorker(coverage.getRenderedImage());
            double noData = iw.getNoData().getMin().doubleValue();
            double[] maximums = iw.getMaximums();
            double[] minimums = iw.getMinimums();

            assertArrayEquals(new double[] {0.602, 0.602, 0.602, 0.0001, 0, 1}, maximums, 1E-6d);
            assertArrayEquals(new double[] {-0.2473, -0.2473, -0.2473, 0, 0, 0}, minimums, 1E-6d);
            Raster data = coverage.getRenderedImage().getData();
            // The pixel in the top right corner should be nodata
            double sample = data.getSampleDouble(data.getWidth() - 1, 0, 0);
            assertEquals(noData, sample, 0d);

            // Make sure that noData isn't rescaled too
        } finally {
            if (coverage != null) {
                ImageUtilities.disposeImage(coverage.getRenderedImage());
                coverage.dispose(true);
            }
        }
    }

    @Test
    public void testRescaleWithNodataFloat() throws IOException {
        GridCoverage2DReader reader =
                new GeoTiffReader(TestData.file(GeoTiffReaderTest.class, "float32_nodata.tif"));
        GridCoverage2D coverage = null;
        double[] redBackground = new double[] {255, 0, 0};
        try {
            coverage = reader.read(null);
            ImageWorker iw = new ImageWorker(coverage.getRenderedImage());

            RenderedImage output =
                    iw.scale(64, 64, 0, 0, Interpolation.getInstance(Interpolation.INTERP_NEAREST))
                            .rescaleToBytes()
                            .bandMerge(3)
                            .forceComponentColorModel()
                            .forceColorSpaceRGB()
                            .getRenderedImage();
            output =
                    new ImageWorker(output)
                            .setBackground(redBackground)
                            .mosaic(
                                    new RenderedImage[] {output},
                                    MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                                    null,
                                    null,
                                    null,
                                    null)
                            .getRenderedImage();

            Raster data = output.getData();
            final int w = data.getWidth();
            final int h = data.getHeight();

            // Before the fix, all the samples were simply either 0,0,0 or 1,1,1
            checkPixel(data, 0, 0, gray(1));
            checkPixel(data, w - 1, 0, gray(43));
            checkPixel(data, w / 2, h / 2, gray(85));
            checkPixel(data, w - 1, h - 1, gray(255));
            checkPixel(data, 0, h - 1, Color.RED);
        } finally {
            if (coverage != null) {
                ImageUtilities.disposeImage(coverage.getRenderedImage());
                coverage.dispose(true);
            }
        }
    }

    private static void checkPixel(Raster data, int x, int y, Color expectedColor) {
        int[] pixel = new int[3];
        data.getPixel(x, y, pixel);
        Color color = new Color(pixel[0], pixel[1], pixel[2]);
        assertEquals(expectedColor, color);
    }

    private static Color gray(int i) {
        return new Color(i, i, i);
    }
}
