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

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.UniqueVisitor;
import org.opengis.feature.simple.SimpleFeatureType;

import static org.easymock.EasyMock.*;
/**
 * 
 *
 * @source $URL$
 */
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

    public void testDelegateAccepts() throws Exception {
        SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
        stb.setName("test");
        stb.add("foo", String.class);
        stb.add("bar", Integer.class);

        UniqueVisitor vis = new UniqueVisitor("bar");

        SimpleFeatureCollection delegate = createMock(SimpleFeatureCollection.class);
        delegate.accepts(vis, null);
        expectLastCall().once();
        replay(delegate);

        ReTypingFeatureCollection rtc =
            new ReTypingFeatureCollection(delegate, stb.buildFeatureType());
        rtc.accepts(vis, null);
        verify(delegate);

        vis = new UniqueVisitor("baz");

        SimpleFeatureIterator it = createNiceMock(SimpleFeatureIterator.class);
        replay(it);

        SimpleFeatureType ft = createNiceMock(SimpleFeatureType.class);
        replay(ft);

        delegate = createMock(SimpleFeatureCollection.class);
        expect(delegate.features()).andReturn(it).once();
        expect(delegate.getSchema()).andReturn(ft).once();

        replay(delegate);
        rtc =
            new ReTypingFeatureCollection(delegate, stb.buildFeatureType());
        rtc.accepts(vis, null);
        verify(delegate);
    }
}
