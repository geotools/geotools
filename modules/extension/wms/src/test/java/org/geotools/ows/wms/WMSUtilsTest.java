/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

public class WMSUtilsTest {

    @Test
    public void testFindCommonEPSGs() {
        List<Layer> layers = new ArrayList<>();

        Layer layer1 = new Layer();
        Set<String> set1 = new TreeSet<>();
        set1.add("EPSG:4326");
        set1.add("EPSG:3005");
        set1.add("EPSG:42101");
        layer1.setSrs(set1);

        Layer layer2 = new Layer();
        Set<String> set2 = new TreeSet<>();
        set2.add("EPSG:3005");
        set2.add("EPSG:42101");
        layer2.setSrs(set2);

        Layer layer3 = new Layer();
        Set<String> set3 = new TreeSet<>();
        set3.add("EPSG:42111");
        layer3.setSrs(set3);

        layers.add(layer1);
        layers.add(layer2);

        Set<String> results1 = WMSUtils.findCommonEPSGs(layers);

        Assert.assertNotNull(results1);
        Assert.assertEquals(2, results1.size());
        Assert.assertTrue(results1.contains("EPSG:3005"));
        Assert.assertTrue(results1.contains("EPSG:42101"));
        Assert.assertFalse(results1.contains("EPSG:4326"));

        layers.clear();

        layers.add(layer1);
        layers.add(layer3);

        Set<String> results2 = WMSUtils.findCommonEPSGs(layers);

        Assert.assertNotNull(results2);
        Assert.assertEquals(0, results2.size());
        Assert.assertTrue(results2.isEmpty());
    }

    @Test
    public void testMatchEPSG() throws Exception {
        CoordinateReferenceSystem crs4326 = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem crs3005 = CRS.decode("EPSG:3005");
        CoordinateReferenceSystem crs3347 = CRS.decode("EPSG:3347");

        Set<String> codes = new TreeSet<>();
        codes.add("EPSG:4326");
        codes.add("EPSG:42102");
        codes.add("EPSG:bork"); // invalid CRSs allowed

        String result1 = WMSUtils.matchEPSG(crs4326, codes);

        Assert.assertNotNull(result1);
        Assert.assertEquals("EPSG:4326", result1);

        // 3005 == 42102
        String result2 = WMSUtils.matchEPSG(crs3005, codes);

        Assert.assertNotNull(result2);
        Assert.assertEquals("EPSG:42102", result2);

        String result3 = WMSUtils.matchEPSG(crs3347, codes);
        Assert.assertNull(result3);
    }
}
