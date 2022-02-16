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

import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * A process providing the union between two feature collections
 *
 * @author Gianni Barrotta - Sinergis
 * @author Andrea Di Nora - Sinergis
 * @author Pietro Arena - Sinergis
 */
@DescribeProcess(
        title = "Union Feature Collections",
        description =
                "Returns single feature collection containing all features from two input feature collections.  The output attribute schema is a combination of the attributes from the inputs.  Attributes with same name but different types will be converted to strings.")
public class UnionFeatureCollection implements VectorProcess {

    static final String SCHEMA_NAME = "Union_Layer";

    @DescribeResult(name = "result", description = "Output feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "first", description = "First input feature collection")
                    SimpleFeatureCollection firstFeatures,
            @DescribeParameter(name = "second", description = "Second feature collection")
                    SimpleFeatureCollection secondFeatures)
            throws ClassNotFoundException {
        if (!(firstFeatures
                .features()
                .next()
                .getDefaultGeometry()
                .getClass()
                .equals(secondFeatures.features().next().getDefaultGeometry().getClass()))) {
            throw new ProcessException("Different default geometries, cannot perform union");
        } else {
            return new UnitedFeatureCollection(firstFeatures, secondFeatures);
        }
    }

    static class UnitedFeatureCollection extends DecoratingSimpleFeatureCollection {

        SimpleFeatureCollection features;

        SimpleFeatureType schema;

        public UnitedFeatureCollection(
                SimpleFeatureCollection delegate, SimpleFeatureCollection features)
                throws ClassNotFoundException {
            super(delegate);
            this.features = features;

            // Create schema containing the attributes from both the feature collections
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            for (AttributeDescriptor descriptor : delegate.getSchema().getAttributeDescriptors()) {
                if (sameNames(features.getSchema(), descriptor)
                        && !sameTypes(features.getSchema(), descriptor)) {
                    AttributeTypeBuilder builder = new AttributeTypeBuilder();
                    builder.setName(descriptor.getLocalName());
                    builder.setNillable(descriptor.isNillable());
                    builder.setBinding(String.class);
                    builder.setMinOccurs(descriptor.getMinOccurs());
                    builder.setMaxOccurs(descriptor.getMaxOccurs());
                    builder.setDefaultValue(descriptor.getDefaultValue());
                    builder.setCRS(
                            this.delegate
                                    .features()
                                    .next()
                                    .getFeatureType()
                                    .getCoordinateReferenceSystem());
                    AttributeDescriptor attributeDescriptor =
                            builder.buildDescriptor(descriptor.getName(), builder.buildType());
                    tb.add(attributeDescriptor);
                } else {
                    tb.add(descriptor);
                }
            }
            for (AttributeDescriptor descriptor : features.getSchema().getAttributeDescriptors()) {
                if (!sameNames(delegate.getSchema(), descriptor)
                        && !sameTypes(delegate.getSchema(), descriptor)) {
                    tb.add(descriptor);
                }
            }

            tb.setCRS(delegate.getSchema().getCoordinateReferenceSystem());
            tb.setNamespaceURI(delegate.getSchema().getName().getNamespaceURI());
            tb.setName(delegate.getSchema().getName());
            this.schema = tb.buildFeatureType();
        }

        @Override
        public SimpleFeatureIterator features() {
            return new UnitedFeatureIterator(
                    delegate.features(), delegate, features.features(), features, getSchema());
        }

        @Override
        public SimpleFeatureType getSchema() {
            return this.schema;
        }

        private boolean sameNames(SimpleFeatureType schema, AttributeDescriptor f) {
            for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
                if (descriptor.getName().equals(f.getName())) {
                    return true;
                }
            }
            return false;
        }

        private boolean sameTypes(SimpleFeatureType schema, AttributeDescriptor f) {
            for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
                if (descriptor.getType().equals(f.getType())) {
                    return true;
                }
            }
            return false;
        }
    }

    static class UnitedFeatureIterator implements SimpleFeatureIterator {

        SimpleFeatureIterator firstDelegate;

        SimpleFeatureIterator secondDelegate;

        SimpleFeatureCollection firstCollection;

        SimpleFeatureCollection secondCollection;

        SimpleFeatureBuilder fb;

        SimpleFeature next;

        int iterationIndex = 0;

        public UnitedFeatureIterator(
                SimpleFeatureIterator firstDelegate,
                SimpleFeatureCollection firstCollection,
                SimpleFeatureIterator secondDelegate,
                SimpleFeatureCollection secondCollection,
                SimpleFeatureType schema) {
            this.firstDelegate = firstDelegate;
            this.secondDelegate = secondDelegate;
            this.firstCollection = firstCollection;
            this.secondCollection = secondCollection;
            fb = new SimpleFeatureBuilder(schema);
        }

        @Override
        public void close() {
            firstDelegate.close();
            secondDelegate.close();
        }

        @Override
        public boolean hasNext() {

            while (next == null && firstDelegate.hasNext()) {
                SimpleFeature f = firstDelegate.next();
                for (PropertyDescriptor property : fb.getFeatureType().getDescriptors()) {
                    fb.set(property.getName(), f.getAttribute(property.getName()));
                }
                next = fb.buildFeature(Integer.toString(iterationIndex));
                fb.reset();
                iterationIndex++;
            }
            while (next == null && secondDelegate.hasNext() && !firstDelegate.hasNext()) {
                SimpleFeature f = secondDelegate.next();
                for (PropertyDescriptor property : fb.getFeatureType().getDescriptors()) {
                    fb.set(property.getName(), f.getAttribute(property.getName()));
                }
                next = fb.buildFeature(Integer.toString(iterationIndex));
                fb.reset();
                iterationIndex++;
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
