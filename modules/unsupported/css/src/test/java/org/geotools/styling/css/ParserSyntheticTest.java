/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.css.selector.Accept;
import org.geotools.styling.css.selector.And;
import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Id;
import org.geotools.styling.css.selector.Or;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.TypeName;
import org.junit.Test;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

public class ParserSyntheticTest extends CssBaseTest {

    @Test
    public void simpleLine() throws IOException {
        String css = "* { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertTrue(r.getSelector() instanceof Accept);
        assertNull(r.getComment());
        assertEquals(1, r.getProperties().size());
        assertProperty(r, 0, "stroke", new Value.Literal("#000000"));
    }

    @Test
    public void simpleLineNamedColor() throws IOException {
        String css = "* { stroke: black; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertTrue(r.getSelector() instanceof Accept);
        assertNull(r.getComment());
        assertEquals(1, r.getProperties().size());
        assertProperty(r, 0, "stroke", new Value.Literal("#000000"));
    }

    @Test
    public void simpleLineNamedColorCaseInsensitive() throws IOException {
        String css = "* { stroke: BlAcK; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertTrue(r.getSelector() instanceof Accept);
        assertNull(r.getComment());
        assertEquals(1, r.getProperties().size());
        assertProperty(r, 0, "stroke", new Value.Literal("#000000"));
    }

    @Test
    public void lineWidth() throws IOException {
        String css = "* { stroke: #000000; stroke-width: 2;}";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
        assertEquals(2, r.getProperties().get(PseudoClass.ROOT).size());
        assertProperty(r, 0, "stroke", new Value.Literal("#000000"));
        assertProperty(r, 1, "stroke-width", new Value.Literal("2"));

        // printResults(css, result);
    }

    @Test
    public void lineWidthPx() throws IOException {
        String css = "* { stroke: #000000; stroke-width: 2px;}";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
        assertEquals(2, r.getProperties().get(PseudoClass.ROOT).size());
        assertProperty(r, 0, "stroke", new Value.Literal("#000000"));
        assertProperty(r, 1, "stroke-width", new Value.Literal("2px"));

        // printResults(css, result);
    }

    @Test
    public void lineColorAttribute() throws IOException, CQLException {
        assertLineColorExpression("* { stroke : [myColorAttribute]; }", "myColorAttribute");
    }

    @Test
    public void lineColorDoubleQuotedAttribute() throws IOException, CQLException {
        assertLineColorExpression("* { stroke : [strSubstring(\"quotedAttribute\", 1, 5)]; }",
                "strSubstring(\"quotedAttribute\", 1, 5)");
    }

    @Test
    public void lineColorStringInCQL() throws IOException, CQLException {
        assertLineColorExpression("* { stroke : [strSubstring('AB#00ffaaCDE', 2, 8)]; }",
                "strSubstring('AB#00ffaaCDE', 2, 8)");
    }

