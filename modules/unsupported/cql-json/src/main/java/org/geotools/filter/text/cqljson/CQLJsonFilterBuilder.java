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

package org.geotools.filter.text.cqljson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.filter.text.cqljson.model.After;
import org.geotools.filter.text.cqljson.model.Anyinteracts;
import org.geotools.filter.text.cqljson.model.Before;
import org.geotools.filter.text.cqljson.model.Begins;
import org.geotools.filter.text.cqljson.model.Begunby;
import org.geotools.filter.text.cqljson.model.Between;
import org.geotools.filter.text.cqljson.model.Contains;
import org.geotools.filter.text.cqljson.model.Crosses;
import org.geotools.filter.text.cqljson.model.Disjoint;
import org.geotools.filter.text.cqljson.model.During;
import org.geotools.filter.text.cqljson.model.Endedby;
import org.geotools.filter.text.cqljson.model.Ends;
import org.geotools.filter.text.cqljson.model.Eq;
import org.geotools.filter.text.cqljson.model.Equals;
import org.geotools.filter.text.cqljson.model.FunctionObjectArgument;
import org.geotools.filter.text.cqljson.model.Gt;
import org.geotools.filter.text.cqljson.model.Gte;
import org.geotools.filter.text.cqljson.model.In;
import org.geotools.filter.text.cqljson.model.Intersects;
import org.geotools.filter.text.cqljson.model.Like;
import org.geotools.filter.text.cqljson.model.Lt;
import org.geotools.filter.text.cqljson.model.Lte;
import org.geotools.filter.text.cqljson.model.Meets;
import org.geotools.filter.text.cqljson.model.Metby;
import org.geotools.filter.text.cqljson.model.Overlappedby;
import org.geotools.filter.text.cqljson.model.Overlaps;
import org.geotools.filter.text.cqljson.model.TContains;
import org.geotools.filter.text.cqljson.model.Tequals;
import org.geotools.filter.text.cqljson.model.Tintersects;
import org.geotools.filter.text.cqljson.model.Touches;
import org.geotools.filter.text.cqljson.model.Toverlaps;
import org.geotools.filter.text.cqljson.model.Within;
import org.geotools.filter.text.generated.parsers.ParseException;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

final class CQLJsonFilterBuilder {

    /** New instance of CQLJsonFilterBuilder */
    public CQLJsonFilterBuilder(final FilterFactory2 filterFactory) {
        this.filterFactory = filterFactory;
    }

    private final FilterFactory2 filterFactory;

