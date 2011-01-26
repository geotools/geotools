package org.geotools.jdbc;

import java.sql.SQLException;

public abstract class JDBCGeographyTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCGeographyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropGeoPointTable();
        } catch (SQLException e) {
        }

        //create all the data
        if(isGeographySupportAvailable()) {
            createGeoPointTable();
        }
    }
    
    /**
     * Creates a table with the following schema:
     * <p>
     * geopoint( id:Integer; name:String; geo:Geography(Point) )
     * </p>
     * <p>
     * The table should be populated with the following data:
     * 0 | 'Town' |  POINT(-110 30)
     * 1 | 'Forest' | POINT(-109 29)
     * 2 | 'London' | POINT(0 49)
     * </p>
     */
    protected abstract void createGeoPointTable() throws Exception;
    
    protected abstract void dropGeoPointTable() throws Exception;
    
    /**
     * Subclasses should override if the database connected to the test
     * does not have geography support
     * @return
     * @throws Exception
     */
    public boolean isGeographySupportAvailable() throws Exception {
        return true;
    }
}
