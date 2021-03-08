/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.data.wfs.internal.parsers;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.MultiCurve;
import org.geotools.gml3.GML;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.Converters;
import org.geotools.wfs.WFS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A {@link GetParser<SimpleFeature>} implementation that uses plain xml pull to parse a GetFeature
 * response.
 *
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 *     <p>
 */
@SuppressWarnings("nls")
public class XmlSimpleFeatureParser implements GetParser<SimpleFeature> {

    private static final Logger LOGGER = Loggers.RESPONSES;

    private GeometryFactory geomFac = new GeometryFactory();

    private InputStream inputStream;

    private XMLStreamReader parser;

    private SimpleFeatureType targetType;

    private SimpleFeatureBuilder builder;

    final String featureNamespace;

    final String featureName;

    private final Map<String, AttributeDescriptor> expectedProperties;

    private int numberOfFeatures = -1;

    private final String axisOrder;

    public XmlSimpleFeatureParser(
            final InputStream getFeatureResponseStream,
            final SimpleFeatureType targetType,
            QName featureDescriptorName,
            String axisOrder)
            throws IOException {

        // this.inputStream = new TeeInputStream(inputStream, System.err);

        this.inputStream = getFeatureResponseStream;
        this.featureNamespace = featureDescriptorName.getNamespaceURI();
        this.featureName = featureDescriptorName.getLocalPart();
        this.targetType = targetType;
        this.builder = new SimpleFeatureBuilder(targetType);
        this.axisOrder = axisOrder;

        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            // disable DTDs
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            // disable external entities
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            parser = factory.createXMLStreamReader(inputStream, "UTF-8");
            // position at root element
            while (parser.hasNext()) {
                if (START_ELEMENT == parser.next()) {
                    break;
                }
            }
            parser.require(START_ELEMENT, null, WFS.FeatureCollection.getLocalPart());

            String nof = parser.getAttributeValue(null, "numberOfFeatures");
            if (nof != null) {
                try {
                    this.numberOfFeatures = Integer.valueOf(nof);
                } catch (NumberFormatException nfe) {
                    LOGGER.warning("Can't parse numberOfFeatures out of " + nof);
                }
            }
        } catch (XMLStreamException e) {
            throw new DataSourceException(e);
        }

