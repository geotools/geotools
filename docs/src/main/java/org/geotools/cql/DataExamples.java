/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

/** */
package org.geotools.cql;

import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.commons.io.FileUtils;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.util.URLs;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * This utility class provide the data required by the CQL/ECQL examples.
 *
 * @author Mauricio Pazos
 */
final class DataExamples extends ECQLExamples {

    private static SimpleFeature COUNTRY = null;

    private static SimpleFeature CITY = null;

    private DataExamples() {
        // utility class
    }

    /**
     * Creates a feature that represent New York city
     *
     * @return a Feature
     */
    public static SimpleFeature getInstanceOfCity() throws Exception {

        if (CITY != null) {
            return CITY;
        }

        final SimpleFeatureType type =
                DataUtilities.createType(
                        "Location",
                        "geometry:Point:srid=4326,"
                                + "cityName:String,"
                                + "over65YearsOld:Double,"
                                + "under18YearsOld:Double,"
                                + "population:Integer,"
                                + "lastEarthQuake:Date");
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        Point point = geometryFactory.createPoint(new Coordinate(-17.2053, 11.9517));

        featureBuilder.add(point);
        featureBuilder.add("New York");
        featureBuilder.add(22.6);
        featureBuilder.add(13.4);
        featureBuilder.add(19541453);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        featureBuilder.add(dateFormatter.parse("1737-11-30T01:30:00Z"));

        CITY = featureBuilder.buildFeature(null);

        return CITY;
    }

    public static SimpleFeature getInstanceOfCountry() throws Exception {

        if (COUNTRY != null) {
            return COUNTRY;
        }
        final SimpleFeatureType type =
                DataUtilities.createType(
                        "Location",
                        "geometry:Polygon:srid=4326,"
                                + "countryName:String,"
                                + "population:Integer,"
                                + "principalMineralResource:String");
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        WKTReader reader = new WKTReader();
        MultiPolygon geometry = (MultiPolygon) reader.read(usaGeometry());

        featureBuilder.add(geometry);
        featureBuilder.add("USA");
        featureBuilder.add(307006550);
        featureBuilder.add("oil");

        COUNTRY = featureBuilder.buildFeature(null);

        return COUNTRY;
    }

    private static String usaGeometry() {
        try {
            return FileUtils.readFileToString(
                    URLs.urlToFile(DataExamples.class.getResource("usa-geometry.wkt")), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Show USA geometry can be loaded and used to make a SimpleFeature. */
    public static void main(String[] args) {
        try {
            System.out.println(getInstanceOfCountry());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
