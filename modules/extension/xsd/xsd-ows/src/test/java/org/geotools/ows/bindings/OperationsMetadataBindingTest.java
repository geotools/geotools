package org.geotools.ows.bindings;

import net.opengis.ows10.OperationsMetadataType;

import org.geotools.ows.OWSTestSupport;

public class OperationsMetadataBindingTest extends OWSTestSupport {

    public void testParse() throws Exception {
        String xml = " <ows:OperationsMetadata xmlns:ows=\"http://www.opengis.net/ows\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">"+
        "  <ows:Operation name=\"GetCapabilities\">"+
        "   <ows:DCP>"+
        "    <ows:HTTP>"+
        "     <ows:Get xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer?\" />"+
        "     <ows:Post xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer\" />"+
        "    </ows:HTTP>"+
        "   </ows:DCP>"+
        "   <ows:Parameter name=\"AcceptVersions\">"+
        "    <ows:Value>1.1.0</ows:Value>"+
        "    <ows:Value>1.0.0</ows:Value>"+
        "   </ows:Parameter>"+
        "   <ows:Parameter name=\"AcceptFormats\">"+
        "    <ows:Value>text/xml</ows:Value>"+
        "   </ows:Parameter>"+
        "  </ows:Operation>"+
        "  <ows:Operation name=\"DescribeFeatureType\">"+
        "   <ows:DCP>"+
        "    <ows:HTTP>"+
        "     <ows:Get xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer?\" />"+
        "     <ows:Post xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer\" />"+
        "    </ows:HTTP>"+
        "   </ows:DCP>"+
        "   <ows:Parameter name=\"outputFormat\">"+
        "    <ows:Value>text/xml; subType=gml/3.1.1/profiles/gmlsf/1.0.0/0</ows:Value>"+
        "   </ows:Parameter>"+
        "  </ows:Operation>"+
        "  <ows:Operation name=\"GetFeature\">"+
        "   <ows:DCP>"+
        "    <ows:HTTP>"+
        "     <ows:Get xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer?\" />"+
        "     <ows:Post xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer\" />"+
        "    </ows:HTTP>"+
        "   </ows:DCP>"+
        "   <ows:Parameter name=\"resultType\">"+
        "    <ows:Value>results</ows:Value>"+
        "    <ows:Value>hits</ows:Value>"+
        "   </ows:Parameter>"+
        "   <ows:Parameter name=\"outputFormat\">"+
        "    <ows:Value>text/xml; subType=gml/3.1.1/profiles/gmlsf/1.0.0/0</ows:Value>"+
        "   </ows:Parameter>"+
        "  </ows:Operation>"+
        "  <ows:ExtendedCapabilities>"+
        "   <ows:Constraint name=\"serviceAxisOrderForSwappableSRS\">"+
        "    <ows:Value>latitude,longitude</ows:Value>"+
        "   </ows:Constraint>"+
        "  </ows:ExtendedCapabilities>"+
        " </ows:OperationsMetadata>";

        buildDocument(xml);
        OperationsMetadataType ops = (OperationsMetadataType) parse();
        assertNotNull(ops);
    }
}