    /**
     * Convert CQL AND to Geotools Filter
     *
     * @param filters CQL
     * @return GeoTools Filter
     */
    public Filter convertAnd(List<Filter> filters) {
        return filterFactory.and(filters);
    }
    /**
     * Convert CQL OR to Geotools Filter
     *
     * @param filters CQL
     * @return GeoTools Filter
     */
    public Filter convertOr(List<Filter> filters) {
        return filterFactory.or(filters);
    }
    /**
     * Convert CQL NOT to Geotools Filter
     *
     * @param filter CQL
     * @return GeoTools Filter
     */
    public Filter convertNot(Filter filter) {
        return filterFactory.not(filter);
    }
    /**
     * Convert CQL AFTER to Geotools Filter
     *
     * @param after CQL
     * @return GeoTools Filter
     */
    public Filter convertAfter(After after) throws ParseException {
        Expression value = null;
        if (after.getFunction() != null) {
            value = convertFunction(after.getFunction());
        } else {
            value = filterFactory.literal(after.getValue());
        }
        return filterFactory.after(filterFactory.property(after.getProperty()), value);
    }
    /**
     * Convert CQL ANY to Geotools Filter
     *
     * @param anyinteracts CQL
     * @return GeoTools Filter
     */
    public Filter convertAnyInteracts(Anyinteracts anyinteracts) throws ParseException {
        Expression value = null;
        if (anyinteracts.getFunction() != null) {
            value = convertFunction(anyinteracts.getFunction());
        } else {
            value = filterFactory.literal(anyinteracts.getValue());
        }
        return filterFactory.anyInteracts(
                filterFactory.property(anyinteracts.getProperty()), value);
    }
    /**
     * Convert CQL BEFORE to Geotools Filter
     *
     * @param before CQL
     * @return GeoTools Filter
     */
    public Filter convertBefore(Before before) throws ParseException {
        Expression value = null;
        if (before.getFunction() != null) {
            value = convertFunction(before.getFunction());
        } else {
            value = filterFactory.literal(before.getValue());
        }
        return filterFactory.before(filterFactory.property(before.getProperty()), value);
    }
    /**
     * Convert CQL BEGINS to Geotools Filter
     *
     * @param begins CQL
     * @return GeoTools Filter
     */
    public Filter convertBegins(Begins begins) throws ParseException {
        Expression value = null;
        if (begins.getFunction() != null) {
            value = convertFunction(begins.getFunction());
        } else {
            value = filterFactory.literal(begins.getValue());
        }
        return filterFactory.begins(filterFactory.property(begins.getProperty()), value);
    }
    /**
     * Convert CQL BEGUNBY to Geotools Filter
     *
     * @param begunby CQL
     * @return GeoTools Filter
     */
    public Filter convertBegunby(Begunby begunby) throws ParseException {
        Expression value = null;
        if (begunby.getFunction() != null) {
            value = convertFunction(begunby.getFunction());
        } else {
            value = filterFactory.literal(begunby.getValue());
        }
        return filterFactory.begunBy(filterFactory.property(begunby.getProperty()), value);
    }
    /**
     * Convert CQL BETWEEN to Geotools Filter
     *
     * @param between CQL
     * @return GeoTools Filter
     */
    public Filter convertBetween(Between between) {
        return filterFactory.between(
                filterFactory.property(between.getProperty()),
                filterFactory.literal(between.getUpper()),
                filterFactory.literal(between.getLower()));
    }
    /**
     * Convert CQL CONTAINS to Geotools Filter
     *
     * @param contains CQL
     * @return GeoTools Filter
     */
    public Filter convertContains(Contains contains) throws ParseException {
        assert contains.getFunction() == null
                : "GeoTools Geometry filters do not support functions as arguments";

        return filterFactory.contains(
                filterFactory.property(contains.getProperty()),
                toGeometry(convertToMap(contains.getValue())));
    }

    private Map<String, Object> convertToMap(Object value) {
        assert value instanceof Map
                : "Object passed to Geometry function must be a Map<String,Object>";
        Map<?, ?> valueMap = (Map<?, ?>) value;
        Map<String, Object> myInput = new HashMap<>(valueMap.size());
        for (Map.Entry<?, ?> entry : valueMap.entrySet()) {
            myInput.put((String) entry.getKey(), entry.getValue());
        }
        return myInput;
    }

    private Literal toGeometry(Map<String, Object> value) throws ParseException {
        return filterFactory.literal(MapToOpenGISGeomUtil.parseMapToGeometry(value));
    }

