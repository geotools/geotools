package com.vividsolutions.jts.geom;

public class EmptyGeometry extends Geometry {

    public EmptyGeometry() {
        super(new GeometryFactory());
    }

    @Override
    public String getGeometryType() {       
        return null;
    }

    @Override
    public Coordinate getCoordinate() {        
        return null;
    }

    @Override
    public Coordinate[] getCoordinates() {
        return null;
    }

    @Override
    public int getNumPoints() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public Geometry getBoundary() {
        return null;
    }

    @Override
    public int getBoundaryDimension() {
        return 0;
    }

    @Override
    public Geometry reverse() {
        return null;
    }

    @Override
    public boolean equalsExact(Geometry other, double tolerance) {
        return false;
    }

    @Override
    public void apply(CoordinateFilter filter) {
        

    }

    @Override
    public void apply(CoordinateSequenceFilter filter) {
        
    }

    @Override
    public void apply(GeometryFilter filter) {
        
    }

    @Override
    public void apply(GeometryComponentFilter filter) {
        
    }

    @Override
    public void normalize() {
        
    }

    @Override
    protected Envelope computeEnvelopeInternal() {
        return null;
    }

    @Override
    protected int compareToSameClass(Object o) {
        return 0;
    }

    @Override
    protected int compareToSameClass(Object o, CoordinateSequenceComparator comp) {
        return 0;
    }
    
    @Override
    public String toString()
    {
        return "";
    }
    

}
