/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.data.hana.metadata.Srs;
import org.geotools.jdbc.JDBCCompound3DTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaCompound3DTestSetup extends JDBCCompound3DTestSetup {

    private static final String TABLE_POINT = "pointCompound3d";

    private static final String TABLE_LINE = "lineCompound3d";

    private static final String TABLE_POLY = "polyCompound3d";

    public HanaCompound3DTestSetup(JDBCTestSetup setup) {
        super(setup);
    }

    @Override
    protected void createLineCompound3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            createSrs(htu);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(7415)"},
                {"name", "VARCHAR(255)"}
            };
            htu.createRegisteredTestTable(TABLE_LINE, cols);

            htu.insertIntoTestTable(
                    TABLE_LINE,
                    htu.nextTestSequenceValueForColumn(TABLE_LINE, "fid"),
                    0,
                    htu.geometry("LINESTRING Z(1 1 0, 2 2 0, 4 2 1, 5 1 1)", 7415),
                    "l1");
            htu.insertIntoTestTable(
                    TABLE_LINE,
                    htu.nextTestSequenceValueForColumn(TABLE_LINE, "fid"),
                    1,
                    htu.geometry("LINESTRING Z(3 0 1, 3 2 2, 3 3 3, 3 4 5)", 7415),
                    "l2");
        }
    }

    @Override
    protected void createPointCompound3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            createSrs(htu);
            htu.createTestSchema();

            String[][] cols = {
                {"fid", "INT PRIMARY KEY"},
                {"id", "INT"},
                {"geom", "ST_Geometry(7415)"},
                {"name", "VARCHAR(255)"}
            };
            htu.createRegisteredTestTable(TABLE_POINT, cols);

            htu.insertIntoTestTable(
                    TABLE_POINT,
                    htu.nextTestSequenceValueForColumn(TABLE_POINT, "fid"),
                    0,
                    htu.geometry("POINT Z(1 1 1)", 7415),
                    "p1");
            htu.insertIntoTestTable(
                    TABLE_POINT,
                    htu.nextTestSequenceValueForColumn(TABLE_POINT, "fid"),
                    1,
                    htu.geometry("POINT Z(3 0 1)", 7415),
                    "p2");
        }
    }

    @Override
    protected void dropLineCompound3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE_LINE);
        }
    }

    @Override
    protected void dropPolyCompound3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE_POLY);
        }
    }

    @Override
    protected void dropPointCompound3DTable() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);
            htu.dropTestTableCascade(TABLE_POINT);
        }
    }

    private void createSrs(HanaTestUtil htu) throws SQLException {
        Srs srs =
                new Srs(
                        "Amersfoort / RD New + NAP height",
                        7415,
                        "EPSG",
                        7415,
                        "COMPD_CS[\"Amersfoort / RD New + NAP height\",PROJCS[\"Amersfoort / RD New\",GEOGCS[\"Amersfoort\",DATUM[\"Amersfoort\",SPHEROID[\"Bessel 1841\",6377397.155,299.1528128,AUTHORITY[\"EPSG\",\"7004\"]],TOWGS84[565.417,50.3319,465.552,-0.398957,0.343988,-1.8774,4.0725],AUTHORITY[\"EPSG\",\"6289\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4289\"]],PROJECTION[\"Oblique_Stereographic\"],PARAMETER[\"latitude_of_origin\",52.15616055555555],PARAMETER[\"central_meridian\",5.38763888888889],PARAMETER[\"scale_factor\",0.9999079],PARAMETER[\"false_easting\",155000],PARAMETER[\"false_northing\",463000],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],AUTHORITY[\"EPSG\",\"28992\"]],VERT_CS[\"NAP height\",VERT_DATUM[\"Normaal Amsterdams Peil\",2005,AUTHORITY[\"EPSG\",\"5109\"]],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"Up\",UP],AUTHORITY[\"EPSG\",\"5709\"]],AUTHORITY[\"EPSG\",\"7415\"]]",
                        "+proj=sterea +lat_0=52.15616055555555 +lon_0=5.38763888888889 +k=0.9999079 +x_0=155000 +y_0=463000 +ellps=bessel +towgs84=565.417,50.3319,465.552,-0.398957,0.343988,-1.8774,4.0725 +units=m +vunits=m +no_defs",
                        "meter",
                        "degree",
                        Srs.Type.PROJECTED,
                        6377397.155,
                        null,
                        299.1528128,
                        646.36,
                        276050.82,
                        308975.28,
                        636456.31);
        htu.createSrs(srs);
    }
}
