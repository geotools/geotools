package org.geotools.ysld;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.nullValue;

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
     * @param scale denominator of the scale
     */
    @SuppressWarnings("unchecked")
    public static Matcher<Rule> appliesToScale(double scale) {
        return describedAs("rule applies to scale denom %0",
                allOf(
                    Matchers.<Rule>hasProperty("maxScaleDenominator", 
                        greaterThan(scale)
                    ),
                    Matchers.<Rule>hasProperty("minScaleDenominator", 
                        lessThanOrEqualTo(scale)
                    )),
                    scale
                );
    }
    
    @SuppressWarnings("unchecked")
    public static Matcher<ScaleRange> rangeContains(double scale) {
        return describedAs("scale range that contains 1:%0",
                allOf(
                    Matchers.<ScaleRange>hasProperty("maxDenom", 
                        greaterThan(scale)
                    ),
                    Matchers.<ScaleRange>hasProperty("minDenom", 
                        lessThanOrEqualTo(scale)
                    )),
                    scale
                );
    }
    
    /**
     * Matches a Literal expression with a value matching m
     * @param m
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public
       static Matcher<Expression> literal(Matcher m) {
           return (Matcher)allOf(
                   instanceOf(Literal.class), 
                   hasProperty("value", m)
                   );
     }
    
    /**
     * Matches a nil expression or null.
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public
       static Matcher<Expression> nilExpression() {
           return (Matcher)Matchers.anyOf(
                   nullValue(),
                   instanceOf(NilExpression.class)
                   );
     }
    /**
     * Matches as an attribute expression for a named attribute.
     * @param name
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public
       static Matcher<Expression> attribute(String name) {
           return (Matcher)allOf(
                   instanceOf(PropertyName.class), 
                   hasProperty("propertyName", equalTo(name))
                   );
    }
    
    /**
     * Matches a function with the given name and parameters matching the given matchers.
     * @param name
     * @param parameters
     * @return
     */
    @SafeVarargs
    public static Matcher<Expression> function(String name, Matcher<Expression>... parameters) {
        return function(name, hasItems(parameters));
    }
    
    /**
     * Matches a function with the given name and a parameter list matching the given matcher.
     * @param name
     * @param parameters
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Matcher<Expression> function(String name, 
            Matcher<? extends Iterable<Expression>> parameters) {
        return (Matcher)allOf(
                   instanceOf(Function.class), 
                   hasProperty("functionName", hasProperty("name", equalTo(name))),
                   hasProperty("parameters", parameters)
                   );
    }
    
    /**
     * Compares the string representation of the object being matched to that of value.
     * @param value
     * @return
     */
    public static Matcher<? extends Object> lexEqualTo (final Object value) {
        return new BaseMatcher<Object>() {
            
            @Override
            public boolean matches(Object arg0) {
                return arg0.toString().equals(value.toString());
            }
            
            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("lexicaly equal to ").appendValue(value.toString());
            }
            
        };
    }
}