    /**
     * Convert CQL DURING to Geotools Filter
     *
     * @param during CQL
     * @return GeoTools Filter
     */
    public Filter convertDuring(During during) throws ParseException {
        Expression value = null;
        if (during.getFunction() != null) {
            value = convertFunction(during.getFunction());
        } else {
            value = filterFactory.literal(during.getValue());
        }
        return filterFactory.during(filterFactory.property(during.getProperty()), value);
    }
    /**
     * Convert CQL ENDEDBY to Geotools Filter
     *
     * @param endedby CQL
     * @return GeoTools Filter
     */
    public Filter convertEndedBy(Endedby endedby) throws ParseException {
        Expression value = null;
        if (endedby.getFunction() != null) {
            value = convertFunction(endedby.getFunction());
        } else {
            value = filterFactory.literal(endedby.getValue());
        }
        return filterFactory.endedBy(filterFactory.property(endedby.getProperty()), value);
    }
    /**
     * Convert CQL ENDS to Geotools Filter
     *
     * @param ends CQL
     * @return GeoTools Filter
     */
    public Filter convertEnds(Ends ends) throws ParseException {
        Expression value = null;
        if (ends.getFunction() != null) {
            value = convertFunction(ends.getFunction());
        } else {
            value = filterFactory.literal(ends.getValue());
        }
        return filterFactory.ends(filterFactory.property(ends.getProperty()), value);
    }
    /**
     * Convert CQL EQ to Geotools Filter
     *
     * @param eq CQL
     * @return GeoTools Filter
     */
    public Filter convertEq(Eq eq) {
        return filterFactory.equals(
                filterFactory.property(eq.getProperty()), filterFactory.literal(eq.getValue()));
    }
    /**
     * Convert CQL EQUALS to Geotools Filter
     *
     * @param equals CQL
     * @return GeoTools Filter
     */
    public Filter convertEquals(Equals equals) throws ParseException {
        assert equals.getFunction() == null
                : "GeoTools Geometry filters do not support functions as arguments";
        return filterFactory.equals(
                filterFactory.property(equals.getProperty()),
                (toGeometry(convertToMap(equals.getValue()))));
    }
    /**
     * Convert CQL GT to Geotools Filter
     *
     * @param gt CQL
     * @return GeoTools Filter
     */
    public Filter convertGt(Gt gt) {
        return filterFactory.greater(
                filterFactory.property(gt.getProperty()), filterFactory.literal(gt.getValue()));
    }
    /**
     * Convert CQL GTE to Geotools Filter
     *
     * @param gte CQL
     * @return GeoTools Filter
     */
    public Filter convertGte(Gte gte) {
        return filterFactory.greaterOrEqual(
                filterFactory.property(gte.getProperty()), filterFactory.literal(gte.getValue()));
    }
    /**
     * Convert CQL In to Geotools Filter
     *
     * @param in CQL
     * @return GeoTools Filter
     */
    public Filter convertIn(In in) {
        PropertyName property = filterFactory.property(in.getProperty());

        org.opengis.filter.expression.Expression[] args =
                new org.opengis.filter.expression.Expression[1];
        args[0] = filterFactory.literal(property);

        Function function = filterFactory.function("PropertyExists", args);
        Literal literalTrue = filterFactory.literal(Boolean.TRUE);

        return filterFactory.equals(function, literalTrue);
    }
    /**
     * Convert CQL INTERSECTS to Geotools Filter
     *
     * @param intersects CQL
     * @return GeoTools Filter
     */
    public Filter convertIntersects(Intersects intersects) throws ParseException {
        assert intersects.getFunction() == null
                : "GeoTools Geometry filters do not support functions as arguments";
        return filterFactory.intersects(
                filterFactory.property(intersects.getProperty()),
                (toGeometry(convertToMap(intersects.getValue()))));
    }
    /**
     * Convert CQL LIKE to Geotools Filter
     *
     * @param like CQL
     * @return GeoTools Filter
     */
    public Filter convertLike(Like like) {
        return filterFactory.like(
                filterFactory.property(like.getProperty()),
                like.getValue().toString(),
                like.getWildcard(),
                like.getSingleChar(),
                like.getEscape());
    }
    /**
     * Convert CQL LT to Geotools Filter
     *
     * @param lt CQL
     * @return GeoTools Filter
     */
    public Filter convertLt(Lt lt) {
        return filterFactory.less(
                filterFactory.property(lt.getProperty()), filterFactory.literal(lt.getValue()));
    }

