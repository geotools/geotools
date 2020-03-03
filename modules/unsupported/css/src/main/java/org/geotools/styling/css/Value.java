/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.Parameter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * A value for a CSS property. Values can be several things, including from literals, expressions,
 * composition of multiple values.
 *
 * @author Andrea Aime - GeoSolutions
 */
abstract class Value {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    public static final Value NONE = new None();

    public static final Map<String, String> COLORS_TO_HEX;

    static {
        COLORS_TO_HEX = new HashMap<String, String>();
        COLORS_TO_HEX.put("aliceblue", "#f0f8ff");
        COLORS_TO_HEX.put("antiquewhite", "#faebd7");
        COLORS_TO_HEX.put("aqua", "#00ffff");
        COLORS_TO_HEX.put("aquamarine", "#7fffd4");
        COLORS_TO_HEX.put("azure", "#f0ffff");
        COLORS_TO_HEX.put("beige", "#f5f5dc");
        COLORS_TO_HEX.put("bisque", "#ffe4c4");
        COLORS_TO_HEX.put("black", "#000000");
        COLORS_TO_HEX.put("blanchedalmond", "#ffebcd");
        COLORS_TO_HEX.put("blue", "#0000ff");
        COLORS_TO_HEX.put("blueviolet", "#8a2be2");
        COLORS_TO_HEX.put("brown", "#a52a2a");
        COLORS_TO_HEX.put("burlywood", "#deb887");
        COLORS_TO_HEX.put("cadetblue", "#5f9ea0");
        COLORS_TO_HEX.put("chartreuse", "#7fff00");
        COLORS_TO_HEX.put("chocolate", "#d2691e");
        COLORS_TO_HEX.put("coral", "#ff7f50");
        COLORS_TO_HEX.put("cornflowerblue", "#6495ed");
        COLORS_TO_HEX.put("cornsilk", "#fff8dc");
        COLORS_TO_HEX.put("crimson", "#dc143c");
        COLORS_TO_HEX.put("cyan", "#00ffff");
        COLORS_TO_HEX.put("darkblue", "#00008b");
        COLORS_TO_HEX.put("darkcyan", "#008b8b");
        COLORS_TO_HEX.put("darkgoldenrod", "#b8860b");
        COLORS_TO_HEX.put("darkgray", "#a9a9a9");
        COLORS_TO_HEX.put("darkgreen", "#006400");
        COLORS_TO_HEX.put("darkgrey", "#a9a9a9");
        COLORS_TO_HEX.put("darkkhaki", "#bdb76b");
        COLORS_TO_HEX.put("darkmagenta", "#8b008b");
        COLORS_TO_HEX.put("darkolivegreen", "#556b2f");
        COLORS_TO_HEX.put("darkorange", "#ff8c00");
        COLORS_TO_HEX.put("darkorchid", "#9932cc");
        COLORS_TO_HEX.put("darkred", "#8b0000");
        COLORS_TO_HEX.put("darksalmon", "#e9967a");
        COLORS_TO_HEX.put("darkseagreen", "#8fbc8f");
        COLORS_TO_HEX.put("darkslateblue", "#483d8b");
        COLORS_TO_HEX.put("darkslategray", "#2f4f4f");
        COLORS_TO_HEX.put("darkslategrey", "#2f4f4f");
        COLORS_TO_HEX.put("darkturquoise", "#00ced1");
        COLORS_TO_HEX.put("darkviolet", "#9400d3");
        COLORS_TO_HEX.put("deeppink", "#ff1493");
        COLORS_TO_HEX.put("deepskyblue", "#00bfff");
        COLORS_TO_HEX.put("dimgray", "#696969");
        COLORS_TO_HEX.put("dimgrey", "#696969");
        COLORS_TO_HEX.put("dodgerblue", "#1e90ff");
        COLORS_TO_HEX.put("firebrick", "#b22222");
        COLORS_TO_HEX.put("floralwhite", "#fffaf0");
        COLORS_TO_HEX.put("forestgreen", "#228b22");
        COLORS_TO_HEX.put("fuchsia", "#ff00ff");
        COLORS_TO_HEX.put("gainsboro", "#dcdcdc");
        COLORS_TO_HEX.put("ghostwhite", "#f8f8ff");
        COLORS_TO_HEX.put("gold", "#ffd700");
        COLORS_TO_HEX.put("goldenrod", "#daa520");
        COLORS_TO_HEX.put("gray", "#808080");
        COLORS_TO_HEX.put("grey", "#808080");
        COLORS_TO_HEX.put("green", "#008000");
        COLORS_TO_HEX.put("greenyellow", "#adff2f");
        COLORS_TO_HEX.put("honeydew", "#f0fff0");
        COLORS_TO_HEX.put("hotpink", "#ff69b4");
        COLORS_TO_HEX.put("indianred", "#cd5c5c");
        COLORS_TO_HEX.put("indigo", "#4b0082");
        COLORS_TO_HEX.put("ivory", "#fffff0");
        COLORS_TO_HEX.put("khaki", "#f0e68c");
        COLORS_TO_HEX.put("lavender", "#e6e6fa");
        COLORS_TO_HEX.put("lavenderblush", "#fff0f5");
        COLORS_TO_HEX.put("lawngreen", "#7cfc00");
        COLORS_TO_HEX.put("lemonchiffon", "#fffacd");
        COLORS_TO_HEX.put("lightblue", "#add8e6");
        COLORS_TO_HEX.put("lightcoral", "#f08080");
        COLORS_TO_HEX.put("lightcyan", "#e0ffff");
        COLORS_TO_HEX.put("lightgoldenrodyellow", "#fafad2");
        COLORS_TO_HEX.put("lightgray", "#d3d3d3");
        COLORS_TO_HEX.put("lightgreen", "#90ee90");
        COLORS_TO_HEX.put("lightgrey", "#d3d3d3");
        COLORS_TO_HEX.put("lightpink", "#ffb6c1");
        COLORS_TO_HEX.put("lightsalmon", "#ffa07a");
        COLORS_TO_HEX.put("lightseagreen", "#20b2aa");
        COLORS_TO_HEX.put("lightskyblue", "#87cefa");
        COLORS_TO_HEX.put("lightslategray", "#778899");
        COLORS_TO_HEX.put("lightslategrey", "#778899");
        COLORS_TO_HEX.put("lightsteelblue", "#b0c4de");
        COLORS_TO_HEX.put("lightyellow", "#ffffe0");
        COLORS_TO_HEX.put("lime", "#00ff00");
        COLORS_TO_HEX.put("limegreen", "#32cd32");
        COLORS_TO_HEX.put("linen", "#faf0e6");
        COLORS_TO_HEX.put("magenta", "#ff00ff");
        COLORS_TO_HEX.put("maroon", "#800000");
        COLORS_TO_HEX.put("mediumaquamarine", "#66cdaa");
        COLORS_TO_HEX.put("mediumblue", "#0000cd");
        COLORS_TO_HEX.put("mediumorchid", "#ba55d3");
        COLORS_TO_HEX.put("mediumpurple", "#9370db");
        COLORS_TO_HEX.put("mediumseagreen", "#3cb371");
        COLORS_TO_HEX.put("mediumslateblue", "#7b68ee");
        COLORS_TO_HEX.put("mediumspringgreen", "#00fa9a");
        COLORS_TO_HEX.put("mediumturquoise", "#48d1cc");
        COLORS_TO_HEX.put("mediumvioletred", "#c71585");
        COLORS_TO_HEX.put("midnightblue", "#191970");
        COLORS_TO_HEX.put("mintcream", "#f5fffa");
        COLORS_TO_HEX.put("mistyrose", "#ffe4e1");
        COLORS_TO_HEX.put("moccasin", "#ffe4b5");
        COLORS_TO_HEX.put("navajowhite", "#ffdead");
        COLORS_TO_HEX.put("navy", "#000080");
        COLORS_TO_HEX.put("oldlace", "#fdf5e6");
        COLORS_TO_HEX.put("olive", "#808000");
        COLORS_TO_HEX.put("olivedrab", "#6b8e23");
        COLORS_TO_HEX.put("orange", "#ffa500");
        COLORS_TO_HEX.put("orangered", "#ff4500");
        COLORS_TO_HEX.put("orchid", "#da70d6");
        COLORS_TO_HEX.put("palegoldenrod", "#eee8aa");
        COLORS_TO_HEX.put("palegreen", "#98fb98");
        COLORS_TO_HEX.put("paleturquoise", "#afeeee");
        COLORS_TO_HEX.put("palevioletred", "#db7093");
        COLORS_TO_HEX.put("papayawhip", "#ffefd5");
        COLORS_TO_HEX.put("peachpuff", "#ffdab9");
        COLORS_TO_HEX.put("peru", "#cd853f");
        COLORS_TO_HEX.put("pink", "#ffc0cb");
        COLORS_TO_HEX.put("plum", "#dda0dd");
        COLORS_TO_HEX.put("powderblue", "#b0e0e6");
        COLORS_TO_HEX.put("purple", "#800080");
        COLORS_TO_HEX.put("red", "#ff0000");
        COLORS_TO_HEX.put("rosybrown", "#bc8f8f");
        COLORS_TO_HEX.put("royalblue", "#4169e1");
        COLORS_TO_HEX.put("saddlebrown", "#8b4513");
        COLORS_TO_HEX.put("salmon", "#fa8072");
        COLORS_TO_HEX.put("sandybrown", "#f4a460");
        COLORS_TO_HEX.put("seagreen", "#2e8b57");
        COLORS_TO_HEX.put("seashell", "#fff5ee");
        COLORS_TO_HEX.put("sienna", "#a0522d");
        COLORS_TO_HEX.put("silver", "#c0c0c0");
        COLORS_TO_HEX.put("skyblue", "#87ceeb");
        COLORS_TO_HEX.put("slateblue", "#6a5acd");
        COLORS_TO_HEX.put("slategray", "#708090");
        COLORS_TO_HEX.put("slategrey", "#708090");
        COLORS_TO_HEX.put("snow", "#fffafa");
        COLORS_TO_HEX.put("springgreen", "#00ff7f");
        COLORS_TO_HEX.put("steelblue", "#4682b4");
        COLORS_TO_HEX.put("tan", "#d2b48c");
        COLORS_TO_HEX.put("teal", "#008080");
        COLORS_TO_HEX.put("thistle", "#d8bfd8");
        COLORS_TO_HEX.put("tomato", "#ff6347");
        COLORS_TO_HEX.put("turquoise", "#40e0d0");
        COLORS_TO_HEX.put("violet", "#ee82ee");
        COLORS_TO_HEX.put("wheat", "#f5deb3");
        COLORS_TO_HEX.put("white", "#ffffff");
        COLORS_TO_HEX.put("whitesmoke", "#f5f5f5");
        COLORS_TO_HEX.put("yellow", "#ffff00");
        COLORS_TO_HEX.put("yellowgreen", "#9acd32");
    }

