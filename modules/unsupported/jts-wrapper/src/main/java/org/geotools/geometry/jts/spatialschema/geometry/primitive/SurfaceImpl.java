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
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/primitive/SurfaceImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

// J2SE direct dependencies

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.geotools.geometry.jts.spatialschema.geometry.GeometryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.JTSGeometry;
import org.geotools.geometry.jts.spatialschema.geometry.JTSUtils;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.SurfacePatch;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Surface with a positive orientation. {@code Surface} is a subclass of {@link Primitive} and is
 * the basis for 2-dimensional geometry. Unorientable surfaces such as the M&ouml;bius band are not
 * allowed. The orientation of a surface chooses an "up" direction through the choice of the upward
 * normal, which, if the surface is not a cycle, is the side of the surface from which the exterior
 * boundary appears counterclockwise. Reversal of the surface orientation reverses the curve
 * orientation of each boundary component, and interchanges the conceptual "up" and "down" direction
 * of the surface. If the surface is the boundary of a solid, the "up" direction is usually outward.
 * For closed surfaces, which have no boundary, the up direction is that of the surface patches,
 * which must be consistent with one another. Its included {@linkplain SurfacePatch surface patches}
 * describe the interior structure of a {@code Surface}.
 *
 * <blockquote>
 *
 * <font size=2> <strong>NOTE:</strong> Other than the restriction on orientability, no other
 * "validity" condition is required for GM_Surface. </font>
 *
 * </blockquote>
 *
 * @UML type GM_Surface
 *
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 * @see PrimitiveFactory#createSurface(List)
 * @see PrimitiveFactory#createSurface(SurfaceBoundary)
 */
public class SurfaceImpl extends GeometryImpl implements Surface {
    protected List<SurfacePatchImpl> patches;

    public SurfaceImpl() {
        this(null);
    }

    public SurfaceImpl(CoordinateReferenceSystem crs) {
        super(crs);
        patches = new ArrayList();
    }

    @SuppressWarnings("unchecked")
    public List<SurfacePatchImpl> getPatches() {
        return patches;
    }

    public SurfaceBoundary getBoundary() {
        return (SurfaceBoundary) super.getBoundary();
    }

    public double[] getUpNormal(DirectPosition point) {
        return new double[] {0, 0, 1};
    }

    public double getPerimeter() {
        return getJTSGeometry().getBoundary().getLength();
    }

    public double getArea() {
        return getJTSGeometry().getArea();
    }

    public CompositeSurface getComposite() {
        return null;
    }

    public int getOrientation() {
        return 0;
    }

    public Surface getPrimitive() {
        return this;
    }

    public Set getComplexes() {
        return null;
    }

    public Set getContainingPrimitives() {
        return null;
    }

    public OrientableSurface[] getProxy() {
        return null;
    }

    public Set getContainedPrimitives() {
        return null;
    }

    /** @see com.polexis.lite.spatialschema.geometry.GeometryImpl#computeJTSPeer() */
    protected org.locationtech.jts.geom.Geometry computeJTSPeer() {
        if (patches.size() > 1) {
            // throw new UnsupportedOperationException("This implementation does not support
            // surfaces with multiple patches.");
            final org.locationtech.jts.geom.Polygon[] polygons =
                    new org.locationtech.jts.geom.Polygon[patches.size()];
            for (int i = 0; i < patches.size(); i++) {
                final JTSGeometry jtsGeometry = (JTSGeometry) patches.get(i);
                polygons[i] = (org.locationtech.jts.geom.Polygon) jtsGeometry.getJTSGeometry();
            }
            return JTSUtils.GEOMETRY_FACTORY.createMultiPolygon(polygons);
        }
        return ((JTSGeometry) patches.get(0)).getJTSGeometry();
    }
}
