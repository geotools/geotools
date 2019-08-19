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
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/primitive/SurfaceBoundaryImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** LiteGO1 implementation of the SurfaceBoundary interface. */
public class SurfaceBoundaryImpl extends PrimitiveBoundaryImpl implements SurfaceBoundary {
    private static final long serialVersionUID = 8658623156496260842L;

    private Ring exterior;
    private List interior;

    public SurfaceBoundaryImpl(CoordinateReferenceSystem crs, Ring exterior, List interior) {
        super(crs);
        this.exterior = exterior;
        this.interior = interior;
    }

    public SurfaceBoundaryImpl(CoordinateReferenceSystem crs, Ring exterior, Ring[] interior) {
        super(crs);
        this.exterior = exterior;
        this.interior = new ArrayList(Arrays.asList(interior));
    }

    /** Returns the exterior ring, or {@code null} if none. */
    public Ring getExterior() {
        return exterior;
    }

    /** Returns the interior rings. */
    public List getInteriors() {
        return interior;
    }
}
