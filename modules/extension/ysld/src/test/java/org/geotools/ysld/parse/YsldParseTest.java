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

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.geotools.ysld.TestUtils.appliesToScale;
import static org.geotools.ysld.TestUtils.attribute;
import static org.geotools.ysld.TestUtils.function;
import static org.geotools.ysld.TestUtils.isColor;
import static org.geotools.ysld.TestUtils.lexEqualTo;
import static org.geotools.ysld.TestUtils.literal;
import static org.geotools.ysld.TestUtils.nilExpression;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.easymock.classextension.EasyMock;
import org.geotools.filter.function.RecodeFunction;
import org.geotools.filter.function.string.ConcatenateFunction;
import org.geotools.process.function.ProcessFunction;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.styling.UomOgcMapping;
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
import org.opengis.style.ContrastEnhancement;
import org.opengis.style.ContrastMethod;
import org.opengis.style.Graphic;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.LinePlacement;
import org.opengis.style.Mark;
import org.opengis.style.PointPlacement;
import org.opengis.style.RasterSymbolizer;
import org.opengis.style.SelectedChannelType;
import org.yaml.snakeyaml.constructor.ConstructorException;

public class YsldParseTest {

    @Test
    public void testAnchor() throws Exception {
        String yaml =
                "blue: &blue rgb(0,0,255)\n"
                        + "point: \n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: *blue\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertEquals(Color.BLUE, SLD.color(SLD.fill(p)));
    }

    @Test
    public void testNamedColor() throws Exception {
        String yaml = "point: \n" + "  symbols: \n" + "  - mark: \n" + "      fill-color: blue\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertEquals(Color.BLUE, SLD.color(SLD.fill(p)));
    }

    @Test
    public void testFilterFunctionNoMarker() throws Exception {
        String yaml = "rules: \n" + "- filter: strEndsWith(foo,'bar') = true\n";

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
        String yaml = "rules: \n" + "- filter: ${strEndsWith(foo,'bar') = true}\n";

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
        String yaml = "rules: \n" + "- filter: ${scalerank < 4}\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        Rule r = SLD.defaultStyle(sld).featureTypeStyles().get(0).rules().get(0);

        Filter f = r.getFilter();
        assertThat(f, Matchers.instanceOf(PropertyIsLessThan.class));
        assertThat(((PropertyIsLessThan) f).getExpression1(), attribute("scalerank"));
        assertThat(((PropertyIsLessThan) f).getExpression2(), literal(4));
    }

