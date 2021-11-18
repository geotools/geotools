/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
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
 */
public class CachingDataStoreGranuleCatalog extends GranuleCatalog {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(CachingDataStoreGranuleCatalog.class);

    private final AbstractGTDataStoreGranuleCatalog adaptee;

    private final SoftValueHashMap<String, GranuleDescriptor> descriptorsCache =
            new SoftValueHashMap<>();

    /** */
    public CachingDataStoreGranuleCatalog(AbstractGTDataStoreGranuleCatalog adaptee) {
        super(null);
        this.adaptee = adaptee;
    }

    @Override
    public void addGranules(
            String typeName, Collection<SimpleFeature> granules, Transaction transaction)
            throws IOException {
        adaptee.addGranules(typeName, granules, transaction);
    }

    @Override
    public void computeAggregateFunction(Query q, FeatureCalc function) throws IOException {
        adaptee.computeAggregateFunction(q, function);
    }

    @Override
    public void createType(String namespace, String typeName, String typeSpec)
            throws IOException, SchemaException {
        adaptee.createType(namespace, typeName, typeSpec);
    }

    @Override
    public void createType(SimpleFeatureType featureType) throws IOException {
        adaptee.createType(featureType);
    }

    @Override
    public void createType(String identification, String typeSpec)
            throws SchemaException, IOException {
        adaptee.createType(identification, typeSpec);
    }

    @Override
    public void dispose() {
        adaptee.dispose();
        if (multiScaleROIProvider != null) {
            multiScaleROIProvider.dispose();
            multiScaleROIProvider = null;
        }
    }

    @Override
    public BoundingBox getBounds(String typeName) {
        return adaptee.getBounds(typeName);
    }

    @Override
    public BoundingBox getBounds(String typeName, Transaction t) {
        return adaptee.getBounds(typeName, t);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        return getGranules(q, Transaction.AUTO_COMMIT);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q, Transaction t) throws IOException {
        boolean decorateWithBounds =
                Boolean.TRUE.equals(q.getHints().get(GranuleSource.NATIVE_BOUNDS));
        if (decorateWithBounds) {
            // delegate down a version that won't decorate with bounds, we want to use the local
            // granule descriptor cache
            Query copy = new Query(q);
            copy.getHints().remove(GranuleSource.NATIVE_BOUNDS);
            q = copy;
            SimpleFeatureCollection granules = adaptee.getGranules(q, t);
            return new BoundsFeatureCollection(granules, this::getGranuleDescriptor);
        } else {
            return adaptee.getGranules(q, t);
        }
    }

    @Override
    public int getGranulesCount(Query q) throws IOException {
        return adaptee.getGranulesCount(q);
    }

    @Override
    public void getGranuleDescriptors(final Query q, final GranuleCatalogVisitor visitor)
            throws IOException {

        final SimpleFeatureCollection features = adaptee.getGranules(q);
        if (features == null) {
            throw new NullPointerException(
                    "The provided SimpleFeatureCollection is null, it's impossible to create an index!");
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Index Loaded");
        }

        // ROI
        final Utils.BBOXFilterExtractor bboxExtractor = new Utils.BBOXFilterExtractor();
        q.getFilter().accept(bboxExtractor, null);
        ReferencedEnvelope requestedBBox = bboxExtractor.getBBox();
        final Geometry intersectionGeometry =
                requestedBBox != null ? JTS.toGeometry(requestedBBox) : null;

        // visiting the features from the underlying store
        try (SimpleFeatureIterator fi = features.features()) {
            Object executor = q.getHints().get(Hints.EXECUTOR_SERVICE);
            if (executor instanceof ExecutorService) {
                parallelGranuleVisit(visitor, intersectionGeometry, fi, (ExecutorService) executor);
            } else {
                sequentialGranuleVisit(visitor, intersectionGeometry, fi);
            }
        }
    }

