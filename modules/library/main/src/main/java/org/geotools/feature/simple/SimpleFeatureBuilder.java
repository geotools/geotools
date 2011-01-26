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
package org.geotools.feature.simple;

import java.rmi.server.UID;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.Types;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.util.Converters;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A builder for features.
 * <p>
 * Simple Usage:
 * <code>
 *  <pre>
 *  //type of features we would like to build ( assume schema = (geom:Point,name:String) )
 *  SimpleFeatureType featureType = ...  
 * 
 *   //create the builder
 *  SimpleFeatureBuilder builder = new SimpleFeatureBuilder();
 *  
 *  //set the type of created features
 *  builder.setType( featureType );
 *  
 *  //add the attributes
 *  builder.add( new Point( 0 , 0 ) );
 *  builder.add( "theName" );
 *  
 *  //build the feature
 *  SimpleFeature feature = builder.buildFeature( "fid" );
 *  </pre>
 * </code>
 * </p>
 * <p>
 * This builder builds a feature by maintaining state. Each call to {@link #add(Object)}
 * creates a new attribute for the feature and stores it locally. When using the 
 * add method to add attributes to the feature, values added must be added in the 
 * same order as the attributes as defined by the feature type. The methods 
 * {@link #set(String, Object)} and {@link #set(int, Object)} are used to add 
 * attributes out of order.
 * </p>
 * <p>
 * Each time the builder builds a feature with a call to {@link #buildFeature(String)}
 * the internal state is reset. 
 * </p>
 * <p>
 * This builder can be used to copy features as well. The following code sample
 * demonstrates:
 * <code>
 * <pre>
 *  //original feature
 *  SimpleFeature original = ...;
 * 
 *  //create and initialize the builder
 *  SimpleFeatureBuilder builder = new SimpleFeatureBuilder();
 *  builder.init(original);
 * 
 *  //create the new feature
 *  SimpleFeature copy = builder.buildFeature( original.getID() );
 * 
 *  </pre>
 * </code>
 * </p>
 * <p>
 * The builder also provides a number of static "short-hand" methods which can 
 * be used when its not ideal to instantiate a new builder, thought this will
 * trigger some extra object allocations. In time critical code sections it's
 * better to instantiate the builder once and use it to build all the required
 * features.
 * <code>
 *   <pre>
 *   SimpleFeatureType type = ..;
 *   Object[] values = ...;
 *   
 *   //build a new feature
 *   SimpleFeature feature = SimpleFeatureBuilder.build( type, values, "fid" );
 *   
 *   ...
 *   
 *   SimpleFeature original = ...;
 *   
 *   //copy the feature
 *   SimpleFeature feature = SimpleFeatureBuilder.copy( original );
 *   </pre>
 * </code>
 * </p>
 * <p>
 * This class is not thread safe nor should instances be shared across multiple 
 * threads.
 * </p>
 *  
 * @author Justin Deoliveira
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class SimpleFeatureBuilder {
    /**
     * logger
     */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.feature");
    
    /** the feature type */
    SimpleFeatureType featureType;
    
    /** the feature factory */
    FeatureFactory factory;
    
    /** the attribute name to index index */
    Map<String, Integer> index;

    /** the values */
    //List<Object> values;
    Object[] values;
    
    /** pointer for next attribute */
    int next;
    
    Map<Object, Object>[] userData;
    
    Map<Object, Object> featureUserData;
    
    boolean validating;
    
    public SimpleFeatureBuilder(SimpleFeatureType featureType) {
        this(featureType, CommonFactoryFinder.getFeatureFactory(null));
    }
    
    public SimpleFeatureBuilder(SimpleFeatureType featureType, FeatureFactory factory) {
        this.featureType = featureType;
        this.factory = factory;

        if(featureType instanceof SimpleFeatureTypeImpl) {
            index = ((SimpleFeatureTypeImpl) featureType).index;
        } else {
            this.index = SimpleFeatureTypeImpl.buildIndex(featureType);
        }
        reset();
    }
    
    public void reset() {
        values = new Object[featureType.getAttributeCount()];
        next = 0;
        userData = null;
        featureUserData = null;
    }
    
    /**
     * Returns the simple feature type used by this builder as a feature template
     * @return
     */
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }
    
    /**
     * Initialize the builder with the provided feature.
     * <p>
     * This method adds all the attributes from the provided feature. It is 
     * useful when copying a feature. 
     * </p>
     */
    public void init( SimpleFeature feature ) {
        reset();
        
        // optimize the case in which we just build
        if(feature instanceof SimpleFeatureImpl) {
            SimpleFeatureImpl impl = (SimpleFeatureImpl) feature;
            System.arraycopy(impl.values, 0, values, 0, impl.values.length);
        } else {
            for (Object value : feature.getAttributes()) {
                add(value);
            }
        }
    }
    
    

    /**
     * Adds an attribute.
     * <p>
     * This method should be called repeatedly for the number of attributes as
     * specified by the type of the feature.
     * </p>
     */
    public void add(Object value) {
        set(next, value);
        next++;
    }

    /**
     * Adds a list of attributes.
     */
    public void addAll(List<Object> values) {
        for (int i = 0; i < values.size(); i++) {
            add(values.get(i));
        }
    }

    /**
     * Adds an array of attributes.
     */
    public void addAll(Object[] values) {
       addAll(Arrays.asList(values));
    }

    /**
     * Adds an attribute value by name.
     * <p>
     * This method can be used to add attribute values out of order.
     * </p>
     * 
     * @param name
     *            The name of the attribute.
     * @param value
     *            The value of the attribute.
     * 
     * @throws IllegalArgumentException
     *             If no such attribute with teh specified name exists.
     */
    public void set(Name name, Object value) {
        set(name.getLocalPart(), value);
    }

    /**
     * Adds an attribute value by name.
     * <p>
     * This method can be used to add attribute values out of order.
     * </p>
     * 
     * @param name
     *            The name of the attribute.
     * @param value
     *            The value of the attribute.
     * 
     * @throws IllegalArgumentException
     *             If no such attribute with teh specified name exists.
     */
    public void set(String name, Object value) {
        int index = featureType.indexOf(name);
        if (index == -1) {
            throw new IllegalArgumentException("No such attribute:" + name);
        }
        set(index, value);
    }

    /**
     * Adds an attribute value by index. *
     * <p>
     * This method can be used to add attribute values out of order.
     * </p>
     * 
     * @param index
     *            The index of the attribute.
     * @param value
     *            The value of the attribute.
     */
    public void set(int index, Object value) {
        if(index >= values.length)
            throw new ArrayIndexOutOfBoundsException("Can handle " 
                    + values.length + " attributes only, index is " + index);
        
        AttributeDescriptor descriptor = featureType.getDescriptor(index);
        values[index] = convert(value, descriptor);
        if(validating)
            Types.validate(descriptor, values[index]);
    }

    private Object convert(Object value, AttributeDescriptor descriptor) {
        //make sure the type of the value and the binding of the type match up
        if ( value != null ) {
            Class<?> target = descriptor.getType().getBinding(); 
            Object converted = Converters.convert(value, target);
            if(converted != null)
                value = converted;
        } else {
            //if the content is null and the descriptor says isNillable is false, 
            // then set the default value
            if (!descriptor.isNillable()) {
                value = descriptor.getDefaultValue();
                if ( value == null ) {
                    //no default value, try to generate one
                    value = DataUtilities.defaultValue(descriptor.getType().getBinding());
                }
            }
        }
        return value;
    }

    /**
     * Builds the feature.
     * <p>
     * The specified <tt>id</tt> may be <code>null</code>. In this case an
     * id will be generated internally by the builder.
     * </p>
     * <p>
     * After this method returns, all internal builder state is reset.
     * </p>
     * 
     * @param id
     *            The id of the feature, or <code>null</code>.
     * 
     * @return The new feature.
     */
    public SimpleFeature buildFeature(String id) {
        // ensure id
        if (id == null) {
            id = SimpleFeatureBuilder.createDefaultFeatureId();
        }

        Object[] values = this.values;
        Map<Object,Object>[] userData = this.userData;
        Map<Object,Object> featureUserData = this.featureUserData;
        reset();
        SimpleFeature sf = factory.createSimpleFeature(values, featureType, id);
        
        // handle the per attribute user data
        if(userData != null) {
            for (int i = 0; i < userData.length; i++) {
                if(userData[i] != null) {
                    sf.getProperty(featureType.getDescriptor(i).getName()).getUserData().putAll(userData[i]);
                }
            }
        }
        
        // handle the feature wide user data
        if(featureUserData != null) {
            sf.getUserData().putAll(featureUserData);
        }
        
        return sf;
    }
    
    /**
     * Quickly builds the feature using the specified values and id 
     * @param id
     * @param values
     * @return
     */
    public SimpleFeature buildFeature(String id, Object[] values ) {
        addAll( values );
        return buildFeature( id );
    }
    
    
    /**
     * Internal method for creating feature id's when none is specified.
     */
    public static String createDefaultFeatureId() {
          // According to GML and XML schema standards, FID is a XML ID
        // (http://www.w3.org/TR/xmlschema-2/#ID), whose acceptable values are those that match an
        // NCNAME production (http://www.w3.org/TR/1999/REC-xml-names-19990114/#NT-NCName):
        // NCName ::= (Letter | '_') (NCNameChar)* /* An XML Name, minus the ":" */
        // NCNameChar ::= Letter | Digit | '.' | '-' | '_' | CombiningChar | Extender
        // We have to fix the generated UID replacing all non word chars with an _ (it seems
        // they area all ":")
        //return "fid-" + NON_WORD_PATTERN.matcher(new UID().toString()).replaceAll("_");
        // optimization, since the UID toString uses only ":" and converts long and integers
        // to strings for the rest, so the only non word character is really ":"
        return "fid-" + new UID().toString().replace(':', '_');
    }
    /**
     * Internal method for a temporary FeatureId that can be assigned
     * a real value after a commit.
     * @param suggestedId suggsted id
     */
    public static FeatureIdImpl createDefaultFeatureIdentifier( String suggestedId ) {
    	if( suggestedId != null ){
    		return new FeatureIdImpl( suggestedId );	
    	}
    	return new FeatureIdImpl( createDefaultFeatureId() );
    }
    
    
    /**
     * Static method to build a new feature.
     * <p>
     * If multiple features need to be created, this method should not be used
     * and instead an instance should be instantiated directly.
     * </p>
     * <p>
     * This method is a short-hand convenience which creates a builder instance
     * internally and adds all the specified attributes.
     * </p>
     * @param type SimpleFeatureType defining the structure for the created feature
     * @param values Attribute values, must be in the order defined by SimpleFeatureType
     * @param id FeatureID for the generated feature, use null to allow one to be supplied for you
     */
    public static SimpleFeature build( SimpleFeatureType type, Object[] values, String id ) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.addAll(values);
        return builder.buildFeature(id);
    }
    
    /**
     * * Static method to build a new feature.
     * <p>
     * If multiple features need to be created, this method should not be used
     * and instead an instance should be instantiated directly.
     * </p>
     * @param type SimpleFeatureType defining the structure for the created feature
     * @param values Attribute values, must be in the order defined by SimpleFeatureType
     * @param id FeatureID for the generated feature, use null to allow one to be supplied for you
     */
    public static SimpleFeature build( SimpleFeatureType type, List<Object> values, String id ) {
        return build( type, values.toArray(), id );
    }
    
    /**
     * Copy an existing feature (the values are reused so be careful with mutable values).
     * <p>
     * If multiple features need to be copied, this method should not be used
     * and instead an instance should be instantiated directly.
     * </p>
     * <p>
     * This method is a short-hand convenience which creates a builder instance
     * and initializes it with the attributes from the specified feature.
     * </p>
     */
    public static SimpleFeature copy(SimpleFeature original) {
        if( original == null ) return null;
        
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(original.getFeatureType());
        builder.init(original); // this is a shallow copy
        return builder.buildFeature(original.getID());
    }
    
    /**
     * Perform a "deep copy" an existing feature resuling in a duplicate of any geometry
     * attributes.
     * <p>
     * This method is scary, expensive and will result in a deep copy of
     * Geometry which may take a significant amount of memory/time to perform.
     * </p>
     * @param original Content
     * @return copy
     */
    public static SimpleFeature deep(SimpleFeature original) {
        if (original == null)
            return null;

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(original.getFeatureType());
        for (Property property : original.getProperties()) {
            Object value = property.getValue();
            try {
                Object copy = value;
                if (value instanceof Geometry) {
                    Geometry geometry = (Geometry) value;
                    copy = geometry.clone();
                }
                builder.set(property.getName(), copy);
            } catch (Exception e) {
                throw new IllegalAttributeException(
                        (AttributeDescriptor) property.getDescriptor(), value, e );
            }
        }
        return builder.buildFeature(original.getID());
    }
    
    /**
     * Builds a new feature whose attribute values are the default ones
     * @param featureType
     * @param featureId
     * @return
     */
    public static SimpleFeature template(SimpleFeatureType featureType, String featureId) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
            builder.add(ad.getDefaultValue());
        }
        return builder.buildFeature(featureId);
    }
        
    /**
     * Copies an existing feature, retyping it in the process. 
     * <p> Be warned, this method will
     * create its own SimpleFeatureBuilder, which will trigger a scan of the SPI looking for 
     * the current default feature factory, which is expensive and has scalability issues.<p>  
     * If you need good performance consider using 
     * {@link SimpleFeatureBuilder#retype(SimpleFeature, SimpleFeatureBuilder)} instead.
     * <p>
     * If the feature type contains attributes in which the original feature 
     * does not have a value for, the value in the resulting feature is set to
     * <code>null</code>.
     * </p>
     * @param feature The original feature.
     * @param featureType The target feature type.
     *  
     * @return The copied feature, with a new type.
     */
    public static SimpleFeature retype(SimpleFeature feature, SimpleFeatureType featureType) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            Object value = feature.getAttribute( att.getName() );
            builder.set(att.getName(), value);
        }
        return builder.buildFeature(feature.getID());
    }
    
    /**
     * Copies an existing feature, retyping it in the process. 
     * <p>
     * If the feature type contains attributes in which the original feature 
     * does not have a value for, the value in the resulting feature is set to
     * <code>null</code>.
     * </p>
     * @param feature The original feature.
     * @param SimpleFeatureBuilder A builder for the target feature type
     *  
     * @return The copied feature, with a new type.
     * @since 2.5.3
     */
    public static SimpleFeature retype(SimpleFeature feature, SimpleFeatureBuilder builder) {
        builder.reset();
        for (AttributeDescriptor att : builder.getFeatureType().getAttributeDescriptors()) {
            Object value = feature.getAttribute( att.getName() );
            builder.set(att.getName(), value);
        }
        return builder.buildFeature(feature.getID());
    }
    
    /**
     * Adds some user data to the next attributed added to the feature.
     * <p>
     * This value is reset when the next attribute is added. 
     * </p>
     * @param key The key of the user data
     * @param value The value of the user data.
    */
    public SimpleFeatureBuilder userData( Object key, Object value ) {
        return setUserData(next, key, value);
    }
    
    @SuppressWarnings("unchecked")
    public SimpleFeatureBuilder setUserData(int index, Object key, Object value) {
        if (userData == null) {
            userData = new Map[values.length];
        }
        if (userData[index] == null) {
            userData[index] = new HashMap<Object, Object>();
        }
        userData[index].put(key, value);
        return this;
    }
    
    /**
     * Sets a feature wide use data key/value pair. The user data map is reset
     * when the feature is built
     * @param key
     * @param value
     * @return
     */
    public SimpleFeatureBuilder featureUserData(Object key, Object value) {
        if(featureUserData == null) {
            featureUserData = new HashMap<Object, Object>();
        }
        featureUserData.put(key, value);
        return this;
    }
    
    public boolean isValidating() {
        return validating;
    }

    public void setValidating(boolean validating) {
        this.validating = validating;
    }
}
