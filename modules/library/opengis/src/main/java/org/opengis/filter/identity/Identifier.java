/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.identity;

/**
 * An object identifier.
 * <p>
 * This class is an abstract base for identifiers. Some known identifiers are:
 * <ul>
 *   <li>FeatureId</li>
 *   <li>GMLObjectId</li>
 *   <li>RecordId</li>
 * </ul>
 * </p>
 *
 * @param <T> The type of the identifier itself.
 * @param <O> The type of objects to be identified.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/filter/identity/Identifier.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Jody Garnett (Refractions Research)
 * @author Justin Deoliveira (The Open Planning Project)
 */
public interface Identifier {
    /**
     * Returns the identifier itself.
     */
    Object getID();

    /**
     * Determines if the id of an object matches the value of the identifier.
     *
     * @param object The object to perform the test against.
     * @return {@code true} if a match, otherwise {@code false}.
     */
    boolean matches(Object object);

    /**
     * Identifier is a data object, equals is based just on getID()
     * @param obj
     * @return true if obj is an Identifier with the same getID()
     */
    ///@Override
    public boolean equals(Object obj);
    
    /**
     * Identifier is a data object, hashCode is based just on getID()
     * @return hashCode based on getID()
     */
    ///@Override
    public int hashCode();
    
    /**
     * Returns a string representation of the identifier.
     * @return getID().toString()
     */
    ///@Override 
    String toString();
}
