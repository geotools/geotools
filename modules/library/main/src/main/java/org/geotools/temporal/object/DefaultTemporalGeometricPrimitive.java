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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opengis.temporal.Duration;
import org.opengis.temporal.Instant;
import org.opengis.temporal.OrdinalReferenceSystem;
import org.opengis.temporal.Period;
import org.opengis.temporal.RelativePosition;
import org.opengis.temporal.Separation;
import org.opengis.temporal.TemporalGeometricPrimitive;

/**
 * An abstract class with two subclasses for representing a temporal instant and a temporal period.
 *
 * @author Mehdi Sidhoum (Geomatys)
 */
public abstract class DefaultTemporalGeometricPrimitive extends DefaultTemporalPrimitive
        implements TemporalGeometricPrimitive, Separation {

    /**
     * Returns the distance from this TM_GeometricPrimitive to another TM_GeometricPrimitive, i.e.
     * the absolute value of the difference between their temporal positions.
     */
    public Duration distance(TemporalGeometricPrimitive other) {
        Duration response = null;
        long diff = 0L;

        if (this instanceof Instant && other instanceof Instant) {
            if (((Instant) this).getPosition().anyOther() != null
                    && ((Instant) other).getPosition().anyOther() != null) {
                if (!((DefaultTemporalPosition) ((Instant) this).getPosition().anyOther())
                        .getFrame()
                        .equals(
                                ((DefaultTemporalPosition)
                                                ((Instant) other).getPosition().anyOther())
                                        .getFrame())) {
                    try {
                        throw new Exception(
                                "the TM_TemporalPositions are not both associated with the same TM_ReferenceSystem !");
                    } catch (Exception ex) {
                        Logger.getLogger(DefaultTemporalGeometricPrimitive.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            } else if (((Instant) this).getPosition().anyOther() != null) {
                if (((Instant) this).getPosition().anyOther().getIndeterminatePosition() != null
                        || ((DefaultTemporalPosition) ((Instant) this).getPosition().anyOther())
                                        .getFrame()
                                instanceof OrdinalReferenceSystem) {
                    try {
                        throw new Exception(
                                "either of the two TM_TemporalPositions is indeterminate or is associated with a TM_OrdianlReferenceSystem !");
                    } catch (Exception ex) {
                        Logger.getLogger(DefaultTemporalGeometricPrimitive.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            } else if (((Instant) other).getPosition().anyOther() != null) {
                if (((Instant) other).getPosition().anyOther().getIndeterminatePosition() != null
                        || ((DefaultTemporalPosition) ((Instant) other).getPosition().anyOther())
                                        .getFrame()
                                instanceof OrdinalReferenceSystem) {
                    try {
                        throw new Exception(
                                "either of the two TM_TemporalPositions is indeterminate or is associated with a TM_OrdianlReferenceSystem !");
                    } catch (Exception ex) {
                        Logger.getLogger(DefaultTemporalGeometricPrimitive.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        if (this.relativePosition(other).equals(RelativePosition.BEFORE)
                || this.relativePosition(other).equals(RelativePosition.AFTER)) {
            if (this instanceof Instant && other instanceof Instant) {
                diff =
                        Math.min(
                                Math.abs(
                                        ((Instant) other).getPosition().getDate().getTime()
                                                - ((Instant) this)
                                                        .getPosition()
                                                        .getDate()
                                                        .getTime()),
                                Math.abs(
                                        ((Instant) this).getPosition().getDate().getTime()
                                                - ((Instant) other)
                                                        .getPosition()
                                                        .getDate()
                                                        .getTime()));
            } else {
                if (this instanceof Instant && other instanceof Period) {
                    diff =
                            Math.min(
                                    Math.abs(
                                            ((Period) other)
                                                            .getBeginning()
                                                            .getPosition()
                                                            .getDate()
                                                            .getTime()
                                                    - ((Instant) this)
                                                            .getPosition()
                                                            .getDate()
                                                            .getTime()),
                                    Math.abs(
                                            ((Period) other)
                                                            .getEnding()
                                                            .getPosition()
                                                            .getDate()
                                                            .getTime()
                                                    - ((Instant) this)
                                                            .getPosition()
                                                            .getDate()
                                                            .getTime()));
                } else {
                    if (this instanceof Period && other instanceof Instant) {
                        diff =
                                Math.min(
                                        Math.abs(
                                                ((Instant) other).getPosition().getDate().getTime()
                                                        - ((Period) this)
                                                                .getEnding()
                                                                .getPosition()
                                                                .getDate()
                                                                .getTime()),
                                        Math.abs(
                                                ((Instant) other).getPosition().getDate().getTime()
                                                        - ((Period) this)
                                                                .getBeginning()
                                                                .getPosition()
                                                                .getDate()
                                                                .getTime()));
                    } else {
                        if (this instanceof Period && other instanceof Period) {
                            diff =
                                    Math.min(
                                            Math.abs(
                                                    ((Period) other)
                                                                    .getEnding()
                                                                    .getPosition()
                                                                    .getDate()
                                                                    .getTime()
                                                            - ((Period) this)
                                                                    .getBeginning()
                                                                    .getPosition()
                                                                    .getDate()
                                                                    .getTime()),
                                            Math.abs(
                                                    ((Period) other)
                                                                    .getBeginning()
                                                                    .getPosition()
                                                                    .getDate()
                                                                    .getTime()
                                                            - ((Period) this)
                                                                    .getEnding()
                                                                    .getPosition()
                                                                    .getDate()
                                                                    .getTime()));
                        }
                    }
                }
            }
        } else {
            if (this.relativePosition(other).equals(RelativePosition.BEGINS)
                    || this.relativePosition(other).equals(RelativePosition.BEGUN_BY)
                    || this.relativePosition(other).equals(RelativePosition.CONTAINS)
                    || this.relativePosition(other).equals(RelativePosition.DURING)
                    || this.relativePosition(other).equals(RelativePosition.ENDED_BY)
                    || this.relativePosition(other).equals(RelativePosition.ENDS)
                    || this.relativePosition(other).equals(RelativePosition.EQUALS)
                    || this.relativePosition(other).equals(RelativePosition.MEETS)
                    || this.relativePosition(other).equals(RelativePosition.MET_BY)
                    || this.relativePosition(other).equals(RelativePosition.OVERLAPPED_BY)
                    || this.relativePosition(other).equals(RelativePosition.OVERLAPS)) {
                diff = 0L;
            }
        }

        response = new DefaultPeriodDuration(Math.abs(diff));
        return response;
    }

    /** Returns the length of this TM_GeometricPrimitive */
    public Duration length() {
        Duration response = null;
        long diff = 0L;
        if (this instanceof Instant) {
            response = new DefaultPeriodDuration(Math.abs(diff));
            return response;
        } else {
            if (this instanceof Period) {
                if (((Period) this).getBeginning() != null && ((Period) this).getEnding() != null) {
                    response =
                            ((DefaultInstant) ((Period) this).getBeginning())
                                    .distance(((DefaultInstant) ((Period) this).getEnding()));
                    return response;
                }
            }
            return null;
        }
    }
}
