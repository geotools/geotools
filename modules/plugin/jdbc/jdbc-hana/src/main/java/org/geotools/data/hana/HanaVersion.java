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
package org.geotools.data.hana;

/**
 * HANA version.
 *
 * <p>The version string has the format "version.00.revision.patchlevel.buildid".
 *
 * @author Stefan Uhrig, SAP SE
 */
class HanaVersion {

    /**
     * Constructor.
     *
     * @param versionString The version string in the format
     *     "version.00.revision.patchlevel.buildid"..
     */
    public HanaVersion(String versionString) {
        String[] components = versionString.split("\\.");
        if (components.length != 5) {
            throw new IllegalArgumentException("Invalid HANA version string " + versionString);
        }
        try {
            this.version = Integer.parseInt(components[0]);
            this.revision = Integer.parseInt(components[2]);
            this.patchLevel = Integer.parseInt(components[3]);
            this.buildId = Long.parseLong(components[4]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid HANA version string " + versionString);
        }
    }

    private int version;

    private int revision;

    private int patchLevel;

    private long buildId;

    public int getVersion() {
        return version;
    }

    public int getRevision() {
        return revision;
    }

    public int getPatchLevel() {
        return patchLevel;
    }

    public long getBuildId() {
        return buildId;
    }
}
