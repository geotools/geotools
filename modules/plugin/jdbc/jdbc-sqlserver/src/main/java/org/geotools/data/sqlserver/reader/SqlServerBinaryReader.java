/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 */

package org.geotools.data.sqlserver.reader;

import static org.geotools.data.sqlserver.reader.Figure.SequenceType.CURVED;
import static org.geotools.data.sqlserver.reader.Figure.SequenceType.STRAIGHT;
import static org.geotools.data.sqlserver.reader.Segment.ARC;
import static org.geotools.data.sqlserver.reader.Segment.FIRST_ARC;
import static org.geotools.data.sqlserver.reader.Segment.FIRST_LINE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ByteArrayInStream;
import org.locationtech.jts.io.ByteOrderDataInStream;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.InStream;

/**
 * Decode Sql Server binary format to JTS
 *
 * @author Anders Bakkevold, Bouvet
 */
public class SqlServerBinaryReader {

    private ByteOrderDataInStream dis = new ByteOrderDataInStream();
    private GeometryFactory gf;
    private CurvedGeometryFactory cgf;
    private SqlServerBinary binary;

    public SqlServerBinaryReader() {
        this(new GeometryFactory());
    }

    public SqlServerBinaryReader(GeometryFactory gf) {
        this.gf = gf;
        if (gf instanceof CurvedGeometryFactory) {
            cgf = (CurvedGeometryFactory) gf;
        } else {
            cgf = new CurvedGeometryFactory(gf, Double.MAX_VALUE);
        }
    }

    public Geometry read(byte[] bytes) throws IOException {
        this.binary = new SqlServerBinary();
        return read(new ByteArrayInStream(bytes));
    }

    public Geometry read(InStream is) throws IOException {
        parse(is);
        readCoordinateSequences();
        Type type = getTypeFromBinary();
        Geometry geometry = decode(0, type);
        geometry.setSRID(binary.getSrid());
        return geometry;
    }

    private Geometry decode(int shapeIndex, Type type) throws SqlServerBinaryParseException {
        switch (type) {
            case GEOMETRYCOLLECTION:
                return decodeGeometryCollection(shapeIndex);
            case POINT:
                return decodePoint(shapeIndex);
            case LINESTRING:
                return decodeLinestring(shapeIndex);
            case POLYGON:
                return decodeCurvePolygon(shapeIndex);
            case MULTILINESTRING:
                return decodeMultiLinestring(shapeIndex);
            case MULTIPOINT:
                return decodeMultiPoint(shapeIndex);
            case MULTIPOLYGON:
                return decodeMultiPolygon(shapeIndex);
            case CIRCULARSTRING:
                return decodeCircularString(shapeIndex);
            case COMPOUNDCURVE:
                return decodeCompoundCurve(shapeIndex);
            case CURVEPOLYGON:
                return decodeCurvePolygon(shapeIndex);
            default:
                throw new SqlServerBinaryParseException("Geometry type unsupported " + type);
        }
    }

    private Geometry decodeCurvePolygon(int shapeIndex) {
        Shape shape = binary.getShape(shapeIndex);
        int figureOffset = shape.getFigureOffset();
        int figureStopIndex = binary.getFigures().length - 1;
        if (shapeIndex + 1 < binary.getShapes().length) {
            Shape nextShape = binary.getShape(shapeIndex + 1);
            figureStopIndex = nextShape.getFigureOffset() - 1;
        }
        // empty polygon case
        if (figureOffset <= -1) {
            return cgf.createPolygon(new Coordinate[0]);
        }
        LinearRing outerShell = null;
        LinearRing[] holes = new LinearRing[figureStopIndex - figureOffset];
        for (int i = figureOffset; i <= figureStopIndex; i++) {
            Figure figure = binary.getFigure(i);
            CoordinateSequence[] sequences = binary.getSequence(i);
            LinearRing ring;
            // compound case
            if (sequences.length > 1) {
                List<Figure.SequenceType> sequenceTypes = figure.getSequenceTypes();
                List<LineString> components = new ArrayList<>(sequences.length);
                for (int j = 0; j < sequences.length; j++) {
                    CoordinateSequence sequence = sequences[j];
                    if (sequenceTypes.get(j) == CURVED) {
                        components.add(cgf.createCurvedGeometry(sequence));
                    } else {
                        components.add(cgf.createLineString(sequence));
                    }
                }
                ring = (LinearRing) cgf.createCurvedGeometry(components);
            } else {
                if (figure.getAttribute() == 2 && binary.getVersion() == 2) { // arc
                    ring = (LinearRing) cgf.createCurvedGeometry(sequences[0]);
                } else {
                    ring = gf.createLinearRing(sequences[0]);
                }
            }
            if (i == figureOffset) {
                outerShell = ring;
            } else {
                holes[i - figureOffset - 1] = ring;
            }
        }

        return cgf.createPolygon(outerShell, holes);
    }

