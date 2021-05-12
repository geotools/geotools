/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gml.stream;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.geometry.jts.MultiCurve;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Parse GML 3 geometries from a StAX XMLStreamReader. */
public class XmlStreamGeometryReader {

    private final XMLStreamReader reader;

    private GeometryFactory geomFac;

    private boolean unsafeXMLAllowed = false;

    private Predicate<CoordinateReferenceSystem> invertAxisNeeded = null;

    private final Map<CoordinateReferenceSystem, Boolean> invertAxisNeededCache = new HashMap<>();

    public XmlStreamGeometryReader(final XMLStreamReader reader, final GeometryFactory geomFac) {
        this.geomFac = geomFac;
        this.reader = reader;
    }

    public void setGeometryFactory(GeometryFactory geomFac) {
        this.geomFac = geomFac;
    }

    public GeometryFactory getGeometryFactory() {
        return geomFac;
    }

    public boolean isUnsafeXMLAllowed() {
        return unsafeXMLAllowed;
    }

    /**
     * @param unsafeXMLAllowed true if you want to parse from an XMLStreamReader not configured for
     *     safe XML parsing (XMLInputFactory.SUPPORT_DTD is true)
     */
    public void setUnsafeXMLAllowed(boolean unsafeXMLAllowed) {
        this.unsafeXMLAllowed = unsafeXMLAllowed;
    }

    /**
     * By default axis are not inverted, you can provide a predicate that determines whether axes
     * need to be inverted for a CRS (only called once per CRS).
     */
    public void setInvertAxisNeeded(Predicate<CoordinateReferenceSystem> invertAxisNeeded) {
        this.invertAxisNeeded = invertAxisNeeded;
    }

    /**
     * @throws IllegalStateException when the reader is configured for unsafe XML and this is not
     *     explicitly allowed by {@link #setUnsafeXMLAllowed setUnsafeXMLAllowed}.
     */
    private void checkUnsafeXML() throws IllegalStateException {
        if (!this.unsafeXMLAllowed) {
            // Only check whether DTDs are enabled because when DTDs are not supported, XXE and XML
            // bombs are not possible. Even if DTDs are enabled but external entities are disabled,
            // XML entity expansion and thus XML bombs are still possible.

            // StAX does not define properties to limit entity expansion or enable secure processing
            // such as JAXP.
            // A StAX implementation like Woodstox provides its' own properties to set limits, such
            // as P_MAX_ENTITY_DEPTH and others, which allows limited use of these XML features.
            // Call setUnsafeXMLAllowed(true) after making sure the XML you are parsing is safe or
            // the StAX parser is configured with appropriate limits if you really need DTD support.

            if (Boolean.TRUE.equals(reader.getProperty(XMLInputFactory.SUPPORT_DTD))) {
                throw new IllegalStateException(
                        "XMLStreamReader allows DTDs but "
                                + this.getClass().getSimpleName()
                                + " is not configured to allow unsafe XML");
            }
        }
    }

    /**
     * Precondition: parser cursor positioned on a geometry property (ej, {@code gml:Point}, etc)
     *
     * <p>Postcondition: parser gets positioned at the end tag of the element it started parsing the
     * geometry at.
     */
    public Geometry readGeometry()
            throws NoSuchAuthorityCodeException, FactoryException, XMLStreamException, IOException {

        checkUnsafeXML();

        final QName startingGeometryTagName = reader.getName();
        int dimension = crsDimension(2);
        CoordinateReferenceSystem crs = crs(DefaultGeographicCRS.WGS84);

        Geometry geom;
        if (GML.Point.equals(startingGeometryTagName)) {
            geom = parsePoint(dimension, crs);
        } else if (GML.LineString.equals(startingGeometryTagName)) {
            geom = parseLineString(dimension, crs);
        } else if (GML.Polygon.equals(startingGeometryTagName)) {
            geom = parsePolygon(dimension, crs);
        } else if (GML.MultiPoint.equals(startingGeometryTagName)) {
            geom = parseMultiPoint(dimension, crs);
        } else if (GML.MultiLineString.equals(startingGeometryTagName)) {
            geom = parseMultiLineString(dimension, crs);
        } else if (GML.MultiCurve.equals(startingGeometryTagName)) {
            geom = parseMultiCurve(dimension, crs);
        } else if (GML.MultiSurface.equals(startingGeometryTagName)) {
            geom = parseMultiSurface(dimension, crs);
        } else if (GML.MultiPolygon.equals(startingGeometryTagName)) {
            geom = parseMultiPolygon(dimension, crs);
        } else {
            throw new IllegalStateException(
                    "Unrecognized geometry element " + startingGeometryTagName);
        }

        reader.require(
                END_ELEMENT,
                startingGeometryTagName.getNamespaceURI(),
                startingGeometryTagName.getLocalPart());

        return geom;
    }

