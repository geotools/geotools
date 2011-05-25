/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Constants to identify JTS geometry types, reducing the need for boiler-plate
 * code such as this...
 * <pre><code>
 * if (Polygon.class.isAssignableFrom(myObject.getClass()) ||
 *         MultiPolygon.class.isAssignableFrom(myObject.getClass())) {
 *     // do polygon thing
 *     ...
 * } else if (LineString.class.isAssignableFrom(myObject.getClass()) ||
 *         MultiLineString.class.isAssignableFrom(myObject.getClass())) {
 *     // do line thing
 *     ....
 * } else {
 *     // do point thing
 *     ....
 * }
 * </code></pre>
 *
 * Instead you can do this...
 * <pre><code>
 * Geometries geomType = Geometries.get(myObject);
 * switch (geomType) {
 *     case POLYGON:
 *     case MULTIPOLYGON:
 *         // do polygon thing
 *         break;
 *
 *     case LINESTRING:
 *     case MULTILINESTRING:
 *         // do line thing
 *         break;
 *
 *     case POINT:
 *     case MULTIPOINT:
 *         // do point thing
 *         break;
 *
 *     default:
 *         // e.g. unspecified Geometry, GeometryCollection
 *         break;
 * }
 * </code></pre>
 * You can also work with {@code Class} objects...
 * <pre><code>
 * Class<? extends Geometry> aClass = ...
 * Geometries type = Geometries.getForBinding( aClass );
 * </code></pre>
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public enum Geometries {
    POINT(Point.class, 2001),

    LINESTRING(LineString.class, 2002),

    POLYGON(Polygon.class, 2003),

    MULTIPOINT(MultiPoint.class, 2004),

    MULTILINESTRING(MultiLineString.class, 2005),

    MULTIPOLYGON(MultiPolygon.class, 2006),

    GEOMETRY(Geometry.class, 2007),

    GEOMETRYCOLLECTION(GeometryCollection.class, 2008);

    private final Class<? extends Geometry> binding;
    private final int sqlType;
    private final String name;
    private final String simpleName;

    private Geometries(Class<? extends Geometry> type, int sqlType) {
        this.binding = type;
        this.sqlType = sqlType;
        this.name = type.getSimpleName();
        this.simpleName = (name.startsWith("Multi") ? name.substring(5) : name);
    }

    /**
     * Return the {@code Geometry} class associated with this type.
     *
     * @return the {@code Geometry} class
     */
    public Class<? extends Geometry> getBinding() {
        return binding;
    }

    /**
     * Return the integer SQL type code for this geometry type.
     *
     * @return integer code
     */
    public Integer getSQLType() {
        return Integer.valueOf(sqlType);
    }

    /**
     * Equivalent to {@linkplain #getName()}.
     *
     * @return the name of this type
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Return a name for this type that is suitable for text descriptions.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the 'simple name'. Returns the same value as {@linkplain #getName()}
     * except for MULTIPOINT, MULTILINESTRING and MULTIPOLYGON, for which it returns
     * the name without the 'Multi' prefix.
     *
     * @return the simple name
     */
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * Get the {@code Geometries} for the given object.
     *
     * @param geom a JTS Geometry object
     *
     * @return the {@code Geometries} for the argument's class, or {@code null}
     *         if the argument is {@code null}
     */
    public static Geometries get(Geometry geom) {
        if (geom != null) {
            return getForBinding(geom.getClass());
        }

        return null;
    }

    /**
     * Get the {@code Geometries} for the given {@code Geometry} class.
     *
     * @param geomClass the class
     *
     * @return the constant for this class
     */
    public static Geometries getForBinding(Class<? extends Geometry> geomClass) {
        for (Geometries gt : Geometries.values()) {
            if (gt.binding == geomClass) {
                return gt;
            }
        }
        
        //no direct match look for a subclass
        Geometries match = null;

        for (Geometries gt : Geometries.values()) {
            if (gt == GEOMETRY || gt == GEOMETRYCOLLECTION) {
                continue;
            }
            
            if (gt.binding.isAssignableFrom(geomClass)) {
                if (match == null) {
                    match = gt;
                } else {
                    // more than one match
                    return null;
                }
            }
        }
        
        if (match == null) {
            //no matches from concrete classes, try abstract classes
            if (GeometryCollection.class.isAssignableFrom(geomClass)) {
                return GEOMETRYCOLLECTION;
            }
            if (Geometry.class.isAssignableFrom(geomClass)) {
                return GEOMETRY;
            }
        }
        
        return match;
    }

    /**
     * Get the {@code Geometries} for the specified name.
     * 
     * @param name The name of the geometry, eg: "POINT"
     * 
     * @return The constant for the name.
     */
    public static Geometries getForName(String name) {
        for (Geometries gt : Geometries.values()) {
            if (gt.getName().equalsIgnoreCase(name)) {
                return gt;
            }
        }
        return null;
    }
    
    /**
     * Get the {@code Geometries} with the given integer SQL type code.
     *
     * @param sqlType the code to look up.
     *
     * @return the matching type or {@code null} if no match was found
     */
    public static Geometries getForSQLType(int sqlType) {
        for (Geometries gt : Geometries.values()) {
            if (gt.sqlType == sqlType) {
                return gt;
            }
        }

        return null;
    }
}
