/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.complex.IndexQueryManager.QueryIndexCoverage;
import org.opengis.filter.Filter;

/**
 * Factory for IndexedMappingFeatureIterator subclasses
 *
 * @author Fernando Miño, Geosolutions
 */
public class IndexedMappingFeatureIteratorFactory {

    protected final AppSchemaDataAccess store;
    protected final FeatureTypeMapping mapping;
    protected final Query query;
    protected final Filter unrolledFilter;
    protected final Transaction transaction;

    private IndexQueryManager indexModeProcessor;

    public IndexedMappingFeatureIteratorFactory(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Filter unrolledFilter,
            Transaction transaction) {
        this.store = store;
        this.mapping = mapping;
        this.query = query;
        this.unrolledFilter = unrolledFilter;
        this.transaction = transaction;
        this.indexModeProcessor = new IndexQueryManager(mapping, query);
    }

    /**
     * Build IndexedMappingFeatureIterator instance for partial or total index query coverage.
     * return null if no index coverage detected for the query
     *
     * @return IndexedMappingFeatureIterator instance
     */
    public IndexedMappingFeatureIterator buildInstance() {
        if (indexModeProcessor.getIndexMode().equals(QueryIndexCoverage.NONE)) return null;
        else if (indexModeProcessor.getIndexMode().equals(QueryIndexCoverage.PARTIAL))
            return new PartialIndexedMappingFeatureIterator(
                    store, mapping, query, unrolledFilter, transaction, indexModeProcessor);
        return new TotalIndexedMappingFeatureIterator(
                store, mapping, query, unrolledFilter, transaction, indexModeProcessor);
    }

    public IndexQueryManager getIndexModeProcessor() {
        return indexModeProcessor;
    }
}
