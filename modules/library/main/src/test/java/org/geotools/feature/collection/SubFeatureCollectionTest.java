/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Iterator;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.data.DataTestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FidFilterImpl;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.identity.FeatureId;

public class SubFeatureCollectionTest extends DataTestCase {
    FeatureCollection<SimpleFeatureType, SimpleFeature> features = FeatureCollections
            .newCollection();

    public SubFeatureCollectionTest(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SubFeatureCollectionTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("Dummy");

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        for (int i = 0; i < 100; i++) {
            features.add(b.buildFeature(null));
        }
    }

    public void testBounds() {
        FeatureCollection<SimpleFeatureType, SimpleFeature> subCollection = features
                .subCollection(new Filter() {

                    public Object accept(FilterVisitor arg0, Object arg1) {
                        return null;
                    }

                    public boolean evaluate(Object arg0) {
                        return true;
                    }

                });

        // Should not throw an UnsupportedOperationException
        // TODO Not semantically testing the bounds
        assertNotNull(subCollection.getBounds());
    }

}
