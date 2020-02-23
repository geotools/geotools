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
package org.geotools.process.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.geotools.util.Converters;
import org.opengis.parameter.Parameter;

/**
 * Annotates static method parameters for publication by {@link StaticMethodsProcessFactory}.
 *
 * @author Jody
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DescribeParameter {
    // annotations cannot default to
    static final String DEFAULT_NULL = "THIS IS THE NULL VALUE FOR THE DEFAULT ATTRIBUTE";

    /** The parameter name */
    String name();

    /** The parameter description */
    String description() default "[undescribed]";

    /** The type of object contained in the parameter in case it's a collection */
    Class collectionType() default Object.class;

    /**
     * Minimum number of occurrences for the parameter. A value of 0 means the parameter is
     * optional, a value greater than one makes sense only if the parameter is a collection, in
     * which case the number of items of the collections will be compared against the minimum value.
     */
    int min() default -1;

    /**
     * Maximum number of occurrences for the parameter. The value must be greater or equal to {@link
     * #min()}, a value greater than one makes sense only if the parameter is a collection, in which
     * case the number of items of the collections will be compared against the maximum value.
     */
    int max() default -1;

    /**
     * The default value for the parameter in case it's not specified. The string value will be
     * first interpreted as a reference to a constant defined with the following two syntaxes:
     *
     * <ul>
     *   <li>classic javadoc reference syntax, <code>com.company.MyClass#THE_CONSTANT</code>
     *   <li>simple name, in which case the constant will be searched in the process class itself
     *       first, and in the parameter type class later
     * </ul>
     *
     * If the above does not work the string value will be converted to the target type using the
     * {@link Converters} class instead. The above lookup path makes sure it's possible to define a
     * reference to a constant for String values too (if {@link Converters} were to be used right
     * away the value of the default itself would be used as the default value).
     *
     * <p>If none of the above heuristics works an exception will be thrown.
     */
    String defaultValue() default DEFAULT_NULL;

    /** The minimum possible value acceptable for this parameter, in case it is a numeric value */
    double minValue() default Double.NEGATIVE_INFINITY;

    /** The maximum possible value acceptable for this parameter, in case it is a numeric value */
    double maxValue() default Double.POSITIVE_INFINITY;

    /**
     * Extra metadata values for this parameter which will be added into the {@link Parameter}
     * metadata map. Each string should be in the key=value form, if not, the string will be taken
     * as the key and the value will be null.
     */
    String[] meta() default {};
}
