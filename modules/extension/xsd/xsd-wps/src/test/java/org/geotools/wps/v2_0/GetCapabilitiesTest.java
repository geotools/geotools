package org.geotools.wps.v2_0;

import java.util.List;
import net.opengis.ows20.AddressType;
import net.opengis.ows20.ContactType;
import net.opengis.ows20.DCPType;
import net.opengis.ows20.HTTPType;
import net.opengis.ows20.KeywordsType;
import net.opengis.ows20.LanguageStringType;
import net.opengis.ows20.LanguagesType;
import net.opengis.ows20.OperationType;
import net.opengis.ows20.OperationsMetadataType;
import net.opengis.ows20.RequestMethodType;
import net.opengis.ows20.ResponsiblePartySubsetType;
import net.opengis.ows20.ServiceIdentificationType;
import net.opengis.ows20.ServiceProviderType;
import net.opengis.wps20.ProcessSummaryType;
import net.opengis.wps20.WPSCapabilitiesType;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Test;

public class GetCapabilitiesTest extends WPSTestSupport {

    @Test
    public void testParse() throws Exception {
        Parser parser = new Parser(createConfiguration());

        Object o =
                parser.parse(getClass().getResourceAsStream("wpsCapabilitiesDocumentExample.xml"));
        Assert.assertTrue(o instanceof WPSCapabilitiesType);

        WPSCapabilitiesType caps = (WPSCapabilitiesType) o;
        assertServiceIdentification(caps.getServiceIdentification());
        assertServiceProvider(caps.getServiceProvider());
        assertOperationsMetadata(caps.getOperationsMetadata());
        Assert.assertNotNull(caps.getContents());
        assertProcessSummaries(caps.getContents().getProcessSummary());
        assertLanguages(caps.getLanguages());
    }

    void assertServiceIdentification(ServiceIdentificationType si) {
        Assert.assertNotNull(si);

        Assert.assertEquals(1, si.getTitle().size());
        Assert.assertEquals(
                "MyWebProcessingService", ((LanguageStringType) si.getTitle().get(0)).getValue());

        Assert.assertEquals(1, si.getAbstract().size());
        Assert.assertEquals(
                "A Demo Service offering typical GIS distance transform processes",
                ((LanguageStringType) si.getAbstract().get(0)).getValue());

        Assert.assertEquals(1, si.getKeywords().size());
        KeywordsType kw = (KeywordsType) si.getKeywords().get(0);

        Assert.assertEquals(3, kw.getKeyword().size());
        Assert.assertEquals(
                "Geoprocessing", ((LanguageStringType) kw.getKeyword().get(0)).getValue());
        Assert.assertEquals("Toolbox", ((LanguageStringType) kw.getKeyword().get(1)).getValue());
        Assert.assertEquals(
                "Distance transform", ((LanguageStringType) kw.getKeyword().get(2)).getValue());

        Assert.assertNotNull(si.getServiceType());
        Assert.assertEquals("WPS", si.getServiceType().getValue());

        Assert.assertNotNull(si.getServiceTypeVersion());
        Assert.assertEquals("2.0.0", si.getServiceTypeVersion());

        Assert.assertEquals("NONE", si.getFees());

        Assert.assertNotNull(si.getAccessConstraints());
        Assert.assertEquals("NONE", si.getAccessConstraints());
    }

    void assertServiceProvider(ServiceProviderType sp) {
        Assert.assertNotNull(sp);
        Assert.assertEquals("TU Dresden", sp.getProviderName());
        Assert.assertEquals("http://tu-dresden.de/geo/gis", sp.getProviderSite().getHref());

        ResponsiblePartySubsetType sc = sp.getServiceContact();
        Assert.assertNotNull(sc);
        Assert.assertEquals("Matthias Mueller", sc.getIndividualName());

        ContactType ci = sc.getContactInfo();
        Assert.assertNotNull(ci);
        Assert.assertNotNull(ci.getPhone().getVoice());
        Assert.assertEquals("+1 613 759-1874", ci.getPhone().getVoice());
        Assert.assertNotNull(ci.getPhone().getFacsimile());
        Assert.assertEquals("+1 613 759-1937", ci.getPhone().getFacsimile());

        Assert.assertNotNull(ci.getAddress());
        AddressType a = ci.getAddress();
        Assert.assertNotNull(a);
        Assert.assertNotNull(a.getDeliveryPoint());
        Assert.assertEquals(
                "Room 1135, Neatby Building, 960, Carling Avenue", a.getDeliveryPoint());
        Assert.assertEquals("Ottawa", a.getCity());
        Assert.assertEquals("ON", a.getAdministrativeArea());
        Assert.assertEquals("Canada", a.getCountry());
        Assert.assertNotNull(a.getElectronicMailAddress());
        Assert.assertNotNull(
                "matthias_mueller@tu-dresden.de", ci.getAddress().getElectronicMailAddress());
    }

