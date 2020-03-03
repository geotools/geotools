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
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

import java.util.List;
import org.geotools.geometry.jts.spatialschema.geometry.JTSGeometry;
import org.geotools.geometry.jts.spatialschema.geometry.JTSUtils;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.SurfacePatchImpl;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.SurfaceInterpolation;

public class PolygonImpl extends SurfacePatchImpl implements Polygon {

    // *************************************************************************
    //  Fields
    // *************************************************************************

    // Why the hell is this a list???
    private List spanningSurface;

    // *************************************************************************
    //  Constructors
    // *************************************************************************

    public PolygonImpl(SurfaceBoundary boundary) {
        // We only support planar polygons
        this(boundary, null);
    }

    public PolygonImpl(SurfaceBoundary boundary, List spanningSurface) {
        super(SurfaceInterpolation.PLANAR, boundary);
        this.spanningSurface = spanningSurface;
    }

    // *************************************************************************
    //  implement the *** interface
    // *************************************************************************

    public int getNumDerivativesOnBoundary() {
        return 0;
    }

    /**
     * @see com.polexis.lite.spatialschema.geometry.primitive.SurfacePatchImpl#calculateJTSPeer()
     */
    public org.locationtech.jts.geom.Geometry calculateJTSPeer() {
        SurfaceBoundary boundary = getBoundary();
        Ring exterior = boundary.getExterior();
        List interiors = boundary.getInteriors();
        org.locationtech.jts.geom.Geometry g = ((JTSGeometry) exterior).getJTSGeometry();
        int numHoles = (interiors != null) ? interiors.size() : 0;
        org.locationtech.jts.geom.LinearRing jtsExterior =
                JTSUtils.GEOMETRY_FACTORY.createLinearRing(g.getCoordinates());
        org.locationtech.jts.geom.LinearRing[] jtsInterior =
                new org.locationtech.jts.geom.LinearRing[numHoles];
        for (int i = 0; i < numHoles; i++) {
            org.locationtech.jts.geom.Geometry g2 =
                    ((JTSGeometry) interiors.get(i)).getJTSGeometry();
            jtsInterior[i] = JTSUtils.GEOMETRY_FACTORY.createLinearRing(g2.getCoordinates());
        }
        org.locationtech.jts.geom.Polygon result =
                JTSUtils.GEOMETRY_FACTORY.createPolygon(jtsExterior, jtsInterior);
        return result;
    }

    public PolyhedralSurface getSurface() {
        return (PolyhedralSurface) super.getSurface();
    }

    /** @see org.opengis.geometry.coordinate.Polygon#getSpanningSurface() */
    public List getSpanningSurface() {
        // Why the hell is this a list???
        return spanningSurface;
    }

    public boolean isValid() {
        org.locationtech.jts.geom.Polygon poly =
                (org.locationtech.jts.geom.Polygon) this.getJTSGeometry();
        return poly.isValid();
    }
}
