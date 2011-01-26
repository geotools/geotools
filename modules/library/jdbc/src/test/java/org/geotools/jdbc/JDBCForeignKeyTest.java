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
package org.geotools.jdbc;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Transaction;
import org.geotools.factory.Hints;
import org.opengis.feature.Association;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;


public abstract class JDBCForeignKeyTest extends JDBCTestSupport {
    protected void connect() throws Exception {
        super.connect();

        dataStore.setAssociations(true);
    }

    public void testGetSchema() throws Exception {
        SimpleFeatureType featureType = dataStore.getSchema("fk");

        assertNotNull(featureType);

        AttributeDescriptor att = featureType.getDescriptor("ft1");
        assertNotNull(att);

        assertEquals(Association.class, att.getType().getBinding());
    }

    public void testGetFeatures() throws Exception {
        Hints hints = new Hints(Hints.ASSOCIATION_TRAVERSAL_DEPTH, new Integer(1));

        DefaultQuery query = new DefaultQuery();
        query.setTypeName("fk");
        query.setHints(hints);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertTrue(reader.hasNext());

        SimpleFeature feature = reader.next();
        Association association = (Association) feature.getAttribute("ft1");
        assertEquals("ft1.0", association.getUserData().get("gml:id"));

        SimpleFeature associated = (SimpleFeature) association.getValue();
        assertNotNull(associated);

        assertEquals("zero", associated.getAttribute("stringProperty"));

        Property attribute = feature.getProperty("ft1");
        assertEquals("ft1.0", attribute.getUserData().get("gml:id"));
        
        reader.close();
    }

    public void testGetFeaturesWithZeroDepth() throws Exception {
        Hints hints = new Hints(Hints.ASSOCIATION_TRAVERSAL_DEPTH, new Integer(0));

        DefaultQuery query = new DefaultQuery();
        query.setTypeName("fk");
        query.setHints(hints);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertTrue(reader.hasNext());

        SimpleFeature feature = reader.next();
        Association association = (Association) feature.getAttribute("ft1");
        assertNull(association.getValue());

        Property attribute = feature.getProperty("ft1");
        assertEquals("ft1.0", attribute.getUserData().get("gml:id"));
        
        reader.close();
    }
}
