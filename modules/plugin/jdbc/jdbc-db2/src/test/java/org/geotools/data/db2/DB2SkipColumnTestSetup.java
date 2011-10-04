package org.geotools.data.db2;

import java.sql.Connection;

import org.geotools.jdbc.JDBCSkipColumnTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class DB2SkipColumnTestSetup extends JDBCSkipColumnTestSetup {

    protected DB2SkipColumnTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createSkipColumnTable() throws Exception {
        Connection con = getDataSource().getConnection();
        
        String stmt = "create table "+DB2TestUtil.SCHEMA_QUOTED+
                        ".\"skipcolumn\" (\"fid\" int generated always as identity (start with 0, increment by 1)," +
                        "\"id\" int ,"+
                        " \"geom\" DB2GSE.ST_GEOMETRY, " +
                        "\"weirdproperty\" XML, " +
                        "\"name\" varchar(255), " +
                        "primary key (\"fid\"))";
        con.prepareStatement(stmt    ).execute();    
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "auto", "geom", DB2TestUtil.SRSNAME, con);
        
        con.prepareStatement( "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"skipcolumn\" " +
        		"(\"id\",\"geom\",\"weirdproperty\",\"name\")  " +
        		"VALUES (0, db2gse.st_GeomFromText('POINT(0 0)', "+DB2TestUtil.SRID+"), null, 'GeoTools')" ).execute();        
        con.close();
        

    }

    @Override
    protected void dropSkipColumnTable() throws Exception {
        Connection con = getDataSource().getConnection();
        DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "skipcolumn", "geom",  con);
        con.prepareStatement("DROP TABLE "+DB2TestUtil.SCHEMA_QUOTED+".\"skipcolumn\"").execute();
        con.close();
    }

}
