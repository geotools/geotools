/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.feature.gs;

import java.util.List;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.feature.collection.FilteringSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.ProcessException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

@DescribeProcess(title = "Query", description = "Applies a filter and an attribute selection to the incoming feature collection. "
        + "The process can be also used as a pure format converter when no filtering or attribute selection is performed")
/**
 * 
 *
 * @source $URL$
 */
public class QueryProcess implements GSProcess {
    @DescribeResult(name = "result", description = "The filtered collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "The feature collection to filter") SimpleFeatureCollection features,
            @DescribeParameter(name = "attribute", description = "The attribute name(s)", collectionType = String.class, min = 0) List<String> attributes,
            @DescribeParameter(name = "filter", min = 0, description = "Filters the input features") Filter filter)
            throws ProcessException {
        // apply filtering if necessary
        if (filter != null && !filter.equals(Filter.INCLUDE)) {
            features = new FilteringSimpleFeatureCollection(features, filter);
        }

        // apply retyping if necessary
        if (attributes != null && attributes.size() > 0) {
            final String[] names = (String[]) attributes.toArray(new String[attributes.size()]);
            SimpleFeatureType ft = SimpleFeatureTypeBuilder.retype(features.getSchema(), names);
            if (!(ft.equals(features.getSchema()))) {
                features = new ReTypingFeatureCollection(features, ft);
            }
        }

        return features;
    }

}