    private void parallelGranuleVisit(
            GranuleCatalogVisitor visitor,
            Geometry intersectionGeometry,
            SimpleFeatureIterator fi,
            ExecutorService executor) {
        // first submit all visitors, in order, allowing them to load granules in parallel
        List<Future<GranuleDescriptor>> futures = new ArrayList<>();
        while (fi.hasNext() && !visitor.isVisitComplete()) {
            final SimpleFeature sf = fi.next();
            futures.add(executor.submit(() -> getGranuleDescriptor(sf)));
        }

        // then grab them one by one, in the same order as the features, to preserve the query sort
        for (Future<GranuleDescriptor> future : futures) {
            try {
                GranuleDescriptor granule = future.get();
                visitGranule(visitor, intersectionGeometry, granule);
            } catch (ExecutionException | InterruptedException e) {
                // the sequential one does not catch exceptions either
                throw new RuntimeException("Unexpected exception occurred loading granules", e);
            }
        }
    }

    private void sequentialGranuleVisit(
            GranuleCatalogVisitor visitor,
            Geometry intersectionGeometry,
            SimpleFeatureIterator fi) {
        while (fi.hasNext() && !visitor.isVisitComplete()) {
            final SimpleFeature sf = fi.next();

            GranuleDescriptor granule = getGranuleDescriptor(sf);
            visitGranule(visitor, intersectionGeometry, granule);
        }
    }

    private void visitGranule(
            GranuleCatalogVisitor visitor,
            Geometry intersectionGeometry,
            GranuleDescriptor granule) {
        if (granule != null) {
            // check ROI inclusion
            final Geometry footprint = granule.getFootprint();
            if (intersectionGeometry == null
                    || footprint == null
                    || polygonOverlap(footprint, intersectionGeometry)) {
                visitor.visit(granule, granule.getOriginator());
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Skipping granule "
                                    + granule
                                    + "\n since its ROI does not intersect the requested area");
                }
            }
        }
    }

    protected GranuleDescriptor getGranuleDescriptor(SimpleFeature sf) {
        String featureId = sf.getID();
        GranuleDescriptor granule = null;
        // caching by granule's location
        if (descriptorsCache.containsKey(featureId)) {
            granule = descriptorsCache.get(featureId);
        } else {
            try {
                // create the granule descriptor
                MultiLevelROI footprint = getGranuleFootprint(sf);
                if (footprint == null || !footprint.isEmpty()) {
                    // caching only if the footprint is either absent or present and
                    // NON-empty
                    granule =
                            new GranuleDescriptor(
                                    sf,
                                    adaptee.suggestedFormat,
                                    adaptee.suggestedRasterSPI,
                                    adaptee.suggestedIsSPI,
                                    adaptee.pathType,
                                    adaptee.locationAttribute,
                                    adaptee.parentLocation,
                                    footprint,
                                    adaptee.heterogeneous,
                                    adaptee.hints); // retain hints since this may contain a
                    // reader or anything
                    descriptorsCache.put(featureId, granule);
                }
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Skipping invalid granule", e);
            }
        }
        return granule;
    }

    protected boolean polygonOverlap(Geometry g1, Geometry g2) {
        // TODO: try to use relate instead
        Geometry intersection = g1.intersection(g2);
        return intersection != null && intersection.getDimension() == 2;
    }

    @Override
    public QueryCapabilities getQueryCapabilities(String typeName) {
        return adaptee.getQueryCapabilities(typeName);
    }

    @Override
    public SimpleFeatureType getType(String typeName) throws IOException {
        return adaptee.getType(typeName);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int removeGranules(Query query) {
        return this.removeGranules(query, Transaction.AUTO_COMMIT);
    }

    @Override
    public int removeGranules(Query query, Transaction transaction) {
        final int val = adaptee.removeGranules(query, transaction);
        // clear cache if needed
        // TODO this can be optimized further filtering out elements using the Query's Filter
        if (val >= 1) {
            descriptorsCache.clear();
        }

        return val;
    }

    @Override
    public String[] getTypeNames() {
        return adaptee.getTypeNames();
    }

    /** @return the adaptee */
    public GranuleCatalog getAdaptee() {
        return adaptee;
    }

    @Override
    public void removeType(String typeName) throws IOException {
        adaptee.removeType(typeName);
    }

    @Override
    public void drop() throws IOException {
        adaptee.drop();
    }
}
