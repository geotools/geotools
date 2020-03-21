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
package org.geotools.geometry.iso.primitive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geotools.geometry.iso.aggregate.MultiSurfaceImpl;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.coordinate.PolygonImpl;
import org.geotools.geometry.iso.coordinate.SurfacePatchImpl;
import org.geotools.geometry.iso.io.GeometryToString;
import org.geotools.geometry.iso.operation.IsSimpleOp;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.SurfacePatch;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Surface (Figure 12) a subclass of Primitive and is the basis for 2-dimensional geometry.
 * Unorientable surfaces such as the Möbius band are not allowed. The orientation of a surface
 * chooses an "up" direction through the choice of the upward normal, which, if the surface is not a
 * cycle, is the side of the surface from which the exterior boundary appears counterclockwise.
 * Reversal of the surface orientation reverses the curve orientation of each boundary component,
 * and interchanges the conceptual "up" and "down" direction of the surface. If the surface is the
 * boundary of a solid, the "up" direction is usually outward. For closed surfaces, which have no
 * boundary, the up direction is that of the surface patches, which must be consistent with one
 * another. Its included SurfacePatches describe the interior structure of a Surface
 *
 * <p>NOTE Other than the restriction on orientability, no other "validity" condition is required
 * for Surface.
 *
 * @author Jackson Roehrig & Sanjay Jena
 */
public class SurfaceImpl extends OrientableSurfaceImpl implements Surface {
    private static final long serialVersionUID = 2431540523002962079L;

    /**
     * The "Segmentation" association relates this Surface to a set of SurfacePatches that shall be
     * joined together to form this Surface. Depending on the interpolation method, the set of
     * patches may require significant additional structure. In general, the form of the patches
     * shall be defined in the application schema.
     *
     * <p>Surface::patch [1..n] : SurfacePatch SurfacePatch::surface [0,1] : Reference<Surface>
     *
     * <p>If the Surface.coordinateDimension is 2, then the entire Surface is one logical patch
     * defined by linear interpolation from the boundary.
     *
     * <p>NOTE In this standard, surface patches do not appear except in the context of a surface,
     * and therefore the cardinality of the surface role in this association could be 1 which
     * would preclude the use of surface patches except in this manner. While this would not affect
     * this Standard, leaving the cardinality as 0..1 allows other standards based on this one to
     * use surface patches in a more open-ended manner.
     */
    protected ArrayList<? extends SurfacePatch> patch = null;

    private SurfaceBoundary boundary = null;

    private Envelope envelope;

    // /**
    // * Constructor without arguments
    // * Surface Patches have to be setted after
    // */
    // public SurfaceImpl(GeometryFactoryImpl factory) {
    // super(factory);
    // this.patch = null;
    // }

    /**
     * Constructor The first version of the constructor for Surface takes a list of SurfacePatches
     * with the appropriate side-toside relationships and creates a Surface.
     *
     * <p>Surface::Surface(patch[1..n] : SurfacePatch) : Surface
     */
    public SurfaceImpl(CoordinateReferenceSystem crs, List<? extends SurfacePatch> patch) {
        super(crs, null, null, null);
        this.initializeSurface(patch);
    }

    /**
     * Constructor The second version, which is guaranteed to work always in 2D coordinate spaces,
     * constructs a Surface by indicating its boundary as a collection of Curves organized into a
     * SurfaceBoundary. In 3D coordinate spaces, this second version of the constructor shall
     * require all of the defining boundary Curve instances to be coplanar (lie in a single plane)
     * which will define the surface interior.
     *
     * <p>Surface::Surface(bdy : SurfaceBoundary) : Surface
     *
     * @param boundary The SurfaceBoundary which defines the Surface
     */
    public SurfaceImpl(SurfaceBoundary boundary) {

        super(boundary.getCoordinateReferenceSystem(), null, null, null);

        // Set Boundary
        this.boundary = boundary;

        // Set Envelope
        this.envelope = boundary.getEnvelope();

        // TODO Is it really necessary to create the surface patches?
        // Create Surface Patch on basis of the Boundary
        ArrayList<SurfacePatch> newPatchList = new ArrayList<SurfacePatch>();
        newPatchList.add(new PolygonImpl((SurfaceBoundaryImpl) boundary, (SurfaceImpl) this));
        this.patch = newPatchList;
    }

