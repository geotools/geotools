/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    Created on May 11, 2005, 9:02 PM
 */
package org.geotools.filter.function;

import junit.framework.TestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

/**
 * @author James
 * @source $URL$
 *     http://svn.osgeo.org/geotools/trunk/modules/library/main/src/test/java/org/geotools/
 *     filter/function/FunctionTestSupport.java $
 */
public abstract class FunctionTestSupport extends TestCase {

    protected SimpleFeatureCollection featureCollection, jenksCollection;

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    protected SimpleFeatureType dataType;

    protected SimpleFeature[] testFeatures;

    /** Creates a new instance of FunctionTestSupport */
    public FunctionTestSupport(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
        dataType =
                DataUtilities.createType(
                        "classification.test1", "id:0,foo:int,bar:double,geom:Point,group:String");

        int iVal[] = new int[] {4, 90, 20, 43, 29, 61, 8, 12};
        double dVal[] = new double[] {2.5, 80.433, 24.5, 9.75, 18, 53, 43.2, 16};
        testFeatures = new SimpleFeature[iVal.length];

        featureCollection = new ListFeatureCollection(dataType);
        GeometryFactory fac = new GeometryFactory();

        for (int i = 0; i < iVal.length; i++) {
            SimpleFeature feature =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {
                                new Integer(i + 1),
                                new Integer(iVal[i]),
                                new Double(dVal[i]),
                                fac.createPoint(new Coordinate(iVal[i], iVal[i])),
                                "Group" + (i % 4)
                            },
                            "classification.t" + (i + 1));
            testFeatures[i] = feature;
            ((ListFeatureCollection) featureCollection).add(feature);
        }
        double[] jenks71 = {
            50.12, 83.9, 76.43, 71.61, 79.66, 84.84, 87.87, 92.45, 119.9, 155.3, 131.5, 111.8,
            96.78, 86.75, 62.41, 96.37, 75.51, 77.29, 85.41, 116.4, 58.5, 75.29, 66.32, 62.65,
            80.45, 72.76, 63.67, 60.27, 68.45, 100.1, 55.3, 54.07, 57.49, 73.52, 68.25, 64.28,
            50.64, 52.47, 68.19, 57.4, 39.72, 60.66, 57.59, 38.22, 57.22, 67.04, 47.29, 71.05,
            50.53, 34.63, 59.65, 62.06, 52.89, 56.35, 57.26, 53.77, 59.89, 55.44, 45.4, 52.21,
            49.38, 51.15, 54.27, 54.32, 41.2, 34.58, 50.11, 52.05, 33.82, 39.88, 36.24, 41.02,
            46.13, 51.15, 32.28, 33.26, 31.78, 31.28, 50.52, 47.21, 32.69, 38.3, 33.83, 40.3, 40.62,
            32.14, 31.66, 26.09, 39.84, 24.83, 28.2, 31.19, 37.57, 27.16, 23.42, 18.57, 30.97,
            17.82, 15.57, 15.93, 28.71, 32.22
        };
        SimpleFeatureType jenksType = DataUtilities.createType("jenks71", "id:0,jenks71:double");
        ListFeatureCollection features = new ListFeatureCollection(jenksType);
        for (int i = 0; i < jenks71.length; i++) {
            features.add(
                    SimpleFeatureBuilder.build(
                            jenksType,
                            new Object[] {new Integer(i + 1), new Double(jenks71[i])},
                            "jenks" + i));
        }
        jenksCollection = features;
    }
}
