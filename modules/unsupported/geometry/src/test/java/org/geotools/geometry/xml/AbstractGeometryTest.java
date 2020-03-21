/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class AbstractGeometryTest extends TestCase {

    private org.opengis.geometry.coordinate.GeometryFactory gFact;

    private org.opengis.geometry.primitive.PrimitiveFactory pFact;

    protected CoordinateReferenceSystem crs;

    protected static String WGS84_WKT =
            "GEOGCS[\"WGS84\", DATUM[\"WGS84\", SPHEROID[\"WGS84\", 6378137.0, 298.257223563]],"
                    + "PRIMEM[\"Greenwich\", 0.0], UNIT[\"degree\",0.017453292519943295], "
                    + "AXIS[\"Longitude\",EAST], AXIS[\"Latitude\",NORTH]]";
    private PositionFactory posFact;

    /** setUp Called before each test. */
    public void setUp() throws FactoryException {
        GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
        gFact = builder.getGeometryFactory();
        pFact = builder.getPrimitiveFactory();
        posFact = builder.getPositionFactory();
    }

    protected GeometryFactory getGeometryFactory() {
        return gFact;
    }

    protected PrimitiveFactory getPrimitiveFactory() {
        return pFact;
    }

    protected DirectPosition createDirectPosition(double x, double y) {
        double[] coords = new double[2];
        coords[0] = x;
        coords[1] = y;
        return posFact.createDirectPosition(coords);
    }

    /**
     * A helper method for creating a Curve from an array of DirectPositions
     *
     * @return a <tt>Curve</tt>
     */
    protected Curve createCurve(final DirectPosition[] points) {
        final List curveSegmentList = Collections.singletonList(createLineString(points));
        final Curve curve = pFact.createCurve(curveSegmentList);
        return curve;
    }

    /**
     * A helper method for creating a lineString from an array of DirectPositions
     *
     * @return <tt>LineString</tt>
     */
    protected LineString createLineString(final DirectPosition[] points) {
        final LineString lineString = gFact.createLineString(new ArrayList(Arrays.asList(points)));
        return lineString;
    }

    /**
     * A helper method for creating a Ring from an array of DirectPositions
     *
     * @return a <tt>Ring</tt>
     */
    protected Ring createRing(final Curve curve) {
        final List curveList = Collections.singletonList(curve);
        final Ring ring = pFact.createRing(curveList);
        return ring;
    }

    /**
     * creates a SurfaceBoundary using a curve as the exterior
     *
     * @return <tt>SurfaceBoundary</tt>
     */
    protected SurfaceBoundary createSurfaceBoundary(Curve exterior) {
        final Ring exteriorRing = createRing(exterior);
        List interiorRingList = Collections.EMPTY_LIST;
        SurfaceBoundary surfaceBoundary = null;
        surfaceBoundary = pFact.createSurfaceBoundary(exteriorRing, interiorRingList);
        return surfaceBoundary;
    }

    /**
     * Creates a simple polygon with no holes
     *
     * @param points points defining the polygon (surface)
     * @return the surface created out of the points
     */
    protected Surface createSurface(final DirectPosition[] points) {
        Curve curve = createCurve(points);
        SurfaceBoundary surfaceBoundary = createSurfaceBoundary(curve);
        Surface surface = getPrimitiveFactory().createSurface(surfaceBoundary);
        return surface;
    }
}
