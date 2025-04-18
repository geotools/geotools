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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.locationtech.jts.geom.Geometry;

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

    public SumAreaVisitor(int attributeTypeIndex, SimpleFeatureType type) throws IllegalFilterException {

        this(factory.property(type.getDescriptor(attributeTypeIndex).getLocalName()));
    }

    public SumAreaVisitor(String attrName, SimpleFeatureType type) throws IllegalFilterException {
        this(factory.property(type.getDescriptor(attrName).getLocalName()));
    }

    @Override
    public Optional<List<Class>> getResultType(List<Class> inputTypes) {
        if (inputTypes == null || inputTypes.size() != 1)
            throw new IllegalArgumentException("Expecting a single type in input, not " + inputTypes);

        Class type = inputTypes.get(0);
        if (Geometry.class.isAssignableFrom(type)) {
            return Optional.of(Collections.singletonList(Double.class));
        }
        throw new IllegalArgumentException("The input type for sum must be geometric, instead this was found: " + type);
    }

    static class SumAreaStrategy implements SumStrategy {
        Double number = null;

        @Override
        public void add(Object value) {
            Number num = (Number) value;
            if (num.doubleValue() >= 0) {
                if (number == null) {
                    number = 0.0;
                }
                number += num.doubleValue();
            }
        }

        @Override
        public Object getResult() {
            return number == null ? null : Double.valueOf(number);
        }
    }
}
