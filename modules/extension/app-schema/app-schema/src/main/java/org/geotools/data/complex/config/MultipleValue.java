/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.config;

import java.util.List;
import org.geotools.data.complex.AttributeMapping;
import org.geotools.data.complex.FeatureTypeMapping;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/** Represents a mapping that can result in multiple values. */
public interface MultipleValue extends Expression {

    /** Returns the ID of the multivalued mapping. */
    String getId();

    /** Sets the feature type mapping that contains this multi valued mapping. */
    void setFeatureTypeMapping(FeatureTypeMapping featureTypeMapping);

    /** Sets the attribute type mapping that contains this multi valued mapping. */
    void setAttributeMapping(AttributeMapping attributeMapping);

    /** Returns the values extracted from the provided feature using this multivalued mapping. */
    List<Object> getValues(Feature feature, AttributeMapping attributeMapping);
}