    private Geometry decodeCircularString(int shapeIndex) {
        Shape shape = binary.getShape(shapeIndex);
        CoordinateSequence sequence = binary.getSequence(shape.getFigureOffset())[0];
        return cgf.createCircularString(sequence);
    }

    private Geometry decodeCompoundCurve(int shapeIndex) throws SqlServerBinaryParseException {
        Shape shape = binary.getShape(shapeIndex);
        Figure figure = binary.getFigure(shape.getFigureOffset());
        CoordinateSequence[] sequences = binary.getSequence(shape.getFigureOffset());
        List<Figure.SequenceType> sequenceTypes = figure.getSequenceTypes();
        List<LineString> components = new ArrayList<>(sequences.length);
        for (int j = 0; j < sequences.length; j++) {
            CoordinateSequence sequence = sequences[j];
            if (sequenceTypes.get(j) == CURVED) {
                components.add(cgf.createCurvedGeometry(sequence));
            } else {
                components.add(cgf.createLineString(sequence));
            }
        }
        return cgf.createCurvedGeometry(components);
    }

    private Geometry decodeMultiPolygon(int shapeIndex) {
        Collection<Geometry> polygons = new ArrayList<Geometry>();
        for (int i = shapeIndex; i < binary.getShapes().length; i++) {
            if (binary.getShape(i).getParentOffset() == shapeIndex) {
                polygons.add(decodeCurvePolygon(i));
            }
        }
        return cgf.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
    }

    private Geometry decodeMultiPoint(int shapeIndex) {
        Collection<Geometry> points = new ArrayList<Geometry>();
        for (int i = shapeIndex; i < binary.getShapes().length; i++) {
            if (binary.getShape(i).getParentOffset() == shapeIndex) {
                points.add(
                        gf.createPoint(
                                binary.getSequence(binary.getShape(i).getFigureOffset())[0]));
            }
        }
        return gf.createMultiPoint(points.toArray(new Point[points.size()]));
    }

    private Geometry decodeMultiLinestring(int shapeIndex) {
        Collection<Geometry> linestrings = new ArrayList<Geometry>();
        for (int i = shapeIndex; i < binary.getShapes().length; i++) {
            if (binary.getShape(i).getParentOffset() == shapeIndex) {
                CoordinateSequence[] sequences =
                        binary.getSequence(binary.getShape(i).getFigureOffset());
                linestrings.add(gf.createLineString(sequences[0]));
            }
        }
        return gf.createMultiLineString(linestrings.toArray(new LineString[linestrings.size()]));
    }

    private Geometry decodeLinestring(int shapeIndex) {
        Shape shape = binary.getShape(shapeIndex);
        CoordinateSequence sequence = binary.getSequence(shape.getFigureOffset())[0];
        return gf.createLineString(sequence);
    }

    private Geometry decodePoint(int shapeIndex) {
        Shape shape = binary.getShapes()[shapeIndex];
        Coordinate coordinate;
        if (binary.isSinglePoint()) {
            coordinate = binary.getCoordinates()[0];
        } else if (shape.getParentOffset() != -1) {
            Figure figure = binary.getFigure(shape.getFigureOffset());
            coordinate = binary.getCoordinates()[figure.getPointOffset()];
        } else {
            coordinate = null;
        }
        return gf.createPoint(coordinate);
    }

