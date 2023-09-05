/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.crs;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;

/**
 * ForceCoordinateSystemFeatureReader provides a CoordinateReferenceSystem for FeatureTypes.
 *
 * <p>ForceCoordinateSystemFeatureReader is a wrapper used to force GeometryAttributes to a user
 * supplied CoordinateReferenceSystem rather then the default supplied by the DataStore.
 *
 * <p>Example Use:
 *
 * <pre><code>
 * ForceCoordinateSystemFeatureReader reader =
 *     new ForceCoordinateSystemFeatureReader( originalReader, forceCS );
 *
 * CoordinateReferenceSystem originalCS =
 *     originalReader.getFeatureType().getDefaultGeometry().getCoordinateSystem();
 *
 * CoordinateReferenceSystem newCS =
 *     reader.getFeatureType().getDefaultGeometry().getCoordinateSystem();
 *
 * assertEquals( forceCS, newCS );
 * </code></pre>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author aaime
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class ForceCoordinateSystemFeatureReader
        implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    protected FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    protected SimpleFeatureBuilder builder;

    /** Shortcut constructor that can be used if the new schema has already been computed */
    ForceCoordinateSystemFeatureReader(
            FeatureReader<SimpleFeatureType, SimpleFeature> reader, SimpleFeatureType schema) {
        this.reader = reader;
        this.builder = new SimpleFeatureBuilder(schema);
    }

    /** Builds a new ForceCoordinateSystemFeatureReader */
    public ForceCoordinateSystemFeatureReader(
            FeatureReader<SimpleFeatureType, SimpleFeature> reader, CoordinateReferenceSystem cs)
            throws SchemaException {
        this(reader, cs, false);
    }

    /** Builds a new ForceCoordinateSystemFeatureReader */
    public ForceCoordinateSystemFeatureReader(
            FeatureReader<SimpleFeatureType, SimpleFeature> reader,
            CoordinateReferenceSystem cs,
            boolean forceOnlyMissing)
            throws SchemaException {
        if (cs == null) {
            throw new NullPointerException("CoordinateSystem required");
        }

        SimpleFeatureType type = reader.getFeatureType();
        CoordinateReferenceSystem originalCs = type.getCoordinateReferenceSystem();

        if (!cs.equals(originalCs)) {
            type = FeatureTypes.transform(type, cs, forceOnlyMissing);
        }
        this.builder = new SimpleFeatureBuilder(type);

        this.reader = reader;
    }

    /** @see FeatureReader#getFeatureType() */
    @Override
    public SimpleFeatureType getFeatureType() {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        if (builder == null) return reader.getFeatureType();

        return builder.getFeatureType();
    }

    /** @see FeatureReader#next() */
    @Override
    public SimpleFeature next()
            throws IOException, IllegalAttributeException, NoSuchElementException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        SimpleFeature next = reader.next();
        if (builder == null) return next;

        return SimpleFeatureBuilder.retype(next, builder);
    }

    /** @see FeatureReader#hasNext() */
    @Override
    public boolean hasNext() throws IOException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        return reader.hasNext();
    }

    /** @see FeatureReader#close() */
    @Override
    public void close() throws IOException {
        if (reader == null) {
            throw new IllegalStateException("Reader has already been closed");
        }

        reader.close();
        reader = null;
        builder = null;
    }
}
