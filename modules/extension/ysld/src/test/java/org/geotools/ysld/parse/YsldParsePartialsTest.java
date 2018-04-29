/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.Ysld;
import org.junit.Test;

public class YsldParsePartialsTest {

    @Test
    public void testParseRules() throws Exception {
        String ysld =
                "rules:\n"
                        + "- symbolizers:\n"
                        + "  - point:\n"
                        + "      symbols:\n"
                        + "      - mark:\n"
                        + "         shape: triangle";

        assertSLD(Ysld.parse(ysld));
    }

    @Test
    public void testParseSymbolizers() throws Exception {
        String ysld =
                "symbolizers:\n"
                        + "- point:\n"
                        + "    symbols:\n"
                        + "    - mark:\n"
                        + "       shape: triangle";

        assertSLD(Ysld.parse(ysld));
    }

    @Test
    public void testParseSymbolizer() throws Exception {
        String ysld = "point:\n" + "  symbols:\n" + "  - mark:\n" + "     shape: triangle";

        assertSLD(Ysld.parse(ysld));
    }

    void assertSLD(StyledLayerDescriptor sld) {
        assertNotNull(sld);
        PointSymbolizer point = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertNotNull(point);
        assertEquals("triangle", SLD.mark(point).getWellKnownName().evaluate(null));
    }
}