    private Geometry decodeGeometryCollection(int shapeIndex) throws SqlServerBinaryParseException {
        Collection<Geometry> geometries = new ArrayList<Geometry>();
        for (int i = shapeIndex + 1; i < binary.getShapes().length; i++) {
            Shape subShape = binary.getShapes()[i];
            if (subShape.getParentOffset() == shapeIndex) {
                geometries.add(decode(i, subShape.getType()));
            }
        }
        return cgf.createGeometryCollection(geometries.toArray(new Geometry[geometries.size()]));
    }

    private Type getTypeFromBinary() {
        if (binary.isSinglePoint()) {
            return Type.POINT;
        }
        if (binary.hasSingleLineSegment()) {
            return Type.LINESTRING;
        }
        return binary.getShapes()[0].getType();
    }

    private void readCoordinateSequences() {
        Figure[] figures = binary.getFigures();
        CoordinateSequence[][] sequences = new CoordinateSequence[figures.length][];
        CoordinateSequenceFactory csFactory = gf.getCoordinateSequenceFactory();
        int segmentIdx = 0;
        for (int i = 0; i < figures.length; i++) {
            Figure figure = figures[i];
            int figurePointOffset = figure.getPointOffset();
            int nextPointOffset =
                    figures.length >= i + 2
                            ? figures[i + 1].getPointOffset()
                            : binary.getCoordinates().length;
            Coordinate[] coordinates =
                    Arrays.copyOfRange(binary.getCoordinates(), figurePointOffset, nextPointOffset);
            int attribute = figure.getAttribute();
            if (binary.getVersion() == 1) {
                if ((attribute == 0 || attribute == 2)
                        && !coordinates[0].equals(coordinates[coordinates.length - 1])) {
                    coordinates = Arrays.copyOf(coordinates, coordinates.length + 1);
                    coordinates[coordinates.length - 1] = coordinates[0];
                }
                sequences[i] = new CoordinateSequence[1];
                sequences[i][0] = csFactory.create(coordinates);
            } else if (binary.getVersion() == 2) {
                if (figure.getAttribute() == 3) {
                    // need to look into the segments, have a mix of straight and curved components
                    List<CoordinateSequence> figureSequences = new ArrayList<>();
                    List<Figure.SequenceType> sequenceTypes = new ArrayList<>();
                    List<Coordinate> sequenceCoordinates = null;
                    Segment[] segments = binary.getSegments();
                    for (int c = 0; c < coordinates.length - 1; ) {
                        Segment segment = segments[segmentIdx++];
                        if (segment == FIRST_ARC || segment == FIRST_LINE) {
                            if (sequenceCoordinates != null) {
                                if (c < coordinates.length - 1) {
                                    sequenceCoordinates.add(coordinates[c]);
                                }
                                CoordinateSequence cs =
                                        csFactory.create(
                                                sequenceCoordinates.toArray(
                                                        new Coordinate
                                                                [sequenceCoordinates.size()]));
                                figureSequences.add(cs);
                            }
                            sequenceCoordinates = new ArrayList<>();
                            sequenceTypes.add(segment == FIRST_ARC ? CURVED : STRAIGHT);
                        }
                        sequenceCoordinates.add(coordinates[c++]);
                        // arc segments are made of 3 points, not two
                        if (segment == FIRST_ARC || segment == ARC) {
                            sequenceCoordinates.add(coordinates[c++]);
                        }
                    }
                    if (sequenceCoordinates != null) {
                        sequenceCoordinates.add(coordinates[coordinates.length - 1]);
                        CoordinateSequence cs =
                                csFactory.create(
                                        sequenceCoordinates.toArray(
                                                new Coordinate[sequenceCoordinates.size()]));
                        figureSequences.add(cs);
                    }
                    sequences[i] =
                            figureSequences.toArray(new CoordinateSequence[figureSequences.size()]);
                    figure.setSequenceTypes(sequenceTypes);
                } else {
                    sequences[i] = new CoordinateSequence[1];
                    sequences[i][0] = csFactory.create(coordinates);
                }
            }
        }
        binary.setSequences(sequences);
    }

