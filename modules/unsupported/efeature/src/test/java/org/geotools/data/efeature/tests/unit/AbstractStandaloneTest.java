/**
 * <copyright>
 *
 * Copyright (c) 2005, 2008 IBM Corporation, Zeligsoft Inc. and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *   Zeligsoft - Bug 234868
 *
 * </copyright>
 * 
 * 
 *
 */
package org.geotools.data.efeature.tests.unit;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.efeature.internal.EFeatureLogFormatter;
import org.geotools.util.logging.Logging;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Abstract test framework for the transaction unit tests.
 * 
 * @author Christian W. Damus (cdamus)
 * @author Kenneth Gulbrandsøy (kengu)
 */
public abstract class AbstractStandaloneTest extends TestCase {

    public static boolean isDebugging = true;
    
    /** 
     * Static logger for all {@link AbstractStandaloneTest} instances 
     */
    protected static final Logger LOGGER = Logging.getLogger(AbstractStandaloneTest.class);
    
    protected static final int TIME_DELTA = 1;    
    protected static final int TIME_TOTAL = 2;
    
    protected long sTime;
    protected long dTime;

    protected URI eResourceURI;
    
    protected String eTestDataPath;

    protected String eResourcePath;
    
    protected Resource eResource;

    protected ResourceSet eResourceSet;

    protected EditingDomain eDomain;
    
    protected boolean reuse; 

    protected boolean delete; 
    
    protected String contentType;
    
    // -----------------------------------------------------
    // Constructors
    // -----------------------------------------------------

    protected AbstractStandaloneTest(String name, String contentType, boolean reuse, boolean delete) {
        super(name);
        this.sTime = System.currentTimeMillis();
        this.dTime = sTime;
        this.reuse = reuse;
        this.delete = delete;
        this.contentType = contentType;
    }

    // -----------------------------------------------------
    // Test configuration methods
    // -----------------------------------------------------

    @Override
    @BeforeClass
    public final void setUp() throws Exception {

        trace("===> Begin : " + getName()); //$NON-NLS-1$
        EFeatureLogFormatter.setMinimal();
        
        File eTestData = org.geotools.test.TestData.file(this,null);
        this.eTestDataPath = eTestData.toString();
        this.eResourcePath = eTestDataPath + "/" + getName()+"."+contentType;
        //
        // Create EMF resource URI
        //
        eResourceURI = URI.createURI(eResourcePath,true);
        //
        // Delete existing?
        //
        if(!reuse) delete(eResourcePath);
        //
        // Forward to implementation
        //
        doSetUp();        
        //
        // Prepare EMF resource
        //        
        eResourceSet = createResourceSet();
        try {
            //
            // Get flags
            //
            boolean createNew = !(reuse && (new File(eResourcePath)).exists());
            //
            // Get resource
            //           
            Resource r = (createNew ? eResourceSet.createResource(eResourceURI, contentType) : 
                                      eResourceSet.getResource(eResourceURI, true)) ;
            if(!createNew) {
                trace("Resource loaded",TIME_DELTA);                
            }

            //
            // Create resource data?
            //
            if(createNew) {
                createTestData(r);
                trace("Resource created",TIME_DELTA);
            }
            //
            // Persist resource to file
            //            
            r.save(Collections.EMPTY_MAP);
            eResource = r;
            //
            // Ensure that this resource is deleted on exist?
            //
            if(delete) deleteOnExit(eResourcePath);
            //
            // Prepare EMF editing domain
            //
            eDomain = createEditingDomain(eResourceSet);
            
        } catch (IOException e) {
            fail("Failed to load test model: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }

        trace("Setup completed",TIME_TOTAL);
        
    }

    protected void doSetUp() throws Exception { /*NOP*/}

    /**
     * Create new {@link ResourceSet resource set} instance.
     * <p>
     * Is called by {@link #setUp()}. 
     * </p>
     * @return a {@link ResourceSet} instance
     */
    protected abstract ResourceSet createResourceSet();    

    /**
     * Create an {@link EditingDomain editing domain} instance
     * <p>
     * Is called by {@link #setUp()}.
     * </p>
     * @return a {@link EditingDomain} instance
     */
    protected abstract EditingDomain createEditingDomain(ResourceSet rset);
    
    /**
     * This method is called by {@link #setUp()} immediately after 
     * the {@link #eResource} is constructed and just before it is
     * saved to file and reference to it is cached. 
     * @param eResource
     * @throws Exception if anything went wrong during data construction
     */
    protected void createTestData(Resource eResource) throws Exception {
        
    }

    @Override
    @AfterClass
    protected final void tearDown() throws Exception {

        try {
            //
            // Allow extend to do it's thing first...
            //
            doTearDown();
            //
            // Then do all the rest.
            //
            if (eResource != null) {
                if (eResource.isLoaded()) {
                    eResource.unload();
                }

                if (eResource.getResourceSet() != null) {
                    eResource.getResourceSet().getResources().remove(eResource);
                }
                eResource = null;
            }               
            
            //delete(eResourcePath);
                    
            eDomain = null;
            eResource = null;
            eResourceURI = null;
            eResourceSet = null;
            
            
        } finally {
            trace("===> End   : " + getName(),TIME_TOTAL); //$NON-NLS-1$
            EFeatureLogFormatter.setStandard();
        }
    }

    protected void doTearDown() throws Exception { /*NOP*/ }
    
    

    // -----------------------------------------------------
    // Other framework methods
    // -----------------------------------------------------

    protected static void delete(String path) {
        delete(new File(path));
    }
    
    protected static void delete(File file) {
        if (!file.exists()) {
            return;
        }

        try {
            if(!file.delete()) {
                throw new IllegalStateException("File "+file+" was not deleted");
            }
        } catch (Exception e) {
            fail("Failed to clean up test file: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
    }    
    
    protected static void deleteOnExit(String path) {
        deleteOnExit(new File(path));
    }
    
    protected static void deleteOnExit(File file) {
        if (!file.exists()) {
            return;
        }

        try {
            file.deleteOnExit();
        } catch (Exception e) {
            fail("Failed to clean up test file: " + e.getLocalizedMessage()); //$NON-NLS-1$
        }
    }        

    protected static void trace(String message) {
        if (isDebugging) {
            LOGGER.log(Level.INFO,message);
        }
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
     * Asserts that we can find an object having the specified name.
     * 
     * @param id - a slash-delimited qualified id, relative to the 
     *  provided <code>offset</code>
     * 
     * @see #find(String)
     */
    protected void assertFound(String id) {
        assertNotNull("Did not find " + id, find(eResource, id)); //$NON-NLS-1$
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
     * Asserts that we cannot find an object with the specified id.
     * 
     * @param id - a slash-delimited qualified id, relative to the 
     *  provided <code>offset</code>    
     *   
     * @see #find(String)
     */
    protected void assertNotFound(String name) {
        assertNull("Found " + name, find(eResource, name)); //$NON-NLS-1$
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
     * Finds the object in the test model having the specified qualified name.
     * 
     * @param id - a slash-delimited qualified id, relative to the provided <code>offset</code>
     * @return the matching object, or <code>null</code> if not found
     */
    protected EObject find(String id) {
        return find(eResource, id);
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

    /**
     * Gets the current domain's command stack.
     * 
     * @return the command stack
     */
    protected CommandStack getCommandStack() {
        return eDomain.getCommandStack();
    }

}