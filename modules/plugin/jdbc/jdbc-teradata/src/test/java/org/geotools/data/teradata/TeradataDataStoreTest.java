/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStoreTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

public class TeradataDataStoreTest extends JDBCDataStoreTest {


    protected JDBCTestSetup createTestSetup() {
        return new TeradataTestSetup();
    }

    public void testCreateSchemaWithCaseSensitivity() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(CRS.decode("EPSG:4326"));
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("stringProperty"), String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        dataStore.createSchema(featureType);


        FeatureWriter w = dataStore.getFeatureWriter(tname("ft2"), Transaction.AUTO_COMMIT);
        w.hasNext();

        SimpleFeature f = (SimpleFeature) w.next();
        f.setAttribute(1, new Integer(0));
        f.setAttribute(2, "one");
        w.write();
        w.close();

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo correct = ff.equal(ff.property(aname("stringProperty")), ff.literal("one"), true);
        PropertyIsEqualTo incorrect = ff.equal(ff.property(aname("stringProperty")), ff.literal("OnE"), true);

        assertEquals(1, dataStore.getFeatureSource("ft2").getCount(new Query(tname("ft2"), correct)));
        assertEquals(0, dataStore.getFeatureSource("ft2").getCount(new Query(tname("ft2"), incorrect)));
    }
}
