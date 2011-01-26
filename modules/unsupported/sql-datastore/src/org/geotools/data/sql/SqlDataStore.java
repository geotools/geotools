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

import java.io.IOException;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.Union;

import org.geotools.data.DataStore;

/**
 * An extension to the {@link org.geotools.data.DataStore} interface that adds
 * the ability of registering a FeatureType as a SQL SELECT construct
 * against featuretypes and/or normal table like data that the DataStore could deal with.
 * <p>
 * A DataStore implementing this interface does not necesarilly has to be backed
 * by a RDBMS. Implementing this interface only means that the DataStore _is able_ to
 * expose a non existent FeatureType in its backend, by somehow executing the 
 * SQL STATEMENT represented by the sql selct object model passed to {@link #registerView(String, PlainSelect)}
 * or {@link #registerView(String, Union)}.
 * </p> 
 * <p>
 * Implementing datastores are free to support only a subset of the SQL constructs
 * that may be used to create a full SELECT statement.
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL$
 * @since 2.3.x
 */
public interface SqlDataStore extends DataStore {

	/**
	 * Creates an in-process data view against one or more actual FeatureTypes
	 * of this DataStore, which will be advertised as <code>typeName</code>
	 * 
	 * @param typeName the name of the view's FeatureType.
	 * @param sqlQuery a full SQL query which will act as the view definition.
	 * @throws IOException
	 */
	void registerView(String typeName, String sqlQuery) throws IOException,
	UnsupportedOperationException;

	/**
	 * Indicates to this DataStore that it has to expose a FeatureType named
	 * <code>typeName</code> whose schema is to be determined by the sql like
	 * query <code>select</code>, and that query has to be used to retrieve
	 * content, plus any filter given over the feature type.
	 * 
	 * <p>
	 * On registering a view, an implementing datastore must ensure that it 
	 * supports all the constructs provided in the query definition and that
	 * it is valid against its domain of AttributeTypes (i.e., could execute
	 * the query to ensure the backend can deal with it)
	 * </p>
	 * 
	 * @param typeName
	 * @param select either an instance of {@link PlainSelect} or {@link Union}
	 * @throws IOException
	 * @throws UnsupportedOperationException if the <code>select</code> construct
	 * type is not supported or contains components that are not supported 
	 * (for example, subselects, etc)
	 */
	void registerView(String typeName, SelectBody select)throws IOException, 
	UnsupportedOperationException;
}
