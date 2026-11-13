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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyledLayerDescriptor;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class MapBoxStyleTest {

    /** Tolerance StreamingRenderer applies when checking a scale against a rule scale range */
    private static final double RENDERER_SCALE_TOLERANCE = 1e-6;

    @Test
    public void validate() throws IOException, ParseException {
        try (Reader reader = MapboxTestUtils.readerTestStyle("fillStyleTest.json")) {
            List<Exception> problems = MapBoxStyle.validate(reader);

            assertTrue("fillStyleTest validate", problems.isEmpty());
        }

        try (Reader reader = MapboxTestUtils.readerTestStyle("styleInvalidName.json")) {
            List<Exception> problems = MapBoxStyle.validate(reader);

            assertEquals("styleInvalidName 1 failure", 1, problems.size());
        }

        try (Reader reader = MapboxTestUtils.readerTestStyle("styleInvalidLayers.json")) {
            List<Exception> problems = MapBoxStyle.validate(reader);

            assertEquals("styleInvalidLayers 2 failure", 2, problems.size());
        }
    }

    /** A layer is drawn at its minzoom and hidden at its maxzoom, at the exact WMTS GoogleMapsCompatible scales. */
    @Test
    public void testLayerZoomRangeExactScales() throws ParseException {
        assertLayerZoomRange22To24(MapboxTestUtils.WEB_MERCATOR_QUAD_ROOT_SCALE);
    }

    /** Same as above, at the GeoWebCache EPSG:900913 scales, computed from rounded bounds. */
    @Test
    public void testLayerZoomRangeGeoWebCacheScales() throws ParseException {
        assertLayerZoomRange22To24(MapboxTestUtils.GEOWEBCACHE_900913_ROOT_SCALE);
    }

    /**
     * Checks that a layer with minzoom 22 and maxzoom 24 is drawn at zoom 22 and hidden at zoom 24, for requests
     * computed from the given scale of the 256px root tile. High zooms have the smallest scales, so the boundaries are
     * checked against the scale tolerance of the renderer where it matters most.
     */
    private void assertLayerZoomRange22To24(double rootTileScale256) throws ParseException {
        String layer = "{'id': 'water', 'type': 'fill', 'source': 'src', 'minzoom': 22, 'maxzoom': 24}";
        MBStyle style = MBStyle.create(MapboxTestUtils.object(MapboxTestUtils.style(layer)));
        Style sld = ((NamedLayer) style.transform().getStyledLayers()[0]).getStyles()[0];
        Rule rule = sld.featureTypeStyles().get(0).rules().get(0);
        double zoom22Scale = MapboxTestUtils.zoomScale(rootTileScale256, 22);
        double zoom24Scale = MapboxTestUtils.zoomScale(rootTileScale256, 24);
        assertThat(zoom22Scale, lessThan(rule.getMaxScaleDenominator() + RENDERER_SCALE_TOLERANCE));
        assertThat(zoom24Scale, lessThan(rule.getMinScaleDenominator() - RENDERER_SCALE_TOLERANCE));
    }

    @Test
    public void testNamedLayers() throws IOException, ParseException {
        try (Reader reader = MapboxTestUtils.readerTestStyle("groupStyleTest.json")) {
            StyledLayerDescriptor sld = MapBoxStyle.parse(reader);

            // background is incorporated in the first style
            assertEquals(3, sld.getStyledLayers().length);

            assertEquals("Lakes", sld.getStyledLayers()[0].getName());
            assertTrue(sld.getStyledLayers()[1] instanceof NamedLayer);

            assertEquals("BasicPolygons", sld.getStyledLayers()[1].getName());
            assertEquals("NamedPlaces", sld.getStyledLayers()[2].getName());
        }
    }

    @Test
    public void testMergeNamedLayers() throws IOException, ParseException {
        try (Reader reader = MapboxTestUtils.readerTestStyle("mergeLayerStyleTest.json")) {
            StyledLayerDescriptor sld = MapBoxStyle.parse(reader);

            assertEquals(3, sld.getStyledLayers().length);

            assertEquals("Lakes", sld.getStyledLayers()[0].getName());
            assertTrue(sld.getStyledLayers()[0] instanceof NamedLayer);
            assertEquals(1, ((NamedLayer) sld.getStyledLayers()[0]).getStyles().length);
            assertEquals(
                    2,
                    ((NamedLayer) sld.getStyledLayers()[0])
                            .getStyles()[0]
                            .featureTypeStyles()
                            .size());

            assertEquals("NamedPlaces", sld.getStyledLayers()[1].getName());
            assertTrue(sld.getStyledLayers()[1] instanceof NamedLayer);
            assertEquals(1, ((NamedLayer) sld.getStyledLayers()[1]).getStyles().length);
            assertEquals(
                    1,
                    ((NamedLayer) sld.getStyledLayers()[1])
                            .getStyles()[0]
                            .featureTypeStyles()
                            .size());

            assertEquals("Lakes", sld.getStyledLayers()[2].getName());
            assertTrue(sld.getStyledLayers()[2] instanceof NamedLayer);
            assertEquals(1, ((NamedLayer) sld.getStyledLayers()[2]).getStyles().length);
            assertEquals(
                    1,
                    ((NamedLayer) sld.getStyledLayers()[2])
                            .getStyles()[0]
                            .featureTypeStyles()
                            .size());
        }
    }

    /** A style with a visible background layer validates without problems. */
    @Test
    public void testValidateBackground() throws Exception {
        String json = MapboxTestUtils.style(
                """
                {
                  "id": "bg",
                  "type": "background",
                  "paint": { "background-color": "#cccccc" }
                }""",
                """
                {
                  "id": "f",
                  "type": "fill",
                  "source": "src",
                  "paint": { "fill-color": "#ff0000" }
                }""");
        List<Exception> problems = MapBoxStyle.validate(new StringReader(json));
        assertEquals(List.of(), problems);
    }
}
