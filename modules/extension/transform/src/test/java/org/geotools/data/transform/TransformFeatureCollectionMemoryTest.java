/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.FeatureCollectionWrapperTestSupport;
import org.geotools.feature.NameImpl;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

public class TransformFeatureCollectionMemoryTest extends FeatureCollectionWrapperTestSupport {

    public SimpleFeatureCollection transformWithExpressions() throws Exception {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("att", ECQL.toExpression("someAtt")));
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(defaultGeom, 1)")));

        SimpleFeatureSource source = DataUtilities.source(delegate);
        TransformFeatureSource transformedSource =
                new TransformFeatureSource(source, new NameImpl("Transformed"), definitions);
        return transformedSource.getFeatures();
    }

    @Test
    public void testPreserveUserData() throws Exception {
        SimpleFeatureCollection transformedCollection = transformWithExpressions();
        SimpleFeature first = DataUtilities.first(transformedCollection);
        assertEquals(TEST_VALUE, first.getUserData().get(TEST_KEY));
    }

    @Test
    public void testOffsetNotSupported() throws Exception {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("att", ECQL.toExpression("someAtt")));
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(defaultGeom, 1)")));

        SimpleFeatureSource source =
                new CollectionFeatureSource(delegate) {
                    /** Return a query caps declaring offset is not supported */
                    @Override
                    public synchronized QueryCapabilities getQueryCapabilities() {
                        return new QueryCapabilities();
                    }
                };
        TransformFeatureSource transformedSource =
                new TransformFeatureSource(source, new NameImpl("Transformed"), definitions);

        // try to transform a query with paging
        Query q = new Query();
        q.setStartIndex(2);
        q.setMaxFeatures(1);
        Query transformed = transformedSource.transformer.transformQuery(q);
        assertNull(transformed.getStartIndex());
        assertEquals(Integer.MAX_VALUE, transformed.getMaxFeatures());

        // run it for good, check paging works
        List<SimpleFeature> features = DataUtilities.list(transformedSource.getFeatures(q));
        assertEquals(1, features.size());
        SimpleFeature f = features.get(0);
        assertEquals("2", f.getID());
    }

    @Test
    public void testOffsetSupported() throws Exception {
        List<Definition> definitions = new ArrayList<>();
        definitions.add(new Definition("att", ECQL.toExpression("someAtt")));
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(defaultGeom, 1)")));

        TransformFeatureSource transformedSource =
                new TransformFeatureSource(
                        DataUtilities.source(delegate), new NameImpl("Transformed"), definitions);

        // try to transform a query with paging
        Query q = new Query();
        q.setStartIndex(2);
        q.setMaxFeatures(1);
        Query transformed = transformedSource.transformer.transformQuery(q);
        assertEquals(2, transformed.getStartIndex().intValue());
        assertEquals(1, transformed.getMaxFeatures());

        // run it for good, check paging works
        List<SimpleFeature> features = DataUtilities.list(transformedSource.getFeatures(q));
        assertEquals(1, features.size());
        SimpleFeature f = features.get(0);
        assertEquals("2", f.getID());
    }
}
