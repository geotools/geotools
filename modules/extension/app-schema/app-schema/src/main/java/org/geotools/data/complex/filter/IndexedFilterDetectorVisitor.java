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
import java.util.stream.Collectors;
import org.geotools.appschema.util.IndexQueryUtils;
import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;

/**
 * Detects which AND/OR filter (BinaryLogicOperator) is the parent operator of indexed filter(s).
 * Then collects the full-indexed subfilter(s). Unrolled Filter implementation
 *
 * @author Fernando Miño - Geosolutions
 */
public class IndexedFilterDetectorVisitor extends DefaultFilterVisitor {

    protected FeatureTypeMapping mapping;

    protected BinaryLogicOperator parentLogicOperator;
    protected List<Filter> indexedFilters = new ArrayList<>();

    public IndexedFilterDetectorVisitor(FeatureTypeMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public Object visit(Or filter, Object data) {
        if (parentLogicOperator != null) return data;
        processFilter(filter);
        return super.visit(filter, data);
    }

    @Override
    public Object visit(And filter, Object data) {
        if (parentLogicOperator != null) return data;
        processFilter(filter);
        return super.visit(filter, data);
    }

    /**
     * Detect if AND/OR filter is the parent operator of indexed filter then collect the
     * full-indexed subfilter(s)
     */
    protected void processFilter(BinaryLogicOperator filter) {
        if (parentLogicOperator != null) return;
        boolean hasindexedFilter = filter.getChildren().stream().anyMatch(c -> isFullyIndexed(c));
        if (hasindexedFilter) {
            parentLogicOperator = filter;
            // get the full indexed subfilter(s)
            indexedFilters =
                    filter.getChildren()
                            .stream()
                            .filter(c -> isFullyIndexed(c))
                            .collect(Collectors.toList());
        }
    }

    /**
     * checks if Filter is fully indexed
     *
     * @param filter unrolled filter
     */
    protected boolean isFullyIndexed(Filter filter) {
        return IndexQueryUtils.checkAllPropertiesIndexed(
                IndexQueryUtils.getAttributesOnFilter(filter), mapping);
    }

    public BinaryLogicOperator getParentLogicOperator() {
        return parentLogicOperator;
    }

    public List<Filter> getIndexedFilters() {
        return indexedFilters;
    }
}
