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
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/geometry/GenericCurveImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

// OpenGIS direct dependencies

import org.geotools.geometry.jts.spatialschema.geometry.JTSGeometry;
import org.opengis.geometry.coordinate.GenericCurve;

/**
 * The {@code GenericCurveImpl} class/interface...
 *
 * @author SYS Technologies
 * @author crossley
 * @version $Revision $
 */
public abstract class GenericCurveImpl implements GenericCurve, JTSGeometry {

    // *************************************************************************
    //  fields
    // *************************************************************************

    private org.locationtech.jts.geom.Geometry jtsPeer;

    protected JTSGeometry parent;

    public final void setParent(JTSGeometry parent) {
        this.parent = parent;
    }

    /** Subclasses must override this method to compute the JTS equivalent of this geometry. */
    protected abstract org.locationtech.jts.geom.Geometry computeJTSPeer();

    /**
     * This method must be called by subclasses whenever the user makes a change to the geometry so
     * that the cached JTS object can be recreated.
     */
    public final void invalidateCachedJTSPeer() {
        jtsPeer = null;
        if (parent != null) parent.invalidateCachedJTSPeer();
    }

    /**
     * This method is meant to be invoked by the JTSUtils utility class when it creates a Geometry
     * from a JTS geometry. This prevents the Geometry from having to recompute the JTS peer the
     * first time.
     */
    protected final void setJTSPeer(org.locationtech.jts.geom.Geometry g) {
        jtsPeer = g;
    }

    /**
     * Returns the JTS version of this geometry. If the geometry has not changed since the last time
     * this method was called, it will return the exact same object.
     */
    public final org.locationtech.jts.geom.Geometry getJTSGeometry() {
        if (jtsPeer == null) {
            jtsPeer = computeJTSPeer();
        }
        return jtsPeer;
    }
}
