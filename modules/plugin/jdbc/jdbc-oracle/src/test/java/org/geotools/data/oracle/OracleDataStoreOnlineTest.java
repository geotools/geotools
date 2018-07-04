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
package org.geotools.data.oracle;

import java.sql.Date;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @source $URL$ */
public class OracleDataStoreOnlineTest extends JDBCDataStoreOnlineTest {

    private OracleTestSetup oracleTestSetup;

    @Override
    protected JDBCTestSetup createTestSetup() {
        oracleTestSetup = new OracleTestSetup();
        return oracleTestSetup;
    }

    public void testCreateSchemaOSGBCrs() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        CoordinateReferenceSystem crs = CRS.decode("EPSG:27700");
        builder.setCRS(crs);
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("dateProperty"), Date.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        // used to fail here - with index creation error
        dataStore.createSchema(featureType);
    }

    public void testCreateSchemaWktCrs() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        CoordinateReferenceSystem crs =
                CRS.parseWKT(
                        "GEOGCS[\"NAD83\", \n"
                                + "  DATUM[\"North American Datum 1983\", \n"
                                + "    SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]], \n"
                                + "    TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], \n"
                                + "    AUTHORITY[\"EPSG\",\"6269\"]], \n"
                                + "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
                                + "  UNIT[\"degree\", 0.017453292519943295], \n"
                                + "  AXIS[\"Geodetic longitude\", EAST], \n"
                                + "  AXIS[\"Geodetic latitude\", NORTH], \n"
                                + "  AUTHORITY[\"EPSG\",\"4269\"]]");
        builder.setCRS(crs);
        builder.add(aname("geometry"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("dateProperty"), Date.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        // used to fail here
        dataStore.createSchema(featureType);
    }

    public void testCreateSpatialIndexNameTooLong() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.add(aname("geometry_one_two_three_four"), Geometry.class);
        builder.add(aname("intProperty"), Integer.class);
        builder.add(aname("dateProperty"), Date.class);

        SimpleFeatureType featureType = builder.buildFeatureType();
        // used to fail here
        dataStore.createSchema(featureType);
    }

    public void testCreateLongVarChar() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("longvar"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        builder.setCRS(DefaultGeographicCRS.WGS84);
        builder.add(aname("geometry_one_two_three_four"), Geometry.class);
        builder.add(aname("longvar"), String.class);

        SimpleFeatureType featureType = builder.buildFeatureType();

        dataStore.createSchema(featureType);
        SimpleFeatureBuilder fBuilder = new SimpleFeatureBuilder(featureType);
        fBuilder.add(null);
        StringBuffer vBuffer = new StringBuffer(4000);
        // to be honest I can't tell from the oracle docs if 4000 or 3999 is the
        // actual limit but anything over 255 used to fail
        for (int i = 0; i < 3999; i++) {
            vBuffer.append("x");
        }

        fBuilder.add(vBuffer.toString());
        SimpleFeature f = fBuilder.buildFeature(null);
        // used to fail here
        Transaction transaction = new DefaultTransaction("create");
        SimpleFeatureSource featureSource =
                dataStore.getFeatureSource(featureType.getName().getLocalPart());
        SimpleFeatureCollection collection = DataUtilities.collection(f);
        SimpleFeatureStore outStore = (SimpleFeatureStore) featureSource;
        outStore.setTransaction(transaction);

        try {
            List<FeatureId> ids = outStore.addFeatures(collection);

            transaction.commit();
        } catch (Exception problem) {
            problem.printStackTrace();
            transaction.rollback();
        } finally {
            transaction.close();
            dataStore.removeSchema(tname("longvar"));
        }
    }

    @Override
    protected void tearDownInternal() throws Exception {

        super.tearDownInternal();
        oracleTestSetup.deleteSpatialTable(tname("longvar"));
    }
}
