/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class JDBCIAUOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCIAUTestSetup createTestSetup();

    @Test
    public void testCRS() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("mars_poi"));
        SimpleFeatureType ft = fs.getSchema();

        CoordinateReferenceSystem crs = ft.getCoordinateReferenceSystem();
        assertEquals("IAU:49900", CRS.lookupIdentifier(crs, true));
        CoordinateReferenceSystem expected = CRS.decode("IAU:49900", true);
        assertTrue(CRS.equalsIgnoreMetadata(expected, crs));

        // check CRS assignment to features
        List<SimpleFeature> features = DataUtilities.list(fs.getFeatures());
        assertEquals(3, features.size());
        for (SimpleFeature feature : features) {
            assertEquals(crs, feature.getFeatureType().getCoordinateReferenceSystem());
            Point p = (Point) feature.getDefaultGeometry();
            assertEquals(crs, p.getUserData());
        }
    }

    @Test
    public void testCreateSchema() throws Exception {
        SimpleFeatureType mg1 = createGeology("mars_geology");

        if (canReuseSRID()) {
            SimpleFeatureType mg2 = createGeology("mars_geology2");

            // check the SRID has been reused
            assertEquals(getSRID(mg1), getSRID(mg2));
        }
    }

    /**
     * Override this method if the database for some reason cannot recognize and reuse a SRID for
     * the same CRS
     *
     * @return
     */
    protected boolean canReuseSRID() {
        return true;
    }

    /** Allow sub-classes to reuse and perfom more checks */
    protected SimpleFeatureType createGeology(String typename)
            throws IOException, FactoryException {
        dataStore.createSchema(buildGeologyType(typename));
        SimpleFeatureType marsGeology = dataStore.getSchema(typename);
        assertEquals(
                "IAU:49901",
                CRS.lookupIdentifier(marsGeology.getCoordinateReferenceSystem(), true));

        return marsGeology;
    }

    private static SimpleFeatureType buildGeologyType(String typeName) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Polygon.class, "IAU:49901");
        tb.add("type", String.class);
        tb.setName(typeName);
        return tb.buildFeatureType();
    }

    private int getSRID(SimpleFeatureType ft) {
        return (int) ft.getGeometryDescriptor().getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
    }
}
