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
package org.geotools.process.vector;

import java.util.List;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.util.ProgressListener;

/**
 * Returns the unique values of a certain attribute
 *
 * @author Andrea Aime
 */
@DescribeProcess(
        title = "Unique",
        description = "Returns the unique values of a given attribute in a feature collection.")
public class UniqueProcess implements VectorProcess {

    @DescribeResult(
            name = "result",
            description = "Feature collection with an attribute containing the unique values")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(
                            name = "attribute",
                            description = "Attribute whose unique values are extracted")
                    String attribute,
            ProgressListener progressListener)
            throws Exception {

        int attIndex = -1;
        List<AttributeDescriptor> atts = features.getSchema().getAttributeDescriptors();
        for (int i = 0; i < atts.size(); i++) {
            if (atts.get(i).getLocalName().equals(attribute)) {
                attIndex = i;
                break;
            }
        }

        UniqueVisitor visitor = new UniqueVisitor(attIndex, features.getSchema());
        features.accepts(visitor, progressListener);
        List uniqueValues = visitor.getResult().toList();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("value", features.getSchema().getDescriptor(attIndex).getType().getBinding());
        tb.setName("UniqueValue");
        SimpleFeatureType ft = tb.buildFeatureType();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);

        ListFeatureCollection result = new ListFeatureCollection(ft);
        for (Object value : uniqueValues) {
            fb.add(value);
            result.add(fb.buildFeature(null));
        }
        return result;
    }
}
