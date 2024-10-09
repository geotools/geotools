package org.geotools.data.flatgeobuf;

import com.google.flatbuffers.FlatBufferBuilder;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.wololo.flatgeobuf.Constants;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.NodeItem;
import org.wololo.flatgeobuf.PackedRTree;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class Conversion {

    public static void serialize(
            SimpleFeatureCollection featureCollection,
            OutputStream outputStream)
            throws IOException {
        SimpleFeatureType featureType = featureCollection.getSchema();
        long featuresCount = featureCollection.size();
        FlatBufferBuilder builder = FlatBuffers.newBuilder(16 * 1024);
        try {
            // wirte header
            HeaderMeta headerMeta = HeaderMetaUtil.fromFeatureType(featureType, featuresCount);
            headerMeta.indexNodeSize = 16;     //
            outputStream.write(Constants.MAGIC_BYTES);
            HeaderMeta.write(headerMeta, outputStream, builder);
            builder.clear();
            // hilbertSort
            List<MyItem> sortItems = hilbertSort(featureCollection, outputStream, headerMeta);
            // write R-tree index
            PackedRTree packedRTree = new PackedRTree(sortItems, (short) 16);
            packedRTree.write(outputStream);
            builder.clear();
            // write featrues
            for (MyItem item : sortItems) {
                SimpleFeature feature = item.feature;
                builder.addInt((int)item.size);
                FeatureConversions.serialize(feature, headerMeta, outputStream, builder);
                builder.clear();
            }
        } finally {
            FlatBuffers.release(builder);
        }
    }

    public static class MyItem extends PackedRTree.FeatureItem {
        public SimpleFeature feature;
    }

    private static List<MyItem> hilbertSort(SimpleFeatureCollection featureCollection, OutputStream outputStream, HeaderMeta headerMeta) throws IOException {
        NodeItem extend = new NodeItem(0);          // init a empty total boundary item
        List<MyItem> nodeItemList = new LinkedList<>();   // wrap all features info
        SimpleFeatureIterator features = featureCollection.features();
        long offset = 0;    // Offset is the referenced feature's byte offset into the data section
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            Geometry geo = (Geometry) feature.getDefaultGeometry();
            Envelope env = geo.getEnvelopeInternal();
            NodeItem nodeItem = new NodeItem(env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), 0);
            MyItem featureItem = new MyItem();
            featureItem.nodeItem = nodeItem;
            featureItem.size = FeatureConversions.calcSerializeSize(feature, headerMeta);
            offset += featureItem.size + 4;   // feature head size(4bit)
            featureItem.offset = offset;
            featureItem.feature = feature;
            nodeItemList.add(featureItem);
        }
        nodeItemList.forEach(x->extend.expand(x.nodeItem));

        PackedRTree.hilbertSort(nodeItemList, extend);
        return nodeItemList;
    }
}
