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

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


/**
 * Service object that takes a geometry an applies a MathTransform on top
 * of it.
 * @author Andrea Aime
 * @source $URL$
 */
public class GeometryCoordinateSequenceTransformer {
    private MathTransform transform;
    private CoordinateSequenceTransformer csTransformer;
    private CoordinateReferenceSystem crs;
    
    public GeometryCoordinateSequenceTransformer() {
        csTransformer = new DefaultCoordinateSequenceTransformer();
    }

    public GeometryCoordinateSequenceTransformer(CoordinateSequenceTransformer transformer) {
        csTransformer = transformer;
    }

    /**
     * Sets the math transform to be used for transformation
     * @param transform
     */
    public void setMathTransform(MathTransform transform) {
        this.transform = transform;
    }

    
    /**
     * Sets the target coordinate reference system.
     * <p>
     * This value is used to set the coordinate reference system of geometries
     * after they have been transformed.
     * </p>
     * @param crs The target coordinate reference system.
     */
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }
    
    /**
     * Applies the transform to the provided geometry, given
     * @param g
     * @throws TransformException
     */
    public Geometry transform(Geometry g) throws TransformException {
        GeometryFactory factory = g.getFactory();
        Geometry transformed = null;
        
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
        
        //copy over user data, do a special check for coordinate reference 
        // systme
        transformed.setUserData(g.getUserData());

        if ((g.getUserData() == null) || g.getUserData() instanceof CoordinateReferenceSystem) {
            //set the new one to be the target crs
            if (crs != null) {
                transformed.setUserData(crs);
            }
        }
        
        return transformed;
    }

    /**
     *
     * @throws TransformException
     */
    public LineString transformLineString(LineString ls, GeometryFactory gf)
        throws TransformException {
        CoordinateSequence cs = projectCoordinateSequence(ls.getCoordinateSequence());
        LineString transformed = null;
        
        if (ls instanceof LinearRing) {
            transformed = gf.createLinearRing(cs);
        } else {
            transformed = gf.createLineString(cs);
        }
        
        transformed.setUserData( ls.getUserData() );
        return transformed;
    }

    /**
     * @param point
     *
     * @throws TransformException
     */
    public Point transformPoint(Point point, GeometryFactory gf)
        throws TransformException {
        CoordinateSequence cs = projectCoordinateSequence(point.getCoordinateSequence());
        Point transformed = gf.createPoint(cs);;
        transformed.setUserData( point.getUserData() );
        return transformed; 
    }

    /**
     * @param cs a CoordinateSequence
     *
     * @throws TransformException
     */
    public CoordinateSequence projectCoordinateSequence(CoordinateSequence cs)
        throws TransformException {
        return csTransformer.transform(cs, transform);
    }

    /**
     * @param polygon
     * @throws TransformException
     */
    public Polygon transformPolygon(Polygon polygon, GeometryFactory gf)
        throws TransformException {
        LinearRing exterior = (LinearRing) transformLineString(polygon.getExteriorRing(), gf);
        LinearRing[] interiors = new LinearRing[polygon.getNumInteriorRing()];

        for (int i = 0; i < interiors.length; i++) {
            interiors[i] = (LinearRing) transformLineString(polygon.getInteriorRingN(i), gf);
        }

        Polygon transformed = gf.createPolygon(exterior, interiors);
        transformed.setUserData( polygon.getUserData() );
        return transformed;
    }
}
