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
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.attributeio.BasicAttributeIO;
import org.geotools.data.jdbc.fidmapper.DefaultFIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.view.DefaultView;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.Hints;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.SQLEncoder;
import org.geotools.filter.SQLEncoderException;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Abstract class for JDBC based DataStore implementations.
 *
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
 * <li> {@link #buildAttributeType(ResultSet) buildAttributeType(ResultSet)} -
 * This should be overriden to construct an attribute type that represents any
 * column types not supported by the default implementation, such as geometry
 * columns. </li>
 * <li> {@link #getGeometryAttributeIO(AttributeDescriptor, QueryData)
 * getGeometryAttributeIO(AttributeDescriptor, QueryData)} - Should be overriden to
 * provide a way to read/write geometries into the format of the database </li>
 * </ul>
 * </p>
 *
 * <p>
 * Additionally subclasses can optionally override the following:
 *
 * <ul>
 * <li> Use a specific FIDMapperFactory by overriding the {@link
 * #buildFIDMapperFactory(JDBCDataStoreConfig)
 * buildFIDMapperFactory(JDBCDataStoreConfig)} method, and eventually disallow
 * user overrides by throwing an {@link java.lang.UnsupportedOperationException
 * UnsupportedOperationException} in the
 * {@link #setFIDMapperFactory(FIDMapperFactory) setFidMapperFactory()} method.
 * </li>
 * <li> {@link #allowTable(String) allowTable} - Used to determine whether a
 * table name should be exposed as a feature type. </li>
 * <li> {@link #determineSRID(String,String) determineSRID} - Used to determine
 * the SpatialReference ID of a geometry column in a table. </li>
 * <li> {@link #buildSQLQuery(String,AttributeDescriptor[],Filter,boolean)
 * buildSQLQuery()} - Sub classes can override this to build a custom SQL query.
 * </li>
 * <li> {@link #getResultSetType(boolean) getResultSetType} if the standard
 * result set type is not satisfactory/does not work with a normal FORWARD_ONLY
 * resultset type </li>
 * <li> {@link #getConcurrency(boolean) getConcurrency} to set the level of
 * concurrency for the result set used to read/write the database </li>
 * </ul>
 * </p>
 *
 * <p>
 * Additionally subclasses may want to set the value of:
 *
 * <ul>
 * <li> sqlNameEscape - character (String) to surround names of SQL objects to
 * support mixed-case and non-English names. </li>
 * </ul>
 * </p>
 *
 * @author Amr Alam, Refractions Research
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @author Chris Holmes, TOPP
 * @author Andrea Aime
 *
 * @source $URL$
 * @version $Id$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public abstract class JDBC1DataStore implements DataStore {

	/** The logger for the filter module. */
	protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc");

	/**
	 * Maps SQL types to Java classes. This might need to be fleshed out more
	 * later, Ive ignored complex types such as ARRAY, BLOB and CLOB. It is
	 * protected so subclasses can override it I guess.
	 *
	 * <p>
	 * These mappings were taken from
	 * http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html#997737
	 * </p>
	 */
	public static final Map TYPE_MAPPINGS = new HashMap();

	static {
		TYPE_MAPPINGS.put(new Integer(Types.VARCHAR), String.class);
		TYPE_MAPPINGS.put(new Integer(Types.CHAR), String.class);
		TYPE_MAPPINGS.put(new Integer(Types.LONGVARCHAR), String.class);

		TYPE_MAPPINGS.put(new Integer(Types.BIT), Boolean.class);
		TYPE_MAPPINGS.put(new Integer(Types.BOOLEAN), Boolean.class);

		TYPE_MAPPINGS.put(new Integer(Types.TINYINT), Short.class);
		TYPE_MAPPINGS.put(new Integer(Types.SMALLINT), Short.class);

		TYPE_MAPPINGS.put(new Integer(Types.INTEGER), Integer.class);
		TYPE_MAPPINGS.put(new Integer(Types.BIGINT), Long.class);

		TYPE_MAPPINGS.put(new Integer(Types.REAL), Float.class);
		TYPE_MAPPINGS.put(new Integer(Types.FLOAT), Double.class);
		TYPE_MAPPINGS.put(new Integer(Types.DOUBLE), Double.class);

		TYPE_MAPPINGS.put(new Integer(Types.DECIMAL), BigDecimal.class);
		TYPE_MAPPINGS.put(new Integer(Types.NUMERIC), BigDecimal.class);

		TYPE_MAPPINGS.put(new Integer(Types.DATE), java.sql.Date.class);
		TYPE_MAPPINGS.put(new Integer(Types.TIME), java.sql.Time.class);
		TYPE_MAPPINGS.put(new Integer(Types.TIMESTAMP),
				java.sql.Timestamp.class);
	}

	private BasicAttributeIO basicAttributeIO;

	/** Manages listener lists for SimpleFeatureSource implementations */
	public FeatureListenerManager listenerManager = new FeatureListenerManager();

	private LockingManager lockingManager = createLockingManager();

	protected final JDBCDataStoreConfig config;

	protected FeatureTypeHandler typeHandler = null;

	/**
	 * The character(s) to surround schema, table and column names an SQL query
	 * to support mixed-case and non-English names
	 */
	protected String sqlNameEscape = "";

	/**
	 * When true, writes are allowed also on tables with volatile FID mappers.
	 * False by default
	 *
	 * @see FIDMapper#isVolatile()
	 */
	protected boolean allowWriteOnVolatileFIDs;

    /**
     * The transaction isolation level to use in a transaction.  One of
     * Connection.TRANSACTION_READ_UNCOMMITTED, TRANSACTION_READ_COMMITTED,
     * TRANSACTION_REPEATABLE_READ, or SERIALIZABLE.
     * 
     * Connection.TRANSACTION_NONE may also be used to indicate "use default".
     */
    protected int transactionIsolation = Connection.TRANSACTION_NONE;
    
	/**
	 * Construct a JDBCDataStore with ConnectionPool and associated
	 * configuration.
	 * 
	 * @param config
	 *
	 * @throws IOException
	 */
	public JDBC1DataStore(JDBCDataStoreConfig config) throws IOException {
		this.config = config;
		this.typeHandler = getFeatureTypeHandler(config);
	}

	/**
	 * Gets the SQL name escape string.
	 *
	 * <p>
	 * The value of this string is prefixed and appended to table schema names,
	 * table names and column names in an SQL statement to support mixed-case
	 * and non-English names.
	 * </p>
	 *
	 * @return the value of the SQL name escape string.
	 */
	public String getSqlNameEscape() {
		return sqlNameEscape;
	}

	/**
	 * Sets the SQL name escape string.
	 *
	 * <p>
	 * The value of this string is prefixed and appended to table schema names,
	 * table names and column names in an SQL statement to support mixed-case
	 * and non-English names.
	 * </p>
	 *
	 * <p>
	 * This value is typically only set once when the DataStore implementation
	 * class is constructed.
	 * </p>
	 *
	 * @param sqlNameEscape
	 *            the name escape character
	 */
	protected void setSqlNameEscape(String sqlNameEscape) {
		this.sqlNameEscape = sqlNameEscape;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @deprecated This is deprecated in favour of the JDBCDataStoreConfig
	 *             object. public JDBCDataStore(ConnectionPool connectionPool)
	 *             throws IOException { this(connectionPool, null, new
	 *             HashMap(), ""); }
	 */

	/**
	 * DOCUMENT ME!
	 *
	 * @param config
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	protected FeatureTypeHandler getFeatureTypeHandler(
			JDBCDataStoreConfig config) throws IOException {
		return new FeatureTypeHandler(this, buildFIDMapperFactory(config),
				config.getTypeHandlerTimeout());
	}

	protected FIDMapperFactory buildFIDMapperFactory(JDBCDataStoreConfig config) {
		return new DefaultFIDMapperFactory();
	}

	public FIDMapper getFIDMapper(String tableName) throws IOException {
		return typeHandler.getFIDMapper(tableName);
	}

	/**
	 * Allows subclass to create LockingManager to support their needs.
	 *
	 */
	protected LockingManager createLockingManager() {
		return new InProcessLockingManager();
	}

    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from "+getClass().getSimpleName() );
        info.setSchema( FeatureTypes.DEFAULT_NAMESPACE );        
        return info;
    }
    
	/**
	 * @see org.geotools.data.DataStore#getFeatureTypes()
	 */
	public String[] getTypeNames() throws IOException {
		return typeHandler.getTypeNames();
	}

	/**
	 * @see org.geotools.data.DataStore#getSchema(java.lang.String)
	 */
	public SimpleFeatureType getSchema(String typeName) throws IOException {
		return typeHandler.getSchema(typeName);
	}

	/**
	 * Create a new featureType.
	 *
	 * <p>
	 * Not currently supported - subclass may implement.
	 * </p>
	 *
	 * @param featureType
	 *
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 *             Creating new schemas is not supported.
	 *
	 * @see org.geotools.data.DataStore#createSchema(org.geotools.feature.FeatureType)
	 */
	public void createSchema(SimpleFeatureType featureType) throws IOException {
		throw new UnsupportedOperationException(
				"Table creation not implemented");
	}

	/**
	 * Used to provide support for changing the DataStore Schema.
	 *
	 * <p>
	 * Specifically this is intended to address updating the metadata Coordinate
	 * System information.
	 * </p>
	 *
	 * <p>
	 * If we can figure out the Catalog API for metadata we will not have to use
	 * such a heavy handed approach.
	 * </p>
	 *
	 * <p>
	 * Subclasses are free to implement various levels of support:
	 * </p>
	 *
	 * <ul>
	 * <li> None - table modification is not supported </li>
	 * <li> CS change - ensure that the attribtue types match and only update
	 * metadata but not table structure. </li>
	 * <li> Allow table change opperations </li>
	 * </ul>
	 *
	 *
	 * @see org.geotools.data.DataStore#updateSchema(java.lang.String,
	 *      org.geotools.feature.FeatureType)
	 */
	public void updateSchema(String typeName, SimpleFeatureType featureType)
			throws IOException {
		throw new UnsupportedOperationException(
				"Table modification not supported");
	}

	/*
	 * // Jody - This is my recomendation for DataStore // in order to support
	 * CS reprojection and override public SimpleFeatureSource getView(final Query
	 * query) throws IOException, SchemaException { String typeName =
	 * query.getTypeName(); FeatureType origionalType = getSchema(typeName);
	 * //CoordinateSystem cs = query.getCoordinateSystem(); //final FeatureType
	 * featureType = DataUtilities.createSubType( origionalType,
	 * query.getPropertyNames(), cs ); final FeatureType featureType =
	 * DataUtilities.createSubType(origionalType, query.getPropertyNames());
	 * return new AbstractFeatureSource() { public DataStore getDataStore() {
	 * return JDBCDataStore.this; } public void
	 * addFeatureListener(FeatureListener listener) {
	 * listenerManager.addFeatureListener(this, listener); } public void
	 * removeFeatureListener(FeatureListener listener) {
	 * listenerManager.removeFeatureListener(this, listener); } public
	 * FeatureType getSchema() { return featureType; } }; }
	 */

	/**
	 * Default implementation based on getFeatureReader and getFeatureWriter.
	 *
	 * <p>
	 * We should be able to optimize this to only get the RowSet once
	 * </p>
	 *
	 * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
	 */
	public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
		if (!typeHandler.getFIDMapper(typeName).isVolatile()
				|| allowWriteOnVolatileFIDs) {
			if (getLockingManager() != null) {
				// Use default JDBCFeatureLocking that delegates all locking
				// the getLockingManager
				//
				return new JDBCFeatureLocking(this, getSchema(typeName));
			} else {
				// subclass should provide a FeatureLocking implementation
				// but for now we will simply forgo all locking
				return new JDBCFeatureStore(this, getSchema(typeName));
			}
		} else {
			return new JDBCFeatureSource(this, getSchema(typeName));
		}
	}

	/**
	 * This is a public entry point to the DataStore.
	 *
	 * <p>
	 * We have given some though to changing this api to be based on query.
	 * </p>
	 *
	 * <p>
	 * Currently the is is the only way to retype your features to different
	 * name spaces.
	 * </p>
	 * (non-Javadoc)
	 */
	public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(final SimpleFeatureType requestType,
			final Filter filter, final Transaction transaction)
			throws IOException {
		String typeName = requestType.getTypeName();
		SimpleFeatureType schemaType = getSchema(typeName);

		int compare = DataUtilities.compare(requestType, schemaType);

		Query query;

		if (compare == 0) {
			// they are the same type
			//
			query = new DefaultQuery(typeName, filter);
		} else if (compare == 1) {
			// featureType is a proper subset and will require reTyping
			//
			String[] names = attributeNames(requestType, filter);
			query = new DefaultQuery(typeName, filter, Query.DEFAULT_MAX,
					names, "getFeatureReader");
		} else {
			// featureType is not compatiable
			//
			throw new IOException("Type " + typeName + " does match request");
		}

		if ((filter == Filter.EXCLUDE) || filter.equals(Filter.EXCLUDE)) {
			return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(requestType);
		}

		FeatureReader<SimpleFeatureType, SimpleFeature> reader = getFeatureReader(query, transaction);

		if (compare == 1) {
			reader = new ReTypeFeatureReader(reader, requestType, false);
		}

		return reader;
	}

	/**
	 * Gets the list of attribute names required for both featureType and filter
	 *
	 * @param featureType
	 *            The FeatureType to get attribute names for.
	 * @param filter
	 *            The filter which needs attributes to filter.
	 *
	 * @return The list of attribute names required by a filter.
	 *
	 * @throws IOException
	 *             If we can't get the schema.
	 */
	protected String[] attributeNames(SimpleFeatureType featureType, Filter filter)
			throws IOException {
		String typeName = featureType.getTypeName();
		SimpleFeatureType origional = getSchema(typeName);
		SQLBuilder sqlBuilder = getSqlBuilder(typeName);

		if (featureType.getAttributeCount() == origional.getAttributeCount()) {
			// featureType is complete (so filter must require subset
			return DataUtilities.attributeNames(featureType);
		}

		String[] typeAttributes = DataUtilities.attributeNames(featureType);
		String[] filterAttributes = DataUtilities.attributeNames(sqlBuilder
				.getPostQueryFilter(filter));

		if ((filterAttributes == null) || (filterAttributes.length == 0)) {
			// no filter attributes required
			return typeAttributes;
		}

		Set set = new HashSet();
		set.addAll(Arrays.asList(typeAttributes));
		set.addAll(Arrays.asList(filterAttributes));

		if (set.size() == typeAttributes.length) {
			// filter required a subset of featureType attributes
			return typeAttributes;
		} else {
			return (String[]) set.toArray(new String[set.size()]);
		}
	}

	/**
	 * The top level method for getting a FeatureReader.
	 *
	 * <p>
	 * Chris- I've gone with the Query object aswell. It just seems to make more
	 * sense. This is pretty well split up across methods. The hooks for DB
	 * specific AttributeReaders are createResultSetReader and
	 * createGeometryReader.
	 * </p>
	 *
	 * <p>
	 * JG- I have implemented getFeatureReader( FeatureType, Filter,
	 * Transasction) ontop of this method, it will Retype as required
	 * </p>
	 *
	 * @param query
	 *            The Query to get a  FeatureReader<SimpleFeatureType, SimpleFeature> for.
	 * @param trans
	 *            The transaction this read operation is being performed in.
	 *
	 * @return A  FeatureReader<SimpleFeatureType, SimpleFeature> that contains features defined by the query.
	 *
	 * @throws IOException
	 *             If an error occurs executing the query.
	 * @throws DataSourceException
	 */
	public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction trans)
			throws IOException {
		String typeName = query.getTypeName();
		SimpleFeatureType featureType = getSchema(typeName);
		FeatureTypeInfo typeInfo = typeHandler.getFeatureTypeInfo(typeName);

		SQLBuilder sqlBuilder = getSqlBuilder(typeName);
		
		Filter preFilter = (Filter) sqlBuilder.getPreQueryFilter(query.getFilter()); //process in DB
		Filter postFilter = (Filter) sqlBuilder.getPostQueryFilter(query.getFilter()); //process after DB
		
		// simplify the filters before using them, often they contain extra
		// Filter.INCLUDE/Filter.EXCLUDE items
		SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();
		preFilter = (Filter) preFilter.accept(simplifier, null);
		postFilter = (Filter) postFilter.accept(simplifier, null);
		
		//JD: This is bad, we should not assume we have the right to change the query object
		Filter originalFilter = (Filter) query.getFilter();
		((DefaultQuery) query).setFilter(preFilter);
		
		String[] requestedNames = propertyNames(query);
		String[] propertyNames;

		// DJB: changed to account for miss-ordered queries
		if (allSameOrder(requestedNames, featureType)) {
			// because we have everything, the filter can run
			propertyNames = requestedNames;
		} else if (requestedNames.length <= featureType.getAttributeCount()) {
			// we will need to reType this :-)
			//
			// check to make sure we have enough for the post filter
			//
			String[] filterNames = DataUtilities.attributeNames(postFilter, featureType);

			//JD: using a list here to maintain order
			List list = new ArrayList();
			list.addAll(Arrays.asList(requestedNames));
			for ( int i = 0; i < filterNames.length; i++ ) {
				if ( !list.contains( filterNames[ i ] ) ) {
					list.add( filterNames[i] );
				}
			}
			
			if (list.size() == requestedNames.length) {
				propertyNames = requestedNames;
			} else {
				propertyNames = (String[]) list.toArray(new String[list.size()]);
			}

			try {
				typeInfo = new FeatureTypeInfo(typeInfo.getFeatureTypeName(),
						DataUtilities.createSubType(typeInfo.getSchema(),
								propertyNames), typeInfo.getFIDMapper());
			} catch (SchemaException e1) {
				throw new DataSourceException("Could not create subtype", e1);
			}
		} else { // too many requested (duplicates?)
			throw new DataSourceException(typeName
					+ " does not contain requested properties:" + query);
		}

		AttributeDescriptor[] attrTypes = null;

		try {
			attrTypes = getAttributeTypes(typeName, propertyNames, query.getHints());
		} catch (SchemaException schemaException) {
			throw new DataSourceException(
					"Some Attribute Names were specified that"
							+ " do not exist in the FeatureType " + typeName
							+ ". " + "Requested names: "
							+ Arrays.asList(propertyNames) + ", "
							+ "FeatureType: " + featureType, schemaException);
		}

		String sqlQuery = constructQuery(query, attrTypes);
		LOGGER.fine(sqlQuery );
		
		//JD: This is bad, we should not assume we have the right to change the query object
		((DefaultQuery) query).setFilter(originalFilter);
		
		QueryData queryData = executeQuery(typeInfo, typeName, sqlQuery, trans,
				false, query.getHints());

		SimpleFeatureType schema;

		try {
			SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
			ftb.setName(typeName);
			ftb.addAll(attrTypes);
			ftb.setNamespaceURI(getNameSpace());
			schema = ftb.buildFeatureType();
		} catch (FactoryRegistryException e) {
			throw new DataSourceException(
					"Schema Factory Error when creating schema for FeatureReader",
					e);
		}

		FeatureReader<SimpleFeatureType, SimpleFeature> reader;
		reader = createFeatureReader(schema, postFilter, queryData);

		try {
			SimpleFeatureType requestType = DataUtilities.createSubType(schema,
					requestedNames);
			if ( !FeatureTypes.equalsExact(requestType, reader.getFeatureType()) ) {
                reader = new ReTypeFeatureReader(reader, requestType, false);
            }
        } catch (SchemaException schemaException) {
            throw new DataSourceException("Could not handle query",
                    schemaException);
        }

        // chorner: this is redundant, since we've already created the reader with the post filter attached		
        // if (postFilter != null && !postFilter.equals(Filter.INCLUDE)) {
        //     reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, postFilter);
        // }
		
		return reader;
	}

	/**
	 * Used internally to call the subclass hooks that construct the SQL query.
	 *
	 * @param query
	 * @param attrTypes
	 *
	 *
	 * @throws IOException
	 * @throws DataSourceException
	 */
	private String constructQuery(Query query, AttributeDescriptor[] attrTypes)
			throws IOException, DataSourceException {
		String typeName = query.getTypeName();
		SQLBuilder sqlBuilder = getSqlBuilder(query.getTypeName());
		sqlBuilder.setHints( query.getHints() ); // hints will control FEATURE_2D etc...
        
		org.opengis.filter.Filter preFilter = sqlBuilder.getPreQueryFilter(query.getFilter()); //dupe?
		//Filter postFilter = sqlBuilder.getPostQueryFilter(query.getFilter());

		FIDMapper mapper = getFIDMapper(typeName);

		String sqlQuery;
		//FeatureTypeInfo info = typeHandler.getFeatureTypeInfo(typeName);
		//boolean useMax = (postFilter == null); // not used yet

		try {
			LOGGER.fine("calling sql builder with filter " + preFilter);

			if (query.getFilter() == Filter.EXCLUDE) {
				StringBuffer buf = new StringBuffer("SELECT ");
				sqlBuilder.sqlColumns(buf, mapper, attrTypes);
				sqlBuilder.sqlFrom(buf, typeName);
				buf.append(" WHERE '1' = '0'"); // NO-OP it
				sqlQuery = buf.toString();
			} else {
				final Integer offset = query.getStartIndex();
                Integer limit = null;
                if(query.getMaxFeatures() != Integer.MAX_VALUE){
                    limit = new Integer(query.getMaxFeatures());
                }
                sqlQuery = sqlBuilder.buildSQLQuery(typeName, mapper,
						attrTypes, preFilter, query.getSortBy(), offset, limit);
			}

			LOGGER.fine("sql is " + sqlQuery);
		} catch (SQLEncoderException e) {
			throw new DataSourceException("Error building SQL Query", e);
		}

		return sqlQuery;
	}

	/**
	 * Create a new  FeatureReader<SimpleFeatureType, SimpleFeature> based on attributeReaders.
	 *
	 * <p>
	 * The provided <code>schema</code> describes the attributes in the
	 * queryData ResultSet. This schema should cover the requirements of
	 * <code>filter</code>.
	 * </p>
	 *
	 * <p>
	 * Retyping to the users requested Schema will not happen in this method.
	 * </p>
	 *
	 * @param schema
	 * @param postFilter
	 *            Filter for post processing, or <code>null</code> if not
	 *            required.
	 * @param queryData
	 *            Holds a ResultSet for attribute Readers
	 *
	 *
	 * @throws IOException
	 */
	protected  FeatureReader<SimpleFeatureType, SimpleFeature> createFeatureReader(SimpleFeatureType schema,
			org.opengis.filter.Filter postFilter, QueryData queryData) throws IOException {
        
        // Thanks Shaun Forbes moving excludes check earlier
        if (postFilter == Filter.EXCLUDE) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(schema);
        }
        
         FeatureReader<SimpleFeatureType, SimpleFeature> fReader = getJDBCFeatureReader(queryData);

		if ((postFilter != null) && (postFilter != Filter.INCLUDE)) {
			fReader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(fReader, postFilter);
		}

		
		return fReader;
	}

	protected JDBCFeatureReader getJDBCFeatureReader(QueryData queryData)
			throws IOException {
		return new JDBCFeatureReader(queryData);
	}

	// protected final AttributeReader createAttributeReader(AttributeDescriptor[]
	// attrTypes, int fidColumnsCount, ResultSet rs) {
	// AttributeIO[] attributeIO = new AttributeIO[attrTypes.length];
	// for(int i = 0; i < attributeIO.length; i++) {
	// if(attrTypes[i].isGeometry()) {
	// attributeIO[i] = getGeometryAttributeIO(attrTypes[i]);
	// } else {
	// attributeIO[i] = getAttributeIO(attrTypes[i]);
	// }
	//
	// }
	// return new JDBCFeatureReader(attrTypes, attributeIO, fidColumnsCount,
	// rs);
	// }

	/**
	 * Returns the basic AttributeIO that can read and write all of the simple
	 * data types
	 *
	 * @param type
	 *
	 */
	protected AttributeIO getAttributeIO(AttributeDescriptor type) {
		if (basicAttributeIO == null) {
			basicAttributeIO = new BasicAttributeIO();
		}

		return basicAttributeIO;
	}

	/**
	 * Hook to create the geometry attribute IO for a vendor specific data
	 * source.
	 *
	 * @param type
	 *            The AttributeDescriptor to read.
	 * @param queryData
	 *            The connection holder
	 *
	 * @return The AttributeIO that will read and write the geometry from the
	 *         results.
	 *
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	protected abstract AttributeIO getGeometryAttributeIO(AttributeDescriptor type,
			QueryData queryData) throws IOException;
	
	/**
	 * See the full version with hints support
	 * @param featureTypeInfo
	 * @param tableName
	 * @param sqlQuery
	 * @param transaction
	 * @param forWrite
	 * @return
	 * @throws IOException
	 */
	protected QueryData executeQuery(FeatureTypeInfo featureTypeInfo,
                String tableName, String sqlQuery, Transaction transaction,
                boolean forWrite) throws IOException {
	    return executeQuery(featureTypeInfo, tableName, sqlQuery, transaction, forWrite, null);
	}

	/**
	 * Executes the SQL Query.
	 *
	 * <p>
	 * This is private in the expectation that subclasses should not need to
	 * change this behaviour.
	 * </p>
	 *
	 * <p>
	 * Jody with a question here - I have stopped this method from closing
	 * connection shared by a Transaction. It sill seems like we are leaving
	 * connections open by using this method. I have also stopped QueryData from
	 * doing the same thing.
	 * </p>
	 *
	 * <p>
	 * Answer from Sean: Resources for successful queries are closed when close
	 * is called on the AttributeReaders constructed with the QueryData. We
	 * can't close them here since they need to be open to read from the
	 * ResultSet.
	 * </p>
	 *
	 * <p>
	 * Jody AttributeReader question: I looked at the code and Attribute Readers
	 * do not close with respect to Transactions (they need to as we can issue a
	 * Reader against a Transaction. I have changed the JDBCDataStore.close
	 * method to force us to keep track of these things.
	 * </p>
	 *
	 * <p>
	 * SG: I've marked this as final since I don't think it shoudl be overriden,
	 * but Im not sure
	 * </p>
	 *
	 * @param featureTypeInfo
	 * @param tableName
	 * @param sqlQuery
	 *            The SQL query to execute.
	 * @param transaction
	 *            The Transaction is included here for handling transaction
	 *            connections at a later stage. It is not currently used.
	 * @param forWrite
	 * @param hints the {@link Query} hints
	 *
	 * @return The QueryData object that contains the resources for the query.
	 * 
	 *
	 * @throws IOException
	 * @throws DataSourceException
	 *             If an error occurs performing the query.
	 *
	 * @task HACK: This is just protected for postgis FeatureWriter purposes.
	 *       Should move back to private when that stuff moves more abstract
	 *       here.
	 */
	protected QueryData executeQuery(FeatureTypeInfo featureTypeInfo,
			String tableName, String sqlQuery, Transaction transaction,
			boolean forWrite, Hints hints) throws IOException {
		LOGGER.fine("About to execute query: " + sqlQuery);

		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;

		try {
			conn = getConnection(transaction);

			setAutoCommit(forWrite, conn);
			statement = conn.createStatement(getResultSetType(forWrite),
					getConcurrency(forWrite));
			statement.setFetchSize(1000);
			rs = statement.executeQuery(sqlQuery);

			return new QueryData(featureTypeInfo, this, conn, statement, rs,
					transaction, hints);
		} catch (SQLException e) {
			// if an error occurred we close the resources
			String msg = "Error Performing SQL query: " + sqlQuery;
			LOGGER.log(Level.SEVERE, msg, e);
			JDBCUtils.close(rs);
			JDBCUtils.close(statement);
			JDBCUtils.close(conn, transaction, e);
			throw new DataSourceException(msg, e);
		}
	}

	/**
	 * This method should be overridden to do nothing by DataStores where
	 * setting autoCommit causes funky behaviour (ie. anytime autoCommit is
	 * changed, every thing up to that point is committed...this isn't good at
	 * this stage)
	 *
	 * @param forWrite
	 * @param conn
	 * @throws SQLException
	 */
	protected void setAutoCommit(boolean forWrite, Connection conn)
			throws SQLException {
		if (!forWrite) {
			// for postgis streaming, but I don't believe it hurts anyone.
			conn.setAutoCommit(false);
		}
	}

	protected int getResultSetType(boolean forWrite) {
		return ResultSet.TYPE_FORWARD_ONLY;
	}

	protected int getConcurrency(boolean forWrite) {
		if (forWrite) {
			return ResultSet.CONCUR_UPDATABLE;
		} else {
			return ResultSet.CONCUR_READ_ONLY;
		}
	}

	/**
	 * Hook for subclass to return a different sql builder.
	 * <p>
	 * Subclasses requiring a ClientTransactionAccessor should override
	 * and instantiate an SQLBuilder with one in the constructor. 
	 * 
	 * @param typeName
	 *            The typename for the sql builder.
	 *
	 * @return A new sql builder.
	 *
	 * @throws IOException
	 *             if anything goes wrong.
	 */
	public SQLBuilder getSqlBuilder(String typeName) throws IOException {
		SimpleFeatureType schema = getSchema( typeName );
		SQLEncoder encoder = new SQLEncoder( );
		encoder.setFeatureType( schema );
		encoder.setFIDMapper(getFIDMapper(typeName));

		return new DefaultSQLBuilder(encoder,schema,null);
	}

	/**
	 * Gets a connection for the provided transaction.
	 *
	 * @param transaction
	 * @return A single use connection.
	 *
	 * @throws IOException
	 * @throws DataSourceException
	 *             If the connection can not be obtained.
	 */
     public Connection getConnection(Transaction transaction)
            throws IOException {
        if (transaction != Transaction.AUTO_COMMIT) {
            // we will need to save a JDBC connection is
            // transaction.putState( connectionPool, JDBCState )
            // throw new UnsupportedOperationException("Transactions not
            // supported yet");
            
            JDBCTransactionState state;
            synchronized (transaction) {
                
                state = (JDBCTransactionState) transaction
    					.getState(this);
                
                if (state == null) {
                    try {
                        Connection conn = createConnection();
                        conn.setAutoCommit(requireAutoCommit());
                        if (getTransactionIsolation() != Connection.TRANSACTION_NONE) {
                            // for us, NONE means use the default, which is
                            // usually READ_COMMITTED
                            conn.setTransactionIsolation(getTransactionIsolation());
                        }
                        state = new JDBCTransactionState(conn);
                        transaction.putState(this, state);
                    } catch (SQLException eep) {
                        throw new DataSourceException("Connection failed:"
                                + eep, eep);
                    }
                }
            }
            return state.getConnection();
        }

        try {
            return createConnection();
        } catch (SQLException sqle) {
            throw new DataSourceException("Connection failed:" + sqle, sqle);
        }
    }

	/**
     * Obtain the transaction isolation level for connections.
     * 
     * @return Connection.TRANSACTION_* value
     * @since 2.2.0
     * @see setTransactionIsolation
     * @see <a href="http://www.postgresql.org/docs/7.4/static/transaction-iso.html">This web page</a>
     */
    public int getTransactionIsolation() {
        return transactionIsolation;
    }
     
    /**
     * Sets the transaction isolation level for connections.
     * 
     * @param value
     *            Connection.TRANSACTION_READ_UNCOMMITTED,
     *            Connection.TRANSACTION_READ_COMMITTED,
     *            Connection.TRANSACTION_REPEATABLE_READ,
     *            Connection.SERIALIZABLE, or Connection.TRANSACTION_NONE
     *            (for use default/do not set)
     * @since 2.2.0
     * @see <a href="http://www.postgresql.org/docs/7.4/static/transaction-iso.html">This web page</a>
     */
    public void setTransactionIsolation(int value) {
        transactionIsolation = value;
    }
     
    /**
     * Return true if transaction is handled on client.  Usually this will not have to be overridden.
     * @return true if transaction is handled on client.  Usually this will not have to be overridden.
     */
	protected boolean requireAutoCommit() {
        return false;
    }

    /**
	 * Create a connection for your JDBC1 database
	 */
	protected abstract Connection createConnection() throws SQLException;

	/**
	 * Provides a hook for sub classes to filter out specific tables in the data
	 * store that are not to be used as geospatial tables. The default
	 * implementation of this method is to allow all tables.
	 *
	 * @param tablename
	 *            A table name to check.
	 *
	 * @return True if the table should be exposed as a FeaturSimpleFeatureTypee if it
	 *         should be ignored.
	 */
	protected boolean allowTable(String tablename) {
		return true;
	}

	/**
	 * Builds the appropriate FID mapper given a table name and a FID mapper
	 * factory
	 *
	 * @param typeName
	 * @param factory
	 *
	 *
	 * @throws IOException
	 */
	protected FIDMapper buildFIDMapper(String typeName, FIDMapperFactory factory)
			throws IOException {
		Connection conn = null;

		try {
			conn = getConnection(Transaction.AUTO_COMMIT);

			FIDMapper mapper = factory.getMapper(null, config.getDatabaseSchemaName(), typeName, conn);

			return mapper;
		} finally {
			JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
		}
	}

	/**
	 * Builds the schema for a table in the database.
	 *
	 * <p>
	 * This works by retrieving the column information for the table from the
	 * DatabaseMetaData object. It then iterates over the information for each
	 * column, calling buildAttributeType(ResultSet) to construct an
	 * AttributeDescriptor for each column. The list of attribute types is then turned
	 * into a FeatureType that defines the schema.
	 * </p>
	 *
	 * <p>
	 * It is not intended that this method is overriden. It should provide the
	 * required functionality for most sub-classes. To add AttributeDescriptor
	 * construction for vendor specific SQL types, such as geometries, override
	 * the buildAttributeType(ResultSet) method.
	 * </p>
	 *
	 * <p>
	 * This may become final later. In fact Ill make it private because I don't
	 * think It will need to be overriden.
	 * </p>
	 *
	 * @param typeName
	 *            The name of the table to construct a feature type for.
	 * @param mapper
	 *            The name of the column holding the fid.
	 *
	 * @return The FeatureType for the table.
	 *
	 * @throws IOException
	 * @throws DataSourceException
	 *             This can occur if there is an SQL error or an error
	 *             constructing the FeatureType.
	 *
	 * @see JDBC1DataStore#buildAttributeType(ResultSet)
	 */
	protected SimpleFeatureType buildSchema(String typeName, FIDMapper mapper)
			throws IOException {
		final int NAME_COLUMN = 4;
		final int TYPE_NAME = 6;
		Connection conn = null;
		ResultSet tableInfo = null;

		try {
			conn = getConnection(Transaction.AUTO_COMMIT);

			DatabaseMetaData dbMetaData = conn.getMetaData();

			List attributeTypes = new ArrayList();

			tableInfo = dbMetaData.getColumns(null, config.getDatabaseSchemaName(), typeName, "%");

			boolean tableInfoFound = false;

			while (tableInfo.next()) {
				tableInfoFound = true;

				try {
					String columnName = tableInfo.getString(NAME_COLUMN);

					if (!mapper.returnFIDColumnsAsAttributes()) {
						boolean isPresent = false;

						for (int i = 0; i < mapper.getColumnCount(); i++) {
							if (columnName.equalsIgnoreCase(mapper
									.getColumnName(i))) {
								isPresent = true;

								break;
							}
						}

						if (isPresent) {
							continue;
						}
					}

					AttributeDescriptor attributeType = buildAttributeType(tableInfo);

					if (attributeType != null) {
						attributeTypes.add(attributeType);
					} else {
						LOGGER.finest("Unknown SQL Type: "
								+ tableInfo.getString(TYPE_NAME));
					}
				} catch (DataSourceException dse) {
					String msg = "Error building attribute type. The column will be ignored";
					LOGGER.log(Level.WARNING, msg, dse);
				}
			}

			if (!tableInfoFound) {
				throw new SchemaNotFoundException(typeName);
			}

			AttributeDescriptor[] types = (AttributeDescriptor[]) attributeTypes
					.toArray(new AttributeDescriptor[0]);

			SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
			ftb.setName(typeName);
			ftb.addAll(types);
			ftb.setNamespaceURI(getNameSpace());
			return ftb.buildFeatureType();
		} catch (SQLException sqlException) {
			JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
			conn = null; // prevent finally block from reclosing
			throw new DataSourceException("SQL Error building FeatureType for "
					+ typeName + " " + sqlException.getMessage(), sqlException);
		} catch (FactoryRegistryException e) {
			throw new DataSourceException("Error creating FeatureType "
					+ typeName, e);
		} finally {
			JDBCUtils.close(tableInfo);
			JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
		}
	}

	/**
	 * Constructs an AttributeDescriptor from a row in a ResultSet. The ResultSet
	 * contains the information retrieved by a call to getColumns() on the
	 * DatabaseMetaData object. This information can be used to construct an
	 * Attribute Type.
	 *
	 * <p>
	 * The default implementation constructs an AttributeDescriptor using the default
	 * JDBC type mappings defined in JDBCDataStore. These type mappings only
	 * handle native Java classes and SQL standard column types, so to handle
	 * Geometry columns, sub classes should override this to check if a column
	 * is a geometry column, if it is a geometry column the appropriate
	 * determination of the geometry type can be performed. Otherwise,
	 * overriding methods should call super.buildAttributeType.
	 * </p>
	 *
	 * <p>
	 * Note: Overriding methods must never move the current row pointer in the
	 * result set.
	 * </p>
	 *
	 * @param rs
	 *            The ResultSet containing the result of a
	 *            DatabaseMetaData.getColumns call.
	 *
	 * @return The AttributeDescriptor built from the ResultSet or null if the column
	 *         should be excluded from the schema.
	 *
	 * @throws IOException
	 *             If an error occurs processing the ResultSet.
	 */
	protected AttributeDescriptor buildAttributeType(ResultSet rs) throws IOException {
		try {
			final int COLUMN_NAME = 4;
			final int DATA_TYPE = 5;
			final int NULLABLE = 11;
			final int LENGTH = 7;
			
			String columnName = rs.getString(COLUMN_NAME);
			int dataType = rs.getInt(DATA_TYPE);
			Class type = (Class) TYPE_MAPPINGS.get(new Integer(dataType));
			
			//check for nullability
			int nullCode = rs.getInt( NULLABLE );
			boolean nillable = true;
			switch( nullCode ) {
				case DatabaseMetaData.columnNoNulls:
					nillable = false;
					break;
					
				case DatabaseMetaData.columnNullable:
					nillable = true;
					break;
					
				case DatabaseMetaData.columnNullableUnknown:
					nillable = true;
					break;
			}
			
			if (type == null) {
				return null;
			} else {
				int min = nillable ? 0 : 1;
				//JD: We would like to set the nillable flag properly here, but there is a lot of 
				// code that sets the value of an attribute to be null while building a feature, 
				// think of feature readers that have to copy content, so we always set it to true,
				// perhaps with the new feature model things like this will be fished out
				//return AttributeTypeFactory.newAttributeType(columnName, type, nillable, null, null, null, min, 1 );
				AttributeTypeBuilder atb = new AttributeTypeBuilder();
				atb.setName(columnName);
				atb.setBinding(type);
				atb.setMinOccurs(min);
				atb.setMaxOccurs(1);
//				atb.setLength( rs.getInt( LENGTH ) );
//				atb.setNillable(nillable);
				return atb.buildDescriptor(columnName);
			}
		} catch (SQLException e) {
			throw new IOException("SQL exception occurred: " + e.getMessage());
		}
	}

    /**
	 * Provides a hook for subclasses to determine the SRID of a geometry
	 * column.
	 *
	 * <p>
	 * This allows SRIDs to be determined in a Vendor specific way and to be
	 * cached by the default implementation. To retreive these srids, get the
	 * FeatureTypeInfo object for the table and call
	 * getSRID(geometryColumnName). This will allow storage of SRIDs for
	 * multiple geometry columns in each table.
	 * </p>
	 *
	 * <p>
	 * If no SRID can be found, subclasses should return -1. The default
	 * implementation always returns -1.
	 * </p>
	 *
	 * @param tableName
	 *            The name of the table to get the SRID for.
	 * @param geometryColumnName
	 *            The name of the geometry column within the table to get SRID
	 *            for.
	 *
	 * @return The SRID for the geometry column in the table or -1.
	 *
	 * @throws IOException
	 */
	protected int determineSRID(String tableName, String geometryColumnName)
			throws IOException {
		return -1;
	}

	/**
	 * Provides the default implementation of determining the FID column.
	 *
	 * <p>
	 * The default implementation of determining the FID column name is to use
	 * the primary key as the FID column. If no primary key is present, null
	 * will be returned. Sub classes can override this behaviour to define
	 * primary keys for vendor specific cases.
	 * </p>
	 *
	 * <p>
	 * There is an unresolved issue as to what to do when there are multiple
	 * primary keys. Maybe a restriction that table much have a single column
	 * primary key is appropriate.
	 * </p>
	 *
	 * <p>
	 * This should not be called by subclasses to retreive the FID column name.
	 * Instead, subclasses should call getFeatureTypeInfo(String) to get the
	 * FeatureTypeInfo for a feature type and get the fidColumn name from the
	 * fidColumn name memeber.
	 * </p>
	 *
	 * @param typeName
	 *            The name of the table to get a primary key for.
	 *
	 * @return The name of the primay key column or null if one does not exist.
	 *
	 * @throws IOException
	 *             This will only occur if there is an error getting a
	 *             connection to the Database.
	 */
	protected String determineFidColumnName(String typeName) throws IOException {
		final int NAME_COLUMN = 4;
		String fidColumnName = null;
		ResultSet rs = null;
		Connection conn = null;

		try {
			conn = getConnection(Transaction.AUTO_COMMIT);

			DatabaseMetaData dbMetadata = conn.getMetaData();
			rs = dbMetadata.getPrimaryKeys(null, null, typeName);

			if (rs.next()) {
				fidColumnName = rs.getString(NAME_COLUMN);
			}
		} catch (SQLException sqlException) {
			JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
			conn = null; // prevent finally block from reclosing
			LOGGER
					.warning("Could not find the primary key - using the default");
		} finally {
			JDBCUtils.close(rs);
			JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
		}

		return fidColumnName;
	}

	/**
	 * Gets the namespace of the data store. TODO: change config over to use URI
	 *
	 * @return The namespace.
	 */
	public URI getNameSpace() {
		try {
			if (config.getNamespace() != null) {
				return new URI(config.getNamespace());
			}
		} catch (URISyntaxException e) {
			LOGGER.warning("Could not use namespace " + config.getNamespace()
					+ " - " + e.getMessage());

			return null;
		}

		return null;
	}
	
	/**
	 * Returns the database schema used by this JDBC datastore 
	 * @return
	 */
	public String getDatabaseSchemaName() {
	    return config.getDatabaseSchemaName();
	}

	/**
	 * Retrieve a FeatureWriter over entire dataset.
	 *
	 * <p>
	 * Quick notes: This FeatureWriter is often used to add new content, or
	 * perform summary calculations over the entire dataset.
	 * </p>
	 *
	 * <p>
	 * Subclass may wish to implement an optimized featureWriter for these
	 * operations.
	 * </p>
	 *
	 * <p>
	 * It should provide Feature for next() even when hasNext() is
	 * <code>false</code>.
	 * </p>
	 *
	 * <p>
	 * Subclasses are responsible for checking with the lockingManger unless
	 * they are providing their own locking support.
	 * </p>
	 *
	 * @param typeName
	 * @param transaction
	 *
	 *
	 * @throws IOException
	 *
	 * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String,
	 *      boolean, org.geotools.data.Transaction)
	 */
	public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
			Transaction transaction) throws IOException {
		return getFeatureWriter(typeName, Filter.INCLUDE, transaction);
	}

	/**
	 * Retrieve a FeatureWriter for creating new content.
	 *
	 * <p>
	 * Subclass may wish to implement an optimized featureWriter for this
	 * operation. One based on prepaired statemnts is a possibility, as we do
	 * not require a ResultSet.
	 * </p>
	 *
	 * <p>
	 * To allow new content the FeatureWriter should provide Feature for next()
	 * even when hasNext() is <code>false</code>.
	 * </p>
	 *
	 * <p>
	 * Subclasses are responsible for checking with the lockingManger unless
	 * they are providing their own locking support.
	 * </p>
	 *
	 * @param typeName
	 * @param transaction
	 *
	 *
	 * @throws IOException
	 *
	 * @see org.geotools.data.DataStore#getFeatureWriter(java.lang.String,
	 *      boolean, org.geotools.data.Transaction)
	 */
	public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
			Transaction transaction) throws IOException {
		FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getFeatureWriter(typeName, Filter.EXCLUDE,
				transaction);

		while (writer.hasNext()) {
			writer.next(); // this would be a use for skip then :-)
		}

		return writer;
	}

	/**
	 * Acquire FeatureWriter for modification of contents specifed by filter.
	 *
	 * <p>
	 * Quick notes: This FeatureWriter is often used to remove contents
	 * specified by the provided filter, or perform summary calculations.
	 * </p>
	 *
	 * <p>
	 * Subclasses are responsible for checking with the lockingManager unless
	 * they are providing their own locking support.
	 * </p>
	 *
	 * @param typeName
	 * @param filter
	 * @param transaction
	 *
	 *
	 * @throws IOException
	 *             If typeName could not be located
	 * @throws NullPointerException
	 *             If the provided filter is null
	 * @throws DataSourceException
	 */
	public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, org.opengis.filter.Filter filter,
			Transaction transaction) throws IOException {
		if (filter == null) {
			throw new NullPointerException("getFeatureReader requires Filter: "
					+ "did you mean Filter.INCLUDE?");
		}

		if (transaction == null) {
			throw new NullPointerException(
					"getFeatureReader requires Transaction: "
							+ "did you mean Transaction.AUTO_COMMIT");
		}

		SimpleFeatureType featureType = getSchema(typeName);
		FeatureTypeInfo info = typeHandler.getFeatureTypeInfo(typeName);
		LOGGER.fine("getting feature writer for " + typeName + ": " + info);

		SQLBuilder sqlBuilder = getSqlBuilder(typeName);
		org.opengis.filter.Filter preFilter = sqlBuilder.getPreQueryFilter(filter);
        org.opengis.filter.Filter postFilter = sqlBuilder.getPostQueryFilter(filter);
		Query query = new DefaultQuery(typeName, preFilter);
		String sqlQuery;

		try {
			sqlQuery = constructQuery(query, getAttributeTypes(typeName,
					propertyNames(query), null));
		} catch (SchemaException e) {
			throw new DataSourceException(
					"Some Attribute Names were specified that"
							+ " do not exist in the FeatureType " + typeName
							+ ". " + "Requested names: "
							+ Arrays.asList(query.getPropertyNames()) + ", "
							+ "FeatureType: " + featureType, e);
		}

		QueryData queryData = executeQuery(typeHandler
				.getFeatureTypeInfo(typeName), typeName, sqlQuery, transaction,
				true, null);
		FeatureReader<SimpleFeatureType, SimpleFeature> reader = createFeatureReader(info.getSchema(),
				postFilter, queryData);
		FeatureWriter<SimpleFeatureType, SimpleFeature> writer = createFeatureWriter(reader, queryData);

		if ((getLockingManager() != null)
				&& getLockingManager() instanceof InProcessLockingManager) {
			InProcessLockingManager inProcess = (InProcessLockingManager) getLockingManager();
			writer = inProcess.checkedWriter(writer, transaction);
		}

        // chorner: writer shouldn't have a wrapped post filter, otherwise one can't add features.
        // if ((postFilter != null) && (postFilter != Filter.INCLUDE)) {
        //     writer = new FilteringFeatureWriter(writer, postFilter);
        // }

		return writer;
	}

	protected JDBCFeatureWriter createFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> reader,
			QueryData queryData) throws IOException {
		LOGGER.fine("returning jdbc feature writer");

		JDBCFeatureWriter featureWriter = new JDBCFeatureWriter(reader, queryData);        
        return featureWriter;
	}

	/**
	 * Get propertyNames in a safe manner.
	 *
	 * <p>
	 * Method will figure out names from the schema for query.getTypeName(), if
	 * query getPropertyNames() is <code>null</code>, or
	 * query.retrieveAllProperties is <code>true</code>.
	 * </p>
	 *
	 * @param query
	 *
	 *
	 * @throws IOException
	 */
	protected String[] propertyNames(Query query) throws IOException {
		String[] names = query.getPropertyNames();

		if ((names == null) || query.retrieveAllProperties()) {
			String typeName = query.getTypeName();
			SimpleFeatureType schema = getSchema(typeName);

			names = new String[schema.getAttributeCount()];

			for (int i = 0; i < schema.getAttributeCount(); i++) {
				names[i] = schema.getDescriptor(i).getLocalName();
			}
		}

		return names;
	}

	/**
	 * Gets the attribute types from from a given type.
	 *
	 * @param typeName
	 *            The name of the feature type to get the AttributeTypes for.
	 * @param propertyNames
	 *            The list of propertyNames to get AttributeTypes for.
	 *
	 * @return the array of attribute types from the schema which match
	 *         propertyNames.
	 *
	 * @throws IOException
	 *             If we can't get the schema.
	 * @throws SchemaException
	 *             if query contains a propertyName that is not a part of this
	 *             type's schema.
	 */
	protected final AttributeDescriptor[] getAttributeTypes(String typeName,
			String[] propertyNames, Hints hints) throws IOException, SchemaException {
		SimpleFeatureType schema = getSchema(typeName);
		AttributeDescriptor[] types = new AttributeDescriptor[propertyNames.length];

		for (int i = 0; i < propertyNames.length; i++) {
			types[i] = schema.getDescriptor(propertyNames[i]);

			if (types[i] == null) {
				throw new SchemaException(typeName
						+ " does not contain requested " + propertyNames[i]
						+ " attribute");
			}
			
			// if we are asked to return a 2d feature make sure to return the
			// proper coordinate dimension
			if(hints != null && hints.containsKey(Hints.FEATURE_2D) 
			        && Boolean.TRUE.equals(hints.get(Hints.FEATURE_2D))
			        && types[i] instanceof GeometryDescriptor &&
			        !Integer.valueOf(2).equals(types[i].getUserData().get(Hints.COORDINATE_DIMENSION))) {
			    AttributeTypeBuilder builder = new AttributeTypeBuilder();
			    builder.init(types[i]);
			    builder.userData(Hints.COORDINATE_DIMENSION, 2);
			    GeometryType type = builder.buildGeometryType();
			    types[i] = builder.buildDescriptor(types[i].getName(), type);
			}
		}

		return types;
	}

	/**
	 * Locking manager used for this DataStore.
	 *
	 * <p>
	 * By default AbstractDataStore makes use of InProcessLockingManager.
	 * </p>
	 *
	 *
	 * @see org.geotools.data.DataStore#getLockingManager()
	 */
	public LockingManager getLockingManager() {
		return lockingManager;
	}

	/**
	 * Sets the FIDMapper for a specific type name
	 *
	 * @param featureTypeName
	 * @param fidMapper
	 */
	public void setFIDMapper(String featureTypeName, FIDMapper fidMapper) {
		typeHandler.setFIDMapper(featureTypeName, fidMapper);
	}

	/**
	 * Returns the FIDMapperFactory used for this data store
	 *
	 */
	public FIDMapperFactory getFIDMapperFactory() {
		return typeHandler.getFIDMapperFactory();
	}

	/**
	 * Allows to override the default FIDMapperFactory.
	 *
	 * <p>
	 * Warning: the ovveride may not be supported by all data stores, in this
	 * case an exception will be thrown
	 * </p>
	 *
	 * @param fmFactory
	 *
	 * @throws UnsupportedOperationException -
	 *             if the datastore does not allow the factory override
	 */
	public void setFIDMapperFactory(FIDMapperFactory fmFactory)
			throws UnsupportedOperationException {
		typeHandler.setFIDMapperFactory(fmFactory);
	}

	/**
	 * returns true if the requested names list all the attributes in the
	 * correct order.
	 *
	 * @param requestedNames
	 * @param ft
	 */
	public boolean allSameOrder(String[] requestedNames, SimpleFeatureType ft) {
		if (requestedNames.length != ft.getAttributeCount())
			return false; // incorrect # of attribute
		for (int t = 0; t < requestedNames.length; t++) {
			if (!(requestedNames[t].equals(ft.getDescriptor(t).getLocalName())))
				return false; // name doesnt match
		}
		return true;
	}

    /**
     * Retrieve approx bounds of all Features.
     * <p>
     * This result is suitable for a quick map display, illustrating the data.
     * This value is often stored as metadata in databases such as oraclespatial.
     * </p>
     * @return null as a generic implementation is not provided.
     */
    public Envelope getEnvelope( String typeName ){
    	return null;
    }
    
    private static final Set BASE_HINTS = Collections.unmodifiableSet(
            new HashSet(Arrays.asList(new Object[] {
            Hints.FEATURE_DETACHED})));

    public Set getSupportedHints() {
        return BASE_HINTS;
    }

    /**
     * Delegates to {@link #getFeatureSource(String)} with
     * {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public SimpleFeatureSource getFeatureSource(Name typeName)
            throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    /**
     * Returns the same list of names than {@link #getTypeNames()} meaning the
     * returned Names have no namespace set.
     * 
     * @since 2.5
     * @see DataAccess#getNames()
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
     * Delegates to {@link #getSchema(String)} with {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getSchema(Name)
     */
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }

    /**
     * Delegates to {@link #updateSchema(String, SimpleFeatureType)} with
     * {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        updateSchema(typeName.getLocalPart(), featureType);
    }    
}
