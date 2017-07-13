/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle;


import org.geotools.mbstyle.layer.RasterMBLayer;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyledLayerDescriptor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.style.FeatureTypeStyle;
import org.opengis.style.RasterSymbolizer;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class RasterMBLayerTest {

    @Test
    public void testRasterLayer() throws IOException, ParseException {
        JSONObject json = MapboxTestUtils.parseTestStyle("rasterStyleTest.json");
        MBStyle rasterStyle = MBStyle.create(json);
        RasterMBLayer rasterLayer = (RasterMBLayer) rasterStyle.layer("testid");
        List<org.geotools.styling.FeatureTypeStyle> featureTypeRaster = rasterLayer.transformInternal(rasterStyle);

        assertEquals(0.59, rasterLayer.getOpacity());
        assertEquals(30L, rasterLayer.getHueRotate());
        assertEquals(0.9, rasterLayer.getSaturation());
        assertEquals(0.94d, rasterLayer.getContrast());
        assertEquals(250L, rasterLayer.getFadeDuration());
        assertEquals(0.28, rasterLayer.getBrightnessMin());
        assertEquals(0.69, rasterLayer.getBrightnessMax());

    }
}
