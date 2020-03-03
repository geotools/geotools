/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style.windbarbs;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.Utilities;

/**
 * A WindBarb object made of reference speed in knots, and related number of longBarbs (10 kts),
 * shortBarbs (5 kts) and pennants (50 kts).
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
class WindBarb {

    /** The logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(WindBarb.class);

    /**
     * A WindBarbDefinition contains parameters used to build the WindBarb, such as the main vector
     * length, the elements Spacing, the length of long barbs...
     *
     * @author Daniele Romagnoli, GeoSolutions SAS
     */
    static class WindBarbDefinition {

        public WindBarbDefinition(
                final int vectorLength,
                final int basePennantLength,
                final int elementsSpacing,
                final int longBarbLength,
                final int zeroWindRadius) {
            // checks
            if (vectorLength <= 0) {
                throw new IllegalArgumentException("Invalid vectorLength:" + vectorLength);
            }
            if (basePennantLength <= 0) {
                throw new IllegalArgumentException("Invalid basePennantLength:" + vectorLength);
            }
            if (elementsSpacing <= 0) {
                throw new IllegalArgumentException("Invalid elementsSpacing:" + vectorLength);
            }
            if (vectorLength < elementsSpacing + basePennantLength) {
                throw new IllegalArgumentException(
                        "Invalid vectorLength<elementsSpacing+basePennantLength : " + vectorLength);
            }
            if (longBarbLength <= 0) {
                throw new IllegalArgumentException("Invalid longBarbLength:" + vectorLength);
            }
            if (zeroWindRadius <= 0) {
                throw new IllegalArgumentException("Invalid zeroWindRadius:" + vectorLength);
            }

            // code
            this.vectorLength = vectorLength;
            this.basePennantLength = basePennantLength;
            this.elementsSpacing = elementsSpacing;
            this.longBarbLength = longBarbLength;
            this.shortBarbLength = longBarbLength / 2;
            this.zeroWindRadius = zeroWindRadius;
        }

        /** The main vector length */
        int vectorLength;

        /** The length of the base of the pennant (the triangle) */
        int basePennantLength;

        /** The distance between multiple barbs, and pennants */
        int elementsSpacing;

        /** The length of a long barb */
        int longBarbLength;

        /** The length of a short barb (is always half the length of a long barb) */
        int shortBarbLength;

        int zeroWindRadius;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("WindBarbDefinition [vectorLength=");
            builder.append(vectorLength);
            builder.append(", basePennantLength=");
            builder.append(basePennantLength);
            builder.append(", elementsSpacing=");
            builder.append(elementsSpacing);
            builder.append(", longBarbLength=");
            builder.append(longBarbLength);
            builder.append(", shortBarbLength=");
            builder.append(shortBarbLength);
            builder.append(", zeroWindRadius=");
            builder.append(zeroWindRadius);
            builder.append("]");
            return builder.toString();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + basePennantLength;
            result = prime * result + elementsSpacing;
            result = prime * result + longBarbLength;
            result = prime * result + shortBarbLength;
            result = prime * result + vectorLength;
            result = prime * result + zeroWindRadius;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof WindBarbDefinition)) {
                return false;
            }
            WindBarbDefinition other = (WindBarbDefinition) obj;
            if (basePennantLength != other.basePennantLength) {
                return false;
            }
            if (elementsSpacing != other.elementsSpacing) {
                return false;
            }
            if (longBarbLength != other.longBarbLength) {
                return false;
            }
            if (shortBarbLength != other.shortBarbLength) {
                return false;
            }
            if (vectorLength != other.vectorLength) {
                return false;
            }
            if (zeroWindRadius != other.zeroWindRadius) {
                return false;
            }
            return true;
        }
    }

    int knots;

    int pennants;

    int longBarbs;

    int shortBarbs;

    static int DEFAULT_BARB_LENGTH = 20;

    static int DEFAULT_BASE_PENNANT_LENGTH = 5;

    static int DEFAULT_ELEMENTS_SPACING = 5;

    static int DEFAULT_FLAGPOLE_LENGTH = 40;

    static int DEFAULT_ZERO_WIND_RADIUS = 5;

    /**
     * A {@link WindBarbDefinition} instance reporting structural values for a WindBarb (vector
     * length, sizes, ...)
     */
    WindBarbDefinition windBarbDefinition;

    static WindBarbDefinition DEFAULT_WINDBARB_DEFINITION =
            new WindBarbDefinition(
                    WindBarb.DEFAULT_FLAGPOLE_LENGTH,
                    WindBarb.DEFAULT_BASE_PENNANT_LENGTH,
                    WindBarb.DEFAULT_ELEMENTS_SPACING,
                    WindBarb.DEFAULT_BARB_LENGTH,
                    WindBarb.DEFAULT_ZERO_WIND_RADIUS);

    public WindBarb(final int knots) {
        this(DEFAULT_WINDBARB_DEFINITION, knots);
    }

    public WindBarb(final WindBarbDefinition definition, final int knots) {
        Utilities.ensureNonNull("definition", definition);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(
                    "Creating WindBarb for knots: "
                            + knots
                            + " and WindBarbDefinition:"
                            + definition.toString());
        }
        this.windBarbDefinition = definition;
        this.knots = knots;
        if (knots == -1) {
            return;
        } else {
            if (knots < 0) {
                throw new IllegalArgumentException("Illegal wind speeds(kn): " + knots);
            }
            if (knots > WindBarbsFactory.MAX_SPEED) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "speed is exceeding MaxSpeed = "
                                    + WindBarbsFactory.MAX_SPEED
                                    + "."
                                    + "\nThe related WindBarb isn't in the cache");
                }
            }
        }
        pennants = knots / 50;
        longBarbs = (knots - (pennants * 50)) / 10;
        shortBarbs = (knots - (pennants * 50) - (longBarbs * 10)) / 5;
    }

    /** Build a {@Shape} WindBarb */
    Shape build() {
        if (knots < 0) {
            return buildAbsentModule();
        } else {
            return buildStandardBarb();
        }
    }

    /** @return */
    private Shape buildStandardBarb() {
        int positionOnPath = -windBarbDefinition.vectorLength;

        // Base barb
        Path2D path = new Path2D.Double();

        // Initialize Barb
        if (knots < 5) {
            // let's use a circle for Calm
            return new Ellipse2D.Float(
                    -windBarbDefinition.zeroWindRadius
                            / 2.0f, // the X coordinate of the upper-left corner of the specified
                    // rectangular area
                    -windBarbDefinition.zeroWindRadius
                            / 2.0f, // the Y coordinate of the upper-left corner of the specified
                    // rectangular area
                    windBarbDefinition.zeroWindRadius,
                    windBarbDefinition.zeroWindRadius);
        } else {

            // draw wind barb line
            path.moveTo(0, 0);
            path.lineTo(0, positionOnPath);
        }

        // pennants management
        if (pennants > 0) {
            positionOnPath = drawPennants(path, positionOnPath);
            positionOnPath += windBarbDefinition.elementsSpacing; // add spacing
        }

        // long barbs management
        if (longBarbs > 0) {
            positionOnPath = drawLongBarbs(path, positionOnPath);
            positionOnPath += windBarbDefinition.elementsSpacing; // add spacing
        }

        // short barbs management
        if (shortBarbs > 0) {
            positionOnPath = drawShortBarb(path, positionOnPath);
        }

        // FLIP for geotools
        final Shape createTransformedShape =
                path.createTransformedShape(AffineTransform.getScaleInstance(1, -1));
        return createTransformedShape;
    }

    /** Build a {@Shape} WindBarb */
    Shape buildAbsentModule() {

        // Base barb
        Path2D path = new Path2D.Double();
        int positionOnPath = 0;

        // draw wind barb line
        path.moveTo(0, positionOnPath);
        positionOnPath -= windBarbDefinition.vectorLength;
        path.lineTo(0, positionOnPath);

        path.moveTo(
                windBarbDefinition.shortBarbLength / 2.0f,
                positionOnPath - windBarbDefinition.shortBarbLength / 2.0f);
        path.lineTo(
                -windBarbDefinition.shortBarbLength / 2.0f,
                positionOnPath + windBarbDefinition.shortBarbLength / 2.0f);

        path.moveTo(
                -windBarbDefinition.shortBarbLength / 2.0f,
                positionOnPath - windBarbDefinition.shortBarbLength / 2.0f);
        path.lineTo(
                windBarbDefinition.shortBarbLength / 2.0f,
                positionOnPath + windBarbDefinition.shortBarbLength / 2.0f);

        // FLIP for geotools
        final Shape createTransformedShape =
                path.createTransformedShape(AffineTransform.getScaleInstance(1, -1));
        return createTransformedShape;
    }

    /** Add short barbs to the shape */
    private int drawShortBarb(Path2D path, int positionOnPath) {
        if (pennants == 0 && longBarbs == 0) {
            positionOnPath = -windBarbDefinition.vectorLength + DEFAULT_ELEMENTS_SPACING;
        }

        path.moveTo(0, positionOnPath);
        path.lineTo(
                windBarbDefinition.shortBarbLength,
                positionOnPath - windBarbDefinition.basePennantLength / 4.0);
        return positionOnPath;
    }

    /** Add long barbs to the shape */
    private int drawLongBarbs(Path2D path, int positionOnPath) {
        if (longBarbs <= 0) {
            return positionOnPath;
        }
        for (int elements = 0; elements < longBarbs; elements++) {

            if (elements >= 1) {
                // spacing if neede
                positionOnPath += windBarbDefinition.elementsSpacing;
            }
            // draw long barb
            path.moveTo(0, positionOnPath);
            path.lineTo(
                    windBarbDefinition.longBarbLength,
                    positionOnPath - windBarbDefinition.basePennantLength / 2.0);
        }
        return positionOnPath;
    }

    /** add Pennants to the shape */
    private int drawPennants(Path2D path, int positionOnPath) {
        if (pennants <= 0) {
            return positionOnPath;
        }

        for (int elements = 0; elements < pennants; elements++) {
            // move forward one pennant at a time

            // draw pennant
            path.moveTo(0, positionOnPath);
            positionOnPath += windBarbDefinition.basePennantLength / 2.0;
            path.lineTo(windBarbDefinition.longBarbLength, positionOnPath); // first edge
            positionOnPath += windBarbDefinition.basePennantLength / 2.0;
            path.lineTo(0, positionOnPath); // second edge
            path.closePath();

            // only one square
        }
        return positionOnPath;
    }
}
