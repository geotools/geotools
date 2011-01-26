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
 */
package org.geotools.gml;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.xml.sax.SAXException;


/**
 * LEVEL1 saxGML4j GML filter: Sends basic alerts for GML types to
 * GMLFilterGeometry.
 * 
 * <p>
 * This filter separates and passes GML events to a GMLHandlerGeometry. The
 * main simplification that it performs is to pass along coordinates as an
 * abstracted method call, regardless of their notation in the GML (Coord vs.
 * Coordinates).  This call turns the coordinates into doubles and makes sure
 * that it distinguishes between 2 and 3 value coordinates.
 * </p>
 * 
 * <p>
 * The filter also handles some more subtle processing, including handling
 * different delimiters (decimal, coordinate, tuple) that may be used by more
 * outlandish GML generators.
 * </p>
 * 
 * <p></p>
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL$
 * @version $Id$
 */
public class GMLFilterDocument extends org.xml.sax.helpers.XMLFilterImpl {
    /** The logger for the GML module */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.gml");

    // Static Globals to handle some expected elements

    /** GML namespace string */
    private static final String GML_NAMESPACE = "http://www.opengis.net/gml";

    /** Coord name */
    private static final String COORD_NAME = "coord";

    /** Coordinates name */
    private static final String COORDINATES_NAME = "coordinates";

    /** X Coordinate name */
    private static final String X_NAME = "X";

    /** Y Coordinate name */
    private static final String Y_NAME = "Y";

    /** Z Coordinate name */
    private static final String Z_NAME = "Z";

    /** Sub geometry elements that may be passed in GML */
    private static final java.util.Collection SUB_GEOMETRY_TYPES = new java.util.Vector(java.util.Arrays
            .asList(new String[] { "outerBoundaryIs", "innerBoundaryIs" }));

    /** Base geometry elements that may be passed in GML */
    private static final java.util.Collection BASE_GEOMETRY_TYPES = new java.util.Vector(java.util.Arrays
            .asList(new String[] {
                    "Point", "LineString", "Polygon", "LinearRing", "Box",
                    "MultiPoint", "MultiLineString", "MultiPolygon",
                    "GeometryCollection"
                }));

    /** Added by Sean Geoghegan to store character data chunks */
    private StringBuffer buffer = new StringBuffer();

    /** Parent of the filter: must implement GMLHandlerGeometry. */
    private GMLHandlerGeometry parent;

    /** Handles all coordinate parsing. */
    private CoordinateReader coordinateReader = new CoordinateReader();

    /** Whether or not this parser should consider namespaces. */
    private boolean namespaceAware = true;

    /**
     * Constructor with parent.
     *
     * @param parent Parent of the filter: must implement GMLHandlerGeometry.
     */
    public GMLFilterDocument(GMLHandlerGeometry parent) {
        super();
        LOGGER.entering("GMLFilterDocument", "new", parent);
        this.parent = parent;
        LOGGER.exiting("GMLFilterDocument", "new");
    }

    /**
     * Checks for GML element start and - if not a coordinates element - sends
     * it directly on down the chain to the appropriate parent handler.  If it
     * is a coordinates (or coord) element, it uses internal methods to set
     * the current state of the coordinates reader appropriately.
     * 
     * <p>
     * Modified by Sean Geoghegan to create new StringBuffers when  entering a
     * coord or coordinate element.
     * </p>
     *
     * @param namespaceURI The namespace of the element.
     * @param localName The local name of the element.
     * @param qName The full name of the element, including namespace prefix.
     * @param atts The element attributes.
     *
     * @throws SAXException Some parsing error occurred while reading
     *         coordinates.
     */
    public void startElement(String namespaceURI, String localName,
        String qName, org.xml.sax.Attributes atts) throws SAXException {
        LOGGER.entering("GMLFilterDocument", "startElement",
            new Object[] { namespaceURI, localName, qName, atts });

        /* if at a GML element, do some checks to determine
         * how to handle the element
         */
        if (namespaceURI != null && namespaceURI.equals(GML_NAMESPACE)) {
            // if geometry, pass it on down the filter chain
            if (BASE_GEOMETRY_TYPES.contains(localName)) {
                parent.geometryStart(localName, atts);
            } else if (SUB_GEOMETRY_TYPES.contains(localName)) {
                parent.geometrySub(localName);
            } else if (COORDINATES_NAME.equals(localName)) {
                // if coordinate, set one of the internal coordinate methods
                coordinateReader.insideCoordinates(true, atts);
                buffer = new StringBuffer();
            } else if (COORD_NAME.equals(localName)) {
                coordinateReader.insideCoord(true);
                buffer = new StringBuffer();
            } else if (X_NAME.equals(localName)) {
                buffer = new StringBuffer();
                coordinateReader.insideX(true);
            } else if (Y_NAME.equals(localName)) {
                buffer = new StringBuffer();
                coordinateReader.insideY(true);
            } else if (Z_NAME.equals(localName)) {
                buffer = new StringBuffer();
                coordinateReader.insideZ(true);
            } else {
                parent.startElement(namespaceURI, localName, qName, atts);
            }
        } else {
            /* all non-GML elements passed on down the filter chain without
             * modification
             */
            parent.startElement(namespaceURI, localName, qName, atts);
        }

        LOGGER.exiting("GMLFilterDocument", "startElement");
    }

