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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureFactory;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureBuilder;
import org.geotools.feature.type.Types;
import org.locationtech.jts.geom.Geometry;

/**
 * A builder for features.
 *
 * <p>Simple Usage: <code>
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
 *
 * <p>This builder builds a feature by maintaining state. Each call to {@link #add(Object)} creates a new attribute for
 * the feature and stores it locally. When using the add method to add attributes to the feature, values added must be
 * added in the same order as the attributes as defined by the feature type. The methods {@link #set(String, Object)}
 * and {@link #set(int, Object)} are used to add attributes out of order.
 *
 * <p>Each time the builder builds a feature with a call to {@link #buildFeature(String)} the internal state is reset.
 *
 * <p>This builder can be used to copy features as well. The following code sample demonstrates: <code>
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
 *
 * <p>The builder also provides a number of static "short-hand" methods which can be used when its not ideal to
 * instantiate a new builder, thought this will trigger some extra object allocations. In time critical code sections
 * it's better to instantiate the builder once and use it to build all the required features. <code>
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
 *
 * <p>This class is not thread safe nor should instances be shared across multiple threads.
 *
 * @author Justin Deoliveira
 * @author Jody Garnett
 */
public class SimpleFeatureBuilder extends FeatureBuilder<FeatureType, Feature> {
    /** logger */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SimpleFeatureBuilder.class);

    /** the feature type */
    SimpleFeatureType featureType;

    /** the feature factory */
    FeatureFactory factory;

    /** the attribute name to index index */
    Map<String, Integer> index;

    /** the values */
    Object[] values;

    /** pointer for next attribute */
    int next;

    /** Attribute userData by index. */
    Map<Object, Object>[] userData;

    Map<Object, Object> featureUserData;

    boolean validating;

    public SimpleFeatureBuilder(SimpleFeatureType featureType) {
        this(featureType, CommonFactoryFinder.getFeatureFactory(null));
    }

    public SimpleFeatureBuilder(SimpleFeatureType featureType, FeatureFactory factory) {
        super(featureType, factory);
        this.featureType = featureType;
        this.factory = factory;

        if (featureType instanceof SimpleFeatureTypeImpl) {
            index = ((SimpleFeatureTypeImpl) featureType).index;
        } else {
            this.index = SimpleFeatureTypeImpl.buildIndex(featureType);
        }
        reset();
    }

    /** Reset the builder, for generating a new feature. */
    public void reset() {
        values = new Object[featureType.getAttributeCount()];
        next = 0;
        userData = null;
        featureUserData = null;
    }

    /** Returns the simple feature type used by this builder as a feature template. */
    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /**
     * Initialize the builder with the provided feature.
     *
     * <p>This method adds all the attributes from the provided feature (along with user data if provided). It is useful
     * when copying a feature.
     *
     * @param feature Feature to copy
     */
    public void init(SimpleFeature feature) {
        reset();

        // optimize the case in which we just build
        if (feature instanceof SimpleFeatureImpl) {
            SimpleFeatureImpl impl = (SimpleFeatureImpl) feature;
            System.arraycopy(impl.values, 0, values, 0, impl.values.length);

            if (impl.userData != null) {
                featureUserData = new HashMap<>(impl.userData);
            }
        } else {
            for (Object value : feature.getAttributes()) {
                add(value);
            }

            if (!feature.getUserData().isEmpty()) {
                featureUserData = new HashMap<>(feature.getUserData());
            }
        }
    }

    /**
     * Adds an attribute.
     *
     * <p>This method should be called repeatedly for the number of attributes as specified by the type of the feature.
     */
    public void add(Object value) {
        set(next, value);
        next++;
    }

    /** Adds a list of attributes, in order provided. */
    public void addAll(List<Object> values) {
        for (Object value : values) {
            add(value);
        }
    }

    /** Adds an array of attributes, in order provided. */
    public void addAll(Object... values) {
        for (Object value : values) {
            add(value);
        }
    }

    /**
     * Adds an attribute value by name.
     *
     * <p>This method can be used to add attribute values out of order.
     *
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     * @throws IllegalArgumentException If no such attribute with the specified name exists.
     */
    public void set(Name name, Object value) {
        set(name.getLocalPart(), value);
    }

    /**
     * Adds an attribute value by name.
     *
     * <p>This method can be used to add attribute values out of order.
     *
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     * @throws IllegalArgumentException If no such attribute with the specified name exists.
     */
    public void set(String name, Object value) {
        int index = featureType.indexOf(name);
        if (index == -1) {
            throw new IllegalArgumentException("No such attribute:" + name);
        }
        set(index, value);
    }

    /**
     * Adds an attribute value by index.
     *
     * <p>This method can be used to add attribute values out of order.
     *
     * <p>The provided value is converted to an object of the required type if required.
     *
     * <p>If {@link #isValidating()} enabled the resulting object is validated against, any restrictions imposed by the
     * attribute descriptor.
     *
     * @param index The index of the attribute.
     * @param value The value of the attribute.
     */
    public void set(int index, Object value) {
        if (index >= values.length)
            throw new ArrayIndexOutOfBoundsException(
                    "Can handle " + values.length + " attributes only, index is " + index);

        AttributeDescriptor descriptor = featureType.getDescriptor(index);
        values[index] = convert(value, descriptor);
        if (validating) Types.validate(descriptor, values[index]);
    }

    /**
     * Convert value into the correct type for the descriptor (supplying a default value if required).
     *
     * @param value value, or {@code null}
     * @param descriptor Attribute descriptor providing type information and default value
     * @return object of the correct type for the descriptor
     */
    private Object convert(Object value, AttributeDescriptor descriptor) {
        if (value == null) {
            // if the content is null and the descriptor says isNillable is false,
            // then set the default value
            if (!descriptor.isNillable()) {
                value = descriptor.getDefaultValue();
                if (value == null) {
                    // no default value, try to generate one
                    value = DataUtilities.defaultValue(descriptor.getType().getBinding());
                }
            }
        } else {
            // make sure the type of the value and the binding of the type match up
            value = super.convert(value, descriptor);
        }
        return value;
    }

    /**
     * Builds the feature.
     *
     * <p>The specified <tt>id</tt> may be <code>null</code>. In this case an id will be generated internally by the
     * builder.
     *
     * <p>After this method returns, all internal builder state is reset.
     *
     * @param id The id of the feature, or <code>null</code>.
     * @return The new feature.
     */
    @Override
    public SimpleFeature buildFeature(String id) {
        // ensure id
        if (id == null) {
            id = SimpleFeatureBuilder.createDefaultFeatureId();
        }

        Object[] values = this.values;
        Map<Object, Object>[] userData = this.userData;
        Map<Object, Object> featureUserData = this.featureUserData;
        reset();
        SimpleFeature sf = factory.createSimpleFeature(values, featureType, id);

        // handle the per attribute user data
        if (userData != null) {
            for (int i = 0; i < userData.length; i++) {
                if (userData[i] != null) {
                    sf.getProperty(featureType.getDescriptor(i).getName())
                            .getUserData()
                            .putAll(userData[i]);
                }
            }
        }

        // handle the feature wide user data
        if (featureUserData != null) {
            sf.getUserData().putAll(featureUserData);
        }

        if (this.validating) {
            Types.validate(sf);
        }

        return sf;
    }

    /** Quickly builds the feature using the specified values and id */
    public SimpleFeature buildFeature(String id, Object... values) {
        addAll(values);
        return buildFeature(id);
    }

    /**
     * Static method to build a new feature.
     *
     * <p>If multiple features need to be created, this method should not be used and instead an instance should be
     * instantiated directly.
     *
     * <p>This method is a short-hand convenience which creates a builder instance internally and adds all the specified
     * attributes.
     *
     * @param type SimpleFeatureType defining the structure for the created feature
     * @param values Attribute values, must be in the order defined by SimpleFeatureType
     * @param id FeatureID for the generated feature, use null to allow one to be supplied for you
     */
    public static SimpleFeature build(SimpleFeatureType type, Object[] values, String id) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.addAll(values);
        return builder.buildFeature(id);
    }

    /**
     * Static method to build a new feature.
     *
     * <p>Simple feature is very forgiving willing to convert values, and supply default values for any missing values.
     *
     * <p>If you have an array of values that exactly match your SimpleFeatureType it is faster to instantiate directly
     * using {@link FeatureFactory#createSimpleFeautre(Object[], AttributeDescriptor, String)}.
     *
     * @param type SimpleFeatureType defining the structure for the created feature
     * @param values Attribute values, must be in the order defined by SimpleFeatureType
     * @param id FeatureID for the generated feature, use null to allow one to be supplied for you
     */
    public static SimpleFeature build(SimpleFeatureType type, List<Object> values, String id) {
        final int ATTRIBUTE_COUNT = type.getAttributeCount();
        final int VALUE_COUNT = values.size();
        if (ATTRIBUTE_COUNT < VALUE_COUNT) {
            LOGGER.fine(String.format(
                    "%s '%s' limited to the first %d values, out of a total %d values provided",
                    type.getTypeName(), id, ATTRIBUTE_COUNT, VALUE_COUNT));
            values = values.subList(0, type.getAttributeCount());
        } else if (ATTRIBUTE_COUNT > VALUE_COUNT) {
            LOGGER.fine(String.format(
                    "%s '%s' used the first %d values, using default values for remaining %d attributes.",
                    type.getTypeName(), id, VALUE_COUNT, ATTRIBUTE_COUNT - VALUE_COUNT));
            values = new ArrayList<>(values);
            values.addAll(Collections.nCopies(ATTRIBUTE_COUNT - VALUE_COUNT, null));
        }
        return build(type, values.toArray(), id);
    }

    /**
     * Copy an existing feature (the values are reused so be careful with mutable values).
     *
     * <p>If multiple features need to be copied, this method should not be used and instead an instance should be
     * instantiated directly.
     *
     * <p>This method is a short-hand convenience which creates a builder instance and initializes it with the
     * attributes from the specified feature.
     */
    public static SimpleFeature copy(SimpleFeature original) {
        if (original == null) return null;

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(original.getFeatureType());
        builder.init(original); // this is a shallow copy
        return builder.buildFeature(original.getID());
    }

    /**
     * Perform a "deep copy" an existing feature resuling in a duplicate of any geometry attributes.
     *
     * <p>This method is scary, expensive and will result in a deep copy of Geometry which may take a significant amount
     * of memory/time to perform.
     *
     * @param original Content
     * @return copy
     */
    public static SimpleFeature deep(SimpleFeature original) {
        if (original == null) return null;

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(original.getFeatureType());
        for (Property property : original.getProperties()) {
            Object value = property.getValue();
            try {
                Object copy = value;
                if (value instanceof Geometry) {
                    Geometry geometry = (Geometry) value;
                    copy = geometry.copy();
                }
                builder.set(property.getName(), copy);
            } catch (Exception e) {
                throw new IllegalAttributeException((AttributeDescriptor) property.getDescriptor(), value, e);
            }
        }
        return builder.buildFeature(original.getID());
    }

    /** Builds a new feature whose attribute values are the default ones */
    public static SimpleFeature template(SimpleFeatureType featureType, String featureId) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
            builder.add(ad.getDefaultValue());
        }
        return builder.buildFeature(featureId);
    }

    /**
     * Copies an existing feature, retyping it in the process.
     *
     * <p>Be warned, this method will create its own SimpleFeatureBuilder, which will trigger a scan of the SPI looking
     * for the current default feature factory, which is expensive and has scalability issues.
     *
     * <p>If you need good performance consider using {@link SimpleFeatureBuilder#retype(SimpleFeature,
     * SimpleFeatureBuilder)} instead.
     *
     * <p>If the feature type contains attributes in which the original feature does not have a value for, the value in
     * the resulting feature is set to <code>null</code>.
     *
     * @param feature The original feature.
     * @param featureType The target feature type.
     * @return The copied feature, with a new type.
     */
    public static SimpleFeature retype(SimpleFeature feature, SimpleFeatureType featureType) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            Object value = feature.getAttribute(att.getName());
            builder.set(att.getName(), value);
        }
        return builder.buildFeature(feature.getID());
    }

    /**
     * Copies an existing feature, retyping it in the process.
     *
     * <p>If the feature type contains attributes in which the original feature does not have a value for, the value in
     * the resulting feature is set to <code>null</code>.
     *
     * @param feature The original feature.
     * @param builder A builder for the target feature type
     * @return The copied feature, with a new type.
     * @since 2.5.3
     */
    public static SimpleFeature retype(SimpleFeature feature, SimpleFeatureBuilder builder) {
        builder.reset();
        for (AttributeDescriptor att : builder.getFeatureType().getAttributeDescriptors()) {
            Object value = feature.getAttribute(att.getName());
            builder.set(att.getName(), value);
        }
        return builder.buildFeature(feature.getID());
    }

    /**
     * Adds some user data to the next attribute added to the feature.
     *
     * <p>This value is reset when the next attribute is added.
     *
     * @param key The key of the user data
     * @param value The value of the user data.
     */
    public SimpleFeatureBuilder userData(Object key, Object value) {
        return setUserData(next, key, value);
    }

    /**
     * Set user data for a specific attribute.
     *
     * @param index The index of the attribute.
     * @param key The key of the user data.
     * @param value The value of the user data.
     */
    @SuppressWarnings("unchecked")
    public SimpleFeatureBuilder setUserData(int index, Object key, Object value) {
        if (userData == null) {
            userData = new Map[values.length];
        }
        if (userData[index] == null) {
            userData[index] = new HashMap<>();
        }
        userData[index].put(key, value);
        return this;
    }

    /** Sets the feature wide user data copying them from the template feature provided */
    public SimpleFeatureBuilder featureUserData(SimpleFeature source) {
        Map<Object, Object> sourceUserData = source.getUserData();
        if (sourceUserData != null && !sourceUserData.isEmpty()) {
            if (featureUserData == null) {
                featureUserData = new HashMap<>();
            }
            featureUserData.putAll(sourceUserData);
        }
        return this;
    }

    /**
     * Sets a feature wide use data key/value pair.
     *
     * <p>The user data map is reset when the feature is built.
     */
    public SimpleFeatureBuilder featureUserData(Object key, Object value) {
        if (featureUserData == null) {
            featureUserData = new HashMap<>();
        }
        featureUserData.put(key, value);
        return this;
    }

    /**
     * True if values are validated when added to builder.
     *
     * @return
     */
    public boolean isValidating() {
        return validating;
    }

    public void setValidating(boolean validating) {
        this.validating = validating;
    }
}
