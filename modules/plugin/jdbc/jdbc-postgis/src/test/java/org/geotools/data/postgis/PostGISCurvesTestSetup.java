/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCCurvesTestSetup;

public class PostGISCurvesTestSetup extends JDBCCurvesTestSetup {

    protected PostGISCurvesTestSetup() {
        super(new PostGISTestSetup());
    }

    @Override
    protected void setUpData() throws Exception {
        dropCurveIntersectionTable();
        super.setUpData();
        createCurveIntersectionTable();
    }

    private void createCurveIntersectionTable() throws Exception {
        String sql = "CREATE TABLE \"curveIntersection\" ("
                + "\"id\" INT, \"name\" VARCHAR, \"geometry\" geometry(MULTISURFACE, 3879), PRIMARY KEY(id))";
        run(sql);

        sql = "CREATE INDEX CURVE_INTERSECTION_GEOMETRY_IDX ON \"curveIntersection\" USING GIST (\"geometry\") ";
        run(sql);

        // adding data
        sql =
                "INSERT INTO \"curveIntersection\" VALUES (0, 'p1', ST_geometryFromText('MULTISURFACE(CURVEPOLYGON((25492700 6677500, 25492950 6677500, 25492950 6677350, 25492700 6677350, 25492700 6677500),COMPOUNDCURVE((25492739.7748 6677444.3615,25492747.0232 6677449.7828),CIRCULARSTRING(25492747.0232 6677449.7828,25492822.2626 6677477.2365,25492900.1601 6677458.6175,25492901.1068 6677457.3233,25492900.7986 6677455.7498),(25492900.7986 6677455.7498,25492885.9851 6677434.3719),CIRCULARSTRING(25492885.9851 6677434.3719,25492832.3384 6677400.8583,25492771.1213 6677416.7832),(25492771.1213 6677416.7832,25492739.7449 6677441.1816),CIRCULARSTRING(25492739.7449 6677441.1816,25492738.9731 6677442.7789,25492739.7748 6677444.3615))))'))";
        run(sql);
        sql =
                "INSERT INTO \"curveIntersection\" VALUES (1, 'p2', ST_geometryFromText('MULTISURFACE(CURVEPOLYGON(COMPOUNDCURVE((25492739.7449 6677441.1816,25492771.1213 6677416.7832),CIRCULARSTRING(25492771.1213 6677416.7832,25492832.3384 6677400.8583,25492885.9851 6677434.3719),(25492885.9851 6677434.3719,25492900.7986 6677455.7498),CIRCULARSTRING(25492900.7986 6677455.7498,25492901.1068 6677457.3233,25492900.1601 6677458.6175,25492822.2626 6677477.2365,25492747.0232 6677449.7828),(25492747.0232 6677449.7828,25492739.7748 6677444.3615),CIRCULARSTRING(25492739.7748 6677444.3615,25492738.9731 6677442.7789,25492739.7449 6677441.1816))))'))";
        run(sql);
    }

    private void dropCurveIntersectionTable() {
        runSafe("DROP TABLE \"curveIntersection\" cascade");
    }

    @Override
    protected void createCircularStringsTable() throws Exception {
        String sql =
                "CREATE TABLE \"circularStrings\" ("
                        + "\"id\" INT, \"name\" VARCHAR, \"geometry\" geometry(CIRCULARSTRING), PRIMARY KEY(id))";
        run(sql);

        sql =
                "INSERT INTO \"circularStrings\" VALUES (0, 'Circle', ST_geometryFromText('CIRCULARSTRING(10 150, 15 145, 20 150, 15 155, 10 150)'))";
        run(sql);
    }

    @Override
    protected void createCompoundCurvesTable() throws Exception {
        String sql =
                "CREATE TABLE \"compoundCurves\" ("
                        + "\"id\" INT, \"name\" VARCHAR, \"geometry\" geometry(COMPOUNDCURVE), PRIMARY KEY(id))";
        run(sql);

        sql =
                "INSERT INTO \"compoundCurves\" VALUES (0, 'ClosedHalfCircle', ST_geometryFromText('COMPOUNDCURVE(CIRCULARSTRING(-10 0, 0 10, 10 0 ),(10 0, -10 0))'))";
        run(sql);
    }

