/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

public class JsonPointerFunctionTest {

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testSimplePointers() {
        String json =
                "{\n" //
                        + "    \"foo\" : [\"bar\", \"baz\"],\n" //
                        + "    \"nested\" : {\"a\" : 1, \"b\": 2},\n" //
                        + "    \"pi\" : 3.1416,\n" //
                        + "    \"v\" : 1234,\n" //
                        + "    \"t\" : true,\n" //
                        + "    \"f\" : false\n" //
                        + "}";

        assertEquals("bar", pointer(json, "/foo/0").evaluate(null));
        assertEquals("baz", pointer(json, "/foo/1").evaluate(null));
        assertEquals(Integer.valueOf(1), pointer(json, "/nested/a").evaluate(null));
        assertEquals(Integer.valueOf(2), pointer(json, "/nested/b").evaluate(null));
        assertEquals(3.1416, (Float) pointer(json, "/pi").evaluate(null), 0.001d);
        assertEquals(Integer.valueOf(1234), pointer(json, "/v").evaluate(null));
        assertEquals(Boolean.TRUE, pointer(json, "/t").evaluate(null));
        assertEquals(Boolean.FALSE, pointer(json, "/f").evaluate(null));
        assertNull(pointer(json, "/foobar").evaluate(null));
        assertNull(pointer(json, "/").evaluate(null));
    }

    @Test
    public void testNestArrayObjects() {
        String json =
                "{\n"
                        + "  \"prop\": {\n" //
                        + "    \"a\": [\n" //
                        + "      {\n" //
                        + "        \"b\": 10\n" //
                        + "      },\n" //
                        + "      {\n" //
                        + "        \"b\": 20\n" //
                        + "      }\n" //
                        + "    ]\n" //
                        + "  }\n" //
                        + "}";

        assertEquals(Integer.valueOf(10), pointer(json, "/prop/a/0/b").evaluate(null));
        assertEquals(Integer.valueOf(20), pointer(json, "/prop/a/1/b").evaluate(null));
    }

    @Test
    public void testExtractArray() {
        String json = "{\"foo\" : [\"bar\", \"baz\"]}";
        assertEquals("[\"bar\",\"baz\"]", pointer(json, "/foo").evaluate(null));
    }

    @Test
    public void testExtractObject() {
        String json = "{\"nested\" : {\"a\" : 1, \"b\": 2}}";
        assertEquals("{\"a\":1,\"b\":2}", pointer(json, "/nested").evaluate(null));
    }

    @Test
    public void testExtractComplex() {
        String json =
                "{\"menu\": {\n"
                        + "  \"id\": \"file\",\n"
                        + "  \"value\": \"File\",\n"
                        + "  \"popup\": {\n"
                        + "    \"menuitem\": [\n"
                        + "      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n"
                        + "      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n"
                        + "      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n"
                        + "    ]\n"
                        + "  }\n"
                        + "}}";

        assertEquals(
                "{\"menuitem\":[{\"value\":\"New\",\"onclick\":\"CreateNewDoc()\"},{\"value\":\"Open\",\"onclick\":\"OpenDoc()\"},{\"value\":\"Close\",\"onclick\":\"CloseDoc()\"}]}",
                pointer(json, "/menu/popup").evaluate(null));
        assertEquals(
                "[{\"value\":\"New\",\"onclick\":\"CreateNewDoc()\"},{\"value\":\"Open\",\"onclick\":\"OpenDoc()\"},{\"value\":\"Close\",\"onclick\":\"CloseDoc()\"}]",
                pointer(json, "/menu/popup/menuitem").evaluate(null));
    }

    public Function pointer(String json, String pointer) {
        return FF.function("jsonPointer", FF.literal(json), FF.literal(pointer));
    }
}
