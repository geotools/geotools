/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 */
package org.geotools.data.oracle.sdo;

/**
 * ETYPE is a set of constants used to describe Oracle SDO Geometries.
 * 
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: jgarnett $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public interface ETYPE {
    /** <code>ETYPE</code> code representing custom geometries (like splines) */
    public static final int CUSTOM = 0;

    /** <code>ETYPE</code> code representing Point */
    public static final int POINT = 1;

    /** <code>ETYPE</code> code representing Line */
    public static final int LINE = 2;

    /** <code>ETYPE</code> code representing Polygon (not recommended) */
    public static final int POLYGON = 3;

    /** <code>ETYPE</code> code representing exterior CCW polygon ring */
    public static final int POLYGON_EXTERIOR = 1003;

    /** <code>ETYPE</code> code representing interior CW polygon ring */
    public static final int POLYGON_INTERIOR = 2003;

    /**
     * <code>ETYPE</code> code representing compound linestring
     * 
     * <p>
     * A compound polygon represents its edges using a combination of sequence
     * of straight and curved edges.
     * </p>
     * 
     * <p>
     * Compound LineString is not representatble as a JTS Geometry
     * </p>
     */
    public static final int COMPOUND = 4;

    /**
     * <code>ETYPE</code> code representing compound polygon.
     * 
     * <p>
     * A compound polygon represents its edge using a combination of sequence
     * of straight and curved edges.
     * </p>
     * 
     * <p>
     * Compound Polygon is not representatble as a JTS Geometry
     * </p>
     */
    public static final int COMPOUND_POLYGON = 5;

    /**
     * <code>ETYPE</code> code representing compound exterior CCW polygon ring
     * 
     * <p>
     * A compound polygon represents its edges using a combination of sequence
     * of straight and curved edges.
     * </p>
     * 
     * <p>
     * Compound Polygon Interior is not representatble as a JTS Geometry
     * </p>
     */
    public static final int COMPOUND_POLYGON_EXTERIOR = 1005;

    /**
     * <code>ETYPE</code> code representing compound interior CW polygon ring
     * 
     * <p>
     * A compound polygon represents its edges using a combination of sequence
     * of straight and curved edges.
     * </p>
     * 
     * <p>
     * Compound Polygon Exterior is not representatble as a JTS Geometry
     * </p>
     */
    public static final int COMPOUND_POLYGON_INTERIOR = 2005;
}
