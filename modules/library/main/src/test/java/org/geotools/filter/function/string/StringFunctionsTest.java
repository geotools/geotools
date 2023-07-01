package org.geotools.filter.function.string;

import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;

public class StringFunctionsTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    @Test
    public void testConcatenate() throws Exception {
        Function function =
                ff.function(
                        "Concatenate", ff.literal("hello"), ff.literal(" "), ff.literal("world"));
        Assert.assertEquals("hello world", function.evaluate(null, String.class));
    }
}
