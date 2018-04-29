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
package org.geotools.styling.css.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.styling.css.selector.Accept;
import org.geotools.styling.css.selector.And;
import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Id;
import org.geotools.styling.css.selector.Or;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.Reject;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.selector.SelectorVisitor;
import org.geotools.styling.css.selector.TypeName;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;

/**
 * Turns a Selector into an OGC filter
 *
 * @author Andrea Aime - GeoSolutions
 */
public class OgcFilterBuilder implements SelectorVisitor {

    public static OgcFilterBuilder INSTANCE = new OgcFilterBuilder();

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    @Override
    public Object visit(Accept accept) {
        return Filter.INCLUDE;
    }

    @Override
    public Object visit(Reject reject) {
        return Filter.EXCLUDE;
    }

    @Override
    public Object visit(Id id) {
        if (id.ids == null || id.ids.isEmpty()) {
            return Filter.INCLUDE;
        }
        Set<Identifier> identifiers = new LinkedHashSet<>();
        for (String identifier : id.ids) {
            FeatureId featureId = FF.featureId(identifier);
            identifiers.add(featureId);
        }
        return FF.id(identifiers);
    }

    @Override
    public Object visit(Data data) {
        return data.filter;
    }

    @Override
    public Object visit(And and) {
        List<Filter> filters = new ArrayList<>();
        for (Selector child : and.getChildren()) {
            Filter filter = (Filter) child.accept(this);
            if (filter == Filter.EXCLUDE) {
                return Filter.EXCLUDE;
            } else if (filter != Filter.INCLUDE) {
                filters.add(filter);
            }
        }

        if (filters.isEmpty()) {
            return Filter.INCLUDE;
        } else {
            return FF.and(filters);
        }
    }

    @Override
    public Object visit(Or or) {
        List<Filter> filters = new ArrayList<>();
        for (Selector child : or.getChildren()) {
            Filter filter = (Filter) child.accept(this);
            if (filter == Filter.INCLUDE) {
                return Filter.INCLUDE;
            } else if (filter != Filter.EXCLUDE) {
                filters.add(filter);
            }
        }

        if (filters.isEmpty()) {
            return Filter.EXCLUDE;
        } else {
            return FF.or(filters);
        }
    }

    @Override
    public Object visit(TypeName typeName) {
        // ignore, it has been handled elsewhere
        return Filter.INCLUDE;
    }

    @Override
    public Object visit(ScaleRange scaleRange) {
        // ignore, it has been handled elsewhere
        return Filter.INCLUDE;
    }

    @Override
    public Object visit(PseudoClass pseudoClass) {
        // ignore, it has been handled elsewhere
        return Filter.INCLUDE;
    }

    public static Filter buildFilter(Selector selector, FeatureType targetFeatureType) {
        Filter filter = (Filter) selector.accept(INSTANCE);
        SimplifyingFilterVisitor simplifier = new UnboundSimplifyingFilterVisitor();
        simplifier.setFeatureType(targetFeatureType);
        return (Filter) filter.accept(simplifier, null);
    }
}
