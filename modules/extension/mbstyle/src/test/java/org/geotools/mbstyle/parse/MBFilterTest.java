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

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Set;
import org.geotools.filter.text.ecql.ECQL;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.style.SemanticType;

public class MBFilterTest {

    /**
     * Parse JSONArray ' rather than " for faster test case writing.
     *
     * @return parsed JSONArray
     */
    private JSONArray array(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        String text = json.replace('\'', '\"');
        Object object = parser.parse(text);
        return (JSONArray) object;
    }

    @Test
    public void mixed() throws ParseException {
        JSONArray json =
                array(
                        "['all',['==', 'class', 'street_limited'],['>=', 'admin_level', 3],['!in', '$type', 'Polygon']]");
        MBFilter mbfilter = new MBFilter(json);
        Set<SemanticType> types = mbfilter.semanticTypeIdentifiers();
        assertTrue(!types.contains(SemanticType.POLYGON));
        Filter filter = mbfilter.filter();
        assertEquals(
                "class = 'street_limited' AND admin_level >= 3 AND NOT ((dimension(geometry()) = 2 AND NOT (isCoverage() = true)))",
                ECQL.toCQL(filter));
    }

    @Test
    public void type() throws ParseException {
        JSONArray json;
        MBFilter mbfilter;
        Filter filter;
        Set<SemanticType> types;

        // common $type examples
        json = array("['in', '$type','LineString']");
        mbfilter = new MBFilter(json);
        types = mbfilter.semanticTypeIdentifiers();
        assertTrue(types.contains(SemanticType.LINE) && types.size() == 1);
        filter = mbfilter.filter();
        assertEquals("dimension(geometry()) IN (1)", ECQL.toCQL(filter));

        json = array("['in', '$type','Polygon']");
        mbfilter = new MBFilter(json);
        types = mbfilter.semanticTypeIdentifiers();
        assertTrue(types.contains(SemanticType.POLYGON) && types.size() == 1);
        filter = mbfilter.filter();
        assertEquals(
                "(dimension(geometry()) = 2 AND NOT (isCoverage() = true))", ECQL.toCQL(filter));

        json = array("['==', '$type','Point']");
        mbfilter = new MBFilter(json);
        types = mbfilter.semanticTypeIdentifiers();
        assertTrue(types.contains(SemanticType.POINT) && types.size() == 1);
        filter = mbfilter.filter();
        assertEquals("dimension(geometry()) = 0", ECQL.toCQL(filter));

        json = array("['in', '$type','Point', 'LineString']");
        mbfilter = new MBFilter(json, null);
        types = mbfilter.semanticTypeIdentifiers();
        assertTrue(
                types.contains(SemanticType.POINT)
                        && types.contains(SemanticType.LINE)
                        && types.size() == 2);
        filter = mbfilter.filter();
        assertEquals("dimension(geometry()) IN (0,1)", ECQL.toCQL(filter));
        try {
            json = array("['==', '$type','Point', 'LineString']");
            mbfilter = new MBFilter(json, null);
            types = mbfilter.semanticTypeIdentifiers();
            fail("expected format exception due to '==' having too many arguments above");
        } catch (MBFormatException expected) {
        }

        // not
        json = array("['!in', '$type','Point', 'LineString']");
        mbfilter = new MBFilter(json, null);
        types = mbfilter.semanticTypeIdentifiers();
        assertTrue(types.contains(SemanticType.POLYGON) && !types.contains(SemanticType.LINE));
        filter = mbfilter.filter();
        assertEquals("NOT (dimension(geometry()) IN (0,1))", ECQL.toCQL(filter));

        // test default handling
        json = array("['==', 'key', 'value']");
        mbfilter = new MBFilter(json);
        types = mbfilter.semanticTypeIdentifiers();
        assertTrue(types.isEmpty());

        json = array("['!=', 'key', 'value']");
        mbfilter = new MBFilter(json, null, SemanticType.LINE);
        types = mbfilter.semanticTypeIdentifiers();
        assertTrue(types.contains(SemanticType.LINE) && types.size() == 1);
    }

