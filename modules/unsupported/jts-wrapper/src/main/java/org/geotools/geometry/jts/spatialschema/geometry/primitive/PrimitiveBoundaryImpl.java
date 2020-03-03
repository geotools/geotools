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
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/primitive/PrimitiveBoundaryImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

// OpenGIS direct dependencies

import org.geotools.geometry.jts.spatialschema.geometry.BoundaryImpl;
import org.opengis.geometry.primitive.PrimitiveBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The boundary of {@linkplain Primitive primitive} objects. This is the root for the various return
 * types of the {@link org.opengis.geometry.coordinate.#getBoundary getBoundary()} method for
 * subtypes of {@link Primitive}. Since points have no boundary, no special subclass is needed for
 * their boundary. @UML type GM_PrimitiveBoundary
 *
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 */
public class PrimitiveBoundaryImpl extends BoundaryImpl implements PrimitiveBoundary {

    // *************************************************************************
    //
    // *************************************************************************

    /** Creates a new {@code PrimitiveBoundaryImpl}. */
    public PrimitiveBoundaryImpl() {
        this(null);
    }

    /** Creates a new {@code PrimitiveBoundaryImpl}. */
    public PrimitiveBoundaryImpl(final CoordinateReferenceSystem crs) {
        super(crs);
    }
}
