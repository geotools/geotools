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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.util.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;

public abstract class GranuleCatalog {

    protected final Hints hints;

    protected MultiLevelROIProvider multiScaleROIProvider;

    /** @param hints */
    public GranuleCatalog(Hints hints) {
        this.hints = hints;
    }

    public void addGranule(
            final String typeName, final SimpleFeature granule, final Transaction transaction)
            throws IOException {
        addGranules(typeName, Collections.singleton(granule), transaction);
    }

    public abstract void addGranules(
            final String typeName, Collection<SimpleFeature> granules, Transaction transaction)
            throws IOException;

    public abstract void computeAggregateFunction(Query q, FeatureCalc function) throws IOException;

    public abstract void createType(String namespace, String typeName, String typeSpec)
            throws IOException, SchemaException;

    public abstract void createType(SimpleFeatureType featureType) throws IOException;

    public abstract void createType(String identification, String typeSpec)
            throws SchemaException, IOException;

    public abstract void dispose();

    public abstract BoundingBox getBounds(final String typeName);

    public abstract SimpleFeatureCollection getGranules(Query q) throws IOException;

    public abstract int getGranulesCount(Query q) throws IOException;

    public abstract void getGranuleDescriptors(Query q, GranuleCatalogVisitor visitor)
            throws IOException;

    public abstract QueryCapabilities getQueryCapabilities(final String typeName);

    public abstract SimpleFeatureType getType(final String typeName) throws IOException;

    public abstract void removeType(final String typeName) throws IOException;

    public abstract int removeGranules(Query query);

    public abstract String[] getTypeNames();

    /** Merges the wrapper hints with the query ones, making sure not to overwrite the query ones */
    protected Query mergeHints(Query q) {
        if (this.hints == null || this.hints.isEmpty()) {
            return q;
        }
        Query clone = new Query(q);
        Hints hints = clone.getHints();
        if (hints == null || hints.isEmpty()) {
            clone.setHints(this.hints);
        } else {
            for (Entry<Object, Object> entry : this.hints.entrySet()) {
                if (!hints.containsKey(entry.getKey())) {
                    hints.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return clone;
    }

    public void setMultiScaleROIProvider(MultiLevelROIProvider footprintProvider) {
        this.multiScaleROIProvider = footprintProvider;
    }

    /**
     * Returns the footprint for the given granule. Mind, when applying insets we might have the
     * case of the geometry being empty (negative buffer eroded it fully), in that case the granule
     * must not be loaded
     */
    protected MultiLevelROI getGranuleFootprint(SimpleFeature sf) {
        if (multiScaleROIProvider != null) {
            try {
                MultiLevelROI roi = multiScaleROIProvider.getMultiScaleROI(sf);
                return roi;
            } catch (IOException e) {
                throw new RuntimeException("Failed to load the footprint for granule: " + sf, e);
            }
        }
        return null;
    }

    /** Returns the list of footprint files for the given granule */
    public List<File> getFootprintFiles(SimpleFeature sf) throws IOException {
        if (multiScaleROIProvider != null) {
            return multiScaleROIProvider.getFootprintFiles(sf);
        }
        return Collections.emptyList();
    }

    /**
     * Drop the underlying catalog, all the individual granule indexes.
     *
     * <p>This is usuallu done when deleting an ImageMosaic storre.
     *
     * @throws IOException in case something bad happens
     */
    public abstract void drop() throws IOException;
}
