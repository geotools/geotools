/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis.ps;

import java.util.Arrays;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostGISDialect;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.jdbc.JDBCUDTOnlineTest;
import org.geotools.jdbc.JDBCUDTTestSetup;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

/**
 * 
 *
 * @source $URL$
 */
public class PostgisUDTOnlineTest extends JDBCUDTOnlineTest {

    public void testBigDates() throws Exception {
        FeatureWriter w = dataStore.getFeatureWriterAppend(tname("bigdates"), Transaction.AUTO_COMMIT);
        w.hasNext();
        
        SimpleFeature f = (SimpleFeature) w.next();
        f.setAttribute(aname("d"), new java.util.Date());
        
        w.write();
        w.close();
        
        SimpleFeatureCollection features = dataStore.getFeatureSource(tname("bigdates")).getFeatures();
        SimpleFeatureIterator fi = features.features();
        while (fi.hasNext()) {
            SimpleFeature n = fi.next();
        }
        
        fi.close();
        
        // build query to grab the dimension values
        final Query dimQuery = new Query( tname("bigdates") );
        dimQuery.setPropertyNames(Arrays.asList("d"));
        features = dataStore.getFeatureSource(tname("bigdates")).getFeatures(dimQuery);
        
        UniqueVisitor v = new UniqueVisitor("d");
        features.accepts(v, null);
        
        SimpleFeatureType schema = dataStore.getSchema(tname("bigdates"));
        SimpleFeatureType newFeatureType = FeatureTypes.newFeatureType(schema.getAttributeDescriptors().toArray(new AttributeDescriptor[0]), "bigdates2");
        dataStore.createSchema(newFeatureType);
        schema = dataStore.getSchema(tname("bigdates2"));
        assertEquals(PostGISDialect.XDate.class, schema.getType("d").getBinding());
        
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo eq = ff.equals(ff.property("d"),ff.literal("2000-06-08T06:00:00Z"));
        features = dataStore.getFeatureSource(tname("bigdates")).getFeatures(eq);
        
    }

    @Override
    protected JDBCUDTTestSetup createTestSetup() {
        return new org.geotools.data.postgis.PostgisUDTTestSetup();
    }

}