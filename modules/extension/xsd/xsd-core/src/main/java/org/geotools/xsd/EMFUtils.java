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
package org.geotools.xsd;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.geotools.feature.FeatureCollection;

/**
 * Utility methods for working with emf model objects.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class EMFUtils {
    /**
     * Determines if an eobject has a particular property.
     *
     * @param eobject The eobject.
     * @param property The property to check for.
     *
     * @return <code>true</code> if the property exists, otherwise <code>false</code.
     */
    public static boolean has(EObject eobject, String property) {
        return feature(eobject, property) != null;
    }

    /**
     * Sets a property of an eobject.
     *
     * @param eobject THe object.
     * @param property The property to set.
     * @param value The value of the property.
     */
    public static void set(EObject eobject, String property, Object value) {
        EStructuralFeature feature = feature(eobject, property);
        eobject.eSet(feature, value);
    }

    /**
     * Gets the property of an eobject.
     *
     * @param eobject The object.
     * @param property The property to get.
     * @return The value of the property.
     */
    public static Object get(EObject eobject, String property) {
        EStructuralFeature feature = feature(eobject, property);

        return eobject.eGet(feature);
    }

    /**
     * Returns a value from a map based property of an eobject.
     *
     * <p>This method does not sort of checking of the property, use {@link #getFromMapSafe(EObject, String, String)}
     * for more leniency.
     *
     * @param eobject The object.
     * @param property The map property.
     * @param key The key to obtain from the map.
     * @return The map value, possibly <code>null</code>.
     */
    public static Object getFromMap(EObject eobject, String property, Object key) {
        Map map = (Map) get(eobject, property);
        return map.get(key);
    }

    /**
     * Returns a value from a map based property of an eobject, handling null cases and the case where the property is
     * not actually a map.
     *
     * <p>This method returns null in cases where the the property does not exist, or it is not a map.
     *
     * @param eobject The object.
     * @param property The map property.
     * @param key The key to obtain from the map.
     * @return The map value, possibly <code>null</code>.
     */
    public static Object getFromMapSafe(EObject eobject, String property, String key) {
        if (!has(eobject, property)) {
            return null;
        }

        Object o = get(eobject, property);
        if (o == null || !(o instanceof Map)) {
            return null;
        }

        return ((Map) o).get(key);
    }

    /**
     * Adds a value to a multi-valued propert of an eobject.
     *
     * <p>The <param>property</param> must map to a multi-valued property of the eobject. The
     * {@link #isCollection(EObject, String)} method can be used to test this.
     *
     * @param eobject The object.
     * @param property The multi-valued property.
     * @param value The value to add.
     */
    public static void add(EObject eobject, String property, Object value) {
        EStructuralFeature feature = feature(eobject, property);

        if (feature != null) {
            add(eobject, feature, value);
        }
    }

    /**
     * Adds a value to a multi-valued propert of an eobject.
     *
     * <p>The <param>feature</param> must map to a multi-valued property of the eobject. The
     * {@link #isCollection(EStructuralFeature)} method can be used to test this.
     *
     * @param eobject The object.
     * @param feature The multi-valued feature.
     * @param value The value to add.
     */
    @SuppressWarnings("unchecked") // playing with generic collections, contents unknown
    public static void add(EObject eobject, EStructuralFeature feature, Object value) {
        if (isCollection(eobject, feature)) {
            Collection collection = (Collection) eobject.eGet(feature);
            if (collection == null) {
                // most likely not an ECollection
                collection = createEmptyCollection(feature);
                eobject.eSet(feature, collection);
            }
            Collection addCollection = collection(value);
            collection.addAll(addCollection);
        }
    }

    static Collection createEmptyCollection(EStructuralFeature feature) {
        Class clazz = feature.getEType().getInstanceClass();
        if (EList.class.isAssignableFrom(clazz)) {
            return new BasicEList<>();
        }
        if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList<>();
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return new HashSet<>();
        }
        throw new IllegalArgumentException("Unable to create collection for " + clazz);
    }

    /**
     * Returns a collection view for value, taking care of the case where value is of an array type, in which case the
     * collection returned contains the array elements, not the array itself.
     *
     * @param value a value to be added to an EObject collection property
     * @return value wrapped in a collection, or a collection containing the array elements in case value is an array.
     * @see #add(EObject, String, Object)
     */
    @SuppressWarnings("unchecked") // playing with generic collections/arrays
    private static Collection collection(Object value) {
        if (null == value) {
            return Collections.emptyList();
        } else if (value.getClass().isArray()) {
            final int len = java.lang.reflect.Array.getLength(value);
            List list = new ArrayList<>(len);

            for (int i = 0; i < len; i++) {
                Object val = Array.get(value, i);
                list.add(val);
            }
            return list;
        } else if (value instanceof FeatureCollection) {
            return Collections.singletonList(value); // force singleton for FeatureCollectionType
        } else if (value instanceof Collection) {
            return (Collection) value;
        }

        return Collections.singletonList(value);
    }

    /**
     * Determines if a property of an eobject is a collection.
     *
     * <p>In the event the property does not exist, this method will return <code>false</code>
     *
     * @return <code>true</code> if hte property is a collection, otherwise <code>false</code>
     */
    public static boolean isCollection(EObject eobject, String property) {
        Object o = get(eobject, property);

        // first check the actual value
        if (o != null) {
            return o instanceof Collection;
        }

        // value was null, try checking the class
        EStructuralFeature feature = feature(eobject, property);

        if (feature == null) {
            return false;
        }

        return isCollection(eobject, feature);
    }

    /**
     * Determines if a feature of an eobject is a collection.
     *
     * @return <code>true</code> if the feature is a collection, otherwise <code>false</code>
     */
    public static boolean isCollection(EObject eobject, EStructuralFeature feature) {

        Object o = eobject.eGet(feature);
        if (o != null) {
            return o instanceof Collection;
        }

        if (Collection.class.isAssignableFrom(feature.getEType().getInstanceClass())) {
            return true;
        }

        return false;
    }

    /**
     * Method which looks up a structure feature of an eobject, first doing an exact name match, then a case insensitive
     * one.
     *
     * @param eobject The eobject.
     * @param property The property
     * @return The structure feature, or <code>null</code> if not found.
     */
    public static EStructuralFeature feature(EObject eobject, String property) {
        EStructuralFeature feature = eobject.eClass().getEStructuralFeature(property);

        if (feature != null) {
            return feature;
        }

        // do a case insentive check, need to do the walk up the type hierarchy
        for (EStructuralFeature eStructuralFeature : eobject.eClass().getEAllStructuralFeatures()) {
            feature = eStructuralFeature;

            if (feature.getName().equalsIgnoreCase(property)) {
                return feature;
            }
        }

        return null;
    }

    /**
     * Method which looks up structural features of an eobject by type.
     *
     * @param eobject The eobject.
     * @param propertyType The type of the properties.
     * @return The list of structure features, or an empty list if none are found.
     */
    public static List<EStructuralFeature> features(EObject eobject, Class<?> propertyType) {
        List<EStructuralFeature> match = new ArrayList<>();
        List<EStructuralFeature> features = eobject.eClass().getEAllStructuralFeatures();

        for (EStructuralFeature feature : features) {
            if (feature.getEType().getInstanceClass().isAssignableFrom(propertyType)) {
                match.add(feature);
            }
        }

        return match;
    }

    /**
     * Sets a particular property on each {@link EObject} in a list to a particular value.
     *
     * <p>The following must hold: <code>
     * objects.size() == values.size()
     * </code>
     *
     * @param objects A list of {@link EObject}.
     * @param property The property to set on each eobject in <code>objects</code>
     * @param values The value to set on each eobjct in <code>objects</code>
     */
    public static void set(List objects, String property, List values) {
        for (int i = 0; i < objects.size(); i++) {
            EObject eobject = (EObject) objects.get(i);
            set(eobject, property, values.get(i));
        }
    }

    /**
     * Sets a particular property on each {@link EObject} in a list to a particular value.
     *
     * <p>
     *
     * @param objects A list of {@link EObject}.
     * @param property The property to set on each eobject in <code>objects</code>
     * @param value The value to set on each eobjct in <code>objects</code>
     */
    public static void set(List objects, String property, Object value) {
        for (Object object : objects) {
            EObject eobject = (EObject) object;
            set(eobject, property, value);
        }
    }

    /**
     * Obtains the values of a particular property on each {@link EObject} in a list.
     *
     * @param objects A list of {@link EObject}.
     * @param property The property to obtain.
     * @return The list of values.
     */
    public static List<Object> get(List objects, String property) {
        List<Object> values = new ArrayList<>();

        for (Object object : objects) {
            EObject eobject = (EObject) object;
            EStructuralFeature feature = feature(eobject, property);

            values.add(eobject.eGet(feature));
        }

        return values;
    }

    /**
     * Determines if a particular propety has been set on an eobject.
     *
     * @param eobject The eobject.
     * @param property The property to check.
     * @return <code>true</code> if the property has been set, otherwise <code>false</code>
     */
    public static boolean isSet(EObject eobject, String property) {
        EStructuralFeature feature = feature(eobject, property);

        return eobject.eIsSet(feature);
    }

    /**
     * Determines if a particular propety has been set on each {@link EObject} in a list.
     *
     * @param objects A list of {@link EObject}
     * @param property The property to check.
     * @return <code>true</code> if every element in the list has been set, otherwise <code>false
     *     </code>
     */
    public static boolean isSet(List objects, String property) {
        for (Object object : objects) {
            EObject eobject = (EObject) object;

            if (!isSet(eobject, property)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines if a particular propety is unset on each {@link EObject} in a list.
     *
     * @param objects A list of {@link EObject}
     * @param property The property to check.
     * @return <code>true</code> if every element in the list is unset, otherwise <code>false</code>
     */
    public static boolean isUnset(List objects, String property) {
        for (Object object : objects) {
            EObject eobject = (EObject) object;

            if (isSet(eobject, property)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Clones an eobject, with the option of performing a deep clone in which referenced eobjects are also cloned.
     *
     * @param prototype The object to be cloned from.
     * @param factory The factory used to create the clone.
     * @param deep indicating wether to perform a deep clone.
     * @return THe cloned object, with all properties the same to the original.
     */
    public static EObject clone(EObject prototype, EFactory factory, boolean deep) {
        EObject clone = factory.create(prototype.eClass());

        for (EStructuralFeature feature : clone.eClass().getEStructuralFeatures()) {
            Object value = prototype.eGet(feature);
            if (deep && value instanceof EObject) {
                EObject evalue = (EObject) value;
                // recursively call
                // TODO:handle cycles in reference graph
                value = clone(evalue, evalue.eClass().getEPackage().getEFactoryInstance(), deep);
            }

            clone.eSet(feature, value);
        }

        return clone;
    }

    /**
     * Copies all the properties from one object to anoter.
     *
     * @param source The object to copy from.
     * @param target The object to copy to.
     */
    public static void copy(EObject source, EObject target) {
        for (EStructuralFeature feature : source.eClass().getEStructuralFeatures()) {
            target.eSet(feature, source.eGet(feature));
        }
    }
}
