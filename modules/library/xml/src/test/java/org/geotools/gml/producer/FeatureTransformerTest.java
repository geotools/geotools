package org.geotools.gml.producer;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.xmlunit.builder.Input;

public class FeatureTransformerTest {

    private static Map<String, String> NAMESPACES = new HashMap<>();

    static {
        NAMESPACES.put("xlink", "http://www.w3.org/1999/xlink");
        NAMESPACES.put("wfs", "http://www.opengis.net/wfs");
        NAMESPACES.put("gml", "http://www.opengis.net/gml");
        NAMESPACES.put("gt", "http://www.geotools.org");
    }

    @Test
    public void testEncodeEmptyArray() throws Exception {
        FeatureTransformer tx = new FeatureTransformer();
        tx.setIndentation(2);
        tx.setCollectionBounding(true);
        tx.setFeatureBounding(true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        tx.transform(new FeatureCollection[0], bos);
        String result = bos.toString();
        // System.out.println(result);

        Source actual = Input.fromString(result).build();
        assertThat(
                actual,
                hasXPath("count(//wfs:FeatureCollection)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("/wfs:FeatureCollection/gml:boundedBy/gml:null", equalTo("unknown"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:featureMember)", equalTo("0"))
                        .withNamespaceContext(NAMESPACES));
    }

    @Test
    public void testRemoveInvalidXMLChars() throws Exception {
        SimpleFeatureType ft =
                DataUtilities.createType("invalidChars", "the_geom:Point,data:String");
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        ft,
                        new Object[] {
                            new WKTReader().read("POINT(0 0)"), "One " + ((char) 0x7) + " test"
                        },
                        "123");
        SimpleFeatureCollection fc = DataUtilities.collection(feature);

        FeatureTransformer tx = new FeatureTransformer();
        tx.setIndentation(2);
        tx.getFeatureTypeNamespaces().declareNamespace(ft, "gt", "http://www.geotools.org");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        tx.transform(fc, bos);
        String result = bos.toString();

        // System.out.println(result);

        Source actual = Input.fromString(result).build();
        assertThat(
                actual,
                hasXPath("count(//wfs:FeatureCollection)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("//gt:data", equalTo("One  test")).withNamespaceContext(NAMESPACES));
    }

    /**
     * Checks FeatureTransformer DateTime formatting handling with
     * 'org.geotools.dateTimeFormatHandling' system property on true.
     */
    @Test
    public void testDateTimeFormatEnabled() throws Exception {
        System.setProperty("org.geotools.dateTimeFormatHandling", "true");
        Hints.scanSystemProperties();
        TimeZone defaultTimeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            SimpleFeatureType ft =
                    DataUtilities.createType("invalidChars", "the_geom:Point,dia:Date");
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.clear();
            cal.set(1982, 11, 3);
            Date dateValue = cal.getTime();
            SimpleFeature feature =
                    SimpleFeatureBuilder.build(
                            ft,
                            new Object[] {new WKTReader().read("POINT(0 0)"), dateValue},
                            "123");
            SimpleFeatureCollection fc = DataUtilities.collection(feature);

            FeatureTransformer tx = new FeatureTransformer();
            tx.setIndentation(2);
            tx.getFeatureTypeNamespaces().declareNamespace(ft, "gt", "http://www.geotools.org");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            tx.transform(fc, bos);
            String result = bos.toString();

            Source actual = Input.fromString(result).build();
            assertThat(
                    actual,
                    hasXPath("count(//wfs:FeatureCollection)", equalTo("1"))
                            .withNamespaceContext(NAMESPACES));
            assertThat(
                    actual,
                    hasXPath("//gt:dia", equalTo("1982-12-03T00:00:00Z"))
                            .withNamespaceContext(NAMESPACES));

        } finally {
            System.getProperties().remove("org.geotools.dateTimeFormatHandling");
            TimeZone.setDefault(defaultTimeZone);
        }
    }

    /**
     * Checks FeatureTransformer DateTime formatting handling with
     * 'org.geotools.dateTimeFormatHandling' system property on false.
     */
    @Test
    public void testDateTimeFormatDisabled() throws Exception {
        System.setProperty("org.geotools.dateTimeFormatHandling", "false");
        Hints.scanSystemProperties();
        TimeZone defaultTimeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            SimpleFeatureType ft =
                    DataUtilities.createType("invalidChars", "the_geom:Point,dia:Date");
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.clear();
            cal.set(1982, 11, 3);
            Date dateValue = cal.getTime();
            SimpleFeature feature =
                    SimpleFeatureBuilder.build(
                            ft,
                            new Object[] {new WKTReader().read("POINT(0 0)"), dateValue},
                            "123");
            SimpleFeatureCollection fc = DataUtilities.collection(feature);

            FeatureTransformer tx = new FeatureTransformer();
            tx.setIndentation(2);
            tx.getFeatureTypeNamespaces().declareNamespace(ft, "gt", "http://www.geotools.org");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            tx.transform(fc, bos);
            String result = bos.toString();

            Source actual = Input.fromString(result).build();
            assertThat(
                    actual,
                    hasXPath("count(//wfs:FeatureCollection)", equalTo("1"))
                            .withNamespaceContext(NAMESPACES));
            assertThat(
                    actual,
                    hasXPath("//gt:dia", equalTo("1982-12-03T00:00:00"))
                            .withNamespaceContext(NAMESPACES));
        } finally {
            System.getProperties().remove("org.geotools.dateTimeFormatHandling");
            TimeZone.setDefault(defaultTimeZone);
        }
    }

    @Test
    public void testInvalidElementName() throws Exception {
        SimpleFeatureType ft = DataUtilities.createType("invalid", "the_geom:Point,foo><bar:int");
        SimpleFeature f = SimpleFeatureBuilder.build(ft, new Object[] {null, 1}, "foo");
        SimpleFeatureCollection fc = DataUtilities.collection(f);
        FeatureTransformer tx = new FeatureTransformer();
        tx.getFeatureTypeNamespaces().declareNamespace(ft, "gt", "http://www.geotools.org");
        Exception e = assertThrows(TransformerException.class, () -> tx.transform(fc));
        assertThat(e.getMessage(), containsString("INVALID_CHARACTER_ERR"));
    }

    @Test
    public void testHtmlNamespaceUri() throws Exception {
        SimpleFeatureType ft = DataUtilities.createType("invalid", "the_geom:Point,script:String");
        SimpleFeature f = SimpleFeatureBuilder.build(ft, new Object[] {null, "bar"}, "foo");
        SimpleFeatureCollection fc = DataUtilities.collection(f);
        FeatureTransformer tx = new FeatureTransformer();
        tx.getFeatureTypeNamespaces().declareNamespace(ft, "gt", "http://www.w3.org/1999/xhtml");
        Exception e = assertThrows(TransformerException.class, () -> tx.transform(fc));
        assertThat(e.getMessage(), containsString("NAMESPACE_ERR"));
    }
}
