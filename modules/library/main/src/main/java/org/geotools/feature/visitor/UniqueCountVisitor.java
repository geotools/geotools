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
package org.geotools.feature.visitor;

import java.util.Set;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;

/**
 * Determines the number of unique features in the collection on the basis of the specified feature
 * attribute.
 */
public class UniqueCountVisitor extends UniqueVisitor {

    Integer count = null;

    public UniqueCountVisitor(String attributeTypeName) {
        super(attributeTypeName);
    }

    public UniqueCountVisitor(int attributeTypeIndex, SimpleFeatureType type)
            throws IllegalFilterException {
        super(attributeTypeIndex, type);
    }

    public UniqueCountVisitor(String attrName, SimpleFeatureType type)
            throws IllegalFilterException {
        super(attrName, type);
    }

    public UniqueCountVisitor(Expression expr) {
        super(expr);
    }

    public void setValue(Integer count) {
        this.count = count;
    }

    @Override
    public CalcResult getResult() {
        Set uniqueValues = getUnique();
        Integer result;
        if (count == null) {
            if (uniqueValues.isEmpty()) return CalcResult.NULL_RESULT;
            else result = uniqueValues.size();
        } else {
            result = count;
        }
        return new CountVisitor.CountResult(result);
    }
}
