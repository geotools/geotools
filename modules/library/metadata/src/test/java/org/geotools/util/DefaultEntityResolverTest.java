package org.geotools.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** Test DefaultEntityResolver and related built-in implementations. */
public class DefaultEntityResolverTest {

    @Test
    public void testNullEntityResolver() throws Exception {
        NullEntityResolver nullEntityResolver = NullEntityResolver.INSTANCE;
        InputSource src;

        src = nullEntityResolver.resolveEntity(
                "p", "http://inspire.ec.europa.eu/draft-schemas/omso/2.0rc3/SpecialisedObservations.xsd");
        assertNull("http", src);

        src = nullEntityResolver.resolveEntity("gml.xsd", "file:/target/classes/org/geotools/gml/v3_2/gml.xsd");
        assertNull("file", src);

        src = nullEntityResolver.resolveEntity(
                "gml.xsd", "jar:file:/usr/local/lib/xsd-core.jar!//org/geotools/gml/v3_2/gml.xsd");
        assertNull("jar:file", src);
    }

    @Test
    public void testPreventEntityResolver() {
        PreventEntityResolver prevent = PreventEntityResolver.INSTANCE;
        assertThrows(SAXException.class, () -> {
            prevent.resolveEntity(
                    "p", "http://inspire.ec.europa.eu/draft-schemas/omso/2.0rc3/SpecialisedObservations.xsd");
        });
        assertThrows(SAXException.class, () -> {
            prevent.resolveEntity("gml.xsd", "file:/target/classes/org/geotools/gml/v3_2/gml.xsd");
        });
        assertThrows(SAXException.class, () -> {
            prevent.resolveEntity("gml.xsd", "jar:file:/usr/local/lib/xsd-core.jar!//org/geotools/gml/v3_2/gml.xsd");
        });
    }

    @Test
    public void testPreventLocalEntityResolver() throws Exception {
        PreventLocalEntityResolver prevent = PreventLocalEntityResolver.INSTANCE;
        InputSource src;

        src = prevent.resolveEntity(
                "p", "http://inspire.ec.europa.eu/draft-schemas/omso/2.0rc3/SpecialisedObservations.xsd");
        assertNull("http", src);
        assertThrows(SAXException.class, () -> {
            prevent.resolveEntity("gml.xsd", "file:/target/classes/org/geotools/gml/v3_2/gml.xsd");
        });
        src = prevent.resolveEntity("gml.xsd", "jar:file:/usr/local/lib/xsd-core.jar!//org/geotools/gml/v3_2/gml.xsd");
        assertNull("jar:file", src);
    }

    @Test
    public void testInternalResolver() throws SAXException, IOException {
        InternalEntityResolver internal = new InternalEntityResolver(PreventEntityResolver.INSTANCE);
        assertThrows(SAXException.class, () -> {
            internal.resolveEntity(
                    "p", "http://inspire.ec.europa.eu/draft-schemas/omso/2.0rc3/SpecialisedObservations.xsd");
        });
        InputSource src = internal.resolveEntity("gml.xsd", "file:/target/classes/org/geotools/gml/v3_2/gml.xsd");
        assertNull("ide", src);

        src = internal.resolveEntity("gml.xsd", "jar:file:/usr/local/lib/xsd-core.jar!//org/geotools/gml/v3_2/gml.xsd");
        assertNull("jar:file", src);
    }

    @Test
    public void testDefaultEnityResolver() throws IOException, SAXException {
        DefaultEntityResolver defaultEntityResolver = DefaultEntityResolver.INSTANCE;

        InputSource src = defaultEntityResolver.resolveEntity("p", "http://schemas.opengis.net/gml/3.1.1/base/gml.xsd");
        assertNull("http", src);

        src = defaultEntityResolver.resolveEntity(
                "p", "http://inspire.ec.europa.eu/schemas/omso/2.0rc3/SpecialisedObservations.xsd");
        assertNull("http", src);

        src = defaultEntityResolver.resolveEntity("p", "http://schemas.opengis.net/wms/1.1.1/capabilities_1_1_1.dtd");
        assertNull("http", src);

        assertThrows(SAXException.class, () -> {
            defaultEntityResolver.resolveEntity(
                    "p", "http://inspire.ec.europa.eu/draft-schemas/omso/2.0rc3/SpecialisedObservations.xsd");
        });

        assertThrows(SAXException.class, () -> {
            defaultEntityResolver.resolveEntity("p", "http://geotools.org/schema.xsd");
        });

        assertThrows(SAXException.class, () -> {
            defaultEntityResolver.resolveEntity(
                    "p",
                    this.getClass()
                            .getResource("/org/geotools/util/factory/foo.jar")
                            .getFile());
        });

        assertThrows(SAXException.class, () -> {
            defaultEntityResolver.resolveEntity("gml", "file:/target/classes/org/geotools/gml/v3_2/gml.xsd");
        });
        src = defaultEntityResolver.resolveEntity(
                "gml.xsd", "jar:file:/usr/local/lib/xsd-core.jar!//org/geotools/gml/v3_2/gml.xsd");
        assertNull("jar:file", src);
    }
}
