/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.complex.feature.wrapper;

import java.io.InvalidClassException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import org.geotools.feature.NameImpl;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.Name;

/**
 * You can make feature wrappers for specific types by extending this class and annotating the
 * descendant class's fields with {@link XSDMapping} to show what they correspond to in the XSD.
 */
public abstract class FeatureWrapper {
    /** Backing field for the underlying complex attribute. */
    private ComplexAttribute underlyingComplexAttribute;

    /**
     * Gets the underlying complex attribute. That is, the complex attribute that was wrapped. NB:
     * This could be a Feature.
     *
     * @return the underlying complex attribute.
     */
    public ComplexAttribute getUnderlyingComplexAttribute() {
        return this.underlyingComplexAttribute;
    }

    /**
     * Sets the underlying complex attribute. That is, the complex attribute that was wrapped. NB:
     * This could be a Feature.
     */
    public void setUnderlyingComplexAttribute(ComplexAttribute underlyingComplexAttribute) {
        this.underlyingComplexAttribute = underlyingComplexAttribute;
    }

    /**
     * Attempt to wrap the feature in a FeatureWrapper class.
     *
     * @param complexAttribute The feature to wrap.
     * @param clazz The class you want the feature to be wrapped as. (This will be the type that is
     *     returned).
     * @return An object of T which is the wrapped feature.
     */
    public static <T extends FeatureWrapper> T wrap(
            ComplexAttribute complexAttribute, Class<T> clazz) throws InvalidClassException {
        try {
            // Create a new instance of the class:
            T wrapper;
            try {
                wrapper = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new InvalidClassException(
                        String.format("Unable instantiate class of type '%s'.", clazz));
            }

            wrapper.setUnderlyingComplexAttribute(complexAttribute);

            String defaultNamespace = null;
            String defaultSeparator = null;

            // Get class-level XSDMapping:
            XSDMapping classLevelXSDMapping = clazz.getAnnotation(XSDMapping.class);
            if (classLevelXSDMapping != null) {
                defaultNamespace = classLevelXSDMapping.namespace();
                defaultSeparator = classLevelXSDMapping.separator();
            }

            // Look through the fields of the class you're trying to create:
            for (Field field : clazz.getFields()) {
                XSDMapping xsdMapping = field.getAnnotation(XSDMapping.class);

                if (xsdMapping != null) {
                    Class<?> fieldType = field.getType();

                    String path = xsdMapping.path();
                    String namespace =
                            xsdMapping.namespace().equals("")
                                    ? defaultNamespace
                                    : xsdMapping.namespace();
                    String separator =
                            xsdMapping.separator().equals("")
                                    ? defaultSeparator
                                    : xsdMapping.separator();

                    Name xsdName = new NameImpl(namespace, separator, xsdMapping.local());

                    ComplexAttribute targetAttribute = complexAttribute;

                    // See if the field has a path to a deeper value:
                    if (!path.equals("")) {
                        String[] steps = path.split("/");

                        for (int i = 0; i < steps.length; i++) {
                            if (targetAttribute == null) {
                                throw new InvalidClassException(
                                        String.format(
                                                "Unable to wrap attribute in class '%s'. Reference to %s could not be found in the attribute.",
                                                clazz, xsdMapping.local()));
                            }

                            // Dig through the attribute to get to the end node.
                            targetAttribute =
                                    (ComplexAttribute) targetAttribute.getProperty(steps[i]);
                        }
                    }

                    // What kind of field is it?
                    if (FeatureWrapper.class.isAssignableFrom(fieldType)) {
                        // The field's type is actually a FeatureWrapper itself
                        // so we need to recurse.
                        // Because we know it's a FeatureWrapper it's safe to
                        // assume that the value is a complex attribute.
                        // The featureWrapperAttribute is like:
                        // ComplexAttributeImpl:MineName<MineNameType
                        // id=MINENAMETYPE_TYPE_1>=[...]
                        ComplexAttribute featureWrapperAttribute =
                                (ComplexAttribute) targetAttribute.getProperty(xsdName);

                        if (featureWrapperAttribute == null) {
                            // What's wrong is that MineName is not being added
                            // to MineNamePropertyType
                            throw new InvalidClassException(
                                    String.format(
                                            "Unable to wrap attribute in class '%s'. '%s' doesn't have required property '%s'.",
                                            clazz.getName(), targetAttribute.getName(), xsdName));
                        }

                        // We get the name of its type and then use that name to
                        // access the actual property, which then gets wrapped:
                        Name typeName = featureWrapperAttribute.getType().getName();
                        ComplexAttribute nestedComplexAttribute =
                                (ComplexAttribute) featureWrapperAttribute.getProperty(typeName);

                        if (nestedComplexAttribute == null) {
                            // What's wrong is that MineName's properties are
                            // missing the mine type
                            throw new InvalidClassException(
                                    String.format(
                                            "Unable to wrap attribute in class '%s'. '%s' doesn't have required property '%s'.",
                                            clazz.getName(), xsdName, typeName));
                        }

                        // Look for this field in the complexAttribute:
                        FeatureWrapper property =
                                wrap(nestedComplexAttribute, (Class<FeatureWrapper>) fieldType);
                        field.set(wrapper, property);
                    } else if (ArrayList.class.isAssignableFrom(fieldType)) {
                        // Collections aren't too dissimilar, you just have to
                        // build up an array list which gets set as the field's
                        // value.

                        // What is the collection actually of?
                        // All this line is doing is taking a type like:
                        // Collection<MineNamePropertyType> and giving me
                        // MineNamePropertyType.
                        Class<?> collectionType =
                                (Class<?>)
                                        ((ParameterizedType) field.getGenericType())
                                                .getActualTypeArguments()[0];

                        ArrayList<Object> collection = new ArrayList<Object>();
                        if (FeatureWrapper.class.isAssignableFrom(collectionType)) {
                            // The collection is complex.
                            for (Property property : targetAttribute.getProperties(xsdName)) {
                                collection.add(
                                        wrap(
                                                (ComplexAttribute) property,
                                                (Class<FeatureWrapper>) collectionType));
                            }
                        } else {
                            // The collection is simple.
                            for (Property property : targetAttribute.getProperties(xsdName)) {
                                collection.add(property.getValue());
                            }
                        }

                        field.set(wrapper, collection);
                    } else {
                        Property property = targetAttribute.getProperty(xsdName);

                        if (property == null) {
                            throw new InvalidClassException(
                                    String.format(
                                            "Unable to wrap attribute in class '%s'. %s could not be found in the attribute.",
                                            clazz, xsdName));
                        }

                        field.set(wrapper, property.getValue());
                    }
                }
            }

            return wrapper;
        } catch (IllegalAccessException iae) {
            throw new InvalidClassException(
                    String.format(
                            "Unable to wrap attribute in class '%s'. Exception of type: '%s' was thrown with message: '%s'",
                            clazz, iae.getClass(), iae.getMessage()));
        }
    }
}
