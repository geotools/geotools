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
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A process simplifying the geometries in the input feature collection according to a specified
 * distance, and using either a topology preserving or a Douglas-Peuker algorithm
 *
 * @author Andrea Aime - OpenGeo
 */
@DescribeProcess(
        title = "Simplify",
        description =
                "Simplifies feature geometry by reducing vertices using Douglas-Peucker simplification.")
public class SimplifyProcess implements VectorProcess {

    @DescribeResult(name = "result", description = "The simplified feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(name = "distance", description = "Simplification distance tolerance")
                    double distance,
            @DescribeParameter(
                            name = "preserveTopology",
                            description =
                                    "If True, ensures that simplified features are topologically valid",
                            defaultValue = "false")
                    boolean preserveTopology)
            throws ProcessException {
        if (distance < 0) {
            throw new ProcessException("Invalid distance, it should be a positive number");
        }

        return new SimplifyingFeatureCollection(features, distance, preserveTopology);
    }

    static class SimplifyingFeatureCollection extends DecoratingSimpleFeatureCollection {
        double distance;

        boolean preserveTopology;

        public SimplifyingFeatureCollection(
                SimpleFeatureCollection delegate, double distance, boolean preserveTopology) {
            super(delegate);
            this.distance = distance;
            this.preserveTopology = preserveTopology;
        }

        @Override
        public SimpleFeatureIterator features() {
            return new SimplifyingFeatureIterator(
                    delegate.features(), distance, preserveTopology, getSchema());
        }
    }

    static class SimplifyingFeatureIterator implements SimpleFeatureIterator {
        SimpleFeatureIterator delegate;

        double distance;

        boolean preserveTopology;

        SimpleFeatureBuilder fb;

        public SimplifyingFeatureIterator(
                SimpleFeatureIterator delegate,
                double distance,
                boolean preserveTopology,
                SimpleFeatureType schema) {
            this.delegate = delegate;
            this.distance = distance;
            this.preserveTopology = preserveTopology;
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
                if (attribute instanceof Geometry) {
                    if (preserveTopology) {
                        attribute =
                                TopologyPreservingSimplifier.simplify(
                                        (Geometry) attribute, distance);
                    } else {
                        attribute =
                                DouglasPeuckerSimplifier.simplify((Geometry) attribute, distance);
                    }
                }
                fb.add(attribute);
            }
            return fb.buildFeature(f.getID());
        }
    }
}
