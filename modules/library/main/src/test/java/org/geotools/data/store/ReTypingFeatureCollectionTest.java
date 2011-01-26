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
package org.geotools.data.store;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;

public class ReTypingFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {

    public void testSchema() throws Exception {
        // see http://jira.codehaus.org/browse/GEOT-1616
        SimpleFeatureType original = delegate.getSchema();
        String newName = original.getTypeName() + "xxx";
        SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
        stb.init(original);
        stb.setName(newName);
        SimpleFeatureType renamed = stb.buildFeatureType();

        ReTypingFeatureCollection rtc = new ReTypingFeatureCollection(delegate, renamed);
        assertEquals(renamed, rtc.getSchema());
    }
}