    /**
     * Convert CQL LTE to Geotools Filter
     *
     * @param lte CQL
     * @return GeoTools Filter
     */
    public Filter convertLte(Lte lte) {
        return filterFactory.lessOrEqual(
                filterFactory.property(lte.getProperty()), filterFactory.literal(lte.getValue()));
    }
    /**
     * Convert CQL MEETS to Geotools Filter
     *
     * @param meets CQL
     * @return GeoTools Filter
     */
    public Filter convertMeets(Meets meets) throws ParseException {
        Expression value = null;
        if (meets.getFunction() != null) {
            value = convertFunction(meets.getFunction());
        } else {
            value = filterFactory.literal(meets.getValue());
        }
        return filterFactory.meets(filterFactory.property(meets.getProperty()), value);
    }
    /**
     * Convert CQL METBY to Geotools Filter
     *
     * @param metby CQL
     * @return GeoTools Filter
     */
    public Filter convertMetBy(Metby metby) throws ParseException {
        Expression value = null;
        if (metby.getFunction() != null) {
            value = convertFunction(metby.getFunction());
        } else {
            value = filterFactory.literal(metby.getValue());
        }
        return filterFactory.metBy(filterFactory.property(metby.getProperty()), value);
    }
    /**
     * Convert CQL OVERLAPPEDBY to Geotools Filter
     *
     * @param overlappedby CQL
     * @return GeoTools Filter
     */
    public Filter convertOverlappedBy(Overlappedby overlappedby) throws ParseException {
        Expression value = null;
        if (overlappedby.getFunction() != null) {
            value = convertFunction(overlappedby.getFunction());
        } else {
            value = filterFactory.literal(overlappedby.getValue());
        }
        return filterFactory.overlappedBy(
                filterFactory.property(overlappedby.getProperty()), value);
    }
    /**
     * Convert CQL OVERLAPS to Geotools Filter
     *
     * @param overlaps CQL
     * @return GeoTools Filter
     */
    public Filter convertOverlaps(Overlaps overlaps) throws ParseException {
        assert overlaps.getFunction() == null
                : "GeoTools Geometry filters do not support functions as arguments";
        return filterFactory.overlaps(
                filterFactory.property(overlaps.getProperty()),
                (toGeometry(convertToMap(overlaps.getValue()))));
    }
    /**
     * Convert CQL TCONTAINS to Geotools Filter
     *
     * @param tContains CQL
     * @return GeoTools Filter
     */
    public Filter convertTContains(TContains tContains) throws ParseException {
        Expression value = null;
        if (tContains.getFunction() != null) {
            value = convertFunction(tContains.getFunction());
        } else {
            value = filterFactory.literal(tContains.getValue());
        }
        return filterFactory.tcontains(filterFactory.property(tContains.getProperty()), value);
    }
    /**
     * Convert CQL TEQUALS to Geotools Filter
     *
     * @param tequals CQL
     * @return GeoTools Filter
     */
    public Filter convertTEquals(Tequals tequals) throws ParseException {
        Expression value = null;
        if (tequals.getFunction() != null) {
            value = convertFunction(tequals.getFunction());
        } else {
            value = filterFactory.literal(tequals.getValue());
        }
        return filterFactory.tequals(filterFactory.property(tequals.getProperty()), value);
    }
    /**
     * Convert CQL TINTERSECTS to Geotools Filter
     *
     * @param tintersects CQL
     * @return GeoTools Filter
     */
    public Filter convertTIntersects(Tintersects tintersects) throws ParseException {
        Expression value = null;
        if (tintersects.getFunction() != null) {
            value = convertFunction(tintersects.getFunction());
        } else {
            value = filterFactory.literal(tintersects.getValue());
        }
        return filterFactory.anyInteracts(filterFactory.property(tintersects.getProperty()), value);
    }
    /**
     * Convert CQL TOUCHES to Geotools Filter
     *
     * @param touches CQL
     * @return GeoTools Filter
     */
    public Filter convertTouches(Touches touches) throws ParseException {
        assert touches.getFunction() == null
                : "GeoTools Geometry filters do not support functions as arguments";
        return filterFactory.touches(
                filterFactory.property(touches.getProperty()),
                (toGeometry(convertToMap(touches.getValue()))));
    }
    /**
     * Convert CQL TOVERLAPS to Geotools Filter
     *
     * @param toverlaps CQL
     * @return GeoTools Filter
     */
    public Filter convertTOverlaps(Toverlaps toverlaps) throws ParseException {
        Expression value = null;
        if (toverlaps.getFunction() != null) {
            value = convertFunction(toverlaps.getFunction());
        } else {
            value = filterFactory.literal(toverlaps.getValue());
        }
        return filterFactory.toverlaps(filterFactory.property(toverlaps.getProperty()), value);
    }
    /**
     * Convert CQL WITHIN to Geotools Filter
     *
     * @param within CQL
     * @return GeoTools Filter
     */
    public Filter convertWithin(Within within) throws ParseException {
        assert within.getFunction() == null
                : "GeoTools Geometry filters do not support functions as arguments";
        return filterFactory.within(
                filterFactory.property(within.getProperty()),
                (toGeometry(convertToMap(within.getValue()))));
    }
    /**
     * Convert CQL DISJOINT to Geotools Filter
     *
     * @param disjoint CQL
     * @return GeoTools Filter
     */
    public Filter convertDisjoint(Disjoint disjoint) throws ParseException {
        assert disjoint.getFunction() == null
                : "GeoTools Geometry filters do not support functions as arguments";
        return filterFactory.disjoint(
                filterFactory.property(disjoint.getProperty()),
                (toGeometry(convertToMap(disjoint.getValue()))));
    }
    /**
     * Convert CQL CROSSES to Geotools Filter
     *
     * @param crosses CQL
     * @return GeoTools Filter
     */
    public Filter convertCrosses(Crosses crosses) throws ParseException {
        assert crosses.getFunction() == null
                : "GeoTools Geometry filters do not support functions as arguments";
        return filterFactory.disjoint(
                filterFactory.property(crosses.getProperty()),
                (toGeometry(convertToMap(crosses.getValue()))));
    }

