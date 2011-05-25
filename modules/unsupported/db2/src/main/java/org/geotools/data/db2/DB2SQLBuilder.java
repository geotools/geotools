/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
package org.geotools.data.db2;

import java.io.IOException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataUtilities;
import org.geotools.data.db2.filter.SQLEncoderDB2;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.GeoAPISQLBuilder;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.filter.SQLEncoderException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;


/**
 * A DB2-specific subclass of DefaultSQLBuilder, which supports DB2 Spatial
 * Extender geometry datatypes.
 *
 * @author David Adler - IBM Corporation
 *
 * @source $URL$
 */
public class DB2SQLBuilder extends GeoAPISQLBuilder {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.db2");
    private String tableSchema = null;
    private String tableName = null;
    private FIDMapper mapper = null;



    /**
     * Creates a DB2SQLBuilder that will provide a table schema to qualify
     * table names. The table schema is provided by the DB2DataStore which
     * means that a given DataStore can only access tables within a single
     * schema.
     * 
     * <p>
     * It would be better if the table schema was managed by FeatureTypeHandler
     * or FeatureType.
     * </p>
     *
     * @param encoder an SQLEncoder
     * @param tableSchema table schema to qualify table names
     * @param featureType the feature type to be used by this SQL builder
     */
    public DB2SQLBuilder(FilterToSQL encoder, String tableSchema, SimpleFeatureType featureType, FIDMapper mapper) {
        super(encoder, featureType, null);
        this.tableSchema = tableSchema;
        this.mapper = mapper;
        this.tableName = featureType.getTypeName();
    }
    
    /**
     * Generates the select column specification for a DB2 geometry column.
     * 
     * <p>
     * Overrides sqlGeometryColumn in DefaultSQLBuilder
     * </p>
     *
     * @param sql A StringBuffer that the column specification can be appended
     *        to.
     * @param geomAttribute An AttributeType for a geometry attribute
     */
    public void sqlGeometryColumn(StringBuffer sql, AttributeDescriptor geomAttribute) {
        sql.append( "DB2GSE.ST_AsBinary(" + sqlGeometryColumnName(geomAttribute) + ")");
    }

    /**
     * Gets the escaped geometry column name.
     *
     * @param geomAttribute the geometry attribute.
     *
     * @return the String with the escaped name.
     */
    String sqlGeometryColumnName(AttributeDescriptor geomAttribute) {
        return this.encoder.escapeName(geomAttribute.getLocalName());
    }

    /**
     * Generates the SELECT clause values to get the geometry min-max values.
     *
     * @param geomAttribute the geometry attribute.
     *
     * @return the string with the 4 column expressions.
     */
    String sqlGeometryMinMaxValues(AttributeDescriptor geomAttribute) {
        String sql;
        String gcName = sqlGeometryColumnName(geomAttribute);
        sql = "MIN(db2gse.ST_MinX(" + gcName + ")), " + "MIN(db2gse.ST_MinY("
            + gcName + ")), " + "MAX(db2gse.ST_MaxX(" + gcName + ")), "
            + "MAX(db2gse.ST_MaxY(" + gcName + ")) ";

        return sql;
    }

    /**
     * Construct the FROM clause for a feature type.  Prefixes the typeName
     * with the table schema provided when this class was constructed.
     * 
     * <p>
     * This method could be promoted to DefaultSQLBuilder if the table schema
     * was propagated up.
     * </p>
     * 
     * <p>
     * Overrides sqlFrom in DefaultSQLBuilder
     * </p>
     *
     * @param sql StringBuffer to be appended to
     * @param typeName Name of the type (table)
     */
    public void sqlFrom(StringBuffer sql, String typeName) {
        sql.append(" FROM ");
        sql.append(getSchemaTableName(typeName));
    }

    /**
     * Builds the SQL query to get the bounds (min-max coordinate values) of a
     * geometry column for a given filter.
     *
     * @param typeName the feature type name.
     * @param geomAttr the geometry attribute.
     * @param filter the filter expression.
     *
     * @return the string to perform the SQL query.
     *
     * @throws SQLEncoderException
     */
    public String buildSQLBoundsQuery(String typeName, AttributeDescriptor geomAttr,
        org.opengis.filter.Filter filter) throws SQLEncoderException {
        StringBuffer sqlBuffer = new StringBuffer();

        sqlBuffer.append("SELECT ");
        sqlBuffer.append(sqlGeometryMinMaxValues(geomAttr));
        sqlFrom(sqlBuffer, typeName);
        sqlWhere(sqlBuffer, filter);

        String sqlStmt = sqlBuffer.toString();
        LOGGER.finer(sqlStmt);

        return sqlStmt;
    }

    /**
     * Gets the SQL encoder associated with this SQL builder.
     *
     * @return the associated encoder
     */
    SQLEncoderDB2 getEncoder() {
        return (SQLEncoderDB2) this.encoder;
    }

    /**
     * Gets the concatenated schema name and table name needed by DB2.
     *
     * @param tableName
     *
     * @return concatenated schema and table name
     */
    String getSchemaTableName(String tableName) {
        return escapeName(this.tableSchema) + "." + escapeName(tableName);
    }

    /**
     * Gets the concatenated schema name and table name needed by DB2.
     *
     * @return concatenated schema and table name
     */
    String getSchemaTableName() {
        return escapeName(this.tableSchema) + "." + escapeName(this.tableName);
    }

