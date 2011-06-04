/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc;

import java.io.IOException;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.opengis.filter.Filter;


/**
 * Abstract class for JDBC (level2) based DataStore implementations.
 * This a convenience class that just extends JDBC2DataStore to keep
 * current datastores that use it happy.
 * Eventually datastores should extend one of JDBC1DataStore, or 
 * JDBC2DataStore.
 * <p>
 * This class provides a default implementation of a JDBC data store. Support
 * for vendor specific JDBC data stores can be easily added to Geotools by
 * subclassing this class and overriding the hooks provided.
 * </p>
 * 
 * <p>
 * At a minimum subclasses should implement the following methods:
 * 
 * <ul>
 * <li>
 * {@link #buildAttributeType(ResultSet) buildAttributeType(ResultSet)} - This
 * should be overriden to construct an attribute type that represents any
 * column types not supported by the default implementation, such as geometry
 * columns.
 * </li>
 * <li>
 * {@link #getGeometryAttributeIO(AttributeType, QueryData)
 * getGeometryAttributeIO(AttributeType, QueryData)}  - Should be overriden to
 * provide a way to read/write geometries into the format of the database
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Additionally subclasses can optionally override the following:
 * 
 * <ul>
 * <li>
 * Use a specific FIDMapperFactory by overriding the {@link
 * #buildFIDMapperFactory(JDBCDataStoreConfig)
 * buildFIDMapperFactory(JDBCDataStoreConfig)} method, and eventually disallow
 * user overrides by throwing an {@link
 * java.lang.UnsupportedOperationException UnsupportedOperationException} in
 * the {@link #setFIDMapperFactory(FIDMapperFactory) setFidMapperFactory()}
 * method.
 * </li>
 * <li>
 * {@link #allowTable(String) allowTable} - Used to determine whether a table
 * name should be exposed as a feature type.
 * </li>
 * <li>
 * {@link #determineSRID(String,String) determineSRID} - Used to determine the
 * SpatialReference ID of a geometry column in a table.
 * </li>
 * <li>
 * {@link #buildSQLQuery(String,AttributeType[],Filter,boolean)
 * buildSQLQuery()} - Sub classes can override this to build a custom SQL
 * query.
 * </li>
 * <li>
 * {@link #getResultSetType(boolean) getResultSetType} if the standard result
 * set type is not satisfactory/does not work with a normal FORWARD_ONLY
 * resultset type
 * </li>
 * <li>
 * {@link #getConcurrency(boolean) getConcurrency} to set the level of
 * concurrency for the result set used to read/write the database
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Additionally subclasses may want to set the value of:
 * 
 * <ul>
 * <li>
 * sqlNameEscape - character (String) to surround names of SQL objects to
 * support mixed-case and non-English names.
 * </li>
 * </ul>
 * </p>
 *
 * @author Amr Alam, Refractions Research
 * @author Sean  Geoghegan, Defence Science and Technology Organisation
 * @author Chris Holmes, TOPP
 * @author Andrea Aime
 *
 * @source $URL$
 * @version $Id$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public abstract class JDBCDataStore extends JDBC2DataStore {
	protected JDBCDataStore( DataSource dataSource, JDBCDataStoreConfig config ) throws IOException {
		super( dataSource, config );		
	}
}
