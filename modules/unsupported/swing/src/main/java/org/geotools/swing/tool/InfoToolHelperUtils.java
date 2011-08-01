/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.referencing.operation.MathTransform;

/**
 * Contains static methods used by some {@code InfoToolHelper} classes.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
public class InfoToolHelperUtils {

    /**
     * Transforms a position. If {@code transform} is {@code null} the original
     * position is returned.
     *
     * @param pos the position
     *
     * @return the transformed position
     */
    public static DirectPosition2D getTransformed(DirectPosition2D pos, MathTransform transform) {
        if (transform != null) {
            try {
                return new DirectPosition2D(transform.transform(pos, null));
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        return pos;
    }



    /**
     * Convert the Object returned by {@linkplain GridCoverage2D#evaluate(DirectPosition)}
     * into an array of {@code Numbers}.
     *
     * @param objArray an Object representing a primitive array
     *
     * @return a new array of Numbers
     */
    public static Number[] asNumberArray(Object objArray) {
        Number[] numbers = null;

        if (objArray instanceof byte[]) {
            byte[] values = (byte[]) objArray;
            numbers = new Number[values.length];
            for (int i = 0; i < values.length; i++) {
                numbers[i] = ((int)values[i]) & 0xff;
            }

        } else if (objArray instanceof int[]) {
            int[] values = (int[]) objArray;
            numbers = new Number[values.length];
            for (int i = 0; i < values.length; i++) {
                numbers[i] = values[i];
            }

        } else if (objArray instanceof float[]) {
            float[] values = (float[]) objArray;
            numbers = new Number[values.length];
            for (int i = 0; i < values.length; i++) {
                numbers[i] = values[i];
            }
        } else if (objArray instanceof double[]) {
            double[] values = (double[]) objArray;
            numbers = new Number[values.length];
            for (int i = 0; i < values.length; i++) {
                numbers[i] = values[i];
            }
        }

        return numbers;
    }

}
