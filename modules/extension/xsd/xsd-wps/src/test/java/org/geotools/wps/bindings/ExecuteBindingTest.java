package org.geotools.wps.bindings;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.InputType;
import net.opengis.wps10.MethodType;

import org.geotools.wfs.WFS;
import org.geotools.wps.WPS;
import org.geotools.wps.WPSTestSupport;

/**
 * This actually tests other bindings as well, since there are few elements declared in
 * WPS and the parser ends up confusing snippets of Execute for something else (think Reference,
 * which is declared in OWS as an element, or Input, same). Se we need to have the parsing 
 * start from a root that can be found in the WPS schema (and in that alone)
 * @author Andrea Aime
 *
 */
public class ExecuteBindingTest extends WPSTestSupport {

    public void testParseCData() throws Exception {
        String body = "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\"\n" + 
        "  outputFormat=\"GML2\"\n" + 
        "  xmlns:topp=\"http://www.openplans.org/topp\"\n" + 
        "  xmlns:wfs=\"http://www.opengis.net/wfs\"\n" + 
        "  xmlns:ogc=\"http://www.opengis.net/ogc\"\n" + 
        "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
        "  xsi:schemaLocation=\"http://www.opengis.net/wfs\n" + 
        "                      http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">\n" + 
        "  <wfs:Query typeName=\"topp:states\">\n" + 
        "    <ogc:Filter>\n" + 
        "       <ogc:FeatureId fid=\"states.1\"/>\n" + 
        "    </ogc:Filter>\n" + 
        "    </wfs:Query>\n" + 
        "</wfs:GetFeature>";
        
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
        "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">\n" + 
        "  <ows:Identifier>orci:Bounds</ows:Identifier>\n" + 
        "  <wps:DataInputs>\n" + 
        "    <wps:Input>\n" + 
        "      <ows:Identifier>features</ows:Identifier>\n" + 
        "      <wps:Reference mimeType=\"text/xml; subtype=wfs-collection/1.0\" " +
        " xlink:href=\"http://demo.opengeo.org/geoserver/wfs\" method=\"POST\">\n" +
        "         <wps:Body>\n" +
        "<![CDATA[" + body + "]]>" +
        "         </wps:Body>\n" +
        "      </wps:Reference>\n" +
        "    </wps:Input>\n" + 
        "  </wps:DataInputs>\n" + 
        "  <wps:ResponseForm>\n" + 
        "    <wps:RawDataOutput>\n" + 
        "      <ows:Identifier>bounds</ows:Identifier>\n" + 
        "    </wps:RawDataOutput>\n" + 
        "  </wps:ResponseForm>\n" + 
        "</wps:Execute>";
        buildDocument(xml);
        
        ExecuteType execute = (ExecuteType) parse(WPS.Execute);
        
        assertEquals("orci:Bounds", execute.getIdentifier().getValue());
        InputType input = (InputType) execute.getDataInputs().getInput().get(0);
        assertEquals("features", input.getIdentifier().getValue());
        InputReferenceType ref = input.getReference();
        assertNotNull(ref);
        assertEquals("http://demo.opengeo.org/geoserver/wfs", ref.getHref());
        assertEquals(MethodType.POST_LITERAL, ref.getMethod());
        // we cannot do this still as the parser strips the white space out of CDATA sections
        // assertEquals(body, ref.getBody());
        // cannot run this either, could not find a way to extract the content element from the parser...
        // assertNull(ref.getContentElement());
    }
    
    public void testParseFull() throws Exception {
        String body = "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\"\n" + 
        "  outputFormat=\"GML2\"\n" + 
        "  xmlns:topp=\"http://www.openplans.org/topp\"\n" + 
        "  xmlns:wfs=\"http://www.opengis.net/wfs\"\n" + 
        "  xmlns:ogc=\"http://www.opengis.net/ogc\"\n" + 
        "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
        "  xsi:schemaLocation=\"http://www.opengis.net/wfs\n" + 
        "                      http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">\n" + 
        "  <wfs:Query typeName=\"topp:states\">\n" + 
        "    <ogc:Filter>\n" + 
        "       <ogc:FeatureId fid=\"states.1\"/>\n" + 
        "    </ogc:Filter>\n" + 
        "    </wfs:Query>\n" + 
        "</wfs:GetFeature>";
        
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
        "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">\n" + 
        "  <ows:Identifier>orci:Bounds</ows:Identifier>\n" + 
        "  <wps:DataInputs>\n" + 
        "    <wps:Input>\n" + 
        "      <ows:Identifier>features</ows:Identifier>\n" + 
        "      <wps:Reference mimeType=\"text/xml; subtype=wfs-collection/1.0\" " +
        " xlink:href=\"http://demo.opengeo.org/geoserver/wfs\" method=\"POST\">\n" +
        "         <wps:Body>" + body + "</wps:Body>\n" +
        "      </wps:Reference>\n" +
        "    </wps:Input>\n" + 
        "  </wps:DataInputs>\n" + 
        "  <wps:ResponseForm>\n" + 
        "    <wps:RawDataOutput>\n" + 
        "      <ows:Identifier>bounds</ows:Identifier>\n" + 
        "    </wps:RawDataOutput>\n" + 
        "  </wps:ResponseForm>\n" + 
        "</wps:Execute>";
        buildDocument(xml);
        
        ExecuteType execute = (ExecuteType) parse(WPS.Execute);
        
        assertEquals("orci:Bounds", execute.getIdentifier().getValue());
        InputType input = (InputType) execute.getDataInputs().getInput().get(0);
        assertEquals("features", input.getIdentifier().getValue());
        InputReferenceType ref = input.getReference();
        assertNotNull(ref);
        assertEquals("http://demo.opengeo.org/geoserver/wfs", ref.getHref());
        assertEquals(MethodType.POST_LITERAL, ref.getMethod());
        assertTrue(ref.getBody() instanceof GetFeatureType);
        // could not find any way to extract the content element...
        // assertEquals(WFS.GetFeature, ref.getContentElement());
    }
}
