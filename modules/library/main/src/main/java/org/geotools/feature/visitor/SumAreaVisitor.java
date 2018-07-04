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
package org.geotools.feature.visitor;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Calculates the Sum of Areas for geometric fields
 *
 * @author Mauro Bartolomeoli
 */
public class SumAreaVisitor extends SumVisitor {

    static FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);

    public SumAreaVisitor(Expression expr) throws IllegalFilterException {
        super(factory.function("area2", expr));
    }

    @Override
    public void visit(SimpleFeature feature) {
        this.setStrategy(new SumAreaStrategy());
        super.visit(feature);
    }

    @Override
    public void visit(Feature feature) {
        this.setStrategy(new SumAreaStrategy());
        super.visit(feature);
    }

    public SumAreaVisitor(int attributeTypeIndex, SimpleFeatureType type)
            throws IllegalFilterException {

        this(factory.property(type.getDescriptor(attributeTypeIndex).getLocalName()));
    }

    public SumAreaVisitor(String attrName, SimpleFeatureType type) throws IllegalFilterException {
        this(factory.property(type.getDescriptor(attrName).getLocalName()));
    }

    static class SumAreaStrategy implements SumStrategy {
        Double number = null;

        public void add(Object value) {
            Number num = (Number) value;
            if (num.doubleValue() >= 0) {
                if (number == null) {
                    number = 0.0;
                }
                number += num.doubleValue();
            }
        }

        public Object getResult() {
            return number == null ? null : new Double(number);
        }
    }
}
