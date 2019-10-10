/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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
package org.geotools.geometry.iso.topograph2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A list of {@link Coordinate}s, which may be set to prevent repeated coordinates from occuring in
 * the list.
 */
public class CoordinateList extends ArrayList {

    // With contributions from Markus Schaber [schabios@logi-track.com]
    // [Jon Aquino 2004-03-25]
    private static final Coordinate[] coordArrayType = new Coordinate[0];

    /** Constructs a new list without any coordinates */
    public CoordinateList() {
        super();
    }

    /**
     * The basic constructor for a CoordinateArray allows repeated points (i.e produces a
     * CoordinateList with exactly the same set of points)
     *
     * @param coord the initial coordinates
     */
    public CoordinateList(Coordinate[] coord) {
        add(coord, true);
    }

    /**
     * Constructs a new list from an array of Coordinates, allowing caller to specify if repeated
     * points are to be removed.
     *
     * @param coord the array of coordinates to load into the list
     * @param allowRepeated if <code>false</code>, repeated points are removed
     */
    public CoordinateList(Coordinate[] coord, boolean allowRepeated) {
        add(coord, allowRepeated);
    }

    public Coordinate getCoordinate(int i) {
        return (Coordinate) get(i);
    }

    /**
     * Add an array of coordinates
     *
     * @param coord The coordinates
     * @param allowRepeated if set to false, repeated coordinates are collapsed
     * @param direction if false, the array is added in reverse order
     * @return true (as by general collection contract)
     */
    public boolean add(Coordinate[] coord, boolean allowRepeated, boolean direction) {
        if (direction) {
            for (int i = 0; i < coord.length; i++) {
                add(coord[i], allowRepeated);
            }
        } else {
            for (int i = coord.length - 1; i >= 0; i--) {
                add(coord[i], allowRepeated);
            }
        }
        return true;
    }

    /**
     * Add an array of coordinates
     *
     * @param coord The coordinates
     * @param allowRepeated if set to false, repeated coordinates are collapsed
     * @return true (as by general collection contract)
     */
    public boolean add(Coordinate[] coord, boolean allowRepeated) {
        add(coord, allowRepeated, true);
        return true;
    }

    /**
     * Add a coordinate
     *
     * @param obj The coordinate to add
     * @param allowRepeated if set to false, repeated coordinates are collapsed
     * @return true (as by general collection contract)
     */
    public boolean add(Object obj, boolean allowRepeated) {
        add((Coordinate) obj, allowRepeated);
        return true;
    }

    /**
     * Add a coordinate
     *
     * @param coord The coordinates
     * @param allowRepeated if set to false, repeated coordinates are collapsed
     */
    public void add(Coordinate coord, boolean allowRepeated) {
        // don't add duplicate coordinates
        if (!allowRepeated) {
            if (size() >= 1) {
                Coordinate last = (Coordinate) get(size() - 1);
                if (last.equals2D(coord)) return;
            }
        }
        super.add(coord);
    }

    /**
     * Add an array of coordinates
     *
     * @param coll The coordinates
     * @param allowRepeated if set to false, repeated coordinates are collapsed
     * @return true (as by general collection contract)
     */
    public boolean addAll(Collection coll, boolean allowRepeated) {
        boolean isChanged = false;
        for (Iterator i = coll.iterator(); i.hasNext(); ) {
            add((Coordinate) i.next(), allowRepeated);
            isChanged = true;
        }
        return isChanged;
    }

    /** Ensure this coordList is a ring, by adding the start point if necessary */
    public void closeRing() {
        if (size() > 0) add(get(0), false);
    }

    /**
     * Returns the Coordinates in this collection.
     *
     * @return the coordinates
     */
    public Coordinate[] toCoordinateArray() {
        return (Coordinate[]) toArray(coordArrayType);
    }

    /**
     * Returns a deep copy of this <tt>CoordinateList</tt> instance.
     *
     * @return a clone of this <tt>CoordinateList</tt> instance
     */
    public Object clone() {
        CoordinateList clone = (CoordinateList) super.clone();
        for (int i = 0; i < this.size(); i++) {
            clone.add(i, ((Coordinate) this.get(i)).clone());
        }
        return clone;
    }
}