    private Geometry parseMultiCurve(int dimension, CoordinateReferenceSystem crs)
            throws IOException, NoSuchAuthorityCodeException, FactoryException, XMLStreamException {

        reader.require(START_ELEMENT, GML.NAMESPACE, GML.MultiCurve.getLocalPart());

        List<LineString> lines = new ArrayList<>(2);

        while (true) {
            reader.nextTag();
            if (END_ELEMENT == reader.getEventType()
                    && GML.MultiCurve.getLocalPart().equals(reader.getLocalName())) {
                // we're done
                break;
            }
            reader.require(START_ELEMENT, GML.NAMESPACE, GML.curveMember.getLocalPart());
            reader.nextTag();
            final QName startingGeometryTagName =
                    new QName(reader.getNamespaceURI(), reader.getLocalName());
            if (GML.LineString.equals(startingGeometryTagName)) {
                lines.add(parseLineString(dimension, crs));
            } else if (GML.CompositeCurve.equals(startingGeometryTagName)) {
                throw new UnsupportedOperationException(
                        GML.CompositeCurve + " is not supported yet");
            } else if (GML.Curve.equals(startingGeometryTagName)) {
                throw new UnsupportedOperationException(GML.Curve + " is not supported yet");
            } else if (GML.OrientableCurve.equals(startingGeometryTagName)) {
                throw new UnsupportedOperationException(
                        GML.OrientableCurve + " is not supported yet");
            }

            reader.nextTag();
            reader.require(END_ELEMENT, GML.NAMESPACE, GML.curveMember.getLocalPart());
        }

        reader.require(END_ELEMENT, GML.NAMESPACE, GML.MultiCurve.getLocalPart());

        MultiCurve geom = new MultiCurve(lines, geomFac, 1.0);
        return geom;
    }

    /**
     * Parses a MultiPoint.
     *
     * <p>Precondition: parser positioned at a {@link GML#MultiPoint MultiPoint} start tag
     *
     * <p>Postcondition: parser positioned at the {@link GML#MultiPoint MultiPoint} end tag of the
     * starting tag
     */
    private Geometry parseMultiPoint(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {
        reader.nextTag();
        final QName memberTag = reader.getName();
        List<Point> points = new ArrayList<>(4);
        if (GML.pointMembers.equals(memberTag)) {
            while (true) {
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()
                        && GML.pointMembers.getLocalPart().equals(reader.getLocalName())) {
                    // we're done
                    break;
                }
                Point p = parsePoint(dimension, crs);
                points.add(p);
            }
            reader.nextTag();
        } else if (GML.pointMember.equals(memberTag)) {
            while (true) {
                reader.nextTag();
                reader.require(START_ELEMENT, GML.NAMESPACE, GML.Point.getLocalPart());

                Point p = parsePoint(dimension, crs);
                points.add(p);
                reader.nextTag();
                reader.require(END_ELEMENT, GML.NAMESPACE, GML.pointMember.getLocalPart());
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()
                        && GML.MultiPoint.getLocalPart().equals(reader.getLocalName())) {
                    // we're done
                    break;
                }
            }
        }
        reader.require(END_ELEMENT, GML.NAMESPACE, GML.MultiPoint.getLocalPart());

