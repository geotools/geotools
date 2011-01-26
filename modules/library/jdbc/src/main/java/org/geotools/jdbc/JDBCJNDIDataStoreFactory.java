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

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.geotools.data.DataStore;
import org.geotools.factory.GeoTools;

/**
 * Abstract implementation of DataStoreFactory for jdbc datastores which obtain
 * a JNDI connection.
 * <p>
 * Subclasses should not need to override any methods, only just call the 
 * parent constructor passing in the non JNDI datastore factory to delegate to. 
 * </p>
 * 
 * @author Christian Mueller
 *
 * @source $URL$
 */
public abstract class JDBCJNDIDataStoreFactory extends JDBCDataStoreFactory {

    
    public final static String J2EERootContext="java:comp/env/";
    /**
     * JNDI data source name
     */
    public static final Param JNDI_REFNAME = new Param("jndiReferenceName", String.class,
            "JNDI data source", true, J2EERootContext+"jdbc/mydatabase");

    /**
     * regular datastore factory to delegate to.
     */
    protected JDBCDataStoreFactory delegate;
    
    protected JDBCJNDIDataStoreFactory(JDBCDataStoreFactory delegate) {
        this.delegate = delegate;
    }
    
    /**
     * Override which explicitly returns null because with a JNDI connection
     * the driver is not known ahead of time.
     */
    @Override
    protected String getDriverClassName() {
        return null;
    }

    /**
     * Override which explicitly returns null, validation queries are 
     * not supported, my be part of the external data source configuration
     */
    @Override
    protected String getValidationQuery() {
        return null;
    }
    
    /**
     * Override which explicitly returns null since there is no jdbc url, the 
     * connection is identified by the JNDI name.
     */
    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        return null;
    }

    /**
     * Override to create the datasource from the external JNDI conection.
     */
    protected DataSource createDataSource(Map params, SQLDialect dialect) throws IOException {
        String jndiName = (String) JNDI_REFNAME.lookUp(params);
        if (jndiName == null)
            throw new IOException("Missing " + JNDI_REFNAME.description);

        Context ctx = null;
        DataSource ds = null;        
        
        try {
            ctx = GeoTools.getInitialContext(GeoTools.getDefaultHints());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
            
        try {    
            ds = (DataSource) ctx.lookup(jndiName);
        } catch (NamingException e1) {
            // check if the user did not specify "java:comp/env" 
            // and this code is running in a J2EE environment
            try {
                if (jndiName.startsWith(J2EERootContext)==false)  {
                    ds = (DataSource) ctx.lookup(J2EERootContext+jndiName);
                    // success --> issue a waring
                    Logger.getLogger(this.getClass().getName()).log(
                		Level.WARNING,"Using "+J2EERootContext+jndiName+" instead of " +
                		jndiName+ " would avoid an unnecessary JNDI lookup");
                }    
            } catch (NamingException e2) {
                // do nothing, was only a try
            }                            
        }    
        
        if (ds == null)
            throw new IOException("Cannot find JNDI data source: " + jndiName);
        else
            return ds;
    }

    /**
     * Determines if the datastore is available.
     * <p>
     * Check in an Initial Context is available, that is all what can be done
     * Checking for the right jdbc jars in the classpath is not possible here 
     * </p>
     */
    public boolean isAvailable() {
        try {
            GeoTools.getInitialContext(GeoTools.getDefaultHints());
            return true;
        } catch (NamingException e) {
            return false;
        }
    }

    /**
     * Override to omit all those parameters which define the creation of 
     * the connection.
     */
    protected void setupParameters(Map parameters) {
        parameters.put(DBTYPE.key, new Param(DBTYPE.key, DBTYPE.type, DBTYPE.description,
                DBTYPE.required, getDatabaseID()));
        parameters.put(JNDI_REFNAME.key, JNDI_REFNAME);
        parameters.put(SCHEMA.key, SCHEMA);
        parameters.put(NAMESPACE.key, NAMESPACE);
        parameters.put(EXPOSE_PK.key, EXPOSE_PK);
        parameters.put(PK_METADATA_TABLE.key, PK_METADATA_TABLE);

    }

    @Override
    protected String getDatabaseID() {
        return delegate.getDatabaseID();
    }

    @Override
    public String getDisplayName() {
        return delegate.getDisplayName() + " (JNDI)";
    }
    
    public String getDescription() {
        return delegate.getDescription() + " (JNDI)";
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map params)
            throws IOException {
        return delegate.createDataStoreInternal(dataStore, params);
    }

    public DataStore createNewDataStore(Map params) throws IOException {
        return delegate.createNewDataStore(params);
    }

    public Map getImplementationHints() {
        return delegate.getImplementationHints();
    }
    
    @Override
    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
        return delegate.createSQLDialect(dataStore);
    }
    
    @Override
    protected boolean checkDBType(Map params) {
        return delegate.checkDBType(params);
    }
    
}
