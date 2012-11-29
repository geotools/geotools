package org.geotools.kml.bindings;

import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xml.Binding;
import org.opengis.feature.simple.SimpleFeatureType;

public class SchemaTypeBindingTest extends KMLTestSupport {

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(KML.SchemaType).getExecutionMode());
    }

    public void testGetType() {
        assertEquals(SimpleFeatureType.class, binding(KML.SchemaType).getType());
    }

    public void testParse() throws Exception {
        String xml = "<Schema name=\"foo\">"
                + "<SimpleField type=\"int\" name=\"quux\"></SimpleField>" + "</Schema>";
        buildDocument(xml);
        SimpleFeatureType ft = (SimpleFeatureType) parse();
        assertEquals("Unexpected number of attributes", 1, ft.getAttributeCount());
        assertEquals("Unexpected column type", Integer.class, ft.getDescriptor("quux").getType()
                .getBinding());
        assertEquals("foo", ft.getName().getLocalPart());
    }

}
