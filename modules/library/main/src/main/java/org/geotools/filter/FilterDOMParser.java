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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.identity.FeatureId;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A dom based parser to build filters as per OGC 01-067
 *
 * @author Ian Turton, CCG
 *
 * @source $URL$
 * @version $Id$
 *
 * @task TODO: split this class up into multiple methods.
 */
public final class FilterDOMParser {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /** Factory to create filters. */
    private static final FilterFactory2 FILTER_FACT = CommonFactoryFinder.getFilterFactory2(null);

    /** Number of children in a between filter. */
    private static final int NUM_BETWEEN_CHILDREN = 3;

    /** Map of comparison names to their filter types. */
    private static java.util.Map comparisions = new java.util.HashMap();

    /** Map of spatial filter names to their filter types. */
    private static java.util.Map spatial = new java.util.HashMap();

    /** Map of logical filter names to their filter types. */
    private static java.util.Map logical = new java.util.HashMap();

    static {
        comparisions.put("PropertyIsEqualTo",
            new Integer(FilterType.COMPARE_EQUALS));
        comparisions.put("PropertyIsNotEqualTo",
            new Integer(FilterType.COMPARE_NOT_EQUALS));
        comparisions.put("PropertyIsGreaterThan",
            new Integer(FilterType.COMPARE_GREATER_THAN));
        comparisions.put("PropertyIsGreaterThanOrEqualTo",
            new Integer(FilterType.COMPARE_GREATER_THAN_EQUAL));
        comparisions.put("PropertyIsLessThan",
            new Integer(FilterType.COMPARE_LESS_THAN));
        comparisions.put("PropertyIsLessThanOrEqualTo",
            new Integer(FilterType.COMPARE_LESS_THAN_EQUAL));
        comparisions.put("PropertyIsLike", new Integer(AbstractFilter.LIKE));
        comparisions.put("PropertyIsNull", new Integer(AbstractFilter.NULL));
        comparisions.put("PropertyIsBetween",
            new Integer(Filter.BETWEEN));
        comparisions.put("FeatureId", new Integer(AbstractFilter.FID));

        spatial.put("Equals", new Integer(AbstractFilter.GEOMETRY_EQUALS));
        spatial.put("Disjoint", new Integer(AbstractFilter.GEOMETRY_DISJOINT));
        spatial.put("Intersects",
            new Integer(Filter.GEOMETRY_INTERSECTS));
        spatial.put("Touches", new Integer(AbstractFilter.GEOMETRY_TOUCHES));
        spatial.put("Crosses", new Integer(AbstractFilter.GEOMETRY_CROSSES));
        spatial.put("Within", new Integer(AbstractFilter.GEOMETRY_WITHIN));
        spatial.put("Contains", new Integer(AbstractFilter.GEOMETRY_CONTAINS));
        spatial.put("Overlaps", new Integer(AbstractFilter.GEOMETRY_OVERLAPS));
        spatial.put("BBOX", new Integer(AbstractFilter.GEOMETRY_BBOX));

        // Beyond and DWithin not handled well
        spatial.put("Beyond", new Integer(AbstractFilter.GEOMETRY_BEYOND));
        spatial.put("DWithin", new Integer(AbstractFilter.GEOMETRY_DWITHIN));
        
        logical.put("And", new Integer(AbstractFilter.LOGIC_AND));
        logical.put("Or", new Integer(AbstractFilter.LOGIC_OR));
        logical.put("Not", new Integer(AbstractFilter.LOGIC_NOT));
    }

    /**
     * Creates a new instance of FilterXMLParser
     */
    private FilterDOMParser() {
    }

