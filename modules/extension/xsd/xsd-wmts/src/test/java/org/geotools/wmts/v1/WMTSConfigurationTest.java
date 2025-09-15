/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wmts.v1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.wmts.v_1.CapabilitiesType;
import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.LayerType;
import org.geotools.filter.v1_1.OGC;
import org.geotools.gml2.GML;
import org.geotools.test.xml.XmlTestSupport;
import org.geotools.util.logging.Logging;
import org.geotools.wmts.WMTS;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xlink.XLINK;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.geotools.xsd.ows.OWS;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** @author Emanuele Tajariol (etj at geo-solutions dot it) */
public class WMTSConfigurationTest extends XmlTestSupport {

    static final Logger LOGGER = Logging.getLogger(WMTSConfigurationTest.class);

    @Override
    protected Map<String, String> getNamespaces() {
        return namespaces(
                Namespace("xlink", "http://www.w3.org/1999/xlink"),
                Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"),
                Namespace("ows", "http://www.opengis.net/ows/1.1"),
                Namespace("wmts", "http://www.opengis.net/wmts/1.0"));
    }

    @Test
    /** Test GEOT-6004 GetCapabilities fails for ows:Profile */
    public void testGEOT6004() throws IOException, SAXException, ParserConfigurationException {
        Parser p = new Parser(new WMTSConfiguration());
        // p.setValidating(false);
        Object parsed;
        try (InputStream is = getClass().getResourceAsStream("./wmtsGetCapabilities_6004.xml")) {
            parsed = p.parse(is);
        }

        assertTrue("Capabilities failed to parse " + parsed.getClass(), parsed instanceof CapabilitiesType);

        CapabilitiesType caps = (CapabilitiesType) parsed;
        ServiceIdentificationType service = caps.getServiceIdentification();
        assertNotNull(service);
        assertEquals("http://www.opengis.net/spec/wmts-simple/1.0/conf/simple-profile/CRS84", service.getProfile());
        ContentsType contents = caps.getContents();
        assertNotNull(contents);
    }

    @Test
    public void testParse() throws IOException, SAXException, ParserConfigurationException {
        Map<String, LayerType> layers = parseCaps("./nasa.getcapa.xml");

        assertEquals(519, layers.size());
        assertTrue(layers.containsKey("MODIS_Terra_L3_SST_MidIR_9km_Night_Annual"));

        layers = parseCaps("geosolutions-wmts-getcapabilities.xml");
        assertEquals(26, layers.size());
    }

    /** */
    private Map<String, LayerType> parseCaps(String capDoc)
            throws IOException, SAXException, ParserConfigurationException {
        Parser p = new Parser(new WMTSConfiguration());
        p.setValidating(false);
        Object parsed;
        try (InputStream is = getClass().getResourceAsStream(capDoc)) {
            parsed = p.parse(is);
        }

        assertTrue("Capabilities failed to parse " + parsed.getClass(), parsed instanceof CapabilitiesType);

        CapabilitiesType caps = (CapabilitiesType) parsed;
        ContentsType contents = caps.getContents();
        assertNotNull(contents);

        Map<String, LayerType> layers = new HashMap<>();

        // Parse layers
        for (Object l : contents.getDatasetDescriptionSummary()) {

            if (l instanceof LayerType layerType) {
                String id = layerType.getIdentifier().getValue();

                layers.put(id, layerType);
            }
        }
        return layers;
    }

    /**
     * TODO.
     *
     * <p>Validation fails due to a gml/xlink conflict of some type:
     *
     * <p>org.xml.sax.SAXParseException; systemId: http://schemas.opengis.net/gml/3.1.1/base/gmlBase.xsd; lineNumber:
     * 268; columnNumber: 44; src-resolve: Cannot resolve the name 'xlink:simpleAttrs' to a(n) 'attribute group'
     * component.
     *
     * <p>on line <attributeGroup ref="xlink:simpleAttrs"/>
     */
    @Ignore
    @Test
    public void testValidate() throws IOException, SAXException, ParserConfigurationException {
        Parser p = new Parser(new WMTSConfiguration());
        p.setValidating(true);
        try (InputStream is = getClass().getResourceAsStream("./nasa.getcapa.xml")) {
            p.parse(is);
        }
        if (!p.getValidationErrors().isEmpty()) {
            for (Exception exception : p.getValidationErrors()) {
                SAXParseException ex = (SAXParseException) exception;
                LOGGER.log(Level.SEVERE, ex.getLineNumber() + "," + ex.getColumnNumber() + " -" + ex.toString());
            }
            fail("Document did not validate.");
        }
    }

