/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.opengis.fes20.FilterCapabilitiesType;
import net.opengis.ows11.DCPType;
import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.RequestMethodType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.WGS84BoundingBoxType;
import net.opengis.wfs20.FeatureCollectionType;
import net.opengis.wfs20.FeatureTypeListType;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.WFSCapabilitiesType;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.capability.FilterCapabilities;
import org.geotools.api.filter.capability.Operator;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.util.URLs;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.StreamingParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.w3c.dom.Document;

public class WFS_2_0_0_ParsingTest {

    Configuration configuration;

    @Before
    public void setUp() throws Exception {}

    @Test
    public void testParseEmptyGetCapabilities() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<WFS_Capabilities "
                + "  version=\"2.0.0\" "
                + "  xmlns=\"http://www.opengis.net/wfs/2.0\""
                + "  xmlns:fes=\"http://www.opengis.net/fes/2.0\""
                + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + "  xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\">"
                + "</WFS_Capabilities>";

        configuration = new org.geotools.wfs.v2_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed = parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Assert.assertNotNull(parsed);
        Assert.assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        Assert.assertNotNull(caps);
        Assert.assertEquals("2.0.0", caps.getVersion());
    }

    @Test
    public void testParseGetCapabilities() throws Exception {
        configuration = new org.geotools.wfs.v2_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed = parser.parse(getClass().getResourceAsStream("geoserver-GetCapabilities_2_0_0.xml"));

        Assert.assertNotNull(parsed);
        Assert.assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        Assert.assertNotNull(caps);
        Assert.assertEquals("2.0.0", caps.getVersion());
        /*
                assertServiceIdentification(caps);
                assertOperationsMetadata(caps);
                assertFeatureTypeList(caps);
                assertFilterCapabilities(caps);
        */
    }

    @Test
    public void testParseGetCapabilitiesFMI() throws Exception {
        configuration = new org.geotools.wfs.v2_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed = parser.parse(getClass().getResourceAsStream("fmi-GetCapabilities_2_0_0.xml"));

        Assert.assertNotNull(parsed);
        Assert.assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        Assert.assertNotNull(caps);
        Assert.assertEquals("2.0.0", caps.getVersion());

        // test stored query parsing
        // TODO:
    }

    @Test
    public void testParseGetCapabilitiesCuzk() throws Exception {
        configuration = new org.geotools.wfs.v2_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed = parser.parse(getClass().getResourceAsStream("cuzk-GetCapabilities_2_0_0.xml"));

        Assert.assertNotNull(parsed);
        Assert.assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        Assert.assertNotNull(caps);
        Assert.assertEquals("2.0.0", caps.getVersion());

        FilterCapabilitiesType fct = caps.getFilterCapabilities();
        Assert.assertNotNull(fct);
    }

    void assertServiceIdentification(WFSCapabilitiesType caps) {
        ServiceIdentificationType sa = caps.getServiceIdentification();
        Assert.assertNotNull(sa);

        Assert.assertEquals(1, sa.getKeywords().size());

        KeywordsType keywords = (KeywordsType) sa.getKeywords().get(0);
        List<String> simpleKeywords = new ArrayList<>();
        for (Object o : keywords.getKeyword()) {
            LanguageStringType lst = (LanguageStringType) o;
            simpleKeywords.add(lst.getValue());
        }
        Assert.assertTrue(simpleKeywords.contains("WFS"));
        Assert.assertTrue(simpleKeywords.contains("WMS"));
        Assert.assertTrue(simpleKeywords.contains("GEOSERVER"));

        Assert.assertEquals("WFS", sa.getServiceType().getValue());
        Assert.assertEquals("2.0.0", sa.getServiceTypeVersion());
    }

    void assertOperationsMetadata(WFSCapabilitiesType caps) {
        OperationsMetadataType om = caps.getOperationsMetadata();
        Assert.assertNotNull(om);

        // assertEquals(6, om.getOperation().size());

        OperationType getCapsOp = (OperationType) om.getOperation().get(0);
        Assert.assertEquals("GetCapabilities", getCapsOp.getName());
        Assert.assertEquals(1, getCapsOp.getDCP().size());

        DCPType dcp1 = (DCPType) getCapsOp.getDCP().get(0);
        Assert.assertEquals(1, dcp1.getHTTP().getGet().size());
        Assert.assertEquals(1, dcp1.getHTTP().getPost().size());

        Assert.assertEquals(
                "http://localhost:8080/geoserver/wfs?get",
                ((RequestMethodType) dcp1.getHTTP().getGet().get(0)).getHref());
        Assert.assertEquals(
                "http://localhost:8080/geoserver/wfs?post",
                ((RequestMethodType) dcp1.getHTTP().getPost().get(0)).getHref());

        int i = 1;
        Assert.assertEquals(
                "DescribeFeatureType", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals("GetFeature", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals(
                "GetPropertyValue", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals(
                "ListStoredQueries", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals(
                "DescribeStoredQueries", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals(
                "CreateStoredQuery", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals(
                "DropStoredQuery", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals("LockFeature", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals(
                "GetFeatureWithLock", ((OperationType) om.getOperation().get(i++)).getName());
        Assert.assertEquals("Transaction", ((OperationType) om.getOperation().get(i++)).getName());
    }

    void assertFeatureTypeList(WFSCapabilitiesType caps) {
        FeatureTypeListType ftl = caps.getFeatureTypeList();
        Assert.assertNotNull(ftl);

        Assert.assertEquals(14, ftl.getFeatureType().size());

        FeatureTypeType featureType = ftl.getFeatureType().get(0);
        Assert.assertEquals("poly_landmarks", featureType.getName().getLocalPart());
        Assert.assertEquals("tiger", featureType.getName().getPrefix());
        Assert.assertEquals("http://www.census.gov", featureType.getName().getNamespaceURI());

        Assert.assertEquals("urn:ogc:def:crs:EPSG::4326", featureType.getDefaultCRS());

        List<WGS84BoundingBoxType> wgs84BoundingBox = featureType.getWGS84BoundingBox();
        Assert.assertEquals(1, wgs84BoundingBox.size());

        WGS84BoundingBoxType bbox = wgs84BoundingBox.get(0);
        Assert.assertEquals("EPSG:4326", bbox.getCrs());
        Assert.assertEquals(BigInteger.valueOf(2), bbox.getDimensions());

        Assert.assertEquals(-74.047185D, (Double) bbox.getLowerCorner().get(0), 1E-6);
        Assert.assertEquals(40.679648D, (Double) bbox.getLowerCorner().get(1), 1E-6);
        Assert.assertEquals(-73.90782D, (Double) bbox.getUpperCorner().get(0), 1E-6);
        Assert.assertEquals(40.882078D, (Double) bbox.getUpperCorner().get(1), 1E-6);
    }

    void assertFilterCapabilities(WFSCapabilitiesType caps) {
        FilterCapabilities fc = caps.getFilterCapabilities();

        Assert.assertNotNull(fc.getSpatialCapabilities());
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators());
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperators());
        Assert.assertEquals(
                11,
                fc.getSpatialCapabilities().getSpatialOperators().getOperators().size());

        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Disjoint"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Equals"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("DWithin"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Beyond"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Intersect"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Touches"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Crosses"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Within"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Contains"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Overlaps"));
        Assert.assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("BBOX"));

        Assert.assertNotNull(fc.getScalarCapabilities());
        Assert.assertTrue(fc.getScalarCapabilities().hasLogicalOperators());
        Assert.assertNotNull(fc.getScalarCapabilities().getArithmeticOperators());
        Assert.assertNotNull(fc.getScalarCapabilities().getComparisonOperators());

        Assert.assertTrue(fc.getScalarCapabilities().getArithmeticOperators().hasSimpleArithmetic());
        Assert.assertNotNull(fc.getScalarCapabilities().getArithmeticOperators().getFunctions());
        Assert.assertEquals(
                7,
                fc.getScalarCapabilities()
                        .getArithmeticOperators()
                        .getFunctions()
                        .getFunctionNames()
                        .size());

        Collection<Operator> operators =
                fc.getScalarCapabilities().getComparisonOperators().getOperators();

        Assert.assertEquals(3, operators.size()); // "Simple_Comparisons" is commented out on
        // purpose

        Assert.assertNotNull(fc.getScalarCapabilities().getComparisonOperators().getOperator("Between"));
        Assert.assertNotNull(fc.getScalarCapabilities().getComparisonOperators().getOperator("Like"));
        Assert.assertNotNull(fc.getScalarCapabilities().getComparisonOperators().getOperator("NullCheck"));
    }

    @Test
    @Ignore
    public void testParseDescribeFeatureType() throws Exception {
        String loc = getClass().getResource("geoserver-DescribeFeatureType.xml").getFile();
        XSDSchema schema = Schemas.parse(loc);

        Assert.assertNotNull(schema);
        final String targetNs = "http://cite.opengeospatial.org/gmlsf";
        final String featureName = "PrimitiveGeoFeature";
        QName name = new QName(targetNs, featureName);
        XSDElementDeclaration elementDeclaration = Schemas.getElementDeclaration(schema, name);
        Assert.assertNotNull(elementDeclaration);

        XSDComplexTypeDefinition type = (XSDComplexTypeDefinition) elementDeclaration.getType();

        Assert.assertEquals("PrimitiveGeoFeatureType", type.getName());
        Assert.assertEquals(targetNs, type.getTargetNamespace());
    }

    @Test
    @Ignore
    public void testParseGetFeature() throws Exception {
        File tmp = File.createTempFile("geoserver-DescribeFeatureType", "xml");
        tmp.deleteOnExit();

        try (InputStream in = getClass().getResourceAsStream("geoserver-DescribeFeatureType.xml")) {
            Files.copy(in, tmp.toPath());
        }

        try (InputStream in = getClass().getResourceAsStream("geoserver-GetFeature.xml")) {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(in);

            // http://cite.opengeospatial.org/gmlsf
            // http://localhost:8080/geoserver/wfs?service=WFS&amp;version=1.1.0&amp;request=DescribeFeatureType&amp;typeName=sf:PrimitiveGeoFeature
            String schemaLocation = doc.getDocumentElement()
                    .getAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation");
            String absolutePath = URLs.fileToUrl(tmp).toExternalForm();

            schemaLocation = schemaLocation.replaceAll(
                    "http://cite.opengeospatial.org/gmlsf .*", "http://cite.opengeospatial.org/gmlsf " + absolutePath);
            doc.getDocumentElement()
                    .setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation", schemaLocation);

            tmp = File.createTempFile("geoserver-GetFeature", "xml");
            tmp.deleteOnExit();

            Transformer tx = TransformerFactory.newInstance().newTransformer();
            tx.transform(new DOMSource(doc), new StreamResult(tmp));
        }

        try (InputStream in = new FileInputStream(tmp)) {

            Parser parser = new Parser(configuration);
            FeatureCollectionType fc = (FeatureCollectionType) parser.parse(in);
            Assert.assertNotNull(fc);

            List featureCollections = fc.getMember();
            Assert.assertEquals(1, featureCollections.size());

            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) featureCollections.get(0);
            Assert.assertEquals(5, featureCollection.size());

            try (SimpleFeatureIterator features = featureCollection.features()) {
                Assert.assertTrue(features.hasNext());

                SimpleFeature f = features.next();

                Assert.assertEquals("PrimitiveGeoFeature.f001", f.getID());
                Assert.assertNull(f.getDefaultGeometry());

                Assert.assertNotNull(f.getAttribute("pointProperty"));
                Point p = (Point) f.getAttribute("pointProperty");

                Assert.assertEquals(39.73245, p.getX(), 0.1);
                Assert.assertEquals(2.00342, p.getY(), 0.1);

                Object intProperty = f.getAttribute("intProperty");
                Assert.assertNotNull(intProperty);
                Assert.assertTrue(intProperty.getClass().getName(), intProperty instanceof BigInteger);

                Assert.assertEquals(BigInteger.valueOf(155), intProperty);
                Assert.assertEquals(new URI("http://www.opengeospatial.org/"), f.getAttribute("uriProperty"));
                Assert.assertEquals(Float.valueOf(12765.0f), f.getAttribute("measurand"));
                Assert.assertTrue(f.getAttribute("dateProperty") instanceof Date);
                Assert.assertEquals(BigDecimal.valueOf(5.03), f.getAttribute("decimalProperty"));
            }
        }
    }

    @Test
    @Ignore
    public void testParseGetFeatureStreaming() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("geoserver-GetFeature.xml")) {
            StreamingParser parser = new StreamingParser(configuration, in, SimpleFeature.class);

            int n = 0;

            while (parser.parse() != null) n++;

            Assert.assertEquals(5, n);
        }
    }
}
