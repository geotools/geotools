/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.io.IOException;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;

/** {@link GranuleSource} wrapper exposing a different type name */
public class RenamingGranuleSource implements GranuleSource {

    protected final String name;
    protected final GranuleSource delegate;
    protected final SimpleFeatureType schema;
    private final String delegateTypeName;

    /**
     * Builds a {@link RenamingGranuleSource}
     *
     * @param name The new type name
     * @param delegate The delegate to be wrapped
     */
    public RenamingGranuleSource(String name, GranuleSource delegate) {
        this.name = name;
        this.delegate = delegate;
        SimpleFeatureType schema = null;
        try {
            schema = delegate.getSchema();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.delegateTypeName = schema.getTypeName();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.init(schema);
        builder.setName(name);
        this.schema = builder.buildFeatureType();
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        Query renamed = renameQuery(q);
        SimpleFeatureCollection granules = delegate.getGranules(renamed);
        SimpleFeatureType targetSchema = this.schema;
        if (q.getPropertyNames() != Query.ALL_NAMES) {
            targetSchema = SimpleFeatureTypeBuilder.retype(schema, q.getPropertyNames());
        }
        return new ReTypingFeatureCollection(granules, targetSchema);
    }

    protected Query renameQuery(Query q) {
        Query renamed = new Query(q);
        renamed.setTypeName(delegateTypeName);
        return renamed;
    }

    @Override
    public int getCount(Query q) throws IOException {
        return delegate.getCount(renameQuery(q));
    }

    @Override
    public ReferencedEnvelope getBounds(Query q) throws IOException {
        return delegate.getBounds(renameQuery(q));
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return schema;
    }

    @Override
    public void dispose() throws IOException {
        delegate.dispose();
    }
}
