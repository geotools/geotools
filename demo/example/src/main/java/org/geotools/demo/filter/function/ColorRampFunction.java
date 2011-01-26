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
package org.geotools.demo.filter.function;

import java.awt.Color;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionExpressionImpl;
import org.opengis.filter.FilterFactory;

/**
 * Generates a colour based on an input floating point value. First the value is
 * converted to a proportion of the expected range of values. If the value falls
 * outside the range {@code null} is returned, otherwise the proportion is used as
 * a hue value, together with parameters for saturation and brightness, to calculate
 * a colour in the HSB colour space which is returned as an RGB value.
 * <p>
 * Example of use in a SLD document to set polygon fill colour based on the value
 * of a numeric attribute FOO with an expected range of values from -10 to 10:
 * <pre><code>
 *   &lt;PolygonSymbolizer>
 *     &lt;Fill>
 *       &lt;CssParameter name="fill">
 *         &lt;ogc:Function name="colorramp">
 *           &lt;ogc:PropertyName>FOO&lt;/ogc:PropertyName>
 *           &lt;!-- min expected value -->
 *           &lt;ogc:Literal>-10&lt;/ogc:Literal>
 *           &lt;!-- max expected value -->
 *           &lt;ogc:Literal>10&lt;/ogc:Literal>
 *           &lt;!-- saturation -->
 *           &lt;ogc:Literal>0.8&lt;/ogc:Literal>
 *           &lt;!-- brightness -->
 *           &lt;ogc:Literal>0.8&lt;/ogc:Literal>
 *         &lt;/ogc:Function>
 *       &lt;/CssParameter>
 *     &lt;/Fill>
 *   &lt;/PolygonSymbolizer>
 * </code></pre>
 *
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class ColorRampFunction extends FunctionExpressionImpl {

    private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /**
     * Constructor.
     */
    public ColorRampFunction() {
        super("colorramp");
    }

    /**
     * Evaluate the function for the given feature and return a colour.
     * 
     * @param feature the input feature
     * @return a {@code Color} instance
     */
    @Override
    public Object evaluate(Object feature) {
        float attributeValue = getExpression(0).evaluate(feature, Float.class);
        float minValue = getExpression(1).evaluate(feature, Float.class);
        float maxValue = getExpression(2).evaluate(feature, Float.class);
        float saturation = getExpression(3).evaluate(feature, Float.class);
        float brightness = getExpression(3).evaluate(feature, Float.class);

        return valueToColor(attributeValue, minValue, maxValue, saturation, brightness);
    }

    /**
     * Calculate a color based on a float value by range-coding the
     * input value and then using this as the hue in an HSB color ramp.
     *
     * @param value input value
     * @return a new {@code Color} object or null if the input value
     *         was outside the range minValue:maxValue
     */
    private Color valueToColor(float value, float minValue, float maxValue, float saturation, float brightness) {
        float hue = (value - minValue) / (maxValue - minValue);
        if (hue < 0.0f || hue > 1.0f) {
            return null;
        }

        int rgb = Color.HSBtoRGB(adjustHue(hue), saturation, brightness);
        return new Color(rgb);
    }

    /**
     * Adjust the hue to lie within the range 0.125 : 0.875
     *
     * @param hue input hue between 0 and 1
     * @return adjusted hue
     */
    private float adjustHue(float hue) {
        return hue * 0.75f + 0.125f;
    }

    /**
     * Get the number of arguments.
     *
     * @return returns 5 (value, min, max, saturation, brightness)
     */
    @Override
    public int getArgCount() {
        return 5;
    }

}
