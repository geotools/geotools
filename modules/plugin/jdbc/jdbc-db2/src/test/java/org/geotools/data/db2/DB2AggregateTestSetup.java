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
package org.geotools.data.db2;

import java.sql.Connection;
import org.geotools.jdbc.JDBCAggregateTestSetup;

public class DB2AggregateTestSetup extends JDBCAggregateTestSetup {

    protected DB2AggregateTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement(
                        "CREATE TABLE "
                                + DB2TestUtil.SCHEMA_QUOTED
                                + ".\"aggregate\"(\"fid\" int  PRIMARY KEY not null GENERATED ALWAYS AS IDENTITY, \"id\" int, "
                                + "\"geom\" db2gse.ST_POLYGON, \"name\" varchar(255) ) ")
                .execute();

        String insertClause =
                "INSERT INTO "
                        + DB2TestUtil.SCHEMA_QUOTED
                        + ".\"aggregate\"(\"id\",\"geom\",\"name\")";
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "aggregate", "geom", DB2TestUtil.SRSNAME, con);
        con.prepareStatement(
                        insertClause
                                + " VALUES ( 0,"
                                + "db2gse.ST_PolyFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',"
                                + DB2TestUtil.SRID
                                + "),"
                                + "'muddy1')")
                .execute();
        con.prepareStatement(
                        insertClause
                                + " VALUES ( 1,"
                                + "db2gse.ST_PolyFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',"
                                + DB2TestUtil.SRID
                                + "),"
                                + "'muddy1')")
                .execute();
        con.prepareStatement(
                        insertClause
                                + " VALUES ( 2,"
                                + "db2gse.ST_PolyFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',"
                                + DB2TestUtil.SRID
                                + "),"
                                + "'muddy2')")
                .execute();
        con.close();
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        Connection con = getDataSource().getConnection();
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "aggregate", "geom", con);
        DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "aggregate", con);
        con.close();
    }
}
