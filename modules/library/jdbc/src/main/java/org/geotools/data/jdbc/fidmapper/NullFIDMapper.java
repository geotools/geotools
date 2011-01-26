/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
/*
 * 17-Jul-2006 D. Adler GEOT-728 Refactor FIDMapper classes
 */
package org.geotools.data.jdbc.fidmapper;

import java.io.IOException;
import java.rmi.server.UID;
import java.sql.Connection;
import java.sql.Statement;

import org.opengis.feature.simple.SimpleFeature;


/**
 * Last resort fid mapper for tables that does not have a primary key. It
 * allows reading the table getting unique FIDs by using the same mechanism
 * used by DefaultFeature, but the same Feature will receive a different FID
 * each time it is loaded from the datastore.
 *
 * @author wolf
 * @source $URL$
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class NullFIDMapper extends AbstractFIDMapper {
    private static final long serialVersionUID = 1L;
    private static final String ARRAY_OUT_OF_BOUND_MESSAGE = "There are no columns in this FIDMapper";

    public NullFIDMapper() {
    	super(null, null);
    }
    /** 
     * Constructor to set schema and table name for Null mapper.
     * 
     * @param tableSchemaName
     * @param tableName
     */
    public NullFIDMapper(String tableSchemaName, String tableName) {
    	super(tableSchemaName, tableName);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getID(java.lang.Object[])
     */
    public String getID(Object[] attributes) {
        // optimization, since the UID toString uses only ":" and converts long and integers
        // to strings for the rest, so the only non word character is really ":"
        return "nfm-" + new UID().toString().replace(':', '_');
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getPKAttributes(java.lang.String)
     */
    public Object[] getPKAttributes(String FID) throws IOException {
        return new Object[0];
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#createID(java.sql.Connection,
     *      org.geotools.feature.Feature, Statement)
     */
    public String createID(Connection conn, SimpleFeature feature, Statement statement)
        throws IOException {
        return getID(null);
    }

 
    /**
     * This FID mappers generates unique IDs out of the blue using {@link UID
     * UID}
     *
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#isVolatile()
     */
    public boolean isVolatile() {
        return true;
    }

    /**
     * @return {@code true} always
     * @see FIDMapper#isValid(String)
     */
    public boolean isValid(String fid) {
        return true;
    }
}
