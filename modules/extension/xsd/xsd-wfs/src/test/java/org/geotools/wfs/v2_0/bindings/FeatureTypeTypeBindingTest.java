/* (c) 2016 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.wfs.v2_0.bindings;

import net.opengis.wfs20.FeatureTypeListType;
import org.geotools.wfs.v2_0.WFSTestSupport;

/** @author Niels Charlier */
public class FeatureTypeTypeBindingTest extends WFSTestSupport {

    public void testParse() throws Exception {
        String xml =
                "<FeatureTypeList><FeatureType><Name>ms:park</Name><Title>Parks</Title><DefaultCRS>urn:ogc:def:crs:EPSG::3978</DefaultCRS><OtherCRS>urn:ogc:def:crs:EPSG::4269</OtherCRS><OtherCRS>urn:ogc:def:crs:EPSG::4326</OtherCRS><OutputFormats><Format>application/gml+xml; version=3.2</Format><Format>text/xml; subtype=gml/3.2.1</Format><Format>text/xml; subtype=gml/3.1.1</Format><Format>text/xml; subtype=gml/2.1.2</Format></OutputFormats><WGS84BoundingBox dimensions=\"2\"><LowerCorner>-173.433267989715 41.4271118471489</LowerCorner><UpperCorner>-13.0481388603488 83.7465953038598</UpperCorner></WGS84BoundingBox></FeatureType></FeatureTypeList>";
        buildDocument(xml);

        FeatureTypeListType gc = (FeatureTypeListType) parse();
        assertNotNull(gc);
        assertEquals(8, gc.getFeatureType().get(0).getOutputFormats().getFormat().size());
        assertEquals(
                "application/gml+xml; version=3.2",
                gc.getFeatureType().get(0).getOutputFormats().getFormat().get(0));
    }
}