    /**
     * Turns this value into a OGC expression. Only literals and expressions can be converted to a
     * OGC expression
     */
    public org.opengis.filter.expression.Expression toExpression() {
        throw new UnsupportedOperationException(
                "Cannot turn this value into a OGC expression: " + this);
    }

    /** Turns this value into a literal. Only true literals support this operation */
    public String toLiteral() {
        throw new UnsupportedOperationException("Cannot turn this value into a literal: " + this);
    }

    /**
     * A literal, that is, a static value, represented as a string
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class Literal extends Value {

        static final Pattern PERCENTAGE = Pattern.compile("(\\d+\\.?\\d*)\\s*%");

        public String body;

        public Literal(String body) {
            this.body = body;
        }

        @Override
        public String toString() {
            return "Literal[" + body + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((body == null) ? 0 : body.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Literal other = (Literal) obj;
            if (body == null) {
                if (other.body != null) return false;
            } else if (!body.equals(other.body)) return false;
            return true;
        }

        public org.opengis.filter.expression.Expression toExpression() {
            Matcher matcher = PERCENTAGE.matcher(body);
            if (matcher.matches()) {
                String group = matcher.group(1);
                Double percentage = Double.valueOf(group);
                return FF.literal(percentage / 100d);
            }
            return FF.literal(body);
        }

        public String toLiteral() {
            return body;
        }
    }

    /**
     * A function, with a name and parameters
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class Function extends Value {
        static final String URL = "url";

        static final String SYMBOL = "symbol";

        public static boolean isGraphicsFunction(Value v) {
            if (!(v instanceof Function)) {
                return false;
            } else {
                Function f = (Function) v;
                return URL.equals(f.name) || SYMBOL.equals(f.name);
            }
        }

        public String name;

        public List<Value> parameters;

        /** Builds a function */
        public Function(String name, List<Value> parameters) {
            super();
            this.parameters = parameters;
            this.name = name;
            if ((URL.equals(name) || SYMBOL.equals(name)) && parameters.size() != 1) {
                throw new IllegalArgumentException(
                        "Function " + name + " takes a single argument, not " + parameters.size());
            }
        }

