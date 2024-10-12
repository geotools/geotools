/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps;

import net.opengis.ows11.AddressType;
import net.opengis.ows11.ContactType;
import net.opengis.ows11.DCPType;
import net.opengis.ows11.HTTPType;
import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.MetadataType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.RequestMethodType;
import net.opengis.ows11.ResponsiblePartySubsetType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.ServiceProviderType;
import net.opengis.wps10.LanguagesType1;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.WPSCapabilitiesType;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Test;

public class GetCapabilitiesTest {

    @Test
    public void testParse() throws Exception {
        WPSConfiguration wps = new WPSConfiguration();
        Parser parser = new Parser(wps);

        Object o = parser.parse(getClass().getResourceAsStream("20_wpsGetCapabilities_response.xml"));
        Assert.assertTrue(o instanceof WPSCapabilitiesType);

        WPSCapabilitiesType caps = (WPSCapabilitiesType) o;
        assertServiceIdentification(caps.getServiceIdentification());
        assertServiceProvider(caps.getServiceProvider());
        assertOperationsMetadata(caps.getOperationsMetadata());
        assertProcessOfferings(caps.getProcessOfferings());
        assertLanguages(caps.getLanguages());
    }

    /**
     * <ows:ServiceIdentification> <ows:Title>AAFC GDAS-based WPS server</ows:Title>
     * <ows:Abstract>AAFC GDAS-based WPS server developed for the OGC WPSie.</ows:Abstract>
     * <ows:Keywords> <ows:Keyword>WPS</ows:Keyword> <ows:Keyword>AAFC</ows:Keyword>
     * <ows:Keyword>geospatial</ows:Keyword> <ows:Keyword>geoprocessing</ows:Keyword>
     * </ows:Keywords> <ows:ServiceType>WPS</ows:ServiceType>
     * <ows:ServiceTypeVersion>1.0.0</ows:ServiceTypeVersion>
     * <ows:ServiceTypeVersion>0.4.0</ows:ServiceTypeVersion> <ows:Fees>NONE</ows:Fees>
     * <ows:AccessConstraints>NONE</ows:AccessConstraints> </ows:ServiceIdentification>
     */
    void assertServiceIdentification(ServiceIdentificationType si) {
        Assert.assertNotNull(si);

        Assert.assertEquals(1, si.getTitle().size());
        Assert.assertEquals(
                "AAFC GDAS-based WPS server",
                ((LanguageStringType) si.getTitle().get(0)).getValue());

        Assert.assertEquals(1, si.getAbstract().size());
        Assert.assertEquals(
                "AAFC GDAS-based WPS server developed for the OGC WPSie.",
                ((LanguageStringType) si.getAbstract().get(0)).getValue());

        Assert.assertEquals(1, si.getKeywords().size());
        KeywordsType kw = (KeywordsType) si.getKeywords().get(0);

        Assert.assertEquals(4, kw.getKeyword().size());
        Assert.assertEquals("WPS", ((LanguageStringType) kw.getKeyword().get(0)).getValue());
        Assert.assertEquals("AAFC", ((LanguageStringType) kw.getKeyword().get(1)).getValue());
        Assert.assertEquals("geospatial", ((LanguageStringType) kw.getKeyword().get(2)).getValue());
        Assert.assertEquals(
                "geoprocessing", ((LanguageStringType) kw.getKeyword().get(3)).getValue());

        Assert.assertNotNull(si.getServiceType());
        Assert.assertEquals("WPS", si.getServiceType().getValue());

        Assert.assertNotNull(si.getServiceTypeVersion());
        Assert.assertEquals("1.0.0", si.getServiceTypeVersion());
        //       assertEquals("0.4.0", si.getServiceTypeVersion().get(1));

        Assert.assertEquals("NONE", si.getFees());

        Assert.assertNotNull(si.getAccessConstraints());
        Assert.assertEquals("NONE", si.getAccessConstraints());
    }

    /**
     * <ows:ServiceProvider> <ows:ProviderName>Agriculture and Agri-Food Canada</ows:ProviderName>
     * <ows:ProviderSite xlink:href="http://gis.agr.gc.ca/"/> <ows:ServiceContact>
     * <ows:IndividualName>Peter Schut</ows:IndividualName> <ows:PositionName>Information System
     * Scientist</ows:PositionName> <ows:ContactInfo> <ows:Phone> <ows:Voice>+1 613
     * 759-1874</ows:Voice> <ows:Facsimile>+1 613 759-1937</ows:Facsimile> </ows:Phone>
     * <ows:Address> <ows:DeliveryPoint>Room 1135, Neatby Building, 960, Carling
     * Avenue</ows:DeliveryPoint> <ows:City>Ottawa</ows:City>
     * <ows:AdministrativeArea>ON</ows:AdministrativeArea> <ows:PostalCode>K1AOC6</ows:PostalCode>
     * <ows:Country>Canada</ows:Country>
     * <ows:ElectronicMailAddress>schutp@agr.gc.ca</ows:ElectronicMailAddress> </ows:Address>
     * </ows:ContactInfo> </ows:ServiceContact> </ows:ServiceProvider>
     */
    void assertServiceProvider(ServiceProviderType sp) {
        Assert.assertNotNull(sp);
        Assert.assertEquals("Agriculture and Agri-Food Canada", sp.getProviderName());
        Assert.assertEquals("http://gis.agr.gc.ca/", sp.getProviderSite().getHref());

        ResponsiblePartySubsetType sc = sp.getServiceContact();
        Assert.assertNotNull(sc);
        Assert.assertEquals("Peter Schut", sc.getIndividualName());

        ContactType ci = sc.getContactInfo();
        Assert.assertNotNull(ci);
        Assert.assertNotNull(ci.getPhone().getVoice());
        Assert.assertEquals("+1 613 759-1874", ci.getPhone().getVoice());
        Assert.assertNotNull(ci.getPhone().getFacsimile());
        Assert.assertEquals("+1 613 759-1937", ci.getPhone().getFacsimile());

        AddressType a = ci.getAddress();
        Assert.assertNotNull(a);
        Assert.assertNotNull(a.getDeliveryPoint());
        Assert.assertEquals("Room 1135, Neatby Building, 960, Carling Avenue", a.getDeliveryPoint());
        Assert.assertEquals("Ottawa", a.getCity());
        Assert.assertEquals("ON", a.getAdministrativeArea());
        Assert.assertEquals("Canada", a.getCountry());
        Assert.assertNotNull(a.getElectronicMailAddress());
        Assert.assertEquals("schutp@agr.gc.ca", a.getElectronicMailAddress());
    }

