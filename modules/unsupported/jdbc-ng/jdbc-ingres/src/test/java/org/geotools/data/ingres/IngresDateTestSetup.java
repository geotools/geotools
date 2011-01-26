package org.geotools.data.ingres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class IngresDateTestSetup extends JDBCDateTestSetup {


    public IngresDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createDateTable() throws Exception {
        Connection con = getDataSource().getConnection();
        con.prepareStatement("CREATE TABLE DATES (D ANSIDATE, DT TIMESTAMP, T TIME)").execute();
        
        PreparedStatement ps = con.prepareStatement("INSERT INTO DATES VALUES (?,?,?)");
        ps.setDate(1, java.sql.Date.valueOf("2009-06-28"));
        //ps.setTimestamp(2, java.sql.Timestamp.valueOf("2009-06-28 15:12:41.0"));
        ps.setTimestamp(2,  new java.sql.Timestamp(new java.text.SimpleDateFormat
        		("HH:mm:ss,dd-yyyy-MM").parse("15:12:41,28-2009-06").getTime()));
        ps.setTime(3, java.sql.Time.valueOf("15:12:41"));
        ps.execute();
        ps.setDate(1, java.sql.Date.valueOf("2009-01-15"));
 //       ps.setTimestamp(2, java.sql.Timestamp.valueOf("2009-01-15 13:10:12.0"));
        ps.setTimestamp(2,  new java.sql.Timestamp(new java.text.SimpleDateFormat
        		("HH:mm:ss,dd-yyyy-MM").parse("13:10:12,15-2009-01").getTime()));
        ps.setTime(3, java.sql.Time.valueOf("13:10:12"));
        ps.execute();
        ps.setDate(1, java.sql.Date.valueOf("2009-09-29"));
 //       ps.setTimestamp(2, java.sql.Timestamp.valueOf("2009-09-29 17:54:23.0"));
        ps.setTimestamp(2,  new java.sql.Timestamp(new java.text.SimpleDateFormat
        		("HH:mm:ss,dd-yyyy-MM").parse("17:54:23,29-2009-09").getTime()));
        ps.setTime(3, java.sql.Time.valueOf("17:54:23"));
        ps.execute();
        ps.close();
        con.close();               
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE DATES");
    }

}