    @Test
    public void id() throws ParseException {
        JSONArray json;
        MBFilter mbfilter;
        Filter filter;

        json = array("['has', '$id','foo.1']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("IN ('foo.1')", ECQL.toCQL(filter));

        json = array("['in', '$id','foo.1','foo.2']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("IN ('foo.1','foo.2')", ECQL.toCQL(filter));

        json = array("['!has', '$id','foo.1']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("NOT (IN ('foo.1'))", ECQL.toCQL(filter));

        json = array("['!in', '$id','foo.1','foo.2']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("NOT (IN ('foo.1','foo.2'))", ECQL.toCQL(filter));
    }

    @Test
    public void existential() throws ParseException {
        JSONArray json = array("['has', 'key']");

        MBFilter mbfilter = new MBFilter(json);
        Filter filter = mbfilter.filter();
        assertEquals("NOT (key IS NULL)", ECQL.toCQL(filter));

        json = array("['!has', 'key', 'value']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("key IS NULL", ECQL.toCQL(filter));
    }

    @Test
    public void comparisonFilters() throws ParseException {
        JSONArray json;
        MBFilter mbfilter;

        // being really quick here, no need to check null / instanceof if we just cast
        json = array("['==', 'key', 'value']");
        mbfilter = new MBFilter(json);
        PropertyIsEqualTo equal = (PropertyIsEqualTo) mbfilter.filter();
        assertEquals("key", ((PropertyName) equal.getExpression1()).getPropertyName());
        assertEquals("value", ((Literal) equal.getExpression2()).getValue());

        // okay that takes too long just check ECQL
        assertEquals("key = 'value'", ECQL.toCQL(equal));

        json = array("['!=', 'key', 'value']");
        mbfilter = new MBFilter(json);
        Filter filter = mbfilter.filter();
        assertEquals("key <> 'value'", ECQL.toCQL(filter));

        json = array("['>', 'key', 'value']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("key > 'value'", ECQL.toCQL(filter));

        json = array("['<', 'key', 'value']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("key < 'value'", ECQL.toCQL(filter));

        json = array("['>=', 'key', 'value']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("key >= 'value'", ECQL.toCQL(filter));

        json = array("['<=', 'key', 'value']");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("key <= 'value'", ECQL.toCQL(filter));
    }

    @Test
    public void membership() throws ParseException {
        JSONArray json = array("['in', 'a', 1, 2, 3]");

        MBFilter mbfilter = new MBFilter(json);
        Filter filter = mbfilter.filter();
        // System.out.println(ECQL.toCQL(filter));
        assertEquals("in(a,1,2,3) = true", ECQL.toCQL(filter));

        json = array("['!in', 'a', 1, 2, 3]");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("in(a,1,2,3) = false", ECQL.toCQL(filter));
    }

    @Test
    public void combining() throws ParseException {
        JSONArray json = array("['all', ['==','a',1],['==','b',2]]");
        MBFilter mbfilter = new MBFilter(json);
        Filter filter = mbfilter.filter();
        assertEquals("a = 1 AND b = 2", ECQL.toCQL(filter));

        json = array("['any', ['==','a',1],['==','b',2]]");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("a = 1 OR b = 2", ECQL.toCQL(filter));

        json = array("['none', ['==','a',1],['==','b',2]]");
        mbfilter = new MBFilter(json);
        filter = mbfilter.filter();
        assertEquals("NOT (a = 1) AND NOT (b = 2)", ECQL.toCQL(filter));
    }

    @Test
    public void nestedAllSemanticIdentifiers() throws ParseException {
        JSONArray json =
                array(
                        "[\n"
                                + "        'all',\n"
                                + "        ['==', '$type', 'LineString'],\n"
                                + "        ['all', ['==', 'brunnel', 'tunnel'], ['==', 'class', 'path']]\n"
                                + "      ]");
        MBFilter mbfilter = new MBFilter(json);
        // used to go in infinite recursion
        Set<SemanticType> identifiers = mbfilter.semanticTypeIdentifiers();
        assertEquals(Collections.singleton(SemanticType.LINE), identifiers);
    }
}
