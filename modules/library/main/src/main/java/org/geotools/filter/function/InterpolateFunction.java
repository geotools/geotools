/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.Parameter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.text.Text;
import org.geotools.util.Converters;
import org.geotools.util.KVP;

/**
 * This is an implemenation of the Interpolate function as defined by OGC Symbology Encoding (SE) 1.1 specification.
 *
 * <p>The first parameter should be either the name of a numeric feature property or, if this function is being used as
 * a raster colormap, the String "RasterData" (case-insensitive).
 *
 * <p>Following this there should be a sequence of interpolation points, each of which is described by two parameters:
 * the first a datum and the second a return value. In the SE speicification these parameters are expected to be
 * Literals but in this implementation more general Expressions are also supported.
 *
 * <p>Two optional parameters can be provided following the interpolation points: A "method" parameter which can take
 * the values "numeric" or "color" and a "mode" parameter which can take the values "linear", "cosine" or "cubic" (Note:
 * it would make more sense if these terms were reversed but we are adhering to their use as published in the OGC
 * specification).
 *
 * <p><b>Number of points and interpolation modes</b>
 *
 * <ul>
 *   <li>Linear and cosine interpolation each require at least two interpolation points to be supplied.
 *   <li>Cubic interpolation normally requires at least four points with at least two points either side of the value
 *       being interpolated. In this function, deal generously with values that lie in the first or last interpolation
 *       segment by adding a duplicate of the first or last point as an extra point. This means that it is allowed
 *       (though not necessarily sensible) to use cubic interpolation with only three points.
 *   <li>If only two points are supplied but cubic interpolation is specified, the function will fall back to linear
 *       interpolation.
 *   <li>For all interpolation modes, incoming values outside the range of the interpolation points will be mapped to
 *       the value of the closest point (min or max).
 *   <li>The function will accept a single interpolation point, but all incoming values will simply be mapped to the
 *       value of that point regardless of the type of interpolation requested.
 *   <li>If no interpolation points are supplied, an {@code Exception} is thrown.
 *
 * @author Michael Bedward
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class InterpolateFunction implements Function {

    private static final Logger LOGGER = Logger.getLogger(InterpolateFunction.class.getName());

    private static final FilterFactory ff2 = CommonFactoryFinder.getFilterFactory(null);
    private static final double EPS = 1.0e-8;

    /** Use as a literal value to indicate interpolation mode */
    public static final String MODE_LINEAR = "linear";

    /** Use as a literal value to indicate interpolation mode */
    public static final String MODE_COSINE = "cosine";

    /** Use as a literal value to indicate interpolation mode */
    public static final String MODE_CUBIC = "cubic";

    /*
     * TODO: Surely the SE spec has "method" and "mode" the wrong
     * way around. It makes much more sense to have mode as numeric
     * or color and method as linear, cosine or cubic.
     */

    /** Use as a literal value to indicate interpolation method */
    public static final String METHOD_NUMERIC = "numeric";

    /** Use as a literal value to indicate interpolation method */
    public static final String METHOD_COLOR = "color";

    /** Alternate spelling - being kind to users */
    private static final String METHOD_COLOUR = "colour";

    private static enum Mode {
        LINEAR,
        COSINE,
        CUBIC
    }

    private static enum Method {
        NUMERIC,
        COLOR
    }

    private Mode mode;
    private boolean modeSpecified;
    private Method method;
    private boolean methodSpecified;

    protected abstract class InterpPoint {
        abstract double getData(Object object);

        abstract double getValue(Object object);

        abstract Color getColor(Object object);

        public <T> T getValue(Object object, Class<T> context) {
            if (method == Method.COLOR) {
                return Converters.convert(getColor(object), context);
            } else {
                return Converters.convert(getValue(object), context);
            }
        }
    }

    protected class DynamicPoint extends InterpPoint {
        Expression data;
        Expression value;

        public DynamicPoint(Expression data, Expression value) {
            this.data = data;
            this.value = value;
        }

        @Override
        public double getData(Object object) {
            return data.evaluate(object, Double.class);
        }

        @Override
        public double getValue(Object object) {
            return value.evaluate(object, Double.class);
        }

        @Override
        public Color getColor(Object object) {
            return value.evaluate(object, Color.class);
        }
    }

    protected abstract class ConstantPoint extends InterpPoint {

        double data;
        double value;

        public ConstantPoint(double data) {
            this.data = data;
        }

        @Override
        double getData(Object object) {
            return data;
        }
    }

    protected class ConstantNumericPoint extends ConstantPoint {

        double value;

        public ConstantNumericPoint(double data, double value) {
            super(data);
            this.value = value;
        }

        @Override
        double getValue(Object object) {
            return value;
        }

        @Override
        Color getColor(Object object) {
            return null;
        }
    }

    protected class ConstantColorPoint extends ConstantPoint {

        Color value;

        public ConstantColorPoint(double data, Color value) {
            super(data);
            this.value = value;
        }

        @Override
        double getValue(Object object) {
            return Double.NaN;
        }

        @Override
        Color getColor(Object object) {
            return value;
        }
    }

    protected volatile List<InterpPoint> interpPoints;

    /** Use as a PropertyName when defining a color map. The "Raterdata" is expected to apply to only a single band; */
    public static final String RASTER_DATA = "Rasterdata";

    private final List<Expression> parameters;
    private final Literal fallback;

    /** Make the instance of FunctionName available in a consistent spot. */
    public static final FunctionName NAME;

    static {
        Parameter<Object> lookup = new Parameter<>("lookup", Object.class, 1, 1);
        Parameter<Object> table = new Parameter<>("data value pairs", Object.class, 4, -1);
        Parameter<String> mode = new Parameter<>(
                "mode",
                String.class,
                Text.text("mode"),
                Text.text("linear, cosine or cubic"),
                true,
                1,
                1,
                MODE_LINEAR,
                new KVP(Parameter.OPTIONS, Arrays.asList(new String[] {MODE_LINEAR, MODE_COSINE, MODE_CUBIC})));
        Parameter<String> method = new Parameter<>(
                "method",
                String.class,
                Text.text("method"),
                Text.text("numeric or color"),
                false,
                0,
                1,
                METHOD_NUMERIC,
                new KVP(Parameter.OPTIONS, Arrays.asList(new String[] {METHOD_NUMERIC, METHOD_COLOR})));
        NAME = new FunctionNameImpl("Interpolate", lookup, table, mode, method);
    }

    public InterpolateFunction() {
        this(new ArrayList<>(), null);
    }

    public InterpolateFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
    }

    @Override
    public String getName() {
        return "Interpolate";
    }

    @Override
    public FunctionName getFunctionName() {
        return NAME;
    }

    @Override
    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public Object evaluate(Object object) {
        return evaluate(object, Object.class);
    }

    /**
     * {@inheritDoc}
     *
     * <p>When {@code context} is unspecified (i.e. {@code null} or {@code Object.class}), it'll be derived from the
     * {@code method} parameter, as {@code java.awt.Color.class} when {@code method == COLOR}, and as
     * {@code java.lang.Double} when {@code method == NUMERIC}.
     *
     * @throws IllegalArgumentException if {@code context == java.awt.Color.class} and {@code method != COLOR}
     */
    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        threadSafeInitialize();
        context = sanitizeContext(context);
        validateArguments(context);

        /*
         * Lookup value should be either the name of a feature property which can be evaluated to a Double or the string
         * "RasterData" (case-insensitive) indicating use of this function in a raster colormap.
         */
        final Expression lookup = parameters.get(0);
        Double lookupValue = null;

        /*
         * TODO: is this the correct way to handle the rasterdata option ?
         */
        // This should be safe. Rationale: raster mode assumes the lookup expression evaluates to
        // Rasterdata, and then the input object is considered to be a number. While the lookup
        // could dynamically evaluate to "Rasterdata" in a non-raster context (feature input),
        // the input object would not be a number and the following cast would fail.
        if (lookup instanceof Literal && RASTER_DATA.equalsIgnoreCase(lookup.evaluate(object, String.class))) {
            lookupValue = ((Number) object).doubleValue();
        } else {
            lookupValue = lookup.evaluate(object, Double.class);
        }
        if (lookupValue == null) return null;

        /* Degenerate case: a single interpolation point. Evaluate it directly. */
        if (interpPoints.size() == 1) {
            return parameters.get(2).evaluate(object, context);
        }

        final int segment = findSegment(lookupValue, object);
        if (segment <= 0) {
            // Data below the range of the interpolation points
            return interpPoints.get(0).getValue(object, context);
        } else if (segment >= interpPoints.size()) {
            // Data above the range of the interpolation points
            return interpPoints.get(interpPoints.size() - 1).getValue(object, context);
        }

        /*
         * The lookup value is within or above the range of the interpolation points - perform the requested type of
         * interpolation
         */
        switch (mode) {
            case COSINE:
                return cosineInterpolate(lookupValue, object, segment, context);

            case CUBIC:
                return cubicInterpolate(lookupValue, object, segment, context);

            case LINEAR:
            default:
                return linearInterpolate(lookupValue, object, segment, context);
        }
    }

    /**
     * Returns the default context based on method when it's not specified (e.g. null or Object.class), for the
     * Converters to return the default object type: Color if method == COLOR, Double of method == NUMERIC. If a
     * specific "context" is asked, returns it as-is.
     */
    @SuppressWarnings("unchecked")
    private <T> Class<T> sanitizeContext(Class<T> context) {
        final boolean specified = context != null && !Object.class.equals(context);
        if (!specified) {
            if (method == Method.NUMERIC) {
                context = (Class<T>) Double.class;
            } else if (method == Method.COLOR) {
                context = (Class<T>) Color.class;
            } else {
                // should never reach here for as long as initialize() is called first
                throw new IllegalStateException("Unknown method, expected NUMERIC or COLOR");
            }
        }
        return context;
    }

    private void validateArguments(Class<?> context) {
        if (Color.class.isAssignableFrom(context) && method != Method.COLOR) {
            throw new IllegalArgumentException(
                    "Unable to evaluate Color as the interpolation method is set as " + method);
        }
    }

    @SuppressWarnings("LongDoubleConversion")
    private <T> T linearInterpolate(
            final Double lookupValue, final Object object, final int segment, Class<T> context) {
        if (segment < 1 || segment >= interpPoints.size()) {
            throw new IllegalArgumentException("segment index outside valid range");
        }

        Double data1 = interpPoints.get(segment).getData(object);
        Double data0 = interpPoints.get(segment - 1).getData(object);
        if (method == Method.COLOR) {
            Color color1 = interpPoints.get(segment).getColor(object);
            Color color0 = interpPoints.get(segment - 1).getColor(object);
            int r = (int)
                    clamp(Math.round(doLinear(lookupValue, data0, data1, color0.getRed(), color1.getRed())), 0, 255);
            int g = (int) clamp(
                    Math.round(doLinear(lookupValue, data0, data1, color0.getGreen(), color1.getGreen())), 0, 255);
            int b = (int)
                    clamp(Math.round(doLinear(lookupValue, data0, data1, color0.getBlue(), color1.getBlue())), 0, 255);
            return Converters.convert(new Color(r, g, b), context);
        } else { // assume numeric
            Double value1 = interpPoints.get(segment).getValue(object);
            Double value0 = interpPoints.get(segment - 1).getValue(object);

            double interpolated = doLinear(lookupValue, data0, data1, value0, value1);
            return Converters.convert(interpolated, context);
        }
    }

    @SuppressWarnings("LongDoubleConversion")
    private <T> T cosineInterpolate(
            final Double lookupValue, final Object object, final int segment, Class<T> context) {
        if (segment < 1 || segment >= interpPoints.size()) {
            throw new IllegalArgumentException("segment index outside valid range");
        }

        Double data1 = interpPoints.get(segment).getData(object);
        Double data0 = interpPoints.get(segment - 1).getData(object);
        if (method == Method.COLOR) {
            Color color1 = interpPoints.get(segment).getColor(object);
            Color color0 = interpPoints.get(segment - 1).getColor(object);
            int r = (int)
                    clamp(Math.round(doCosine(lookupValue, data0, data1, color0.getRed(), color1.getRed())), 0, 255);
            int g = (int) clamp(
                    Math.round(doCosine(lookupValue, data0, data1, color0.getGreen(), color1.getGreen())), 0, 255);
            int b = (int)
                    clamp(Math.round(doCosine(lookupValue, data0, data1, color0.getBlue(), color1.getBlue())), 0, 255);
            return Converters.convert(new Color(r, g, b), context);

        } else { // assume numeric
            Double value1 = interpPoints.get(segment).getValue(object);
            Double value0 = interpPoints.get(segment - 1).getValue(object);

            double interpolated = doCosine(lookupValue, data0, data1, value0, value1);
            return Converters.convert(interpolated, context);
        }
    }

    @SuppressWarnings("LongDoubleConversion")
    private <T> T cubicInterpolate(final Double lookupValue, final Object object, int segment, Class<T> context) {
        if (segment < 1 || segment >= interpPoints.size()) {
            throw new IllegalArgumentException("segment index outside valid range");
        }

        double[] xi = new double[4];
        double[] yi = new double[4];
        List<InterpPoint> workingPoints = new ArrayList<>(interpPoints);

        /*
         * We deal generously with lookup values that fall into the first or
         * last interpolation segment by adding a duplicate of the first or
         * last point to the working interpolation points. The datum of
         * the duplicate point is set such that the difference between it and
         * the 'real' end-point's datum is the same as the distance between the
         * 'real' end-point and the next 'real' point.
         */
        if (segment == 1) {
            double data0 = workingPoints.get(0).getData(object);
            double data1 = workingPoints.get(1).getData(object);
            Expression firstValue = parameters.get(2);
            workingPoints.add(0, buildInterpPoint(ff2.literal(2 * data0 - data1), firstValue));
            segment++;
        } else if (segment == interpPoints.size() - 1) {
            double data0 = workingPoints.get(segment).getData(object);
            double data1 = workingPoints.get(segment - 1).getData(object);
            Expression lastValue = parameters.get(1 + interpPoints.size() * 2);
            workingPoints.add(buildInterpPoint(ff2.literal(2 * data0 - data1), lastValue));
        }

        for (int i = segment - 2, k = 0; k < 4; i++, k++) {
            xi[k] = workingPoints.get(i).getData(object);
        }

        if (method == Method.COLOR) {
            Color[] ci = new Color[4];
            for (int i = segment - 2, k = 0; k < 4; i++, k++) {
                ci[k] = workingPoints.get(i).getColor(object);
            }

            for (int i = 0; i < 4; i++) {
                yi[i] = ci[i].getRed();
            }
            int r = (int) clamp(Math.round(doCubic(lookupValue, xi, yi)), 0, 255);

            for (int i = 0; i < 4; i++) {
                yi[i] = ci[i].getGreen();
            }
            int g = (int) clamp(Math.round(doCubic(lookupValue, xi, yi)), 0, 255);

            for (int i = 0; i < 4; i++) {
                yi[i] = ci[i].getBlue();
            }
            int b = (int) clamp(Math.round(doCubic(lookupValue, xi, yi)), 0, 255);

            return Converters.convert(new Color(r, g, b), context);

        } else { // numeric
            for (int i = segment - 2, k = 0; k < 4; i++, k++) {
                yi[k] = workingPoints.get(i).getValue(object);
            }
            double interpolated = doCubic(lookupValue, xi, yi);
            return Converters.convert(interpolated, context);
        }
    }

    @Override
    public Literal getFallbackValue() {
        return fallback;
    }

    private void threadSafeInitialize() {
        // initialize the lookup data structures only once and in a thread safe way please
        if (interpPoints == null) {
            synchronized (this) {
                if (interpPoints == null) {
                    initialize();
                }
            }
        }
    }

    /**
     * This method checks for optional parameters setting the mode and method and retrieves the interpolation points.
     */
    private void initialize() {
        setMode();
        setMethod();
        final int numControlParameters = (modeSpecified ? 1 : 0) + (methodSpecified ? 1 : 0);

        // There should be at least one interpolation point consisting of a data and value parameter
        final int numInterpolationParmaters = parameters.size() - numControlParameters - 1;
        if (numInterpolationParmaters < 2) {
            throw new IllegalArgumentException("Need at least one interpolation point");
        }

        if (numInterpolationParmaters % 2 != 0) {
            throw new IllegalArgumentException("Missing data or value in interpolation points ?");
        }

        List<Expression> sub = parameters.subList(1, parameters.size() - numControlParameters);
        interpPoints = new ArrayList<>();
        for (int i = 0; i < numInterpolationParmaters; i += 2) {
            interpPoints.add(buildInterpPoint(sub.get(i), sub.get(i + 1)));
        }

        if (mode == Mode.CUBIC) {
            if (interpPoints.size() < 3) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Cubic interpolation requested but not enough"
                            + "points supplied. Falling back to linear interpolation");
                }
                mode = Mode.LINEAR;
            }
        }
    }

    private InterpPoint buildInterpPoint(Expression data, Expression value) {
        if (data instanceof Literal && value instanceof Literal) {
            if (method == Method.COLOR) {
                Color color = value.evaluate(null, Color.class);
                if (color == null) {
                    throw new IllegalArgumentException("Could not convert value " + value + " to a color");
                }
                return new ConstantColorPoint(data.evaluate(null, Double.class), color);
            } else {
                Double numeric = value.evaluate(null, Double.class);
                if (numeric == null) {
                    throw new IllegalArgumentException("Could not convert value " + value + " to a number");
                }
                return new ConstantNumericPoint(data.evaluate(null, Double.class), numeric);
            }
        }
        return new DynamicPoint(data, value);
    }

    /** Review parameters and generate {@link Mode} linear cosine, cubbic based on optional parameter. */
    private void setMode() {
        boolean specified = false;

        // If mode is specified it will be the last or second last parameter
        final int n = parameters.size();
        for (int i = 2; i >= 1 && !specified; i--) {
            int index = n - i;
            if (index > 1) {
                Expression expr = parameters.get(index);
                if (expr instanceof Literal && ((Literal) expr).getValue() instanceof String) {
                    String value = (String) ((Literal) expr).getValue();
                    if (value.equalsIgnoreCase(MODE_LINEAR)) {
                        mode = Mode.LINEAR;
                        specified = true;

                    } else if (value.equalsIgnoreCase(MODE_COSINE)) {
                        mode = Mode.COSINE;
                        specified = true;

                    } else if (value.equalsIgnoreCase(MODE_CUBIC)) {
                        mode = Mode.CUBIC;
                        specified = true;
                    }
                }
            }
        }

        // default mode is linear
        if (!specified) {
            mode = Mode.LINEAR;
        }

        modeSpecified = specified;
    }

    /** Review parameters and generate {@link Method} numeric or color based on optional parameter. */
    private void setMethod() {
        boolean specified = false;

        // If method is specified it will be the last or second last parameter
        final int n = parameters.size();
        for (int i = 2; i >= 1 && !specified; i--) {
            int index = n - i;
            if (index > 1) {
                Expression expr = parameters.get(index);
                if (expr instanceof Literal && ((Literal) expr).getValue() instanceof String) {
                    String value = (String) ((Literal) expr).getValue();
                    if (value.equalsIgnoreCase(METHOD_NUMERIC)) {
                        method = Method.NUMERIC;
                        specified = true;

                    } else if (value.equalsIgnoreCase(METHOD_COLOR) || value.equalsIgnoreCase(METHOD_COLOUR)) {
                        method = Method.COLOR;
                        specified = true;
                    }
                }
            }
        }

        // default mode is numeric
        if (!specified) {
            method = Method.NUMERIC;
        }

        methodSpecified = specified;
    }

    /**
     * Find the interpolation segment containing the lookup value. The value returned is the index, in the interpPoints
     * list, of the higher point of the segment.
     *
     * @return segment index; or 0 if the lookup value is below the range of the interpolation points; or {@code max
     *     segment index + 1} if it is above the range
     */
    private int findSegment(Double lookupValue, Object object) {
        int segment = interpPoints.size();

        for (int i = 0; i < interpPoints.size(); i++) {
            Double data = interpPoints.get(i).getData(object);
            if (lookupValue <= data) {
                segment = i;
                break;
            }
        }

        return segment;
    }

    /**
     * Performs linear interpolation
     *
     * @param x value for which a y ordinate is being interpolated
     * @param x0 lower interpolation point x ordinate
     * @param x1 upper interpolation point x ordinate
     * @param y0 lower interpolation point y ordinate
     * @param y1 upper interpolation point y ordinate
     * @return interpolated y value
     */
    private double doLinear(double x, double x0, double x1, double y0, double y1) {
        double xspan = getSpan(x0, x1);
        double t = (x - x0) / xspan;
        return y0 + t * (y1 - y0);
    }

    /**
     * Performs consine interpolation
     *
     * @param x value for which a y ordinate is being interpolated
     * @param x0 lower interpolation point x ordinate
     * @param x1 upper interpolation point x ordinate
     * @param y0 lower interpolation point y ordinate
     * @param y1 upper interpolation point y ordinate
     * @return interpolated y value
     */
    private double doCosine(double x, double x0, double x1, double y0, double y1) {
        double xspan = getSpan(x0, x1);
        double t = (x - x0) / xspan;
        double tcos = 0.5 * (1.0 - Math.cos(t * Math.PI));
        return y0 + tcos * (y1 - y0);
    }

    /**
     * Cubic hermite spline interpolation. This is adapted from the description of the algorithm at:
     * http://en.wikipedia.org/wiki/Cubic_Hermite_spline. Tangent caculations are done with simple finite differencing
     * in the interests of speed.
     *
     * <p>The input arrays xi and yi contain the coordinates of four interpolation points defining three segments with
     * the middle segment containing the point for which we seek an interpolated value.
     *
     * @param x x ordinate of the point for which we seek an interpolated value and which lies between xi[1] and xi[2]
     * @param xi x ordinates of the four interpolation points
     * @param yi y ordinates of the four interpolation points
     * @return interpolated y value
     */
    private double doCubic(double x, double[] xi, double[] yi) {
        double span12 = getSpan(xi[1], xi[2]);
        double t = (x - xi[1]) / span12;

        if (t < EPS) {
            return yi[1];
        } else if (1.0 - t < EPS) {
            return yi[2];
        }

        double span01 = getSpan(xi[0], xi[1]);
        double span23 = getSpan(xi[2], xi[3]);
        double t2 = t * t;
        double t3 = t2 * t;

        double m1 = 0.5 * ((yi[2] - yi[1]) / span12 + (yi[1] - yi[0]) / span01);
        double m2 = 0.5 * ((yi[3] - yi[2]) / span23 + (yi[2] - yi[1]) / span12);

        double y = (2 * t3 - 3 * t2 + 1) * yi[1]
                + (t3 - 2 * t2 + t) * span12 * m1
                + (-2 * t3 + 3 * t2) * yi[2]
                + (t3 - t2) * span12 * m2;

        return y;
    }

    /**
     * Helper method for the linear, cosine and cubic interpolation methods. Checks that the span of the interval from
     * x0 to x1 is > 0.
     *
     * @param x0 lower interval point
     * @param x1 upper interval point
     * @return interval span
     * @throws IllegalArgumentException if the span is less than a small tolerance value
     */
    private double getSpan(double x0, double x1) {
        double xspan = x1 - x0;
        // the span should be > 0
        if (xspan < EPS) {
            throw new IllegalArgumentException(
                    "Interpolation points must be in ascending order of data (lookup) values with no ties");
        }

        return xspan;
    }

    /**
     * Clamp a value to lie between the given min and max values (inclusive)
     *
     * @param x input value
     * @param min minimum
     * @param max maximum
     * @return the clamped value
     */
    private double clamp(double x, double min, double max) {
        return Math.max(min, Math.min(max, x));
    }

    /** Creates a String representation of this Function with the function name and the arguments. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        List<org.geotools.api.filter.expression.Expression> params = getParameters();
        if (params != null) {
            org.geotools.api.filter.expression.Expression exp;
            for (Iterator<org.geotools.api.filter.expression.Expression> it = params.iterator(); it.hasNext(); ) {
                exp = it.next();
                sb.append("[");
                sb.append(exp);
                sb.append("]");
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterpolateFunction that = (InterpolateFunction) o;
        return modeSpecified == that.modeSpecified
                && methodSpecified == that.methodSpecified
                && mode == that.mode
                && method == that.method
                && Objects.equals(interpPoints, that.interpPoints)
                && Objects.equals(parameters, that.parameters)
                && Objects.equals(fallback, that.fallback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, modeSpecified, method, methodSpecified, interpPoints, parameters, fallback);
    }
}
