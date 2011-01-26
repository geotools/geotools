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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.Union;

/**
 * Utility class to help DataStoreFactories for
 * {@linkplain org.geotools.data.sql.SqlDataStore}s register the views provided
 * in a <code>java.util.Map</code> in the call to the factory's
 * <code>createDataStore(Map)</code> method.
 * <p>
 * Due to the non hierarchical nature of a Map, it is no so easy to provide a
 * variable number of arguments on it for the same keyword, since they're
 * usually read from a .properties file.
 * <p>
 * </p>
 * This class helps to minimize the work needed to provide such a mapping of
 * various SQL views to an in-process feature type by defining the following
 * structure for a Map&lt;String,String&gt; passed to createDataStore. Example
 * .properties file:
 * 
 * <pre><code>
 *      dbtype=...
 *      &lt;usual datastore's parameters&gt;...
 *      sqlView.1.typeName = ViewType1
 *      sqlView.1.sqlQuery = select gid, the_geom, table2.someField \
 *                           from table1, table2 \
 *                           where table1.gid = table2.table1_id
 *     
 *      sqlView.2.typeName = ViewType2
 *      sqlView.2.sqlQuery = select ...
 * </code></pre>
 * 
 * This way, this class' utility method
 * {@linkplain #registerSqlViews(SqlDataStore, Map)} will receive a
 * {@linkplain org.geotools.data.sql.SqlDataStore} and the Map of datastore
 * factory parameters and call
 * {@linkplain org.geotools.data.sql.SqlDataStore#registerView(String, String)}
 * for each pair of <code>sqlView.N.typeName, sqlView.N.sqlQuery</code>
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 *
 * @source $URL$
 */
public class ViewRegisteringFactoryHelper {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ViewRegisteringFactoryHelper.class.getPackage()
					.getName());

	private ViewRegisteringFactoryHelper() {
		// no-op
	}

	/**
	 * Registers the sql views provided in <code>params</code> on the
	 * SqlDataStore <code>dataStore</code>
	 * 
	 * @param dataStore
	 * @param params
	 * @throws IOException
	 */
	public static void registerSqlViews(SqlDataStore dataStore, Map params)
			throws IOException {
		Map cleanedUp = cleanUpViewDefinitions(params);
		for (Iterator it = cleanedUp.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			String typeName = (String) entry.getKey();
			String sqlQuery = (String) entry.getValue();
			
			LOGGER.finer("registering view " + typeName);
			LOGGER.finest("sql query is '" + sqlQuery + "'");

			SelectBody selectBody = SqlParser.parse(sqlQuery);
			if(selectBody instanceof Union){
				dataStore.registerView(typeName, (Union)selectBody);
			}else if(selectBody instanceof PlainSelect){
				dataStore.registerView(typeName, (PlainSelect)selectBody);
			}else{
				throw new IllegalStateException(selectBody.getClass().getName());
			}
			
			//dataStore.registerView(typeName, sqlQuery);

		}
	}

	/**
	 * Looks up the set of "surrogate.XYZ" keys
	 * in <code>params</code> and returns a cleaned up map of XYZ/value.
	 * 
	 * @param params
	 * @return
	 */
	static Map cleanUpSurrogateParams(Map params) {
		Map cleanedUpParams = new HashMap();
		for (Iterator it = params.keySet().iterator(); it.hasNext();) {
			final String key = (String) it.next();
			if (!key.startsWith("surrogate.")) {
				continue;
			}

			String clean = key.substring("surrogate.".length());
			cleanedUpParams.put(clean, params.get(key));
		}
		return cleanedUpParams;
	}

	/**
	 * Looks up the set of "sqlView.N.typeName" and "sqlView.N.sqlQuery" keys
	 * in <code>params</code> and returns a cleaned up map of typeName/query.
	 * 
	 * @param params
	 * @return
	 */
	public static Map cleanUpViewDefinitions(Map params) {
		Map cleanedUpViews = new HashMap();
		for (Iterator it = params.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			if (!key.startsWith("sqlView.")) {
				continue;
			}
			if (!key.endsWith(".typeName")) {
				continue;
			}

			String typeName = (String) params.get(key);

			String viewId = key.substring("sqlView.".length(), key
					.indexOf(".typeName"));

			String queryKey = "sqlView." + viewId + ".sqlQuery";

			String query = (String) params.get(queryKey);
			if (query == null) {
				throw new IllegalArgumentException(
						"No SQL query definition provided for type name "
								+ typeName);
			}
			cleanedUpViews.put(typeName, query);
		}
		return cleanedUpViews;
	}

}
