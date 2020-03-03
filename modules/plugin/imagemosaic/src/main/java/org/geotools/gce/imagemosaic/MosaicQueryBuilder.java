/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.data.Query;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/** Helper class building the mosaic query given the request and a reference bbox */
class MosaicQueryBuilder {

    static final Logger LOGGER = Logging.getLogger(MosaicQueryBuilder.class);

    RasterLayerRequest request;
    RasterManager rasterManager;
    ReferencedEnvelope queryBBox;

    public MosaicQueryBuilder(RasterLayerRequest request, ReferencedEnvelope bbox) {
        this.request = request;
        this.rasterManager = request.getRasterManager();
        this.queryBBox = bbox;
    }

    public Query build() throws TransformException, IOException, FactoryException {
        Query query = initQuery();
        handleAdditionalFilters(query);
        handleSortByClause(query);

        return query;
    }

    /**
     * This method is responsible for initializing the {@link Query} object with the BBOX filter as
     * per the incoming {@link RasterLayerRequest}.
     *
     * @return a {@link Query} object with the BBOX {@link Filter} in it.
     * @throws IOException in case something bad happens
     */
    private Query initQuery() throws TransformException, FactoryException, IOException {
        final String typeName = rasterManager.getTypeName();
        Filter bbox = null;
        if (typeName != null) {
            Query query = new Query(typeName);
            // max number of elements
            if (request.getMaximumNumberOfGranules() > 0) {
                query.setMaxFeatures(request.getMaximumNumberOfGranules());
            }
            final PropertyName geometryProperty =
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.property(
                            rasterManager
                                    .getGranuleCatalog()
                                    .getType(typeName)
                                    .getGeometryDescriptor()
                                    .getName());
            if (request.isHeterogeneousGranules() && queryBBox != null) {
                ProjectionHandler handler =
                        ProjectionHandlerFinder.getHandler(
                                queryBBox, queryBBox.getCoordinateReferenceSystem(), true);
                if (handler != null) {
                    List<ReferencedEnvelope> envelopes = handler.getQueryEnvelopes();
                    if (envelopes != null && envelopes.size() > 0) {
                        List<Filter> filters = new ArrayList<>();
                        for (ReferencedEnvelope envelope : envelopes) {
                            Filter f =
                                    FeatureUtilities.DEFAULT_FILTER_FACTORY.bbox(
                                            geometryProperty, envelope);
                            filters.add(f);
                        }
                        if (envelopes.size() == 1) {
                            bbox = filters.get(0);
                        } else {
                            bbox = FeatureUtilities.DEFAULT_FILTER_FACTORY.or(filters);
                        }
                    }
                }
            }
            if (bbox == null && queryBBox != null) {
                bbox = FeatureUtilities.DEFAULT_FILTER_FACTORY.bbox(geometryProperty, queryBBox);
            }
            if (bbox != null) {
                query.setFilter(bbox);
            }
            return query;
        } else {
            throw new IllegalStateException("GranuleCatalog feature type was null!!!");
        }
    }

    /**
     * This method is responsible for creating the filters needed for addtional dimensions like
     * TIME, ELEVATION additional Domains
     *
     * @param query the {@link Query} to set filters for.
     */
    private void handleAdditionalFilters(Query query) {
        final List times = request.getRequestedTimes();
        final List elevations = request.getElevation();
        final Map<String, List> additionalDomains = request.getRequestedAdditionalDomains();
        final Filter filter = request.getFilter();
        final boolean hasTime = (times != null && times.size() > 0);
        final boolean hasElevation = (elevations != null && elevations.size() > 0);
        final boolean hasAdditionalDomains = additionalDomains.size() > 0;
        final boolean hasFilter = filter != null && !Filter.INCLUDE.equals(filter);
        // prepare eventual filter for filtering granules
        // handle elevation indexing first since we then combine this with the max in case we are
        // asking for current in time
        if (hasElevation) {
            final Filter elevationF =
                    rasterManager.elevationDomainManager.createFilter(
                            GridCoverage2DReader.ELEVATION_DOMAIN, elevations);
            query.setFilter(
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(), elevationF));
        }

