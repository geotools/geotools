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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.data.postgis.PostgisTests.Fixture;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public abstract class AbstractVersionedPostgisDataTestCase extends DataTestCase {
    Fixture f;

    ManageableDataSource pool;

    VersionedPostgisDataStore store;

    PostgisConnectionFactory pcFactory;

    protected SimpleFeatureType railType;
    protected SimpleFeatureType treeType;

    protected SimpleFeature[] railFeatures;
    protected SimpleFeature[] treeFeatures;

    protected ReferencedEnvelope railBounds;
    protected ReferencedEnvelope treeBounds;

    public AbstractVersionedPostgisDataTestCase(String name) {
        super(name);
    }

    public String getFixtureFile() {
        return "versioned.properties";
    }
    
    protected void setUp() throws Exception {
        super.setUp();

        f = PostgisTests.newFixture(getFixtureFile());

        String url = "jdbc:postgresql" + "://" + f.host + ":" + f.port + "/" + f.database;
        pool = DataSourceUtil.buildDefaultDataSource(url, "org.postgresql.Driver", f.user, f.password, 20, 1, "select now()", false, -1);

        // make sure versioned metadata is not in the way
        SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_TABLESCHANGED, false);
        SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_VERSIONEDTABLES, false);
        SqlTestUtils.dropTable(pool, VersionedPostgisDataStore.TBL_CHANGESETS, true);
        SqlTestUtils.execute(pool, "DELETE FROM geometry_columns");
        
        setUpLakeTable();
        setUpRiverTable();
        setUpRoadTable();
        setUpRailTable();
        setUpNoPrimaryKeyTable();
        setUpTreeTable();
        setUpEmptyTable();
        setUpPointTable();
        setUpGeometrylessTable();
    }

    protected void dataSetUp() throws Exception {
        super.dataSetUp();

        railType = DataUtilities.createType(getName() + ".rail",
                "geom:LineString:nillable;srid=4326");
        railFeatures = new SimpleFeature[1];
        // 0,0 +-----------+ 10,10
        railFeatures[0] = SimpleFeatureBuilder.build(railType, new Object[] { line(new int[] { 0,0, 10, 10}) },
                "rail.1");
        railBounds = new ReferencedEnvelope();
        railBounds.include(railFeatures[0].getBounds());
        
        treeType = DataUtilities.createType(getName() +".tree",
          "geom:Point:nillable,name:String");
        treeFeatures = new SimpleFeature[1];
        treeFeatures[0] = SimpleFeatureBuilder.build(treeType, new Object[]{
            gf.createPoint(new Coordinate(5,5)),
            "BigPine"
        },
        "tree.tr1"
        );
        treeBounds = new ReferencedEnvelope();
        treeBounds.include(treeFeatures[0].getBounds());      
    }

    protected VersionedPostgisDataStore getDataStore() throws IOException {
        if (store == null) {
            store = buildDataStore();
        }
        return store;
    }

    /**
     * Builds a brand new datastore
     * 
     * @return
     * @throws IOException
     */
    protected VersionedPostgisDataStore buildDataStore() throws IOException {
        VersionedPostgisDataStore ds = new VersionedPostgisDataStore(pool, f.schema, getName(),
                PostgisDataStore.OPTIMIZE_SQL);
        ds.setWKBEnabled(true);
        return ds;
    }

    protected void tearDown() throws Exception {
        store = null;
        pool.close();
        super.tearDown();
    }
    
    protected void setUpTreeTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','tree','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".tree cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema + ".tree ( id serial primary key)");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                    + "', 'tree', 'geom', -1, 'POINT', 2);");
            s.execute("ALTER TABLE " + f.schema + ".tree add name varchar;");

            for (int i = 0; i < treeFeatures.length; i++) {
                SimpleFeature feature = treeFeatures[i];

                // strip out the lake.
                String ql = "INSERT INTO " + f.schema + ".tree (geom,name) VALUES ("
                        + "GeometryFromText('"
                        + ((Geometry) feature.getAttribute("geom")).toText() + "', -1 )," + "'"
                        + feature.getAttribute("name") + "')";

                s.execute(ql);
            }
        } finally {
            conn.close();
        }
    }

    protected void setUpRoadTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','road','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".road cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema
                    + ".road (fid varchar PRIMARY KEY, id int ) WITH OIDS");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                    + "', 'road', 'geom', -1, 'LINESTRING', 2);");
            s.execute("ALTER TABLE " + f.schema + ".road add name varchar;");

            for (int i = 0; i < roadFeatures.length; i++) {
                SimpleFeature feature = roadFeatures[i];

                // strip out the road.
                String fid = feature.getID().substring("road.".length());
                String ql = "INSERT INTO " + f.schema + ".road (fid,id,geom,name) VALUES (" + "'"
                        + fid + "'," + feature.getAttribute("id") + "," + "GeometryFromText('"
                        + ((Geometry) feature.getAttribute("geom")).toText() + "', -1 )," + "'"
                        + feature.getAttribute("name") + "')";

                s.execute(ql);
            }
        } finally {
            conn.close();
        }
    }

    protected void setUpLakeTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','lake','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".lake cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema + ".lake ( id int ) WITH OIDS");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                    + "', 'lake', 'geom', -1, 'POLYGON', 2);");
            s.execute("ALTER TABLE " + f.schema + ".lake add name varchar;");

            for (int i = 0; i < lakeFeatures.length; i++) {
                SimpleFeature feature = lakeFeatures[i];

                // strip out the lake.
                String ql = "INSERT INTO " + f.schema + ".lake (id,geom,name) VALUES ("
                        + feature.getAttribute("id") + "," + "GeometryFromText('"
                        + ((Geometry) feature.getAttribute("geom")).toText() + "', -1 )," + "'"
                        + feature.getAttribute("name") + "')";

                s.execute(ql);
            }
        } finally {
            conn.close();
        }
    }

    protected void setUpRailTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','rail','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".rail  cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema + ".rail ( id serial primary key ) WITH OIDS");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                    + "', 'rail', 'geom', 4326, 'LINESTRING', 2);");

            for (int i = 0; i < railFeatures.length; i++) {
                SimpleFeature feature = railFeatures[i];

                // strip out the lake.
                String ql = "INSERT INTO " + f.schema + ".rail (geom) VALUES ("
                        + "GeometryFromText('"
                        + ((Geometry) feature.getAttribute("geom")).toText() + "', 4326 ))";

                s.execute(ql);
            }
        } finally {
            conn.close();
        }
    }
    
    protected void setUpNoPrimaryKeyTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','nopk','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".nopk  cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema + ".nopk ( id int ) WITHOUT OIDS");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                    + "', 'nopk', 'geom', -1, 'POLYGON', 2);");
            s.execute("ALTER TABLE " + f.schema + ".nopk add name varchar;");
        } finally {
            conn.close();
        }
    }

