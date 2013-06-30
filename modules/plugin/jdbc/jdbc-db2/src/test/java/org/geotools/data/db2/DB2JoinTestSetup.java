/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCJoinTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class DB2JoinTestSetup extends JDBCJoinTestSetup {

    public DB2JoinTestSetup() {
        super(new DB2TestSetup());
    }
    
    @Override
    protected void createJoinTable() throws Exception {
                
        Connection con = getDataSource().getConnection();
        
        String stmt = "create table "+DB2TestUtil.SCHEMA_QUOTED+
                        ".\"ftjoin\" (\"id\" int ," +
                        "\"name\" varchar(255), " +
                        " \"geom\" DB2GSE.ST_GEOMETRY, \"join1intProperty\" INT ) ";
                        
        con.prepareStatement(stmt    ).execute();    
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "ftjoin", "geom", DB2TestUtil.SRSNAME, con);
        
        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\" (\"id\",\"name\", \"geom\" , \"join1intProperty\")  " +                        
                "VALUES (0, 'zero',db2gse.st_GeomFromText('POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))', "+DB2TestUtil.SRID+"),0)" )
                .execute();
        
        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\" (\"id\",\"name\", \"geom\", \"join1intProperty\")  " +                        
                "VALUES (1, 'one',db2gse.st_GeomFromText('POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))', "+DB2TestUtil.SRID+"),1)" )
                .execute();

        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\" (\"id\",\"name\", \"geom\", \"join1intProperty\")  " +                        
                "VALUES (2, 'two',db2gse.st_GeomFromText('POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))', "+DB2TestUtil.SRID+"),2)" )
                .execute();

        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\" (\"id\",\"name\", \"geom\", \"join1intProperty\")  " +                        
                "VALUES (3, 'three',NULL,3)" )
                .execute();

        stmt = "create table "+DB2TestUtil.SCHEMA_QUOTED+
                ".\"ftjoin2\" (\"id\" int ," +
                "\"join2intProperty\" int, " +
                "\"stringProperty2\" VARCHAR(255)) ";
                
                
        con.prepareStatement(stmt    ).execute();
        
        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin2\" (\"id\",\"join2intProperty\", \"stringProperty2\")  " +                        
                "VALUES (0, 0, '2nd zero')" )
                .execute();
        
        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin2\" (\"id\",\"join2intProperty\", \"stringProperty2\")  " +                        
                "VALUES (1, 1, '2nd one')" )
                .execute();
        
        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin2\" (\"id\",\"join2intProperty\", \"stringProperty2\")  " +                        
                "VALUES (2, 2, '2nd two')" )
                .execute();

        con.prepareStatement( 
                "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin2\" (\"id\",\"join2intProperty\", \"stringProperty2\")  " +                        
                "VALUES (3, 3, '2nd three')" )
                .execute();


        
//        run( "CREATE TABLE ftjoin2( id INT, join2intProperty INT, stringProperty2 VARCHAR(255))");
//        run( "INSERT INTO ftjoin2 VALUES (0, 0, '2nd zero')");
//        run( "INSERT INTO ftjoin2 VALUES (1, 1, '2nd one')");
//        run( "INSERT INTO ftjoin2 VALUES (2, 2, '2nd two')");
//        run( "INSERT INTO ftjoin2 VALUES (3, 3, '2nd three')");

        
        con.close();
        
        
        
    }

    @Override
    protected void dropJoinTable() throws Exception {
        Connection con = getDataSource().getConnection();
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "ftjoin", "geom",  con);
        con.prepareStatement("DROP TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin\"").execute();
        con.prepareStatement("DROP TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"ftjoin2\"").execute();
        
        con.close();
        
        
        
    }

}
