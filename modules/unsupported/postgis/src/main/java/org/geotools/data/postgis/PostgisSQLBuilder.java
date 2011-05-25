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
package org.geotools.data.postgis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.jdbc.DefaultSQLBuilder;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.factory.Hints;
import org.geotools.filter.Filter;
import org.geotools.filter.SQLEncoder;
import org.geotools.filter.SQLEncoderException;
import org.geotools.filter.SQLEncoderPostgis;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Builds sql for postgis.
 *
 * @author Chris Holmes
 *
 * @source $URL$
 */
public class PostgisSQLBuilder extends DefaultSQLBuilder {
    /** If true, WKB format is used instead of WKT */
    protected boolean WKBEnabled = false;
    
    /** If true, ByteA function is used to transfer WKB data*/
    protected boolean byteaEnabled = false;

    /** If true, tables are qualified with a schema **/
    protected boolean schemaEnabled = true;
    
    /** the datastore **/
    protected JDBCDataStoreConfig config;
    
    /**
     *
     */
    public PostgisSQLBuilder(int srid, JDBCDataStoreConfig config) {
        this((SQLEncoder) new SQLEncoderPostgis(srid),config);
    }

    /**
     * Constructor with encoder.  Use PostgisSQLBuilder(encoder, config, ft) if possible.
     * 
     * @param encoder
     */
    public PostgisSQLBuilder(SQLEncoder encoder, JDBCDataStoreConfig config) {
        super(encoder);
        this.config = config;
    }
    
    public PostgisSQLBuilder(SQLEncoder encoder, JDBCDataStoreConfig config, SimpleFeatureType ft) {
    	super(encoder);
        this.config = config;
        this.ft = ft;
        encoder.setFeatureType( ft );
    }
    
    /**
     * Overrides to support offset and maxFeatures
     * @see DefaultSQLBuilder#buildSQLQuery(String, FIDMapper, AttributeDescriptor[], org.opengis.filter.Filter, SortBy[], Integer, Integer)
     */
    @Override
    public String buildSQLQuery(String typeName,
            FIDMapper mapper,
            AttributeDescriptor[] attrTypes,
            org.opengis.filter.Filter filter,
            SortBy[] sortBy,
            Integer offset,
            Integer limit) throws SQLEncoderException {
        
        if(offset != null){
            //we need to add the PK as sorting order regardless of the client asking for a specific order or not
            //so we can ensure a consistent order if the client asks for ordering over an attribute other than the PK
            List<SortBy> sortAtts = new ArrayList<SortBy>();
            if(sortBy != null){
                sortAtts.addAll(Arrays.asList(sortBy));
            }
            if(!(sortAtts.contains(SortBy.NATURAL_ORDER) || sortAtts.contains(SortBy.REVERSE_ORDER))){
                //no natural order contained in the required list, append PK ordering...
                sortAtts.add(SortBy.NATURAL_ORDER);
            }
            sortBy = sortAtts.toArray(new SortBy[sortAtts.size()]);
        }
        
        final String selectStatement = super.buildSQLQuery(typeName, mapper, attrTypes, filter, sortBy, offset, limit);
        StringBuilder sb = new StringBuilder(selectStatement);
        
        if(offset != null){
            sb.append(" OFFSET ").append(offset);
        }
        
        if(limit != null){
            sb.append(" LIMIT ").append(limit);
        }
        
        return sb.toString();
    }
    
    /**
     * Overrides to support NATURAL_ORDER and REVERSE_ORDER
     */
    @Override
    protected void addOrderByPK(StringBuffer sql, FIDMapper mapper, SortOrder sortOrder)
            throws SQLEncoderException {
        if (mapper == null || mapper.getColumnCount() == 0) {
            throw new SQLEncoderException(
                    "NATURAL_ORDER and REVERSE_ORDER is not supported without a primary key");
        }

        final String order = SortOrder.ASCENDING == sortOrder ? "ASC" : "DESC";
        String colName;
        final int columnCount = mapper.getColumnCount();
        for (int idx = 0; idx < columnCount; idx++) {
            colName = mapper.getColumnName(idx);
            sql.append(colName).append(" ").append(order);
            if (idx < columnCount - 1) {
                sql.append(", ");
            }
        }
    }