        // handle generic filter since we then combine this with the max in case we are asking for
        // current in time
        if (hasFilter) {
            query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(), filter));
        }

        // fuse time query with the bbox query
        if (hasTime) {
            final Filter timeFilter =
                    this.rasterManager.timeDomainManager.createFilter(
                            GridCoverage2DReader.TIME_DOMAIN, times);
            query.setFilter(
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(), timeFilter));
        }

        // === Custom Domains Management
        if (hasAdditionalDomains) {
            final List<Filter> additionalFilter = new ArrayList<>();
            for (Map.Entry<String, List> entry : additionalDomains.entrySet()) {

                // build a filter for each dimension
                final String domainName =
                        entry.getKey() + RasterManager.DomainDescriptor.DOMAIN_SUFFIX;
                additionalFilter.add(
                        rasterManager.domainsManager.createFilter(domainName, entry.getValue()));
            }
            // merge with existing ones
            query.setFilter(
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.and(
                            query.getFilter(),
                            FeatureUtilities.DEFAULT_FILTER_FACTORY.and(additionalFilter)));
        }
    }

    /**
     * Handles the optional {@link SortBy} clause for the query to the catalog
     *
     * @param query the {@link Query} to set the {@link SortBy} for.
     */
    private void handleSortByClause(final Query query) throws IOException {
        Utilities.ensureNonNull("query", query);
        LOGGER.fine("Prepping to manage SortBy Clause");
        final String sortByClause = request.getSortClause();
        final GranuleCatalog catalog = rasterManager.getGranuleCatalog();
        if (sortByClause != null && sortByClause.length() > 0) {
            final String[] elements = sortByClause.split(",");
            if (elements != null && elements.length > 0) {
                final List<SortBy> clauses = new ArrayList<>(elements.length);
                for (String element : elements) {
                    // check
                    if (element == null || element.length() <= 0) {
                        continue; // next, please!
                    }
                    try {
                        // which clause?
                        // ASCENDING
                        element = element.trim();
                        if (element.endsWith(Utils.ASCENDING_ORDER_IDENTIFIER)) {
                            String attribute = element.substring(0, element.length() - 2);
                            clauses.add(
                                    new SortByImpl(
                                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(
                                                    attribute),
                                            SortOrder.ASCENDING));
                            LOGGER.fine("Added clause ASCENDING on attribute:" + attribute);
                        } else
                        // DESCENDING
                        if (element.contains(Utils.DESCENDING_ORDER_IDENTIFIER)) {
                            String attribute = element.substring(0, element.length() - 2);
                            clauses.add(
                                    new SortByImpl(
                                            FeatureUtilities.DEFAULT_FILTER_FACTORY.property(
                                                    attribute),
                                            SortOrder.DESCENDING));
                            LOGGER.fine("Added clause DESCENDING on attribute:" + attribute);
                        } else {
                            LOGGER.fine("Ignoring sort clause :" + element);
                        }
                    } catch (Exception e) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                        }
                    }
                }

                // assign to query if sorting is supported!
                SortBy[] sortBy = clauses.toArray(new SortBy[] {});
                if (catalog.getQueryCapabilities(rasterManager.getTypeName())
                        .supportsSorting(sortBy)) {
                    query.setSortBy(sortBy);
                }
            } else {
                LOGGER.fine("No SortBy Clause");
            }
        } else {
            // no specified sorting, is this a heterogeneous CRS mosaic?
            String crsAttribute = rasterManager.getCrsAttribute();
            if (crsAttribute != null) {
                SortBy sort =
                        new SortByImpl(
                                FeatureUtilities.DEFAULT_FILTER_FACTORY.property(crsAttribute),
                                SortOrder.ASCENDING);
                SortBy[] sortBy = new SortBy[] {sort};
                if (catalog.getQueryCapabilities(rasterManager.getTypeName())
                        .supportsSorting(sortBy)) {
                    query.setSortBy(sortBy);
                } else {
                    LOGGER.severe(
                            "Sorting parameter ignored, underlying datastore cannot sort on "
                                    + Arrays.toString(sortBy));
                }
            }
        }
    }
}
