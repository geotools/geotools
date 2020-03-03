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
 */
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
import org.opengis.geometry.Boundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The abstract root data type for all the data types used to represent the boundary of geometric
 * objects. Any subclass of {@link Geometry} will use a subclass of {@code Boundary} to represent
 * its boundary through the operation {@link Geometry#getBoundary}. By the nature of geometry,
 * boundary objects are cycles. @UML type GM_Boundary
 *
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 */
public class BoundaryImpl extends ComplexImpl implements Boundary {

    // *************************************************************************
    //
    // *************************************************************************

    /** Creates a new {@code BoundaryImpl}. */
    public BoundaryImpl() {
        this(null);
    }

    /** Creates a new {@code BoundaryImpl}. */
    public BoundaryImpl(final CoordinateReferenceSystem crs) {
        super(crs);
    }
}