        /** Builds a function */
        public Function(String name, Value... parameters) {
            this(name, Arrays.asList(parameters));
        }

        @Override
        public String toString() {
            return "Function [name=" + name + ", parameters=" + parameters + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
            return result;
        }

        @Override
        public org.opengis.filter.expression.Expression toExpression() {
            // turn function call if possible
            org.opengis.filter.expression.Expression[] params =
                    this.parameters
                            .stream()
                            .map(v -> v.toExpression())
                            .toArray(s -> new org.opengis.filter.expression.Expression[s]);
            return FF.function(this.name, params);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Function other = (Function) obj;
            if (name == null) {
                if (other.name != null) return false;
            } else if (!name.equals(other.name)) return false;
            if (parameters == null) {
                if (other.parameters != null) return false;
            } else if (!parameters.equals(other.parameters)) return false;
            return true;
        }
    }

    /**
     * A function, with a name and named parameters
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class TransformFunction extends Value {
        static final String URL = "url";

        static final String SYMBOL = "symbol";

        public String name;

        public Map<String, Value> parameters;

        /** Builds a function */
        public TransformFunction(String name, Map<String, Value> parameters) {
            super();
            this.parameters = parameters;
            this.name = name;
            if ((URL.equals(name) || SYMBOL.equals(name)) && parameters.size() != 1) {
                throw new IllegalArgumentException(
                        "Function " + name + " takes a single argument, not " + parameters.size());
            }
        }

        @Override
        public String toString() {
            return "TransformFunction [name=" + name + ", parameters=" + parameters + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            TransformFunction other = (TransformFunction) obj;
            if (name == null) {
                if (other.name != null) return false;
            } else if (!name.equals(other.name)) return false;
            if (parameters == null) {
                if (other.parameters != null) return false;
            } else if (!parameters.equals(other.parameters)) return false;
            return true;
        }

        @Override
        public org.opengis.filter.expression.Expression toExpression() {
            Map<String, Parameter<?>> paramInfo = loadProcessInfo(processName(name));
            if (paramInfo == null) {
                throw new RuntimeException(
                        "Could not locate rendering transformation named " + name);
            }
            List<org.opengis.filter.expression.Expression> arguments = new ArrayList<>();

            // See if we have to add the implicit parameter layer
            String inputLayerParameter = getInputLayerParameter(paramInfo);
            if (inputLayerParameter != null && !parameters.containsKey(inputLayerParameter)) {
                arguments.add(toParamFunction(inputLayerParameter, null));
            }

            // Transform all the parameters we have
            for (Map.Entry<String, Value> p : parameters.entrySet()) {
                String key = p.getKey();
                Value v = p.getValue();
                org.opengis.filter.expression.Expression ex = toParamFunction(key, v);
                arguments.add(ex);
            }

            org.opengis.filter.expression.Expression[] argsArray = toExpressionArray(arguments);
            return FF.function(name, argsArray);
        }

        private String getInputLayerParameter(Map<String, Parameter<?>> paramInfo) {
            for (Map.Entry<String, Parameter<?>> entry : paramInfo.entrySet()) {
                if (entry.getValue() != null) {
                    final Class<?> type = entry.getValue().getType();
                    if (GridCoverage2D.class.isAssignableFrom(type)
                            || FeatureCollection.class.isAssignableFrom(type)
                            || GridCoverage2DReader.class.isAssignableFrom(type)) {
                        return entry.getKey();
                    }
                }
            }

            return null;
        }

        private org.opengis.filter.expression.Expression toParamFunction(String key, Value v) {
            List<org.opengis.filter.expression.Expression> paramArgs = new ArrayList<>();
            // the param name
            paramArgs.add(FF.literal(key));
            if (v instanceof MultiValue) {
                MultiValue mv = (MultiValue) v;
                for (Value cv : mv.values) {
                    final org.opengis.filter.expression.Expression ex = cv.toExpression();
                    paramArgs.add(ex);
                }
            } else if (v != null) {
                final org.opengis.filter.expression.Expression ex = v.toExpression();
                paramArgs.add(ex);
            }
            org.opengis.filter.expression.Expression[] paramArgsArray =
                    toExpressionArray(paramArgs);
            org.opengis.filter.expression.Function function =
                    FF.function("parameter", paramArgsArray);
            return function;
        }

        private org.opengis.filter.expression.Expression[] toExpressionArray(
                List<org.opengis.filter.expression.Expression> arguments) {
            org.opengis.filter.expression.Expression[] argsArray =
                    (org.opengis.filter.expression.Expression[])
                            arguments.toArray(
                                    new org.opengis.filter.expression.Expression[arguments.size()]);
            return argsArray;
        }

        public static Name processName(String name) {
            String[] split = name.split(":");
            if (split.length == 1) {
                return new NameImpl(split[0]);
            }

            return new NameImpl(split[0], split[1]);
        }

        @SuppressWarnings("unchecked")
        public static Map<String, Parameter<?>> loadProcessInfo(Name name) {
            Class<?> processorsClass = null;
            try {
                processorsClass =
                        Class.forName(
                                "org.geotools.process.Processors",
                                false,
                                Value.class.getClassLoader());
                Method getParameterInfo = processorsClass.getMethod("getParameterInfo", Name.class);
                return (Map<String, Parameter<?>>) getParameterInfo.invoke(null, name);
            } catch (Exception e) {
                throw new RuntimeException("Error looking up process info", e);
            }
        }

        private boolean isRenderingTransformation(FunctionName fn) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    /**
     * An expression, backed by an OGC {@link org.opengis.filter.expression.Expression}
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class Expression extends Value {
        public org.opengis.filter.expression.Expression expression;

        public Expression(org.opengis.filter.expression.Expression expression) {
            super();
            this.expression = expression;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((expression == null) ? 0 : expression.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Expression other = (Expression) obj;
            if (expression == null) {
                if (other.expression != null) return false;
            } else if (!expression.equals(other.expression)) return false;
            return true;
        }

        @Override
        public String toString() {
            return "Expression [expression=" + expression + "]";
        }

        public org.opengis.filter.expression.Expression toExpression() {
            return expression;
        }

        public String toLiteral() {
            throw new UnsupportedOperationException(
                    "Cannot turn this value into a literal: " + this);
        }
    }

    /**
     * A list of values
     *
     * @author Andrea Aime - GeoSolutions
     */
    public static class MultiValue extends Value {
        List<Value> values;

        public MultiValue(List<Value> values) {
            this.values = values;
        }

        public MultiValue(Value... values) {
            this.values = Arrays.asList(values);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((values == null) ? 0 : values.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            MultiValue other = (MultiValue) obj;
            if (values == null) {
                if (other.values != null) return false;
            } else if (!values.equals(other.values)) return false;
            return true;
        }

        @Override
        public String toLiteral() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.size(); i++) {
                sb.append(values.get(i).toLiteral());
                if (i < values.size() - 1) {
                    sb.append(" ");
                }
            }

            return sb.toString();
        }
    }

    public static class None extends Value {

        private None() {}

        @Override
        public org.opengis.filter.expression.Expression toExpression() {
            return null;
        }

        @Override
        public String toLiteral() {
            return null;
        }
    }
}
