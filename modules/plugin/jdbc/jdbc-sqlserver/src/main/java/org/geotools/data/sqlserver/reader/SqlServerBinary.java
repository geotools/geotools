package org.geotools.data.sqlserver.reader;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;

/**
 * Represents the information from a binary sqlserver geometry
 *
 * @author Anders Bakkevold, Bouvet
 *
 * @source $URL$
 */
class SqlServerBinary {

    private int srid;
    private int numberOfPoints;
    private Coordinate[] coordinates;
    private Shape[] shapes;
    private Figure[] figures;
    private CoordinateSequence[] sequences;

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public void setSerializationProperties(byte serializationProperties) {
        this.serializationProperties = serializationProperties;
    }

    public boolean hasZ() {
        return (serializationProperties & 1) == 1;
    }

    public boolean hasM() {
        return (serializationProperties & 2) == 2;
    }

    public boolean isValid(){
        return (serializationProperties & 4) == 4;
    }

    public boolean isSinglePoint() {
        return (serializationProperties & 8) == 8;
    }

    public boolean hasSingleLineSegment() {
        return (serializationProperties & 16) == 16;
    }

    private byte serializationProperties;

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }

    public void setShapes(Shape[] shapes) {
        this.shapes = shapes;
    }

    public void setFigures(Figure[] figures) {
        this.figures = figures;
    }

    public Figure[] getFigures() {
        return figures;
    }

    public void setSequences(CoordinateSequence[] sequences) {
        this.sequences = sequences;
    }

    public Shape[] getShapes() {
        return shapes;
    }

    public Shape getShape(int index) {
        return shapes[index];
    }

    public Figure getFigure(int index) {
        return figures[index];
    }

    public CoordinateSequence getSequence(int index) {
        return sequences[index];
    }
}
