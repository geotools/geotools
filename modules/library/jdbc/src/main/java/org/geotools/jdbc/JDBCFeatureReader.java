/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.factory.Hints;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Association;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AssociationDescriptor;
import org.opengis.feature.type.AssociationType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * Reader for jdbc datastore
 * 
 * @author Justin Deoliveira, The Open Plannign Project.
 *
 *
 *
 * @source $URL$
 */
public class JDBCFeatureReader implements  FeatureReader<SimpleFeatureType, SimpleFeature> {
    protected static final Logger LOGGER = Logging.getLogger(JDBCFeatureReader.class);
    
    /**
     * When true, the stack trace that created a reader that wasn't closed is recorded and then
     * printed out when warning the user about this.
     */
    protected static final Boolean TRACE_ENABLED = "true".equalsIgnoreCase(System.getProperty("gt2.jdbc.trace"));

    /**
     * The feature source the reader originated from. 
     */
    protected JDBCFeatureSource featureSource;
    /**
     * the datastore
     */
    protected JDBCDataStore dataStore;
    /**
     * schema of features
     */
    protected SimpleFeatureType featureType;
    /**
     * geometry factory used to create geometry objects
     */
    protected GeometryFactory geometryFactory;
    /**
     * hints
     */
    protected Hints hints;
    /**
     * current transaction
     */
    protected Transaction tx;
    /**
     * flag indicating if the iterator has another feature
     */
    protected Boolean next;
    /**
     * feature builder
     */
    protected SimpleFeatureBuilder builder;
    /**
     * The primary key    
     */
    protected PrimaryKey pkey;
    
    /**
     * statement,result set that is being worked from.
     */
    protected Statement st;
    protected ResultSet rs;
    protected Connection cx;
    protected Exception tracer;
    protected String[] columnNames;
    
    public JDBCFeatureReader( String sql, Connection cx, JDBCFeatureSource featureSource, SimpleFeatureType featureType, Hints hints ) 
        throws SQLException {
        init( featureSource, featureType, hints );
        
        //create the result set
        this.cx = cx;
        st = cx.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        st.setFetchSize(featureSource.getDataStore().getFetchSize());
        
        ((BasicSQLDialect)featureSource.getDataStore().getSQLDialect()).onSelect(st, cx, featureType);
        rs = st.executeQuery(sql);
    }
    
    public JDBCFeatureReader( PreparedStatement st, Connection cx, JDBCFeatureSource featureSource, SimpleFeatureType featureType, Hints hints ) 
        throws SQLException {
            
        init( featureSource, featureType, hints );
        
        //create the result set
        this.cx = cx;
        this.st = st;
        
        ((PreparedStatementSQLDialect)featureSource.getDataStore().getSQLDialect()).onSelect(st, cx, featureType);
        rs = st.executeQuery();
    }
    
    protected void init( JDBCFeatureSource featureSource, SimpleFeatureType featureType, Hints hints ) {
        // init the tracer if we need to debug a connection leak
        if(TRACE_ENABLED) {
            tracer = new Exception();
            tracer.fillInStackTrace();
        }
        
        // init base fields
        this.featureSource = featureSource;
        this.dataStore = featureSource.getDataStore();
        this.featureType = featureType;
        this.tx = featureSource.getTransaction();
        this.hints = hints;
        
        //grab a geometry factory... check for a special hint
        geometryFactory = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
        if (geometryFactory == null) {
            // look for a coordinate sequence factory
            CoordinateSequenceFactory csFactory = 
                (CoordinateSequenceFactory) hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);

            if (csFactory != null) {
                geometryFactory = new GeometryFactory(csFactory);
            }
        }

        if (geometryFactory == null) {
            // fall back on one privided by datastore
            geometryFactory = dataStore.getGeometryFactory();
        }
        
        // create a feature builder using the factory hinted or the one coming 
        // from the datastore
        FeatureFactory ff = (FeatureFactory) hints.get(Hints.FEATURE_FACTORY);
        if(ff == null)
            ff = featureSource.getDataStore().getFeatureFactory();
        builder = new SimpleFeatureBuilder(featureType, ff);
        
