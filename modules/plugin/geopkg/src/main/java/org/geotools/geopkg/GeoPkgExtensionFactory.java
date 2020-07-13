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
package org.geotools.geopkg;

public interface GeoPkgExtensionFactory {

    /**
     * Builds an extension object by name
     *
     * @param name The extension name
     * @param geoPackage The GeoPackage the extension will work on
     * @return The extension object, or null if this factory does not implement the given extension
     */
    public GeoPkgExtension getExtension(String name, GeoPackage geoPackage);

    /**
     * Builds an extension object by class
     *
     * @param extensionClass The extension class
     * @param geoPackage The GeoPackage the extension will work on
     * @return The extension object, or null if this factory does not implement the given extension
     */
    public GeoPkgExtension getExtension(Class extensionClass, GeoPackage geoPackage);
}
