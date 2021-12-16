/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

/**
 * Adapts a {@link GetParser<SimpleFeature>} to the geotools {@link FeatureReader} interface, being
 * the base for all the data content related implementations in the WFS module.
 */
class WFSFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private SimpleFeature next;

    private GetParser<SimpleFeature> parser;

    private SimpleFeatureType featureType;

    private GetFeatureResponse response;

    /** @Deprecated (pass on response so it can be disposed.) */
    @Deprecated
    public WFSFeatureReader(final GetParser<SimpleFeature> parser) throws IOException {
        this(parser, null);
    }

    public WFSFeatureReader(
            final GetParser<SimpleFeature> parser, final GetFeatureResponse response)
            throws IOException {
        this.response = response;
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
        final GetParser<SimpleFeature> parser = this.parser;
        final GetFeatureResponse response = this.response;
        this.parser = null;
        this.next = null;
        this.response = null;
        if (parser != null) {
            parser.close();
        }
        if (response != null) {
            response.dispose();
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
