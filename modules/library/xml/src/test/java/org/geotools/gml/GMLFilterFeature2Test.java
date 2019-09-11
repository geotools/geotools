package org.geotools.gml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;

public class GMLFilterFeature2Test {

    @Test
    public void test() {
        FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection = parseGml(xml);
        SimpleFeature feature = featureCollection.features().next();
        // bug GEOT-6365 returned "\n    \n  Utah" instead of "Utah"
        Assert.assertEquals("Utah", feature.getAttribute("STATE_NAME"));
    }

    private static SAXParser parser = createParser();

    private static SAXParser createParser() {
        try {
            return SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException | SAXException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static FeatureCollection<SimpleFeatureType, SimpleFeature> parseGml(String input) {
        final DefaultFeatureCollection features = new DefaultFeatureCollection();
        try {
            final GMLFilterFeature featureFilter = new GMLFilterFeature(new GMLReceiver(features));
            final GMLFilterGeometry geometryFilter = new GMLFilterGeometry(featureFilter);
            final GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

            final ParserAdapter parserAdapter = new ParserAdapter(parser.getParser());
            parserAdapter.setContentHandler(documentFilter);
            parserAdapter.parse(
                    new InputSource(
                            new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))));
            return features;
        } catch (final SAXException | IOException e) {
            throw new RuntimeException("Parsing error", e);
        }
    }

    private static final String xml =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<wfs:FeatureCollection xmlns:wfs=\"http://www.opengis.net/wfs\"\n"
                    + "  xmlns:topp=\"http://www.openplans.org/topp\"\n"
                    + "  xmlns:gml=\"http://www.opengis.net/gml\"\n"
                    + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                    + "  xsi:schemaLocation=\"http://www.openplans.org/topp http://localhost:8080/geoserver/wfs/DescribeFeatureType?typeName=topp:states http://www.opengis.net/wfs http://localhost:8080/geoserver/data/capabilities/wfs/1.0.0/WFS-basic.xsd\">\n"
                    + "  <gml:boundedBy><gml:Box srsName=\"http://www.opengis.net/gml/srs/epsg.xml#4326\">\n"
                    + "    <gml:coordinates decimal=\".\" cs=\",\" ts=\" \">-114.046463,36.988972\n"
                    + "  -102.036758,42.002377</gml:coordinates></gml:Box></gml:boundedBy>\n"
                    + "  <gml:featureMember><topp:states\n"
                    + "    fid=\"states.46\"><topp:the_geom><gml:MultiPolygon\n"
                    + "    srsName=\"http://www.opengis.net/gml/srs/epsg.xml#4326\">\n"
                    + "  <gml:polygonMember><gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>\n"
                    + "    <gml:coordinates decimal=\".\" cs=\",\" ts=\" \">-114.046463,38.137691\n"
                    + "  -114.044273,38.57114 -114.043449,38.679043 -114.039276,39.538742\n"
                    + "  -112.899216,36.996243 -114.043137,36.996563 -114.046448,37.598507\n"
                    + "  -114.046463,38.137691</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs>\n"
                    + "  </gml:Polygon></gml:polygonMember></gml:MultiPolygon></topp:the_geom>\n"
                    + "  <topp:STATE_NAME>Utah</topp:STATE_NAME><topp:STATE_FIPS>49</topp:STATE_FIPS>\n"
                    + "  <topp:SUB_REGION>Mtn</topp:SUB_REGION><topp:STATE_ABBR>UT</topp:STATE_ABBR>\n"
                    + "  <topp:LAND_KM>212815.546</topp:LAND_KM><topp:WATER_KM>7086.152</topp:WATER_KM>\n"
                    + "  <topp:PERSONS>1722850.0</topp:PERSONS><topp:FAMILIES>410862.0</topp:FAMILIES>\n"
                    + "  <topp:HOUSHOLD>537273.0</topp:HOUSHOLD><topp:MALE>855759.0</topp:MALE>\n"
                    + "  <topp:FEMALE>867091.0</topp:FEMALE><topp:WORKERS>564185.0</topp:WORKERS>\n"
                    + "  <topp:DRVALONE>541226.0</topp:DRVALONE><topp:CARPOOL>111197.0</topp:CARPOOL>\n"
                    + "  <topp:PUBTRANS>16971.0</topp:PUBTRANS><topp:EMPLOYED>736059.0</topp:EMPLOYED>\n"
                    + "  <topp:UNEMPLOY>41389.0</topp:UNEMPLOY><topp:SERVICE>196289.0</topp:SERVICE>\n"
                    + "  <topp:MANUAL>102232.0</topp:MANUAL><topp:P_MALE>0.497</topp:P_MALE>\n"
                    + "  <topp:P_FEMALE>0.503</topp:P_FEMALE><topp:SAMP_POP>304592.0</topp:SAMP_POP>\n"
                    + "  </topp:states></gml:featureMember></wfs:FeatureCollection>";
}
