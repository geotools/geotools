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

import java.util.List;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;

/**
 * @author Jackson Roehrig & Sanjay Jena
 *     <p>A Polygon (Figure 21) is a surface patch that is defined by a set of boundary curves and
 *     an underlying surface to which these curves adhere. The default is that the curves are
 *     coplanar and the polygon uses planar interpolation in its interior.
 */
public class PolygonImpl extends SurfacePatchImpl implements Polygon {

    /**
     * The attribute "boundary" stores the SurfaceBoundary that is the boundary of this Polygon.
     *
     * <p>Polygon::boundary : SurfaceBoundary
     *
     * <p>NOTE The boundary of a surface patch need not be in the same Complex as the containing
     * Surface. The curves that are contained in the interior of the Surface (act as common boundary
     * to 2 surface patches) are not part of any Complex in which the Surface is contained. They are
     * purely constructive and would not play in any topological relation between Surface and Curve
     * that defines the connectivity of the Complex.
     *
     * <p>IMPLEMENTATION ANNOTATION: The boundary will be realised and stored within the super class
     * SurfacePatch
     */

    /**
     * The optional spanning surface provides a mechanism for spanning the interior of the polygon.
     *
     * <p>Polygon::spanningSurface [0,1] : Surface
     *
     * <p>NOTE The spanning surface should have no boundary components that intersect the boundary
     * of the polygon, and there should be no ambiguity as to which portion of the surface is
     * described by the bounding curves for the polygon. The most common spanning surface is an
     * elevation model, which is not directly described in this standard, although Tins and gridded
     * surfaces are often used in this role.
     */
    // Spanning surface of the Polygon
    private SurfaceImpl spanningSurface = null;

    // Envelope of the Polygon
    private EnvelopeImpl envelope = null;

    // Array of Neighbours of the Polygon
    // private SurfacePatchImpl m_neighbours[] = null;

    /**
     * Constructor This first variant of a constructor of Polygon creates a Polygon directly from a
     * set of boundary curves (organized into a SurfaceBoundary) which shall be defined using
     * coplanar Positions as controlPoints. Polygon::Polygon(boundary : SurfaceBondary) : Polygon
     *
     * <p>NOTE The meaning of exterior in the SurfaceBoundary is consistent with the plane of the
     * constructed planar polygon.
     */
    public PolygonImpl(SurfaceBoundaryImpl boundary) {
        this(boundary, null);
    }

    /**
     * This second variant of a constructor of Polygon creates a Polygon lying on a spanning
     * surface. There is no restriction of the types of interpolation used by the composite curves
     * used in the SurfaceBoundary, but they must all be lie on the "spanningSurface" for the
     * process to succeed. Polygon(boundary : SurfaceBondary, spanSurf : Surface) : Polygon
     *
     * <p>NOTE It is important that the boundary components be oriented properly for this to work.
     * It is often the case that in bounded manifolds, such as the sphere, there is an ambiguity
     * unless the orientation is properly used.
     *
     * @param spanSurf - the Spanning Surface of the polygon
     */
    public PolygonImpl(SurfaceBoundaryImpl boundary, SurfaceImpl spanSurf) {
        // The Constructor will not except a boundary which is NULL; an
        // exception will shall be thrown in the constructor of the super class
        // SurfacePatch
        // The boundary shall be build in lower classes like Triangle
        /* Call super constructor to store boundary */
        super(boundary);
        /* Set Spanning Surface of the Polygon */
        this.spanningSurface = spanSurf;
        /* Create Envelope of the Polygon */
        this.envelope = (EnvelopeImpl) this.createEnvelope();
    }

    /* (non-Javadoc)
     * @see org.geotools.geometry.featgeom.coordinate.SurfacePatchImpl#getEnvelope()
     */
    public Envelope getEnvelope() {
        return this.envelope;
    }

    /**
     * Creates the Envelope for the Polygon
     *
     * @return Envelope for the Polygon
     */
    private Envelope createEnvelope() {
        /* Return Envelope of the given Surface Patch Boundary */
        return (this.getBoundary() != null) ? this.getBoundary().getEnvelope() : null;
    }

    public PolyhedralSurface getSurface() {
        return (PolyhedralSurface) super.getSurface();
    }

    // /**
    // * This method returns the neighbours of the SurfacePatch within the
    // relative Surface.
    // * The order and number of the returned SurfacePatch Array depends from
    // the type of SurfacePatch and has to be implemented individually.
    // * This method can be useful for example for calculating the boundary of a
    // surface, which is only defined by patches.
    // * @return Ordered Array of neighbours of the SurfacePatch; NULL if
    // neighbours not set
    // */
    // public SurfacePatchImpl[] getNeighbours() {
    // return this.m_neighbours;
    // }

    // /**
    // * Sets the neighbours of this SurfacePatch
    // */
    // public void setNeighbours(SurfacePatchImpl[] neighbourPatches) {
    // this.m_neighbours = neighbourPatches;
    // }

    //	/**
    //	 * spanningSurface The optional spanning surface provides a mechanism for
    //	 * spanning the interior of the polygon.
    //	 *
    //	 * Polygon::spanningSurface [0,1] : Surface
    //	 *
    //	 * NOTE The spanning surface should have no boundary components that
    //	 * intersect the boundary of the polygon, and there should be no ambiguity
    //	 * as to which portion of the surface is described by the bounding curves
    //	 * for the polygon. The most common spanning surface is an elevation model,
    //	 * which is not directly described in this standard, although Tins and
    //	 * gridded surfaces are often used in this role.
    //	 *
    //	 * @return SurfaceImpl
    //	 */
    //	public SurfaceImpl spanningSurface() {
    //		return this.spanningSurface;
    //	}

    //	/**
    //	 * @return double
    //	 */
    //	public double perimeter() {
    //		return this.getBoundary().getLength();
    //	}

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Polygon#getSpanningSurface()
     */
    public List getSpanningSurface() {
        // TODO semantic JR, SJ
        // TODO implementation
        // TODO test
        // TODO documentation
        return null;
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.GenericSurface#getUpNormal(org.opengis.geometry.coordinate.DirectPosition)
     */
    public double[] getUpNormal(DirectPosition point) {
        // TODO semantic JR, SJ
        // TODO implementation
        // TODO test
        // TODO documentation
        return null;
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.GenericSurface#getPerimeter()
     */
    public double getPerimeter() {
        // TODO semantic JR, SJ
        // TODO implementation
        // TODO test
        // TODO documentation
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.GenericSurface#getArea()
     */
    public double getArea() {
        // TODO semantic JR, SJ
        // TODO implementation
        // TODO test
        // TODO documentation
        return 0;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((envelope == null) ? 0 : envelope.hashCode());
        result = PRIME * result + ((spanningSurface == null) ? 0 : spanningSurface.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final PolygonImpl other = (PolygonImpl) obj;
        if (envelope == null) {
            if (other.envelope != null) return false;
        } else if (!envelope.equals(other.envelope)) return false;
        if (spanningSurface == null) {
            if (other.spanningSurface != null) return false;
        } else if (!spanningSurface.equals(other.spanningSurface)) return false;
        return true;
    }
}
