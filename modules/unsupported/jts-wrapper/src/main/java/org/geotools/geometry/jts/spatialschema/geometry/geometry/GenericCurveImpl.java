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
import org.opengis.geometry.coordinate.GenericCurve;

import org.geotools.geometry.jts.JTSGeometry;

/**
 * The {@code GenericCurveImpl} class/interface...
 * 
 * @author SYS Technologies
 * @author crossley
 *
 * @source $URL$
 * @version $Revision $
 */
public abstract class GenericCurveImpl implements GenericCurve, JTSGeometry {

    //*************************************************************************
    //  fields
    //*************************************************************************

    private com.vividsolutions.jts.geom.Geometry jtsPeer;

    protected JTSGeometry parent;

    public final void setParent(JTSGeometry parent) {
        this.parent = parent;
    }

    /**
     * Subclasses must override this method to compute the JTS equivalent of
     * this geometry.
     */
    protected abstract com.vividsolutions.jts.geom.Geometry computeJTSPeer();

    /**
     * This method must be called by subclasses whenever the user makes a change
     * to the geometry so that the cached JTS object can be recreated.
     */
    public final void invalidateCachedJTSPeer() {
        jtsPeer = null;
        if (parent != null) parent.invalidateCachedJTSPeer();
    }

    /**
     * This method is meant to be invoked by the JTSUtils utility class when it
     * creates a Geometry from a JTS geometry.  This prevents the Geometry from
     * having to recompute the JTS peer the first time.
     */
    protected final void setJTSPeer(com.vividsolutions.jts.geom.Geometry g) {
        jtsPeer = g;
    }

    /**
     * Returns the JTS version of this geometry.  If the geometry has not
     * changed since the last time this method was called, it will return the
     * exact same object.
     */
    public final com.vividsolutions.jts.geom.Geometry getJTSGeometry() {
        if (jtsPeer == null) {
            jtsPeer = computeJTSPeer();
        }
        return jtsPeer;
    }
}