    /**
     * Initializes the Surface: - Sets the surface patches - Sets the Boundary, or calculates it if
     * doesn´t exist
     *
     * @param patch List of SurfacePatch´s
     */
    private void initializeSurface(List<? extends SurfacePatch> patch) {

        if (patch == null)
            throw new IllegalArgumentException("Empty array SurfacePatch."); // $NON-NLS-1$

        if (patch.isEmpty())
            throw new IllegalArgumentException("Empty array SurfacePatch."); // $NON-NLS-1$

        // Calculate the boundary for the SurfacePatches. The continuity of the SurfacePatches is
        // checked within the creation of the surface boundary
        this.boundary = this.createBoundary(patch);

        /* Add patches to patch list */
        ArrayList<SurfacePatch> newPatchList = new ArrayList<SurfacePatch>();
        for (SurfacePatch p : patch) {
            if (p != null) newPatchList.add(p);
        }
        this.patch = newPatchList;

        // Build the envelope for the Surface based on the SurfacePatch envelopes
        SurfacePatchImpl tFirstPatch = (SurfacePatchImpl) patch.get(0);
        // this.envelope = new EnvelopeImpl(tFirstPatch.getEnvelope());
        this.envelope = new EnvelopeImpl(tFirstPatch.getEnvelope());
        for (SurfacePatch p : patch)
            ((EnvelopeImpl) this.envelope).expand(((SurfacePatchImpl) p).getEnvelope());
    }

