/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.transform;

import java.io.IOException;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.styling.StyledLayerDescriptor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

public class StyleTransformFunctionTest {

    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();  
    
    @Test
    public void testLineLayerWithFunctions() throws IOException, ParseException {
        JSONObject styleJson = MapboxTestUtils.parseTestStyle("lineStyleFunctionTest.json");
        
        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(styleJson);
        StyledLayerDescriptor transformed = new MBStyleTransformer().tranform(mbStyle);    // TODO fails b/c functions aren't used by transformers & parsers  
        
        // Assert that things are as expected
    }
    
}
