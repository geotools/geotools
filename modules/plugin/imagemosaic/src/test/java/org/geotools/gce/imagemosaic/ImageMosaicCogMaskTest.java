/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.gce.imagemosaic;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMParser;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.eclipse.imagen.ROI;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.HarvestedSource;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.test.TestData;
import org.geotools.test.wiremock.FileRangeResponseTransformer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ImageMosaicCogMaskTest {

    private static final double DELTA = 1E-4;
    private static WireMockServer wireMockServer;

    @BeforeClass
    public static void setUpServer() {
        // Initialize WireMock server with range read support
        WireMockConfiguration configuration =
                wireMockConfig().dynamicPort().extensions(new FileRangeResponseTransformer());
        wireMockServer = new WireMockServer(configuration);

        wireMockServer.start();

        // Register a stub mapping programmatically
        File file = new File("./src/test/resources/org/geotools/gce/imagemosaic/test-data/masked2cog.tif");
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/masked2cog.tif"))
                .willReturn(WireMock.aResponse()
                        .withTransformer("file-range-response-transformer", "filePath", file)
                        .withStatus(200)));
    }

    @AfterClass
    public static void tearDownServer() {
        if (wireMockServer != null) wireMockServer.stop();
    }

    @Test
    public void testMaskedCog() throws Exception {
        // TestUtils.logAll();

        URL url = TestData.url(this, "masked2cog");
        ImageMosaicReader reader = TestUtils.getReader(url);
        GridCoverage2D coverage = null;
        try {
            assertNotNull(reader);

            // harvest the cog
            URL cogSource = new URL("http://localhost:" + wireMockServer.port() + "/masked2cog.tif");
            List<HarvestedSource> harvest = reader.harvest(null, cogSource, null);
            assertEquals(1, harvest.size());
            HarvestedSource source = harvest.get(0);
            assertEquals(cogSource, source.getSource());

            //            reader.dispose();
            //            reader = TestUtils.getReader(url);

            // activate footprint management
            GeneralParameterValue[] params = new GeneralParameterValue[3];
            ParameterValue<String> footprintManagement = AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
            footprintManagement.setValue(FootprintBehavior.Transparent.name());
            params[0] = footprintManagement;

            // this prevents us from having problems with link to files still open.
            ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_IMAGEN_IMAGEREAD.createValue();
            jaiImageRead.setValue(false);
            params[1] = jaiImageRead;

            // read the cog
            coverage = reader.read(params);
            assertNotNull(coverage);
            RenderedImage image = coverage.getRenderedImage();
            assertEquals(1, image.getNumXTiles());
            assertEquals(1, image.getNumYTiles());

            // used to return just the mask, a single band image (immediate read, has just the actual pixels)
            Raster raster = image.getTile(0, 0);
            assertEquals(200, raster.getWidth());
            assertEquals(200, raster.getHeight());
            assertEquals(4, raster.getNumBands()); // footprint management adds alpha

            // the mask is there
            Object property = image.getProperty("ROI");
            assertThat(property, instanceOf(ROI.class));
            ROI roi = (ROI) property;

            // check pixels outside the mask
            int[] pixel = new int[4];
            raster.getPixel(10, 10, pixel);
            assertArrayEquals(new int[] {0, 0, 0, 0}, pixel);
            assertFalse(roi.contains(10, 10));

            // check pixels inside the mask (the image is just 190x190)
            raster.getPixel(190, 190, pixel);
            assertArrayEquals(new int[] {255, 255, 68, 255}, pixel);
            assertTrue(roi.contains(190, 190));

            Object pamDataset = coverage.getProperty(GridCoverage2DReader.PAM_DATASET);
            PAMDataset pam = (PAMDataset) pamDataset;
            assertNotNull(pam);
            PAMParser parser = PAMParser.getInstance();
            PAMDataset.PAMRasterBand b0 = pam.getPAMRasterBand().get(0);
            assertEquals(255, Double.parseDouble(parser.getMetadataValue(b0, "STATISTICS_MAXIMUM")), DELTA);
            assertEquals(132.20475, Double.parseDouble(parser.getMetadataValue(b0, "STATISTICS_MEAN")), DELTA);
            assertEquals(0, Double.parseDouble(parser.getMetadataValue(b0, "STATISTICS_MINIMUM")), DELTA);
            assertEquals(127.4131677, Double.parseDouble(parser.getMetadataValue(b0, "STATISTICS_STDDEV")), DELTA);
            PAMDataset.PAMRasterBand b2 = pam.getPAMRasterBand().get(2);
            assertEquals(68, Double.parseDouble(parser.getMetadataValue(b2, "STATISTICS_MAXIMUM")), DELTA);
            assertEquals(35.2546, Double.parseDouble(parser.getMetadataValue(b2, "STATISTICS_MEAN")), DELTA);
            assertEquals(0, Double.parseDouble(parser.getMetadataValue(b2, "STATISTICS_MINIMUM")), DELTA);
            assertEquals(33.976844, Double.parseDouble(parser.getMetadataValue(b2, "STATISTICS_STDDEV")), DELTA);

        } finally {
            if (reader != null) reader.dispose();
            if (coverage != null) coverage.dispose(true);
        }
    }
}
