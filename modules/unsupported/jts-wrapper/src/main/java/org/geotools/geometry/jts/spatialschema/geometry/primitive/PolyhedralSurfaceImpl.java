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
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.geotools.geometry.jts.spatialschema.geometry.GeometryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.JTSGeometry;
import org.geotools.geometry.jts.spatialschema.geometry.JTSUtils;
import org.geotools.geometry.jts.spatialschema.geometry.geometry.PolygonImpl;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The {@code PolyhedralSurfaceImpl} class/interface...
 *
 * @author SYS Technologies
 * @author dillard
 * @version $Revision $
 */
public class PolyhedralSurfaceImpl extends GeometryImpl implements PolyhedralSurface {

    protected List<PolygonImpl> patches;

    /** Creates a new {@code PolyhedralSurfaceImpl}. */
    public PolyhedralSurfaceImpl(CoordinateReferenceSystem crs) {
        super(crs);
        patches = new ArrayList();
    }

    public PolyhedralSurfaceImpl() {
        this(null);
    }

    public SurfaceBoundary getBoundary() {
        return (SurfaceBoundary) super.getBoundary();
    }

    @SuppressWarnings("unchecked")
    public List<PolygonImpl> getPatches() {
        return patches;
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

    public PolyhedralSurfaceImpl clone() {
        return (PolyhedralSurfaceImpl) super.clone();
    }
}
