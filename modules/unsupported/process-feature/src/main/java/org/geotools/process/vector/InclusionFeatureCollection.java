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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.WrappingIterator;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A process providing a feature collection containing the features of the first input collection
 * which are included in the second feature collection
 * 
 * @author Gianni Barrotta - Sinergis
 * @author Andrea Di Nora - Sinergis
 * @author Pietro Arena - Sinergis
 */
@DescribeProcess(title = "Inclusion of Feature Collections", description = "Returns a feature collection consisting of the features from the first collection which are spatially contained in at least one feature of the second collection.")
public class InclusionFeatureCollection implements VectorProcess {
    @DescribeResult(description = "Output feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "first", description = "First feature collection") SimpleFeatureCollection firstFeatures,
            @DescribeParameter(name = "second", description = "Second feature collection") SimpleFeatureCollection secondFeatures) {
        return new IncludedFeatureCollection(firstFeatures, secondFeatures);
    }

    /**
     * Wrapper that will trigger the "included" computation as features are requested
     */
    static class IncludedFeatureCollection extends DecoratingSimpleFeatureCollection {

        SimpleFeatureCollection features;

        public IncludedFeatureCollection(SimpleFeatureCollection delegate,
                SimpleFeatureCollection features) {
            super(delegate);
            this.features = features;

        }

        @Override
        public SimpleFeatureIterator features() {
            return new IncludedFeatureIterator(delegate.features(), delegate, features, getSchema());
        }

        @Override
        public Iterator<SimpleFeature> iterator() {
            return new WrappingIterator(features());
        }

        @Override
        public void close(Iterator<SimpleFeature> close) {
            if (close instanceof WrappingIterator) {
                ((WrappingIterator) close).close();
            }
        }
    }

    /**
     * Computes the inclusion property as we stream
     */
    static class IncludedFeatureIterator implements SimpleFeatureIterator {
        SimpleFeatureIterator delegate;

        SimpleFeatureCollection firstFeatures;

        SimpleFeatureCollection secondFeatures;

        SimpleFeatureBuilder fb;

        SimpleFeature next;

        String dataGeomName;
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        public IncludedFeatureIterator(SimpleFeatureIterator delegate,
                SimpleFeatureCollection firstFeatures, SimpleFeatureCollection secondFeatures,
                SimpleFeatureType schema) {
            this.delegate = delegate;
            this.firstFeatures = firstFeatures;
            this.secondFeatures = secondFeatures;
            this.fb = new SimpleFeatureBuilder(schema);
            this.dataGeomName = this.firstFeatures.getSchema().getGeometryDescriptor()
                    .getLocalName();

        }

        public void close() {
            delegate.close();
        }

        public boolean hasNext() {
            while (next == null && delegate.hasNext()) {
                SimpleFeature f = delegate.next();
                for (Object attribute : f.getAttributes()) {
                    if (attribute instanceof Geometry) {
                        Geometry geom = (Geometry) attribute;
                        Filter overFilter = ff
                                .contains(ff.property(dataGeomName), ff.literal(geom));
                        SimpleFeatureCollection subFeatureCollectionInclusion = this.secondFeatures
                                .subCollection(overFilter);
                        if (subFeatureCollectionInclusion.size() > 0) {
                            next = f;
                        }
                    }
                }
            }
            return next != null;
        }

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
