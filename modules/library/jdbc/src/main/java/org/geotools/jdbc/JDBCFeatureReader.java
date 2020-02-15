/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.util.ScreenMap;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.Types;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.operation.TransformException;

/**
 * Reader for jdbc datastore
 *
 * @author Justin Deoliveira, The Open Plannign Project.
 */
public class JDBCFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    protected static final Logger LOGGER = Logging.getLogger(JDBCFeatureReader.class);

    /**
     * When true, the stack trace that created a reader that wasn't closed is recorded and then
     * printed out when warning the user about this.
     */
    protected static final Boolean TRACE_ENABLED =
            "true".equalsIgnoreCase(System.getProperty("gt2.jdbc.trace"));

    /** The feature source the reader originated from. */
    protected JDBCFeatureSource featureSource;
    /** the datastore */
    protected JDBCDataStore dataStore;
    /** schema of features */
    protected SimpleFeatureType featureType;
    /** geometry factory used to create geometry objects */
    protected GeometryFactory geometryFactory;

    /** the query */
    protected Query query;

    /** hints */
    protected Hints hints;

    /** Screenmap for feature skipping behaviour */
    protected ScreenMap screenMap;

    /** current transaction */
    protected Transaction tx;
    /** flag indicating if the iterator has another feature */
    protected Boolean next;

    /** The next feature to be returned */
    private SimpleFeature nextFeature;

    /** feature builder */
    protected SimpleFeatureBuilder builder;
    /** The primary key */
    protected PrimaryKey pkey;

    /** statement,result set that is being worked from. */
    protected Statement st;

    protected ResultSet rs;
    protected Connection cx;
    protected ResultSetMetaData md;
    protected Exception tracer;
    protected String[] columnNames;

    /** offset/column index to start reading from result set */
    protected int offset = 0;

    protected JDBCReaderCallback callback = JDBCReaderCallback.NULL;
    private int[] attributeRsIndex;

    public JDBCFeatureReader(
            String sql,
            Connection cx,
            JDBCFeatureSource featureSource,
            SimpleFeatureType featureType,
            Query query)
            throws SQLException {
        init(featureSource, featureType, query);

        // create the result set
        this.cx = cx;
        st = cx.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        st.setFetchSize(featureSource.getDataStore().getFetchSize());

        ((BasicSQLDialect) featureSource.getDataStore().getSQLDialect())
                .onSelect(st, cx, featureType);
        runQuery(() -> st.executeQuery(sql), st);
    }

    public JDBCFeatureReader(
            PreparedStatement st,
            Connection cx,
            JDBCFeatureSource featureSource,
            SimpleFeatureType featureType,
            Query query)
            throws SQLException {

        init(featureSource, featureType, query);

        // create the result set
        this.cx = cx;
        this.st = st;

        ((PreparedStatementSQLDialect) featureSource.getDataStore().getSQLDialect())
                .onSelect(st, cx, featureType);
        runQuery(st::executeQuery, st);
    }

    public JDBCFeatureReader(
            ResultSet rs,
            Connection cx,
            int offset,
            JDBCFeatureSource featureSource,
            SimpleFeatureType featureType,
            Query query)
            throws SQLException {
        init(featureSource, featureType, query);

        this.cx = cx;
        this.st = rs.getStatement();
        this.rs = rs;
        this.offset = offset;
    }

    protected void init(
            JDBCFeatureSource featureSource, SimpleFeatureType featureType, Query query) {
        // init the tracer if we need to debug a connection leak
        if (TRACE_ENABLED) {
            tracer = new Exception();
            tracer.fillInStackTrace();
        }

        // init base fields
        this.featureSource = featureSource;
        this.dataStore = featureSource.getDataStore();
        this.featureType = featureType;
        this.tx = featureSource.getTransaction();
        this.query = query;
        this.hints = query != null ? query.getHints() : null;

        // grab a geometry factory... check for a special hint
        geometryFactory =
                (hints != null) ? (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY) : null;
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

        Double linearizationTolerance =
                hints != null ? (Double) hints.get(Hints.LINEARIZATION_TOLERANCE) : null;
        if (linearizationTolerance != null) {
            geometryFactory = new CurvedGeometryFactory(geometryFactory, linearizationTolerance);
        }

        // screenmap support
        this.screenMap = hints != null ? (ScreenMap) hints.get(Hints.SCREENMAP) : null;

        // create a feature builder using the factory hinted or the one coming
        // from the datastore
        FeatureFactory ff =
                hints != null ? (FeatureFactory) hints.get(Hints.FEATURE_FACTORY) : null;
        if (ff == null) ff = featureSource.getDataStore().getFeatureFactory();
        builder = new SimpleFeatureBuilder(featureType, ff);

        // find the primary key
        try {
            pkey = dataStore.getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.attributeRsIndex = buildAttributeRsIndex();

        callback = dataStore.getCallbackFactory().createReaderCallback();
        callback.init(this);
    }

    @FunctionalInterface
    interface QueryRunner {
        ResultSet run() throws Exception;
    }

    void runQuery(QueryRunner runner, Statement st) throws SQLException {
        callback.beforeQuery(st);
        try {
            rs = runner.run();
            callback.afterQuery(st);
        } catch (Exception e1) {
            callback.queryError(e1);

            // make sure to mark as closed, otherwise we are going to log that it was not
            try {
                close();
            } catch (IOException e2) {
                LOGGER.log(Level.FINE, "Failed to close the reader, moving on", e2);
            }
            throw new SQLException(e1);
        }
    }

    public JDBCFeatureReader(JDBCFeatureReader other) {
        this.featureType = other.featureType;
        this.dataStore = other.dataStore;
        this.featureSource = other.featureSource;
        this.tx = other.tx;
        this.hints = other.hints;
        this.geometryFactory = other.geometryFactory;
        this.builder = other.builder;
        this.st = other.st;
        this.rs = other.rs;
        this.md = other.md;
    }

    public void setNext(Boolean next) {
        this.next = next;
        if (next == null) {
            nextFeature = null;
        }
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public PrimaryKey getPrimaryKey() {
        return pkey;
    }

    public Query getQuery() {
        return query;
    }

    public boolean hasNext() throws IOException {
        ensureOpen();

        if (next == null) {
            try {
                while (nextFeature == null && !Boolean.FALSE.equals(next)) {
                    callback.beforeNext(rs);
                    next = Boolean.valueOf(rs.next());
                    callback.afterNext(rs, next);

                    if (next) {
                        nextFeature = readNextFeature();
                    }
                }

                if (!next) {
                    callback.finish(this);
                }
            } catch (SQLException e) {
                callback.rowError(e);
                throw new RuntimeException(e);
            }
        }

        return next.booleanValue();
    }

    protected SimpleFeature readNextFeature() throws IOException {
        // figure out the fid
        String fid;

        try {
            fid = dataStore.encodeFID(pkey, rs, offset);
            if (fid == null) {
                // fid could be null during an outer join
                return null;
            }
            // wrap the fid in the type name
            fid = featureType.getTypeName() + "." + fid;
        } catch (Exception e) {
            throw new RuntimeException("Could not determine fid from primary key", e);
        }

        // round up attributes
        final int attributeCount = featureType.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            AttributeDescriptor type = featureType.getDescriptor(i);

            try {
                Object value = null;

                // is this a geometry?
                if (type instanceof GeometryDescriptor) {
                    GeometryDescriptor gatt = (GeometryDescriptor) type;

                    // read the geometry
                    try {
                        value =
                                dataStore
                                        .getSQLDialect()
                                        .decodeGeometryValue(
                                                gatt,
                                                rs,
                                                offset + attributeRsIndex[i],
                                                geometryFactory,
                                                cx,
                                                hints);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (value != null) {
                        // check to see if a crs was set
                        Geometry geometry = (Geometry) value;
                        if (geometry.getUserData() == null) {
                            // if not set, set from descriptor
                            geometry.setUserData(gatt.getCoordinateReferenceSystem());
                        }

                        try {
                            // is position already busy skip it
                            if (screenMap != null) {
                                if (screenMap.canSimplify(geometry.getEnvelopeInternal())) {
                                    if (screenMap.checkAndSet(geometry.getEnvelopeInternal())) {
                                        builder.reset();
                                        return null;
                                    } else {
                                        value = screenMap.getSimplifiedShape(geometry);
                                    }
                                }
                            }
                        } catch (TransformException e) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(
                                        Level.WARNING,
                                        "Failed to process screenmap checks, proceeding without",
                                        e);
                            }
                        }
                    }

                } else {
                    value = rs.getObject(offset + attributeRsIndex[i]);
                }

                // they value may need conversion. We let converters chew the initial
                // value towards the target type, if the result is not the same as the
                // original, then a conversion happened and we may want to report it to the
                // user (being the feature type reverse engineerd, it's unlikely a true
                // conversion will be needed)
                if (value != null) {
                    Class binding = type.getType().getBinding();
                    Object converted = Converters.convert(value, binding);
                    if (converted != null && converted != value) {
                        value = converted;
                        if (dataStore.getLogger().isLoggable(Level.FINER)) {
                            String msg =
                                    value
                                            + " is not of type "
                                            + binding.getName()
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
            builder.reset();
            throw new RuntimeException(e);
        }
    }

    protected void ensureNext() {
        if (next == null) {
            throw new IllegalStateException("Must call hasNext before calling next");
        }
    }

    protected void ensureOpen() throws IOException {
        if (rs == null) {
            throw new IOException("reader already closed");
        }
    }

    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        try {
            ensureOpen();
            if (!hasNext()) {
                throw new NoSuchElementException(
                        "No more features in this reader, you should call "
                                + "hasNext() to check for feature availability");
            }

            // join readers share the same resultset among many readers, won't call hasNext() on
            // each
            if (nextFeature == null) {
                nextFeature = readNextFeature();
            }
            if (nextFeature == null && screenMap != null) {
                throw new IllegalStateException(
                        "Feature joining currently not supported along screenmap");
            }

            return nextFeature;
        } finally {
            // reset the next flag. We do this in a finally block to make sure we
            // move to the next record no matter what, if the current one could
            // not be read there is no salvation for it anyways
            nextFeature = null;
            next = null;
        }
    }

    /**
     * Builds an array containing the position in the result set for each attribute. It takes into
     * account that rs positions start by one, about the exposed primary keys, and the fact that
     * exposed pk can be only partially selected in the output
     */
    private int[] buildAttributeRsIndex() {
        LinkedHashSet<String> pkColumns = dataStore.getColumnNames(pkey);
        List<String> pkColumnsList = new ArrayList<String>(pkColumns);
        int[] indexes = new int[featureType.getAttributeCount()];
        int exposedPks = 0;
        for (int i = 0; i < indexes.length; i++) {
            String attName = featureType.getDescriptor(i).getLocalName();
            if (pkColumns.contains(attName)) {
                indexes[i] = pkColumnsList.indexOf(attName) + 1;
                exposedPks++;
            } else {
                indexes[i] = i + pkColumns.size() - exposedPks + 1;
            }
        }
        return indexes;
    }

    public void close() throws IOException {
        if (dataStore != null) {
            // clean up
            dataStore.closeSafe(rs);
            dataStore.closeSafe(st);

            dataStore.releaseConnection(cx, featureSource.getState());
        }

        cleanup();
    }

    /**
     * Cleans up the reader state without closing the accessory resultset, statement and connection.
     * Use only if the above are shared with another object that will take care of closing them.
     */
    protected void cleanup() throws IOException {
        // throw away state
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
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        if (dataStore != null) {
            LOGGER.warning(
                    "There is code leaving feature readers/iterators open, this is leaking statements and connections!");
            if (TRACE_ENABLED) {
                LOGGER.log(
                        Level.WARNING,
                        "The unclosed reader originated on this stack trace",
                        tracer);
            }
            close();
        }
    }

    /**
     * Feature wrapper around a result set. (used only by the writing subclasses, make sure the
     * metadata field is initialized before using it)
     */
    protected class ResultSetFeature implements SimpleFeature {
        /** result set */
        ResultSet rs;
        /** connection */
        Connection cx;
        /** primary key */
        PrimaryKey key;

        /** updated values */
        Object[] values;

        /** fid */
        FeatureId fid;

        /** dirty flags */
        boolean[] dirty;

        /** Marks this feature as "new", about to be inserted */
        boolean newFeature;

        /** name index */
        HashMap<String, Integer> index;
        /** user data */
        HashMap<Object, Object> userData = new HashMap<Object, Object>();

        /** true if primary keys are not returned (the default is false) */
        boolean exposePrimaryKeys;

        ResultSetFeature(ResultSet rs, Connection cx) throws SQLException, IOException {
            this.rs = rs;
            this.cx = cx;

            // get the primary key, ensure its not contained in the values
            key = dataStore.getPrimaryKey(featureType);
            int count = md.getColumnCount();
            columnNames = new String[count];

            exposePrimaryKeys = featureSource.getState().isExposePrimaryKeyColumns();
            for (int i = 0; i < md.getColumnCount(); i++) {
                String columnName = md.getColumnName(i + 1);
                columnNames[i] = columnName;
                if (!exposePrimaryKeys) {
                    for (PrimaryKeyColumn col : key.getColumns()) {
                        if (col.getName().equals(columnName)) {
                            count--;
                            break;
                        }
                    }
                }
            }

            // set up values
            values = new Object[count];
            dirty = new boolean[values.length];

            // set up name lookup
            index = new HashMap<String, Integer>();

            int offset = 0;

            O:
            for (int i = 0; i < md.getColumnCount(); i++) {
                if (!exposePrimaryKeys) {
                    for (PrimaryKeyColumn col : key.getColumns()) {
                        if (col.getName().equals(md.getColumnName(i + 1))) {
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

            // clear values
            for (int i = 0; i < values.length; i++) {
                values[i] = null;
                dirty[i] = false;
            }

            this.fid = SimpleFeatureBuilder.createDefaultFeatureIdentifier(fid);
        }

        public void init() throws SQLException, IOException {
            // get fid
            // PrimaryKey pkey = dataStore.getPrimaryKey(featureType);

            // TODO: factory fid prefixing out
            init(featureType.getTypeName() + "." + dataStore.encodeFID(key, rs, offset));
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

        public void setID(String id) {
            ((FeatureIdImpl) fid).setID(id);
        }

        public Object getAttribute(String name) {
            return getAttribute(index.get(name));
        }

        public Object getAttribute(Name name) {
            return getAttribute(name.getLocalPart());
        }

        public Object getAttribute(int index) throws IndexOutOfBoundsException {
            return getAttributeInternal(index, mapToResultSetIndex(index));
        }

        private int mapToResultSetIndex(int index) {
            // map the index to result set
            int rsindex = index;

            for (int i = 0; i <= index; i++) {
                if (!exposePrimaryKeys) {
                    for (PrimaryKeyColumn col : key.getColumns()) {
                        if (col.getName().equals(columnNames[i])) {
                            rsindex++;
                            break;
                        }
                    }
                }
            }

            rsindex++;
            return rsindex;
        }

        private Object getAttributeInternal(int index, int rsindex) {
            if (!newFeature && values[index] == null && !dirty[index]) {
                synchronized (this) {
                    try {
                        if (!newFeature && values[index] == null && !dirty[index]) {
                            // load the value from the result set, check the case
                            // in which its a geometry, this case the dialect needs
                            // to read it

                            AttributeDescriptor att = featureType.getDescriptor(index);
                            if (att instanceof GeometryDescriptor) {
                                GeometryDescriptor gatt = (GeometryDescriptor) att;
                                values[index] =
                                        dataStore
                                                .getSQLDialect()
                                                .decodeGeometryValue(
                                                        gatt,
                                                        rs,
                                                        rsindex,
                                                        dataStore.getGeometryFactory(),
                                                        cx,
                                                        hints);
                            } else {
                                values[index] = rs.getObject(rsindex);
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        // do not throw exception because of insert mode
                        // TODO: set a flag for insert vs update
                        // throw new RuntimeException( e );
                        values[index] = null;
                    }
                }
            }
            return values[index];
        }

        public void setAttribute(String name, Object value) {
            if (dataStore.getLogger().isLoggable(Level.FINE)) {
                dataStore.getLogger().fine("Setting " + name + " to " + value);
            }

            int i = index.get(name);
            setAttribute(i, value);
        }

        public void setAttribute(Name name, Object value) {
            setAttribute(name.getLocalPart(), value);
        }

        public void setAttribute(int index, Object value) throws IndexOutOfBoundsException {
            if (dataStore.getLogger().isLoggable(Level.FINE)) {
                dataStore.getLogger().fine("Setting " + index + " to " + value);
            }
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

        public boolean isDirty(String name) {
            return isDirty(index.get(name));
        }

        /** Just releasing references, not an actual "Closeable" close */
        public void close() {
            rs = null;
            cx = null;
            columnNames = null;
        }

        public List<Object> getAttributes() {
            // ensure initialized values GEOT-6264
            for (int k = 0; k < values.length; k++) getAttribute(k);
            return Arrays.asList(values);
        }

        public Object getDefaultGeometry() {
            GeometryDescriptor defaultGeometry = featureType.getGeometryDescriptor();
            return defaultGeometry != null ? getAttribute(defaultGeometry.getName()) : null;
        }

        public void setAttributes(Object[] object) {
            if (object == null) {
                throw new NullPointerException("Attributes array is null");
            } else if (object.length != values.length) {
                throw new IllegalArgumentException(
                        "The passed array has wrong size: passed_size="
                                + object.length
                                + " values_size"
                                + values.length);
            }
            for (int i = 0; i < object.length; i++) {
                setAttribute(i, object[i]);
            }
        }

        public void setDefaultGeometry(Object defaultGeometry) {
            GeometryDescriptor descriptor = featureType.getGeometryDescriptor();
            setAttribute(descriptor.getName(), defaultGeometry);
        }

        public BoundingBox getBounds() {
            Object obj = getDefaultGeometry();
            if (obj instanceof Geometry) {
                Geometry geometry = (Geometry) obj;
                return ReferencedEnvelope.create(
                        geometry.getEnvelopeInternal(), featureType.getCoordinateReferenceSystem());
            }
            return ReferencedEnvelope.create(featureType.getCoordinateReferenceSystem());
        }

        public GeometryAttribute getDefaultGeometryProperty() {
            GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
            GeometryAttribute geometryAttribute = null;
            if (geometryDescriptor != null) {
                Object defaultGeometry = getDefaultGeometry();
                geometryAttribute =
                        new GeometryAttributeImpl(defaultGeometry, geometryDescriptor, null);
            }
            return geometryAttribute;
        }

        public void setDefaultGeometryProperty(GeometryAttribute defaultGeometry) {
            if (defaultGeometry != null) setDefaultGeometry(defaultGeometry.getValue());
            else setDefaultGeometry(null);
        }

        public Collection<Property> getProperties() {
            throw new UnsupportedOperationException("Use getAttributes()");
        }

        public Collection<Property> getProperties(Name name) {
            throw new UnsupportedOperationException("Use getAttributes()");
        }

        public Collection<Property> getProperties(String name) {
            throw new UnsupportedOperationException("Use getAttributes()");
        }

        public Property getProperty(Name name) {
            throw new UnsupportedOperationException("Use getAttribute()");
        }

        public Property getProperty(String name) {
            throw new UnsupportedOperationException("Use getAttribute()");
        }

        public Collection<? extends Property> getValue() {
            return getProperties();
        }

        public void setValue(Collection<Property> value) {
            int i = 0;
            for (Property p : value) {
                this.values[i] = p.getValue();
            }
        }

        public AttributeDescriptor getDescriptor() {
            return new AttributeDescriptorImpl(
                    featureType, featureType.getName(), 0, Integer.MAX_VALUE, true, null);
        }

        public Name getName() {
            return featureType.getName();
        }

        public Map<Object, Object> getUserData() {
            return userData;
        }

        public boolean isNillable() {
            return true;
        }

        public void setValue(Object value) {
            setValue((Collection<Property>) value);
        }

        public void validate() {
            for (int i = 0; i < values.length; i++) {
                AttributeDescriptor descriptor = getType().getDescriptor(i);
                Types.validate(descriptor, values[i]);
            }
        }
    }
}
