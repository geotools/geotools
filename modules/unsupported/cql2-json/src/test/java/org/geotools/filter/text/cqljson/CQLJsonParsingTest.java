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
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Test;

public class CQLJsonParsingTest extends CQLJsonTest {
    @Test
    public void convertLikeTest() throws IOException, CQLException {
        // {"op":"like","args":[{"property":"eo:instrument"},"OLI%"]}
        Filter likeFilter = parse("{\"op\":\"like\",\"args\":[{\"property\":\"eo:instrument\"},\"OLI%\"]}");
        Assert.assertEquals("\"eo:instrument\" ILIKE 'OLI%'", ECQL.toCQL(likeFilter));
    }

    @Test
    public void convertBinaryEqualsTest() throws IOException, CQLException {
        // {"op":"=","args":[{"property":"scene_id"},"LC82030282019133LGN00"]}
        Filter equalsFilter = parse("{\"op\":\"=\",\"args\":[{\"property\":\"scene_id\"},\"LC82030282019133LGN00\"]}");
        Assert.assertEquals("scene_id = 'LC82030282019133LGN00'", ECQL.toCQL(equalsFilter));
    }

    @Test
    public void convertBinaryNotEqualsTest() throws IOException, CQLException {
        // {"op":"<>","args":[{"property":"scene_id"},"LC82030282019133LGN00"]}
        Filter equalsFilter = parse("{\"op\":\"<>\",\"args\":[{\"property\":\"scene_id\"},\"LC82030282019133LGN00\"]}");
        Assert.assertEquals("scene_id <> 'LC82030282019133LGN00'", ECQL.toCQL(equalsFilter));
    }

