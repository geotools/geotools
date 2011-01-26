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

import junit.framework.TestCase;
import net.opengis.ows11.AddressType;
import net.opengis.ows11.CodeType;
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

import org.geotools.xml.Parser;

public class GetCapabilitiesTest extends TestCase {

    public void testParse() throws Exception {
        WPSConfiguration wps = new WPSConfiguration();
        Parser parser = new Parser( wps );
        
        Object o = parser.parse( getClass().getResourceAsStream( "20_wpsGetCapabilities_response.xml"));
        assertTrue( o instanceof WPSCapabilitiesType);
        
        WPSCapabilitiesType caps = (WPSCapabilitiesType) o;
        assertServiceIdentification( caps.getServiceIdentification() );
        assertServiceProvider( caps.getServiceProvider() );
        assertOperationsMetadata( caps.getOperationsMetadata() );
        assertProcessOfferings( caps.getProcessOfferings() );
        assertLanguages( caps.getLanguages() );
    }
    
    /**
     * <ows:ServiceIdentification>
     *           <ows:Title>AAFC GDAS-based WPS server</ows:Title>
     *           <ows:Abstract>AAFC GDAS-based WPS server developed for the OGC WPSie.</ows:Abstract>
     *           <ows:Keywords>
     *                   <ows:Keyword>WPS</ows:Keyword>
     *                   <ows:Keyword>AAFC</ows:Keyword>
     *                   <ows:Keyword>geospatial</ows:Keyword>
     *                   <ows:Keyword>geoprocessing</ows:Keyword>
     *           </ows:Keywords>
     *           <ows:ServiceType>WPS</ows:ServiceType>
     *           <ows:ServiceTypeVersion>1.0.0</ows:ServiceTypeVersion>
     *           <ows:ServiceTypeVersion>0.4.0</ows:ServiceTypeVersion>
     *           <ows:Fees>NONE</ows:Fees>
     *           <ows:AccessConstraints>NONE</ows:AccessConstraints>
     *   </ows:ServiceIdentification>
     */
    void assertServiceIdentification( ServiceIdentificationType si ) {
        assertNotNull( si );
        
        assertEquals( 1, si.getTitle().size() );
        assertEquals( "AAFC GDAS-based WPS server", ((LanguageStringType) si.getTitle().get(0)).getValue() );
        
        assertEquals( 1, si.getAbstract().size() );
        assertEquals( "AAFC GDAS-based WPS server developed for the OGC WPSie.", ((LanguageStringType) si.getAbstract().get(0)).getValue() );

        assertEquals( 1, si.getKeywords().size() );
        KeywordsType kw = (KeywordsType) si.getKeywords().get( 0 );
        
        assertEquals( 4, kw.getKeyword().size() );
        assertEquals( "WPS", ((LanguageStringType)kw.getKeyword().get(0)).getValue() );
        assertEquals( "AAFC", ((LanguageStringType)kw.getKeyword().get(1)).getValue() );
        assertEquals( "geospatial", ((LanguageStringType)kw.getKeyword().get(2)).getValue() );
        assertEquals( "geoprocessing", ((LanguageStringType)kw.getKeyword().get(3)).getValue() );
        
        assertNotNull( si.getServiceType() );
        assertEquals( "WPS", si.getServiceType().getValue() );
        
        assertEquals( 2, si.getServiceTypeVersion().size() );
        assertEquals( "1.0.0", si.getServiceTypeVersion().get(0) );
        assertEquals( "0.4.0", si.getServiceTypeVersion().get(1) );
        
        assertEquals( "NONE", si.getFees() );
        
        assertEquals( 1, si.getAccessConstraints().size() );
        assertEquals( "NONE", si.getAccessConstraints().get(0) );
    }
    
