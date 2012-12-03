package org.geotools.kml.bindings;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xml.Binding;

public class ExtendedDataTypeBindingTest extends KMLTestSupport {

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(KML.ExtendedDataType).getExecutionMode());
    }

    public void testGetType() {
        assertEquals(Map.class, binding(KML.ExtendedDataType).getType());
    }

    // to avoid warnings
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseExtendedData() throws Exception {
        return (Map<String, Object>) parse();
    }

    @SuppressWarnings("unchecked")
    public void testParseEmpty() throws Exception {
        String xml = "<ExtendedData></ExtendedData>";
        buildDocument(xml);
        Map<String, Object> document = parseExtendedData();
        assertEquals(3, document.size());
        List<URI> schemas = (List<URI>) document.get("schemas");
        Map<String, Object> typed = (Map<String, Object>) document.get("typed");
        Map<String, Object> untyped = (Map<String, Object>) document.get("untyped");
        assertTrue(schemas.isEmpty());
        assertTrue(typed.isEmpty());
        assertTrue(untyped.isEmpty());
    }

    public void testParseUntyped() throws Exception {
        String xml = "<ExtendedData>" + "<Data name=\"foo\"><value>bar</value></Data>"
                + "</ExtendedData>";
        buildDocument(xml);
        Map<String, Object> document = parseExtendedData();
        @SuppressWarnings("unchecked")
        Map<String, Object> untyped = (Map<String, Object>) document.get("untyped");
        assertEquals("bar", untyped.get("foo"));
    }

    @SuppressWarnings("unchecked")
    public void testParseTyped() throws Exception {
        String xml = "<ExtendedData>" + "<SchemaData schemaUrl=\"#foo\">"
                + "<SimpleData name=\"quux\">morx</SimpleData>" + "</SchemaData>"
                + "</ExtendedData>";
        buildDocument(xml);
        Map<String, Object> document = parseExtendedData();
        Map<String, Object> typed = (Map<String, Object>) document.get("typed");
        assertEquals("morx", typed.get("quux"));
        List<URI> schemaURLS = (List<URI>) document.get("schemas");
        assertEquals(1, schemaURLS.size());
        assertEquals("foo", schemaURLS.get(0).getFragment());
    }

    @SuppressWarnings("unchecked")
    public void testParseMultipleTypes() throws Exception {
        String xml = "<ExtendedData>" + "<SchemaData schemaUrl=\"#foo1\">"
                + "<SimpleData name=\"quux\">morx</SimpleData>" + "</SchemaData>"
                + "<SchemaData schemaUrl=\"#foo2\">"
                + "<SimpleData name=\"fleem\">zul</SimpleData>" + "</SchemaData>"
                + "</ExtendedData>";
        buildDocument(xml);
        Map<String, Object> document = parseExtendedData();
        Map<String, Object> typed = (Map<String, Object>) document.get("typed");
        assertEquals("morx", typed.get("quux"));
        List<URI> schemaURLS = (List<URI>) document.get("schemas");
        assertEquals(2, schemaURLS.size());
        assertEquals("foo1", schemaURLS.get(0).getFragment());
        assertEquals("foo2", schemaURLS.get(1).getFragment());
    }

}