//    protected void killTestTables() throws Exception {
//        Connection conn = pool.getConnection();
//
//        try {
//            Statement s = conn.createStatement();
//
//            try {
//                s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','road','geom')");
//            } catch (Exception ignore) {
//            }
//
//            try {
//                s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','river','geom')");
//            } catch (Exception ignore) {
//            }
//
//            try {
//                s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','lake','geom')");
//            } catch (Exception ignore) {
//            }
//            
//            try {
//                s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','rail','geom')");
//            } catch (Exception ignore) {
//            }
//            
//            try {
//                s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','empty','geom')");
//            } catch (Exception ignore) {
//            }
//            
//            try {
//                s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','point','geom')");
//            } catch (Exception ignore) {
//            }
//
//            try {
//                s.execute("DROP TABLE " + f.schema + ".road");
//            } catch (Exception ignore) {
//            }
//
//            try {
//                s.execute("DROP TABLE " + f.schema + ".river");
//            } catch (Exception ignore) {
//            }
//
//            try {
//                s.execute("DROP TABLE " + f.schema + ".lake");
//            } catch (Exception ignore) {
//            }
//            
//            try {
//                s.execute("DROP TABLE " + f.schema + ".rail");
//            } catch (Exception ignore) {
//            }
//            
//            try {
//                s.execute("DROP TABLE " + f.schema + ".empty");
//            } catch (Exception ignore) {
//            }
//            
//            try {
//                s.execute("DROP TABLE " + f.schema + ".point");
//            } catch (Exception ignore) {
//            }
//
//        } finally {
//            conn.close();
//        }
//    }

    protected void setUpRiverTable() throws Exception {
        Connection conn = pool.getConnection();

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','river','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".river cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema
                    + ".river(fid varchar PRIMARY KEY, id int) WITH OIDS");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                    + "', 'river', 'geom', -1, 'MULTILINESTRING', 2);");
            s.execute("ALTER TABLE " + f.schema + ".river add river varchar");
            s.execute("ALTER TABLE " + f.schema + ".river add flow float8");

            for (int i = 0; i < riverFeatures.length; i++) {
                SimpleFeature feature = riverFeatures[i];
                String fid = feature.getID().substring("river.".length());
                s
                        .execute("INSERT INTO " + f.schema
                                + ".river (fid, id, geom, river, flow) VALUES (" + "'" + fid + "',"
                                + feature.getAttribute("id") + "," + "GeometryFromText('"
                                + feature.getAttribute("geom").toString() + "', -1 )," + "'"
                                + feature.getAttribute("river") + "',"
                                + feature.getAttribute("flow") + ")");
            }
        } finally {
            conn.close();
        }
    }
    
    protected void setUpEmptyTable() throws Exception {
        Connection conn = pool.getConnection();

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','empty','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".empty cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema
                    + ".empty(fid varchar PRIMARY KEY, id int)");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                    + "', 'empty', 'geom', -1, 'POINT', 2);");
        } finally {
            conn.close();
        }
    }
    
    protected void setUpPointTable() throws Exception {
        Connection conn = pool.getConnection();

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema + "','point','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".point cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema
                    + ".point(fid varchar PRIMARY KEY, id int)");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                    + "', 'point', 'geom', 4326, 'POINT', 2);");
            
            s.execute("INSERT INTO " + f.schema
                    + ".point (fid, id, geom) VALUES (" + "'point1',1," +
                    "GeometryFromText('POINT (0.0 0.0)',4326))");
        } finally {
            conn.close();
        }
    }
    
    protected void setUpGeometrylessTable() throws Exception {
        Connection conn = pool.getConnection();

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".gless cascade");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema
                    + ".gless(fid uuid primary key, name varchar(256), flow double precision)");
            
            s.execute("INSERT INTO " + f.schema
                    + ".gless (fid, name, flow) VALUES ('3228895e-4c83-451d-b793-55c795300be9', 'first', 10.5)");
            s.execute("INSERT INTO " + f.schema
                    + ".gless (fid, name, flow) VALUES ('611e9785-e775-481e-b5ca-02f59a6998aa', 'second', 0.0)");
        } finally {
            conn.close();
        }
    }
}
