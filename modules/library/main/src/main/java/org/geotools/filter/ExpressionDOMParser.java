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
 *    Created on 03 July 2002, 10:21
 */
package org.geotools.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.TopologyException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * parses short sections of gml for use in expressions and filters. Hopefully we can get away without a full parser
 * here.
 *
 * @author iant
 * @author Niels Charlier
 */
public final class ExpressionDOMParser {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ExpressionDOMParser.class);

    /** Factory for creating filters. */
    private FilterFactory ff;

    /** Factory for creating geometry objects */
    private static GeometryFactory gfac = new GeometryFactory();

    /** number of coordinates in a box */
    private static final int NUM_BOX_COORDS = 5;

    /**
     * @param filterFactory The FilterFactory to create literals and expressions with.
     * @throws NullPointerException in case of filterFactory is null.
     */
    public ExpressionDOMParser(FilterFactory filterFactory) {
        Objects.requireNonNull(filterFactory);
        this.ff = filterFactory;
    }

    /** Constructor injection */
    public ExpressionDOMParser() {
        this(CommonFactoryFinder.getFilterFactory(null));
    }

    /**
     * Setter injection
     *
     * @param filterFactory The FilterFactory to create literals and expressions with.
     * @throws NullPointerException in case of filterFactory is null.
     */
    public void setFilterFactory(FilterFactory filterFactory) {
        Objects.requireNonNull(filterFactory);
        ff = filterFactory;
    }

    private static NamespaceSupport getNameSpaces(Node node) {
        NamespaceSupport namespaces = new NamespaceSupport();
        while (node != null) {
            NamedNodeMap atts = node.getAttributes();

            if (atts != null) {
                for (int i = 0; i < atts.getLength(); i++) {
                    Node att = atts.item(i);

                    if (att.getNamespaceURI() != null
                            && att.getNamespaceURI().equals("http://www.w3.org/2000/xmlns/")
                            && namespaces.getURI(att.getLocalName()) == null) {
                        namespaces.declarePrefix(att.getLocalName(), att.getNodeValue());
                    }
                }
            }

            node = node.getParentNode();
        }

        return namespaces;
    }

    /**
     * parses an expression for a filter.
     *
     * @param root the root node to parse, should be an filter expression.
     * @return the geotools representation of the expression held in the node.
     */
    public Expression expression(Node root) {
        // NodeList children = root.getChildNodes();
        // LOGGER.finest("children "+children);
        if (root == null || root.getNodeType() != Node.ELEMENT_NODE) {
            LOGGER.finer("bad node input ");

            return null;
        }

        LOGGER.finer("processing root " + root.getLocalName());

        Node child = root;

        String childName = child.getLocalName() != null ? child.getLocalName() : child.getNodeName();

        if (childName.indexOf(':') != -1) {
            // the DOM parser wasnt properly set to handle namespaces...
            childName = childName.substring(childName.indexOf(':') + 1);
        }

        if (childName.equalsIgnoreCase("Literal")) {
            LOGGER.finer("processing literal " + child);

            NodeList kidList = child.getChildNodes();
            LOGGER.finest("literal elements (" + kidList.getLength() + ") " + kidList.toString());

            for (int i = 0; i < kidList.getLength(); i++) {
                Node kid = kidList.item(i);
                LOGGER.finest("kid " + i + " " + kid);

                if (kid == null) {
                    LOGGER.finest("Skipping ");

                    continue;
                }

                if (kid.getNodeValue() == null) {
                    /* it might be a gml string so we need to convert it into
                     * a geometry this is a bit tricky since our standard
                     * gml parser is SAX based and we're a DOM here.
                     */
                    LOGGER.finer("node " + kid.getNodeValue() + " namespace " + kid.getNamespaceURI());
                    LOGGER.fine("a literal gml string?");

                    try {
                        Geometry geom = gml(kid);

                        if (geom != null) {
                            LOGGER.finer("built a " + geom.getGeometryType() + " from gml");
                            LOGGER.finer("\tpoints: " + geom.getNumPoints());
                        } else {
                            LOGGER.finer("got a null geometry back from gml parser");
                        }

                        return ff.literal(geom);
                    } catch (IllegalFilterException ife) {
                        LOGGER.warning("Problem building GML/JTS object: " + ife);
                    }

                    return null;
                }

                // CDATA shouldn't be interpretted
                if (kid.getNodeType() != Node.CDATA_SECTION_NODE
                        && kid.getNodeValue().trim().length() == 0) {
                    LOGGER.finest("empty text element");

                    continue;
                }

                // debuging only

                /*switch(kid.getNodeType()){
                case Node.ELEMENT_NODE:
                    LOGGER.finer("element :"+kid);
                    break;
                case Node.TEXT_NODE:
                    LOGGER.finer("text :"+kid);
                    break;
                case Node.ATTRIBUTE_NODE:
                    LOGGER.finer("Attribute :"+kid);
                    break;
                case Node.CDATA_SECTION_NODE:
                    LOGGER.finer("Cdata :"+kid);
                    break;
                case Node.COMMENT_NODE:
                    LOGGER.finer("comment :"+kid);
                    break;
                } */
                String nodeValue = kid.getNodeValue();
                LOGGER.finer("processing " + nodeValue);

                try {
                    // always store internal values as strings.  We might lose info otherwise.
                    return ff.literal(nodeValue);
                } catch (IllegalFilterException ife) {
                    LOGGER.finer("Unable to build expression " + ife);
                    return null;
                }
            }
            // creates an empty literal expression if there is nothing inside the literal
            return ff.literal("");
        }

        if (childName.equalsIgnoreCase("add")) {
            try {
                LOGGER.fine("processing an Add");

                // Node left = null;
                // Node right = null;
                Node value = child.getFirstChild();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add left value -> " + value + "<-");
                Expression left = expression(value);
                value = value.getNextSibling();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add right value -> " + value + "<-");
                Expression right = expression(value);

                return ff.add(left, right);
            } catch (IllegalFilterException ife) {
                LOGGER.warning("Unable to build expression " + ife);
                return null;
            }
        }

        if (childName.equalsIgnoreCase("sub")) {
            try {
                // NodeList kids = child.getChildNodes();
                Node value = child.getFirstChild();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }
                LOGGER.finer("add left value -> " + value + "<-");
                Expression left = expression(value);
                value = value.getNextSibling();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add right value -> " + value + "<-");
                Expression right = expression(value);

                return ff.subtract(left, right);
            } catch (IllegalFilterException ife) {
                LOGGER.warning("Unable to build expression " + ife);

                return null;
            }
        }

        if (childName.equalsIgnoreCase("mul")) {
            try {
                // NodeList kids = child.getChildNodes();
                Node value = child.getFirstChild();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add left value -> " + value + "<-");
                Expression left = expression(value);
                value = value.getNextSibling();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add right value -> " + value + "<-");
                Expression right = expression(value);

                return ff.multiply(left, right);
            } catch (IllegalFilterException ife) {
                LOGGER.warning("Unable to build expression " + ife);

                return null;
            }
        }

        if (childName.equalsIgnoreCase("div")) {
            try {
                Node value = child.getFirstChild();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add left value -> " + value + "<-");
                Expression left = expression(value);
                value = value.getNextSibling();

                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                }

                LOGGER.finer("add right value -> " + value + "<-");
                Expression right = expression(value);

                return ff.divide(left, right);
            } catch (IllegalFilterException ife) {
                LOGGER.warning("Unable to build expression " + ife);

                return null;
            }
        }

        if (childName.equalsIgnoreCase("PropertyName")) {
            try {
                // JD: trim whitespace here
                String value = child.getFirstChild().getNodeValue();
                value = value != null ? value.trim() : value;
                PropertyName attribute = ff.property(value, getNameSpaces(root));

                //                attribute.setAttributePath(child.getFirstChild().getNodeValue());
                return attribute;
            } catch (IllegalFilterException ife) {
                LOGGER.warning("Unable to build expression: " + ife);

                return null;
            }
        }

        if (childName.equalsIgnoreCase("Function")) {
            Element param = (Element) child;

            NamedNodeMap map = param.getAttributes();
            String funcName = null;

            for (int k = 0; k < map.getLength(); k++) {
                String res = map.item(k).getNodeValue();
                String name = map.item(k).getLocalName();

                if (name == null) {
                    name = map.item(k).getNodeName();
                }
                if (name.indexOf(':') != -1) {
                    // the DOM parser was not properly set to handle namespaces...
                    name = name.substring(name.indexOf(':') + 1);
                }

                LOGGER.fine("attribute " + name + " with value of " + res);

                if (name.equalsIgnoreCase("name")) {
                    funcName = res;
                }
            }

            if (funcName == null) {
                LOGGER.severe("failed to find a function name in " + child);
                return null;
            }

            ArrayList<Expression> args = new ArrayList<>();
            Node value = child.getFirstChild();

            ARGS:
            while (value != null) {
                while (value.getNodeType() != Node.ELEMENT_NODE) {
                    value = value.getNextSibling();
                    if (value == null) break ARGS;
                }
                args.add(expression(value));
                value = value.getNextSibling();
            }
            Expression[] array = args.toArray(new Expression[0]);
            return ff.function(funcName, array);
        }

        if (child.getNodeType() == Node.TEXT_NODE) {
            LOGGER.finer("processing a text node " + root.getNodeValue());

            String nodeValue = root.getNodeValue();
            LOGGER.finer("Text name " + nodeValue);

            // see if it's an int
            try {
                try {
                    Integer intLiteral = Integer.valueOf(nodeValue);

                    return ff.literal(intLiteral);
                } catch (NumberFormatException e) {
                    /* really empty */
                }

                try {
                    Double doubleLit = Double.valueOf(nodeValue);

                    return ff.literal(doubleLit);
                } catch (NumberFormatException e) {
                    /* really empty */
                }

                return ff.literal(nodeValue);
            } catch (IllegalFilterException ife) {
                LOGGER.finer("Unable to build expression " + ife);
            }
        }

        return null;
    }

    public Geometry gml(Node root) {
        // look for the SRS name, if available
        Node srsNameNode = root.getAttributes().getNamedItem("srsName");
        CoordinateReferenceSystem crs = null;
        if (srsNameNode != null) {
            String srs = srsNameNode.getTextContent();
            try {
                crs = CRS.decode(srs);
            } catch (Exception e) {
                LOGGER.warning("Failed to parse the specified SRS " + srs);
            }
        }

        // parse the geometry
        Geometry g = _gml(root);

        // force the crs if necessary
        if (crs != null) {
            g.setUserData(crs);
        }

        return g;
    }

    /**
     * Parses the gml of this node to jts.
     *
     * @param root the parent node of the gml to parse.
     * @return the java representation of the geometry contained in root.
     */
    private Geometry _gml(Node root) {
        LOGGER.finer("processing gml " + root);

        List<Coordinate> coordList;
        Node child = root;

        // Jesus I hate DOM.  I have no idea why this was checking for localname
        // and then nodename - lead to non-deterministic behavior, that for
        // some reason only failed if the filter parser was used within the
        // SLDparser.  I really would like that class redone, so we don't have
        // to use this crappy DOM GML parser.
        String childName = child.getNodeName();
        if (childName == null) {
            childName = child.getLocalName();
        }
        if (!childName.startsWith("gml:")) {
            childName = "gml:" + childName;
        }

        if (childName.equalsIgnoreCase("gml:box")) {
            coordList = new ExpressionDOMParser(CommonFactoryFinder.getFilterFactory()).coords(child);

            org.locationtech.jts.geom.Envelope env = new org.locationtech.jts.geom.Envelope();

            for (Coordinate coordinate : coordList) {
                env.expandToInclude(coordinate);
            }
            Coordinate[] coords = new Coordinate[NUM_BOX_COORDS];
            coords[0] = new Coordinate(env.getMinX(), env.getMinY());
            coords[1] = new Coordinate(env.getMinX(), env.getMaxY());
            coords[2] = new Coordinate(env.getMaxX(), env.getMaxY());
            coords[3] = new Coordinate(env.getMaxX(), env.getMinY());
            coords[4] = new Coordinate(env.getMinX(), env.getMinY());

            // return new ReferencedEnvelope( env, null );
            org.locationtech.jts.geom.LinearRing ring = null;

            try {
                ring = gfac.createLinearRing(coords);
            } catch (org.locationtech.jts.geom.TopologyException tope) {
                LOGGER.fine("Topology Exception in GMLBox" + tope);

                return null;
            }
            return gfac.createPolygon(ring, null);
        }

        // check for geometry properties
        if (childName.equalsIgnoreCase("gml:polygonmember")
                || childName.equalsIgnoreCase("gml:pointmember")
                || childName.equalsIgnoreCase("gml:linestringmember")
                || childName.equalsIgnoreCase("gml:linearringmember")) {

            for (int i = 0; i < child.getChildNodes().getLength(); i++) {
                Node newChild = child.getChildNodes().item(i);
                if (newChild.getNodeType() == Node.ELEMENT_NODE) {
                    childName = newChild.getNodeName();
                    if (!childName.startsWith("gml:")) {
                        childName = "gml:" + childName;
                    }
                    root = newChild;
                    child = newChild;
                    break;
                }
            }
        }

        if (childName.equalsIgnoreCase("gml:polygon")) {
            LOGGER.finer("polygon");

            LinearRing outer = null;
            List<LinearRing> inner = new ArrayList<>();
            NodeList kids = root.getChildNodes();

            for (int i = 0; i < kids.getLength(); i++) {
                Node kid = kids.item(i);
                LOGGER.finer("doing " + kid);

                String kidName = kid.getNodeName();
                if (kidName == null) {
                    kidName = child.getLocalName();
                }
                if (!kidName.startsWith("gml:")) {
                    kidName = "gml:" + kidName;
                }

                if (kidName.equalsIgnoreCase("gml:outerBoundaryIs")) {
                    outer = (LinearRing) gml(kid);
                }

                if (kidName.equalsIgnoreCase("gml:innerBoundaryIs")) {
                    inner.add((LinearRing) gml(kid));
                }
            }

            if (inner.isEmpty()) {
                return gfac.createPolygon(outer, null);
            } else {
                return gfac.createPolygon(outer, inner.toArray(new LinearRing[0]));
            }
        }

        if (childName.equalsIgnoreCase("gml:outerBoundaryIs") || childName.equalsIgnoreCase("gml:innerBoundaryIs")) {
            LOGGER.finer("Boundary layer");

            NodeList kids = ((Element) child).getElementsByTagName("gml:LinearRing");
            if (kids.getLength() == 0) kids = ((Element) child).getElementsByTagName("LinearRing");
            return gml(kids.item(0));
        }

        if (childName.equalsIgnoreCase("gml:linearRing")) {
            LOGGER.finer("LinearRing");
            coordList = new ExpressionDOMParser(CommonFactoryFinder.getFilterFactory()).coords(child);

            org.locationtech.jts.geom.LinearRing ring = null;

            try {
                ring = gfac.createLinearRing(coordList.toArray(new Coordinate[] {}));
            } catch (TopologyException te) {
                LOGGER.finer("Topology Exception build linear ring: " + te);

                return null;
            }

            return ring;
        }

        if (childName.equalsIgnoreCase("gml:linestring")) {
            LOGGER.finer("linestring");
            coordList = new ExpressionDOMParser(CommonFactoryFinder.getFilterFactory()).coords(child);

            LineString line = gfac.createLineString(coordList.toArray(new Coordinate[] {}));

            return line;
        }

        if (childName.equalsIgnoreCase("gml:point")) {
            LOGGER.finer("point");
            coordList = new ExpressionDOMParser(CommonFactoryFinder.getFilterFactory()).coords(child);

            Point point = gfac.createPoint(coordList.get(0));

            return point;
        }

        if (childName.toLowerCase().startsWith("gml:multipolygon")
                || childName.toLowerCase().startsWith("gml:multilinestring")
                || childName.toLowerCase().startsWith("gml:multipoint")) {

            List<Geometry> multi = new ArrayList<>();

            // parse all children thru parseGML
            NodeList kids = child.getChildNodes();

            for (int i = 0; i < kids.getLength(); i++) {
                if (kids.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    multi.add(gml(kids.item(i)));
                }
            }

            if (childName.toLowerCase().startsWith("gml:multipolygon")) {
                LOGGER.finer("MultiPolygon");
                return gfac.createMultiPolygon(multi.toArray(new Polygon[0]));
            } else if (childName.toLowerCase().startsWith("gml:multilinestring")) {
                LOGGER.finer("MultiLineString");
                return gfac.createMultiLineString(multi.toArray(new LineString[0]));
            } else {
                LOGGER.finer("MultiPoint");
                return gfac.createMultiPoint(multi.toArray(new Point[0]));
            }
        }

        return null;
    }

    /**
     * Parses a dom node into a coordinate list.
     *
     * @param root the root node representation of gml:coordinates.
     * @return the coordinates in a list.
     */
    public List<Coordinate> coords(Node root) {
        LOGGER.finer("parsing coordinate(s) " + root);

        List<Coordinate> clist = new ArrayList<>();
        NodeList kids = root.getChildNodes();

        for (int i = 0; i < kids.getLength(); i++) {
            Node child = kids.item(i);
            LOGGER.finer("doing " + child);

            // if (child.getLocalName().equalsIgnoreCase("gml:coordinate")) {
            //  String internal = child.getNodeValue();
            // }
            String childName = child.getNodeName();
            if (childName == null) {
                childName = child.getLocalName();
            }
            if (!childName.startsWith("gml:")) {
                childName = "gml:" + childName;
            }
            if (childName.equalsIgnoreCase("gml:coord")) {
                // DJB: adding support for:
                // <gml:coord><gml:X>-180.0</gml:X><gml:Y>-90.0</gml:Y></gml:coord>
                // <gml:coord><gml:X>180.0</gml:X><gml:Y>90.0</gml:Y></gml:coord>
                Coordinate c = new Coordinate();
                NodeList grandChildren = child.getChildNodes();

                for (int t = 0; t < grandChildren.getLength(); t++) {
                    Node grandChild = grandChildren.item(t);
                    String grandChildName = grandChild.getNodeName();
                    if (grandChildName == null) grandChildName = grandChild.getLocalName();
                    if (!grandChildName.startsWith("gml:")) grandChildName = "gml:" + grandChildName;

                    if (grandChildName.equalsIgnoreCase("gml:x")) {
                        c.x = Double.parseDouble(grandChild
                                .getChildNodes()
                                .item(0)
                                .getNodeValue()
                                .trim());
                    } else if (grandChildName.equalsIgnoreCase("gml:y")) {
                        c.y = Double.parseDouble(grandChild
                                .getChildNodes()
                                .item(0)
                                .getNodeValue()
                                .trim());
                    } else if (grandChildName.equalsIgnoreCase("gml:z")) {
                        c.setZ(Double.parseDouble(grandChild
                                .getChildNodes()
                                .item(0)
                                .getNodeValue()
                                .trim()));
                    }
                }
                clist.add(c);
            }

            if (childName.equalsIgnoreCase("gml:coordinates")) {
                LOGGER.finer("coordinates " + child.getFirstChild().getNodeValue());

                NodeList grandKids = child.getChildNodes();

                for (int k = 0; k < grandKids.getLength(); k++) {
                    Node grandKid = grandKids.item(k);

                    if (grandKid.getNodeValue() == null) {
                        continue;
                    }

                    if (grandKid.getNodeValue().trim().length() == 0) {
                        continue;
                    }

                    String outer = grandKid.getNodeValue().trim();
                    // handle newline and tab whitespace along with space
                    StringTokenizer ost = new StringTokenizer(outer, " \n\t");

                    while (ost.hasMoreTokens()) {
                        String internal = ost.nextToken();
                        StringTokenizer ist = new StringTokenizer(internal, ",");
                        double xCoord = Double.parseDouble(ist.nextToken());
                        double yCoord = Double.parseDouble(ist.nextToken());
                        double zCoord = Double.NaN;

                        if (ist.hasMoreTokens()) {
                            zCoord = Double.parseDouble(ist.nextToken());
                        }

                        clist.add(new Coordinate(xCoord, yCoord, zCoord));
                    }
                }
            }
        }

        return clist;
    }
}
