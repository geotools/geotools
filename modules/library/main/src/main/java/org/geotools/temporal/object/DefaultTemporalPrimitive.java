/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.temporal.object;

import java.util.Date;
import org.geotools.api.temporal.Instant;
import org.geotools.api.temporal.Period;
import org.geotools.api.temporal.RelativePosition;
import org.geotools.api.temporal.TemporalOrder;
import org.geotools.api.temporal.TemporalPrimitive;

/**
 * An abstract class that represents a non-decomposed element of geometry or topology of time.
 *
 * @author Mehdi Sidhoum (Geomatys)
 * @author Simone Giannecchini, GeoSolutions SAS
 */
@SuppressWarnings("ComparableType")
public abstract class DefaultTemporalPrimitive extends DefaultTemporalObject
        implements TemporalPrimitive, TemporalOrder, Comparable<TemporalPrimitive> {

    @Override
    public int compareTo(TemporalPrimitive that) {
        if (that == null) throw new IllegalArgumentException("Provided temporal object is null");
        final RelativePosition pos = this.relativePosition(that);
        if (pos == null) throw new ClassCastException("The provided object cannot be compared to this one");
        if (pos == RelativePosition.BEFORE) return -1;
        if (pos == RelativePosition.AFTER) return +1;

        if (pos == RelativePosition.EQUALS) return 0;

        // TODO rethink this since it looks like it is a pretty dirty hack
        if (this instanceof Period && that instanceof Instant || this instanceof Instant && that instanceof Period) {
            if (pos == RelativePosition.ENDED_BY
                    || pos == RelativePosition.BEGUN_BY
                    || pos == RelativePosition.CONTAINS) return 0;
        }

        // TODO rethink this since it looks like it is a pretty dirty hack
        if (this instanceof Period && that instanceof Period) {
            if (pos == RelativePosition.MEETS) return -1;
            if (pos == RelativePosition.BEGINS) return -1;
            if (pos == RelativePosition.BEGUN_BY) return +1;
            if (pos == RelativePosition.ENDS) return +1;
            if (pos == RelativePosition.ENDED_BY) return -1;
            if (pos == RelativePosition.OVERLAPS) return -1;
            if (pos == RelativePosition.OVERLAPPED_BY) return +1;
            if (pos == RelativePosition.DURING || pos == RelativePosition.CONTAINS || pos == RelativePosition.EQUALS)
                return 0;
        }

        throw new IllegalStateException("Unable to compare the provided object with this one");
    }

    /**
     * Returns a value for relative position which are provided by the enumerated data type TM_RelativePosition and are
     * based on the 13 temporal relationships identified by Allen (1983).
     *
     * @param other TemporalPrimitive
     */
    @Override
    public RelativePosition relativePosition(TemporalPrimitive other) {
        if (this instanceof Instant && other instanceof Instant) {
            Instant thisInstant = (Instant) this;
            Instant otherIstant = (Instant) other;

            return relativePosition(thisInstant, otherIstant);
        } else {
            if (this instanceof Period && other instanceof Instant) {
                Period thisPeriod = (Period) this;
                Instant otherInstant = (Instant) other;

                return relativePosition(thisPeriod, otherInstant);
            } else {
                if (this instanceof Instant && other instanceof Period) {
                    Instant thisIstant = (Instant) this;
                    Period otherPeriod = (Period) other;

                    return relativePosition(thisIstant, otherPeriod);
                } else {
                    if (this instanceof Period && other instanceof Period) {
                        Period thisPeriod = (Period) this;
                        Period otherPeriod = (Period) other;

                        return relativePosition(thisPeriod, otherPeriod);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private RelativePosition relativePosition(Period thisPeriod, Period otherPeriod) {
        Date thisBeginning = thisPeriod.getBeginning().getPosition().getDate();
        Date thisEnding = thisPeriod.getEnding().getPosition().getDate();
        Date otherBeginning = otherPeriod.getBeginning().getPosition().getDate();
        Date otherEnding = otherPeriod.getEnding().getPosition().getDate();
        if (thisEnding.before(otherBeginning)) {
            return RelativePosition.BEFORE;
        } else if (thisEnding.compareTo(otherBeginning) == 0) {
            return RelativePosition.MEETS;
        } else if (thisBeginning.before(otherBeginning)
                && thisEnding.after(otherBeginning)
                && thisEnding.before(otherEnding)) {
            return RelativePosition.OVERLAPS;
        } else if (thisBeginning.compareTo(otherBeginning) == 0 && thisEnding.before(otherEnding)) {
            return RelativePosition.BEGINS;
        } else if (thisBeginning.compareTo(otherBeginning) == 0 && thisEnding.after(otherEnding)) {
            return RelativePosition.BEGUN_BY;
        } else if (thisBeginning.after(otherBeginning) && thisEnding.before(otherEnding)) {
            return RelativePosition.DURING;
        } else if (thisBeginning.before(otherBeginning) && thisEnding.after(otherEnding)) {
            return RelativePosition.CONTAINS;
        } else if (thisBeginning.compareTo(otherBeginning) == 0 && thisEnding.compareTo(otherEnding) == 0) {
            return RelativePosition.EQUALS;
        } else if (thisBeginning.after(otherBeginning)
                && thisBeginning.before(otherEnding)
                && thisEnding.after(otherEnding)) {
            return RelativePosition.OVERLAPPED_BY;
        } else if (thisBeginning.after(otherBeginning) && thisEnding.compareTo(otherEnding) == 0) {
            return RelativePosition.ENDS;
        } else if (thisBeginning.before(otherBeginning) && thisEnding.compareTo(otherEnding) == 0) {
            return RelativePosition.ENDED_BY;
        } else {
            return thisBeginning.compareTo(otherEnding) == 0 ? RelativePosition.MET_BY : RelativePosition.AFTER;
        }
    }

    private RelativePosition relativePosition(Instant thisInstant, Period otherPeriod) {
        Date otherEnd = otherPeriod.getEnding().getPosition().getDate();
        Date thisDate = thisInstant.getPosition().getDate();
        if (otherEnd.before(thisDate)) {
            return RelativePosition.AFTER;
        } else {
            if (otherEnd.compareTo(thisDate) == 0) {
                return RelativePosition.ENDS;
            } else {
                Date otherBeginning = otherPeriod.getBeginning().getPosition().getDate();
                if (otherBeginning.before(thisDate) && otherEnd.after(thisDate)) {
                    return RelativePosition.DURING;
                } else {
                    return otherBeginning.compareTo(thisDate) == 0 ? RelativePosition.BEGINS : RelativePosition.BEFORE;
                }
            }
        }
    }

    private RelativePosition relativePosition(Period thisPeriod, Instant otherInstant) {
        Date thisEnd = thisPeriod.getEnding().getPosition().getDate();
        Date otherDate = otherInstant.getPosition().getDate();
        if (thisEnd.before(otherDate)) {
            return RelativePosition.BEFORE;
        } else if (thisEnd.compareTo(otherDate) == 0) {
            return RelativePosition.ENDED_BY;
        } else {
            Date thisStart = thisPeriod.getBeginning().getPosition().getDate();
            if (thisStart.before(otherDate) && thisEnd.after(otherDate)) {
                return RelativePosition.CONTAINS;
            } else {
                return thisStart.compareTo(otherDate) == 0 ? RelativePosition.BEGUN_BY : RelativePosition.AFTER;
            }
        }
    }

    private RelativePosition relativePosition(Instant thisInstant, Instant otherIstant) {
        Date thisDate = thisInstant.getPosition().getDate();
        Date otherDate = otherIstant.getPosition().getDate();
        if (thisDate.before(otherDate)) {
            return RelativePosition.BEFORE;
        } else {
            return thisDate.compareTo(otherDate) == 0 ? RelativePosition.EQUALS : RelativePosition.AFTER;
        }
    }
}
