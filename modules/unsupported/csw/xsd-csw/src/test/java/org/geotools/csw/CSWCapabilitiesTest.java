package org.geotools.csw;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.cat.csw20.CapabilitiesType;
import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.GetCapabilitiesType;
import net.opengis.ows10.ContactType;
import net.opengis.ows10.DCPType;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.RequestMethodType;
import net.opengis.ows10.ResponsiblePartySubsetType;
import net.opengis.ows10.SectionsType;
import net.opengis.ows10.ServiceIdentificationType;
import net.opengis.ows10.ServiceProviderType;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.geotools.filter.v1_1.OGC;
import org.geotools.gml2.GML;
import org.geotools.ows.OWS;
import org.geotools.xlink.XLINK;
import org.geotools.xml.Encoder;
import org.geotools.xml.EncoderDelegate;
import org.geotools.xml.Parser;
import org.junit.Test;
import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.IdCapabilities;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperators;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;


public class CSWCapabilitiesTest {
    
    Parser parser = new Parser(new CSWConfiguration());

    @Test
    public void testParseCapabilitiesRequest() throws Exception {
        String capRequestPath = "GetCapabilities.xml";
        GetCapabilitiesType caps = (GetCapabilitiesType) parser.parse(getClass().getResourceAsStream(capRequestPath));
        assertEquals("CSW", caps.getService());
        
        List versions = caps.getAcceptVersions().getVersion();
        assertEquals("2.0.2", versions.get(0));
        assertEquals("2.0.0", versions.get(1));
        assertEquals("0.7.2", versions.get(2));
        
        List sections = caps.getSections().getSection();
        assertEquals("OperationsMetadata", sections.get(0));
    }
    
    @Test
    public void testParseCapabilities() throws Exception {
        CapabilitiesType caps = (CapabilitiesType) parser.parse(getClass().getResourceAsStream("Capabilities.xml"));
        assertEquals("2.0.2", caps.getVersion());
        
        ServiceIdentificationType si = caps.getServiceIdentification();
        assertEquals("con terra GmbH Catalogue Server", si.getTitle());
        assertEquals("terraCatalog 2.1 - Web based Catalogue Service \n" + 
        		"        (CS-W 2.0.0/AP ISO19115/19 0.9.3 (DE-Profil 1.0.1)) for service, datasets and applications", si.getAbstract());
        KeywordsType keywords = (KeywordsType) si.getKeywords().get(0);
        assertEquals("CS-W", keywords.getKeyword().get(0));
        assertEquals("ISO19119", keywords.getKeyword().get(1));
        assertEquals("http://www.conterra.de", keywords.getType().getCodeSpace());
        assertEquals("theme", keywords.getType().getValue());
        assertEquals("CSW", si.getServiceType().getValue());
        // minor trouble here, this should be a list, not a string
        assertEquals("2.0.2", si.getServiceTypeVersion());
        
        ServiceProviderType sp = caps.getServiceProvider();
        assertEquals("con terra GmbH", sp.getProviderName());
        assertEquals("http://www.conterra.de", sp.getProviderSite().getHref());
        ResponsiblePartySubsetType rp = sp.getServiceContact();
        assertEquals("Markus Neteler", rp.getIndividualName());
        assertEquals("GRASS leader", rp.getPositionName());
        ContactType ci = rp.getContactInfo();
        assertEquals("+49-251-7474-400", ci.getPhone().getVoice());
        assertEquals("Marting-Luther-King-Weg 24", ci.getAddress().getDeliveryPoint());
        assertEquals("Muenster", ci.getAddress().getCity());
        assertEquals("mailto:conterra@conterra.de", ci.getOnlineResource().getHref());
        
        OperationsMetadataType opm = caps.getOperationsMetadata();
        assertEquals(6, opm.getOperation().size());
        OperationType gr = (OperationType) opm.getOperation().get(0);
        assertEquals("GetRecords", gr.getName());
        DCPType dcp = (DCPType) gr.getDCP().get(0);
        RequestMethodType rm = (RequestMethodType) dcp.getHTTP().getPost().get(0);
        assertEquals("http://tc22-test:9090/soapService/services/CSWDiscovery", rm.getHref());
        assertEquals(6, gr.getParameter().size());
        DomainType param = (DomainType) gr.getParameter().get(0);
        assertEquals("TypeName", param.getName());
        assertEquals("gmd:MD_Metadata", param.getValue().get(0));
        assertEquals("csw:Record", param.getValue().get(1));
        assertEquals(2, gr.getConstraint().size());
        DomainType ct = (DomainType) gr.getConstraint().get(0);
        assertEquals("SupportedISOQueryables", ct.getName());
        assertEquals(25, ct.getValue().size());
        assertEquals("RevisionDate", ct.getValue().get(0));
        assertEquals("OperatesOnWithOpName", ct.getValue().get(24));
        
        /** This fails, caps are not getting parsed **/
        FilterCapabilities filterCapabilities = caps.getFilterCapabilities();
        assertNotNull(filterCapabilities);
        SpatialCapabilities spatial = filterCapabilities.getSpatialCapabilities();
        Collection<GeometryOperand> geoms = spatial.getGeometryOperands();
        assertEquals(4, geoms.size());
        assertTrue(geoms.contains(GeometryOperand.Envelope));
        assertTrue(geoms.contains(GeometryOperand.Polygon));
        assertTrue(geoms.contains(GeometryOperand.LineString));
        assertTrue(geoms.contains(GeometryOperand.Point));
        SpatialOperators sop = spatial.getSpatialOperators();
        assertEquals(11, sop.getOperators().size());
        assertNotNull(sop.getOperator("Crosses"));
        assertNotNull(sop.getOperator("Overlaps"));
        assertNotNull(sop.getOperator("BBOX"));
        assertNotNull(sop.getOperator("Touches"));
        assertNotNull(sop.getOperator("Intersects"));
        assertNotNull(sop.getOperator("Equals"));
        assertNotNull(sop.getOperator("Within"));
        assertNotNull(sop.getOperator("Contains"));
        assertNotNull(sop.getOperator("DWithin"));
        assertNotNull(sop.getOperator("Beyond"));
        ScalarCapabilities scalar = filterCapabilities.getScalarCapabilities();
        assertTrue(scalar.hasLogicalOperators());
        ComparisonOperators comparison = scalar.getComparisonOperators();
        assertEquals(9, comparison.getOperators().size());
        assertNotNull(comparison.getOperator("NullCheck"));
        assertNotNull(comparison.getOperator("Between"));
        assertNotNull(comparison.getOperator("LessThan"));
        assertNotNull(comparison.getOperator("GreaterThan"));
        assertNotNull(comparison.getOperator("GreaterThanEqualTo"));
        assertNotNull(comparison.getOperator("EqualTo"));
        assertNotNull(comparison.getOperator("NotEqualTo"));
        assertNotNull(comparison.getOperator("Like"));
        assertNotNull(comparison.getOperator("LessThanEqualTo"));
        ArithmeticOperators arithmetic = scalar.getArithmeticOperators();
        assertEquals(0, arithmetic.getFunctions().getFunctionNames().size());
        IdCapabilities id = filterCapabilities.getIdCapabilities();
        assertTrue(id.hasFID());
        assertFalse(id.hasEID());
    }
    
