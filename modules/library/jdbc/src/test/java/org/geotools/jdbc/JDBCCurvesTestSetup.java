/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

public abstract class JDBCCurvesTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCCurvesTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        dropCompoundCurvesTable();
        dropCircularStringsTable();
        dropCurvesTable();
        createCurvesTable();
        createCircularStringsTable();
        createCompoundCurvesTable();
    }

    /**
     * Creates a table named 'curves' with the following schema:
     *
     * <p>curves(name:String,geometry:Geometry)
     *
     * <p>The table has the following data:
     *
     * <ul>
     *   <li>Single Arc|CIRCULARSTRING(10 150, 15 145, 20 150, 15 155, 10 150)
     * </ul>
     */
    protected abstract void createCurvesTable() throws Exception;

    /**
     * Creates a table named 'circularStrings' with the following schema:
     *
     * <p>circularStrings(name:String,geometry:Geometry)
     *
     * <p>The table has the following data:
     *
     * <ul>
     *   <li>Circle|CIRCULARSTRING(10 150, 15 145, 20 150, 15 155, 10 150)
     * </ul>
     */
    protected abstract void createCircularStringsTable() throws Exception;

    /**
     * Creates a table named 'compoundCurves' with the following schema:
     *
     * <p>compoundCurves(name:String,geometry:Geometry)
     *
     * <p>The table has the following data (you can use more specific constructs than
     * GeometryCollection if the database supports them):
     *
     * <ul>
     *   <li>Single arc|CIRCULARSTRING (10 15, 15 20, 20 15) Arc string|CIRCULARSTRING (10 35, 15
     *       40, 20 35, 25 30, 30 35)
     *   <li>Compound line string|COMPOUNDCURVE ((10 45, 20 45), CIRCULARSTRING (20 45, 23 48, 20
     *       51), (20 51, 10 51))
     *   <li>Closed mixed line|COMPOUNDCURVE ((10 78, 10 75, 20 75, 20 78), CIRCULARSTRING (20 78,
     *       15 80, 10 78))
     *   <li>Circle|CURVEPOLYGON (CIRCULARSTRING (10 150, 15 145, 20 150, 15 155, 10 150))
     *   <li>Compound polygon|CURVEPOLYGON (COMPOUNDCURVE ((6| 10 1, 14 10), CIRCULARSTRING (14| 10
     *       14, 6 10)))
     *   <li>Compound polygon with hole|CURVEPOLYGON (COMPOUNDCURVE ((20 30, 11 30, 7 22, 7 15, 11|
     *       21| 27 30), CIRCULARSTRING (27 30, 25 27, 20 30)), CIRCULARSTRING (10 17, 15 12, 20 17,
     *       15 22, 10 17))
     *   <li>Multipolygon with curves|GEOMETRYCOLLECTION (CURVEPOLYGON (COMPOUNDCURVE ((6| 10 1, 14
     *       10), CIRCULARSTRING (14| 10 14, 6 10)), COMPOUNDCURVE ((13| 10 2, 7 10), CIRCULARSTRING
     *       (7| 10 13, 13 10))), CURVEPOLYGON (COMPOUNDCURVE ((106 110, 110 101, 114 110),
     *       CIRCULARSTRING Multicurve|GEOMETRYCOLLECTION (LINESTRING (0 0, 5 5), CIRCULARSTRING (4
     *       0, 4 4, 8 4))
     * </ul>
     */
    protected abstract void createCompoundCurvesTable() throws Exception;

    protected abstract void dropCurvesTable() throws Exception;

    protected abstract void dropCircularStringsTable() throws Exception;

    protected abstract void dropCompoundCurvesTable() throws Exception;
}
