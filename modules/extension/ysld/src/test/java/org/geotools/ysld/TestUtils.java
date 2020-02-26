/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.nullValue;

import java.awt.Color;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.geotools.styling.Rule;
import org.geotools.ysld.parse.ScaleRange;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;

public enum TestUtils {
    ;

    /**
     * Matches a rule if it applies to a given scale
     *
     * @param scale denominator of the scale
     */
    public static Matcher<Rule> appliesToScale(double scale) {
        return describedAs(
                "rule applies to scale denom %0",
                allOf(
                        Matchers.<Rule>hasProperty("maxScaleDenominator", greaterThan(scale)),
                        Matchers.<Rule>hasProperty(
                                "minScaleDenominator", lessThanOrEqualTo(scale))),
                scale);
    }

    public static Matcher<ScaleRange> rangeContains(double scale) {
        return describedAs(
                "scale range that contains 1:%0",
                allOf(
                        Matchers.<ScaleRange>hasProperty("maxDenom", greaterThan(scale)),
                        Matchers.<ScaleRange>hasProperty("minDenom", lessThanOrEqualTo(scale))),
                scale);
    }

    /** Matches a Literal expression with a value matching m */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Matcher<Expression> literal(Matcher m) {
        return (Matcher) allOf(instanceOf(Literal.class), hasProperty("value", m));
    }

    /** Matches a Literal expression with a value matching o */
    public static Matcher<Expression> literal(Object o) {
        return literal(lexEqualTo(o));
    }

    /** Matches a nil expression or null. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Matcher<Expression> nilExpression() {
        return (Matcher) Matchers.anyOf(nullValue(), instanceOf(NilExpression.class));
    }

    /** Matches as an attribute expression for a named attribute. */
    public static Matcher<Expression> attribute(String name) {
        return Matchers.<Expression>allOf(
                Matchers.<Expression>instanceOf(PropertyName.class),
                Matchers.<Expression>hasProperty("propertyName", equalTo(name)));
    }

    /** Matches a function with the given name and parameters matching the given matchers. */
    @SafeVarargs
    public static Matcher<Expression> function(String name, Matcher<Expression>... parameters) {
        return function(name, hasItems(parameters));
    }

    /** Matches a function with the given name and a parameter list matching the given matcher. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Matcher<Expression> function(
            String name, Matcher<? extends Iterable<Expression>> parameters) {
        return (Matcher)
                allOf(
                        instanceOf(Function.class),
                        hasProperty("functionName", hasProperty("name", equalTo(name))),
                        hasProperty("parameters", parameters));
    }

    /** Compares the string representation of the object being matched to that of value. */
    public static Matcher<? extends Object> lexEqualTo(final Object value) {
        return new BaseMatcher<Object>() {

            @Override
            public boolean matches(Object arg0) {
                if (arg0 == null) {
                    arg0 = "";
                }
                return arg0.toString().equals(value.toString());
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("lexicaly equal to ").appendValue(value.toString());
            }
        };
    }

    /**
     * Converts a Number to double, otherwise converts to string and then parses as double then
     * matches to the given value.
     */
    public static Matcher<? extends Object> numEqualTo(final double value, final double epsilon) {
        return new BaseMatcher<Object>() {

            @Override
            public boolean matches(Object obj) {
                double num;
                if (obj instanceof Number) {
                    num = ((Number) obj).doubleValue();
                } else {
                    num = Double.parseDouble(obj.toString());
                }
                return Math.abs(num - value) < epsilon;
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("can be parsed as ").appendValue(value);
                arg0.appendText(" to within ").appendValue(epsilon);
            }
        };
    }

    /**
     * Converts a Number to long, otherwise converts to string and then parses as double then
     * matches to the given value.
     */
    public static Matcher<? extends Object> numEqualTo(final long value) {
        return new BaseMatcher<Object>() {

            @Override
            public boolean matches(Object obj) {
                double num;
                if (obj instanceof Number) {
                    num = ((Number) obj).longValue();
                } else {
                    num = Long.parseLong(obj.toString());
                }
                return num == value;
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("can be parsed as ").appendValue(value);
            }
        };
    }

    public static Matcher<String> asHexInt(final Matcher<Integer> m) {
        return new BaseMatcher<String>() {
            @Override
            public boolean matches(Object arg0) {
                return m.matches(Integer.parseInt((String) arg0, 16));
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("Hexadecimal string ").appendDescriptionOf(m);
            }
        };
    }

