/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map.event;

import java.util.EventObject;

import org.geotools.map.MapContext;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.geotools.geometry.jts.ReferencedEnvelope;


/**
 * Event object for MapContext area of interest and coordinate system changes.
 *
 * @author wolf
 * @source $URL$
 */
public class MapBoundsEvent extends EventObject {
    /** Area of interest changed */
    public static final int AREA_OF_INTEREST_MASK = 1;

    /** Coordinate system changed */
    public static final int COORDINATE_SYSTEM_MASK = 2;

    /** Used to check that the type flag is acceptable */
    private static final int NEXT_FLAG = 4;

    /** Holds value of property type. */
    private int type;

    private ReferencedEnvelope oldAreaOfInterest;
    private ReferencedEnvelope newAreaOfInterest;

    /**
     * Creates a new instance of BoundsEvent
     *
     * @param source the map context reporting the change
     *
     * @param type the type of change indicated by one or both of the bit masks
     *        {@linkplain #AREA_OF_INTEREST_MASK} and {@linkplain #COORDINATE_SYSTEM_MASK}
     *
     * @param oldAreaOfInterest the context's previous area of interest
     *
     * @param newAreaOfInterest the context's new area of interest
     *
     * @throws IllegalArgumentException if type is invalid
     */
    public MapBoundsEvent(MapContext source, int type,
            ReferencedEnvelope oldAreaOfInterest, ReferencedEnvelope newAreaOfInterest) {
        super(source);

        if (type >= NEXT_FLAG) {
            throw new IllegalArgumentException("Type is not acceptable, maximum value is "
                + (NEXT_FLAG - 1) + ", passed value is " + type);
        }

        this.type = type;
        this.oldAreaOfInterest = oldAreaOfInterest;
        this.newAreaOfInterest = newAreaOfInterest;
    }

    /**
     * Getter for property type. The type is a bitwise or of the masks defined above.
     *
     * @return Value of property type.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Get the previous coordinate reference system. This is a convenience
     * method equivalent to
     * {@linkplain #getOldAreaOfInterest()}.getCoordinateReferenceSystem()
     *
     * @return the previous CoordinateReferenceSystem object
     */
    public CoordinateReferenceSystem getOldCoordinateReferenceSystem() {
        return oldAreaOfInterest.getCoordinateReferenceSystem();
    }

    /**
     * Get the new coordinate reference system. This is a convenience
     * method equivalent to
     * {@linkplain #getNewAreaOfInterest()}.getCoordinateReferenceSystem()
     *
     * @return the new CoordinateReferenceSystem object
     */
    public CoordinateReferenceSystem getNewCoordinateReferenceSystem() {
        return newAreaOfInterest.getCoordinateReferenceSystem();
    }

    /**
     * Get the old area of interest
     */
    public ReferencedEnvelope getOldAreaOfInterest() {
        return this.oldAreaOfInterest;
    }

    /**
     * Get the new area of interest
     */
    public ReferencedEnvelope getNewAreaOfInterest() {
        return this.newAreaOfInterest;
}
}
