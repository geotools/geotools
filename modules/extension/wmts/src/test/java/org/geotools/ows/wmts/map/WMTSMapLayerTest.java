/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.ows.wmts.map;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.ows.wmts.WMTSTestUtils;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;

public class WMTSMapLayerTest {

    @Test
    public void testConstructorSourceCRS() throws Exception {
        URL url = TestData.file(null, "geodata.nationaalgeoregister.nl.xml")
                .toURI()
                .toURL();
        WebMapTileServer server = new WebMapTileServer(url);
        WMTSCapabilities caps = WMTSTestUtils.createCapabilities("geodata.nationaalgeoregister.nl.xml");
        WMTSLayer layer = caps.getLayerList().get(0);
        WMTSMapLayer mapLayer = new WMTSMapLayer(server, layer, CRS.decode("EPSG:3857"));
        SimpleFeatureCollection fc = mapLayer.toFeatureCollection();
        SimpleFeature f = fc.features().next();
        AttributeExpressionImpl xpath = new AttributeExpressionImpl("params");
        GeneralParameterValue[] params = xpath.evaluate(f, GeneralParameterValue[].class);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) ((ParameterValue) params[0]).getValue();
        assertEquals(CRS.decode("EPSG:3857"), crs);
    }

    @Test
    public void testNativelySupported() throws Exception {
        WMTSCapabilities capa = WMTSTestUtils.createCapabilities("getcapa_kvp.xml");
        URL url = new URL("http://localhost:8080/geoserver/gwc/service/wmts?");
        WebMapTileServer server = new WebMapTileServer(url, capa);
        WMTSMapLayer mapLayer = new WMTSMapLayer(server, capa.getLayer("tasmania"));
        boolean wgs84supported = mapLayer.isNativelySupported(DefaultGeographicCRS.WGS84);
        Assert.assertTrue(wgs84supported);
    }
}
