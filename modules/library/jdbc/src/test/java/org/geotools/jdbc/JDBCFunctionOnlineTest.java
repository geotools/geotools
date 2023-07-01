package org.geotools.jdbc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LengthFunction;
import org.geotools.filter.function.FilterFunction_strConcat;
import org.geotools.filter.function.FilterFunction_strEndsWith;
import org.geotools.filter.function.FilterFunction_strEqualsIgnoreCase;
import org.geotools.filter.function.FilterFunction_strIndexOf;
import org.geotools.filter.function.FilterFunction_strLength;
import org.geotools.filter.function.FilterFunction_strStartsWith;
import org.geotools.filter.function.FilterFunction_strSubstring;
import org.geotools.filter.function.FilterFunction_strSubstringStart;
import org.geotools.filter.function.FilterFunction_strToLowerCase;
import org.geotools.filter.function.FilterFunction_strToUpperCase;
import org.geotools.filter.function.FilterFunction_strTrim;
import org.geotools.filter.function.math.FilterFunction_abs;
import org.geotools.filter.function.math.FilterFunction_abs_2;
import org.geotools.filter.function.math.FilterFunction_abs_3;
import org.geotools.filter.function.math.FilterFunction_abs_4;
import org.geotools.filter.function.math.FilterFunction_ceil;
import org.geotools.util.logging.Logging;
import org.junit.Test;

/**
 * Base class for native function encoding tests
 *
 * @author Andrea Aime - GeoSolutions
 */
public abstract class JDBCFunctionOnlineTest extends JDBCTestSupport {
    static final Logger LOGGER = Logging.getLogger(JDBCFunctionOnlineTest.class);

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    SimpleFeatureSource fs;

    @Override
    protected void connect() throws Exception {
        super.connect();

        fs = dataStore.getFeatureSource(tname("ft1"));
    }

