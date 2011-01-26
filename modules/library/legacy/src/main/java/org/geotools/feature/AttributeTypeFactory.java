/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.util.Collections;
import java.util.Map;

import org.geotools.factory.Factory;
import org.opengis.filter.Filter;


/**
 * Abstract class for AttributeType factories.  Extending classes need only
 * implement createAttributeType
 *
 * @author Ian Schneider, USDA-ARS
 * @source $URL$
 * @version $Id$
 */
public abstract class AttributeTypeFactory implements Factory {
    
    /**
     * Returns the default attribute factory for the system - constucting a new
     * one if this is first time the method has been called.
     *
     * @return the default instance of AttributeTypeFactory.
     */
    public static AttributeTypeFactory defaultInstance() {
        // depend on CommonFactoryFinder to keep singleton cached
        //
        return new DefaultAttributeTypeFactory();
    }

    /**
     * Returns a new instance of the current AttributeTypeFactory.
     * <p>
     * If no implementations are found then DefaultAttributeTypeFactory is returned.
     * </p>
     * @return A new instance of an AttributeTypeFactory.
     * @deprecated Please use CommonFactoryFinder
     */
    public static AttributeTypeFactory newInstance() {
        return new DefaultAttributeTypeFactory(); // only need new instance if factory stateful?
    }
    
    /**
     * Creates a new AttributeType with the given name, class and nillable
     * values.
     *
     * @param name The name of the AttributeType to be created.
     * @param clazz The class that objects will validate against.
     * @param isNillable If nulls are allowed (will force min=0)
     *
     * @return A new AttributeType of name, clazz and isNillable.
     */
    public static AttributeType newAttributeType(String name, Class clazz,
        boolean isNillable,int fieldLength,Object defaultValue) {
        return defaultInstance().createAttributeType(name, clazz, isNillable,fieldLength, defaultValue);
    }
    /**
     * Creates a new AttributeType with the addition of metadata like CRS.
     * <p>
     * Currently MetaData is used to supply the CoordinateSequence
     * when making a GeometryAttributeType.
     * </p>
     * @param name name of the attribute type to be created
     * @param clazz Class that objects will validate against
     * @param isNillable If nulls are allowed (will force min=0)
     * @param fieldLength A common restriction (this will produce a Filter)
     * @param defaultValue Initial valid value for new Feature 
     * @param metaData Additional information (example a CoordinateReferenceSystem for a Geometry)
     * 
     * @return Created AttributeType
     */
    public static AttributeType newAttributeType(String name, Class clazz,
        boolean isNillable,int fieldLength,Object defaultValue, Object metaData) {
        return defaultInstance().createAttributeType(name, clazz, isNillable,fieldLength, defaultValue, metaData);
    }
    /**
     * Creates a new AttributeType with the addition of metadata like CRS.
     * 
     * @param name name of the attribute type to be created
     * @param clazz Class that objects will validate against
     * @param isNillable If nulls are allowed (will force min=0)
     * @param restriction Filter restricting valid content 
     * @param defaultValue Initial valid value for new Feature
     * @param metaData Additional information (example a CoordinateReferenceSystem for a Geometry)
     * 
     * @return Created AttributeType
     */
    public static AttributeType newAttributeType(String name, Class clazz,
        boolean isNillable, Filter restriction,Object defaultValue, Object metaData) {
        return defaultInstance().createAttributeType(name, clazz, isNillable, restriction, defaultValue, metaData);
    }
    
    /**
     * Creates a new AttributeType.
     * 
     * @param name name of the attribute type to be created
     * @param clazz Class that objects will validate against
     * @param isNillable If nulls are allowed (will force min=0)
     * @param restriction 
     * @param defaultValue
     * @param metaData metaData Additional information (example a CoordinateReferenceSystem for a Geometry)
     * @param min minimum number of occurances for an array class
     * @param max maximum number of occurances for an array class
     * @return Created AttributeType
     */
    public static AttributeType newAttributeType(String name, Class clazz,
        boolean isNillable, Filter restriction,Object defaultValue, Object metaData, int min, int max ) {
        return defaultInstance().createAttributeType(name, clazz, isNillable, restriction, defaultValue, metaData, min, max);
    }
    /**
     * Creates a new AttributeType with the given name, class and nillable
     * values.
     *
     * @param name The name of the AttributeType to be created.
     * @param clazz The class that objects will validate against.
     * @param isNillable If nulls are allowed (will force min=0)
     *
     * @return A new AttributeType of name, clazz and isNillable.
     */
    public static AttributeType newAttributeType(String name, Class clazz,
        boolean isNillable,int fieldLength) {
        return defaultInstance().createAttributeType(name, clazz, isNillable,fieldLength);
    }

