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
 */
package org.geotools.mbtiles;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class VectorLayersMetadataTest {

    @Test
    public void testParseMetadata() throws Exception {
        String json =
                IOUtils.toString(
                        getClass().getResourceAsStream("vectorLayers.json"),
                        StandardCharsets.UTF_8);
        VectorLayersMetadata layersMetadata = VectorLayersMetadata.parseMetadata(json);
        List<VectorLayerMetadata> layers = layersMetadata.getLayers();
        assertEquals(15, layers.size());
        VectorLayerMetadata water = layers.get(1);
        assertEquals("waterway", water.getId());
        assertEquals(Integer.valueOf(0), water.getMinZoom());
        assertEquals(Integer.valueOf(14), water.getMaxZoom());
        assertEquals("String", water.getFields().get("class"));

        VectorLayerMetadata mountainPeak = layers.get(4);
        assertEquals("mountain_peak", mountainPeak.getId());
        assertEquals(Integer.valueOf(0), mountainPeak.getMinZoom());
        assertEquals(Integer.valueOf(14), mountainPeak.getMaxZoom());
        assertEquals("Number", mountainPeak.getFields().get("rank"));
    }
}
