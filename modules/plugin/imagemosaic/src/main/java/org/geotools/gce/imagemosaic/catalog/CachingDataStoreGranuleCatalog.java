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
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
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
class CachingDataStoreGranuleCatalog extends GranuleCatalog {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(CachingDataStoreGranuleCatalog.class);

    private final AbstractGTDataStoreGranuleCatalog adaptee;

    private final SoftValueHashMap<String, GranuleDescriptor> descriptorsCache =
            new SoftValueHashMap<String, GranuleDescriptor>();

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
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        return adaptee.getGranules(q);
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
            while (fi.hasNext() && !visitor.isVisitComplete()) {
                final SimpleFeature sf = fi.next();

                GranuleDescriptor granule = null;

                // caching by granule's location
                // synchronized (descriptorsCache) {
                String featureId = sf.getID();
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

                if (granule != null) {
                    // check ROI inclusion
                    final Geometry footprint = granule.getFootprint();
                    if (intersectionGeometry == null
                            || footprint == null
                            || polygonOverlap(footprint, intersectionGeometry)) {
                        visitor.visit(granule, sf);
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
        }
    }

    private boolean polygonOverlap(Geometry g1, Geometry g2) {
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
    public int removeGranules(Query query) {
        final int val = adaptee.removeGranules(query);
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
