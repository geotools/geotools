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

/**
 * Specifies how a generic OGC simple geometry handler should behave.
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class SubHandler {
    /** Indicates start of a geometry. */
    public static final int GEOMETRY_START = 1;

    /** Indicates end of a geometry. */
    public static final int GEOMETRY_END = 2;

    /** Indicates a sub geometry message. */
    public static final int GEOMETRY_SUB = 3;

    private String srs = null;
    
    /**
     * @return the srs
     */
    protected String getSRS() {
        return srs;
    }
    /**
     * Creates a basic SRID by looking at the provided srs.
     * <p>
     * As an example "EPSG:4326" would be turned into 4326
     * 
     * @return An int value based on the srs field, or 0
     */
    protected int getSRID(){
        if( srs == null ) return 0;
        String split[] = srs.split("\\:");
        try {
            return Integer.parseInt( split[ split.length-1]);
        }
        catch( NumberFormatException ignore ){
            return 0; // probably some complicated OGC SRS
        }
    }
    
    public void setSRS( String SRS ){
        srs = SRS;
    }
    
    /**
     * Adds a coordinate to the object being built if appropriate.
     *
     * @param coordinate Coordinate to add
     */
    public abstract void addCoordinate(
        com.vividsolutions.jts.geom.Coordinate coordinate);

    /**
     * Tells the handler that it just saw a subhandler.
     *
     * @param message The sub geometry message (i.e. isInnerBoundary).
     * @param type The type of sub message (start, end, etc.)
     */
    public void subGeometry(String message, int type) {
    }

    /**
     * Determines whether or not the geometry is ready to return.
     *
     * @param message The geometry to inspect.
     *
     * @return Flag for a complete geometry.
     */
    public abstract boolean isComplete(String message);

    /**
     * Creates a new JTS geometry.
     *
     * @param geometryFactory The JTS geometry factory to use for geometry
     *        creation.
     *
     * @return An OGC simple geometry type for return.
     */
    public abstract com.vividsolutions.jts.geom.Geometry create(
        com.vividsolutions.jts.geom.GeometryFactory geometryFactory);

    /**
     * Describes the handler.
     *
     * @return String representation of the current handler.
     */
    public String toString() {
        String name = this.getClass().getName();
        int index = name.lastIndexOf('.');

        return name.substring(index + 1);
    }
}
