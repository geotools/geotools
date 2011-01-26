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
import java.io.InputStream;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Properties;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.text.WKTParser;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.opengis.geometry.Boundary;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.xml.sax.InputSource;

/**
 * This TestCase picks up the file with the same name as the test method, a JTS test, and applies it to the provided
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
public class GeometryConformanceTestCases extends TestCase {

	TestResult result;
	@Override
	public void run(TestResult result) {
		this.result = result;
		super.run(result);
		result = null;
	}

	public void testLineTests() throws Exception {
		perform();
	}
	public void testPolygonTests() throws Exception {
		perform();
	}
	
	public void testPolygonWithHoleTests() throws Exception {
		perform();
	}
	public void testSimplePolygonTest() throws Exception {
		perform();
	}
	public void testTestBoundary() throws Exception {
		perform();
	}
	public void testTestBuffer() throws Exception {
		perform();
	}
//	public void testTestCentroid() throws Exception {
//		perform();
//	}
	public void testTestConvexHullbig() throws Exception {
		setName("testTestConvexHull-big");
		perform();
	}
	public void testTestConvexHull() throws Exception {
		perform();
	}
	public void testTestDisjoint() throws Exception {
		perform();
	}
	public void testTestFunctionAA() throws Exception {
		perform();
	}
	public void testTestFunctionAAPrec() throws Exception {
		perform();
	}
	public void testTestFunctionLA() throws Exception {
		perform();
	}
	public void testTestFunctionLAPrec() throws Exception {
		perform();
	}
	public void testTestFunctionLL() throws Exception {
		perform();
	}
	public void testTestFunctionLLPrec() throws Exception {
		perform();
	}
	public void testTestFunctionPA() throws Exception {
		perform();
	}
	public void testTestFunctionPL() throws Exception {
		perform();
	}
	public void testTestFunctionPLPrec() throws Exception {
		perform();
	}
	public void testTestFunctionPP() throws Exception {
		perform();
	}
	public void testTestInteriorPoint() throws Exception {
		perform();
	}
	public void testTestRectanglePredicate() throws Exception {
		perform();
	}
	public void testTestRelateAA_general() throws Exception {
		perform();
	}
	public void testTestRelateAA() throws Exception {
		perform();
	}


	public void testTestRelatePA() throws Exception {
		perform();
	}
	
	public void testTestRelatePA_general() throws Exception {
		perform();
	}
	public void testTestRelatePL() throws Exception {
		perform();
	}
	public void testTestRelatePL_general() throws Exception {
		perform();
}
	public void testTestRelatePP() throws Exception {
		perform();
	}
	public void testTestRelatePP_general() throws Exception {
		perform();
}
	
// These tests aren't applicable	
//	public void testTestSimple() throws Exception {
//		perform();
//	}
//	public void testTestValid() throws Exception {
//		perform();
//	}
//	public void testTestValid2big() throws Exception {
//		setName("testTestValid2-big");
//		perform();
//	}
//	public void testTestValid2() throws Exception {
//		perform();
//	}
	
	public void testTestWithinDistance() throws Exception {
		perform();
	}
	
	
    private void perform() throws Exception {   	
		String name = getName();
		name = name.substring( 4 )+".xml";
		
		File file = TestData.file(GeometryConformanceTestCases.class, name );		
		//assertTrue( name+" exists", file.exists() );
		
		GeometryTestParser parser = new GeometryTestParser();
		Properties excludes = findExclusions(file);
		
		if( isAllExcluded(excludes) ) return;

		InputStream inputStream = file.toURI().toURL().openStream();
		InputSource inputSource = new InputSource(inputStream);
        GeometryTestContainer container = parser.parseTestDefinition(inputSource);
        container.checkTestOverrides(name, excludes);                      
        
        TestResult tempResult = new TestResult();
        container.runAllTestCases( tempResult );

        // TODO: grab the error and print
        assertTrue( "failures: "+tempResult.failureCount(), tempResult.wasSuccessful() ); 
        Enumeration enums = tempResult.failures();
        while (enums.hasMoreElements()) {
        	System.out.println("what");
        	AssertionFailedError failure = (AssertionFailedError) enums.nextElement();
        	assertTrue("--"+failure.toString(), tempResult.wasSuccessful());
        }
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
                        	MultiPrimitive curves = (MultiPrimitive) op.getExpectedResult();
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