    void assertOperationsMetadata(OperationsMetadataType om) {
        Assert.assertNotNull(om);

        Assert.assertEquals(6, om.getOperation().size());
        assertOperationType(
                (OperationType) om.getOperation().get(0),
                "GetCapabilities",
                "http://wps1.gis.geo.tu-dresden.de/wps",
                true,
                false);
        assertOperationType(
                (OperationType) om.getOperation().get(1),
                "DescribeProcess",
                "http://wps1.gis.geo.tu-dresden.de/wps",
                true,
                true);
        assertOperationType(
                (OperationType) om.getOperation().get(2),
                "Execute",
                "http://wps1.gis.geo.tu-dresden.de/wps",
                false,
                true);
        assertOperationType(
                (OperationType) om.getOperation().get(3),
                "GetStatus",
                "http://wps1.gis.geo.tu-dresden.de/wps",
                true,
                true);
        assertOperationType(
                (OperationType) om.getOperation().get(4),
                "GetResult",
                "http://wps1.gis.geo.tu-dresden.de/wps",
                true,
                true);
        assertOperationType(
                (OperationType) om.getOperation().get(5),
                "Dismiss",
                "http://wps1.gis.geo.tu-dresden.de/wps",
                true,
                true);
    }

    void assertProcessSummaries(List<ProcessSummaryType> processSummary) {
        Assert.assertNotNull(processSummary);
        Assert.assertEquals(2, processSummary.size());

        ProcessSummaryType pb = (ProcessSummaryType) processSummary.get(0);
        Assert.assertNotNull(pb);
        Assert.assertNotNull(pb.getIdentifier());
        Assert.assertNotNull(
                "http://my.site/distance-transform/euclidean-distance",
                pb.getIdentifier().getValue());
        Assert.assertEquals("Euclidean Distance", pb.getTitle().get(0).getValue());

        Assert.assertNotNull(pb.getJobControlOptions());
        Assert.assertEquals("sync-execute", pb.getJobControlOptions().get(0));
        Assert.assertEquals("async-execute", pb.getJobControlOptions().get(1));
        Assert.assertEquals("dismiss", pb.getJobControlOptions().get(2));

        pb = (ProcessSummaryType) processSummary.get(1);
        Assert.assertNotNull(pb);
        Assert.assertNotNull(pb.getIdentifier());
        Assert.assertNotNull(
                "http://my.site/distance-transform/cost-distance", pb.getIdentifier().getValue());
        Assert.assertEquals("Cost Distance", pb.getTitle().get(0).getValue());
        Assert.assertNotNull("1.4.0", pb.getProcessVersion());
        Assert.assertNotNull(pb.getJobControlOptions());
        Assert.assertEquals("sync-execute", pb.getJobControlOptions().get(0));
        Assert.assertEquals("async-execute", pb.getJobControlOptions().get(1));
        Assert.assertEquals("dismiss", pb.getJobControlOptions().get(2));
    }

    void assertLanguages(LanguagesType languages) {
        Assert.assertNotNull(languages);
        Assert.assertEquals("fr-FR,ca-CA", languages.getLanguage());
    }

    public void assertOperationType(
            OperationType op,
            String operationName,
            String href,
            Boolean httpGet,
            Boolean httpPost) {
        Assert.assertEquals(operationName, op.getName());
        Assert.assertEquals(1, op.getDCP().size());

        HTTPType http = ((DCPType) op.getDCP().get(0)).getHTTP();
        if (httpGet) {
            Assert.assertEquals(1, http.getGet().size());
            Assert.assertEquals(href, ((RequestMethodType) http.getGet().get(0)).getHref());
        }
        if (httpPost) {
            Assert.assertEquals(1, http.getPost().size());
            Assert.assertEquals(href, ((RequestMethodType) http.getPost().get(0)).getHref());
        }
    }
}
