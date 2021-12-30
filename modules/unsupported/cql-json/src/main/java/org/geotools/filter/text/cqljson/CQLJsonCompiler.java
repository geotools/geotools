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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.filter.text.commons.ICompiler;
import org.geotools.filter.text.commons.IToken;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cqljson.model.After;
import org.geotools.filter.text.cqljson.model.And;
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
import org.geotools.filter.text.cqljson.model.Gt;
import org.geotools.filter.text.cqljson.model.Gte;
import org.geotools.filter.text.cqljson.model.In;
import org.geotools.filter.text.cqljson.model.Intersects;
import org.geotools.filter.text.cqljson.model.Like;
import org.geotools.filter.text.cqljson.model.Lt;
import org.geotools.filter.text.cqljson.model.Lte;
import org.geotools.filter.text.cqljson.model.Meets;
import org.geotools.filter.text.cqljson.model.Metby;
import org.geotools.filter.text.cqljson.model.Or;
import org.geotools.filter.text.cqljson.model.Overlappedby;
import org.geotools.filter.text.cqljson.model.Overlaps;
import org.geotools.filter.text.cqljson.model.Predicates;
import org.geotools.filter.text.cqljson.model.TContains;
import org.geotools.filter.text.cqljson.model.Tequals;
import org.geotools.filter.text.cqljson.model.Touches;
import org.geotools.filter.text.cqljson.model.Toverlaps;
import org.geotools.filter.text.cqljson.model.Within;
import org.geotools.filter.text.generated.parsers.ParseException;
import org.geotools.util.logging.Logging;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

public class CQLJsonCompiler implements ICompiler {

    static final Logger LOGGER = Logging.getLogger(CQLJsonCompiler.class);

    /** cql expression to compile */
    private final String source;

    private CQLJsonFilterBuilder builder;

    private Filter filter;

    /** new instance of CQL Compiler */
    public CQLJsonCompiler(final String cqlSource, final FilterFactory2 filterFactory) {

        assert filterFactory != null : "filterFactory cannot be null";

        this.source = cqlSource;
        this.builder = new CQLJsonFilterBuilder(filterFactory);
    }

    @Override
    public String getSource() {
        return source;
    }

    /**
     * Goes through the Predicate beans to find non-null properties
     *
     * @param predicates CQL Predicates
     * @return CQL Predicate Properties that are non-null
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object getNonNull(Object predicates)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (predicates instanceof Predicates) {
            for (PropertyDescriptor propertyDescriptor :
                    Introspector.getBeanInfo(Predicates.class).getPropertyDescriptors()) {
                Method method = propertyDescriptor.getReadMethod();
                if (method.invoke(predicates) != null) {
                    if (method.invoke(predicates) instanceof Predicates)
                        return method.invoke(predicates);
                    else if (method.invoke(predicates) instanceof AbstractList) {
                        return method.invoke(predicates);
                    }
                }
            }
        } else if (predicates instanceof AbstractList) {
            return predicates;
        }
        return null;
    }

    /**
     * Compiles Filter from predicates source json
     *
     * @throws CQLException If there is an issue Parsing the predicates
     */
    @Override
    public void compileFilter() throws CQLException {

        Predicates predicates = CQLJson.jsonToPredicates(source);
        try {
            filter = convertToFilter(predicates);
        } catch (ParseException e) {
            throw new CQLException(e.getMessage());
        }
    }

    /**
     * Get compiled filter
     *
     * @return Filter
     * @throws CQLException
     */
    @Override
    public Filter getFilter() throws CQLException {
        return filter;
    }

    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public void compileExpression() throws CQLException {
        // Is this really necessary for CQL-JSON??
    }
    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public Expression getExpression() throws CQLException {
        return null;
    }
    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public void compileFilterList() throws CQLException {
        // Is this really necessary for CQL-JSON??
    }
    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public List<Filter> getFilterList() throws CQLException {
        return null;
    }
    /**
     * Is this needed for CQL-JSON?
     *
     * @throws CQLException
     */
    @Override
    public IToken getTokenInPosition(int position) {
        return null;
    }

