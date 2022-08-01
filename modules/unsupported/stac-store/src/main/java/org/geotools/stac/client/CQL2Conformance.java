/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.client;

import static java.util.regex.Pattern.quote;

import java.util.List;
import java.util.regex.Pattern;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.InFunction;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;

/** Conformance clasess for CQL2 */
public enum CQL2Conformance {
    TEXT("cql2-text"),
    JSON("cql2-json"),
    BASIC("basic-cql2", Capabilities.BASIC),
    ADVANCED("advanced-comparison-operators", Capabilities.ADVANCED),
    BASIC_SPATIAL("basic-spatial-operators", Capabilities.BASIC_SPATIAL),
    SPATIAL_OPERATORS("spatial-operators", Capabilities.SPATIAL),
    TEMPORAL_OPERATORS("temporal-operators", Capabilities.TEMPORAL),
    ARITHMETIC("arithmetic", Capabilities.ARITHMETIC),
    PROPERTY_PROPERTY("property-property");

    private static final String CQL_PREFIX = "http://www.opengis.net/spec/cql2/1.0";

    private static class Capabilities {
        private static final FilterCapabilities BASIC;
        private static final FilterCapabilities ADVANCED;
        private static final FilterCapabilities BASIC_SPATIAL;
        private static final FilterCapabilities SPATIAL;

        private static final FilterCapabilities ARITHMETIC;

        private static final FilterCapabilities TEMPORAL;

        static {
            BASIC = new FilterCapabilities();
            BASIC.addAll(FilterCapabilities.LOGICAL_OPENGIS);
            BASIC.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
            BASIC.addType(PropertyIsNull.class);

            ADVANCED = new FilterCapabilities();
            ADVANCED.addAll(BASIC);
            ADVANCED.addType(PropertyIsLike.class);
            ADVANCED.addType(PropertyIsBetween.class);
            ADVANCED.addAll(InFunction.getInCapabilities());

            BASIC_SPATIAL = new FilterCapabilities();
            BASIC_SPATIAL.addType(BBOX.class);
            BASIC_SPATIAL.addType(Intersects.class);

            SPATIAL = new FilterCapabilities();
            SPATIAL.addAll(BASIC_SPATIAL);
            SPATIAL.addType(Contains.class);
            SPATIAL.addType(Crosses.class);
            SPATIAL.addType(Disjoint.class);
            SPATIAL.addType(Equals.class);
            SPATIAL.addType(Overlaps.class);
            SPATIAL.addType(Touches.class);
            SPATIAL.addType(Within.class);

            // skipping the period oriented operators for now
            TEMPORAL = new FilterCapabilities();
            TEMPORAL.addType(After.class);
            TEMPORAL.addType(Before.class);

            ARITHMETIC = new FilterCapabilities();
            ARITHMETIC.addType(FilterCapabilities.SIMPLE_ARITHMETIC);
        }
    }

    private final Pattern pattern;

    private final FilterCapabilities capabilities;

    CQL2Conformance(String suffix) {
        this(suffix, new FilterCapabilities());
    }

    CQL2Conformance(String suffix, FilterCapabilities capabilities) {
        this.pattern = Pattern.compile(quote(CQL_PREFIX) + ".*/" + quote(suffix));
        this.capabilities = capabilities;
    }

    /**
     * Checks if this STAC conformance class is a match, ignoring details about the version such as
     * beta/RC/minor version changes (as long as it's a 1.0.x)
     *
     * @param conformance
     */
    public boolean matches(List<String> conformance) {
        return conformance.stream().anyMatch(c -> pattern.matcher(c).matches());
    }

    /**
     * Returns the capabilities linked to this conformance class, or an empty capabilities set,
     * otherwise
     */
    public FilterCapabilities getCapabilities() {
        return capabilities;
    }
}
