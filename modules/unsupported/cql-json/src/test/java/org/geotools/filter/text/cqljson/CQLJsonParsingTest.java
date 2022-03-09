package org.geotools.filter.text.cqljson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;

public class CQLJsonParsingTest {
    @Test
    public void convertLikeTest() throws IOException, CQLException {
        Filter likeFilter = parse("cqlJsonTest.json", 6);
        Assert.assertEquals("\"eo:instrument\" ILIKE 'OLI%'", ECQL.toCQL(likeFilter));
    }

    @Test
    public void convertBinaryEqualsTest() throws IOException, CQLException {
        Filter equalsFilter = parse("cqlJsonTest.json", 0);
        Assert.assertEquals("scene_id = 'LC82030282019133LGN00'", ECQL.toCQL(equalsFilter));
    }

    @Test
    public void convertBinaryNotEqualsTest() throws IOException, CQLException {
        Filter equalsFilter = parse("cqlJsonTest.json", 1);
        Assert.assertEquals("scene_id <> 'LC82030282019133LGN00'", ECQL.toCQL(equalsFilter));
    }

    @Test
    public void convertBinaryGreaterThanTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 2);
        Assert.assertEquals("count > 4", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBinaryLessThanTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 3);
        Assert.assertEquals("count < 4", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBinaryGreaterThanOrEqTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 4);
        Assert.assertEquals("count >= 4", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBinaryLessThanOrEqTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 5);
        Assert.assertEquals("count <= 4", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBetweenTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 7);
        Assert.assertEquals("\"eo:cloud_cover\" BETWEEN 0.1 AND 0.2", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertInTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 8);
        Assert.assertEquals("\"landsat:wrs_path\" IN ('153','154','155')", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertOrTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 10);
        Assert.assertEquals("\"ro:cloud_cover\" IN (0.1,0.2)", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertAndTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 9);
        Assert.assertEquals(
                "\"ro:cloud_cover\" < 0.1 AND \"landsat:wrs_row\" = 28 AND \"landsat:wrs_path\" = 203",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertIsNullTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 11);
        Assert.assertEquals("scene_id IS NULL", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertContainsTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 12);
        Assert.assertEquals(
                "CONTAINS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertCrossesTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 13);
        Assert.assertEquals(
                "CROSSES(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertDisjointTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 14);
        Assert.assertEquals(
                "DISJOINT(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertSEqualsTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 15);
        Assert.assertEquals(
                "EQUALS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertIntersectsTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 16);
        Assert.assertEquals(
                "INTERSECTS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertIntersectsGeoJsonTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 20);
        Assert.assertEquals(
                "INTERSECTS(location, POLYGON ((-10 -10, 10 -10, 10 10, -10 -10)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertOverlapsTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 17);
        Assert.assertEquals(
                "OVERLAPS(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertTouchesTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 18);
        Assert.assertEquals(
                "TOUCHES(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertWithinTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 19);
        Assert.assertEquals(
                "WITHIN(location, POLYGON ((-118 33.8, -118 34, -117.9 34, -117.9 33.8, -118 33.8)))",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertNotTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 21);
        Assert.assertEquals("NOT (floors < 5)", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertAfterTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 23);
        Assert.assertEquals("built AFTER '2012-06-05'", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertBeforeTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 22);
        Assert.assertEquals("built BEFORE '2015-01-01'", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertTDisjointTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 24);
        Assert.assertEquals(
                "updated BEFORE '[2017-06-10T07:30:00Z, 2017-06-10T07:30:00Z]' AND updated AFTER '[2017-06-10T07:30:00Z, 2017-06-10T07:30:00Z]'",
                ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertTEqualsTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 26);
        Assert.assertEquals("built = '2012-06-05'", ECQL.toCQL(gtFilter));
    }

    @Test
    public void convertDuringTest() throws IOException, CQLException {
        Filter gtFilter = parse("cqlJsonTest.json", 25);
        Assert.assertEquals(
                "updated DURING '[2017-06-10T07:30:00Z, 2017-06-10T07:30:00Z]'",
                ECQL.toCQL(gtFilter));
    }

    @Test(expected = CQLException.class)
    public void convertFinishedByTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 27);
    }

    @Test(expected = CQLException.class)
    public void convertFinishingTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 28);
    }

    @Test(expected = CQLException.class)
    public void convertTIntersectsTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 29);
    }

    @Test(expected = CQLException.class)
    public void convertMeetsTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 30);
    }

    @Test(expected = CQLException.class)
    public void convertMetByTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 31);
    }

    @Test(expected = CQLException.class)
    public void convertTOverlappedByTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 32);
    }

    @Test(expected = CQLException.class)
    public void convertTOverlapsTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 33);
    }

    @Test(expected = CQLException.class)
    public void convertStartedByTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 34);
    }

    @Test(expected = CQLException.class)
    public void convertStartsTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 35);
    }

    @Test(expected = CQLException.class)
    public void convertAContainedByTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 36);
    }

    @Test(expected = CQLException.class)
    public void convertAContainsTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 37);
    }

    @Test(expected = CQLException.class)
    public void convertAEqualsTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 38);
    }

    @Test(expected = CQLException.class)
    public void convertAOverlapsTest() throws IOException, CQLException {
        parse("cqlJsonTest.json", 39);
    }

    private Filter parse(String file, Integer lineNumber) throws IOException, CQLException {
        ClassLoader classLoader = getClass().getClassLoader();
        File fileHandle = new File(classLoader.getResource(file).getFile());
        List<String> lines = Files.readAllLines(fileHandle.toPath(), StandardCharsets.UTF_8);
        CQLJsonCompiler cqlJsonCompiler =
                new CQLJsonCompiler(lines.get(lineNumber), new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        return cqlJsonCompiler.getFilter();
    }
}
