/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v1_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.opengis.ows10.DCPType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.RequestMethodType;
import net.opengis.ows10.ServiceIdentificationType;
import net.opengis.ows10.WGS84BoundingBoxType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.InsertResultsType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.OperationsType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.WFSCapabilitiesType;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.util.URLs;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.StreamingParser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.Operator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class WFSParsingTest {

    Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new org.geotools.wfs.v1_0.WFSConfiguration_1_0();
    }

    @Test
    public void testParseEmptyGetCapabilities() throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<WFS_Capabilities "
                        + "  version=\"1.0.0\" "
                        + "  xmlns=\"http://www.opengis.net/wfs\""
                        + "  xmlns:ogc=\"http://www.opengis.net/ogc\""
                        + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                        + "  xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-capabilities.xsd\">"
                        + "</WFS_Capabilities>";

        configuration = new org.geotools.wfs.v1_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed = parser.parse(new ByteArrayInputStream(xml.getBytes()));
        assertNotNull(parsed);
        assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        assertNotNull(caps);
        assertEquals("1.0.0", caps.getVersion());
    }

    @Test
    public void testParseGetCapabilities() throws Exception {
        configuration = new org.geotools.wfs.v1_0.WFSCapabilitiesConfiguration();

        Parser parser = new Parser(configuration);
        Object parsed =
                parser.parse(getClass().getResourceAsStream("geoserver-GetCapabilities.xml"));

        assertNotNull(parsed);
        assertTrue(parsed.getClass().getName(), parsed instanceof WFSCapabilitiesType);
        WFSCapabilitiesType caps = (WFSCapabilitiesType) parsed;

        assertNotNull(caps);
        assertEquals("1.0.0", caps.getVersion());

        assertServiceIdentification(caps);
        assertOperationsMetadata(caps);
        assertFeatureTypeList(caps);
        assertFilterCapabilities(caps);
        assertFeatureTypeListOperations(caps);
    }

    /** TODO: fix me */
    @Test
    @Ignore
    public void testParseGetCapabilitiesDeegree() throws Exception {
        Parser parser = new Parser(configuration);
        WFSCapabilitiesType caps =
                (WFSCapabilitiesType)
                        parser.parse(getClass().getResourceAsStream("deegree-GetCapabilities.xml"));

        assertNotNull(caps);
        assertEquals("1.0.0", caps.getVersion());

        // assertServiceIdentification(caps);
        assertOperationsMetadataDeeGree(caps);
        // assertFeatureTypeList(caps);
        // assertFilterCapabilities(caps);
    }

    private void assertOperationsMetadataDeeGree(WFSCapabilitiesType caps) {
        OperationsMetadataType om = caps.getOperationsMetadata();
        assertNotNull(om);

        assertEquals(6, om.getOperation().size());

        OperationType getCapsOp = (OperationType) om.getOperation().get(2);
        assertEquals("GetCapabilities", getCapsOp.getName());
        assertEquals(1, getCapsOp.getDCP().size());

        DCPType dcp = (DCPType) getCapsOp.getDCP().get(0);
        assertEquals(1, dcp.getHTTP().getGet().size());
        assertEquals(1, dcp.getHTTP().getPost().size());

        assertEquals(
                "http://demo.deegree.org/deegree-wfs/services?",
                ((RequestMethodType) dcp.getHTTP().getGet().get(0)).getHref());
        assertEquals(
                "http://demo.deegree.org/deegree-wfs/services",
                ((RequestMethodType) dcp.getHTTP().getPost().get(0)).getHref());

        assertEquals("GetFeature", ((OperationType) om.getOperation().get(0)).getName());
        assertEquals("DescribeFeatureType", ((OperationType) om.getOperation().get(1)).getName());
        assertEquals("GetFeatureWithLock", ((OperationType) om.getOperation().get(3)).getName());
        assertEquals("LockFeature", ((OperationType) om.getOperation().get(4)).getName());
        assertEquals("Transaction", ((OperationType) om.getOperation().get(5)).getName());
    }

    void assertServiceIdentification(WFSCapabilitiesType caps) {
        ServiceIdentificationType sa = caps.getServiceIdentification();
        assertNotNull(sa);

        assertEquals(1, sa.getKeywords().size());

        KeywordsType keywords = (KeywordsType) sa.getKeywords().get(0);
        assertTrue(keywords.getKeyword().contains("WFS"));
        assertTrue(keywords.getKeyword().contains("NY"));
        assertTrue(keywords.getKeyword().contains("New York"));

        assertEquals("WFS", sa.getServiceType().getValue());
        assertEquals("1.0.0", sa.getServiceTypeVersion());
    }

    void assertOperationsMetadata(WFSCapabilitiesType caps) {
        OperationsMetadataType om = caps.getOperationsMetadata();
        assertNotNull(om);

        // assertEquals(6, om.getOperation().size());

        OperationType getCapsOp = (OperationType) om.getOperation().get(0);
        assertEquals("GetCapabilities", getCapsOp.getName());
        assertEquals(2, getCapsOp.getDCP().size());

        DCPType dcp1 = (DCPType) getCapsOp.getDCP().get(0);
        DCPType dcp2 = (DCPType) getCapsOp.getDCP().get(1);
        assertEquals(1, dcp1.getHTTP().getGet().size());
        assertEquals(1, dcp2.getHTTP().getPost().size());

        assertEquals(
                "http://localhost:8080/geoserver/wfs?request=GetCapabilities",
                ((RequestMethodType) dcp1.getHTTP().getGet().get(0)).getHref());
        assertEquals(
                "http://localhost:8080/geoserver/wfs",
                ((RequestMethodType) dcp2.getHTTP().getPost().get(0)).getHref());

        assertEquals("DescribeFeatureType", ((OperationType) om.getOperation().get(1)).getName());
        assertEquals(2, ((OperationType) om.getOperation().get(1)).getDCP().size());
        assertEquals("GetFeature", ((OperationType) om.getOperation().get(2)).getName());
        assertEquals(2, ((OperationType) om.getOperation().get(2)).getDCP().size());
        assertEquals("Transaction", ((OperationType) om.getOperation().get(3)).getName());
        assertEquals(2, ((OperationType) om.getOperation().get(3)).getDCP().size());
        assertEquals("LockFeature", ((OperationType) om.getOperation().get(4)).getName());
        assertEquals(2, ((OperationType) om.getOperation().get(4)).getDCP().size());
        assertEquals("GetFeatureWithLock", ((OperationType) om.getOperation().get(5)).getName());
        assertEquals(2, ((OperationType) om.getOperation().get(5)).getDCP().size());
    }

    void assertFeatureTypeListOperations(WFSCapabilitiesType caps) {
        OperationsType o = caps.getFeatureTypeList().getOperations();
        assertNotNull(o);

        assertEquals(5, o.getOperation().size());
        assertEquals("Query", ((net.opengis.wfs.OperationType) o.getOperation().get(3)).getName());
        assertEquals("Insert", ((net.opengis.wfs.OperationType) o.getOperation().get(0)).getName());
        assertEquals("Update", ((net.opengis.wfs.OperationType) o.getOperation().get(1)).getName());
        assertEquals("Delete", ((net.opengis.wfs.OperationType) o.getOperation().get(2)).getName());
        assertEquals("Lock", ((net.opengis.wfs.OperationType) o.getOperation().get(4)).getName());
    }

    void assertFeatureTypeList(WFSCapabilitiesType caps) {
        FeatureTypeListType ftl = caps.getFeatureTypeList();
        assertNotNull(ftl);

        assertEquals(3, ftl.getFeatureType().size());

        FeatureTypeType featureType = (FeatureTypeType) ftl.getFeatureType().get(0);
        assertEquals("poly_landmarks", featureType.getName().getLocalPart());
        assertEquals("tiger", featureType.getName().getPrefix());
        assertEquals("http://www.census.gov", featureType.getName().getNamespaceURI());

        assertEquals("EPSG:4326", featureType.getDefaultSRS());

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

    @Test
    @Ignore
    public void testParseDescribeFeatureType() throws Exception {
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
    @Test
    @Ignore
    public void testParseGetFeature() throws Exception {
        File tmp = File.createTempFile("geoserver-DescribeFeatureType", "xml");
        tmp.deleteOnExit();

        InputStream in = getClass().getResourceAsStream("geoserver-DescribeFeatureType.xml");
        copy(in, tmp);

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

        List featureCollections = fc.getFeature();
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
            assertEquals(Float.valueOf(12765.0f), f.getAttribute("measurand"));
            assertTrue(f.getAttribute("dateProperty") instanceof Date);
            assertEquals(BigDecimal.valueOf(5.03), f.getAttribute("decimalProperty"));
        } finally {
            features.close();
        }
    }

    void copy(InputStream in, File to) throws Exception {
        Writer writer = new BufferedWriter(new FileWriter(to));
        InputStreamReader reader = new InputStreamReader(in);

        int b = -1;

        while ((b = reader.read()) != -1) {
            writer.write(b);
        }

        writer.flush();
        writer.close();
    }

    @Test
    @Ignore
    public void testParseGetFeatureStreaming() throws Exception {
        InputStream in = getClass().getResourceAsStream("geoserver-GetFeature.xml");
        StreamingParser parser = new StreamingParser(configuration, in, SimpleFeature.class);

        int n = 0;

        while (parser.parse() != null) n++;

        assertEquals(5, n);
    }

    @Test
    public void testParseTransactionResponse()
            throws IOException, SAXException, ParserConfigurationException {
        Parser parser = new Parser(configuration);
        Object parsed = parser.parse(getClass().getResourceAsStream("transactionResponse.xml"));

        assertNotNull(parsed);
        assertTrue(parsed.getClass().getName(), parsed instanceof TransactionResponseType);

        TransactionResponseType response = (TransactionResponseType) parsed;

        InsertResultsType insert = response.getInsertResults();

        assertEquals(0, insert.getFeature().size());

        parsed = parser.parse(getClass().getResourceAsStream("transactionResponse2.xml"));

        assertNotNull(parsed);
        assertTrue(parsed.getClass().getName(), parsed instanceof TransactionResponseType);

        response = (TransactionResponseType) parsed;

        insert = response.getInsertResults();

        assertEquals(2, insert.getFeature().size());
        assertEquals(
                "fid1",
                ((InsertedFeatureType) insert.getFeature().get(0))
                        .getFeatureId()
                        .get(0)
                        .toString());
        assertEquals(
                "fid2",
                ((InsertedFeatureType) insert.getFeature().get(1))
                        .getFeatureId()
                        .get(0)
                        .toString());
    }
}