    /**
     * "escape" the specified name. This is currently delegated to the encoder
     * object and for DB2 this means that the specified name will be
     * surrounded by double-quote characters in order to ensure case
     * sensitivity.
     *
     * @param name
     *
     * @return escaped name
     */
    String escapeName(String name) {
        return this.encoder.escapeName(name);
    }
	/**
     * Creates a sql insert statement.  Uses each feature's schema, which makes
     * it possible to insert out of order, as well as inserting less than all
     * features.
     *
     * @param attributes the attribute columns to be inserted
     * @param feature the feature to add.
     *
     * @return an insert sql statement.
     *
     * @throws IOException
     */
    protected String makeInsertSql(List<AttributeDescriptor> attributes, SimpleFeature feature) throws IOException {

		SQLEncoderDB2 db2Encoder = (SQLEncoderDB2) encoder;

        String attrValue = null;
        boolean firstAttr = true;
		StringBuffer colNameList = new StringBuffer("");
		StringBuffer valueList = new StringBuffer("");

        for (int i = 0; i < attributes.size(); i++) {
			String colName = escapeName(attributes.get(i).getLocalName());
			if (!firstAttr) {
				colNameList.append(", ");
				valueList.append(", ");
			}
			firstAttr = false;
            colNameList.append(colName);
            
			Object currAtt = feature.getAttribute(i);        	
			if (currAtt == null) {
				attrValue = "NULL";
			}
			else 
			if (Geometry.class.isAssignableFrom(attributes.get(i).getType().getBinding())) {
				attrValue = db2Encoder.db2Geom((Geometry) currAtt);
			} else 
			if (String.class.isAssignableFrom(attributes.get(i).getType().getBinding())) {
				attrValue = "'" + currAtt.toString() + "'";
				} else	{
					attrValue = currAtt.toString();
				}

			
            valueList.append(attrValue);
        }


        String statementSQL = "INSERT INTO " + getSchemaTableName()
			+ "( " + colNameList.toString() + ")"
			+ " VALUES(" + valueList.toString() + ")"
		;
        return (statementSQL);
    }
	/**
	 * Generates the SQL UPDATE statement
	 * 
	 * @param attributes the attribute columns to be inserted
	 * @param feature
	 * 
	 * @return DB2 UPDATE statement
	 * 
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 */
	protected String makeUpdateSql(List<AttributeDescriptor> attributes, SimpleFeature live, SimpleFeature current)
			throws IOException {

		boolean firstAttr = true;
		SQLEncoderDB2 db2Encoder = (SQLEncoderDB2) encoder;
		StringBuffer statementSQL = new StringBuffer("UPDATE " + getSchemaTableName()
				+ " SET ");

		for (int i = 0; i < current.getAttributeCount(); i++) {
			Object currAtt = current.getAttribute(i);
			Object liveAtt = live.getAttribute(i);

			if (!DataUtilities.attributesEqual(currAtt, liveAtt)) {
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.fine("modifying att# " + i + " to " + currAtt);
				}
				String attrValue = null;
				String attrName = attributes.get(i).getLocalName();

				if (Geometry.class.isAssignableFrom(attributes.get(i).getType().getBinding())) {

					attrValue = db2Encoder.db2Geom((Geometry) currAtt);
				} else 
				if (String.class.isAssignableFrom(attributes.get(i).getType().getBinding())) {
					attrValue = "'" + currAtt.toString() + "'";
					} else {
						attrValue = currAtt.toString();
					}

				String colName = escapeName(attrName);
				if (!firstAttr) {
					statementSQL.append(", ");
				}
				firstAttr = false;
				statementSQL.append(colName).append(" = ").append(attrValue);
			}
		}
		statementSQL.append(makeFIDWhere(current));
		return (statementSQL.toString());
	}
	/**
	 * Generates the SQL delete statement
	 * 
	 * @param feature
	 * 
	 * @return DB2 DELETE statement
	 * @throws IOException
	 * 
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 */
	public String makeDeleteSql(SimpleFeature feature) throws IOException {
		String deleteSQL  = "DELETE FROM "
			+ getSchemaTableName() + makeFIDWhere(feature);
		return (deleteSQL);
	}	
	
	/** 
	 * Build a DB2 WHERE clause based on the FID column values
	 * 
	 * @param feature
	 * @return A DB2 WHERE clause based on the FID column values.
	 * @throws IOException
	 */
	protected String makeFIDWhere(SimpleFeature feature) throws IOException {
		
		StringBuffer statementSQL = new StringBuffer(" WHERE ");
		Object[] pkValues = mapper.getPKAttributes(feature.getID());

		if (mapper.getColumnCount() == 0) {
			// can't update/delete without a primary key
			throw new UnsupportedOperationException();
		}
		boolean firstCol = true;
		for (int i = 0; i < mapper.getColumnCount(); i++) {
			
			if (!firstCol) {
				statementSQL.append(" AND ");
				firstCol = false;
			}
			
			statementSQL.append(
					escapeName(mapper.getColumnName(i)))
					.append(" = ");

			// don't put quotes around numeric values
			if (isTypeNumeric(mapper.getColumnType(i))) {
				statementSQL.append(pkValues[i]);
			} else {
				statementSQL.append("'" + pkValues[i] + "'");
			}
		}
		return (statementSQL.toString());		
	}  
	/**
	 * Checks if column type is SQL numeric type
	 * 
	 * @param SQL columnType
	 * 
	 * @return true if the column is an SQL numeric type
	 */
	protected boolean isTypeNumeric(int columnType) {
		boolean numeric = false;

		if ((columnType == Types.BIT) || (columnType == Types.TINYINT)
				|| (columnType == Types.SMALLINT)
				|| (columnType == Types.INTEGER)
				|| (columnType == Types.BIGINT) || (columnType == Types.FLOAT)
				|| (columnType == Types.REAL) || (columnType == Types.DOUBLE)
				|| (columnType == Types.NUMERIC)
				|| (columnType == Types.DECIMAL)) {
			numeric = true;
		}

		return (numeric);
	}	
	
}
