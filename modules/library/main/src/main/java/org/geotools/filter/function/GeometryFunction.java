/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.VolatileFunction;

/**
 * Function the returns the default geometry of a feature, or null if there is none, or it's not a
 * JTS geometry
 *
 * @author Andrea Aime - GeoSolutions
 */
public class GeometryFunction extends FunctionExpressionImpl implements VolatileFunction {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "geometry",
                    FunctionNameImpl.parameter(
                            "geometry",
                            Boolean.class,
                            "Default Geometry",
                            "Default geometry, or null if there is none."));

    public GeometryFunction() {
        super(NAME);
    }

    public Object evaluate(Feature feature) {
        if (feature == null) {
            return null;
        } else if (feature instanceof SimpleFeature) {
            SimpleFeature sf = (SimpleFeature) feature;
            return geometry(sf.getDefaultGeometry());
        } else {
            GeometryAttribute ga = feature.getDefaultGeometryProperty();
            if (ga == null) {
                return null;
            } else {
                return geometry(ga.getValue());
            }
        }
    }

    private Object geometry(Object value) {
        if (value instanceof Geometry) {
            return value;
        } else {
            return null;
        }
    }

    public Object evaluate(Object object) {
        if (object instanceof Feature) {
            return evaluate((Feature) object);
        } else {
            return null;
        }
    }
}
