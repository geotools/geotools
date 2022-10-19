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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.NearestVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

public class ReTypingFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {

    @Test
    public void testSchema() throws Exception {
        // see http://jira.codehaus.org/browse/GEOT-1616
        SimpleFeatureType schema = delegate.getSchema();
        SimpleFeatureType renamed = buildRenamedFeatureType(schema, schema.getTypeName() + "xxx");

        ReTypingFeatureCollection rtc = new ReTypingFeatureCollection(delegate, renamed);
        assertEquals(renamed, rtc.getSchema());
    }

    private SimpleFeatureType buildRenamedFeatureType(SimpleFeatureType schema, String newName) {
        SimpleFeatureType original = schema;
        SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
        stb.init(original);
        stb.setName(newName);
        return stb.buildFeatureType();
    }

    @Test
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

        @SuppressWarnings("PMD.CloseResource")
        SimpleFeatureIterator it = createNiceMock(SimpleFeatureIterator.class);
        replay(it);

        SimpleFeatureType ft = createNiceMock(SimpleFeatureType.class);
        replay(ft);

        delegate = createMock(SimpleFeatureCollection.class);
        expect(delegate.features()).andReturn(it).once();
        expect(delegate.getSchema()).andReturn(ft).once();

        replay(delegate);
        rtc = new ReTypingFeatureCollection(delegate, stb.buildFeatureType());
        rtc.accepts(vis, null);
        verify(delegate);
    }

    @Test
    public void testDelegateAcceptsNearest() throws Exception {
        SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
        stb.setName("test");
        stb.add("foo", String.class);
        stb.add("bar", Integer.class);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        NearestVisitor vis = new NearestVisitor(ff.property("bar"), Integer.valueOf(0));

        SimpleFeatureCollection delegate = createMock(SimpleFeatureCollection.class);
        delegate.accepts(vis, null);
        expectLastCall().once();
        replay(delegate);

        ReTypingFeatureCollection rtc =
                new ReTypingFeatureCollection(delegate, stb.buildFeatureType());
        rtc.accepts(vis, null);
        verify(delegate);

        vis = new NearestVisitor(ff.property("baz"), "abc");

        @SuppressWarnings("PMD.CloseResource")
        SimpleFeatureIterator it = createNiceMock(SimpleFeatureIterator.class);
        replay(it);

        SimpleFeatureType ft = createNiceMock(SimpleFeatureType.class);
        replay(ft);

        delegate = createMock(SimpleFeatureCollection.class);
        expect(delegate.features()).andReturn(it).once();
        expect(delegate.getSchema()).andReturn(ft).once();

        replay(delegate);
        rtc = new ReTypingFeatureCollection(delegate, stb.buildFeatureType());
        rtc.accepts(vis, null);
        verify(delegate);
    }

    @Test
    public void testPreserveUserData() throws Exception {
        SimpleFeatureType schema = delegate.getSchema();
        SimpleFeatureType renamed = buildRenamedFeatureType(schema, schema.getTypeName() + "xxx");
        ReTypingFeatureCollection rtc = new ReTypingFeatureCollection(delegate, renamed);
        SimpleFeature first = DataUtilities.first(rtc);
        assertEquals(TEST_VALUE, first.getUserData().get(TEST_KEY));
    }

    @Test
    public void testSimilarAttributes() throws Exception {
        SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
        stb.setName("original");
        stb.add("bar", Integer.class);

        SimpleFeatureType schema = stb.buildFeatureType();
        Assert.assertEquals("bar", schema.getDescriptor(0).getType().getName().getLocalPart());

        SimpleFeatureCollection source = new DefaultFeatureCollection("source", schema);

        SimpleFeatureTypeBuilder stb2 = new SimpleFeatureTypeBuilder();
        stb2.setName("test");

        AttributeTypeBuilder atb = new AttributeTypeBuilder();
        atb.setName("Integer");
        atb.setBinding(Integer.class);
        stb2.add(atb.buildDescriptor("bar"));

        SimpleFeatureType schema2 = stb2.buildFeatureType();
        Assert.assertEquals("Integer", schema2.getDescriptor(0).getType().getName().getLocalPart());
        SimpleFeatureCollection retyped = new ReTypingFeatureCollection(source, schema2);
        try (SimpleFeatureIterator features = retyped.features()) {
            Assert.assertFalse(features.hasNext());
        }
    }
}
