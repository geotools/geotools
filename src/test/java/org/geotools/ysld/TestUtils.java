package org.geotools.ysld;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import org.geotools.styling.Rule;
import org.geotools.ysld.parse.ScaleRange;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

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
    
}
