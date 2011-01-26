package org.geotools.data.db2;

import java.sql.Connection;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCViewTestSetup;

public class DB2ViewTestSetup extends JDBCViewTestSetup {
    
    protected DB2ViewTestSetup() {
        super(new DB2TestSetup());
        
    }
    
    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        
        dataStore.setDatabaseSchema( null );
    }

    @Override
    protected void createLakesTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	String statement = "create table "+DB2TestUtil.SCHEMA_QUOTED+".\"lakes\""+ 
        "(\"fid\" int not null , \"id\" int, \"geom\" db2gse.st_polygon, \"name\" varchar(32), primary key (\"fid\") )";
    	con.prepareStatement(statement).execute();

        statement = "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"lakes\" (\"fid\", \"id\",\"geom\",\"name\") VALUES ( 0, 0,"
                + "db2gse.st_PolyFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',"+DB2TestUtil.SRID+"),"
                + "'muddy')";
        con.prepareStatement(statement).execute();
        
        DB2Util.executeRegister(DB2TestUtil.SCHEMA, "lakes", "geom", DB2TestUtil.SRSNAME, con);
        con.close();
    }

    @Override
    protected void dropLakesTable() throws Exception {
    	Connection con = getDataSource().getConnection();
    	DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "lakes", "geom", con);
    	DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "lakes", con);        
        con.close();        
    }

    @Override
    protected void createLakesView() throws Exception {
    	Connection con = getDataSource().getConnection();
    	
    	con.prepareStatement("create view "+DB2TestUtil.SCHEMA_QUOTED+ ".\"lakesview\" " +
    			" as select * from "+ DB2TestUtil.SCHEMA_QUOTED+ ".\"lakes\" ").execute();    	
    	DB2Util.executeRegister(DB2TestUtil.SCHEMA, "lakesview", "geom", DB2TestUtil.SRSNAME, con);
        con.close();        
    	        
    }


    @Override
    protected void dropLakesView() throws Exception {
    	Connection con = getDataSource().getConnection();
    	DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "lakesview", "geom", con);
    	DB2TestUtil.dropView(DB2TestUtil.SCHEMA, "lakesview", con);        
        con.close();        

    }

    @Override
    protected void createLakesViewPk() throws Exception {        
    }

    @Override
    protected void dropLakesViewPk() throws Exception {
        
    }

}
