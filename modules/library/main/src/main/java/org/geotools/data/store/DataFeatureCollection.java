/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.CollectionEvent;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureReaderIterator;
import org.geotools.feature.collection.DelegateSimpleFeatureIterator;
import org.geotools.feature.collection.SubFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * A starting point for implementing FeatureCollection's backed onto a FeatureReader.
 *
 * <p>This implementation requires you to implement the following:
 *
 * <ul>
 *   <li>getSchema() - this should match reader.getSchema()
 *   <li>reader()</br> features() - override one of these two method to access content
 *   <li>getBounds()
 *   <li>getCount()
 *   <li>collection()
 *       <p>This class will implement the 'extra' methods required by FeatureCollection for you (in simple terms based
 *       on the FeatureResults API). Anything that is <i>often</i> customised is available to you as a constructor
 *       parameters.
 *       <p>Enjoy.
 *
 * @author jgarnett
 * @since 2.1.RC0
 */
public abstract class DataFeatureCollection implements SimpleFeatureCollection {

    /** logger */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DataFeatureCollection.class);

    private static int unique = 0;

    /** Collection based on a generic collection */
    protected DataFeatureCollection() {
        this("features" + (unique++));
    }
    /** Collection based on a generic collection */
    protected DataFeatureCollection(String id) {
        this(id, null);
    }

    /** Subclass must think about what consitructors it needs. */
    protected DataFeatureCollection(String id, SimpleFeatureType memberType) {
        this.id = id == null ? "featureCollection" : id;
        this.schema = memberType;
    }

    /** To let listeners know that something has changed. */
    protected void fireChange(SimpleFeature[] features, int type) {
        CollectionEvent cEvent = new CollectionEvent(this, features, type);

        for (CollectionListener listener : listeners) {
            listener.collectionChanged(cEvent);
        }
    }

    protected void fireChange(SimpleFeature feature, int type) {
        fireChange(new SimpleFeature[] {feature}, type);
    }

    protected void fireChange(Collection coll, int type) {
        @SuppressWarnings("unchecked")
        Collection<SimpleFeature> fcoll = coll;
        SimpleFeature[] features = fcoll.toArray(SimpleFeature[]::new);
        fireChange(features, type);
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        return new DelegateFeatureReader<>(getSchema(), features());
    }

    //
    // Feature Results methods
    //
    // To be implemented by subclass
    //

    @Override
    public abstract ReferencedEnvelope getBounds();

    public abstract int getCount() throws IOException;
    // public abstract SimpleFeatureCollection collection() throws IOException;

    //
    // Additional Subclass "hooks"
    //
    /**
     * Subclass may provide an implementation of this method to indicate that read/write support is provided.
     *
     * <p>All operations that attempt to modify the "data" will use this method, allowing them to throw an
     * "UnsupportedOperationException" in the same manner as Collections.unmodifiableCollection(Collection c), or just
     * return null.
     *
     * @throws UnsupportedOperationException To indicate that write support is not avaiable
     * @return the writer, or null if write support is not available
     */
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> writer() throws IOException {
        return null;
    }
    //
    // SimpleFeatureCollection methods
    //
    // implemented in terms of feature results
    //

    //
    // Content Access
    //
    /** Set of open resource iterators & featureIterators (no common super-class or interface) */
    private final Set<Object> open = new HashSet<>();

    /** listeners */
    protected List<CollectionListener> listeners = new ArrayList<>();

    /** id used when serialized to gml */
    protected String id;

    protected SimpleFeatureType schema;
    /**
     * SimpleFeatureIterator is entirely based on iterator().
     *
     * <p>So when we implement FeatureCollection.iterator() this will work out of the box.
     */
    @Override
    public SimpleFeatureIterator features() {
        SimpleFeatureIterator iterator = new DelegateSimpleFeatureIterator(iterator());
        open.add(iterator);
        return iterator;
    }

    /** Iterator may (or may) not support modification. */
    public final Iterator<SimpleFeature> iterator() {
        Iterator<SimpleFeature> iterator;
        try {
            iterator = openIterator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        open.add(iterator);
        return iterator;
    }

    /**
     * Returns a FeatureWriterIterator, or FeatureReaderIterator over content.
     *
     * <p>If you have a way to tell that you are readonly please subclass with a less hardcore check - this
     * implementations catches a UnsupportedOpperationsException from wrtier()!
     *
     * @return Iterator, should be closed closeIterator
     */
    @SuppressWarnings("PMD.CloseResource")
    protected Iterator<SimpleFeature> openIterator() throws IOException {
        try {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer();
            if (writer != null) {
                return new FeatureWriterIterator(writer());
            }
        } catch (IOException badWriter) {
            return new NoContentIterator(badWriter);
        } catch (UnsupportedOperationException readOnly) {
        }

        try {
            return new FeatureReaderIterator<>(reader());
        } catch (IOException e) {
            return new NoContentIterator(e);
        }
    }

    public final void close(Iterator<SimpleFeature> close) {
        try {
            closeIterator(close);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error closing iterator", e);
        }
        open.remove(close);
    }

    protected void closeIterator(Iterator<SimpleFeature> close) throws IOException {
        if (close instanceof FeatureReaderIterator) {
            @SuppressWarnings("PMD.CloseResource")
            FeatureReaderIterator<SimpleFeature> iterator = (FeatureReaderIterator<SimpleFeature>) close;
            iterator.close(); // only needs package visability
        } else if (close instanceof FeatureWriterIterator) {
            FeatureWriterIterator iterator = (FeatureWriterIterator) close;
            iterator.close(); // only needs package visability
        }
    }

    public void close(FeatureIterator<SimpleFeature> iterator) {
        iterator.close();
        open.remove(iterator);
    }

    /** Default implementation based on getCount() - this may be expensive */
    @Override
    public int size() {
        try {
            return getCount();
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "IOException while calculating size() of FeatureCollection", e);
            return 0;
        }
    }

    public void purge() {
        for (Iterator<Object> i = open.iterator(); i.hasNext(); ) {
            Object iterator = i.next();
            try {
                if (iterator instanceof Iterator) {
                    @SuppressWarnings("unchecked")
                    Iterator<SimpleFeature> cast = (Iterator<SimpleFeature>) iterator;
                    closeIterator(cast);
                }
                if (iterator instanceof FeatureIterator) {
                    ((SimpleFeatureIterator) iterator).close();
                }
            } catch (Throwable e) {
                // TODO: Log e = ln
            } finally {
                i.remove();
            }
        }
    }
    //
    // Off into implementation land!
    //
    /**
     * Default implementation based on creating an reader, testing hasNext, and closing.
     *
     * <p>For once the Collections API does not give us an escape route, we *have* to check the data.
     */
    @Override
    public boolean isEmpty() {
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader()) {
            try {
                return !reader.hasNext();
            } catch (IOException e) {
                return true; // error seems like no features are available
            }
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof SimpleFeature)) return false;
        SimpleFeature value = (SimpleFeature) o;
        String ID = value.getID();

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader()) {
            try {
                while (reader.hasNext()) {
                    SimpleFeature feature = reader.next();
                    if (!ID.equals(feature.getID())) {
                        continue; // skip with out full equal check
                    }
                    if (value.equals(feature)) return true;
                }
                return false; // not found
            } catch (IOException | IllegalAttributeException | NoSuchElementException e) {
                return false; // error seems like no features are available
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Object[] toArray() {
        return toArray(new SimpleFeature[size()]);
    }

    @Override
    public <T> T[] toArray(T[] array) {
        List<T> list = new ArrayList<>();
        Iterator<SimpleFeature> i = iterator();
        try {
            while (i.hasNext()) {
                @SuppressWarnings("unchecked")
                T next = (T) i.next();
                list.add(next);
            }
        } finally {
            close(i);
        }
        return list.toArray(array);
    }

    public boolean add(SimpleFeature arg0) {
        return false;
    }

    public boolean remove(Object arg0) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) {
            if (contains(o) == false) return false;
        }
        return true;
    }

    /**
     * Optimized implementation of addAll that recognizes the use of collections obtained with subCollection( filter ).
     *
     * <p>This method is constructed by either:
     *
     * <ul>
     *   <li>Filter OR
     *   <li>Removing an extact match of Filter AND
     * </ul>
     */
    public boolean addAll(Collection collection) {
        if (collection instanceof FeatureCollection) {
            return addAll((FeatureCollection<?, ?>) collection);
        }
        try (FeatureWriter writer = writer()) {
            if (writer == null) {
                return false;
            }
            // skip to end
            while (writer.hasNext()) {
                writer.next();
            }
            for (Object obj : collection) {
                if (obj instanceof SimpleFeature) {
                    SimpleFeature copy = (SimpleFeature) obj;
                    SimpleFeature feature = (SimpleFeature) writer.next();

                    feature.setAttributes(copy.getAttributes());
                    writer.write();
                }
            }
            return true;
        } catch (IOException ignore) {
            return false;
        }
    }

    public boolean addAll(FeatureCollection resource) {
        return false;
    }

    public boolean removeAll(Collection arg0) {
        return false;
    }

    public boolean retainAll(Collection arg0) {
        return false;
    }

    public void clear() {}

    @Override
    public void accepts(
            org.geotools.api.feature.FeatureVisitor visitor, org.geotools.api.util.ProgressListener progress)
            throws IOException {
        DataUtilities.visit(this, visitor, progress);
    }

    /**
     * Will return an optimized subCollection based on access to the origional FeatureSource.
     *
     * <p>The subCollection is constructed by using an AND Filter. For the converse of this opperation please see
     * collection.addAll( Collection ), it has been optimized to be aware of these filter based SubCollections.
     *
     * <p>This method is intended in a manner similar to subList, example use: <code>
     * collection.subCollection( myFilter ).clear()
     * </code>
     *
     * @param filter Filter used to determine sub collection.
     * @since GeoTools 2.2, Filter 1.1
     */
    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        if (filter == Filter.INCLUDE) {
            return this;
        }
        return new SubFeatureCollection(this, filter);
    }

    /**
     * Construct a sorted view of this content.
     *
     * <p>Sorts may be combined togther in a stable fashion, in congruence with the Filter 1.1 specification. This
     * method should also be able to handle GeoTools specific sorting through detecting order as a SortBy2 instance.
     *
     * @since GeoTools 2.2, Filter 1.1
     * @return FeatureList sorted according to provided order
     */
    @Override
    public SimpleFeatureCollection sort(SortBy order) {
        return null; // new OrderedFeatureList( this, order );
    }

    @Override
    public String getID() {
        return id;
    }

    public final void addListener(CollectionListener listener) throws NullPointerException {
        listeners.add(listener);
    }

    public final void removeListener(CollectionListener listener) throws NullPointerException {
        listeners.remove(listener);
    }

    @Override
    public SimpleFeatureType getSchema() {
        return schema;
    }
}
