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
package org.geotools.geometry.jts.spatialschema.geometry;


import org.opengis.geometry.Geometry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the part of the JTS test XML file
 * that is wrapped with the "case" tags. It contains two
 * geometry objects and then one or more tests to apply
 * to those geometries.
 * 
 * @author Jody Garnett
 * @author Joel Skelton
 *
 * @source $URL$
 */
public class GeometryTestCase {
    
    private static final Log LOG = LogFactory.getLog(GeometryTestCase.class);
    private List operationList;
    private Geometry geomA;
    private Geometry geomB;
    private String description;

    /**
     * Constructor
     */
    public GeometryTestCase() {
        this.operationList = new ArrayList();
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
     * @return
     */
    public boolean runTestCases() {
        boolean result = true;
        LOG.info("Running test:" + description);
        for (Iterator i = operationList.iterator(); i.hasNext();) {
            GeometryTestOperation op = (GeometryTestOperation) i.next();
            LOG.info("Running test case:" + op);
            if (!op.run(geomA, geomB)) {
                LOG.info(op.toString() + " failed");
                result = false;
            }
        }
        return result;
    }
}
