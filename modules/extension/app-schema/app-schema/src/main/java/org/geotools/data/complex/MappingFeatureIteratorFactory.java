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

/**
 * @author Russell Petty (GeoScience Victoria)
 *
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/MappingFeatureIteratorFactory.java $
 */
public class MappingFeatureIteratorFactory {
    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.complex");


    protected static class CheckIfNestedFilterVisitor extends DefaultFilterVisitor {

        public boolean hasNestedAttributes = false;

        public Object visit( PropertyName expression, Object data ) {
            if (expression instanceof NestedAttributeExpression) {
                hasNestedAttributes = true;
            }
            return data;
        }
    }

    public static IMappingFeatureIterator getInstance(AppSchemaDataAccess store,
            FeatureTypeMapping mapping, Query query, Filter unrolledFilter) throws IOException {

        if (mapping instanceof XmlFeatureTypeMapping) {
            return new XmlMappingFeatureIterator(store, mapping, query);
        }

        if (AppSchemaDataAccessConfigurator.isJoining()) {
            if (!(query instanceof JoiningQuery)) {
                query = new JoiningQuery(query);
            }
            FeatureSource mappedSource = mapping.getSource();
            FilterCapabilities capabilities = getFilterCapabilities(mappedSource);
            

            IMappingFeatureIterator iterator;
            if (unrolledFilter != null) {
                query.setFilter(Filter.INCLUDE);
                Query unrolledQuery = store.unrollQuery(query, mapping);
                unrolledQuery.setFilter(unrolledFilter);
                if (isSimpleType(mapping)) {
                    iterator = new MappingAttributeIterator(store, mapping, query, false, unrolledQuery);
                } else {
                    iterator = new DataAccessMappingFeatureIterator(store, mapping, query, false, unrolledQuery);
                }

            } else {
                Filter filter = query.getFilter();
                ComplexFilterSplitter splitter = new ComplexFilterSplitter( capabilities , mapping );
                filter.accept(splitter, null);

                query.setFilter(splitter.getFilterPre());
                filter = splitter.getFilterPost();
                int maxFeatures = Query.DEFAULT_MAX;
                if (filter != null && filter != Filter.INCLUDE) {
                    maxFeatures = query.getMaxFeatures();
                    query.setMaxFeatures(Query.DEFAULT_MAX);
                }
                iterator = new DataAccessMappingFeatureIterator(store, mapping, query, false);
                if (filter != null && filter != Filter.INCLUDE) {
                    iterator = new PostFilteringMappingFeatureIterator(iterator, filter, maxFeatures);
                }
            }
            return iterator;
        } else {
            if (query.getFilter() != null) {
                Query unrolledQuery = store.unrollQuery(query, mapping);
                Filter filter = unrolledQuery.getFilter();
                CheckIfNestedFilterVisitor visitor = new CheckIfNestedFilterVisitor();
                filter.accept(visitor, null);
                if (visitor.hasNestedAttributes) {
                    FeatureSource mappedSource = mapping.getSource();
                    if (mappedSource instanceof JDBCFeatureSource
                            || mappedSource instanceof JDBCFeatureStore) {
                        FilterCapabilities capabilities = getFilterCapabilities(mappedSource);
                        ComplexFilterSplitter splitter = new ComplexFilterSplitter(capabilities,
                                mapping);
                        filter.accept(splitter, null);
                        query.setFilter(splitter.getFilterPre());
                        unrolledQuery.setFilter(splitter.getFilterPre());
                        filter = splitter.getFilterPost();
                    } else {
                        // VT:no Filtering capbilities cause source may not be of jdbc type
                        // therefore we continue;
                        // has nested attribute in the filter expression
                        unrolledQuery.setFilter(Filter.INCLUDE);
                    }
                    return new FilteringMappingFeatureIterator(store, mapping, query,
                            unrolledQuery, filter);
                } else if (!filter.equals(Filter.INCLUDE) && !filter.equals(Filter.EXCLUDE)
                        && !(filter instanceof FidFilterImpl)) {
                    // normal filters
                    if (isSimpleType(mapping)) {
                        return new MappingAttributeIterator(store, mapping, query, true, unrolledQuery);
                    } else {
                        return new DataAccessMappingFeatureIterator(store, mapping, query, true, unrolledQuery);
                    }
                }
            }

            return new DataAccessMappingFeatureIterator(store, mapping, query, false);
        }
    }
    
    private static boolean isSimpleType(FeatureTypeMapping mapping) {
        return Types.isSimpleContentType(mapping.getTargetFeature().getType());       
    }
    
    private static FilterCapabilities getFilterCapabilities(FeatureSource mappedSource)throws IllegalArgumentException{
        FilterCapabilities capabilities=null;
        if (mappedSource instanceof JDBCFeatureSource) {
            capabilities = ((JDBCFeatureSource) mappedSource).getDataStore().getFilterCapabilities();
        } else if (mappedSource instanceof JDBCFeatureStore){
            capabilities = ((JDBCFeatureStore) mappedSource).getDataStore().getFilterCapabilities();
        } else {
            throw new IllegalArgumentException("Joining queries are only supported on JDBC data stores");
        }
        return capabilities;
    }       
   
}
