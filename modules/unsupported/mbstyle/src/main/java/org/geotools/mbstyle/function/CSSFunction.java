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
package org.geotools.mbstyle.function;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.Parameter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.geotools.text.Text;
import org.geotools.util.ColorConverterFactory;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Generate a Color using {@link ColorConverterFactory}, the input is expected to be a
 * String.
 * 
 * <h2>Parameters:</h2>
 * 
 * <ol start="0">
 * <li>string: string defining color</li>
 * </ol>
 * 
 * We are using this function, rather than the default converter in order to force the use
 * of CONVERT_CSS_TO_COLOR.
 *
 * @author Jody Garnett (Boundless)
 */
public class CSSFunction extends FunctionImpl {

    public static final FunctionName NAME;
    static {
        Parameter<Color> color = new Parameter<Color>("color", Color.class, 1, 1);
        Parameter<Double> string = new Parameter<Double>("string", Double.class,
                Text.text("String"),
                Text.text("Color definition provided as hex, rgb, or css color name."));
        NAME = new FunctionNameImpl("css", color, string);
    }

    public CSSFunction() {
        this.functionName = NAME;
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        Expression input = getParameters().get(0);
        String string = input.evaluate(object, String.class);
        if (string == null) {
            return null;
        }
        Color color;
        try {
            color = ColorConverterFactory.CONVERT_CSS_TO_COLOR.convert(string, Color.class);
        } catch (Exception e) {
            return null;
        }
        
        return Converters.convert(color, context);
    }
}
