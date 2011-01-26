/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.jdbc.GeoAPISQLBuilder;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.filter.SQLEncoderException;
import org.geotools.filter.UnaliasSQLEncoder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

public class BypassSqlSQLBuilder extends GeoAPISQLBuilder {

	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(BypassSqlSQLBuilder.class.getPackage().getName());
	
	private BypassSqlFeatureTypeHandler ftHandler;
	
	protected Map fieldAliases;

	public BypassSqlSQLBuilder(BypassSqlFeatureTypeHandler ftHandler) {
		this(new UnaliasSQLEncoder(), ftHandler);
	}

	public BypassSqlSQLBuilder(UnaliasSQLEncoder encoder,
			BypassSqlFeatureTypeHandler ftHandler) {
		super(encoder, null, null);
		this.ftHandler = ftHandler;
	}

	/**
	 * Constructs the full SQL SELECT statement for the supplied Filter.
	 * 
	 * <p>
	 * The statement is constructed by concatenating the SELECT column list,
	 * FROM table specification and WHERE clause appropriate to the supplied
	 * Filter.
	 * </p>
	 * 
	 * @param typeName
	 *            The name of the table (feature type) to be queried
	 * @param mapper
	 *            FIDMapper to identify the FID columns in the table
	 * @param attrTypes
	 *            The specific attribute columns to be selected
	 * @param filter
	 *            The Filter that will be used by the encoder to construct the
	 *            WHERE clause
	 * 
	 * @return The fully formed SQL SELECT statement
	 * 
	 * @throws SQLEncoderException
	 *             Not thrown by this method but may be thrown by the encoder
	 *             class
	 */
	public String buildSQLQuery(String typeName, FIDMapper mapper,
			AttributeDescriptor[] attrTypes, org.opengis.filter.Filter filter)
			throws SQLEncoderException {
		String sqlStmt;

		if (this.ftHandler != null && this.ftHandler.isView(typeName)) {
			final StringBuffer sqlBuffer = new StringBuffer();
			final String sqlQuery = ftHandler.getQuery(typeName);
			fieldAliases = parseAliases(sqlQuery);

			UnaliasSQLEncoder encoder = (UnaliasSQLEncoder) super.encoder;
			encoder.setAliases(fieldAliases);
			
			SimpleFeatureType fType;
			try {
				fType = ftHandler.getFeatureTypeInfo(typeName).getSchema();
			} catch (Exception e) {
				throw new RuntimeException("should not happen!: "
						+ e.getMessage());
			}

			//String select = getSelect(sqlQuery, fType);
			String select = "select " ; 
			sqlBuffer.append(select);

			sqlColumns(sqlBuffer, mapper, attrTypes, fieldAliases);

			String from = getFrom(sqlQuery);
			sqlBuffer.append(from);

			String where = getWhere(sqlQuery, mapper, filter);
			sqlBuffer.append(where);

			String groupBy = getGroupBy(sqlQuery);
			if(groupBy != null){
				sqlBuffer.append(groupBy);
			}

			String orderBy = getOrderBy(sqlQuery);
			if(orderBy != null){
				sqlBuffer.append(orderBy);
			}

			sqlStmt = sqlBuffer.toString();
		} else {
			sqlStmt = super.buildSQLQuery(typeName, mapper, attrTypes, filter);
		}
		LOGGER.finer(sqlStmt);
		return sqlStmt;
	}

	public void sqlColumns(final StringBuffer sql, final FIDMapper mapper,
			final AttributeDescriptor[] attributes, final Map aliases) {

		String sqlExpression;
		String alias;
		
		for (int i = 0; i < mapper.getColumnCount(); i++) {
			alias = mapper.getColumnName(i);
			sqlExpression = (String)aliases.get(alias);
			sql.append(encoder.escapeName(sqlExpression) + ", ");
		}

		for (int i = 0; i < attributes.length; i++) {
			alias = attributes[i].getLocalName();
			sqlExpression = (String)aliases.get(alias);

			String fieldName = sqlExpression;
			if(!alias.equals(sqlExpression)){
				fieldName += " AS " + alias;
			}
			if (attributes[i] instanceof GeometryDescriptor) {
				sqlGeometryColumn(sql, attributes[i]);
			} else {
				sql.append(encoder.escapeName(fieldName));
			}

			if (i < (attributes.length - 1)) {
				sql.append(", ");
			}
		}
	}
	
