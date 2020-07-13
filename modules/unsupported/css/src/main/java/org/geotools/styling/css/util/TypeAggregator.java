/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Collects type of properties, by name. When a property is given multiple types, a common ancestor
 * is found
 *
 * @author Andrea Aime - GeoSolutions
 */
class TypeAggregator {
    Map<String, Class> types = new LinkedHashMap<>();

    static final List<Class<?>> INTEGRAL_NUMBER_TYPES =
            Arrays.asList(
                    (Class<?>) Byte.class,
                    Short.class,
                    Integer.class,
                    Long.class,
                    BigInteger.class);

    static final List<Class<?>> FLOAT_NUMBER_TYPES =
            Arrays.asList((Class<?>) Float.class, Double.class, BigDecimal.class);

    /** Adds/merges a property and its type */
    public void addType(String name, Class<?> binding) {
        Class existingBinding = types.get(name);
        if (existingBinding == null) {
            types.put(name, binding);
        } else {
            if (!existingBinding.isAssignableFrom(binding)) {
                if (binding.isAssignableFrom(existingBinding)) {
                    types.put(name, binding);
                } else if (Number.class.isAssignableFrom(binding)
                        && Number.class.isAssignableFrom(existingBinding)) {
                    // go towards the larger number class, fall back on
                    // Number if the binding is not integral nor float (custom/unforeseen Number
                    // subclass)
                    if (INTEGRAL_NUMBER_TYPES.contains(existingBinding)) {
                        if (INTEGRAL_NUMBER_TYPES.contains(binding)) {
                            if (INTEGRAL_NUMBER_TYPES.indexOf(binding)
                                    > INTEGRAL_NUMBER_TYPES.indexOf(existingBinding)) {
                                types.put(name, binding);
                            }
                        } else if (FLOAT_NUMBER_TYPES.contains(binding)) {
                            types.put(name, binding);
                        } else {
                            types.put(name, Number.class);
                        }
                    } else if (FLOAT_NUMBER_TYPES.contains(existingBinding)) {
                        if (FLOAT_NUMBER_TYPES.contains(binding)) {
                            if (FLOAT_NUMBER_TYPES.indexOf(binding)
                                    > FLOAT_NUMBER_TYPES.indexOf(existingBinding)) {
                                types.put(name, binding);
                            } else if (!INTEGRAL_NUMBER_TYPES.contains(binding)) {
                                types.put(name, Number.class);
                            }
                        }
                    }
                } else {
                    // TODO: we could scan the superclasses and implemented
                    // interfaces to find a real common ancestor instead
                    // of directly falling back to Object
                    types.put(name, Object.class);
                }
            }
        }
    }

    /** Builds a feature type containing all of the attributes found so far */
    public SimpleFeatureType getFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        for (Map.Entry<String, Class> entry : types.entrySet()) {
            String name = entry.getKey();
            Class type = entry.getValue();
            if (Geometry.class.isAssignableFrom(type)) {
                builder.add(name, type, DefaultEngineeringCRS.GENERIC_2D);
            } else {
                builder.add(name, type);
            }
        }
        builder.setName("GuessedFeatureType");

        return builder.buildFeatureType();
    }
}
