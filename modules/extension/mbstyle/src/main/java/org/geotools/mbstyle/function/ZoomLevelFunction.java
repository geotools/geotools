/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * Function that takes a scale denominator and a srid and outputs the zoom level.
 *
 * <p>Note: EPSG:3857 is currently the only supported srid, and zoom levels are assumed to be at the equator. <br>
 * <br>
 * Example:
 *
 * <pre>
 * ff.function("zoomLevel",
 *               ff.function("env", ff.literal("wms_scale_denominator")),
 *               ff.literal("EPSG:3857")
 *            );
 * </pre>
 *
 * The above function would evaluate to the zoomLevel that corresponds to the current environment's
 * wms_scale_denominator, for EPSG:3857. <br>
 * <br>
 * Example 2:
 *
 * <pre>
 * ff.function("zoomLevel",
 *               ff.literal(136494.693347),
 *               ff.literal("EPSG:3857")
 *            );
 * </pre>
 *
 * The above function would evaluate to a zoomLevel of 12.
 */
public class ZoomLevelFunction extends FunctionExpressionImpl {

    public static final FunctionName NAME = new FunctionNameImpl(
            "zoomLevel",
            parameter("zoomLevel", Number.class),
            parameter("scaleDenominator", Number.class),
            parameter("srid", String.class));

    /** Name of the system variable controlling {@link #ROOT_TILE_PIXELS} */
    public static final String MBSTYLE_ROOT_TILE_PIXELS_KEY = "MBSTYLE_ROOT_TILE_PIXELS";

    /** Number of pixels in the side of the root tile. Assumes 512 if not otherwise configured. */
    public static final int ROOT_TILE_PIXELS =
            Integer.parseInt(System.getProperty(MBSTYLE_ROOT_TILE_PIXELS_KEY, "512"));

    /** Scale denominator of the root tile. Changes based on the ROOT_TILE_PIXELS value. */
    public static final double EPSG_3857_O_SCALE = 559_082_263.9508929 / ROOT_TILE_PIXELS * 256;

    public ZoomLevelFunction() {
        super(NAME);
    }

    public int getArgCount() {
        return 2;
    }

    @Override
    public Object evaluate(Object feature) {
        Number arg0;
        String arg1;

        try {
            arg0 = getExpression(0).evaluate(feature, Number.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function zoomLevel argument #0 - expected Number", e);
        }

        try {
            arg1 = getExpression(1).evaluate(feature, String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function zoomLevel argument #1 - expected String", e);
        }

        if ("EPSG:3857".equals(arg1)) {
            // This constant is the zoomLevel 0 scale denominator for web mercator at the equator.
            return Math.log(EPSG_3857_O_SCALE / arg0.doubleValue()) / Math.log(2);
        } else {
            throw new IllegalArgumentException("Unsupported srid for zoomLevel function: " + arg1);
        }
    }
}