    /**
     * Creates a SurfaceBoundary by a list of neigboured patches
     *
     * @param patches List of surface patches that represent the surface
     * @return the SurfaceBoundary of the surface represented by the set of surface patches
     * @throws IllegalArgument Exception if the union of the surface patches is not a surface. That
     *     means that the patches are not continuous.
     */
    private SurfaceBoundaryImpl createBoundary(List<? extends SurfacePatch> patches) {

        if (patches.isEmpty()) return null;

        SurfacePatch firstPatch = patches.get(0);
        if (patches.size() == 1) return (SurfaceBoundaryImpl) firstPatch.getBoundary();

        Surface firstPatchSurface = new SurfaceImpl(firstPatch.getBoundary());
        Set<OrientableSurface> surfaceList = new HashSet<OrientableSurface>();

        for (int i = 1; i < patches.size(); i++) {
            SurfacePatch nextPatch = patches.get(i);
            surfaceList.add(new SurfaceImpl(nextPatch.getBoundary()));
        }

        MultiSurface ms = new MultiSurfaceImpl(getCoordinateReferenceSystem(), surfaceList);
        TransfiniteSet unionResultSurface = firstPatchSurface.union(ms);
        if (!(unionResultSurface instanceof SurfaceImpl))
            throw new IllegalArgumentException("Surface patches are not continuous");

        return (SurfaceBoundaryImpl) ((SurfaceImpl) unionResultSurface).getBoundary();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.geometry.featgeom.primitive.PrimitiveImpl#getBoundary()
     */
    public SurfaceBoundaryImpl getBoundary() {
        // ok
        // Return the Boundary of this surface
        return (SurfaceBoundaryImpl) boundary;
    }

    /**
     * Sets the Boundary of the Surface
     *
     * @param boundary The boundary to set.
     */
    public void setBoundary(SurfaceBoundaryImpl boundary) {
        this.boundary = boundary;
    }

    /**
     * Sets the Surface Patches and Boundary for the Surface
     *
     * @param surfacePatches - ArrayList of Surface Patches, which represent the Surface
     */
    protected void setPatches(List<? extends SurfacePatch> surfacePatches) {
        this.initializeSurface(surfacePatches);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.primitive.Surface#getPatches()
     */
    public List<? extends SurfacePatch> getPatches() {
        return this.patch;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.geometry.featgeom.root.GeometryImpl#getEnvelope()
     */
    public Envelope getEnvelope() {
        // TODO documentation
        return this.envelope;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.stdss.fgeo.primitive.OrientablePrimitive#createMate()
     */
    protected OrientablePrimitiveImpl createProxy() {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation
        return null;
    }

    // Not used
    //	/**
    //	 */
    //	public void splitBoundary(double distance) {
    //		this.getBoundary().split(distance);
    //	}

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.geometry.featgeom.root.GeometryImpl#clone()
     */
    public SurfaceImpl clone() throws CloneNotSupportedException {
        // Test OK
        // Clone SurfaceBoundary and use it to create new Surface
        SurfaceBoundary newBoundary = (SurfaceBoundary) this.boundary.clone();
        return new SurfaceImpl(newBoundary);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.primitive.OrientableSurface#getComposite()
     */
    public CompositeSurface getComposite() {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation
        return null;
    }

    public Surface getPrimitive() {
        return null;
    }

    public OrientableSurface[] getProxy() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.root.Geometry#isSimple()
     */
    public boolean isSimple() {
        // Test OK
        // Test simplicity by building a topological graph and testing for self-intersection
        // Is Simple, if the exterior ring and the interior rings does not have selfintersections
        // and the exterior ring and the interior rings don´t touch or intersect each other.
        IsSimpleOp simpleOp = new IsSimpleOp();
        return simpleOp.isSimple(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericSurface#getUpNormal(org.opengis.geometry.coordinate.DirectPosition)
     */
    public double[] getUpNormal(DirectPosition point) {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericSurface#getPerimeter()
     */
    public double getPerimeter() {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericSurface#getArea()
     */
    public double getArea() {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.geometry.featgeom.root.GeometryImpl#getDimension(org.opengis.geometry.coordinate.DirectPosition)
     */
    public int getDimension(DirectPosition point) {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation
        return 2;
    }

    public String toString() {
        return GeometryToString.getString(this);
    }

    /**
     * Returns a list of the rings which define the surface: First element is the exterior ring
     * (island), the following elements, if exist, define the interior rings (holes)
     *
     * @return List of Ring: First element is the exterior ring (island), the following elements, if
     *     exist, define the interior rings (holes)
     */
    public List<Ring> getBoundaryRings() {

        List<Ring> rList = new ArrayList();
        rList.add(this.boundary.getExterior());
        Iterator tInteriorRings = this.boundary.getInteriors().iterator();

        while (tInteriorRings.hasNext()) {
            rList.add((Ring) tInteriorRings.next());
        }

        return rList;
    }

    /* (non-Javadoc)
     * @see org.geotools.geometry.featgeom.root.GeometryImpl#getRepresentativePoint()
     */
    public DirectPosition getRepresentativePoint() {
        // Return the representative point of the surface´s boundary
        // TODO Note: This solution is not correct, since the representative point of the surface
        // boundary may not be on the surface
        return this.getBoundary().getRepresentativePoint();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((boundary == null) ? 0 : boundary.hashCode());
        result = PRIME * result + ((envelope == null) ? 0 : envelope.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SurfaceImpl other = (SurfaceImpl) obj;
        if (boundary == null) {
            if (other.boundary != null) return false;
        } else if (!boundary.equals(other.boundary)) return false;
        /*  Envelope.class doesn't have equals implemented
        if (envelope == null) {
        	if (other.envelope != null)
        		return false;
        } else if (!envelope.equals(other.envelope))
        	return false;
        */
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.root.Geometry#transform(org.opengis.referencing.crs.CoordinateReferenceSystem,
     *      org.opengis.referencing.operation.MathTransform)
     */
    public Geometry transform(CoordinateReferenceSystem newCRS, MathTransform transform)
            throws TransformException {

        // loop through each ring in this Surface and transform it to the new CRS, then
        // use the new rings to build a new Surface and return that.
        PrimitiveFactory primitiveFactory = new PrimitiveFactoryImpl(newCRS, getPositionFactory());

        List<Ring> currentRings = this.getBoundaryRings();
        Iterator<Ring> iter = currentRings.iterator();
        Ring newExterior = null;
        List<Ring> newInteriors = new ArrayList<Ring>();
        while (iter.hasNext()) {
            Ring thisRing = (Ring) iter.next();

            // exterior Ring should be first element in the list
            if (newExterior == null) {
                newExterior = (Ring) thisRing.transform(newCRS, transform);
            } else {
                newInteriors.add((Ring) thisRing.transform(newCRS, transform));
            }
        }

        // use the new Ring list to build a new Surface and return it
        SurfaceBoundaryImpl surfaceBoundary =
                (SurfaceBoundaryImpl)
                        primitiveFactory.createSurfaceBoundary(newExterior, newInteriors);
        SurfaceImpl newSurface = (SurfaceImpl) primitiveFactory.createSurface(surfaceBoundary);
        return newSurface;
    }
}
