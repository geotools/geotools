package org.geotools.data.postgis.synch;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.postgis.ModifiedFeatureIds;
import org.geotools.data.postgis.VersionedPostgisDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * This DataStore provides a wrapper for the {@link VersionedPostgisDataStore} 
 * that hides the existence of the synchronizing metadata tables.
 * 
 * @author markles
 *
 */
public class SynchronizedPostgisDataStore implements VersioningDataStore {
	
	static final String TBL_SYNCH_UNITS = "synch_units";
	static final String TBL_SYNCH_TABLES = "synch_tables";
	static final String TBL_SYNCH_UNIT_TABLES = "synch_unit_tables";
	static final String TBL_SYNCH_OUTSTANDING = "synch_outstanding";
	static final String TBL_SYNCH_CONFLICTS = "synch_conflicts";
	static final String TBL_SYNCH_HISTORY = "synch_history";
	
	
	VersionedPostgisDataStore dataStore = null;
	
    public SynchronizedPostgisDataStore(DataSource dataSource, String schema, String namespace,
            int optimizeMode) throws IOException {
    	dataStore = new VersionedPostgisDataStore(dataSource, schema, namespace, optimizeMode);
    }

    public SynchronizedPostgisDataStore(DataSource dataSource, String schema, String namespace)
            throws IOException {
    	dataStore = new VersionedPostgisDataStore(dataSource, schema, namespace);
    }

    public SynchronizedPostgisDataStore(DataSource dataSource, String namespace) throws IOException {
    	dataStore = new VersionedPostgisDataStore(dataSource, namespace);
    }

    public SynchronizedPostgisDataStore(DataSource dataSource) throws IOException {
    	dataStore = new VersionedPostgisDataStore(dataSource);
    }

	/**
	 * @param featureType
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#createSchema(org.opengis.feature.simple.SimpleFeatureType)
	 */
	public void createSchema(SimpleFeatureType featureType) throws IOException {
		dataStore.createSchema(featureType);
	}

	/**
	 * 
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#dispose()
	 */
	public void dispose() {
		dataStore.dispose();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return dataStore.equals(obj);
	}

	/**
	 * @param t
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getConnection(org.geotools.data.Transaction)
	 */
	public Connection getConnection(Transaction t) throws IOException {
		return dataStore.getConnection(t);
	}

	/**
	 * @param query
	 * @param trans
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getFeatureReader(org.geotools.data.Query, org.geotools.data.Transaction)
	 */
	public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(
			Query query, Transaction trans) throws IOException {
		return dataStore.getFeatureReader(query, trans);
	}

	/**
	 * @param typeName
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getFeatureSource(org.opengis.feature.type.Name)
	 */
	public SimpleFeatureSource getFeatureSource(
			Name typeName) throws IOException {
		return dataStore.getFeatureSource(typeName);
	}

	/**
	 * @param typeName
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getFeatureSource(java.lang.String)
	 */
	public SimpleFeatureSource getFeatureSource(
			String typeName) throws IOException {
		return dataStore.getFeatureSource(typeName);
	}

