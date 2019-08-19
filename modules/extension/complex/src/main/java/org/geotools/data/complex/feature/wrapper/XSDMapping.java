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
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.complex.feature.wrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation on descendants of FeatureWrapper.
 *
 * <p>It can indicate how fields map to objects in a Feature but describing their name including
 * name and namespace. At class-level you can set namespace and separator to serve as defaults for
 * the fields within the class. At field-level you can specify the local name and the namespace and
 * separator (the last two will be inherited from a class-level XSDMapping if one is present).
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface XSDMapping {

    /** The XML Local name that the field corresponds to. */
    String local() default "";

    /**
     * The XML namespace the field corresponds to. Define at class-level to set a default namespace
     * for all contained fields.
     */
    String namespace() default "";

    /**
     * The separator used in the full XML name. Define at class-level to set a default separator for
     * all contained fields.
     */
    String separator() default "";

    /**
     * You can use path to set an XPATH-style expression to indicate where a particular field should
     * be found in a complex feature.
     */
    String path() default "";
}
