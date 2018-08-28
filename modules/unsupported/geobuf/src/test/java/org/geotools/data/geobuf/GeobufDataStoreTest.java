/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geobuf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.util.URLs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeobufDataStoreTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void readPoints() throws Exception {
        File file =
                URLs.urlToFile(
                        getClass()
                                .getClassLoader()
                                .getResource("org/geotools/data/geobuf/points.pbf"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("points", store.getTypeNames()[0]);
        SimpleFeatureSource featureSource = store.getFeatureSource("points");
        assertEquals(
                "geom:Point,id:Integer,name:String",
                DataUtilities.encodeType(featureSource.getSchema()));
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        assertEquals(2, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "POINT (24.257813 49.61071)", feature.getDefaultGeometry().toString());
                    assertEquals(1, feature.getAttribute("id"));
                    assertEquals("number 1", feature.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "POINT (-92.460937 40.178873)",
                            feature.getDefaultGeometry().toString());
                    assertEquals(2, feature.getAttribute("id"));
                    assertEquals("number 2", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void writePoints() throws Exception {
        File file = temporaryFolder.newFile("points.pbf");
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        // Write
        SimpleFeatureType featureType =
                DataUtilities.createType("test2", "geom:Point,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("points");
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            gf.createPoint(new Coordinate(-8.349609375, 14.349547837185362)),
                            "ABC",
                            1
                        },
                        "location.1");
        SimpleFeature feature2 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            gf.createPoint(new Coordinate(-18.349609375, 24.349547837185362)),
                            "DEF",
                            2
                        },
                        "location.2");
        SimpleFeatureCollection collection =
                DataUtilities.collection(new SimpleFeature[] {feature1, feature2});
        featureStore.addFeatures(collection);

        // Read
        SimpleFeatureCollection featureCollection = featureStore.getFeatures();
        assertEquals(2, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                if (c == 0) {
                    assertEquals("POINT (-8.349609 14.349548)", f.getDefaultGeometry().toString());
                    assertEquals("1", f.getAttribute("id"));
                    assertEquals("ABC", f.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals("POINT (-18.349609 24.349548)", f.getDefaultGeometry().toString());
                    assertEquals("2", f.getAttribute("id"));
                    assertEquals("DEF", f.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void readLineStrings() throws Exception {
        File file =
                URLs.urlToFile(
                        getClass()
                                .getClassLoader()
                                .getResource("org/geotools/data/geobuf/lines.pbf"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("lines", store.getTypeNames()[0]);
        SimpleFeatureSource featureSource = store.getFeatureSource("lines");
        assertEquals(
                "geom:LineString,id:Integer,name:String",
                DataUtilities.encodeType(featureSource.getSchema()));
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        assertEquals(3, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "LINESTRING (94.570313 63.233627, 70.3125 58.263287, 47.460937 57.515823, "
                                    + "24.609374 50.513427)",
                            feature.getDefaultGeometry().toString());
                    assertEquals(1, feature.getAttribute("id"));
                    assertEquals("number 1", feature.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "LINESTRING (-89.296875 40.446947, -61.171875 20.303417, -47.460938 44.339565, "
                                    + "-76.992188 58.447733, -107.226563 50.289339)",
                            feature.getDefaultGeometry().toString());
                    assertEquals(2, feature.getAttribute("id"));
                    assertEquals("number 2", feature.getAttribute("name"));
                } else if (c == 2) {
                    assertEquals(
                            "LINESTRING (-47.460938 -13.923404, 7.382812 8.407168)",
                            feature.getDefaultGeometry().toString());
                    assertEquals(3, feature.getAttribute("id"));
                    assertEquals("number 3`", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void writeLineStrings() throws Exception {
        File file = temporaryFolder.newFile("lines.pbf");
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        // Write
        SimpleFeatureType featureType =
                DataUtilities.createType("lines", "geom:LineString,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("lines");
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            gf.createLineString(
                                    new Coordinate[] {
                                        new Coordinate(0, 0), new Coordinate(10, 10)
                                    }),
                            "ABC",
                            1
                        },
                        "location.1");
        SimpleFeature feature2 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            gf.createLineString(
                                    new Coordinate[] {
                                        new Coordinate(20, 20),
                                        new Coordinate(21, 21),
                                        new Coordinate(23, 23)
                                    }),
                            "DEF",
                            2
                        },
                        "location.2");
        SimpleFeatureCollection collection =
                DataUtilities.collection(new SimpleFeature[] {feature1, feature2});
        featureStore.addFeatures(collection);

        // Read
        SimpleFeatureCollection featureCollection = featureStore.getFeatures();
        assertEquals(2, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                if (c == 0) {
                    assertEquals("LINESTRING (0 0, 10 10)", f.getDefaultGeometry().toString());
                    assertEquals("1", f.getAttribute("id"));
                    assertEquals("ABC", f.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "LINESTRING (20 20, 21 21, 23 23)", f.getDefaultGeometry().toString());
                    assertEquals("2", f.getAttribute("id"));
                    assertEquals("DEF", f.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void readPolygons() throws Exception {
        File file =
                URLs.urlToFile(
                        getClass()
                                .getClassLoader()
                                .getResource("org/geotools/data/geobuf/polygons.pbf"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("polygons", store.getTypeNames()[0]);
        SimpleFeatureSource featureSource = store.getFeatureSource("polygons");
        assertEquals(
                "geom:Polygon,id:Integer,name:String",
                DataUtilities.encodeType(featureSource.getSchema()));
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        assertEquals(4, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "POLYGON ((59.0625 57.704147, 37.617187 24.527135, 98.789062 36.031332, "
                                    + "59.062499 57.704147, 59.0625 57.704147))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(1, feature.getAttribute("id"));
                    assertEquals("name 1", feature.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "POLYGON ((-72.773438 58.077876, -89.296876 17.644022, -37.265626 -2.460181, "
                                    + "-2.109376 42.811522, -15.117189 60.413853, -50.976564 29.840645, -64.687502 39.909737, "
                                    + "-58.71094 56.365251, -72.77344 58.077877, -72.773438 58.077876))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(2, feature.getAttribute("id"));
                    assertEquals("name 2", feature.getAttribute("name"));
                } else if (c == 2) {
                    assertEquals(
                            "POLYGON ((12.65625 63.548552, 12.65625 69.534517, 29.179687 69.534517, "
                                    + "29.179687 63.548552, 12.65625 63.548552))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(3, feature.getAttribute("id"));
                    assertEquals("name 3", feature.getAttribute("name"));
                } else if (c == 3) {
                    assertEquals(
                            "POLYGON ((-22.5 67.875541, -22.5 73.124945, -8.789062 73.124945, "
                                    + "-8.789062 67.875541, -22.5 67.875541))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(4, feature.getAttribute("id"));
                    assertEquals("name 4", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void writePolygons() throws Exception {
        File file = temporaryFolder.newFile("polygons.pbf");
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        // Write
        SimpleFeatureType featureType =
                DataUtilities.createType("lines", "geom:Polygon,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store.getFeatureSource("polygons");
        WKTReader reader = new WKTReader();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "POLYGON ((59.0625 57.704147, 37.617187 24.527135, 98.789062 36.031332, "
                                            + "59.062499 57.704147, 59.0625 57.704147))"),
                            "ABC",
                            1
                        },
                        "location.1");
        SimpleFeature feature2 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "POLYGON ((-72.773438 58.077876, -89.296876 17.644022, -37.265626 -2.460181, "
                                            + "-2.109376 42.811522, -15.117189 60.413853, -50.976564 29.840645, -64.687502 39.909737, "
                                            + "-58.71094 56.365251, -72.77344 58.077877, -72.773438 58.077876))"),
                            "DEF",
                            2
                        },
                        "location.2");
        SimpleFeature feature3 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "POLYGON ((12.65625 63.548552, 12.65625 69.534517, 29.179687 69.534517, "
                                            + "29.179687 63.548552, 12.65625 63.548552))"),
                            "GHI",
                            3
                        },
                        "location.3");
        SimpleFeature feature4 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "POLYGON ((-22.5 67.875541, -22.5 73.124945, -8.789062 73.124945, "
                                            + "-8.789062 67.875541, -22.5 67.875541))"),
                            "JKL",
                            4
                        },
                        "location.4");
        SimpleFeatureCollection collection =
                DataUtilities.collection(
                        new SimpleFeature[] {feature1, feature2, feature3, feature4});
        featureStore.addFeatures(collection);

        // Read
        SimpleFeatureCollection featureCollection = featureStore.getFeatures();
        assertEquals(4, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "POLYGON ((59.0625 57.704147, 37.617187 24.527135, 98.789062 36.031332, "
                                    + "59.062499 57.704147, 59.0625 57.704147))",
                            feature.getDefaultGeometry().toString());
                    assertEquals("1", feature.getAttribute("id"));
                    assertEquals("ABC", feature.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "POLYGON ((-72.773438 58.077876, -89.296876 17.644022, -37.265626 -2.460181, "
                                    + "-2.109376 42.811522, -15.117189 60.413853, -50.976564 29.840645, -64.687502 39.909737, "
                                    + "-58.71094 56.365251, -72.77344 58.077877, -72.773438 58.077876))",
                            feature.getDefaultGeometry().toString());
                    assertEquals("2", feature.getAttribute("id"));
                    assertEquals("DEF", feature.getAttribute("name"));
                } else if (c == 2) {
                    assertEquals(
                            "POLYGON ((12.65625 63.548552, 12.65625 69.534517, 29.179687 69.534517, "
                                    + "29.179687 63.548552, 12.65625 63.548552))",
                            feature.getDefaultGeometry().toString());
                    assertEquals("3", feature.getAttribute("id"));
                    assertEquals("GHI", feature.getAttribute("name"));
                } else if (c == 3) {
                    assertEquals(
                            "POLYGON ((-22.5 67.875541, -22.5 73.124945, -8.789062 73.124945, "
                                    + "-8.789062 67.875541, -22.5 67.875541))",
                            feature.getDefaultGeometry().toString());
                    assertEquals("4", feature.getAttribute("id"));
                    assertEquals("JKL", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void readMultiPoints() throws Exception {
        File file =
                URLs.urlToFile(
                        getClass()
                                .getClassLoader()
                                .getResource("org/geotools/data/geobuf/multipoints.pbf"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("multipoints", store.getTypeNames()[0]);
        SimpleFeatureSource featureSource = store.getFeatureSource("multipoints");
        assertEquals(
                "geom:MultiPoint,id:Integer,name:String",
                DataUtilities.encodeType(featureSource.getSchema()));
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        assertEquals(2, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "MULTIPOINT ((24.257813 49.61071), (-92.460937 40.178873))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(1, feature.getAttribute("id"));
                    assertEquals("number 1", feature.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "MULTIPOINT ((100 0), (101 1))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(2, feature.getAttribute("id"));
                    assertEquals("number 2", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void writeMultiPoints() throws Exception {
        File file = temporaryFolder.newFile("multipoints.pbf");
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        // Write
        SimpleFeatureType featureType =
                DataUtilities.createType("test2", "geom:MultiPoint,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore =
                (SimpleFeatureStore) store.getFeatureSource("multipoints");
        WKTReader reader = new WKTReader();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "MULTIPOINT ((24.257813 49.61071), (-92.460937 40.178873))"),
                            "ABC",
                            9
                        },
                        "location.1");
        SimpleFeature feature2 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {reader.read("MULTIPOINT ((100 0), (101 1))"), "TYU", 56},
                        "location.2");
        SimpleFeatureCollection collection =
                DataUtilities.collection(new SimpleFeature[] {feature1, feature2});
        featureStore.addFeatures(collection);

        // Read
        SimpleFeatureCollection featureCollection = featureStore.getFeatures();
        assertEquals(2, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "MULTIPOINT ((24.257813 49.61071), (-92.460937 40.178873))",
                            feature.getDefaultGeometry().toString());
                    assertEquals("9", feature.getAttribute("id"));
                    assertEquals("ABC", feature.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "MULTIPOINT ((100 0), (101 1))",
                            feature.getDefaultGeometry().toString());
                    assertEquals("56", feature.getAttribute("id"));
                    assertEquals("TYU", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void readMultiLineStrings() throws Exception {
        File file =
                URLs.urlToFile(
                        getClass()
                                .getClassLoader()
                                .getResource("org/geotools/data/geobuf/multilinestrings.pbf"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("multilinestrings", store.getTypeNames()[0]);
        SimpleFeatureSource featureSource = store.getFeatureSource("multilinestrings");
        assertEquals(
                "geom:MultiLineString,id:Integer,name:String",
                DataUtilities.encodeType(featureSource.getSchema()));
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        assertEquals(2, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "MULTILINESTRING ((24.257813 49.61071, 45.12 67.45), "
                                    + "(-92.460937 40.178873, 54.321 65.562))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(1, feature.getAttribute("id"));
                    assertEquals("number 1", feature.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "MULTILINESTRING ((100 0, 101 1), (102 2, 103 3))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(2, feature.getAttribute("id"));
                    assertEquals("number 2", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void writeMultiLineStrings() throws Exception {
        File file = temporaryFolder.newFile("multilinestrings.pbf");
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        // Write
        SimpleFeatureType featureType =
                DataUtilities.createType("test2", "geom:MultiLineString,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore =
                (SimpleFeatureStore) store.getFeatureSource("multilinestrings");
        WKTReader reader = new WKTReader();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "MULTILINESTRING ((24.257813 49.61071, 45.12 67.45), (-92.460937 40.178873, 54.321 65.562))"),
                            "ABC",
                            9
                        },
                        "location.1");
        SimpleFeature feature2 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read("MULTILINESTRING ((100 0, 101 1), (102 2, 103 3))"),
                            "TYU",
                            56
                        },
                        "location.2");
        SimpleFeatureCollection collection =
                DataUtilities.collection(new SimpleFeature[] {feature1, feature2});
        featureStore.addFeatures(collection);

        // Read
        SimpleFeatureCollection featureCollection = featureStore.getFeatures();
        assertEquals(2, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                if (c == 0) {
                    assertEquals(
                            "MULTILINESTRING ((24.257813 49.61071, 45.12 67.45), "
                                    + "(-92.460937 40.178873, 54.321 65.562))",
                            f.getDefaultGeometry().toString());
                    assertEquals("9", f.getAttribute("id"));
                    assertEquals("ABC", f.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "MULTILINESTRING ((100 0, 101 1), (102 2, 103 3))",
                            f.getDefaultGeometry().toString());
                    assertEquals("56", f.getAttribute("id"));
                    assertEquals("TYU", f.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void readMultiPolygons() throws Exception {
        File file =
                URLs.urlToFile(
                        getClass()
                                .getClassLoader()
                                .getResource("org/geotools/data/geobuf/multipolygons.pbf"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("multipolygons", store.getTypeNames()[0]);
        SimpleFeatureSource featureSource = store.getFeatureSource("multipolygons");
        assertEquals(
                "geom:MultiPolygon,id:Integer,name:String",
                DataUtilities.encodeType(featureSource.getSchema()));
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        assertEquals(1, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "MULTIPOLYGON (((102 2, 103 2, 103 3, 102 3, 102 2)), "
                                    + "((100 0, 101 0, 101 1, 100 1, 100 0)), "
                                    + "((100.2 0.2, 100.8 0.2, 100.8 0.8, 100.2 0.8, 100.2 0.2)))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(1, feature.getAttribute("id"));
                    assertEquals("number 1", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void writeMultiPolygons() throws Exception {
        File file = temporaryFolder.newFile("multipolygons.pbf");
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        // Write
        SimpleFeatureType featureType =
                DataUtilities.createType("test2", "geom:MultiPolygon,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore =
                (SimpleFeatureStore) store.getFeatureSource("multipolygons");
        WKTReader reader = new WKTReader();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "MULTIPOLYGON (((102 2, 103 2, 103 3, 102 3, 102 2)), "
                                            + "((102 2, 103 2, 103 3, 102 3, 102 2)), "
                                            + "((102 2, 103 2, 103 3, 102 3, 102 2)))"),
                            "ABC",
                            9
                        },
                        "location.1");
        SimpleFeature feature2 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "MULTIPOLYGON ("
                                            +
                                            // Polygon #1
                                            "((40 40, 20 45, 45 30, 40 40)), "
                                            +
                                            // Polygon #2
                                            "((20 35, 10 30, 10 10, 30 5, 45 20, 20 35), "
                                            + "(30 20, 20 15, 20 25, 30 20)))"),
                            "TYU",
                            56
                        },
                        "location.2");
        SimpleFeature feature3 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)), "
                                            + "((15 5, 40 10, 10 20, 5 10, 15 5)))"),
                            "WER",
                            32
                        },
                        "location.3");

        SimpleFeatureCollection collection =
                DataUtilities.collection(new SimpleFeature[] {feature1, feature2, feature3});
        featureStore.addFeatures(collection);

        // Read
        SimpleFeatureCollection featureCollection = featureStore.getFeatures();
        assertEquals(3, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                if (c == 0) {
                    assertEquals(
                            "MULTIPOLYGON (((102 2, 103 2, 103 3, 102 3, 102 2)), "
                                    + "((102 2, 103 2, 103 3, 102 3, 102 2)), "
                                    + "((102 2, 103 2, 103 3, 102 3, 102 2)))",
                            f.getDefaultGeometry().toString());
                    assertEquals("9", f.getAttribute("id"));
                    assertEquals("ABC", f.getAttribute("name"));
                } else if (c == 1) {
                    assertEquals(
                            "MULTIPOLYGON ("
                                    +
                                    // Polygon #1
                                    "((40 40, 20 45, 45 30, 40 40)), "
                                    +
                                    // Polygon #2
                                    "((20 35, 10 30, 10 10, 30 5, 45 20, 20 35), "
                                    + "(30 20, 20 15, 20 25, 30 20)))",
                            f.getDefaultGeometry().toString());
                    assertEquals("56", f.getAttribute("id"));
                    assertEquals("TYU", f.getAttribute("name"));
                } else if (c == 2) {
                    assertEquals(
                            "MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)), "
                                    + "((15 5, 40 10, 10 20, 5 10, 15 5)))",
                            f.getDefaultGeometry().toString());
                    assertEquals("32", f.getAttribute("id"));
                    assertEquals("WER", f.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void readGeometryCollections() throws Exception {
        File file =
                URLs.urlToFile(
                        getClass()
                                .getClassLoader()
                                .getResource("org/geotools/data/geobuf/geometrycollections.pbf"));
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertEquals(1, store.getTypeNames().length);
        assertEquals("geometrycollections", store.getTypeNames()[0]);
        SimpleFeatureSource featureSource = store.getFeatureSource("geometrycollections");
        assertEquals(
                "geom:GeometryCollection,id:Integer,name:String",
                DataUtilities.encodeType(featureSource.getSchema()));
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        assertEquals(1, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int c = 0;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (c == 0) {
                    assertEquals(
                            "GEOMETRYCOLLECTION (POINT (100 0), LINESTRING (101 0, 102 1))",
                            feature.getDefaultGeometry().toString());
                    assertEquals(1, feature.getAttribute("id"));
                    assertEquals("number 1", feature.getAttribute("name"));
                }
                c++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void writeGeometryCollections() throws Exception {
        File file = temporaryFolder.newFile("geometrycollections.pbf");
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        // Write
        SimpleFeatureType featureType =
                DataUtilities.createType("test2", "geom:GeometryCollection,name:String,id:int");
        store.createSchema(featureType);
        SimpleFeatureStore featureStore =
                (SimpleFeatureStore) store.getFeatureSource("geometrycollections");
        WKTReader reader = new WKTReader();
        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(
                        featureType,
                        new Object[] {
                            reader.read(
                                    "GEOMETRYCOLLECTION (POINT (100 0), LINESTRING (101 0, 102 1))"),
                            "ABC",
                            9
                        },
                        "location.1");
        SimpleFeatureCollection collection =
                DataUtilities.collection(new SimpleFeature[] {feature1});
        featureStore.addFeatures(collection);

        // Read
        SimpleFeatureCollection featureCollection = featureStore.getFeatures();
        assertEquals(1, featureCollection.size());
        SimpleFeatureIterator it = featureCollection.features();
        try {
            int i = 1;
            while (it.hasNext()) {
                SimpleFeature feature = it.next();
                if (i == 1) {
                    assertEquals(
                            "GEOMETRYCOLLECTION (POINT (100 0), LINESTRING (101 0, 102 1))",
                            feature.getDefaultGeometry().toString());
                    assertEquals("9", feature.getAttribute("id"));
                    assertEquals("ABC", feature.getAttribute("name"));
                }
                i++;
            }
        } finally {
            it.close();
        }
        store.dispose();
    }

    @Test
    public void removeSchema() throws Exception {
        File file = temporaryFolder.newFile("points.pbf");
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);
        assertTrue(file.exists());
        store.removeSchema("points");
        assertFalse(file.exists());
    }
}
