/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.type.Types;
import org.geotools.util.Converters;
import org.opengis.feature.Attribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * An implementation of SimpleFeature that has a more relaxed attitude about being valid.
 * 
 * @author Jesse Eichar
 *
 * @source $URL$
 */
public class LenientFeature extends SimpleFeatureImpl {
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.wfs");
    
    boolean constructing;
    /**
     * Creates a new instance of flat feature, which must take a flat feature
     * type schema and all attributes as arguments.
     *
     * @param schema Feature type schema for this flat feature.
     * @param attributes Initial attributes for this feature.
     * @param featureID The unique ID for this feature.
     *
     * @throws IllegalAttributeException Attribtues do not conform to feature
     *         type schema.
     * @throws NullPointerException if schema is null.
     */
    protected LenientFeature(List<Attribute> attributes, SimpleFeatureType schema, String featureID)
        throws IllegalAttributeException, NullPointerException {
        super( preFix(attributes, schema), checkSchema( schema),
              SimpleFeatureBuilder.createDefaultFeatureIdentifier(featureID ));
        // superclass just punts the values in ... we are going to validate if needed
        constructing=true;
        List<Object> values = toValues( attributes );
        //values = fixed( values );
        setAttributes(values);
        constructing=false;
    }
    
    private static List<Object> preFix( List<Attribute> attributes, SimpleFeatureType schema ){
        List result = new ArrayList();
        for (Attribute att : attributes) {
            result.add(att.getValue());
        }
        while( attributes.size() < schema.getAttributeCount() ){
            AttributeDescriptor required = schema.getDescriptor(attributes.size()-1);
            // or use required.getDefaultValue()
            // Attribute newAttribute = new LenientAttribute( null, required, null );
            attributes.add(null);
        }
        return result;
    }
    private static SimpleFeatureType checkSchema(SimpleFeatureType schema) {
        if (schema == null) {
            throw new NullPointerException("schema");
        }
        return schema;
    }

    /**
     * Sets the attribute at position to val.
     *
     * @param position the index of the attribute to set.
     * @param val the new value to give the attribute at position.
     *
     * @throws IllegalAttributeException if the passed in val does not validate
     *         against the AttributeType at that position.
     */
    public void setAttribute(int position, Object val)
        throws IllegalAttributeException {
        AttributeDescriptor type = getFeatureType().getDescriptor(position);
        try {
            
            if ((val == null) && !type.isNillable()) {
                val = type.getDefaultValue();
            }
            Object parsed = parse(type, val);
            
            try {
                Types.validate( type, parsed );
            } catch (Throwable e) {
                if( constructing ){
                    LOGGER.logp(Level.WARNING, "LenientFeature", "setAttribute", "Illegal Argument but ignored since we are being lenient",
                            e);
                } else {
                    throw new IllegalAttributeException(type, val, e);
                }
            }
            super.setAttribute(position, val);            
        } catch (IllegalArgumentException iae) {
            throw new IllegalAttributeException(type, val, iae);
        }
    }

    /**
     * Sets all attributes for this feature, passed in as a list.
     * @param attributes All feature attributes.
     * @throws IllegalAttributeException Passed attributes do not match feature
     *         type.
     */    
    public void setAttributes(List<Object> attributes) {
        if( constructing ){
            super.setAttributes( fixed( attributes ));
        }
        else {
            super.setAttributes( attributes );
        }
    }

    /** We are going to make this work no matter what so try and figure out some mapping */
    List<Object> fixed( List<Object> attributes ){
        if ( attributes == null ){
            attributes = Arrays.asList(new Object[getFeatureType().getAttributeCount()]);
        }
        if ( attributes.size() != getFeatureType().getAttributeCount() ) {
            String msg = "Expected " + getFeatureType().getAttributeCount() + " attributes but " 
                + attributes.size() + " were specified";
                throw new IllegalArgumentException( msg );                    
        }
        List<Object> fixed;
        fixed = assumeCorrectOrder( attributes );
        if( fixed == null ){
            fixed = greedyMatch(attributes);
        }
        return fixed;
    }
    /** Extract the values out of a properties  */
    static List<Object> toValues( List<Attribute> properties ){
        List<Object> values = new ArrayList<Object>();
        for( Property property : properties ){
            if(property != null)
                values.add( property.getValue() );
            else
                values.add(property);
        }
        return values;
    }

