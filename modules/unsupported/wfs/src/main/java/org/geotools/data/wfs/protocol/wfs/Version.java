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
package org.geotools.data.wfs.protocol.wfs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enumeration for the supported WFS versions
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *      http://svn.geotools.org/geotools/trunk/gt/modules/plugin/wfs/src/main/java/org/geotools/data/wfs/Version.java $
 */
public enum Version {
    v1_0_0("1.0.0"), v1_1_0("1.1.0");

    private String version;

    private Version(final String version) {
        this.version = version;
    }

    public String toString() {
        return this.version;
    }

    public static Version highest() {
        List<Version> versions = new ArrayList<Version>(Arrays.asList(values()));
        Collections.sort(versions);
        return versions.get(versions.size() - 1);
    }

    /**
     * To be used instead of {@code valueOf(String)}, as valueOf in enum can't
     * be overridden and the argument string must match exactly the enum name,
     * yet we want a lookup by version number.
     * 
     * @param version
     * @return the Version corresponding to {@code version} or {@code null} if
     *         not found.
     */
    public static Version find(final String version) {
        for (Version v : values()) {
            if (v.version.equals(version)) {
                return v;
            }
        }
        return null;
    }
}
