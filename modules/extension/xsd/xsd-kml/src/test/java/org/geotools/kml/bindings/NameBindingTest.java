package org.geotools.kml.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.kml.Folder;
import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xsd.Binding;
import org.junit.Test;

public class NameBindingTest extends KMLTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(String.class, binding(KML.name).getType());
    }

    @Test
    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(KML.name).getExecutionMode());
    }

    @Test
    public void testParseName() throws Exception {
        String xml = "<name>fleem</name>";
        buildDocument(xml);

        String name = (String) parse();
        assertEquals("fleem", name);
    }

    @Test
    public void testParseNameInFolder() throws Exception {
        String xml = "<kml><Folder>"
                + "<name>foo</name>"
                + "<Placemark>"
                + "<name>bar</name>"
                + "</Placemark>"
                + "</Folder></kml>";
        buildDocument(xml);

        SimpleFeature document = (SimpleFeature) parse();
        assertEquals("foo", document.getAttribute("name"));

        @SuppressWarnings("unchecked")
        Collection<SimpleFeature> features = (Collection<SimpleFeature>) document.getAttribute("Feature");
        assertEquals(1, features.size());
        SimpleFeature feature = features.iterator().next();
        Map<Object, Object> userData = feature.getUserData();
        Object folderObject = userData.get("Folder");
        assertNotNull("No folder user data", folderObject);
        assertTrue("Unknown folder object in user data", folderObject instanceof List<?>);
        @SuppressWarnings("unchecked")
        List<Folder> folders = (List<Folder>) folderObject;
        assertEquals(1, folders.size());
        assertEquals("foo", folders.get(0).getName());
    }
}