    @Test
    public void convertBinaryGreaterThanTest() throws IOException, CQLException {
        // {"op":">","args":[{"property":"count"},4]}
        Filter gtFilter = parse("{\"op\":\">\",\"args\":[{\"property\":\"count\"},4]}");
        Assert.assertEquals("count > 4", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBinaryLessThanTest() throws IOException, CQLException {
        // {"op":"<","args":[{"property":"count"},4]}
        Filter gtFilter = parse("{\"op\":\"<\",\"args\":[{\"property\":\"count\"},4]}\n");
        Assert.assertEquals("count < 4", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBinaryGreaterThanOrEqTest() throws IOException, CQLException {
        // {"op":">=","args":[{"property":"count"},4]}
        Filter gtFilter = parse("{\"op\":\">=\",\"args\":[{\"property\":\"count\"},4]}");
        Assert.assertEquals("count >= 4", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBinaryLessThanOrEqTest() throws IOException, CQLException {
        // {"op":"<=","args":[{"property":"count"},4]}
        Filter gtFilter = parse("{\"op\":\"<=\",\"args\":[{\"property\":\"count\"},4]}");
        Assert.assertEquals("count <= 4", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBetweenTest() throws IOException, CQLException {
        // {"op":"between","args":[{"property":"eo:cloud_cover"},0.1,0.2]}
        Filter gtFilter = parse("{\"op\":\"between\",\"args\":[{\"property\":\"eo:cloud_cover\"},0.1,0.2]}");
        Assert.assertEquals("\"eo:cloud_cover\" BETWEEN 0.1 AND 0.2", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertInTest() throws IOException, CQLException {
        // {"op":"in","args":[{"property":"landsat:wrs_path"},["153","154","155"]]}
        Filter gtFilter =
                parse("{\"op\":\"in\",\"args\":[{\"property\":\"landsat:wrs_path\"},[\"153\",\"154\",\"155\"]]}");
        Assert.assertEquals("\"landsat:wrs_path\" IN ('153','154','155')", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertOrTest() throws IOException, CQLException {
        // {"op":"or","args":[{"op":"=","args":[{"property":"ro:cloud_cover"},0.1]},{"op":"=","args":[{"property":"ro:cloud_cover"},0.2]}]}
        Filter gtFilter = parse(
                "{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"ro:cloud_cover\"},0.1]},{\"op\":\"=\",\"args\":[{\"property\":\"ro:cloud_cover\"},0.2]}]}");
        Assert.assertEquals("\"ro:cloud_cover\" IN (0.1,0.2)", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertAndTest() throws IOException, CQLException {
        // {"op":"and","args":[{"op":"<","args":[{"property":"ro:cloud_cover"},0.1]},{"op":"=","args":[{"property":"landsat:wrs_row"},28]},{"op":"=","args":[{"property":"landsat:wrs_path"},203]}]}
        Filter gtFilter = parse(
                "{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"ro:cloud_cover\"},0.1]},{\"op\":\"=\",\"args\":[{\"property\":\"landsat:wrs_row\"},28]},{\"op\":\"=\",\"args\":[{\"property\":\"landsat:wrs_path\"},203]}]}");
        Assert.assertEquals(
                "\"ro:cloud_cover\" < 0.1 AND \"landsat:wrs_row\" = 28 AND \"landsat:wrs_path\" = 203",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertIsNullTest() throws IOException, CQLException {
        // {"op":"isNull","args":[{"property":"scene_id"}]}
        Filter gtFilter = parse("{\"op\":\"isNull\",\"args\":[{\"property\":\"scene_id\"}]}");
        Assert.assertEquals("scene_id IS NULL", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertContainsTest() throws IOException, CQLException {
        // {"op":"s_contains","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter = parse(
                "{\"op\":\"s_contains\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "CONTAINS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertCrossesTest() throws IOException, CQLException {
        // {"op":"s_crosses","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter =
                parse("{\"op\":\"s_crosses\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "CROSSES(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertDisjointTest() throws IOException, CQLException {
        // {"op":"s_disjoint","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter = parse(
                "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "DISJOINT(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertSEqualsTest() throws IOException, CQLException {
        // {"op":"s_equals","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter =
                parse("{\"op\":\"s_equals\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "EQUALS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertIntersectsTest() throws IOException, CQLException {
        // {"op":"s_intersects","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter = parse(
                "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "INTERSECTS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertIntersectsGeoJsonTest() throws IOException, CQLException {
        // {"op":"s_intersects","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter = parse(
                "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "INTERSECTS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertOverlapsTest() throws IOException, CQLException {
        // {"op":"s_overlaps","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter = parse(
                "{\"op\":\"s_overlaps\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "OVERLAPS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertTouchesTest() throws IOException, CQLException {
        // {"op":"s_touches","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter =
                parse("{\"op\":\"s_touches\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "TOUCHES(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertWithinTest() throws IOException, CQLException {
        // {"op":"s_within","args":[{"property":"location"},{"bbox":[-118,33.8,-117.9,34]}]}
        Filter gtFilter =
                parse("{\"op\":\"s_within\",\"args\":[{\"property\":\"location\"},{\"bbox\":[-118,33.8,-117.9,34]}]}");
        Assert.assertEquals(
                "WITHIN(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertNotTest() throws IOException, CQLException {
        // {"op":"not","args":[{"op":"<","args":[{"property":"floors"},5]}]}
        Filter gtFilter = parse("{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"floors\"},5]}]}");
        Assert.assertEquals("NOT (floors < 5)", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertAfterTest() throws IOException, CQLException {
        // {"op":"t_after","args":[{"property":"built"},{"date":"2012-06-05"}]}
        Filter gtFilter = parse("{\"op\":\"t_after\",\"args\":[{\"property\":\"built\"},{\"date\":\"2012-06-05\"}]}");
        Assert.assertEquals("built AFTER '2012-06-05'", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBeforeTest() throws IOException, CQLException {
        // {"op":"t_before","args":[{"property":"built"},{"date":"2015-01-01"}]}
        Filter gtFilter = parse("{\"op\":\"t_before\",\"args\":[{\"property\":\"built\"},{\"date\":\"2015-01-01\"}]}");
        Assert.assertEquals("built BEFORE '2015-01-01'", ECQL.toCQL(gtFilter));
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
        Filter gtFilter = parse("{\"op\":\"t_equals\",\"args\":[{\"property\":\"built\"},{\"date\":\"2012-06-05\"}]}");
        Assert.assertEquals("built = '2012-06-05'", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertDuringTest() throws IOException, CQLException {
        // {"op":"t_during","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        Filter gtFilter = parse(
                "{\"op\":\"t_during\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
        Assert.assertEquals("updated DURING '[2017-06-10T07:30:00Z, 2017-06-10T07:30:00Z]'", ECQL.toCQL(gtFilter));
    }

    @Test(expected = CQLException.class)
    public void convertFinishedByTest() throws IOException, CQLException {
        // {"op":"t_finishedBy","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_finishedBy\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertFinishingTest() throws IOException, CQLException {
        // {"op":"t_finishing","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_finishing\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertTIntersectsTest() throws IOException, CQLException {
        // {"op":"t_intersects","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_intersects\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertMeetsTest() throws IOException, CQLException {
        // {"op":"t_meets","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_meets\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertMetByTest() throws IOException, CQLException {
        // {"op":"t_metBy","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_metBy\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertTOverlappedByTest() throws IOException, CQLException {
        // {"op":"t_overlappedBy","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_overlappedBy\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertTOverlapsTest() throws IOException, CQLException {
        // {"op":"t_overlaps","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_overlaps\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertStartedByTest() throws IOException, CQLException {
        // {"op":"t_startedBy","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_startedBy\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertStartsTest() throws IOException, CQLException {
        // {"op":"t_starts","args":[{"property":"updated"},{"interval":["2017-06-10T07:30:00Z","2017-06-11T10:30:00Z"]}]}
        parse(
                "{\"op\":\"t_starts\",\"args\":[{\"property\":\"updated\"},{\"interval\":[\"2017-06-10T07:30:00Z\",\"2017-06-11T10:30:00Z\"]}]}");
    }

    @Test(expected = CQLException.class)
    public void convertAContainedByTest() throws IOException, CQLException {
        // {"op":"a_containedBy","args":[{"property":"vat"},["ren","lozenge"]]}
        parse("{\"op\":\"a_containedBy\",\"args\":[{\"property\":\"vat\"},[\"ren\",\"lozenge\"]]}");
    }

    @Test(expected = CQLException.class)
    public void convertAContainsTest() throws IOException, CQLException {
        // {"op":"a_contains","args":[{"property":"vat"},["ren","lozenge"]]}
        parse("{\"op\":\"a_contains\",\"args\":[{\"property\":\"vat\"},[\"ren\",\"lozenge\"]]}");
    }

    @Test(expected = CQLException.class)
    public void convertAEqualsTest() throws IOException, CQLException {
        // {"op":"a_equals","args":[{"property":"vat"},["ren","lozenge"]]}
        parse("{\"op\":\"a_equals\",\"args\":[{\"property\":\"vat\"},[\"ren\",\"lozenge\"]]}");
    }

    @Test(expected = CQLException.class)
    public void convertAOverlapsTest() throws IOException, CQLException {
        // {"op":"a_overlaps","args":[{"property":"vat"},["ren","lozenge"]]}
        parse("{\"op\":\"a_overlaps\",\"args\":[{\"property\":\"vat\"},[\"ren\",\"lozenge\"]]}");
    }
}
