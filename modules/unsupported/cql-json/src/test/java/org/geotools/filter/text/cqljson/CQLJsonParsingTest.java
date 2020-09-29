package org.geotools.filter.text.cqljson;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CQLJsonParsingTest {
    @Test
    public void convertJsonToPredicates() throws IOException, CQLException {
        //{"and":[{"eq":{"property":"swimming_pool","value":true}},{"or":[{"gt":{"property":"floor","value":5}},{"like":{"property":"material","value":"brick%"}},{"like":{"property":"material","value":"%brick"}}]}]}
        Filter filter = parse("cqlJsonTest.json",0);
        Assert.assertEquals(
                "swimming_pool = true AND (floor > 5 OR material ILIKE 'brick%' OR material ILIKE '%brick')",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertAndIntersects() throws IOException, CQLException {
        //{"and":[{"eq":{"property":"beamMode","value":"ScanSAR Narrow"}},{"eq":{"property":"swathDirection","value":"ascending"}},{"eq":{"property":"polarization","value":"HH+VV+HV+VH"}},{"intersects":{"property":"footprint","value":{"type":"Polygon","coordinates":[[[-77.117938,38.93686],[-77.040604,39.995648],[-76.910536,38.892912],[-77.039359,38.791753],[-77.047906,38.841462],[-77.034183,38.840655],[-77.033142,38.85749]]]}}}]}
        Filter filter = parse("cqlJsonTest.json",1);
        Assert.assertEquals(
                true,
                ECQL.toCQL(filter)
                        .startsWith(
                                "beamMode = 'ScanSAR Narrow' AND swathDirection = 'ascending' " +
                                        "AND polarization = 'HH+VV+HV+VH' AND INTERSECTS(footprint, " +
                                        "'org.geotools.geometry.jts.spatialschema.geometry.primitive.SurfaceImpl"));
    }

    @Test
    public void convertLTE() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"lte\": {"
                        + "                \"property\": \"taxes\","
                        + "                \"value\": 500"
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("taxes <= 500", ECQL.toCQL(filter));
    }

    @Test
    public void convertNotLTE() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "\"not\":{"
                        + "             \"lte\": {"
                        + "                \"property\": \"taxes\","
                        + "                \"value\": 500"
                        + "             }"
                        + "          }"
                        + "         }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("NOT (taxes <= 500)", ECQL.toCQL(filter));
    }

    @Test
    public void convertAndLTE() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "\"and\": ["
                        + "{\"not\":{"
                        + "             \"lte\": {"
                        + "                \"property\": \"taxes\","
                        + "                \"value\": 500"
                        + "             }"
                        + "          }"
                        + "         },"
                        + "             {\"lte\": {"
                        + "                \"property\": \"taxes\","
                        + "                \"value\": 800"
                        + "             }"
                        + "             }"
                        + "          ]"
                        + "}";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("NOT (taxes <= 500) AND taxes <= 800", ECQL.toCQL(filter));
    }

    @Test
    public void convertCompoundAndLTE() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "  \"and\": ["
                        + "    {"
                        + "      \"lte\": {"
                        + "        \"property\": \"taxes\","
                        + "        \"value\": 500"
                        + "      }"
                        + "    },"
                        + "    {"
                        + "      \"and\": ["
                        + "        {"
                        + "          \"lte\": {"
                        + "            \"property\": \"taxes\","
                        + "            \"value\": 600"
                        + "          }"
                        + "        },"
                        + "        {"
                        + "          \"lte\": {"
                        + "            \"property\": \"taxes\","
                        + "            \"value\": 400"
                        + "          }"
                        + "        }"
                        + "      ]"
                        + "    }"
                        + "  ]"
                        + "}";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                "taxes <= 500 AND (taxes <= 600 AND taxes <= 400)",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertLike() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"like\": {"
                        + "                \"property\": \"owner\","
                        + "                \"value\": \"% Jones %\""
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("owner ILIKE '% Jones %'", ECQL.toCQL(filter));
    }

    @Test
    public void convertStartsWithLike() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"like\": {"
                        + "                \"wildcard\": \"%\","
                        + "                \"property\": \"owner\","
                        + "                \"value\": \"Mike%\""
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("owner ILIKE 'Mike%'", ECQL.toCQL(filter));
    }

    @Test
    public void convertNotLike() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"not\": {"
                        + "                \"like\": {"
                        + "                   \"property\": \"owner\","
                        + "                   \"value\": \"% Mike %\""
                        + "                }"
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("NOT (owner ILIKE '% Mike %')", ECQL.toCQL(filter));
    }

    @Test
    public void convertEQ() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"eq\": {"
                        + "                \"property\": \"swimming_pool\","
                        + "                \"value\": true"
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("swimming_pool = true", ECQL.toCQL(filter));
    }

    @Test
    public void convertGTAndEQ() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"and\": ["
                        + "                {"
                        + "                   \"gt\": {"
                        + "                      \"property\": \"floor\","
                        + "                      \"value\": 5"
                        + "                   }"
                        + "                },"
                        + "                {"
                        + "                   \"eq\": {"
                        + "                      \"property\": \"swimming_pool\","
                        + "                      \"value\": true"
                        + "                   }"
                        + "                }"
                        + "             ]"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("floor > 5 AND swimming_pool = true", ECQL.toCQL(filter));
    }

    @Test
    public void convertAndOr() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"and\": ["
                        + "                {"
                        + "                   \"eq\": {"
                        + "                      \"property\": \"swimming_pool\","
                        + "                      \"value\": true"
                        + "                   }"
                        + "                },"
                        + "                {"
                        + "                   \"or\": ["
                        + "                      {"
                        + "                         \"gt\": {"
                        + "                            \"property\": \"floor\","
                        + "                            \"value\": 5"
                        + "                         }"
                        + "                      },"
                        + "                      {"
                        + "                         \"like\": {"
                        + "                            \"property\": \"material\","
                        + "                            \"value\": \"brick%\""
                        + "                         }"
                        + "                      },"
                        + "                      {"
                        + "                         \"like\": {"
                        + "                            \"property\": \"material\","
                        + "                            \"value\": \"%brick\""
                        + "                         }"
                        + "                      } "
                        + "                   ]"
                        + "                }"
                        + "             ]"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                "swimming_pool = true AND (floor > 5 OR material ILIKE 'brick%' OR material ILIKE '%brick')",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertOrAnd2() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"or\": ["
                        + "                {"
                        + "                   \"and\": ["
                        + "                      {"
                        + "                         \"gt\": {"
                        + "                            \"property\": \"floors\","
                        + "                            \"value\": 5"
                        + "                         }"
                        + "                      },"
                        + "                      {"
                        + "                         \"eq\": {"
                        + "                            \"property\": \"material\","
                        + "                            \"value\": \"brick\""
                        + "                         }"
                        + "                      }"
                        + "                   ]"
                        + "                },"
                        + "                {"
                        + "                   \"eq\": {"
                        + "                      \"property\": \"swimming_pool\","
                        + "                      \"value\": true"
                        + "                   }"
                        + "                }"
                        + "             ]"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                "(floors > 5 AND material = 'brick') OR swimming_pool = true",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertOrNot() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"or\": ["
                        + "                {"
                        + "                   \"not\": {"
                        + "                      \"lt\": {"
                        + "                         \"property\": \"floors\","
                        + "                         \"value\": 5"
                        + "                      }"
                        + "                   }"
                        + "                },"
                        + "                {"
                        + "                   \"eq\": {"
                        + "                       \"property\": \"swimming_pool\","
                        + "                       \"value\": true"
                        + "                   }"
                        + "                }"
                        + "             ]"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                "NOT (floors < 5) OR swimming_pool = true", ECQL.toCQL(filter));
    }

    @Test
    public void convertLikeAnd() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"and\": ["
                        + "                {"
                        + "                   \"or\": ["
                        + "                      {"
                        + "                         \"like\": {"
                        + "                            \"property\": \"owner\","
                        + "                            \"value\": \"mike%\""
                        + "                         }"
                        + "                      },"
                        + "                      {"
                        + "                         \"like\": {"
                        + "                            \"property\": \"owner\","
                        + "                            \"value\": \"Mike%\""
                        + "                         }"
                        + "                      }"
                        + "                   ]"
                        + "                },"
                        + "                {"
                        + "                   \"lt\": {"
                        + "                      \"property\": \"floors\","
                        + "                      \"value\": 4"
                        + "                   }"
                        + "                }"
                        + "             ]"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                "(owner ILIKE 'mike%' OR owner ILIKE 'Mike%') AND floors < 4",
                ECQL.toCQL(filter));
    }

    @Test
    public void convertBefore() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"before\": {"
                        + "                \"property\": \"built\","
                        + "                \"value\": \"2015-01-01\""
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("built BEFORE '2015-01-01'", ECQL.toCQL(filter));
    }

    @Test
    public void convertAfter() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"after\": {"
                        + "                \"property\": \"built\","
                        + "                \"value\": \"2012-06-05\""
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals("built AFTER '2012-06-05'", ECQL.toCQL(filter));
    }

    @Test
    public void convertDuring() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"during\": {"
                        + "                \"property\": \"updated\","
                        + "                \"value\": [\"2017-06-10T07:30:00\",\"2017-06-11T10:30:00\"]"
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                "updated DURING '[2017-06-10T07:30:00, 2017-06-11T10:30:00]'", ECQL.toCQL(filter));
    }

    @Test
    public void convertWithin() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"within\": {"
                        + "                \"property\": \"location\","
                        + "                \"value\": { \"bbox\": [33.8,-118,34,-117.9] }"
                        + "              }"
                        + "           }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                true,
                ECQL.toCQL(filter)
                        .startsWith(
                                "WITHIN(location, 'org.geotools.geometry.jts.spatialschema.geometry.primitive.SurfaceImpl"));
    }

    @Test
    public void convertIntersects() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"intersects\": {"
                        + "                \"property\": \"location\","
                        + "                \"value\": {"
                        + "                   \"type\": \"Polygon\","
                        + "                   \"coordinates\": [[[-10.0, -10.0],[10.0, -10.0],[10.0, 10.0],[-10.0, -10.0]]]"
                        + "                }"
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                true,
                ECQL.toCQL(filter)
                        .startsWith(
                                "INTERSECTS(location, 'org.geotools.geometry.jts.spatialschema.geometry.primitive.SurfaceImpl"));
    }

    @Test
    public void convertAndWithin() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"and\": ["
                        + "                {"
                        + "                   \"gt\": {"
                        + "                      \"property\": \"floors\","
                        + "                      \"value\": 5"
                        + "                   }"
                        + "                },"
                        + "                {"
                        + "                   \"within\": {"
                        + "                      \"property\": \"geometry\","
                        + "                      \"value\": { \"bbox\": [33.8,-118,34,-117.9] }"
                        + "                   }"
                        + "                }"
                        + "             ]"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                true,
                ECQL.toCQL(filter)
                        .startsWith(
                                "floors > 5 AND WITHIN(geometry, 'org.geotools.geometry.jts.spatialschema.geometry.primitive.SurfaceImpl"));
    }

    @Test
    public void convertTouchesLine() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"touches\": {"
                        + "                \"property\": \"location\","
                        + "                \"value\": {"
                        + "                   \"type\": \"LineString\","
                        + "                   \"coordinates\": [[100.0, 0.0],[101.0, 1.0]]"
                        + "                }"
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                true,
                ECQL.toCQL(filter)
                        .startsWith(
                                "TOUCHES(location, 'org.geotools.geometry.jts.spatialschema.geometry.primitive.CurveImpl"));
    }

    @Test
    public void convertContainsPoint() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"contains\": {"
                        + "                \"property\": \"location\","
                        + "                \"value\": {"
                        + "                   \"type\": \"Point\","
                        + "                   \"coordinates\": [100.0, 0.0]"
                        + "                }"
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                true,
                ECQL.toCQL(filter)
                        .startsWith(
                                "CONTAINS(location, 'org.geotools.geometry.jts.spatialschema.geometry.primitive.PointImpl"));
    }

    @Test
    public void convertOverlapsMultiPoint() throws JsonProcessingException, CQLException {
        String taxes =
                "{"
                        + "             \"overlaps\": {"
                        + "                \"property\": \"location\","
                        + "                \"value\": {"
                        + "                   \"type\": \"MultiPoint\","
                        + "                   \"coordinates\": [[100.0, 0.0],[101.0, 1.0]]"
                        + "                }"
                        + "             }"
                        + "          }";
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(taxes, new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        Filter filter = cqlJsonCompiler.getFilter();
        Assert.assertEquals(
                true,
                ECQL.toCQL(filter)
                        .startsWith(
                                "OVERLAPS(location, 'org.geotools.geometry.jts.spatialschema.geometry.aggregate.MultiPointImpl"));
    }

   private Filter parse(String file, Integer lineNumber) throws IOException, CQLException {
        ClassLoader classLoader = getClass().getClassLoader();
        File fileHandle = new File(classLoader.getResource(file).getFile());
        List<String> lines = Files.readAllLines(fileHandle.toPath(), StandardCharsets.UTF_8);
        CQLJsonCompiler cqlJsonCompiler = new CQLJsonCompiler(lines.get(lineNumber), new FilterFactoryImpl());
        cqlJsonCompiler.compileFilter();
        return cqlJsonCompiler.getFilter();
    }
}
