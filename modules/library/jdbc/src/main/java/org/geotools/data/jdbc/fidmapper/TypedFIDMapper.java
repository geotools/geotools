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
package org.geotools.data.jdbc.fidmapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import org.opengis.feature.simple.SimpleFeature;


/**
 * This fidmapper just takes another fid mapper and builds fids  based on the
 * wrapped FIDMapper by prefixing them with the feature type name, that is,
 * the resulting fid follow the &ltfeatureTypeName&gt.&ltbasic_fid&gt pattern.
 *
 * @author wolf
 * @source $URL$
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class TypedFIDMapper extends AbstractFIDMapper {
    private static final long serialVersionUID = 1L;
    private String featureTypeName;
    private FIDMapper wrappedMapper;

    /**
     * Creates a new TypedFIDMapper object.
     *
     * @param wrapped
     * @param featureTypeName
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public TypedFIDMapper(FIDMapper wrapped, String featureTypeName) {
    	super(null, null);
        if (wrapped == null) {
            throw new IllegalArgumentException(
                "The wrapped feature mapper cannot be null");
        }

        if (featureTypeName == null) {
            throw new IllegalArgumentException(
                "The featureTypeName cannot be null");
        }

        this.wrappedMapper = wrapped;
        this.featureTypeName = featureTypeName;
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getID(java.lang.Object[])
     */
    public String getID(Object[] attributes) {
        return featureTypeName + "." + wrappedMapper.getID(attributes);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getPKAttributes(java.lang.String)
     */
    public Object[] getPKAttributes(String FID) throws IOException {
        int pos = FID.indexOf(".");

        return wrappedMapper.getPKAttributes(FID.substring(pos + 1));
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#returnFIDColumnsAsAttributes()
     */
    public boolean returnFIDColumnsAsAttributes() {
        return wrappedMapper.returnFIDColumnsAsAttributes();
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnCount()
     */
    public int getColumnCount() {
        return wrappedMapper.getColumnCount();
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnName(int)
     */
    public String getColumnName(int colIndex) {
        return wrappedMapper.getColumnName(colIndex);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnType(int)
     */
    public int getColumnType(int colIndex) {
        return wrappedMapper.getColumnType(colIndex);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnSize(int)
     */
    public int getColumnSize(int colIndex) {
        return wrappedMapper.getColumnSize(colIndex);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getColumnDecimalDigits(int)
     */
    public int getColumnDecimalDigits(int colIndex) {
        return wrappedMapper.getColumnSize(colIndex);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#isAutoIncrement(int)
     */
    public boolean isAutoIncrement(int colIndex) {
        return wrappedMapper.isAutoIncrement(colIndex);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
        if (!(object instanceof TypedFIDMapper)) {
            return false;
        }

        TypedFIDMapper other = (TypedFIDMapper) object;

        return other.wrappedMapper.equals(wrappedMapper)
        && (other.featureTypeName == featureTypeName);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#createID(java.sql.Connection,
     *      org.geotools.feature.Feature, Statement)
     */
    public String createID(Connection conn, SimpleFeature feature, Statement statement)
        throws IOException {
        return featureTypeName + "."
        + wrappedMapper.createID(conn, feature, statement);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#initSupportStructures()
     */
    public void initSupportStructures() {
        wrappedMapper.initSupportStructures();
    }

    /**
     * Returns the base mapper wrapped by this TypedFIDMapper
     *
     */
    public FIDMapper getWrappedMapper() {
        return wrappedMapper;
    }
    
    public String toString() {
    	return "Wrapped:" + getWrappedMapper().toString();
    }
    public String getTableName() {
    	return ((AbstractFIDMapper)getWrappedMapper()).getTableName();
    }
    public String getTableSchemaName() {
    	return ((AbstractFIDMapper)getWrappedMapper()).getTableSchemaName();
    }
    
    public boolean isVolatile() {
        return wrappedMapper.isVolatile();
    }

    /**
     * @see FIDMapper#isValid(String)
     */
    public boolean isValid(String fid) {
        if (!fid.startsWith(this.featureTypeName + ".")) {
            return false;
        }
        if (fid.length() < this.featureTypeName.length() + 1) {
            return false;
        }
        String wrappedFid = fid.substring(this.featureTypeName.length() + 1);
        return wrappedMapper.isValid(wrappedFid);
    }
}
