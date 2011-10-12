package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs20.DescribeFeatureTypeType;

import org.geotools.wfs.v2_0.WFSTestSupport;

public class DescribeFeatureTypeTypeBindingTest extends WFSTestSupport {

    public void testParse() throws Exception {
        String xml = 
            "<DescribeFeatureType service='WFS' version='2.0.0' xmlns='http://www.opengis.net/wfs/2.0' " + 
            "xmlns:myns='http://www.myserver.com/myns' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " + 
            "xsi:schemaLocation='http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd'>" + 
            "<TypeName>myns:TreesA_1M</TypeName>" + 
            "<TypeName>myns:RoadL_1M</TypeName>" + 
         "</DescribeFeatureType>";
        buildDocument(xml);
        
        DescribeFeatureTypeType dft = (DescribeFeatureTypeType) parse();
        assertNotNull(dft);
        
        assertEquals(2, dft.getTypeName().size());
        assertEquals(new QName("http://www.myserver.com/myns", "TreesA_1M"), dft.getTypeName().get(0));
        assertEquals(new QName("http://www.myserver.com/myns", "RoadL_1M"), dft.getTypeName().get(1));
    }
}
