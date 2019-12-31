/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v1_0;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * A simple container class for containing both a distance and a units (some string value, but
 * should be <code>m</code> or <code>mi</code> or similar.
 */
public final class DistanceUnits {
    private final double distance;
    private final String units;

    public static DistanceUnits of(double distance, String units) {
        return new DistanceUnits(distance, units);
    }

    private DistanceUnits(double distance, String units) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance must be >= 0");
        }
        if (units == null) {
            throw new IllegalArgumentException("units must not be null");
        }
        this.distance = distance;
        this.units = units;
    }

    public double getDistance() {
        return distance;
    }

    public String getUnits() {
        return units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DistanceUnits that = (DistanceUnits) o;
        return Double.compare(that.distance, distance) == 0 && Objects.equals(units, that.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, units);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DistanceUnits.class.getSimpleName() + "[", "]")
                .add("distance=" + distance)
                .add("units='" + units + "'")
                .toString();
    }
}
