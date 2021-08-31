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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.geometry.jts.CompoundCurvedGeometry;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Parse GML geometries from a StAX XMLStreamReader. */
public class XmlStreamGeometryReader {
    public static final double DEFAULT_CURVE_TOLERANCE = Double.MAX_VALUE;

    private final XMLStreamReader reader;

    private GeometryFactory geomFac;

    private CurvedGeometryFactory curvedGeometryFactory;

    private boolean unsafeXMLAllowed = false;

    private Predicate<CoordinateReferenceSystem> invertAxisNeeded = null;

    private final Map<CoordinateReferenceSystem, Boolean> invertAxisNeededCache = new HashMap<>();

    private String gmlNamespace;

    /**
     * Create a new instance of the XML Stream Geometry Reader.
     *
     * @param reader the reader providing the data to parse
     */
    public XmlStreamGeometryReader(final XMLStreamReader reader) {
        this(reader, new GeometryFactory());
    }

    /**
     * Create a new instance of the XML Stream Geometry Reader.
     *
     * @param reader the reader providing the data to parse
     * @param geometryFactory a specific {@link GeometryFactory} to use for constructing geometries
     */
    public XmlStreamGeometryReader(
            final XMLStreamReader reader, final GeometryFactory geometryFactory) {
        this(
                reader,
                geometryFactory,
                new CurvedGeometryFactory(geometryFactory, DEFAULT_CURVE_TOLERANCE));
    }

    /**
     * Create a new instance of the XML Stream Geometry Reader.
     *
     * @param reader the reader providing the data to parse
     * @param geometryFactory a specific {@link GeometryFactory} to use for constructing geometries
     * @param curvedGeometryFactory a specific {@link CurvedGeometryFactory} to use for constructing
     *     curved geometries
     */
    public XmlStreamGeometryReader(
            final XMLStreamReader reader,
            final GeometryFactory geometryFactory,
            final CurvedGeometryFactory curvedGeometryFactory) {
        this.reader = reader;
        this.geomFac = geometryFactory;
        this.curvedGeometryFactory = curvedGeometryFactory;
    }

