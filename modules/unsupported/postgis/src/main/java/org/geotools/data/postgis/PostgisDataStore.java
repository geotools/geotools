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
package org.geotools.data.postgis;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.FeatureTypeInfo;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.JDBCFeatureLocking;
import org.geotools.data.jdbc.JDBCFeatureSource;
import org.geotools.data.jdbc.JDBCFeatureStore;
import org.geotools.data.jdbc.JDBCFeatureWriter;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.attributeio.WKTAttributeIO;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.geotools.data.postgis.attributeio.EWKTAttributeIO;
import org.geotools.data.postgis.attributeio.PgWKBAttributeIO;
import org.geotools.data.postgis.fidmapper.PostgisFIDMapperFactory;
import org.geotools.data.postgis.referencing.PostgisAuthorityFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureTypes;
import org.geotools.filter.SQLEncoderPostgis;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;


/**
 * Postgis DataStore implementation.
 *
 * <p>
 * This datastore by default will read/write geometries in WKT format.<br>
 * Optionally use of WKB can be turned on, in which case you may want to turn
 * on also the use of the bytea function, that fasten the data trasfer, but
 * that it's available only from version 0.7.2 onwards.
 * </p>
 *
 * @author Chris Holmes, TOPP
 * @author Andrea Aime
 * @author Paolo Rizzi
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: So Paolo Rizzi has a number of improvements in
 *       http://jira.codehuas.org/browse/GEOT-379  I rolled in a few of them,
 *       but  some beg more fundamental questions - like the use of primary
 *       keys - in the geotools model.  See the issue for a bit more
 *       discussion, and I will attempt to write my thoughts up on wiki soon.
 *       -ch
 */
public class PostgisDataStore extends JDBCDataStore implements DataStore {

    /** The logger for the postgis module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.postgis");

    /** Factory for producing geometries (from JTS). */
    protected static GeometryFactory geometryFactory = new GeometryFactory();

    /** Well Known Text reader (from JTS). */
    protected static WKTReader geometryReader = new WKTReader(geometryFactory);

    /** Map of postgis geometries to jts geometries */
    private static Map GEOM_TYPE_MAP = new LinkedHashMap();

    static {
        GEOM_TYPE_MAP.put("GEOMETRY", Geometry.class);
        GEOM_TYPE_MAP.put("POINT", Point.class);
        GEOM_TYPE_MAP.put("POINTM", Point.class);
        GEOM_TYPE_MAP.put("LINESTRING", LineString.class);
        GEOM_TYPE_MAP.put("LINESTRINGM", LineString.class);
        GEOM_TYPE_MAP.put("POLYGON", Polygon.class);
        GEOM_TYPE_MAP.put("POLYGONM", Polygon.class);
        GEOM_TYPE_MAP.put("MULTIPOINT", MultiPoint.class);
        GEOM_TYPE_MAP.put("MULTIPOINTM", MultiPoint.class);
        GEOM_TYPE_MAP.put("MULTILINESTRING", MultiLineString.class);
        GEOM_TYPE_MAP.put("MULTILINESTRINGM", MultiLineString.class);
        GEOM_TYPE_MAP.put("MULTIPOLYGON", MultiPolygon.class);
        GEOM_TYPE_MAP.put("MULTIPOLYGONM", MultiPolygon.class);
        GEOM_TYPE_MAP.put("GEOMETRYCOLLECTION", GeometryCollection.class);
        GEOM_TYPE_MAP.put("GEOMETRYCOLLECTIONM", GeometryCollection.class);

        // SQL MM "Curve" extensions
        GEOM_TYPE_MAP.put("CIRCULARSTRING", LineString.class);
        GEOM_TYPE_MAP.put("COMPOUNDCURVE", LineString.class);
        GEOM_TYPE_MAP.put("CURVEPOLYGON", Polygon.class);
    }

    private static Map CLASS_MAPPINGS = new HashMap();

    static {
        CLASS_MAPPINGS.put(String.class, "VARCHAR");

        CLASS_MAPPINGS.put(Boolean.class, "BOOLEAN");

        CLASS_MAPPINGS.put(Integer.class, "INTEGER");
        CLASS_MAPPINGS.put(Long.class, "BIGINT");
        CLASS_MAPPINGS.put(Float.class, "REAL");
        CLASS_MAPPINGS.put(Double.class, "DOUBLE PRECISION");

        CLASS_MAPPINGS.put(BigDecimal.class, "DECIMAL");

        CLASS_MAPPINGS.put(java.sql.Date.class, "DATE");
        CLASS_MAPPINGS.put(java.util.Date.class, "DATE");
        CLASS_MAPPINGS.put(java.sql.Time.class, "TIME");
        CLASS_MAPPINGS.put(java.sql.Timestamp.class, "TIMESTAMP");
    }

    private static Map GEOM_CLASS_MAPPINGS = new HashMap();

    //why don't we just stick this in with the non-geom class mappings?
    static {
        // init the inverse map
        Set keys = GEOM_TYPE_MAP.keySet();

        for (Iterator it = keys.iterator(); it.hasNext();) {
            String name = (String) it.next();
            Class geomClass = (Class) GEOM_TYPE_MAP.get(name);
            //make sure that the the first found class is returned for "class to geometrytype"
            if (GEOM_CLASS_MAPPINGS.get(geomClass)==null)
                GEOM_CLASS_MAPPINGS.put(geomClass, name);
        }
    }

    /** OPTIMIZE_MODE constants */
    public static final int OPTIMIZE_SAFE = 0;
    public static final int OPTIMIZE_SQL = 1;

    /** Maximum string size for postgres */
    private static final int MAX_ALLOWED_VALUE = 10485760;

    //JD: GEOT-723, keeping this reference static allows the authority factory
    // to hold onto a stale connection pool when a new datastore is created.
    //private static PostgisAuthorityFactory paf = null;
    private PostgisAuthorityFactory paf = null;

    /** PostGIS version information (persisted here so we don't have to keep asking the database what version it is, in perpituity. */
    protected PostgisDBInfo dbInfo;

    /** Enables the use of geos operators */
    protected boolean useGeos;

    /**
     * Current optimize mode
     * @deprecated Dot not use this directly, use {@link #getOptimizeMode()}.
     */
    public int OPTIMIZE_MODE;

    /** If true, WKB format is used instead of WKT */
    protected boolean WKBEnabled = false;

    /**
     * If true, the bytea function will be used to optimize even further data
     * loading when using WKB format
     */
    protected boolean byteaEnabled = false;

    /**
     *  postgis 1.0 changed the way WKB is handled, this needs to be
     *  set if version >1.
     *  (it affects the way you send WKB to the database)
     */
    protected boolean byteaWKB = false;

    /**
     * If true then the bounding box filters will use the && postgis operator,
     * which uses the spatial index and performs against the envelope of the
     * geom, leading to greater speed and slightly less accuracy.
     */
    protected boolean looseBbox;

    /**
     * set to true if the bounds for a table should be computed using the
	 * estimated_extent' function, but beware that this function is less accurate
	 * and in some cases *far* less accurate if the data within the actual bounds
	 * does not follow a uniform distribution.
     */
    protected boolean estimatedExtent;

    /** Flag indicating whether schema support **/
    protected boolean schemaEnabled = true;

