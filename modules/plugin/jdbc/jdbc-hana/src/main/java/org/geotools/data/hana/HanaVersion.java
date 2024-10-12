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

import java.util.Objects;

/**
 * HANA version.
 *
 * <p>The version string has the format "version.00.revision.patchlevel.buildid".
 *
 * @author Stefan Uhrig, SAP SE
 */
class HanaVersion implements Comparable<HanaVersion> {

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

    public HanaVersion(int version, int revision, int patchLevel, long buildId) {
        this.version = version;
        this.revision = revision;
        this.patchLevel = patchLevel;
        this.buildId = buildId;
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

    @Override
    public int compareTo(HanaVersion that) {
        if (that == null) throw new NullPointerException();

        if (this.version != that.version) return Integer.compare(this.version, that.version);
        if (this.revision != that.revision) return Integer.compare(this.revision, that.revision);
        if (this.patchLevel != that.patchLevel) return Integer.compare(this.patchLevel, that.patchLevel);

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HanaVersion that = (HanaVersion) o;
        return version == that.version
                && revision == that.revision
                && patchLevel == that.patchLevel
                && buildId == that.buildId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, revision, patchLevel, buildId);
    }
}