    public void setGeometryFactory(GeometryFactory geometryFactory) {
        this.geomFac = geometryFactory;
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
     * Reads the next {@link Geometry} from the stream.
     *
     * <p>Precondition: parser cursor positioned on a geometry property (eg. {@code gml:Point},
     * etc).
     *
     * <p>Postcondition: parser gets positioned at the end tag of the element it started parsing the
     * geometry at.
     *
     * @throws XMLStreamException when the parser cursor is not positioned on the expected element
     * @throws NoSuchAuthorityCodeException when parsing the EPSG code fails
     * @throws FactoryException when finding an EPSG parser fails
     * @throws IOException unkown error
     */
    public Geometry readGeometry()
            throws NoSuchAuthorityCodeException, FactoryException, XMLStreamException, IOException {

        checkUnsafeXML();

        if (GML.NAMESPACE.equals(reader.getNamespaceURI())) {
            this.gmlNamespace = GML.NAMESPACE;
        } else if (GML.NAMESPACE_3_2.equals(reader.getNamespaceURI())) {
            this.gmlNamespace = GML.NAMESPACE_3_2;
        } else {
            throw new IllegalStateException(
                    "Expected a geometry element in the GML namespace but found \""
                            + reader.getName()
                            + "\"");
        }

        final String startingGeometryTagName = reader.getLocalName();
        int dimension = crsDimension(2);
        CoordinateReferenceSystem crs = crs(DefaultGeographicCRS.WGS84);

        Geometry geom;
        if (GML.Point.equals(startingGeometryTagName)) {
            geom = parsePoint(dimension, crs);
        } else if (GML.LineString.equals(startingGeometryTagName)) {
            geom = parseLineString(dimension, crs);
        } else if (GML.Curve.equals(startingGeometryTagName)) {
            geom = parseCurve(dimension, crs);
        } else if (GML.Polygon.equals(startingGeometryTagName)) {
            geom = parsePolygon(dimension, crs);
        } else if (GML.Surface.equals(startingGeometryTagName)) {
            geom = parseSurface(dimension, crs);
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

        reader.require(END_ELEMENT, this.gmlNamespace, startingGeometryTagName);

        return geom;
    }

    private Geometry parseMultiCurve(int dimension, CoordinateReferenceSystem crs)
            throws IOException, NoSuchAuthorityCodeException, FactoryException, XMLStreamException {

        reader.require(START_ELEMENT, this.gmlNamespace, GML.MultiCurve);

        List<LineString> lines = new ArrayList<>(2);

        while (true) {
            reader.nextTag();
            if (END_ELEMENT == reader.getEventType()
                    && GML.MultiCurve.equals(reader.getLocalName())) {
                // we're done
                break;
            }
            reader.require(START_ELEMENT, this.gmlNamespace, GML.curveMember);
            reader.nextTag();
            reader.require(START_ELEMENT, this.gmlNamespace, null);
            final String startingGeometryTagName = reader.getLocalName();
            if (GML.LineString.equals(startingGeometryTagName)) {
                lines.add(parseLineString(dimension, crs));
            } else if (GML.CompositeCurve.equals(startingGeometryTagName)) {
                throw new UnsupportedOperationException(
                        GML.CompositeCurve + " is not supported yet");
            } else if (GML.Curve.equals(startingGeometryTagName)) {
                lines.add(parseCurve(dimension, crs));
            } else if (GML.OrientableCurve.equals(startingGeometryTagName)) {
                throw new UnsupportedOperationException(
                        GML.OrientableCurve + " is not supported yet");
            }

            reader.nextTag();
            reader.require(END_ELEMENT, this.gmlNamespace, GML.curveMember);
        }

        reader.require(END_ELEMENT, this.gmlNamespace, GML.MultiCurve);

        return this.curvedGeometryFactory.createMultiCurve(lines);
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

        reader.require(START_ELEMENT, this.gmlNamespace, GML.MultiPoint);

        reader.nextTag();
        final String memberTag = reader.getLocalName();
        List<Point> points = new ArrayList<>(4);
        if (GML.pointMembers.equals(memberTag)) {
            while (true) {
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()
                        && GML.pointMembers.equals(reader.getLocalName())) {
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
                reader.require(START_ELEMENT, this.gmlNamespace, GML.Point);

                Point p = parsePoint(dimension, crs);
                points.add(p);
                reader.nextTag();
                reader.require(END_ELEMENT, this.gmlNamespace, GML.pointMember);
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()
                        && GML.MultiPoint.equals(reader.getLocalName())) {
                    // we're done
                    break;
                }
            }
        }
        reader.require(END_ELEMENT, this.gmlNamespace, GML.MultiPoint);

        return geomFac.createMultiPoint(points.toArray(new Point[0]));
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

        reader.require(START_ELEMENT, this.gmlNamespace, GML.MultiLineString);

        List<LineString> lines = new ArrayList<>(2);

        while (true) {
            reader.nextTag();
            if (END_ELEMENT == reader.getEventType()
                    && GML.MultiLineString.equals(reader.getLocalName())) {
                // we're done
                break;
            }
            reader.require(START_ELEMENT, this.gmlNamespace, GML.lineStringMember);
            reader.nextTag();
            reader.require(START_ELEMENT, this.gmlNamespace, GML.LineString);

            LineString line = parseLineString(dimension, crs);
            lines.add(line);
            reader.nextTag();
            reader.require(END_ELEMENT, this.gmlNamespace, GML.lineStringMember);
        }

        reader.require(END_ELEMENT, this.gmlNamespace, GML.MultiLineString);

        return geomFac.createMultiLineString(lines.toArray(new LineString[0]));
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

        reader.require(START_ELEMENT, this.gmlNamespace, GML.MultiSurface);

        reader.nextTag();
        final String memberTag = reader.getLocalName();
        List<Polygon> polygons = new ArrayList<>(2);
        if (GML.surfaceMembers.equals(memberTag)) {
            while (true) {
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()
                        && GML.surfaceMembers.equals(reader.getLocalName())) {
                    // we're done
                    break;
                }

                switch (reader.getLocalName()) {
                    case GML.Polygon:
                        Polygon p = parsePolygon(dimension, crs);
                        polygons.add(p);
                        break;
                    case GML.Surface:
                        MultiPolygon mp = parseSurface(dimension, crs);
                        for (int i = 0; i < mp.getNumGeometries(); i++)
                            polygons.add((Polygon) mp.getGeometryN(i));
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unknown polygon boundary element: " + reader.getLocalName());
                }
            }
            reader.nextTag();
        } else if (GML.surfaceMember.equals(memberTag)) {
            while (true) {
                reader.nextTag();
                switch (reader.getLocalName()) {
                    case GML.Polygon:
                        Polygon p = parsePolygon(dimension, crs);
                        polygons.add(p);
                        break;
                    case GML.Surface:
                        MultiPolygon mp = parseSurface(dimension, crs);
                        for (int i = 0; i < mp.getNumGeometries(); i++)
                            polygons.add((Polygon) mp.getGeometryN(i));
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unknown polygon boundary element: " + reader.getLocalName());
                }

                reader.nextTag();
                reader.require(END_ELEMENT, this.gmlNamespace, GML.surfaceMember);
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()
                        && GML.MultiSurface.equals(reader.getLocalName())) {
                    // we're done
                    break;
                }
            }
        }
        reader.require(END_ELEMENT, this.gmlNamespace, GML.MultiSurface);

