package org.geotools.filter.v1_0;

import java.util.Objects;
import java.util.StringJoiner;

public final class DistanceUnits {
    private final double distance;
    private final String units;

    public static DistanceUnits of(double distance, String units) {
        return new DistanceUnits(distance, units);
    }

    private DistanceUnits(double distance, String units) {
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
