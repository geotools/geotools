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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequence;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * A stateful geometry collector that will add all geometries into a single resulting geometry
 * collection with the following properties:
 * <ul>
 * <li>the elements of the resulting geometry are simple geometries, adding a geometry collection
 * will result in it being flattened</li>
 * <li>the resulting geometry collection type will match its contents, a generic geometry collection
 * will be used only in case of heterogeneous contents</li>
 * <li>all geometries will be cloned using the provided geometry factory (one based on a
 * {@link PackedCoordinateSequence} is used by default to reduce memory usage)
 * </ul>
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/geometry/jts/GeometryCollector.java $
 */
public class GeometryCollector {
    List<Geometry> geometries = new ArrayList<Geometry>();

    GeometryFactory factory = new GeometryFactory(new PackedCoordinateSequenceFactory());

    long coordinates = 0;

    long maxCoordinates = -1;

    /**
     * Returns the maximum number of coordinates this collector is allowed to keep in the resulting
     * geometry
     * 
     * @return
     */
    public long getMaxCoordinates() {
        return maxCoordinates;
    }

    /**
     * Sets the maximum number of coordinates to be collected. By default is -1, no limit
     * 
     * @param maxCoordinates
     */
    public void setMaxCoordinates(long maxCoordinates) {
        this.maxCoordinates = maxCoordinates;
    }

    /**
     * Returns the geometry factory used to deep clone the geometries while collecting them (if null
     * no cloning will happen)
     * 
     * @return
     */
    public GeometryFactory getFactory() {
        return factory;
    }

    /**
     * Sets the geometry factory used to deep clone the geometries while collecting them. May be set
     * to null to avoid deep cloning. By default a geometry factory based on
     * {@link PackedCoordinateSequenceFactory} is used to minimize the memory usage
     * 
     * @param factory
     */
    public void setFactory(GeometryFactory factory) {
        this.factory = factory;
    }

    /**
     * Returns a geometry collection containing all of the geometries collected in the process
     * 
     * @return
     */
    public GeometryCollection collect() {
        // empty case, we return an empty collection (it's what JTS returns when a geometry
        // operation returns an empty result)
        if (geometries.isEmpty()) {
            return new GeometryCollection(null, factory == null ? new GeometryFactory() : factory);
        }

        // use or guess the geometry factory
        GeometryFactory gf = factory;
        if (gf == null) {
            gf = geometries.get(0).getFactory();
        }
        if (gf == null) {
            gf = new GeometryFactory();
        }

        // build the final collection
        Class collectionClass = guessCollectionType();
        if (collectionClass == MultiPoint.class) {
            Point[] array = (Point[]) geometries.toArray(new Point[geometries.size()]);
            return gf.createMultiPoint(array);
        } else if (collectionClass == MultiPolygon.class) {
            Polygon[] array = (Polygon[]) geometries.toArray(new Polygon[geometries.size()]);
            return gf.createMultiPolygon(array);
        } else if (collectionClass == MultiLineString.class) {
            LineString[] array = (LineString[]) geometries
                    .toArray(new LineString[geometries.size()]);
            return gf.createMultiLineString(array);
        } else {
            Geometry[] array = (Geometry[]) geometries.toArray(new Geometry[geometries.size()]);
            return gf.createGeometryCollection(array);
        }
    }

    private Class guessCollectionType() {
        // empty set? then we'll return an empty point collection
        if (geometries == null || geometries.size() == 0) {
            return GeometryCollection.class;
        }

        // see if all are of the same base geometric type
        Class result = baseType(geometries.get(0).getClass());
        for (int i = 1; i < geometries.size(); i++) {
            Class curr = geometries.get(i).getClass();
            if (curr != result && !(result.isAssignableFrom(curr))) {
                return GeometryCollection.class;
            }
        }

        // return the geometric collection associated with the base type
        if (result == Point.class) {
            return MultiPoint.class;
        } else if (result == LineString.class) {
            return MultiLineString.class;
        } else if (result == Polygon.class) {
            return MultiPolygon.class;
        } else {
            return GeometryCollection.class;
        }

    }

    private Class baseType(Class geometry) {
        if (Polygon.class.isAssignableFrom(geometry)) {
            return Polygon.class;
        } else if (LineString.class.isAssignableFrom(geometry)) {
            return LineString.class;
        } else if (Point.class.isAssignableFrom(geometry)) {
            return Point.class;
        } else {
            return geometry;
        }
    }

    /**
     * Adds a geometry to the collector
     * 
     * @param g
     * @param result
     */
    public void add(Geometry g) {
        if (g == null) {
            return;
        } else if (g instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) g;
            for (int i = 0; i < gc.getNumGeometries(); i++) {
                add(gc.getGeometryN(i));
            }
        } else {
            coordinates += g.getNumPoints();
            if (maxCoordinates > 0 && coordinates > maxCoordinates) {
                throw new IllegalStateException(
                        "Max number of collected ordinates has been exceeded. Current count is "
                                + coordinates + ", max count is " + maxCoordinates);
            }

            // apply the geometry factory if possible (this ensures the proper coordinate sequence
            // usage)
            if (factory != null) {
                g = factory.createGeometry(g);
            }
            geometries.add(g);
        }
    }
}
