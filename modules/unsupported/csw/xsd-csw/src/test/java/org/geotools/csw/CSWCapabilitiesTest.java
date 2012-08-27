package org.geotools.csw;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import net.opengis.cat.csw20.CapabilitiesType;
import net.opengis.cat.csw20.GetCapabilitiesType;
import net.opengis.ows10.ContactType;
import net.opengis.ows10.DCPType;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.RequestMethodType;
import net.opengis.ows10.ResponsiblePartySubsetType;
import net.opengis.ows10.ServiceIdentificationType;
import net.opengis.ows10.ServiceProviderType;

import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.junit.Ignore;
import org.junit.Test;


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
        
        /** This fails, caps are not getting parsed
        FilterCapabilities filterCapabilities = caps.getFilterCapabilities();
        assertNotNull(filterCapabilities);
        **/
    }
    
    @Test
    @Ignore // does not work at the moment
    public void testRoundTripCapabilities() throws Exception {
        CapabilitiesType caps = (CapabilitiesType) parser.parse(getClass().getResourceAsStream("Capabilities.xml"));

        Encoder encoder = new Encoder(new CSWConfiguration());
        encoder.setIndenting(true);
        encoder.setNamespaceAware(true);
        encoder.getNamespaces().declarePrefix("ows", "http://www.opengis.net/ows");
        String encoded = encoder.encodeAsString(caps, CSW.Capabilities);
        System.out.println(encoded);
        
        CapabilitiesType reParsed = (CapabilitiesType) parser.parse(new StringReader(encoded));
        assertEquals(caps.getServiceIdentification(), reParsed.getServiceIdentification());
    }
}