        return curvedGeometryFactory.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }

    private Geometry parseMultiPolygon(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        reader.require(START_ELEMENT, this.gmlNamespace, GML.MultiPolygon);

        List<Polygon> polygons = new ArrayList<>(2);
        reader.nextTag();
        while (true) {
            reader.require(START_ELEMENT, this.gmlNamespace, GML.polygonMember);
            reader.nextTag();
            reader.require(START_ELEMENT, this.gmlNamespace, GML.Polygon);
            Polygon p = parsePolygon(dimension, crs);
            polygons.add(p);
            reader.nextTag();
            reader.require(END_ELEMENT, this.gmlNamespace, GML.polygonMember);
            reader.nextTag();
            if (END_ELEMENT == reader.getEventType()
                    && GML.MultiPolygon.equals(reader.getLocalName())) {
                // we're done
                break;
            }
        }
        reader.require(END_ELEMENT, this.gmlNamespace, GML.MultiPolygon);

        return geomFac.createMultiPolygon(polygons.toArray(new Polygon[0]));
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
        reader.require(START_ELEMENT, this.gmlNamespace, null);

        String name = reader.getLocalName();

        if (GML.exterior.equals(name)) {
            reader.nextTag();
            shell = parseRing(dimension, crs);
            reader.nextTag();
            reader.require(END_ELEMENT, this.gmlNamespace, GML.exterior);
        } else if (GML.outerBoundaryIs.equals(name)) {
            reader.nextTag();
            reader.require(START_ELEMENT, this.gmlNamespace, GML.LinearRing);
            shell = parseLinearRing(dimension, crs);
            reader.nextTag();
            reader.require(END_ELEMENT, this.gmlNamespace, GML.outerBoundaryIs);
        } else {
            throw new IllegalStateException("Unknown polygon boundary element: " + name);
        }

        reader.nextTag();

        name = reader.getLocalName();

        if (START_ELEMENT == reader.getEventType()) {
            if (GML.interior.equals(name) || GML.innerBoundaryIs.equals(name)) {
                // parse interior rings
                holes = new ArrayList<>(2);
                while (true) {
                    reader.require(START_ELEMENT, this.gmlNamespace, name);
                    reader.nextTag();
                    LinearRing hole = parseRing(dimension, crs);
                    holes.add(hole);

                    reader.nextTag();
                    reader.require(END_ELEMENT, this.gmlNamespace, name);
                    reader.nextTag();
                    if (END_ELEMENT == reader.getEventType()) {
                        // we're done
                        reader.require(END_ELEMENT, this.gmlNamespace, GML.Polygon);
                        break;
                    }
                }
            }
        }

        reader.require(END_ELEMENT, this.gmlNamespace, GML.Polygon);

        LinearRing[] holesArray = null;
        if (holes != null) {
            holesArray = holes.toArray(new LinearRing[0]);
        }
        Polygon geom = curvedGeometryFactory.createPolygon(shell, holesArray);
        geom.setUserData(crs);
        return geom;
    }

    /**
     * Parse a Polygon out of a Surface element.
     *
     * <p>Precondition: parser positioned at a {@link GML#Surface Surface} start tag
     *
     * <p>Postcondition: parser positioned at the {@link GML#Surface Surface} end tag of the
     * starting tag
     */
    private MultiPolygon parseSurface(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, FactoryException, IOException {

        List<Polygon> polygons = new ArrayList<>(2);

        reader.nextTag();
        // patches
        reader.require(START_ELEMENT, this.gmlNamespace, null);
        String patchesName = reader.getLocalName();

        if (GML.patches.equals(patchesName)) {
            while (true) {
                reader.nextTag();
                if (END_ELEMENT == reader.getEventType()) {
                    if (GML.Surface.equals(reader.getLocalName())) {
                        // we're done
                        break;
                    } else if (GML.patches.equals(reader.getLocalName())) {
                        reader.nextTag();
                        break;
                    }
                }
                reader.require(START_ELEMENT, this.gmlNamespace, GML.PolygonPatch);
                Polygon p = parsePolygonPatch(dimension, crs);
                polygons.add(p);
            }
        } else {
            throw new IllegalStateException("Unknown polygon boundary element: " + patchesName);
        }

        reader.require(END_ELEMENT, this.gmlNamespace, GML.Surface);

        return geomFac.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }

    /**
     * Parses a PolygonPatch.
     *
     * <p>Precondition: parser positioned at a {@link GML#PolygonPatch PolygonPatch} start tag
     *
     * <p>Postcondition: parser positioned at the {@link GML#PolygonPatch PolygonPatch} end tag of
     * the starting tag
     */
    private Polygon parsePolygonPatch(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, FactoryException {
        LinearRing shell;
        List<LinearRing> holes = null;

        reader.nextTag();
        reader.require(START_ELEMENT, this.gmlNamespace, null);

        String name = reader.getLocalName();

        if (GML.exterior.equals(name)) {
            reader.nextTag();
            shell = parseRing(dimension, crs);
            reader.nextTag();
            reader.require(END_ELEMENT, this.gmlNamespace, GML.exterior);
        } else {
            throw new IllegalStateException("Unknown polygon boundary element: " + name);
        }

        reader.nextTag();

        name = reader.getLocalName();

        if (START_ELEMENT == reader.getEventType()) {
            if (GML.interior.equals(name)) {
                // parse interior rings
                holes = new ArrayList<>(2);
                while (true) {
                    reader.require(START_ELEMENT, this.gmlNamespace, name);
                    reader.nextTag();
                    LinearRing hole = parseRing(dimension, crs);
                    holes.add(hole);

                    reader.nextTag();
                    reader.require(END_ELEMENT, this.gmlNamespace, name);
                    reader.nextTag();
                    if (END_ELEMENT == reader.getEventType()) {
                        // we're done
                        reader.require(END_ELEMENT, this.gmlNamespace, GML.PolygonPatch);
                        break;
                    }
                }
            }
        }

        reader.require(END_ELEMENT, this.gmlNamespace, GML.PolygonPatch);

        LinearRing[] holesArray = null;
        if (holes != null) {
            holesArray = holes.toArray(new LinearRing[0]);
        }
        Polygon geom = curvedGeometryFactory.createPolygon(shell, holesArray);
        geom.setUserData(crs);
        return geom;
    }

    private LinearRing parseRing(final int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        String name = reader.getLocalName();

        if (GML.LinearRing.equals(name)) {
            return parseLinearRing(dimension, crs);
        }

        reader.require(START_ELEMENT, this.gmlNamespace, GML.Ring);

        List<LineString> components = new ArrayList<>();

        reader.nextTag();
        while (true) {
            reader.require(START_ELEMENT, this.gmlNamespace, GML.curveMember);
            reader.nextTag();

            name = reader.getLocalName();
            reader.require(START_ELEMENT, this.gmlNamespace, null);
            if (GML.LineString.equals(name)) {
                components.add(parseLineString(dimension, crs));
            } else {
                LineString lineString = parseCurve(dimension, crs);
                // We don't want compound curves containing other compound curves
                if (lineString instanceof CompoundCurvedGeometry) {
                    components.addAll(((CompoundCurvedGeometry<?>) lineString).getComponents());
                } else {
                    components.add(lineString);
                }
            }

            reader.nextTag();
            reader.require(END_ELEMENT, this.gmlNamespace, GML.curveMember);
            reader.nextTag();
            if (END_ELEMENT == reader.getEventType() && GML.Ring.equals(reader.getLocalName())) {
                // we're done
                break;
            }
        }
        // createCurvedGeometry() will create a CompoundRing which extends LinearRing
        LinearRing linearRing =
                (LinearRing) this.curvedGeometryFactory.createCurvedGeometry(components);
        linearRing.setUserData(crs);
        return linearRing;
    }

    private LinearRing parseLinearRing(final int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {
        reader.require(START_ELEMENT, this.gmlNamespace, GML.LinearRing);

        crs = crs(crs);
        Coordinate[] lineCoords = parseLineStringInternal(dimension, crs);

        reader.require(END_ELEMENT, this.gmlNamespace, GML.LinearRing);

        LinearRing linearRing = geomFac.createLinearRing(lineCoords);
        linearRing.setUserData(crs);
        return linearRing;
    }

    private LineString parseLineStringSegment(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {
        return parseLineString(dimension, crs, true);
    }

    private LineString parseLineString(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {
        return parseLineString(dimension, crs, false);
    }

    private LineString parseLineString(
            int dimension, CoordinateReferenceSystem crs, final boolean isSegment)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        final String lineStringElement = isSegment ? GML.LineStringSegment : GML.LineString;

        reader.require(START_ELEMENT, this.gmlNamespace, lineStringElement);

        crs = crs(crs);
        Coordinate[] coordinates = parseLineStringInternal(dimension, crs);

        reader.require(END_ELEMENT, this.gmlNamespace, lineStringElement);

        LineString geom = geomFac.createLineString(coordinates);
        geom.setUserData(crs);
        return geom;
    }

    private Coordinate[] parseLineStringInternal(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException {

        final String lineElementName = reader.getLocalName();

        reader.nextTag();
        Coordinate[] lineCoords;

        final String coordsName = reader.getLocalName();
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
            } while (eventType == START_ELEMENT && GML.pos.equals(tagName));

            lineCoords = coords.toArray(new Coordinate[0]);

        } else if (GML.posList.equals(coordsName)) {
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
            } while (eventType == START_ELEMENT && GML.coord.equals(tagName));

            lineCoords = coords.toArray(new Coordinate[0]);
        } else {
            throw new IllegalStateException(
                    "Expected posList or pos inside LinearRing: " + tagName);
        }
        reader.require(END_ELEMENT, this.gmlNamespace, lineElementName);
        return lineCoords;
    }

    private LineString parseCurve(final int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        reader.require(START_ELEMENT, this.gmlNamespace, GML.Curve);

        List<LineString> lines = new ArrayList<>(2);

        reader.nextTag();
        reader.require(START_ELEMENT, this.gmlNamespace, GML.segments);

        reader.nextTag();
        while (true) {
            reader.require(START_ELEMENT, this.gmlNamespace, null);

            final String name = reader.getLocalName();

            if (GML.LineStringSegment.equals(name)) {
                lines.add(parseLineStringSegment(dimension, crs));
            } else if (GML.Arc.equals(name)) {
                lines.add(parseArc(dimension, crs));
            } else {
                throw new UnsupportedOperationException(
                        "Curve segment " + name + " is not supported yet");
            }

            reader.nextTag();

            if (END_ELEMENT == reader.getEventType()
                    && GML.segments.equals(reader.getLocalName())) {
                // we're done
                break;
            }
        }

        reader.nextTag();
        reader.require(END_ELEMENT, this.gmlNamespace, GML.Curve);

        LineString geom = curvedGeometryFactory.createCurvedGeometry(lines);
        geom.setUserData(crs);
        return geom;
    }

    private LineString parseArc(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {
        reader.require(START_ELEMENT, this.gmlNamespace, GML.Arc);

        crs = crs(crs);

        Coordinate[] coordinates = parseLineStringInternal(dimension, crs);

        reader.require(END_ELEMENT, this.gmlNamespace, GML.Arc);

        LineString geom =
                curvedGeometryFactory.createCircularString(
                        curvedGeometryFactory.getCoordinateSequenceFactory().create(coordinates));
        geom.setUserData(crs);
        return geom;
    }

    private Point parsePoint(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        reader.require(START_ELEMENT, this.gmlNamespace, GML.Point);

        crs = crs(crs);

        reader.nextTag();
        reader.require(START_ELEMENT, this.gmlNamespace, null);
        Coordinate point;
        final String name = reader.getLocalName();
        if (GML.pos.equals(name)) {
            Coordinate[] coords = parseCoordList(dimension, crs);
            point = coords[0];
            reader.nextTag();
        } else if (GML.coordinates.equals(name)) {
            Coordinate[] coords = parseCoordinates(dimension, crs);
            point = coords[0];
            reader.nextTag();
        } else if (GML.coord.equals(name)) {
            point = parseCoord();
            reader.nextTag();
        } else {
            throw new IllegalStateException("Unknown coordinate element for Point: " + name);
        }

        reader.require(END_ELEMENT, this.gmlNamespace, GML.Point);

        Point geom = geomFac.createPoint(point);
        geom.setUserData(crs);
        return geom;
    }

    private Coordinate parseCoord() throws XMLStreamException, IOException {
        reader.require(START_ELEMENT, this.gmlNamespace, GML.coord);

        double z = 0;
        reader.nextTag();
        reader.require(START_ELEMENT, this.gmlNamespace, "X");

        double x = Double.parseDouble(reader.getElementText());

        reader.nextTag();
        reader.require(START_ELEMENT, this.gmlNamespace, "Y");

        double y = Double.parseDouble(reader.getElementText());

        reader.nextTag();
        if (START_ELEMENT == reader.getEventType()) {
            reader.require(START_ELEMENT, this.gmlNamespace, "Z");
            z = Double.parseDouble(reader.getElementText());
            reader.nextTag();
        }
        reader.require(END_ELEMENT, this.gmlNamespace, GML.coord);
        return new Coordinate(x, y, z);
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
        return CRS.decode(srsName);
    }

    private int crsDimension(final int defaultValue) {
        String srsDimension = reader.getAttributeValue(null, "srsDimension");
        if (srsDimension == null) {
            return defaultValue;
        }
        return Integer.parseInt(srsDimension);
    }

    private Coordinate[] parseCoordList(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException {
        // we might be on a posList tag with srsDimension defined
        dimension = crsDimension(dimension);
        String rawTextValue = reader.getElementText();
        return toCoordList(rawTextValue, dimension, crs);
    }

    private Coordinate[] parseCoordinates(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException {
        reader.require(START_ELEMENT, this.gmlNamespace, GML.coordinates);
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

        reader.require(END_ELEMENT, this.gmlNamespace, GML.coordinates);
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
            x = Double.parseDouble(split[i]);
            y = Double.parseDouble(split[i + 1]);
            if (dimension > 2) {
                z = Double.parseDouble(split[i + 2]);
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
                    ordinate = split[0] + '.' + split[1];
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