    @Test
    public void testRoundTripCapabilities() throws Exception {
        CapabilitiesType caps = (CapabilitiesType) parser.parse(getClass().getResourceAsStream("Capabilities.xml"));

        Encoder encoder = new Encoder(new CSWConfiguration());
        encoder.setIndenting(true);
        encoder.setNamespaceAware(true);
        encoder.getNamespaces().declarePrefix("ows", OWS.NAMESPACE);
        encoder.getNamespaces().declarePrefix("ogc", OGC.NAMESPACE);
        encoder.getNamespaces().declarePrefix("gml", GML.NAMESPACE);
        String encoded = encoder.encodeAsString(caps, CSW.Capabilities);
        // System.out.println(encoded);
        
        CapabilitiesType reParsed = (CapabilitiesType) parser.parse(new StringReader(encoded));
        assertTrue(EMFUtils.emfEquals(caps, reParsed));
    }
    
    @Test 
    @SuppressWarnings("unchecked")
    public void testExtendedCapabilities() throws Exception {
        Csw20Factory cswf = Csw20Factory.eINSTANCE;
        Ows10Factory owsf = Ows10Factory.eINSTANCE;
        CapabilitiesType caps = cswf.createCapabilitiesType();
        OperationsMetadataType om = owsf.createOperationsMetadataType();
        caps.setOperationsMetadata(om);
        final String rimNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
        om.setExtendedCapabilities(new EncoderDelegate() {
            
            @Override
            public void encode(ContentHandler output) throws Exception {
                AttributesImpl atts = new AttributesImpl();
                output.startPrefixMapping("rim", rimNamespace);
                atts.addAttribute(rimNamespace, "test", "rim:test", "xs:string", "test");
                output.startElement(rimNamespace, "Slot", "rim:Slot", atts);
                String content = "test content";
                output.characters(content.toCharArray(), 0, content.length());
                output.endElement(rimNamespace, "test", "rim:test");
                output.endPrefixMapping("rim");
            }
        });
        
        Encoder encoder = new Encoder(new CSWConfiguration());
        encoder.setIndenting(true);
        encoder.setNamespaceAware(true);
        encoder.getNamespaces().declarePrefix("ows", OWS.NAMESPACE);
        encoder.getNamespaces().declarePrefix("ogc", OGC.NAMESPACE);
        encoder.getNamespaces().declarePrefix("gml", GML.NAMESPACE);
        String encoded = encoder.encodeAsString(caps, CSW.Capabilities);
        
        // System.out.println(encoded);

        // prepare xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("csw", CSW.NAMESPACE);
        namespaces.put("ows", OWS.NAMESPACE);
        namespaces.put("rim", rimNamespace);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
        
        Document doc = XMLUnit.buildControlDocument(encoded);
        XMLAssert.assertXpathEvaluatesTo("test", "/csw:Capabilities/ows:OperationsMetadata/rim:Slot/@rim:test", doc);
        XMLAssert.assertXpathEvaluatesTo("test content", "/csw:Capabilities/ows:OperationsMetadata/rim:Slot", doc);
    }
}
