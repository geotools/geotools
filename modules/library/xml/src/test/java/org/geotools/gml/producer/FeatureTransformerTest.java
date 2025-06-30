package org.geotools.gml.producer;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.transform.TransformerException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.test.xml.XmlTestSupport;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.io.WKTReader;

public class FeatureTransformerTest extends XmlTestSupport {

    @Override
    protected Map<String, String> getNamespaces() {
        return namespaces(
                Namespace("xlink", "http://www.w3.org/1999/xlink"),
                Namespace("wfs", "http://www.opengis.net/wfs"),
                Namespace("gml", "http://www.opengis.net/gml"),
                Namespace("gt", "http://www.geotools.org"));
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

        assertThat(result, hasXPath("count(//wfs:FeatureCollection)", equalTo("1")));
        assertThat(result, hasXPath("/wfs:FeatureCollection/gml:boundedBy/gml:null", equalTo("unknown")));
        assertThat(result, hasXPath("count(//gml:featureMember)", equalTo("0")));
    }

    @Test
    public void testRemoveInvalidXMLChars() throws Exception {
        SimpleFeatureType ft = DataUtilities.createType("invalidChars", "the_geom:Point,data:String");
        SimpleFeature feature = SimpleFeatureBuilder.build(
                ft, new Object[] {new WKTReader().read("POINT(0 0)"), "One " + (char) 0x7 + " test"}, "123");
        SimpleFeatureCollection fc = DataUtilities.collection(feature);

        FeatureTransformer tx = new FeatureTransformer();
        tx.setIndentation(2);
        tx.getFeatureTypeNamespaces().declareNamespace(ft, "gt", "http://www.geotools.org");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        tx.transform(fc, bos);
        String result = bos.toString();

        assertThat(result, hasXPath("count(//wfs:FeatureCollection)", equalTo("1")));
        assertThat(result, hasXPath("//gt:data", equalTo("One  test")));
    }

    /**
     * Checks FeatureTransformer DateTime formatting handling with 'org.geotools.dateTimeFormatHandling' system property
     * on true.
     */
    @Test
    public void testDateTimeFormatEnabled() throws Exception {
        System.setProperty("org.geotools.dateTimeFormatHandling", "true");
        Hints.scanSystemProperties();
        TimeZone defaultTimeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            SimpleFeatureType ft = DataUtilities.createType("invalidChars", "the_geom:Point,dia:Date");
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.clear();
            cal.set(1982, 11, 3);
            Date dateValue = cal.getTime();
            SimpleFeature feature =
                    SimpleFeatureBuilder.build(ft, new Object[] {new WKTReader().read("POINT(0 0)"), dateValue}, "123");
            SimpleFeatureCollection fc = DataUtilities.collection(feature);

            FeatureTransformer tx = new FeatureTransformer();
            tx.setIndentation(2);
            tx.getFeatureTypeNamespaces().declareNamespace(ft, "gt", "http://www.geotools.org");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            tx.transform(fc, bos);
            String result = bos.toString();

            assertThat(result, hasXPath("count(//wfs:FeatureCollection)", equalTo("1")));
            assertThat(result, hasXPath("//gt:dia", equalTo("1982-12-03T00:00:00Z")));

        } finally {
            System.getProperties().remove("org.geotools.dateTimeFormatHandling");
            TimeZone.setDefault(defaultTimeZone);
        }
    }

    /**
     * Checks FeatureTransformer DateTime formatting handling with 'org.geotools.dateTimeFormatHandling' system property
     * on false.
     */
    @Test
    public void testDateTimeFormatDisabled() throws Exception {
        System.setProperty("org.geotools.dateTimeFormatHandling", "false");
        Hints.scanSystemProperties();
        TimeZone defaultTimeZone = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            SimpleFeatureType ft = DataUtilities.createType("invalidChars", "the_geom:Point,dia:Date");
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            cal.clear();
            cal.set(1982, 11, 3);
            Date dateValue = cal.getTime();
            SimpleFeature feature =
                    SimpleFeatureBuilder.build(ft, new Object[] {new WKTReader().read("POINT(0 0)"), dateValue}, "123");
            SimpleFeatureCollection fc = DataUtilities.collection(feature);

            FeatureTransformer tx = new FeatureTransformer();
            tx.setIndentation(2);
            tx.getFeatureTypeNamespaces().declareNamespace(ft, "gt", "http://www.geotools.org");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            tx.transform(fc, bos);
            String result = bos.toString();

            assertThat(result, hasXPath("count(//wfs:FeatureCollection)", equalTo("1")));
            assertThat(result, hasXPath("//gt:dia", equalTo("1982-12-03T00:00:00")));
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
