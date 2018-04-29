/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeoPkgSchemaTest {

    GeoPackage geopackage;

    @Before
    public void setUp() throws Exception {
        geopackage =
                new GeoPackage(File.createTempFile("GeoPkgSchemaTest", "db", new File("target")));
        geopackage.init();
    }

    @After
    public void tearDown() throws Exception {
        geopackage.close();
    }

    @Test
    public void TilesTableSchema() {
        TileEntry e = new TileEntry();
        e.setTableName("tilestable1");
        e.setBounds(new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84));
        e.getTileMatricies().add(new TileMatrix(0, 1, 1, 256, 256, 0.1, 0.1));

        try {
            geopackage.create(e);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Map<String, Object>> expectedColumnDefinitions = new ArrayList<>();
        Map<String, Object> idColumn = new HashMap<>();
        idColumn.put("cid", 0);
        idColumn.put("name", "id");
        idColumn.put("typeval", "INTEGER");
        idColumn.put("notnull", true);
        idColumn.put("default_value", null);
        idColumn.put("pk", true);
        expectedColumnDefinitions.add(idColumn);
        Map<String, Object> zoomLevelColumn = new HashMap<>();
        zoomLevelColumn.put("cid", 1);
        zoomLevelColumn.put("name", "zoom_level");
        zoomLevelColumn.put("typeval", "INTEGER");
        zoomLevelColumn.put("notnull", true);
        zoomLevelColumn.put("default_value", null);
        zoomLevelColumn.put("pk", false);
        expectedColumnDefinitions.add(zoomLevelColumn);
        Map<String, Object> tileColumnColumn = new HashMap<>();
        tileColumnColumn.put("cid", 2);
        tileColumnColumn.put("name", "tile_column");
        tileColumnColumn.put("typeval", "INTEGER");
        tileColumnColumn.put("notnull", true);
        tileColumnColumn.put("default_value", null);
        tileColumnColumn.put("pk", false);
        expectedColumnDefinitions.add(tileColumnColumn);
        Map<String, Object> tileRowColumn = new HashMap<>();
        tileRowColumn.put("cid", 3);
        tileRowColumn.put("name", "tile_row");
        tileRowColumn.put("typeval", "INTEGER");
        tileRowColumn.put("notnull", true);
        tileRowColumn.put("default_value", null);
        tileRowColumn.put("pk", false);
        expectedColumnDefinitions.add(tileRowColumn);
        Map<String, Object> tiledataColumn = new HashMap<>();
        tiledataColumn.put("cid", 4);
        tiledataColumn.put("name", "tile_data");
        tiledataColumn.put("typeval", "BLOB");
        tiledataColumn.put("notnull", true);
        tiledataColumn.put("default_value", null);
        tiledataColumn.put("pk", false);
        expectedColumnDefinitions.add(tiledataColumn);

        ArrayList<Map<String, Object>> columnDefinitions;
        try {
            columnDefinitions = getTableSchema(e.getTableName());
            assertEquals(expectedColumnDefinitions, columnDefinitions);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void GeoPackageMetadataTableSchema() {

        ArrayList<Map<String, Object>> expectedColumnDefinitions = new ArrayList<>();
        Map<String, Object> idColumn = new HashMap<>();
        idColumn.put("cid", 0);
        idColumn.put("name", "id");
        idColumn.put("typeval", "INTEGER");
        idColumn.put("notnull", true);
        idColumn.put("default_value", null);
        idColumn.put("pk", true);
        expectedColumnDefinitions.add(idColumn);
        Map<String, Object> mdscopeColumn = new HashMap<>();
        mdscopeColumn.put("cid", 1);
        mdscopeColumn.put("name", "md_scope");
        mdscopeColumn.put("typeval", "TEXT");
        mdscopeColumn.put("notnull", true);
        mdscopeColumn.put("default_value", "'dataset'");
        mdscopeColumn.put("pk", false);
        expectedColumnDefinitions.add(mdscopeColumn);
        Map<String, Object> standarduriColumn = new HashMap<>();
        standarduriColumn.put("cid", 2);
        standarduriColumn.put("name", "md_standard_uri");
        standarduriColumn.put("typeval", "TEXT");
        standarduriColumn.put("notnull", true);
        standarduriColumn.put("default_value", null);
        standarduriColumn.put("pk", false);
        expectedColumnDefinitions.add(standarduriColumn);
        Map<String, Object> mimetypeColumn = new HashMap<>();
        mimetypeColumn.put("cid", 3);
        mimetypeColumn.put("name", "mime_type");
        mimetypeColumn.put("typeval", "TEXT");
        mimetypeColumn.put("notnull", true);
        mimetypeColumn.put("default_value", "'text/xml'");
        mimetypeColumn.put("pk", false);
        expectedColumnDefinitions.add(mimetypeColumn);
        Map<String, Object> metadataColumn = new HashMap<>();
        metadataColumn.put("cid", 4);
        metadataColumn.put("name", "metadata");
        metadataColumn.put("typeval", "TEXT");
        metadataColumn.put("notnull", true);
        metadataColumn.put("default_value", "''");
        metadataColumn.put("pk", false);
        expectedColumnDefinitions.add(metadataColumn);

        ArrayList<Map<String, Object>> columnDefinitions;
        try {
            columnDefinitions = getTableSchema("gpkg_metadata");
            assertEquals(expectedColumnDefinitions, columnDefinitions);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }

    private ArrayList<Map<String, Object>> getTableSchema(String tableName) throws SQLException {
        ArrayList<Map<String, Object>> columnDefinitions = new ArrayList<>();
        Connection cx = geopackage.getDataSource().getConnection();
        try (Statement st = cx.createStatement()) {
            ResultSet rs = st.executeQuery(String.format("PRAGMA table_info('%s')", tableName));
            while (rs.next()) {
                Map<String, Object> columnDefinition = new HashMap<>();
                columnDefinition.put("cid", rs.getInt("cid"));
                columnDefinition.put("name", rs.getString("name"));
                columnDefinition.put("typeval", rs.getString("type"));
                columnDefinition.put("notnull", rs.getBoolean("notnull"));
                columnDefinition.put("default_value", rs.getString("dflt_value"));
                columnDefinition.put("pk", rs.getBoolean("pk"));
                columnDefinitions.add(columnDefinition);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            cx.close();
        }
        return columnDefinitions;
    }
}
