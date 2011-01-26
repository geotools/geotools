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
 *
 */
package org.geotools.data.oracle;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.filter.Filter;
import org.geotools.filter.SQLEncoder;
import org.geotools.filter.SQLEncoderException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * Provides SQL encoding functions for the Oracle Datasource
 *
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @author $Author: seangeo $
 * @source $URL$
 * @version $Id$ Last Modified: $Date: 2003/11/05 00:53:37 $
 */
final class SqlStatementEncoder {
    /** A logger for logging */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.oracle");
    /** SQL Where clause encoder */
    private SQLEncoder whereEncoder;
    /** FID column of the table */
    private String fidColumn;
    /** Name of the table */
    private String tableName;

    // Copied from JDBCDataStore
	protected static final TypeMapping TYPE_MAPPINGS = new TypeMapping();
	static {
		TYPE_MAPPINGS.put("CHAR", String.class);
		TYPE_MAPPINGS.put("LONGVARCHAR", String.class);
		TYPE_MAPPINGS.put("VARCHAR", String.class);

		TYPE_MAPPINGS.put("BIT", Boolean.class);
		TYPE_MAPPINGS.put("BOOLEAN", Boolean.class);

		TYPE_MAPPINGS.put("TINYINT", Short.class);
		TYPE_MAPPINGS.put("SMALLINT", Short.class);

		TYPE_MAPPINGS.put("INTEGER", Integer.class);
		TYPE_MAPPINGS.put("BIGINT", Long.class);

		TYPE_MAPPINGS.put("REAL", Float.class);
		TYPE_MAPPINGS.put("FLOAT", Double.class);
		TYPE_MAPPINGS.put("DOUBLE", Double.class);

		TYPE_MAPPINGS.put("DECIMAL", BigDecimal.class);
		TYPE_MAPPINGS.put("NUMERIC", BigDecimal.class);

		TYPE_MAPPINGS.put("DATE", java.sql.Date.class);
		TYPE_MAPPINGS.put("TIME", java.sql.Time.class);
		TYPE_MAPPINGS.put("TIMESTAMP",
				java.sql.Timestamp.class);
		TYPE_MAPPINGS.put("MDSYS.SDO_GEOMETRY", Geometry.class );
	}
	static class TypeMapping {
		Map intMap = new HashMap();
		Map sqlMap = new HashMap();
		Map typeMap = new HashMap();
		