    /**
     *  <ows:ServiceProvider>
     *           <ows:ProviderName>Agriculture and Agri-Food Canada</ows:ProviderName>
     *           <ows:ProviderSite xlink:href="http://gis.agr.gc.ca/"/>
     *           <ows:ServiceContact>
     *                   <ows:IndividualName>Peter Schut</ows:IndividualName>
     *                   <ows:PositionName>Information System Scientist</ows:PositionName>
     *                   <ows:ContactInfo>
     *                           <ows:Phone>
     *                                   <ows:Voice>+1 613 759-1874</ows:Voice>
     *                                   <ows:Facsimile>+1 613 759-1937</ows:Facsimile>
     *                           </ows:Phone>
     *                           <ows:Address>
     *                                   <ows:DeliveryPoint>Room 1135, Neatby Building, 960, Carling Avenue</ows:DeliveryPoint>
     *                                   <ows:City>Ottawa</ows:City>
     *                                   <ows:AdministrativeArea>ON</ows:AdministrativeArea>
     *                                   <ows:PostalCode>K1AOC6</ows:PostalCode>
     *                                   <ows:Country>Canada</ows:Country>
     *                                   <ows:ElectronicMailAddress>schutp@agr.gc.ca</ows:ElectronicMailAddress>
     *                           </ows:Address>
     *                   </ows:ContactInfo>
     *           </ows:ServiceContact>
     *   </ows:ServiceProvider>
     * 
     */
    void assertServiceProvider( ServiceProviderType sp ) {
        assertNotNull( sp );
        assertEquals( "Agriculture and Agri-Food Canada", sp.getProviderName() );
        assertEquals( "http://gis.agr.gc.ca/", sp.getProviderSite().getHref() );
        
        ResponsiblePartySubsetType sc = sp.getServiceContact();
        assertNotNull( sc );
        assertEquals( "Peter Schut", sc.getIndividualName() );
        
        ContactType ci = sc.getContactInfo();
        assertNotNull( ci );
        assertEquals( 1, ci.getPhone().getVoice().size() );
        assertEquals( "+1 613 759-1874", ci.getPhone().getVoice().get( 0 ) );
        assertEquals( 1, ci.getPhone().getFacsimile().size() );
        assertEquals( "+1 613 759-1937", ci.getPhone().getFacsimile().get( 0 ) );

        AddressType a = ci.getAddress();
        assertNotNull( a );
        assertEquals( 1, a.getDeliveryPoint().size() );
        assertEquals( "Room 1135, Neatby Building, 960, Carling Avenue", a.getDeliveryPoint().get( 0 ) );
        assertEquals( "Ottawa", a.getCity() );
        assertEquals( "ON", a.getAdministrativeArea() );
        assertEquals( "Canada", a.getCountry() );
        assertEquals( 1, a.getElectronicMailAddress().size() );
        assertEquals( "schutp@agr.gc.ca", a.getElectronicMailAddress().get(0) );
    }
    
