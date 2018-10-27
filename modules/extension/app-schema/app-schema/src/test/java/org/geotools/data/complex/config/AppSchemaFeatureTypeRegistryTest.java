package org.geotools.data.complex.config;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.geotools.feature.NameImpl;
import org.geotools.gml3.v3_2.GML;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.SchemaIndex;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is to test GMLHandler in AppSchemaFeatureTypeRegistry.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class AppSchemaFeatureTypeRegistryTest extends AppSchemaTestSupport {
    private static final String BOREHOLE_NS = "http://xmlns.geosciml.org/Borehole/3.2";

    private static final Name BOREHOLE_TYPE = new NameImpl(BOREHOLE_NS, "BoreholeType");

    private static SchemaIndex schemaIndex;

    private static NamespaceSupport gml32NS;

    @BeforeClass
    public static void oneTimeSetUp() throws IOException {
        SchemaResolver resolver = new SchemaResolver();
        String schemalocation =
                SchemaResolver.resolveClasspathLocation(
                        "http://schemas.geosciml.org/borehole/3.2/borehole.xsd");
        EmfComplexFeatureReader schemaParser = EmfComplexFeatureReader.newInstance();
        schemaParser.setResolver(resolver);
        schemaIndex = schemaParser.parse(BOREHOLE_NS, schemalocation);
        // namespace support with GML32 declared
        gml32NS = new NamespaceSupport();
        gml32NS.declarePrefix("gml", GML.NAMESPACE);
    }

    @Test
    public void testGML32Declared() {
        AppSchemaFeatureTypeRegistry registry = new AppSchemaFeatureTypeRegistry(gml32NS);
        registry.addSchemas(schemaIndex);
        AttributeType type = registry.getAttributeType(BOREHOLE_TYPE);
        assertTrue(type instanceof FeatureType);
    }

    @Test
    public void testGML32Undeclared() {
        // GEOT-4756: no namespace support provided.. due to namespaces not set in
        // mapping file. This is legitimate if the mapping doesn't use any GML attributes.
        // Previously, this won't work.
        AppSchemaFeatureTypeRegistry registry = new AppSchemaFeatureTypeRegistry();
        registry.addSchemas(schemaIndex);
        AttributeType type = registry.getAttributeType(BOREHOLE_TYPE);
        assertTrue(type instanceof FeatureType);
    }
}
