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
 */
package org.geotools.geometry.xml;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import junit.framework.AssertionFailedError;
import junit.framework.Protectable;
import junit.framework.Test;
import junit.framework.TestResult;

import org.opengis.geometry.Geometry;

/**
 * This class represents the part of the JTS test XML file
 * that is wrapped with the "case" tags. It contains two
 * geometry objects and then one or more tests to apply
 * to those geometries
 * @author <a href="mailto:joel@lggi.com">Joel Skelton</a>
 *
 * @source $URL$
 */
public class GeometryTestCase implements Test {
    private String name;
    private static final Logger LOG = org.geotools.util.logging.Logging.getLogger("org.geotools.geometry");
    private List<GeometryTestOperation> operationList;
    private Geometry geomA;
    private Geometry geomB;
    private String description;

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * Constructor
     */
    public GeometryTestCase() {
        this.operationList = new ArrayList<GeometryTestOperation>();
        this.geomA = null;
        this.geomB = null;
        description = "No description";        
    }

    /**
     * Sets the geometry specified by the A tag
     * @param a
     */
    public void setGeometryA(Geometry a) {
        geomA = a;
    }

    /**
     * Sets the geometry specified by the b tag
     * @param b
     */
    public void setGeometryB(Geometry b) {
        geomB = b;
    }

    /**
     * Adds in a test operation that will be run on the given
     * A and B geometries.
     * @param op
     */
    public void addTestOperation(GeometryTestOperation op) {
        operationList.add(op);
    }
    
    public void removeTestOperation(GeometryTestOperation op) {
        operationList.remove(op);
    }
    
    public int getOperationCount() {
        return operationList.size();
    }
    
    public GeometryTestOperation findTestOperation(String name) {
        Iterator<GeometryTestOperation> operations = operationList.iterator();
        while (operations.hasNext()) {
            GeometryTestOperation operation = operations.next();
            if (name.equalsIgnoreCase(operation.getOperation())) {
                return operation;
            }
        }
        return null;
    }

    /**
     * Returns the current description text
     * 
     * @return String description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description text string for this test case. The
     * description is used for logging results.
     * @param desc
     */
    public void setDescription(String desc) {
        description = desc;
    }

    /**
     * Run any test operations stored for this test case
     * @param result2 
     * @return result
     */
    public boolean runTestCases(TestResult result2) {
        boolean result = true;
        result2.startTest( this );
        //LOG.info("Running test:" + description);
        for (GeometryTestOperation op : operationList) {
            if (!op.run(geomA, geomB)) {
                LOG.severe(this.toString() + " - " + op.toString() + " actual result: " + op.getActualResult() + " failed");
                result2.addFailure( this, new AssertionFailedError(op.toString() + " failed"));
                result = false;
            }
        }
        result2.stop();
        return result;
    }
    public int countTestCases() {
        return 1;
    }

    public String toString() {
        return this.description;
        //return name + "("+this.description+")";
    }

    
    public void run( TestResult result ) {
        result.startTest( this );            
        Protectable p= new Protectable() {
            public void protect() throws Throwable {
                runBare();
            }
        };
        result.runProtected( this, p);
        result.endTest( this );
    }
    
    public void runBare() throws Throwable {
        Throwable exception= null;
        try {
            runTest();
        } catch (Throwable running) {
            exception= running;
        }
        if (exception != null) throw exception;
    }
    public void runTest() {
        for (GeometryTestOperation op : operationList) {
            op.runTest( geomA, geomB );            
        }
    }
    public void testEmpty() {
    	//do nothing
    }
}
