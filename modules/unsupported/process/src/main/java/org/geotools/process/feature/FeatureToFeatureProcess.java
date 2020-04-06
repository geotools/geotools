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

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.util.ProgressListener;

/**
 * Abstract implementation of Process for feature collections.
 *
 * <p>Subclasses need to implement {@link #processFeature(SimpleFeature, Map)}. This method should
 * perform the operation on the feature, changing any attributes on the feature as necessary.
 *
 * @see FeatureToFeatureProcessFactory
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 */
public abstract class FeatureToFeatureProcess extends AbstractFeatureCollectionProcess {

    /** Constructor */
    public FeatureToFeatureProcess(FeatureToFeatureProcessFactory factory) {
        super(factory);
    }

    public final Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor) {
        if (monitor == null) {
            monitor = new NullProgressListener();
        }

        // read the parameters, features and buffer amount
        FeatureCollection features =
                (FeatureCollection) input.get(FeatureToFeatureProcessFactory.FEATURES.key);

        // start progress
        float scale = 100f / features.size();
        monitor.started();

        // create the result feature collection
        SimpleFeatureType targetSchema =
                getTargetSchema((SimpleFeatureType) features.getSchema(), input);
        DefaultFeatureCollection result = new DefaultFeatureCollection(null, targetSchema);

        SimpleFeatureBuilder fb = new SimpleFeatureBuilder((SimpleFeatureType) result.getSchema());
        FeatureIterator fi = features.features();
        try {
            int counter = 0;
            while (fi.hasNext()) {
                // copy the feature
                fb.init((SimpleFeature) fi.next());
                SimpleFeature feature = fb.buildFeature(null);

                // buffer the geometry
                try {
                    processFeature(feature, input);
                } catch (Exception e) {
                    monitor.exceptionOccurred(e);
                }

                monitor.progress(scale * counter++);
                result.add(feature);
            }
        } finally {
            fi.close();
        }
        monitor.complete();

        // return the result
        Map<String, Object> output = new HashMap<String, Object>();
        output.put(FeatureToFeatureProcessFactory.RESULT.key, result);
        return output;
    }

    /**
     * Subclasses should override if the target schema is different that then original schema (mind,
     * if the number of attributes changes it's better to roll your own class instead of using this
     * one)
     */
    protected SimpleFeatureType getTargetSchema(
            SimpleFeatureType sourceSchema, Map<String, Object> input) {
        return sourceSchema;
    }
}
