/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.opengis.filter;

/**
 * This filter should be treated as an extension point that allows the injection of a complex
 * filtering expression defined in the language of the target data store. This can be used, for
 * example, to inject a complex SQL expression that cannot be defined with the available filters.
 *
 * <p>Native filters are not meant to be used by end users, but instead to be used by developers to
 * support advanced use cases. There should always be a business \ code layer between end users and
 * native filters. Not complying with this rule may introduce security risks, e.g. SQL injections.
 */
public interface NativeFilter extends Filter {

    String NAME = "NativeFilter";

    /**
     * Returns the native filter expression defined in the target data source language.
     *
     * @return the native expression defined in the target data source language.
     */
    String getNative();
}
