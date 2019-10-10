/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * GeoTools custom ersion of Spotbugs SuppressFBWarnings, added here to avoid dependencies on extra
 * jars and for IDE friendlyness (IntelliJ does not seem to provide completion for optional
 * dependencies, even if annotations not found at runtime apparently don't cause issues).
 *
 * <p>Spotbugs checks annotations by name, anything ending with "SuppressFBWarnings" or
 * "SuppressWarnings" will be checked for suppressions. We cannot use SuppressWarnings simply
 * because it is not retained in bytecode, which is what SpotBugs processes.
 */
@Retention(RetentionPolicy.CLASS)
public @interface SuppressFBWarnings {
    /**
     * The set of FindBugs warnings that are to be suppressed in annotated element. The value can be
     * a bug category, kind or pattern.
     */
    String[] value() default {};

    /** Optional documentation of the reason why the warning is suppressed */
    String justification() default "";
}