    @Test
    public void testFilterWithEscape() throws Exception {
        String yaml = "rules: \n" + "- filter: ${foo = '\\$\\}'}\n";

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
                "feature-styles: \n"
                        + "- transform:\n"
                        + "    name: ras:Contour\n"
                        + "    params:\n"
                        + "      levels:\n"
                        + "      - 1000\n"
                        + "      - 1100\n"
                        + "      - 1200\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Expression tx = fs.getTransformation();
        assertNotNull(tx);

        ProcessFunction pf = (ProcessFunction) tx;

        assertThat(
                pf,
                hasProperty(
                        "parameters",
                        containsInAnyOrder(
                                rtParam("data"),
                                rtParam("levels", literal(1000), literal(1100), literal(1200)))));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    Matcher<Function> rtParam(final String name, final Matcher<?>... values) {
        return new BaseMatcher() {

            @Override
            public boolean matches(Object item) {
                Function f = (Function) item;
                List<Expression> parameters = f.getParameters();
                Literal nameExpr = (Literal) parameters.get(0);
                if (!nameExpr.getValue().equals(name)) {
                    return false;
                }
                if (values.length != parameters.size() - 1) {
                    return false;
                }
                for (int i = 0; i < values.length; i++) {
                    if (!values[i].matches(parameters.get(i + 1))) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Parameter named ");
                description.appendValue(name);
                if (values.length == 0) {
                    description.appendText(" with no values");
                } else if (values.length == 1) {
                    description.appendText(" with value ");
                    description.appendDescriptionOf(values[0]);
                } else {
                    description
                            .appendText(" with values ")
                            .appendList("[", ", ", "]", Arrays.asList(values));
                }
            }
        };
    }

    @Test
    public void testRenderingTransformationHeatmap() throws IOException {
        String yaml =
                "feature-styles: \n"
                        + "- transform:\n"
                        + "    name: vec:Heatmap\n"
                        + "    params:\n"
                        + "      weightAttr: pop2000\n"
                        + "      radius: 100\n"
                        + "      pixelsPerCell: 10\n"
                        + "      outputBBOX: ${env('wms_bbox')}\n"
                        + "      outputWidth: ${env('wms_width')}\n"
                        + "      outputHeight: ${env('wms_height')}\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Expression tx = fs.getTransformation();
        assertNotNull(tx);

        ProcessFunction pf = (ProcessFunction) tx;

        assertThat(
                pf,
                hasProperty(
                        "parameters",
                        containsInAnyOrder(
                                rtParam("data"),
                                rtParam("weightAttr", literal("pop2000")),
                                rtParam("radius", literal(100)),
                                rtParam("pixelsPerCell", literal(10)),
                                rtParam("outputBBOX", function("env", literal("wms_bbox"))),
                                rtParam("outputWidth", function("env", literal("wms_width"))),
                                rtParam("outputHeight", function("env", literal("wms_height"))))));
    }

    @Test
    public void testRenderingTransformationAlternateInputParam() throws IOException {
        String yaml =
                "feature-styles: \n"
                        + "- transform:\n"
                        + "    input: foo\n"
                        + "    name: ras:Contour\n"
                        + "    params:\n"
                        + "      levels:\n"
                        + "      - 1000\n"
                        + "      - 1100\n"
                        + "      - 1200\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Expression tx = fs.getTransformation();
        assertNotNull(tx);

        ProcessFunction pf = (ProcessFunction) tx;

        assertThat(
                pf,
                hasProperty(
                        "parameters",
                        containsInAnyOrder(
                                rtParam("foo"),
                                rtParam("levels", literal(1000), literal(1100), literal(1200)))));
    }

    @Test
    public void testRenderingTransformationWMSAuto() throws IOException {
        String yaml =
                "feature-styles: \n"
                        + "- transform:\n"
                        + "    name: vec:Heatmap\n"
                        + "    params:\n"
                        + "      weightAttr: pop2000\n"
                        + "      radius: 100\n"
                        + "      pixelsPerCell: 10\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Expression tx = fs.getTransformation();
        assertNotNull(tx);

        ProcessFunction pf = (ProcessFunction) tx;

        assertThat(
                pf,
                hasProperty(
                        "parameters",
                        containsInAnyOrder(
                                rtParam("data"),
                                rtParam("weightAttr", literal("pop2000")),
                                rtParam("radius", literal(100)),
                                rtParam("pixelsPerCell", literal(10)),
                                rtParam("outputBBOX", function("env", literal("wms_bbox"))),
                                rtParam("outputWidth", function("env", literal("wms_width"))),
                                rtParam("outputHeight", function("env", literal("wms_height"))))));
    }

    @Test
    public void testRenderingTransformationWMSAutoMixed() throws IOException {
        String yaml =
                "feature-styles: \n"
                        + "- transform:\n"
                        + "    name: vec:Heatmap\n"
                        + "    params:\n"
                        + "      weightAttr: pop2000\n"
                        + "      radius: 100\n"
                        + "      pixelsPerCell: 10\n"
                        + "      outputBBOX: ${env('test')}\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Expression tx = fs.getTransformation();
        assertNotNull(tx);

        ProcessFunction pf = (ProcessFunction) tx;

        assertThat(
                pf,
                hasProperty(
                        "parameters",
                        containsInAnyOrder(
                                rtParam("data"),
                                rtParam("weightAttr", literal("pop2000")),
                                rtParam("radius", literal(100)),
                                rtParam("pixelsPerCell", literal(10)),
                                rtParam("outputBBOX", function("env", literal("test"))),
                                rtParam("outputWidth", function("env", literal("wms_width"))),
                                rtParam("outputHeight", function("env", literal("wms_height"))))));
    }

    @Test
    public void testNestedRenderingTransformation() throws IOException {
        String yaml =
                "feature-styles:\n"
                        + "- transform:\n"
                        + "    name: ras:Contour\n"
                        + "    params:\n"
                        + "      data: \n"
                        + "        name: vec:BarnesSurface\n"
                        + "        input: data\n"
                        + "        params:\n"
                        + "          valuAttr: MxTmp\n"
                        + "      levels:\n"
                        + "      - -5\n"
                        + "      - 0\n"
                        + "      - 5\n"
                        + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Expression tx = fs.getTransformation();
        assertNotNull(tx);

        ProcessFunction pf = (ProcessFunction) tx;

        assertThat(pf, hasProperty("name", equalTo("ras:Contour")));
        Function param1 = (Function) pf.getParameters().get(1);
        Function param0 = (Function) pf.getParameters().get(0);
        assertThat(param1, rtParam("levels", literal(-5), literal(0), literal(5)));
        assertThat(
                param0, rtParam("data", allOf(hasProperty("name", equalTo("vec:BarnesSurface")))));

        assertThat(
                pf,
                hasProperty(
                        "parameters",
                        containsInAnyOrder(
                                rtParam(
                                        "data",
                                        allOf(
                                                instanceOf(ProcessFunction.class),
                                                hasProperty("name", equalTo("vec:BarnesSurface")))),
                                rtParam("levels", literal(-5), literal(0), literal(5)))));
    }

    @Test
    public void testLabelShield() throws Exception {
        String yaml =
                "feature-styles:\n"
                        + "- name: name\n"
                        + " rules:\n"
                        + " - symbolizers:\n"
                        + "   - line:\n"
                        + "       stroke-color: '#555555'\n"
                        + "       stroke-width: 1.0\n"
                        + "    - text:\n"
                        + "       label: name\n"
                        + "       symbols:\n"
                        + "        - mark:\n"
                        + "           shape: circle\n"
                        + "           fill-color: '#995555'\n"
                        + "       geometry: ${geom}";
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomSimple() throws IOException {
        String yaml =
                "grid:\n"
                        + "  initial-scale: 5000000\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple(0, 0)
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(
                                appliesToScale(5000000),
                                not(appliesToScale(5000000 / 2)),
                                not(appliesToScale(5000000 * 2)))));
    }

    @Test
    public void testZoomSimpleRange() throws IOException {
        String yaml =
                "grid:\n"
                        + "  initial-scale: 5000000\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple(1, 2)
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        Rule r = fs.rules().get(0);

        assertThat(r, appliesToScale(5000000 / 2)); // Z=1
        assertThat(r, appliesToScale(5000000 / 4)); // Z=2
        assertThat(r, not(appliesToScale(5000000))); // Z=0
        assertThat(r, not(appliesToScale(5000000 / 8))); // Z=3
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomSimpleWithDifferentInitial() throws IOException {
        String yaml =
                "grid:\n"
                        + "  initial-scale: 5000000\n"
                        + "  initial-level: 3\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple(0, 0)
                        + "\n"
                        + "  - zoom: "
                        + tuple(3, 3)
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(
                                appliesToScale(5000000 * 8),
                                not(appliesToScale(5000000 / 2 * 8)),
                                not(appliesToScale(5000000 * 2 * 8))),
                        allOf(
                                appliesToScale(5000000 * 8),
                                not(appliesToScale(5000000 / 2 * 8)),
                                not(appliesToScale(5000000 * 2 * 8)))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomList() throws IOException {
        String yaml =
                "grid:\n"
                        + "  scales:\n"
                        + "  - 5000000\n"
                        + "  - 2000000\n"
                        + "  - 1000000\n"
                        + "  - 500000\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple(0, 0)
                        + "\n"
                        + "  - zoom: "
                        + tuple(2, 2)
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(appliesToScale(5000000d), not(appliesToScale(2000000d))),
                        allOf(
                                appliesToScale(1000000d),
                                not(appliesToScale(2000000d)),
                                not(appliesToScale(500000d)))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomListWithInitial() throws IOException {
        String yaml =
                "grid:\n"
                        + "  initial-level: 3\n"
                        + "  scales:\n"
                        + "  - 5000000\n"
                        + "  - 2000000\n"
                        + "  - 1000000\n"
                        + "  - 500000\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple(3, 3)
                        + "\n"
                        + "  - zoom: "
                        + tuple(5, 5)
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(appliesToScale(5000000d), not(appliesToScale(2000000d))),
                        allOf(
                                appliesToScale(1000000d),
                                not(appliesToScale(2000000d)),
                                not(appliesToScale(500000d)))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomListWithRanges() throws IOException {
        String yaml =
                "grid:\n"
                        + "  scales:\n"
                        + "  - 5000000\n"
                        + "  - 2000000\n"
                        + "  - 1000000\n"
                        + "  - 500000\n"
                        + "  - 200000\n"
                        + "  - 100000\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple(null, 1)
                        + "\n"
                        + "  - zoom: "
                        + tuple(2, 3)
                        + "\n"
                        + "  - zoom: "
                        + tuple(4, null)
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(
                                appliesToScale(5000000d),
                                appliesToScale(2000000d),
                                not(appliesToScale(1000000d)),
                                not(appliesToScale(500000d)),
                                not(appliesToScale(200000d)),
                                not(appliesToScale(100000d))),
                        allOf(
                                not(appliesToScale(5000000d)),
                                not(appliesToScale(2000000d)),
                                appliesToScale(1000000d),
                                appliesToScale(500000d),
                                not(appliesToScale(200000d)),
                                not(appliesToScale(100000d))),
                        allOf(
                                not(appliesToScale(5000000d)),
                                not(appliesToScale(2000000d)),
                                not(appliesToScale(1000000d)),
                                not(appliesToScale(500000d)),
                                appliesToScale(200000d),
                                appliesToScale(100000d))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomListWithMinMaxKeywords() throws IOException {
        String yaml =
                "grid:\n"
                        + "  scales:\n"
                        + "  - 5000000\n"
                        + "  - 2000000\n"
                        + "  - 1000000\n"
                        + "  - 500000\n"
                        + "  - 200000\n"
                        + "  - 100000\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple("min", 1)
                        + "\n"
                        + "  - zoom: "
                        + tuple(2, 3)
                        + "\n"
                        + "  - zoom: "
                        + tuple(4, "max")
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(
                                appliesToScale(5000000d),
                                appliesToScale(2000000d),
                                not(appliesToScale(1000000d)),
                                not(appliesToScale(500000d)),
                                not(appliesToScale(200000d)),
                                not(appliesToScale(100000d))),
                        allOf(
                                not(appliesToScale(5000000d)),
                                not(appliesToScale(2000000d)),
                                appliesToScale(1000000d),
                                appliesToScale(500000d),
                                not(appliesToScale(200000d)),
                                not(appliesToScale(100000d))),
                        allOf(
                                not(appliesToScale(5000000d)),
                                not(appliesToScale(2000000d)),
                                not(appliesToScale(1000000d)),
                                not(appliesToScale(500000d)),
                                appliesToScale(200000d),
                                appliesToScale(100000d))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testScaleWithMinMaxKeywords() throws IOException {
        String yaml =
                "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - scale: "
                        + tuple(1500000, "max")
                        + "\n"
                        + "  - scale: "
                        + tuple(300000, 1500000)
                        + "\n"
                        + "  - scale: "
                        + tuple("min", 300000)
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(
                                appliesToScale(5000000d),
                                appliesToScale(2000000d),
                                not(appliesToScale(1000000d)),
                                not(appliesToScale(500000d)),
                                not(appliesToScale(200000d)),
                                not(appliesToScale(100000d))),
                        allOf(
                                not(appliesToScale(5000000d)),
                                not(appliesToScale(2000000d)),
                                appliesToScale(1000000d),
                                appliesToScale(500000d),
                                not(appliesToScale(200000d)),
                                not(appliesToScale(100000d))),
                        allOf(
                                not(appliesToScale(5000000d)),
                                not(appliesToScale(2000000d)),
                                not(appliesToScale(1000000d)),
                                not(appliesToScale(500000d)),
                                appliesToScale(200000d),
                                appliesToScale(100000d))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testScaleWithNull() throws IOException {
        String yaml =
                "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - scale: "
                        + tuple(1500000, null)
                        + "\n"
                        + "  - scale: "
                        + tuple(300000, 1500000)
                        + "\n"
                        + "  - scale: "
                        + tuple(null, 300000)
                        + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(
                                appliesToScale(5000000d),
                                appliesToScale(2000000d),
                                not(appliesToScale(1000000d)),
                                not(appliesToScale(500000d)),
                                not(appliesToScale(200000d)),
                                not(appliesToScale(100000d))),
                        allOf(
                                not(appliesToScale(5000000d)),
                                not(appliesToScale(2000000d)),
                                appliesToScale(1000000d),
                                appliesToScale(500000d),
                                not(appliesToScale(200000d)),
                                not(appliesToScale(100000d))),
                        allOf(
                                not(appliesToScale(5000000d)),
                                not(appliesToScale(2000000d)),
                                not(appliesToScale(1000000d)),
                                not(appliesToScale(500000d)),
                                appliesToScale(200000d),
                                appliesToScale(100000d))));
    }

    static final String GOOGLE_MERCATOR_TEST_RULES =
            "  - zoom: "
                    + tuple(0, 0)
                    + "\n"
                    + "  - zoom: "
                    + tuple(1, 1)
                    + "\n"
                    + "  - zoom: "
                    + tuple(2, 2)
                    + "\n"
                    + "  - zoom: "
                    + tuple(3, 3)
                    + "\n"
                    + "  - zoom: "
                    + tuple(4, 4)
                    + "\n"
                    + "  - zoom: "
                    + tuple(5, 5)
                    + "\n"
                    + "  - zoom: "
                    + tuple(6, 6)
                    + "\n"
                    + "  - zoom: "
                    + tuple(7, 7)
                    + "\n"
                    + "  - zoom: "
                    + tuple(8, 8)
                    + "\n"
                    + "  - zoom: "
                    + tuple(9, 9)
                    + "\n"
                    + "  - zoom: "
                    + tuple(10, 10)
                    + "\n"
                    + "  - zoom: "
                    + tuple(11, 11)
                    + "\n"
                    + "  - zoom: "
                    + tuple(12, 12)
                    + "\n"
                    + "  - zoom: "
                    + tuple(13, 13)
                    + "\n"
                    + "  - zoom: "
                    + tuple(14, 14)
                    + "\n"
                    + "  - zoom: "
                    + tuple(15, 15)
                    + "\n"
                    + "  - zoom: "
                    + tuple(16, 16)
                    + "\n"
                    + "  - zoom: "
                    + tuple(17, 17)
                    + "\n"
                    + "  - zoom: "
                    + tuple(18, 18)
                    + "\n"
                    + "  - zoom: "
                    + tuple(19, 19)
                    + "\n";

    static final String WGS84_TEST_RULES =
            "  - zoom: "
                    + tuple(0, 0)
                    + "\n"
                    + "    name: WGS84:00\n"
                    + "  - zoom: "
                    + tuple(1, 1)
                    + "\n"
                    + "    name: WGS84:01\n"
                    + "  - zoom: "
                    + tuple(2, 2)
                    + "\n"
                    + "    name: WGS84:02\n"
                    + "  - zoom: "
                    + tuple(3, 3)
                    + "\n"
                    + "    name: WGS84:03\n"
                    + "  - zoom: "
                    + tuple(4, 4)
                    + "\n"
                    + "    name: WGS84:04\n"
                    + "  - zoom: "
                    + tuple(5, 5)
                    + "\n"
                    + "    name: WGS84:05\n"
                    + "  - zoom: "
                    + tuple(6, 6)
                    + "\n"
                    + "    name: WGS84:06\n"
                    + "  - zoom: "
                    + tuple(7, 7)
                    + "\n"
                    + "    name: WGS84:07\n"
                    + "  - zoom: "
                    + tuple(8, 8)
                    + "\n"
                    + "    name: WGS84:08\n"
                    + "  - zoom: "
                    + tuple(9, 9)
                    + "\n"
                    + "    name: WGS84:09\n"
                    + "  - zoom: "
                    + tuple(10, 10)
                    + "\n"
                    + "    name: WGS84:10\n"
                    + "  - zoom: "
                    + tuple(11, 11)
                    + "\n"
                    + "    name: WGS84:11\n"
                    + "  - zoom: "
                    + tuple(12, 12)
                    + "\n"
                    + "    name: WGS84:12\n"
                    + "  - zoom: "
                    + tuple(13, 13)
                    + "\n"
                    + "    name: WGS84:13\n"
                    + "  - zoom: "
                    + tuple(14, 14)
                    + "\n"
                    + "    name: WGS84:14\n"
                    + "  - zoom: "
                    + tuple(15, 15)
                    + "\n"
                    + "    name: WGS84:15\n"
                    + "  - zoom: "
                    + tuple(16, 16)
                    + "\n"
                    + "    name: WGS84:16\n"
                    + "  - zoom: "
                    + tuple(17, 17)
                    + "\n"
                    + "    name: WGS84:17\n"
                    + "  - zoom: "
                    + tuple(18, 18)
                    + "\n"
                    + "    name: WGS84:18\n"
                    + "  - zoom: "
                    + tuple(19, 19)
                    + "\n"
                    + "    name: WGS84:19\n"
                    + "  - zoom: "
                    + tuple(20, 20)
                    + "\n"
                    + "    name: WGS84:20\n";

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
        String yaml = "feature-styles: \n" + "- name: name\n" + "  rules:\n" + WGS84_TEST_RULES;

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        doTestForWGS84(sld);
    }

    @Test
    public void testNamed() throws IOException {
        String yaml =
                "grid:\n"
                        + "  name: WebMercator\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + GOOGLE_MERCATOR_TEST_RULES;

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        doTestForGoogleMercator(sld);
    }

    @SuppressWarnings("unchecked")
    private void doTestForGoogleMercator(StyledLayerDescriptor sld) throws IOException {
        double scaleDenominators[] = new double[GOOGLE_MERCATOR_PIXEL_SIZES.length];
        for (int i = 0; i < GOOGLE_MERCATOR_PIXEL_SIZES.length; i++) {
            scaleDenominators[i] = OGC_DPI * INCHES_PER_METRE * GOOGLE_MERCATOR_PIXEL_SIZES[i];
        }

        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        for (int i = 0; i < GOOGLE_MERCATOR_PIXEL_SIZES.length; i++) {
            scaleDenominators[i] = OGC_DPI * INCHES_PER_METRE * GOOGLE_MERCATOR_PIXEL_SIZES[i];
        }

        assertThat(fs.rules().size(), is(20));

        assertThat(
                fs.rules().get(0),
                allOf(
                        appliesToScale(scaleDenominators[0]),
                        not(appliesToScale(scaleDenominators[1]))));
        assertThat(
                fs.rules().get(19),
                allOf(
                        appliesToScale(scaleDenominators[19]),
                        not(appliesToScale(scaleDenominators[18]))));
        for (int i = 1; i < 19; i++) {
            assertThat(
                    fs.rules().get(i),
                    describedAs(
                            "rule applies only to level %0 (%1)",
                            allOf(
                                    appliesToScale(scaleDenominators[i]),
                                    not(appliesToScale(scaleDenominators[i + 1])),
                                    not(appliesToScale(scaleDenominators[i + -1]))),
                            i,
                            scaleDenominators[i]));
        }
    }

    Matcher<Double> mCloseTo(final double value, final double epsilon) {
        return new BaseMatcher<Double>() {

            @Override
            public boolean matches(Object arg0) {
                return Math.abs(value / (Double) arg0 - 1) <= epsilon;
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

        for (int i = 0; i < WGS84_SCALE_DENOMS.length; i++) {
            assertThat(
                    context.getScaleDenominator(i), mCloseTo(WGS84_SCALE_DENOMS[i], 0.00000001d));
        }
    }

    @SuppressWarnings("unchecked")
    private void doTestForWGS84(StyledLayerDescriptor sld) throws IOException {
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);

        assertThat(fs.rules().size(), is(21));

        Rule first = fs.rules().get(0);
        assertThat(
                first,
                allOf(
                        appliesToScale(WGS84_SCALE_DENOMS[0]),
                        not(appliesToScale(WGS84_SCALE_DENOMS[1]))));

        for (int i = 1; i < 20; i++) {
            Rule r = fs.rules().get(i);
            assertThat(
                    r,
                    describedAs(
                            "rule applies only to level %0 (%1)",
                            allOf(
                                    appliesToScale(WGS84_SCALE_DENOMS[i]),
                                    not(appliesToScale(WGS84_SCALE_DENOMS[i + 1])),
                                    not(appliesToScale(WGS84_SCALE_DENOMS[i + -1]))),
                            i,
                            WGS84_SCALE_DENOMS[i]));
        }

        Rule last = fs.rules().get(20);
        assertThat(
                last,
                allOf(
                        appliesToScale(WGS84_SCALE_DENOMS[20]),
                        not(appliesToScale(WGS84_SCALE_DENOMS[19]))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNamedWithFinder() throws IOException {
        String yaml =
                "grid:\n"
                        + "  name: test\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple(0, 0);

        ZoomContextFinder finder = createMock(ZoomContextFinder.class);
        ZoomContext context = createMock(ZoomContext.class);

        expect(finder.get("test")).andReturn(context);
        expect(context.getRange(0, 0)).andReturn(new ScaleRange(42, 64));

        replay(finder, context);

        StyledLayerDescriptor sld = Ysld.parse(yaml, Arrays.asList(finder), (ResourceLocator) null);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        fs.rules().get(0).getMaxScaleDenominator();
        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(
                                Matchers.<Rule>hasProperty(
                                        "maxScaleDenominator", Matchers.closeTo(64, 0.0000001d)),
                                Matchers.<Rule>hasProperty(
                                        "minScaleDenominator", Matchers.closeTo(42, 0.0000001d)))));

        verify(finder, context);
    }

    @Test
    public void testWellKnownWithCustomFinder() throws IOException {
        String yaml =
                "grid:\n"
                        + "  name: WebMercator\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + GOOGLE_MERCATOR_TEST_RULES;

        ZoomContextFinder finder = createMock(ZoomContextFinder.class);

        expect(finder.get("WebMercator")).andReturn(null);

        replay(finder);

        StyledLayerDescriptor sld = Ysld.parse(yaml, Arrays.asList(finder), (ResourceLocator) null);
        doTestForGoogleMercator(
                sld); // The additional finder doesn't have a WebMercator context and so should not
        // interfere.

        verify(finder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCustomFinderOverridesWellKnown() throws IOException {
        String yaml =
                "grid:\n"
                        + "  name: WebMercator\n"
                        + "feature-styles: \n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - zoom: "
                        + tuple(0, 0);

        ZoomContextFinder finder = createMock(ZoomContextFinder.class);
        ZoomContext context = createMock(ZoomContext.class);

        expect(finder.get("WebMercator")).andReturn(context);
        expect(context.getRange(0, 0)).andReturn(new ScaleRange(42, 64));

        replay(finder, context);

        StyledLayerDescriptor sld = Ysld.parse(yaml, Arrays.asList(finder), (ResourceLocator) null);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        fs.rules().get(0).getMaxScaleDenominator();
        assertThat(
                (Iterable<Rule>) fs.rules(),
                hasItems(
                        allOf(
                                Matchers.<Rule>hasProperty(
                                        "maxScaleDenominator", Matchers.closeTo(64, 0.0000001d)),
                                Matchers.<Rule>hasProperty(
                                        "minScaleDenominator", Matchers.closeTo(42, 0.0000001d)))));

        verify(finder, context);
    }

    @Test
    public void testParseNoStrokeFillDefaults() throws Exception {
        String yaml = "polygon: \n" + "  fill-color: blue\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PolygonSymbolizer p = (PolygonSymbolizer) SLD.symbolizers(SLD.defaultStyle(sld))[0];
        assertEquals(Color.BLUE, SLD.color(SLD.fill(p)));
        assertNull(SLD.stroke(p));

        yaml = "polygon: \n" + "  stroke-color: blue\n";

        sld = Ysld.parse(yaml);
        p = (PolygonSymbolizer) SLD.symbolizers(SLD.defaultStyle(sld))[0];
        assertEquals(Color.BLUE, SLD.color(SLD.stroke(p)));
        assertNull(SLD.fill(p));
    }

    static String tuple(Object... values) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        String join = "";
        for (Object o : values) {
            builder.append(join);
            join = ", ";
            if (o == null) {
                builder.append("null");
            } else {
                String s = o.toString();
                if (s.isEmpty() || s.startsWith("#") || s.equalsIgnoreCase("null")) {
                    builder.append(String.format("'%s'", s));
                } else {
                    builder.append(s);
                }
            }
        }
        builder.append("]");
        return builder.toString();
    }

    @Test
    public void testColourMap() throws Exception {
        String yaml =
                "raster: \n"
                        + "  color-map:\n"
                        + "    type: values\n"
                        + "    entries:\n"
                        + "    - "
                        + tuple("#ff0000", "1.0", "0", "start")
                        + "\n"
                        + "    - "
                        + tuple("#00ff00", "1.0", "500", "middle")
                        + "\n"
                        + "    - "
                        + tuple("#0000ff", "1.0", "1000", "end")
                        + "\n"
                        + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        RasterSymbolizer symb = (RasterSymbolizer) fs.rules().get(0).symbolizers().get(0);

        // need to use the geotools.styling interface as it provides the accessors for the entries.
        ColorMap map = (ColorMap) symb.getColorMap();

        Color colour1 = (Color) map.getColorMapEntry(0).getColor().evaluate(null);
        Color colour2 = (Color) map.getColorMapEntry(1).getColor().evaluate(null);
        Color colour3 = (Color) map.getColorMapEntry(2).getColor().evaluate(null);

        assertThat(colour1, is(Color.RED));
        assertThat(colour2, is(Color.GREEN));
        assertThat(colour3, is(Color.BLUE));
    }

    @Test
    public void testColourMapExpression() throws Exception {
        String yaml =
                "raster: \n"
                        + "  color-map:\n"
                        + "    type: values\n"
                        + "    entries:\n"
                        + "    - "
                        + tuple("#ff0000", "1.0", "0", "start")
                        + "\n"
                        + "    - "
                        + tuple("#00ff00", "1.0", "500", "middle")
                        + "\n"
                        + "    - "
                        + tuple("#0000ff", "'${pow(0.75, 1.2)}'", "1000", "end")
                        + "\n"
                        + // Expression containing comma needs to be quoted
                        "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        RasterSymbolizer symb = (RasterSymbolizer) fs.rules().get(0).symbolizers().get(0);

        // need to use the geotools.styling interface as it provides the accessors for the entries.
        ColorMap map = (ColorMap) symb.getColorMap();

        assertThat(
                map.getColorMapEntry(2).getOpacity(), function("pow", literal(0.75), literal(1.2)));
    }

    @Test
    public void testColourMapEmpty() throws Exception {
        String yaml =
                "raster: \n"
                        + "  color-map:\n"
                        + "    type: values\n"
                        + "    entries:\n"
                        + "    - "
                        + tuple("#ff0000", "1.0", "0", "start")
                        + "\n"
                        + "    - "
                        + tuple("#00ff00", "", "500", "middle")
                        + "\n"
                        + "    - "
                        + tuple("#0000ff", null, "1000", "end")
                        + "\n"
                        + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        RasterSymbolizer symb = (RasterSymbolizer) fs.rules().get(0).symbolizers().get(0);

        // need to use the geotools.styling interface as it provides the accessors for the entries.
        ColorMap map = (ColorMap) symb.getColorMap();

        // System.out.println(map.getColorMapEntry(0).getColor().evaluate(null));
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
        String yaml = "text: \n" + "  label: test literal\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);

        Expression label = symb.getLabel();
        assertThat(
                label,
                allOf(instanceOf(Literal.class), hasProperty("value", equalTo("test literal"))));
    }

    @Test
    public void testLabelEmbeded() throws Exception {
        String yaml = "text: \n" + "  label: literal0${attribute1}literal2\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);

        Expression label = symb.getLabel();
        assertThat(label, instanceOf(ConcatenateFunction.class));
        List<Expression> params = ((ConcatenateFunction) label).getParameters();
        assertThat(params.size(), is(3));
        assertThat(params.get(0), literal(equalTo("literal0")));
        assertThat(params.get(1), attribute("attribute1"));
        assertThat(params.get(2), literal(equalTo("literal2")));
    }

    @Test
    public void testLabelAttribute() throws Exception {
        String yaml = "text: \n" + "  label: ${testAttribute}\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);

        Expression label = symb.getLabel();
        assertThat(label, attribute("testAttribute"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testExpressionLiteral() throws Exception {
        String yaml = "text: \n" + "  geometry: test literal\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);

        Expression expr = symb.getGeometry();
        assertThat(
                expr,
                allOf(instanceOf(Literal.class), hasProperty("value", equalTo("test literal"))));
    }

    @Test
    public void testExpressionEmbeded() throws Exception {
        String yaml = "text: \n" + "  geometry: literal0${attribute1}literal2\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);

        Expression expr = symb.getGeometry();
        assertThat(expr, instanceOf(ConcatenateFunction.class));
        List<Expression> params = ((ConcatenateFunction) expr).getParameters();
        assertThat(params.size(), is(3));
        assertThat(params.get(0), literal(equalTo("literal0")));
        assertThat(params.get(1), attribute("attribute1"));
        assertThat(params.get(2), literal(equalTo("literal2")));
    }

    @Test
    public void testExpressionLong() throws Exception {
        String yaml =
                "polygon:\n"
                        + "  fill-color: ${recode(MAPCOLOR7, 1.0, '#FFC3C3', 2.0, '#FFE3C3', 3.0, '#FFFFC3', 4.0, '#C3FFE3', 5.0, '#C3FFFF', 6.0, '#C3C3FF', 7.0, '#BFC3FF')}\n";
        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        PolygonSymbolizer symb = (PolygonSymbolizer) fs.rules().get(0).symbolizers().get(0);

        Expression expr = symb.getFill().getColor();
        assertThat(expr, instanceOf(RecodeFunction.class));
        List<Expression> params = ((RecodeFunction) expr).getParameters();
        assertThat(params.size(), is(7 * 2 + 1));
        int i = 0;
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
                "polygon:\n"
                        + "  fill-color: ${recode(MAPCOLOR7, \n"
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
        List<Expression> params = ((RecodeFunction) expr).getParameters();
        assertThat(params.size(), is(7 * 2 + 1));
        int i = 0;
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
                "polygon:\n"
                        + "  fill-color: >\n"
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
        List<Expression> params = ((RecodeFunction) expr).getParameters();
        assertThat(params.size(), is(7 * 2 + 1));
        int i = 0;
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
                "polygon:\n"
                        + "  fill-color: |\n"
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
        List<Expression> params = ((RecodeFunction) expr).getParameters();
        assertThat(params.size(), is(7 * 2 + 1));
        int i = 0;
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
        String yaml = "text: \n" + "  geometry: ${testAttribute}\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        FeatureTypeStyle fs = SLD.defaultStyle(sld).featureTypeStyles().get(0);
        TextSymbolizer symb = (TextSymbolizer) fs.rules().get(0).symbolizers().get(0);

        Expression expr = symb.getGeometry();
        assertThat(expr, attribute("testAttribute"));
    }

    @Test
    public void testBadExpression() throws Exception {
        String yaml = "polygon: \n" + "  stroke-width: ${round(foo) 1000}\n";
        try {
            Ysld.parse(yaml);
            fail("Bad expression should have thrown exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDynamicColor() throws Exception {
        String yaml =
                "point: \n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: ${colourAttribute}\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(SLD.fill(p).getColor(), attribute("colourAttribute"));
    }

    @Test
    public void testEvilExpression1() throws Exception {
        String yaml =
                "point: \n" + "  symbols: \n" + "  - mark: \n" + "      fill-color: \\$\\}\\\\\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(SLD.fill(p).getColor(), literal(equalTo("$}\\")));
    }

    @Test
    public void testColorHex() throws Exception {
        String yaml =
                "point: \n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: 0x001122\n"
                        + "      stroke-color: 0x334455\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(SLD.fill(p).getColor(), literal(isColor("001122")));
        assertThat(SLD.stroke(p).getColor(), literal(isColor("334455")));
    }

    @Test
    public void testColorQuotedHex() throws Exception {
        String yaml =
                "point: \n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: '0x001122'\n"
                        + "      stroke-color: '0x334455'\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(SLD.fill(p).getColor(), literal(isColor("001122")));
        assertThat(SLD.stroke(p).getColor(), literal(isColor("334455")));
    }

    @Test
    public void testColorQuotedHash() throws Exception {
        String yaml =
                "point: \n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: '#001122'\n"
                        + "      stroke-color: '#334455'\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(SLD.fill(p).getColor(), literal(isColor("001122")));
        assertThat(SLD.stroke(p).getColor(), literal(isColor("334455")));
    }

    @Test
    public void testColorQuotedBare() throws Exception {
        String yaml =
                "point: \n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: '001122'\n"
                        + "      stroke-color: '334455'\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(SLD.fill(p).getColor(), literal(isColor("001122")));
        assertThat(SLD.stroke(p).getColor(), literal(isColor("334455")));
    }

    @Test
    public void testColorSexegesimal() throws Exception {
        String yaml =
                "point: \n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: 1:17:40:20:15\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(SLD.fill(p).getColor(), literal(isColor("FFFFFF")));
    }

    @Test
    public void testRasterBandSelectionGray() throws Exception {
        String yaml = "raster:\n" + "  channels:\n" + "    gray:\n" + "      name: foo\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
        SelectedChannelType grayChannel = r.getChannelSelection().getGrayChannel();
        assertThat(grayChannel.getChannelName().evaluate(null, String.class), equalTo("foo"));
        assertThat(grayChannel.getContrastEnhancement(), nullContrast());
    }

    @Test
    public void testRasterBandSelectionGreyWithContrast() throws Exception {
        String yaml =
                "raster:\n"
                        + "  channels:\n"
                        + "    gray:\n"
                        + "      name: foo\n"
                        + "      contrast-enhancement:\n"
                        + "        mode: normalize\n"
                        + "        gamma: 1.2\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
        SelectedChannelType grayChannel = r.getChannelSelection().getGrayChannel();
        assertThat(grayChannel.getChannelName().evaluate(null, String.class), equalTo("foo"));
        assertThat(grayChannel.getContrastEnhancement().getGammaValue(), literal(equalTo("1.2")));
        assertThat(
                grayChannel.getContrastEnhancement().getMethod(),
                equalTo(ContrastMethod.NORMALIZE));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static Matcher<ContrastEnhancement> nullContrast() {
        return (Matcher)
                describedAs(
                        "Null Contrast Enhancement",
                        anyOf(
                                nullValue(),
                                allOf(
                                        hasProperty("gammaValue", nilExpression()),
                                        hasProperty(
                                                "method",
                                                anyOf(nullValue(), is(ContrastMethod.NONE))))));
    }

    @Test
    public void testRasterBandSelectionRGB() throws Exception {
        String yaml =
                "raster:\n"
                        + "  channels:\n"
                        + "    red:\n"
                        + "      name: foo\n"
                        + "    green:\n"
                        + "      name: bar\n"
                        + "      contrast-enhancement:\n"
                        + "        mode: normalize\n"
                        + "    blue:\n"
                        + "      name: baz\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));

        SelectedChannelType[] rgbChannels = r.getChannelSelection().getRGBChannels();

        assertThat(rgbChannels[0].getChannelName().evaluate(null, String.class), equalTo("foo"));
        assertThat(rgbChannels[1].getChannelName().evaluate(null, String.class), equalTo("bar"));
        assertThat(rgbChannels[2].getChannelName().evaluate(null, String.class), equalTo("baz"));

        assertThat(rgbChannels[0].getContrastEnhancement(), nullContrast());
        assertThat(rgbChannels[1].getContrastEnhancement().getGammaValue(), nilExpression());
        assertThat(
                rgbChannels[1].getContrastEnhancement().getMethod(),
                equalTo(ContrastMethod.NORMALIZE));
        assertThat(rgbChannels[2].getContrastEnhancement(), nullContrast());
    }

    @Test
    public void testRasterBandSelectionGrayTerse() throws Exception {
        String yaml = "raster:\n" + "  channels:\n" + "    gray: 1\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
        SelectedChannelType grayChannel = r.getChannelSelection().getGrayChannel();
        assertThat(grayChannel.getChannelName().evaluate(null, String.class), equalTo("1"));
        assertThat(grayChannel.getContrastEnhancement(), nullContrast());
    }

    @Test
    public void testRasterBandSelectionRGBTerse() throws Exception {
        String yaml =
                "raster:\n" + "  channels:\n" + "    red: 1\n" + "    green: 2\n" + "    blue: 3\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);
        RasterSymbolizer r = SLD.rasterSymbolizer(SLD.defaultStyle(sld));
        SelectedChannelType[] rgbChannels = r.getChannelSelection().getRGBChannels();
        assertThat(rgbChannels[0].getChannelName().evaluate(null, String.class), equalTo("1"));
        assertThat(rgbChannels[2].getContrastEnhancement(), nullContrast());
        assertThat(rgbChannels[1].getChannelName().evaluate(null, String.class), equalTo("2"));
        assertThat(rgbChannels[2].getContrastEnhancement(), nullContrast());
        assertThat(rgbChannels[2].getChannelName().evaluate(null, String.class), equalTo("3"));
        assertThat(rgbChannels[2].getContrastEnhancement(), nullContrast());
    }

    @Test
    public void testMarkOpacity() throws Exception {
        String yaml =
                "point: \n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: '#FF0000'\n"
                        + "      fill-opacity: 0.5\n"; // Not just 'opacity'

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));

        assertThat(
                ((Mark) p.getGraphic().graphicalSymbols().get(0)).getFill().getOpacity(),
                literal(lexEqualTo(0.5d)));
    }

    @Test
    public void testLineOffset() throws Exception {
        // See GEOT-3912
        String yaml =
                "line:\n" + "  stroke-color: '#555555'\n" + "  stroke-width: 1.0\n" + "  offset: 5";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer l = SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(l.getPerpendicularOffset(), is(literal(5)));
        // SLD/SE 1.1 feature that may not be supported by renderer
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPointDisplacement() throws Exception {
        String yaml =
                "point: \n"
                        + "  displacement: "
                        + tuple(10, 42)
                        + "\n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: '#FF0000'\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(
                p.getGraphic().getDisplacement(),
                allOf(
                        hasProperty("displacementX", literal(10)),
                        hasProperty("displacementY", literal(42))));
        // SLD/SE 1.1 feature that may not be supported by renderer
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPointAnchor() throws Exception {
        String yaml =
                "point: \n"
                        + "  anchor: "
                        + tuple(0.75, 0.25)
                        + "\n"
                        + "  symbols: \n"
                        + "  - mark: \n"
                        + "      fill-color: '#FF0000'\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));
        assertThat(
                p.getGraphic().getAnchorPoint(),
                allOf(
                        hasProperty("anchorPointX", literal(0.75)),
                        hasProperty("anchorPointY", literal(0.25))));
        // SLD/SE 1.1 feature that may not be supported by renderer
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testTextDisplacement() throws Exception {
        String yaml = "text: \n" + "  displacement: " + tuple(10, 42) + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        TextSymbolizer p = SLD.textSymbolizer(SLD.defaultStyle(sld));
        assertThat(
                ((PointPlacement) p.getLabelPlacement()).getDisplacement(),
                allOf(
                        hasProperty("displacementX", literal(10)),
                        hasProperty("displacementY", literal(42))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testTextAnchor() throws Exception {
        String yaml = "text: \n" + "  anchor: " + tuple(0.75, 0.25) + "\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        TextSymbolizer t = SLD.textSymbolizer(SLD.defaultStyle(sld));
        assertThat(
                ((PointPlacement) t.getLabelPlacement()).getAnchorPoint(),
                allOf(
                        hasProperty("anchorPointX", literal(0.75)),
                        hasProperty("anchorPointY", literal(0.25))));
    }

    @Test
    public void testTextPlacementType() throws Exception {
        String yaml = "text: \n" + "  placement: line\n" + "  offset: 4\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        TextSymbolizer t = SLD.textSymbolizer(SLD.defaultStyle(sld));
        assertThat(t.getLabelPlacement(), instanceOf(LinePlacement.class));
        Expression e = ((LinePlacement) t.getLabelPlacement()).getPerpendicularOffset();
        assertThat(((LinePlacement) t.getLabelPlacement()).getPerpendicularOffset(), literal(4));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testTextGraphicDisplacement() throws Exception {
        String yaml =
                "text:\n"
                        + "    label: ${name}\n"
                        + "    displacement: "
                        + tuple(42, 64)
                        + "\n"
                        + "    graphic:\n"
                        + "      displacement: "
                        + tuple(10, 15)
                        + "\n"
                        + "      symbols:\n"
                        + "      - mark:\n"
                        + "          shape: circle\n"
                        + "          fill-color: '#995555'\n"
                        + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        TextSymbolizer2 p = (TextSymbolizer2) SLD.textSymbolizer(SLD.defaultStyle(sld));
        assertThat(
                p.getGraphic().getDisplacement(),
                allOf(
                        hasProperty("displacementX", literal(10)),
                        hasProperty("displacementY", literal(15))));
        assertThat(
                ((PointPlacement) p.getLabelPlacement()).getDisplacement(),
                allOf(
                        hasProperty("displacementX", literal(42)),
                        hasProperty("displacementY", literal(64))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRelativeExternalGraphicNoResolver() throws Exception {
        String yaml =
                "feature-styles:\n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - symbolizers:\n"
                        + "    - point:\n"
                        + "        size: 32\n"
                        + "        symbols:\n"
                        + "        - external:\n"
                        + "            url: smileyface.png\n"
                        + "            format: image/png\n";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));

        assertThat(p.getGraphic().graphicalSymbols().get(0), instanceOf(ExternalGraphic.class));
        ExternalGraphic eg = (ExternalGraphic) p.getGraphic().graphicalSymbols().get(0);
        assertThat(eg.getLocation(), equalTo(new URL("file:smileyface.png")));
        assertThat(
                eg.getOnlineResource().getLinkage(),
                anyOf(equalTo(new URI("smileyface.png")), equalTo(new URI("file:smileyface.png"))));
    }

    @Test
    public void testRelativeExternalGraphicWithResolver() throws Exception {
        String yaml =
                "feature-styles:\n"
                        + "- name: name\n"
                        + "  rules:\n"
                        + "  - symbolizers:\n"
                        + "    - point:\n"
                        + "        size: 32\n"
                        + "        symbols:\n"
                        + "        - external:\n"
                        + "            url: smileyface.png\n"
                        + "            format: image/png\n";

        ResourceLocator locator = EasyMock.createMock(ResourceLocator.class);

        expect(locator.locateResource("smileyface.png"))
                .andReturn(new URL("http://itworked/smileyface.png"));

        replay(locator);

        StyledLayerDescriptor sld =
                Ysld.parse(yaml, Collections.<ZoomContextFinder>emptyList(), locator);

        PointSymbolizer p = SLD.pointSymbolizer(SLD.defaultStyle(sld));

        assertThat(p.getGraphic().graphicalSymbols().get(0), instanceOf(ExternalGraphic.class));
        ExternalGraphic eg = (ExternalGraphic) p.getGraphic().graphicalSymbols().get(0);
        assertThat(eg.getLocation(), equalTo(new URL("http://itworked/smileyface.png")));
        assertThat(
                eg.getOnlineResource().getLinkage(),
                equalTo(new URI("http://itworked/smileyface.png")));

        verify(locator);
    }

    @Test
    public void testTextSymbolizerPriority() throws Exception {
        String yaml = "text:\n" + "    label: ${name}\n" + "    priority: ${pop}\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        TextSymbolizer2 p = (TextSymbolizer2) SLD.textSymbolizer(SLD.defaultStyle(sld));
        assertNotNull(p.getPriority());
        assertTrue(p.getPriority() instanceof PropertyName);
        assertEquals("pop", ((PropertyName) p.getPriority()).getPropertyName());
    }

    @Test
    public void testStrokeLinejoinDefault() throws Exception {
        String yaml = "line:\n" + "    stroke-color: \"#ff0000\"\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));
        assertThat(p, hasProperty("stroke", hasProperty("lineJoin", literal("miter"))));
    }

    @Test
    public void testStrokeLinejoinBevel() throws Exception {
        String yaml = "line:\n" + "    stroke-linejoin: bevel\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));
        assertThat(p, hasProperty("stroke", hasProperty("lineJoin", literal("bevel"))));
    }

    @Test
    public void testStrokeLinejoinMitre() throws Exception {
        String yaml = "line:\n" + "    stroke-linejoin: mitre\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));
        assertThat(p, hasProperty("stroke", hasProperty("lineJoin", literal("mitre"))));
    }

    @Test
    public void testSuite54() throws IOException {
        try (InputStream input = YsldTests.ysld("poly", "suite-54.ysld")) {
            StyledLayerDescriptor sld = Ysld.parse(input);
            Style style = SLD.styles(sld)[0];
            TextSymbolizer text = SLD.textSymbolizer(style);
            LabelPlacement placement = text.getLabelPlacement();

            assertNotNull(placement);
        }
    }

    @Test
    public void testMultiplyCompositeOnFeatureTypeStyle() throws Exception {
        String yaml = "feature-styles:\n" + "- name: name\n" + "  x-composite: multiply\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        FeatureTypeStyle fts = SLD.featureTypeStyles(sld)[0];

        assertThat(fts, hasProperty("options", hasEntry("composite", "multiply")));
    }

    @Test
    public void testMultiplyCompositeOnSymbolizer() throws Exception {

        String yaml = "line:\n" + "  x-composite: multiply\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("options", hasEntry("composite", "multiply")));
    }

    @Test
    public void testMultiplyCompositeBaseOnFeatureTypeStyle() throws Exception {
        String yaml = "feature-styles:\n" + "- name: name\n" + "  x-composite-base: true\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        FeatureTypeStyle fts = SLD.featureTypeStyles(sld)[0];

        assertThat(fts, hasProperty("options", hasEntry("composite-base", "true")));
    }

    @Test
    public void testMultiplyCompositeBaseOnSymbolizer() throws Exception {

        String yaml = "line:\n" + "  x-composite-base: true\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("options", hasEntry("composite-base", "true")));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testStrokeGraphic() throws Exception {

        String yaml =
                "line:\n"
                        + "  stroke-graphic:\n"
                        + "    symbols:\n"
                        + "    - mark:\n"
                        + "        shape: circle\n"
                        + "        fill-color: '#995555'\n"
                        + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("stroke", hasProperty("graphicStroke")));
        Graphic g = p.getStroke().getGraphicStroke();
        List<GraphicalSymbol> symbols = g.graphicalSymbols();
        ((Mark) symbols.get(0)).getFill().getColor();
        assertThat(
                symbols,
                (Matcher)
                        hasItems(
                                allOf(
                                        instanceOf(Mark.class),
                                        hasProperty("wellKnownName", literal("circle")),
                                        hasProperty(
                                                "fill",
                                                hasProperty(
                                                        "color", literal(isColor("995555")))))));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testStrokeGraphicFill() throws Exception {

        String yaml =
                "line:\n"
                        + "  stroke-graphic-fill:\n"
                        + "    symbols:\n"
                        + "    - mark:\n"
                        + "        shape: circle\n"
                        + "        fill-color: '#995555'\n"
                        + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("stroke", hasProperty("graphicFill")));
        Graphic g = p.getStroke().getGraphicFill();
        List<GraphicalSymbol> symbols = g.graphicalSymbols();
        ((Mark) symbols.get(0)).getFill().getColor();
        assertThat(
                symbols,
                (Matcher)
                        hasItems(
                                allOf(
                                        instanceOf(Mark.class),
                                        hasProperty("wellKnownName", literal("circle")),
                                        hasProperty(
                                                "fill",
                                                hasProperty(
                                                        "color", literal(isColor("995555")))))));
    }

    @Test
    public void testUomMetre() throws Exception {

        String yaml = "line:\n" + "  uom: metre\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("unitOfMeasure", sameInstance(UomOgcMapping.METRE.getUnit())));
    }

    @Test
    public void testUomFoot() throws Exception {

        String yaml = "line:\n" + "  uom: foot\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("unitOfMeasure", sameInstance(UomOgcMapping.FOOT.getUnit())));
    }

    @Test
    public void testUomPixel() throws Exception {

        String yaml = "line:\n" + "  uom: pixel\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("unitOfMeasure", sameInstance(UomOgcMapping.PIXEL.getUnit())));
    }

    @Test
    public void testUomSymbol() throws Exception {

        String yaml = "line:\n" + "  uom: m\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("unitOfMeasure", sameInstance(UomOgcMapping.METRE.getUnit())));
    }

    @Test
    public void testUomSEURL() throws Exception {

        String yaml = "line:\n" + "  uom: " + UomOgcMapping.FOOT.getSEString() + "\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("unitOfMeasure", sameInstance(UomOgcMapping.FOOT.getUnit())));
    }

    @Test
    public void testUomAmerican() throws Exception {

        String yaml = "line:\n" + "  uom: meter\n" + "";

        StyledLayerDescriptor sld = Ysld.parse(yaml);

        LineSymbolizer p = (LineSymbolizer) SLD.lineSymbolizer(SLD.defaultStyle(sld));

        assertThat(p, hasProperty("unitOfMeasure", sameInstance(UomOgcMapping.METRE.getUnit())));
    }

    @Test
    public void testDeserializationAttempt() throws Exception {
        try {
            String yaml = "!!java.util.Date\n" + "date: 25\n" + "month: 12\n" + "year: 2016";
            Ysld.parse(yaml);
            fail("Expected parsing to fail");
        } catch (ConstructorException e) {
            assertThat(e.getMessage(), containsString("could not determine a constructor"));
        }
    }
}
