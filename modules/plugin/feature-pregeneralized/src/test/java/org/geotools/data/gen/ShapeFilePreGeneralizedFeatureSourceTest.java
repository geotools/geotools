/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.gen;

import org.geotools.data.Repository;
import org.junit.Test;

public class ShapeFilePreGeneralizedFeatureSourceTest
        extends AbstractPreGeneralizedFeatureSourceTest {

    static final String ConfigName = "src/test/resources/geninfo_shapefile.xml";

    @Override
    protected Repository getRepository() {
        return new DSFinderRepository();
    }

    @Test
    public void testPropertySelection() {
        testPropertySelection(ConfigName);
    }

    @Test
    public void testGetCount() {
        testGetCount(ConfigName);
    }

    @Test
    public void testGetBounds() {
        testGetBounds(ConfigName);
    }

    @Test
    public void testFeatureReader() {
        testFeatureReader(ConfigName);
    }

    @Test
    public void testFeatureReaderWithoutGeom() {
        testFeatureReaderWithoutGeom(ConfigName);
    }

    @Test
    public void testGetFeatures() {
        testGetFeatures(ConfigName);
    }

    @Test
    public void testGetFeatures2() {
        testGetFeatures2(ConfigName);
    }

    @Test
    public void testGetFeatures3() {
        testGetFeatures3(ConfigName);
    }

    @Test
    public void testGetDataStore() {
        testGetDataStore(ConfigName);
    }

    @Test
    public void testGetNameAndInfo() {
        testNameAndInfo(ConfigName);
    }

    @Test
    public void testQueryCapabilities() {
        testQueryCapabilities(ConfigName, true);
    }

    @Test
    public void testGetSchema() {
        testGetSchema(ConfigName);
    }
}
