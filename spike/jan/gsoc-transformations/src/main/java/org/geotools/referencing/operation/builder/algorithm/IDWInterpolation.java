/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder.algorithm;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;


/**
 * Implementation of IDW Interpolation.
 *
 * @author jezekjan
 *
 */
public class IDWInterpolation extends AbstractInterpolation {
    /**Max distance to take points into account */
    private double maxDist = 0;

    /**
     *
     * @param positions HashMap containing {@link org.opengis.geometry.DirectPosition} as
     * key and value of general parameter as value
     * @param dx Value of step in x direction between generated cells
     * @param dy Value of step in y direction between generated cells
     * @param envelope Envelope that should be filled by generated grid
     */
    public IDWInterpolation(HashMap<DirectPosition, Float> positions, int xNumOfCells, int yNumOfCells, Envelope envelope) {
        super(positions, xNumOfCells, yNumOfCells, envelope);
    }

    /**
     * Constructs Interpolation from specified positions and its values.
     * @param positions
     */
    public IDWInterpolation(HashMap/*<DirectPosition, float>*/ positions) {
        super(positions);
    }

    /** 
     * Returns max distance that defines when the point are taken into account
     * during interpolation.
     * @return max distance (when 0 than all points are taken)
     */
    public double getMaxDist() {
        return maxDist;
    }

    /**
     * Sets max distance that defines when the point are taken into account
     * during interpolation (when 0 than all points are taken - 0 is the default).
     * @param maxDist max distance 
     */
    public void setMaxDist(double maxDist) {
        this.maxDist = 10000;
    }

    public float getValue(DirectPosition p) {
        return calculateValue(p);
    }

    /**
     * Computes nearest points.
     * @param p
     * @param maxdistance
     * @param number
     * @return
     *
     * @todo consider some indexing mechanism for finding the nearest positions
     */
    private HashMap getNearestPositions(DirectPosition p, double maxdistance) {
        HashMap nearest = new HashMap();

        DirectPosition source = null;
        double dist;

        for (Iterator i = this.getPositions().keySet().iterator(); i.hasNext();) {
            source = (DirectPosition) i.next();

            /**
             *  @todo - calculate elipsoidal distance/             
             */
            /* if ((source != null)
               || source.getCoordinateReferenceSystem().getClass()
                            .isAssignableFrom(DefaultGeographicCRS.class)) {
               dist = ((DefaultGeographicCRS) source.getCoordinateReferenceSystem()).distance(p
                       .getCoordinates(), source.getCoordinates()).doubleValue();
               } else {*/
            dist = ((Point2D) p).distance((Point2D) source);

            //}
            if (((dist != 0) || (dist < maxdistance))) {
                nearest.put(source, new Double(dist));
            }
        }

        return nearest;
    }

    private float calculateValue(DirectPosition p) {
        return calculateValue(p, getMaxDist());
    }

    /**
     * Computes value at point {@link p} from points that are not farer than {@link maxDistance}
     * @param p
     * @param maxDist
     * @return
     */
    private float calculateValue(DirectPosition p, double maxDist) {
        HashMap nearest = getNearestPositions(p, maxDist);

        float value;
        double sumdValue = 0;
        double sumweight = 0;

        for (Iterator i = nearest.keySet().iterator(); i.hasNext();) {
            DirectPosition dp = (DirectPosition) i.next();
            double distance = ((Double) nearest.get(dp)).doubleValue();
            double weight = (1 / Math.pow(distance, 2));

            sumdValue = sumdValue + (float) ( (this.getPositions().get(dp)) * weight);

            sumweight = sumweight + weight;
        }

        value = (float) (sumdValue / sumweight);

        return value;
    }
}
