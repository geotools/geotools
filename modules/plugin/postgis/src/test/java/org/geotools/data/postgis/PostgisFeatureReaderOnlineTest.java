/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.sql.Statement;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.fidmapper.PostgisFIDMapperFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Hits a PostGIS database with a feature reader.
 * 
 * @author Cory Horner, Refractions Research 
 *
 * @source $URL$
 */
public class PostgisFeatureReaderOnlineTest extends AbstractPostgisOnlineTestCase {

    protected void createTables(Statement st) throws Exception {
        createTable1(st);
        createTable2(st);
        createTable3(st);
        //advance the sequence to larger values
        st.execute("SELECT setval('"+table1+"_fid_seq', 2000000000);");
        st.execute("SELECT setval('"+table3+"_fid_seq', 6000000000);");
        //put some data in there
        String[] keys = new String[] {"name", "the_geom"};
        String[] values = new String[] {"'f1'", "GeomFromText('POINT(1294523.17592358 469418.897140173)',4326)"};
        addFeatureManual(table1, keys, values);
        values[0] = "'f2'";
        values[1] = "GeomFromText('POINT(1281485.7108 459444.7332)',4326)";
        addFeatureManual(table2, keys, values);
        values[0] = "'f3'";
        values[1] = "GeomFromText('POINT(1271185.71084336 454376.774827237)',4326)";
        addFeatureManual(table3, keys, values);
    }
    
    protected void setupGeometryColumns(Statement st) throws Exception {
        String preSql = "INSERT INTO geometry_columns (f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, type) VALUES ('',";
        String postSql = ", 'the_geom', 2, 4326, 'POINT')";
        //table1: no entry
        //table2: geometry_columns f_table_schema = ''
        String sql = preSql + "'', '" + table2 + "'" + postSql;
        st.execute(sql);
        // table3: geometry_columns f_table_schema = 'public'
        sql = preSql + "'public', '" + table3 + "'" + postSql;
        st.execute(sql);
    }
    
    /**
     * Make sure that both large integer and long values are acceptable and valid.
     * @throws Exception
     */
    public void testReadFid() throws Exception {
    	if ( ((PostgisFIDMapperFactory) ds.getFIDMapperFactory() ).isReturningTypedFIDMapper() ) {
    		assertEquals(table1+".2000000001",attemptRead(table1)); //int is signed :(
            assertEquals(table3+".6000000001",attemptRead(table3));	
    	}
    	else {
    		assertEquals("2000000001",attemptRead(table1)); //int is signed :(
            assertEquals("6000000001",attemptRead(table3));
    	}
    }

    /**
     * Adds a feature so we have something to read.
     */
    protected boolean addFeature(String table) throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(table, Transaction.AUTO_COMMIT);
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = (SimpleFeature) writer.next();
        }

        feature = (SimpleFeature) writer.next();
        feature.setAttribute(0, "test");
        //feature.setAttribute(1, val);
        writer.write();
        String id = feature.getID();
        return id != null;
    }
    
    protected void addFeatureManual(String table, String[] keys, String[] values) throws Exception {
        Statement st = ds.getDataSource().getConnection().createStatement();
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO \"");
        sql.append(table);
        sql.append("\" (");
        for (int i = 0; i < keys.length; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(keys[i]);
        }
        sql.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(values[i]);
        }
        sql.append(")");
        st.execute(sql.toString());
        st.close();
    }
    
    protected String attemptRead(String table) throws Exception {
        //addFeature(table);
        Query query = new DefaultQuery(table);
         FeatureReader<SimpleFeatureType, SimpleFeature> fr = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertTrue(fr.hasNext());
        SimpleFeature feature = fr.next();
        String id = feature.getID();
        fr.close();
        return id;
    }
    
    public void testGetSchema() throws Exception {
        //test that getSchema works when a entry does not exist in the geometry_columns table
        SimpleFeatureType schema;
        schema = ds.getSchema(table1);
        assertNotNull(schema);
        //test that getSchema works when geometry_columns f_table_schema = public
        schema = ds.getSchema(table2);
        assertNotNull(schema);
        //test that getSchema works when geometry_columns f_table_schema = ''
        schema = ds.getSchema(table3);
        assertNotNull(schema);
    }
}
