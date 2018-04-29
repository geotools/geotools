/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css.selector;

import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.styling.css.util.FilterSpecificityExtractor;
import org.geotools.styling.css.util.UnboundSimplifyingFilterVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

public class Data extends Selector {

    public static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    public static Selector combineAnd(List<Data> selectors, Object ctx) {
        if (selectors.size() == 1) {
            return selectors.get(0);
        }

        List<Filter> filters = new ArrayList<>();
        FeatureType featureType = null;
        for (Data selector : selectors) {
            filters.add(selector.filter);
            featureType = selector.featureType;
        }

        org.opengis.filter.And and = FF.and(filters);
        SimplifyingFilterVisitor visitor;
        if (ctx instanceof SimplifyingFilterVisitor) {
            visitor = (SimplifyingFilterVisitor) ctx;
        } else {
            visitor = new UnboundSimplifyingFilterVisitor();
            visitor.setFeatureType(featureType);
        }
        Filter simplified = (Filter) and.accept(visitor, null);
        if (Filter.INCLUDE.equals(simplified)) {
            return ACCEPT;
        } else if (Filter.EXCLUDE.equals(simplified)) {
            return REJECT;
        } else {
            return new Data(simplified);
        }
    }

    public Filter filter;

    public FeatureType featureType;

    public Data(Filter filter) {
        this.filter = filter;
    }

    public Data(String filter) {
        try {
            this.filter = ECQL.toFilter(filter);
        } catch (CQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filter == null) ? 0 : filter.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Data other = (Data) obj;
        if (filter == null) {
            if (other.filter != null) return false;
        } else if (!filter.equals(other.filter)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "OGCFilter [filter=" + ECQL.toCQL(filter) + "]";
    }

    @Override
    public Specificity getSpecificity() {
        FilterSpecificityExtractor extractor = new FilterSpecificityExtractor();
        filter.accept(extractor, null);
        return new Specificity(0, extractor.getSpecificityScore(), 0);
    }

    public Object accept(SelectorVisitor visitor) {
        return visitor.visit(this);
    }
}
