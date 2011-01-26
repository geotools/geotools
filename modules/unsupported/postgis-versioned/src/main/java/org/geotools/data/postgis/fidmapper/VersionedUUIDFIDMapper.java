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
import java.sql.Statement;
import java.sql.Types;
import java.util.regex.Pattern;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.fidmapper.MultiColumnFIDMapper;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A versioned fid mapper working with UUID primary key, using a random generator
 */
class VersionedUUIDFIDMapper extends MultiColumnFIDMapper implements VersionedFIDMapper {
    protected UUIDFIDMapper wrapped;
    
    static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    public VersionedUUIDFIDMapper(String tableSchemaName, String tableName, String colName, int colType, int colSize) {
        super(tableSchemaName, tableName, new String[] { "revision", colName }, new int[] {
                colType, Types.OTHER}, new int[] { colSize, 0 }, new int[] { 0, 0 },
                new boolean[] { false, false });
        returnFIDColumnsAsAttributes = true;
        wrapped = new UUIDFIDMapper(colName, colType);
    }

    public String getUnversionedFid(String versionedFID) {
        // we assume revision is the last column, since it has been added with
        // an alter table "add". Also, we make the fid "typed" to ensure WFS keeps on working
        return tableName + "." + versionedFID.substring(versionedFID.lastIndexOf('&') + 1);
    }
    
    public String createVersionedFid(String extenalFID, long revision) {
        return revision + "&" + extenalFID.substring(tableName.length() + 1);
    }

    public Object[] getUnversionedPKAttributes(String FID) throws IOException {
        // check we can parse this
        if (!FID.startsWith(tableName + "."))
            throw new DataSourceException("The FID is invalid, should start with '" + tableName
                    + ".'");

        // leverate superclass parsing, then throw away the last element
        Object[] values = getPKAttributes(FID.substring(tableName.length() + 1) + "&0");
        Object[] unversioned = new Object[values.length - 1];
        System.arraycopy(values, 0, unversioned, 0, unversioned.length);
        return unversioned;
    }

    public String createID(Connection conn, SimpleFeature feature, Statement statement)
            throws IOException {
        if (feature.getAttribute(colNames[1]) == null) {
            try {
                String fid = feature.getID();
                String id;
                if(fid.startsWith(tableName + ".") && 
                        UUID_PATTERN.matcher(fid.substring(tableName.length() + 1)).matches()) {
                    id = fid.substring(tableName.length() + 1);
                } else {
                    id = wrapped.createID(conn, feature, statement);
                }
                feature.setAttribute(colNames[1], id);
            } catch (Exception e) {
                throw new DataSourceException("Could not generate key for the "
                        + "unset primary key column " + colNames[1], e);
            }
        }
        return super.createID(conn, feature, statement);
    }

    

}
