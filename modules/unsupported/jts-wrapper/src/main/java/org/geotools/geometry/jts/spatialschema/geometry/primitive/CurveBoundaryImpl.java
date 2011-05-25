/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/primitive/CurveBoundaryImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.Point;

/**
 * This is Chris's implementation of a CurveBoundary.  I started it and
 * realized about halfway through that I won't necessarily need it.  So the
 * last few methods are still unimplemented (and just delegate to the
 * superclass, which currently does nothing).
 *
 *
 * @source $URL$
 */
public class CurveBoundaryImpl extends PrimitiveBoundaryImpl implements CurveBoundary {
    
    //*************************************************************************
    //  
    //*************************************************************************
    /**
     * Comment for {@code EMPTY_COMPLEX_ARRAY}.
     */
    private static final Complex [] EMPTY_COMPLEX_ARRAY = new Complex[0];

    //*************************************************************************
    //  
    //*************************************************************************
    
    private Point startPoint;
    
    private Point endPoint;
    
    private Set pointSet;

    //*************************************************************************
    //  
    //*************************************************************************
    
    public CurveBoundaryImpl(
            final CoordinateReferenceSystem crs, 
            final Point startPoint, 
            final Point endPoint) {
        
        super(crs);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        HashSet tempSet = new HashSet();
        if (startPoint != null) {
            tempSet.add(startPoint);
        }
        if (endPoint != null) { 
            tempSet.add(endPoint);
        }
        this.pointSet = Collections.unmodifiableSet(tempSet);
    }

    //*************************************************************************
    //  
    //*************************************************************************

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveBoundary#getStartPoint()
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveBoundary#getEndPoint()
     */
    public Point getEndPoint() {
        return endPoint;
    }
}
