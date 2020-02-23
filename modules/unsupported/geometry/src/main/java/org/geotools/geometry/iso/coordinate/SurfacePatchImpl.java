/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.coordinate;

import java.io.Serializable;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceInterpolation;
import org.opengis.geometry.primitive.SurfacePatch;

/**
 * @author Jackson Roehrig & Sanjay Jena
 *     <p>GM_SurfacePatch (Figure 20) defines a homogeneous portion of a GM_Surface. The
 *     multiplicity of the association "Segmentation" (Figure 12) specifies that each
 *     GM_SurfacePatch shall be in at most one GM_Surface.
 */
public abstract class SurfacePatchImpl implements SurfacePatch, Serializable {

    /**
     * The attribute "interpolation" determines the surface interpolation mechanism used for this
     * GM_SurfacePatch. This mechanism uses the control points and control parameters defined in the
     * various subclasses to determine the position of this GM_ SurfacePatch.
     *
     * <p>GM_SurfacePatch::Interpolation : GM_SurfaceInterpolation Default is Planar interpolation
     */
    private SurfaceInterpolation interpolation = SurfaceInterpolation.PLANAR;

    /**
     * The attribute sequences "numDerivativesOnBoundary" specifies the type of continuity between
     * this surface patch and its immediate neighbors with which it shares a boundary curve. The
     * sequence of values corresponds to the GM_Rings in the GM_SurfaceBoundary returned by
     * GM_GenericCurve::boundary for this patch. The default value of "0" means simple continuity,
     * which is a mandatory minimum level of continuity. This level is referred to as "C0" in
     * mathematical texts. A value of 1 means that the functions are continuous and differentiable
     * at the appropriate end point: "C1" continuity. A value of "n" for any integer means n-times
     * differentiable: "Cn" continuity.
     *
     * <p>GM_SurfacePatch::numDerivativesOnBoundary[0..1] : Integer Default is 0 (simple continuity)
     */
    private int numDerivativesOnBoundary = 0;

    /**
     * Segment association from specification of GM_Surface The "Segmentation" association relates
     * this GM_Surface to a set of GM_SurfacePatches that shall be joined together to form this
     * GM_Surface. Depending on the interpolation method, the set of patches may require significant
     * additional structure. In general, the form of the patches shall be defined in the application
     * schema.
     *
     * <p>GM_Surface::patch [1..n] : GM_SurfacePatch GM_SurfacePatch::surface [0,1] :
     * Reference<GM_Surface>
     *
     * <p>If the GM_Surface.coordinateDimension is 2, then the entire GM_Surface is one logical
     * patch defined by linear interpolation from the boundary.
     *
     * <p>NOTE In this standard, surface patches do not appear except in the context of a surface,
     * and therefore the cardinality of the surface role in this association could be 1 which
     * would preclude the use of surface patches except in this manner. While this would not affect
     * this Standard, leaving the cardinality as 0..1 allows other standards based on this one to
     * use surface patches in a more open-ended manner.
     */
    private SurfaceImpl surface = null;

    /** Boundary of the Surface Patch */
    private SurfaceBoundaryImpl boundary = null;

    /** Constructs the Surface patch and stores the boundary */
    protected SurfacePatchImpl(SurfaceBoundaryImpl boundary) {
        // Throw Exception if Boundary is NULL:
        // Since the boundary is the only information source which contains
        // coordinates, at this point it is impossible to create a boundary
        // the boundary must be build in lower classes like Polygon or Triangle
        // TODO This behaviour must be reviewed again: Probably there are cases when we are not able
        // to create a SurfacePatch by the boundary, for example when creating Triangles which are
        // defined by three positions.
        if (boundary == null)
            throw new IllegalArgumentException("Boundary of SurfacePatch must not be NULL");
        this.boundary = boundary;
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.primitive.SurfacePatch#getInterpolation()
     */
    public SurfaceInterpolation getInterpolation() {
        // ok
        return this.interpolation;
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.primitive.SurfacePatch#getBoundary()
     */
    public SurfaceBoundaryImpl getBoundary() {
        // ok
        return this.boundary;
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.primitive.SurfacePatch#getSurface()
     */
    public Surface getSurface() {
        // ok
        return this.surface;
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.primitive.SurfacePatch#getNumDerivativesOnBoundary()
     */
    public int getNumDerivativesOnBoundary() {
        return this.numDerivativesOnBoundary;
    }

    /**
     * Added to Class requirements of GM_SurfacePatch Returns the envelope of the SurfacePatch
     *
     * @return Envelope of the CurveSegment
     */
    public abstract Envelope getEnvelope();

    /**
     * Sets the reference to the corresponding Surface of this SurfacePatch
     *
     * @param surface Corresponding Surface
     */
    public void setSurface(SurfaceImpl surface) {
        this.surface = surface;
    }
}
