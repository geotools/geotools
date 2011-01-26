/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.sql.SQLException;

public abstract class JDBCPrimaryKeyFinderTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCPrimaryKeyFinderTestSetup(JDBCTestSetup delegate) {
        super( delegate );
    }
    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropMetadataTable();
        } catch (SQLException e) {
        }
        try {
            dropSequencedPrimaryKeyTable();
        } catch (SQLException e) {
        }
        try {
            dropAssignedSinglePkView();
        } catch (SQLException e) {
        }
        try {
            dropAssignedMultiPkView();
        } catch (SQLException e) {
        }
        try {
            dropPlainTable();
        } catch (SQLException e) {
        }
        
        //create all the data
        createMetadataTable();
        createSequencedPrimaryKeyTable();
        createPlainTable();
        createAssignedSinglePkView();
        createAssignedMultiPkView();
    }

    /**
     * Drops the metadata table
     */
    protected abstract void dropMetadataTable() throws Exception;

    /**
     * Drops the "assignedsinglepk" view.
     */
    protected abstract void dropAssignedSinglePkView() throws Exception;
    
    /**
     * Drops the "assignedmultipk" view.
     */
    protected abstract void dropAssignedMultiPkView() throws Exception;
    
    /**
     * Drops the "plaintable" table.
     */
    protected abstract void dropPlainTable() throws Exception;
    
    /**
     * Drops the "seqtable" table.
     */
    protected abstract void dropSequencedPrimaryKeyTable() throws Exception;
    

    /**
     * Creates the default geotools metadata table, empty.
     * <p>
     * The table is named "gt_pk_metdata". See {@link MetadataTablePrimaryKeyFinder} javadoc for the 
     * expected table structure
     * </p>
     * 
     */
    protected abstract void createMetadataTable() throws Exception;
    
    /**
     * Creates a table named 'plaintable' with no primary key and the following structure
     * <p>
     * plaintable( key1:Integer, key2: Integer, name:String; geom:Geometry; ) 
     * </p>
     * <p>
     * The table should be populated with the following data:
     *  1, 2, "one" | NULL
     *  2, 3, "two" | NULL
     *  3, 4, "three" | NULL
     * </p>
     */
    protected abstract void createPlainTable() throws Exception;
    
    /**
     * Creates a view named 'assignedsinglepk' that selects all of the contents of 'plaintable' and registers
     * the key1 column as an assigned primary key in the pk metadata table
     */
    protected abstract void createAssignedSinglePkView() throws Exception;
    
    /**
     * Creates a view named 'assignedmultipk' that selects all of the contents of 'plaintable' and registers
     * the key1, key2 columns as an assigned primary key in the pk metadata table
     */
    protected abstract void createAssignedMultiPkView() throws Exception;
    
    /**
     * Creates a table with a primary key column, a sequence name 'pksequence", and
     * links the two using the primary key metadata table
     * <p>
     * seqtable( name:String; geom:Geometry; ) 
     * </p>
     * <p>
     * The table should be populated with the following data:
     *  "one" | NULL ; pkey = 1
     *  "two" | NULL ; pkey = 2
     *  "three" | NULL ; pkey = 3
     * </p>
     */
    protected abstract void createSequencedPrimaryKeyTable() throws Exception;
   
}
