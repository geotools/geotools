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

public class ShapeFilePreGeneralizedFeatureSourceTest extends
        AbstractPreGeneralizedFeatureSourceTest {

    static final String ConfigName = "src/test/resources/geninfo_shapefile.xml";

    protected Repository getRepository() {
        return new DSFinderRepository();
    }

    public void testGetCount() {
        testGetCount(ConfigName);
    }

    public void testGetBounds() {
        testGetBounds(ConfigName);
    }

    public void testFeatureReader() {
        testFeatureReader(ConfigName);
    }

    public void testFeatureReaderWithoutGeom() {
        testFeatureReaderWithoutGeom(ConfigName);
    }

    public void testGetFeatures() {
        testGetFeatures(ConfigName);
    }

    public void testGetFeatures2() {
        testGetFeatures2(ConfigName);
    }

    public void testGetFeatures3() {
        testGetFeatures3(ConfigName);
    }

    public void testGetDataStore() {
        testGetDataStore(ConfigName);
    }

    public void testGetNameAndInfo() {
        testNameAndInfo(ConfigName);
    }

    public void testQueryCapabilities() {
        testQueryCapabilities(ConfigName);
    }

    public void testGetSchema() {
        testGetSchema(ConfigName);
    }

}
