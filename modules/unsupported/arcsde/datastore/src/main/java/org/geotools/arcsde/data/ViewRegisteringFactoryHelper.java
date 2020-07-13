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
package org.geotools.arcsde.data;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.Union;
import org.geotools.data.DataSourceException;
import org.geotools.util.logging.Logging;

/**
 * Utility class to help DataStoreFactories for {@linkplain org.geotools.data.sql.SqlDataStore}s
 * register the views provided in a <code>java.util.Map</code> in the call to the factory's <code>
 * createDataStore(Map)</code> method.
 *
 * <p><b>NOTE</b> this class is a rough copy of the one in the sql-datastore unsupported module. We
 * are incorporating it here as don't want to depend on sql-datastore. Thus, it's expected to be
 * replaced by the original once we work out what to do with the sql-datastore module.
 *
 * <p>Due to the non hierarchical nature of a Map, it is no so easy to provide a variable number of
 * arguments on it for the same keyword, since they're usually read from a .properties file.
 *
 * <p>This class helps to minimize the work needed to provide such a mapping of various SQL views to
 * an in-process feature type by defining the following structure for a Map&lt;String,String&gt;
 * passed to createDataStore. Example .properties file:
 *
 * <pre>
 * &lt;code&gt;
 *      dbtype=...
 *      &lt;usual datastore's parameters&gt;...
 *      sqlView.1.typeName = ViewType1
 *      sqlView.1.sqlQuery = select gid, the_geom, table2.someField \
 *                           from table1, table2 \
 *                           where table1.gid = table2.table1_id
 *
 *      sqlView.2.typeName = ViewType2
 *      sqlView.2.sqlQuery = select ...
 * &lt;/code&gt;
 * </pre>
 *
 * This way, this class' utility method {@linkplain #registerSqlViews(SqlDataStore, Map)} will
 * receive a {@linkplain org.geotools.data.sql.SqlDataStore} and the Map of datastore factory
 * parameters and call {@linkplain org.geotools.data.sql.SqlDataStore#registerView(String, String)}
 * for each pair of <code>sqlView.N.typeName, sqlView.N.sqlQuery</code>
 *
 * @author Gabriel Roldan, Axios Engineering
 */
public class ViewRegisteringFactoryHelper {
    private static final Logger LOGGER = Logging.getLogger(ViewRegisteringFactoryHelper.class);

    private ViewRegisteringFactoryHelper() {
        // no-op
    }

    /**
     * Registers the sql views provided in <code>params</code> on the SqlDataStore <code>dataStore
     * </code>
     */
    public static void registerSqlViews(ArcSDEDataStore dataStore, Map<String, Serializable> params)
            throws IOException {
        Map<String, Serializable> cleanedUp = cleanUpViewDefinitions(params);
        for (Map.Entry<String, Serializable> entry : cleanedUp.entrySet()) {
            String typeName = (String) entry.getKey();
            String sqlQuery = (String) entry.getValue();

            LOGGER.finer("registering view " + typeName);
            LOGGER.finest("sql query is '" + sqlQuery + "'");

            PlainSelect selectBody = parseSqlQuery(sqlQuery);
            dataStore.registerView(typeName, selectBody);
        }
    }

    /**
     * Looks up the set of "sqlView.N.typeName" and "sqlView.N.sqlQuery" keys in <code>params</code>
     * and returns a cleaned up map of typeName/query.
     */
    public static Map<String, Serializable> cleanUpViewDefinitions(
            Map<String, Serializable> params) {
        Map<String, Serializable> cleanedUpViews = new HashMap<String, Serializable>();
        for (Map.Entry<String, Serializable> entry : params.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith("sqlView.")) {
                continue;
            }
            if (!key.endsWith(".typeName")) {
                continue;
            }

            String typeName = (String) entry.getValue();

            String viewId = key.substring("sqlView.".length(), key.indexOf(".typeName"));

            String queryKey = "sqlView." + viewId + ".sqlQuery";

            String query = (String) params.get(queryKey);
            if (query == null) {
                throw new IllegalArgumentException(
                        "No SQL query definition provided for type name " + typeName);
            }
            cleanedUpViews.put(typeName, query);
        }
        return cleanedUpViews;
    }

    public static PlainSelect parseSqlQuery(String selectStatement) throws IOException {
        CCJSqlParserManager pm = new CCJSqlParserManager();
        Statement statement;
        try (Reader reader = new StringReader(selectStatement)) {
            statement = pm.parse(reader);
        } catch (Exception e) {
            throw new DataSourceException(
                    "parsing select statement: " + e.getCause().getMessage(), e);
        }
        if (!(statement instanceof Select)) { // either PlainSelect or Union
            throw new IllegalArgumentException("expected select or union statement: " + statement);
        }
        SelectBody selectBody = ((Select) statement).getSelectBody();
        if (selectBody instanceof Union) {
            // dataStore.registerView(typeName, (Union) selectBody);
            throw new UnsupportedOperationException(
                    "ArcSDEDataStore does not supports registering Union queries");
        } else if (selectBody instanceof PlainSelect) {
            return (PlainSelect) selectBody;
        } else {
            throw new IllegalStateException(selectBody.getClass().getName());
        }
    }
}
