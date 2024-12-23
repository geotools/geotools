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
package org.geotools.data.view;

import java.io.IOException;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.IllegalFilterException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class DefaultViewTest {

    String typeName = "type1";
    private SimpleFeatureSource fs;

    @Before
    public void setUp() throws Exception {

        SimpleFeatureType ft = DataUtilities.createType(typeName, "geom:Point,name:String,id:int");
        ListFeatureCollection collection = new ListFeatureCollection(ft);
        collection.add(createFeatures(ft, 1));
        collection.add(createFeatures(ft, 2));
        collection.add(createFeatures(ft, 3));
        collection.add(createFeatures(ft, 4));
        fs = DataUtilities.source(collection);
    }

    private SimpleFeature createFeatures(SimpleFeatureType ft, int i) throws IllegalAttributeException {
        GeometryFactory fac = new GeometryFactory();
        return SimpleFeatureBuilder.build(
                ft, new Object[] {fac.createPoint(new Coordinate(i, i)), "name" + i, Integer.valueOf(i)}, null);
    }

    @Test
    public void testGetFeatures() throws Exception {
        SimpleFeatureSource view = getView();

        try (SimpleFeatureIterator features = view.getFeatures().features()) {
            int count = 0;
            while (features.hasNext()) {
                count++;
                features.next();
            }

            Assert.assertEquals(2, count);
        }
    }

    @Test
    public void testGetFeaturesQuery() throws Exception {

        SimpleFeatureSource view = getView();

        try (SimpleFeatureIterator features = view.getFeatures(getQuery()).features()) {
            int count = 0;
            while (features.hasNext()) {
                count++;
                features.next();
            }

            Assert.assertEquals(1, count);
        }
    }

    @Test
    public void testGetFeaturesFilter() throws Exception {

        SimpleFeatureSource view = getView();
        Filter f = getFilter();
        try (SimpleFeatureIterator features = view.getFeatures(f).features()) {
            int count = 0;
            while (features.hasNext()) {
                count++;
                features.next();
            }
            Assert.assertEquals(1, count);
        }
    }

    @Test
    public void testGetCount() throws Exception {
        SimpleFeatureSource view = getView();

        Query query = getQuery();
        int count = view.getCount(query);
        Assert.assertEquals(1, count);
    }

    private Query getQuery() throws IllegalFilterException {
        Filter f = getFilter();
        Query query = new Query(typeName, f, new String[0]);
        return query;
    }

    private Filter getFilter() throws IllegalFilterException {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);
        Filter f = fac.equals(fac.property("name"), fac.literal("name2"));
        return f;
    }

    private SimpleFeatureSource getView() throws IllegalFilterException, IOException, SchemaException {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);
        Filter f = fac.less(fac.property("id"), fac.literal(3));

        SimpleFeatureSource view = DataUtilities.createView(fs, new Query(typeName, f));
        return view;
    }
}