		public void put( String name, Class javaType ){
			if( name.indexOf(".") == -1 && name.indexOf("_") == -1 ){
				try {
					Field field = Types.class.getField( name );
					Integer integer = (Integer) field.get( null );			
					intMap.put( integer, javaType );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sqlMap.put( name, javaType );
			typeMap.put( javaType, name );
		}
		public String getName( Class type ){
			for( Iterator i=typeMap.keySet().iterator(); i.hasNext(); ){
				Class t = (Class) i.next();
				if( t == type ){
					return (String) typeMap.get( t );
				}
			}
			// okay now consider inheritance
			for( Iterator i=typeMap.keySet().iterator(); i.hasNext(); ){
				Class t = (Class) i.next();
				if( t.isAssignableFrom( type )){
					return (String) typeMap.get( t );					
				}
			}			
			return "NIL";
		}
	};
    /**
     * Creates a new SQL Statement encoder.
     *
     * @param whereEncoder This in the encoder used for where clauses.
     * @param tablename This the table name to use in SQL statements.
     * @param fidColumn The fid column for the table.
     */
    SqlStatementEncoder(SQLEncoder whereEncoder, String tablename, String fidColumn) {
        this.whereEncoder = whereEncoder;
        this.tableName = tablename;
        this.fidColumn = fidColumn;
    }

    /**
     * Creates a table for the provided schema.
     * <p>
     * CREATE TABLE tableName (fidColumn int, att1 type1, att2 type2, .... )
     * </p>
     * <p>
     * You should consider calling makeCreateIndexSQL to set up for fid based
     * indexing, and we should have something for spatial indexes.
     * </p>
     * 
     * @param schema
     * @return SQL used to create the table
     */
    String makeCreateTableSQL(SimpleFeatureType schema ){
    	StringBuffer sql = new StringBuffer("CREATE TABLE ");
        sql.append(tableName);
        sql.append("(");
        sql.append(fidColumn);
        sql.append(" NUMBER,");

        AttributeDescriptor[] attributeTypes = schema.getAttributeDescriptors().toArray(new AttributeDescriptor[schema.getAttributeDescriptors().size()]);

        for (int i = 0; i < attributeTypes.length; i++) {
            sql.append(attributeTypes[i].getLocalName());
            sql.append(" ");
            sql.append( makeType( attributeTypes[i].getType().getBinding() ));
            if (i < (attributeTypes.length - 1)) {
                sql.append(",");
            } else { 
                sql.append(")");
            }
        }

        return sql.toString();
    }
    public String makeCreateFidIndex(){
    	StringBuffer sql = new StringBuffer();
        
        // FID INDEX!
        sql.append("CREATE UNIQUE INDEX ");
    	sql.append( tableName );
        sql.append("_index ON (");
        sql.append(fidColumn);
        sql.append(" )");

        return sql.toString();
    }
    public String makeCreateGeomIndex( SimpleFeatureType schema ){
    	StringBuffer sql = new StringBuffer();
    	
        // SPATIAL INDEX (On default geometry)
        String defaultGeometry = schema.getGeometryDescriptor().getLocalName();
    	sql.append("CREATE INDEX ");
    	sql.append( tableName );
        sql.append("_sidx ON ");
        sql.append( tableName );
        sql.append("(");
        sql.append( defaultGeometry );
        sql.append(") INDEXTYPE IS mdsys.spatial_index");

        return sql.toString();
    }
    
    /** Map from Java type space to Oracle typespace  - for use by createTableSQL */
    String makeType( Class type ){
    	return (String) TYPE_MAPPINGS.getName( type );
    }
    
    /**
     * Constructs an Insert SQL statement template for this feature type.
     *
     * @param featureType The feature type to construct the statement for.
     *
     * @return The SQL insert template.  The FID column will always be first, followed by each
     *         feature attribute.  The VALUES section will contain ?'s for each attribute of the
     *         feature type.
     */
    String makeInsertSQL(SimpleFeatureType featureType) {
        StringBuffer sql = new StringBuffer("INSERT INTO ");

        sql.append(tableName);
        sql.append("(");
        sql.append(fidColumn);
        sql.append(",");

        AttributeDescriptor[] attributeTypes = featureType.getAttributeDescriptors().toArray(new AttributeDescriptor[featureType.getAttributeDescriptors().size()]);

        for (int i = 0; i < attributeTypes.length; i++) {
            sql.append(attributeTypes[i].getLocalName());
            if (i < (attributeTypes.length - 1)) {
                sql.append(",");
            } else { 
                sql.append(")");
            }
        }

        sql.append(" VALUES (?,"); // fid column        

        for (int i = 0; i < attributeTypes.length; i++) {
            sql.append("?");
            if (i < (attributeTypes.length - 1)) {
                sql.append(",");
            } else { 
                sql.append(")");
            }
        }

        return sql.toString();
    }

    /**
     * Makes an SQL statement for getFeatures.  Constructs an SQL statement that will select the
     * features from the table based on the filter.
     *
     * @param attrTypes The Attribute types for the select statement
     * @param filter The filter to convert to a where statement.
     * @param maxFeatures The max amount of features to return.
     * @param useMax True if we are to use the maxFeature as the max.
     *
     * @return An SQL statement.
     *
     * @throws SQLEncoderException If an error occurs encoding the SQL
     */
    String makeSelectSQL(AttributeDescriptor[] attrTypes, Filter filter, int maxFeatures, boolean useMax)
        throws SQLEncoderException {
        LOGGER.finer("Creating sql for Query: mf=" + maxFeatures + " filter=" + filter 
            +  " useMax=" + useMax);

        StringBuffer sqlBuffer = new StringBuffer();

        sqlBuffer.append("SELECT ");
        sqlBuffer.append(fidColumn);

        for (int i = 0; i < attrTypes.length; i++) {
            sqlBuffer.append(", ");
            sqlBuffer.append(attrTypes[i].getLocalName());
        }

        sqlBuffer.append(" FROM ");
        sqlBuffer.append(tableName);

        if (filter != null && filter != org.geotools.filter.Filter.NONE) {
            String where = whereEncoder.encode(filter);

            sqlBuffer.append(" ");
            sqlBuffer.append(where);

            if (useMax && (maxFeatures > 0)) {
                sqlBuffer.append(" and ROWNUM <= ");
                sqlBuffer.append(maxFeatures);
            }
        } else if (useMax && (maxFeatures > 0)) {
            sqlBuffer.append(" WHERE ROWNUM <= ");
            sqlBuffer.append(maxFeatures);
        }

        String sqlStmt = sqlBuffer.toString();

        LOGGER.finer("sqlString = " + sqlStmt);

        return sqlStmt;
    }

    /**
     * Makes a template SQL statement for use in an update prepared statement. The template will
     * have the form:  <code>UPDATE &lt;tablename&gt; SET &lt;type&gt = ?</code>
     *
     * @param attributeTypes The feature attributes that are being updated.
     *
     * @return An SQL template.
     */
    String makeModifyTemplate(AttributeDescriptor[] attributeTypes) {
        StringBuffer buffer = new StringBuffer("UPDATE ");

        buffer.append(tableName);
        buffer.append(" SET ");

        for (int i = 0; i < attributeTypes.length; i++) {
            buffer.append(attributeTypes[i].getLocalName());
            buffer.append(" = ? ");
            if (i < (attributeTypes.length - 1)) {
                buffer.append(", ");
            } else {
                buffer.append(" ");
            }
        }

        return buffer.toString();
    }

    String makeModifyTemplate(AttributeDescriptor[] attributeTypes, Filter filter)
        throws SQLEncoderException {
        String whereClause = whereEncoder.encode(filter);

        return makeModifyTemplate(attributeTypes) + " " + whereClause;
    }

    String makeDeleteSQL(Filter filter) throws SQLEncoderException {
        return "DELETE FROM " + tableName + " " + whereEncoder.encode(filter);
    }

	public String makeAddGeomMetadata(SimpleFeatureType featureType, Envelope bounds, int srid) {
    	StringBuffer sql = new StringBuffer();
    	
        // SPATIAL INDEX (On default geometry)
        String defaultGeometry = featureType.getGeometryDescriptor().getLocalName();
        
    	sql.append("INSERT INTO user_sdo_geom_metadata");    	
    	sql.append("  (TABLE_NAME, COLUMN_NAME,DIMINFO,SRID)");
    	sql.append("VALUES (");
    	sql.append("   '"+tableName+"',");
        sql.append("   '"+defaultGeometry+"',");
        sql.append("   MDSYS.SDO_DIM_ARRAY(");
        sql.append("       MDSYS.SDO_DIM_ELEMENT('X', "+bounds.getMinX()+","+bounds.getMaxX()+", 0.005),"); // -- use appropriate values here ie. min and max x and y
        sql.append("       MDSYS.SDO_DIM_ELEMENT('Y', "+bounds.getMinY()+","+bounds.getMaxY()+", 0.005)");
        sql.append("   ),");
        sql.append("   "+(srid == -1 ? "NULL" : String.valueOf(srid)));
        sql.append(")");
        return sql.toString();
	}
}
