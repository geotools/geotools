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

import org.opengis.feature.simple.SimpleFeature;

/**
 * A custom fid mapper to be used for versioned feature collections
 * @author Andrea Aime - TOPP
 */
class VersionedFeatureCollectionFidMapper implements VersionedFIDMapper {
    VersionedFIDMapper wrapped;
    
    public VersionedFeatureCollectionFidMapper(VersionedFIDMapper wrapped) {
        this.wrapped = wrapped;
    }

    public String createID(Connection conn, SimpleFeature feature, Statement statement)
            throws IOException {
        throw new UnsupportedOperationException("Read only fid mapper");
    }

    public int getColumnCount() {
        return wrapped.getColumnCount();
    }

    public int getColumnDecimalDigits(int colIndex) {
        return wrapped.getColumnDecimalDigits(colIndex);
    }

    public String getColumnName(int colIndex) {
        return wrapped.getColumnName(colIndex);
    }

    public int getColumnSize(int colIndex) {
        return wrapped.getColumnSize(colIndex);
    }

    public int getColumnType(int colIndex) {
        return wrapped.getColumnType(colIndex);
    }

    public String getID(Object[] attributes) {
        return wrapped.getUnversionedFid(wrapped.getID(attributes));
    }

    public Object[] getPKAttributes(String FID) throws IOException {
        return wrapped.getPKAttributes(FID);
    }

    public boolean hasAutoIncrementColumns() {
        return wrapped.hasAutoIncrementColumns();
    }

    public void initSupportStructures() {
        wrapped.initSupportStructures();
    }

    public boolean isAutoIncrement(int colIndex) {
        return wrapped.isAutoIncrement(colIndex);
    }

    public boolean isVolatile() {
        return wrapped.isVolatile();
    }

    public boolean returnFIDColumnsAsAttributes() {
        return wrapped.returnFIDColumnsAsAttributes();
    }

    public String createVersionedFid(String extenalFID, long revision) {
        return wrapped.createVersionedFid(extenalFID, revision);
    }

    public String getUnversionedFid(String versionedFID) {
        return wrapped.getUnversionedFid(versionedFID);
    }

    public Object[] getUnversionedPKAttributes(String FID) throws IOException {
        return wrapped.getUnversionedPKAttributes(FID);
    }

    public boolean isValid(String fid) {
        return wrapped.isValid(fid);
    }

}
