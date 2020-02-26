/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureWriter;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureLocking;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

/**
 * Abstract implementation of FeatureStore.
 *
 * <p>List its base class {@link ContentFeatureSource}, this feature store works off of operations
 * provided by {@link FeatureCollection}.
 *
 * <p>The {@link #addFeatures(SimpleFeatureCollection)} method is used to add features to the
 * feature store. The method should return the "persistent" feature id's which are generated after
 * the feature has been added to persistent storage. Often the persistent fid is different from the
 * fid specified by the actual feature being inserted. For this reason {@link
 * SimpleFeature#getUserData()} is used to report back persistent fids. It is up to the implementor
 * of the feature collection to report this value back after a feature has been inserted. As an
 * example, consider an implementation of {@link FeatureCollection#add(Object)}.
 *
 * <pre>
 *  boolean add( Object o ) {
 *    SimpleFeature feature = (SimpleFeature) o;
 *
 *    //1.add the feature to storage
 *    ...
 *
 *    //2. derive the persistent fid
 *    String fid = ...;
 *
 *    //3. set the user data
 *    feature.getUserData().put( "fid", fid );
 *
 *  }
 * </pre>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public abstract class ContentFeatureStore extends ContentFeatureSource
        implements SimpleFeatureStore, SimpleFeatureLocking {

    /** Flag writer for adding new content */
    protected final int WRITER_ADD = ContentDataStore.WRITER_ADD;
    /** Flag writer for updating content in place */
    protected final int WRITER_UPDATE = ContentDataStore.WRITER_UPDATE;
    /** Flag writer for commit (AUTO_COMMIT with no events) */
    protected final int WRITER_COMMIT = ContentDataStore.WRITER_COMMIT;

    public static final String ORIGINAL_FEATURE_KEY = "_original_";

    /**
     * Creates the content feature store.
     *
     * @param entry The entry for the feature store.
     * @param query The defining query.
     */
    public ContentFeatureStore(ContentEntry entry, Query query) {
        super(entry, query);
    }

    /**
     * Returns a writer over features specified by a filter.
     *
     * @param filter The filter
     */
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getWriter(Filter filter)
            throws IOException {
        return getWriter(filter, WRITER_ADD | WRITER_UPDATE);
    }

    /**
     * Returns a writer over features specified by a filter.
     *
     * @param filter The filter
     * @param flags flags specifying writing mode
     */
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getWriter(Filter filter, int flags)
            throws IOException {
        return getWriter(new Query(getSchema().getTypeName(), filter), flags);
    }

    /**
     * Returns a writer over features specified by a query.
     *
     * @param query The query
     */
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getWriter(Query query)
            throws IOException {
        return getWriter(query, WRITER_ADD | WRITER_UPDATE);
    }

    /**
     * Returns a writer over features specified by a query.
     *
     * @param query The query
     * @param flags flags specifying writing mode
     */
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getWriter(Query query, int flags)
            throws IOException {
        query = joinQuery(query);
        query = resolvePropertyNames(query);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        if (!canTransact() && transaction != null && transaction != Transaction.AUTO_COMMIT) {
            if ((flags | WRITER_COMMIT) == WRITER_COMMIT) {
                // Simple simple writer with no events or locking
                writer = getWriterInternal(query, flags);
                // filtering may not be needed
                if (!canFilter()) {
                    if (query.getFilter() != null && query.getFilter() != Filter.INCLUDE) {
                        writer = new FilteringFeatureWriter(writer, query.getFilter());
                    }
                }
            } else {
                DiffTransactionState state =
                        (DiffTransactionState) getTransaction().getState(getEntry());
                // reader will take care of filtering
                // DiffContentWriter takes care of events
                @SuppressWarnings("PMD.CloseResource") // wrapped and returned
                FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(query);
                writer = state.diffWriter(this, reader);
            }
        } else {
            writer = getWriterInternal(query, flags);
            // events
            if (!canEvent()) {
                writer = new EventContentFeatureWriter(this, writer);
            }
            // filtering
            if (!canFilter()) {
                if (query.getFilter() != null && query.getFilter() != Filter.INCLUDE) {
                    writer = new FilteringFeatureWriter(writer, query.getFilter());
                }
            }
        }
        // Use InProcessLockingManager to assert write locks?
        if (!canLock()) {
            InProcessLockingManager lockingManager =
                    (InProcessLockingManager) getDataStore().getLockingManager();
            writer = lockingManager.checkedWriter(writer, transaction);
        }
        // Finished
        return writer;
    }

    /**
     * Subclass method for returning a native writer from the datastore.
     *
     * <p>It is important to note that if the native writer intends to handle any of the following
     * natively:
     *
     * <ul>
     *   <li>reprojection
     *   <li>filtering
     *   <li>events
     *   <li>max feature limiting
     *   <li>sorting
     *   <li>locking
     * </ul>
     *
     * Then it <b>*must*</b> set the corresponding flags to <code>true</code>:
     *
     * <ul>
     *   <li>{@link #canReproject()}
     *   <li>{@link #canFilter()}
     *   <li>{@link #canEvent()}
     *   <li>{@link #canLimit()}
     *   <li>{@link #canSort()}
     *   <li>{@link #canLock()}
     * </ul>
     *
     * @param query Query
     * @param flags See {@link #WRITER_ADD} and {@link #WRITER_UPDATE}
     */
    protected abstract FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterInternal(
            Query query, int flags) throws IOException;

    /**
     * Adds a collection of features to the store.
     *
     * <p>This method operates by getting an appending feature writer and writing all the features
     * in <tt>collection</tt> to it. Directly after a feature is written its id is obtained and
     * added to the returned set.
     */
    public List<FeatureId> addFeatures(Collection collection) throws IOException {

        // gather up id's
        List<FeatureId> ids = new ArrayList<FeatureId>(collection.size());

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getWriterAppend();
        try {
            for (Iterator f = collection.iterator(); f.hasNext(); ) {
                FeatureId id = addFeature((SimpleFeature) f.next(), writer);
                ids.add(id);
            }
        } finally {
            writer.close();
        }

        return ids;
    }

    /** Adds a collection of features to the store. */
    public List<FeatureId> addFeatures(
            FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection)
            throws IOException {
        // gather up id's
        List<FeatureId> ids = new ArrayList<FeatureId>();

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getWriterAppend();
        FeatureIterator<SimpleFeature> f = featureCollection.features();
        try {
            while (f.hasNext()) {
                SimpleFeature feature = f.next();
                FeatureId id = addFeature(feature, writer);
                ids.add(id);
            }
        } finally {
            writer.close();
            f.close();
        }
        return ids;
    }

    /** Utility method that ensures we are going to write only in append mode */
    private FeatureWriter<SimpleFeatureType, SimpleFeature> getWriterAppend() throws IOException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                getWriter(Filter.INCLUDE, WRITER_ADD);
        while (writer.hasNext()) {
            writer.next();
        }
        return writer;
    }

    FeatureId addFeature(
            final SimpleFeature feature, FeatureWriter<SimpleFeatureType, SimpleFeature> writer)
            throws IOException {
        // grab next feature and populate it
        // JD: worth a note on how we do this... we take a "pull" approach
        // because the raw schema we are inserting into may not match the
        // schema of the features we are inserting
        final SimpleFeature toWrite = writer.next();
        for (int i = 0; i < toWrite.getType().getAttributeCount(); i++) {
            String name = toWrite.getType().getDescriptor(i).getLocalName();
            toWrite.setAttribute(name, feature.getAttribute(name));
        }

        // copy over the user data
        toWrite.getUserData().putAll(feature.getUserData());

        // pass through the fid if the user asked so
        boolean useExisting =
                Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));
        if (getQueryCapabilities().isUseProvidedFIDSupported() && useExisting) {
            ((FeatureIdImpl) toWrite.getIdentifier()).setID(feature.getID());
        }

        // Need to save a link to the original feature in order to be able to set the ID once it
        // is actually saved (see JDBCInsertFeatureWriter)
        toWrite.getUserData().put(ORIGINAL_FEATURE_KEY, feature);

        // perform the write
        writer.write();

        // copy any metadata from the feature that was actually written (not always effective, see
        // JDBCInsertFeatureWriter)
        feature.getUserData().putAll(toWrite.getUserData());
        feature.getUserData().remove(ORIGINAL_FEATURE_KEY);

        return toWrite.getIdentifier();
    }

    /**
     * Sets the feature of the source.
     *
     * <p>This method operates by first clearing the contents of the feature store ({@link
     * #removeFeatures(Filter)}), and then obtaining an appending feature writer and writing all
     * features from <tt>reader</tt> to it.
     */
    public final void setFeatures(FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        // remove features
        removeFeatures(Filter.INCLUDE);

        // grab a feature writer for insert
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                getWriter(Filter.INCLUDE, WRITER_ADD);
        try {
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();

                // grab next feature and populate it
                // JD: worth a note on how we do this... we take a "pull" approach
                // because the raw schema we are inserting into may not match the
                // schema of the features we are inserting
                SimpleFeature toWrite = writer.next();
                for (int i = 0; i < toWrite.getType().getAttributeCount(); i++) {
                    String name = toWrite.getType().getDescriptor(i).getLocalName();
                    toWrite.setAttribute(name, feature.getAttribute(name));
                }

                // perform the write
                writer.write();
            }
        } finally {
            writer.close();
        }
    }

    public void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
            throws IOException {
        Name attributeNames[] = new Name[type.length];
        for (int i = 0; i < type.length; i++) {
            attributeNames[i] = type[i].getName();
        }
        modifyFeatures(attributeNames, value, filter);
    }

    /**
     * Modifies/updates the features of the store which match the specified filter.
     *
     * <p>This method operates by obtaining an updating feature writer based on the specified
     * <tt>filter</tt> and writing the updated values to it.
     *
     * <p>The <tt>filter</tt> must not be <code>null</code>, in this case this method will throw an
     * {@link IllegalArgumentException}.
     */
    public void modifyFeatures(Name[] type, Object[] value, Filter filter) throws IOException {
        if (filter == null) {
            String msg = "Must specify a filter, must not be null.";
            throw new IllegalArgumentException(msg);
        }
        filter = resolvePropertyNames(filter);

        // grab a feature writer
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getWriter(filter, WRITER_UPDATE);
        try {
            while (writer.hasNext()) {
                SimpleFeature toWrite = writer.next();

                for (int i = 0; i < type.length; i++) {
                    toWrite.setAttribute(type[i], value[i]);
                }

                writer.write();
            }

        } finally {
            writer.close();
        }
    }

    public final void modifyFeatures(String name, Object attributeValue, Filter filter)
            throws IOException {
        modifyFeatures(
                new Name[] {
                    new NameImpl(name),
                },
                new Object[] {
                    attributeValue,
                },
                filter);
    }

    public final void modifyFeatures(String[] names, Object[] values, Filter filter)
            throws IOException {
        Name attributeNames[] = new Name[names.length];
        for (int i = 0; i < names.length; i++) {
            attributeNames[i] = new NameImpl(names[i]);
        }
        modifyFeatures(attributeNames, values, filter);
    }

    /** Calls through to {@link #modifyFeatures(Name[], Object[], Filter)}. */
    public final void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
            throws IOException {

        modifyFeatures(new Name[] {type.getName()}, new Object[] {value}, filter);
    }

    /** Calls through to {@link #modifyFeatures(Name[], Object[], Filter)}. */
    public final void modifyFeatures(Name name, Object value, Filter filter) throws IOException {

        modifyFeatures(new Name[] {name}, new Object[] {value}, filter);
    }

    /**
     * Removes the features from the store which match the specified filter.
     *
     * <p>This method operates by obtaining an updating feature writer based on the specified
     * <tt>filter</tt> and removing every feature from it.
     *
     * <p>The <tt>filter</tt> must not be <code>null</code>, in this case this method will throw an
     * {@link IllegalArgumentException}.
     */
    public void removeFeatures(Filter filter) throws IOException {
        if (filter == null) {
            String msg = "Must specify a filter, must not be null.";
            throw new IllegalArgumentException(msg);
        }
        filter = resolvePropertyNames(filter);

        // grab a feature writer
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getWriter(filter, WRITER_UPDATE);
        try {
            // remove everything
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }

        } finally {
            writer.close();
        }
    }
}