    private void assertLineColorExpression(String css, String expectedExpression)
            throws CQLException {
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);
        // printResults(css, result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
        assertEquals(1, r.getProperties().size());
        assertProperty(r, 0, "stroke", new Value.Expression(ECQL.toExpression(expectedExpression)));
    }

    @Test
    public void staticStringDoubleQuote() throws IOException {
        String css = "* { label : \"test\"; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
        assertEquals(1, r.getProperties().size());
        assertProperty(r, 0, "label", new Value.Literal("test"));

        // printResults(css, result);
    }

    @Test
    public void staticLabelSingleQuote() throws IOException {
        String css = "* { label : 'test'; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
        assertEquals(1, r.getProperties().size());
        assertProperty(r, 0, "label", new Value.Literal("test"));

        // printResults(css, result);
    }

    @Test
    public void staticLabelFontFamily() throws IOException {
        String css = "* { label : 'test'; font-family: Arial; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
        assertEquals(2, r.getProperties().get(PseudoClass.ROOT).size());
        assertProperty(r, 0, "label", new Value.Literal("test"));
        assertProperty(r, 1, "font-family", new Value.Literal("Arial"));

        // printResults(css, result);
    }

    @Test
    public void testFunction() throws IOException {
        String css = "* { mark: symbol(circle); }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
        assertEquals(1, r.getProperties().size());
        assertProperty(r, 0, "mark",
                new Value.Function("symbol",
                        Collections.singletonList((Value) new Value.Literal("circle"))));
    }

    @Test
    public void testMultiValue() throws IOException {
        String css = "* { label-anchor: 50% 50%; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
        assertEquals(1, r.getProperties().size());
        assertProperty(r, 0, "label-anchor", new Value.MultiValue(new Value.Literal("50%"),
                new Value.Literal("50%")));
    }

    @Test
    public void ogcSelector() throws IOException, CQLException {
        String css = "[myAttribute > 10] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Data);
        Data of = (Data) r.getSelector();
        assertEquals(ECQL.toFilter("myAttribute > 10"), of.filter);
    }

    @Test
    public void minScaleSelector() throws IOException, CQLException {
        String css = "[@scale > 1000] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof ScaleRange);
        ScaleRange sr = (ScaleRange) r.getSelector();
        assertEquals(1000, sr.range.getMinValue(), 0d);
        assertEquals(Double.POSITIVE_INFINITY, sr.range.getMaxValue(), 0d);
    }

    @Test
    public void maxScaleSelector() throws IOException, CQLException {
        String css = "[@scale < 1000] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof ScaleRange);
        ScaleRange sr = (ScaleRange) r.getSelector();
        assertEquals(0, sr.range.getMinValue(), 0d);
        assertEquals(1000, sr.range.getMaxValue(), 0d);
    }

    @Test
    public void qualifiedTypeNameSelector() throws IOException, CQLException {
        String css = "topp:states { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof TypeName);
        TypeName s = (TypeName) r.getSelector();
        assertEquals("topp:states", s.name);
    }

    @Test
    public void simpleTypeNameSelector() throws IOException, CQLException {
        String css = "states { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof TypeName);
        TypeName s = (TypeName) r.getSelector();
        assertEquals("states", s.name);
    }

    @Test
    public void idSelector() throws IOException, CQLException {
        String css = "#states.2 { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Id);
        Id s = (Id) r.getSelector();
        assertEquals(1, s.ids.size());
        assertEquals("states.2", s.ids.iterator().next());
    }

    @Test
    public void pseudoClassSelector() throws IOException, CQLException {
        String css = ":mark { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof PseudoClass);
        PseudoClass s = (PseudoClass) r.getSelector();
        assertEquals("mark", s.getClassName());
        assertEquals(-1, s.getNumber());
    }

    @Test
    public void numberedPseudoClassSelector() throws IOException, CQLException {
        String css = ":nth-mark(2) { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof PseudoClass);
        PseudoClass s = (PseudoClass) r.getSelector();
        assertEquals("mark", s.getClassName());
        assertEquals(2, s.getNumber());
    }

    @Test
    public void andDataScale() throws IOException, CQLException {
        String css = "[att < 15] [@scale > 3000] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof And);
        And s = (And) r.getSelector();
        assertEquals(new Data(ECQL.toFilter("att < 15")), s.children.get(0));
        assertEquals(new ScaleRange(3000, true, Double.POSITIVE_INFINITY, true), s.children.get(1));
    }

    @Test
    public void andTypeScale() throws IOException, CQLException {
        String css = "topp:states [@scale > 3000] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof And);
        And s = (And) r.getSelector();
        assertEquals(new TypeName("topp:states"), s.children.get(0));
        assertEquals(new ScaleRange(3000, true, Double.POSITIVE_INFINITY, true), s.children.get(1));
    }

    @Test
    public void orDataScale() throws IOException, CQLException {
        String css = "[att < 15], [@scale > 3000] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Or);
        Or s = (Or) r.getSelector();
        assertEquals(new Data(ECQL.toFilter("att < 15")), s.children.get(0));
        assertEquals(new ScaleRange(3000, true, Double.POSITIVE_INFINITY, true), s.children.get(1));
    }

    @Test
    public void orTypeScale() throws IOException, CQLException {
        String css = "topp:states, [@scale > 3000] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Or);
        Or s = (Or) r.getSelector();
        assertEquals(new TypeName("topp:states"), s.children.get(0));
        assertEquals(new ScaleRange(3000, true, Double.POSITIVE_INFINITY, true), s.children.get(1));
    }

    @Test
    public void orIdentifiers() throws IOException, CQLException {
        String css = "#states.2, #states.3 { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Or);
        Or s = (Or) r.getSelector();
        assertEquals(new Id("states.2"), s.children.get(0));
        assertEquals(new Id("states.3"), s.children.get(1));
    }

    @Test
    public void nestedOr() throws IOException, CQLException {
        String css = "#states.2, #states.3, [myAtt > 10] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertTrue(r.getSelector() instanceof Or);
        assertNull(r.getComment());
        Or s = (Or) r.getSelector();
        assertEquals(new Id("states.2"), s.children.get(0));
        assertTrue(s.children.get(1) instanceof Or);
        Or or = (Or) s.children.get(1);
        assertEquals(new Id("states.3"), or.children.get(0));
        assertEquals(new Data(ECQL.toFilter("myAtt > 10")), or.children.get(1));

    }

    @Test
    public void nestedAnd() throws IOException, CQLException {
        String css = "#states.2 [@scale > 1000] [myAtt > 10] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertTrue(r.getSelector() instanceof And);
        assertNull(r.getComment());
        And s = (And) r.getSelector();
        assertEquals(new Id("states.2"), s.children.get(0));
        assertEquals(new ScaleRange(1000, true, Double.POSITIVE_INFINITY, true), s.children.get(1));
        assertEquals(new Data(ECQL.toFilter("myAtt > 10")), s.children.get(2));

    }

    @Test
    public void nestedOrAnd() throws IOException, CQLException {
        String css = "#states.2 [@scale > 1000], [myAtt > 10] { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertNull(r.getComment());
        assertTrue(r.getSelector() instanceof Or);
        Or s = (Or) r.getSelector();
        assertEquals(new Data(ECQL.toFilter("myAtt > 10")), s.children.get(1));
        assertTrue(s.children.get(0) instanceof And);
        And and = (And) s.children.get(0);
        assertEquals(new Id("states.2"), and.children.get(0));
        assertEquals(new ScaleRange(1000, true, Double.POSITIVE_INFINITY, true),
                and.children.get(1));

    }

    @Test
    public void testComment() {
        String css = "/* This is a comment */ * { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertEquals("This is a comment", r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
    }

    @Test
    public void testCommentMultiline() {
        String commentInternal = "* @title The title\n" + //
                "  * @abstract The abstract";
        String comment = "/*\n" + //
                commentInternal + //
                "\n*/";
        String css = comment + " * { stroke: #000000; }";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertEquals(commentInternal, r.getComment());
        assertTrue(r.getSelector() instanceof Accept);
    }

    @Test
    public void testCommentAmongProperties1() {
        String css = "* { stroke: #000000; /* This is a comment */}";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertTrue(r.getSelector() instanceof Accept);
        assertProperty(r, 0, "stroke", new Value.Literal("#000000"));
    }

    @Test
    public void testCommentAmongProperties2() {
        String css = "* {  /* This is a comment */ stroke: #000000;}";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(1, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertTrue(r.getSelector() instanceof Accept);
    }

    @Test
    public void testMultiRule() {
        String css = "* {stroke: #000000;} states {fill:red;} ";
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        assertEquals(2, ss.getRules().size());
        CssRule r = ss.getRules().get(0);
        assertTrue(r.getSelector() instanceof Accept);
        assertProperty(r, 0, "stroke", new Value.Literal("#000000"));
        r = ss.getRules().get(1);
        assertEquals(r.getSelector(), new TypeName("states"));
        assertProperty(r, 0, "fill", new Value.Literal("#ff0000"));
    }

}
