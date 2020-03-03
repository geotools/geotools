/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

/**
 * Encapsulates the results from a FeatureCalc, and includes methods for obtaining and merging
 * results.
 *
 * @author Cory Horner, Refractions
 * @see FeatureCalc
 * @since 2.2.M2
 */
public interface CalcResult {
    /**
     * The result obtained when a FeatureCalc found no features to visit. It lets itself merge with
     * any other result by just returning the other result as the output of the merge
     */
    public static final CalcResult NULL_RESULT =
            new AbstractCalcResult() {
                /** Always compatible */
                public boolean isCompatible(CalcResult targetResults) {
                    return true;
                };

                /** Just returns the other result */
                public CalcResult merge(CalcResult resultsToAdd) {
                    return resultsToAdd;
                };
            };

    /**
     * Returns true if the target results is a compatible type with the current results, with
     * compatible meaning that the two results may be merged.
     *
     * @param targetResults the second CalcResult Object
     * @return true if the targetResults can be merged with the current results
     */
    public boolean isCompatible(CalcResult targetResults);

    /**
     * Returns the merged results of two CalcResult. The way in which the results are merged is
     * dependent on the type of the results added. A new instance is created containing the merged
     * results.
     *
     * <p>For example: merging two min functions would return the smaller of the two values; merging
     * a count and a sum would return an average.
     *
     * @return the merged results
     */
    public CalcResult merge(CalcResult resultsToAdd);

    /**
     * Actual answer
     *
     * @return the calculation result as a generic object
     */
    public Object getValue();

    /**
     * Access getValue as an int
     *
     * @return the calculation result as a int (or 0 if not applicable)
     */
    public int toInt();

    /**
     * Access getValue as a double
     *
     * @return the calculation result as a double (or 0 if not applicable)
     */
    public double toDouble();

    /**
     * Access getValue as a string
     *
     * @return the calculation result as a string (or "" if not applicable)
     */
    public String toString();

    /**
     * Access getValue as a long
     *
     * @return the calculation result as a long (or 0 if not applicable)
     */
    public long toLong();

    /**
     * Access getValue as a float
     *
     * @return the calculation result as a float (or 0 if not applicable)
     */
    public float toFloat();

    /**
     * Access getValue as a geometry
     *
     * @return the calculation result as a geometry (or null if not applicable)
     */
    public Geometry toGeometry();

    /**
     * Access getValue as an envelope
     *
     * @return the calculation result as an envelope (or null if not applicable)
     */
    public Envelope toEnvelope();

    /**
     * Access getValue as a point
     *
     * @return the calculation result as a point (or null if not applicable)
     */
    public Point toPoint();

    /**
     * Access getValue as a set
     *
     * @return the calculation result as a set (or null if not applicable)
     */
    public Set toSet();

    /**
     * Access getValue as a list
     *
     * @return the calculation result as a list (or null if not applicable)
     */
    public List toList();

    /**
     * Access getValue as an array
     *
     * @return the calculation result as an array (or null if not applicable)
     */
    public Object[] toArray();

    /**
     * Access getValue as a map
     *
     * @return the calculation result as a map (or null if not applicable)
     */
    public Map toMap();
}