    @Test
    public void testStrfunc() throws IOException {
        assumeFalse(skipTests(FilterFunction_strConcat.class));

        Function func =
                ff.function("strConcat", ff.property(aname("stringProperty")), ff.literal("abc"));
        Filter filter = ff.equals(func, ff.literal("zeroabc"));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrfuncNumbers() throws IOException {
        assumeFalse(skipTests(FilterFunction_strConcat.class));

        Function func =
                ff.function(
                        "strConcat",
                        ff.property(aname("intProperty")),
                        ff.property(aname("intProperty")));
        Filter filter = ff.equals(func, ff.literal("11"));

        assertFeatures(fs, filter, tname("ft1") + ".1");
    }

    @Test
    public void testStrEndsWith() throws IOException {
        assumeFalse(skipTests(FilterFunction_strEndsWith.class));

        Function func =
                ff.function("strEndsWith", ff.property(aname("stringProperty")), ff.literal("o"));
        Filter filter = ff.equals(func, ff.literal(true));

        assertFeatures(fs, filter, tname("ft1") + ".0", tname("ft1") + ".2");
    }

    @Test
    public void testStrEndsWithOtherProperty() throws IOException {
        assumeFalse(skipTests(FilterFunction_strEndsWith.class));

        Function func =
                ff.function(
                        "strEndsWith",
                        ff.property(aname("doubleProperty")),
                        ff.property(aname("intProperty")));
        Filter filter = ff.equals(func, ff.literal(true));

        assertFeatures(fs, filter, tname("ft1") + ".0", tname("ft1") + ".1", tname("ft1") + ".2");
    }

    @Test
    public void testStrStartsWith() throws IOException {
        assumeFalse(skipTests(FilterFunction_strStartsWith.class));

        Function func =
                ff.function(
                        "strStartsWith", ff.property(aname("stringProperty")), ff.literal("ze"));
        Filter filter = ff.equals(func, ff.literal(true));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrStartsWithOtherProperty() throws IOException {
        assumeFalse(skipTests(FilterFunction_strStartsWith.class));

        Function func =
                ff.function(
                        "strStartsWith",
                        ff.property(aname("doubleProperty")),
                        ff.property(aname("intProperty")));
        Filter filter = ff.equals(func, ff.literal(true));

        assertFeatures(fs, filter, tname("ft1") + ".0", tname("ft1") + ".1", tname("ft1") + ".2");
    }

    @Test
    public void testStrIndexOf() throws IOException {
        assumeFalse(skipTests(FilterFunction_strIndexOf.class));

        Function func =
                ff.function("strIndexOf", ff.property(aname("stringProperty")), ff.literal("er"));
        Filter filter = ff.equals(func, ff.literal(1));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrLength() throws IOException {
        assumeFalse(skipTests(FilterFunction_strLength.class));

        Function func = ff.function("strLength", ff.property(aname("stringProperty")));
        Filter filter = ff.equals(func, ff.literal(4));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testLength() throws IOException {
        assumeFalse(skipTests(LengthFunction.class));

        Function func = ff.function("length", ff.property(aname("stringProperty")));
        Filter filter = ff.equals(func, ff.literal(4));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrLower() throws IOException {
        assumeFalse(skipTests(FilterFunction_strToLowerCase.class));

        Function func = ff.function("strToLowerCase", ff.property(aname("intProperty")));
        Filter filter = ff.equals(func, ff.literal("0"));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrUpper() throws IOException {
        assumeFalse(skipTests(FilterFunction_strToUpperCase.class));

        Function func = ff.function("strToUpperCase", ff.property(aname("stringProperty")));
        Filter filter = ff.equals(func, ff.literal("ZERO"));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrEqualsIgnoreCase() throws IOException {
        assumeFalse(skipTests(FilterFunction_strEqualsIgnoreCase.class));

        Function func =
                ff.function(
                        "strEqualsIgnoreCase",
                        ff.property(aname("stringProperty")),
                        ff.literal("ZeRo"));
        Filter filter = ff.equals(func, ff.literal(true));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrSubstring() throws IOException {
        assumeFalse(skipTests(FilterFunction_strSubstring.class));

        // intentionally mixing string and int literals
        Function func =
                ff.function(
                        "strSubstring",
                        ff.property(aname("stringProperty")),
                        ff.literal("1"),
                        ff.literal(3));
        Filter filter = ff.equals(func, ff.literal("er"));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrSubstringStart() throws IOException {
        assumeFalse(skipTests(FilterFunction_strSubstringStart.class));

        // intentionally mixing string and int literals
        Function func =
                ff.function(
                        "strSubstringStart", ff.property(aname("stringProperty")), ff.literal("1"));
        Filter filter = ff.equals(func, ff.literal("ero"));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testStrTrim() throws IOException {
        assumeFalse(skipTests(FilterFunction_strTrim.class));
        assumeFalse(skipTests(FilterFunction_strConcat.class));

        // intentionally mixing string and int literals
        Function func1 =
                ff.function("strConcat", ff.property(aname("stringProperty")), ff.literal("   "));
        Function func2 = ff.function("strTrim", func1);
        Filter filter = ff.equals(func2, ff.literal("zero"));

        assertFeatures(fs, filter, tname("ft1") + ".0");
    }

    @Test
    public void testAbs() throws IOException {
        assumeFalse(skipTests(FilterFunction_abs.class));

        // intentionally forcing a integer abs (makes no sense, but it's there...)
        Expression mul = ff.multiply(ff.property(aname("doubleProperty")), ff.literal(-1));
        Function func2 = ff.function("abs", mul);
        Filter filter = ff.equals(func2, ff.literal("1"));

        assertFeatures(fs, filter, tname("ft1") + ".1");
    }

    @Test
    public void testAbs2() throws IOException {
        assumeFalse(skipTests(FilterFunction_abs_2.class));

        // intentionally forcing a integer abs (makes no sense, but it's there...)
        Expression mul = ff.multiply(ff.property(aname("doubleProperty")), ff.literal(-1));
        Function func2 = ff.function("abs_2", mul);
        Filter filter = ff.equals(func2, ff.literal("1"));

        assertFeatures(fs, filter, tname("ft1") + ".1");
    }

    @Test
    public void testAbs3() throws IOException {
        assumeFalse(skipTests(FilterFunction_abs_3.class));

        // intentionally forcing a integer abs (makes no sense, but it's there...)
        Expression mul = ff.multiply(ff.property(aname("doubleProperty")), ff.literal(-1));
        Function func2 = ff.function("abs_3", mul);
        Filter filter = ff.greaterOrEqual(func2, ff.literal(1));

        assertFeatures(fs, filter, tname("ft1") + ".1", tname("ft1") + ".2");
    }

    @Test
    public void testAbs4() throws IOException {
        assumeFalse(skipTests(FilterFunction_abs_4.class));

        // intentionally forcing a integer abs (makes no sense, but it's there...)
        Expression mul = ff.multiply(ff.property(aname("doubleProperty")), ff.literal(-1));
        Function func2 = ff.function("abs_4", mul);
        Filter filter = ff.greaterOrEqual(func2, ff.literal(1));

        assertFeatures(fs, filter, tname("ft1") + ".1", tname("ft1") + ".2");
    }

    @Test
    public void testCeil() throws IOException {
        assumeFalse(skipTests(FilterFunction_ceil.class));

        Function func = ff.function("ceil", ff.property(aname("doubleProperty")));
        Filter filter = ff.equals(func, ff.literal(2));

        assertFeatures(fs, filter, tname("ft1") + ".1");
    }

    @Test
    public void testFloor() throws IOException {
        assumeFalse(skipTests(FilterFunction_ceil.class));

        Function func = ff.function("floor", ff.property(aname("doubleProperty")));
        Filter filter = ff.equals(func, ff.literal(1));

        assertFeatures(fs, filter, tname("ft1") + ".1");
    }

    void assertFeatures(SimpleFeatureSource fs2, Filter filter, String... ids) throws IOException {

        Set<String> idSet = new HashSet<>(Arrays.asList(ids));
        int count = 0;
        try (SimpleFeatureIterator fi = fs.getFeatures(filter).features()) {
            while (fi.hasNext()) {
                SimpleFeature sf = fi.next();
                assertTrue("Found unexpected id " + sf.getID(), idSet.contains(sf.getID()));
                count++;
            }
        }

        if (count != idSet.size()) {
            fail("Expected to find " + idSet.size() + " features, but was " + count);
        }
    }

    protected boolean skipTests(Class<?> fClass) {
        if (!dataStore.getFilterCapabilities().supports(fClass)) {
            LOGGER.log(
                    Level.INFO,
                    "Function {0} is not natively supported, skipping test",
                    fClass.getSimpleName());
            return true;
        }
        return false;
    }
}