    /**
     * <ows:OperationsMetadata> <ows:Operation name="GetCapabilities"> <ows:DCP> <ows:HTTP> <ows:Get
     * xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS?"/> </ows:HTTP> </ows:DCP> </ows:Operation>
     * <ows:Operation name="DescribeProcess"> <ows:DCP> <ows:HTTP> <ows:Get
     * xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS?"/> <ows:Post
     * xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS"/> </ows:HTTP> </ows:DCP> </ows:Operation>
     * <ows:Operation name="Execute"> <ows:DCP> <ows:HTTP> <ows:Get
     * xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS?"/> <ows:Post
     * xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS"/> </ows:HTTP> </ows:DCP> </ows:Operation>
     * </ows:OperationsMetadata>
     */
    void assertOperationsMetadata(OperationsMetadataType om) {
        Assert.assertNotNull(om);

        Assert.assertEquals(3, om.getOperation().size());

        OperationType op = (OperationType) om.getOperation().get(0);
        Assert.assertEquals("GetCapabilities", op.getName());
        Assert.assertEquals(1, op.getDCP().size());

        HTTPType http = ((DCPType) op.getDCP().get(0)).getHTTP();
        Assert.assertEquals(1, http.getGet().size());
        Assert.assertEquals(
                "http://wms1.agr.gc.ca/GeoPS/GeoPS?",
                ((RequestMethodType) http.getGet().get(0)).getHref());

        op = (OperationType) om.getOperation().get(1);
        Assert.assertEquals("DescribeProcess", op.getName());
        Assert.assertEquals(1, op.getDCP().size());

        http = ((DCPType) op.getDCP().get(0)).getHTTP();
        Assert.assertEquals(1, http.getGet().size());
        Assert.assertEquals(
                "http://wms1.agr.gc.ca/GeoPS/GeoPS?",
                ((RequestMethodType) http.getGet().get(0)).getHref());
        Assert.assertEquals(1, http.getPost().size());
        Assert.assertEquals(
                "http://wms1.agr.gc.ca/GeoPS/GeoPS",
                ((RequestMethodType) http.getPost().get(0)).getHref());

        op = (OperationType) om.getOperation().get(2);
        Assert.assertEquals("Execute", op.getName());
        Assert.assertEquals(1, op.getDCP().size());

        http = ((DCPType) op.getDCP().get(0)).getHTTP();
        Assert.assertEquals(1, http.getGet().size());
        Assert.assertEquals(
                "http://wms1.agr.gc.ca/GeoPS/GeoPS?",
                ((RequestMethodType) http.getGet().get(0)).getHref());
        Assert.assertEquals(1, http.getPost().size());
        Assert.assertEquals(
                "http://wms1.agr.gc.ca/GeoPS/GeoPS",
                ((RequestMethodType) http.getPost().get(0)).getHref());
    }

    /**
     * <wps:ProcessOfferings> <wps:Process wps:processVersion="1">
     * <ows:Identifier>buffer</ows:Identifier> <ows:Title>Buffer a polygon feature</ows:Title>
     * <ows:Abstract>Buffer the polygon coordinates found in one GML stream by a given buffer
     * distance, and output the results in GML.</ows:Abstract> <ows:Metadata xlink:title="buffer" />
     * <ows:Metadata xlink:title="polygon" /> </wps:Process> </wps:ProcessOfferings>
     */
    void assertProcessOfferings(ProcessOfferingsType po) {
        Assert.assertNotNull(po);
        Assert.assertEquals(1, po.getProcess().size());

        ProcessBriefType pb = (ProcessBriefType) po.getProcess().get(0);
        Assert.assertNotNull(pb.getIdentifier());
        Assert.assertEquals("buffer", pb.getIdentifier().getValue());

        Assert.assertNotNull(pb.getTitle());
        Assert.assertEquals("Buffer a polygon feature", pb.getTitle().getValue());

        Assert.assertNotNull(pb.getAbstract());
        Assert.assertEquals(
                "Buffer  the polygon coordinates found in one GML stream by a given buffer "
                        + "distance, and output the results in GML.",
                pb.getAbstract().getValue());

        Assert.assertEquals(2, pb.getMetadata().size());
        Assert.assertEquals("buffer", ((MetadataType) pb.getMetadata().get(0)).getTitle());
        Assert.assertEquals("polygon", ((MetadataType) pb.getMetadata().get(1)).getTitle());
    }

    void assertLanguages(LanguagesType1 l) {
        Assert.assertNotNull(l.getDefault());
        Assert.assertEquals("en-CA", l.getDefault().getLanguage());

        Assert.assertEquals(2, l.getSupported().getLanguage().size());
        Assert.assertEquals("en-CA", l.getSupported().getLanguage().get(0));
        Assert.assertEquals("fr-CA", l.getSupported().getLanguage().get(1));
    }
}
