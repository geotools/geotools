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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;

/**
 * This class simply builds an SRTREE spatial index in memory for fast indexed geometric queries.
 *
 * <p>Since the {@link ImageMosaicReader} heavily uses spatial queries to find out which are the involved tiles during
 * mosaic creation, it is better to do some caching and keep the index in memory as much as possible, hence we came up
 * with this index.
 *
 * @author Simone Giannecchini, S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * @since 2.5
 */
public class CachingDataStoreGranuleCatalog extends DelegatingGranuleCatalog {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(CachingDataStoreGranuleCatalog.class);

    private final SoftValueHashMap<String, GranuleDescriptor> descriptorsCache = new SoftValueHashMap<>();

    /** */
    public CachingDataStoreGranuleCatalog(GranuleCatalog adaptee) {
        super(adaptee);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        return getGranules(q, Transaction.AUTO_COMMIT);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q, Transaction t) throws IOException {
        boolean decorateWithBounds = Boolean.TRUE.equals(q.getHints().get(GranuleSource.NATIVE_BOUNDS));
        if (decorateWithBounds) {
            // delegate down a version that won't decorate with bounds, we want to use the local
            // granule descriptor cache
            Query copy = new Query(q);
            copy.getHints().remove(GranuleSource.NATIVE_BOUNDS);
            q = copy;
            SimpleFeatureCollection granules = adaptee.getGranules(q, t);

            CatalogConfigurationBean configuration = adaptee.getConfigurations().getByTypeQuery(q);
            return new BoundsFeatureCollection(granules, sf -> getGranuleDescriptor(configuration, sf));
        } else {
            return adaptee.getGranules(q, t);
        }
    }

    @Override
    public void getGranuleDescriptors(final Query q, final GranuleCatalogVisitor visitor) throws IOException {

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
        final Geometry intersectionGeometry = requestedBBox != null ? JTS.toGeometry(requestedBBox) : null;

        // visiting the features from the underlying store
        CatalogConfigurationBean configuration = adaptee.getConfigurations().getByTypeQuery(q);
        try (SimpleFeatureIterator fi = features.features()) {
            Object executor = q.getHints().get(Hints.EXECUTOR_SERVICE);
            if (executor instanceof ExecutorService service) {
                parallelGranuleVisit(configuration, visitor, intersectionGeometry, fi, service);
            } else {
                sequentialGranuleVisit(configuration, visitor, intersectionGeometry, fi);
            }
        }
    }

    private void parallelGranuleVisit(
            CatalogConfigurationBean configuration,
            GranuleCatalogVisitor visitor,
            Geometry intersectionGeometry,
            SimpleFeatureIterator fi,
            ExecutorService executor) {
        // first submit all visitors, in order, allowing them to load granules in parallel
        List<Future<GranuleDescriptor>> futures = new ArrayList<>();
        try {
            while (fi.hasNext() && !visitor.isVisitComplete()) {
                final SimpleFeature sf = fi.next();
                futures.add(executor.submit(() -> getGranuleDescriptor(configuration, sf)));
            }
        } catch (RejectedExecutionException e) {
            int submitted = futures.size();
            int leftover = 1;
            while (fi.hasNext()) {
                fi.next();
                leftover++;
            }
            throw new RuntimeException(
                    "We were not allowed to fetch all mosaic granules, submitted "
                            + Integer.toString(submitted)
                            + " while still having "
                            + Integer.toString(leftover)
                            + " left",
                    e);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Parallel granule visit using executor " + executor.toString());
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
            CatalogConfigurationBean configuration,
            GranuleCatalogVisitor visitor,
            Geometry intersectionGeometry,
            SimpleFeatureIterator fi) {
        while (fi.hasNext() && !visitor.isVisitComplete()) {
            final SimpleFeature sf = fi.next();

            GranuleDescriptor granule = getGranuleDescriptor(configuration, sf);
            visitGranule(visitor, intersectionGeometry, granule);
        }
    }

    private void visitGranule(GranuleCatalogVisitor visitor, Geometry intersectionGeometry, GranuleDescriptor granule) {
        if (granule != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Visiting granule " + granule.getGranuleUrl().toString());
            }
            // check ROI inclusion
            final Geometry footprint = granule.getFootprint();
            if (intersectionGeometry == null || footprint == null || polygonOverlap(footprint, intersectionGeometry)) {
                visitor.visit(granule, granule.getOriginator());
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Skipping granule " + granule + "\n since its ROI does not intersect the requested area");
                }
            }
        }
    }

    protected GranuleDescriptor getGranuleDescriptor(CatalogConfigurationBean configuration, SimpleFeature sf) {
        // caching by combination of feature id and coverage name
        String featureId = sf.getID();
        String key = configuration.getName() + "/" + featureId;

        GranuleDescriptor granule = null;
        if (descriptorsCache.containsKey(key)) {
            granule = descriptorsCache.get(key);
        } else {
            try {
                // create the granule descriptor
                MultiLevelROI footprint = getGranuleFootprint(sf);
                if (footprint == null || !footprint.isEmpty()) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Creating new Granule Descriptor for feature Id: " + key);
                    }
                    // caching only if the footprint is either absent or present and
                    // NON-empty
                    granule = new GranuleDescriptor(
                            sf,
                            configuration.suggestedFormat(),
                            configuration.suggestedSPI(),
                            configuration.suggestedIsSPI(),
                            configuration.getPathType(),
                            configuration.getLocationAttribute(),
                            adaptee.getParentLocation(),
                            footprint,
                            configuration.isHeterogeneous(),
                            adaptee.getHints());
                    descriptorsCache.put(key, granule);
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
}
