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
package org.geotools.data.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.factory.Hints;

/**
 * Converts among arrays and different collection classes.
 *
 * <p>THe following conversions are supported:
 *
 * <ul>
 *   <li>Collection to Collection where collections are different types ( ex list to set )
 *   <li>Collection to Array
 *   <li>Array to Collection
 *   <li>Array to Array where the declared type of the target array is assignable from the declared
 *       type of the source array
 * </ul>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class CollectionConverterFactory implements ConverterFactory {

    /** Converter for collection to collection */
    protected static final Converter CollectionToCollection =
            new Converter() {

                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    // if source is already an instance nevermind
                    if (target.isInstance(source)) {
                        return target.cast(source);
                    }

                    // dynamically create and add
                    Collection<Object> converted = newCollection(target);
                    if (converted != null) {
                        @SuppressWarnings("unchecked")
                        Collection<Object> castSource = (Collection<Object>) source;
                        converted.addAll(castSource);
                    }

                    return target.cast(converted);
                }
            };

    /** Converter for collection to array. */
    protected static final Converter CollectionToArray =
            new Converter() {

                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    Collection s = (Collection) source;
                    Object array = Array.newInstance(target.getComponentType(), s.size());

                    try {
                        int x = 0;
                        for (Object o : s) {
                            Array.set(array, x++, o);
                        }

                        return target.cast(array);
                    } catch (Exception e) {
                        // Means an incompatable type assignment

                    }

                    return null;
                }
            };

    /** Converter for array to collection. */
    protected static final Converter ArrayToCollection =
            new Converter() {

                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    Collection<Object> collection = newCollection(target);
                    if (collection != null) {
                        int length = Array.getLength(source);
                        for (int i = 0; i < length; i++) {
                            collection.add(Array.get(source, i));
                        }
                    }

                    return target.cast(collection);
                }
            };

    /** Converter for array to array. */
    protected static final Converter ArrayToArray =
            new Converter() {

                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    // get the individual component types
                    Class<?> s = source.getClass().getComponentType();
                    Class<?> t = target.getComponentType();

                    // make sure the source can be assiigned to the target
                    if (t.isAssignableFrom(s)) {
                        int length = Array.getLength(source);
                        Object converted = Array.newInstance(t, length);

                        for (int i = 0; i < length; i++) {
                            Array.set(converted, i, Array.get(source, i));
                        }

                        @SuppressWarnings("unchecked")
                        T cast = (T) converted;
                        return cast;
                    }

                    return null;
                }
            };

    protected static Collection<Object> newCollection(Class target) throws Exception {
        if (target.isInterface()) {
            // try the common ones
            if (List.class.isAssignableFrom(target)) {
                return new ArrayList<>();
            }
            if (SortedSet.class.isAssignableFrom(target)) {
                return new TreeSet<>();
            } else if (Set.class.isAssignableFrom(target)) {
                return new HashSet<>();
            }

            // could not figure out
            return null;
        } else {
            // instantiate directly
            @SuppressWarnings("unchecked")
            Collection<Object> result = (Collection) target.getDeclaredConstructor().newInstance();
            return result;
        }
    }

    @Override
    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        if ((Collection.class.isAssignableFrom(source) || source.isArray())
                && (Collection.class.isAssignableFrom(target) || target.isArray())) {

            // both collections?
            if (Collection.class.isAssignableFrom(source)
                    && Collection.class.isAssignableFrom(target)) {
                return CollectionToCollection;
            }

            // both arrays?
            if (source.isArray() && target.isArray()) {
                return ArrayToArray;
            }

            // collection to array?
            if (Collection.class.isAssignableFrom(source) && target.isArray()) {
                return CollectionToArray;
            }

            // array to collection?
            if (source.isArray() && Collection.class.isAssignableFrom(target)) {
                return ArrayToCollection;
            }
        }

        return null;
    }
}
