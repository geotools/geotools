package org.geotools.ysld.parse;

import org.easymock.classextension.EasyMock;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.RecodeFunction;
import org.geotools.filter.function.string.ConcatenateFunction;
import org.geotools.process.function.ProcessFunction;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.ysld.TestUtils;
import org.geotools.ysld.Ysld;
import org.geotools.ysld.YsldTests;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.geotools.styling.ColorMap;
import org.opengis.style.ContrastEnhancement;
import org.opengis.style.ContrastMethod;
import org.opengis.style.LinePlacement;
import org.opengis.style.Mark;
import org.opengis.style.PointPlacement;
import org.opengis.style.RasterSymbolizer;
import org.opengis.style.SelectedChannelType;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.geotools.ysld.TestUtils.appliesToScale;
import static org.geotools.ysld.TestUtils.attribute;
import static org.geotools.ysld.TestUtils.isColor;
import static org.geotools.ysld.TestUtils.literal;
import static org.geotools.ysld.TestUtils.nilExpression;
import static org.geotools.ysld.TestUtils.lexEqualTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void testFilterFunctionNoMarker() throws Exception {
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
    public void testFilterFunctionWithMarker() throws Exception {
        String yaml =
        "rules: \n"+
        "- filter: ${strEndsWith(foo,'bar') = true}\n";

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
    public void testFilterFunctionWithMarker2() throws Exception {
        String yaml =
        "rules: \n"+
        "- filter: ${scalerank < 4}\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        Rule r = SLD.defaultStyle(sld).featureTypeStyles().get(0).rules().get(0);
        
        Filter f = r.getFilter();
        assertThat(f, Matchers.instanceOf(PropertyIsLessThan.class));
        assertThat(((PropertyIsLessThan) f).getExpression1(), attribute("scalerank"));
        assertThat(((PropertyIsLessThan) f).getExpression2(), literal(4));
    }
    @Test
    public void testFilterWithEscape() throws Exception {
        String yaml =
        "rules: \n"+
        "- filter: ${foo = '\\$\\}'}\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        Rule r = SLD.defaultStyle(sld).featureTypeStyles().get(0).rules().get(0);
        
        Filter f = r.getFilter();
        assertThat(f, Matchers.instanceOf(PropertyIsEqualTo.class));
        assertThat(((PropertyIsEqualTo) f).getExpression1(), attribute("foo"));
        assertThat(((PropertyIsEqualTo) f).getExpression2(), literal("$}"));
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
            "      outputBBOX: ${wms_bbox}\n" +
            "      outputWidth: ${wms_width}\n" +
            "      outputHeight: ${wms_height}\n";


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
                "       geometry: ${geom}";
                        
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testZoomSimple() throws IOException {
        String yaml =
            "grid:\n"+
            "  initial-scale: 5000000\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            "  - zoom: (0,0)\n";
        
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        
        assertThat((Iterable<Rule>)fs.rules(), hasItems(
                allOf(
                        appliesToScale(5000000),
                        not(appliesToScale(5000000/2)),
                        not(appliesToScale(5000000*2))
                    )));
        
    }
    
    @Test
    public void testZoomSimpleRange() throws IOException {
        String yaml =
            "grid:\n"+
            "  initial-scale: 5000000\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            "  - zoom: (1,2)\n";
        
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        
        Rule r = fs.rules().get(0);
        
        assertThat(r, appliesToScale(5000000/2)); // Z=1
        assertThat(r, appliesToScale(5000000/4)); // Z=2
        assertThat(r, not(appliesToScale(5000000)));  // Z=0
        assertThat(r, not(appliesToScale(5000000/8))); // Z=3
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testZoomSimpleWithDifferentInitial() throws IOException {
        String yaml =
            "grid:\n"+
            "  initial-scale: 5000000\n"+
            "  initial-level: 3\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            "  - zoom: (0,0)\n"+
            "  - zoom: (3,3)\n";
        
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        
        assertThat((Iterable<Rule>)fs.rules(), hasItems(
                allOf(
                        appliesToScale(5000000*8),
                        not(appliesToScale(5000000/2*8)),
                        not(appliesToScale(5000000*2*8))
                    ),
                allOf(
                        appliesToScale(5000000*8),
                        not(appliesToScale(5000000/2*8)),
                        not(appliesToScale(5000000*2*8))
                    )
                ));
        
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testZoomList() throws IOException {
        String yaml =
            "grid:\n"+
            "  scales:\n"+
            "  - 5000000\n"+
            "  - 2000000\n"+
            "  - 1000000\n"+
            "  - 500000\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            "  - zoom: (0,0)\n"+
            "  - zoom: (2,2)\n";
        
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        
        assertThat((Iterable<Rule>)fs.rules(), hasItems(
                allOf(
                        appliesToScale(5000000d),
                        not(appliesToScale(2000000d))
                    ),
                allOf(
                        appliesToScale(1000000d),
                        not(appliesToScale(2000000d)),
                        not(appliesToScale(500000d))
                    )
                ));
        
    }
    @SuppressWarnings("unchecked")
    @Test
    public void testZoomListWithInitial() throws IOException {
        String yaml =
            "grid:\n"+
            "  initial-level: 3\n"+
            "  scales:\n"+
            "  - 5000000\n"+
            "  - 2000000\n"+
            "  - 1000000\n"+
            "  - 500000\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            "  - zoom: (3,3)\n"+
            "  - zoom: (5,5)\n";
        
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        
        assertThat((Iterable<Rule>)fs.rules(), hasItems(
                allOf(
                        appliesToScale(5000000d),
                        not(appliesToScale(2000000d))
                    ),
                allOf(
                        appliesToScale(1000000d),
                        not(appliesToScale(2000000d)),
                        not(appliesToScale(500000d))
                    )
                ));
        
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testZoomListWithRanges() throws IOException {
        String yaml =
            "grid:\n"+
            "  scales:\n"+
            "  - 5000000\n"+
            "  - 2000000\n"+
            "  - 1000000\n"+
            "  - 500000\n"+
            "  - 200000\n"+
            "  - 100000\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            "  - zoom: (,1)\n"+
            "  - zoom: (2,3)\n"+
            "  - zoom: (4,)\n";
        
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        
        assertThat((Iterable<Rule>)fs.rules(), hasItems(
                allOf(
                        appliesToScale(5000000d),
                        appliesToScale(2000000d),
                        not(appliesToScale(1000000d)),
                        not(appliesToScale(500000d)),
                        not(appliesToScale(200000d)),
                        not(appliesToScale(100000d))
                        ),
                allOf(
                        not(appliesToScale(5000000d)),
                        not(appliesToScale(2000000d)),
                        appliesToScale(1000000d),
                        appliesToScale(500000d),
                        not(appliesToScale(200000d)),
                        not(appliesToScale(100000d))
                        ),
                allOf(
                        not(appliesToScale(5000000d)),
                        not(appliesToScale(2000000d)),
                        not(appliesToScale(1000000d)),
                        not(appliesToScale(500000d)),
                        appliesToScale(200000d),
                        appliesToScale(100000d)
                        )
                ));
        
    }
    final static String GOOGLE_MERCATOR_TEST_RULES=
            "  - zoom: (0,0)\n"+
            "  - zoom: (1,1)\n"+
            "  - zoom: (2,2)\n"+
            "  - zoom: (3,3)\n"+
            "  - zoom: (4,4)\n"+
            "  - zoom: (5,5)\n"+
            "  - zoom: (6,6)\n"+
            "  - zoom: (7,7)\n"+
            "  - zoom: (8,8)\n"+
            "  - zoom: (9,9)\n"+
            "  - zoom: (10,10)\n"+
            "  - zoom: (11,11)\n"+
            "  - zoom: (12,12)\n"+
            "  - zoom: (13,13)\n"+
            "  - zoom: (14,14)\n"+
            "  - zoom: (15,15)\n"+
            "  - zoom: (16,16)\n"+
            "  - zoom: (17,17)\n"+
            "  - zoom: (18,18)\n"+
            "  - zoom: (19,19)\n";
    
    final static String WGS84_TEST_RULES=
            "  - zoom: (0,0)\n"+
            "    name: WGS84:00\n"+
            "  - zoom: (1,1)\n"+
            "    name: WGS84:01\n"+
            "  - zoom: (2,2)\n"+
            "    name: WGS84:02\n"+
            "  - zoom: (3,3)\n"+
            "    name: WGS84:03\n"+
            "  - zoom: (4,4)\n"+
            "    name: WGS84:04\n"+
            "  - zoom: (5,5)\n"+
            "    name: WGS84:05\n"+
            "  - zoom: (6,6)\n"+
            "    name: WGS84:06\n"+
            "  - zoom: (7,7)\n"+
            "    name: WGS84:07\n"+
            "  - zoom: (8,8)\n"+
            "    name: WGS84:08\n"+
            "  - zoom: (9,9)\n"+
            "    name: WGS84:09\n"+
            "  - zoom: (10,10)\n"+
            "    name: WGS84:10\n"+
            "  - zoom: (11,11)\n"+
            "    name: WGS84:11\n"+
            "  - zoom: (12,12)\n"+
            "    name: WGS84:12\n"+
            "  - zoom: (13,13)\n"+
            "    name: WGS84:13\n"+
            "  - zoom: (14,14)\n"+
            "    name: WGS84:14\n"+
            "  - zoom: (15,15)\n"+
            "    name: WGS84:15\n"+
            "  - zoom: (16,16)\n"+
            "    name: WGS84:16\n"+
            "  - zoom: (17,17)\n"+
            "    name: WGS84:17\n"+
            "  - zoom: (18,18)\n"+
            "    name: WGS84:18\n"+
            "  - zoom: (19,19)\n"+
            "    name: WGS84:19\n"+
            "  - zoom: (20,20)\n"+
            "    name: WGS84:20\n";
    
    // m/px
    double[] GOOGLE_MERCATOR_PIXEL_SIZES = {
            156543.0339280410,
            78271.51696402048,
            39135.75848201023,
            19567.87924100512,
            9783.939620502561,
            4891.969810251280,
            2445.984905125640,
            1222.992452562820,
            611.4962262814100,
            305.7481131407048,
            152.8740565703525,
            76.43702828517624,
            38.21851414258813,
            19.10925707129406,
            9.554628535647032,
            4.777314267823516,
            2.388657133911758,
            1.194328566955879,
            0.5971642834779395,
            0.29858214173896974,
            0.14929107086948487
    };
    
    double INCHES_PER_METRE = 39.3701;
    double OGC_DPI = 90;
    
    final double[] WGS84_SCALE_DENOMS = {
            559_082_263.9508929d,
            279_541_132.0143589d,
            139_770_566.00717944d,
            69_885_283.00358972d,
            34_942_641.50179486d,
            17_471_320.75089743d,
            8_735_660.375448715d,
            4_367_830.1877243575d,
            2_183_915.0938621787d,
            1_091_957.5469310894d,
            545_978.7734655447d,
            272_989.38673277234d,
            136_494.69336638617d,
            68_247.34668319309d,
            34_123.67334159654d,
            17_061.83667079827d,
            8_530.918335399136d,
            4_265.459167699568d,
            2_132.729583849784d,
            1_066.364791924892d,
            533.182395962446d
    };
    
    @Test
    public void testZoomDefault() throws IOException {
        String yaml =
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            WGS84_TEST_RULES;
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        doTestForWGS84(sld);
        
    }
    @Test
    public void testNamed() throws IOException {
        String yaml =
            "grid:\n"+
            "  name: WebMercator\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            GOOGLE_MERCATOR_TEST_RULES;
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        doTestForGoogleMercator(sld);
        
    }
    
    @SuppressWarnings("unchecked")
    private void doTestForGoogleMercator(StyledLayerDescriptor sld) throws IOException {
        double scaleDenominators[] = new double[GOOGLE_MERCATOR_PIXEL_SIZES.length];
        for(int i=0; i<GOOGLE_MERCATOR_PIXEL_SIZES.length; i++){
            scaleDenominators[i]=OGC_DPI*INCHES_PER_METRE*GOOGLE_MERCATOR_PIXEL_SIZES[i];
        }
        

        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        
        for(int i=0; i<GOOGLE_MERCATOR_PIXEL_SIZES.length; i++){
            scaleDenominators[i]=OGC_DPI*INCHES_PER_METRE*GOOGLE_MERCATOR_PIXEL_SIZES[i];
        }
        
        assertThat(fs.rules().size(), is(20));
        
        assertThat(fs.rules().get(0), allOf(
                appliesToScale(scaleDenominators[0]),
                not(appliesToScale(scaleDenominators[1]))
        ));
        assertThat(fs.rules().get(19) ,allOf(
                appliesToScale(scaleDenominators[19]),
                not(appliesToScale(scaleDenominators[18]))
            ));
        for(int i = 1; i<19; i++){
            assertThat(fs.rules().get(i) ,describedAs("rule applies only to level %0 (%1)",
                    allOf(
                        appliesToScale(scaleDenominators[i]),
                        not(appliesToScale(scaleDenominators[i+1])),
                        not(appliesToScale(scaleDenominators[i+-1]))
                    ),
                    i,scaleDenominators[i]
               ));
        }
    }
    Matcher<Double> mCloseTo(final double value, final double epsilon) {
        return new BaseMatcher<Double>(){

            @Override
            public boolean matches(Object arg0) {
                return Math.abs(value/(Double)arg0-1)<=epsilon;
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("divided by ")
                .appendValue(value)
                .appendText(" within ")
                .appendValue(epsilon)
                .appendText(" of 1.");
            }
            
        };
    }
    
    @Test
    public void testWGS84Scales() throws Exception {
        ZoomContext context = WellKnownZoomContextFinder.getInstance().get("DEFAULT");
        
        for(int i=0; i<WGS84_SCALE_DENOMS.length; i++) {
            assertThat(context.getScaleDenominator(i), mCloseTo(WGS84_SCALE_DENOMS[i], 0.00000001d));
        }
    }
    
    @SuppressWarnings("unchecked")
    private void doTestForWGS84(StyledLayerDescriptor sld) throws IOException {
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        
        assertThat(fs.rules().size(), is(21));
        
        Rule first = fs.rules().get(0);
        assertThat(first, allOf(
                appliesToScale(WGS84_SCALE_DENOMS[0]),
                not(appliesToScale(WGS84_SCALE_DENOMS[1]))
        ));

        for(int i = 1; i<20; i++){
            Rule r = fs.rules().get(i);
            assertThat(r ,describedAs("rule applies only to level %0 (%1)",
                    allOf(
                        appliesToScale(WGS84_SCALE_DENOMS[i]),
                        not(appliesToScale(WGS84_SCALE_DENOMS[i+1])),
                        not(appliesToScale(WGS84_SCALE_DENOMS[i+-1]))
                    ),
                    i,WGS84_SCALE_DENOMS[i]
               ));
        }
        
        Rule last = fs.rules().get(20);
        assertThat(last ,allOf(
                appliesToScale(WGS84_SCALE_DENOMS[20]),
                not(appliesToScale(WGS84_SCALE_DENOMS[19]))
            ));

    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testNamedWithFinder() throws IOException {
        String yaml =
            "grid:\n"+
            "  name: test\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            "  - zoom: (0,0)";
        
        ZoomContextFinder finder = createMock(ZoomContextFinder.class);
        ZoomContext context = createMock(ZoomContext.class);
        
        expect(finder.get("test")).andReturn(context);
        expect(context.getRange(0, 0)).andReturn(new ScaleRange(42, 64));
        
        replay(finder, context);
        
        StyledLayerDescriptor sld = Ysld.parse(yaml, Arrays.asList(finder), (ResourceLocator)null);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        fs.rules().get(0).getMaxScaleDenominator();
        assertThat((Iterable<Rule>)fs.rules(), hasItems(
                allOf(
                        Matchers.<Rule>hasProperty("maxScaleDenominator", Matchers.closeTo(64,0.0000001d)),
                        Matchers.<Rule>hasProperty("minScaleDenominator", Matchers.closeTo(42,0.0000001d))
                    )));
        
        verify(finder, context);
    }
    
    @Test
    public void testWellKnownWithCustomFinder() throws IOException {
        String yaml =
            "grid:\n"+
            "  name: WebMercator\n"+
            "feature-styles: \n"+
            "- name: name\n"+
            "  rules:\n"+
            GOOGLE_MERCATOR_TEST_RULES;
        
        ZoomContextFinder finder = createMock(ZoomContextFinder.class);
        
        expect(finder.get("WebMercator")).andReturn(null);
        
        replay(finder);
        
        StyledLayerDescriptor sld = Ysld.parse(yaml, Arrays.asList(finder), (ResourceLocator)null);
        doTestForGoogleMercator(sld); // The additional finder doesn't have a WebMercator context and so should not interfere.
        
        verify(finder);
    }
    @SuppressWarnings("unchecked")
    @Test
    public void testCustomFinderOverridesWellKnown() throws IOException {
        String yaml =
                "grid:\n"+
                "  name: WebMercator\n"+
                "feature-styles: \n"+
                "- name: name\n"+
                "  rules:\n"+
                "  - zoom: (0,0)";
            
            ZoomContextFinder finder = createMock(ZoomContextFinder.class);
            ZoomContext context = createMock(ZoomContext.class);
            
            expect(finder.get("WebMercator")).andReturn(context);
            expect(context.getRange(0, 0)).andReturn(new ScaleRange(42, 64));
            
            replay(finder, context);
            
            StyledLayerDescriptor sld = Ysld.parse(yaml, Arrays.asList(finder), (ResourceLocator)null);
            FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
            fs.rules().get(0).getMaxScaleDenominator();
            assertThat((Iterable<Rule>)fs.rules(), hasItems(
                    allOf(
                            Matchers.<Rule>hasProperty("maxScaleDenominator", Matchers.closeTo(64,0.0000001d)),
                            Matchers.<Rule>hasProperty("minScaleDenominator", Matchers.closeTo(42,0.0000001d))
                        )));
            
            verify(finder, context);
    }

    @Test
    public void testParseNoStrokeFillDefaults() throws Exception {
        String yaml =
            "polygon: \n"+
            "  fill-color: blue\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PolygonSymbolizer p = (PolygonSymbolizer) SLD.symbolizers(SLD.defaultStyle(sld))[0];
        assertEquals(Color.BLUE, SLD.color(SLD.fill(p)));
        assertNull(SLD.stroke(p));

        yaml =
            "polygon: \n"+
            "  stroke-color: blue\n";

        sld = Ysld.parse(yaml);
        p = (PolygonSymbolizer) SLD.symbolizers(SLD.defaultStyle(sld))[0];
        assertEquals(Color.BLUE, SLD.color(SLD.stroke(p)));
        assertNull(SLD.fill(p));
    }

    @Test
    public void testColourMap() throws Exception {
        String yaml =
                "raster: \n" +
                "  color-map:\n" +
                "    type: values\n" +
                "    entries:\n" +
                "    - ('#ff0000, 1.0, 0, start)\n" +
                "    - ('#00ff00', 1.0, 500, middle)\n" +
                "    - ('#0000ff', 1.0, 1000, end)\n" +
                "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        RasterSymbolizer symb = (RasterSymbolizer) fs.rules().get(0).symbolizers().get(0);

        // need to use the geotools.styling interface as it provides the accessors for the entries.
        ColorMap map = (ColorMap) symb.getColorMap();

        System.out.println(map.getColorMapEntry(0).getColor().evaluate(null));
        Color colour1 = (Color) map.getColorMapEntry(0).getColor().evaluate(null);
        Color colour2 = (Color) map.getColorMapEntry(1).getColor().evaluate(null);
        Color colour3 = (Color) map.getColorMapEntry(2).getColor().evaluate(null);

        assertThat(colour1, is(Color.RED));
        assertThat(colour2, is(Color.GREEN));
        assertThat(colour3, is(Color.BLUE));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testLabelLiteral() throws Exception {
        String yaml =
                "text: \n" +
                "  label: test literal\n" +
                "";
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression label = symb.getLabel();
        assertThat(label, allOf(instanceOf(Literal.class), hasProperty("value", equalTo("test literal"))));
    }

    @Test
    public void testLabelEmbeded() throws Exception {
        String yaml =
                "text: \n" +
                "  label: literal0${attribute1}literal2\n" +
                "";
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression label = symb.getLabel();
        assertThat(label, instanceOf(ConcatenateFunction.class));
        List<Expression> params = ((ConcatenateFunction)label).getParameters();
        assertThat(params.size(), is(3));
        assertThat(params.get(0), literal(equalTo("literal0")));
        assertThat(params.get(1), attribute("attribute1"));
        assertThat(params.get(2), literal(equalTo("literal2")));
    }

    @Test
    public void testLabelAttribute() throws Exception {
        String yaml =
                "text: \n" +
                "  label: ${testAttribute}\n" +
                "";
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression label = symb.getLabel();
        assertThat(label, attribute("testAttribute"));
    }
    @SuppressWarnings("unchecked")
    @Test
    public void testExpressionLiteral() throws Exception {
        String yaml =
                "text: \n" +
                "  geometry: test literal\n" +
                "";
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression expr = symb.getGeometry();
        assertThat(expr, allOf(instanceOf(Literal.class), hasProperty("value", equalTo("test literal"))));
    }

    @Test
    public void testExpressionEmbeded() throws Exception {
        String yaml =
                "text: \n" +
                "  geometry: literal0${attribute1}literal2\n" +
                "";
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression expr = symb.getGeometry();
        assertThat(expr, instanceOf(ConcatenateFunction.class));
        List<Expression> params = ((ConcatenateFunction)expr).getParameters();
        assertThat(params.size(), is(3));
        assertThat(params.get(0), literal(equalTo("literal0")));
        assertThat(params.get(1), attribute("attribute1"));
        assertThat(params.get(2), literal(equalTo("literal2")));
    }

    @Test
    public void testExpressionLong() throws Exception {
        String yaml =
        "polygon:\n" +
        "  fill-color: ${recode(MAPCOLOR7, 1.0, '#FFC3C3', 2.0, '#FFE3C3', 3.0, '#FFFFC3', 4.0, '#C3FFE3', 5.0, '#C3FFFF', 6.0, '#C3C3FF', 7.0, '#BFC3FF')}\n";        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        PolygonSymbolizer symb = (PolygonSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression expr = symb.getFill().getColor();
        assertThat(expr, instanceOf(RecodeFunction.class));
        List<Expression> params = ((RecodeFunction)expr).getParameters();
        assertThat(params.size(), is(7*2+1));
        int i=0;
        assertThat(params.get(i++), attribute("MAPCOLOR7"));
        assertThat(params.get(i++), literal(equalTo(1.0)));
        assertThat(params.get(i++), literal(isColor("FFC3C3")));
        assertThat(params.get(i++), literal(equalTo(2.0)));
        assertThat(params.get(i++), literal(isColor("FFE3C3")));
        assertThat(params.get(i++), literal(equalTo(3.0)));
        assertThat(params.get(i++), literal(isColor("FFFFC3")));
        assertThat(params.get(i++), literal(equalTo(4.0)));
        assertThat(params.get(i++), literal(isColor("C3FFE3")));
        assertThat(params.get(i++), literal(equalTo(5.0)));
        assertThat(params.get(i++), literal(isColor("C3FFFF")));
        assertThat(params.get(i++), literal(equalTo(6.0)));
        assertThat(params.get(i++), literal(isColor("C3C3FF")));
        assertThat(params.get(i++), literal(equalTo(7.0)));
        assertThat(params.get(i++), literal(isColor("BFC3FF")));
    }
    @Test
    public void testExpressionLongBreak() throws Exception {
        String yaml =
        "polygon:\n" +
        "  fill-color: ${recode(MAPCOLOR7, \n"
        + "    1.0, '#FFC3C3', \n"
        + "    2.0, '#FFE3C3', \n"
        + "    3.0, '#FFFFC3', \n"
        + "    4.0, '#C3FFE3', \n"
        + "    5.0, '#C3FFFF', \n"
        + "    6.0, '#C3C3FF', \n"
        + "    7.0, '#BFC3FF')}\n";        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        PolygonSymbolizer symb = (PolygonSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression expr = symb.getFill().getColor();
        assertThat(expr, instanceOf(RecodeFunction.class));
        List<Expression> params = ((RecodeFunction)expr).getParameters();
        assertThat(params.size(), is(7*2+1));
        int i=0;
        assertThat(params.get(i++), attribute("MAPCOLOR7"));
        assertThat(params.get(i++), literal(equalTo(1.0)));
        assertThat(params.get(i++), literal(isColor("FFC3C3")));
        assertThat(params.get(i++), literal(equalTo(2.0)));
        assertThat(params.get(i++), literal(isColor("FFE3C3")));
        assertThat(params.get(i++), literal(equalTo(3.0)));
        assertThat(params.get(i++), literal(isColor("FFFFC3")));
        assertThat(params.get(i++), literal(equalTo(4.0)));
        assertThat(params.get(i++), literal(isColor("C3FFE3")));
        assertThat(params.get(i++), literal(equalTo(5.0)));
        assertThat(params.get(i++), literal(isColor("C3FFFF")));
        assertThat(params.get(i++), literal(equalTo(6.0)));
        assertThat(params.get(i++), literal(isColor("C3C3FF")));
        assertThat(params.get(i++), literal(equalTo(7.0)));
        assertThat(params.get(i++), literal(isColor("BFC3FF")));
    }
    
    @Ignore // This was a test to understand what was going on.  Expect it to fail
    @Test
    public void testExpressionLongBreakFolded() throws Exception {
        String yaml =
        "polygon:\n" +
        "  fill-color: >\n"
        + "    ${recode(MAPCOLOR7, \n"
        + "    1.0, '#FFC3C3', \n"
        + "    2.0, '#FFE3C3', \n"
        + "    3.0, '#FFFFC3', \n"
        + "    4.0, '#C3FFE3', \n"
        + "    5.0, '#C3FFFF', \n"
        + "    6.0, '#C3C3FF', \n"
        + "    7.0, '#BFC3FF')}\n";
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        PolygonSymbolizer symb = (PolygonSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression expr = symb.getFill().getColor();
        assertThat(expr, instanceOf(RecodeFunction.class));
        List<Expression> params = ((RecodeFunction)expr).getParameters();
        assertThat(params.size(), is(7*2+1));
        int i=0;
        assertThat(params.get(i++), attribute("MAPCOLOR7"));
        assertThat(params.get(i++), literal(equalTo(1.0)));
        assertThat(params.get(i++), literal(isColor("FFC3C3")));
        assertThat(params.get(i++), literal(equalTo(2.0)));
        assertThat(params.get(i++), literal(isColor("FFE3C3")));
        assertThat(params.get(i++), literal(equalTo(3.0)));
        assertThat(params.get(i++), literal(isColor("FFFFC3")));
        assertThat(params.get(i++), literal(equalTo(4.0)));
        assertThat(params.get(i++), literal(isColor("C3FFE3")));
        assertThat(params.get(i++), literal(equalTo(5.0)));
        assertThat(params.get(i++), literal(isColor("C3FFFF")));
        assertThat(params.get(i++), literal(equalTo(6.0)));
        assertThat(params.get(i++), literal(isColor("C3C3FF")));
        assertThat(params.get(i++), literal(equalTo(7.0)));
        assertThat(params.get(i++), literal(isColor("BFC3FF")));
    }
    @Ignore // This was a test to understand what was going on.  Expect it to fail
    @Test
    public void testExpressionLongBreakPreserved() throws Exception {
        String yaml =
        "polygon:\n" +
        "  fill-color: |\n"
        + "    ${recode(MAPCOLOR7, \n"
        + "    1.0, '#FFC3C3', \n"
        + "    2.0, '#FFE3C3', \n"
        + "    3.0, '#FFFFC3', \n"
        + "    4.0, '#C3FFE3', \n"
        + "    5.0, '#C3FFFF', \n"
        + "    6.0, '#C3C3FF', \n"
        + "    7.0, '#BFC3FF')}\n";        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        PolygonSymbolizer symb = (PolygonSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression expr = symb.getFill().getColor();
        assertThat(expr, instanceOf(RecodeFunction.class));
        List<Expression> params = ((RecodeFunction)expr).getParameters();
        assertThat(params.size(), is(7*2+1));
        int i=0;
        assertThat(params.get(i++), attribute("MAPCOLOR7"));
        assertThat(params.get(i++), literal(equalTo(1.0)));
        assertThat(params.get(i++), literal(isColor("FFC3C3")));
        assertThat(params.get(i++), literal(equalTo(2.0)));
        assertThat(params.get(i++), literal(isColor("FFE3C3")));
        assertThat(params.get(i++), literal(equalTo(3.0)));
        assertThat(params.get(i++), literal(isColor("FFFFC3")));
        assertThat(params.get(i++), literal(equalTo(4.0)));
        assertThat(params.get(i++), literal(isColor("C3FFE3")));
        assertThat(params.get(i++), literal(equalTo(5.0)));
        assertThat(params.get(i++), literal(isColor("C3FFFF")));
        assertThat(params.get(i++), literal(equalTo(6.0)));
        assertThat(params.get(i++), literal(isColor("C3C3FF")));
        assertThat(params.get(i++), literal(equalTo(7.0)));
        assertThat(params.get(i++), literal(isColor("BFC3FF")));
    }
    @Test
    public void testExpressionAttribute() throws Exception {
        String yaml =
                "text: \n" +
                "  geometry: ${testAttribute}\n" +
                "";
        
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);
        
        Expression expr = symb.getGeometry();
        assertThat(expr, attribute("testAttribute"));
    }
    
   @Test
    public void testBadExpression() throws Exception {
        String yaml =
            "polygon: \n"+
            "  stroke-width: ${round(foo) 1000}\n";
        try {
            Ysld.parse(yaml);
            fail("Bad expression should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            // expected
        }
    }
   
   @Test
   public void testDynamicColor() throws Exception {
       String yaml =
       "point: \n"+
       "  symbols: \n" +
       "  - mark: \n" +
       "      fill-color: ${colourAttribute}\n";

       StyledLayerDescriptor sld = Ysld.parse(yaml);
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(SLD.fill(p).getColor(), attribute("colourAttribute"));
   }
   
   @Test
   public void testEvilExpression1() throws Exception {
       String yaml =
       "point: \n"+
       "  symbols: \n" +
       "  - mark: \n" +
       "      fill-color: \\$\\}\\\\\n";

       StyledLayerDescriptor sld = Ysld.parse(yaml);
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(SLD.fill(p).getColor(), literal(equalTo("$}\\")));
   }
   
   @Test
   public void testColorHex() throws Exception {
       String yaml =
       "point: \n"+
       "  symbols: \n" +
       "  - mark: \n" +
       "      fill-color: 0x001122\n"+
       "      stroke-color: 0x334455\n";

       StyledLayerDescriptor sld = Ysld.parse(yaml);
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(SLD.fill(p).getColor(), literal(isColor("001122")));
       assertThat(SLD.stroke(p).getColor(), literal(isColor("334455")));
   }
   
   @Test
   public void testColorQuotedHex() throws Exception {
       String yaml =
       "point: \n"+
       "  symbols: \n" +
       "  - mark: \n" +
       "      fill-color: '0x001122'\n"+
       "      stroke-color: '0x334455'\n";

       StyledLayerDescriptor sld = Ysld.parse(yaml);
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(SLD.fill(p).getColor(), literal(isColor("001122")));
       assertThat(SLD.stroke(p).getColor(), literal(isColor("334455")));
   }
   @Test
   public void testColorQuotedHash() throws Exception {
       String yaml =
       "point: \n"+
       "  symbols: \n" +
       "  - mark: \n" +
       "      fill-color: '#001122'\n"+
       "      stroke-color: '#334455'\n";

       StyledLayerDescriptor sld = Ysld.parse(yaml);
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(SLD.fill(p).getColor(), literal(isColor("001122")));
       assertThat(SLD.stroke(p).getColor(), literal(isColor("334455")));
   }
   @Test
   public void testColorQuotedBare() throws Exception {
       String yaml =
       "point: \n"+
       "  symbols: \n" +
       "  - mark: \n" +
       "      fill-color: '001122'\n"+
       "      stroke-color: '334455'\n";

       StyledLayerDescriptor sld = Ysld.parse(yaml);
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(SLD.fill(p).getColor(), literal(isColor("001122")));
       assertThat(SLD.stroke(p).getColor(), literal(isColor("334455")));
   }
   @Test
   public void testColorSexegesimal() throws Exception {
       String yaml =
       "point: \n"+
       "  symbols: \n" +
       "  - mark: \n" +
       "      fill-color: 1:17:40:20:15\n";

       StyledLayerDescriptor sld = Ysld.parse(yaml);
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(SLD.fill(p).getColor(), literal(isColor("FFFFFF")));
   }

   @Test
   public void testRasterBandSelectionGray() throws Exception {
       String yaml =
       "raster:\n"+
       "  channels:\n" +
       "    gray:\n" +
       "      name: foo\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
       SelectedChannelType grayChannel = r.getChannelSelection().getGrayChannel();
       assertThat(grayChannel.getChannelName(), equalTo("foo"));
       assertThat(grayChannel.getContrastEnhancement(), nullContrast());
   }
   @Test
   public void testRasterBandSelectionGreyWithContrast() throws Exception {
       String yaml =
       "raster:\n"+
       "  channels:\n" +
       "    gray:\n" +
       "      name: foo\n"+
       "      contrast-enhancement:\n"+
       "        mode: normalize\n"+
       "        gamma: 1.2\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
       SelectedChannelType grayChannel = r.getChannelSelection().getGrayChannel();
       assertThat(grayChannel.getChannelName(), equalTo("foo"));
       assertThat(grayChannel.getContrastEnhancement().getGammaValue(), literal(equalTo("1.2")));
       assertThat(grayChannel.getContrastEnhancement().getMethod(), equalTo(ContrastMethod.NORMALIZE));
   }
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
   static Matcher<ContrastEnhancement> nullContrast() {
       return (Matcher)describedAs("Null Contrast Enhancement", 
               anyOf(
                   nullValue(), 
                   allOf(
                       hasProperty("gammaValue", nilExpression()),
                       hasProperty("method", 
                           anyOf(
                               nullValue(),
                               is(ContrastMethod.NONE))))));
   }
   
   @Test
   public void testRasterBandSelectionRGB() throws Exception {
       String yaml =
       "raster:\n"+
       "  channels:\n" +
       "    red:\n" +
       "      name: foo\n"+
       "    green:\n" +
       "      name: bar\n"+
       "      contrast-enhancement:\n"+
       "        mode: normalize\n"+
       "    blue:\n" +
       "      name: baz\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
       
       SelectedChannelType[] rgbChannels = r.getChannelSelection().getRGBChannels();
       
       assertThat(rgbChannels[0].getChannelName(), equalTo("foo"));
       assertThat(rgbChannels[1].getChannelName(), equalTo("bar"));
       assertThat(rgbChannels[2].getChannelName(), equalTo("baz"));
       
       assertThat(rgbChannels[0].getContrastEnhancement(), nullContrast());
       assertThat(rgbChannels[1].getContrastEnhancement().getGammaValue(), nilExpression());
       assertThat(rgbChannels[1].getContrastEnhancement().getMethod(), equalTo(ContrastMethod.NORMALIZE));
       assertThat(rgbChannels[2].getContrastEnhancement(), nullContrast());
   }
   
   @Test
   public void testRasterBandSelectionGrayTerse() throws Exception {
       String yaml =
       "raster:\n"+
       "  channels:\n" +
       "    gray: 1\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
       SelectedChannelType grayChannel = r.getChannelSelection().getGrayChannel();
       assertThat(grayChannel.getChannelName(), equalTo("1"));
       assertThat(grayChannel.getContrastEnhancement(), nullContrast());
   }
   
   @Test
   public void testRasterBandSelectionRGBTerse() throws Exception {
       String yaml =
       "raster:\n"+
       "  channels:\n" +
       "    red: 1\n"+
       "    green: 2\n"+
       "    blue: 3\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
       SelectedChannelType[] rgbChannels = r.getChannelSelection().getRGBChannels();
       assertThat(rgbChannels[0].getChannelName(), equalTo("1"));
       assertThat(rgbChannels[2].getContrastEnhancement(), nullContrast());
       assertThat(rgbChannels[1].getChannelName(), equalTo("2"));
       assertThat(rgbChannels[2].getContrastEnhancement(), nullContrast());
       assertThat(rgbChannels[2].getChannelName(), equalTo("3"));
       assertThat(rgbChannels[2].getContrastEnhancement(), nullContrast());
   }
   
   @Test
   public void testMarkOpacity() throws Exception {
       String yaml =
               "point: \n"+
               "  symbols: \n" +
               "  - mark: \n" +
               "      fill-color: '#FF0000'\n"+
               "      fill-opacity: 0.5\n"; // Not just 'opacity'
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       
       assertThat(((Mark) p.getGraphic().graphicalSymbols().get(0)).getFill().getOpacity(), literal(lexEqualTo(0.5d)));
   }
   
   @Test
   public void testLineOffset() throws Exception {
       // See GEOT-3912
       String yaml =
               "line:\n"+
               "  stroke-color: '#555555'\n"+
               "  stroke-width: 1.0\n"+
               "  offset: 5";
                       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       LineSymbolizer l = SLD.lineSymbolizer(SLD.defaultStyle(sld));
       
       assertThat(l.getPerpendicularOffset(), is(literal(5)));
       // SLD/SE 1.1 feature that may not be supported by renderer
   }
   
   @SuppressWarnings("unchecked")
   @Test
   public void testPointDisplacement() throws Exception {
       String yaml =
               "point: \n"+
               "  displacement: (10, 42)\n"+
               "  symbols: \n" +
               "  - mark: \n" +
               "      fill-color: '#FF0000'\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(p.getGraphic().getDisplacement(), allOf(
               hasProperty("displacementX", literal(10)),
               hasProperty("displacementY", literal(42))));
       // SLD/SE 1.1 feature that may not be supported by renderer
   }
   @SuppressWarnings("unchecked")
   @Test
   public void testPointAnchor() throws Exception {
       String yaml =
               "point: \n"+
               "  anchor: (0.75, 0.25)\n"+
               "  symbols: \n" +
               "  - mark: \n" +
               "      fill-color: '#FF0000'\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       assertThat(p.getGraphic().getAnchorPoint(), allOf(
               hasProperty("anchorPointX", literal(0.75)),
               hasProperty("anchorPointY", literal(0.25))));
       // SLD/SE 1.1 feature that may not be supported by renderer
   }
   
   @SuppressWarnings("unchecked")
   @Test
   public void testTextDisplacement() throws Exception {
       String yaml =
               "text: \n"+
               "  displacement: (10, 42)\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       TextSymbolizer p = SLD.textSymbolizer(SLD.defaultStyle(sld));
       assertThat(((PointPlacement)p.getLabelPlacement()).getDisplacement(), allOf(
               hasProperty("displacementX", literal(10)),
               hasProperty("displacementY", literal(42))));
   }
   
   @SuppressWarnings("unchecked")
   @Test
   public void testTextAnchor() throws Exception {
       String yaml =
               "text: \n"+
               "  anchor: (0.75, 0.25)\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       TextSymbolizer t = SLD.textSymbolizer(SLD.defaultStyle(sld));
       assertThat(((PointPlacement)t.getLabelPlacement()).getAnchorPoint(), allOf(
               hasProperty("anchorPointX", literal(0.75)),
               hasProperty("anchorPointY", literal(0.25))));
   }
   
   @Test
   public void testTextPlacementType() throws Exception {
       String yaml =
               "text: \n"+
               "  placement: line\n"+
               "  offset: 4\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       TextSymbolizer t = SLD.textSymbolizer(SLD.defaultStyle(sld));
       assertThat(t.getLabelPlacement(), instanceOf(LinePlacement.class));
       Expression e = ((LinePlacement)t.getLabelPlacement()).getPerpendicularOffset();
       assertThat(((LinePlacement)t.getLabelPlacement()).getPerpendicularOffset(), 
               literal(4));
   }
   
   @SuppressWarnings("unchecked")
   @Test
   public void testTextGraphicDisplacement() throws Exception {
       String yaml =
               "text:\n"+
               "    label: ${name}\n"+
               "    displacement: (42,64)\n"+
               "    graphic:\n"+
               "      displacement: (10,15)\n"+
               "      symbols:\n"+
               "      - mark:\n"+
               "          shape: circle\n"+
               "          fill-color: '#995555'\n"+
               "";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       TextSymbolizer2 p = (TextSymbolizer2) SLD.textSymbolizer(SLD.defaultStyle(sld));
       assertThat(p.getGraphic().getDisplacement(), allOf(
               hasProperty("displacementX", literal(10)),
               hasProperty("displacementY", literal(15))));
       assertThat(((PointPlacement)p.getLabelPlacement()).getDisplacement(), allOf(
               hasProperty("displacementX", literal(42)),
               hasProperty("displacementY", literal(64))));
   }
   
   @SuppressWarnings("unchecked")
   @Test
   public void testRelativeExternalGraphicNoResolver() throws Exception {
       String yaml =
               "feature-styles:\n"+
               "- name: name\n"+
               "  rules:\n"+
               "  - symbolizers:\n"+
               "    - point:\n"+
               "        size: 32\n"+
               "        symbols:\n"+
               "        - external:\n"+
               "            url: smileyface.png\n"+
               "            format: image/png\n";
       
       StyledLayerDescriptor sld = Ysld.parse(yaml);
       
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       
       assertThat(p.getGraphic().graphicalSymbols().get(0), instanceOf(ExternalGraphic.class));
       ExternalGraphic eg = (ExternalGraphic) p.getGraphic().graphicalSymbols().get(0);
       assertThat(eg.getLocation(), equalTo(new URL("file:smileyface.png")));
       assertThat(eg.getOnlineResource().getLinkage(), anyOf(equalTo(new URI("smileyface.png")),equalTo(new URI("file:smileyface.png"))));
   }
   
   @Test
   public void testRelativeExternalGraphicWithResolver() throws Exception {
       String yaml =
               "feature-styles:\n"+
               "- name: name\n"+
               "  rules:\n"+
               "  - symbolizers:\n"+
               "    - point:\n"+
               "        size: 32\n"+
               "        symbols:\n"+
               "        - external:\n"+
               "            url: smileyface.png\n"+
               "            format: image/png\n";
       
       ResourceLocator locator = EasyMock.createMock(ResourceLocator.class);
       
       expect(locator.locateResource("smileyface.png")).andReturn(new URL("http://itworked/smileyface.png"));
       
       replay(locator);
       
       StyledLayerDescriptor sld = Ysld.parse(yaml, Collections.<ZoomContextFinder> emptyList(), locator);
       
       PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
       
       assertThat(p.getGraphic().graphicalSymbols().get(0), instanceOf(ExternalGraphic.class));
       ExternalGraphic eg = (ExternalGraphic) p.getGraphic().graphicalSymbols().get(0);
       assertThat(eg.getLocation(), equalTo(new URL("http://itworked/smileyface.png")));
       assertThat(eg.getOnlineResource().getLinkage(), equalTo(new URI("http://itworked/smileyface.png")));
       
       verify(locator);
   }

    @Test
    public void testTextSymbolizerPriority() throws Exception {
        String yaml =
            "text:\n"+
                    "    label: ${name}\n"+
                    "    priority: ${pop}\n"+
                    "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        TextSymbolizer2 p = (TextSymbolizer2) SLD.textSymbolizer(SLD.defaultStyle(sld));
        assertNotNull(p.getPriority());
        assertTrue(p.getPriority() instanceof PropertyName);
        assertEquals("pop", ((PropertyName) p.getPriority()).getPropertyName());
    }
    
    @Test
    public void testStrokeLinejoinDefault() throws Exception {
        String yaml =
            "line:\n"+
            "    stroke-color: \"#ff0000\"\n"+
            "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));
        assertThat(p, hasProperty("stroke", hasProperty("lineJoin", literal("miter"))));
    }
    
    @Test
    public void testStrokeLinejoinBevel() throws Exception {
        String yaml =
            "line:\n"+
            "    stroke-linejoin: bevel\n"+
            "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));
        assertThat(p, hasProperty("stroke", hasProperty("lineJoin", literal("bevel"))));
    }
    
    @Test
    public void testStrokeLinejoinMitre() throws Exception {
        String yaml =
            "line:\n"+
            "    stroke-linejoin: mitre\n"+
            "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));
        assertThat(p, hasProperty("stroke", hasProperty("lineJoin", literal("mitre"))));
    }

    @Test
    public void testSuite54() throws IOException{
        try (InputStream input = YsldTests.ysld("poly", "suite-54.ysld")){
            StyledLayerDescriptor sld = Ysld.parse(input);
            Style style = SLD.styles(sld)[0];
            TextSymbolizer text = SLD.textSymbolizer(style);
            LabelPlacement placement = text.getLabelPlacement();
            
            assertNotNull( placement );
        }        
    }
}
