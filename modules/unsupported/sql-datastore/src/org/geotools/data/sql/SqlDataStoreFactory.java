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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;

/**
 * A DataStoreFactory that produces any {@link org.geotools.data.sql.SqlDataStore}, whose
 * only purpose is to simplify the process of defining in-process views by
 * specifying them as plain SQL SELECT statements in the Map of parameters.
 * <p>
 * The only mandatory parameter to use this factory is <code>"wrapper.type"</code>, whose
 * value must be the fixed literal <code>"sql"</code>. Though a bunch of other parameters
 * will be needed to actually find the surrogate datastore, those parameters are
 * dependent on the parameters expected by the surrogate datastore factory.
 * To prevent parameter name clashes and the surrogate being found prior to this
 * factory in the lookup process, all surrogate datastre's parameters must be 
 * prepended by the literal <code>"surrogate."</code>, as in the following example:
 * <pre><code>
 *      wrapper.type=SQL
 *      surrogate.property1=...
 *      surrogate.property2=...
 *      surrogate.property3=...
 *      surrogate.property4=...
 *      
 *      sqlView.1.typeName = ViewType1
 *      sqlView.1.sqlQuery = select gid, the_geom, table2.someField \
 *                           from table1, table2 \
 *                           where table1.gid = table2.table1_id
 *     
 *      sqlView.2.typeName = ViewType2
 *      sqlView.2.sqlQuery = select ...
 * </code></pre>
 * </p>
 * <p>
 * As noted above, apart from the surrogate parameters you'll have to specify
 * which SQL queries to use as FeatureType "views". The process is quite simple:
 * prepend <code>"sqlView.N."</code> to <code>"typeName"</code> and <code>"sqlQuery"</code>
 * for each view you want to define.
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $id$
 * @since 2.3.x
 * @source $URL$
 */
public class SqlDataStoreFactory implements DataStoreFactorySpi {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SqlDataStore.class.getPackage().getName());
	
	private static final String WRAPPERTYPE_FIXED_VALUE = "sql";
	
	private static Param WRAPPERTYPE =  new Param("wrapper.type", String.class,
            "Needs to be the fixed value 'sql' to be recognized", true, 
            WRAPPERTYPE_FIXED_VALUE);
	
	public DataStore createDataStore(Map params) throws IOException {
		if(!canProcess(params)){
			throw new DataSourceException("can't process " + params);
		}
		Map surrogateParams = ViewRegisteringFactoryHelper.cleanUpSurrogateParams(params);
		DataStore surrogate = DataStoreFinder.getDataStore(surrogateParams);
		if(surrogate == null){
			throw new DataSourceException("Can't locate surrogate datastore: " + 
					surrogateParams);
		}
		if(!(surrogate instanceof SqlDataStore)){
			throw new DataSourceException("Surrogate datastore is not a SqlDataStore: " + 
					surrogate.getClass());
		}
		
		SqlDataStore sqlDs = (SqlDataStore)surrogate;
		ViewRegisteringFactoryHelper.registerSqlViews(sqlDs, params);
		
		return sqlDs;
	}

	public DataStore createNewDataStore(Map params) throws IOException {
		throw new UnsupportedOperationException();
	}

	public String getDisplayName() {
		return "SQL select based datastore";
	}

	public String getDescription() {
		return "DataStore that adds in-process views to ahother one that implements SqlDataStore";
	}

	public Param[] getParametersInfo() {
		return new Param[]{WRAPPERTYPE};
	}

	/**
	 * Checks only if the "wrapper.type" keywords is equal to the
	 * fixed value "sql".
	 */
	public boolean canProcess(Map params) {
		try {
			return WRAPPERTYPE_FIXED_VALUE.equalsIgnoreCase(
					String.valueOf( WRAPPERTYPE.lookUp(params) ) ); 
		} catch (Exception e) {
			LOGGER.finer("lookup failed on parameter " + WRAPPERTYPE.key);
			return false;
		} 
	}

	/**
	 * @return <code>true</code>
	 * @REVISIT: checking surrogate's datastore availability
	 * would be costly which goes against this method intentions.
	 */
	public boolean isAvailable() {
		return true;  
	}

	/**
	 * @return an empty Map. No implementation hints
	 * defined for this datastore.
	 */
	public Map getImplementationHints() {
		return Collections.EMPTY_MAP;
	}

}
