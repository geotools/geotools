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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.appschema.util.IndexQueryUtils;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.complex.IndexIdIterator.IndexFeatureIdIterator;
import org.geotools.data.complex.IndexIdIterator.IndexUniqueVisitorIterator;
import org.geotools.data.complex.IndexQueryManager.QueryIndexCoverage;
import org.geotools.data.complex.filter.IndexUnmappingVisitor;
import org.geotools.data.complex.util.XPathUtil;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.util.factory.GeoTools;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;

/**
 * Base class for Indexed Iterators
 *
 * @author Fernando Miño (Geosolutions
 */
public abstract class IndexedMappingFeatureIterator implements IMappingFeatureIterator {

    private static int MAX_FEATURES_ROUND = 100;

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

    protected final AppSchemaDataAccess store;
    protected final FeatureTypeMapping mapping;
    protected final Query query;
    protected final Filter unrolledFilter;
    protected final Transaction transaction;

    protected IndexIdIterator indexIterator;
    protected FeatureIterator<? extends Feature> sourceIterator;

    protected IndexQueryManager indexModeProc;
    protected QueryIndexCoverage queryMode;

    public IndexedMappingFeatureIterator(
            AppSchemaDataAccess store,
            FeatureTypeMapping mapping,
            Query query,
            Filter unrolledFilter,
            Transaction transaction,
            IndexQueryManager indexModeProcessor) {
        this.store = store;
        this.mapping = mapping;
        this.query = query;
        this.unrolledFilter = unrolledFilter;
        this.transaction = transaction;
        this.indexModeProc = indexModeProcessor;
        selectExecutionPlan();
    }

    /**
     * Analyze query and select a plan: 1.- All fields indexed, execute all query on index layer 2.-
     * Mixed fields indexed and not, execute indexed operators and re-map query to database
     */
    protected void selectExecutionPlan() {
        queryMode = indexModeProc.getIndexMode();
    }

    protected Query unrollIndexes(Query query) {
        Query newQuery = new Query(query);
        newQuery.setFilter(unrollFilter(query.getFilter()));
        return newQuery;
    }

    protected SortBy[] unrollSortBy(SortBy[] sortArray) {
        if (sortArray == null) return null;
        ArrayList<SortBy> unrolledSorts = new ArrayList<>();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        for (SortBy aSort : sortArray) {
            SortBy newSort =
                    ff.sort(
                            unrollIndex(aSort.getPropertyName(), mapping).getPropertyName(),
                            aSort.getSortOrder());
            unrolledSorts.add(newSort);
        }
        return unrolledSorts.toArray(new SortBy[] {});
    }

    public static PropertyName unrollIndex(PropertyName expression, FeatureTypeMapping mapping) {
        String targetXPath = expression.getPropertyName();
        AttributeMapping attMp = getIndexedAttribute(mapping, targetXPath);
        if (attMp != null) {
            return new AttributeExpressionImpl(attMp.getIndexField());
        }
        return new AttributeExpressionImpl((String) null);
    }

    protected Filter unrollFilter(Filter filter) {
        IndexUnmappingVisitor visitor = new IndexUnmappingVisitor(mapping);
        Filter unrolledFilter = (Filter) filter.accept(visitor, null);
        return unrolledFilter;
    }

    public StepList getFidStepList() {
        return XPathUtil.rootElementSteps(mapping.getTargetFeature(), mapping.namespaces);
    }

    public AttributeMapping getFidAttrMap() {
        return mapping.getAttributeMapping(getFidStepList());
    }

    public String getFidIndexName() {
        return getFidAttrMap().getIndexField();
    }

    protected boolean isDenormalized() {
        if (!"id".equals(getFidIndexName().toLowerCase())) return true;
        return false;
    }

    protected IndexIdIterator getIndexIterator() {
        if (indexIterator == null) {
            try {
                initializeIndexIterator();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return indexIterator;
    }

    /** Initialize the index FeatureCollection and iterator */
    protected void initializeIndexIterator() throws IOException {
        // rebuild Query to fetch only id attributes:
        Query idQuery = transformQueryToIdsOnly();
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc =
                mapping.getIndexSource().getFeatures(idQuery);
        // if denormalized index:
        if (isDenormalized()) {
            indexIterator = new IndexUniqueVisitorIterator(fc, idQuery, getFidIndexName());
        } else {
            indexIterator = new IndexFeatureIdIterator(fc.features());
        }
    }

    /**
     * Convert query to retrieve only id field, no other fields
     *
     * @return converted Query
     */
    protected Query transformQueryToIdsOnly() {
        Query idsQuery = new Query(unrollIndexes(query));
        idsQuery.setProperties(getIndexQueryProperties());
        idsQuery.setTypeName(mapping.getIndexSource().getSchema().getTypeName());
        return idsQuery;
    }

    protected List<PropertyName> getIndexQueryProperties() {
        return Arrays.asList(new PropertyName[] {ff.property(getFidIndexName())});
    }

    /**
     * Extracts next id list from index iterator
     *
     * @return list of id string
     */
    protected List<String> getNextSourceIdList() {
        int numFeatures = 0;
        List<String> ids = new ArrayList<>();
        while (numFeatures < MAX_FEATURES_ROUND && getIndexIterator().hasNext()) {
            ids.add(indexIterator.next());
            numFeatures++;
        }
        return ids;
    }

    /**
     * Search for indexed attribute, including on Nested Features
     *
     * @return indexed attribute xpath, or null if not found
     */
    protected static AttributeMapping getIndexedAttribute(
            FeatureTypeMapping mapping, String xpath) {
        return IndexQueryUtils.getIndexedAttribute(mapping, xpath);
    }
}
