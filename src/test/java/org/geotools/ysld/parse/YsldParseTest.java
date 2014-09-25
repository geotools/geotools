package org.geotools.ysld.parse;

import org.geotools.process.function.ProcessFunction;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.Ysld;
import org.junit.Test;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

import java.awt.*;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class YsldParseTest {

    @Test
    public void testAnchor() throws Exception {
        String yaml =
        "blue: &blue rgb(0,0,255)\n" +
        "point: \n"+
        "  symbols: \n" +
        "  - mark: \n" +
        "      fill-color: *blue\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertEquals(Color.BLUE, SLD.color(SLD.fill(p)));
    }

    @Test
    public void testNamedColor() throws Exception {
        String yaml =
        "point: \n"+
        "  symbols: \n" +
        "  - mark: \n" +
        "      fill-color: blue\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertEquals(Color.BLUE, SLD.color(SLD.fill(p)));
    }

    @Test
    public void testFunction() throws Exception {
        String yaml =
        "rules: \n"+
        "- filter: strEndsWith(foo,'bar') = true\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        Rule r = SLD.defaultStyle(sld).featureTypeStyles().get(0).rules().get(0);

        PropertyIsEqualTo f = (PropertyIsEqualTo) r.getFilter();
        Function func = (Function) f.getExpression1();
        assertEquals("strEndsWith", func.getName());
        assertTrue(func.getParameters().get(0) instanceof PropertyName);
        assertTrue(func.getParameters().get(1) instanceof Literal);

        Literal lit = (Literal) f.getExpression2();
    }

    @Test
    public void testRenderingTransformation() throws IOException {
        String yaml =
        "feature-styles: \n"+
        "- transform:\n" +
        "    name: ras:Contour\n" +
        "    params:\n" +
        "      levels:\n" +
        "      - 1000\n" +
        "      - 1100\n" +
        "      - 1200\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Expression tx = fs.getTransformation();
        assertNotNull(tx);

        ProcessFunction pf = (ProcessFunction) tx;
        assertEquals(2, pf.getParameters().size());

        Function e1 = (Function) pf.getParameters().get(0);
        assertEquals(1, e1.getParameters().size());
        assertTrue(e1.getParameters().get(0) instanceof Literal);
        assertEquals("data", e1.getParameters().get(0).evaluate(null, String.class));

        Function e2 = (Function) pf.getParameters().get(1);
        assertEquals(4, e2.getParameters().size());
        assertTrue(e2.getParameters().get(0) instanceof Literal);
        assertEquals("levels", e2.getParameters().get(0).evaluate(null, String.class));
        assertEquals(1000, e2.getParameters().get(1).evaluate(null, Integer.class).intValue());
        assertEquals(1100, e2.getParameters().get(2).evaluate(null, Integer.class).intValue());
        assertEquals(1200, e2.getParameters().get(3).evaluate(null, Integer.class).intValue());
    }

    @Test
    public void testRenderingTransformationHeatmap() throws IOException {
        String yaml =
            "feature-styles: \n"+
            "- transform:\n" +
            "    name: vec:Heatmap\n" +
            "    params:\n" +
            "      weightAttr: pop2000\n" +
            "      radius: 100\n" +
            "      pixelsPerCell: 10\n" +
            "      outputBBOX: wms_bbox\n" +
            "      outputWidth: wms_width\n" +
            "      outputHeight: wms_height\n";


        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Expression tx = fs.getTransformation();
        assertNotNull(tx);

        ProcessFunction pf = (ProcessFunction) tx;
        assertEquals(7, pf.getParameters().size());

        Function e = (Function) pf.getParameters().get(0);
        assertEquals(1, e.getParameters().size());
        assertTrue(e.getParameters().get(0) instanceof Literal);
        assertEquals("data", e.getParameters().get(0).evaluate(null, String.class));

        e = (Function) pf.getParameters().get(1);
        assertEquals(2, e.getParameters().size());
        assertTrue(e.getParameters().get(0) instanceof Literal);
        assertEquals("weightAttr", e.getParameters().get(0).evaluate(null, String.class));
        assertEquals("pop2000", e.getParameters().get(1).evaluate(null, String.class));
    }
    
    @Test
    public void testLabelShield() throws Exception {
        String yaml =
                "feature-styles:\n"+
                "- name: name\n"+
                " rules:\n"+
                " - symbolizers:\n"+
                "   - line:\n"+
                "       stroke-color: '#555555'\n"+
                "       stroke-width: 1.0\n"+
                "    - text:\n"+
                "       label: name\n"+
                "       symbols:\n"+
                "        - mark:\n"+
                "           shape: circle\n"+
                "           fill-color: '#995555'\n"+
                "       geometry: geom";
                        
    }
}
