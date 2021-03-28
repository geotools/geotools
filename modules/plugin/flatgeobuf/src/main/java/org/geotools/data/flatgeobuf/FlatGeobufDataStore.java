/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.wololo.flatgeobuf.geotools.FeatureTypeConversions;
import org.wololo.flatgeobuf.geotools.HeaderMeta;

public class FlatGeobufDataStore extends ContentDataStore implements FileDataStore {

    private static final Logger LOGGER = Logging.getLogger(FlatGeobufDataStore.class);

    final URL url;
    final HeaderMeta headerMeta;
    final String typeName;

    public FlatGeobufDataStore(URL url) throws IOException {
        this.url = url;
        File file;
        String fileName;
        try {
            file = new File(this.url.toURI());
            fileName = removeFileExtension(file.getName(), true);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        FileChannel fileChannel =
                (FileChannel)
                        Files.newByteChannel(file.toPath(), EnumSet.of(StandardOpenOption.READ));
        MappedByteBuffer bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        bb.order(ByteOrder.LITTLE_ENDIAN);
        this.headerMeta = FeatureTypeConversions.deserialize(bb);
        String name = this.headerMeta.name;
        if (name == null || name.isEmpty()) {
            LOGGER.info("No name in header will use file name " + fileName);
            this.typeName = fileName;
        } else {
            LOGGER.info("Using name found in header as typeName " + name);
            this.typeName = name;
        }
    }

    public static String removeFileExtension(String filename, boolean removeAllExtensions) {
        if (filename == null || filename.isEmpty()) return filename;
        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return filename.replaceAll(extPattern, "");
    }

    HeaderMeta readHeaderMeta() throws IOException {
        File file;
        try {
            file = new File(this.url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        FileChannel fileChannel =
                (FileChannel)
                        Files.newByteChannel(file.toPath(), EnumSet.of(StandardOpenOption.READ));
        MappedByteBuffer bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return FeatureTypeConversions.deserialize(bb);
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return Collections.singletonList(getTypeName());
    }

    Name getTypeName() throws IOException {
        return new NameImpl(namespaceURI, typeName);
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return getFeatureSource();
    }

    @Override
    public ContentFeatureSource getFeatureSource() throws IOException {
        ContentEntry entry = ensureEntry(getTypeName());
        return new FlatGeobufFeatureSource(entry, url, headerMeta);
    }

    @Override
    public SimpleFeatureType getSchema() throws IOException {
        return getSchema(getTypeName());
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader() throws IOException {
        return super.getFeatureReader(
                new Query(getTypeName().getLocalPart()), Transaction.AUTO_COMMIT);
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void updateSchema(SimpleFeatureType featureType) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            Filter filter, Transaction transaction) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction)
            throws IOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
            Transaction transaction) throws IOException {
        throw new RuntimeException("Not implemented");
    }
}
