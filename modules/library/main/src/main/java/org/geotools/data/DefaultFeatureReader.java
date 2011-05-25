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
package org.geotools.data;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * Basic support for reading Features from an AttributeReader.
 *
 * @author Ian Schneider
 *
 * @source $URL$
 * @version $Id$
 */
public class DefaultFeatureReader implements  FeatureReader<SimpleFeatureType, SimpleFeature> {
    private final AttributeReader attributeReader;
    private final SimpleFeatureType schema;
    protected final Object[] attributes;

    /**
     * Creates a new instance of AbstractFeatureReader
     *
     * @param attributeReader AttributeReader for contents
     * @param schema FeatureType to use, <code>null</code> if not provided
     *
     * @throws SchemaException If Schema could not be obtained
     */
    public DefaultFeatureReader(AttributeReader attributeReader,
        SimpleFeatureType schema) throws SchemaException {
        this.attributeReader = attributeReader;

        if (schema == null) {
            schema = createSchema();
        }

        this.schema = schema;
        this.attributes = new Object[attributeReader.getAttributeCount()];
    }

    public DefaultFeatureReader(AttributeReader attributeReader)
        throws SchemaException {
        this(attributeReader, null);
    }

    public SimpleFeature next()
        throws IOException, IllegalAttributeException, NoSuchElementException {
        SimpleFeature f = null;

        if (attributeReader.hasNext()) {
            attributeReader.next();
            f = readFeature(attributeReader);
        }

        return f;
    }

    protected SimpleFeatureType createSchema() throws SchemaException {
        
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        for (int i = 0, ii = attributeReader.getAttributeCount(); i < ii;
                i++) {
            builder.add(attributeReader.getAttributeType(i));
        }

        return builder.buildFeatureType();
    }

    protected SimpleFeature readFeature(AttributeReader atts)
        throws IllegalAttributeException, IOException {
        for (int i = 0, ii = atts.getAttributeCount(); i < ii; i++) {
            attributes[i] = atts.read(i);
        }

        return SimpleFeatureBuilder.build( schema, attributes, null );
    }

    public void close() throws IOException {
        attributeReader.close();
    }

    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    public boolean hasNext() throws IOException {
        return attributeReader.hasNext();
    }
}