    private void parse(InStream is) throws IOException {
        dis.setInStream(is);
        dis.setOrder(ByteOrderValues.LITTLE_ENDIAN);
        binary.setSrid(dis.readInt());
        byte version = dis.readByte();
        if (!(version == 1 | version == 2)) {
            throw new SqlServerBinaryParseException(
                    "Unsupported version (only supports version 1 and 2): " + version);
        }
        binary.setVersion(version);
        binary.setSerializationProperties(dis.readByte());

        readNumberOfPoints();
        readCoordinates();
        readZValues();
        readMValues();

        if (binary.isSinglePoint()) {
            binary.setFigures(new Figure[] {new Figure(1, 0)});
            binary.setShapes(new Shape[] {new Shape(-1, 0, 2)});
        } else if (binary.hasSingleLineSegment()) {
            binary.setFigures(new Figure[] {new Figure(1, 0)});
            binary.setShapes(new Shape[] {new Shape(-1, 0, 1)});
        } else {
            readFigures();
            readShapes();
            readSegments();
        }
    }

    private void readSegments() throws IOException {
        if (binary.getVersion() > 1) {
            if (binary.hasSegments()) {
                int numberOfSegments = dis.readInt();
                Segment[] segments = new Segment[numberOfSegments];
                for (int i = 0; i < numberOfSegments; i++) {
                    segments[i] = Segment.findSegment(dis.readByte());
                }
                binary.setSegments(segments);
            }
        }
    }

    private void readNumberOfPoints() throws IOException {
        if (binary.isSinglePoint()) {
            binary.setNumberOfPoints(1);
        } else if (binary.hasSingleLineSegment()) {
            binary.setNumberOfPoints(2);
        } else {
            binary.setNumberOfPoints(dis.readInt());
        }
    }

    private void readCoordinates() throws IOException {
        Coordinate[] coordinates = new Coordinate[binary.getNumberOfPoints()];
        for (int i = 0; i < binary.getNumberOfPoints(); i++) {
            coordinates[i] = readCoordinate();
        }
        binary.setCoordinates(coordinates);
    }

    private void readShapes() throws IOException {
        int numberOfShapes;
        Shape[] shapesMetadata;
        numberOfShapes = dis.readInt();
        shapesMetadata = new Shape[numberOfShapes];
        for (int i = 0; i < numberOfShapes; i++) {
            int parentOffset = dis.readInt();
            int figureOffset = dis.readInt();
            int shapeType = dis.readByte();
            shapesMetadata[i] = new Shape(parentOffset, figureOffset, shapeType);
        }
        binary.setShapes(shapesMetadata);
    }

    private void readFigures() throws IOException {
        int numberOfFigures;
        Figure[] figuresMetadata;
        numberOfFigures = dis.readInt();
        figuresMetadata = new Figure[numberOfFigures];
        for (int i = 0; i < numberOfFigures; i++) {
            byte figureAttribute = dis.readByte();
            int figurePointOffset = dis.readInt();
            figuresMetadata[i] = new Figure(figureAttribute, figurePointOffset);
        }
        binary.setFigures(figuresMetadata);
    }

    private void readMValues() throws IOException {
        // measure values are currently discarded, as they cannot be represented in a JTS Geometry
        if (binary.hasM()) {
            for (int i = 0; i < binary.getNumberOfPoints(); i++) {
                dis.readDouble();
            }
        }
    }

    private void readZValues() throws IOException {
        if (binary.hasZ()) {
            for (int i = 0; i < binary.getNumberOfPoints(); i++) {
                binary.getCoordinates()[i].setZ(dis.readDouble());
            }
        }
    }

    private Coordinate readCoordinate() throws IOException {
        return new Coordinate(dis.readDouble(), dis.readDouble());
    }
}