    // --------------Internal-----------------------------------------------
    private Filter convertToFilter(Object predicates) throws ParseException {
        Filter out = null;
        Object processedNotNull = null;
        try {
            processedNotNull = getNonNull(predicates);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new CQLException(e.getMessage());
        }
        if (processedNotNull != null) {
            switch (processedNotNull.getClass().getSimpleName()) {
                case "And":
                    And and = (And) processedNotNull;
                    List<Filter> filters =
                            and.stream()
                                    .map(
                                            a -> {
                                                try {
                                                    return convertToFilter(a);
                                                } catch (ParseException e) {
                                                    LOGGER.log(Level.SEVERE, "", e);
                                                }
                                                return (Filter) a;
                                            })
                                    .collect(Collectors.toList());
                    out = builder.convertAnd(filters);
                    break;
                case "Or":
                    Or or = (Or) processedNotNull;
                    List<Filter> filtersOr =
                            or.stream()
                                    .map(
                                            a -> {
                                                try {
                                                    return convertToFilter(a);
                                                } catch (ParseException e) {
                                                    LOGGER.log(Level.SEVERE, "", e);
                                                }
                                                return (Filter) a;
                                            })
                                    .collect(Collectors.toList());
                    out = builder.convertOr(filtersOr);
                    break;
                case "Predicates": // This is Not, which can apply to any predicates
                    Filter notFilter = convertToFilter(processedNotNull);
                    out = builder.convertNot(notFilter);
                    break;
                case "Lte":
                    out = builder.convertLte((Lte) processedNotNull);
                    break;
                default:
                case "Eq":
                    out = builder.convertEq((Eq) processedNotNull);
                    break;
                case "Lt":
                    out = builder.convertLt((Lt) processedNotNull);
                    break;
                case "Gte":
                    out = builder.convertGte((Gte) processedNotNull);
                    break;
                case "Gt":
                    out = builder.convertGt((Gt) processedNotNull);
                    break;
                case "Between":
                    out = builder.convertBetween((Between) processedNotNull);
                    break;
                case "Like":
                    out = builder.convertLike((Like) processedNotNull);
                    break;
                case "In":
                    out = builder.convertIn((In) processedNotNull);
                    break;
                case "Equals":
                    try {
                        out = builder.convertEquals((Equals) processedNotNull);
                    } catch (ParseException e) {
                        throw new CQLException(e.getMessage());
                    }
                    break;
                case "Disjoint":
                    try {
                        out = builder.convertDisjoint((Disjoint) processedNotNull);
                    } catch (ParseException e) {
                        throw new CQLException(e.getMessage());
                    }
                    break;
                case "Touches":
                    try {
                        out = builder.convertTouches((Touches) processedNotNull);
                    } catch (ParseException e) {
                        throw new CQLException(e.getMessage());
                    }
                    break;
                case "Within":
                    try {
                        out = builder.convertWithin((Within) processedNotNull);
                    } catch (ParseException e) {
                        throw new CQLException(e.getMessage());
                    }
                    break;
                case "Overlaps":
                    try {
                        out = builder.convertOverlaps((Overlaps) processedNotNull);
                    } catch (ParseException e) {
                        throw new CQLException(e.getMessage());
                    }
                    break;
                case "Crosses":
                    try {
                        out = builder.convertCrosses((Crosses) processedNotNull);
                    } catch (ParseException e) {
                        throw new CQLException(e.getMessage());
                    }
                    break;
                case "Intersects":
                    try {
                        out = builder.convertIntersects((Intersects) processedNotNull);
                    } catch (ParseException e) {
                        throw new CQLException(e.getMessage());
                    }
                    break;
                case "Contains":
                    try {
                        out = builder.convertContains((Contains) processedNotNull);
                    } catch (ParseException e) {
                        throw new CQLException(e.getMessage());
                    }
                    break;
                case "After":
                    out = builder.convertAfter((After) processedNotNull);
                    break;
                case "Before":
                    out = builder.convertBefore((Before) processedNotNull);
                    break;
                case "Begins":
                    out = builder.convertBegins((Begins) processedNotNull);
                    break;
                case "Begunby":
                    out = builder.convertBegunby((Begunby) processedNotNull);
                    break;
                case "Tcontains":
                    out = builder.convertTContains((TContains) processedNotNull);
                    break;
                case "During":
                    out = builder.convertDuring((During) processedNotNull);
                    break;
                case "Endedby":
                    out = builder.convertEndedBy((Endedby) processedNotNull);
                    break;
                case "Ends":
                    out = builder.convertEnds((Ends) processedNotNull);
                    break;
                case "Tequals":
                    out = builder.convertTEquals((Tequals) processedNotNull);
                    break;
                case "Meets":
                    out = builder.convertMeets((Meets) processedNotNull);
                    break;
                case "Metby":
                    out = builder.convertMetBy((Metby) processedNotNull);
                    break;
                case "Toverlaps":
                    out = builder.convertTOverlaps((Toverlaps) processedNotNull);
                    break;
                case "Overlappedby":
                    out = builder.convertOverlappedBy((Overlappedby) processedNotNull);
                    break;
            }
        }
        return out;
    }
}