    /**
     * Produces the select information required.
     * <p>
     * The featureType, if known, is always requested.
     * </p>
     * <p>
     * sql: <code>featureID (,attributeColumn)</code>
     * </p>
     * <p>
     * We may need to provide AttributeReaders with a hook so they can request a wrapper function.
     * </p>
     * 
     * @param sql
     * @param mapper
     * @param attributes
     */
    public void sqlColumns(StringBuffer sql, FIDMapper mapper,
        AttributeDescriptor[] attributes) {
        for (int i = 0; i < mapper.getColumnCount(); i++) {
            sql.append("\""+mapper.getColumnName(i)+"\"");
            // DJB: add quotes in. NOTE: if FID mapper isnt oid (ie. PK - Primary Key), you could be
            // requesting PK columns multiple times
            if ((attributes.length > 0) || (i < (mapper.getColumnCount() - 1))) {
                sql.append(", ");
            }
        }

        for (int i = 0; i < attributes.length; i++) {
            AttributeDescriptor attribute = attributes[i];

                if (attribute instanceof GeometryDescriptor) {   
                    GeometryDescriptor geometryAttribute = (GeometryDescriptor) attribute;
                    CoordinateReferenceSystem crs = geometryAttribute.getCoordinateReferenceSystem();
                    final int D = isForce2D() ? 2 : -1;
                    
                    if (WKBEnabled) {
                        if(byteaEnabled) {
                            columnGeometryByteaWKB( sql, geometryAttribute, D );
                        } else {
                            columnGeometryWKB( sql, geometryAttribute, D );
                        }
                    } else {
                        columnGeometry( sql, geometryAttribute, D );
                    }
                } else {
                    columnAttribute(sql, attribute);
                }

                if (i < (attributes.length - 1)) {
                    sql.append(", ");
                }
            }
        }
        /** Used when WKB "ByteA" is enabled */
        private void columnGeometryByteaWKB(StringBuffer sql,
                GeometryDescriptor geometryAttribute, final int D) {
            
            sql.append("encode(");
            if( D == 2 ) {
                sql.append("asBinary(");
            } else {
                sql.append("asEWKB(");
            }
            columnGeometry(sql, geometryAttribute.getLocalName(), D );
            sql.append(",'XDR'),'base64')");
        }
        /** Used when plain WKB is enabled */   
        private void columnGeometryWKB(StringBuffer sql,
                GeometryDescriptor geometryAttribute, final int D) {
            
            if( D == 3 ){
                sql.append("asEWKB(");
            }
            else {
                sql.append("asBinary(");
            }
            columnGeometry(sql, geometryAttribute.getLocalName(), D );
            sql.append(",'XDR')");
        }
        /** Used to request a text format. */    
        private void columnGeometry(StringBuffer sql,
                GeometryDescriptor geometryAttribute, final int D) {
            if( D == 3 && !isForce2D() ){
                sql.append("asEWKT(");
            }
            else {
                sql.append("asText(");
            }
            columnGeometry(sql, geometryAttribute.getLocalName(), D );
            sql.append(")");
        }
        /**
         * Used to wrap the correct function (force_3d or force3d) around
         * the request for geometry data.
         * <p>
         * This method prevents the request of extra ordinates that will
         * not be used.
         * 
         * @see isForce2D
         * @see Hints.FEATURE_2D
         *  
         * @param sql
         * @param geomName
         * @param D
         */
        private void columnGeometry(StringBuffer sql,String geomName, final int D) {
            if (D == 2 || isForce2D() ){ 
                sql.append("force_2d(\"" + geomName + "\")");
            } else {
                sql.append("\"" + geomName + "\"" );
            }
        }
        
        private final void columnAttribute(StringBuffer sql, AttributeDescriptor attribute){
            sql.append( "\"" );
            sql.append( attribute.getLocalName() );
            sql.append( "\"" );
        }
    
    /**
     * Constructs FROM clause for featureType
     * 
     * <p>
     * sql: <code>FROM typeName</code>
     * </p>
     *
     * @param sql
     * @param typeName
     */
    public void sqlFrom(StringBuffer sql, String typeName) {
        sql.append(" FROM ");
        sql.append(encodeTableName(typeName));
    }

    /**
     * Constructs WHERE clause, if needed, for FILTER.
     * 
     * <p>
     * sql: <code>WHERE filter encoding</code>
     * </p>
     *
     * @param sql DOCUMENT ME!
     * @param preFilter DOCUMENT ME!
     *
     * @throws SQLEncoderException DOCUMENT ME!
     */
    public void sqlWhere(StringBuffer sql, Filter preFilter)
        throws SQLEncoderException {
        if ((preFilter != null) || (preFilter == org.geotools.filter.Filter.NONE)) {
            String where = encoder.encode(preFilter);
            sql.append(" ");
            sql.append(where);
        }
    }

    /**
     * Returns true if the WKB format is used to transfer geometries, false
     * otherwise
     *
     */
    public boolean isWKBEnabled() {
        return WKBEnabled;
    }

    /**
     * If turned on, WKB will be used to transfer geometry data instead of  WKT
     *
     * @param enabled
     */
    public void setWKBEnabled(boolean enabled) {
        WKBEnabled = enabled;
    }
    
    /**
     * Enables the use of the bytea function to transfer faster WKB geometries
     */
    public boolean isByteaEnabled() {
        return byteaEnabled;
    }
    /**
     * Enables/disables the use of the bytea function
     * @param byteaEnable
     */
    public void setByteaEnabled(boolean byteaEnable) {
        byteaEnabled = byteaEnable;
    }
    /**
     * Enables/disables schema name qualification.
     */
    public void setSchemaEnabled(boolean schemaEnabled) {
		this.schemaEnabled = schemaEnabled;
	}
    /**
     * @return true if table names are prefixed with the containing schema.
     */
    public boolean isSchemaEnabled() {
		return schemaEnabled;
	}
    
    public String encodeTableName(String tableName) {
    	return schemaEnabled ? 
			"\"" + config.getDatabaseSchemaName() + "\".\"" + tableName + "\"" : 
			"\"" + tableName + "\""; 
    }
    
    public String encodeColumnName(String columnName) {
    	return "\"" + columnName + "\""; 
    }
}
