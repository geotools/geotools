package org.geotools.jdbc;

public abstract class JDBCDateTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        try {
            dropDateTable();
        }
        catch( Exception e ) {}
        
        createDateTable();
    }
    
    /**
     * Creates a table named 'dates' which has a variety of temporal 
     * attributes, with the following schema:
     * <p>
     * dates(d:Date,dt:Datetime,t:Time) 
     * </p>
     * <p>
     * The table has the following data:
     *
     *  2009-06-28 | 2009-06-28 15:12:41 | 15:12:41
     *  2009-01-15 | 2009-01-15 13:10:12 | 13:10:12
     *  2009-09-29 | 2009-09-29 17:54:23 | 17:54:23
     * </p>
     *  </p>
     */
    protected abstract void createDateTable() throws Exception;
    
    /**
     * Drops the 'date' table.
     */
    protected abstract void dropDateTable() throws Exception; 
}
