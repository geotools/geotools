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
import junit.framework.TestCase;
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
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.util.URLs;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xml.Schemas;
import org.geotools.xml.StreamingParser;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.Operator;
import org.w3c.dom.Document;

/** @source $URL$ */
public class WFS_2_0_0_ParsingTest extends TestCase {

    Configuration configuration;

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testParseEmptyGetCapabilities() throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<WFS_Capabilities "
                        + "  version=\"2.0.0\" "
                        + "  xmlns=\"http://www.opengis.net/wfs/2.0\""
                        + "  xmlns:fes=\"http://www.opengis.net/fes/2.0\""
                        + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                        + "  xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\">"
                        + "</WFS_Capabilities>";

        configuration = new org.geotools.wfs.v2_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed = parser.parse(new ByteArrayInputStream(xml.getBytes()));
        assertNotNull(parsed);
        assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        assertNotNull(caps);
        assertEquals("2.0.0", caps.getVersion());
    }

    public void testParseGetCapabilities() throws Exception {
        configuration = new org.geotools.wfs.v2_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed =
                parser.parse(getClass().getResourceAsStream("geoserver-GetCapabilities_2_0_0.xml"));

        assertNotNull(parsed);
        assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        assertNotNull(caps);
        assertEquals("2.0.0", caps.getVersion());
        /*
                assertServiceIdentification(caps);
                assertOperationsMetadata(caps);
                assertFeatureTypeList(caps);
                assertFilterCapabilities(caps);
        */
    }

    public void testParseGetCapabilitiesFMI() throws Exception {
        configuration = new org.geotools.wfs.v2_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed =
                parser.parse(getClass().getResourceAsStream("fmi-GetCapabilities_2_0_0.xml"));

        assertNotNull(parsed);
        assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        assertNotNull(caps);
        assertEquals("2.0.0", caps.getVersion());

        // test stored query parsing
        // TODO:
    }

    public void testParseGetCapabilitiesCuzk() throws Exception {
        configuration = new org.geotools.wfs.v2_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed =
                parser.parse(getClass().getResourceAsStream("cuzk-GetCapabilities_2_0_0.xml"));

        assertNotNull(parsed);
        assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        assertNotNull(caps);
        assertEquals("2.0.0", caps.getVersion());

        FilterCapabilitiesType fct = caps.getFilterCapabilities();
        assertNotNull(fct);
    }

    void assertServiceIdentification(WFSCapabilitiesType caps) {
        ServiceIdentificationType sa = caps.getServiceIdentification();
        assertNotNull(sa);

        assertEquals(1, sa.getKeywords().size());

        KeywordsType keywords = (KeywordsType) sa.getKeywords().get(0);
        List<String> simpleKeywords = new ArrayList<String>();
        for (Object o : keywords.getKeyword()) {
            LanguageStringType lst = (LanguageStringType) o;
            simpleKeywords.add(lst.getValue());
        }
        assertTrue(simpleKeywords.contains("WFS"));
        assertTrue(simpleKeywords.contains("WMS"));
        assertTrue(simpleKeywords.contains("GEOSERVER"));

        assertEquals("WFS", sa.getServiceType().getValue());
        assertEquals("2.0.0", sa.getServiceTypeVersion().get(0));
    }

    void assertOperationsMetadata(WFSCapabilitiesType caps) {
        OperationsMetadataType om = caps.getOperationsMetadata();
        assertNotNull(om);

        // assertEquals(6, om.getOperation().size());

        OperationType getCapsOp = (OperationType) om.getOperation().get(0);
        assertEquals("GetCapabilities", getCapsOp.getName());
        assertEquals(1, getCapsOp.getDCP().size());

        DCPType dcp1 = (DCPType) getCapsOp.getDCP().get(0);
        assertEquals(1, dcp1.getHTTP().getGet().size());
        assertEquals(1, dcp1.getHTTP().getPost().size());

        assertEquals(
                "http://localhost:8080/geoserver/wfs?get",
                ((RequestMethodType) dcp1.getHTTP().getGet().get(0)).getHref());
        assertEquals(
                "http://localhost:8080/geoserver/wfs?post",
                ((RequestMethodType) dcp1.getHTTP().getPost().get(0)).getHref());

        int i = 1;
        assertEquals("DescribeFeatureType", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals("GetFeature", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals("GetPropertyValue", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals("ListStoredQueries", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals(
                "DescribeStoredQueries", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals("CreateStoredQuery", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals("DropStoredQuery", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals("LockFeature", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals("GetFeatureWithLock", ((OperationType) om.getOperation().get(i++)).getName());
        assertEquals("Transaction", ((OperationType) om.getOperation().get(i++)).getName());
    }

    void assertFeatureTypeList(WFSCapabilitiesType caps) {
        FeatureTypeListType ftl = caps.getFeatureTypeList();
        assertNotNull(ftl);

        assertEquals(14, ftl.getFeatureType().size());

        FeatureTypeType featureType = (FeatureTypeType) ftl.getFeatureType().get(0);
        assertEquals("poly_landmarks", featureType.getName().getLocalPart());
        assertEquals("tiger", featureType.getName().getPrefix());
        assertEquals("http://www.census.gov", featureType.getName().getNamespaceURI());

        assertEquals("urn:ogc:def:crs:EPSG::4326", featureType.getDefaultCRS());

        List<WGS84BoundingBoxType> wgs84BoundingBox = featureType.getWGS84BoundingBox();
        assertEquals(1, wgs84BoundingBox.size());

        WGS84BoundingBoxType bbox = wgs84BoundingBox.get(0);
        assertEquals("EPSG:4326", bbox.getCrs());
        assertEquals(BigInteger.valueOf(2), bbox.getDimensions());

        assertEquals(-74.047185D, (Double) bbox.getLowerCorner().get(0), 1E-6);
        assertEquals(40.679648D, (Double) bbox.getLowerCorner().get(1), 1E-6);
        assertEquals(-73.90782D, (Double) bbox.getUpperCorner().get(0), 1E-6);
        assertEquals(40.882078D, (Double) bbox.getUpperCorner().get(1), 1E-6);
    }

    void assertFilterCapabilities(WFSCapabilitiesType caps) {
        FilterCapabilities fc = (FilterCapabilities) caps.getFilterCapabilities();

        assertNotNull(fc.getSpatialCapabilities());
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators());
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperators());
        assertEquals(11, fc.getSpatialCapabilities().getSpatialOperators().getOperators().size());

        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Disjoint"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Equals"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("DWithin"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Beyond"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Intersect"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Touches"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Crosses"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Within"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Contains"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("Overlaps"));
        assertNotNull(fc.getSpatialCapabilities().getSpatialOperators().getOperator("BBOX"));

        assertNotNull(fc.getScalarCapabilities());
        assertTrue(fc.getScalarCapabilities().hasLogicalOperators());
        assertNotNull(fc.getScalarCapabilities().getArithmeticOperators());
        assertNotNull(fc.getScalarCapabilities().getComparisonOperators());

        assertTrue(fc.getScalarCapabilities().getArithmeticOperators().hasSimpleArithmetic());
        assertNotNull(fc.getScalarCapabilities().getArithmeticOperators().getFunctions());
        assertEquals(
                7,
                fc.getScalarCapabilities()
                        .getArithmeticOperators()
                        .getFunctions()
                        .getFunctionNames()
                        .size());

        Collection<Operator> operators =
                fc.getScalarCapabilities().getComparisonOperators().getOperators();

        assertEquals(3, operators.size()); // "Simple_Comparisons" is commented out on purpose

        assertNotNull(fc.getScalarCapabilities().getComparisonOperators().getOperator("Between"));
        assertNotNull(fc.getScalarCapabilities().getComparisonOperators().getOperator("Like"));
        assertNotNull(fc.getScalarCapabilities().getComparisonOperators().getOperator("NullCheck"));
    }

    public void _testParseDescribeFeatureType() throws Exception {
        String loc = getClass().getResource("geoserver-DescribeFeatureType.xml").getFile();
        XSDSchema schema = Schemas.parse(loc);

        assertNotNull(schema);
        final String targetNs = "http://cite.opengeospatial.org/gmlsf";
        final String featureName = "PrimitiveGeoFeature";
        QName name = new QName(targetNs, featureName);
        XSDElementDeclaration elementDeclaration = Schemas.getElementDeclaration(schema, name);
        assertNotNull(elementDeclaration);

        XSDComplexTypeDefinition type = (XSDComplexTypeDefinition) elementDeclaration.getType();

        assertEquals("PrimitiveGeoFeatureType", type.getName());
        assertEquals(targetNs, type.getTargetNamespace());
    }

    @SuppressWarnings("unchecked")
    public void _testParseGetFeature() throws Exception {
        File tmp = File.createTempFile("geoserver-DescribeFeatureType", "xml");
        tmp.deleteOnExit();

        InputStream in = getClass().getResourceAsStream("geoserver-DescribeFeatureType.xml");
        Files.copy(in, tmp.toPath());

        in = getClass().getResourceAsStream("geoserver-GetFeature.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(in);

        // http://cite.opengeospatial.org/gmlsf
        // http://localhost:8080/geoserver/wfs?service=WFS&amp;version=1.1.0&amp;request=DescribeFeatureType&amp;typeName=sf:PrimitiveGeoFeature
        String schemaLocation =
                doc.getDocumentElement()
                        .getAttributeNS(
                                "http://www.w3.org/2001/XMLSchema-instance", "schemaLocation");
        String absolutePath = URLs.fileToUrl(tmp).toExternalForm();

        schemaLocation =
                schemaLocation.replaceAll(
                        "http://cite.opengeospatial.org/gmlsf .*",
                        "http://cite.opengeospatial.org/gmlsf " + absolutePath);
        doc.getDocumentElement()
                .setAttributeNS(
                        "http://www.w3.org/2001/XMLSchema-instance",
                        "schemaLocation",
                        schemaLocation);

        tmp = File.createTempFile("geoserver-GetFeature", "xml");
        tmp.deleteOnExit();

        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new DOMSource(doc), new StreamResult(tmp));

        in = new FileInputStream(tmp);

        Parser parser = new Parser(configuration);
        FeatureCollectionType fc = (FeatureCollectionType) parser.parse(in);
        assertNotNull(fc);

        List featureCollections = fc.getMember();
        assertEquals(1, featureCollections.size());

        SimpleFeatureCollection featureCollection;
        featureCollection = (SimpleFeatureCollection) featureCollections.get(0);
        assertEquals(5, featureCollection.size());

        SimpleFeatureIterator features = featureCollection.features();

        try {
            assertTrue(features.hasNext());

            SimpleFeature f = features.next();

            assertEquals("PrimitiveGeoFeature.f001", f.getID());
            assertNull(f.getDefaultGeometry());

            assertNotNull(f.getAttribute("pointProperty"));
            Point p = (Point) f.getAttribute("pointProperty");

            assertEquals(39.73245, p.getX(), 0.1);
            assertEquals(2.00342, p.getY(), 0.1);

            Object intProperty = f.getAttribute("intProperty");
            assertNotNull(intProperty);
            assertTrue(intProperty.getClass().getName(), intProperty instanceof BigInteger);

            assertEquals(BigInteger.valueOf(155), intProperty);
            assertEquals(new URI("http://www.opengeospatial.org/"), f.getAttribute("uriProperty"));
            assertEquals(new Float(12765.0), f.getAttribute("measurand"));
            assertTrue(f.getAttribute("dateProperty") instanceof Date);
            assertEquals(BigDecimal.valueOf(5.03), f.getAttribute("decimalProperty"));
        } finally {
            features.close();
        }
    }

    public void _testParseGetFeatureStreaming() throws Exception {
        InputStream in = getClass().getResourceAsStream("geoserver-GetFeature.xml");
        StreamingParser parser = new StreamingParser(configuration, in, SimpleFeature.class);

        int n = 0;

        while (parser.parse() != null) n++;

        assertEquals(5, n);
    }
}
