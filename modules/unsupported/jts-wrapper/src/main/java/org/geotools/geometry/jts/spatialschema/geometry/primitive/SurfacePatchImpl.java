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
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/primitive/SurfacePatchImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

// OpenGIS direct dependencies

import org.geotools.geometry.jts.spatialschema.geometry.JTSGeometry;
import org.geotools.geometry.jts.spatialschema.geometry.geometry.GenericSurfaceImpl;
import org.locationtech.jts.geom.Geometry;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.SurfaceInterpolation;
import org.opengis.geometry.primitive.SurfacePatch;

/**
 * Defines a homogeneous portion of a {@linkplain Surface surface}. Each {@code SurfacePatch} shall
 * be in at most one {@linkplain Surface surface}. @UML type GM_SurfacePatch
 *
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 */
public abstract class SurfacePatchImpl extends GenericSurfaceImpl
        implements SurfacePatch, JTSGeometry {
    @SuppressWarnings("PMD.UnusedPrivateField")
    private Surface surface;

    private SurfaceInterpolation interpolation;
    private SurfaceBoundary boundary;
    private org.locationtech.jts.geom.Geometry jtsPeer;

    public SurfacePatchImpl(SurfaceInterpolation interpolation, SurfaceBoundary boundary) {
        this.interpolation = interpolation;
        this.boundary = boundary;
    }

    /**
     * Returns the patch which own this surface patch.
     *
     * <blockquote>
     *
     * <font size=2> <strong>NOTE:</strong> In this specification, surface patches do not appear
     * except in the context of a surface, and therefore this method should never returns {@code
     * null} which would preclude the use of surface patches except in this manner. While this would
     * not affect this specification, allowing {@code null} owner allows other standards based on
     * this one to use surface patches in a more open-ended manner. </font>
     *
     * </blockquote>
     *
     * @return The owner of this surface patch, or {@code null} if none. @UML association surface
     * @see Surface#getPatches
     */
    public Surface getSurface() {
        return null;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    /**
     * Determines the surface interpolation mechanism used for this {@code SurfacePatch}. This
     * mechanism uses the control points and control parameters defined in the various subclasses to
     * determine the position of this {@code SurfacePatch}.
     *
     * @return The interpolation mechanism. @UML operation interpolation
     */
    public SurfaceInterpolation getInterpolation() {
        return interpolation;
    }

    /**
     * Specifies the type of continuity between this surface patch and its immediate neighbors with
     * which it shares a boundary curve. The sequence of values corresponds to the {@linkplain Ring
     * rings} in the {@linkplain SurfaceBoundary surface boundary} returned by {@link #getBoundary}
     * for this patch. The default value of "0" means simple continuity, which is a mandatory
     * minimum level of continuity. This level is referred to as "C<sup>0</sup>" in mathematical
     * texts. A value of 1 means that the functions are continuous and differentiable at the
     * appropriate end point: "C<sup>1</sup>" continuity. A value of "n" for any integer means
     * <var>n</var>-times differentiable: "C<sup>n</sup>" continuity.
     *
     * @return The type of continuity between this surface patch and its immediate neighbors. @UML
     *     operation numDerivativesOnBoundary
     */
    public abstract int getNumDerivativesOnBoundary();

    /** @see com.polexis.lite.spatialschema.geometry.primitive.SurfacePatchImpl#getBoundary() */
    public SurfaceBoundary getBoundary() {
        return boundary;
    }

    /**
     * @see
     *     com.polexis.lite.spatialschema.geometry.geometry.GenericSurfaceImpl#getUpNormal(org.opengis.geometry.coordinate.DirectPosition)
     */
    public final double[] getUpNormal(DirectPosition point) {
        return new double[] {0, 0, 1};
    }

    /** @see com.polexis.lite.spatialschema.geometry.geometry.GenericSurfaceImpl#getPerimeter() */
    public final double getPerimeter() {
        org.locationtech.jts.geom.Geometry jtsGeom = getJTSGeometry();
        return jtsGeom.getBoundary().getLength();
    }

    public final double getArea() {
        org.locationtech.jts.geom.Geometry jtsGeom = getJTSGeometry();
        return jtsGeom.getArea();
    }

    /**
     * Retrieves the equivalent JTS geometry for this object. Note that this operation may be
     * expensive if the geometry must be computed.
     */
    public Geometry getJTSGeometry() {
        if (jtsPeer == null) {
            jtsPeer = calculateJTSPeer();
        }
        return jtsPeer;
    }

    /**
     * This method is invoked to cause the JTS object to be recalculated the next time it is
     * requested. This method will be called by the underlying guts of the code when something has
     * changed.
     */
    public void invalidateCachedJTSPeer() {
        jtsPeer = null;
    }

    public abstract org.locationtech.jts.geom.Geometry calculateJTSPeer();
}
