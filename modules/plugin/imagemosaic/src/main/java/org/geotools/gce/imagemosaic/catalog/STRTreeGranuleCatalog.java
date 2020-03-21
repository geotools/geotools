/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.sort.SortedFeatureReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.ItemVisitor;
import org.locationtech.jts.index.strtree.STRtree;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.geometry.BoundingBox;

/**
 * This class simply builds an SRTREE spatial index in memory for fast indexed geometric queries.
 *
 * <p>Since the {@link ImageMosaicReader} heavily uses spatial queries to find out which are the
 * involved tiles during mosaic creation, it is better to do some caching and keep the index in
 * memory as much as possible, hence we came up with this index.
 *
 * @author Simone Giannecchini, S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for
 *     jar:file:foo.jar/bar.properties URLs
 * @since 2.5
 * @version 10.0
 */
@SuppressWarnings("unused")
class STRTreeGranuleCatalog extends GranuleCatalog {

    /** Logger. */
    static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(STRTreeGranuleCatalog.class);

    private static class JTSIndexVisitorAdapter implements ItemVisitor {

        private GranuleCatalogVisitor adaptee;

        private Filter filter;

        private int maxGranules = -1;

        private int granuleIndex = 0;

        public JTSIndexVisitorAdapter(final GranuleCatalogVisitor adaptee) {
            this(adaptee, (Query) null);
        }

        public JTSIndexVisitorAdapter(final GranuleCatalogVisitor adaptee, Query q) {
            this.adaptee = adaptee;
            this.filter = q == null ? Query.ALL.getFilter() : q.getFilter();
            this.maxGranules = q == null ? -1 : q.getMaxFeatures();
        }

        public JTSIndexVisitorAdapter(final GranuleCatalogVisitor adaptee, Filter filter) {
            this.adaptee = adaptee;
            this.filter = filter == null ? Query.ALL.getFilter() : filter;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.locationtech.jts.index.ItemVisitor#visitItem(java.lang.Object)
         */
        public void visitItem(Object o) {
            if (maxGranules > 0 && granuleIndex > maxGranules) {
                return; // Skip
            }
            if (adaptee.isVisitComplete()) {
                return; // Skipt
            }
            if (o instanceof GranuleDescriptor) {
                final GranuleDescriptor g = (GranuleDescriptor) o;
                final SimpleFeature originator = g.getOriginator();
                if (originator != null && filter.evaluate(originator)) {
                    adaptee.visit(g, null);
                    granuleIndex++;
                }
                return;
            }
            throw new IllegalArgumentException("Unable to visit provided item" + o);
        }
    }

    private GranuleCatalog wrappedCatalogue;

    private String typeName;

    public STRTreeGranuleCatalog(
            final Properties params,
            AbstractGTDataStoreGranuleCatalog wrappedCatalogue,
            final Hints hints) {
        super(hints);
        Utilities.ensureNonNull("params", params);
        this.wrappedCatalogue = wrappedCatalogue;
        this.typeName = (String) params.get("TypeName");
        if (typeName == null) {
            wrappedCatalogue.getValidTypeNames().iterator().next();
        }
    }

    /** The {@link STRtree} index. */
    private STRtree index;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    /** Constructs a {@link STRTreeGranuleCatalog} out of a {@link FeatureCollection}. */
    @SuppressFBWarnings("UL_UNRELEASED_LOCK")
    private void checkIndex(Lock readLock) throws IOException {
        final Lock writeLock = rwLock.writeLock();
        try {
            // upgrade the read lock to write lock
            readLock.unlock();
            writeLock.lock();

            // do your thing
            if (index == null) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("No index exits and we create a new one.");
                createIndex();
            } else if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Index does not need to be created...");

        } finally {
            try {
                // get read lock again
                readLock.lock();
            } finally {
                // leave write lock
                writeLock.unlock();
            }
        }
    }

