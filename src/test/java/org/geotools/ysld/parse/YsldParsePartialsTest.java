package org.geotools.ysld.parse;

import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.Ysld;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class YsldParsePartialsTest {

    @Test
    public void testParseRules() throws Exception {
        String ysld =
            "rules:\n" +
            "- symbolizers:\n" +
            "  - point:\n" +
            "      symbols:\n" +
            "      - mark:\n" +
            "         shape: triangle";

        assertSLD(Ysld.parse(Ysld.reader(ysld)));
    }

    @Test
    public void testParseSymbolizers() throws Exception {
        String ysld =
            "symbolizers:\n" +
            "- point:\n" +
            "    symbols:\n" +
            "    - mark:\n" +
            "       shape: triangle";

        assertSLD(Ysld.parse(Ysld.reader(ysld)));
    }

    @Test
    public void testParseSymbolizer() throws Exception {
        String ysld =
            "point:\n" +
            "  symbols:\n" +
            "  - mark:\n" +
            "     shape: triangle";

        assertSLD(Ysld.parse(Ysld.reader(ysld)));
    }

    void assertSLD(StyledLayerDescriptor sld) {
        assertNotNull(sld);
        PointSymbolizer point =
                SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertNotNull(point);
        assertEquals("triangle", SLD.mark(point).getWellKnownName().evaluate(null));
    }

}
