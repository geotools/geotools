/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.label;

import org.geotools.renderer.style.TextStyle2D;
import org.geotools.styling.TextSymbolizer.PolygonAlignOptions;
import org.locationtech.jts.algorithm.MinimumDiameter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;

/**
 * Helper class that keeps the state of the alternate polygon labelling angle to avoid its (sometime
 * expensive) computation as the labeller tries different labelling positions
 *
 * @author Andrea Aime - GeoSolutions
 */
class TextStyle2DExt extends TextStyle2D {

    Double alternateRotation;

    LabelCacheItem item;

    public TextStyle2DExt(LabelCacheItem item) {
        super(item.getTextStyle());
        this.item = item;
    }

    void setupPolygonAlign(PreparedGeometry pg) {
        if (item.getPolygonAlign() == PolygonAlignOptions.NONE) return;
    }

    boolean flipRotation(Geometry geometry) {
        if (item.getPolygonAlign() == PolygonAlignOptions.NONE) {
            return false;
        }

        // lazily compute rotation
        if (alternateRotation == null) {
            double radians = 0;
            if (item.getPolygonAlign() == PolygonAlignOptions.ORTHO) {
                radians = calcPolygonAlignOrthoAngle(geometry);
            } else if (item.getPolygonAlign() == PolygonAlignOptions.MBR) {
                radians = calcPolygonAlignMBRAngle(geometry);
            }

            alternateRotation = radians;
        }

        // swap the two rotations
        double temp = getRotation();
        setRotation(alternateRotation);
        alternateRotation = temp;
        return true;
    }

    double calcPolygonAlignOrthoAngle(Geometry geometry) {
        Envelope envelope = geometry.getEnvelopeInternal();
        if (envelope.getHeight() > envelope.getWidth()) {
            return -(Math.PI / 2);
        } else {
            return 0;
        }
    }

    double calcPolygonAlignMBRAngle(Geometry geometry) {
        // use JTS MinimumDiameter class to calc MBR
        Geometry mbr = new MinimumDiameter(geometry).getMinimumRectangle();

        // calc angle from the longest side of the MBR
        Coordinate[] coordinates = mbr.getCoordinates();
        double dx, dy;
        if (coordinates[0].distance(coordinates[1]) > coordinates[1].distance(coordinates[2])) {
            dx = coordinates[1].x - coordinates[0].x;
            dy = coordinates[1].y - coordinates[0].y;
        } else {
            dx = coordinates[2].x - coordinates[1].x;
            dy = coordinates[2].y - coordinates[1].y;
        }
        double angle = Math.atan(dy / dx);
        // make sure we turn PI/2 into -PI/2, we don't want some labels looking straight up
        // and some others straight down, when almost vertical they should all be oriented
        // on the same side
        if (Math.abs(angle - Math.PI / 2) < Math.PI / 180.0) {
            angle = -Math.PI / 2 + Math.abs(angle - Math.PI / 2);
        }
        return angle;
    }
}