    @Override
    protected void createCurvesTable() throws Exception {
        String sql =
                "CREATE TABLE \"curves\" ("
                        + "\"id\" INT, \"name\" VARCHAR, \"geometry\" geometry, PRIMARY KEY(id))";
        run(sql);

        sql = "CREATE INDEX CURVES_GEOMETRY_IDX ON \"curves\" USING GIST (\"geometry\") ";
        run(sql);

        // adding data
        sql =
                "INSERT INTO CURVES VALUES (0, 'Single arc', ST_geometryFromText('CIRCULARSTRING(10 15, 15 20, 20 15)'))";
        run(sql);

        sql =
                "INSERT INTO CURVES VALUES (1, 'Arc string', ST_geometryFromText('CIRCULARSTRING(10 35, 15 40, 20 35, 25 30, 30 35 )'))";
        run(sql);

        sql =
                "INSERT INTO CURVES VALUES (2, 'Compound line string', ST_geometryFromText('COMPOUNDCURVE((10 45, 20 45), "
                        + "CIRCULARSTRING(20.0 45.0, 23.0 48.0, 20.0 51.0 ), (20 51, 10 51))'))";
        run(sql);

        sql =
                "INSERT INTO CURVES VALUES (3, 'Closed mixed line', ST_geometryFromText('COMPOUNDCURVE((10 78, 10 75, 20 75, 20 78), CIRCULARSTRING(20 78, 15 80, 10 78))'))";
        run(sql);

        sql =
                "INSERT INTO CURVES VALUES (4, 'Circle', ST_geometryFromText('CURVEPOLYGON(CIRCULARSTRING(10 150, 15 145, 20 150, 15 155, 10 150))'))";
        run(sql);

        sql =
                "INSERT INTO CURVES VALUES (5, 'Compound polygon', ST_geometryFromText('CURVEPOLYGON(COMPOUNDCURVE((6 10, 10 1, 14 10), CIRCULARSTRING(14 10, 10 14, 6 10)))'))";
        run(sql);

        sql =
                "INSERT INTO CURVES VALUES (6, 'Compound polygon with hole', ST_geometryFromText('CURVEPOLYGON("
                        + "COMPOUNDCURVE((20 30, 11 30, 7 22, 7 15, 11 10, 21 10, 27 30), CIRCULARSTRING(27 30, 25 27, 20 30)), "
                        + "CIRCULARSTRING(10 17, 15 12, 20 17, 15 22, 10 17))'))";
        run(sql);

        sql =
                "INSERT INTO CURVES VALUES (7, 'Multipolygon with curves', ST_geometryFromText('MULTISURFACE(CURVEPOLYGON("
                        + "COMPOUNDCURVE((6 10, 10 1, 14 10), CIRCULARSTRING(14 10, 10 14, 6 10)), COMPOUNDCURVE((13 10, 10 2, 7 10), CIRCULARSTRING(7 10, 10 13, 13 10))), "
                        + "CURVEPOLYGON(COMPOUNDCURVE((106 110, 110 101, 114 110), CIRCULARSTRING(114 110, 110 114, 106 110))))'))";
        run(sql);

        sql =
                "INSERT INTO CURVES VALUES (8, 'Multicurve', ST_geometryFromText('MULTICURVE((0 0, 5 5),CIRCULARSTRING(4 0, 4 4, 8 4))'))";
        run(sql);

        // this one is a postgis specific extra
        sql =
                "INSERT INTO CURVES VALUES (9, 'SquareHole2Points', ST_geometryFromText('CURVEPOLYGON((-10 -10, -10 -8, -8 -8, -8 -10, -10 -10), CIRCULARSTRING(-9 -8.5, -9 -9.5, -9 -8.5))'))";
        run(sql);
    }

    @Override
    protected void dropCurvesTable() throws Exception {
        runSafe("DROP TABLE \"curves\" cascade");
    }

    @Override
    protected void dropCircularStringsTable() {
        runSafe("DROP TABLE \"circularStrings\" cascade");
    }

    @Override
    protected void dropCompoundCurvesTable() {
        runSafe("DROP TABLE \"compoundCurves\" cascade");
    }
}
