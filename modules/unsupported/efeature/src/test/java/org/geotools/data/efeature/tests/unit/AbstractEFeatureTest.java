/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature.tests.unit;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.util.logging.Logging;

import junit.framework.TestCase;

/**
 * @author kengu - 14. juni 2011
 *
 */
public abstract class AbstractEFeatureTest extends TestCase {

    public static boolean isDebugging = true;
    /** 
     * Logger for all {@link AbstractResourceTest} implementations 
     */
    protected final Logger LOGGER;
    protected static final int TIME_DELTA = 1;
    protected static final int TIME_TOTAL = 2;
    
    private long sTime;
    private long dTime;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * @param name
     */
    public AbstractEFeatureTest(String name) {
        super(name);
        sTime();
        LOGGER = Logging.getLogger(name);
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected void trace(String message) {
        if (isDebugging) {
            LOGGER.log(Level.INFO,message);
        }
    }
    
    protected long sTime() {
        sTime = System.currentTimeMillis();
        dTime = sTime;
        return sTime; 
    }

    protected long dTime() {
        return dTime = System.currentTimeMillis();
    }

    protected void trace(String message, int delta) {
        if (isDebugging) {
            long tac = System.currentTimeMillis();
            if(delta==TIME_DELTA) {
                message += ", Time: +" + (tac-dTime)/1000.0;
            } else if(delta==TIME_TOTAL) {
                message += ", Time: ~" + (tac-sTime)/1000.0;
            }
            dTime = tac;
            trace(message);
        }
    }

    /**
     * Records a failure due to an exception that should not have been thrown.
     * 
     * @param e the exception
     */
    protected void fail(Exception e) {
        e.printStackTrace();
        fail("Should not have thrown: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }

    /**
     * Asserts that we can find an object having the specified id, 
     * relative to the specified offset object.
     * 
     * @param offset - the object from which to start looking 
     *  (to which the <code>id</code> is relative). This can be a 
     *  resource or an element
     * @param id - a slash-delimited qualified id, relative to the 
     *  provided <code>offset</code>
     * 
     * @see #find(Object, String)
     */
    protected void assertFound(Object offset, String id) {
        assertNotNull("Did not find " + id, find(offset, id)); //$NON-NLS-1$
    }

    /**
     * Asserts that we cannot find an object having the specified name, relative to the specified
     * starting object.
     * 
     * @param offset - the object from which to start looking 
     *  (to which the <code>name</code> is relative). This can be a 
     *  resource or an element
     * @param id - a slash-delimited qualified id, relative to the 
     *  provided <code>offset</code>
     * 
     * @see #find(Object, String)
     */
    protected void assertNotFound(Object offset, String id) {
        assertNull("Found " + id, find(offset, id)); //$NON-NLS-1$
    }

    /**
     * Finds the object in the test model having the specified id, starting from some object.
     * 
     * @param offset - the starting object (resource or element)
     * @param id - a slash-delimited qualified id, relative to the provided <code>offset</code>
     * @return the matching object, or <code>null</code> if not found
     */
    protected EObject find(Object offset, String id) {
        EObject result = null;
        Object current = offset;
    
        String[] ids = tokenize(id);
    
        for (int i = 0; (current != null) && (i < ids.length); i++) {
            id = ids[i];
            result = null;
    
            for (EObject it : getContents(current)) {
                if (id.equals(EcoreUtil.getID(it))) {
                    result = it;
                    break;
                }
            }
            current = result;
        }
    
        return result;
    }

    /**
     * Gets the contents of an object.
     * 
     * @param object an object, which may be a resource or an element
     * @return its immediate contents (children)
     */
    private List<EObject> getContents(Object object) {
        if (object instanceof EObject) {
            return ((EObject) object).eContents();
        } else if (object instanceof Resource) {
            return ((Resource) object).getContents();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Tokenizes a qualified id on the slashes.
     * 
     * @param id - a qualified id
     * @return the parts between the slashes
     */
    private String[] tokenize(String id) {
        return id.split("/"); //$NON-NLS-1$
    }

}