/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.gce.geotiff;

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
import it.geosolutions.imageio.core.BasicAuthURI;
import it.geosolutions.imageio.plugins.cog.CogImageReadParam;
import it.geosolutions.imageioimpl.plugins.cog.CogImageInputStreamSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogSourceSPIProvider;
import it.geosolutions.imageioimpl.plugins.cog.HttpRangeReader;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.imagen.ROI;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.test.wiremock.FileRangeResponseTransformer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeoTIFFReaderMaskCOGTest {

    private static WireMockServer wireMockServer;

    @BeforeClass
    public static void setUpServer() {
        // Initialize WireMock server with range read support
        WireMockConfiguration configuration =
                wireMockConfig().dynamicPort().extensions(new FileRangeResponseTransformer());
        wireMockServer = new WireMockServer(configuration);

        wireMockServer.start();

        // Register a stub mapping programmatically
        File file = new File("./src/test/resources/org/geotools/gce/geotiff/test-data/mask/masked2cog.tif");
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/masked2cog.tif"))
                .willReturn(WireMock.aResponse()
                        .withTransformer("file-range-response-transformer", "filePath", file)
                        .withStatus(200)));
    }

    @AfterClass
    public static void tearDownServer() {
        if (wireMockServer != null) wireMockServer.stop();
    }

    private CogSourceSPIProvider getInputProvider() {
        String url = "http://localhost:" + wireMockServer.port() + "/masked2cog.tif";
        BasicAuthURI cogUri = new BasicAuthURI(url, false);
        HttpRangeReader rangeReader = new HttpRangeReader(cogUri.getUri(), CogImageReadParam.DEFAULT_HEADER_LENGTH);
        CogSourceSPIProvider input = new CogSourceSPIProvider(
                cogUri,
                new CogImageReaderSpi(),
                new CogImageInputStreamSpi(),
                rangeReader.getClass().getName());
        return input;
    }

    @Test
    public void testCogRead() throws URISyntaxException, IOException {
        GeoTiffReader reader = new GeoTiffReader(getInputProvider());
        assertEquals("masked2cog", reader.getGridCoverageNames()[0]);
        GridCoverage2D coverage = reader.read();
        assertNotNull(coverage);
        RenderedImage image = coverage.getRenderedImage();
        assertEquals(1, image.getNumXTiles());
        assertEquals(1, image.getNumYTiles());

        // used to return just the mask, a single band image
        Raster raster = image.getTile(0, 0);
        assertEquals(512, raster.getWidth());
        assertEquals(512, raster.getHeight());
        assertEquals(3, raster.getNumBands());

        // the mask is there
        Object property = image.getProperty("ROI");
        assertThat(property, instanceOf(ROI.class));
        ROI roi = (ROI) property;

        // check pixels outside the mask
        int[] pixel = new int[3];
        raster.getPixel(10, 10, pixel);
        assertArrayEquals(new int[] {0, 0, 0}, pixel);
        assertFalse(roi.contains(10, 10));

        // check pixels inside the mask (the image is just 190x190)
        raster.getPixel(190, 190, pixel);
        assertArrayEquals(new int[] {255, 255, 68}, pixel);
        assertTrue(roi.contains(190, 190));
    }
}
