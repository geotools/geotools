/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
 * HANA Cloud version.
 *
 * <p>The version string has the format "<year>.<week>.<patch>-<addendum>".
 *
 * @author Johannes Quast, SAP SE
 */
class HanaCloudVersion implements Comparable<HanaCloudVersion> {

    public static final HanaCloudVersion INVALID_VERSION = new HanaCloudVersion(0, 0, 0);

    private int year;

    private int week;

    private int patch;

    /**
     * Constructor.
     *
     * @param versionString The version string in the format "<year>.<week>.<patch>-<addendum>"..
     */
    public HanaCloudVersion(String versionString) {
        String[] components = versionString.replace('-', '.').split("\\.");
        if (components.length < 3) {
            throw new IllegalArgumentException(
                    "Invalid HANA Cloud version string " + versionString);
        }
        try {
            this.year = Integer.parseInt(components[0]);
            this.week = Integer.parseInt(components[1]);
            this.patch = Integer.parseInt(components[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid HANA Cloud version string " + versionString);
        }
    }

    public HanaCloudVersion(int year, int week, int patch) {
        this.year = year;
        this.week = week;
        this.patch = patch;
    }

    public int getYear() {
        return year;
    }

    public int getWeek() {
        return week;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public int compareTo(HanaCloudVersion that) {
        if (that == null) throw new NullPointerException();

        if (this.year != that.year) return Integer.compare(this.year, that.year);
        if (this.week != that.week) return Integer.compare(this.week, that.week);
        if (this.patch != that.patch) return Integer.compare(this.patch, that.patch);

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HanaCloudVersion that = (HanaCloudVersion) o;
        return year == that.year && week == that.week && patch == that.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, week, patch);
    }
}