    private Expression convertFunction(org.geotools.filter.text.cqljson.model.Function function)
            throws ParseException {
        List<Expression> functionArgs = functionArgsToExpressions(function.getArguments());
        return filterFactory.function(function.getName(), functionArgs.toArray(new Expression[0]));
    }

    private List<Expression> functionArgsToExpressions(List<FunctionObjectArgument> arguments)
            throws ParseException {
        List<Expression> expressions = new ArrayList<>();
        for (FunctionObjectArgument functionObjectArgument : arguments) {
            if (functionObjectArgument.getAdd() != null) {
                expressions.add(
                        filterFactory.add(
                                filterFactory.literal(
                                        functionObjectArgument.getAdd().getProperty()),
                                filterFactory.literal(functionObjectArgument.getAdd().getValue())));
            }
            if (functionObjectArgument.getSub() != null) {
                expressions.add(
                        filterFactory.subtract(
                                filterFactory.literal(
                                        functionObjectArgument.getAdd().getProperty()),
                                filterFactory.literal(functionObjectArgument.getAdd().getValue())));
            }
            if (functionObjectArgument.getMul() != null) {
                expressions.add(
                        filterFactory.multiply(
                                filterFactory.literal(
                                        functionObjectArgument.getAdd().getProperty()),
                                filterFactory.literal(functionObjectArgument.getAdd().getValue())));
            }
            if (functionObjectArgument.getDiv() != null) {
                expressions.add(
                        filterFactory.divide(
                                filterFactory.literal(
                                        functionObjectArgument.getAdd().getProperty()),
                                filterFactory.literal(functionObjectArgument.getAdd().getValue())));
            }
            if (functionObjectArgument.getBbox() != null) {
                Geometry geo =
                        MapToOpenGISGeomUtil.parseMapToGeometry(
                                convertToMap(functionObjectArgument.getBbox()));
                double minx = geo.getEnvelope().getCoordinates()[0].x;
                double miny = geo.getEnvelope().getCoordinates()[0].y;
                double maxx = geo.getEnvelope().getCoordinates()[2].x;
                double maxy = geo.getEnvelope().getCoordinates()[2].y;
                expressions.add(
                        filterFactory
                                .bbox(
                                        functionObjectArgument.getProperty(),
                                        minx,
                                        miny,
                                        maxx,
                                        maxy,
                                        "EPSG:" + geo.getSRID())
                                .getExpression1());
            }
            if (functionObjectArgument.getGeometry() != null) {
                Geometry geo =
                        MapToOpenGISGeomUtil.parseMapToGeometry(
                                convertToMap(functionObjectArgument.getGeometry()));
                double minx = geo.getEnvelope().getCoordinates()[0].x;
                double miny = geo.getEnvelope().getCoordinates()[0].y;
                double maxx = geo.getEnvelope().getCoordinates()[2].x;
                double maxy = geo.getEnvelope().getCoordinates()[2].y;
                expressions.add(
                        filterFactory
                                .bbox(
                                        functionObjectArgument.getProperty(),
                                        minx,
                                        miny,
                                        maxx,
                                        maxy,
                                        "EPSG:" + geo.getSRID())
                                .getExpression1());
            }

            if (functionObjectArgument.getTemporalValue() != null) {
                expressions.add(filterFactory.literal(functionObjectArgument.getTemporalValue()));
            }

            if (functionObjectArgument.getFunction() != null) {
                expressions.add(convertFunction(functionObjectArgument.getFunction()));
            }
            if (functionObjectArgument.getProperty() != null) {
                expressions.add(filterFactory.property(functionObjectArgument.getProperty()));
            }
        }
        return expressions;
    }
}
