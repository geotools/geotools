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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.identity.FeatureId;
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
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(FilterDOMParser.class);

    /** Factory to create filters. */
    private static final FilterFactory2 FILTER_FACTORY =
            CommonFactoryFinder.getFilterFactory2(null);

    /** Number of children in a between filter. */
    private static final int NUM_BETWEEN_CHILDREN = 3;

    /** Map of comparison names to their filter types. */
    private static java.util.Map<String, Integer> comparisions = new HashMap<>();

    /** Map of spatial filter names to their filter types. */
    private static java.util.Map<String, Integer> spatial = new HashMap<>();

    /** Map of logical filter names to their filter types. */
    private static java.util.Map<String, Integer> logical = new HashMap<>();

    static {
        comparisions.put("PropertyIsEqualTo", Integer.valueOf(FilterType.COMPARE_EQUALS));
        comparisions.put("PropertyIsNotEqualTo", Integer.valueOf(FilterType.COMPARE_NOT_EQUALS));
        comparisions.put("PropertyIsGreaterThan", Integer.valueOf(FilterType.COMPARE_GREATER_THAN));
        comparisions.put(
                "PropertyIsGreaterThanOrEqualTo",
                Integer.valueOf(FilterType.COMPARE_GREATER_THAN_EQUAL));
        comparisions.put("PropertyIsLessThan", Integer.valueOf(FilterType.COMPARE_LESS_THAN));
        comparisions.put(
                "PropertyIsLessThanOrEqualTo", Integer.valueOf(FilterType.COMPARE_LESS_THAN_EQUAL));
        comparisions.put("PropertyIsLike", Integer.valueOf(AbstractFilter.LIKE));
        comparisions.put("PropertyIsNull", Integer.valueOf(AbstractFilter.NULL));
        comparisions.put("PropertyIsBetween", Integer.valueOf(FilterType.BETWEEN));
        comparisions.put("FeatureId", Integer.valueOf(AbstractFilter.FID));

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
    public static Filter parseFilter(Node root) {

        final ExpressionDOMParser expressionDOMParser = new ExpressionDOMParser(FILTER_FACTORY);

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

        if (comparisions.containsKey(childName))
            return parseComparision(expressionDOMParser, child, childName);
        else if (spatial.containsKey(childName)) {
            return parseSpatial(expressionDOMParser, child, childName);
        } else if (logical.containsKey(childName)) {
            return parseLogical(child, childName);
        }

        LOGGER.warning("unknown filter " + root);

        return null;
    }

    /**
     * @param child
     * @param childName
     * @return
     */
    public static Filter parseLogical(Node child, String childName) {
        LOGGER.finest("a logical filter " + childName);

        try {
            List<Filter> children = getChildren(child);

            if (childName.equals("And")) return FILTER_FACTORY.and(children);
            else if (childName.equals("Or")) return FILTER_FACTORY.or(children);
            else if (childName.equals("Not")) {
                if (children.size() != 1)
                    throw new IllegalFilterException(
                            "Filter negation can be " + "applied to one and only one child filter");
                return FILTER_FACTORY.not(children.get(0));
            } else {
                throw new RuntimeException(
                        "Logical filter, but not And, Or, Not? " + "This should not happen");
            }
        } catch (IllegalFilterException ife) {
            LOGGER.warning("Unable to build filter: " + ife);

            return null;
        }
    }

    /**
     * @param child
     * @return
     */
    public static List<Filter> getChildren(Node child) {
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
        return children;
    }

    /**
     * @param expressionDOMParser
     * @param child
     * @param childName
     * @return
     */
    public static Filter parseSpatial(
            final ExpressionDOMParser expressionDOMParser, Node child, String childName) {
        LOGGER.finest("a spatial filter " + childName);

        Filter ret = null;
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

            String valueName =
                    (value.getLocalName() != null) ? value.getLocalName() : value.getNodeName();
            if (valueName.indexOf(':') != -1) {
                // the DOM parser was not properly set to handle namespaces...
                valueName = valueName.substring(valueName.indexOf(':') + 1);
            }

            // need to cache the next node as the following parsing trick will
            // ruin the DOM hierarchy
            Node nextNode = value.getNextSibling();
            Expression right;
            if (!(valueName.equalsIgnoreCase("Literal")
                    || valueName.equalsIgnoreCase("propertyname"))) {
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
                    ret = FILTER_FACTORY.equal(left, right);
                    break;
                case FilterType.GEOMETRY_DISJOINT:
                    ret = FILTER_FACTORY.disjoint(left, right);
                    break;
                case FilterType.GEOMETRY_INTERSECTS:
                    ret = FILTER_FACTORY.intersects(left, right);
                    break;
                case FilterType.GEOMETRY_TOUCHES:
                    ret = FILTER_FACTORY.touches(left, right);
                    break;
                case FilterType.GEOMETRY_CROSSES:
                    ret = FILTER_FACTORY.crosses(left, right);
                    break;
                case FilterType.GEOMETRY_WITHIN:
                    ret = FILTER_FACTORY.within(left, right);
                    break;
                case FilterType.GEOMETRY_CONTAINS:
                    ret = FILTER_FACTORY.contains(left, right);
                    break;
                case FilterType.GEOMETRY_OVERLAPS:
                    ret = FILTER_FACTORY.overlaps(left, right);
                    break;
                case FilterType.GEOMETRY_DWITHIN:
                    ret = parseDWithin(left, nextNode, right, units);
                    break;
                case FilterType.GEOMETRY_BEYOND:
                    ret = parseBeyond(left, nextNode, right, units);
                    break;
                case FilterType.GEOMETRY_BBOX:
                    ret = parseGeometryBBox(left, right);
                    break;
                default:
                    LOGGER.warning("Unable to build filter: " + childName);
                    ret = null;
            }
        } catch (IllegalFilterException ife) {
            LOGGER.warning("Unable to build filter: " + ife);

            return null;
        }
        return ret;
    }

    /**
     * @param left
     * @param nextNode
     * @param right
     * @param units
     * @return
     */
    public static Filter parseDWithin(
            Expression left, Node nextNode, Expression right, String units) {
        Filter ret;
        Node value;
        double distance;
        String nodeName;
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
                    "Parsing DWithin, was expecting to find Distance but found "
                            + value.getLocalName());
        }
        distance = Double.parseDouble(value.getTextContent());
        if (value.getAttributes().getNamedItem("units") != null)
            units = value.getAttributes().getNamedItem("units").getTextContent();
        ret = FILTER_FACTORY.dwithin(left, right, distance, units);
        return ret;
    }

    /**
     * @param left
     * @param nextNode
     * @param right
     * @param units
     * @return
     */
    public static Filter parseBeyond(
            Expression left, Node nextNode, Expression right, String units) {
        Filter ret;
        Node value;
        double distance;
        String nodeName;
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
                    "Parsing Beyond, was expecting to find Distance but found "
                            + value.getLocalName());
        }
        distance = Double.parseDouble(value.getTextContent());
        if (value.getAttributes().getNamedItem("units") != null)
            units = value.getAttributes().getNamedItem("units").getTextContent();
        ret = FILTER_FACTORY.beyond(left, right, distance, units);
        return ret;
    }

    /**
     * @param left
     * @param right
     * @return
     */
    public static Filter parseGeometryBBox(Expression left, Expression right) {
        Filter ret;
        Literal literal = (Literal) right;
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
        ret = FILTER_FACTORY.bbox(left, bbox);
        return ret;
    }

    /**
     * @param expressionDOMParser
     * @param child
     * @param childName
     * @return
     */
    public static Filter parseComparision(
            final ExpressionDOMParser expressionDOMParser, Node child, String childName) {

        LOGGER.finer("a comparision filter " + childName);

        Filter ret;
        try {
            short type = comparisions.get(childName).shortValue();
            LOGGER.finer("type is " + type);

            if (type == AbstractFilter.FID) {
                ret = parseFIDFilter(child);
            } else if (type == AbstractFilter.BETWEEN) {
                ret = parseBetween(expressionDOMParser, child);
            } else if (type == AbstractFilter.LIKE) {
                ret = parseLike(expressionDOMParser, child);
            } else if (type == AbstractFilter.NULL) {
                ret = parseNullFilter(child);
            }

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
                    ret = FILTER_FACTORY.equals(left, right);
                    break;
                case FilterType.COMPARE_GREATER_THAN:
                    ret = FILTER_FACTORY.greater(left, right);
                    break;
                case FilterType.COMPARE_GREATER_THAN_EQUAL:
                    ret = FILTER_FACTORY.greaterOrEqual(left, right);
                    break;
                case FilterType.COMPARE_LESS_THAN:
                    ret = FILTER_FACTORY.less(left, right);
                    break;
                case FilterType.COMPARE_LESS_THAN_EQUAL:
                    ret = FILTER_FACTORY.lessOrEqual(left, right);
                    break;
                case FilterType.COMPARE_NOT_EQUALS:
                    ret = FILTER_FACTORY.notEqual(left, right, true);
                    break;
                default:
                    LOGGER.warning("Unable to build filter for " + childName);
                    ret = null;
            }
        } catch (IllegalFilterException ife) {
            LOGGER.warning("Unable to build filter: " + ife);
            return null;
        }

        return ret;
    }

    /**
     * @param child
     * @return
     */
    public static Filter parseFIDFilter(Node child) {
        Filter ret;
        Set<FeatureId> ids = new HashSet<>();
        Element fidElement = (Element) child;
        ids.add(FILTER_FACTORY.featureId(fidElement.getAttribute("fid")));

        Node sibling = fidElement.getNextSibling();

        while (sibling != null) {
            LOGGER.finer("Parsing another FidFilter");

            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                fidElement = (Element) sibling;

                String fidElementName = fidElement.getLocalName();
                if (fidElementName == null) {
                    fidElementName = fidElement.getNodeName(); // HACK ?
                }
                if (fidElementName.indexOf(':') != -1) {
                    // the DOM parser wasnt properly set to handle namespaces...
                    fidElementName = fidElementName.substring(fidElementName.indexOf(':') + 1);
                }
                if ("FeatureId".equals(fidElementName)) {
                    ids.add(FILTER_FACTORY.featureId(fidElement.getAttribute("fid")));
                }
            }

            sibling = sibling.getNextSibling();
        }

        ret = FILTER_FACTORY.id(ids);
        return ret;
    }

    /**
     * @param expressionDOMParser
     * @param child
     * @return
     */
    public static Filter parseBetween(final ExpressionDOMParser expressionDOMParser, Node child) {
        Filter ret;
        NodeList kids = child.getChildNodes();

        if (kids.getLength() < NUM_BETWEEN_CHILDREN) {
            throw new IllegalFilterException(
                    "wrong number of children in Between filter: expected 3 got "
                            + kids.getLength());
        }

        Node value = child.getFirstChild();

        while (value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }

        LOGGER.finer("add middle value -> " + value + "<-");
        Expression middle = expressionDOMParser.expression(value);
        Expression lower = null;
        Expression upper = null;

        for (int i = 0; i < kids.getLength(); i++) {
            Node kid = kids.item(i);

            String kidName = (kid.getLocalName() != null) ? kid.getLocalName() : kid.getNodeName();
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

        ret = FILTER_FACTORY.between(middle, lower, upper);
        return ret;
    }

    /**
     * @param expressionDOMParser
     * @param child
     * @return
     */
    public static Filter parseLike(final ExpressionDOMParser expressionDOMParser, Node child) {
        Filter ret = null;
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

            String res = (kid.getLocalName() != null) ? kid.getLocalName() : kid.getNodeName();
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
            String res = (kid.getLocalName() != null) ? kid.getLocalName() : kid.getNodeName();
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

            LOGGER.finer(
                    "Building like filter "
                            + value.toString()
                            + "\n"
                            + pattern
                            + " "
                            + wildcard
                            + " "
                            + single
                            + " "
                            + escape);

            ret = FILTER_FACTORY.like(value, pattern, wildcard, single, escape);
        }

        return ret;
    }

    /**
     * Parses a null filter from a node known to be a null node.
     *
     * @param nullNode the PropertyIsNull node.
     * @return a null filter of the expression contained in null node.
     */
    private static PropertyIsNull parseNullFilter(Node nullNode) throws IllegalFilterException {

        final ExpressionDOMParser expressionDOMParser = new ExpressionDOMParser(FILTER_FACTORY);

        LOGGER.finest("parsing null node: " + nullNode);

        Node value = nullNode.getFirstChild();

        while (value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }

        LOGGER.finest("add null value -> " + value + "<-");
        Expression expr = expressionDOMParser.expression(value);

        return FILTER_FACTORY.isNull(expr);
    }
}
