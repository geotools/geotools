/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on 10 July 2002, 17:14
 */
package org.geotools.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A dom based parser to build filters as per OGC 01-067
 *
 * @author Ian Turton, CCG
 * @author Niels Charlier
 * @version $Id$
 * @task TODO: split this class up into multiple methods.
 */
public final class FilterDOMParser {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FilterDOMParser.class);

    /** Factory to create filters. */
    private static final FilterFactory FILTER_FACT = CommonFactoryFinder.getFilterFactory(null);

    /** Number of children in a between filter. */
    private static final int NUM_BETWEEN_CHILDREN = 3;

    /** Map of comparison names to their filter types. */
    private static java.util.Map<String, Integer> comparisons = new HashMap<>();

    /** Map of spatial filter names to their filter types. */
    private static java.util.Map<String, Integer> spatial = new HashMap<>();

    /** Map of logical filter names to their filter types. */
    private static java.util.Map<String, Integer> logical = new HashMap<>();

    static {
        comparisons.put("PropertyIsEqualTo", Integer.valueOf(FilterType.COMPARE_EQUALS));
        comparisons.put("PropertyIsNotEqualTo", Integer.valueOf(FilterType.COMPARE_NOT_EQUALS));
        comparisons.put("PropertyIsGreaterThan", Integer.valueOf(FilterType.COMPARE_GREATER_THAN));
        comparisons.put("PropertyIsGreaterThanOrEqualTo", Integer.valueOf(FilterType.COMPARE_GREATER_THAN_EQUAL));
        comparisons.put("PropertyIsLessThan", Integer.valueOf(FilterType.COMPARE_LESS_THAN));
        comparisons.put("PropertyIsLessThanOrEqualTo", Integer.valueOf(FilterType.COMPARE_LESS_THAN_EQUAL));
        comparisons.put("PropertyIsLike", Integer.valueOf(AbstractFilter.LIKE));
        comparisons.put("PropertyIsNull", Integer.valueOf(AbstractFilter.NULL));
        comparisons.put("PropertyIsBetween", Integer.valueOf(FilterType.BETWEEN));
        comparisons.put("FeatureId", Integer.valueOf(AbstractFilter.FID));

        spatial.put("Equals", Integer.valueOf(AbstractFilter.GEOMETRY_EQUALS));
        spatial.put("Disjoint", Integer.valueOf(AbstractFilter.GEOMETRY_DISJOINT));
        spatial.put("Intersects", Integer.valueOf(FilterType.GEOMETRY_INTERSECTS));
        spatial.put("Touches", Integer.valueOf(AbstractFilter.GEOMETRY_TOUCHES));
        spatial.put("Crosses", Integer.valueOf(AbstractFilter.GEOMETRY_CROSSES));
        spatial.put("Within", Integer.valueOf(AbstractFilter.GEOMETRY_WITHIN));
        spatial.put("Contains", Integer.valueOf(AbstractFilter.GEOMETRY_CONTAINS));
        spatial.put("Overlaps", Integer.valueOf(AbstractFilter.GEOMETRY_OVERLAPS));
        spatial.put("BBOX", Integer.valueOf(AbstractFilter.GEOMETRY_BBOX));

        // Beyond and DWithin not handled well
        spatial.put("Beyond", Integer.valueOf(AbstractFilter.GEOMETRY_BEYOND));
        spatial.put("DWithin", Integer.valueOf(AbstractFilter.GEOMETRY_DWITHIN));

        logical.put("And", Integer.valueOf(AbstractFilter.LOGIC_AND));
        logical.put("Or", Integer.valueOf(AbstractFilter.LOGIC_OR));
        logical.put("Not", Integer.valueOf(AbstractFilter.LOGIC_NOT));
    }

    /** Creates a new instance of FilterXMLParser */
    private FilterDOMParser() {}

    /**
     * Parses the filter using DOM.
     *
     * @param root a dom node containing FILTER as the root element.
     * @task TODO: split up this insanely long method.
     */
    public static org.geotools.api.filter.Filter parseFilter(Node root) {

        // NodeList children = root.getChildNodes();
        // LOGGER.finest("children "+children);
        if (root == null || root.getNodeType() != Node.ELEMENT_NODE) {
            LOGGER.finest("bad node input ");

            return null;
        }

        LOGGER.finest("processing root " + root.getLocalName() + " " + root.getNodeName());

        Node child = root;
        String childName = child.getLocalName();
        if (childName == null) {
            childName = child.getNodeName(); // HACK ?
        }
        if (childName.indexOf(':') != -1) {
            // the DOM parser wasnt properly set to handle namespaces...
            childName = childName.substring(childName.indexOf(':') + 1);
        }

        LOGGER.finest("looking up " + childName);

        if (comparisons.containsKey(childName)) {
            return parseComparisonFilter(child, childName);
        } else if (spatial.containsKey(childName)) {
            return parseSpatialFilter(child, childName);
        } else if (logical.containsKey(childName)) {
            return parseLogicalFilter(child, childName);
        }

        LOGGER.warning("unknown filter " + root);

        return null;
    }

    private static Filter parseComparisonFilter(Node child, String childName) {
        LOGGER.finer("a comparision filter " + childName);
        ExpressionDOMParser expressionDOMParser = new ExpressionDOMParser(FILTER_FACT);

        // boolean like = false;
        // boolean between = false;
        try {
            short type = comparisons.get(childName).shortValue();
            // CompareFilter filter = null;
            LOGGER.finer("type is " + type);

            if (type == AbstractFilter.FID) {
                return parseFidFilter((Element) child);
            } else if (type == AbstractFilter.BETWEEN) {
                return parseBetweenFilter(expressionDOMParser, child);
            } else if (type == AbstractFilter.LIKE) {
                return parseLikeFilter(expressionDOMParser, child);
            } else if (type == AbstractFilter.NULL) {
                return parseNullFilter(child);
            }

            return parseSimpleComparison(expressionDOMParser, child, childName, type);
        } catch (IllegalFilterException ife) {
            LOGGER.warning("Unable to build filter: " + ife);
            return null;
        }
    }

    private static Filter parseLogicalFilter(Node child, String childName) {
        LOGGER.finest("a logical filter " + childName);

        try {
            List<Filter> children = new ArrayList<>();
            NodeList map = child.getChildNodes();

            for (int i = 0; i < map.getLength(); i++) {
                Node kid = map.item(i);

                if (kid == null || kid.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                LOGGER.finest("adding to logic filter " + kid.getLocalName());
                children.add(parseFilter(kid));
            }

            if (childName.equals("And")) return FILTER_FACT.and(children);
            else if (childName.equals("Or")) return FILTER_FACT.or(children);
            else if (childName.equals("Not")) {
                if (children.size() != 1)
                    throw new IllegalFilterException(
                            "Filter negation can be " + "applied to one and only one child filter");
                return FILTER_FACT.not(children.get(0));
            } else {
                throw new RuntimeException("Logical filter, but not And, Or, Not? " + "This should not happen");
            }
        } catch (IllegalFilterException ife) {
            LOGGER.warning("Unable to build filter: " + ife);

            return null;
        }
    }

    private static BinaryComparisonOperator parseSimpleComparison(
            ExpressionDOMParser expressionDOMParser, Node child, String childName, short type) {
        // find and parse left and right values
        Node value = child.getFirstChild();

        while (value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }

        LOGGER.finest("add left value -> " + value + "<-");
        Expression left = expressionDOMParser.expression(value);
        value = value.getNextSibling();

        while (value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }

        LOGGER.finest("add right value -> " + value + "<-");
        Expression right = expressionDOMParser.expression(value);

        switch (type) {
            case FilterType.COMPARE_EQUALS:
                return FILTER_FACT.equals(left, right);

            case FilterType.COMPARE_GREATER_THAN:
                return FILTER_FACT.greater(left, right);

            case FilterType.COMPARE_GREATER_THAN_EQUAL:
                return FILTER_FACT.greaterOrEqual(left, right);

            case FilterType.COMPARE_LESS_THAN:
                return FILTER_FACT.less(left, right);

            case FilterType.COMPARE_LESS_THAN_EQUAL:
                return FILTER_FACT.lessOrEqual(left, right);

            case FilterType.COMPARE_NOT_EQUALS:
                return FILTER_FACT.notEqual(left, right, true);

            default:
                LOGGER.warning("Unable to build filter for " + childName);
                return null;
        }
    }

    private static Id parseFidFilter(Element child) {
        Set<FeatureId> ids = new HashSet<>();
        Element fidElement = child;
        ids.add(FILTER_FACT.featureId(fidElement.getAttribute("fid")));

        Node sibling = fidElement.getNextSibling();

        while (sibling != null) {
            LOGGER.finer("Parsing another FidFilter");

            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                Element fidElement1;
                fidElement1 = (Element) sibling;

                String fidElementName = fidElement1.getLocalName();
                if (fidElementName == null) {
                    fidElementName = fidElement1.getNodeName(); // HACK ?
                }
                if (fidElementName.indexOf(':') != -1) {
                    // the DOM parser wasnt properly set to handle namespaces...
                    fidElementName = fidElementName.substring(fidElementName.indexOf(':') + 1);
                }
                if ("FeatureId".equals(fidElementName)) {
                    ids.add(FILTER_FACT.featureId(fidElement1.getAttribute("fid")));
                }
            }

            sibling = sibling.getNextSibling();
        }

        return FILTER_FACT.id(ids);
    }

    private static PropertyIsLike parseLikeFilter(ExpressionDOMParser expressionDOMParser, Node child) {
        String wildcard = null;
        String single = null;
        String escape = null;
        String pattern = null;
        Expression value = null;
        NodeList map = child.getChildNodes();

        int parsedExpressions = 0;
        for (int i = 0; i < map.getLength(); i++) {
            Node kid = map.item(i);

            if (kid == null || kid.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            String res = kid.getLocalName() != null ? kid.getLocalName() : kid.getNodeName();
            if (res.indexOf(':') != -1) {
                // the DOM parser wasnt properly set to handle namespaces...
                res = res.substring(res.indexOf(':') + 1);
            }

            if (parsedExpressions == 0) {
                value = expressionDOMParser.expression(kid);
                parsedExpressions++;
            } else if (parsedExpressions == 1 && res.equalsIgnoreCase("Literal")) {
                pattern = expressionDOMParser.expression(kid).toString();
                parsedExpressions++;
            }
        }

        NamedNodeMap kids = child.getAttributes();

        for (int i = 0; i < kids.getLength(); i++) {
            Node kid = kids.item(i);

            // if(kid == null || kid.getNodeType() != Node.ELEMENT_NODE) continue;
            String res = kid.getLocalName() != null ? kid.getLocalName() : kid.getNodeName();
            if (res.indexOf(':') != -1) {
                // the DOM parser wasnt properly set to handle namespaces...
                res = res.substring(res.indexOf(':') + 1);
            }
            if (res.equalsIgnoreCase("wildCard")) {
                wildcard = kid.getNodeValue();
            }

            if (res.equalsIgnoreCase("singleChar")) {
                single = kid.getNodeValue();
            }

            if (res.equalsIgnoreCase("escapeChar") || res.equalsIgnoreCase("escape")) {
                escape = kid.getNodeValue();
            }
        }

        if (!(wildcard == null || single == null || escape == null || pattern == null)) {
            // LikeFilter lfilter = FILTER_FACT.createLikeFilter();
            LOGGER.finer("Building like filter "
                    + value.toString()
                    + "\n"
                    + pattern
                    + " "
                    + wildcard
                    + " "
                    + single
                    + " "
                    + escape);
            // lfilter.setValue(value);
            // lfilter.setPattern(pattern, wildcard, single, escape);

            return FILTER_FACT.like(value, pattern, wildcard, single, escape);
        }

        LOGGER.finer("Problem building like filter\n" + pattern + " " + wildcard + " " + single + " " + escape);

        return null;
    }

    private static PropertyIsBetween parseBetweenFilter(ExpressionDOMParser expressionDOMParser, Node child) {
        // BetweenFilter bfilter = FILTER_FACT.createBetweenFilter();

        NodeList kids = child.getChildNodes();

        if (kids.getLength() < NUM_BETWEEN_CHILDREN) {
            throw new IllegalFilterException(
                    "wrong number of children in Between filter: expected 3 got " + kids.getLength());
        }

        Node value = child.getFirstChild();

        while (value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }

        // first expression
        // value = kid.getFirstChild();
        // while(value.getNodeType() != Node.ELEMENT_NODE )
        // value = value.getNextSibling();
        LOGGER.finer("add middle value -> " + value + "<-");
        Expression middle = expressionDOMParser.expression(value);
        Expression lower = null;
        Expression upper = null;

        for (int i = 0; i < kids.getLength(); i++) {
            Node kid = kids.item(i);

            String kidName = kid.getLocalName() != null ? kid.getLocalName() : kid.getNodeName();
            if (kidName.indexOf(':') != -1) {
                // the DOM parser wasnt properly set to handle namespaces...
                kidName = kidName.substring(kidName.indexOf(':') + 1);
            }
            if (kidName.equalsIgnoreCase("LowerBoundary")) {
                value = kid.getFirstChild();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add left value -> " + value + "<-");
                lower = expressionDOMParser.expression(value);
            }

            if (kidName.equalsIgnoreCase("UpperBoundary")) {
                value = kid.getFirstChild();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add right value -> " + value + "<-");
                upper = expressionDOMParser.expression(value);
            }
        }

        return FILTER_FACT.between(middle, lower, upper);
    }

    /**
     * Parses a null filter from a node known to be a null node.
     *
     * @param nullNode the PropertyIsNull node.
     * @return a null filter of the expression contained in null node.
     */
    private static PropertyIsNull parseNullFilter(Node nullNode) throws IllegalFilterException {

        final ExpressionDOMParser expressionDOMParser = new ExpressionDOMParser(FILTER_FACT);

        LOGGER.finest("parsing null node: " + nullNode);

        Node value = nullNode.getFirstChild();

        while (value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }

        LOGGER.finest("add null value -> " + value + "<-");
        Expression expr = expressionDOMParser.expression(value);

        return FILTER_FACT.isNull(expr);
    }

    private static BinarySpatialOperator parseSpatialFilter(Node child, String childName) {
        LOGGER.finest("a spatial filter " + childName);
        ExpressionDOMParser expressionDOMParser = new ExpressionDOMParser(FILTER_FACT);

        try {
            short type = spatial.get(childName).shortValue();
            //  GeometryFilter filter = FILTER_FACT.createGeometryFilter(type);
            Node value = child.getFirstChild();

            while (value.getNodeType() != Node.ELEMENT_NODE) {
                value = value.getNextSibling();
            }

            LOGGER.finest("add left value -> " + value + "<-");
            Expression left = expressionDOMParser.expression(value);
            value = value.getNextSibling();

            while (value.getNodeType() != Node.ELEMENT_NODE) {
                value = value.getNextSibling();
            }

            LOGGER.finest("add right value -> " + value + "<-");

            String valueName = value.getLocalName() != null ? value.getLocalName() : value.getNodeName();
            if (valueName.indexOf(':') != -1) {
                // the DOM parser was not properly set to handle namespaces...
                valueName = valueName.substring(valueName.indexOf(':') + 1);
            }

            // need to cache the next node as the following parsing trick will
            // ruin the DOM hierarchy
            Node nextNode = value.getNextSibling();
            Expression right;
            if (!(valueName.equalsIgnoreCase("Literal") || valueName.equalsIgnoreCase("propertyname"))) {
                Element literal = value.getOwnerDocument().createElement("literal");

                literal.appendChild(value);
                LOGGER.finest("Built new literal " + literal);
                right = expressionDOMParser.expression(literal);
            } else {
                right = expressionDOMParser.expression(value);
            }

            String units = null;
            switch (type) {
                case FilterType.GEOMETRY_EQUALS:
                    return FILTER_FACT.equal(left, right);

                case FilterType.GEOMETRY_DISJOINT:
                    return FILTER_FACT.disjoint(left, right);

                case FilterType.GEOMETRY_INTERSECTS:
                    return FILTER_FACT.intersects(left, right);

                case FilterType.GEOMETRY_TOUCHES:
                    return FILTER_FACT.touches(left, right);

                case FilterType.GEOMETRY_CROSSES:
                    return FILTER_FACT.crosses(left, right);

                case FilterType.GEOMETRY_WITHIN:
                    return FILTER_FACT.within(left, right);

                case FilterType.GEOMETRY_CONTAINS:
                    return FILTER_FACT.contains(left, right);

                case FilterType.GEOMETRY_OVERLAPS:
                    return FILTER_FACT.overlaps(left, right);

                case FilterType.GEOMETRY_DWITHIN:
                    return parseWithinFilter(left, nextNode, right, units);

                case FilterType.GEOMETRY_BEYOND:
                    return parseBeyondFilter(left, nextNode, right, units);

                case FilterType.GEOMETRY_BBOX:
                    return parseBBOXFilter(left, (Literal) right);
                default:
                    LOGGER.warning("Unable to build filter: " + childName);
                    return null;
            }
        } catch (IllegalFilterException ife) {
            LOGGER.warning("Unable to build filter: " + ife);

            return null;
        }
    }

    private static BBOX parseBBOXFilter(Expression left, Literal right) {
        Literal literal = right;
        Object obj = literal.getValue();
        ReferencedEnvelope bbox = null;
        if (obj instanceof Geometry) {
            bbox = JTS.toEnvelope((Geometry) obj);
        } else if (obj instanceof ReferencedEnvelope) {
            bbox = (ReferencedEnvelope) obj;
        } else if (obj instanceof Envelope) {
            // no clue about CRS / srsName so we should guess
            bbox = new ReferencedEnvelope((Envelope) obj, null);
        }
        return FILTER_FACT.bbox(left, bbox);
    }

    private static Beyond parseBeyondFilter(Expression left, Node nextNode, Expression right, String units) {
        double distance;
        String nodeName;
        Node value;
        value = nextNode;
        while (value != null && value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }
        if (value == null) {
            throw new IllegalFilterException("Beyond is missing the Distance element");
        }
        nodeName = value.getNodeName();
        if (nodeName.indexOf(':') > 0) {
            nodeName = nodeName.substring(nodeName.indexOf(":") + 1);
        }
        if (!"Distance".equals(nodeName)) {
            throw new IllegalFilterException(
                    "Parsing Beyond, was expecting to find Distance but found " + value.getLocalName());
        }
        distance = Double.parseDouble(value.getTextContent());
        if (value.getAttributes().getNamedItem("units") != null)
            units = value.getAttributes().getNamedItem("units").getTextContent();
        return FILTER_FACT.beyond(left, right, distance, units);
    }

    private static DWithin parseWithinFilter(Expression left, Node nextNode, Expression right, String units) {
        double distance;
        String nodeName;
        Node value;
        value = nextNode;
        while (value != null && value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }
        if (value == null) {
            throw new IllegalFilterException("DWithin is missing the Distance element");
        }

        nodeName = value.getNodeName();
        if (nodeName.indexOf(':') > 0) {
            nodeName = nodeName.substring(nodeName.indexOf(":") + 1);
        }
        if (!"Distance".equals(nodeName)) {
            throw new IllegalFilterException(
                    "Parsing DWithin, was expecting to find Distance but found " + value.getLocalName());
        }
        distance = Double.parseDouble(value.getTextContent());
        if (value.getAttributes().getNamedItem("units") != null)
            units = value.getAttributes().getNamedItem("units").getTextContent();
        return FILTER_FACT.dwithin(left, right, distance, units);
    }
}
