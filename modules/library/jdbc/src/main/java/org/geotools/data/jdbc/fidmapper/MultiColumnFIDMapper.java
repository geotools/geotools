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
 * Created on 18-apr-2004
 * 12-jul-2006 D. Adler GEOT-728 Refactor FIDMapper classes
 */
package org.geotools.data.jdbc.fidmapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Statement;

import org.geotools.data.DataSourceException;
import org.opengis.feature.simple.SimpleFeature;


/**
 * A simple implementation of FIDMapper for multi column primary keys
 *
 * @author wolf
 * @source $URL$
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class MultiColumnFIDMapper extends AbstractFIDMapper {
    private static final long serialVersionUID = 1L;
    private static final String UTF8 = "UTF-8";
    
    /**
     * Builds a new instance of the MultiColumnFIDMapper
     *
     * @param tableSchemaName
     * @param tableName
     * @param colNames - column names
     * @param colTypes - column types, see {@link java.sql.Types}
     * @param colSizes - column sizes
     * @param colDecimalDigits - column decimals
     * @param autoIncrement - flags for auto-increment tests
     *
     * @throws IllegalArgumentException
     */
    public MultiColumnFIDMapper(String tableSchemaName, String tableName, 
    		String[] colNames, int[] colTypes,
			int[] colSizes, int[] colDecimalDigits, boolean[] autoIncrement) {
    	super(tableSchemaName, tableName);
        if ((colNames == null) || (colTypes == null) || (autoIncrement == null)) {
            throw new IllegalArgumentException(
                "Column description arrays must be not null");
        }

        if (colNames.length == 0) {
            throw new IllegalArgumentException(
                "Column description arrays must be not empty");
        }

        if ((colNames.length != colTypes.length)
                || (colNames.length != autoIncrement.length)) {
            throw new IllegalArgumentException(
                "Column description arrays must have the same size");
        }

        this.colNames = colNames;
        this.colTypes = colTypes;
        this.colSizes = colSizes;
        this.colDecimalDigits = colDecimalDigits;
        this.autoIncrement = autoIncrement;
        this.returnFIDColumnsAsAttributes = true;
    }

    /**
     * Builds a new instance of the MultiColumnFIDMapper
     *
     * @param colNames - column names
     * @param colTypes - column types, see {@link java.sql.Types}
     * @param colSizes - column sizes
     * @param colDecimalDigits - column decimals
     * @param autoIncrement - flags for auto-increment tests
     *
     * @throws IllegalArgumentException
     */
    public MultiColumnFIDMapper(String[] colNames, int[] colTypes,
        int[] colSizes, int[] colDecimalDigits, boolean[] autoIncrement) {
    	this(null, null, colNames, colTypes, colSizes, colDecimalDigits,
    			autoIncrement);
    }



    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getID(java.lang.Object[])
     */
    public String getID(Object[] attributes) {
        StringBuffer sb = new StringBuffer();

        try {
            for (int i = 0; i < attributes.length; i++) {
                sb.append(URLEncoder.encode(attributes[i].toString(), UTF8));

                if (i < (attributes.length - 1)) {
                    sb.append("&");
                }
            }
        } catch (UnsupportedEncodingException e) {
            // c'mon, don't tell me UTF-8 is not supported ;-)
        }

        return sb.toString();
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getPKAttributes(java.lang.String)
     */
    public Object[] getPKAttributes(String FID) throws IOException {
        String[] attributes = FID.split("&");

        if (attributes.length != colNames.length) {
            throw new DataSourceException(
                "The FID is not compatible with MultiColumnFIDMapper, was expecting "
                + colNames.length + " URL-encoded columns and got "
                + attributes.length + " columns");
        }

        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = URLDecoder.decode(attributes[i], UTF8);
        }

        return attributes;
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#createID(java.sql.Connection,
     *      org.geotools.feature.Feature, Statement)
     */
    public String createID(Connection conn, SimpleFeature feature, Statement statement)
        throws IOException {
        String[] attValues = new String[colNames.length];

        for (int i = 0; i < colNames.length; i++) {
            attValues[i] = feature.getAttribute(colNames[i]).toString();
        }

        return getID(attValues);
    }

    /**
     * @return {@code true} if splitting fid by the same separator than
     *         {@link #getPKAttributes(String)} results in the same number of strings than the
     *         number of columns this FIDMapper uses to compose a fid
     * @see FIDMapper#isValid(String)
     */
    public boolean isValid(String fid) {
        String[] attributes = fid.split("&");
        return attributes.length == colNames.length;
    }

}