    /**
     * Reads the only internal characters read by pure GML parsers, which are
     * coordinates.  These coordinates are sent to the coordinates reader
     * class, which interprets them appropriately, depending on its current
     * state.
     * 
     * <p>
     * Modified by Sean Geoghegan to append character data to buffer when
     * inside a coordinate or coord element.  SAX doesn't guarentee that all
     * the character data of an element will be passed to the character method
     * in one call, it may be split up into chunks.
     * </p>
     *
     * @param ch Raw coordinate string from the GML document.
     * @param start Beginning character position of raw coordinate string.
     * @param length Length of the character string.
     *
     * @throws SAXException Some parsing error occurred while reading
     *         coordinates.
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        LOGGER.entering("GMLFilterDocument", "characters",
            new Object[] { ch, new Integer(start), new Integer(length) });

        /* the methods here read in both coordinates and coords and
         * take the grunt-work out of this task for geometry handlers
         * see the documentation for CoordinatesReader to see what this entails
         */
        String rawCoordinates = new String(ch, start, length);

        /* determines how to read coordinates, depending on
         * what element we are currently inside
         */
        if (coordinateReader.insideCoordinates()) {
            buffer.append(rawCoordinates);

            //coordinateReader.readCoordinates(rawCoordinates);
        } else if (coordinateReader.insideCoord()) {
            buffer.append(rawCoordinates);

            //coordinateReader.readCoord(rawCoordinates);
        } else {
            /* all non-coordinate data passed on down the
             * filter chain without modification
             */
            parent.characters(ch, start, length);
        }

