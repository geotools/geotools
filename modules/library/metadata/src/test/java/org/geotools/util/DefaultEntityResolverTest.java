package org.geotools.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DefaultEntityResolverTest {

    @Test
    public void testResolve() throws IOException, SAXException {
        DefaultEntityResolver toTest = new DefaultEntityResolver();

        InputSource src = toTest.resolveEntity("p", "http://schemas.opengis.net/gml/3.1.1/base/gml.xsd");
        assertNull(src);

        src = toTest.resolveEntity("p", "http://inspire.ec.europa.eu/schemas/omso/2.0rc3/SpecialisedObservations.xsd");
        assertNull(src);

        assertThrows(SAXException.class, () -> {
            toTest.resolveEntity(
                    "p", "http://inspire.ec.europa.eu/draft-schemas/omso/2.0rc3/SpecialisedObservations.xsd");
        });

        assertThrows(SAXException.class, () -> {
            toTest.resolveEntity("p", "http://geotools.org/schema.xsd");
        });

        assertThrows(SAXException.class, () -> {
            toTest.resolveEntity(
                    "p",
                    this.getClass()
                            .getResource("/org/geotools/util/factory/foo.jar")
                            .getFile());
        });
    }
}
