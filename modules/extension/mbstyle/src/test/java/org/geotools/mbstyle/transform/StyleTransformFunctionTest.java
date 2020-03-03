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
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.styling.StyledLayerDescriptor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

public class StyleTransformFunctionTest {

    /**
     * Right now these tests exist basically to ensure that these styles with functions are parsed
     * without exception. Lower level unit tests exist to test the results of parsing individual
     * elements
     */
    @Test
    public void testLineLayerWithFunctions() throws IOException, ParseException {
        JSONObject styleJson = MapboxTestUtils.parseTestStyle("lineStyleFunctionTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(styleJson);
        StyledLayerDescriptor transformed = mbStyle.transform();
    }

    @Test
    public void testLabelLayerWithFunctions() throws IOException, ParseException {
        JSONObject styleJson = MapboxTestUtils.parseTestStyle("labelFunctionStyleTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(styleJson);
        StyledLayerDescriptor transform = mbStyle.transform();
    }

    @Test
    public void testSymbolLayerWithFunctions() throws IOException, ParseException {
        JSONObject styleJson = MapboxTestUtils.parseTestStyle("symbolStyleFunctionTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(styleJson);
        StyledLayerDescriptor transformed = mbStyle.transform();
    }

    @Test
    public void testFillLayerWithFunctions() throws IOException, ParseException {
        JSONObject styleJson = MapboxTestUtils.parseTestStyle("fillStyleFunctionTest.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(styleJson);
        StyledLayerDescriptor transformed = mbStyle.transform();
    }

    @Test
    public void testFillLayerWithFunctionsZoomAndProperty() throws IOException, ParseException {
        JSONObject styleJson =
                MapboxTestUtils.parseTestStyle("fillStyleFunctionTestZoomAndProperty.json");

        // Parse to MBStyle
        MBStyle mbStyle = new MBStyle(styleJson);
        StyledLayerDescriptor transformed = mbStyle.transform();
    }
}
