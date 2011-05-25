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
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.data.postgis.VersionedPostgisDataStore;

/**
 * A fid mapper factory that makes sure the revision attribute does not become part of the key
 * (since users outside are unaware of it).
 * 
 * @author aaime
 * @since 2.4
 *
 *
 * @source $URL$
 */
public class VersionedFIDMapperFactory extends DefaultFIDMapperFactory {

    Set versionedTypes = new HashSet();

    private FIDMapperFactory unversionedFactory;

    public VersionedFIDMapperFactory(FIDMapperFactory unversionedFactory) {
        super();
        this.unversionedFactory = unversionedFactory;
        returnFIDColumnsAsAttributes = true;
        returningTypedFIDMapper = false;
    }

    public void setVersionedTypes(String[] versionedTypes) {
        this.versionedTypes.clear();
        this.versionedTypes.addAll(Arrays.asList(versionedTypes));
    }

    /**
     * Gets the appropriate FIDMapper for the specified table.
     * 
     * @param catalog
     * @param schema
     * @param tableName
     * @param connection
     *            the active database connection to get table key information
     * 
     * @return the appropriate FIDMapper for the specified table.
     * 
     * @throws IOException
     *             if any error occurs.
     */
    public FIDMapper getMapper(String catalog, String schema, String tableName,
            Connection connection) throws IOException {
        // handle changesets as a special one
        if(VersionedPostgisDataStore.TBL_CHANGESETS.equals(tableName)) {
            PostGISAutoIncrementFIDMapper mapper = new PostGISAutoIncrementFIDMapper(VersionedPostgisDataStore.TBL_CHANGESETS, "revision", Types.NUMERIC, true);
            return new TypedFIDMapper(mapper, VersionedPostgisDataStore.TBL_CHANGESETS);
        }
        
        // for non versioned types we're good with the standard mappers, but we
        // must remember that versioned data store uses typed fids externally
        // (only internal ones are non typed)
        if (!versionedTypes.contains(tableName)) {
            if(tableName.endsWith("_vfc_view")) {
                try {
                    String otn = VersionedPostgisDataStore.getVFCTableName(tableName);
                    // let's see if the original feature table is there and versioned
                    VersionedFIDMapper mapper = (VersionedFIDMapper) getMapper(catalog, schema,otn, connection);
                    return new VersionedFeatureCollectionFidMapper(mapper);
//                    return new VersionedFeatureCollectionFidMapper(mapper);
                } catch(Exception e ) {
                    // ok, it wasn't a versioned feature collection view
                }
            }
            
            return unversionedFactory.getMapper(catalog, schema, tableName, connection);
        }

        ColumnInfo[] colInfos = getPkColumnInfo(catalog, schema, tableName, connection);
        if (colInfos.length <= 1)
            throw new IOException("Versioned type (" + tableName
                    + ") with a primary key with less than 2 columns,"
                    + " this cannot be, there's a error");

        // assume revision is the last column
        if (colInfos.length == 2) {
            return buildSingleColumnVersionedFidMapper(schema, tableName, connection, colInfos);
        } else {
            return buildMultiColumnFIDMapper(schema, tableName, connection, colInfos);
        }
    }

    protected FIDMapper buildSingleColumnVersionedFidMapper(String schema, String tableName,
            Connection connection, ColumnInfo[] colInfos) {
        ColumnInfo col = colInfos[1];
        if (col.isAutoIncrement() && colInfos.length == 2) {
            return new VersionedAutoincrementFIDMapper(schema, tableName, col.colName,
                    col.dataType, colInfos[0].decimalDigits);
        }  if("uuid".equals(col.getTypeName())) {
            return new VersionedUUIDFIDMapper(schema, tableName, col.getColName(), col.dataType, col.size);
        } else if (isIntegralType(col.dataType)) {
            return buildMultiColumnFIDMapper(schema, tableName, connection, colInfos);
        } else {
            return buildMultiColumnFIDMapper(schema, tableName, connection, colInfos);
        }
    }

    protected FIDMapper buildMultiColumnFIDMapper(String schema, String tableName,
            Connection connection, ColumnInfo[] colInfos) {
        String[] colNames = new String[colInfos.length];
        int[] colTypes = new int[colInfos.length];
        int[] colSizes = new int[colInfos.length];
        int[] colDecimalDigits = new int[colInfos.length];
        boolean[] autoIncrement = new boolean[colInfos.length];

        for (int i = 0; i < colInfos.length; i++) {
            ColumnInfo ci = colInfos[i];
            colNames[i] = ci.colName;
            colTypes[i] = ci.dataType;
            colSizes[i] = ci.size;
            colDecimalDigits[i] = ci.decimalDigits;
            autoIncrement[i] = ci.autoIncrement;
        }

        return new VersionedMulticolumnFIDMapper(schema, tableName, colNames, colTypes, colSizes,
                colDecimalDigits, autoIncrement);
    }
}
