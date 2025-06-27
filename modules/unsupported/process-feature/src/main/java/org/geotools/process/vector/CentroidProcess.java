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
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

/**
 * A process that returns the centroids for the geometries in the input feature collection.
 *
 * @author Rohan Singh
 */
@DescribeProcess(title = "Centroid", description = "Computes the geometric centroids of features")
public class CentroidProcess implements VectorProcess {

    @DescribeResult(name = "result", description = "Centroids of input features")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features)
            throws ProcessException {
        return DataUtilities.simple(new CentroidFeatureCollection(features));
    }

    static class CentroidFeatureCollection extends SimpleProcessingCollection {
        SimpleFeatureCollection delegate;

        public CentroidFeatureCollection(SimpleFeatureCollection delegate) {
            this.delegate = delegate;
        }

        @Override
        public SimpleFeatureIterator features() {
            return new CentroidFeatureIterator(delegate.features(), getSchema());
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return DataUtilities.bounds(features());
        }

        @Override
        protected SimpleFeatureType buildTargetFeatureType() {
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            for (AttributeDescriptor ad : delegate.getSchema().getAttributeDescriptors()) {
                if (ad instanceof GeometryDescriptor) {
                    GeometryDescriptor gd = (GeometryDescriptor) ad;
                    Class<?> binding = ad.getType().getBinding();
                    if (Point.class.isAssignableFrom(binding)) {
                        tb.add(ad);
                    } else {
                        tb.minOccurs(ad.getMinOccurs());
                        tb.maxOccurs(ad.getMaxOccurs());
                        tb.nillable(ad.isNillable());
                        tb.add(ad.getLocalName(), Point.class, gd.getCoordinateReferenceSystem());
                    }
                } else {
                    tb.add(ad);
                }
            }
            tb.setName(delegate.getSchema().getName());
            return tb.buildFeatureType();
        }

        @Override
        public int size() {
            return delegate.size();
        }
    }

    static class CentroidFeatureIterator implements SimpleFeatureIterator {
        SimpleFeatureIterator delegate;

        SimpleFeatureBuilder fb;

        public CentroidFeatureIterator(SimpleFeatureIterator delegate, SimpleFeatureType schema) {
            this.delegate = delegate;
            fb = new SimpleFeatureBuilder(schema);
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            SimpleFeature f = delegate.next();
            for (Object attribute : f.getAttributes()) {
                if (attribute instanceof Geometry && !(attribute instanceof Point)) {
                    attribute = ((Geometry) attribute).getCentroid();
                }
                fb.add(attribute);
            }
            return fb.buildFeature(f.getID());
        }
    }
}
