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
            final String sql,
            final Connection cx,
            final JDBCFeatureSource featureSource,
            final SimpleFeatureType featureType,
            final Query query)
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
            final PreparedStatement st,
            final Connection cx,
            final JDBCFeatureSource featureSource,
            final SimpleFeatureType featureType,
            final Query query)
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
            final ResultSet rs,
            final Connection cx,
            final int offset,
            final JDBCFeatureSource featureSource,
            final SimpleFeatureType featureType,
            final Query query)
            throws SQLException {
        init(featureSource, featureType, query);

        this.cx = cx;
        this.st = rs.getStatement();
        this.rs = rs;
        this.offset = offset;
    }

    protected void init(
            final JDBCFeatureSource featureSource,
            final SimpleFeatureType featureType,
            final Query query) {
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
                hints != null ? (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY) : null;
        if (geometryFactory == null) {
            // look for a coordinate sequence factory
            final CoordinateSequenceFactory csFactory =
                    (CoordinateSequenceFactory) hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);

            if (csFactory != null) {
                geometryFactory = new GeometryFactory(csFactory);
            }
        }

        if (geometryFactory == null) {
            // fall back on one privided by datastore
            geometryFactory = dataStore.getGeometryFactory();
        }

        final Double linearizationTolerance =
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
        if (ff == null) {
            ff = featureSource.getDataStore().getFeatureFactory();
        }
        builder = new SimpleFeatureBuilder(featureType, ff);

        // find the primary key
        try {
            pkey = dataStore.getPrimaryKey(featureType);
        } catch (final IOException e) {
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

    void runQuery(final QueryRunner runner, final Statement st) throws SQLException {
        callback.beforeQuery(st);
        try {
            rs = runner.run();
            callback.afterQuery(st);
        } catch (final Exception e1) {
            callback.queryError(e1);

            // make sure to mark as closed, otherwise we are going to log that it was not
            try {
                close();
            } catch (final IOException e2) {
                LOGGER.log(Level.FINE, "Failed to close the reader, moving on", e2);
            }
            throw new SQLException(e1);
        }
    }

    public JDBCFeatureReader(final JDBCFeatureReader other) {
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

    public void setNext(final Boolean next) {
        this.next = next;
        if (next == null) {
            nextFeature = null;
        }
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public PrimaryKey getPrimaryKey() {
        return pkey;
    }

    public Query getQuery() {
        return query;
    }

    @Override
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
            } catch (final SQLException e) {
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
        } catch (final Exception e) {
            throw new RuntimeException("Could not determine fid from primary key", e);
        }

        // round up attributes
        final int attributeCount = featureType.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            final AttributeDescriptor type = featureType.getDescriptor(i);

            try {
                Object value = null;

                // is this a geometry?
                if (type instanceof GeometryDescriptor) {
                    final GeometryDescriptor gatt = (GeometryDescriptor) type;

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
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (value != null) {
                        // check to see if a crs was set
                        final Geometry geometry = (Geometry) value;
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
                        } catch (final TransformException e) {
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
                    final Class binding = type.getType().getBinding();
                    final Object converted = Converters.convert(value, binding);
                    if (converted != null && converted != value) {
                        value = converted;
                        if (dataStore.getLogger().isLoggable(Level.FINER)) {
                            final String msg =
                                    value
                                            + " is not of type "
                                            + binding.getName()
                                            + ", attempting conversion";
                            dataStore.getLogger().finer(msg);
                        }
                    }
                }

                builder.add(value);
            } catch (final SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // create the feature
        try {
            return builder.buildFeature(fid);
        } catch (final IllegalAttributeException e) {
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

    @Override
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
     *
     * @return
     */
    private int[] buildAttributeRsIndex() {
        final LinkedHashSet<String> pkColumns = dataStore.getColumnNames(pkey);
        final List<String> pkColumnsList = new ArrayList<String>(pkColumns);
        final int[] indexes = new int[featureType.getAttributeCount()];
        int exposedPks = 0;
        for (int i = 0; i < indexes.length; i++) {
            final String attName = featureType.getDescriptor(i).getLocalName();
            if (pkColumns.contains(attName)) {
                indexes[i] = pkColumnsList.indexOf(attName) + 1;
                exposedPks++;
            } else {
                indexes[i] = i + pkColumns.size() - exposedPks + 1;
            }
        }
        return indexes;
    }

    @Override
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

        ResultSetFeature(final ResultSet rs, final Connection cx) throws SQLException, IOException {
            this.rs = rs;
            this.cx = cx;

            // get the primary key, ensure its not contained in the values
            key = dataStore.getPrimaryKey(featureType);
            int count = md.getColumnCount();
            columnNames = new String[count];

            exposePrimaryKeys = featureSource.getState().isExposePrimaryKeyColumns();
            for (int i = 0; i < md.getColumnCount(); i++) {
                final String columnName = md.getColumnName(i + 1);
                columnNames[i] = columnName;
                if (!exposePrimaryKeys) {
                    for (final PrimaryKeyColumn col : key.getColumns()) {
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
                    for (final PrimaryKeyColumn col : key.getColumns()) {
                        if (col.getName().equals(md.getColumnName(i + 1))) {
                            offset++;
                            continue O;
                        }
                    }
                }

                index.put(md.getColumnName(i + 1), i - offset);
            }
        }

        public void init(final String fid) {
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

            // ensure initialized values GEOT-6264
            for (int k = 0; k < values.length; k++) {
                getAttribute(k);
            }
        }

        @Override
        public SimpleFeatureType getFeatureType() {
            return featureType;
        }

        @Override
        public SimpleFeatureType getType() {
            return featureType;
        }

        @Override
        public FeatureId getIdentifier() {
            return fid;
        }

        @Override
        public String getID() {
            return fid.getID();
        }

        public void setID(final String id) {
            ((FeatureIdImpl) fid).setID(id);
        }

        @Override
        public Object getAttribute(final String name) {
            return getAttribute(index.get(name));
        }

        @Override
        public Object getAttribute(final Name name) {
            return getAttribute(name.getLocalPart());
        }

        @Override
        public Object getAttribute(final int index) throws IndexOutOfBoundsException {
            return getAttributeInternal(index, mapToResultSetIndex(index));
        }

        private int mapToResultSetIndex(final int index) {
            // map the index to result set
            int rsindex = index;

            for (int i = 0; i <= index; i++) {
                if (!exposePrimaryKeys) {
                    for (final PrimaryKeyColumn col : key.getColumns()) {
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

        private Object getAttributeInternal(final int index, final int rsindex) {
            if (!newFeature && values[index] == null && !dirty[index]) {
                synchronized (this) {
                    try {
                        if (!newFeature && values[index] == null && !dirty[index]) {
                            // load the value from the result set, check the case
                            // in which its a geometry, this case the dialect needs
                            // to read it

                            final AttributeDescriptor att = featureType.getDescriptor(index);
                            if (att instanceof GeometryDescriptor) {
                                final GeometryDescriptor gatt = (GeometryDescriptor) att;
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
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    } catch (final SQLException e) {
                        // do not throw exception because of insert mode
                        // TODO: set a flag for insert vs update
                        // throw new RuntimeException( e );
                        values[index] = null;
                    }
                }
            }
            return values[index];
        }

        @Override
        public void setAttribute(final String name, final Object value) {
            if (dataStore.getLogger().isLoggable(Level.FINE)) {
                dataStore.getLogger().fine("Setting " + name + " to " + value);
            }

            final int i = index.get(name);
            setAttribute(i, value);
        }

        @Override
        public void setAttribute(final Name name, final Object value) {
            setAttribute(name.getLocalPart(), value);
        }

        @Override
        public void setAttribute(final int index, final Object value)
                throws IndexOutOfBoundsException {
            if (dataStore.getLogger().isLoggable(Level.FINE)) {
                dataStore.getLogger().fine("Setting " + index + " to " + value);
            }
            values[index] = value;
            dirty[index] = true;
        }

        @Override
        public void setAttributes(final List<Object> values) {
            for (int i = 0; i < values.size(); i++) {
                setAttribute(i, values.get(i));
            }
        }

        @Override
        public int getAttributeCount() {
            return values.length;
        }

        public boolean isDirty(final int index) {
            return dirty[index];
        }

        /** @deprecated use {@link #isDirty(String)} instead */
        @Deprecated
        public boolean isDirrty(final String name) {
            return isDirty(name);
        }

        public boolean isDirty(final String name) {
            return isDirty(index.get(name));
        }

        public void close() {
            rs = null;
            cx = null;
            columnNames = null;
        }

        @Override
        public List<Object> getAttributes() {
            return Arrays.asList(values);
        }

        @Override
        public Object getDefaultGeometry() {
            final GeometryDescriptor defaultGeometry = featureType.getGeometryDescriptor();
            return defaultGeometry != null ? getAttribute(defaultGeometry.getName()) : null;
        }

        @Override
        public void setAttributes(final Object[] object) {
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

        @Override
        public void setDefaultGeometry(final Object defaultGeometry) {
            final GeometryDescriptor descriptor = featureType.getGeometryDescriptor();
            setAttribute(descriptor.getName(), defaultGeometry);
        }

        @Override
        public BoundingBox getBounds() {
            final Object obj = getDefaultGeometry();
            if (obj instanceof Geometry) {
                final Geometry geometry = (Geometry) obj;
                return ReferencedEnvelope.create(
                        geometry.getEnvelopeInternal(), featureType.getCoordinateReferenceSystem());
            }
            return ReferencedEnvelope.create(featureType.getCoordinateReferenceSystem());
        }

        @Override
        public GeometryAttribute getDefaultGeometryProperty() {
            final GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
            GeometryAttribute geometryAttribute = null;
            if (geometryDescriptor != null) {
                final Object defaultGeometry = getDefaultGeometry();
                geometryAttribute =
                        new GeometryAttributeImpl(defaultGeometry, geometryDescriptor, null);
            }
            return geometryAttribute;
        }

        @Override
        public void setDefaultGeometryProperty(final GeometryAttribute defaultGeometry) {
            if (defaultGeometry != null) {
                setDefaultGeometry(defaultGeometry.getValue());
            } else {
                setDefaultGeometry(null);
            }
        }

        @Override
        public Collection<Property> getProperties() {
            throw new UnsupportedOperationException("Use getAttributes()");
        }

        @Override
        public Collection<Property> getProperties(final Name name) {
            throw new UnsupportedOperationException("Use getAttributes()");
        }

        @Override
        public Collection<Property> getProperties(final String name) {
            throw new UnsupportedOperationException("Use getAttributes()");
        }

        @Override
        public Property getProperty(final Name name) {
            throw new UnsupportedOperationException("Use getAttribute()");
        }

        @Override
        public Property getProperty(final String name) {
            throw new UnsupportedOperationException("Use getAttribute()");
        }

        @Override
        public Collection<? extends Property> getValue() {
            return getProperties();
        }

        @Override
        public void setValue(final Collection<Property> value) {
            final int i = 0;
            for (final Property p : value) {
                this.values[i] = p.getValue();
            }
        }

        @Override
        public AttributeDescriptor getDescriptor() {
            return new AttributeDescriptorImpl(
                    featureType, featureType.getName(), 0, Integer.MAX_VALUE, true, null);
        }

        @Override
        public Name getName() {
            return featureType.getName();
        }

        @Override
        public Map<Object, Object> getUserData() {
            return userData;
        }

        @Override
        public boolean isNillable() {
            return true;
        }

        @Override
        public void setValue(final Object value) {
            setValue((Collection<Property>) value);
        }

        @Override
        public void validate() {
            for (int i = 0; i < values.length; i++) {
                final AttributeDescriptor descriptor = getType().getDescriptor(i);
                Types.validate(descriptor, values[i]);
            }
        }
    }
}
