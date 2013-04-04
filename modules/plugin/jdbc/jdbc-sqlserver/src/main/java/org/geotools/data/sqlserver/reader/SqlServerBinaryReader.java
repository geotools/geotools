package org.geotools.data.sqlserver.reader;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ByteArrayInStream;
import com.vividsolutions.jts.io.ByteOrderDataInStream;
import com.vividsolutions.jts.io.ByteOrderValues;
import com.vividsolutions.jts.io.InStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Decode Sql Server binary format to JTS
 *
 * @author Anders Bakkevold, Bouvet
 *
 * @source $URL$
 */
public class SqlServerBinaryReader {

    private ByteOrderDataInStream dis = new ByteOrderDataInStream();
    private GeometryFactory gf = new GeometryFactory();
    private SqlServerBinary binary;

    public SqlServerBinaryReader() {
        this.gf = new GeometryFactory();
    }

    public SqlServerBinaryReader(GeometryFactory gf) {
        this.gf = gf;
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
                return decodePolygon(shapeIndex);
            case MULTILINESTRING:
                return decodeMultiLinestring(shapeIndex);
            case MULTIPOINT:
                return decodeMultiPoint(shapeIndex);
            case MULTIPOLYGON:
                return decodeMultiPolygon(shapeIndex);
            default:
                throw new SqlServerBinaryParseException("Geometry type unsupported " + type);
        }
    }

    private Geometry decodeMultiPolygon( int shapeIndex) {
        Collection<Geometry> polygons = new ArrayList<Geometry>();
        for (int i = shapeIndex; i < binary.getShapes().length; i++) {
            if (binary.getShape(i).getParentOffset() == shapeIndex) {
                polygons.add(gf.createPolygon(binary.getSequence(binary.getShape(i).getFigureOffset())));
            }
        }
        return gf.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));        }

    private Geometry decodeMultiPoint(int shapeIndex) {
        Collection<Geometry> points = new ArrayList<Geometry>();
        for (int i = shapeIndex; i < binary.getShapes().length; i++) {
            if (binary.getShape(i).getParentOffset() == shapeIndex) {
                points.add(gf.createPoint(binary.getSequence(binary.getShape(i).getFigureOffset())));
            }
        }
        return gf.createMultiPoint(points.toArray(new Point[points.size()]));
    }

    private Geometry decodeMultiLinestring(int shapeIndex) {
        Collection<Geometry> linestrings = new ArrayList<Geometry>();
        for (int i = shapeIndex; i < binary.getShapes().length; i++) {
            if (binary.getShape(i).getParentOffset() == shapeIndex) {
                linestrings.add(gf.createLineString(binary.getSequence(binary.getShape(i).getFigureOffset())));
            }
        }
        return gf.createMultiLineString(linestrings.toArray(new LineString[linestrings.size()]));
    }

    private Geometry decodePolygon(int shapeIndex) {
        Shape shape = binary.getShape(shapeIndex);
        int figureOffset = shape.getFigureOffset();
        int figureStopIndex = binary.getFigures().length-1;
        if (shapeIndex +1 < binary.getShapes().length) {
            Shape nextShape = binary.getShape(shapeIndex + 1);
            figureStopIndex = nextShape.getFigureOffset()-1;
        }
        List<LinearRing> linearRings = new ArrayList<LinearRing>();
        if (figureOffset <= -1) {
            return gf.createPolygon(new Coordinate[0]);
        }
        for (int i = figureOffset; i <= figureStopIndex; i++) {
            CoordinateSequence sequence = binary.getSequence(i);
            linearRings.add(gf.createLinearRing(sequence));
        }
        LinearRing outerShell = linearRings.remove(0);
        LinearRing[] holes = linearRings.toArray(new LinearRing[linearRings.size()]);
        return gf.createPolygon(outerShell, holes);
    }

    private Geometry decodeLinestring(int shapeIndex) {
        Shape shape = binary.getShape(shapeIndex);
        CoordinateSequence sequence = binary.getSequence(shape.getFigureOffset());
        return gf.createLineString(sequence);
    }

    private Geometry decodePoint(int shapeIndex) {
        Shape shape = binary.getShapes()[shapeIndex];
        Coordinate coordinate;
        if (binary.isSinglePoint()) {
            coordinate = binary.getCoordinates()[0];
        }  else if (shape.getParentOffset() != -1) {
            Figure figure = binary.getFigure(shape.getFigureOffset());
            coordinate = binary.getCoordinates()[figure.getPointOffset()];
        } else {
            coordinate = null;
        }
        return gf.createPoint(coordinate);
    }

    private Geometry decodeGeometryCollection(int shapeIndex) throws SqlServerBinaryParseException {
        Collection<Geometry> geometries = new ArrayList<Geometry>();
        for (int i = shapeIndex +1; i < binary.getShapes().length; i++) {
            Shape subShape = binary.getShapes()[i];
            if (subShape.getParentOffset() == shapeIndex) {
                geometries.add(decode(i, subShape.getType()));
            }
        }
        return gf.buildGeometry(geometries);
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
        CoordinateSequence[] sequences = new CoordinateSequence[figures.length];
        for (int i = 0; i < figures.length; i++) {
            int figurePointOffset = figures[i].getPointOffset();
            int nextPointOffset = figures.length >= i+2 ? figures[i+1].getPointOffset() : binary.getCoordinates().length;
            Coordinate[] coordinates = Arrays.copyOfRange(binary.getCoordinates(), figurePointOffset, nextPointOffset);
            int attribute = figures[i].getAttribute();
            if ((attribute == 0 || attribute == 2) && !coordinates[0].equals(coordinates[coordinates.length-1]) ) {
                coordinates = Arrays.copyOf(coordinates, coordinates.length + 1);
                coordinates[coordinates.length-1] = coordinates[0];
            }
            sequences[i] = gf.getCoordinateSequenceFactory().create(coordinates);
        }
        binary.setSequences(sequences);
    }

    private void parse(InStream is) throws IOException {
        dis.setInStream(is);
        dis.setOrder(ByteOrderValues.LITTLE_ENDIAN);
        binary.setSrid(dis.readInt());
        byte version = dis.readByte();
        if (version != 1) {
            throw new SqlServerBinaryParseException("Unsupported version (only supports version 1): " + version);
        }
        binary.setSerializationProperties(dis.readByte());

        readNumberOfPoints();
        readCoordinates();
        readZValues();
        readMValues();

        if (binary.isSinglePoint()) {
            binary.setFigures(new Figure[] { new Figure(1,0) });
            binary.setShapes(new Shape[] { new Shape(-1,0,2)});
        } else if (binary.hasSingleLineSegment()) {
            binary.setFigures(new Figure[] { new Figure(1,0) });
            binary.setShapes(new Shape[] { new Shape(-1,0,1)});
        } else {
            readFigures();
            readShapes();
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
        for(int i = 0; i<binary.getNumberOfPoints(); i++) {
            coordinates[i] = readCoordinate();
        }
        binary.setCoordinates(coordinates);
    }

    private void readShapes() throws IOException {
        int numberOfShapes;Shape[] shapesMetadata;
        numberOfShapes = dis.readInt();
        shapesMetadata = new Shape[numberOfShapes];
        for (int i = 0; i < numberOfShapes; i++) {
            int parentOffset = dis.readInt();
            int figureOffset = dis.readInt();
            int shapeType =  dis.readByte();
            shapesMetadata[i] = new Shape(parentOffset, figureOffset, shapeType);
        }
        binary.setShapes(shapesMetadata);
    }

    private void readFigures() throws IOException {
        int numberOfFigures;Figure[] figuresMetadata;
        numberOfFigures = dis.readInt();
        figuresMetadata = new Figure[numberOfFigures];
        for (int i = 0; i < numberOfFigures; i++) {
            byte figureAttribute = dis.readByte();
            int figurePointOffset = dis.readInt();
            figuresMetadata[i] = new Figure(figureAttribute,figurePointOffset);
        }
        binary.setFigures(figuresMetadata);
    }

    private void readMValues() throws IOException {
        //measure values are currently discarded, as they cannot be represented in a JTS Geometry
        if (binary.hasM()) {
            for (int i = 0; i < binary.getNumberOfPoints(); i++) {
                dis.readDouble();
            }
        }
    }

    private void readZValues() throws IOException {
        if (binary.hasZ()) {
            for (int i = 0; i < binary.getNumberOfPoints(); i++) {
                binary.getCoordinates()[i].z = dis.readDouble();
            }
        }
    }

    private Coordinate readCoordinate() throws IOException {
        return new Coordinate(dis.readDouble(), dis.readDouble());
    }
}
