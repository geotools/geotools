/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc;

import static org.geotools.util.Converters.*;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import junit.framework.TestCase;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;

/**
 * Unit test for sql encoding of filters into where statements.
 *
 * @author Chris Holmes, TOPP
 * @author Saul Farber, MassGIS
 */
public class FilterToSQLTest extends TestCase {
    private FilterFactory filterFac = CommonFactoryFinder.getFilterFactory(null);
    private static Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(FilterToSQLTest.class);

    private SimpleFeatureType integerFType;
    private SimpleFeatureType stringFType;
    private SimpleFeatureType sqlDateFType;
    private SimpleFeatureType timestampFType;
    private SimpleFeatureType dateFType;
    private FilterToSQL encoder;
    private StringWriter output;

    public void setUp() throws Exception {
        Level debugLevel = Level.FINE;
        Locale.setDefault(new Locale("en", "US"));
        Logger log = LOGGER;
        while (log != null) {
            log.setLevel(debugLevel);
            for (int i = 0; i < log.getHandlers().length; i++) {
                Handler h = log.getHandlers()[i];
                h.setLevel(debugLevel);
            }
            log = log.getParent();
        }

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("testFeatureType");
        ftb.add("testAttr", Integer.class);
        integerFType = ftb.buildFeatureType();

        ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("testFeatureType");
        ftb.add("testAttr", java.sql.Date.class);
        sqlDateFType = ftb.buildFeatureType();

        ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("testFeatureType");
        ftb.add("testAttr", java.sql.Timestamp.class);
        timestampFType = ftb.buildFeatureType();

        ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("testFeatureType");
        ftb.add("testAttr", Date.class);
        dateFType = ftb.buildFeatureType();

        ftb.setName("testFeatureType");
        ftb.add("testAttr", String.class);
        stringFType = ftb.buildFeatureType();

        output = new StringWriter();
        encoder = new FilterToSQL(output);

        FIDMapper mapper =
                new FIDMapper() {

                    @Override
                    public boolean returnFIDColumnsAsAttributes() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public boolean isVolatile() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public boolean isValid(String fid) {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public boolean isAutoIncrement(int colIndex) {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public void initSupportStructures() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public boolean hasAutoIncrementColumns() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public Object[] getPKAttributes(String FID) throws IOException {
                        return new Object[] {FID};
                    }

                    @Override
                    public String getID(Object[] attributes) {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getColumnType(int colIndex) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public int getColumnSize(int colIndex) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public String getColumnName(int colIndex) {
                        return "id";
                    }

                    @Override
                    public int getColumnDecimalDigits(int colIndex) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public int getColumnCount() {
                        return 1;
                    }

                    @Override
                    public String createID(
                            Connection conn, SimpleFeature feature, Statement statement)
                            throws IOException {
                        // TODO Auto-generated method stub
                        return null;
                    }
                };
        encoder.setFIDMapper(mapper);
    }

    public void testIntegerContext() throws Exception {

        Expression literal = filterFac.literal(5);
        Expression prop =
                filterFac.property(integerFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);

        encoder.setFeatureType(integerFType);
        encoder.encode(filter);

        LOGGER.fine("testAttr is an Integer " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = 5");
    }

    public void testSqlDateContext() throws Exception {
        Expression literal = filterFac.literal("2002-12-03");
        Expression prop =
                filterFac.property(sqlDateFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);

        encoder.setFeatureType(sqlDateFType);
        encoder.encode(filter);

        LOGGER.fine(
                "testAttr is a java.sql.Date " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = '2002-12-03'");
    }

    public void testTimestampContext() throws Exception {
        Expression literal = filterFac.literal("2002-12-03 10:00");
        Expression prop =
                filterFac.property(timestampFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);

        encoder.setFeatureType(timestampFType);
        encoder.encode(filter);

        LOGGER.fine("testAttr is a Timestampa " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = '2002-12-03 10:00'");
    }

    public void testDateContext() throws Exception {
        Expression literal = filterFac.literal("2002-12-03 10:00");
        Expression prop =
                filterFac.property(dateFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);

        encoder.setFeatureType(dateFType);
        encoder.encode(filter);

        LOGGER.fine(
                "testAttr is a java.util.Date " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = '2002-12-03 10:00'");
    }

    public void testStringContext() throws Exception {
        Expression literal = filterFac.literal(5);
        Expression prop =
                filterFac.property(stringFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);

        encoder.setFeatureType(stringFType);
        encoder.encode(filter);

        LOGGER.fine("testAttr is a String " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = '5'");
    }

    public void testIntegerToNumberContext() throws Exception {

        Expression literal = filterFac.literal(5.0);
        Expression prop =
                filterFac.property(integerFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);

        encoder.setFeatureType(integerFType);
        encoder.encode(filter);

        LOGGER.fine("testAttr is an Integer " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = 5.0");
    }

    public void testInclude() throws Exception {
        encoder.encode(Filter.INCLUDE);
        assertEquals(output.getBuffer().toString(), "WHERE 1 = 1");
    }

    public void testExclude() throws Exception {
        encoder.encode(Filter.EXCLUDE);
        assertEquals(output.getBuffer().toString(), "WHERE 0 = 1");
    }

    public void testIdFilterMulti() throws Exception {
        Set<FeatureId> fids = new LinkedHashSet<FeatureId>();
        fids.add(filterFac.featureId("fid1"));
        fids.add(filterFac.featureId("fid2"));
        Id id = filterFac.id(fids);

        encoder.encode(id);
        assertEquals("WHERE ((id = 'fid1') OR (id = 'fid2'))", output.toString());
    }

    public void testIdFilterSingle() throws Exception {
        Set<FeatureId> fids = new LinkedHashSet<FeatureId>();
        fids.add(filterFac.featureId("fid1"));
        Id id = filterFac.id(fids);

        encoder.encode(id);
        assertEquals("WHERE (id = 'fid1')", output.toString());
    }

    public void testEscapeQuote() throws FilterToSQLException {
        PropertyIsEqualTo equals =
                filterFac.equals(filterFac.property("attribute"), filterFac.literal("A'A"));
        encoder.encode(equals);
        assertEquals("WHERE attribute = 'A''A'", output.toString());
    }

    public void testExpression() throws Exception {
        Add a = filterFac.add(filterFac.property("testAttr"), filterFac.literal(5));
        encoder.encode(a);
        assertEquals("testAttr + 5", output.toString());
    }

    public void testEscapeQuoteFancy() throws FilterToSQLException {
        org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Object fancyLiteral =
                new Object() {

                    public String toString() {
                        return "A'A";
                    }
                };
        PropertyIsEqualTo equals = ff.equals(ff.property("attribute"), ff.literal(fancyLiteral));
        StringWriter output = new StringWriter();
        FilterToSQL encoder = new FilterToSQL(output);
        encoder.encode(equals);
        assertEquals("WHERE attribute = 'A''A'", output.toString());
    }

    public void testNumberEscapes() throws Exception {
        Add a = filterFac.add(filterFac.property("testAttr"), filterFac.literal(5));
        PropertyIsEqualTo equal = filterFac.equal(filterFac.property("testAttr"), a, false);
        StringWriter output = new StringWriter();
        FilterToSQL encoder = new FilterToSQL(output);
        // this test must pass even when the target feature type is not known
        // encoder.setFeatureType(integerFType);
        encoder.encode(equal);
        assertEquals("WHERE testAttr = testAttr + 5", output.toString());
    }

    public void testInline() throws Exception {
        PropertyIsEqualTo equal =
                filterFac.equal(filterFac.property("testAttr"), filterFac.literal(5), false);
        StringWriter output = new StringWriter();
        FilterToSQL encoder = new FilterToSQL(output);
        encoder.setInline(true);

        encoder.encode(equal);
        assertEquals("testAttr = 5", output.toString());
    }

    public void testAfterInstant() throws Exception {
        Date date = convert("2002-12-03 10:00:00AM", Date.class);
        DefaultInstant instant = new DefaultInstant(new DefaultPosition(date));
        Expression literal = filterFac.literal(instant);
        Expression prop =
                filterFac.property(timestampFType.getAttributeDescriptors().get(0).getLocalName());
        PropertyIsEqualTo filter = filterFac.equals(prop, literal);

        encoder.setFeatureType(timestampFType);
        encoder.encode(filter);

        LOGGER.fine("testAttr is a Timestamp " + filter + " -> " + output.getBuffer().toString());
        assertEquals(output.getBuffer().toString(), "WHERE testAttr = '2002-12-03 10:00:00.0'");
    }

    public void testSimpleIn() throws FilterToSQLException {
        // straight
        assertEquals(encodeInComparison("in", true, "true", 1, 2), "WHERE testAttr IN (1, 2)");
        // double negation
        assertEquals(encodeInComparison("in", false, "false", 1, 2), "WHERE testAttr IN (1, 2)");
    }

    public void testMixedLogic() throws FilterToSQLException {
        assertEquals(encodeInComparison("in", true, "true", 1, 2), "WHERE testAttr IN (1, 2)");
        assertEquals(encodeInComparison("in", false, "false", 1, 2), "WHERE testAttr IN (1, 2)");
    }

    public void testIn2To10() throws FilterToSQLException {
        for (int i = 2; i <= 10; i++) {
            Object[] values = new Object[i];
            for (int j = 0; j < values.length; j++) {
                values[j] = j;
            }
            String commaSeparatedValues =
                    Arrays.stream(values)
                            .map(v -> String.valueOf(v))
                            .collect(Collectors.joining(", "));
            assertEquals(
                    encodeInComparison("in" + i, true, "true", values),
                    "WHERE testAttr IN (" + commaSeparatedValues + ")");
        }
    }

    public void testInWithLessThan() throws FilterToSQLException {
        FilterToSQL encoder = new FilterToSQL(output);

        Function function = buildInFunction("in", new Object[] {1, 2});
        Filter filter = filterFac.less(function, filterFac.literal(true));
        encoder.encode(filter);

        // weird but legit, at least in some databases
        assertEquals("WHERE (testAttr IN (1, 2)) < true", output.getBuffer().toString());
    }

    public String encodeInComparison(
            String functionName, boolean equality, String literal, Object... valueList)
            throws FilterToSQLException {
        FilterToSQL encoder = new FilterToSQL(output);

        Function function = buildInFunction(functionName, valueList);
        Filter filter;
        if (equality) {
            filter = filterFac.equal(function, filterFac.literal(literal), true);
        } else {
            filter = filterFac.notEqual(function, filterFac.literal(literal), true);
        }
        encoder.encode(filter);

        String result = output.getBuffer().toString();
        output.getBuffer().setLength(0);
        return result;
    }

    public Function buildInFunction(String functionName, Object[] valueList) {
        Stream<Literal> values = Arrays.stream(valueList).map(v -> filterFac.literal(v));
        Stream<PropertyName> property = Stream.of(filterFac.property("testAttr"));
        Expression[] literals = Stream.concat(property, values).toArray(i -> new Expression[i]);
        return filterFac.function(functionName, literals);
    }
}
