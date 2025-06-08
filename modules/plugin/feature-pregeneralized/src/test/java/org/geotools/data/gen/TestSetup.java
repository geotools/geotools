/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.geotools.TestData;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.FileDataStoreFactorySpi;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.data.DefaultRepository;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.gen.tool.Toolbox;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

public class TestSetup {

    public static DefaultRepository REPOSITORY;

    public static Map<Double, Map<String, Integer>> POINTMAP;

    static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;

        try {

            createShapeFilePyramd();

            REPOSITORY = new DefaultRepository();
            POINTMAP = new HashMap<>();
            POINTMAP.put(0.0, new HashMap<>());
            POINTMAP.put(5.0, new HashMap<>());
            POINTMAP.put(10.0, new HashMap<>());
            POINTMAP.put(20.0, new HashMap<>());
            POINTMAP.put(50.0, new HashMap<>());

            URL url = TestData.url("shapes/streams.shp");

            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(url);

            String typeName = ds.getSchema().getTypeName();
            REPOSITORY.register("dsStreams", ds);

            MemoryDataStore dsStreams_5 = createMemStoreVertical(ds.getSchema(), "dsStreams_5", "streams_5");
            MemoryDataStore dsStreams_10 = createMemStoreVertical(ds.getSchema(), "dsStreams_10", "streams_10");
            MemoryDataStore dsStreams_20 = createMemStoreVertical(ds.getSchema(), "dsStreams_20", "streams_20");
            MemoryDataStore dsStreams_50 = createMemStoreVertical(ds.getSchema(), "dsStreams_50", "streams_50");

            MemoryDataStore dsStreams_5_10 = createMemStoreMixed(ds.getSchema(), "dsStreams_5_10", "streams_5_10");
            MemoryDataStore dsStreams_20_50 = createMemStoreMixed(ds.getSchema(), "dsStreams_20_50", "streams_20_50");

            MemoryDataStore dsStreams_5_10_20_50 =
                    createMemStoreHorizontal(ds.getSchema(), "dsStreams_5_10_20_50", "streams_5_10_20_50");

            // StreamsFeatureSource = ds.getFeatureSource(typeName);
            Query query = new Query(typeName, Filter.INCLUDE);
            try (Transaction t = new DefaultTransaction();
                    FeatureReader<SimpleFeatureType, SimpleFeature> reader = ds.getFeatureReader(query, t)) {
                while (reader.hasNext()) {

                    SimpleFeature stream = reader.next();

                    POINTMAP.get(0.0).put(stream.getID(), ((Geometry) stream.getDefaultGeometry()).getNumPoints());

                    addGeneralizedFeatureVertical(stream, dsStreams_5, 5.0);
                    addGeneralizedFeatureVertical(stream, dsStreams_10, 10.0);
                    addGeneralizedFeatureVertical(stream, dsStreams_20, 20.0);
                    addGeneralizedFeatureVertical(stream, dsStreams_50, 50.0);

                    addGeneralizedFeatureMixed(stream, dsStreams_5_10, 5.0, 10.0);
                    addGeneralizedFeatureMixed(stream, dsStreams_20_50, 20.0, 50.0);
                    addGeneralizedFeatureHorizontal(stream, dsStreams_5_10_20_50);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        initialized = true;
    }

    private static MemoryDataStore createMemStoreVertical(SimpleFeatureType typ, String name, String fname)
            throws IOException {
        MemoryDataStore memDS = new MemoryDataStore();

        List<AttributeDescriptor> attrs = new ArrayList<>();
        attrs.addAll(typ.getAttributeDescriptors());
        // have a non linear mapping between source attributes and target attributes
        Collections.shuffle(attrs, new Random(1));

        SimpleFeatureTypeImpl sft = new SimpleFeatureTypeImpl(
                new NameImpl(fname),
                attrs,
                typ.getGeometryDescriptor(),
                typ.isAbstract(),
                typ.getRestrictions(),
                typ.getSuper(),
                typ.getDescription());

        memDS.createSchema(sft);
        REPOSITORY.register(name, memDS);
        return memDS;
    }

    private static MemoryDataStore createMemStoreMixed(SimpleFeatureType typ, String name, String fname)
            throws IOException {
        MemoryDataStore memDS = new MemoryDataStore();
        List<AttributeDescriptor> attrs = new ArrayList<>();
        attrs.addAll(typ.getAttributeDescriptors());
        // have a non linear mapping between source attributes and target attributes
        Collections.shuffle(attrs, new Random(1));

        GeometryDescriptorImpl geom2Descr = new GeometryDescriptorImpl(
                typ.getGeometryDescriptor().getType(),
                new NameImpl("the_geom2"),
                typ.getGeometryDescriptor().getMinOccurs(),
                typ.getGeometryDescriptor().getMaxOccurs(),
                typ.getGeometryDescriptor().isNillable(),
                typ.getGeometryDescriptor().getDefaultValue());
        attrs.add(geom2Descr);
        SimpleFeatureTypeImpl sft = new SimpleFeatureTypeImpl(
                new NameImpl(fname),
                attrs,
                typ.getGeometryDescriptor(),
                typ.isAbstract(),
                typ.getRestrictions(),
                typ.getSuper(),
                typ.getDescription());

        memDS.createSchema(sft);
        REPOSITORY.register(name, memDS);
        return memDS;
    }

    private static MemoryDataStore createMemStoreHorizontal(SimpleFeatureType typ, String name, String fname)
            throws IOException {
        MemoryDataStore memDS = new MemoryDataStore();

        List<AttributeDescriptor> attrs = new ArrayList<>();
        attrs.addAll(typ.getAttributeDescriptors());
        // have a non linear mapping between source attributes and target attributes
        Collections.shuffle(attrs, new Random(1));

        GeometryDescriptorImpl geom2Descr = new GeometryDescriptorImpl(
                typ.getGeometryDescriptor().getType(),
                new NameImpl("the_geom5"),
                typ.getGeometryDescriptor().getMinOccurs(),
                typ.getGeometryDescriptor().getMaxOccurs(),
                typ.getGeometryDescriptor().isNillable(),
                typ.getGeometryDescriptor().getDefaultValue());
        attrs.add(geom2Descr);
        geom2Descr = new GeometryDescriptorImpl(
                typ.getGeometryDescriptor().getType(),
                new NameImpl("the_geom10"),
                typ.getGeometryDescriptor().getMinOccurs(),
                typ.getGeometryDescriptor().getMaxOccurs(),
                typ.getGeometryDescriptor().isNillable(),
                typ.getGeometryDescriptor().getDefaultValue());
        attrs.add(geom2Descr);
        geom2Descr = new GeometryDescriptorImpl(
                typ.getGeometryDescriptor().getType(),
                new NameImpl("the_geom20"),
                typ.getGeometryDescriptor().getMinOccurs(),
                typ.getGeometryDescriptor().getMaxOccurs(),
                typ.getGeometryDescriptor().isNillable(),
                typ.getGeometryDescriptor().getDefaultValue());
        attrs.add(geom2Descr);

        geom2Descr = new GeometryDescriptorImpl(
                typ.getGeometryDescriptor().getType(),
                new NameImpl("the_geom50"),
                typ.getGeometryDescriptor().getMinOccurs(),
                typ.getGeometryDescriptor().getMaxOccurs(),
                typ.getGeometryDescriptor().isNillable(),
                typ.getGeometryDescriptor().getDefaultValue());
        attrs.add(geom2Descr);

        SimpleFeatureTypeImpl sft = new SimpleFeatureTypeImpl(
                new NameImpl(fname),
                attrs,
                typ.getGeometryDescriptor(),
                typ.isAbstract(),
                typ.getRestrictions(),
                typ.getSuper(),
                typ.getDescription());

        memDS.createSchema(sft);
        REPOSITORY.register(name, memDS);
        return memDS;
    }

    private static void addGeneralizedFeatureVertical(SimpleFeature feature, MemoryDataStore memDS, double distance)
            throws IOException {
        Geometry geomNew = TopologyPreservingSimplifier.simplify((Geometry) feature.getDefaultGeometry(), distance);
        SimpleFeature feature_gen = SimpleFeatureBuilder.deep(feature);
        feature_gen.setDefaultGeometry(geomNew);
        memDS.addFeature(feature_gen);
        POINTMAP.get(distance).put(feature_gen.getID(), geomNew.getNumPoints());
    }

    private static void addGeneralizedFeatureMixed(
            SimpleFeature feature, MemoryDataStore memDS, double distance1, double distance2) throws IOException {
        SimpleFeature feature_gen2 =
                SimpleFeatureBuilder.template(memDS.getSchema(memDS.getTypeNames()[0]), feature.getID());
        feature_gen2.setAttribute("CAT_ID", feature.getAttribute("CAT_ID"));
        Geometry geomNew = TopologyPreservingSimplifier.simplify((Geometry) feature.getDefaultGeometry(), distance1);
        feature_gen2.setAttribute("the_geom", geomNew);
        geomNew = TopologyPreservingSimplifier.simplify((Geometry) feature.getDefaultGeometry(), distance2);
        feature_gen2.setAttribute("the_geom2", geomNew);
        memDS.addFeature(feature_gen2);
    }

    private static void addGeneralizedFeatureHorizontal(SimpleFeature feature, MemoryDataStore memDS)
            throws IOException {
        SimpleFeature feature_gen2 =
                SimpleFeatureBuilder.template(memDS.getSchema(memDS.getTypeNames()[0]), feature.getID());
        feature_gen2.setAttribute("CAT_ID", feature.getAttribute("CAT_ID"));

        feature_gen2.setAttribute("the_geom", feature.getDefaultGeometry());
        Geometry geomNew = TopologyPreservingSimplifier.simplify((Geometry) feature.getDefaultGeometry(), 5);
        feature_gen2.setAttribute("the_geom5", geomNew);
        geomNew = TopologyPreservingSimplifier.simplify((Geometry) feature.getDefaultGeometry(), 10);
        feature_gen2.setAttribute("the_geom10", geomNew);
        geomNew = TopologyPreservingSimplifier.simplify((Geometry) feature.getDefaultGeometry(), 20);
        feature_gen2.setAttribute("the_geom20", geomNew);
        geomNew = TopologyPreservingSimplifier.simplify((Geometry) feature.getDefaultGeometry(), 50);
        feature_gen2.setAttribute("the_geom50", geomNew);
        memDS.addFeature(feature_gen2);

        memDS.addFeature(feature_gen2);
    }

    private static void createShapeFilePyramd() throws IOException {

        File baseDir = new File("target" + File.separator + "0");
        if (baseDir.exists() == false) baseDir.mkdir();
        else return; // already done

        // ///////// create property file for streams
        String propFileName = "target" + File.separator + "0" + File.separator + "streams.properties";
        File propFile = new File(propFileName);
        try (FileOutputStream out = new FileOutputStream(propFile)) {
            String line = ShapefileDataStoreFactory.URLP.key + "=" + "file:target/0/streams.shp\n";
            out.write(line.getBytes());
        }
        // ////////

        URL url = TestData.url("shapes/streams.shp");

        ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(url);

        Map<String, Serializable> params = new HashMap<>();
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
        params.put(
                ShapefileDataStoreFactory.URLP.key,
                new File("target/0/streams.shp").toURI().toURL());
        ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);

        SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);

        ds.createSchema(fs.getSchema());
        ds.forceSchemaCRS(fs.getSchema().getCoordinateReferenceSystem());
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                        ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
                SimpleFeatureIterator it = fs.getFeatures().features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                SimpleFeature fNew = writer.next();
                fNew.setAttributes(f.getAttributes());
                writer.write();
            }
        }
        ds.dispose();
        shapeDS.dispose();

        Toolbox tb = new Toolbox();
        tb.parse(new String[] {
            "generalize",
            "target" + File.separator + "0" + File.separator + "streams.shp",
            "target",
            "5.0,10.0,20.0,50.0"
        });
    }
}