        // HACK! use a case insensitive set to compare the comming attribute names with the ones in
        // the schema. Rationale being that the FGDC CubeWerx server has a missmatch in the case of
        // property names between what it states in a DescribeFeatureType and in a GetFeature
        // requests
        expectedProperties = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (AttributeDescriptor desc : targetType.getAttributeDescriptors()) {
            expectedProperties.put(desc.getLocalName(), desc);
        }
    }

    @Override
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        if (null != geometryFactory) {
            this.geomFac = geometryFactory;
        }
    }

    @Override
    public FeatureType getFeatureType() {
        return targetType;
    }

    @Override
    public int getNumberOfFeatures() {
        return numberOfFeatures;
    }

    @Override
    public void close() throws IOException {
        if (this.inputStream != null) {
            try {
                this.parser.close();
                this.parser = null;
                this.inputStream.close();
                this.inputStream = null;
            } catch (XMLStreamException e) {
                throw new DataSourceException(e);
            }
        }
    }

    @Override
    public SimpleFeature parse() throws IOException {
        final String fid;
        try {
            fid = seekFeature();
            if (fid == null) {
                return null;
            }
            int tagType;
            Object attributeValue;
            while (true) {
                tagType = parser.next();
                if (END_DOCUMENT == tagType) {
                    close();
                    return null;
                }

                if (END_ELEMENT == tagType
                        && featureNamespace.equals(parser.getNamespaceURI())
                        && featureName.equals(parser.getLocalName())) {
                    // found end of current feature
                    break;
                }
                if (START_ELEMENT == tagType) {
                    AttributeDescriptor descriptor = expectedProperties.get(parser.getLocalName());
                    if (descriptor != null) {
                        attributeValue = parseAttributeValue();
                        builder.set(descriptor.getLocalName(), attributeValue);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new DataSourceException(e);
        }
        SimpleFeature feature = builder.buildFeature(fid);
        return feature;
    }

    /**
     * Parses the value of the current attribute, parser cursor shall be on a feature attribute
     * START_ELEMENT event.
     */
    private Object parseAttributeValue() throws XMLStreamException, IOException {
        final String name = parser.getLocalName();
        final AttributeDescriptor attribute = expectedProperties.get(name);
        final AttributeType type = attribute.getType();
        Object parsedValue;
        if (type instanceof GeometryType) {
            parser.nextTag();
            try {
                parsedValue = parseGeom();
            } catch (FactoryException e) {
                throw new DataSourceException(e);
            }
        } else {
            String rawTextValue = parser.getElementText();
            Class<?> binding = type.getBinding();
            parsedValue = Converters.convert(rawTextValue, binding);
        }
        return parsedValue;
    }

    /**
     * Precondition: parser cursor positioned on a geometry property (ej, {@code gml:Point}, etc)
     *
     * <p>Postcondition: parser gets positioned at the end tag of the element it started parsing the
     * geometry at
     */
    private Geometry parseGeom()
            throws NoSuchAuthorityCodeException, FactoryException, XMLStreamException, IOException {
        final QName startingGeometryTagName = parser.getName();
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

        parser.require(
                END_ELEMENT,
                startingGeometryTagName.getNamespaceURI(),
                startingGeometryTagName.getLocalPart());

        return geom;
    }

    private Geometry parseMultiCurve(int dimension, CoordinateReferenceSystem crs)
            throws IOException, NoSuchAuthorityCodeException, FactoryException, XMLStreamException {

        parser.require(START_ELEMENT, GML.NAMESPACE, GML.MultiCurve.getLocalPart());

        List<LineString> lines = new ArrayList<>(2);

        while (true) {
            parser.nextTag();
            if (END_ELEMENT == parser.getEventType()
                    && GML.MultiCurve.getLocalPart().equals(parser.getLocalName())) {
                // we're done
                break;
            }
            parser.require(START_ELEMENT, GML.NAMESPACE, GML.curveMember.getLocalPart());
            parser.nextTag();
            final QName startingGeometryTagName =
                    new QName(parser.getNamespaceURI(), parser.getLocalName());
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

            parser.nextTag();
            parser.require(END_ELEMENT, GML.NAMESPACE, GML.curveMember.getLocalPart());
        }

        parser.require(END_ELEMENT, GML.NAMESPACE, GML.MultiCurve.getLocalPart());

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
        parser.nextTag();
        final QName memberTag = parser.getName();
        List<Point> points = new ArrayList<>(4);
        if (GML.pointMembers.equals(memberTag)) {
            while (true) {
                parser.nextTag();
                if (END_ELEMENT == parser.getEventType()
                        && GML.pointMembers.getLocalPart().equals(parser.getLocalName())) {
                    // we're done
                    break;
                }
                Point p = parsePoint(dimension, crs);
                points.add(p);
            }
            parser.nextTag();
        } else if (GML.pointMember.equals(memberTag)) {
            while (true) {
                parser.nextTag();
                parser.require(START_ELEMENT, GML.NAMESPACE, GML.Point.getLocalPart());

                Point p = parsePoint(dimension, crs);
                points.add(p);
                parser.nextTag();
                parser.require(END_ELEMENT, GML.NAMESPACE, GML.pointMember.getLocalPart());
                parser.nextTag();
                if (END_ELEMENT == parser.getEventType()
                        && GML.MultiPoint.getLocalPart().equals(parser.getLocalName())) {
                    // we're done
                    break;
                }
            }
        }
        parser.require(END_ELEMENT, GML.NAMESPACE, GML.MultiPoint.getLocalPart());

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

        parser.require(START_ELEMENT, GML.NAMESPACE, GML.MultiLineString.getLocalPart());

        List<LineString> lines = new ArrayList<>(2);

        while (true) {
            parser.nextTag();
            if (END_ELEMENT == parser.getEventType()
                    && GML.MultiLineString.getLocalPart().equals(parser.getLocalName())) {
                // we're done
                break;
            }
            parser.require(START_ELEMENT, GML.NAMESPACE, GML.lineStringMember.getLocalPart());
            parser.nextTag();
            parser.require(START_ELEMENT, GML.NAMESPACE, GML.LineString.getLocalPart());

            LineString line = parseLineString(dimension, crs);
            lines.add(line);
            parser.nextTag();
            parser.require(END_ELEMENT, GML.NAMESPACE, GML.lineStringMember.getLocalPart());
        }

        parser.require(END_ELEMENT, GML.NAMESPACE, GML.MultiLineString.getLocalPart());

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

        parser.require(START_ELEMENT, GML.NAMESPACE, GML.MultiSurface.getLocalPart());

        parser.nextTag();
        final QName memberTag = parser.getName();
        List<Polygon> polygons = new ArrayList<>(2);
        if (GML.surfaceMembers.equals(memberTag)) {
            while (true) {
                parser.nextTag();
                if (END_ELEMENT == parser.getEventType()
                        && GML.surfaceMembers.getLocalPart().equals(parser.getLocalName())) {
                    // we're done
                    break;
                }
                Polygon p = parsePolygon(dimension, crs);
                polygons.add(p);
            }
            parser.nextTag();
        } else if (GML.surfaceMember.equals(memberTag)) {
            while (true) {
                parser.nextTag();
                Polygon p = parsePolygon(dimension, crs);
                polygons.add(p);
                parser.nextTag();
                parser.require(END_ELEMENT, GML.NAMESPACE, GML.surfaceMember.getLocalPart());
                parser.nextTag();
                if (END_ELEMENT == parser.getEventType()
                        && GML.MultiSurface.getLocalPart().equals(parser.getLocalName())) {
                    // we're done
                    break;
                }
            }
        }
        parser.require(END_ELEMENT, GML.NAMESPACE, GML.MultiSurface.getLocalPart());

        Geometry geom = geomFac.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
        return geom;
    }

    private Geometry parseMultiPolygon(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        parser.require(START_ELEMENT, GML.NAMESPACE, GML.MultiPolygon.getLocalPart());

        List<Polygon> polygons = new ArrayList<>(2);
        parser.nextTag();
        while (true) {
            parser.require(START_ELEMENT, GML.NAMESPACE, GML.polygonMember.getLocalPart());
            parser.nextTag();
            parser.require(START_ELEMENT, GML.NAMESPACE, GML.Polygon.getLocalPart());
            Polygon p = parsePolygon(dimension, crs);
            polygons.add(p);
            parser.nextTag();
            parser.require(END_ELEMENT, GML.NAMESPACE, GML.polygonMember.getLocalPart());
            parser.nextTag();
            if (END_ELEMENT == parser.getEventType()
                    && GML.MultiPolygon.getLocalPart().equals(parser.getLocalName())) {
                // we're done
                break;
            }
        }
        parser.require(END_ELEMENT, GML.NAMESPACE, GML.MultiPolygon.getLocalPart());

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

        parser.nextTag();
        parser.require(START_ELEMENT, GML.NAMESPACE, null);

        QName name = parser.getName();

        if (GML.exterior.equals(name)) {
            parser.nextTag();
            shell = parseLinearRing(dimension, crs);
            parser.nextTag();
            parser.require(END_ELEMENT, GML.NAMESPACE, GML.exterior.getLocalPart());
        } else if (GML.outerBoundaryIs.equals(name)) {
            parser.nextTag();
            parser.require(START_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());
            shell = parseLinearRing(dimension, crs);
            parser.nextTag();
            parser.require(END_ELEMENT, GML.NAMESPACE, GML.outerBoundaryIs.getLocalPart());
        } else {
            throw new IllegalStateException("Unknown polygon boundary element: " + name);
        }

        parser.nextTag();

        name = parser.getName();

        if (START_ELEMENT == parser.getEventType()) {
            if (GML.interior.equals(name) || GML.innerBoundaryIs.equals(name)) {
                // parse interior rings
                holes = new ArrayList<>(2);
                while (true) {
                    parser.require(START_ELEMENT, GML.NAMESPACE, name.getLocalPart());
                    parser.nextTag();
                    parser.require(START_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());

                    LinearRing hole = parseLinearRing(dimension, crs);

                    parser.require(END_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());

                    holes.add(hole);

                    parser.nextTag();
                    parser.require(END_ELEMENT, GML.NAMESPACE, name.getLocalPart());
                    parser.nextTag();
                    if (END_ELEMENT == parser.getEventType()) {
                        // we're done
                        parser.require(END_ELEMENT, GML.NAMESPACE, GML.Polygon.getLocalPart());
                        break;
                    }
                }
            }
        }

        parser.require(END_ELEMENT, GML.NAMESPACE, GML.Polygon.getLocalPart());

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
        parser.require(START_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());

        crs = crs(crs);
        Coordinate[] lineCoords = parseLineStringInternal(dimension, crs);

        parser.require(END_ELEMENT, GML.NAMESPACE, GML.LinearRing.getLocalPart());

        LinearRing linearRing = geomFac.createLinearRing(lineCoords);
        linearRing.setUserData(crs);
        return linearRing;
    }

    private LineString parseLineString(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        parser.require(START_ELEMENT, GML.NAMESPACE, GML.LineString.getLocalPart());

        crs = crs(crs);
        Coordinate[] coordinates = parseLineStringInternal(dimension, crs);

        parser.require(END_ELEMENT, GML.NAMESPACE, GML.LineString.getLocalPart());

        LineString geom = geomFac.createLineString(coordinates);
        geom.setUserData(crs);
        return geom;
    }

    private Coordinate[] parseLineStringInternal(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException {

        final QName lineElementName = parser.getName();

        parser.nextTag();
        Coordinate[] lineCoords;

        final QName coordsName = parser.getName();
        String tagName = parser.getLocalName();
        if (GML.pos.equals(coordsName)) {
            Coordinate[] point;
            List<Coordinate> coords = new ArrayList<>();
            int eventType;
            do {
                point = parseCoordList(dimension, crs);
                coords.add(point[0]);
                parser.nextTag();
                tagName = parser.getLocalName();
                eventType = parser.getEventType();
            } while (eventType == START_ELEMENT && GML.pos.getLocalPart().equals(tagName));

            lineCoords = coords.toArray(new Coordinate[coords.size()]);

        } else if (GML.posList.equals(coordsName)) {
            // parser.require(START_ELEMENT, GML.NAMESPACE,
            // GML.posList.getLocalPart());
            lineCoords = parseCoordList(dimension, crs);
            parser.nextTag();
        } else if (GML.coordinates.equals(coordsName)) {
            lineCoords = parseCoordinates(dimension, crs);
            parser.nextTag();
        } else if (GML.coord.equals(coordsName)) {
            Coordinate point;
            List<Coordinate> coords = new ArrayList<>();
            int eventType;
            do {
                point = parseCoord();
                coords.add(point);
                parser.nextTag();
                tagName = parser.getLocalName();
                eventType = parser.getEventType();
            } while (eventType == START_ELEMENT && GML.coord.getLocalPart().equals(tagName));

            lineCoords = coords.toArray(new Coordinate[coords.size()]);
        } else {
            throw new IllegalStateException(
                    "Expected posList or pos inside LinearRing: " + tagName);
        }
        parser.require(
                END_ELEMENT, lineElementName.getNamespaceURI(), lineElementName.getLocalPart());
        return lineCoords;
    }

    private Point parsePoint(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException, NoSuchAuthorityCodeException, FactoryException {

        parser.require(START_ELEMENT, GML.NAMESPACE, GML.Point.getLocalPart());

        crs = crs(crs);

        parser.nextTag();
        parser.require(START_ELEMENT, GML.NAMESPACE, null);
        Coordinate point;
        if (GML.pos.getLocalPart().equals(parser.getLocalName())) {
            Coordinate[] coords = parseCoordList(dimension, crs);
            point = coords[0];
            parser.nextTag();
        } else if (GML.coordinates.getLocalPart().equals(parser.getLocalName())) {
            Coordinate[] coords = parseCoordinates(dimension, crs);
            point = coords[0];
            parser.nextTag();
        } else if (GML.coord.getLocalPart().equals(parser.getLocalName())) {
            point = parseCoord();
            parser.nextTag();
        } else {
            throw new IllegalStateException(
                    "Unknown coordinate element for Point: " + parser.getLocalName());
        }

        parser.require(END_ELEMENT, GML.NAMESPACE, GML.Point.getLocalPart());

        Point geom = geomFac.createPoint(point);
        geom.setUserData(crs);
        return geom;
    }

    private Coordinate parseCoord() throws XMLStreamException, IOException {
        parser.require(START_ELEMENT, GML.NAMESPACE, GML.coord.getLocalPart());

        double z = 0;
        parser.nextTag();
        parser.require(START_ELEMENT, GML.NAMESPACE, "X");

        double x = Double.parseDouble(parser.getElementText());

        parser.nextTag();
        parser.require(START_ELEMENT, GML.NAMESPACE, "Y");

        double y = Double.parseDouble(parser.getElementText());

        parser.nextTag();
        if (START_ELEMENT == parser.getEventType()) {
            parser.require(START_ELEMENT, GML.NAMESPACE, "Z");
            z = Double.parseDouble(parser.getElementText());
            parser.nextTag();
        }
        parser.require(END_ELEMENT, GML.NAMESPACE, GML.coord.getLocalPart());
        Coordinate point = new Coordinate(x, y, z);
        return point;
    }

    private CoordinateReferenceSystem crs(CoordinateReferenceSystem defaultValue)
            throws NoSuchAuthorityCodeException, FactoryException {
        String srsName = parser.getAttributeValue(null, "srsName");
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
        String srsDimension = parser.getAttributeValue(null, "srsDimension");
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
        String rawTextValue = parser.getElementText();
        Coordinate[] coords = toCoordList(rawTextValue, dimension, crs);
        return coords;
    }

    private Coordinate[] parseCoordinates(int dimension, CoordinateReferenceSystem crs)
            throws XMLStreamException, IOException {
        parser.require(START_ELEMENT, GML.NAMESPACE, GML.coordinates.getLocalPart());
        // we might be on a posList tag with srsDimension defined
        dimension = crsDimension(dimension);

        String decimalSeparator = parser.getAttributeValue("", "decimal");
        if (decimalSeparator == null) { // default
            decimalSeparator = ".";
        }
        String coordSeparator = parser.getAttributeValue("", "cs");
        if (coordSeparator == null) { // default
            coordSeparator = ",";
        }
        String tupleSeparator = parser.getAttributeValue("", "ts");
        if (tupleSeparator == null) { // default
            tupleSeparator = " ";
        }

        String rawTextValue = parser.getElementText();
        Coordinate[] coords =
                toCoordList(
                        rawTextValue,
                        decimalSeparator,
                        coordSeparator,
                        tupleSeparator,
                        dimension,
                        crs);

        parser.require(END_ELEMENT, GML.NAMESPACE, GML.coordinates.getLocalPart());
        return coords;
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
        boolean invertXY = WFSConfig.invertAxisNeeded(axisOrder, crs);
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

        boolean invertXY = WFSConfig.invertAxisNeeded(axisOrder, crs);

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

    private String seekFeature() throws IOException, XMLStreamException {
        int tagType;

        while (true) {
            tagType = parser.next();
            if (tagType == END_DOCUMENT) {
                close();
                return null;
            }
            if (START_ELEMENT != tagType) {
                continue;
            }
            if (START_ELEMENT == tagType) {
                String namespace = parser.getNamespaceURI();
                String name = parser.getLocalName();
                if (featureNamespace.equals(namespace) && featureName.equals(name)) {
                    String featureId =
                            parser.getAttributeValue(
                                    GML.id.getNamespaceURI(), GML.id.getLocalPart());

                    if (featureId == null) {
                        featureId = parser.getAttributeValue(null, "fid");
                    }
                    // Mapserver hack
                    if (featureId == null) {
                        featureId = parser.getAttributeValue(null, "id");
                    }
                    return featureId;
                }
            }
        }
    }
}
