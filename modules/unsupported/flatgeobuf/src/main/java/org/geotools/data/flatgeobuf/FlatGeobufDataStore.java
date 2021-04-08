/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.wololo.flatgeobuf.ColumnMeta;
import org.wololo.flatgeobuf.GeometryConversions;
import org.wololo.flatgeobuf.HeaderMeta;

public class FlatGeobufDataStore extends ContentDataStore {
    final URL url;
    final File file;
    final String typeName;

    HeaderMeta headerMeta;
    SimpleFeatureType phantomFeatureType;

    public FlatGeobufDataStore(URL url) {
        this.url = url;
        this.file = getFile(url);
        this.typeName = getTypeName(url);
    }

    static File getFile(URL url) {
        if (url.getProtocol().equals("file")) {
            try {
                return Paths.get(url.toURI()).toFile();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    static String getTypeName(URL url) {
        String path;
        try {
            path = url.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String lastSegment = path.substring(path.lastIndexOf('/') + 1);
        String name = lastSegment.substring(0, lastSegment.lastIndexOf('.'));
        return name;
    }

    protected HeaderMeta getHeaderMeta() throws IOException {
        if (headerMeta == null) {
            if (file != null && !file.exists()) {
                return null;
            }
            headerMeta = HeaderMeta.read(url.openStream());
        }
        return headerMeta;
    }

    protected URL getURL() {
        return url;
    }

    protected File getFile() {
        return file;
    }

    protected SimpleFeatureType getFeatureType(Name name) throws IOException {
        getHeaderMeta();
        if (headerMeta != null) {
            SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
            ftb.setName(name);
            ftb.setAbstract(false);
            CoordinateReferenceSystem crs;
            try {
                crs = headerMeta.srid > 0 ? CRS.decode("EPSG:" + headerMeta.srid) : null;
            } catch (FactoryException e) {
                throw new IOException(e);
            }
            ftb.setCRS(crs);
            ftb.add("geom", GeometryConversions.getGeometryClass(headerMeta.geometryType));
            for (ColumnMeta columnMeta : headerMeta.columns)
                ftb.add(columnMeta.name, columnMeta.getBinding());
            SimpleFeatureType featureType = ftb.buildFeatureType();
            return featureType;
        } else if (phantomFeatureType != null) {
            return phantomFeatureType;
        }
        throw new RuntimeException("Could not get FeatureType");
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return Collections.singletonList(getTypeName());
    }

    Name getTypeName() throws IOException {
        getHeaderMeta();
        return new NameImpl(namespaceURI, typeName);
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) {
        this.phantomFeatureType = featureType;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry contentEntry)
            throws IOException {
        if (file != null && !file.exists()) {
            return new FlatGeobufFeatureStore(contentEntry, Query.ALL);
        } else {
            return new FlatGeobufFeatureSource(contentEntry, Query.ALL);
        }
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        this.removeSchema(typeName.getLocalPart());
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        if (!file.exists()) {
            throw new IOException(
                    "Can't delete " + file.getAbsolutePath() + " because it doesn't exist!");
        }
        file.delete();
    }
}