        Geometry geom = geomFac.createMultiPoint(points.toArray(new Point[points.size()]));
        return geom;
    }

    /**
     * Parses a MultiLineString.
     *
     * <p>Precondition: parser positioned at a {@link GML#MultiLineString MultiLineString} start tag
     *
     * <p>Postcondition: parser positioned at the {@link GML#MultiLineString MultiLineString} end
     * tag of the starting tag
     */
    private MultiLineString parseMultiLineString(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        reader.require(START_ELEMENT, GML.NAMESPACE, GML.MultiLineString.getLocalPart());

        List<LineString> lines = new ArrayList<>(2);

        while (true) {
            reader.nextTag();
            if (END_ELEMENT == reader.getEventType()
                    && GML.MultiLineString.getLocalPart().equals(reader.getLocalName())) {
                // we're done
                break;
            }
            reader.require(START_ELEMENT, GML.NAMESPACE, GML.lineStringMember.getLocalPart());
            reader.nextTag();
            reader.require(START_ELEMENT, GML.NAMESPACE, GML.LineString.getLocalPart());

            LineString line = parseLineString(dimension, crs);
            lines.add(line);
            reader.nextTag();
            reader.require(END_ELEMENT, GML.NAMESPACE, GML.lineStringMember.getLocalPart());
        }

        reader.require(END_ELEMENT, GML.NAMESPACE, GML.MultiLineString.getLocalPart());

        MultiLineString geom =
                geomFac.createMultiLineString(lines.toArray(new LineString[lines.size()]));
        return geom;
    }

    /**
     * Parses a MultiPolygon out of a MultiSurface element (because our geometry model only supports
     * MultiPolygon).
     *
     * <p>Precondition: parser positioned at a {@link GML#MultiSurface MultiSurface} start tag
     *
     * <p>Postcondition: parser positioned at the {@link GML#MultiSurface MultiSurface} end tag of
     * the starting tag
     */
    private Geometry parseMultiSurface(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        reader.require(START_ELEMENT, GML.NAMESPACE, GML.MultiSurface.getLocalPart());

        reader.nextTag();
        final QName memberTag = reader.getName();
        List<Polygon> polygons = new ArrayList<>(2);
        if (GML.surfaceMembers.equals(memberTag)) {
            while (true) {
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()
                        && GML.surfaceMembers.getLocalPart().equals(reader.getLocalName())) {
                    // we're done
                    break;
                }
                Polygon p = parsePolygon(dimension, crs);
                polygons.add(p);
            }
            reader.nextTag();
        } else if (GML.surfaceMember.equals(memberTag)) {
            while (true) {
                reader.nextTag();
                Polygon p = parsePolygon(dimension, crs);
                polygons.add(p);
                reader.nextTag();
                reader.require(END_ELEMENT, GML.NAMESPACE, GML.surfaceMember.getLocalPart());
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()
                        && GML.MultiSurface.getLocalPart().equals(reader.getLocalName())) {
                    // we're done
                    break;
                }
            }
        }
        reader.require(END_ELEMENT, GML.NAMESPACE, GML.MultiSurface.getLocalPart());

        Geometry geom = geomFac.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
        return geom;
    }

    private Geometry parseMultiPolygon(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        reader.require(START_ELEMENT, GML.NAMESPACE, GML.MultiPolygon.getLocalPart());

        List<Polygon> polygons = new ArrayList<>(2);
        reader.nextTag();
        while (true) {
            reader.require(START_ELEMENT, GML.NAMESPACE, GML.polygonMember.getLocalPart());
            reader.nextTag();
            reader.require(START_ELEMENT, GML.NAMESPACE, GML.Polygon.getLocalPart());
            Polygon p = parsePolygon(dimension, crs);
            polygons.add(p);
            reader.nextTag();
            reader.require(END_ELEMENT, GML.NAMESPACE, GML.polygonMember.getLocalPart());
            reader.nextTag();
            if (END_ELEMENT == reader.getEventType()
                    && GML.MultiPolygon.getLocalPart().equals(reader.getLocalName())) {
                // we're done
                break;
            }
        }
        reader.require(END_ELEMENT, GML.NAMESPACE, GML.MultiPolygon.getLocalPart());

        Geometry geom = geomFac.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
        return geom;
    }

    /**
     * Parses a polygon.
     *
     * <p>Precondition: parser positioned at a {@link GML#Polygon Polygon} start tag
     *
     * <p>Postcondition: parser positioned at the {@link GML#Polygon Polygon} end tag of the
     * starting tag
     */
    private Polygon parsePolygon(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {
        LinearRing shell;
        List<LinearRing> holes = null;

        reader.nextTag();
        reader.require(START_ELEMENT, GML.NAMESPACE, null);

        QName name = reader.getName();

        if (GML.exterior.equals(name)) {
            reader.nextTag();
            shell = parseLinearRing(dimension, crs);
            reader.nextTag();
            reader.require(END_ELEMENT, GML.NAMESPACE, GML.exterior.getLocalPart());
        } else if (GML.outerBoundaryIs.equals(name)) {
            reader.nextTag();
            reader.require(START_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());
            shell = parseLinearRing(dimension, crs);
            reader.nextTag();
            reader.require(END_ELEMENT, GML.NAMESPACE, GML.outerBoundaryIs.getLocalPart());
        } else {
            throw new IllegalStateException("Unknown polygon boundary element: " + name);
        }

        reader.nextTag();

        name = reader.getName();

        if (START_ELEMENT == reader.getEventType()) {
            if (GML.interior.equals(name) || GML.innerBoundaryIs.equals(name)) {
                // parse interior rings
                holes = new ArrayList<>(2);
                while (true) {
                    reader.require(START_ELEMENT, GML.NAMESPACE, name.getLocalPart());
                    reader.nextTag();
                    reader.require(START_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());

                    LinearRing hole = parseLinearRing(dimension, crs);

                    reader.require(END_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());

                    holes.add(hole);

                    reader.nextTag();
                    reader.require(END_ELEMENT, GML.NAMESPACE, name.getLocalPart());
                    reader.nextTag();
                    if (END_ELEMENT == reader.getEventType()) {
                        // we're done
                        reader.require(END_ELEMENT, GML.NAMESPACE, GML.Polygon.getLocalPart());
                        break;
                    }
                }
            }
        }

        reader.require(END_ELEMENT, GML.NAMESPACE, GML.Polygon.getLocalPart());

        LinearRing[] holesArray = null;
        if (holes != null) {
            holesArray = holes.toArray(new LinearRing[holes.size()]);
        }
        Polygon geom = geomFac.createPolygon(shell, holesArray);
        geom.setUserData(crs);
        return geom;
    }

    private LinearRing parseLinearRing(final int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {
        reader.require(START_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());

        crs = crs(crs);
        Coordinate[] lineCoords = parseLineStringInternal(dimension, crs);

        reader.require(END_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());

        LinearRing linearRing = geomFac.createLinearRing(lineCoords);
        linearRing.setUserData(crs);
        return linearRing;
    }

    private LineString parseLineString(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        reader.require(START_ELEMENT, GML.NAMESPACE, GML.LineString.getLocalPart());

        crs = crs(crs);
        Coordinate[] coordinates = parseLineStringInternal(dimension, crs);

        reader.require(END_ELEMENT, GML.NAMESPACE, GML.LineString.getLocalPart());

        LineString geom = geomFac.createLineString(coordinates);
        geom.setUserData(crs);
        return geom;
    }

    private Coordinate[] parseLineStringInternal(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException {

        final QName lineElementName = reader.getName();

        reader.nextTag();
        Coordinate[] lineCoords;

        final QName coordsName = reader.getName();
        String tagName = reader.getLocalName();
        if (GML.pos.equals(coordsName)) {
            Coordinate[] point;
            List<Coordinate> coords = new ArrayList<>();
            int eventType;
            do {
                point = parseCoordList(dimension, crs);
                coords.add(point[0]);
                reader.nextTag();
                tagName = reader.getLocalName();
                eventType = reader.getEventType();
            } while (eventType == START_ELEMENT && GML.pos.getLocalPart().equals(tagName));

            lineCoords = coords.toArray(new Coordinate[coords.size()]);

        } else if (GML.posList.equals(coordsName)) {
            // parser.require(START_ELEMENT, GML.NAMESPACE,
            // GML.posList.getLocalPart());
            lineCoords = parseCoordList(dimension, crs);
            reader.nextTag();
        } else if (GML.coordinates.equals(coordsName)) {
            lineCoords = parseCoordinates(dimension, crs);
            reader.nextTag();
        } else if (GML.coord.equals(coordsName)) {
            Coordinate point;
            List<Coordinate> coords = new ArrayList<>();
            int eventType;
            do {
                point = parseCoord();
                coords.add(point);
                reader.nextTag();
                tagName = reader.getLocalName();
                eventType = reader.getEventType();
            } while (eventType == START_ELEMENT && GML.coord.getLocalPart().equals(tagName));

            lineCoords = coords.toArray(new Coordinate[coords.size()]);
        } else {
            throw new IllegalStateException(
                    "Expected posList or pos inside LinearRing: " + tagName);
        }
        reader.require(
                END_ELEMENT, lineElementName.getNamespaceURI(), lineElementName.getLocalPart());
        return lineCoords;
    }

    private Point parsePoint(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        reader.require(START_ELEMENT, GML.NAMESPACE, GML.Point.getLocalPart());

        crs = crs(crs);

        reader.nextTag();
        reader.require(START_ELEMENT, GML.NAMESPACE, null);
        Coordinate point;
        if (GML.pos.getLocalPart().equals(reader.getLocalName())) {
            Coordinate[] coords = parseCoordList(dimension, crs);
            point = coords[0];
            reader.nextTag();
        } else if (GML.coordinates.getLocalPart().equals(reader.getLocalName())) {
            Coordinate[] coords = parseCoordinates(dimension, crs);
            point = coords[0];
            reader.nextTag();
        } else if (GML.coord.getLocalPart().equals(reader.getLocalName())) {
            point = parseCoord();
            reader.nextTag();
        } else {
            throw new IllegalStateException(
                    "Unknown coordinate element for Point: " + reader.getLocalName());
        }

        reader.require(END_ELEMENT, GML.NAMESPACE, GML.Point.getLocalPart());

        Point geom = geomFac.createPoint(point);
        geom.setUserData(crs);
        return geom;
    }

    private Coordinate parseCoord() throws XMLStreamException, IOException {
        reader.require(START_ELEMENT, GML.NAMESPACE, GML.coord.getLocalPart());

        double z = 0;
        reader.nextTag();
        reader.require(START_ELEMENT, GML.NAMESPACE, "X");

        double x = Double.parseDouble(reader.getElementText());

        reader.nextTag();
        reader.require(START_ELEMENT, GML.NAMESPACE, "Y");

        double y = Double.parseDouble(reader.getElementText());

        reader.nextTag();
        if (START_ELEMENT == reader.getEventType()) {
            reader.require(START_ELEMENT, GML.NAMESPACE, "Z");
            z = Double.parseDouble(reader.getElementText());
            reader.nextTag();
        }
        reader.require(END_ELEMENT, GML.NAMESPACE, GML.coord.getLocalPart());
        Coordinate point = new Coordinate(x, y, z);
        return point;
    }

    private CoordinateReferenceSystem crs(CoordinateReferenceSystem defaultValue)
            throws NoSuchAuthorityCodeException, FactoryException {
        String srsName = reader.getAttributeValue(null, "srsName");
        if (srsName == null) {
            return defaultValue;
        }
        // boolean forceXY = false;
        // forceXY=true breaks axis flipping handling
        if (srsName.startsWith("http://") && srsName.indexOf('#') != -1) {
            // forceXY = true;
            srsName = "EPSG:" + srsName.substring(1 + srsName.lastIndexOf('#'));
        }
        CoordinateReferenceSystem crs = CRS.decode(srsName); // , forceXY);
        return crs;
    }

    private int crsDimension(final int defaultValue) {
        String srsDimension = reader.getAttributeValue(null, "srsDimension");
        if (srsDimension == null) {
            return defaultValue;
        }
        int dimension = Integer.valueOf(srsDimension);
        return dimension;
    }

    private Coordinate[] parseCoordList(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException {
        // we might be on a posList tag with srsDimension defined
        dimension = crsDimension(dimension);
        String rawTextValue = reader.getElementText();
        Coordinate[] coords = toCoordList(rawTextValue, dimension, crs);
        return coords;
    }

    private Coordinate[] parseCoordinates(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException {
        reader.require(START_ELEMENT, GML.NAMESPACE, GML.coordinates.getLocalPart());
        // we might be on a posList tag with srsDimension defined
        dimension = crsDimension(dimension);

        String decimalSeparator = reader.getAttributeValue("", "decimal");
        if (decimalSeparator == null) { // default
            decimalSeparator = ".";
        }
        String coordSeparator = reader.getAttributeValue("", "cs");
        if (coordSeparator == null) { // default
            coordSeparator = ",";
        }
        String tupleSeparator = reader.getAttributeValue("", "ts");
        if (tupleSeparator == null) { // default
            tupleSeparator = " ";
        }

        String rawTextValue = reader.getElementText();
        Coordinate[] coords =
                toCoordList(
                        rawTextValue,
                        decimalSeparator,
                        coordSeparator,
                        tupleSeparator,
                        dimension,
                        crs);

        reader.require(END_ELEMENT, GML.NAMESPACE, GML.coordinates.getLocalPart());
        return coords;
    }

    private boolean checkInvertAxisNeededCache(final CoordinateReferenceSystem crs) {
        if (invertAxisNeeded == null) {
            return false;
        }

        Boolean invert = invertAxisNeededCache.get(crs);
        if (invert == null) {
            invert = this.invertAxisNeeded.test(crs);
            invertAxisNeededCache.put(crs, invert);
        }
        return invert;
    }

    private Coordinate[] toCoordList(
            String rawTextValue, final int dimension, CoordinateReferenceSystem crs) {
        rawTextValue = rawTextValue.trim();
        rawTextValue = rawTextValue.replaceAll("\n", " ");
        rawTextValue = rawTextValue.replaceAll("\r", " ");
        String[] split = rawTextValue.trim().split(" +");
        final int ordinatesLength = split.length;
        if (ordinatesLength % dimension != 0) {
            throw new IllegalArgumentException(
                    "Number of ordinates ("
                            + ordinatesLength
                            + ") does not match crs dimension: "
                            + dimension);
        }
        boolean invertXY = this.checkInvertAxisNeededCache(crs);
        final int nCoords = ordinatesLength / dimension;
        Coordinate[] coords = new Coordinate[nCoords];
        Coordinate coord;
        int currCoordIdx = 0;
        double x, y, z;
        for (int i = 0; i < ordinatesLength; i += dimension) {
            x = Double.valueOf(split[i]);
            y = Double.valueOf(split[i + 1]);
            if (dimension > 2) {
                z = Double.valueOf(split[i + 2]);
                if (invertXY) {
                    coord = new Coordinate(y, x, z);
                } else {
                    coord = new Coordinate(x, y, z);
                }
            } else {
                if (invertXY) {
                    coord = new Coordinate(y, x);
                } else {
                    coord = new Coordinate(x, y);
                }
            }
            coords[currCoordIdx] = coord;
            currCoordIdx++;
        }
        return coords;
    }

    private Coordinate[] toCoordList(
            String rawTextValue,
            final String decimalSeparator,
            final String coordSeparator,
            final String tupleSeparator,
            final int dimension,
            CoordinateReferenceSystem crs) {

        rawTextValue = rawTextValue.replaceAll("[\n\r]", " ").trim();

        String[] tuples = rawTextValue.split("\\" + tupleSeparator + "+");

        final int nCoords = tuples.length;

        Coordinate[] coords = new Coordinate[nCoords];
        Coordinate coord;

        boolean invertXY = this.checkInvertAxisNeededCache(crs);

        double x, y, z;

        for (int i = 0; i < nCoords; i++) {
            String tuple = tuples[i];
            String[] oridnates = tuple.split("\\" + coordSeparator + "+");
            double[] parsedOrdinates = new double[oridnates.length];
            for (int o = 0; o < oridnates.length; o++) {
                String ordinate = oridnates[o];
                if (!".".equals(decimalSeparator)) {
                    String[] split = ordinate.split("\\" + decimalSeparator);
                    ordinate = new StringBuilder(split[0]).append('.').append(split[1]).toString();
                }
                parsedOrdinates[o] = Double.parseDouble(ordinate);
            }

            x = parsedOrdinates[0];
            y = parsedOrdinates[1];
            if (dimension > 2 && parsedOrdinates.length > 2) {
                z = parsedOrdinates[2];
                if (invertXY) {
                    coord = new Coordinate(y, x, z);
                } else {
                    coord = new Coordinate(x, y, z);
                }
            } else {
                if (invertXY) {
                    coord = new Coordinate(y, x);
                } else {
                    coord = new Coordinate(x, y);
                }
            }
            coords[i] = coord;
        }
        return coords;
    }
}
