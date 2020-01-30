package org.geotools.gml.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.w3c.dom.Document;

public class FeatureTransformerTest {

    @Before
    public void setup() {
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("gt", "http://www.geotools.org");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
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

        Document dom = XMLUnit.buildControlDocument(result);
        assertXpathEvaluatesTo("1", "count(//wfs:FeatureCollection)", dom);
        assertXpathEvaluatesTo("unknown", "/wfs:FeatureCollection/gml:boundedBy/gml:null", dom);
        assertXpathEvaluatesTo("0", "count(//gml:featureMember)", dom);
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

        Document dom = XMLUnit.buildControlDocument(result);
        assertXpathEvaluatesTo("1", "count(//wfs:FeatureCollection)", dom);
        assertXpathEvaluatesTo("One  test", "//gt:data", dom);
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
            Document dom = XMLUnit.buildControlDocument(result);
            assertXpathEvaluatesTo("1", "count(//wfs:FeatureCollection)", dom);
            assertXpathEvaluatesTo("1982-12-03T00:00:00Z", "//gt:dia", dom);
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
            Document dom = XMLUnit.buildControlDocument(result);
            assertXpathEvaluatesTo("1", "count(//wfs:FeatureCollection)", dom);
            assertXpathEvaluatesTo("1982-12-03T00:00:00", "//gt:dia", dom);
        } finally {
            System.getProperties().remove("org.geotools.dateTimeFormatHandling");
            TimeZone.setDefault(defaultTimeZone);
        }
    }
}
