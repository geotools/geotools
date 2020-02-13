/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.opengis.filter.capability.FunctionName;

public class MapBoxAnchorFunction extends FunctionExpressionImpl {

    enum Axis {
        x() {
            @Override
            Double evaluate(SymbolMBLayer.TextAnchor anchor) {
                return anchor.getX();
            }
        },
        y() {
            @Override
            Double evaluate(SymbolMBLayer.TextAnchor anchor) {
                return anchor.getY();
            }
        };

        static Axis parse(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Axis should be either x or y");
            }
            return Axis.valueOf(name.toLowerCase());
        }

        abstract Double evaluate(SymbolMBLayer.TextAnchor anchor);
    }

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "mbAnchor",
                    parameter("anchorPercentage", Double.class),
                    parameter("anchorName", String.class, 1, 1),
                    parameter("axis", String.class, 1, 1));

    protected MapBoxAnchorFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object) {
        SymbolMBLayer.TextAnchor anchor;
        try { // attempt to get value and perform conversion
            anchor =
                    SymbolMBLayer.TextAnchor.parse(getExpression(0).evaluate(object, String.class));
        } catch (Exception e) { // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function \"mbAnchor\" argument #0 - expected anchor name, one of: "
                            + Stream.of(SymbolMBLayer.TextAnchor.values())
                                    .map(a -> a.name())
                                    .collect(Collectors.joining()));
        }

        Axis axis = Axis.parse(getExpression(1).evaluate(object, String.class));

        return axis.evaluate(anchor);
    }
}
