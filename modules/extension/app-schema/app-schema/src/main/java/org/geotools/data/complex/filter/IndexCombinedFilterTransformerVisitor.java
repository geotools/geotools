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
package org.geotools.data.complex.filter;

import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;

/**
 * Duplicates Filter replacing mixed-indexed operator with combined ID IN clause + remaining filters
 *
 * @author Fernando Miño, Geosolutions
 */
public class IndexCombinedFilterTransformerVisitor extends DuplicatingFilterVisitor {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private BinaryLogicOperator indexedParentLogicOperator;
    private List<Filter> indexedFilters;
    private Filter idInFilter;

    public IndexCombinedFilterTransformerVisitor(
            BinaryLogicOperator indexedParentLogicOperator,
            List<Filter> indexedFilters,
            Filter idInFilter) {
        super();
        this.indexedParentLogicOperator = indexedParentLogicOperator;
        this.indexedFilters = indexedFilters;
        this.idInFilter = idInFilter;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        Or replace = process(filter);
        if (replace == null) return super.visit(filter, extraData);
        else return replace;
    }

    @Override
    public Object visit(And filter, Object extraData) {
        And replace = process(filter);
        if (replace == null) return super.visit(filter, extraData);
        else return replace;
    }

    private <T extends BinaryLogicOperator> T process(T filter) {
        // is this the logic-operator to replace?
        if (filter.equals(indexedParentLogicOperator)) {
            // build AND/OR children:
            List<Filter> gfilters = new ArrayList<>();
            // add ID IN () filter
            gfilters.add(idInFilter);
            // add non-indexed filters (duplicates):
            indexedParentLogicOperator.getChildren().stream()
                    .filter(f -> indexedFilters.stream().noneMatch(i -> i.equals(f)))
                    .forEach(f -> gfilters.add(duplicateFilter(f)));
            // build replace operator filter
            return createOperator(filter, gfilters);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends BinaryLogicOperator> T createOperator(T oldParent, List<Filter> children) {
        T result = null;
        if (oldParent instanceof And) {
            result = (T) ff.and(children);
        } else if (oldParent instanceof Or) {
            result = (T) ff.or(children);
        }
        return result;
    }

    private Filter duplicateFilter(Filter filter) {
        DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor();
        Filter result = (Filter) filter.accept(visitor, ff);
        return result;
    }
}
