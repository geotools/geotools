/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 * 	  (c) 2015 Open Source Geospatial Foundation - all rights reserved
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
package org.geotools.data.csv.parse;

import static org.junit.Assert.assertEquals;

import org.geotools.data.csv.CSVFileState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTReader2;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * This test case is only focused on testing the individual strategies in isolation.
 *
 * @author travis
 */
public class CSVWriteStrategyTest {
    // docs start attributes
    @Test
    public void Attributes() throws Exception {
        CSVFileState fileState = new CSVFileState("CITY, NUMBER, YEAR", "TEST");
        CSVStrategy strategy = new CSVAttributesOnlyStrategy(fileState);

        SimpleFeatureType featureType = strategy.buildFeatureType();
        assertEquals("TEST", featureType.getName().getLocalPart());
        assertEquals(3, featureType.getAttributeCount());

        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        featureType, new Object[] {"Trento", 140, 2002}, "TEST-fid1");
        String[] csvRecord = new String[] {"Trento", "140", "2002"};
        SimpleFeature parsed = strategy.decode("fid1", csvRecord);
        assertEquals(feature, parsed);

        String[] record = strategy.encode(feature);
        assertEquals(csvRecord.length, record.length);
        if (csvRecord.length == record.length) {
            for (int i = 0; i < csvRecord.length; i++) {
                assertEquals(csvRecord[i], record[i]);
            }
        }
    }
    // docs end attributes

    // docs start latlon
    @Test
    public void LatLon() throws Exception {
        CSVFileState fileState = new CSVFileState("LAT, LON, CITY, NUMBER, YEAR", "TEST");
        CSVStrategy strategy = new CSVLatLonStrategy(fileState);

        SimpleFeatureType featureType = strategy.buildFeatureType();
        assertEquals("TEST", featureType.getName().getLocalPart());
        // 4 because LAT/LON should be stored internally as POINT
        assertEquals(4, featureType.getAttributeCount());

        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Point trento = gf.createPoint(new Coordinate(46.066667, 11.116667));
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        featureType, new Object[] {trento, "Trento", 140, 2002}, "TEST-fid1");
        String[] csvRecord = new String[] {"46.066667", "11.116667", "Trento", "140", "2002"};
        SimpleFeature parsed = strategy.decode("fid1", csvRecord);
        assertEquals(feature, parsed);

        String[] record = strategy.encode(feature);
        assertEquals(csvRecord.length, record.length);
        if (csvRecord.length == record.length) {
            for (int i = 0; i < csvRecord.length; i++) {
                assertEquals(csvRecord[i], record[i]);
            }
        }
    }
    // docs end latlon

    // docs start SpecifiedLatLon
    @Test
    public void SpecifiedLatLon() throws Exception {
        CSVFileState fileState = new CSVFileState("TAL, NOL, CITY, NUMBER, YEAR", "TEST");
        CSVStrategy strategy = new CSVLatLonStrategy(fileState, "TAL", "NOL");

        SimpleFeatureType featureType = strategy.buildFeatureType();
        assertEquals("TEST", featureType.getName().getLocalPart());
        // 4 because LAT/LON should be stored internally as POINT
        assertEquals(4, featureType.getAttributeCount());

        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        Point trento = gf.createPoint(new Coordinate(46.066667, 11.116667));
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        featureType, new Object[] {trento, "Trento", 140, 2002}, "TEST-fid1");
        String[] csvRecord = new String[] {"46.066667", "11.116667", "Trento", "140", "2002"};
        SimpleFeature parsed = strategy.decode("fid1", csvRecord);
        assertEquals(feature, parsed);

        String[] record = strategy.encode(feature);
        assertEquals(csvRecord.length, record.length);
        if (csvRecord.length == record.length) {
            for (int i = 0; i < csvRecord.length; i++) {
                assertEquals(csvRecord[i], record[i]);
            }
        }
    }
    // docs end SpecifiedLatLon

    @Test
    public void WKT() throws Exception {
        CSVFileState fileState = new CSVFileState("POINT, CITY, NUMBER, YEAR", "TEST");
        CSVStrategy strategy = new CSVSpecifiedWKTStrategy(fileState, "POINT");

        SimpleFeatureType featureType = strategy.buildFeatureType();
        assertEquals("TEST", featureType.getName().getLocalPart());
        assertEquals(4, featureType.getAttributeCount());

        WKTReader2 wktReader = new WKTReader2();
        Geometry geom = wktReader.read("POINT (1 1)");
        SimpleFeature feature =
                SimpleFeatureBuilder.build(
                        featureType, new Object[] {geom, "Trento", 140, 2002}, "TEST-fid1");
        String[] csvRecord = new String[] {"POINT (1 1)", "Trento", "140", "2002"};
        SimpleFeature parsed = strategy.decode("fid1", csvRecord);
        assertEquals(feature, parsed);

        String[] record = strategy.encode(feature);
        assertEquals(csvRecord.length, record.length);
        if (csvRecord.length == record.length) {
            for (int i = 0; i < csvRecord.length; i++) {
                assertEquals(csvRecord[i], record[i]);
            }
        }
    }
}
