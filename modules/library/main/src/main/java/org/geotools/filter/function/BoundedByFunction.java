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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.filter.capability.FunctionName;
import org.opengis.geometry.Geometry;

/** A function returning the bounds of a feature (including all geometries) */
public class BoundedByFunction extends FunctionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl("boundedBy", parameter("result", Geometry.class));

    public BoundedByFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        if (!(object instanceof Feature)) {
            return null;
        }

        Feature feature = (Feature) object;
        ReferencedEnvelope env = ReferencedEnvelope.reference(feature.getBounds());
        if (env != null) {
            return JTS.toGeometry(env);
        } else {
            return null;
        }
    }
}
