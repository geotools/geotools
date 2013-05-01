/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import static org.easymock.EasyMock.*;

import java.io.IOException;

import junit.framework.TestCase;

import org.geotools.data.simple.SimpleFeatureCollection;

public class DecoratingSimpleFeatureCollectionTest extends TestCase {

    public void testAccepts() throws IOException {

        SimpleFeatureCollection mock = createMock(SimpleFeatureCollection.class);
        mock.accepts(null, null);
        expectLastCall().once();
        replay(mock);
        
        DecoratingSimpleFeatureCollection col = new DecoratingSimpleFeatureCollection(mock);
        col.accepts(null, null);

        verify(mock);
    }
}
