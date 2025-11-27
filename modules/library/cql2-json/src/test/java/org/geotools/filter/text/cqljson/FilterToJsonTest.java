/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cqljson;

import java.io.IOException;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Test;
import tools.jackson.databind.JsonNode;

public class FilterToJsonTest extends CQLJsonTest {
    @Test
    public void convertLikeTest() throws IOException, CQLException {
        // {"op":"like","args":[{"property":"eo:instrument"},"OLI%"]}
        String line = "{\"op\":\"like\",\"args\":[{\"property\":\"eo:instrument\"},\"OLI%\"]}";
        Filter likeFilter = parse(line);
        Assert.assertEquals("\"eo:instrument\" ILIKE 'OLI%'", ECQL.toCQL(likeFilter));
        JsonNode node = serialize(likeFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertBinaryEqualsTest() throws IOException, CQLException {
        // {"op":"=","args":[{"property":"scene_id"},"LC82030282019133LGN00"]}
        String line = "{\"op\":\"=\",\"args\":[{\"property\":\"scene_id\"},\"LC82030282019133LGN00\"]}";
        Filter equalsFilter = parse(line);
        Assert.assertEquals("scene_id = 'LC82030282019133LGN00'", ECQL.toCQL(equalsFilter));
        JsonNode node = serialize(equalsFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertBinaryNotEqualsTest() throws IOException, CQLException {
        // {"op":"<>","args":[{"property":"scene_id"},"LC82030282019133LGN00"]}
        String line = "{\"op\":\"<>\",\"args\":[{\"property\":\"scene_id\"},\"LC82030282019133LGN00\"]}";
        Filter notEqualsFilter = parse(line);
        Assert.assertEquals("scene_id <> 'LC82030282019133LGN00'", ECQL.toCQL(notEqualsFilter));
        JsonNode node = serialize(notEqualsFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertBinaryGreaterThanTest() throws IOException, CQLException {
        // {"op":">","args":[{"property":"count"},4]}
        String line = "{\"op\":\">\",\"args\":[{\"property\":\"count\"},4]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals("count > 4", ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertBinaryLessThanTest() throws IOException, CQLException {
        // {"op":"<","args":[{"property":"count"},4]}
        String line = "{\"op\":\"<\",\"args\":[{\"property\":\"count\"},4]}";
        Filter ltFilter = parse(line);
        Assert.assertEquals("count < 4", ECQL.toCQL(ltFilter));
        JsonNode node = serialize(ltFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertBinaryGreaterThanOrEqTest() throws IOException, CQLException {
        // {"op":">=","args":[{"property":"count"},4]}
        String line = "{\"op\":\">=\",\"args\":[{\"property\":\"count\"},4]}";
        Filter gteFilter = parse(line);
        Assert.assertEquals("count >= 4", ECQL.toCQL(gteFilter));
        JsonNode node = serialize(gteFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertBinaryLessThanOrEqTest() throws IOException, CQLException {
        // {"op":"<=","args":[{"property":"count"},4]}
        String line = "{\"op\":\"<=\",\"args\":[{\"property\":\"count\"},4]}";
        Filter lteFilter = parse(line);
        Assert.assertEquals("count <= 4", ECQL.toCQL(lteFilter));
        JsonNode node = serialize(lteFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertBetweenTest() throws IOException, CQLException {
        // {"op":"between","args":[{"property":"eo:cloud_cover"},0.1,0.2]}
        String line = "{\"op\":\"between\",\"args\":[{\"property\":\"eo:cloud_cover\"},0.1,0.2]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals("\"eo:cloud_cover\" BETWEEN 0.1 AND 0.2", ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertInTest() throws IOException, CQLException {
        // {"op":"in","args":[{"property":"landsat:wrs_path"},["153","154","155"]]}
        String line = "{\"op\":\"in\",\"args\":[{\"property\":\"landsat:wrs_path\"},[\"153\",\"154\",\"155\"]]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals("\"landsat:wrs_path\" IN ('153','154','155')", ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertOrTest() throws IOException, CQLException {
        // {"op":"or","args":[{"op":"=","args":[{"property":"ro:cloud_cover"},0.1]},{"op":"=","args":[{"property":"ro:cloud_cover"},0.2]}]}
        String line =
                "{\"op\":\"or\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"ro:cloud_cover\"},0.1]},{\"op\":\"=\",\"args\":[{\"property\":\"ro:cloud_cover\"},0.2]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals("\"ro:cloud_cover\" > 0.1 OR \"ro:cloud_cover\" = 0.2", ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertAndTest() throws IOException, CQLException {
        // {"op":"and","args":[{"op":"<","args":[{"property":"ro:cloud_cover"},0.1]},{"op":"=","args":[{"property":"landsat:wrs_row"},28]},{"op":"=","args":[{"property":"landsat:wrs_path"},203]}]}
        String line =
                "{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"ro:cloud_cover\"},0.1]},{\"op\":\"=\",\"args\":[{\"property\":\"landsat:wrs_row\"},28]},{\"op\":\"=\",\"args\":[{\"property\":\"landsat:wrs_path\"},203]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "\"ro:cloud_cover\" < 0.1 AND \"landsat:wrs_row\" = 28 AND \"landsat:wrs_path\" = 203",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertIsNullTest() throws IOException, CQLException {
        // {"op":"isNull","args":[{"property":"scene_id"}]}
        String line = "{\"op\":\"isNull\",\"args\":[{\"property\":\"scene_id\"}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals("scene_id IS NULL", ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertContainsTest() throws IOException, CQLException {
        // {"op":"s_contains","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_contains\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "CONTAINS(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        // BBOX becomes a Polygon when converting to filter, so conversion back gets you the polygon
        // equivalent
        String line2 =
                "{\"op\":\"s_contains\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Assert.assertEquals(line2, node.toString());
    }

    @Test
    public void convertCrossesTest() throws IOException, CQLException {
        // {"op":"s_crosses","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_crosses\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "CROSSES(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertDisjointTest() throws IOException, CQLException {
        // {"op":"s_disjoint","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "DISJOINT(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertSEqualsTest() throws IOException, CQLException {
        // {"op":"s_equals","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_equals\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "EQUALS(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertIntersectsGeoJsonTest() throws IOException, CQLException {
        // {"op":"s_intersects","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "INTERSECTS(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertIntersectsTest() throws IOException, CQLException {
        // {"op":"s_intersects","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "INTERSECTS(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        // Conversion to filter converts bbox to Polygon, so converting back returns polygon GeoJSON
        String line2 =
                "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Assert.assertEquals(line2, node.toString());
    }

    @Test
    public void convertFunctionExpressionTest() throws Exception {
        // {"op":"and","args":[{"op":"<","args":[{"property":"ro:cloud_cover"},{"function":{"name":"tan","args":[1]}}]},
        // {"op":"=","args":[{"property":"landsat:wrs_row"},28]},
        // {"op":"=","args":[{"property":"landsat:wrs_path"},203]}]}
        String line =
                "{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"ro:cloud_cover\"},{\"function\":{\"name\":\"tan\",\"args\":[1]}}]},"
                        + "{\"op\":\"=\",\"args\":[{\"property\":\"landsat:wrs_row\"},28]},"
                        + "{\"op\":\"=\",\"args\":[{\"property\":\"landsat:wrs_path\"},203]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "\"ro:cloud_cover\" < tan(1) AND \"landsat:wrs_row\" = 28 AND \"landsat:wrs_path\" = 203",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertOverlapsTest() throws IOException, CQLException {
        // {"op":"s_overlaps","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_overlaps\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "OVERLAPS(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertTouchesTest() throws IOException, CQLException {
        // {"op":"s_touches","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_touches\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "TOUCHES(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertWithinTest() throws IOException, CQLException {
        // {"op":"s_within","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        String line =
                "{\"op\":\"s_within\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118.0,33.8,-117.9,34.0]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(
                "WITHIN(location, POLYGON ((-118 33.8, -117.9 33.8, -117.9 34, -118 34, -118 33.8)))",
                ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertNotTest() throws IOException, CQLException {
        // {"op":"not","args":[{"op":"<","args":[{"property":"floors"},5]}]}
        String line = "{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"floors\"},5]}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals("NOT (floors < 5)", ECQL.toCQL(gtFilter));
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertTimestampFilter() throws Exception {
        String line = "{\"op\":\"=\",\"args\":[{\"property\":\"date\"},{\"timestamp\":\"2022-04-16T00:00:00Z\"}]}";
        Filter gtFilter = parse(line);
        Assert.assertEquals(CQL.toFilter("date = '2022-04-16T00:00:00Z'"), gtFilter);
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertDateFilter() throws Exception {
        String line = "{\"op\":\"=\",\"args\":[{\"property\":\"date\"},{\"date\":\"2022-04-16\"}]}";
        Filter gtFilter = parse(line);
        JsonNode node = serialize(gtFilter);
        Assert.assertEquals(line, node.toString());
    }

    @Test
    public void convertAfterTest() throws IOException, CQLException {
        // {"op":"t_after","args":[{"property":"built"},{"date":"2012-06-05"}]}
        Filter gtFilter = parse(
                "{\"op\":\"t_after\",\"args\":[{\"property\":\"built\"},{\"timestamp\":\"2012-06-05T00:00:00Z\"}]}");
        Assert.assertEquals(ECQL.toFilter("built AFTER 2012-06-05T00:00:00Z"), gtFilter);
    }

    @Test
    public void convertBeforeTest() throws IOException, CQLException {
        // {"op":"t_before","args":[{"property":"built"},{"date":"2015-01-01"}]}
        Filter gtFilter = parse(
                "{\"op\":\"t_before\",\"args\":[{\"property\":\"built\"},{\"timestamp\":\"2015-01-01T00:00:00Z\"}]}");
        Assert.assertEquals(ECQL.toFilter("built BEFORE 2015-01-01T00:00:00Z"), gtFilter);
    }

    @Test
    public void convertTDisjointTest() throws IOException, CQLException {
        // {"op":"t_disjoint","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        Filter gtFilter = parse(
                "{\"op\":\"t_disjoint\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
        Assert.assertEquals(
                "updated BEFORE '[2017-06-10T07:30:00Z, 2017-06-10T07:30:00Z]' AND updated AFTER '[2017-06-10T07:30:00Z, 2017-06-10T07:30:00Z]'",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertTEqualsTest() throws IOException, CQLException {
        // {"op":"t_equals","args":[{"property":"built"},{"date":"2012-06-05"}]}
        Filter gtFilter = parse(
                "{\"op\":\"t_equals\",\"args\":[{\"property\":\"built\"},{\"timestamp\":\"2012-06-05T00:00:00Z\"}]}");
        Assert.assertEquals(ECQL.toFilter("built = '2012-06-05T00:00:00Z'"), gtFilter);
    }

    @Test
    public void convertDuringTest() throws IOException, CQLException {
        // {"op":"t_during","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        Filter gtFilter = parse(
                "{\"op\":\"t_during\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
        Assert.assertEquals("updated DURING '[2017-06-10T07:30:00Z, 2017-06-10T07:30:00Z]'", ECQL.toCQL(gtFilter));
    }
}