    public static Matcher<String> asColor(final Matcher<Color> m) {
        return new BaseMatcher<String>() {
            @Override
            public boolean matches(Object arg0) {
                Color c;
                try {
                    c = Color.decode((String) arg0);
                } catch (NumberFormatException ex) {
                    c = new Color(Integer.parseInt((String) arg0, 16));
                }
                return m.matches(c);
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("represents colour ").appendDescriptionOf(m);
            }
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Matcher<Object> isColor(Color c) {
        String hex = String.format("#%06x", c.getRGB() & 0x00FFFFFF);
        return Matchers.describedAs(
                "is the colour %0 %1",
                anyOf(
                        (Matcher) allOf(instanceOf(String.class), asColor(equalTo(c))),
                        (Matcher) allOf(instanceOf(Color.class), equalTo(c)),
                        (Matcher)
                                allOf(instanceOf(Integer.class), equalTo(c.getRGB() & 0x00FFFFFF))),
                hex,
                c);
    }

    public static Matcher<Object> isColor(String s) {
        Color c;
        c = new Color(Integer.parseInt(s, 16));
        return isColor(c);
    }

    /** Matches a YamlSeq where the specified entry matching the given matcher */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Matcher<Object> yHasItem(final int i, final Matcher<? extends Object> m) {
        return new BaseMatcher() {

            @Override
            public boolean matches(Object obj) {
                if (!(obj instanceof YamlSeq)) return false;
                YamlSeq seq = (YamlSeq) obj;

                Object value = null;
                try {
                    value = seq.map(i);
                } catch (IllegalArgumentException ex1) {
                    try {
                        value = seq.seq(i);
                    } catch (IllegalArgumentException ex2) {
                        value = seq.get(i);
                    }
                }
                return (m.matches(value));
            }

            @Override
            public void describeTo(Description desc) {
                desc.appendText("YamlSeq with item ")
                        .appendValue(i)
                        .appendText(" that ")
                        .appendDescriptionOf(m);
            }
        };
    }

    /** Matches a YamlMap with an entry as named that has a value which matches the given matcher */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Matcher<Object> yHasEntry(final String key, final Matcher<? extends Object> m) {
        return new BaseMatcher() {

            @Override
            public boolean matches(Object obj) {
                if (!(obj instanceof YamlMap)) return false;
                YamlMap map = (YamlMap) obj;

                if (!map.has(key)) return false;
                Object value = null;
                try {
                    value = map.map(key);
                } catch (IllegalArgumentException ex1) {
                    try {
                        value = map.seq(key);
                    } catch (IllegalArgumentException ex2) {
                        value = map.get(key);
                    }
                }
                return (m.matches(value));
            }

            @Override
            public void describeTo(Description desc) {
                desc.appendText("YamlMap with entry ")
                        .appendValue(key)
                        .appendText(" and value ")
                        .appendDescriptionOf(m);
            }
        };
    }

    /** Matches a YamlMap with an entry as named that has a value which matches the given matcher */
    public static Matcher<Object> yHasEntry(final String key) {
        return yHasEntry(key, Matchers.any(Object.class));
    }

    private static final Pattern TUPLE_STRIP = Pattern.compile("^\\s*\\(([^)]*)\\)\\s*$");

    private static final Pattern TUPLE_SPLIT = Pattern.compile("\\s*,\\s*");

    /** Matches a YamlSeq */
    @SafeVarargs
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Matcher<? extends Object> yContains(final Matcher<? extends Object>... matchers) {
        return new BaseMatcher() {

            @Override
            public boolean matches(Object obj) {
                YamlSeq seq;
                if (obj instanceof YamlSeq) {
                    seq = (YamlSeq) obj;
                } else {
                    return false;
                }

                if (seq.raw().size() != matchers.length) {
                    return false;
                }

                for (int i = 0; i < matchers.length; i++) {
                    if (!matchers[i].matches(seq.get(i))) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public void describeTo(Description desc) {
                desc.appendList("Yaml Sequence with values [", ", ", "]", Arrays.asList(matchers));
            }
        };
    }

    /** Matches a YSLD Tuple with values matching the given matchers. */
    @SafeVarargs
    public static Matcher<? extends Object> yTuple(final Matcher<? extends Object>... matchers) {
        return yContains(matchers);
    }

    /** Matches a YSLD Tuple with n values */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Matcher<Object> yTuple(int n) {
        Matcher[] matchers = new Matcher[n];
        Arrays.fill(matchers, anything());
        return Matchers.describedAs("A YSLD Tuple with %0 values", (Matcher) yTuple(matchers), n);
    }

    /** For apparent consistency to the user, some values are wrapped in fake YAML strings. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Matcher<? extends Object> fakeString(final Matcher<? extends Object> m) {
        return new BaseMatcher() {

            @Override
            public boolean matches(Object obj) {
                if (obj instanceof String) {
                    String str = (String) obj;
                    if (str.startsWith("'") && str.endsWith("'")) {
                        str = str.substring(1, str.length() - 1);
                    } else if (str.startsWith("\"") && str.endsWith("\"")) {
                        str = str.substring(1, str.length() - 1);
                    }
                    return m.matches(str);
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description desc) {
                desc.appendText("a fake YAML string ").appendDescriptionOf(m);
            }
        };
    }

    /** For apparent consistency to the user, some values are wrapped in fake YAML strings. */
    public static Matcher<? extends Object> fakeString(final String s) {
        return fakeString(equalTo(s));
    }
}
