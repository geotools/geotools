package org.geotools.jdbc;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
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
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Base class for native function encoding tests
 * 
 * @author Andrea Aime - GeoSolutions
 */
public abstract class JDBCFunctionTest extends JDBCTestSupport {
    static final Logger LOGGER = Logging.getLogger(JDBCFunctionTest.class);
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    SimpleFeatureSource fs;
    
    @Override
    protected void connect() throws Exception {
        super.connect();
        
        fs = dataStore.getFeatureSource(tname("ft1"));
    }

    public void testStrfunc() throws IOException {
        if (skipTests(FilterFunction_strConcat.class)) {
            return;
        }
        
        Function func = ff.function("strConcat", 
                ff.property(aname("stringProperty")), ff.literal("abc"));
        Filter filter = ff.equals(func, ff.literal("zeroabc"));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrfuncNumbers() throws IOException {
        if (skipTests(FilterFunction_strConcat.class)) {
            return;
        }
        
        Function func = ff.function("strConcat", 
                ff.property(aname("intProperty")), ff.property(aname("intProperty")));
        Filter filter = ff.equals(func, ff.literal("11"));
        
        assertFeatures(fs, filter, tname("ft1") + ".1");
    }
    
    public void testStrEndsWith() throws IOException {
        if (skipTests(FilterFunction_strEndsWith.class)) {
            return;
        }
        
        Function func = ff.function("strEndsWith", 
                ff.property(aname("stringProperty")), ff.literal("o"));
        Filter filter = ff.equals(func, ff.literal(true));
        
        assertFeatures(fs, filter, tname("ft1") + ".0", tname("ft1") + ".2");
    }
    
    public void testStrEndsWithOtherProperty() throws IOException {
        if (skipTests(FilterFunction_strEndsWith.class)) {
            return;
        }
        
        Function func = ff.function("strEndsWith", 
                ff.property(aname("doubleProperty")), ff.property(aname("intProperty")));
        Filter filter = ff.equals(func, ff.literal(true));
        
        assertFeatures(fs, filter, tname("ft1") + ".0", tname("ft1") + ".1", tname("ft1") + ".2");
    }
    
    public void testStrStartsWith() throws IOException {
        if (skipTests(FilterFunction_strStartsWith.class)) {
            return;
        }
        
        Function func = ff.function("strStartsWith", 
                ff.property(aname("stringProperty")), ff.literal("ze"));
        Filter filter = ff.equals(func, ff.literal(true));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrStartsWithOtherProperty() throws IOException {
        if (skipTests(FilterFunction_strStartsWith.class)) {
            return;
        }
        
        Function func = ff.function("strStartsWith", 
                ff.property(aname("doubleProperty")), ff.property(aname("intProperty")));
        Filter filter = ff.equals(func, ff.literal(true));
        
        assertFeatures(fs, filter, tname("ft1") + ".0", tname("ft1") + ".1", tname("ft1") + ".2");
    }
    
    public void testStrIndexOf() throws IOException {
        if (skipTests(FilterFunction_strIndexOf.class)) {
            return;
        }
        
        Function func = ff.function("strIndexOf", 
                ff.property(aname("stringProperty")), ff.literal("er"));
        Filter filter = ff.equals(func, ff.literal(1));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrLength() throws IOException {
        if (skipTests(FilterFunction_strLength.class)) {
            return;
        }
        
        Function func = ff.function("strLength", 
                ff.property(aname("stringProperty")));
        Filter filter = ff.equals(func, ff.literal(4));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrLower() throws IOException {
        if (skipTests(FilterFunction_strToLowerCase.class)) {
            return;
        }
        
        Function func = ff.function("strToLowerCase", 
                ff.property(aname("intProperty")));
        Filter filter = ff.equals(func, ff.literal("0"));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrUpper() throws IOException {
        if (skipTests(FilterFunction_strToUpperCase.class)) {
            return;
        }
        
        Function func = ff.function("strToUpperCase", 
                ff.property(aname("stringProperty")));
        Filter filter = ff.equals(func, ff.literal("ZERO"));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrEqualsIgnoreCase() throws IOException {
        if (skipTests(FilterFunction_strEqualsIgnoreCase.class)) {
            return;
        }
        
        Function func = ff.function("strEqualsIgnoreCase", 
                ff.property(aname("stringProperty")), ff.literal("ZeRo"));
        Filter filter = ff.equals(func, ff.literal(true));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrSubstring() throws IOException {
        if (skipTests(FilterFunction_strSubstring.class)) {
            return;
        }
        
        // intentionally mixing string and int literals
        Function func = ff.function("strSubstring", 
                ff.property(aname("stringProperty")), ff.literal("1"), ff.literal(3));
        Filter filter = ff.equals(func, ff.literal("er"));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrSubstringStart() throws IOException {
        if (skipTests(FilterFunction_strSubstringStart.class)) {
            return;
        }
        
        // intentionally mixing string and int literals
        Function func = ff.function("strSubstringStart", 
                ff.property(aname("stringProperty")), ff.literal("1"));
        Filter filter = ff.equals(func, ff.literal("ero"));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testStrTrim() throws IOException {
        if (skipTests(FilterFunction_strTrim.class) || skipTests(FilterFunction_strConcat.class)) {
            return;
        }
        
        // intentionally mixing string and int literals
        Function func1 = ff.function("strConcat", ff.property(aname("stringProperty")), ff.literal("   "));
        Function func2 = ff.function("strTrim", func1);
        Filter filter = ff.equals(func2, ff.literal("zero"));
        
        assertFeatures(fs, filter, tname("ft1") + ".0");
    }
    
    public void testAbs() throws IOException {
        if (skipTests(FilterFunction_abs.class)) {
            return;
        }
        
        // intentionally forcing a integer abs (makes no sense, but it's there...)
        Expression mul = ff.multiply(ff.property(aname("doubleProperty")), ff.literal(-1));
        Function func2 = ff.function("abs", mul);
        Filter filter = ff.equals(func2, ff.literal("1"));
        
        assertFeatures(fs, filter, tname("ft1") + ".1");
    }
    
    public void testAbs2() throws IOException {
        if (skipTests(FilterFunction_abs_2.class)) {
            return;
        }
        
        // intentionally forcing a integer abs (makes no sense, but it's there...)
        Expression mul = ff.multiply(ff.property(aname("doubleProperty")), ff.literal(-1));
        Function func2 = ff.function("abs_2", mul);
        Filter filter = ff.equals(func2, ff.literal("1"));
        
        assertFeatures(fs, filter, tname("ft1") + ".1");
    }
    
    public void testAbs3() throws IOException {
        if (skipTests(FilterFunction_abs_3.class)) {
            return;
        }
        
        // intentionally forcing a integer abs (makes no sense, but it's there...)
        Expression mul = ff.multiply(ff.property(aname("doubleProperty")), ff.literal(-1));
        Function func2 = ff.function("abs_3", mul);
        Filter filter = ff.greaterOrEqual(func2, ff.literal(1));
        
        assertFeatures(fs, filter, tname("ft1") + ".1", tname("ft1") + ".2");
    }
    
    public void testAbs4() throws IOException {
        if (skipTests(FilterFunction_abs_4.class)) {
            return;
        }
        
        // intentionally forcing a integer abs (makes no sense, but it's there...)
        Expression mul = ff.multiply(ff.property(aname("doubleProperty")), ff.literal(-1));
        Function func2 = ff.function("abs_4", mul);
        Filter filter = ff.greaterOrEqual(func2, ff.literal(1));
        
        assertFeatures(fs, filter, tname("ft1") + ".1", tname("ft1") + ".2");
    }
    
    public void testCeil() throws IOException {
        if (skipTests(FilterFunction_ceil.class)) {
            return;
        }
        
        Function func = ff.function("ceil", ff.property(aname("doubleProperty")));
        Filter filter = ff.equals(func, ff.literal(2));
        
        assertFeatures(fs, filter, tname("ft1") + ".1");
    }
    
    public void testFloor() throws IOException {
        if (skipTests(FilterFunction_ceil.class)) {
            return;
        }
        
        Function func = ff.function("floor", ff.property(aname("doubleProperty")));
        Filter filter = ff.equals(func, ff.literal(1));
        
        assertFeatures(fs, filter, tname("ft1") + ".1");
    }
    
    void assertFeatures(SimpleFeatureSource fs2, Filter filter, String... ids) throws IOException {
        SimpleFeatureIterator fi = null;
        Set<String> idSet = new HashSet<String>(Arrays.asList(ids));
        int count = 0;
        try {
            fi = fs.getFeatures(filter).features();
            while(fi.hasNext()) {
                SimpleFeature sf = fi.next();
                assertTrue("Found unexpected id " + sf.getID(), idSet.contains(sf.getID()));
                count++;
            }
        } finally {
            if(fi != null) {
                fi.close();
            }
        }

        if(count != idSet.size()) {
            fail("Expected to find " + idSet.size() + " features, but was " + count);
        }
    }

    protected boolean skipTests(Class<?> fClass) {
        if (!dataStore.getFilterCapabilities().supports(fClass)) {
            LOGGER.log(Level.INFO, "Function {0} is not natively supported, skipping test", fClass
                    .getSimpleName());
            return true;
        }
        return false;
    }
    
}
