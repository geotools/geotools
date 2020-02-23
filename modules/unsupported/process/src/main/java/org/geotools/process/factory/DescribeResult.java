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
import org.opengis.parameter.Parameter;

/**
 * DescribeProcess information for publication by DEMProcessFactory.
 *
 * <p>Annotation is used to mark a method for publication via DEMProcessFactory
 *
 * @author Jody
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DescribeResult {
    /** The name of the result, defaults to "result" */
    String name() default "result";

    /**
     * The type of the result, needed only when there are multiple ones (since it cannot be desumed
     * from the process Map return type
     */
    Class<?> type() default Object.class;

    /** The result description */
    String description() default "[undescribed]";

    /** If true, this is the primary result of the process */
    boolean primary() default false;

    /**
     * Extra metadata values for this parameter which will be added into the {@link Parameter}
     * metadata map. Each string should be in the key=value form, if not, the string will be taken
     * as the key and the value will be null.
     */
    String[] meta() default {};
}
