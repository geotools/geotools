package org.geotools.ysld.parse;

import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.Ysld;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class YamlAnchorTest {

    @Test
    public void testSimple() throws Exception {
        String yaml =
        "blue: &blue rgb(0,0,255)\n" +
        "point: \n"+
        "  symbols: \n" +
        "  - mark: \n" +
        "      fill: \n" +
        "        color: *blue\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertEquals(Color.BLUE, SLD.color(SLD.fill(p)));

    }
}
