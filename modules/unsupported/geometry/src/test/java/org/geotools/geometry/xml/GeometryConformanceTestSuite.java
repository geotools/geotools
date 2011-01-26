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
package org.geotools.geometry.xml;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.text.WKTParser;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.opengis.geometry.Boundary;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.xml.sax.InputSource;

/**
 * This TestSuite picks up each JTS test and applies it to the provided
 * Geometry*Factory.
 * 
 * If an accompanying .properties file is found, the following entries may
 * apply:
 *  - disabled=true: eliminates all tests in the xml file
 *  - description=skipped: skips the "case" entry with that description
 *  - description=intersection|skipped: skips the intersection operation
 *  for that description.
 *  - description=intersection|WKT TEXT: replaces the expectedResult value
 *  with the WKT text.
 *  - description=intersection|boundary: for the case of multiprimitives, we need 
 *  to convert the resulting geometry to a boundary to compare with
 *  - description=intersection|point: for the case of points, we need to convert
 *  the resulting geometry to a point instead of a position to compare with
 *  - description=intersection|skipped|union|skipped: skips both the 
 * intersection and union operations.
 * 
 * notes:
 * - spaces must be replaced by "_" in description
 * - "No_description" is the default
 *
 * @source $URL$
 */
public class GeometryConformanceTestSuite extends TestSuite {

    public static Test suite() {
        GeometryTestParser parser = new GeometryTestParser();

        GeometryConformanceTestSuite suite = new GeometryConformanceTestSuite();

        File dir;
        try {
            dir = TestData.file(GeometryConformanceTestSuite.class, "LineTests.xml")
                    .getParentFile();

            File tests[] = dir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.toString().endsWith(".xml");
                }
            });
            for (int i = 0; i < tests.length; i++) {
                File testFile = tests[i];
                Properties excludes = findExclusions(testFile);
                System.out.println("file: "+testFile.getName());
                if (!isAllExcluded(excludes)) {
                    InputStream inputStream = testFile.toURI().toURL().openStream();
                    try {
                        InputSource inputSource = new InputSource(inputStream);
                        GeometryTestContainer container = parser
                                .parseTestDefinition(inputSource);
                        
                        container.addToTestSuite(testFile.getName(), suite, excludes);
                    } catch (Exception eek){
                        //eek.printStackTrace();
                    } finally {
                        inputStream.close();
                    }
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return suite;
    }
    
    private static Properties findExclusions(File xmlFile) {
        try {
            String excludesPath = xmlFile.getPath();
            excludesPath = excludesPath.substring(0, excludesPath.length()-3);
            excludesPath = excludesPath.concat("properties");
            File excludesFile = new File(excludesPath);
            if (excludesFile.exists()) {
                Properties excludes = new Properties();
                InputStream inputStream = excludesFile.toURI().toURL().openStream();
                excludes.load(inputStream);
                return excludes;
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    private static boolean isAllExcluded(Properties prop) {
        if (prop != null && prop.containsKey("disabled")) {
            if (prop.getProperty("disabled").equalsIgnoreCase("true")) {
                return true;    
            }
        }
        return false;    
    }
    
    public static boolean hasExclusion(Properties prop, String testName) {
        if (prop != null && prop.containsKey(testName)) {
            return true;
        }
        return false;
    }
    
    public static boolean isExcluded(Properties prop, String testName) {
        String key = testName.replaceAll(" ", "_");
        if (hasExclusion(prop, key)) {
            if (prop.getProperty(key).equalsIgnoreCase("skipped")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Scans the operations in the testcase and removes or replaces the entries
     * as specified in the excludes property.
     * 
     * @param testCase
     * @param excludes
     * @return
     */
    public static GeometryTestCase overrideOps(GeometryTestCase testCase, Properties excludes) {
        String test = testCase.getDescription().replaceAll(" ", "_");
        if (excludes != null && excludes.containsKey(test)) {
            String value = excludes.getProperty(test);
            if (value.contains("|")) {
                String[] strings = value.split("\\|");
                for (int i = 0; i < strings.length; i++) {
                    String operationName = strings[i];
                    String operationValue = strings[++i];
                    GeometryTestOperation op = testCase.findTestOperation(operationName);
                    if (op != null) {
                        testCase.removeTestOperation(op);
                        //check for override, rather than just remove
                        if (operationValue.equalsIgnoreCase("skipped")) {
                        	continue;
                        }
                        else if (operationValue.equalsIgnoreCase("boundary")) {
                        	// post process into a surface boundary
                        	GeometryImpl curves = (GeometryImpl) op.getExpectedResult();
                        	//MultiPrimitive curves = (MultiPrimitive) op.getExpectedResult();
                        	Boundary boundary = curves.getBoundary();
                        	op.setExpectedResult( boundary );
                        }
                        else if (operationValue.equalsIgnoreCase("point")) {
                        	// post obj into a point
                        	PointImpl point = (PointImpl) op.getExpectedResult();
                        	op.setExpectedResult( point );
                        }
                        else { // try parsing this thing as WKT
                        	GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
                            GeometryFactory geomFact = builder.getGeometryFactory();
                            PrimitiveFactory primFact = builder.getPrimitiveFactory();
                            WKTParser wktFactory = new WKTParser(geomFact, primFact, null, builder.getAggregateFactory());
                            try {
                                Object expectedResult = wktFactory.parse(operationValue);
                                op.setExpectedResult(expectedResult);
                                testCase.addTestOperation(op);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return testCase;
    }
}
