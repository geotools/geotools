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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.geotools.styling.css.selector.PseudoClass;
import org.geotools.util.logging.Logging;
import org.opengis.style.Style;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.ToStringFormatter;
import org.parboiled.trees.GraphNode;
import org.parboiled.trees.GraphUtils;

public class CssBaseTest {

    static final Logger LOGGER = Logging.getLogger(CssBaseTest.class);

    CssParser parser = CssParser.getInstance();

    protected void assertProperty(
            CssRule r,
            PseudoClass pseudoClass,
            int propertyIdx,
            String propertyName,
            Value expectedValue) {
        Property p = r.getProperties().get(pseudoClass).get(propertyIdx);
        assertEquals(propertyName, p.getName());
        assertEquals(1, p.getValues().size());
        Value value = p.getValues().get(0);
        assertEquals(expectedValue, value);
    }

    protected void assertProperty(
            CssRule r, int propertyIdx, String propertyName, Value expectedValue) {
        assertProperty(r, PseudoClass.ROOT, propertyIdx, propertyName, expectedValue);
    }

    protected void printResults(String input, ParsingResult<?> result) {
        Object value = result.parseTreeRoot.getValue();
        if (value != null) {
            String str = value.toString();
            int ix = str.indexOf('|');
            if (ix >= 0) str = str.substring(ix + 2); // extract value part of AST node toString()
            LOGGER.info(input + " = " + str + '\n');
        }
        if (value instanceof GraphNode) {
            LOGGER.info(
                    "\nAbstract Syntax Tree:\n"
                            + GraphUtils.printTree((GraphNode) value, new ToStringFormatter(null))
                            + '\n');
        } else {
            LOGGER.info("\nParse Tree:\n" + ParseTreeUtils.printNodeTree(result) + '\n');
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
        return CssParser.parse(css);
    }

    protected void assertNoErrors(ParsingResult<?> result) {
        if (result.hasErrors()) {
            LOGGER.severe(ErrorUtils.printParseErrors(result));
        }
        assertFalse("\n" + ErrorUtils.printParseErrors(result), result.hasErrors());
    }
}
