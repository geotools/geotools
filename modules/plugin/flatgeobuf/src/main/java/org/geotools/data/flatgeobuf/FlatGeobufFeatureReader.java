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
import java.util.EnumSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.wololo.flatgeobuf.geotools.FeatureCollectionConversions;
import org.wololo.flatgeobuf.geotools.HeaderMeta;

public class FlatGeobufFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    final SimpleFeatureType schema;
    final HeaderMeta headerMeta;

    final FileChannel fileChannel;
    final MappedByteBuffer bb;

    final Iterator<SimpleFeature> it;

    public FlatGeobufFeatureReader(
            SimpleFeatureType schema, URL url, HeaderMeta headerMeta, Envelope bbox)
            throws IOException {
        this.schema = schema;
        this.headerMeta = headerMeta;
        File file;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
        fileChannel =
                (FileChannel)
                        Files.newByteChannel(file.toPath(), EnumSet.of(StandardOpenOption.READ));
        bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        bb.order(ByteOrder.LITTLE_ENDIAN);
        it = FeatureCollectionConversions.deserialize(bb, headerMeta, schema, bbox).iterator();
    }

    public FlatGeobufFeatureReader(SimpleFeatureType schema, URL url, HeaderMeta headerMeta)
            throws IOException {
        this(schema, url, headerMeta, null);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        return it.next();
    }

    @Override
    public boolean hasNext() throws IOException {
        return it.hasNext();
    }

    @Override
    public void close() throws IOException {
        fileChannel.close();
    }
}
