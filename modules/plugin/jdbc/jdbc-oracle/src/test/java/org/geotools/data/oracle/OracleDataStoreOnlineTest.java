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

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 *
 * @source $URL$
 */
public class OracleDataStoreOnlineTest extends JDBCDataStoreOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }
    
    public void testCreateSchemaWktCrs() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(tname("ft2"));
        builder.setNamespaceURI(dataStore.getNamespaceURI());
        CoordinateReferenceSystem crs = CRS.parseWKT("GEOGCS[\"NAD83\", \n" + 
                "  DATUM[\"North American Datum 1983\", \n" + 
                "    SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]], \n" + 
                "    TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], \n" + 
                "    AUTHORITY[\"EPSG\",\"6269\"]], \n" + 
                "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n" + 
                "  UNIT[\"degree\", 0.017453292519943295], \n" + 
                "  AXIS[\"Geodetic longitude\", EAST], \n" + 
                "  AXIS[\"Geodetic latitude\", NORTH], \n" + 
                "  AUTHORITY[\"EPSG\",\"4269\"]]");
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

}
