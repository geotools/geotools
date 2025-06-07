/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.spatial.BBOXImpl;
import org.geotools.filter.spatial.BeyondImpl;
import org.geotools.filter.spatial.ContainsImpl;
import org.geotools.filter.spatial.CrossesImpl;
import org.geotools.filter.spatial.DWithinImpl;
import org.geotools.filter.spatial.DisjointImpl;
import org.geotools.filter.spatial.EqualsImpl;
import org.geotools.filter.spatial.IntersectsImpl;
import org.geotools.filter.spatial.OverlapsImpl;
import org.geotools.filter.spatial.TouchesImpl;
import org.geotools.filter.spatial.WithinImpl;
import org.xml.sax.Attributes;

/**
 * Creates filters from FilterFilter, which reads in a SAX stream and passes the appropriate messages here.
 *
 * @author Rob Hranac, Vision for New York<br>
 * @author Chris Holmes, TOPP
 * @version $Id$
 */
public class FilterSAXParser {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FilterSAXParser.class);

    /** The number of attributes to be found in a like filter */
    private static final int NUM_LIKE_ATTS = 3;

    /** The filter being currently constructed */
    private Filter curFilter = null;

    /** The current completion state of the filter */
    private String curState = "uninitialized";

    /** The short representation of this type of filter */
    private short filterType;

    /** factory for creating filters. */
    @SuppressWarnings({"PMD.UnusedPrivateField", "UnusedVariable"})
    private FilterFactory ff;

    /** the Attributes of the filter (only applicable to LIKE filters, I think) */
    private Map<String, Object> attributes = new HashMap<>();

    /** Constructor which flags the operator as between. */
    public FilterSAXParser() {
        this(CommonFactoryFinder.getFilterFactory());
    }
    /** Constructor injdection */
    public FilterSAXParser(FilterFactory factory) {
        ff = factory;
    }

    /** Setter injection */
    public void setFilterFactory(FilterFactory factory) {
        ff = factory;
    }
    /**
     * Handles all incoming generic string 'messages,' including a message to create the filter, based on the XML tag
     * that represents the start of the filter.
     *
     * @param filterType The string from the SAX filter.
     * @throws IllegalFilterException Filter is illegal.
     */
    public void start(short filterType) throws IllegalFilterException {
        LOGGER.finest("starting filter type " + filterType);

        if ((filterType == AbstractFilter.FID) && !curState.equals("fid")) {
            LOGGER.finer("creating the FID filter");
            curFilter = new FidFilterImpl(Collections.emptySet());
        } else if (AbstractFilter.isGeometryDistanceFilter(filterType)) {
            switch (filterType) {
                case FilterType.GEOMETRY_BEYOND:
                    curFilter = new BeyondImpl(null, null);
                    break;
                case FilterType.GEOMETRY_DWITHIN:
                    curFilter = new DWithinImpl(null, null);
                    break;
                default:
                    throw new IllegalFilterException("Not one of the accepted spatial filter types.");
            }
        } else if (AbstractFilter.isGeometryFilter(filterType)) {
            switch (filterType) {
                case FilterType.GEOMETRY_EQUALS:
                    curFilter = new EqualsImpl(null, null);
                    break;
                case FilterType.GEOMETRY_DISJOINT:
                    curFilter = new DisjointImpl(null, null);
                    break;
                case FilterType.GEOMETRY_DWITHIN:
                    curFilter = new DWithinImpl(null, null);
                    break;
                case FilterType.GEOMETRY_INTERSECTS:
                    curFilter = new IntersectsImpl(null, null);
                    break;
                case FilterType.GEOMETRY_CROSSES:
                    curFilter = new CrossesImpl(null, null);
                    break;
                case FilterType.GEOMETRY_WITHIN:
                    curFilter = new WithinImpl(null, null);
                    break;
                case FilterType.GEOMETRY_CONTAINS:
                    curFilter = new ContainsImpl(null, null);
                    break;
                case FilterType.GEOMETRY_OVERLAPS:
                    curFilter = new OverlapsImpl(null, null);
                    break;
                case FilterType.GEOMETRY_BEYOND:
                    curFilter = new BeyondImpl(null, null);
                    break;
                case FilterType.GEOMETRY_BBOX:
                    curFilter = new BBOXImpl(null, null);
                    break;
                case FilterType.GEOMETRY_TOUCHES:
                    curFilter = new TouchesImpl(null, null);
                    break;
                default:
                    throw new IllegalFilterException("Not one of the accepted spatial filter types.");
            }
        } else if (filterType == AbstractFilter.BETWEEN) {
            curFilter = new IsBetweenImpl(null, null, null);
        } else if (filterType == AbstractFilter.NULL) {
            curFilter = new NullFilterImpl(Expression.NIL);
        } else if (filterType == AbstractFilter.LIKE) {
            curFilter = new LikeFilterImpl();
        } else if (AbstractFilter.isCompareFilter(filterType)) {
            switch (filterType) {
                case FilterType.COMPARE_EQUALS:
                    curFilter = new IsEqualsToImpl(null, null);
                    break;
                case FilterType.COMPARE_NOT_EQUALS:
                    curFilter = new IsNotEqualToImpl(null, null);
                    break;
                case FilterType.COMPARE_GREATER_THAN:
                    curFilter = new IsGreaterThanImpl(null, null);
                    break;
                case FilterType.COMPARE_GREATER_THAN_EQUAL:
                    curFilter = new IsGreaterThanOrEqualToImpl(null, null);
                    break;
                case FilterType.COMPARE_LESS_THAN:
                    curFilter = new IsLessThenImpl(null, null);
                    break;
                case FilterType.COMPARE_LESS_THAN_EQUAL:
                    curFilter = new IsLessThenOrEqualToImpl(null, null);
                    break;
                case FilterType.BETWEEN:
                    curFilter = new IsBetweenImpl(null, null, null);
                    break;
                default:
                    throw new IllegalFilterException("Must be one of <,<=,==,>,>=,<>");
            }
        } else {
            throw new IllegalFilterException("Filter start with invalid type: " + filterType);
        }

        curState = setInitialState(filterType);
        this.filterType = filterType;

        attributes = new HashMap<>();
    }

    /**
     * Handles all incoming generic string 'messages,' including a message to create the filter, based on the XML tag
     * that represents the start of the filter.
     *
     * @param message The string from the SAX filter.
     * @throws IllegalFilterException Filter is illegal.
     */
    public void value(String message) throws IllegalFilterException {}

    /**
     * Adds the passed in expression to the current filter. Generally created by the ExpressionSAXParser.
     *
     * @param expression The value of the attribute for comparison.
     * @throws IllegalFilterException if the expression does not match what the current filter is expecting.
     * @task REVISIT: split this method up.
     */
    public void expression(Expression expression) throws IllegalFilterException {
        // Handle all filter compare states and expressions
        if (filterType == FilterType.BETWEEN) {
            if (curState.equals("attribute")) {
                ((IsBetweenImpl) curFilter).setExpression(expression);
                curState = "LowerBoundary";
            } else if (curState.equals("LowerBoundary")) {
                ((BinaryComparisonAbstract) curFilter).setExpression1(expression);
                curState = "UpperBoundary";
            } else if (curState.equals("UpperBoundary")) {
                ((BinaryComparisonAbstract) curFilter).setExpression2(expression);
                curState = "complete";
            } else {
                throw new IllegalFilterException("Got expression for Between Filter in illegal state: " + curState);
            }
        } else if (AbstractFilter.isCompareFilter(filterType)) {
            if (curState.equals("leftValue")) {
                ((BinaryComparisonAbstract) curFilter).setExpression1(expression);
                curState = "rightValue";
            } else if (curState.equals("rightValue")) {
                ((BinaryComparisonAbstract) curFilter).setExpression2(expression);
                curState = "complete";
            } else {
                throw new IllegalFilterException("Got expression for Compare Filter in illegal state: " + curState);
            }
        } else if (filterType == FilterType.NULL) {
            if (curState.equals("attribute")) {
                ((NullFilterImpl) curFilter).setExpression(expression);
                curState = "complete";
            } else {
                throw new IllegalFilterException("Got expression for Null Filter in illegal state: " + curState);
            }
        } else if (AbstractFilter.isGeometryFilter(filterType)) {
            if (curState.equals("leftValue")) {
                ((BinaryComparisonAbstract) curFilter).setExpression1(expression);
                curState = "rightValue";
            } else if (curState.equals("rightValue")) {
                ((BinaryComparisonAbstract) curFilter).setExpression2(expression);

                if (AbstractFilter.isGeometryDistanceFilter(filterType)) {
                    curState = "distance";
                } else {
                    curState = "complete";
                }

                LOGGER.finer("expression called on geometry, curState = " + curState);
            } else {
                throw new IllegalFilterException("Got expression for Geometry Filter in illegal state: " + curState);
            }
        } else if (filterType == AbstractFilter.LIKE) {
            if (curState.equals("attribute")) {
                ((LikeFilterImpl) curFilter).setExpression(expression);
                curState = "pattern";
            } else if (curState.equals("pattern")) {
                if (attributes.size() < NUM_LIKE_ATTS) {
                    throw new IllegalFilterException("Got wrong number of attributes (expecting minimum 3): "
                            + attributes.size()
                            + "\n"
                            + attributes);
                }

                String wildcard = (String) attributes.get("wildCard");

                if ((wildcard == null) || (wildcard.length() != 1)) {
                    throw new IllegalFilterException(
                            "like filter -- required attribute 'wildCard' missing or not exactly 1 char long.  Capitalization?");
                }
                String singleChar = (String) attributes.get("singleChar");

                if ((singleChar == null) || (singleChar.length() != 1)) {
                    throw new IllegalFilterException(
                            "like filter -- required attribute 'singleChar' missing  or not exactly 1 char long.  Capitalization?");
                }

                String escapeChar = (String) attributes.get("escape");
                if (escapeChar == null) // totally against spec, but...
                escapeChar = (String) attributes.get("escapeChar");

                if ((escapeChar == null) || (escapeChar.length() != 1)) {
                    throw new IllegalFilterException(
                            "like filter -- required attribute 'escape' missing  or not exactly 1 char long.  Capitalization?");
                }

                LOGGER.fine("escape char is " + escapeChar);

                String matchCase = (String) attributes.get("matchCase");
                if (matchCase != null) {
                    ((LikeFilterImpl) curFilter).setMatchingCase(Boolean.parseBoolean(matchCase));
                }

                if (expression instanceof Literal) {
                    Literal literal = (Literal) expression;
                    Object value = literal.getValue();
                    if (value != null && value instanceof String) {
                        String pattern = (String) value;

                        ((LikeFilterImpl) curFilter).setLiteral(pattern);
                        ((LikeFilterImpl) curFilter).setWildCard(wildcard);
                        ((LikeFilterImpl) curFilter).setSingleChar(singleChar);
                        ((LikeFilterImpl) curFilter).setEscape(escapeChar);
                    } else {
                        throw new ClassCastException("Pattern Literal must be a string:" + value);
                    }
                } else {
                    throw new ClassCastException("Pattern must be a literal String");
                }
                curState = "complete";
            } else {
                throw new IllegalFilterException("Got expression for Like Filter in illegal state: " + curState);
            }
        }

        LOGGER.finer("current state (end): " + curState);
    }

    /**
     * Creates the filter held in the parser.
     *
     * @return The current filter to be created by this parser.
     * @throws IllegalFilterException If called before the filter is in a complete state.
     */
    public Filter create() throws IllegalFilterException {
        if (isComplete()) {
            LOGGER.finer("complete called, state = " + curState);
            curState = "complete"; // added by cholmes fid bug.

            return curFilter;
        } else {
            throw new IllegalFilterException(
                    "Got to the end state of an incomplete filter, current" + " state is " + curState);
        }
    }

    /**
     * Sets the state that shall be expected next based on the filterType. So if a between, null or like is the
     * currentFilter then attribute should be next, if an fid filter then fid should be next. If it's a comparison,
     * geometry or not, then leftValue should be next.
     *
     * @param filterType An AbstractFilter short of the filter type.
     * @return the string of what state should come next.
     * @throws IllegalFilterException if the filter type is not recognized.
     */
    private static String setInitialState(short filterType) throws IllegalFilterException {
        if ((filterType == AbstractFilter.BETWEEN)
                || (filterType == AbstractFilter.NULL)
                || (filterType == AbstractFilter.LIKE)) {
            return "attribute";
        } else if ((filterType == AbstractFilter.FID)) {
            return "fid";
        } else if (AbstractFilter.isCompareFilter(filterType) || AbstractFilter.isGeometryFilter(filterType)) {
            return "leftValue";
        } else {
            throw new IllegalFilterException("Filter type: " + filterType + " is not recognized");
        }
    }

    /**
     * This sets the distance for a GeometryDistanceFilter. It currently ignores the units, and attempts to convert the
     * distance to a double.
     *
     * @param distance the distance - should be a string of a double.
     * @param units a reference to a units dictionary.
     * @throws IllegalFilterException if the distance string can not be converted to a double.
     * @task TODO: Implement units, probably with org.geotools.units package and a special distance class in the filter
     *     package. It would be nice if the distance class could get any type of units, like it would handle the
     *     conversion.
     */
    public void setDistance(String distance, String units) throws IllegalFilterException {
        LOGGER.finer("set distance called, current state is " + curState);

        if (curState.equals("distance")) {
            try {
                double distDouble = Double.parseDouble(distance);
                ((CartesianDistanceFilter) curFilter).setDistance(distDouble);
                curState = "complete";
            } catch (NumberFormatException nfe) {
                throw new IllegalFilterException("could not parse distance: " + distance + " to a double");
            }
        } else {
            throw new IllegalFilterException("Got distance for Geometry Distance Filter in illegal state: "
                    + curState
                    + ", geometry and property should be set first");
        }
    }

    /**
     * Sets the filter attributes. Called when attributes are encountered by the filter filter. Puts them in a hash map
     * by thier name and value.
     *
     * @param atts the attributes to set.
     */
    public void setAttributes(Attributes atts) {
        LOGGER.finer("got attribute: " + atts.getLocalName(0) + ", " + atts.getValue(0));
        LOGGER.finer("current state: " + curState);

        if (curState.equals("fid")) {
            LOGGER.finer("is a fid");
            ((FidFilterImpl) curFilter).addFid(atts.getValue(0));
            LOGGER.finer("added fid");
        } else {
            for (int i = 0; i < atts.getLength(); i++) {
                this.attributes.put(atts.getLocalName(i), atts.getValue(i));
            }
        }
    }

    /**
     * Indicates that the filter is in a complete state (ready to be created.)
     *
     * @return <tt>true</tt> if the current state is either complete or fid
     */
    private boolean isComplete() {
        return (curState.equals("complete") || curState.equals("fid"));
    }
}
