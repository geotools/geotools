package org.geotools.kml.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.kml.Folder;
import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xsd.Binding;
import org.junit.Test;

public class FolderBindingTest extends KMLTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(SimpleFeature.class, binding(KML.Folder).getType());
    }

    @Test
    public void testExecutionMode() throws Exception {
        assertEquals(Binding.AFTER, binding(KML.Folder).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        String xml = "<Folder>" + "<name>foo</name>" + "</Folder>";
        buildDocument(xml);
        SimpleFeature folder = (SimpleFeature) parse();
        assertEquals("foo", folder.getAttribute("name"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFolderHierarchy() throws Exception {
        String xml = "<Folder><name>foo</name>"
                + "<Folder><name>bar</name>"
                + "<Placemark><name>morx</name></Placemark>"
                + "</Folder>"
                + "</Folder>";
        buildDocument(xml);
        SimpleFeature folder1 = (SimpleFeature) parse();
        List<SimpleFeature> features = (List<SimpleFeature>) folder1.getAttribute("Feature");
        SimpleFeature folder2 = features.get(0);
        features = (List<SimpleFeature>) folder2.getAttribute("Feature");
        SimpleFeature placemark = features.get(0);
        Map<Object, Object> userData = placemark.getUserData();
        Object folderObject = userData.get("Folder");
        assertNotNull("No folder user data", folderObject);
        assertTrue("Unknown user data", folderObject instanceof List<?>);
        List<Folder> folders = (List<Folder>) folderObject;
        assertEquals(2, folders.size());
        assertEquals("foo", folders.get(0).getName());
        assertEquals("bar", folders.get(1).getName());
        assertEquals("morx", placemark.getAttribute("name"));
    }
}
