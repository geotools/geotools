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

import java.util.EnumSet;
import java.util.EventObject;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContext;
import org.geotools.map.MapViewport;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Event object for MapContext area of interest and coordinate system changes.
 * 
 * @author wolf
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools
 *         /map/event/MapBoundsEvent.java $
 */
public class MapBoundsEvent extends EventObject {
    private static final long serialVersionUID = -2063712912599101999L;

    /** Area of interest changed, mask used by {@link Type#BOUNDS} */
    public static final int AREA_OF_INTEREST_MASK = 1;

    /** Coordinate system changed, mask used by {@link Type#CRS} */
    public static final int COORDINATE_SYSTEM_MASK = 2;

    /** Used to check that the type flag is acceptable */
    private static final int NEXT_FLAG = 4;

    /** Type of map bounds event */
    public enum Type {
        BOUNDS(AREA_OF_INTEREST_MASK), CRS(COORDINATE_SYSTEM_MASK);

        final private int MASK;

        private Type(int mask) {
            this.MASK = mask;
        }

        /**
         * Return the traditional mask value one of: {@link MapBoundsEvent#AREA_OF_INTEREST_MASK} or
         * {@link MapBoundsEvent#COORDINATE_SYSTEM_MASK}
         */
        public int getMask() {
            return MASK;
        }
    }

    /** Holds value of property type. */
    private EnumSet<Type> type;

    private ReferencedEnvelope oldAreaOfInterest;

    private ReferencedEnvelope newAreaOfInterest;

    /**
     * Creates a new instance of BoundsEvent
     * 
     * @param source
     *            the map context reporting the change
     * 
     * @param type
     *            the type of change indicated by one or both of the bit masks
     *            {@linkplain #AREA_OF_INTEREST_MASK} and {@linkplain #COORDINATE_SYSTEM_MASK}
     * 
     * @param oldAreaOfInterest
     *            the context's previous area of interest
     * 
     * @param newAreaOfInterest
     *            the context's new area of interest
     * 
     * @throws IllegalArgumentException
     *             if type is invalid
     */
    public MapBoundsEvent(MapContext source, int type, ReferencedEnvelope oldAreaOfInterest,
            ReferencedEnvelope newAreaOfInterest) {
        super(source);

        this.type = EnumSet.noneOf(Type.class);
        if ((type & AREA_OF_INTEREST_MASK) != 0) {
            this.type.add(Type.BOUNDS);
        } else if ((type & COORDINATE_SYSTEM_MASK) != 0) {
            this.type.add(Type.CRS);
        } else {
            throw new IllegalArgumentException("Type is not acceptable, maximum value is "
                    + (NEXT_FLAG - 1) + ", passed value is " + type);
        }
        this.oldAreaOfInterest = oldAreaOfInterest;
        this.newAreaOfInterest = newAreaOfInterest;
    }

    /**
     * Creates a new instance of BoundsEvent.
     * <p>
     * Example:
     * 
     * <pre>
     * new MapBoundsEvent(map, EnumSet.of(Type.BOUNDS), null, bounds)
     * </pre>
     * 
     * @param source
     *            map viewport reporting the change
     * 
     * @param type
     *            Type of event indicating {@link Type#BOUNDS} or {@link Type#CRS}
     * 
     * @param oldAreaOfInterest
     *            the context's previous area of interest
     * 
     * @param newAreaOfInterest
     *            the context's new area of interest
     * 
     * @throws IllegalArgumentException
     *             if type is invalid
     */
    public MapBoundsEvent(MapViewport source, Type type, ReferencedEnvelope oldBounds,
            ReferencedEnvelope newBounds) {
        this(source, EnumSet.of(type), oldBounds, newBounds);
    }

    /**
     * Creates a new instance of BoundsEvent.
     * <p>
     * Example:
     * 
     * <pre>
     * new MapBoundsEvent(map, EnumSet.of(Type.BOUNDS), null, bounds)
     * </pre>
     * 
     * @param source
     *            map viewport reporting the change
     * 
     * @param type
     *            EnumSet flagging one or both of the following {@linkplain #AREA_OF_INTEREST_MASK}
     *            and {@linkplain #COORDINATE_SYSTEM_MASK}
     * 
     * @param oldAreaOfInterest
     *            the context's previous area of interest
     * 
     * @param newAreaOfInterest
     *            the context's new area of interest
     * 
     * @throws IllegalArgumentException
     *             if type is invalid
     */
    public MapBoundsEvent(MapViewport source, EnumSet<Type> type, ReferencedEnvelope oldBounds,
            ReferencedEnvelope newBounds) {
        super(source);
        this.type = type;
        this.oldAreaOfInterest = oldBounds;
        this.newAreaOfInterest = newBounds;
    }

    /**
     * Getter for property type. The type is a bitwise or of the masks defined above.
     * 
     * @return Value of property type.
     */
    public int getType() {
        int field = 0;
        for (Type t : type) {
            field |= t.getMask();
        }
        return field;
    }

    /**
     * Set of event types raised for this event.
     * 
     * @return
     */
    public EnumSet<Type> getEventType() {
        return type;
    }

    /**
     * Get the previous coordinate reference system. This is a convenience method equivalent to
     * {@linkplain #getOldAreaOfInterest()}.getCoordinateReferenceSystem()
     * 
     * @return the previous CoordinateReferenceSystem object
     */
    public CoordinateReferenceSystem getOldCoordinateReferenceSystem() {
        return oldAreaOfInterest.getCoordinateReferenceSystem();
    }

    /**
     * Get the new coordinate reference system. This is a convenience method equivalent to
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
