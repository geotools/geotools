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
package org.geotools.ysld.validate;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.geotools.ysld.UomMapper;
import org.geotools.ysld.Ysld;
import org.geotools.ysld.parse.ZoomContext;
import org.geotools.ysld.parse.ZoomContextFinder;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

public class YsldValidateTest {

    @Test
    public void testMalformed() throws Exception {
        String ysld = "foo:\n" + "  foo: bar\n" + "   foo: bar\n";
        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        assertEquals(2, e.getProblemMark().getLine());
    }

    @Test
    public void testColor() throws Exception {
        String ysld = "fill-color: 21xyz";

        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        assertEquals(0, e.getProblemMark().getLine());
        assertEquals(12, e.getProblemMark().getColumn());
    }

    @Test
    public void testFilter() throws Exception {
        String ysld = "filter: foo";

        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(1, errors.size());

        MarkedYAMLException e = errors.get(0);
        // Note this is the start of the expression, a better version would find the error within
        // the CQL
        assertThat(e.getProblemMark(), problemAt(1, 9));
    }

    @Test
    public void testFilterOK() throws Exception {
        String ysld = "filter: scalerank < 4";

        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(0, errors.size());
    }

    @Test
    public void testFilterOWithBracesK() throws Exception {
        String ysld = "filter: ${scalerank < 4}";

        List<MarkedYAMLException> errors = validate(ysld);
        assertEquals(0, errors.size());
    }

    Matcher<Mark> problemAt(int line, int column) {
        return Matchers.describedAs(
                "Problem at Line %0 Column %1",
                allOf(
                        Matchers.<Mark>hasProperty("line", is(line - 1)),
                        Matchers.<Mark>hasProperty("column", is(column - 1))),
                line,
                column);
    }

    @SuppressWarnings("unchecked")
    Matcher<Mark> problemOn(int line) {
        return Matchers.describedAs(
                "Problem somewhere on Line %0",
                allOf(Matchers.<Mark>hasProperty("line", is(line - 1))), line);
    }

    static final String[] EXPRESSION_KEYS = {
        "offset",
        "shape",
        "gamma",
        "stroke-width",
        "stroke-opacity",
        "stroke-linejoin",
        "stroke-linecap",
        "stroke-dashoffset",
        "geometry",
        "label",
        "font-family",
        "font-size",
        "font-style",
        "font-weight",
        "size",
        "rotation",
        "gap",
        "initial-gap",
        "radius"
    };