    /**
     * Parses the filter using DOM.
     *
     * @param root a dom node containing FILTER as the root element.
     *
     * @return DOCUMENT ME!
     *
     * @task TODO: split up this insanely long method.
     */
    public static org.opengis.filter.Filter parseFilter(Node root) {
        LOGGER.finer("parsingFilter " + root.getLocalName());

        //NodeList children = root.getChildNodes();
        //LOGGER.finest("children "+children);
        if ((root == null) || (root.getNodeType() != Node.ELEMENT_NODE)) {
            LOGGER.finest("bad node input ");

            return null;
        }

        LOGGER.finest("processing root " + root.getLocalName() + " " + root.getNodeName());

        Node child = root;
        String childName = child.getLocalName();
        if(childName==null){
            childName= child.getNodeName();//HACK ?
        }
        if (childName.indexOf(':') != -1)
        {
        	//the DOM parser wasnt properly set to handle namespaces...
        	childName = childName.substring(childName.indexOf(':')+1);
        }
        
        LOGGER.finest("looking up " + childName);

        if (comparisions.containsKey(childName)) {
            LOGGER.finer("a comparision filter " + childName);

            //boolean like = false;
            //boolean between = false;
            try {
                short type = ((Integer) comparisions.get(childName)).shortValue();
                //CompareFilter filter = null;
                LOGGER.finer("type is " + type);

                if (type == AbstractFilter.FID) {
                    Set<FeatureId> ids = new HashSet<FeatureId>();
                    Element fidElement = (Element) child;
                    ids.add(FILTER_FACT.featureId(fidElement.getAttribute("fid")));

                    Node sibling = fidElement.getNextSibling();

                    while (sibling != null) {
                        LOGGER.finer("Parsing another FidFilter");

                        if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                            fidElement = (Element) sibling;
                            
                            String fidElementName = fidElement.getLocalName();
                            if(fidElementName==null){
                                fidElementName= fidElement.getNodeName();//HACK ?
                            }
                            if (fidElementName.indexOf(':') != -1)
                            {
                            	//the DOM parser wasnt properly set to handle namespaces...
                            	fidElementName = fidElementName.substring(fidElementName.indexOf(':')+1);
                            }
                            if ("FeatureId".equals(fidElementName)) {
                                ids.add(FILTER_FACT.featureId(fidElement.getAttribute("fid")));
                            }
                        }

                        sibling = sibling.getNextSibling();
                    }

                    return FILTER_FACT.id(ids);
                } else if (type == AbstractFilter.BETWEEN) {
                    //BetweenFilter bfilter = FILTER_FACT.createBetweenFilter();

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

                    // first expression
                    //value = kid.getFirstChild();
                    //while(value.getNodeType() != Node.ELEMENT_NODE ) 
                    //value = value.getNextSibling();
                    LOGGER.finer("add middle value -> " + value + "<-");
                    Expression middle = ExpressionDOMParser.parseExpression(value);
                    Expression lower = null;
                    Expression upper = null;

                    for (int i = 0; i < kids.getLength(); i++) {
                        Node kid = kids.item(i);

                        String kidName = (kid.getLocalName()!=null)?kid.getLocalName():kid.getNodeName(); 
                        if (kidName.indexOf(':') != -1)
                        {
                        	//the DOM parser wasnt properly set to handle namespaces...
                        	kidName = kidName.substring(kidName.indexOf(':')+1);
                        }
                        if (kidName.equalsIgnoreCase("LowerBoundary")) {
                            value = kid.getFirstChild();

                            while (value.getNodeType() != Node.ELEMENT_NODE) {
                                value = value.getNextSibling();
                            }

                            LOGGER.finer("add left value -> " + value + "<-");
                            lower = ExpressionDOMParser.parseExpression(value);
                        }

                        if (kidName.equalsIgnoreCase("UpperBoundary")) {
                            value = kid.getFirstChild();

                            while (value.getNodeType() != Node.ELEMENT_NODE) {
                                value = value.getNextSibling();
                            }

                            LOGGER.finer("add right value -> " + value + "<-");
                            upper = ExpressionDOMParser.parseExpression(value);
                        }
                    }

                    return FILTER_FACT.between( middle, lower, upper );
                } else if (type == AbstractFilter.LIKE) {
                    String wildcard = null;
                    String single = null;
                    String escape = null;
                    String pattern = null;
                    Expression value = null;
                    NodeList map = child.getChildNodes();

                    for (int i = 0; i < map.getLength(); i++) {
                        Node kid = map.item(i);

                        if ((kid == null)
                                || (kid.getNodeType() != Node.ELEMENT_NODE)) {
                            continue;
                        }

                        String res = (kid.getLocalName()!=null)?kid.getLocalName():kid.getNodeName(); 
                        if (res.indexOf(':') != -1)
                        {
                        	//the DOM parser wasnt properly set to handle namespaces...
                        	res = res.substring(res.indexOf(':')+1);
                        }

                        if (res.equalsIgnoreCase("PropertyName")) {
                            value = ExpressionDOMParser.parseExpression(kid);
                        }

                        if (res.equalsIgnoreCase("Literal")) {
                            pattern = ExpressionDOMParser.parseExpression(kid)
                                                         .toString();
                        }
                    }

                    NamedNodeMap kids = child.getAttributes();

                    for (int i = 0; i < kids.getLength(); i++) {
                        Node kid = kids.item(i);

                        //if(kid == null || kid.getNodeType() != Node.ELEMENT_NODE) continue;
                        String res = (kid.getLocalName()!=null)?kid.getLocalName():kid.getNodeName(); 
                        if (res.indexOf(':') != -1)
                        {
                        	//the DOM parser wasnt properly set to handle namespaces...
                        	res = res.substring(res.indexOf(':')+1);
                        }
                        if (res.equalsIgnoreCase("wildCard")) {
                            wildcard = kid.getNodeValue();
                        }

                        if (res.equalsIgnoreCase("singleChar")) {
                            single = kid.getNodeValue();
                        }

                        if (res.equalsIgnoreCase("escapeChar")
                                || res.equalsIgnoreCase("escape")) {
                            escape = kid.getNodeValue();
                        }
                    }

                    if (!((wildcard == null) || (single == null)
                            || (escape == null) || (pattern == null))) {
                        //LikeFilter lfilter = FILTER_FACT.createLikeFilter();
                        LOGGER.finer("Building like filter " + value.toString()
                            + "\n" + pattern + " " + wildcard + " " + single
                            + " " + escape);
                        //lfilter.setValue(value);
                        //lfilter.setPattern(pattern, wildcard, single, escape);

                        return FILTER_FACT.like( value, pattern, wildcard, single, escape );
                    }

                    LOGGER.finer("Problem building like filter\n" + pattern
                        + " " + wildcard + " " + single + " " + escape);

                    return null;
                } else if (type == AbstractFilter.NULL) {
                    return parseNullFilter(child);
                }

                // find and parse left and right values
                Node value = child.getFirstChild();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finest("add left value -> " + value + "<-");
                Expression left = ExpressionDOMParser.parseExpression(value);
                value = value.getNextSibling();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finest("add right value -> " + value + "<-");
                Expression right = ExpressionDOMParser.parseExpression(value);
                
                switch (type){
                case FilterType.COMPARE_EQUALS:
                    return FILTER_FACT.equals( left, right );
                    
                case FilterType.COMPARE_GREATER_THAN:
                    return FILTER_FACT.greater( left, right );
                    
                case FilterType.COMPARE_GREATER_THAN_EQUAL:
                    return FILTER_FACT.greaterOrEqual( left, right );
                    
                case FilterType.COMPARE_LESS_THAN:
                    return FILTER_FACT.less( left, right );
                    
                case FilterType.COMPARE_LESS_THAN_EQUAL:
                    return FILTER_FACT.lessOrEqual(left, right );

                case FilterType.COMPARE_NOT_EQUALS:
                    return FILTER_FACT.notEqual(left, right, true);

                default:
                    LOGGER.warning("Unable to build filter for " + childName);
                    return null;
                }
            } catch (IllegalFilterException ife) {
                LOGGER.warning("Unable to build filter: " + ife);
                return null;
            }
        } else if (spatial.containsKey(childName)) {
            LOGGER.finest("a spatial filter " + childName);

            try {
                short type = ((Integer) spatial.get(childName)).shortValue();
                //  GeometryFilter filter = FILTER_FACT.createGeometryFilter(type);
                Node value = child.getFirstChild();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finest("add left value -> " + value + "<-");
                Expression left = ExpressionDOMParser.parseExpression(value);
                value = value.getNextSibling();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finest("add right value -> " + value + "<-");

                String valueName = (value.getLocalName()!=null)?value.getLocalName():value.getNodeName(); 
                if (valueName.indexOf(':') != -1)
                {
                	//the DOM parser was not properly set to handle namespaces...
                	valueName = valueName.substring(valueName.indexOf(':')+1);
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
                    right = ExpressionDOMParser.parseExpression(literal);
                } else {
                    right = ExpressionDOMParser.parseExpression(value);
                }
                
                
                double distance;
                String units = null;
                String nodeName = null;
                switch ( type ){
                case FilterType.GEOMETRY_EQUALS:
                    return FILTER_FACT.equal( left, right );
                    
                case FilterType.GEOMETRY_DISJOINT:
                    return FILTER_FACT.disjoint( left, right );
                    
                case FilterType.GEOMETRY_INTERSECTS:
                    return FILTER_FACT.intersects( left, right );
                    
                case FilterType.GEOMETRY_TOUCHES:
                    return FILTER_FACT.touches( left, right );
                    
                case FilterType.GEOMETRY_CROSSES:
                    return FILTER_FACT.touches( left, right );
                    
                case FilterType.GEOMETRY_WITHIN:
                    return FILTER_FACT.within( left, right );
                    
                case FilterType.GEOMETRY_CONTAINS:
                    return FILTER_FACT.contains( left, right );
                    
                case FilterType.GEOMETRY_OVERLAPS:
                    return FILTER_FACT.overlaps( left, right );
                    
                case FilterType.GEOMETRY_DWITHIN:
                    value = nextNode;
                    while (value != null && value.getNodeType() != Node.ELEMENT_NODE) {
                        value = value.getNextSibling();
                    }
                    if(value == null) {
                        throw new IllegalFilterException("DWithin is missing the Distance element");
                    } 
                    
                    nodeName = value.getNodeName();
                    if(nodeName.indexOf(':') > 0) {
                        nodeName = nodeName.substring(nodeName.indexOf(":") + 1);
                    }
                    if(!"Distance".equals(nodeName)) {
                        throw new IllegalFilterException("Parsing DWithin, was expecting to find Distance but found " + value.getLocalName());
                    }
                    distance = Double.parseDouble(value.getTextContent());
                    if(value.getAttributes().getNamedItem("units") != null)
                        units = value.getAttributes().getNamedItem("units").getTextContent();
                    return FILTER_FACT.dwithin(left, right, distance, units );
                    
                case FilterType.GEOMETRY_BEYOND:
                    value = nextNode;
                    while (value != null && value.getNodeType() != Node.ELEMENT_NODE) {
                        value = value.getNextSibling();
                    }
                    if(value == null) {
                        throw new IllegalFilterException("Beyond is missing the Distance element");
                    } 
                    nodeName = value.getNodeName();
                    if(nodeName.indexOf(':') > 0) {
                        nodeName = nodeName.substring(nodeName.indexOf(":") + 1);
                    }
                    if(!"Distance".equals(nodeName)) {
                        throw new IllegalFilterException("Parsing Beyond, was expecting to find Distance but found " + value.getLocalName());
                    }
                    distance = Double.parseDouble(value.getTextContent());
                    if(value.getAttributes().getNamedItem("units") != null)
                        units = value.getAttributes().getNamedItem("units").getTextContent();
                    return FILTER_FACT.beyond(left, right, distance, units );
                    
                    
                case FilterType.GEOMETRY_BBOX:
                {
                    Literal literal = (Literal) right;
                    Object obj = literal.getValue();
                    ReferencedEnvelope bbox = null;
                    if( obj instanceof Geometry){
                        bbox = JTS.toEnvelope( (Geometry) obj );
                    }
                    else if (obj instanceof ReferencedEnvelope){
                        bbox = (ReferencedEnvelope) obj;
                    }
                    else if (obj instanceof Envelope){
                        // no clue about CRS / srsName so we should guess
                        bbox = new ReferencedEnvelope( (Envelope) obj, null );
                    }
                    return FILTER_FACT.bbox( left, bbox );                                                                                    
                }
                default:
                    LOGGER.warning("Unable to build filter: " + childName);
                    return null;
                }
            } catch (IllegalFilterException ife) {
                LOGGER.warning("Unable to build filter: " + ife);

                return null;
            }
        } else if (logical.containsKey(childName)) {
            LOGGER.finest("a logical filter " + childName);

            try {
                List<org.opengis.filter.Filter> children = new ArrayList<org.opengis.filter.Filter>();
                NodeList map = child.getChildNodes();

                for (int i = 0; i < map.getLength(); i++) {
                    Node kid = map.item(i);

                    if ((kid == null)
                            || (kid.getNodeType() != Node.ELEMENT_NODE)) {
                        continue;
                    }

                    LOGGER.finest("adding to logic filter " + kid.getLocalName());
                    children.add(parseFilter(kid));
                }
                
                if(childName.equals("And"))
                    return FILTER_FACT.and(children);
                else if(childName.equals("Or"))
                    return FILTER_FACT.or(children);
                else if(childName.equals("Not")) {
                    if(children.size() != 1)
                        throw new IllegalFilterException("Filter negation can be " +
                        		"applied to one and only one child filter");
                    return FILTER_FACT.not(children.get(0));
                } else {
                    throw new RuntimeException("Logical filter, but not And, Or, Not? " +
                    		"This should not happen");
                }
            } catch (IllegalFilterException ife) {
                LOGGER.warning("Unable to build filter: " + ife);

                return null;
            }
        }

        LOGGER.warning("unknown filter " + root);

        return null;
    }

    /**
     * Parses a null filter from a node known to be a null node.
     *
     * @param nullNode the PropertyIsNull node.
     *
     * @return a null filter of the expression contained in null node.
     *
     * @throws IllegalFilterException DOCUMENT ME!
     */
    private static PropertyIsNull parseNullFilter(Node nullNode)
        throws IllegalFilterException {
        LOGGER.finest("parsing null node: " + nullNode);

        Node value = nullNode.getFirstChild();

        while (value.getNodeType() != Node.ELEMENT_NODE) {
            value = value.getNextSibling();
        }

        LOGGER.finest("add null value -> " + value + "<-");
        Expression expr = ExpressionDOMParser.parseExpression(value);

        return FILTER_FACT.isNull( expr );
    }

}