        LOGGER.exiting("GMLFilterDocument", "characters");
    }

    /**
     * Checks for GML element end and - if not a coordinates element -  sends
     * it directly on down the chain to the appropriate parent handler.  If it
     * is a coordinates (or coord) element,  it uses internal methods to set
     * the current state of the coordinates reader appropriately.
     * 
     * <p>
     * Modified by Sean Geoghegan. When we reach the end of a coord or
     * coordinate element, then the buffer is passed to the handler for
     * processing.
     * </p>
     *
     * @param namespaceURI The namespace of the element.
     * @param localName The local name of the element.
     * @param qName The full name of the element, including namespace prefix.
     *
     * @throws SAXException Some parsing error occurred while  reading
     *         coordinates.
     */
    public void endElement(String namespaceURI, String localName, String qName)
        throws SAXException {
        LOGGER.entering("GMLFilterDocument", "endElement",
            new Object[] { namespaceURI, localName, qName });

        /* if leaving a GML element, handle and pass to appropriate
         * internal or external method
         */
        if (namespaceURI.equals(GML_NAMESPACE) || !namespaceAware) {
            // if geometry, pass on down the chain to appropriate handlers
            if (BASE_GEOMETRY_TYPES.contains(localName)) {
                parent.geometryEnd(localName);
            } else if (SUB_GEOMETRY_TYPES.contains(localName)) {
                parent.geometrySub(localName);
            } else if (COORDINATES_NAME.equals(localName)) {
                // Convert the string buffer to a string and process the
                // coordinate, then end the coordinate status in the handler.
                coordinateReader.readCoordinates(buffer.toString());
                coordinateReader.insideCoordinates(false);
            } else if (COORD_NAME.equals(localName)) {
                coordinateReader.readCoord(buffer.toString());
                coordinateReader.insideCoord(false);
            } else if (X_NAME.equals(localName)) {
                coordinateReader.readCoord(buffer.toString());
                coordinateReader.insideX(false);
            } else if (Y_NAME.equals(localName)) {
                coordinateReader.readCoord(buffer.toString());
                coordinateReader.insideY(false);
            } else if (Z_NAME.equals(localName)) {
                coordinateReader.readCoord(buffer.toString());
                coordinateReader.insideZ(false);
            } else if (!namespaceAware) {
                /* if not namespace aware, then just pass element through;
                 * otherwise, there is some error in the GML
                 */
                parent.endElement(namespaceURI, localName, qName);
            } else {
                parent.endElement(namespaceURI, localName, qName);
            }

            //else { throw new SAXException("Unrecognized GML element."); }
        } else {
            /* all non-GML elements passed on down the filter chain
             * without modification
             */
            parent.endElement(namespaceURI, localName, qName);
        }

        LOGGER.exiting("GMLFilterDocument", "endElement");
    }

    /**
     * Simplifies the parsing process for GML coordinate elements.
     * 
     * <p>
     * One of the more annoying features of GML (from a SAX parsing
     * perspective) is the dual coord and coordinate representation of
     * coordinates. To further complicate the matter, delimiters for the
     * coordinates element are quite flexible.  This class hides all that
     * nasty complexity beneath a benign exterior and greatly reduces the
     * complexity of the GMLFilterDocument code.
     * </p>
     */
    private class CoordinateReader {
        /** Flag for indicating not inside any tag. */
        private static final int NOT_INSIDE = 0;

        /** Flag for indicating inside coord tag. */
        private static final int INSIDE_COORD = 1;

        /** Flag for indicating inside coordinates tag. */
        private static final int INSIDE_COORDINATES = 2;

        /** Flag for indicating inside X tag. */
        private static final int INSIDE_X = 1;

        /** Flag for indicating inside Y tag. */
        private static final int INSIDE_Y = 2;

        /** Flag for indicating inside Z tag. */
        private static final int INSIDE_Z = 3;

        /** Remembers where we are inside the GML coordinate stream. */
        private int insideOuterFlag = NOT_INSIDE;

        /** Remembers where we are inside the GML coordinate stream. */
        private int insideInnerFlag = NOT_INSIDE;

        /** Remembers last X coordinate read. */
        private Double x = new Double(Double.NaN);

        /** Remembers last Y coordinate read. */
        private Double y = new Double(Double.NaN);

        /** Remembers last Z coordinate read. */
        private Double z = new Double(Double.NaN);

        /**
         * Stores requested delimiter for coordinate  separation; default = ','
         */
        private String coordinateDelimeter = ",";

        /** Stores requested delimiter for tuple separation; default = ' ' */
        private String tupleDelimeter = " ";

        /** Stores requested delimiter for decimal separation; default = '.' */
        private StringBuffer decimalDelimeter = new StringBuffer(".");

        /**
         * Remembers whether or not the standard decimal is used, to speed up
         * parsing.
         */
        private boolean standardDecimalFlag = true;

        /**
         * Empty constructor.
         */
        public CoordinateReader() {
        }

        /**
         * Reads raw coordinates from the GML and returns them to the parent as
         * neat functions.
         *
         * @param coordinateString Raw coordinate string from the GML document.
         *
         * @throws SAXException Some parsing error occurred while reading
         *         coordinates.
         */
        public void readCoordinates(String coordinateString)
            throws SAXException {
            /* if non-standard delimiter, replace it with
             * standard ',' through the entire string
             */
            if (!standardDecimalFlag) {
                coordinateString = coordinateString.replace(decimalDelimeter
                        .charAt(0), '.');
            }

            // separate tuples and loop through the set
            StringTokenizer coordinateSets = new StringTokenizer(coordinateString
                    .trim(), tupleDelimeter);
            StringTokenizer coordinates;

            // loop through each of the coordinate sets.
            // Depending on the number of coordinates found,
            // call the correct parent coordinate class
            while (coordinateSets.hasMoreElements()) {
                coordinates = new StringTokenizer(coordinateSets.nextToken(),
                        coordinateDelimeter);
                x = new Double(coordinates.nextToken().trim());
                y = new Double(coordinates.nextToken().trim());

                if (coordinates.hasMoreElements()) {
                    z = new Double(coordinates.nextToken().trim());
                    parent.gmlCoordinates(x.doubleValue(), y.doubleValue(),
                        z.doubleValue());
                } else {
                    parent.gmlCoordinates(x.doubleValue(), y.doubleValue());
                }
            }
        }

        /**
         * Reads a coord string.  Note that this string is actually inside an
         * X, Y, Z tag and is not directly returned by the parent function,
         * unlike the readCoordinates method.
         *
         * @param coordString The raw coordinate string from the XML document.
         */
        public void readCoord(String coordString) {
            // if non-standard delimiter, replace it with standard ',' 
            // through the entire string
            if (!standardDecimalFlag) {
                coordString = coordString.replace(decimalDelimeter.charAt(0),
                        '.');
            }

            // determine which coord string we are inside
            // set internal x,y,z values depending on the return
            switch (insideInnerFlag) {
            case INSIDE_X:
                x = new Double(coordString.trim());

                break;

            case INSIDE_Y:
                y = new Double(coordString.trim());

                break;

            case INSIDE_Z:
                z = new Double(coordString.trim());

                break;

            default:
                break;
            }
        }

        /**
         * Sets an entrance into a coordinates element.
         *
         * @param isInside Sets whether or not we are  inside a coordinates
         *        tag.
         * @param atts Passes the coordinates tag attributes.
         */
        public void insideCoordinates(boolean isInside,
            org.xml.sax.Attributes atts) {
            this.insideCoordinates(isInside);
        }

        /**
         * Sets an entrance into a coordinates element.
         *
         * @param isInside Sets whether or not we are inside a coordinates tag.
         */
        public void insideCoordinates(boolean isInside) {
            if (isInside) {
                insideOuterFlag = INSIDE_COORDINATES;
            } else {
                insideOuterFlag = NOT_INSIDE;
            }
        }

        /**
         * Sets an entrance into a coord element.
         *
         * @param isInside Sets whether or not we are inside a coord tag.
         *
         * @throws SAXException if error occurs in reading
         */
        public void insideCoord(boolean isInside) throws SAXException {
            // if entering coord tag, simply set our internal flag for this
            if (isInside) {
                insideOuterFlag = INSIDE_COORD;
            } else {
                // if leaving coord tag, send coordinates to parent and 
                // set all internal values to null equivalent.
                // if coordinates exist, send on down the filter chain
                // otherwise, throw an exception
                if ((!x.isNaN()) && (!y.isNaN()) && (z.isNaN())) {
                    parent.gmlCoordinates(x.doubleValue(), y.doubleValue());
                } else if ((!x.isNaN()) && (!y.isNaN()) && (!z.isNaN())) {
                    parent.gmlCoordinates(x.doubleValue(), y.doubleValue(),
                        z.doubleValue());
                }

                //else {
                x = new Double(Double.NaN);
                y = new Double(Double.NaN);
                z = new Double(Double.NaN);
                insideOuterFlag = NOT_INSIDE;
            }
        }

        /**
         * Sets an entrance into an X element.
         *
         * @param isInside Sets whether or not we are inside an X tag.
         */
        public void insideX(boolean isInside) {
            if (isInside) {
                insideInnerFlag = INSIDE_X;
            } else {
                insideInnerFlag = NOT_INSIDE;
            }
        }

        /**
         * Sets an entrance into a Y element.
         *
         * @param isInside Sets whether or not we are inside a Y tag.
         */
        public void insideY(boolean isInside) {
            if (isInside) {
                insideInnerFlag = INSIDE_Y;
            } else {
                insideInnerFlag = NOT_INSIDE;
            }
        }

        /**
         * Sets an entrance into a Z element.
         *
         * @param isInside Sets whether or not we are inside a Z tag.
         */
        public void insideZ(boolean isInside) {
            if (isInside) {
                insideInnerFlag = INSIDE_Z;
            } else {
                insideInnerFlag = NOT_INSIDE;
            }
        }

        /**
         * Queries whether or not we are inside a coordinates element.
         *
         * @return true/false
         */
        public boolean insideCoordinates() {
            return insideOuterFlag == INSIDE_COORDINATES;
        }

        /**
         * Queries whether or not we are inside a coord element.
         *
         * @return true/false
         */
        public boolean insideCoord() {
            return insideOuterFlag == INSIDE_COORD;
        }
    }
}
