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

package org.geotools.resources.coverage;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;
import java.util.List;
import org.geotools.util.Utilities;

/**
 * Performs some naive intersections on GeometryCollections, because JTS is not
 * supporting them.
 *
 * @author Emanuele Tajariol (GeoSolutions)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/coverage/src/main/java/org/geotools/resources/coverage/IntersectUtils.java $
 */
public class IntersectUtils {

    /**
     * Tests whether the two geometries intersect.
     *
     * <P>This method relies completely on {@link Geometry#intersects(Geometry)}
     * but also tries to unroll <TT>GeometryCollection</TT>s.
     *
     * @param g1
     * @param g2
     * @return true if the two geometries intersect.
     */
    public static boolean intersects(Geometry g1, Geometry g2) {
    	Utilities.ensureNonNull("g1", g1);
    	Utilities.ensureNonNull("g2", g2);
        if(g1 instanceof GeometryCollection) {
            if(g2 instanceof GeometryCollection) {
                return intersects((GeometryCollection)g1, (GeometryCollection)g2);
            } else {
                return intersects((GeometryCollection)g1, g2);
            }
        } else {
            if(g2 instanceof GeometryCollection) {
                return intersects((GeometryCollection)g2, g1);
            } else {
                return g1.intersects(g2);
            }
        }
    }

    /**
     * Helper method for {@link #intersects(Geometry, Geometry) intersects(Geometry, Geometry)}
     */
    private static boolean intersects(GeometryCollection gc, Geometry g) {
    	final int size=gc.getNumGeometries();
        for (int i = 0; i < size; i++) {
            Geometry g1 = (Geometry)gc.getGeometryN(i);
            if( g1.intersects(g) )
                return true;
        }
        return false;
    }

    /**
     * Helper method for {@link #intersects(Geometry, Geometry) intersects(Geometry, Geometry)}
     */
    private static boolean intersects(GeometryCollection gc1, GeometryCollection gc2) {
    	final int size=gc1.getNumGeometries();
        for (int i = 0; i < size; i++) {
            Geometry g1 = (Geometry)gc1.getGeometryN(i);
            if( intersects(gc2, g1) )
                return true;
        }
        return false;
    }


    /**
     * Return the intersection of two Geometries.
     * <BR>This method relies completely on {@link Geometry#intersection(Geometry)}
     * but also tries to unroll <TT>GeometryCollection</TT>s.
     *
     * @param g1
     * @param g2
     * @return true if the two geometries intersect.
     */
    public static Geometry intersection(Geometry g1, Geometry g2) {
        if(g1 instanceof GeometryCollection) {
            if(g2 instanceof GeometryCollection) {
                return intersection((GeometryCollection)g1, (GeometryCollection)g2);
            } else {
                List<Geometry> ret = intersection((GeometryCollection)g1, g2);
                return g1.getFactory().createGeometryCollection(GeometryFactory.toGeometryArray(ret));
            }
        } else {
            if(g2 instanceof GeometryCollection) {
                List<Geometry> ret = intersection((GeometryCollection)g2, g1);
                return g1.getFactory().createGeometryCollection(GeometryFactory.toGeometryArray(ret));
            } else {
                return g1.intersection(g2);
            }
        }
    }

    /**
     * Helper method for {@link #intersection(Geometry, Geometry) intersection(Geometry, Geometry)}
     */
    private static List<Geometry> intersection(GeometryCollection gc, Geometry g) {
        List<Geometry> ret = new ArrayList<Geometry>();
        final int size=gc.getNumGeometries();
        for (int i = 0; i < size; i++) {
            Geometry g1 = (Geometry)gc.getGeometryN(i);
            collect(g1.intersection(g), ret);
        }
        return ret;
    }

    /**
     * Helper method for {@link #intersection(Geometry, Geometry) intersection(Geometry, Geometry)}
     */
    private static GeometryCollection intersection(GeometryCollection gc1, GeometryCollection gc2) {
        List<Geometry> ret = new ArrayList<Geometry>();
        final int size=gc1.getNumGeometries();
        for (int i = 0; i < size; i++) {
            Geometry g1 = (Geometry)gc1.getGeometryN(i);
            List<Geometry> partial = intersection(gc2, g1);
            ret.addAll(partial);
        }

        return gc1.getFactory().createGeometryCollection(GeometryFactory.toGeometryArray(ret));
    }

    /**
     * Adds into the <TT>collector</TT> the Geometry <TT>g</TT>, or, if
     * <TT>g</TT> is a GeometryCollection, every geometry in it.
     *
     * @param g the Geometry (or GeometryCollection to unroll)
     * @param collector the Collection where the Geometries will be added into
     */
    private static void collect(Geometry g, List<Geometry> collector) {
        if(g instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection)g;
            for (int i = 0; i < gc.getNumGeometries(); i++) {
                Geometry loop = gc.getGeometryN(i);
                if(! loop.isEmpty())
                    collector.add(loop);
            }
        } else {
            if(! g.isEmpty())
                collector.add(g);
        }
    }

    /**
     * Flatten all first-level (i.e. non recursively) bundles
     * (GeometryCollections and MultiPolygons) into a GeometryCollection.
     * <BR>Valid structures:<UL>
     * <LI>null -> null</LI>
     * <LI>Polygon -> Polygon</LI>
     * <LI>MultiPolygon -> GeometryCollection of Polygons</LI>
     * <LI>GeometryCollection holding Polygons and MultiPolygons -> GeometryCollection of Polygons</LI>
     * </UL>
     * @throws IllegalArgumentException when encountering illegal Geometries; message is the Geometry class name.
     * @param geometry
     * @return
     */
    public static Geometry unrollGeometries(Geometry geometry)
        throws IllegalArgumentException {

        if(geometry == null)
            return null;

        if(geometry instanceof com.vividsolutions.jts.geom.Polygon) {
            return geometry;

        } else if(geometry instanceof MultiPolygon) {

            MultiPolygon mp = (MultiPolygon)geometry;
            return geometry.getFactory().createGeometryCollection(unrollGeometries(mp).toArray(new Geometry[0]));

        } else if(geometry instanceof GeometryCollection) {
            List<com.vividsolutions.jts.geom.Polygon> ret = new ArrayList<Polygon>();

            GeometryCollection gc = (GeometryCollection)geometry;
            for (int i = 0; i < gc.getNumGeometries(); i++) {
                Geometry g = gc.getGeometryN(i);
                if(g instanceof com.vividsolutions.jts.geom.Polygon) {
                    ret.add((com.vividsolutions.jts.geom.Polygon) g );
                } else if(g instanceof MultiPolygon) {
                    ret.addAll(unrollGeometries((MultiPolygon)g));
                } else {
                    throw new IllegalArgumentException(g.getClass().toString());
                }
            }
            return geometry.getFactory().createGeometryCollection(ret.toArray(new Geometry[0]));
        } else {
            throw new IllegalArgumentException(geometry.getClass().toString());
        }
    }

    private static List<com.vividsolutions.jts.geom.Polygon> unrollGeometries(MultiPolygon mp) {
        List<com.vividsolutions.jts.geom.Polygon> ret = new ArrayList<Polygon>();

        for (int i = 0; i < mp.getNumGeometries(); i++) {
            com.vividsolutions.jts.geom.Polygon g = (com.vividsolutions.jts.geom.Polygon)mp.getGeometryN(i);
            ret.add(g);
        }

        return ret;
    }

}
