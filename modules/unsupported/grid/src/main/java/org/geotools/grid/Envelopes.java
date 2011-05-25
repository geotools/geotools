/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * A helper class to create bounding envelopes with width and height that are
 * simple multiples of a given resolution.
 * <pre><code>
 * // Example of use: creating an envlope with 'neat' lat-lon bounds
 * // that encompasses a set of features
 *
 * SimpleFeatureSource featureSource = ...
 * ReferencedEnvelope featureBounds = featureSource.getBounds();
 * ReferencedEnvelope latLonEnv = featureBounds.transform(DefaultGeographicCRS.WGS84, true);
 *
 * // 1 degree grid resoluation
 * final double gridSize = 1.0;
 * ReferencedEnvelope gridBounds = Envelopes.expandToInclude(latLonEnv, gridSize);
 * </code></pre>
 *
 * @todo move this class or its methods to a more general module
 *
 * @author mbedward
 * @since 2.7
 *
 * @source $URL$
 * @version $Id$
 */
public class Envelopes {

    private static final double EPS = 1.0E-8;

    /**
     * Include the provided envelope, expanding as necessary and rounding
     * the bounding coordinates such that they are multiples of the
     * specified resolution. For example, if {@code resolution} is 100 then the
     * min and max bounding coordinates of this envelope will set to mutliples
     * of 100 by rounding down the min values and rounding up the max values
     * if required.
     * <pre><code>
     * // Example, create a new envelope that cntains an input envlope and
     * // whose boundind coordinates are multiples of 100
     * //
     * ReferencedEnvelope inputEnv = ...
     * ReferencedEnvelope roundedEnv = new ReferencedEnvelope();
     * roundedEnv.expandToInclude(inputEnv, 100);
     * </code></pre>
     *
     * @param srcEnv the envelope to include
     * @param resolution resolution (in world distance units) of the resulting
     *        boundary coordinates
     *
     * @return a new envelope with 'rounded' bounding coordinates
     */
    public static ReferencedEnvelope expandToInclude(ReferencedEnvelope srcEnv, double resolution) {
        double minX = roundOrdinate(srcEnv.getMinX(), resolution, false);
        double maxX = roundOrdinate(srcEnv.getMaxX(), resolution, true);
        double minY = roundOrdinate(srcEnv.getMinY(), resolution, false);
        double maxY = roundOrdinate(srcEnv.getMaxY(), resolution, true);
        
        ReferencedEnvelope expanded = new ReferencedEnvelope(srcEnv);
        expanded.expandToInclude(minX, minY);
        expanded.expandToInclude(maxX, maxY);
        return expanded;
    }

    /**
     * Helper method to round ordinate values up or down to a specified resolution.
     * The returned value will be a multiple of the specified resolution.
     * <pre><code>
     * double ordinate = 1234.56;
     * double resolution = 100;
     * double rounded;
     *
     * // this will return 1200
     * rounded = roundOrdinate(ordinate, resolution, false);
     *
     * // this will return 1300
     * rounded = roundOrdinate(ordinate, resolution, true);
     * </code></pre>
     * @param ordinate the ordinate to round up or down.
     * @param resolution the desired resolution
     * @param roundUp true to round up; false to round down
     *
     * @return the rounded ordinate value
     */
    private static double roundOrdinate(double ordinate, double resolution, boolean roundUp) {
        double unsigned = Math.abs(ordinate);
        boolean negative = ordinate < 0.0;
        if (negative) {
            roundUp = !roundUp;
        }
        double rounded;
        if (roundUp) {
            double x = unsigned / resolution;
            int up = (x - (long) x) > EPS ? 1 : 0;
            rounded = resolution * (up + (long) x);
        } else {
            rounded = resolution * (long) (unsigned / resolution);
        }
        return negative ? -rounded : rounded;
    }
}
