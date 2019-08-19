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
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.wfs.internal.parsers.XmlComplexFeatureParser;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;

/**
 * Defines the complex feature iterator implementation class. It's responsible for exposing an
 * iterator-style interface for complex features.
 *
 * @author Adam Brown (Curtin University of Technology)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class ComplexFeatureIteratorImpl implements FeatureIterator<Feature> {

    private static final Logger LOGGER = Logging.getLogger(ComplexFeatureIteratorImpl.class);

    private XmlComplexFeatureParser parser;

    private Feature parsedFeature;

    private boolean hasNextCalled;

    /**
     * Initialises a new instance of ComplexFeatureIteratorImpl.
     *
     * @param parser The feature parser to use.
     */
    public ComplexFeatureIteratorImpl(XmlComplexFeatureParser parser) {
        this.parser = parser;
        this.parsedFeature = null;
        this.hasNextCalled = false;
    }

    @Override
    public boolean hasNext() {
        this.hasNextCalled = true;
        try {
            parsedFeature = parser.parse();
            return parsedFeature != null;
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            close();
            return false;
        }
    }

    @Override
    public Feature next() throws NoSuchElementException {
        if (!hasNextCalled) {
            if (hasNext()) {
                this.hasNextCalled = false;
                return parsedFeature;
            } else {
                close();
                return null;
            }
        } else {
            this.hasNextCalled = false;
            return parsedFeature;
        }
    }

    @Override
    public void close() {
        try {
            parser.close();
            parsedFeature = null;
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
    }
}
