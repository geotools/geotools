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
package org.geotools.data.postgis.fidmapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.jdbc.fidmapper.AbstractFIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.opengis.feature.simple.SimpleFeature;
import org.postgresql.PGStatement;


/**
 * Supports the creation of a Feature ID based on the Potgres row OID field.
 * <p>
 * This is <b>NOT</b> a stable approach for FID (as updates and so on will change the
 * OID), but it will be our best guess in the case of read only access where an
 * index is not present.
 * 
 * @author wolf
 *
 * @source $URL$
 */
public class OIDFidMapper extends AbstractFIDMapper {

    /** <code>serialVersionUID</code> field */
    private static final long serialVersionUID = 3257569520561763632L;

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#initSupportStructures()
     */
    public void initSupportStructures() {
        // nothing to do, oid are supported in the table or not depending on 
        // database configuration and table creation commands (not sure)
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getID(java.lang.Object[])
     */
    public String getID(Object[] attributes) {
        return attributes[0].toString();
    }

    /**
     * Will always return an emtpy array since OIDs are not updatable, 
     * so we don't try to parse the Feature ID at all.
     * Um - this causes failures in SQLEncoder - that may be the place
     * to fix it, but I'm putting it in here for now.  I believe that
     * the oid will not try to get updated since auto increment
     * is set to false.
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getPKAttributes(java.lang.String)
     */
    public Object[] getPKAttributes(String FID) throws IOException {
	try {
	    return new Object[] { new Long(Long.parseLong(FID)) };
	} catch (NumberFormatException nfe) {
	    //if we get a really bad featureid we want to return something
	    //that will not mess up the database and throw an exception,
	    //we just want to not match against it, so we return -1
	    return new Object[] { new Integer(-1) };
	}
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#createID(java.sql.Connection, org.geotools.feature.Feature, Statement)
     */
    public String createID(Connection conn, SimpleFeature feature, Statement statement) throws IOException {
        try {
            if(!(statement instanceof PGStatement)) {
                UnWrapper uw = DataSourceFinder.getUnWrapper(statement);
                if(uw != null)
                    statement = uw.unwrap(statement);
            }
            PGStatement pgStatement = (PGStatement) statement;
            return String.valueOf(pgStatement.getLastOID());
        } catch (SQLException e) {
            throw new DataSourceException("Problems occurred while getting last generate oid from Postgresql statement", e);
        } catch (ClassCastException e) {
            throw new DataSourceException("Statement is not a PGStatement. OIDFidMapper can be used only with Postgres!", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#returnFIDColumnsAsAttributes()
     */
    public boolean returnFIDColumnsAsAttributes() {
        return false;
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnCount()
     */
    public int getColumnCount() {
        return 1;
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnName(int)
     */
    public String getColumnName(int colIndex) {
        return "oid";
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnType(int)
     */
    public int getColumnType(int colIndex) {
        return Types.NUMERIC;
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnSize(int)
     */
    public int getColumnSize(int colIndex) {
        return 8;
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnDecimalDigits(int)
     */
    public int getColumnDecimalDigits(int colIndex) {
        return 0;
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#isAutoIncrement(int)
     */
    public boolean isAutoIncrement(int colIndex) {
        return true;
    }

    /**
     * @return {@code true} if fid can be parsed to a long, as OID's are longs
     * @see FIDMapper#isValid(String)
     */
    public boolean isValid(String fid) {
        try{
            Long.parseLong(fid, 10);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

}
