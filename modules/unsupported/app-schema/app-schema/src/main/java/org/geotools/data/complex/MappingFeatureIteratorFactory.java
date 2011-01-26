/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.Query;
import org.geotools.data.complex.filter.MultiValuedOrImpl;
import org.geotools.filter.FidFilterImpl;
import org.opengis.filter.Filter;

/**
 * @author Russell Petty, GSV
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/MappingFeatureIteratorFactory.java $
 */
public class MappingFeatureIteratorFactory {
    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.complex");

    public static IMappingFeatureIterator getInstance(AppSchemaDataAccess store,
            FeatureTypeMapping mapping, Query query) throws IOException {

        if (mapping instanceof XmlFeatureTypeMapping) {
            return new XmlMappingFeatureIterator(store, mapping, query);
        }

        boolean isFiltered = false;
        if (query.getFilter() != null) {
            Query unrolledQuery = store.unrollQuery(query, mapping);
            Filter filter = unrolledQuery.getFilter();
            if (filter instanceof MultiValuedOrImpl) {
                // has nested attribute in the filter expression
                unrolledQuery.setFilter(Filter.INCLUDE);
                return new FilteringMappingFeatureIterator(store, mapping, unrolledQuery, filter);
            } else if (!filter.equals(Filter.INCLUDE) && !filter.equals(Filter.EXCLUDE)
                    && !(filter instanceof FidFilterImpl)) {
                // normal filters
                isFiltered = true;
            }
        }

        DataAccessMappingFeatureIterator iterator = null;
        try {
            iterator = new DataAccessMappingFeatureIterator(store, mapping, query, isFiltered);
        } catch (IOException e) {
            // HACK HACK HACK
            // could mean it's a combination of filters (such as AND) involving nested attribute
            // it's hard to predetermine such condition, because a filter could be deeply nested
            // and combined endlessly, i.e. AND inside AND, etc.
            // it's also a bit naughty to catch Exception in general, but it's unpredicatable
            // what's going to be thrown, could be different for different datastore backend
            if (isFiltered) {
                LOGGER
                        .info("Caught exception: "
                                + e.getMessage()
                                + "in DataAccessMappingFeatureIterator."
                                + "Assuming this is caused by filtering nested attribute."
                                + "Retrying with FilteringMappingFeatureIterator.");
                Query unrolledQuery = store.unrollQuery(query, mapping);
                Filter filter = unrolledQuery.getFilter();
                unrolledQuery.setFilter(Filter.INCLUDE);
                iterator = new FilteringMappingFeatureIterator(store, mapping, unrolledQuery,
                        filter);
            } else {
                throw e;
            }
        }
        return iterator;
    }
}
