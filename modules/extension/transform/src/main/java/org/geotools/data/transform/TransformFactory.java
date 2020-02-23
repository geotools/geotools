/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;

/**
 * Builds a transformed {@link SimpleFeatureStore} or {@link SimpleFeatureSource} based on the
 * definitions provided
 *
 * @author Andrea Aime - GeoSolutions
 */
public class TransformFactory {

    static final Logger LOGGER = Logging.getLogger(TransformFactory.class);

    /**
     * Creates a transformed SimpleFeatureSource/SimpleFeatureStore from the original source, giving
     * it a certain name and a set of computed properties
     *
     * @returns A transformed SimpleFeatureStore in case at least one of the definitions was
     *     invertible, a transformed SimpleFeatureSource otherwise
     */
    public static SimpleFeatureSource transform(
            SimpleFeatureSource source, String name, List<Definition> definitions)
            throws IOException {
        return transform(
                source,
                new NameImpl(source.getSchema().getName().getNamespaceURI(), name),
                definitions);
    }

    /**
     * Creates a transformed SimpleFeatureSource/SimpleFeatureStore from the original source, giving
     * it a certain name and a set of computed properties
     *
     * @returns A transformed SimpleFeatureStore in case at least one of the definitions was
     *     invertible, a transformed SimpleFeatureSource otherwise
     */
    public static SimpleFeatureSource transform(
            SimpleFeatureSource source, Name name, List<Definition> definitions)
            throws IOException {
        if (source instanceof SimpleFeatureStore) {
            try {
                return new TransformFeatureStore((SimpleFeatureStore) source, name, definitions);
            } catch (IllegalArgumentException e) {
                LOGGER.log(
                        Level.FINEST,
                        "Could not transform the provided store, will turn it into a read "
                                + "only SimpleFeatureSource instead (this is not a problem unless you "
                                + "actually needed to write on the store)",
                        e);
            }
        }

        return new TransformFeatureSource(source, name, definitions);
    }
}
