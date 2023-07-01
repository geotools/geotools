/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import javax.xml.namespace.QName;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.Binding;
import org.junit.Test;

public class GeopkgtypeBindingTest extends GPKGTestSupport {
    @Test
    public void testType() {
        assertEquals(GeoPackageProcessRequest.class, binding(GPKG.geopkgtype).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GPKG.geopkgtype).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        buildDocument(
                "<geopackage name='mygeopackage' path='file://test' remove='true'>"
                        + "<features name=\"features1\" identifier=\"f1\">"
                        + "<description>features1 description</description>"
                        + "<featuretype>featuretypename</featuretype>"
                        + "<propertynames>property1,property2</propertynames>"
                        + "<filter xmlns:fes=\"http://www.opengis.net/fes/2.0\">"
                        + "<fes:PropertyIsEqualTo>"
                        + "<fes:ValueReference>propertyx</fes:ValueReference>"
                        + "<fes:Literal>999</fes:Literal>"
                        + "</fes:PropertyIsEqualTo>"
                        + "</filter>"
                        + "</features>"
                        + "<tiles name=\"tiles1\" identifier=\"t1\">"
                        + "<description>tiles1 description</description>"
                        + "<srs>EPSG:4326</srs>"
                        + "<bbox>"
                        + "  <minx>0</minx>"
                        + "  <maxx>100</maxx>"
                        + "  <miny>10</miny>"
                        + "  <maxy>50</maxy>"
                        + "</bbox>"
                        + "<layers>layer1, layer2</layers>"
                        + "<styles>style1, style2</styles>"
                        + "<format>png</format>"
                        + "<bgcolor>ffff00</bgcolor>"
                        + "<transparent>true</transparent>"
                        + "<coverage>"
                        + "  <minZoom>1</minZoom>"
                        + "  <maxZoom>10</maxZoom>"
                        + "</coverage>"
                        + "<gridset>"
                        + "  <name>gridsetname</name>"
                        + "</gridset>"
                        + "</tiles>"
                        + "</geopackage>");
        Object result = parse(GPKG.geopkgtype);
        assertTrue(result instanceof GeoPackageProcessRequest);
        GeoPackageProcessRequest request = (GeoPackageProcessRequest) result;

        assertNotNull(request.getRemove());
        assertTrue(request.getRemove());
        assertTrue(request.getPath().toString().equalsIgnoreCase("file://test"));
        assertEquals(2, request.getLayerCount());

        assertTrue(request.getLayer(0) instanceof GeoPackageProcessRequest.FeaturesLayer);
        GeoPackageProcessRequest.FeaturesLayer features =
                (GeoPackageProcessRequest.FeaturesLayer) request.getLayer(0);
        assertEquals("f1", features.getIdentifier());
        assertEquals("features1", features.getName());
        assertEquals("features1 description", features.getDescription());
        assertEquals("featuretypename", features.getFeatureType().getLocalPart());
        assertEquals(2, features.getPropertyNames().size());
        assertTrue(
                features.getPropertyNames()
                        .contains(new QName("http://www.opengis.net/gpkg", "property1", "")));
        assertTrue(
                features.getPropertyNames()
                        .contains(new QName("http://www.opengis.net/gpkg", "property2", "")));
        assertTrue(features.getFilter() instanceof PropertyIsEqualTo);
        PropertyIsEqualTo filter = (PropertyIsEqualTo) features.getFilter();
        assertTrue(filter.getExpression1() instanceof PropertyName);
        assertTrue(filter.getExpression2() instanceof Literal);
        assertEquals("propertyx", ((PropertyName) filter.getExpression1()).getPropertyName());
        assertEquals("999", ((Literal) filter.getExpression2()).getValue());

        assertTrue(request.getLayer(0) instanceof GeoPackageProcessRequest.FeaturesLayer);
        GeoPackageProcessRequest.TilesLayer tiles =
                (GeoPackageProcessRequest.TilesLayer) request.getLayer(1);
        assertEquals("t1", tiles.getIdentifier());
        assertEquals("tiles1", tiles.getName());
        assertEquals("tiles1 description", tiles.getDescription());
        assertEquals("EPSG:4326", tiles.getSrs().toString());
        assertEquals("png", tiles.getFormat());
        assertEquals(Color.YELLOW, tiles.getBgColor());
        assertTrue(tiles.isTransparent());
        assertEquals("gridsetname", tiles.getGridSetName());
        assertEquals(2, tiles.getLayers().size());
        assertTrue(tiles.getLayers().contains(new QName("http://www.opengis.net/gpkg", "layer1")));
        assertTrue(tiles.getLayers().contains(new QName("http://www.opengis.net/gpkg", "layer2")));
        assertEquals(2, tiles.getStyles().size());
        assertTrue(tiles.getStyles().contains("style1"));
        assertTrue(tiles.getStyles().contains("style2"));
    }
}
