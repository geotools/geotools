/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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

import java.util.NoSuchElementException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * Buffers a feature collection using a certain distance
 *
 * @author Gianni Barrotta - Sinergis
 * @author Andrea Di Nora - Sinergis
 * @author Pietro Arena - Sinergis
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(
        title = "Buffer",
        description =
                "Buffers features by a distance value supplied either as a parameter or by a feature attribute. Calculates buffers based on Cartesian distances.")
public class BufferFeatureCollection implements VectorProcess {
    @DescribeResult(description = "Buffered feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(name = "distance", description = "Fixed value to use for the buffer distance")
                    Double distance,
            @DescribeParameter(
                            name = "attributeName",
                            description = "Attribute containing the buffer distance value",
                            min = 0)
                    String attribute) {

        if (distance == null && (attribute == null || attribute == "")) {
            throw new IllegalArgumentException("Buffer distance was not specified");
        }

        if (attribute != null && !"".equals(attribute)) {
            if (features.getSchema().getDescriptor(attribute) == null) {
                boolean found = false;
                // case insensitive search
                for (AttributeDescriptor ad : features.getSchema().getAttributeDescriptors()) {
                    if (ad.getLocalName().equals(attribute)) {
                        attribute = ad.getLocalName();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IllegalArgumentException(
                            "Attribute not found among the source collection ones: " + attribute);
                }
            }
        } else {
            attribute = null;
        }
        return new BufferedFeatureCollection(features, attribute, distance);
    }

    /** Wrapper that will trigger the buffer computation as features are requested */
    static class BufferedFeatureCollection extends SimpleProcessingCollection {

        Double distance;

        String attribute;

        SimpleFeatureCollection delegate;

        public BufferedFeatureCollection(SimpleFeatureCollection delegate, String attribute, Double distance) {
            this.distance = distance;
            this.attribute = attribute;
            this.delegate = delegate;
        }

        @Override
        public SimpleFeatureIterator features() {
            return new BufferedFeatureIterator(delegate, this.attribute, this.distance, getSchema());
        }

        @Override
        public ReferencedEnvelope getBounds() {
            if (attribute == null) {
                // in this case we just have to expand the original collection bounds
                ReferencedEnvelope re = delegate.getBounds();
                re.expandBy(distance);
                return re;
            } else {
                // unlucky case, we need to actually compute by hand...
                return DataUtilities.bounds(features());
            }
        }

        @Override
        protected SimpleFeatureType buildTargetFeatureType() {
            // create schema
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            for (AttributeDescriptor descriptor : delegate.getSchema().getAttributeDescriptors()) {
                if (!(descriptor.getType() instanceof GeometryTypeImpl)
                        || !delegate.getSchema().getGeometryDescriptor().equals(descriptor)) {
                    tb.add(descriptor);
                } else {
                    AttributeTypeBuilder builder = new AttributeTypeBuilder();
                    builder.setBinding(MultiPolygon.class);
                    AttributeDescriptor attributeDescriptor =
                            builder.buildDescriptor(descriptor.getLocalName(), builder.buildType());
                    tb.add(attributeDescriptor);
                    if (tb.getDefaultGeometry() == null) {
                        tb.setDefaultGeometry(descriptor.getLocalName());
                    }
                }
            }
            tb.setDescription(delegate.getSchema().getDescription());
            tb.setCRS(delegate.getSchema().getCoordinateReferenceSystem());
            tb.setName(delegate.getSchema().getName());
            return tb.buildFeatureType();
        }

        @Override
        public int size() {
            return delegate.size();
        }
    }

    /** Buffers each feature as we scroll over the collection */
    static class BufferedFeatureIterator implements SimpleFeatureIterator {
        SimpleFeatureIterator delegate;

        SimpleFeatureCollection collection;

        Double distance;

        String attribute;

        int count;

        SimpleFeatureBuilder fb;

        SimpleFeature next;

        public BufferedFeatureIterator(
                SimpleFeatureCollection delegate, String attribute, Double distance, SimpleFeatureType schema) {
            this.delegate = delegate.features();
            this.distance = distance;
            this.collection = delegate;
            this.attribute = attribute;
            fb = new SimpleFeatureBuilder(schema);
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Override
        public boolean hasNext() {
            while (next == null && delegate.hasNext()) {
                SimpleFeature f = delegate.next();
                for (Object value : f.getAttributes()) {
                    if (value instanceof Geometry) {
                        Double fDistance = distance;
                        if (this.attribute != null) {
                            fDistance = Converters.convert(f.getAttribute(this.attribute), Double.class);
                        }
                        if (fDistance != null && fDistance != 0.0) {
                            value = ((Geometry) value).buffer(fDistance);
                        }
                    }
                    fb.add(value);
                }
                next = fb.buildFeature("" + count);
                count++;
                fb.reset();
            }
            return next != null;
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("hasNext() returned false!");
            }
            SimpleFeature result = next;
            next = null;
            return result;
        }
    }
}
