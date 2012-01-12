/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.io.IOException;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.filter.ComplexFilterSplitter;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.feature.Types;
import org.geotools.filter.FidFilterImpl;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.NestedAttributeExpression;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCFeatureStore;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Russell Petty (GeoScience Victoria)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/extension/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/MappingFeatureIteratorFactory.java $
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/MappingFeatureIteratorFactory.java $
 */
public class MappingFeatureIteratorFactory {
    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.complex");    

    public static IMappingFeatureIterator getInstance(AppSchemaDataAccess store,
            FeatureTypeMapping mapping, Query query, Filter unrolledFilter) throws IOException {

        if (mapping instanceof XmlFeatureTypeMapping) {
            return new XmlMappingFeatureIterator(store, mapping, query);
        }

        boolean isJoining = AppSchemaDataAccessConfigurator.isJoining();

        if (isJoining) {
            if (!(query instanceof JoiningQuery)) {
                query = new JoiningQuery(query);
            }
        }
        IMappingFeatureIterator iterator;
        if (unrolledFilter != null) {
            // unrolledFilter is set in JoiningNestedAttributeMapping
            // so this is for nested features with joining
            query.setFilter(Filter.INCLUDE);
            Query unrolledQuery = store.unrollQuery(query, mapping);
            unrolledQuery.setFilter(unrolledFilter);
            if (isSimpleType(mapping)) {
                iterator = new MappingAttributeIterator(store, mapping, query, unrolledQuery);
            } else {
                iterator = new DataAccessMappingFeatureIterator(store, mapping, query,
                        unrolledQuery);
            }
        } else {
            FeatureSource mappedSource = mapping.getSource();
            if (isJoining || mappedSource instanceof JDBCFeatureSource
                    || mappedSource instanceof JDBCFeatureStore) {
                // has database as data source, we can use the data source filter capabilities
                FilterCapabilities capabilities = getFilterCapabilities(mappedSource);
                ComplexFilterSplitter splitter = new ComplexFilterSplitter(capabilities, mapping);
                Filter filter = query.getFilter();
                filter.accept(splitter, null);
                Filter preFilter = splitter.getFilterPre();
                query.setFilter(preFilter);
                filter = splitter.getFilterPost();

                int maxFeatures = Query.DEFAULT_MAX;
                if (filter != null && filter != Filter.INCLUDE) {
                    maxFeatures = query.getMaxFeatures();
                    query.setMaxFeatures(Query.DEFAULT_MAX);
                }
                // need to flag if this is non joining and has pre filter because it needs
                // to find denormalised rows that match the id (but doesn't match pre filter)
                boolean isFiltered = !isJoining && preFilter != null && preFilter != Filter.INCLUDE;
                iterator = new DataAccessMappingFeatureIterator(store, mapping, query, isFiltered);
                if (filter != null && filter != Filter.INCLUDE) {
                    iterator = new PostFilteringMappingFeatureIterator(iterator, filter,
                            maxFeatures);
                }
            } else if (mappedSource instanceof MappingFeatureSource) {
                // web service data access wrapper
                iterator = new DataAccessMappingFeatureIterator(store, mapping, query);
            } else {
                // non database sources e.g. property data store
                Query unrolledQuery = store.unrollQuery(query, mapping);
                Filter filter = unrolledQuery.getFilter();

                if (!filter.equals(Filter.INCLUDE) && !filter.equals(Filter.EXCLUDE)
                        && !(filter instanceof FidFilterImpl)) {
                    // non joining with filters
                    // the filter can't be passed to the data source as is and has to be
                    // performed
                    // per simple feature
                    // also in case the data is denormalised and the 2nd row matches the filter, it
                    // will still find the first row
                    // since this iterator would group the sources by id before evaluating the
                    // filter
                    unrolledQuery.setFilter(Filter.INCLUDE);

                    CoordinateReferenceSystem crs = query.getCoordinateSystemReproject();
                    if (crs != null) {
                        // remove reprojection too, as it should be done after filter is applied
                        // to be consistent with app-schema with JDBC sources
                        unrolledQuery.setCoordinateSystemReproject(null);
                    }
                    iterator = new FilteringMappingFeatureIterator(store, mapping, query,
                            unrolledQuery, filter);
                } else {
                    // non database sources with no filters
                    iterator = new DataAccessMappingFeatureIterator(store, mapping, query);
                }
            }
        }

        return iterator;
    }

    private static boolean isSimpleType(FeatureTypeMapping mapping) {
        return Types.isSimpleContentType(mapping.getTargetFeature().getType());
    }

    private static FilterCapabilities getFilterCapabilities(FeatureSource mappedSource)
            throws IllegalArgumentException {
        FilterCapabilities capabilities = null;
        if (mappedSource instanceof JDBCFeatureSource) {
            capabilities = ((JDBCFeatureSource) mappedSource).getDataStore()
                    .getFilterCapabilities();
        } else if (mappedSource instanceof JDBCFeatureStore) {
            capabilities = ((JDBCFeatureStore) mappedSource).getDataStore().getFilterCapabilities();
        } else {
            throw new IllegalArgumentException(
                    "Joining queries are only supported on JDBC data stores");
        }
        return capabilities;
    }

}
