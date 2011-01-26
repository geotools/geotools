package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class OracleDateTestSetup extends JDBCDateTestSetup {


    protected OracleDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createDateTable() throws Exception {
        run( "CREATE TABLE DATES (D DATE, DT TIMESTAMP, T TIMESTAMP)");
        
        //_date('1998/05/31:12:00:00AM', 'yyyy/mm/dd:hh:mi:ssam'));
        
        run( "INSERT INTO DATES VALUES (" +
                "TO_DATE('2009-06-28', 'yyyy-MM-dd'), " +
                "TO_DATE('2009-06-28 15:12:41', 'yyyy-MM-dd HH24:mi:ss')," +
                "TO_DATE('15:12:41', 'HH24:mi:ss')  )");
        
        run( "INSERT INTO DATES VALUES (" +
                "TO_DATE('2009-01-15', 'yyyy-MM-dd'), " +
                "TO_DATE('2009-01-15 13:10:12', 'yyyy-MM-dd HH24:mi:ss')," +
                "TO_DATE('13:10:12', 'HH24:mi:ss')  )");
        
        run( "INSERT INTO DATES VALUES (" +
                "TO_DATE('2009-09-29', 'yyyy-MM-dd'), " +
                "TO_DATE('2009-09-29 17:54:23', 'yyyy-MM-dd HH24:mi:ss')," +
                "TO_DATE('17:54:23', 'HH24:mi:ss')  )");
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE DATES");
    }

}