    @Test
    public void testExpression() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("rules:\n" + "- symbolizers:\n" + "  - line:\n");
        for (String key : EXPRESSION_KEYS) {
            builder.append("      ")
                    .append(key)
                    .append(": ")
                    .append("${round([len] 1000)}")
                    .append("\n"); // Invalid
            builder.append("      ")
                    .append(key)
                    .append(": ")
                    .append("${round([len] / 1000)}")
                    .append("\n"); // Valid
        }
        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(EXPRESSION_KEYS.length));

        final int firstErrorLine = 4;

        for (int i = 0; i < EXPRESSION_KEYS.length; i++) {
            assertThat(errors.get(i).getProblemMark(), problemOn(i * 2 + firstErrorLine));
        }
    }

    @Test
    public void testWellKnownZoomContext() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("grid:\n" + "  name: WGS84\n");

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(0));
    }

    @Test
    public void testNotWellKnownZoomContext() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("grid:\n" + "  name: SIGMA:957\n");

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(1));

        assertThat(errors.get(0).getProblemMark(), problemOn(2));
    }

    @Test
    public void testExtendedZoomContext() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("grid:\n" + "  name: SIGMA:957\n");

        ZoomContextFinder finder = createMock("finder", ZoomContextFinder.class);
        ZoomContext zctxt = createMock("zctxt", ZoomContext.class);

        expect(finder.get("SIGMA:957")).andStubReturn(zctxt);

        replay(finder, zctxt);

        List<MarkedYAMLException> errors =
                validate(builder.toString(), Collections.singletonList(finder));
        assertThat(errors.size(), is(0));

        verify(finder, zctxt);
    }

    @Test
    public void testZoomContextEPSG4326IsBad() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("grid:\n" + "  name: EPSG:4326\n");

        ZoomContextFinder finder = createMock("finder", ZoomContextFinder.class);
        ZoomContext zctxt = createMock("zctxt", ZoomContext.class);

        expect(finder.get("EPSG:4326")).andStubReturn(zctxt);

        replay(finder, zctxt);

        List<MarkedYAMLException> errors =
                validate(builder.toString(), Collections.singletonList(finder));
        assertThat(errors.size(), is(1));

        assertThat(errors.get(0).getProblemMark(), problemOn(2));

        verify(finder, zctxt);
    }

    List<MarkedYAMLException> dump(List<MarkedYAMLException> errors) {
        for (MarkedYAMLException e : errors) {
            // System.out.println(e.toString());
        }
        return errors;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomWithDefaultGridValidateOrder() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(
                "rules:\n"
                        + // 1
                        "- zoom: [0,1]\n"
                        + // 2 OK
                        "- zoom: [1,2]\n"
                        + // 3 OK
                        "- zoom: [-1,2]\n"
                        + // 4 OK
                        "- zoom: [1, 0]\n"
                        + // 5 Bad
                        "- zoom: [100, 10]\n"
                        + // 6 Bad
                        "- zoom: [-2, -10]\n"
                        + // 7 Bad
                        "- zoom: [2, -2]\n"); // 8 Bad

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(5)),
                        hasProperty("problemMark", problemOn(6)),
                        hasProperty("problemMark", problemOn(7)),
                        hasProperty("problemMark", problemOn(8))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomWithDefaultGridTupleSize() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(
                "rules:\n"
                        + // 1
                        "- zoom: [0,0]\n"
                        + // 2 OK
                        "- zoom: []\n"
                        + // 3 Bad
                        "- zoom: [0]\n"
                        + // 4 Bad
                        "- zoom: [0,0,0]\n"); // 5 Bad

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(3)),
                        hasProperty("problemMark", problemOn(4)),
                        hasProperty("problemMark", problemOn(5))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomWithDefaultGridValidateRange() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(
                "rules:\n"
                        + // 1
                        "- zoom: [0,max]\n"
                        + // 2 OK
                        "- zoom: [min,0]\n"
                        + // 3 OK
                        "- zoom: [19,max]\n"
                        + // 4 OK
                        "- zoom: [min,19]\n"
                        + // 5 OK
                        "- zoom: [-1, max]\n"
                        + // 6 OK, RatioZoomContext allows negative
                        "- zoom: [min, -1]\n"
                        + // 7 OK, RatioZoomContext allows negative
                        "- zoom: [min, foo]\n"
                        + // 8 Bad
                        "- zoom: [foo, max]\n"
                        + // 9 Bad
                        "- zoom: [0.5, max]\n"
                        + // 10 Bad
                        "- zoom: [min, 0.5]\n"); // 11 Bad

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(8)),
                        hasProperty("problemMark", problemOn(9)),
                        hasProperty("problemMark", problemOn(10)),
                        hasProperty("problemMark", problemOn(11))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testScaleTupleSize() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(
                "rules:\n"
                        + // 1
                        "- scale: [0.0,0.0]\n"
                        + // 2 OK
                        "- scale: []\n"
                        + // 3 Bad
                        "- scale: [0.0]\n"
                        + // 4 Bad
                        "- scale: [0.0,0.0,0.0]\n"); // 5 Bad

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(3)),
                        hasProperty("problemMark", problemOn(4)),
                        hasProperty("problemMark", problemOn(5))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testScaleValidateRange() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(
                "rules:\n"
                        + // 1
                        "- scale: [100000,max]\n"
                        + // 2 OK
                        "- scale: [min,100000]\n"
                        + // 3 OK
                        "- scale: [10000000,max]\n"
                        + // 4 OK
                        "- scale: [min,10000000]\n"
                        + // 5 OK
                        "- scale: [-1, max]\n"
                        + // 6 Bad
                        "- scale: [min, -1]\n"
                        + // 7 Bad
                        "- scale: [min, foo]\n"
                        + // 8 Bad
                        "- scale: [foo, max]\n"
                        + // 9 Bad
                        "- scale: [100000.1, max]\n"
                        + // 10 OK
                        "- scale: [min, 100000.1]\n"); // 11 OK

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(6)),
                        hasProperty("problemMark", problemOn(7)),
                        hasProperty("problemMark", problemOn(8)),
                        hasProperty("problemMark", problemOn(9))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testZoomWithExtendedGridValidateRange() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(
                "grid:\n"
                        + // 1
                        "  name: SIGMA:957\n"
                        + // 2
                        "rules:\n"
                        + // 3
                        "- zoom: [0,max]\n"
                        + // 4 OK
                        "- zoom: [min,0]\n"
                        + // 5 OK
                        "- zoom: [2,max]\n"
                        + // 6 OK
                        "- zoom: [min,2]\n"
                        + // 7 OK
                        "- zoom: [5,max]\n"
                        + // 8 OK
                        "- zoom: [min,5]\n"
                        + // 9 OK
                        "- zoom: [6, max]\n"
                        + // 10 Bad, Out of Range
                        "- zoom: [min, 6]\n"
                        + // 11 Bad, Out of Range
                        "- zoom: [-1, max]\n"
                        + // 12 Bad, Out of Range
                        "- zoom: [min, -1]\n"
                        + // 13 Bad, Out of Range
                        "- zoom: [min, foo]\n"
                        + // 14 Bad
                        "- zoom: [foo, max]\n"
                        + // 15 Bad
                        "- zoom: [0.5, max]\n"
                        + // 16 Bad
                        "- zoom: [min, 0.5]\n"); // 17 Bad

        ZoomContextFinder finder = createMock("finder", ZoomContextFinder.class);
        ZoomContext zctxt = createMock("zctxt", ZoomContext.class);

        expect(finder.get("SIGMA:957")).andStubReturn(zctxt);

        expect(zctxt.isInRange(0)).andStubReturn(true);
        expect(zctxt.isInRange(2)).andStubReturn(true);
        expect(zctxt.isInRange(5)).andStubReturn(true);
        expect(zctxt.isInRange(-1)).andStubReturn(false);
        expect(zctxt.isInRange(6)).andStubReturn(false);

        replay(finder, zctxt);

        List<MarkedYAMLException> errors =
                validate(builder.toString(), Collections.singletonList(finder));
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(10)),
                        hasProperty("problemMark", problemOn(11)),
                        hasProperty("problemMark", problemOn(12)),
                        hasProperty("problemMark", problemOn(13)),
                        hasProperty("problemMark", problemOn(14)),
                        hasProperty("problemMark", problemOn(15)),
                        hasProperty("problemMark", problemOn(16)),
                        hasProperty("problemMark", problemOn(17))));

        verify(finder, zctxt);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAnchorTupleSize() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("rules:\n");
        builder.append(
                "- point: \n" + "    anchor: [0.75, 0.25]\n" // 3 OK
                );
        builder.append(
                "- point: \n" + "    anchor: []\n" // 5 Bad
                );
        builder.append(
                "- point: \n" + "    anchor: [0.0]\n" // 7 Bad
                );
        builder.append(
                "- point: \n" + "    anchor: [0.0, 0.0, 0.0]\n" // 9 Bad
                );

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(5)),
                        hasProperty("problemMark", problemOn(7)),
                        hasProperty("problemMark", problemOn(9))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAnchorValidateValues() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("rules:\n");
        builder.append(
                "- point: \n" + "    anchor: [0.75, 0.25]\n" // 3 OK
                );
        builder.append(
                "- point: \n" + "    anchor: [-1000, -0.0001]\n" // 5 OK
                );
        builder.append(
                "- point: \n" + "    anchor: [\"${round([len]/1000)}\", 0.0]\n" // 7 OK
                );
        builder.append(
                "- point: \n" + "    anchor: [0.0, \"${round([len]/1000)}\"]\n" // 9 OK
                );
        builder.append(
                "- point: \n" + "    anchor: [\"${round([len] 1000)}\", 0.0]\n" // 11 Bad, invalid
                // expression
                );
        builder.append(
                "- point: \n" + "    anchor: [0.0, \"${round([len] 1000)}\"]\n" // 13 Bad invalid
                // expression
                );

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(11)),
                        hasProperty("problemMark", problemOn(13))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDisplacementTupleSize() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("rules:\n");
        builder.append(
                "- point: \n" + "    displacement: [0, 0]\n" // 3 OK
                );
        builder.append(
                "- point: \n" + "    displacement: []\n" // 5 Bad
                );
        builder.append(
                "- point: \n" + "    displacement: [0]\n" // 7 Bad
                );
        builder.append(
                "- point: \n" + "    displacement: [0, 0, 0]\n" // 9 Bad
                );

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(5)),
                        hasProperty("problemMark", problemOn(7)),
                        hasProperty("problemMark", problemOn(9))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDisplacementValidateValues() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("rules:\n");
        builder.append(
                "- point: \n" + "    displacement: [1, 1]\n" // 3 OK
                );
        builder.append(
                "- point: \n" + "    displacement: [-1, -2]\n" // 5 OK
                );
        builder.append(
                "- point: \n" + "    displacement: [\"${round([len]/1000)}\", 0]\n" // 7 OK
                );
        builder.append(
                "- point: \n" + "    displacement: [0, \"${round([len]/1000)}\"]\n" // 9 OK
                );
        builder.append(
                "- point: \n"
                        + "    displacement: [\"${round([len] 1000)}\", 0]\n" // 11 Bad, invalid
                // expression
                );
        builder.append(
                "- point: \n"
                        + "    displacement: [0, \"${round([len] 1000)}\"]\n" // 13 Bad invalid
                // expression
                );

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(11)),
                        hasProperty("problemMark", problemOn(13))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testColourMapEntries() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("raster: \n" + "  color-map:\n" + "    type: values\n" + "    entries:\n");
        builder.append("    - [\"#ff0000\", 1.0, 0, \"start\"]\n"); // 5 OK
        builder.append("    - [red, 1, 0, \"blah\"]\n"); // 6 OK
        builder.append("    - [\"rgb(0,0,0)\", null, 0, \"start\"]\n"); // 7 OK
        builder.append("    - [\"#ff0000\", '', 0, null]\n"); // 8 OK
        builder.append(
                "    - [\"${[something]}\", \"${[something]}\", \"${[something]}\", null]\n"); // 9
        // OK

        builder.append(
                "    - [\"#ff0000\", \"${round([len] 1000)}\", 0, \"start\"]\n"); // 10 Bad opacity
        builder.append("    - [\"#ff0000\", 1.0, \"${round([len] 1000)}\", \"start\"]\n"); // 11 Bad
        // quantity

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(
                errors,
                contains(
                        hasProperty("problemMark", problemOn(10)),
                        hasProperty("problemMark", problemOn(11))));
    }

    @Test
    public void testWellKnownZoomContextWithOtherError() throws Exception {
        // Making sure that doing the grid validation doesn't screw up other validation later
        StringBuilder builder = new StringBuilder();
        builder.append("grid:\n");
        builder.append("  name: WebMercator\n");
        builder.append("filter: foo\n");

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors.size(), is(1));
        assertThat(errors.get(0).getProblemMark(), problemOn(3));
    }

    List<MarkedYAMLException> validate(String ysld) throws IOException {
        // return dump(Ysld.validate(ysld));
        return this.validate(ysld, Collections.<ZoomContextFinder>emptyList());
    }

    List<MarkedYAMLException> validate(String ysld, List<ZoomContextFinder> ctxts)
            throws IOException {
        // return dump(Ysld.validate(ysld));
        return Ysld.validate(ysld, ctxts, new UomMapper());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRenderingTransform() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("transform:")
                .append("\n")
                .append("  name: ras:Contour")
                .append("\n")
                .append("  input: data")
                .append("\n")
                .append("  params:")
                .append("\n")
                .append("    levels:")
                .append("\n")
                .append("    - -10")
                .append("\n")
                .append("    - -5")
                .append("\n")
                .append("    - 0")
                .append("\n")
                .append("    - 5")
                .append("\n")
                .append("    - 10")
                .append("\n")
                .append("    simplify: true")
                .append("\n")
                .append("");

        List<MarkedYAMLException> errors = validate(builder.toString(), Collections.EMPTY_LIST);
        assertThat(errors, empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNestedRenderingTransform() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("transform:")
                .append("\n")
                .append("  name: ras:Contour")
                .append("\n")
                .append("  params:")
                .append("\n")
                .append("    data:")
                .append("\n")
                .append("      name: vec:BarnesSurface")
                .append("\n")
                .append("      input: data")
                .append("\n")
                .append("      params:")
                .append("\n")
                .append("        valueAttr: natlscale")
                .append("\n")
                .append("        dataLimit: 500")
                .append("\n")
                .append("        scale: 15.0")
                .append("\n")
                .append("        convergence: 0.2")
                .append("\n")
                .append("        passes: 3")
                .append("\n")
                .append("        minObservations: 2")
                .append("\n")
                .append("        maxObservationDistance: 15")
                .append("\n")
                .append("        pixelsPerCell: 8")
                .append("\n")
                .append("        queryBuffer: 40")
                .append("\n")
                .append("    levels:")
                .append("\n")
                .append("    - -10")
                .append("\n")
                .append("    - -5")
                .append("\n")
                .append("    - 0")
                .append("\n")
                .append("    - 5")
                .append("\n")
                .append("    - 10")
                .append("\n")
                .append("    simplify: true")
                .append("\n")
                .append("");

        List<MarkedYAMLException> errors = validate(builder.toString(), Collections.EMPTY_LIST);
        assertThat(errors, empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testErrorAfterNestedRenderingTransform() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("transform:")
                .append("\n")
                .append("  name: ras:Contour")
                .append("\n")
                .append("  params:")
                .append("\n")
                .append("    data:")
                .append("\n")
                .append("      name: vec:BarnesSurface")
                .append("\n")
                .append("      input: data")
                .append("\n")
                .append("      params:")
                .append("\n")
                .append("        valueAttr: natlscale")
                .append("\n")
                .append("        dataLimit: 500")
                .append("\n")
                .append("        scale: 15.0")
                .append("\n")
                .append("        convergence: 0.2")
                .append("\n")
                .append("        passes: 3")
                .append("\n")
                .append("        minObservations: 2")
                .append("\n")
                .append("        maxObservationDistance: 15")
                .append("\n")
                .append("        pixelsPerCell: 8")
                .append("\n")
                .append("        queryBuffer: 40")
                .append("\n")
                .append("    levels:")
                .append("\n")
                .append("    - -10")
                .append("\n")
                .append("    - -5")
                .append("\n")
                .append("    - 0")
                .append("\n")
                .append("    - 5")
                .append("\n")
                .append("    - 10")
                .append("\n")
                .append("    simplify: true")
                .append("\n")
                .append("rules:")
                .append("\n")
                .append("- point:")
                .append("\n")
                .append("    displacement: []")
                .append("\n") // Empty displacement on line 26
                .append("");

        List<MarkedYAMLException> errors = validate(builder.toString(), Collections.EMPTY_LIST);
        assertThat(errors, contains(hasProperty("problemMark", problemOn(26))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testTupleInVariable() throws Exception {
        String yaml =
                "define: &s [400000,max]\n"
                        + "\n"
                        + "feature-styles:\n"
                        + "- rules:\n"
                        + "  - scale: *s\n"
                        + "    filter: ${x = true}\n";

        List<MarkedYAMLException> errors = validate(yaml, Collections.EMPTY_LIST);
        assertThat(errors, empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testVariableInTuple() throws Exception {
        String yaml =
                "define: &s 400000\n"
                        + "\n"
                        + "feature-styles:\n"
                        + "- rules:\n"
                        + "  - scale: [*s, max]\n"
                        + "    filter: ${x = true}\n";

        List<MarkedYAMLException> errors = validate(yaml, Collections.EMPTY_LIST);
        assertThat(errors, empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testVariableInPermissive() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("define: &p -5\n")
                .append("transform:")
                .append("\n")
                .append("  name: ras:Contour")
                .append("\n")
                .append("  input: data")
                .append("\n")
                .append("  params:")
                .append("\n")
                .append("    levels:")
                .append("\n")
                .append("    - -10")
                .append("\n")
                .append("    - *p")
                .append("\n")
                .append("    - 0")
                .append("\n")
                .append("    - 5")
                .append("\n")
                .append("    - 10")
                .append("\n")
                .append("    simplify: true")
                .append("\n")
                .append("");

        List<MarkedYAMLException> errors = validate(builder.toString(), Collections.EMPTY_LIST);
        assertThat(errors, empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPermissiveInVariable() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("define: &p\n")
                .append("    levels:")
                .append("\n")
                .append("    - -10")
                .append("\n")
                .append("    - -5")
                .append("\n")
                .append("    - 0")
                .append("\n")
                .append("    - 5")
                .append("\n")
                .append("    - 10")
                .append("\n")
                .append("    simplify: true")
                .append("\n")
                .append("transform:")
                .append("\n")
                .append("  name: ras:Contour")
                .append("\n")
                .append("  input: data")
                .append("\n")
                .append("  params: *p")
                .append("\n")
                .append("");

        List<MarkedYAMLException> errors = validate(builder.toString(), Collections.EMPTY_LIST);
        assertThat(errors, empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testColourMapEntryAsVariable() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("define: &e [red, 1, 0, \"blah\"]\n");
        builder.append("raster: \n" + "  color-map:\n" + "    type: values\n" + "    entries:\n");
        builder.append("    - [\"#ff0000\", 1.0, 0, \"start\"]\n");
        builder.append("    - *e\n");
        builder.append("    - [\"rgb(0,0,0)\", null, 0, \"start\"]\n");
        builder.append("  filter: ${foo = true}\n");

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors, empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testColourMapAsVariable() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("define: &c \n");
        builder.append("  - [\"#ff0000\", 1.0, 0, \"start\"]\n");
        builder.append("  - [red, 1, 0, \"blah\"]\n");
        builder.append("  - [\"rgb(0,0,0)\", null, 0, \"start\"]\n");
        builder.append(
                "raster: \n" + "  color-map:\n" + "    type: values\n" + "    entries: *c\n");
        builder.append("  filter: ${foo = true}\n");

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors, empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testColourAsVariable() throws Exception {
        // GEOT-5445 - Try each permutation of variables with fill-color and stroke-color.
        // This tests the known validator issue, plus any potential unknown related issues.
        StringBuilder builder = new StringBuilder();
        builder.append("define: &color '#ff9900'\n");
        builder.append("symbolizers:\n");
        builder.append("- polygon:\n");
        builder.append("    fill-color: *color\n");
        builder.append("    stroke-color: '#000000'\n");

        List<MarkedYAMLException> errors = validate(builder.toString());
        assertThat(errors, empty());

        builder = new StringBuilder();
        builder.append("define: &color '#ff9900'\n");
        builder.append("symbolizers:\n");
        builder.append("- polygon:\n");
        builder.append("    fill-color: *color\n");
        builder.append("    stroke-color: '#000000'\n");

        errors = validate(builder.toString());
        assertThat(errors, empty());

        builder = new StringBuilder();
        builder.append("define: &color '#000000'\n");
        builder.append("symbolizers:\n");
        builder.append("- polygon:\n");
        builder.append("    fill-color: '#ff9900'\n");
        builder.append("    stroke-color: *color\n");

        errors = validate(builder.toString());
        assertThat(errors, empty());

        builder = new StringBuilder();
        builder.append("define: &color '#000000'\n");
        builder.append("symbolizers:\n");
        builder.append("- polygon:\n");
        builder.append("    stroke-color: *color\n");
        builder.append("    fill-color: '#ff9900'\n");

        errors = validate(builder.toString());
        assertThat(errors, empty());
    }
}
