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

package org.geotools.mbstyle.parse;

import java.util.Set;
import org.geotools.api.filter.expression.Expression;
import org.geotools.mbstyle.parse.MBFunction.FunctionCategory;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class MBStyleTestUtils {
    public static Matcher<MBFunction> categories(Matcher<? super Set<FunctionCategory>> matcher) {
        return new BaseMatcher<>() {

            @Override
            public boolean matches(Object item) {
                if (item instanceof MBFunction function) {
                    return matcher.matches(function.category());
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("MBFunction with categories ").appendDescriptionOf(matcher);
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                if (item instanceof MBFunction function) {
                    description.appendText("categories were ");
                    description.appendValueList("[", ", ", "]", function.category());
                } else {
                    description.appendText("was a ").appendValue(item.getClass().getName());
                }
            }
        };
    }

    public static Matcher<Expression> evaluatesTo(Object object, Matcher<?> resultMatcher) {
        return new BaseMatcher<>() {

            @Override
            public boolean matches(Object item) {
                if (item instanceof Expression expression) {
                    return resultMatcher.matches(expression.evaluate(object));
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("expression evaluating ")
                        .appendValue(object)
                        .appendText(" to ")
                        .appendDescriptionOf(resultMatcher);
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                if (item instanceof Expression expression) {
                    Object result = expression.evaluate(object);
                    description.appendValue(item).appendText(" evaluates to ").appendValue(result);
                } else {
                    description.appendText("was a ").appendValue(item.getClass().getName());
                }
            }
        };
    }

    public static Matcher<Number> asInt(Matcher<Integer> matcher) {
        return new BaseMatcher<>() {

            @Override
            public boolean matches(Object item) {
                if (item instanceof Number) {
                    return matcher.matches(((Long) item).intValue());
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(matcher);
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                matcher.describeMismatch(item, description);
            }
        };
    }

    public static Matcher<Number> equalInt(Integer value) {
        return asInt(Matchers.equalTo(value));
    }

    public static <T> Matcher<Expression> evaluatesTo(
            Object object, Class<T> context, Matcher<? super T> resultMatcher) {
        return new BaseMatcher<>() {

            @Override
            public boolean matches(Object item) {
                if (item instanceof Expression expression) {
                    return resultMatcher.matches(expression.evaluate(object, context));
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("expression evaluating ")
                        .appendValue(object)
                        .appendText(" to ")
                        .appendDescriptionOf(resultMatcher);
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                if (item instanceof Expression expression) {
                    Object result = expression.evaluate(object);
                    description.appendValue(item).appendText(" evaluates to ").appendValue(result);
                } else {
                    description.appendText("was a ").appendValue(item.getClass().getName());
                }
            }
        };
    }
}
