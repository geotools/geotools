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
    public void convertJsonToPredicates() throws IOException, CQLException {
        // {"and":[{"eq":{"property":"swimming_pool","value":true}},{"or":[{"gt":{"property":"floor","value":5}},{"like":{"property":"material","value":"brick%"}},{"like":{"property":"material","value":"%brick"}}]}]}
        Filter filter = parse("cqlJsonTest.json", 0);
        Assert.assertEquals(
                "swimming_pool = true AND (floor > 5 OR material ILIKE 'brick%' OR material ILIKE '%brick')",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertAndIntersects() throws IOException, CQLException {
        // {"and":[{"eq":{"property":"beamMode","value":"ScanSAR
        // Narrow"}},{"eq":{"property":"swathDirection","value":"ascending"}},{"eq":{"property":"polarization","value":"HH+VV+HV+VH"}},{"intersects":{"property":"footprint","value":{"type":"Polygon","coordinates":[[[-77.117938,38.93686],[-77.040604,39.995648],[-76.910536,38.892912],[-77.039359,38.791753],[-77.047906,38.841462],[-77.034183,38.840655],[-77.033142,38.85749]]]}}}]}
        Filter filter = parse("cqlJsonTest.json", 1);
        Assert.assertEquals(
                "beamMode = 'ScanSAR Narrow' AND swathDirection = 'ascending' AND polarization = 'HH+VV+HV+VH' AND INTERSECTS(footprint, POLYGON ((-77.117938 38.93686, -77.040604 39.995648, -76.910536 38.892912, -77.039359 38.791753, -77.047906 38.841462, -77.034183 38.840655, -77.033142 38.85749, -77.117938 38.93686)))",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertLTE() throws IOException, CQLException {
        // {"lte":{"property":"taxes","value":500}}
        Filter filter = parse("cqlJsonTest.json", 2);
        Assert.assertEquals("taxes <= 500", ECQL.toCQL(filter));
    }

    @Test
    public void convertNotLTE() throws IOException, CQLException {
        // {"not":{"lte":{"property":"taxes","value":500}}}
        Filter filter = parse("cqlJsonTest.json", 3);
        Assert.assertEquals("NOT (taxes <= 500)", ECQL.toCQL(filter));
    }

    @Test
    public void convertAndLTE() throws IOException, CQLException {
        // {"and":[{"not":{"lte":{"property":"taxes","value":500}}},{"lte":{"property":"taxes","value":800}}]}
        Filter filter = parse("cqlJsonTest.json", 4);
        Assert.assertEquals("NOT (taxes <= 500) AND taxes <= 800", ECQL.toCQL(filter));
    }

    @Test
    public void convertCompoundAndLTE() throws IOException, CQLException {
        // {"and":[{"lte":{"property":"taxes","value":500}},{"and":[{"lte":{"property":"taxes","value":600}},{"lte":{"property":"taxes","value":400}}]}]}
        Filter filter = parse("cqlJsonTest.json", 5);
        Assert.assertEquals("taxes <= 500 AND (taxes <= 600 AND taxes <= 400)", ECQL.toCQL(filter));
    }

    @Test
    public void convertLike() throws IOException, CQLException {
        // {"like":{"property":"owner","value":"% Jones %"}}
        Filter filter = parse("cqlJsonTest.json", 6);
        Assert.assertEquals("owner ILIKE '% Jones %'", ECQL.toCQL(filter));
    }

    @Test
    public void convertStartsWithLike() throws IOException, CQLException {
        // {"like":{"wildcard":"%","property":"owner","value":"Mike%"}}
        Filter filter = parse("cqlJsonTest.json", 7);
        Assert.assertEquals("owner ILIKE 'Mike%'", ECQL.toCQL(filter));
    }

    @Test
    public void convertNotLike() throws IOException, CQLException {
        // {"not":{"like":{"property":"owner","value":"% Mike %"}}}
        Filter filter = parse("cqlJsonTest.json", 8);
        Assert.assertEquals("NOT (owner ILIKE '% Mike %')", ECQL.toCQL(filter));
    }

    @Test
    public void convertEQ() throws IOException, CQLException {
        // {"eq":{"property":"swimming_pool","value":true}}
        Filter filter = parse("cqlJsonTest.json", 9);
        Assert.assertEquals("swimming_pool = true", ECQL.toCQL(filter));
    }

    @Test
    public void convertGTAndEQ() throws IOException, CQLException {
        // {"and":[{"gt":{"property":"floor","value":5}},{"eq":{"property":"swimming_pool","value":true}}]}
        Filter filter = parse("cqlJsonTest.json", 10);
        Assert.assertEquals("floor > 5 AND swimming_pool = true", ECQL.toCQL(filter));
    }

    @Test
    public void convertAndOr() throws IOException, CQLException {
        // {"and":[{"eq":{"property":"swimming_pool","value":true}},{"or":[{"gt":{"property":"floor","value":5}},{"like":{"property":"material","value":"brick%"}},{"like":{"property":"material","value":"%brick"}}]}]}
        Filter filter = parse("cqlJsonTest.json", 11);
        Assert.assertEquals(
                "swimming_pool = true AND (floor > 5 OR material ILIKE 'brick%' OR material ILIKE '%brick')",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertOrAnd2() throws IOException, CQLException {
        // {"or":[{"and":[{"gt":{"property":"floors","value":5}},{"eq":{"property":"material","value":"brick"}}]},{"eq":{"property":"swimming_pool","value":true}}]}
        Filter filter = parse("cqlJsonTest.json", 12);
        Assert.assertEquals(
                "(floors > 5 AND material = 'brick') OR swimming_pool = true", ECQL.toCQL(filter));
    }

    @Test
    public void convertOrNot() throws IOException, CQLException {
        // {"or":[{"not":{"lt":{"property":"floors","value":5}}},{"eq":{"property":"swimming_pool","value":true}}]}
        Filter filter = parse("cqlJsonTest.json", 13);
        Assert.assertEquals("NOT (floors < 5) OR swimming_pool = true", ECQL.toCQL(filter));
    }

    @Test
    public void convertLikeAnd() throws IOException, CQLException {
        // {"and":[{"or":[{"like":{"property":"owner","value":"mike%"}},{"like":{"property":"owner","value":"Mike%"}}]},{"lt":{"property":"floors","value":4}}]}
        Filter filter = parse("cqlJsonTest.json", 14);
        Assert.assertEquals(
                "(owner ILIKE 'mike%' OR owner ILIKE 'Mike%') AND floors < 4", ECQL.toCQL(filter));
    }

    @Test
    public void convertBefore() throws IOException, CQLException {
        Filter filter = parse("cqlJsonTest.json", 15);
        Assert.assertEquals("built BEFORE '2015-01-01'", ECQL.toCQL(filter));
    }

    @Test
    public void convertAfter() throws IOException, CQLException {
        // {"after":{"property":"built","value":"2012-06-05"}}
        Filter filter = parse("cqlJsonTest.json", 16);
        Assert.assertEquals("built AFTER '2012-06-05'", ECQL.toCQL(filter));
    }

    @Test
    public void convertDuring() throws IOException, CQLException {
        // {"during":{"property":"updated","value":["2017-06-10T07:30:00","2017-06-11T10:30:00"]}}
        Filter filter = parse("cqlJsonTest.json", 23);
        Assert.assertEquals(
                "updated DURING '[2017-06-10T07:30:00, 2017-06-11T10:30:00]'", ECQL.toCQL(filter));
    }

    @Test
    public void convertWithin() throws IOException, CQLException {
        // {"within":{"property":"location","value":{"bbox":[33.8,-118,34,-117.9]}}}
        Filter filter = parse("cqlJsonTest.json", 17);
        Assert.assertEquals(
                "WITHIN(location, POLYGON ((33.8 -118, 33.8 -117.9, 34 -117.9, 34 -118, 33.8 -118)))",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertIntersects() throws IOException, CQLException {
        // {"intersects":{"property":"location","value":{"type":"Polygon","coordinates":[[[-10,-10],[10,-10],[10,10],[-10,-10]]]}}}
        Filter filter = parse("cqlJsonTest.json", 18);
        Assert.assertEquals(
                "INTERSECTS(location, POLYGON ((-10 -10, 10 -10, 10 10, -10 -10)))",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertAndWithin() throws IOException, CQLException {
        // {"and":[{"gt":{"property":"floors","value":5}},{"within":{"property":"geometry","value":{"bbox":[33.8,-118,34,-117.9]}}}]}
        Filter filter = parse("cqlJsonTest.json", 19);
        Assert.assertEquals(
                "floors > 5 AND WITHIN(geometry, POLYGON ((33.8 -118, 33.8 -117.9, 34 -117.9, 34 -118, 33.8 -118)))",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertTouchesLine() throws IOException, CQLException {
        // {"touches":{"property":"location","value":{"type":"LineString","coordinates":[[100,0],[101,1]]}}}
        Filter filter = parse("cqlJsonTest.json", 20);
        Assert.assertEquals("TOUCHES(location, LINESTRING (100 0, 101 1))", ECQL.toCQL(filter));
    }

    @Test
    public void convertContainsPoint() throws IOException, CQLException {
        // {"contains":{"property":"location","value":{"type":"Point","coordinates":[100,0]}}}
        Filter filter = parse("cqlJsonTest.json", 21);
        Assert.assertEquals("CONTAINS(location, POINT (100 0))", ECQL.toCQL(filter));
    }

    @Test
    public void convertOverlapsMultiPoint() throws IOException, CQLException {
        // {"overlaps":{"property":"location","value":{"type":"MultiPoint","coordinates":[[100,0],[101,1]]}}}
        Filter filter = parse("cqlJsonTest.json", 22);
        Assert.assertEquals(
                "OVERLAPS(location, MULTIPOINT ((100 0), (101 1)))", ECQL.toCQL(filter));
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