    /**
     * Try to figure out how to use the values in the order provided.
     * 
     * @param newAtts List of values in the correct order (may not be complete?)
     * @return List of objects in the correct order; or null if we could not do it
     */
    List<Object> assumeCorrectOrder( List<Object> newAtts ) {
        SimpleFeatureType schema = getFeatureType();
        List<Object> tmp = Arrays.asList(new Object[schema.getAttributeCount()]);
        for( int i = 0; i < newAtts.size() && i<schema.getAttributeCount(); i++ ) {
            Object object = newAtts.get(i);
            AttributeDescriptor att = schema.getDescriptor(i);
            if( object==null ){
                continue;
            }
            try {
                Object value = parse( att, object );
                tmp.set(i, value);
            }
            catch ( IllegalArgumentException cannotConvert ){
                return null; // we cannot use these values in the provided order
            }
        }
        return tmp;
    }

    List<Object> greedyMatch(List<Object> newAtts ) {
        SimpleFeatureType schema = getFeatureType();
        List<Object> relaxedAttrs=Arrays.asList(new Object[schema.getAttributeCount()]);
        boolean inValid = false;
        for( int i = 0; i < newAtts.size(); i++ ) {
            Object object = newAtts.get(i);
            boolean found = false;
            if( object==null )
                continue;
            Class realClass = object.getClass();
            for( int j = 0; j < schema.getAttributeCount(); j++ ) {
                AttributeDescriptor att = schema.getDescriptor(j);
                Class requiredClass = att.getType().getBinding();
                if( relaxedAttrs.get(j)==null && requiredClass.isAssignableFrom(realClass) ){
                    relaxedAttrs.set(j,object);
                    found=true;
                    break;
                }
            }
            if( !found ) {
                inValid=true;
            }
        }
        newAtts=relaxedAttrs;
        if( inValid ){
            StringBuffer buf=new StringBuffer();
            buf.append("WFSFeatureType#setAttributes(Object[]):");
            buf.append("\nAttributes were not correct for the feature Type:");
            buf.append(schema.getTypeName());
            buf.append(".  Made best guess:\n Recieved: ");
            for( int i = 0; i < newAtts.size(); i++ ) {
                Object object = newAtts.get(i);
                buf.append(object==null?"null":object.toString());
                buf.append(",");
            }
            buf.append("\nBest Guess: \n");
            for( int i = 0; i < relaxedAttrs.size(); i++ ) {
                Object object = relaxedAttrs.get(i);
                buf.append(object==null?"null":object.toString());
                buf.append(",");
            }

            LOGGER.warning(buf.toString());
        }
        return relaxedAttrs;
    }


    /**
     * Creates an exact copy of this feature.
     *
     * @return A default feature.
     *
     * @throws RuntimeException If some contents are not cloneable.
     */
    public Object clone() {
        try {
            LenientFeature clone = (LenientFeature) super.clone();
            for (int i = 0; i < getAttributeCount(); i++) {
                clone.setAttribute(i, getAttribute(i));                
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("The impossible has happened", e);
        }
    }
    /**
     * Old version of Types.parse that returns null when unhappy.
     */
    public static Object parse(AttributeDescriptor descriptor, Object value) throws IllegalArgumentException {
        if (value == null){
            if( descriptor.isNillable()){
                return descriptor.getDefaultValue();
            }
        }
        else {
            Class target = descriptor.getType().getBinding(); 
            if ( !target.isAssignableFrom( value.getClass() ) ) {
                // attempt to convert
                Object converted = Converters.convert(value,target);
                if( converted != null){
                    return converted;
                }
                throw new IllegalArgumentException("Could not convert");
            }
        }        
        return value;
    }
}
