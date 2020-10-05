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

import it.geosolutions.imageio.plugins.cog.CogImageReadParam;
import it.geosolutions.imageioimpl.plugins.cog.*;
import java.awt.*;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URISyntaxException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.*;
import org.junit.Assert;
import org.junit.Test;

/** Testing {@link GeoTiffReader} with COG data */
public class GeoTiffReaderCogOnlineTest extends Assert {

    @Test
    public void testCogRead() throws URISyntaxException, IOException {
        String url =
                "https://s3-us-west-2.amazonaws.com/landsat-pds/c1/L8/153/075/LC08_L1TP_153075_20190515_20190515_01_RT/LC08_L1TP_153075_20190515_20190515_01_RT_B2.TIF";
        CogUri cogUri = new CogUri(url, false);
        HttpRangeReader rangeReader =
                new HttpRangeReader(cogUri.getUri(), CogImageReadParam.DEFAULT_HEADER_LENGTH);
        CogSourceSPIProvider input =
                new CogSourceSPIProvider(
                        cogUri,
                        new CogImageReaderSpi(),
                        new CogImageInputStreamSpi(),
                        rangeReader.getClass().getName());
        GeoTiffReader reader = new GeoTiffReader(input);
        GridCoverage2D coverage = reader.read(null);
        assertNotNull(coverage);
        RenderedImage image = coverage.getRenderedImage();
        int numTileX = image.getNumXTiles();
        int numTileY = image.getNumYTiles();

        Raster raster = image.getTile(numTileX / 2, numTileY / 2);
        assertEquals(512, raster.getWidth());
        assertEquals(512, raster.getHeight());
        assertEquals(1, raster.getNumBands());
    }
}
