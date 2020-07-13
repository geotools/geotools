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

package org.geotools.tpk;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;

public class TPKReaderTest {

    @Test
    public void testZoomlevel_0() throws IOException {
        TPKReader reader = new TPKReader(getClass().getResource("sample_v2.tpkx"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(200, 200)),
                        new ReferencedEnvelope(-180, 180.0, -85.0, 85.0, TPKReader.WGS_84));
        parameters[0] = new Parameter<>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read(parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(-20037508.34, gc.getEnvelope().getMinimum(0), 0.01);
        assertEquals(-20037508.34, gc.getEnvelope().getMinimum(1), 0.01);
        assertEquals(20037508.34, gc.getEnvelope().getMaximum(0), 0.01);
        assertEquals(20037508.34, gc.getEnvelope().getMaximum(1), 0.01);
        assertEquals(256, img.getWidth());
        assertEquals(256, img.getHeight());
    }

    @Test
    public void testZoomlevel_1() throws IOException {
        TPKReader reader = new TPKReader(getClass().getResource("sample_v2.tpkx"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(500, 500)),
                        new ReferencedEnvelope(-180, 180.0, -85.0, 85.0, TPKReader.WGS_84));
        parameters[0] = new Parameter<>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read(parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(-20037508.34, gc.getEnvelope().getMinimum(0), 0.01);
        assertEquals(-20037508.34, gc.getEnvelope().getMinimum(1), 0.01);
        assertEquals(20037508.34, gc.getEnvelope().getMaximum(0), 0.01);
        assertEquals(20037508.34, gc.getEnvelope().getMaximum(1), 0.01);
        assertEquals(512, img.getWidth());
        assertEquals(512, img.getHeight());
    }

    @Test
    public void testZoomlevel_2() throws IOException {
        TPKReader reader = new TPKReader(getClass().getResource("sample_v2.tpkx"), null);

        GeneralParameterValue[] parameters = new GeneralParameterValue[1];
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(500, 500)),
                        new ReferencedEnvelope(0, 45, 0, 45, TPKReader.WGS_84));
        parameters[0] = new Parameter<>(AbstractGridFormat.READ_GRIDGEOMETRY2D, gg);
        GridCoverage2D gc = reader.read(parameters);
        RenderedImage img = gc.getRenderedImage();
        assertEquals(0, gc.getEnvelope().getMinimum(0), 0.01);
        assertEquals(0, gc.getEnvelope().getMinimum(1), 0.01);
        assertEquals(20037508.34 / 2, gc.getEnvelope().getMaximum(0), 0.01);
        assertEquals(20037508.34 / 2, gc.getEnvelope().getMaximum(1), 0.01);
        assertEquals(256, img.getWidth());
        assertEquals(256, img.getHeight());
    }
}
