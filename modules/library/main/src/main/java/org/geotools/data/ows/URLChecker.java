/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
 * Checks URIs/URLs/locations to confirm if they are allowed for use. Used to restrict access to
 * remote servers, e.g. to prevent access to untrusted servers or local resources, as many OGC
 * services either allow users to provide links to remote resources, or provide links that are not
 * restricted to their original host. <br>
 * Implementations should be registered using SPI in <code>META-INF/services/org.geotools.data
 * .ows.URLChecker</code>.
 */
// doc-begin
public interface URLChecker {

    /** @return URLChecker name that best describes its purpose (e.g. GML Schema evaluator etc) */
    String getName();

    /** @return Boolean flag indicating if this URLChecker should be used */
    Boolean isEnabled();

    /**
     * Used to confirm location is allowed for use.
     *
     * <p>URLChecker is used to confirm if a location is allowed for use, returning {@true} when
     * they recognize a location as permitted. Several URLChecker instances are expected to be
     * available, the location will be allowed if at least one URLChecker can confirm it is
     * permitted for use.
     *
     * <p>Location is normalized to remove all redundant {@code .} and {@code ..} path names, and
     * provide absolute file references using empty host name {@code file:///path} approach.
     *
     * @param location Location expressed as normalized URL, URI or path.
     * @return {@code true} indicates the URLChecker can confirm the location is allowed for use,
     *     {@code false} indicates the URLChecker is unable to confirm.
     */
    boolean confirm(String location);
}
// doc-end