    @Test
    // Test that the encoding of WMTS as a DOM document works correctly. This
    // parses an getCapabilities document and then encodes it and checks it's
    // been encoded correctly
    public void testEncode() throws Exception {

        Parser myParser = new Parser(new WMTSConfiguration());

        CapabilitiesType caps = (CapabilitiesType) myParser.parse(getClass().getResourceAsStream("nasa.getcapa.xml"));

        Encoder encoder = new Encoder(new WMTSConfiguration());
        encoder.setIndenting(true);
        encoder.setSchemaLocation(WMTS.NAMESPACE, WMTS.WMTSGetCapabilitiesResponseSchemalocation);
        encoder.setNamespaceAware(true);
        encoder.getNamespaces().declarePrefix("ows", OWS.NAMESPACE);
        encoder.getNamespaces().declarePrefix("ogc", OGC.NAMESPACE);
        encoder.getNamespaces().declarePrefix("gml", GML.NAMESPACE);
        encoder.getNamespaces().declarePrefix("wmts", WMTS.NAMESPACE);
        encoder.getNamespaces().declarePrefix("xlink", XLINK.NAMESPACE);
        Document doc = encoder.encodeAsDOM(caps, WMTS.Capabilities);

        assertThat(doc, hasXPath("count(//wmts:Contents/wmts:Layer)", equalTo("519")));

        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent'])",
                        equalTo("1")));

        /////////////////////////////////////
        // Check the layer/wmts:Style values
        /////////////////////////////////////
        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Style[ows:Identifier='default'])",
                        equalTo("1")));

        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Style/wmts:LegendURL"
                                + "[@width='125'][@height='130'][@format='image/png'][@xlink:href='https://some-url?some-parameter=value3&another-parameter=value4'])",
                        equalTo("1")));

        /////////////////////////////////////
        // Check the layer/wmts:MetadataURL values
        /////////////////////////////////////
        // TODO: Not all the values are being parsed for the metadata so these
        //       tests are not succeeding
        /*
                assertEquals(
                "1",
                xpath.evaluate(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:MetadataURL[@xlink:type='simple'][xlink:role='http://earthdata.nasa.gov/gibs/metadata-type/colormap'][xlink:href='https://gibs.earthdata.nasa.gov/colormaps/v1.3/AMSR2_Snow_Water_Equivalent.xml'][xlink:title='GIBS Color Map: Data - RGB Mapping'])",
                        doc));

                assertEquals(
                "1",
                xpath.evaluate(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:MetadataURL[@xlink:type='simple'][xlink:role='http://earthdata.nasa.gov/gibs/metadata-type/colormap/1.0'][xlink:href='https://gibs.earthdata.nasa.gov/colormaps/v1.0/AMSR2_Snow_Water_Equivalent.xml'][xlink:title='GIBS Color Map: Data - RGB Mapping'])",
                        doc));

        assertEquals(
                "1",
                xpath.evaluate(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:MetadataURL[@xlink:type='simple'][xlink:role='http://earthdata.nasa.gov/gibs/metadata-type/colormap/1.2'][xlink:href='https://gibs.earthdata.nasa.gov/colormaps/v1.2/AMSR2_Snow_Water_Equivalent.xml'][xlink:title='GIBS Color Map: Data - RGB Mapping'])",
                        doc));

        assertEquals(
                "1",
                xpath.evaluate(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:MetadataURL[@xlink:type='simple'][xlink:role='http://earthdata.nasa.gov/gibs/metadata-type/colormap/1.3'][xlink:href='https://gibs.earthdata.nasa.gov/colormaps/v1.3/AMSR2_Snow_Water_Equivalent.xml'][xlink:title='GIBS Color Map: Data - RGB Mapping'])",
                        doc));*/

        /////////////////////////////////////
        // Checking the layer/wmts:ResourceURL
        /////////////////////////////////////
        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:ResourceURL[@resourceType='tile']"
                                + "[@format='image/png']"
                                + "[@template='https://gibs.earthdata.nasa.gov/wmts/epsg4326/best/AMSR2_Snow_Water_Equivalent/default/{Time}/{TileMatrixSet}/{TileMatrix}/{TileRow}/{TileCol}.png'])",
                        equalTo("1")));

        /////////////////////////////////////
        // Checking the service metadata URL
        /////////////////////////////////////
        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:ServiceMetadataURL[@xlink:href='https://gibs.earthdata.nasa.gov/wmts/epsg4326/best/1.0.0/WMTSCapabilities.xml'])",
                        equalTo("1")));

        /////////////////////////////////////
        // Check the wmts:dimension values
        /////////////////////////////////////
        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Dimension[ows:Identifier='time'])",
                        equalTo("1")));

        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Dimension[ows:Identifier='time'])",
                        equalTo("1")));

        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Dimension/ows:Title[text()]",
                        equalTo("Title for a Dimension element")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Dimension/ows:Abstract[text()]",
                        equalTo("Abstract for a Dimension element")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Dimension/ows:Identifier[text()]",
                        equalTo("time")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Dimension/wmts:Default[text()]",
                        equalTo("2017-06-16")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Dimension/wmts:Current[text()]",
                        equalTo("false")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:Layer[ows:Identifier='AMSR2_Snow_Water_Equivalent']/wmts:Dimension/wmts:Value[text()]",
                        equalTo("2015-07-30/2017-06-16/P1D")));

        // Check one of the TileMatrices and that it has the right values set
        assertThat(
                doc,
                hasXPath("count(//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()='31.25m']])", equalTo("1")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()='31.25m']]/ows:Abstract[text()]",
                        equalTo("Abstract for the Tile Matrix Set")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()='31.25m']]/ows:Title[text()]",
                        equalTo("Title for 31.25m TileMatrixSet")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()='31.25m']]/ows:SupportedCRS[text()]",
                        equalTo("urn:ogc:def:crs:OGC:1.3:CRS84")));

        // Check that the TileMatrixSet keywords have been set and that they are correct
        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()='31.25m']]/ows:Keywords/ows:Keyword)",
                        equalTo("1")));
        assertThat(
                doc,
                hasXPath(
                        "count(//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()='31.25m']]/ows:Keywords/ows:Keyword[text()=\"3125mKeyword\"])",
                        equalTo("1")));

        // Check each tilematrix has the right values set
        for (int i = 1; i <= 12; i++) {

            assertThat(
                    doc,
                    hasXPath(
                            "count(//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()=\"31.25m\"]]/wmts:TileMatrix["
                                    + i
                                    + "]/ows:Keywords/ows:Keyword)",
                            equalTo("1")));

            assertThat(
                    doc,
                    hasXPath(
                            "count(//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()=\"31.25m\"]]/wmts:TileMatrix["
                                    + i
                                    + "]/ows:Abstract[text()=\"Abstract for grid "
                                    + (i - 1)
                                    + "\"])",
                            equalTo("1")));

            assertThat(
                    doc,
                    hasXPath(
                            "count(//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()=\"31.25m\"]]/wmts:TileMatrix["
                                    + i
                                    + "]/ows:Title[text()=\"Grid Title "
                                    + (i - 1)
                                    + "\"])",
                            equalTo("1")));

            assertThat(
                    doc,
                    hasXPath(
                            "count(//wmts:Contents/wmts:TileMatrixSet[ows:Identifier[text()=\"31.25m\"]]/wmts:TileMatrix["
                                    + i
                                    + "]/ows:Keywords/ows:Keyword[text()=\"Keyword"
                                    + (i - 1)
                                    + "\"])",
                            equalTo("1")));
        }

        /////////////////////////////////////
        // Check the wmts:Themes element
        /////////////////////////////////////
        assertThat(doc, hasXPath("count(//wmts:Themes/wmts:Theme)", equalTo("1")));

        assertThat(
                doc,
                hasXPath(
                        "//wmts:Themes/wmts:Theme[ows:Identifier[text()='Foundation']]/ows:Title[text()]",
                        equalTo("Foundation")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Themes/wmts:Theme[ows:Identifier[text()='Foundation']]/ows:Abstract[text()]",
                        equalTo("World reference data")));

        assertThat(doc, hasXPath("count(//wmts:Themes/wmts:Theme/wmts:Theme)", equalTo("2")));

        assertThat(
                doc,
                hasXPath(
                        "//wmts:Themes/wmts:Theme/wmts:Theme[ows:Identifier[text()='DEM']]/ows:Title[text()]",
                        equalTo("Digital Elevation Model")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Themes/wmts:Theme/wmts:Theme[ows:Identifier[text()='DEM']]/wmts:LayerRef[text()]",
                        equalTo("etopo2")));

        assertThat(
                doc,
                hasXPath(
                        "//wmts:Themes/wmts:Theme/wmts:Theme[ows:Identifier[text()='AdmBoundaries']]/ows:Title[text()]",
                        equalTo("Administrative Boundaries")));
        assertThat(
                doc,
                hasXPath(
                        "//wmts:Themes/wmts:Theme/wmts:Theme[ows:Identifier[text()='AdmBoundaries']]/wmts:LayerRef[text()]",
                        equalTo("AdminBoundaries")));

        /////////////////////////////////////
        // ServiceProvider Section
        /////////////////////////////////////
        assertThat(
                doc,
                hasXPath(
                        "//ows:ServiceProvider/ows:ProviderName[text()]",
                        equalTo("National Aeronautics and Space Administration")));

        assertThat(
                doc,
                hasXPath(
                        "count(//ows:ServiceProvider/ows:ProviderSite[@xlink:href=\"https://earthdata.nasa.gov/\"])",
                        equalTo("1")));
        assertThat(
                doc,
                hasXPath("//ows:ServiceContact/ows:IndividualName[text()]", equalTo("ServiceContact IndividualName")));
        assertThat(
                doc, hasXPath("//ows:ServiceContact/ows:PositionName[text()]", equalTo("ServiceContact PositionName")));
        assertThat(
                doc,
                hasXPath(
                        "//ows:ServiceContact/ows:ContactInfo/ows:Address/ows:DeliveryPoint[text()]",
                        equalTo("ContactInfo Address DeliveryPoint")));

        assertThat(
                doc,
                hasXPath(
                        "//ows:ServiceContact/ows:ContactInfo/ows:Address/ows:City[text()]",
                        equalTo("ContactInfo Address City")));

        assertThat(
                doc,
                hasXPath(
                        "//ows:ServiceContact/ows:ContactInfo/ows:Address/ows:AdministrativeArea[text()]",
                        equalTo("ContactInfo Address AdministrativeArea")));

        assertThat(
                doc,
                hasXPath(
                        "//ows:ServiceContact/ows:ContactInfo/ows:Address/ows:PostalCode[text()]",
                        equalTo("ContactInfo Address PostalCode")));

        assertThat(
                doc,
                hasXPath(
                        "//ows:ServiceContact/ows:ContactInfo/ows:Address/ows:Country[text()]",
                        equalTo("ContactInfo Address Country")));

        assertThat(
                doc,
                hasXPath(
                        "//ows:ServiceContact/ows:ContactInfo/ows:Address/ows:ElectronicMailAddress[text()]",
                        equalTo("ContactInfo Address ElectronicMailAddress")));
    }

    /** Utility method to print out a dom. */
    protected void print(Document dom) throws Exception {
        TransformerFactory txFactory = TransformerFactory.newInstance();
        try {
            txFactory.setAttribute("{http://xml.apache.org/xalan}indent-number", Integer.valueOf(4));
        } catch (Exception e) {
            // some
        }

        Transformer tx = txFactory.newTransformer();
        tx.setOutputProperty(OutputKeys.METHOD, "xml");
        tx.setOutputProperty(OutputKeys.INDENT, "yes");

        tx.transform(new DOMSource(dom), new StreamResult(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));
    }
}
