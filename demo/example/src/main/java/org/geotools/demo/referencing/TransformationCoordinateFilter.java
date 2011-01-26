/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.referencing;

import org.geotools.geometry.GeneralDirectPosition;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.CoordinateFilter;

/**
 * A coordinate filter that can be used to tranform each coordinate 
 * in a geometry. This is applied to each individual coordinate,
 * so it does not work well when the polygon crosses 180 + central lat 
 * or 90 + lat of origin. There are also some problems with projections that
 * have issues with 90 lat. 
 *
 * @source $URL$
 * @version $Id:
 * @author rschulz
 */
public class TransformationCoordinateFilter implements CoordinateFilter{
    /* Transform to apply to each coordinate*/
    private MathTransform transform;
    
    /** Creates a new instance of TransformationCoordinateFilter */
    public TransformationCoordinateFilter(MathTransform transform) {
        this.transform = transform;
    }
    
    /*performs a transformation on a coordinate*/
    public void filter(com.vividsolutions.jts.geom.Coordinate coordinate) {
        DirectPosition point = new GeneralDirectPosition(coordinate.x, coordinate.y);
        try {
            point = transform.transform(point, point);
        }
        catch (org.opengis.referencing.operation.TransformException e) {
            System.out.println("Error in transformation: " + e);
        }
        
        coordinate.x = point.getOrdinate(0);
        coordinate.y = point.getOrdinate(1);
    }
    
}
