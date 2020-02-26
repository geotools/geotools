/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.feature;

import java.util.Map;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Process which buffers an entire feature collection.
 *
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 */
public class BufferFeatureCollectionProcess extends FeatureToFeatureProcess {

    /** Constructor */
    public BufferFeatureCollectionProcess(BufferFeatureCollectionFactory factory) {
        super(factory);
    }

    @Override
    protected void processFeature(SimpleFeature feature, Map<String, Object> input)
            throws Exception {
        Double buffer = (Double) input.get(BufferFeatureCollectionFactory.BUFFER.key);

        Geometry g = (Geometry) feature.getDefaultGeometry();
        g = g.buffer(buffer);

        if (g instanceof Polygon) {
            g = g.getFactory().createMultiPolygon(new Polygon[] {(Polygon) g});
        }

        feature.setDefaultGeometry(g);
    }

    @Override
    protected SimpleFeatureType getTargetSchema(
            SimpleFeatureType sourceSchema, Map<String, Object> input) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        for (AttributeDescriptor ad : sourceSchema.getAttributeDescriptors()) {
            GeometryDescriptor defaultGeometry = sourceSchema.getGeometryDescriptor();
            if (ad == defaultGeometry) {
                tb.add(
                        ad.getName().getLocalPart(),
                        MultiPolygon.class,
                        defaultGeometry.getCoordinateReferenceSystem());
            } else {
                tb.add(ad);
            }
        }
        tb.setName(sourceSchema.getName());
        return tb.buildFeatureType();
    }
}
