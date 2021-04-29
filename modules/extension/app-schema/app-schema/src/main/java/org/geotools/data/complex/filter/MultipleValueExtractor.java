/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.complex.config.MultipleValue;
import org.geotools.filter.FilterAttributeExtractor;
import org.opengis.filter.expression.PropertyName;

public class MultipleValueExtractor extends FilterAttributeExtractor {

    private List<MultipleValue> multipleValues = new ArrayList<>();

    @Override
    public Object visit(PropertyName expression, Object data) {
        if (expression instanceof MultipleValue) multipleValues.add((MultipleValue) expression);
        return super.visit(expression, data);
    }

    public List<MultipleValue> getMultipleValues() {
        return multipleValues;
    }
}
