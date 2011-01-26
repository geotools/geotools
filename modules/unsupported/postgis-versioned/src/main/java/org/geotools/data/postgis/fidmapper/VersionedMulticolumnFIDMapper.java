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
import java.rmi.server.UID;
import java.sql.Connection;
import java.sql.Statement;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.fidmapper.MultiColumnFIDMapper;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Covers both the basic and multicolumn fid mappers
 * 
 * @author aaime
 * @since 2.4
 */
class VersionedMulticolumnFIDMapper extends MultiColumnFIDMapper implements VersionedFIDMapper {

    public VersionedMulticolumnFIDMapper(String tableSchemaName, String tableName,
            String[] colNames, int[] colTypes, int[] colSizes, int[] colDecimalDigits,
            boolean[] autoIncrement) {
        super(tableSchemaName, tableName, colNames, colTypes, colSizes, colDecimalDigits,
                autoIncrement);
        returnFIDColumnsAsAttributes = true;
    }

    public String getUnversionedFid(String versionedFID) {
        // we assume revision is the last column, since it has been added with
        // an alter table "add". Also, we make the fid "typed" to ensure WFS keeps on working
        return tableName + "." + versionedFID.substring(versionedFID.indexOf('&') + 1);
    }

    public String createVersionedFid(String externalFID, long revision) {
        return revision + "&" + externalFID.substring(tableName.length() + 1);
    }

    public Object[] getUnversionedPKAttributes(String FID) throws IOException {
        // check we can parse this
        if (!FID.startsWith(tableName + "."))
            throw new DataSourceException("The FID is invalid, should start with '" + tableName
                    + "', it's " + FID + " instead");

        // leverage superclass parsing, then throw away the last element
        Object[] values = super.getPKAttributes("0&" + FID.substring(tableName.length() + 1));
        Object[] unversioned = new Object[values.length - 1];
        System.arraycopy(values, 1, unversioned, 0, unversioned.length);
        return unversioned;
    }

    public String createID(Connection conn, SimpleFeature feature, Statement statement)
            throws IOException {
        if (colNames.length == 2 && feature.getAttribute(colNames[1]) == null) {
            try {
                feature.setAttribute(colNames[1], (new UID()).toString());
            } catch (IllegalAttributeException e) {
                throw new DataSourceException("Could not generate key for the "
                        + "unset primary key column " + colNames[0], e);
            }
        }
        return super.createID(conn, feature, statement);
    }
    
    public Object[] getPKAttributes(String FID) throws IOException {
        return super.getPKAttributes(FID);
    }

}