    protected PostgisDataStore(DataSource dataSource)
        throws IOException {
        this(dataSource, (String) null);
    }

    protected PostgisDataStore(DataSource dataSource, String namespace)
        throws IOException {
        this(dataSource, schema(null), namespace);
    }

    protected PostgisDataStore(DataSource dataSource, String schema,
        String namespace) throws IOException {
        this(dataSource,
            new JDBCDataStoreConfig(namespace, schema(schema), new HashMap(),
                new HashMap()), OPTIMIZE_SQL);
    }

    protected PostgisDataStore(DataSource dataSource, String schema,
        String namespace, int optimizeMode) throws IOException {
        this(dataSource,
            new JDBCDataStoreConfig(namespace, schema(schema), new HashMap(),
                new HashMap()), optimizeMode);
    }

    /**
     * Simple helper method to ensure that a schema is always set.
     */
    protected static String schema(String schema) {
    	if (schema != null && !"".equals(schema))
    		return schema;

        return (String) PostgisDataStoreFactory.SCHEMA.sample;
    }

    public PostgisDataStore(DataSource dataSource,
        JDBCDataStoreConfig config, int optimizeMode) throws IOException {
        super(dataSource, config);
        guessDataStoreOptions();
        OPTIMIZE_MODE = optimizeMode;

        // use the specific postgis fid mapper factory
        setFIDMapperFactory( buildFIDMapperFactory( config ) );
    }

    /**
     * Allows subclass to create LockingManager to support their needs.
     *
     */
    protected LockingManager createLockingManager() {
        return new InProcessLockingManager();
    }

    /**
     * Creates a new sql builder for encoding raw sql statements;
     *
     */
    protected PostgisSQLBuilder createSQLBuilder() {
        PostgisSQLBuilder builder = new PostgisSQLBuilder(new SQLEncoderPostgis(), config);
        initBuilder(builder);
        return builder;
    }

