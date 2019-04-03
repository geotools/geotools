/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.wmts.v1;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wmts.v_1.CapabilitiesType;
import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.LayerType;
import org.geotools.util.logging.Logging;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** @author Emanuele Tajariol (etj at geo-solutions dot it) */
public class WMTSConfigurationTest {

    static final Logger LOGGER = Logging.getLogger(WMTSConfigurationTest.class);

    public WMTSConfigurationTest() {}

    @Test
    public void testParse() throws IOException, SAXException, ParserConfigurationException {
        Parser p = new Parser(new WMTSConfiguration());
        p.setValidating(false);
        Object parsed;
        try (InputStream is = getClass().getResourceAsStream("./nasa.getcapa.xml")) {
            parsed = p.parse(is);
        }

        assertTrue(
                "Capabilities failed to parse " + parsed.getClass(),
                parsed instanceof CapabilitiesType);

        CapabilitiesType caps = (CapabilitiesType) parsed;
        ContentsType contents = caps.getContents();
        assertNotNull(contents);

        Map<String, LayerType> layers = new HashMap<>();

        // Parse layers
        for (Object l : contents.getDatasetDescriptionSummary()) {

            if (l instanceof LayerType) {
                LayerType layerType = (LayerType) l;
                String id = layerType.getIdentifier().getValue();

                layers.put(id, layerType);
            }
        }

        assertEquals(519, layers.size());
        assertTrue(layers.containsKey("MODIS_Terra_L3_SST_MidIR_9km_Night_Annual"));
    }

    /**
     * TODO.
     *
     * <p>Validation fails due to a gml/xlink conflict of some type:
     *
     * <p>org.xml.sax.SAXParseException; systemId:
     * http://schemas.opengis.net/gml/3.1.1/base/gmlBase.xsd; lineNumber: 268; columnNumber: 44;
     * src-resolve: Cannot resolve the name 'xlink:simpleAttrs' to a(n) 'attribute group' component.
     *
     * <p>on line <attributeGroup ref="xlink:simpleAttrs"/>
     */
    @Ignore
    @Test
    public void testValidate() throws IOException, SAXException, ParserConfigurationException {
        Parser p = new Parser(new WMTSConfiguration());
        p.setValidating(true);
        try (InputStream is = getClass().getResourceAsStream("./nasa.getcapa.xml")) {
            p.parse(is);
        }
        if (!p.getValidationErrors().isEmpty()) {
            for (Iterator e = p.getValidationErrors().iterator(); e.hasNext(); ) {
                SAXParseException ex = (SAXParseException) e.next();
                LOGGER.log(
                        Level.SEVERE,
                        ex.getLineNumber() + "," + ex.getColumnNumber() + " -" + ex.toString());
            }
            fail("Document did not validate.");
        }
    }
}