    /**
     * Creates a new AttributeType with the given name, class and nillable
     * values.
     *
     * @param name The name of the AttributeType to be created.
     * @param clazz The class that objects will validate against.
     * @param isNillable If nulls are allowed (will force min=0)
     *
     * @return A new AttributeType of name, clazz and isNillable.
     */
    public static AttributeType newAttributeType(String name, Class clazz,
        boolean isNillable) {
        return defaultInstance().createAttributeType(name, clazz, isNillable,Integer.MAX_VALUE);
    }

    /**
     * Convenience method to just specify name and class.  Nulls are allowed as
     * attributes by default (isNillable = <code>true</code>).
     *
     * @param name The name of the AttributeType to be created.
     * @param clazz The class that objects will validate against.
     *
     * @return A new AttributeType of name and clazz.
     */
    public static AttributeType newAttributeType(String name, Class clazz) {
        return newAttributeType(name, clazz, true);
    }

    /**
     * Constucts a new AttributeType that accepts Features (specified by a
     * FeatureType)
     *
     * @param name The name of the AttributeType to be created.
     * @param type the FeatureType that features will validate agist
     * @param isNillable If nulls are allowed (will force min=0)
     *
     * @return A new AttributeType of name, type, and isNillable.
     */
    public static AttributeType newAttributeType(String name, FeatureType type,
        boolean isNillable) {
        return defaultInstance().createAttributeType(name, type, isNillable);
    }

    /**
     * Constucts a new AttributeType that accepts Feature (specified by a
     * FeatureType).  Nulls are allowed as attributes by default (isNillable =
     * <code>true</code>).
     *
     * @param name The name of the AttributeType to be created.
     * @param type the FeatureType that features will validate agist
     *
     * @return A new AttributeType of name and type.
     */
    public static AttributeType newAttributeType(String name, FeatureType type) {
        return newAttributeType(name, type, true);
    }

    /**
     * Create an AttributeType with the given name, Class, nillability, 
     * fieldLength, and provided defaultValue.
     *
     * @param name The name of the AttributeType to be created.
     * @param clazz The class that objects will validate against.
     * @param isNillable if nulls are allowed in the new type.
     *
     * @return the new AttributeType
     * @throws IllegalArgumentException If the field is not nillable, yet
     */
    protected abstract AttributeType createAttributeType(String name,
        Class clazz, boolean isNillable, int fieldLength, Object defaultValue);
    
    /**
     * Create an AttributeType with the given name, Class, nillability, 
     * fieldLength, and provided defaultValue.
     *
     * @param name The name of the AttributeType to be created.
     * @param clazz The class that objects will validate against.
     * @param isNillable if nulls are allowed in the new type.
     * @param restriction Used to limit the valid values
     * @return the new AttributeType
     * @throws IllegalArgumentException If the field is not nillable, yet
     */
    protected abstract AttributeType createAttributeType(String name,
        Class clazz, boolean isNillable, Filter restriction, Object defaultValue, Object metadata);
    
    /**
     * Create an AttributeType with the given name, Class, nillability, 
     * fieldLength, and provided defaultValue.
     *
     * @param name The name of the AttributeType to be created.
     * @param clazz The class that objects will validate against.
     * @param isNillable if nulls are allowed in the new type.
     * @param restriction Used to limit the valid values
     * @param min the minimum number of occurences of the attribute
     * @param max the maximum number of occurences of the attribute
     * @return the new AttributeType
     * @throws IllegalArgumentException If the field is not nillable, yet
     */
    protected abstract AttributeType createAttributeType(
		String name, Class type, boolean isNillable, Filter restriction, Object defaultValue, 
		Object metaData, int min, int max 
	);
    
    /**
     * Create an AttributeType with the given name, Class, nillability, and
     * fieldLength, defering the defaultValue to the type of Attribute.
     */
    protected abstract AttributeType createAttributeType(String name,
        Class clazz, boolean isNillable, int fieldLength);

    /**
     * Create a Feature AttributeType which holds the a Feature instance which
     * is of the given FeatureType or null if any arbitrary Feature can be held.
     *
     * @param name The name of the AttributeType to be created.
     * @param type The FeatureType that Features will validate against.
     * @param isNillable if nulls are allowed in the new type.
     *
     * @return the new AttributeType
     */
    protected abstract AttributeType createAttributeType(String name, 
        FeatureType type, boolean isNillable);
        
    /**
     * Create a Feature AttributeType which holds the a Feature instance which
     * is of the given FeatureType or null if any arbitrary Feature can be held.
     *
     * @param name The name of the AttributeType to be created.
     * @param type The FeatureType that Features will validate against.
     * @param isNillable if nulls are allowed in the new type.
     * @param defaultValue default value, may be null if isNilable is true
     * @param metaData metadata for attribute type (such as CoordianteReferenceSystem)
     * @return the new AttributeType
     */        
    protected abstract AttributeType createAttributeType( String name, Class type, boolean isNillable, int fieldLength, Object defaultValue, Object metaData );        

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
