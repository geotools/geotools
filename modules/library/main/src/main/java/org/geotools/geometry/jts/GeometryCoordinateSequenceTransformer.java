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
package org.geotools.geometry.jts;

import java.util.ArrayList;
import java.util.List;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Service object that takes a geometry and applies a {@link MathTransform} to the coordinates it
 * contains, creating a new geometry as the transformed output.
 *
 * <p>The standard usage pattern is to supply a {@link MathTransform} and @link
 * CoordinateReferenceSystem} explicitly. The {@link #transform(Geometry)} method can then be used
 * to construct transformed geometries using the {@link GeometryFactory} and {@link
 * CoordinateSequenceFactory} of the input geometry.
 *
 * @author Andrea Aime
 * @author Martin Davis
 */
public class GeometryCoordinateSequenceTransformer {
    private MathTransform transform = null;
    private CoordinateReferenceSystem crs;
    private CoordinateSequenceTransformer inputCSTransformer = null;
    private CoordinateSequenceTransformer csTransformer = null;
    private GeometryFactory currGeometryFactory = null;

    private boolean curveCompatible;

    /**
     * Creates a transformer which uses the {@link CoordinateSequenceFactory} of the source
     * geometries.
     */
    public GeometryCoordinateSequenceTransformer() {
        // the csTransformer is initialized from the first geometry
        // and the supplied transform
    }

    /**
     * Creates a transformer which uses a client-specified {@link CoordinateSequenceTransformer}.
     *
     * <p><b>WARNING:</b> The CoordinateSequenceTransformer must use the same {@link
     * CoordinateSequenceFactory} as the output GeometryFactory, so that geometries are constructed
     * consistently.
     */
    public GeometryCoordinateSequenceTransformer(CoordinateSequenceTransformer transformer) {
        inputCSTransformer = transformer;
        csTransformer = transformer;
    }

    /** Sets the math transform to be used for transformation */
    public void setMathTransform(MathTransform transform) {
        this.transform = transform;
        this.curveCompatible = isCurveCompatible(transform);
    }

    /**
     * Sets the target coordinate reference system.
     *
     * <p>This value is used to set the coordinate reference system of geometries after they have
     * been transformed.
     *
     * @param crs The target coordinate reference system.
     */
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    /**
     * Initializes the internal CoordinateSequenceTransformer if not specified explicitly.
     *
     * @param gf the factory to use
     */
    private void init(GeometryFactory gf) {
        // don't init if csTransformer already exists
        if (inputCSTransformer != null) return;
        // don't reinit if gf is the same (the usual case)
        if (currGeometryFactory == gf) return;

        currGeometryFactory = gf;
        CoordinateSequenceFactory csf = gf.getCoordinateSequenceFactory();
        csTransformer = new DefaultCoordinateSequenceTransformer(csf);
    }

    /**
     * Applies the transform to the provided geometry, creating a new transformed geometry.
     *
     * @param g the geometry to transform
     * @return a new transformed geometry
     */
    public Geometry transform(Geometry g) throws TransformException {
        GeometryFactory factory = g.getFactory();
        Geometry transformed = null;

        // lazily init csTransformer using geometry's CSFactory
        init(factory);

        if (g instanceof Point) {
            transformed = transformPoint((Point) g, factory);
        } else if (g instanceof MultiPoint) {
            MultiPoint mp = (MultiPoint) g;
            Point[] points = new Point[mp.getNumGeometries()];

            for (int i = 0; i < points.length; i++) {
                points[i] = transformPoint((Point) mp.getGeometryN(i), factory);
            }

            transformed = factory.createMultiPoint(points);
        } else if (g instanceof LineString) {
            transformed = transformLineString((LineString) g, factory);
        } else if (g instanceof MultiLineString) {
            MultiLineString mls = (MultiLineString) g;
            LineString[] lines = new LineString[mls.getNumGeometries()];

            for (int i = 0; i < lines.length; i++) {
                lines[i] = transformLineString((LineString) mls.getGeometryN(i), factory);
            }

            transformed = factory.createMultiLineString(lines);
        } else if (g instanceof Polygon) {
            transformed = transformPolygon((Polygon) g, factory);
        } else if (g instanceof MultiPolygon) {
            MultiPolygon mp = (MultiPolygon) g;
            Polygon[] polygons = new Polygon[mp.getNumGeometries()];

            for (int i = 0; i < polygons.length; i++) {
                polygons[i] = transformPolygon((Polygon) mp.getGeometryN(i), factory);
            }

            transformed = factory.createMultiPolygon(polygons);
        } else if (g instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) g;
            Geometry[] geoms = new Geometry[gc.getNumGeometries()];

            for (int i = 0; i < geoms.length; i++) {
                geoms[i] = transform(gc.getGeometryN(i));
            }

            transformed = factory.createGeometryCollection(geoms);
        } else {
            throw new IllegalArgumentException("Unsupported geometry type " + g.getClass());
        }

        // copy over user data
        // do a special check for coordinate reference system
        transformed.setUserData(g.getUserData());

        if ((g.getUserData() == null) || g.getUserData() instanceof CoordinateReferenceSystem) {
            // set the new one to be the target crs
            if (crs != null) {
                transformed.setUserData(crs);
            }
        }

        return transformed;
    }

    /** @throws TransformException */
    public LineString transformLineString(LineString ls, GeometryFactory gf)
            throws TransformException {
        if (ls instanceof CurvedGeometry<?> && curveCompatible) {
            return transformCurvedLineString((CurvedGeometry) ls, gf);
        } else {
            return transformStraightLineString(ls, gf);
        }
    }

    private boolean isCurveCompatible(MathTransform mt) {
        if (!(mt instanceof AffineTransform2D)) {
            return false;
        }

        AffineTransform2D at = (AffineTransform2D) mt;
        // return true if we are scaling by the same amount, and the rotational
        // elements are equal in absolute value
        return at.getScaleX() == at.getScaleY()
                && Math.abs(at.getShearX()) == Math.abs(at.getShearY());
    }

    private LineString transformStraightLineString(LineString ls, GeometryFactory gf)
            throws TransformException {
        // if required, init csTransformer using geometry's CSFactory
        init(gf);

        CoordinateSequence cs = projectCoordinateSequence(ls.getCoordinateSequence());
        LineString transformed = null;

        if (ls instanceof LinearRing) {
            transformed = gf.createLinearRing(cs);
        } else {
            transformed = gf.createLineString(cs);
        }

        transformed.setUserData(ls.getUserData());
        return transformed;
    }

    private LineString transformCurvedLineString(CurvedGeometry<?> curved, GeometryFactory gf)
            throws TransformException {
        CurvedGeometryFactory cf = CurvedGeometries.getFactory(curved);
        if (curved instanceof SingleCurvedGeometry<?>) {
            SingleCurvedGeometry<?> single = (SingleCurvedGeometry<?>) curved;
            double[] controlPoints = single.getControlPoints();
            double[] target = new double[controlPoints.length];
            transform.transform(controlPoints, 0, target, 0, controlPoints.length / 2);
            return cf.createCurvedGeometry(2, target);
        } else {
            CompoundCurvedGeometry<?> compound = (CompoundCurvedGeometry<?>) curved;
            List<LineString> reprojected = new ArrayList<>();
            for (LineString component : compound.getComponents()) {
                LineString ls = transformLineString(component, gf);
                reprojected.add(ls);
            }
            return cf.createCurvedGeometry(reprojected);
        }
    }

    /** */
    public Point transformPoint(Point point, GeometryFactory gf) throws TransformException {

        // if required, init csTransformer using geometry's CSFactory
        init(gf);

        CoordinateSequence cs = projectCoordinateSequence(point.getCoordinateSequence());
        Point transformed = gf.createPoint(cs);
        transformed.setUserData(point.getUserData());
        return transformed;
    }

    /** @param cs a CoordinateSequence */
    private CoordinateSequence projectCoordinateSequence(CoordinateSequence cs)
            throws TransformException {
        return csTransformer.transform(cs, transform);
    }

    /** */
    public Polygon transformPolygon(Polygon polygon, GeometryFactory gf) throws TransformException {
        LinearRing exterior = (LinearRing) transformLineString(polygon.getExteriorRing(), gf);
        LinearRing[] interiors = new LinearRing[polygon.getNumInteriorRing()];

        for (int i = 0; i < interiors.length; i++) {
            interiors[i] = (LinearRing) transformLineString(polygon.getInteriorRingN(i), gf);
        }

        Polygon transformed = gf.createPolygon(exterior, interiors);
        transformed.setUserData(polygon.getUserData());
        return transformed;
    }
}