        // find the primary key
        try {
            pkey = dataStore.getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    
    public JDBCFeatureReader( JDBCFeatureReader other ) {
        this.featureType = other.featureType;
        this.dataStore = other.dataStore;
        this.featureSource = other.featureSource;
        this.tx = other.tx;
        this.hints = other.hints;
        this.geometryFactory = other.geometryFactory;
        this.builder = other.builder;
        this.st = other.st;
        this.rs = other.rs;
    }
    
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public boolean hasNext() throws IOException {
        ensureOpen();
        
        if (next == null) {
            try {
                next = Boolean.valueOf(rs.next());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return next.booleanValue();
    }

    protected void ensureNext() {
        if (next == null) {
            throw new IllegalStateException("Must call hasNext before calling next");
        }
    }
    
    protected void ensureOpen() throws IOException {
        if ( rs == null ) {
            throw new IOException( "reader already closed" );
        }
    }
    
    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        try {
            ensureOpen();
            if(!hasNext()) {
                throw new NoSuchElementException("No more features in this reader, you should call " +
                		"hasNext() to check for feature availability");
            }
            
            //grab the connection
            Connection cx;
            try {
                cx = st.getConnection();
            } 
            catch (SQLException e) {
                throw (IOException) new IOException().initCause(e);
            }
            
            // figure out the fid
            String fid;
    
            try {
                fid = dataStore.encodeFID(pkey,rs);
    
                // wrap the fid in the type name
                fid = featureType.getTypeName() + "." + fid;
            } catch (Exception e) {
                throw new RuntimeException("Could not determine fid from primary key", e);
            }
    
            // check for the association traversal depth hint, if not > 0 dont
            // resolve the associated feature or geometry
            Integer depth = (Integer) hints.get(Hints.ASSOCIATION_TRAVERSAL_DEPTH);
    
            if (depth == null) {
                depth = new Integer(0);
            }
    
            PropertyName associationPropertyName = 
                (PropertyName) hints.get(Hints.ASSOCIATION_PROPERTY);
    
            // round up attributes
            final int attributeCount = featureType.getAttributeCount();
            int[] attributeRsIndex = buildAttributeRsIndex();
            for(int i = 0; i < attributeCount; i++) {
                AttributeDescriptor type = featureType.getDescriptor(i);
                
                //figure out if any referenced attributes should be resolved
                boolean resolve = depth.intValue() > 0;
    
                if (resolve && (associationPropertyName != null)) {
                    AttributeDescriptor associationProperty = (AttributeDescriptor) associationPropertyName
                        .evaluate(featureType);
                    resolve = (associationProperty != null)
                        && associationProperty.getLocalName().equals(type.getLocalName());
                }
    
                try {
                    Object value = null;
    
                    // is this a geometry?
                    if (type instanceof GeometryDescriptor) {
                        GeometryDescriptor gatt = (GeometryDescriptor) type;
                        
                        //read the geometry
                        try {
                            value = dataStore.getSQLDialect()
                                             .decodeGeometryValue(gatt, rs, attributeRsIndex[i],
                                    geometryFactory, cx);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        
                        if (value != null) {
                            //check to see if a crs was set
                            Geometry geometry = (Geometry) value;
                            if ( geometry.getUserData() == null ) {
                                //if not set, set from descriptor
                                geometry.setUserData( gatt.getCoordinateReferenceSystem() );
                            }
                        } else {
                            // check case where this is an associated geometry
                            if (dataStore.isAssociations()) {
                                try {
                                    dataStore.ensureAssociationTablesExist(st.getConnection());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
    
                                Statement select = null;
                                ResultSet gas = null;
                                try {
                                    if ( dataStore.getSQLDialect() instanceof PreparedStatementSQLDialect ) {
                                        select = dataStore.selectGeometryAssociationSQLPS(fid, null, gatt.getLocalName(), cx);
                                        gas = ((PreparedStatement)select).executeQuery();
                                    }
                                    else {
                                        String sql = dataStore.selectGeometryAssociationSQL(fid, null,
                                                gatt.getLocalName());
                                        dataStore.getLogger().fine(sql);    
                                        select = st.getConnection().createStatement();
                                        gas = select.executeQuery(sql.toString()); 
                                    }
                                    
                                    
                                    if (gas.next()) {
                                        String gid = gas.getString("gid");
                                        boolean ref = gas.getBoolean("ref");
    
                                        Geometry g = null;
    
                                        // if this is a "referenced" geometry,
                                        // do not
                                        // read it if the depth is <= 0
                                        if (ref && !resolve) {
                                            // use a stub
                                            g = geometryFactory.createPoint(new CoordinateArraySequence(
                                                        new Coordinate[] {  }));
                                            //g = new NullGeometry();
                                            dataStore.setGmlProperties(g, gid, null, null);
                                        } else {
                                            // read the geometry
                                            ResultSet grs = null;
                                            if ( dataStore.getSQLDialect() instanceof PreparedStatementSQLDialect ) {
                                                dataStore.closeSafe( select );
                                                select = dataStore.selectGeometrySQLPS(gid, cx);
                                                
                                                grs = ((PreparedStatement) select).executeQuery();
                                            }
                                            else {
                                                String sql = dataStore.selectGeometrySQL(gid);
                                                dataStore.getLogger().fine(sql);
    
                                                grs = select.executeQuery(sql);    
                                            }
                                            
                                            try {
                                                // should always be one
                                                if (!grs.next()) {
                                                    throw new SQLException("no entry for: " + gid
                                                        + " in " + JDBCDataStore.GEOMETRY_TABLE);
                                                }
    
                                                String name = grs.getString("name");
                                                String desc = grs.getString("description");
    
                                                if (grs.getObject("geometry") != null) {
                                                    //read the geometry
                                                    g = dataStore.getSQLDialect()
                                                                 .decodeGeometryValue(gatt, grs,
                                                            "geometry", geometryFactory, cx);
                                                } else {
                                                    //multi geometry?
                                                    String gtype = grs.getString("type");
    
                                                    if ("MULTIPOINT".equals(gtype)
                                                            || "MULTILINESTRING".equals(gtype)
                                                            || "MULTIPOLYGON".equals(gtype)) {
                                                        ResultSet mg = null;
                                                        if ( dataStore.getSQLDialect() instanceof PreparedStatementSQLDialect ) {
                                                            dataStore.closeSafe( select );
                                                            select = dataStore.selectMultiGeometrySQLPS(gid, cx);
                                                            
                                                            mg = ((PreparedStatement)select).executeQuery();
                                                        }
                                                        else {
                                                            String sql = dataStore.selectMultiGeometrySQL(gid);
                                                            dataStore.getLogger().fine(sql);
    
                                                            mg = select.executeQuery(sql);    
                                                        }
                                                        
                                                        try {
                                                            ArrayList members = new ArrayList();
    
                                                            while (mg.next()) {
                                                                String mgid = mg.getString("mgid");
                                                                boolean mref = mg.getBoolean("ref");
                                                                
                                                                Geometry member = null;
                                                                if ( !mref || resolve ) {
                                                                    Statement select2 = null;
                                                                    ResultSet mgg = null;
                                                                    if ( dataStore.getSQLDialect() instanceof PreparedStatementSQLDialect ) {
                                                                        select2 = dataStore.selectGeometrySQLPS(mgid, cx);
                                                                        mgg = ((PreparedStatement)select2).executeQuery();
                                                                    }
                                                                    else {
                                                                        String sql = dataStore.selectGeometrySQL(mgid);
                                                                        dataStore.getLogger().fine(sql);
    
                                                                        select2 = st.getConnection()
                                                                                              .createStatement();
                                                                        mgg = select2.executeQuery(sql);    
                                                                    }
                                                                    
                                                                    try {
                                                                        mgg.next();
    
                                                                        String mname = mgg.getString(
                                                                                "name");
                                                                        String mdesc = mgg.getString(
                                                                                "description");
    
                                                                        member = dataStore.getSQLDialect()
                                                                                                   .decodeGeometryValue(gatt,
                                                                                mgg, "geometry",
                                                                                geometryFactory, cx );
    
                                                                        dataStore.setGmlProperties(member, mgid,
                                                                            mname, mdesc);
                                                                        
                                                                    } finally {
                                                                        dataStore.closeSafe(mgg);
                                                                        dataStore.closeSafe(select2);
                                                                    }
                                                                }
                                                                else {
                                                                    //create a stub
                                                                    // use a stub
                                                                    member = geometryFactory.createPoint(new CoordinateArraySequence(
                                                                                new Coordinate[] {  }));
                                                                    dataStore.setGmlProperties(member, mgid, null, null);
                                                                }
                                                                
                                                                members.add(member);
                                                            }
    
                                                            if ("MULTIPOINT".equals(gtype)) {
                                                                g = geometryFactory.createMultiPoint((Point[]) members
                                                                        .toArray(new Point[members
                                                                            .size()]));
                                                            } else if ("MULTILINESTRING".equals(
                                                                        gtype)) {
                                                                g = geometryFactory
                                                                    .createMultiLineString((LineString[]) members
                                                                        .toArray(new LineString[members
                                                                            .size()]));
                                                            } else if ("MULTIPOLYGON".equals(gtype)) {
                                                                g = geometryFactory
                                                                    .createMultiPolygon((Polygon[]) members
                                                                        .toArray(new Polygon[members
                                                                            .size()]));
                                                            } else {
                                                                g = geometryFactory
                                                                    .createGeometryCollection((Geometry[]) members
                                                                        .toArray(new Geometry[members
                                                                            .size()]));
                                                            }
                                                        } finally {
                                                            dataStore.closeSafe(mg);
                                                        }
                                                    }
                                                }
    
                                                dataStore.setGmlProperties(g, gid, name, desc);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            } finally {
                                                dataStore.closeSafe(grs);
                                            }
                                        }
    
                                        value = g;
                                    }
                                    
                                } finally {
                                    dataStore.closeSafe( gas );
                                    dataStore.closeSafe(select);
                                }
                            }
                        }
                    } else {
                        value = rs.getObject(attributeRsIndex[i]);
                    }
    
                    // is this an association?
                    if (dataStore.isAssociations()
                            && Association.class.equals(type.getType().getBinding()) && (value != null)) {
                     
                        Statement select = null;
                        ResultSet associations = null;
                        if (dataStore.getSQLDialect() instanceof PreparedStatementSQLDialect ) {
                            select = dataStore.selectAssociationSQLPS(fid, cx);
                            associations = ((PreparedStatement)select).executeQuery();
                        }
                        else {
                            String sql = dataStore.selectAssociationSQL(fid);
                            dataStore.getLogger().fine(sql);
                            
                            select = st.getConnection().createStatement();
                            associations = select.executeQuery(sql);
                        }
                        
    
                        try {
                            if (associations.next()) {
                                String rtable = associations.getString("rtable");
                                String rfid = associations.getString("rfid");
    
                                SimpleFeatureType associatedType = null;
    
                                try {
                                    associatedType = dataStore.getSchema(rtable);
                                } catch (IOException e) {
                                    //only log here, this means that the association
                                    // is probably bad... which we still want to 
                                    // handle, and fail only when and if we actually
                                    // resolve the link
                                    String msg = "Could not load schema: " + rtable;
                                    dataStore.getLogger().log(Level.WARNING, msg, e);
                                }
    
                                // set the referenced id + typeName as user data
                                builder.userData("gml:id", rtable + "." + rfid);
                                builder.userData("gml:featureTypeName", rtable);
    
                                FeatureTypeFactory tf = dataStore.getFeatureTypeFactory();
    
                                if (associatedType == null) {
                                    //means there was a problem with the link, 
                                    // create a dummy type
                                    SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder(tf);
                                    tb.setName(rtable);
                                    associatedType = tb.buildFeatureType();
                                }
    
                                //create an association
                                AssociationType associationType = tf.createAssociationType(type
                                        .getName(), associatedType, false, Collections.EMPTY_LIST,
                                        null, null);
                                AssociationDescriptor associationDescriptor = tf
                                    .createAssociationDescriptor(associationType, type.getName(),
                                        1, 1, true);
    
                                FeatureFactory f = dataStore.getFeatureFactory();
                                Association association = f.createAssociation(null,
                                        associationDescriptor);
                                association.getUserData().put("gml:id", rtable + "." + rfid);
    
                                if (resolve) {
                                    // use the value as an the identifier in a query against
                                    // the
                                    // referenced type
                                    DefaultQuery query = new DefaultQuery(rtable);
    
                                    Hints hints = new Hints(Hints.ASSOCIATION_TRAVERSAL_DEPTH,
                                            new Integer(depth.intValue() - 1));
                                    query.setHints(hints);
    
                                    FilterFactory ff = dataStore.getFilterFactory();
                                    Id filter = ff.id(Collections.singleton(ff.featureId(
                                                    value.toString())));
                                    query.setFilter(filter);
    
                                    try {
                                        // grab a reader and get the feature, there should
                                        // only
                                        // be one
                                         FeatureReader<SimpleFeatureType, SimpleFeature> r = dataStore.getFeatureReader(query, tx);
                                        try {
                                            r.hasNext();
    
                                            SimpleFeature associated = r.next();
                                            association.setValue(associated);
                                        } finally {
                                            r.close();
                                        }
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
    
                                // set the actual value to be the association
                                value = association;
                            }
                        } finally {
                            dataStore.closeSafe(associations);
                            dataStore.closeSafe(select);
                        }
                    }
    
                    // they value may need conversion. We let converters chew the initial
                    // value towards the target type, if the result is not the same as the
                    // original, then a conversion happened and we may want to report it to the
                    // user (being the feature type reverse engineerd, it's unlikely a true
                    // conversion will be needed)
                    if(value != null) {
                        Class binding = type.getType().getBinding();
                        Object converted = Converters.convert(value, binding);
                        if(converted != null && converted != value) {
                            value = converted;
                            if (dataStore.getLogger().isLoggable(Level.FINER)) {
                                String msg = value + " is not of type " + binding.getName()
                                    + ", attempting conversion";
                                dataStore.getLogger().finer(msg);
                            }
                        }
                    }
    
                    builder.add(value);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
    
            // create the feature
            try {
                return builder.buildFeature(fid);
            } catch (IllegalAttributeException e) {
                throw new RuntimeException(e);
            }
        } finally {
            // reset the next flag. We do this in a finally block to make sure we
            // move to the next record no matter what, if the current one could
            // not be read there is no salvation for it anyways
            next = null;
        }
    }

    /**
     * Builds an array containing the position in the result set for each attribute.
     * It takes into account that rs positions start by one, about the exposed primary keys,
     * and the fact that exposed pk can be only partially selected in the output
     * @return
     */
    private int[] buildAttributeRsIndex() {
        LinkedHashSet<String> pkColumns = dataStore.getColumnNames(pkey);
        List<String> pkColumnsList = new ArrayList<String>(pkColumns);
        int[] indexes = new int[featureType.getAttributeCount()];
        int exposedPks = 0;
        for(int i = 0; i < indexes.length; i++) {
            String attName = featureType.getDescriptor(i).getLocalName();
            if(pkColumns.contains(attName)) {
                indexes[i] = pkColumnsList.indexOf(attName) + 1;
                exposedPks++;
            } else {
                indexes[i] = i + pkColumns.size() - exposedPks + 1;
            }
        }
        return indexes;
    }

    public void close() throws IOException {
        if ( dataStore != null ) {
            //clean up
            dataStore.closeSafe( rs );
            dataStore.closeSafe( st );

            dataStore.releaseConnection(cx, featureSource.getState() );
        }
        else {
            //means we are already closed... should we throw an exception?
        }
        cleanup();
    }

    /**
     * Cleans up the reader state without closing the accessory resultset, statement
     * and connection. Use only if the above are shared with another object that will
     * take care of closing them.
     */
    protected void cleanup() {
        //throw away state
        rs = null;
        st = null;
        dataStore = null;
        featureSource = null;
        featureType = null;
        geometryFactory = null;
        tx = null;
        hints = null;
        next = null;
        builder = null;
        tracer = null;
    }
    
    @Override
    protected void finalize() throws Throwable {
        if(dataStore != null) {
            LOGGER.warning("There is code leaving feature readers/iterators open, this is leaking statements and connections!");
            if(TRACE_ENABLED) {
                LOGGER.log(Level.WARNING, "The unclosed reader originated on this stack trace", tracer);
            }
            close();
        }
    }
    
     /**
     * Feature wrapper around a result set.
     */
    protected class ResultSetFeature implements SimpleFeature {
        /**
         * result set
         */
        ResultSet rs;
        /**
         * connection
         */
        Connection cx;
        /**
         * primary key
         */
        PrimaryKey key;
        
        /**
         * updated values
         * */
        Object[] values;

        /**
         * fid
         */
        FeatureId fid;

        /**
         * dirty flags
         */
        boolean[] dirty;
        
        /**
         * Marks this feature as "new", about to be inserted
         */
        boolean newFeature;
        
        /**
         * name index
         */
        HashMap<String, Integer> index;
        /**
         * user data
         */
        HashMap<Object, Object> userData = new HashMap<Object, Object>();
        
        /**
         * true if primary keys are not returned (the default is false)
         */
        boolean exposePrimaryKeys;

        ResultSetFeature(ResultSet rs, Connection cx) throws SQLException, IOException {
            this.rs = rs;
            this.cx = cx;
            
            //get the result set metadata
            ResultSetMetaData md = rs.getMetaData();

            //get the primary key, ensure its not contained in the values
            key = dataStore.getPrimaryKey(featureType);
            int count = md.getColumnCount();
            columnNames=new String[count];

            exposePrimaryKeys = featureSource.getState().isExposePrimaryKeyColumns();
            for (int i = 0; i < md.getColumnCount(); i++) {
            	String columnName =md.getColumnName(i + 1); 
            	columnNames[i]=columnName;
            	if(!exposePrimaryKeys) {
                    for ( PrimaryKeyColumn col : key.getColumns() ) {
                        if (col.getName().equals(columnName)) {
                            count--;
                            break;
                        }    
                    }
            	}
                
            }

            //set up values
            values = new Object[count];
            dirty = new boolean[values.length];

            //set up name lookup
            index = new HashMap<String, Integer>();

            int offset = 0;

         O: for (int i = 0; i < md.getColumnCount(); i++) {
                if(!exposePrimaryKeys) {
                    for( PrimaryKeyColumn col : key.getColumns() ) {
                        if ( col.getName().equals( md.getColumnName(i+1))) {
                            offset++;
                            continue O;
                        }
                    }
                }
                
                index.put(md.getColumnName(i + 1), i - offset);
            }
        }

        public void init(String fid) {
            // mark as new according to the fid
            newFeature = fid == null;
            
            //clear values
            for (int i = 0; i < values.length; i++) {
                values[i] = null;
                dirty[i] = false;
            }

            this.fid = SimpleFeatureBuilder.createDefaultFeatureIdentifier(fid);
        }

        public void init() throws SQLException, IOException {
            //get fid
            //PrimaryKey pkey = dataStore.getPrimaryKey(featureType);

            //TODO: factory fid prefixing out
            init(featureType.getTypeName() + "." + dataStore.encodeFID( key, rs ));
        }

        public SimpleFeatureType getFeatureType() {
            return featureType;
        }

        public SimpleFeatureType getType() {
            return featureType;
        }

        public FeatureId getIdentifier() {
            return fid;
        }
        public String getID() {
            return fid.getID();
        }

        public void setID( String id ) {
            ((FeatureIdImpl)fid).setID(id);
        }
        
        public Object getAttribute(String name) {
            return getAttribute(index.get(name));
        }

        public Object getAttribute(Name name) {
            return getAttribute(name.getLocalPart());
        }

        public Object getAttribute(int index) throws IndexOutOfBoundsException {
            return getAttributeInternal( index, mapToResultSetIndex(index) );
        }

        private int mapToResultSetIndex( int index ) {
            //map the index to result set
            int rsindex = index;
            
            for ( int i = 0; i <= index; i++ ) {
                if(!exposePrimaryKeys) {
                    for( PrimaryKeyColumn col : key.getColumns() ) {
                        if ( col.getName().equals( columnNames[i])) {
                            rsindex++;
                            break;
                        }
                    }
                }
            }
            
            rsindex++;
            return rsindex;
        }
        
        private Object getAttributeInternal( int index, int rsindex ) {
            if (!newFeature && values[index] == null && !dirty[index]) {
                synchronized (this) {
                    try {
                        if (!newFeature && values[index] == null && !dirty[index]) {
                            //load the value from the result set, check the case 
                            // in which its a geometry, this case the dialect needs
                            // to read it
                            
                                AttributeDescriptor att = featureType.getDescriptor(index);
                                if ( att instanceof GeometryDescriptor ) {
                                    GeometryDescriptor gatt = (GeometryDescriptor) att;
                                    values[index] = dataStore.getSQLDialect()
                                        .decodeGeometryValue( gatt, rs, rsindex, dataStore.getGeometryFactory(), st.getConnection() );
                                }
                                else {
                                    values[index] = rs.getObject( rsindex );    
                                }
                        }
                    } catch (IOException e ) {
                        throw new RuntimeException( e );
                     } catch (SQLException e) {
                         //do not throw exception because of insert mode
                         //TODO: set a flag for insert vs update
                         //throw new RuntimeException( e );
                         values[index] = null;
                     }
                               
                }
            }
            return values[index];
        }
        
        public void setAttribute(String name, Object value) {
            dataStore.getLogger().fine("Setting " + name + " to " + value);

            int i = index.get(name);
            setAttribute(i, value);
        }

        public void setAttribute(Name name, Object value) {
            setAttribute(name.getLocalPart(), value);
        }

        public void setAttribute(int index, Object value)
            throws IndexOutOfBoundsException {
            dataStore.getLogger().fine("Setting " + index + " to " + value);
            values[index] = value;
            dirty[index] = true;
        }

        public void setAttributes(List<Object> values) {
            for (int i = 0; i < values.size(); i++) {
                setAttribute(i, values.get(i));
            }
        }

        public int getAttributeCount() {
            return values.length;
        }

        public boolean isDirty(int index) {
            return dirty[index];
        }

        public boolean isDirrty(String name) {
            return isDirty(index.get(name));
        }

        public void close() {
            rs = null;
            cx = null;
            columnNames=null;
        }

        public List<Object> getAttributes() {
            throw new UnsupportedOperationException();
        }

        public Object getDefaultGeometry() {
            GeometryDescriptor defaultGeometry = featureType.getGeometryDescriptor();
            return defaultGeometry != null ? getAttribute( defaultGeometry.getName() ) : null;
        }

        public void setAttributes(Object[] object) {
            throw new UnsupportedOperationException();
        }

        public void setDefaultGeometry(Object defaultGeometry) {
            GeometryDescriptor descriptor = featureType.getGeometryDescriptor();
            setAttribute(descriptor.getName(), defaultGeometry );            
        }

        public BoundingBox getBounds() {
            Object obj = getDefaultGeometry();
            if( obj instanceof Geometry ){
                Geometry geometry = (Geometry) obj;
                return new ReferencedEnvelope( geometry.getEnvelopeInternal(), featureType.getCoordinateReferenceSystem() );
            }
            return new ReferencedEnvelope( featureType.getCoordinateReferenceSystem() );
        }

        public GeometryAttribute getDefaultGeometryProperty() {
            throw new UnsupportedOperationException();
        }

        public void setDefaultGeometryProperty(GeometryAttribute defaultGeometry) {
            throw new UnsupportedOperationException();
        }

        public Collection<Property> getProperties() {
            throw new UnsupportedOperationException();
        }

        public Collection<Property> getProperties(Name name) {
            throw new UnsupportedOperationException();
        }

        public Collection<Property> getProperties(String name) {
            throw new UnsupportedOperationException();
        }

        public Property getProperty(Name name) {
            throw new UnsupportedOperationException();
        }

        public Property getProperty(String name) {
            throw new UnsupportedOperationException();
        }

        public Collection<?extends Property> getValue() {
            throw new UnsupportedOperationException();
        }

        public void setValue(Collection<Property> value) {
            throw new UnsupportedOperationException();
        }

        public AttributeDescriptor getDescriptor() {
            throw new UnsupportedOperationException();
        }

        public Name getName() {
            throw new UnsupportedOperationException();
        }

        public Map<Object, Object> getUserData() {
            return userData;
        }

        public boolean isNillable() {
            throw new UnsupportedOperationException();
        }

        public void setValue(Object value) {
            throw new UnsupportedOperationException();
        }
        public void validate() {
        }
    }

}
