/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Created on August 16, 2003, 5:10 PM
 */
package org.geotools.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.api.data.Query;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.helpers.NamespaceSupport;

/** @author jamesm */
public class QueryTest {

    @Test
    public void testFullConstructor() {
        Query query = new Query("mytype", Filter.INCLUDE, 10, new String[] {"foo"}, "myquery");
        Assert.assertNotNull(query);
    }

    /** Test of getPropertyNames method, of class org.geotools.api.data.Query. */
    @Test
    public void testPropertyNames() {
        // System.out.println("testPropertyNames");
        Query query = new Query();
        Assert.assertNull(query.getPropertyNames());
        query.setPropertyNames("foo", "bar");
        String[] names = query.getPropertyNames();
        Assert.assertNotNull(names);
        Assert.assertEquals("foo", names[0]);
        List<String> list = Arrays.asList(names);
        query.setPropertyNames(list);
        names = query.getPropertyNames();

        Assert.assertEquals("bar", names[1]);

        // test compatibility with getProperties method
        List<PropertyName> properties2 = query.getProperties();
        Assert.assertNotNull(properties2);
        Assert.assertEquals("foo", properties2.get(0).getPropertyName());
        Assert.assertEquals("bar", properties2.get(1).getPropertyName());

        query.setPropertyNames(Query.ALL_NAMES);
        Assert.assertNull(query.getPropertyNames());

        query = new Query("Test", Filter.INCLUDE, new String[] {"foo", "wibble"});
        Assert.assertNotNull(query.getPropertyNames());
    }

    /** Test of set/getProperties method, of class org.geotools.api.data.Query. */
    @Test
    public void testProperties() {
        final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        // System.out.println("testProperties");
        Query query = new Query();
        Assert.assertNull(query.getProperties());
        List<PropertyName> properties = new ArrayList<>();

        NamespaceSupport nsContext = new NamespaceSupport();
        nsContext.declarePrefix("foo", "FooNamespace");

        PropertyName fooProp = ff.property("foo", nsContext);
        PropertyName barProp = ff.property("bar", nsContext);
        properties.add(fooProp);
        properties.add(barProp);

        query.setProperties(properties);

        List<PropertyName> properties2 = query.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertEquals(fooProp, properties2.get(0));
        Assert.assertEquals(barProp, properties2.get(1));
        Assert.assertEquals(nsContext, properties2.get(0).getNamespaceContext());

        // test compatibility with getPropertyNames method
        String[] names = query.getPropertyNames();
        Assert.assertEquals("foo", names[0]);
        Assert.assertEquals("bar", names[1]);

        query.setProperties(Query.ALL_PROPERTIES);
        Assert.assertNull(query.getProperties());

        query = new Query("Test", Filter.INCLUDE, properties);
        Assert.assertNotNull(query.getProperties());
    }

    /** Test of retrieveAllProperties method, of class org.geotools.api.data.Query. */
    @Test
    public void testRetrieveAllProperties() {
        // System.out.println("testRetrieveAllProperties");

        Query query = new Query();
        Assert.assertTrue(query.retrieveAllProperties());

        query.setPropertyNames("foo", "bar");
        Assert.assertFalse(query.retrieveAllProperties());

        query.setPropertyNames(Query.ALL_NAMES);
        Assert.assertTrue(query.retrieveAllProperties());

        query.setProperties(Query.ALL_PROPERTIES);
        Assert.assertTrue(query.retrieveAllProperties());

        query.setPropertyNames("foo", "bar");
        query.setProperties(Query.ALL_PROPERTIES);
        Assert.assertTrue(query.retrieveAllProperties());
    }

    /** Test of getMaxFeatures method, of class org.geotools.api.data.Query. */
    @Test
    public void testMaxFeatures() {
        // System.out.println("testMaxFeatures");
        Query query = new Query();
        Assert.assertEquals(Query.DEFAULT_MAX, query.getMaxFeatures());

        query.setMaxFeatures(5);
        Assert.assertEquals(5, query.getMaxFeatures());
    }

    /** Test of getFilter method, of class org.geotools.api.data.Query. */
    @Test
    public void testFilter() {
        // System.out.println("testGetFilter");
        Query query = new Query();
        query.setFilter(Filter.EXCLUDE);
        Assert.assertEquals(Filter.EXCLUDE, query.getFilter());

        query = new Query("test", Filter.INCLUDE);
        Assert.assertEquals(Filter.INCLUDE, query.getFilter());
    }

    /** Test of getTypeName method, of class org.geotools.api.data.Query. */
    @Test
    public void testTypeName() {
        Query query = new Query();
        Assert.assertNull(query.getTypeName());

        query.setTypeName("foobar");
        Assert.assertEquals("foobar", query.getTypeName());

        query = new Query("mytype", Filter.EXCLUDE);
        Assert.assertEquals("mytype", query.getTypeName());
    }

    /** Test of getHandle method, of class org.geotools.api.data.Query. */
    @Test
    public void testHandle() {
        // System.out.println("testGetHandle");
        Query query = new Query();
        Assert.assertNull(query.getHandle());
        query.setHandle("myquery");
        Assert.assertEquals("myquery", query.getHandle());
    }

    /** Test of getVersion method, of class org.geotools.api.data.Query. */
    @Test
    public void testVersion() {
        // System.out.println("testGetVersion");
        Query query = new Query();
        Assert.assertNull(query.getVersion());
    }

    /** Test of toString method, of class org.geotools.api.data.Query. */
    @Test
    public void testToString() {
        // System.out.println("testToString");
        Query query = new Query();
        Assert.assertNotNull(query.toString());

        query.setHandle("myquery");
        Assert.assertNotNull(query.toString());

        query.setFilter(Filter.EXCLUDE);
        Assert.assertNotNull(query.toString());

        query.setPropertyNames("foo", "bar");
        Assert.assertNotNull(query.toString());

        query = new Query();
        query.setSortBy(SortBy.NATURAL_ORDER);
        Assert.assertTrue(query.toString().contains("[sort by: NATURAL]"));
        query.setSortBy(SortBy.REVERSE_ORDER);
        Assert.assertTrue(query.toString().contains("[sort by: REVERSE]"));
    }

    @Test
    public void testSortByEquality() {
        Query q1 = new Query();

        Query q2 = new Query();
        q2.setSortBy(SortBy.NATURAL_ORDER);
        assertThat(q1, not(equalTo(q2)));

        Query q3 = new Query();
        q3.setSortBy(SortBy.NATURAL_ORDER);
        assertThat(q2, equalTo(q3));
    }
}
