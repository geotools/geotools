package org.geotools.jdbc;

import java.sql.SQLException;

public abstract class JDBCViewTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCViewTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        try {
            dropLakesView();
        } catch (SQLException e) {
        }
        
        try {
            dropLakesViewPk();
        } catch (SQLException e) {
        }
        
        try {
            dropLakesTable();
        } catch (SQLException e) {
        }
        
        //create all the data
        createLakesTable();
        createLakesView();
        createLakesViewPk();
    }
    
    /**
     * Creates a table with the following schema:
     * <p>
     * lakes(fid: Integer (pk), id:Integer; geom:Polygon; name:String )
     * </p>
     * <p>
     * The table should be populated with the following data:
     * <pre>
     * 0 | 0 | POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6));srid=4326 | "muddy"
     * </pre>
     * </p>
     */
    protected abstract void createLakesTable() throws Exception;
    
    /**
     * Creates a "lakesview" view that simply returns the lake table fully.
     * The table should be registered in the geometry metadata tables 
     */
    protected abstract void createLakesView() throws Exception;
    
    /**
     * If the database supports views with primary keys, creates a "lakesviewpk" view 
     * that simply returns the lake table fully and makes sure the original pk
     * is registered as a pk in the view as well.
     * The table should be registered in the geometry metadata tables.
     * If no primary key on views support is available, the method can be empty
     */
    protected abstract void createLakesViewPk() throws Exception;
    
    /**
     * Drops the "lakes" table.
     */
    protected abstract void dropLakesTable() throws Exception;
    
    /**
     * Drops the "lakesview" view.
     */
    protected abstract void dropLakesView() throws Exception;
    
    /**
     * Drops the "lakesviewpk" view
     */
    protected abstract void dropLakesViewPk() throws Exception;

}
