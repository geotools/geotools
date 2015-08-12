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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.sld.SLDConfiguration;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.xml.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.opengis.style.Style;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Reads and translates all tests checking for errors in the process
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
@RunWith(Parameterized.class)
public abstract class AbstractIntegrationTest extends CssBaseTest {

    private static final StyleFactory STYLE_FACTORY = CommonFactoryFinder.getStyleFactory();
    File file;

    boolean exclusiveRulesEnabled;

    public AbstractIntegrationTest(String name, File file, Boolean exclusiveRulesEnabled) {
        this.file = file;
        this.exclusiveRulesEnabled = exclusiveRulesEnabled;
    }

    @Test
    public void translateTest() throws Exception {
        String css = FileUtils.readFileToString(file);
        if (!exclusiveRulesEnabled) {
            css = "@mode \"Simple\";\n" + css;
        }

        File sldFile = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName())
                + (exclusiveRulesEnabled ? "" : "-first")
                + ".sld");
        if (!sldFile.exists()) {
            Stylesheet ss = CssParser.parse(css);
            CssTranslator tx = new CssTranslator();
            Style style = tx.translate(ss);
            writeStyle(style, sldFile);
            // throw new IllegalStateException("Could not locate sample sld file " +
            // sldFile.getPath());
        }

        Style actual = cssToSld(css);
        File sldFile2 = new File("./target/css", FilenameUtils.getBaseName(file.getName())
                + ".sld");
        writeStyle(actual, sldFile2);
        String actualSld = FileUtils.readFileToString(sldFile2);

        List validationErrors = validateSLD(actualSld);
        if (!validationErrors.isEmpty()) {
            System.out.println("Validation failed, errors are: ");
            for (Object e : validationErrors) {
                if (e instanceof SAXParseException) {
                    SAXParseException se = (SAXParseException) e;
                    System.out.println("line " + se.getLineNumber() + ": "
                            + se.getLocalizedMessage());
                } else {
                    System.out.println(e);
                }

            }
            System.err.println("Validation failed, the two files are: " + sldFile.getAbsolutePath()
                    + " " + sldFile2.getAbsolutePath());
            fail("Validation failed");
        }

        String expectedSld = FileUtils.readFileToString(sldFile);
        StyledLayerDescriptor expectedSLD = parseToSld(expectedSld);
        StyledLayerDescriptor actualSLD = parseToSld(actualSld);
//        Document expectedDom = XMLUnit.buildControlDocument(expectedSld);
//        Document actualDom = XMLUnit.buildControlDocument(actualSld);
//        Diff diff = new Diff(expectedDom, actualDom);
//        if (!diff.identical()) {
        if (!expectedSLD.equals(actualSLD)) {
            String message = "Comparison failed, the two files are: " + sldFile.getAbsolutePath()
                    + " " + sldFile2.getAbsolutePath();
            System.err.println(message);
            fail(message);
        }
//        }
    }

    StyledLayerDescriptor parseToSld(String sld) {
        SLDParser parser = new SLDParser(CommonFactoryFinder.getStyleFactory());
        parser.setInput(new StringReader(sld));
        return parser.parseSLD();
    }


    private List validateSLD(String sld) throws IOException, SAXException,
            ParserConfigurationException {
        Parser parser = new Parser(new SLDConfiguration());
        parser.validate(new StringReader(sld));
        return parser.getValidationErrors();
    }


    private void writeStyle(Style s, File sldFile) throws TransformerException, IOException,
            FileNotFoundException {
        StyledLayerDescriptor sld = STYLE_FACTORY.createStyledLayerDescriptor();
        NamedLayer layer = STYLE_FACTORY.createNamedLayer();
        layer.addStyle((org.geotools.styling.Style) s);
        sld.layers().add(layer);
        if (!sldFile.getParentFile().exists()) {
            assertTrue(sldFile.getParentFile().mkdirs());
        }
        try (FileOutputStream fos = new FileOutputStream(sldFile)) {
            SLDTransformer tx = new SLDTransformer();
            tx.setIndentation(2);
            tx.transform(sld, fos);
        }
    }

    private Style cssToSld(String css) {
        ParsingResult<Stylesheet> result = new ReportingParseRunner<Stylesheet>(parser.StyleSheet())
                .run(css);

        assertNoErrors(result);
        Stylesheet ss = result.parseTreeRoot.getValue();
        CssTranslator translator = new CssTranslator();
        return translator.translate(ss);
    }

}
