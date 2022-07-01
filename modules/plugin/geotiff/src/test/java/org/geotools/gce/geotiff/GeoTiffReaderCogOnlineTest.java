/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.core.BasicAuthURI;
import it.geosolutions.imageio.plugins.cog.CogImageReadParam;
import it.geosolutions.imageioimpl.plugins.cog.CogImageInputStreamSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogImageReader;
import it.geosolutions.imageioimpl.plugins.cog.CogImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogSourceSPIProvider;
import it.geosolutions.imageioimpl.plugins.cog.DefaultCogImageInputStream;
import it.geosolutions.imageioimpl.plugins.cog.HttpRangeReader;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/** Testing {@link GeoTiffReader} with COG data */
public class GeoTiffReaderCogOnlineTest extends Assert {

    private CogSourceSPIProvider getInputProvider() {
        String url =
                "https://s3-us-west-2.amazonaws.com/sentinel-cogs/sentinel-s2-l2a-cogs/5/C/MK/2018/10/S2B_5CMK_20181020_0_L2A/B01.tif";
        BasicAuthURI cogUri = new BasicAuthURI(url, false);
        HttpRangeReader rangeReader =
                new HttpRangeReader(cogUri.getUri(), CogImageReadParam.DEFAULT_HEADER_LENGTH);
        CogSourceSPIProvider input =
                new CogSourceSPIProvider(
                        cogUri,
                        new CogImageReaderSpi(),
                        new CogImageInputStreamSpi(),
                        rangeReader.getClass().getName());
        return input;
    }

    @Test
    public void testCogRead() throws URISyntaxException, IOException {
        GeoTiffReader reader = new GeoTiffReader(getInputProvider());
        assertEquals("B01", reader.getGridCoverageNames()[0]);
        GridCoverage2D coverage = reader.read(null);
        assertNotNull(coverage);
        RenderedImage image = coverage.getRenderedImage();
        int numTileX = image.getNumXTiles();
        int numTileY = image.getNumYTiles();

        Raster raster = image.getTile(numTileX / 2, numTileY / 2);
        assertEquals(256, raster.getWidth());
        assertEquals(256, raster.getHeight());
        assertEquals(1, raster.getNumBands());
    }

    @Test
    public void testCogReadBandSelect() throws URISyntaxException, IOException {
        GeoTiffReader reader = new GeoTiffReader(getInputProvider());

        ParameterValue<int[]> bands = AbstractGridFormat.BANDS.createValue();
        bands.setValue(new int[] {0, 0});
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {bands});
        assertNotNull(coverage);
        assertEquals(2, coverage.getRenderedImage().getSampleModel().getNumBands());
        assertEquals(2, coverage.getRenderedImage().getColorModel().getNumComponents());
    }

    // the old cog file used to have a landsat-pds scene with a sidecar .ovr file that is now gone
    @Test
    @Ignore
    public void testCogOverview() throws URISyntaxException, IOException {
        GeoTiffReader reader = new GeoTiffReader(getInputProvider());
        assertEquals("B01", reader.getGridCoverageNames()[0]);

        GeneralParameterValue[] params = new GeneralParameterValue[1];
        // Define a GridGeometry in order to reduce the output
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 24,
                reader.getOriginalGridRange().getSpan(1) / 24);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));
        params[0] = gg;

        GridCoverage2D coverage = reader.read(params);
        assertNotNull(coverage);
        RenderedImage image = coverage.getRenderedImage();
        int numTileX = image.getNumXTiles();
        int numTileY = image.getNumYTiles();

        CogImageReader imageReader = (CogImageReader) image.getProperty("JAI.ImageReader");
        Object input = imageReader.getInput();
        assertTrue(input instanceof DefaultCogImageInputStream);
        @SuppressWarnings("PMD.CloseResource") // closed elsewhere
        DefaultCogImageInputStream inputStream = (DefaultCogImageInputStream) input;
        String url = inputStream.getUrl();

        // The reader is using the .ovr file as input, it's using the overviews
        assertTrue(url.endsWith(".ovr"));

        // Reading the whole bbox resulted in only 1 tile:
        // It's using the overview
        assertEquals(1, numTileX);
        assertEquals(1, numTileY);

        Raster raster = image.getTile(numTileX / 2, numTileY / 2);
        assertEquals(1, raster.getNumBands());
    }
}