	public static Map parseAliases(final String sqlQueryDefinition){
		Map aliases = new HashMap();
		String sqlQ = sqlQueryDefinition.toLowerCase();
		int idxFrom = sqlQ.indexOf("from ");
		int firstField = 7 + sqlQ.indexOf("select");
		
		String fields = sqlQueryDefinition.substring(firstField, idxFrom);
		
		LOGGER.fine("fields: " + fields);
		List fieldsList = Arrays.asList(fields.split(","));
		
		for(Iterator it = fieldsList.iterator(); it.hasNext();){
			String aliasDef = (String)it.next();
			aliasDef = aliasDef.trim().toLowerCase();
			LOGGER.fine("parsing alias from '" + aliasDef + "'");
			int idx = aliasDef.indexOf("as ");
			if(idx > 0){
				String sqlExpr, alias;
				sqlExpr = aliasDef.substring(0, idx);
				alias = aliasDef.substring(3 + idx);
				LOGGER.fine("sqlExpr: " + sqlExpr + ", alias: " + alias);
				aliases.put(alias.trim(), sqlExpr.trim());
				aliases.put(alias.trim().toUpperCase(), sqlExpr.trim().toUpperCase()); 
			}else{
				LOGGER.fine(aliasDef + " is not aliased");
				aliases.put(aliasDef, aliasDef);
				aliases.put(aliasDef.toUpperCase(), aliasDef.toUpperCase());
			}
		}
		
		return aliases;
	}

	/**
	 * Returns the "SELECT" part of the SQL query definition for FeatureType
	 * <code>fType</code>, without the column names. This allows to maintain
	 * DB specific keywords, for example, <code>SELECT TOP 100 ...</code> in
	 * SQLServer.
	 * 
	 * @param sqlQueryDefinition
	 * @param fType
	 * @return
	 * @throws SQLEncoderException
	 */
	private String getSelect(String sqlQueryDefinition, SimpleFeatureType fType)
			throws SQLEncoderException {
		AttributeDescriptor firstAtt = fType.getDescriptor(0);
		String firstAttName = firstAtt.getLocalName().toLowerCase();

		int index = sqlQueryDefinition.indexOf(firstAttName);
		if (index == -1) {
			throw new SQLEncoderException(
					"attribute "
							+ firstAttName
							+ " not found in sql query definition. It should be the first one!: "
							+ sqlQueryDefinition);
		}

		String select = sqlQueryDefinition.substring(0, index);
		return select;
	}

	public String getFrom(String sqlQueryDefinition) {
		String search = " from ";
		String searchIn = sqlQueryDefinition.toLowerCase();
		int index = searchIn.indexOf(search);
		int lastIndex = searchIn.lastIndexOf(" where ");
		if (lastIndex == -1) {
			lastIndex = searchIn.lastIndexOf("group by");
		}
		if (lastIndex == -1) {
			lastIndex = searchIn.lastIndexOf("order by");
		}
		String from;
		if (lastIndex == -1) {
			from = sqlQueryDefinition.substring(index);
		} else {
			from = sqlQueryDefinition.substring(index, lastIndex);
		}
		return from;
	}

	public String getWhere(String sqlQueryDefinition, FIDMapper mapper,
			org.opengis.filter.Filter filter) throws SQLEncoderException {
		String search = " where ";
		String searchIn = sqlQueryDefinition.toLowerCase();
		int index = searchIn.lastIndexOf(search);

		StringBuffer where = new StringBuffer();
		encoder.setFIDMapper(mapper);
		super.sqlWhere(where, filter);

		if (index > 0) {
			int lastIndex = searchIn.lastIndexOf("group by");
			if (lastIndex == -1) {
				lastIndex = searchIn.lastIndexOf("order by");
			}
			String queryWhere;
			if (lastIndex == -1) {
				queryWhere = sqlQueryDefinition.substring(index
						+ search.length());
			} else {
				queryWhere = sqlQueryDefinition.substring(index
						+ search.length(), lastIndex);
			}

			String filterWhere = where.toString();
			
			if(where.length() > 0){
				where.insert(7, queryWhere + " AND (");
				where.append(")");
			}else{
				where.append(" WHERE "  + queryWhere);
			}
		}
		return where.toString();
	}

	private String getGroupBy(String sqlQueryDefinition) {
		return null;
	}

	private String getOrderBy(String sqlQueryDefinition) {
		String sql = sqlQueryDefinition.toLowerCase();
		int idx = sql.lastIndexOf("order by");
		String orderBy = null;
		if(idx > 0){
			orderBy = " " + sqlQueryDefinition.substring(idx);
		}
		return orderBy;
	}

}
