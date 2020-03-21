/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.spi.ImageReaderSpi;
import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.TransformException;

/**
 * Testing {@link OverviewsController}.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class OverviewsControllerTest extends Assert {

    static double THRESHOLD = 0.000001;

    static class TestSet {

        public TestSet(OverviewConfig[] ot) {
            super();
            this.ot = ot;
        }

        double resolution;

        OverviewConfig[] ot;
    }

    static class GranuleParams {

        public GranuleParams(int imageIndex, int ssx, int ssy) {
            super();
            this.imageIndex = imageIndex;
            this.ssx = ssx;
            this.ssy = ssy;
        }

        int imageIndex;

        int ssx;

        int ssy;
    }

    static class OverviewConfig {

        public OverviewConfig(OverviewPolicy ovPolicy, GranuleParams g1, GranuleParams g2) {
            super();
            this.ovPolicy = ovPolicy;
            this.g1 = g1;
            this.g2 = g2;
        }

        OverviewPolicy ovPolicy;

        GranuleParams g1;

        GranuleParams g2;
    }

    private static final TestSet at1 =
            new TestSet(
                    new OverviewConfig[] {
                        new OverviewConfig(
                                OverviewPolicy.QUALITY,
                                new GranuleParams(3, 1, 1),
                                new GranuleParams(2, 1, 1)),
                        new OverviewConfig(
                                OverviewPolicy.SPEED,
                                new GranuleParams(4, 1, 1),
                                new GranuleParams(2, 1, 1)),
                        new OverviewConfig(
                                OverviewPolicy.NEAREST,
                                new GranuleParams(3, 1, 1),
                                new GranuleParams(2, 1, 1)),
                        new OverviewConfig(
                                OverviewPolicy.IGNORE,
                                new GranuleParams(0, 9, 9),
                                new GranuleParams(0, 5, 5))
                    });
    private static final TestSet at2 =
            new TestSet(
                    new OverviewConfig[] {
                        new OverviewConfig(
                                OverviewPolicy.QUALITY,
                                new GranuleParams(3, 1, 1),
                                new GranuleParams(2, 1, 2)),
                        new OverviewConfig(
                                OverviewPolicy.SPEED,
                                new GranuleParams(4, 1, 1),
                                new GranuleParams(2, 1, 2)),
                        new OverviewConfig(
                                OverviewPolicy.NEAREST,
                                new GranuleParams(3, 1, 1),
                                new GranuleParams(2, 1, 2)),
                        new OverviewConfig(
                                OverviewPolicy.IGNORE,
                                new GranuleParams(0, 9, 9),
                                new GranuleParams(0, 5, 5))
                    });

    private static final Logger LOGGER = Logger.getLogger(OverviewsControllerTest.class.toString());

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(OverviewsControllerTest.class);
    }

    private static final ImageReaderSpi spi = new TIFFImageReaderSpi();

    /**
     * Tests the {@link OverviewsController} with support for different resolutions/different number
     * of overviews.
     *
     * <p>world_a.tif => Pixel Size = (0.833333333333333,-0.833333333333333); 4 overviews
     * world_b.tif => Pixel Size = (1.406250000000000,-1.406250000000000); 2 overviews
     */
    @Test
    public void testHeterogeneousGranules()
            throws IOException, MismatchedDimensionException, FactoryException, TransformException {

        final CoordinateReferenceSystem WGS84 = CRS.decode("EPSG:4326", true);
        final ReferencedEnvelope TEST_BBOX_A = new ReferencedEnvelope(-180, 0, -90, 90, WGS84);
        final ReferencedEnvelope TEST_BBOX_B = new ReferencedEnvelope(0, 180, 0, 90, WGS84);

        URL heterogeneousGranulesURL = TestData.url(this, "heterogeneous");
        // //
        //
        // Initialize mosaic variables
        //
        // //
        final Hints hints = new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, WGS84);
        hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, true);
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(heterogeneousGranulesURL, hints);
        Assert.assertNotNull(format);
        Assert.assertFalse("UknownFormat", format instanceof UnknownFormat);

        final ImageMosaicReader reader =
                (ImageMosaicReader) format.getReader(heterogeneousGranulesURL, hints);
        Assert.assertNotNull(reader);

        final String name = reader.getGridCoverageNames()[0];
        final int nOv = reader.getDatasetLayout().getNumInternalOverviews();
        final double[][] hRes = reader.getResolutionLevels();
        final RasterManager rasterManager = reader.getRasterManager(name);

        // //
        //
        // Initialize granules related variables
        //
        // //
        final File g1File = new File(URLs.urlToFile(heterogeneousGranulesURL), "world_a.tif");
        final File g2File = new File(URLs.urlToFile(heterogeneousGranulesURL), "world_b.tif");
        final ImageReadParam readParamsG1 = new ImageReadParam();
        final ImageReadParam readParamsG2 = new ImageReadParam();
        int imageIndexG1 = 0;
        int imageIndexG2 = 0;

        final GranuleDescriptor granuleDescriptor1 =
                new GranuleDescriptor(
                        g1File.getAbsolutePath(),
                        TEST_BBOX_A,
                        null,
                        spi,
                        null,
                        (MultiLevelROI) null,
                        -1,
                        true,
                        false,
                        hints);
        final GranuleDescriptor granuleDescriptor2 =
                new GranuleDescriptor(
                        g2File.getAbsolutePath(),
                        TEST_BBOX_B,
                        null,
                        spi,
                        null,
                        (MultiLevelROI) null,
                        -1,
                        true,
                        false,
                        hints);
        assertNotNull(granuleDescriptor1.toString());
        assertNotNull(granuleDescriptor2.toString());

        final OverviewsController ovControllerG1 = granuleDescriptor1.overviewsController;
        final OverviewsController ovControllerG2 = granuleDescriptor2.overviewsController;

        // //
        //
        // Initializing read request
        //
        // //
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final GridEnvelope originalRange = reader.getOriginalGridRange();
        final Rectangle rasterArea =
                new Rectangle(
                        0,
                        0,
                        (int) Math.ceil(originalRange.getSpan(0) / 9.0),
                        (int) Math.ceil(originalRange.getSpan(1) / 9.0));
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper(range, envelope);
        geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
        final AffineTransform gridToWorld = geMapper.createAffineTransform();
        final double requestedResolution[] =
                new double[] {
                    XAffineTransform.getScaleX0(gridToWorld),
                    XAffineTransform.getScaleY0(gridToWorld)
                };

        TestSet at = null;
        if (nOv == 4 && Math.abs(hRes[0][0] - 0.833333333333) <= THRESHOLD) {
            at = at1;
        } else if (nOv == 2 && Math.abs(hRes[0][1] - 1.40625) <= THRESHOLD) {
            at = at2;
        } else {
            return;
        }

        // //
        //
        // Starting OverviewsController tests
        //
        // //
        final OverviewPolicy[] ovPolicies =
                new OverviewPolicy[] {
                    OverviewPolicy.QUALITY,
                    OverviewPolicy.SPEED,
                    OverviewPolicy.NEAREST,
                    OverviewPolicy.IGNORE
                };
        for (int i = 0; i < ovPolicies.length; i++) {
            OverviewPolicy ovPolicy = ovPolicies[i];
            LOGGER.info("Testing with OverviewPolicy = " + ovPolicy.toString());
            imageIndexG1 =
                    ReadParamsController.setReadParams(
                            requestedResolution,
                            ovPolicy,
                            DecimationPolicy.ALLOW,
                            readParamsG1,
                            rasterManager,
                            ovControllerG1,
                            null);
            imageIndexG2 =
                    ReadParamsController.setReadParams(
                            requestedResolution,
                            ovPolicy,
                            DecimationPolicy.ALLOW,
                            readParamsG2,
                            rasterManager,
                            ovControllerG2,
                            null);
            assertSame(at.ot[i].g1.imageIndex, imageIndexG1);
            assertSame(at.ot[i].g2.imageIndex, imageIndexG2);
            assertSame(at.ot[i].g1.ssx, readParamsG1.getSourceXSubsampling());
            assertSame(at.ot[i].g1.ssy, readParamsG1.getSourceYSubsampling());
            assertSame(at.ot[i].g2.ssx, readParamsG2.getSourceXSubsampling());
            assertSame(at.ot[i].g2.ssy, readParamsG2.getSourceYSubsampling());
        }
        reader.dispose();
    }

    /** @param args */
    public static void main(String[] args) {
        TestRunner.run(OverviewsControllerTest.suite());
    }

    @Before
    public void setUp() throws Exception {
        // remove generated file
        cleanUp();
    }

    /** Cleaning up the generated files (shape and properties so that we recreate them). */
    private void cleanUp() throws FileNotFoundException, IOException {
        File dir = TestData.file(this, "heterogeneous/");
        File[] files =
                dir.listFiles(
                        (FilenameFilter)
                                FileFilterUtils.notFileFilter(
                                        FileFilterUtils.or(
                                                FileFilterUtils.or(
                                                        FileFilterUtils.suffixFileFilter("tif"),
                                                        FileFilterUtils.suffixFileFilter("aux")),
                                                FileFilterUtils.nameFileFilter(
                                                        "datastore.properties"))));
        for (File file : files) {
            file.delete();
        }
    }

    @After
    public void tearDown() throws FileNotFoundException, IOException {
        cleanUp();
    }
}