	/**
	 * @param typeName
	 * @param filter
	 * @param transaction
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getFeatureWriter(java.lang.String, org.opengis.filter.Filter, org.geotools.data.Transaction)
	 */
	public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
			String typeName, Filter filter, Transaction transaction)
			throws IOException {
		return dataStore.getFeatureWriter(typeName, filter, transaction);
	}

	/**
	 * @param typeName
	 * @param transaction
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getFeatureWriter(java.lang.String, org.geotools.data.Transaction)
	 */
	public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
			String typeName, Transaction transaction) throws IOException {
		return dataStore.getFeatureWriter(typeName, transaction);
	}

	/**
	 * @param typeName
	 * @param transaction
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getFeatureWriterAppend(java.lang.String, org.geotools.data.Transaction)
	 */
	public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
			String typeName, Transaction transaction) throws IOException {
		return dataStore.getFeatureWriterAppend(typeName, transaction);
	}

	/**
	 * @param tableName
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getFIDMapper(java.lang.String)
	 */
	public FIDMapper getFIDMapper(String tableName) throws IOException {
		return dataStore.getFIDMapper(tableName);
	}

	/**
	 * @return
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getInfo()
	 */
	public ServiceInfo getInfo() {
		return dataStore.getInfo();
	}

	/**
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getLastRevision()
	 */
	public long getLastRevision() throws IOException {
		return dataStore.getLastRevision();
	}

	/**
	 * @return
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getLockingManager()
	 */
	public LockingManager getLockingManager() {
		return dataStore.getLockingManager();
	}

	/**
	 * @param typeName
	 * @param version1
	 * @param version2
	 * @param originalFilter
	 * @param users
	 * @param transaction
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getModifiedFeatureFIDs(java.lang.String, java.lang.String, java.lang.String, org.opengis.filter.Filter, java.lang.String[], org.geotools.data.Transaction)
	 */
	public ModifiedFeatureIds getModifiedFeatureFIDs(String typeName,
			String version1, String version2, Filter originalFilter,
			String[] users, Transaction transaction) throws IOException {
		return dataStore.getModifiedFeatureFIDs(typeName, version1, version2,
				originalFilter, users, transaction);
	}

	/**
	 * @param version1
	 * @param version2
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getModifiedFeatureTypes(java.lang.String, java.lang.String)
	 */
	public String[] getModifiedFeatureTypes(String version1, String version2)
			throws IOException {
		return dataStore.getModifiedFeatureTypes(version1, version2);
	}

	/**
	 * This is stolen verbatim from {@link VersionedPostgisDataStore}
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getNames()
	 */
    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<Name>(typeNames.length);
        for (String typeName : typeNames) {
            names.add(new NameImpl(typeName));
        }
        return names;
    }

	/**
	 * We're intercepting malicious requests for our metadata tables and failing, but 
	 * delegating all other requests.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getSchema(org.opengis.feature.type.Name)
	 */
	public SimpleFeatureType getSchema(Name name) throws IOException {
		String st = name.getLocalPart();
			if(st.equals(TBL_SYNCH_UNITS) ||
					st.equals(TBL_SYNCH_TABLES) ||
					st.equals(TBL_SYNCH_UNIT_TABLES) ||
					st.equals(TBL_SYNCH_OUTSTANDING) ||
					st.equals(TBL_SYNCH_CONFLICTS) ||
					st.equals(TBL_SYNCH_HISTORY))
				throw new IOException(st + " is a protected type.  Hands off.");
		return dataStore.getSchema(name);
	}

	/**
	 * Intercept all requests for our metadata tables and fail.  Delegate all others.
	 * 
	 * @param typeName
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getSchema(java.lang.String)
	 */
	public SimpleFeatureType getSchema(String typeName) throws IOException {
			if(typeName.equals(TBL_SYNCH_UNITS) ||
					typeName.equals(TBL_SYNCH_TABLES) ||
					typeName.equals(TBL_SYNCH_UNIT_TABLES) ||
					typeName.equals(TBL_SYNCH_OUTSTANDING) ||
					typeName.equals(TBL_SYNCH_CONFLICTS) ||
					typeName.equals(TBL_SYNCH_HISTORY))
				throw new IOException(typeName + " is a protected type.  Hands off.");
		return dataStore.getSchema(typeName);
	}

	/**
	 * Retrieves a list of all available featuretypes.
	 * 
	 * Removes any references to our synchronized metadata tables.
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getTypeNames()
	 */
	public String[] getTypeNames() throws IOException {
		List<String> names = new ArrayList<String>(Arrays.asList(dataStore.getTypeNames()));
		names.remove(TBL_SYNCH_UNITS);
		names.remove(TBL_SYNCH_TABLES);
		names.remove(TBL_SYNCH_UNIT_TABLES);
		names.remove(TBL_SYNCH_OUTSTANDING);
		names.remove(TBL_SYNCH_HISTORY);
		names.remove(TBL_SYNCH_CONFLICTS);
		return names.toArray(new String[names.size()]);
	}

	/**
	 * @param query
	 * @return
	 * @throws IOException
	 * @throws SchemaException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#getView(org.geotools.data.Query)
	 */
	public FeatureSource<SimpleFeatureType, SimpleFeature> getView(Query query)
			throws IOException, SchemaException {
		return dataStore.getView(query);
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return dataStore.hashCode();
	}

	/**
	 * @param typeName
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#isVersioned(java.lang.String)
	 */
	public boolean isVersioned(String typeName) throws IOException {
		return dataStore.isVersioned(typeName);
	}

	/**
	 * @param typeName
	 * @return
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#isVersionedFeatureCollection(java.lang.String)
	 */
	public boolean isVersionedFeatureCollection(String typeName)
			throws IOException {
		return dataStore.isVersionedFeatureCollection(typeName);
	}

	/**
	 * @param enabled
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#setLooseBbox(boolean)
	 */
	public void setLooseBbox(boolean enabled) {
		dataStore.setLooseBbox(enabled);
	}

	/**
	 * @param typeName
	 * @param versioned
	 * @param author
	 * @param message
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#setVersioned(java.lang.String, boolean, java.lang.String, java.lang.String)
	 */
	public void setVersioned(String typeName, boolean versioned, String author,
			String message) throws IOException {
		dataStore.setVersioned(typeName, versioned, author, message);
	}

	/**
	 * @param enabled
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#setWKBEnabled(boolean)
	 */
	public void setWKBEnabled(boolean enabled) {
		dataStore.setWKBEnabled(enabled);
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return dataStore.toString();
	}

	/**
	 * @param typeName
	 * @param featureType
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#updateSchema(org.opengis.feature.type.Name, org.opengis.feature.simple.SimpleFeatureType)
	 */
	public void updateSchema(Name typeName, SimpleFeatureType featureType)
			throws IOException {
		dataStore.updateSchema(typeName, featureType);
	}

	/**
	 * @param typeName
	 * @param featureType
	 * @throws IOException
	 * @see org.geotools.data.postgis.VersionedPostgisDataStore#updateSchema(java.lang.String, org.opengis.feature.simple.SimpleFeatureType)
	 */
	public void updateSchema(String typeName, SimpleFeatureType featureType)
			throws IOException {
		dataStore.updateSchema(typeName, featureType);
	}
	
}
