package org.geotools.data.db2;

import java.sql.Connection;

import org.geotools.jdbc.JDBCDateTestSetup;

public class DB2DateTestSetup extends JDBCDateTestSetup {

    public DB2DateTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE TABLE "+DB2TestUtil.SCHEMA_QUOTED+
             ".\"dates\"(\"d\" DATE, \"dt\" TIMESTAMP, \"t\" TIME)").execute();
        
        String insertClause = "INSERT INTO "+DB2TestUtil.SCHEMA_QUOTED+".\"dates\"(\"d\",\"dt\",\"t\")";
        
        con.prepareStatement(insertClause+" VALUES (" +
              "DATE('2009-06-28'), " +
              "TIMESTAMP('2009-06-28', '15:12:41')," +
              "TIME('15:12:41')  )")
              .execute();
        
        con.prepareStatement(insertClause+" VALUES (" +
              "DATE('2009-01-15'), " +
              "TIMESTAMP('2009-01-15','13:10:12')," +
              "TIME('13:10:12')  )")
              .execute();

        con.prepareStatement(insertClause+" VALUES (" +
              "DATE('2009-09-29'), " +
              "TIMESTAMP('2009-09-29', '17:54:23')," +
              "TIME('17:54:23') )")
               .execute();
        
        
        con.close();
        
    }

    @Override
    protected void dropDateTable() throws Exception {
        Connection con = getDataSource().getConnection();               
        DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "dates", con);
        con.close();
    }

}
