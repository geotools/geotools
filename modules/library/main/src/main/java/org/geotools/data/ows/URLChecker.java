/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

/**
 * Defines how to implement URL validation
 *
 * @author ImranR
 */
public interface URLChecker {

    /** @return URLChecker name that best describes its purpose (e.g GML Schema evaluator etc) */
    String getName();

    /** @return Boolean flag indicating if this URLChecker should be used */
    boolean isEnabled();

    /**
     * Provide implementation to evaluate URL/URI passed in string form
     *
     * @param url to be evaluated
     * @return boolean response to the
     */
    boolean evaluate(String url);
}
