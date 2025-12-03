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
package org.geotools.dggs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.dggs.gstore.DGGSResolutionCalculator;
import org.geotools.dggs.gstore.DGGSStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * Duplicates the filter, turning simple spatial filters and {@link org.geotools.dggs.DGGSFunction} instances into
 * filters against the zone identifier, for a given resolution. Spatial filters must already be expressed in
 * {@link DefaultGeographicCRS#WGS84}
 */
public class DGGSFilterTransformer extends DuplicatingFilterVisitor {

    public static final int RESOLUTION_NOT_SPECIFIED = -1;

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    public static Filter adapt(
            Filter filter,
            DGGSInstance<?> dggs,
            DGGSResolutionCalculator resolutionCalculator,
            int resolution,
            AttributeDescriptor zoneAttribute) {
        DGGSFilterTransformer adapter =
                new DGGSFilterTransformer(dggs, resolutionCalculator, resolution, zoneAttribute);
        return (Filter) filter.accept(adapter, null);
    }

    DGGSInstance<?> dggs;
    int resolution;
    AttributeDescriptor zoneAttribute;
    DGGSResolutionCalculator resolutionCalculator;

    public DGGSFilterTransformer(
            DGGSInstance<?> dggs,
            DGGSResolutionCalculator resolutionCalculator,
            int resolution,
            AttributeDescriptor zoneAttribute) {
        this.dggs = dggs;
        this.resolution = resolution;
        this.zoneAttribute = zoneAttribute;
        this.resolutionCalculator = resolutionCalculator;
    }

    // TODO: turn DGGSFunction too

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        if (filter.getExpression1() instanceof DGGSSetFunction function
                && filter.getExpression2() instanceof Literal
                && Boolean.TRUE.equals(filter.getExpression2().evaluate(null, Boolean.class))) {
            if (function.isStable()) {
                Iterator<Zone> zones = function.getMatchedZones();
                return getFilterFrom(zones);
            }
        }

        return super.visit(filter, extraData);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        // assuming a single geometry property in the DGGS output type
        if (filter.getExpression1() instanceof PropertyName && filter.getExpression2() instanceof Literal) {
            Geometry geometry = (Geometry) filter.getExpression2().evaluate(Geometry.class);
            if (geometry instanceof Polygon polygon) {
                Iterator<Zone> zones = dggs.polygon(polygon, resolution, true);
                return getFilterFrom(dggs, zones, resolution, resolutionCalculator, zoneAttribute);
            } else if (geometry instanceof MultiPolygon multiPolygon) {
                List<Iterator<Zone>> iterators = new ArrayList<>();
                for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                    Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
                    iterators.add(dggs.polygon(polygon, resolution, true));
                }
                @SuppressWarnings("unchecked")
                Iterator<Zone> zones = IteratorUtils.chainedIterator(iterators.toArray(n -> new Iterator[n]));
                return getFilterFrom(dggs, zones, resolution, resolutionCalculator, zoneAttribute);
            }
        }
        // fallback for non supported cases
        return super.visit(filter, extraData);
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        ReferencedEnvelope envelope = ReferencedEnvelope.reference(filter.getBounds());
        if (resolution == RESOLUTION_NOT_SPECIFIED) {
            // TODO: ask the DGGS instance to return a compact list, with parent zones to be
            // used as a Like filter
            //            NumberRange<Integer> resolutions =
            // resolutionCalculator.getValidResolutions();
            //            List<Filter> filters =
            //                    IntStream.range(resolutions.getMinValue(),
            // resolutions.getMaxValue() + 1)
            //                            .mapToObj(r ->
            // getFilterFrom(dggs.zonesFromEnvelope(envelope, r)))
            //                            .collect(Collectors.toList());
            //            return FF.or(filters);
            return super.visit(filter, extraData);
        } else {
            return getFilterFrom(
                    dggs,
                    dggs.zonesFromEnvelope(envelope, resolution, true),
                    resolution,
                    resolutionCalculator,
                    zoneAttribute);
        }
    }

    private Filter getFilterFrom(Iterator<Zone> zones) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(FF.property(zoneAttribute.getLocalName()));
        while (zones.hasNext()) {
            expressions.add(FF.literal(zones.next().getId()));
        }
        if (expressions.size() == 1) return Filter.EXCLUDE;
        Function inFunction = FF.function("in", expressions.toArray(new Expression[expressions.size()]));
        return FF.equal(inFunction, FF.literal(Boolean.TRUE), false);
    }

    /**
     * Maps a list of zones and a target resolution to an optimized equivalent for a database only having zone ids and
     * resolution fields.
     *
     * @param zones
     * @param resolution
     * @param zoneAttribute
     * @return
     */
    public static Filter getFilterFrom(
            DGGSInstance<?> dggs,
            Iterator<Zone> zones,
            int resolution,
            DGGSResolutionCalculator resolutionCalculator,
            AttributeDescriptor zoneAttribute) {
        List<Filter> filters = new ArrayList<>();
        List<Expression> inExpressions = new ArrayList<>();
        inExpressions.add(FF.property(zoneAttribute.getLocalName()));
        while (zones.hasNext()) {
            Zone zone = zones.next();
            // exact match
            if (zone.getResolution() == resolution) {
                inExpressions.add(buildZoneLiteral(dggs, zoneAttribute, zone.getId()));
            } else { // parent match
                Filter childFilter = buildChildFilter(dggs, zone.getId(), resolution, zoneAttribute);
                filters.add(childFilter);
            }
        }

        if (!filters.isEmpty()) {
            Or or = FF.or(new ArrayList<>(filters));
            filters.clear();
            filters.add(or);
        }
        if (inExpressions.size() > 1) {
            Function inFunction = FF.function("in", inExpressions.toArray(new Expression[inExpressions.size()]));
            Filter directChildMatches = FF.equal(inFunction, FF.literal(Boolean.TRUE), false);
            filters.add(directChildMatches);
        }

        Filter resolutionFilter = (resolutionCalculator == null || !resolutionCalculator.hasFixedResolution())
                ? FF.equal(FF.property(DGGSStore.RESOLUTION), FF.literal(resolution), true)
                : Filter.INCLUDE;

        if (filters.isEmpty()) {
            return resolutionFilter;
        } else if (filters.size() == 1) {
            return FF.and(filters.get(0), resolutionFilter);
        } else {
            return FF.and(FF.or(filters), resolutionFilter);
        }
    }

    /**
     * Builds a literal expression for the zone identifier, converting to the appropriate type if needed.
     *
     * @param dggs
     * @param zoneAttribute
     * @param zoneIdText
     * @return
     */
    private static Expression buildZoneLiteral(
            DGGSInstance<?> dggs, AttributeDescriptor zoneAttribute, String zoneIdText) {

        Class<?> binding = zoneAttribute.getType().getBinding();

        // String / text column: keep String ID
        if (String.class.isAssignableFrom(binding) || CharSequence.class.isAssignableFrom(binding)) {
            return FF.literal(zoneIdText);
        }

        // Numeric column:  convert to the DGGS numeric ID (e.g., Long for H3)
        if (Number.class.isAssignableFrom(binding)) {
            Number numericId = parseNumericZoneId(dggs, zoneIdText);
            return FF.literal(numericId);
        }

        // Fallback: safest is to stick with String
        return FF.literal(zoneIdText);
    }

    private static <I> Number parseNumericZoneId(DGGSInstance<I> dggs, String zoneIdText) {
        I parsed = dggs.parseId(zoneIdText);

        if (parsed instanceof Number n) {
            return n;
        }

        throw new IllegalArgumentException(
                "Zone attribute is numeric but DGGSInstance ID type is not Number: " + dggs.idType());
    }

    private static <I> Filter buildChildFilter(
            DGGSInstance<I> dggs, String id, int resolution, AttributeDescriptor zoneAttribute) {

        I zoneId = dggs.parseId(id);
        return dggs.getChildFilter(FF, zoneId, resolution, false, zoneAttribute);
    }
}
