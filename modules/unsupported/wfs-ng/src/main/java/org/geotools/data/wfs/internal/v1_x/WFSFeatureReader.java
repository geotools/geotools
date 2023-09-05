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
package org.geotools.data.wfs.internal.v1_x;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;

/**
 * Adapts a {@link GetParser<SimpleFeature>} to the geotools {@link FeatureReader} interface, being
 * the base for all the data content related implementations in the WFS module.
 *
 * @author Gabriel Roldan (TOPP)
 * @since 2.5.x
 * @see WFSDataStore#getFeatureReader(Query, Transaction)
 */
class WFSFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private SimpleFeature next;

    private GetParser<SimpleFeature> parser;

    private SimpleFeatureType featureType;

    public WFSFeatureReader(final GetParser<SimpleFeature> parser) throws IOException {
        this.parser = parser;
        this.next = parser.parse();
        if (this.next != null) {
            FeatureType parsedType = next.getFeatureType();
            if (parsedType instanceof SimpleFeatureType) {
                this.featureType = (SimpleFeatureType) parsedType;
            } else {
                // this is the FeatureType as parsed by the StreamingParser, we need a simple view
                this.featureType = EmfAppSchemaParser.toSimpleFeatureType(parsedType);
            }
        }
    }

    /** @see FeatureReader#close() */
    @Override
    public void close() throws IOException {
        // System.err.println("Closing WFSFeatureReader for " + featureType.getName());
        final GetParser<SimpleFeature> parser = this.parser;
        this.parser = null;
        this.next = null;
        if (parser != null) {
            parser.close();
        }
    }

    /** @see FeatureReader#getFeatureType() */
    @Override
    public SimpleFeatureType getFeatureType() {
        if (featureType == null) {
            throw new IllegalStateException(
                    "No features were retrieved, shouldn't be calling getFeatureType()");
        }
        return featureType;
    }

    /** @see FeatureReader#hasNext() */
    @Override
    public boolean hasNext() throws IOException {
        return next != null;
    }

    /** @see FeatureReader#next() */
    @Override
    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (this.next == null) {
            throw new NoSuchElementException();
        }
        SimpleFeature current = this.next;
        this.next = parser.parse();
        return current;
    }
}