    /**
     * <ows:OperationsMetadata>
     *           <ows:Operation name="GetCapabilities">
     *                   <ows:DCP>
     *                           <ows:HTTP>
     *                                  <ows:Get xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS?"/>
     *                           </ows:HTTP>
     *                   </ows:DCP>
     *           </ows:Operation>
     *           <ows:Operation name="DescribeProcess">
     *                  <ows:DCP>
     *                           <ows:HTTP>
     *                                   <ows:Get xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS?"/>
     *                                   <ows:Post xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS"/>
     *                           </ows:HTTP>
     *                   </ows:DCP>
     *           </ows:Operation>
     *           <ows:Operation name="Execute">
     *                   <ows:DCP>
     *                           <ows:HTTP>
     *                                   <ows:Get xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS?"/>
     *                                   <ows:Post xlink:href="http://wms1.agr.gc.ca/GeoPS/GeoPS"/>
     *                           </ows:HTTP>
     *                   </ows:DCP>
     *           </ows:Operation>
     *   </ows:OperationsMetadata>
     */
    void assertOperationsMetadata( OperationsMetadataType om ) {
        assertNotNull( om );
        
        assertEquals( 3, om.getOperation().size() );
        
        OperationType op = (OperationType) om.getOperation().get( 0 );
        assertEquals( "GetCapabilities", op.getName() );
        assertEquals( 1, op.getDCP().size() );
        
        HTTPType http = ((DCPType)op.getDCP().get(0)).getHTTP();
        assertEquals( 1, http.getGet().size() );
        assertEquals( "http://wms1.agr.gc.ca/GeoPS/GeoPS?", ((RequestMethodType)http.getGet().get(0)).getHref() );
    
        op = (OperationType) om.getOperation().get( 1 );
        assertEquals( "DescribeProcess", op.getName() );
        assertEquals( 1, op.getDCP().size() );
        
        http = ((DCPType)op.getDCP().get(0)).getHTTP();
        assertEquals( 1, http.getGet().size() );
        assertEquals( "http://wms1.agr.gc.ca/GeoPS/GeoPS?", ((RequestMethodType)http.getGet().get(0)).getHref() );
        assertEquals( 1, http.getPost().size() );
        assertEquals( "http://wms1.agr.gc.ca/GeoPS/GeoPS", ((RequestMethodType)http.getPost().get(0)).getHref() );

        op = (OperationType) om.getOperation().get( 2 );
        assertEquals( "Execute", op.getName() );
        assertEquals( 1, op.getDCP().size() );
        
        http = ((DCPType)op.getDCP().get(0)).getHTTP();
        assertEquals( 1, http.getGet().size() );
        assertEquals( "http://wms1.agr.gc.ca/GeoPS/GeoPS?", ((RequestMethodType)http.getGet().get(0)).getHref() );
        assertEquals( 1, http.getPost().size() );
        assertEquals( "http://wms1.agr.gc.ca/GeoPS/GeoPS", ((RequestMethodType)http.getPost().get(0)).getHref() );
    }
    
    /**
     * <wps:ProcessOfferings>
     *           <wps:Process wps:processVersion="1">
     *                   <ows:Identifier>buffer</ows:Identifier>
     *                   <ows:Title>Buffer a polygon feature</ows:Title>
     *                   <ows:Abstract>Buffer  the polygon coordinates found in one GML stream by a given buffer distance, and output the results in GML.</ows:Abstract>
     *                   <ows:Metadata xlink:title="buffer" />
     *                   <ows:Metadata xlink:title="polygon" />
     *           </wps:Process>
     *   </wps:ProcessOfferings>
     */
    void assertProcessOfferings( ProcessOfferingsType po ) {
        assertNotNull( po );
        assertEquals( 1, po.getProcess().size() );
        
        ProcessBriefType pb = (ProcessBriefType) po.getProcess().get( 0 );
        assertNotNull( pb.getIdentifier() );
        assertEquals( "buffer", ((CodeType) pb.getIdentifier()).getValue() );
        
        assertNotNull( pb.getTitle() );
        assertEquals( "Buffer a polygon feature", pb.getTitle().getValue() );
    
        assertNotNull( pb.getAbstract() );
        assertEquals( "Buffer  the polygon coordinates found in one GML stream by a given buffer distance, and output the results in GML.", pb.getAbstract().getValue() );
    
        assertEquals( 2, pb.getMetadata().size() );
        assertEquals( "buffer", ((MetadataType)pb.getMetadata().get(0)).getTitle() );
        assertEquals( "polygon", ((MetadataType)pb.getMetadata().get(1)).getTitle() );
    }
    
    void assertLanguages( LanguagesType1 l ) {
        assertNotNull( l.getDefault() );
        assertEquals( "en-CA", l.getDefault().getLanguage() );
       
        assertEquals( 2, l.getSupported().getLanguage().size() );
        assertEquals( "en-CA", l.getSupported().getLanguage().get( 0 ) );
        assertEquals( "fr-CA", l.getSupported().getLanguage().get( 1 ) );
    }
}
