/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/BoundaryImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry;

// OpenGIS direct dependencies
import org.geotools.geometry.jts.spatialschema.geometry.complex.ComplexImpl;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.Boundary;


/**
 * The abstract root data type for all the data types used to represent the boundary of geometric
 * objects. Any subclass of {@link Geometry} will use a subclass of {@code Boundary} to
 * represent its boundary through the operation {@link Geometry#getBoundary}. By the nature of
 * geometry, boundary objects are cycles.
 *
 * @UML type GM_Boundary
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 * @source $URL$
 * @version 2.0
 */
public class BoundaryImpl extends ComplexImpl implements Boundary {
    
    //*************************************************************************
    //  
    //*************************************************************************
    
    /**
     * Creates a new {@code BoundaryImpl}.
     * 
     */
    public BoundaryImpl() {
        this(null);
    }

    /**
     * Creates a new {@code BoundaryImpl}.
     * @param crs
     */
    public BoundaryImpl(final CoordinateReferenceSystem crs) {
        super(crs);
    }
}