    /**
     * This method shall only be called when the <code>indexLocation</code> is of protocol <code>
     * file:</code>
     */
    private void createIndex() {

        Iterator<GranuleDescriptor> it = null;
        final Collection<GranuleDescriptor> features = new ArrayList<GranuleDescriptor>();
        //
        // Load tiles informations, especially the bounds, which will be
        // reused
        //
        try {

            wrappedCatalogue.getGranuleDescriptors(
                    new Query(typeName),
                    new GranuleCatalogVisitor() {

                        @Override
                        public void visit(GranuleDescriptor granule, SimpleFeature o) {
                            features.add(granule);
                        }
                    });
            if (features == null)
                throw new NullPointerException(
                        "The provided SimpleFeatureCollection is null, it's impossible to create an index!");

            if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Index Loaded");

            // load the feature from the shapefile and create JTS index
            it = features.iterator();

            // now build the index
            // TODO make it configurable as far the index is involved
            STRtree tree = new STRtree();
            long size = 0;
            while (it.hasNext()) {
                final GranuleDescriptor granule = it.next();
                final ReferencedEnvelope env =
                        ReferencedEnvelope.reference(granule.getGranuleBBOX());
                final Geometry g =
                        FeatureUtilities.getPolygon(
                                new Rectangle2D.Double(
                                        env.getMinX(),
                                        env.getMinY(),
                                        env.getWidth(),
                                        env.getHeight()),
                                0);
                tree.insert(g.getEnvelopeInternal(), granule);
            }

            // force index construction --> STRTrees are built on first call to
            // query
            tree.build();

            // save the soft reference
            index = tree;
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.FeatureIndex#findFeatures(org.locationtech.jts.geom.Envelope)
     */
    @SuppressWarnings("unchecked")
    public List<GranuleDescriptor> getGranules(final BoundingBox envelope) throws IOException {
        Utilities.ensureNonNull("envelope", envelope);
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            checkIndex(lock);
            return index.query(ReferencedEnvelope.reference(envelope));
        } finally {
            lock.unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.gce.imagemosaic.FeatureIndex#findFeatures(org.locationtech.jts.geom.Envelope, org.locationtech.jts.index.ItemVisitor)
     */
    public void getGranules(final BoundingBox envelope, final GranuleCatalogVisitor visitor)
            throws IOException {
        Utilities.ensureNonNull("envelope", envelope);
        Utilities.ensureNonNull("visitor", visitor);
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            checkIndex(lock);

            index.query(
                    ReferencedEnvelope.reference(envelope), new JTSIndexVisitorAdapter(visitor));
        } finally {
            lock.unlock();
        }
    }

    public void dispose() {
        final Lock l = rwLock.writeLock();
        try {
            l.lock();

            // original index
            if (wrappedCatalogue != null) {
                try {
                    wrappedCatalogue.dispose();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            }
            if (multiScaleROIProvider != null) {
                multiScaleROIProvider.dispose();
            }
        } finally {
            index = null;
            multiScaleROIProvider = null;
            l.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        q = mergeHints(q);
        Utilities.ensureNonNull("q", q);
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            // get filter and check bbox
            final Filter filter = q.getFilter();
            // try to combine the index bbox with the one that may come from the query.
            ReferencedEnvelope requestedBBox = extractAndCombineBBox(filter);

            // load what we need to load
            checkIndex(lock);
            final List<GranuleDescriptor> features = index.query(requestedBBox);
            List<SimpleFeature> filtered = new ArrayList<>();
            final int maxGranules = q.getMaxFeatures();
            int numGranules = 0;
            for (GranuleDescriptor g : features) {
                // check how many tiles we are returning
                if (q.getSortBy() == null && maxGranules > 0 && numGranules >= maxGranules) break;
                final SimpleFeature originator = g.getOriginator();
                if (originator != null && filter.evaluate(originator)) filtered.add(originator);
            }
            if (q.getSortBy() != null) {
                Comparator<SimpleFeature> comparator =
                        SortedFeatureReader.getComparator(q.getSortBy());
                if (comparator != null) {
                    Collections.sort(filtered, comparator);
                }
                if (maxGranules > 0 && filtered.size() > maxGranules) {
                    filtered = filtered.subList(0, maxGranules);
                }
            }
            return new ListFeatureCollection(wrappedCatalogue.getType(typeName), filtered);
        } finally {
            lock.unlock();
        }
    }

    private ReferencedEnvelope extractAndCombineBBox(Filter filter) {
        final Utils.BBOXFilterExtractor bboxExtractor = new Utils.BBOXFilterExtractor();
        filter.accept(bboxExtractor, null);
        BoundingBox bbox = wrappedCatalogue.getBounds(typeName);
        return ReferencedEnvelope.reference(bbox);
    }

    public List<GranuleDescriptor> getGranules() throws IOException {
        return getGranules(this.getBounds(typeName));
    }

    public void getGranuleDescriptors(Query q, GranuleCatalogVisitor visitor) throws IOException {
        Utilities.ensureNonNull("q", q);
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            // get filter and check bbox
            final Filter filter = q.getFilter();
            ReferencedEnvelope requestedBBox = extractAndCombineBBox(filter);

            // get filter and check bbox
            checkIndex(lock);
            Comparator<SimpleFeature> comparator =
                    q.getSortBy() == null ? null : SortedFeatureReader.getComparator(q.getSortBy());
            if (comparator == null) {
                index.query(requestedBBox, new JTSIndexVisitorAdapter(visitor, q));
            } else {
                final List<GranuleDescriptor> unfilteredGranules = index.query(requestedBBox);
                List<GranuleDescriptor> granules =
                        unfilteredGranules
                                .stream()
                                .filter(gd -> filter.evaluate(gd.getOriginator()))
                                .collect(Collectors.toList());

                Comparator<GranuleDescriptor> granuleComparator =
                        (gd1, gd2) -> {
                            SimpleFeature sf1 = gd1.getOriginator();
                            SimpleFeature sf2 = gd2.getOriginator();
                            return comparator.compare(sf1, sf2);
                        };
                Collections.sort(granules, granuleComparator);
                int maxGranules = q.getMaxFeatures();
                if (maxGranules > 0 && granules.size() > maxGranules) {
                    granules = granules.subList(0, maxGranules);
                }
                for (GranuleDescriptor gd : granules) {
                    if (visitor.isVisitComplete()) {
                        break;
                    }
                    visitor.visit(gd, gd.getOriginator());
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public BoundingBox getBounds(String typeName) {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            return wrappedCatalogue.getBounds(typeName);

        } finally {
            lock.unlock();
        }
    }

    /** @throws IllegalStateException */
    private void checkStore() throws IllegalStateException {
        if (wrappedCatalogue == null)
            throw new IllegalStateException("The underlying store has already been disposed!");
    }

    @Override
    public SimpleFeatureType getType(final String typeName) throws IOException {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            return this.wrappedCatalogue.getType(typeName);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String[] getTypeNames() {
        return typeName != null ? new String[] {typeName} : null;
    }

    public void computeAggregateFunction(Query query, FeatureCalc function) throws IOException {
        query = mergeHints(query);
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            wrappedCatalogue.computeAggregateFunction(query, function);
        } finally {
            lock.unlock();
        }
    }

    public QueryCapabilities getQueryCapabilities() {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();

            return wrappedCatalogue.getQueryCapabilities(typeName);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getGranulesCount(Query q) throws IOException {
        return wrappedCatalogue.getGranulesCount(mergeHints(q));
    }

    @Override
    public void addGranule(String typeName, SimpleFeature granule, Transaction transaction)
            throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void addGranules(
            String typeName, Collection<SimpleFeature> granules, Transaction transaction)
            throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void createType(String namespace, String typeName, String typeSpec)
            throws IOException, SchemaException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void createType(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void createType(String identification, String typeSpec)
            throws SchemaException, IOException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public QueryCapabilities getQueryCapabilities(String typeName) {
        if (this.typeName.equals(typeName)) {
            return getQueryCapabilities();
        }
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public int removeGranules(Query query) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void removeType(String typeName) throws IOException {
        final Lock lock = rwLock.readLock();
        try {
            lock.lock();
            checkStore();
            this.wrappedCatalogue.removeType(typeName);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void drop() throws IOException {
        final Lock lock = rwLock.writeLock();
        try {
            lock.lock();
            checkStore();
            this.wrappedCatalogue.drop();
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            lock.unlock();
        }
    }
}
