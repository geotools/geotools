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
import org.geotools.filter.Capabilities;
import org.geotools.filter.capability.FilterCapabilitiesImpl;
import org.geotools.filter.capability.TemporalCapabilitiesImpl;
import org.geotools.filter.capability.TemporalOperatorImpl;
import org.geotools.filter.visitor.IsFullySupportedFilterVisitor;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.capability.TemporalCapabilities;
import org.opengis.filter.capability.TemporalOperators;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

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