    /**
     * Attempts to figure out some optimization options, based on some postgis
     * metadata.  If the version is later than 0.7.2 then bytea will be used
     * to read geometries if WKB is enabled.  And it will read if GEOS is
     * enabled from the version string as well.
     *
     * @throws IOException
     */
    protected void guessDataStoreOptions() throws IOException {
        PostgisDBInfo dbInfo = getDBInfo();
        if (dbInfo == null) { //assume
            LOGGER.severe("Could not obtain PostgisDBInfo");
            byteaEnabled = true;
            byteaWKB = false;
            useGeos = true;
            schemaEnabled = true;
        } else {
            byteaEnabled = dbInfo.isByteaEnabled();
            if (dbInfo.getMajorVersion() >= 1) {
                byteaWKB = true; // force ew wkb writing format
            }
            useGeos = dbInfo.isGeosEnabled();
            schemaEnabled = dbInfo.isSchemaEnabled();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.data.DataStore#getTypeNames()
     */
    public String[] getTypeNames() throws IOException {

        final int TABLE_NAME_COL = 3;
        Connection conn = null;
        List list = new ArrayList();

        try {
            conn = getConnection(Transaction.AUTO_COMMIT);

            DatabaseMetaData meta = conn.getMetaData();  // DB: shouldnt this be done by looking at geometry_columns?  or are you trying to allow non-spatial tables in as well?
            String[] tableType = { "TABLE" , "VIEW"};
            ResultSet tables = meta.getTables(null,
                    config.getDatabaseSchemaName(), "%", tableType);

            while (tables.next()) {
            	String tableName = tables.getString(TABLE_NAME_COL);

                if (allowTable(tableName)) {
                    list.add(tableName);
                }
            }
            tables.close();

            return (String[]) list.toArray(new String[list.size()]);
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
            conn = null;

            String message = "Error querying database for list of tables:"
                + sqlException.getMessage();
            throw new DataSourceException(message, sqlException);
        } finally {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }

    	/*
        //Justin's patch from uDig, should be faster, but untested.
    	Connection conn = null;
		String namespace = config.getNamespace();
		try {
			conn = getConnection(Transaction.AUTO_COMMIT);

			PreparedStatement st = null;

			if (namespace != null && !namespace.trim().equals("")) { //$NON-NLS-1$
				st = conn.prepareStatement(
					"SELECT distinct a.relname "  //$NON-NLS-1$
					+ "FROM pg_class a, pg_attribute b, pg_namespace c, pg_type d " //$NON-NLS-1$
				   + "WHERE a.oid = b.attrelid " //$NON-NLS-1$
				   	 + "AND b.atttypid = d.oid "  //$NON-NLS-1$
				   	 + "AND a.relnamespace = c.oid "  //$NON-NLS-1$
				   	 + "AND c.nspname = ? " //$NON-NLS-1$
				   	 + "AND d.typname = ? " //$NON-NLS-1$
				   	 + "AND a.relname in (SELECT f_table_name FROM geometry_columns)" //$NON-NLS-1$
				);
				st.setString(1, namespace);
				st.setString(2, "geometry"); //$NON-NLS-1$
			}
			else {
				st = conn.prepareStatement(
					"SELECT distinct a.relname "  //$NON-NLS-1$
					+ "FROM pg_class a, pg_attribute b, pg_type d " //$NON-NLS-1$
				   + "WHERE a.oid = b.attrelid " //$NON-NLS-1$
				   	 + "AND b.atttypid = d.oid "  //$NON-NLS-1$
				   	 + "AND d.typname = ? " //$NON-NLS-1$
				   	 + "AND a.relname in (SELECT f_table_name FROM geometry_columns)" //$NON-NLS-1$
				);
				st.setString(1, "geometry"); //$NON-NLS-1$
			}

			ResultSet rs = st.executeQuery();
			ArrayList names = new ArrayList();
			while(rs.next()) {
				String table = rs.getString(1);
				if (allowTable(table)){
					names.add(table);
				}

			}

			return (String[])names.toArray(new String[names.size()]);
		}
		catch (SQLException sqlException) {
	        JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
	        conn = null;
	        throw new DataSourceException( sqlException );
	    }
	    finally {
	        JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
	    }
	    */
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
    	Connection conn = null;

    	try {
    		conn = createConnection();
            Statement st = null;
            ResultSet rs = null;
            Envelope envelope = null;

        	SimpleFeatureType schema = getSchema(typeName);
        	String geomName = schema.getGeometryDescriptor().getLocalName();

	    	// optimization, postgis version >= 1.0 contains estimated_extent
            // function to query the stats of the table to determine the bbox,
            // however, it may return null
		    if (getDBInfo().getMajorVersion() >= 1) {
		    	//try the estimated_extent([schema], table, geocolumn) function
	    	    String q;
                String dbSchema = config.getDatabaseSchemaName();
                if (!schemaEnabled || dbSchema == null || "".equals(dbSchema)) {
                    q = "SELECT AsText(force_2d(envelope(estimated_extent('"+typeName+"','"+geomName+"'))))";
                } else {
                    q = "SELECT AsText(force_2d(envelope(estimated_extent('"+dbSchema+"','"+typeName+"','"+geomName+"'))))";
                }
                st = conn.createStatement();
                rs = st.executeQuery(q);

		    	if (rs.next()) {
		    		//parse return value
		    		String wkt = rs.getString(1);
		    		if (wkt != null &&  !wkt.trim().equals("")) { //$NON-NLS-1$
		    			envelope = geometryReader.read(wkt).getEnvelopeInternal();

		    			// expand the bounds by 20% (10% in each direction)
		    			// Works whether or not the bounds are at the origin
		    			double minX = envelope.getMinX();
		    			double minY = envelope.getMinY();
		    			double maxX = envelope.getMaxX();
		    			double maxY = envelope.getMaxY();
		    			double deltaX = (maxX - minX)*0.1;
		    			double deltaY = (maxY - minY)*0.1;
		    			envelope.expandToInclude(minX - deltaX, minY - deltaY);
		    			envelope.expandToInclude(maxX + deltaX, maxY + deltaY);
		    		} else {
                        LOGGER.warning("PostGIS estimated_extent function did not return a result." +
                                "\nPerhaps 'ANALYZE "+typeName+";' needs to be run or the table is empty?");
                    }
		    	}

		    	rs.close();
		    	st.close();
		    }

		    if (envelope == null) {

                //try to generate an approximation
                envelope = new Envelope();
                //this is an attempt to grab a handful of envelopes without counting the features
                final int blockSize = 10; //how many features to grab on each postgis hit
                final int fetchAllLimit = 99; //if we don't exceed this value, just fetch all features
                //final int upperLimit = 1000000; //aim for this many features
                //final int nBlocks = 7; //number of times to hit postgis
                //int[] offset = new int[nBlocks];
                //automatic range calculation (for tweaking)
                //once we hit 100,000 features in our scan, things get really slow
                //therefore we'll stop around 50k
                //offset[0] = 1;
                //double magicNumber = Math.pow(upperLimit, 1.0 / (nBlocks - 1));
                //for (int i = 1; i < nBlocks; i++) {
                //    offset[i] = (int) Math.ceil(offset[i-1] * magicNumber);
                //    System.out.println(offset[i]);
                //}
                //offset[0] = 0;
                int[] offset = new int[] {0,10,100,1000,10000,20000,40000};

                int hits = 0;
                int misses = 0;
		    	for (int i = 0; i < offset.length && misses < 4; i++) {
                    String limit = " LIMIT " + blockSize + " OFFSET " + offset[i];;
                    if (i+1 < offset.length && offset[i+1]-offset[i] <= blockSize) {
                        limit = " LIMIT " + blockSize * 2 + " OFFSET " + offset[i];
                        offset[i+1] = offset[i] + blockSize;
                        i++;
                    }
                    String q = "SELECT AsText(force_2d(envelope(" + geomName + "))) FROM " + typeName;
                    if (offset[i] > -1) {
                        q = q + limit;
                    }
                    st = conn.createStatement();
                    rs = st.executeQuery(q);
                    boolean gotEnvelope = false;
                    while (rs.next()) {
                        gotEnvelope = true;
                        String wkt = rs.getString(1);
                        if (wkt != null && !wkt.trim().equals("")) { //$NON-NLS-1$
                            Envelope e = geometryReader.read(wkt)
                                .getEnvelopeInternal();

                            if (envelope.isNull())
                                envelope.init(e);
                            else
                                envelope.expandToInclude(e);
                        }
                    }
                    if (gotEnvelope) {
                        hits++;
                    } else {
                        misses++;
                        if (hits == 0) { //there are no features!
                            rs.close();
                            st.close();
                            return new Envelope();
                        }
                        if (offset[i-1] < fetchAllLimit) { //just fetch everything
                            offset[i] = -1;
                        } else {
                            //went beyond the last feature
                            //on our first miss, we move back 50% and try again
                            //on our second miss, we stop guessing and look between last 2 hits
                            int min = offset[i-1];
                            int max = offset[i];
                            if (misses == 2) {
                                min = offset[i-2];
                                max = offset[i-1];
                            }
                            if (misses < 3) {
                                offset[i] = (int) ((min + max) / 2.0);
                                int width = (int) ((max - min) / (double) (offset.length - i));
                                for (int j = i+1; j < offset.length; j++) {
                                    offset[j] = min + (width * (j-i));
                                }
                            } else {
                                rs.close();
                                st.close();
                                break;
                            }
                        }
                        i--;
                    }
                    rs.close();
                    st.close();
                    if (offset[i] == -1)
                        break;
                }

		    	// expand since this is an approximation
		    	// Works whether or not the bounds are at the origin
		    	double minX = envelope.getMinX();
		    	double minY = envelope.getMinY();
		    	double maxX = envelope.getMaxX();
		    	double maxY = envelope.getMaxY();
		    	double deltaX = (maxX - minX)*1.0;
		    	double deltaY = (maxY - minY)*1.0;
		    	envelope.expandToInclude(minX - deltaX, minY - deltaY);
		    	envelope.expandToInclude(maxX + deltaX, maxY + deltaY);

		    }
		    return envelope;
    	} catch (Exception ignore) {
			return null;
		} finally {
    		if( conn != null ){
				try {
					conn.close();
				} catch (SQLException e) {
					// I give up
				}
    		}
    	}
    }


    protected boolean allowTable(String tablename) {
        if (tablename.equals("geometry_columns")) {
            return false;
        } else if (tablename.startsWith("spatial_ref_sys")) {
            return false;
        }

        //others?
        return true;
    }

    /**
     * Override this method to perform a few permission checks before the super
     * class has a chance to do its thing.
     */
    protected SimpleFeatureType buildSchema(String typeName, FIDMapper mapper) throws IOException {
    	//be sure we can query the necessary tables
    	//TODO: should spatial_ref_sys be in here?
    	Connection conn = getConnection(Transaction.AUTO_COMMIT);

    	try {
			Statement st = conn.createStatement();

			try {
				st.execute("SELECT * FROM geometry_columns LIMIT 0;");
			}
			catch (Throwable t) {
				String msg = "Error querying relation: geometry_columns." +
				 	" Possible cause:" + t.getLocalizedMessage();
				throw new DataSourceException(msg,t);
			}
			try {
                SQLEncoderPostgis encoder = new SQLEncoderPostgis(-1);
                encoder.setSupportsGEOS(useGeos);
                PostgisSQLBuilder builder = new PostgisSQLBuilder(encoder,
                        config);
                initBuilder(builder);

                st.execute("SELECT * FROM " + builder.encodeTableName(typeName)
                        + " LIMIT 0;");
            }
			catch (Throwable t) {
				String msg = "Error querying relation:" + typeName + "."  +
						" Possible cause:" + t.getLocalizedMessage();
				throw new DataSourceException(msg,t);
			}
            st.close();
		}
    	catch (SQLException e) {
    		JDBCUtils.close(conn, Transaction.AUTO_COMMIT, e);
    		throw new DataSourceException(e);
		}
    	finally {
    		JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
    	}

    	//everything is cool, keep going
    	return super.buildSchema(typeName, mapper);
    }




    /**
     * This is a public entry point to the DataStore.
     *
     * <p>
     * We have given some though to changing this api to be based on query.
     * </p>
     *
     * <p>
     * Currently this is the only way to retype your features to different
     * name spaces.
     * </p>
     * (non-Javadoc)
     *
     * @see org.geotools.data.DataStore#getFeatureReader(org.geotools.feature.FeatureType,
     *      org.geotools.filter.Filter, org.geotools.data.Transaction)
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
     * Gets the list of attribute names required for both featureType and
     * filter
     *
     * @param featureType The FeatureType to get attribute names for.
     * @param filter The filter which needs attributes to filter.
     *
     * @return The list of attribute names required by a filter.
     *
     * @throws IOException If we can't get the schema.
     */
    protected String[] attributeNames(SimpleFeatureType featureType, Filter filter)
        throws IOException {
        String typeName = featureType.getTypeName();
        SimpleFeatureType original = getSchema(typeName);
        SQLBuilder sqlBuilder = getSqlBuilder(typeName);

        if (featureType.getAttributeCount() == original.getAttributeCount()) {
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
     * DOCUMENT ME!
     *
     * @param typeName
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public SQLBuilder getSqlBuilder(String typeName) throws IOException {
        FeatureTypeInfo info = typeHandler.getFeatureTypeInfo(typeName);
        int srid = -1;

        SQLEncoderPostgis encoder = new SQLEncoderPostgis();
        encoder.setSupportsGEOS(useGeos);
        encoder.setFIDMapper(typeHandler.getFIDMapper(typeName));

        if (info.getSchema().getGeometryDescriptor() != null) {
            String geom = info.getSchema().getGeometryDescriptor().getLocalName();
            srid = info.getSRID(geom);
            encoder.setDefaultGeometry(geom);
        }

        encoder.setFeatureType( info.getSchema() );
        encoder.setSRID(srid);
        encoder.setLooseBbox(looseBbox);

        PostgisSQLBuilder builder = new PostgisSQLBuilder(encoder,config,info.getSchema());
        initBuilder(builder);

        return builder;
    }

    protected void initBuilder(PostgisSQLBuilder builder) {
    	builder.setWKBEnabled(WKBEnabled);
        builder.setByteaEnabled(byteaEnabled);
        builder.setSchemaEnabled(schemaEnabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tableName
     * @param geometryColumnName
     *
     *
     * @throws IOException DOCUMENT ME!
     * @throws DataSourceException DOCUMENT ME!
     */
    protected int determineSRID(String tableName, String geometryColumnName)
        throws IOException {
        Connection dbConnection = null;

        try {
            String dbSchema = config.getDatabaseSchemaName();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT srid FROM geometry_columns WHERE ");
            if (schemaEnabled && dbSchema != null && dbSchema.length() > 0) {
                sql.append("f_table_schema='");
                sql.append(dbSchema);
                sql.append("' AND ");
            }
            sql.append("f_table_name='");
            sql.append(tableName);
            sql.append("' AND f_geometry_column='");
            sql.append(geometryColumnName);
            sql.append("';");

            String sqlStatement = sql.toString();
            LOGGER.fine("srid statement is " + sqlStatement);

            dbConnection = getConnection(Transaction.AUTO_COMMIT);
            Statement statement = dbConnection.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                int retSrid = result.getInt("srid");
                JDBCUtils.close(statement);

                return retSrid;
            }
            result.close();

            //try asking the first feature for its srid
            sql = new StringBuffer();
            sql.append("SELECT SRID(\"");
            sql.append(geometryColumnName);
            sql.append("\") FROM \"");
            if (schemaEnabled && dbSchema != null && dbSchema.length() > 0) {
                sql.append(dbSchema);
                sql.append("\".\"");
            }
            sql.append(tableName);
            sql.append("\" LIMIT 1");
            sqlStatement = sql.toString();
            result = statement.executeQuery(sqlStatement);
            if (result.next()) {
                int retSrid = result.getInt(1);
                JDBCUtils.close(statement);
                return retSrid;
            }

            String mesg = "No geometry column row for srid in table: "
                + tableName + ", geometry column " + geometryColumnName;
            throw new DataSourceException(mesg);
        } catch (SQLException sqle) {
            String message = sqle.getMessage();
            throw new DataSourceException(message, sqle);
        } finally {
            JDBCUtils.close(dbConnection, Transaction.AUTO_COMMIT, null);
        }
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
     * @param array DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return The name of the primay key column or null if one does not exist.
     */

    //    protected String determineFidColumnName(String typeName)
    //        throws IOException {
    //        String fidColumn = super.determineFidColumnName(typeName);
    //
    //        if(fidColumn == null)
    //        	fidColumn = DEFAULT_FID_COLUMN;
    //
    //        return fidColumn;
    //    }
    /*
    private static boolean isPresent(String[] array, String value) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if ((array[i] != null) && (array[i].equals(value))) {
                    return (true);
                }
            }
        }

        return (false);
    }
    */
    /**
     * Constructs an AttributeDescriptor from a row in a ResultSet. The ResultSet
     * contains the information retrieved by a call to getColumns() on the
     * DatabaseMetaData object. This information can be used to construct an
     * Attribute Type.
     *
     * <p>
     * This implementation construct an AttributeDescriptor using the default JDBC
     * type mappings defined in JDBCDataStore. These type mappings only handle
     * native Java classes and SQL standard column types. If a geometry type
     * is found then getGeometryAttribute is called.
     * </p>
     *
     * <p>
     * Note: Overriding methods must never move the current row pointer in the
     * result set.
     * </p>
     *
     * @param metadataRs The ResultSet containing the result of a
     *        DatabaseMetaData.getColumns call.
     *
     * @return The AttributeDescriptor built from the ResultSet.
     *
     * @throws IOException If an error occurs processing the ResultSet.
     */
    protected AttributeDescriptor buildAttributeType(ResultSet metadataRs)
        throws IOException {
        try {
            final int TABLE_NAME = 3;
            final int COLUMN_NAME = 4;
            final int TYPE_NAME = 6;
            final int NULLABLE = 11;
            String typeName = metadataRs.getString(TYPE_NAME);

            if (typeName.equals("geometry")) {
                String tableName = metadataRs.getString(TABLE_NAME);
                String columnName = metadataRs.getString(COLUMN_NAME);

                // check for nullability
                int nullCode = metadataRs.getInt( NULLABLE );
                boolean nillable = isNullable(nullCode);

                return getGeometryAttribute(tableName, columnName, nillable);
            } else if("uuid".equals(typeName)) {
                String tableName = metadataRs.getString(TABLE_NAME);
                String columnName = metadataRs.getString(COLUMN_NAME);

                // check for nullability
                int nullCode = metadataRs.getInt( NULLABLE );
                boolean nillable = isNullable(nullCode);

                AttributeTypeBuilder atb = new AttributeTypeBuilder();
                atb.setName(columnName);
                atb.setBinding(String.class);
                atb.setMinOccurs(nillable ? 0 : 1);
                atb.setMaxOccurs(1);
                return atb.buildDescriptor(columnName);
            } else {
                return super.buildAttributeType(metadataRs);
            }
        } catch (SQLException e) {
            throw new IOException("Sql error occurred: " + e.getMessage());
        }
    }

    private boolean isNullable(int nullCode) {
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
        return nillable;
    }

    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#buildFIDMapperFactory(org.geotools.data.jdbc.JDBCDataStoreConfig)
     */
    protected FIDMapperFactory buildFIDMapperFactory(JDBCDataStoreConfig config) {
        return new PostgisFIDMapperFactory( config );
    }

    protected FIDMapper buildFIDMapper( String typeName, FIDMapperFactory factory ) throws IOException {
        Connection conn = null;

        try {
            conn = getConnection(Transaction.AUTO_COMMIT);

            String dbSchema = config.getDatabaseSchemaName();
            FIDMapper mapper = factory.getMapper(null, dbSchema, typeName, conn);

            return mapper;
        } finally {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
    }

    /**
     * Returns an attribute type for a geometry column in a feature table.
     *
     * @param tableName The feature table name.
     * @param columnName The geometry column name.
     * @param nillable
     *
     * @return Geometric attribute.
     *
     * @throws IOException DOCUMENT ME!
     *
     * @task REVISIT: combine with querySRID, as they use the same select
     *       statement.
     * @task This should probably take a Transaction, so if things mess up then
     *       we can rollback.
     */
    AttributeDescriptor getGeometryAttribute(String tableName, String columnName, boolean nillable)
        throws IOException {
        Connection dbConnection = null;
        Class type = null;
        int srid = 0;
        int dimension = 2;
        try {
            dbConnection = getConnection(Transaction.AUTO_COMMIT);
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT type, coord_dimension FROM geometry_columns WHERE ");
            String dbSchema = config.getDatabaseSchemaName();
            if (schemaEnabled && dbSchema != null && dbSchema.length() > 0) {
                sql.append("f_table_schema='");
                sql.append(dbSchema);
                sql.append("' AND ");
            }
            sql.append("f_table_name='");
            sql.append(tableName);
            sql.append("' AND f_geometry_column='");
            sql.append(columnName);
            sql.append("';");

            String sqlStatement = sql.toString();
            LOGGER.fine("geometry type sql statement is " + sqlStatement);

            String geometryType = null;

            // retrieve the result set from the JDBC driver
            Statement statement = dbConnection.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                geometryType = result.getString("type");
                dimension = result.getInt("coord_dimension");
                LOGGER.fine("geometry type is: " + geometryType);
                if(dimension < 2) {
                    dimension = 2;
                    LOGGER.warning("Geometry dimension " + dimension + " + is invalid, assuming 2");
                }

            }
            result.close();

            if (geometryType == null) {
                //no geometry_columns entry, try grabbing a feature
                sql = new StringBuffer();
                if (WKBEnabled) {
                    sql.append("SELECT encode(AsBinary(force_2d(\"");
                    sql.append(columnName);
                    sql.append("\"), 'XDR'),'base64') FROM \"");
                } else {
                    sql.append("SELECT AsText(\"");
                    sql.append(columnName);
                    sql.append("\") FROM \"");
                }
                if (schemaEnabled && dbSchema != null && dbSchema.length() > 0) {
                    sql.append(dbSchema);
                    sql.append("\".\"");
                }
                sql.append(tableName);
                sql.append("\" LIMIT 1");
                sqlStatement = sql.toString();
                result = statement.executeQuery(sqlStatement);
                if (result.next()) {
                    AttributeIO attrIO = getGeometryAttributeIO(null, null);
                    Object object = attrIO.read(result, 1);
                    if (object instanceof Geometry) {
                        Geometry geom = (Geometry) object;
                        geometryType = geom.getGeometryType().toUpperCase();
                        type = geom.getClass();
                        srid = geom.getSRID(); //will return 0 unless we support EWKB
                    }
                }
                result.close();
            }
            statement.close();

            if (geometryType == null) {
                String msg = " no geometry found in the GEOMETRY_COLUMNS table"
                    + " for " + tableName + " of the postgis install.  A row"
                    + " for " + columnName + " is required"
                    + " for geotools to work correctly";
                throw new DataSourceException(msg);
            }

            if (type == null) {
                type = (Class) GEOM_TYPE_MAP.get(geometryType);
            }

        } catch (SQLException sqe) {
            throw new IOException("An SQL exception occurred: "
                + sqe.getMessage());
        } finally {
            JDBCUtils.close(dbConnection, Transaction.AUTO_COMMIT, null);
        }

        if (srid < 1) {
            //try again
            srid = determineSRID(tableName, columnName);
        }
        CoordinateReferenceSystem crs = null;

        try {
            crs = getPostgisAuthorityFactory().createCRS(srid);
        } catch (FactoryException e) {
            crs = null;
        }

        return new AttributeTypeBuilder().name(columnName).binding(type)
        	.nillable(nillable).crs(crs).userData(Hints.COORDINATE_DIMENSION, dimension).buildDescriptor(columnName);
    }

    private PostgisAuthorityFactory getPostgisAuthorityFactory() {
        if (paf == null) {
            paf = new PostgisAuthorityFactory(dataSource);
        }

        return paf;
    }

    /**
     * Gets the sql geometry column name for this type.
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     *
     * @task TODO: test this, I can just make sure it compiles.
     */
    private String getGeometrySQLTypeName(Class type) {
        String res = (String) GEOM_CLASS_MAPPINGS.get(type);

        if (res == null) {
            throw new RuntimeException("Unknown type name for class " + type
                + " please update GEOMETRY_MAPPINGS");
        }

        return res;
    }

    /**
     * Creates a FeatureType in this instance of the PostgisDataStore. Since we
     * don't yet know which attribute in the FeatureType is the primary key, we
     * will create our own called "fid_tablename", which has its own sequence
     * called "tablename_fid_seq". The user should not interact with this
     * column, although its value will be the FID. This method currently assumes
     * there are only 2 dimensions.
     *
     * @throws IOException
     *             if something goes horribly wrong or the table already exists
     * @see org.geotools.data.DataStore#createSchema(org.geotools.feature.FeatureType)
     */
    public void createSchema(SimpleFeatureType featureType) throws IOException {
    	String tableName = featureType.getTypeName();

    	String lcTableName = tableName.toLowerCase();

        AttributeDescriptor[] attributeType = (AttributeDescriptor[]) featureType.getAttributeDescriptors().toArray(new AttributeDescriptor[featureType.getAttributeDescriptors().size()]);
        String dbSchema = config.getDatabaseSchemaName();

        PostgisSQLBuilder sqlb = createSQLBuilder();


        //the featureType won't tell us who the primary key is, so we'll create
        //our own "fid_tablename".  Later when we load the featureType, we will
        //pretend we didn't see fid_tablename when we return the attributes.
        String fidColumn = lcTableName + "_fid";

        //make sure the fid column doesn't already exist
        for (int i = 0; i < attributeType.length; i++) {
        	if (attributeType[i].getLocalName().equalsIgnoreCase(fidColumn)) {
                String message = "The featuretype cannot contain the column "
                    + fidColumn + ", since this is used as the hidden FID column";
                throw new IOException(message);
            }
        }

        Connection con = this.getConnection(Transaction.AUTO_COMMIT);
        Statement st = null;

        boolean shouldExecute = !tablePresent(tableName, con);

        try {
            con.setAutoCommit(false);
            st = con.createStatement();

            StringBuffer sql = new StringBuffer("CREATE TABLE ");
			sql.append(sqlb.encodeTableName(tableName));
            sql.append(" (");
            sql.append(sqlb.encodeColumnName(fidColumn));
            sql.append(" serial PRIMARY KEY,");
            sql.append(makeSqlCreate(attributeType));
            sql.append(");");

            String sqlStr = sql.toString();
            LOGGER.info(sqlStr);

            if (shouldExecute) {
                st.execute(sqlStr);
            }

            //fix from pr: it may be that table existed and then was dropped
            //without removing its geometry info from GEOMETRY_COLUMNS.
            //To support this, try to delete before inserting.
            //Preserving case for table names gives problems,
            //so convert to lower case

            sql = new StringBuffer("DELETE FROM GEOMETRY_COLUMNS WHERE f_table_catalog=''");
            sql.append(" AND f_table_schema = '");
            sql.append(dbSchema);
            sql.append("'");
            sql.append("AND f_table_name = '");
            sql.append(tableName);
            sql.append("';");

            //prints statement for later reuse
            sqlStr = sql.toString();
            LOGGER.info(sqlStr);

            if (shouldExecute) {
                st.execute(sqlStr);
            }

            //Ok, so Paolo Rizzi suggested that we get rid of our hand-adding
            //of geometry column information and use AddGeometryColumn instead
            //as it is better (this is in GEOT-379, he attached an extended
            //datastore that does postgis fixes).  But I am pretty positive
            //the reason we are doing things this way is to preserve the order
            //of FeatureTypes.  I know this is fairly silly, from most
            //information perspectives, but from another perspective it seems
            //to make sense - if you were transfering a featureType from one
            //data store to another then it should have the same order, right?
            //And order is important in WFS.  There are a few caveats though
            //for one I don't even know if things work right.  I imagine the
            //proper constraints that a AddGeometryColumn operation does are
            //not set in our hand version, for one.  I would feel better about
            //ignoring the order and just doing things as we like if we had
            //views in place, if users could add the schema, and then be able
            //to get it back in exactly the order they wanted.  So for now
            //let's leave things as is, and maybe talk about it in an irc. -ch
            for (int i = 0; i < attributeType.length; i++) {
                if (!(attributeType[i] instanceof GeometryDescriptor)) {
                    continue;
                }
                GeometryDescriptor geomAttribute = (GeometryDescriptor) attributeType[i];
                String columnName = attributeType[i].getLocalName();

                CoordinateReferenceSystem refSys = geomAttribute.getCoordinateReferenceSystem();
                int SRID;

                if (refSys != null) {
                	try {
                        Set ident = refSys.getIdentifiers();
                        if ((ident == null || ident.isEmpty()) && refSys == DefaultGeographicCRS.WGS84) {
                            SRID = 4326;
                        } else {
                            String code = ((NamedIdentifier) ident.toArray()[0]).getCode();
                            SRID = Integer.parseInt(code);
                        }
                    } catch (Exception e) {
                        LOGGER.warning("SRID could not be determined");
                        SRID = -1;
                    }
                } else {
                    SRID = -1;
                }

//                DatabaseMetaData metaData = con.getMetaData();
//                ResultSet rs = metaData.getCatalogs();
//                rs.next();
//
//                //String dbName = rs.getString(1);
//                rs.close();

                String typeName = null;

                //this construct seems unnecessary, since we already would
                //pass over if this wasn't a geometry...
                Class type = geomAttribute.getType().getBinding();


                int dimension = 2;
                typeName = getGeometrySQLTypeName(type);

                GeometryDescriptor gd = (GeometryDescriptor) geomAttribute;
                if(gd.getUserData().get(Hints.COORDINATE_DIMENSION) instanceof Integer) {
                    dimension = (Integer) gd.getUserData().get(Hints.COORDINATE_DIMENSION);
                }

                if (typeName != null) {
//                    statementSQL = new StringBuffer(
//                        "SELECT AddGeometryColumn('" + dbSchema + "','"
//                        + tableName + "','" + attributeType[i].getName()
//                        + "','" + SRID + "','" + typeName + "',2);"
//                    ); //assumes 2-D

                	//add a row to the geometry_columns table
                    sql = new StringBuffer("INSERT INTO GEOMETRY_COLUMNS VALUES (");
                    sql.append("'','");
                    sql.append(dbSchema);
                    sql.append("','");
                    sql.append(tableName);
                    sql.append("','");
                    sql.append(columnName);
                    sql.append("',");
                    sql.append(dimension);
                    sql.append(",");
                    sql.append(SRID);
                    sql.append(",'");
                    sql.append(typeName);
                    sql.append("');");

                    sqlStr = sql.toString();
                    LOGGER.info(sqlStr);

                    if (shouldExecute) {
                        st.execute(sqlStr);
                    }

                    //add geometry constaints to the table
                    if (SRID > -1) {
                        sql = new StringBuffer("ALTER TABLE ");
                        sql.append(sqlb.encodeTableName(tableName));
                        sql.append(" ADD CONSTRAINT enforce_srid_");
                        sql.append( columnName );
                        sql.append(" CHECK (SRID(");
                        sql.append(sqlb.encodeColumnName(columnName));
                        sql.append(") = ");
                        sql.append(SRID);
                        sql.append(");");
                        sqlStr = sql.toString();
	                    LOGGER.info(sqlStr);
	                    if (shouldExecute) {
	                        st.execute(sqlStr);
	                    }
                    }

                    sql = new StringBuffer("ALTER TABLE ");
                    sql.append(sqlb.encodeTableName(tableName));
                    sql.append(" ADD CONSTRAINT enforce_dims_");
                    sql.append(columnName);
                    sql.append(" CHECK (ndims(");
                    sql.append(sqlb.encodeColumnName(columnName));
                    sql.append(") = ");
                    sql.append(dimension);
                    sql.append(");");
                    sqlStr = sql.toString();
                    LOGGER.info(sqlStr);
                    if (shouldExecute) {
                        st.execute(sqlStr);
                    }

                    if (!typeName.equals("GEOMETRY")) {
                        sql = new StringBuffer("ALTER TABLE ");
                        sql.append(sqlb.encodeTableName(tableName));
                        sql.append(" ADD CONSTRAINT enforce_geotype_");
                        sql.append(columnName);
                        sql.append(" CHECK (geometrytype(");
                        sql.append(sqlb.encodeColumnName(columnName));
                        sql.append(") = '");
                        sql.append(typeName);
                        sql.append("'::text OR ");
                        sql.append(sqlb.encodeColumnName(columnName));
                        sql.append(" IS NULL);");
	                    sqlStr = sql.toString();
                        LOGGER.info(sqlStr);
	                    if (shouldExecute) {
	                        st.execute(sqlStr);
	                    }
                    }

                } else {
                	LOGGER.warning("Error: " + geomAttribute.getLocalName()+ " unknown type!!!");
                }



                //also build a spatial index on each geometry column.
                sql = new StringBuffer("CREATE INDEX spatial_");
                sql.append(tableName);
                sql.append("_");
                sql.append(attributeType[i].getLocalName().toLowerCase());
                sql.append(" ON ");
                sql.append(sqlb.encodeTableName(tableName));
                sql.append(" USING GIST (");
                sql.append(sqlb.encodeColumnName(attributeType[i].getLocalName()));
                sql.append(");");

                sqlStr = sql.toString();
                LOGGER.info(sqlStr);

                if (shouldExecute) {
                    st.execute(sqlStr);
                }
            }

            con.commit();

        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException sqle) {
                throw new IOException(sqle.getMessage());
            }

            throw (IOException) new IOException(e.getMessage()).initCause( e );
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                throw new IOException(e.getMessage());
            } finally {
                try {
                    if (con != null) {
                        con.setAutoCommit(true);
                        con.close();
                    }
                } catch (SQLException e) {
                    throw new IOException(e.getMessage());
                }
            }
        }

        if (!shouldExecute) {
            throw new IOException("The table " + tableName + " already exists.");
        }
    }

//    /**
//     * Returns the sql type name given the SQL type code
//     *
//     * @param typeCode
//     *
//     *
//     * @throws RuntimeException DOCUMENT ME!
//     */
//    private String getSQLTypeName(int typeCode) {
//        Class typeClass = (Class) TYPE_MAPPINGS.get(new Integer(typeCode));
//
//        if (typeClass == null) {
//            throw new RuntimeException("Unknown type " + typeCode
//                + " please update TYPE_MAPPINGS");
//        }
//
//        String typeName = (String) CLASS_MAPPINGS.get(typeClass);
//
//        if (typeName == null) {
//            throw new RuntimeException("Unknown type name for class "
//                + typeClass.getName() + " please update CLASS_MAPPINGS");
//        }
//
//        return typeName;
//    }

    private StringBuffer makeSqlCreate(AttributeDescriptor[] attributeType)
        throws IOException {
        StringBuffer buf = new StringBuffer("");

        for (int i = 0; i < attributeType.length; i++) {
            String typeName = null;
            typeName = (String) CLASS_MAPPINGS.get(attributeType[i].getType().getBinding());
            if (typeName == null)
            	typeName = (String) GEOM_CLASS_MAPPINGS.get(attributeType[i].getType().getBinding());

            if (typeName != null) {
                if (attributeType[i] instanceof GeometryDescriptor) {
                    typeName = "GEOMETRY";
                } else if (typeName.equals("VARCHAR")) {
                	int length = FeatureTypes.getFieldLength(attributeType[i]);
                	if (length < 1) {
                		LOGGER.warning("FeatureType did not specify string length; defaulted to 256");
                		length = 256;
                	}
                    else if (length > MAX_ALLOWED_VALUE)
                    {
                        length = MAX_ALLOWED_VALUE;
                    }
                    typeName = typeName + "(" + length + ")";
                }

                if (!attributeType[i].isNillable()) {
                    typeName = typeName + " NOT NULL";
                }

                //TODO review!!! Is toString() always OK???
                Object defaultValue = attributeType[i].getDefaultValue();

                if (defaultValue != null) {
                    typeName = typeName + " DEFAULT '"
                        + defaultValue.toString() + "'";
                }

                buf.append(" \"" + attributeType[i].getLocalName() + "\" " + typeName + ",");

            } else {
            	String msg;
            	if (attributeType[i] == null) {
            		msg = "AttributeDescriptor was null!";
            	} else {
            		msg = "Type '" + attributeType[i].getType().getBinding() + "' not supported!";
            	}
                throw (new IOException(msg));
            }
        }

        return buf.deleteCharAt(buf.length()-1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param table
     * @param con
     *
     *
     * @throws IOException DOCUMENT ME!
     * @throws DataSourceException DOCUMENT ME!
     */
    private boolean tablePresent(String table, Connection con)
        throws IOException {
        final int TABLE_NAME_COL = 3;
        Connection conn = null;
        //List list = new ArrayList();

        try {
            conn = getConnection(Transaction.AUTO_COMMIT);

            DatabaseMetaData meta = conn.getMetaData();
            String[] tableType = { "TABLE" };
            ResultSet tables = meta.getTables(null,
                    config.getDatabaseSchemaName(), "%", tableType);

            while (tables.next()) {
                String tableName = tables.getString(TABLE_NAME_COL);

                if (allowTable(tableName) && (tableName != null)
                        && (tableName.equalsIgnoreCase(table))) {
                    return (true);
                }
            }

            return false;
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
            conn = null;

            String message = "Error querying database for list of tables:"
                + sqlException.getMessage();
            throw new DataSourceException(message, sqlException);
        } finally {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param query
     *
     *
     * @throws IOException DOCUMENT ME!
     */
    /**
     * Get propertyNames in a safe manner.
     *
     * <p>
     * Method wil figure out names from the schema for query.getTypeName(), if
     * query getPropertyNames() is <code>null</code>, or
     * query.retrieveAllProperties is <code>true</code>.
     * </p>
     *
     * @param query
     *
     *
     * @throws IOException
     */
    /*
    private String[] propertyNames(Query query) throws IOException {
        String[] names = query.getPropertyNames();

        if ((names == null) || query.retrieveAllProperties()) {
            String typeName = query.getTypeName();
            FeatureType schema = getSchema(typeName);

            names = new String[schema.getAttributeCount()];

            for (int i = 0; i < schema.getAttributeCount(); i++) {
                names[i] = schema.getAttributeType(i).getName();
            }
        }

        return names;
    }
    */
    /**
     * @see org.geotools.data.DataStore#updateSchema(java.lang.String,
     *      org.geotools.feature.FeatureType)
     */
    public void updateSchema(String typeName, SimpleFeatureType featureType)
        throws IOException {
    	throw new IOException("PostgisDataStore.updateSchema not yet implemented");
    	//TODO: implement updateSchema
    }

    /**
     * Default implementation based on getFeatureReader and getFeatureWriter.
     *
     * <p>
     * We should be able to optimize this to only get the RowSet once
     * </p>
     *
     * @see org.geotools.data.DataStore#getFeatureSource(java.lang.String)
     */
    public SimpleFeatureSource getFeatureSource(String typeName)
        throws IOException {
        if (!typeHandler.getFIDMapper(typeName).isVolatile()
                || allowWriteOnVolatileFIDs) {
            LOGGER.fine("get Feature source called on " + typeName);

            if (OPTIMIZE_MODE == OPTIMIZE_SQL) {
                LOGGER.fine("returning pg feature locking");

                return createFeatureLockingInternal(this, getSchema(typeName));
            }

            // default
            if (getLockingManager() != null) {
                // Use default JDBCFeatureLocking that delegates all locking
                // the getLockingManager
                LOGGER.fine("returning jdbc feature locking");

                return new JDBCFeatureLocking(this, getSchema(typeName));
            } else {
                LOGGER.fine(
                    "returning jdbc feature store (lock manager is null)");

                // subclass should provide a FeatureLocking implementation
                // but for now we will simply forgo all locking
                return new JDBCFeatureStore(this, getSchema(typeName));
            }
        } else {
            return new JDBCFeatureSource(this, getSchema(typeName));
        }
    }

    public PostgisFeatureLocking createFeatureLockingInternal(
		PostgisDataStore ds, SimpleFeatureType type
	) throws IOException {

    	return new PostgisFeatureLocking(ds,type);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fReader
     * @param queryData
     *
     *
     * @throws IOException DOCUMENT ME!
     */
    protected JDBCFeatureWriter createFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> fReader,
        QueryData queryData) throws IOException {
    	PostgisSQLBuilder sqlBuilder =
    		(PostgisSQLBuilder) getSqlBuilder(fReader.getFeatureType().getTypeName());
        PostgisFeatureWriter postgisFeatureWriter = new PostgisFeatureWriter(fReader, queryData, WKBEnabled,byteaWKB,sqlBuilder);
        return postgisFeatureWriter;
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

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.data.DataStore#getFeatureWriterAppend(java.lang.String,
     *      org.geotools.data.Transaction)
     */

    /**
     * Retrieve a FeatureWriter for creating new content.
     *
     * <p>
     * Subclass may wish to implement an optimized featureWriter for this
     * operation. One based on prepared statements is a possibility, as we do
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

    int getSRID(String typeName, String geomColName) throws IOException {
        return typeHandler.getFeatureTypeInfo(typeName).getSRID(geomColName);
    }

    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#getGeometryAttributeIO(org.geotools.feature.AttributeDescriptor)
     */
    protected AttributeIO getGeometryAttributeIO(AttributeDescriptor type,
        QueryData queryData) {

        // grab the crs if available
        GeometryDescriptor geometryType = (GeometryDescriptor) type;
        CoordinateReferenceSystem crs = null;
        if(geometryType != null)
            crs = geometryType.getCoordinateReferenceSystem();

        Hints hints = queryData != null ? queryData.getHints() : GeoTools.getDefaultHints();
        if(geometryType != null && geometryType.getUserData().get(Hints.COORDINATE_DIMENSION) instanceof Integer) {
            hints.put(Hints.COORDINATE_DIMENSION, geometryType.getUserData().get(Hints.COORDINATE_DIMENSION));
        }
        int D = (crs == null || Boolean.TRUE.equals( queryData.getHints().get( Hints.FEATURE_2D )))
                ? 2 : crs.getCoordinateSystem().getDimension();

        if (WKBEnabled) {
            return new PgWKBAttributeIO(isByteaEnabled(), hints);
        } else {
            if( D == 3 ){
                return new EWKTAttributeIO();
            }
            else {
                return new WKTAttributeIO();
            }
        }
    }

    protected int getResultSetType(boolean forWrite) {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    protected int getConcurrency(boolean forWrite) {
        return ResultSet.CONCUR_READ_ONLY;
    }

    /**
     * Returns true if the WKB format is used to transfer geometries, false
     * otherwise
     *
     */
    public boolean isWKBEnabled() {
        return WKBEnabled;
    }

    /**
     * If turned on, WKB will be used to transfer geometry data instead of  WKT
     *
     * @param enabled
     */
    public void setWKBEnabled(boolean enabled) {
        WKBEnabled = enabled;
    }

    /**
     * Sets this postgis instance to use a less strict but faster bounding box
     * query.  Setting this to <tt>true</tt> will have PostGIS issue bounding
     * box queries against the envelope of the geometry, so some may be
     * <i>slighty</i> wrong, but will perform much faster.  The  intersects
     * function can still be used to obtain the exact query.
     *
     * @param isLooseBbox <tt>true</tt> if this should have a loose Bbox.
     */
    public void setLooseBbox(boolean isLooseBbox) {
        this.looseBbox = isLooseBbox;
    }

    /**
     * Whether the bounding boxes issued against this postgis datastore are on
     * the envelope of the geometry or the actual geometry.
     *
     * @return <tt>true</tt> if the bounding box is 'loose', against the
     *         envelope instead of the actual geometry.
     */
    public boolean isLooseBbox() {
        return looseBbox;
    }

    /**
     * Returns true if the data store is using the bytea function to fasten WKB
     * data transfer, false otherwise
     *
     */
    public boolean isByteaEnabled() {
        return byteaEnabled;
    }

    public void setByteaWKB(boolean byteaWKB) {
        this.byteaWKB = byteaWKB;
    }
    public boolean isByteaWKB()
    {
    	return byteaWKB;
    }
    /**
     * Enables the use of bytea function for WKB data transfer (will improve
     * performance).  Note this function need not be set by the programmer, as
     * the datastore will use it to optimize performance whenever it can (when
     * postGIS is 0.7.2 or later)
     *
     * @param byteaEnabled
     */
    public void setByteaEnabled(boolean byteaEnabled) {
        this.byteaEnabled = byteaEnabled;
    }


    /**
     * Enables the use of the 'estimated_extent' function for bounds computation.
     * <p>
     * Beware that this function is an approximation and is dependent on the
     * degree to with the data in the actual bounds follows a uniform distribution.
     * </p>
     */
    public void setEstimatedExtent(boolean estimatedExtent) {
    	this.estimatedExtent = estimatedExtent;

    	//also make sure optimize mode is set properly
    	if ( estimatedExtent ) {
    		LOGGER.info( "Setting OPTIMIZE_MODE to 'SQL'" );
    		setOptimizeMode( OPTIMIZE_SQL );
    	}
    }

    /**
     * @see {@link #setEstimatedExtent(boolean)}.
     */
    public boolean isEstimatedExtent() {
    	return estimatedExtent;
    }

    /**
     * Sets the optimization mode for the datastore.
     *
     * @param mode One of {@link #OPTIMIZE_SAFE},{@link #OPTIMIZE_SQL}.
     */
    public void setOptimizeMode( int mode ) {
    	OPTIMIZE_MODE = mode;
    }
    public int getOptimizeMode() {
    	return OPTIMIZE_MODE;
    }

	public SimpleFeatureType getSchema(String arg0) throws IOException {
		return super.getSchema(arg0);
	}

    /**
     * Obtains the postgis datastore connection pool.
     *
     * @return ConnectionPool
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Obtains database specific information, such as version, supported
     * functions, etc.
     */
    public PostgisDBInfo getDBInfo() {
        if (dbInfo == null) {
            Connection conn;
            try {
                conn = getConnection(Transaction.AUTO_COMMIT);
                dbInfo = new PostgisDBInfo(conn);
            } catch (IOException e1) {
                LOGGER.log(Level.SEVERE, "Could not obtain DBInfo object", e1);
            }
        }
        return dbInfo;
    }

    /**
     * Returns the JDBC type constant (as in {@link Types}) that maps to the given
     * Java class binding when constructing attribute types, or null if no such mapping exist.
     *
     * @param attributeTypeBinding
     * @return
     */
    public Integer getJdbcType(Class attributeTypeBinding){
        Map.Entry entry;
        Integer jdbcType = null;
        Class binding;
        for(Iterator it = TYPE_MAPPINGS.entrySet().iterator(); it.hasNext();){
            entry = (Map.Entry) it.next();
            binding = (Class) entry.getValue();
            if(binding.equals(attributeTypeBinding)){
                jdbcType = (Integer)entry.getKey();
                break;
            }
        }
        return jdbcType;
    }

    /**
     * The hints supported by this datastore depending on the configuration
     */
    private static final Set BASE_HINTS = Collections.unmodifiableSet(
            new HashSet(Arrays.asList(new Object[] {
            Hints.FEATURE_DETACHED})));
    private static final Set WKB_HINTS = Collections.unmodifiableSet(
            new HashSet(Arrays.asList(new Object[] {
            Hints.FEATURE_DETACHED,
            Hints.JTS_COORDINATE_SEQUENCE_FACTORY,
            Hints.JTS_GEOMETRY_FACTORY})));
    public Set getSupportedHints() {
        if(isWKBEnabled()) {
            return WKB_HINTS;
        } else {
            return BASE_HINTS;
        }
    }
}

