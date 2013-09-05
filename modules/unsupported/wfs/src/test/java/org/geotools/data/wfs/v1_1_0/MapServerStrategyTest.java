/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_1_0;

import static org.geotools.data.wfs.protocol.http.HttpUtil.requestKvp;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.CUBEWERX_GOVUNITCE;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.GEOS_ARCHSITES;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.MAPSRV_GOVUNITCE;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.createTestProtocol;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.wfs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.ResultTypeType;

import org.geotools.data.Query;
import org.geotools.data.wfs.protocol.wfs.GetFeature;
import org.geotools.data.wfs.protocol.wfs.WFSExtensions;
import org.geotools.data.wfs.protocol.wfs.WFSResponse;
import org.geotools.data.wfs.protocol.wfs.GetFeature.ResultType;
import org.geotools.data.wfs.v1_1_0.DataTestSupport.TestHttpProtocol;
import org.geotools.data.wfs.v1_1_0.DataTestSupport.TestHttpResponse;
import org.geotools.data.wfs.v1_1_0.WFSStrategy.RequestComponents;
import org.geotools.test.TestData;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author "Mauro Bartolomeoli - mauro.bartolomeoli@geo-solutions.it"
 *
 */
public class MapServerStrategyTest {
    private static MapServerStrategy strategy;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        strategy = new MapServerStrategy();
        createTestProtocol(MAPSRV_GOVUNITCE.CAPABILITIES, strategy);
    }
    
    @Test
    public void testNamespaceURIMappings() throws IOException {
        assertTrue(strategy.getNamespaceURIMappings().size() > 0);
        assertTrue(strategy.getNamespaceURIMappings().containsKey("http://www.opengis.net/wfs"));
    }
    
    @Test
    public void testPrefixedTypeName() {
        assertEquals("testFeature", strategy.getPrefixedTypeName(new QName("", "testFeature", "")));
        assertEquals("testFeature", strategy.getPrefixedTypeName(new QName("http://www.opengis.net/wfs", "testFeature", "wfs")));
        assertEquals("test:testFeature", strategy.getPrefixedTypeName(new QName("http://www.opengis.net/test", "testFeature", "test")));
        
        assertNotNull(DataTestSupport.wfs.getFeatureTypeName(MAPSRV_GOVUNITCE.FEATURETYPENAME));
    }
    
    @Test
    public void testFieldTypeMappings() {
        assertTrue(strategy.getFieldTypeMappings().containsKey(new QName("http://www.w3.org/2001/XMLSchema", "Character")));
        assertTrue(strategy.getFieldTypeMappings().containsKey(new QName("http://www.w3.org/2001/XMLSchema", "Integer")));
        assertTrue(strategy.getFieldTypeMappings().containsKey(new QName("http://www.w3.org/2001/XMLSchema", "Real")));
    }
    
}
