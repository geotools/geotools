/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.filter.function;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Illustrates how to write a custom filter function that implements the
 * {@linkplain org.opengis.filter.expression.Function} interface directly.
 * The example here is a function that can be used in a {@linkplain org.geotools.styling.Style}
 * to dynamically set the fill color of polygon features based on a numeric
 * feature attribute (e.g. country population size).
 * <p>
 * GeoTools provides a great many useful filter functions for numeric and
 * spatial operations but sometimes it is still necessary, or perhaps easier,
 * for users to create custom functions. This can be done by extending the
 * GeoTools classes or, as in this example, implementing the {@code Function}
 * interface directly.
 *
 * @author Michael Bedward
 *
 * @source $URL$
 */
public class ColorRampFunction implements Function {

    private static final float EPS = 1.0e-6F;

    private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    private String name;
    private List<Expression> paramList;

    private final Literal fallback;

    private float minValue;
    private float maxValue;
    private float saturation;
    private float brightness;

    /**
     * Constructor.
     *
     * @param valueExpr the expression that will be used to get the feature value
     *        from which the fill color will be calculated
     *
     * @param minValue minimum expected feature value
     *
     * @param maxValue maximum expected feature value
     *
     * @param saturation HSB color model saturation (between 0 and 1)
     *
     * @param brightness HSB color model brightness (between 0 and 1)
     */
    public ColorRampFunction(Expression valueExpr, float minValue, float maxValue, float saturation, float brightness) {
        this.name = "ColorRamp";
        this.paramList = new ArrayList<Expression>();

        fallback = filterFactory.literal(Color.GRAY);

        if (minValue - maxValue > -EPS) {
            throw new IllegalArgumentException("minValue must be greater than maxValue");
        }

        this.minValue = minValue;
        this.maxValue = maxValue;

        if (saturation < 0.0d || saturation > 1.0d ||
            brightness < 0.0d || brightness > 1.0d) {
            throw new IllegalArgumentException("saturation and brightness must between 0 and 1");
        }

        this.saturation = saturation;
        this.brightness = brightness;

        this.paramList.add(valueExpr);
    }

    /**
     * Get the name of this function
     *
     * @return function name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the parameters that have been set for this function
     * (only one parameter for the current implementation).
     *
     * @return live list of function parameters
     */
    public List<Expression> getParameters() {
        return paramList;
    }

    /**
     * Get the fallback value for this function
     *
     * @return {@code Color.GRAY} as a {@code Literal} object
     */
    public Literal getFallbackValue() {
        return fallback;
    }

    /**
     * Evaluate the function for a given feature. The feature's
     * value will be extracted and a color calculated to use as the
     * polygon fill.
     *
     * @param feature the input feature
     * @return a {@code Color} object
     */
    public Object evaluate(Object feature) {
        float value = ((Number) getParameters().get(0).evaluate(feature)).floatValue();
        System.out.println("value = " + value);
        return valueToColor(value);
    }

    /**
     * Evaluate the function for a given feature. The feature's
     * value will be extracted and a color calculated to use as the
     * polygon fill.
     *
     * @param <T> type parameter - must be {@code Color} or a subclass
     * @param feature the input feature
     * @param clazz a Class object for the type (e.g. {@code Color.class})
     *
     * @return a {@code Color} object
     */
    public <T> T evaluate(Object feature, Class<T> clazz) {
        if (Color.class.isAssignableFrom(clazz)) {
            return clazz.cast(evaluate(feature));
        }

        throw new UnsupportedOperationException("Color is the only supported class");
    }

    /**
     * Accept a visitor that wishes to examine this function's
     * parameters. This method will be invoked during the rendering
     * process to get Stroke and Fill attributes.
     *
     * @param visitor a visitor object
     * @param arg visitor-specific argument
     *
     * @return result of the visit
     */
    public Object accept(ExpressionVisitor visitor, Object arg) {
        return visitor.visit(this, arg);
    }

    /**
     * Calculate a color based on a float value. The calculation
     * involves range-coding the input value and then using this as
     * the hue in an HSB color ramp.
     *
     * @param value the input feature value
     * @return a new {@code Color} object or null if the input value
     *         was outside the range minValue:maxValue
     */
    private Color valueToColor(float value) {
        float hue = (value - minValue) / (maxValue - minValue);
        if (hue < 0.0f || hue > 1.0f) {
            return null;
        }

        int rgb = Color.HSBtoRGB(hue * 0.5f, saturation, brightness);
        return new Color(rgb);
    }

}
