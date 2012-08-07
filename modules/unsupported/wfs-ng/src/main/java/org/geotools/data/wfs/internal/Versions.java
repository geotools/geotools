/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import org.geotools.util.Version;

/**
 * Supported WFS versions
 */
public final class Versions {

    public static final Version v1_0_0 = new Version("1.0.0");

    public static final Version v1_1_0 = new Version("1.1.0");

    public static final Version v2_0_0 = new Version("2.0.0");

    public static Version find(String capsVersion) {
        if (capsVersion == null) {
            throw new IllegalArgumentException();
        }
        Version v = new Version(capsVersion);
        if (v1_0_0.equals(v)) {
            return v1_0_0;
        }
        if (v1_1_0.equals(v)) {
            return v1_1_0;
        }
        if (v2_0_0.equals(v)) {
            return v2_0_0;
        }
        throw new IllegalArgumentException();
    }

}
