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

import org.apache.commons.io.IOUtils;
import org.geotools.styling.css.selector.PseudoClass;
import org.opengis.style.Style;
import org.parboiled.Parboiled;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.ToStringFormatter;
import org.parboiled.trees.GraphNode;
import org.parboiled.trees.GraphUtils;

public class CssBaseTest {

    protected CssParser parser = Parboiled.createParser(CssParser.class);

    protected void assertProperty(CssRule r, int propertyIdx, String propertyName,
            Value expectedValue) {
        Property p = r.getProperties().get(PseudoClass.ROOT).get(propertyIdx);
        assertEquals(propertyName, p.getName());
        assertEquals(1, p.getValues().size());
        Value value = p.getValues().get(0);
        assertEquals(expectedValue, value);
    }

    protected void printResults(String input, ParsingResult<?> result) {
        Object value = result.parseTreeRoot.getValue();
        if (value != null) {
            String str = value.toString();
            int ix = str.indexOf('|');
            if (ix >= 0)
                str = str.substring(ix + 2); // extract value part of AST node toString()
            System.out.println(input + " = " + str + '\n');
        }
        if (value instanceof GraphNode) {
            System.out.println("\nAbstract Syntax Tree:\n"
                    + GraphUtils.printTree((GraphNode) value, new ToStringFormatter(null)) + '\n');
        } else {
            System.out.println("\nParse Tree:\n" + ParseTreeUtils.printNodeTree(result) + '\n');
        }
    }

    protected String readResource(String resource) throws IOException {
        return IOUtils.toString(ParserSyntheticTest.class.getResourceAsStream(resource));
    }

    protected Style translate(String css) {
        Stylesheet ss = parse(css);
        CssTranslator translator = new CssTranslator();
        return translator.translate(ss);
    }

    protected Stylesheet parse(String css) {
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);
        assertNoErrors(result);

        Stylesheet ss = result.parseTreeRoot.getValue();
        return ss;
    }

    protected void assertNoErrors(ParsingResult<?> result) {
        if (result.hasErrors()) {
            System.out.println(ErrorUtils.printParseErrors(result));
        }
        assertFalse("\n" + ErrorUtils.printParseErrors(result), result.hasErrors());
    }
}
