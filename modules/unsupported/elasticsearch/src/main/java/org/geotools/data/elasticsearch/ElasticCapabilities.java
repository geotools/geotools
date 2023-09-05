/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch;

import java.util.HashMap;
import java.util.Map;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.capability.TemporalCapabilities;
import org.geotools.api.filter.capability.TemporalOperators;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.AnyInteracts;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.filter.Capabilities;
import org.geotools.filter.capability.FilterCapabilitiesImpl;
import org.geotools.filter.capability.TemporalCapabilitiesImpl;
import org.geotools.filter.capability.TemporalOperatorImpl;
import org.geotools.filter.visitor.IsFullySupportedFilterVisitor;

/**
 * Custom {@link Capabilities} supporting temporal capabilities and operators. Uses a custom {@link
 * IsFullySupportedFilterVisitor} to enable support for {@link IncludeFilter}, {@link ExcludeFilter}
 * and {@link BegunBy}.
 */
class ElasticCapabilities extends Capabilities {
    private static final Map<Class<?>, String> temporalNames;

    static {
        temporalNames = new HashMap<>();
        temporalNames.put(After.class, After.NAME);
        temporalNames.put(AnyInteracts.class, AnyInteracts.NAME);
        temporalNames.put(Before.class, Before.NAME);
        temporalNames.put(Begins.class, Begins.NAME);
        temporalNames.put(BegunBy.class, BegunBy.NAME);
        temporalNames.put(During.class, During.NAME);
        temporalNames.put(EndedBy.class, EndedBy.NAME);
        temporalNames.put(Ends.class, Ends.NAME);
        temporalNames.put(Meets.class, Meets.NAME);
        temporalNames.put(MetBy.class, MetBy.NAME);
        temporalNames.put(OverlappedBy.class, OverlappedBy.NAME);
        temporalNames.put(TContains.class, TContains.NAME);
        temporalNames.put(TEquals.class, TEquals.NAME);
        temporalNames.put(TOverlaps.class, TOverlaps.NAME);
    }

    private IsFullySupportedFilterVisitor fullySupportedVisitor;

    public ElasticCapabilities() {
        super(new ElasticFilterCapabilities());

        addAll(LOGICAL_OPENGIS);
        addAll(SIMPLE_COMPARISONS_OPENGIS);
        addType(PropertyIsNull.class);
        addType(PropertyIsBetween.class);
        addType(Id.class);
        addType(IncludeFilter.class);
        addType(ExcludeFilter.class);
        addType(PropertyIsLike.class);

        // spatial filters
        addType(BBOX.class);
        addType(Contains.class);
        // addType(Crosses.class);
        addType(Disjoint.class);
        // addType(Equals.class);
        addType(Intersects.class);
        // addType(Overlaps.class);
        // addType(Touches.class);
        addType(Within.class);
        addType(DWithin.class);
        addType(Beyond.class);

        // temporal filters
        addType(After.class);
        addType(Before.class);
        addType(Begins.class);
        addType(BegunBy.class);
        addType(During.class);
        addType(Ends.class);
        addType(EndedBy.class);
        addType(TContains.class);
        addType(TEquals.class);
    }

    @Override
    public boolean fullySupports(Filter filter) {
        if (fullySupportedVisitor == null) {
            fullySupportedVisitor = new ElasticIsFullySupportedFilterVisitor();
        }
        return filter != null ? (Boolean) filter.accept(fullySupportedVisitor, null) : false;
    }

    @Override
    public String toOperationName(Class filterType) {
        if (filterType != null && temporalNames.containsKey(filterType)) {
            return temporalNames.get(filterType);
        }
        return super.toOperationName(filterType);
    }

    @Override
    public void addName(String name) {
        if (name != null && temporalNames.containsValue(name)) {
            final TemporalOperators operators =
                    getContents().getTemporalCapabilities().getTemporalOperators();
            operators.getOperators().add(new TemporalOperatorImpl(name));
        } else {
            super.addName(name);
        }
    }

    private static class ElasticFilterCapabilities extends FilterCapabilitiesImpl {

        TemporalCapabilitiesImpl temporal;

        @Override
        public TemporalCapabilities getTemporalCapabilities() {
            if (temporal == null) {
                temporal = new TemporalCapabilitiesImpl();
                super.setTemporal(temporal);
            }
            return temporal;
        }
    }

    private class ElasticIsFullySupportedFilterVisitor extends IsFullySupportedFilterVisitor {

        ElasticIsFullySupportedFilterVisitor() {
            super(getContents());
        }

        @Override
        public Object visit(ExcludeFilter filter, Object extraData) {
            return true;
        }

        @Override
        public Object visit(IncludeFilter filter, Object extraData) {
            return true;
        }

        @Override
        public Object visit(BegunBy begunBy, Object extraData) {
            return visit((BinaryTemporalOperator) begunBy, BegunBy.NAME);
        }
    }
}
