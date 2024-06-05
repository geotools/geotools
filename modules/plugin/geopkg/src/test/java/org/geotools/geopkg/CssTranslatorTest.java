
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) [Year], Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.styling.css.CssTranslatorTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.styling.Style;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.junit.Before;
import org.junit.Test;

public class CssTranslatorTest {
    private CssTranslatorTest translator;
    private Stylesheet stylesheet;

    @Before
    public void setUp(){
        translator = new CssTranslatorTest();
        stylesheet = new Stylesheet();
    }
    /**
     * Test to ensure that the 'autoRuleNames' directive in the CssTranslator assigns unique names
     * to each rule in the translated style.
     */
    @Test
    public void testTranslateWithAutoNames(){
        stylesheet.addDirective(CssTranslator.DIRECTIVE_AUTO_RULE_NAMES, "true");

        Style translatedStyle = translator.translate(stylesheet);
        int ruleNbr = 0;
        for(FeatureTypeStyle ftStyle : translatedStyle.featureTypeStyle()){
            for( Rule rule : ftStyle.rules()){
                assertEquals("Rule name does not match the expected unique name",
                                String.format("%d", ruleNbr++), rule.getName());
            }
        }
    }
}
