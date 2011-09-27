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
 */
package org.geotools.data.efeature.tests.unit;

import java.io.File;
import java.io.IOException;
import java.util.Collections;


import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.internal.EFeatureLogFormatter;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Abstract test framework for {@link EFeature} tests backed by an {@link Resource}.
 * 
 * @author Christian W. Damus (cdamus)
 * @author Kenneth Gulbrandsøy (kengu)
 *
 * @source $URL$
 */
public abstract class AbstractResourceTest extends AbstractEFeatureTest {

    protected static final String EMPTY_RESOURCE_TEST = "EFeatureEmptyTest";
    
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

    protected <T> AbstractResourceTest(String name, String contentType, boolean reuse, boolean delete) {
        super(name);
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
        //
        // Notify test start
        //
        trace("===> Begin : " + getName() + "[" + contentType + "]"); //$NON-NLS-1$
        EFeatureLogFormatter.setMinimal();
        //
        // Prepare test data information
        //
        File eTestData = org.geotools.test.TestData.file(this,null);
        this.eTestDataPath = eTestData.toString();
        this.eResourcePath = eTestDataPath + "/" + createFileName(getName())+"."+contentType;
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
                createTestData(getName(), r);
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
     * Is called by {@link #setUp()}, and always BEFORE 
     * {@link #createEditingDomain(ResourceSet)}.
     * </p>
     * @return a {@link ResourceSet} instance
     */
    protected abstract ResourceSet createResourceSet();    

    /**
     * Create an {@link EditingDomain editing domain} instance
     * <p>
     * Is called by {@link #setUp()}, and always AFTER
     * {@link #createResourceSet()}.
     * </p>
     * @return a {@link EditingDomain} instance
     */
    protected abstract EditingDomain createEditingDomain(ResourceSet rset);
    
    /**
     * Create file name.
     * <p>
     * Default implementation is to return 
     * {@link Class#getSimpleName() implementing class name}. 
     * Override this method if other name should be used.
     * @param name - the test name
     * @return a file name.
     */
    protected String createFileName(String name) {
        return getClass().getSimpleName();
    }
    
    /**
     * This method is called by {@link #setUp()} immediately after 
     * the {@link #eResource} is constructed and just before it is
     * saved to file and reference to it is cached. 
     * @param name - test name
     * @param eResource - the test data resource
     * @throws Exception if anything went wrong during data construction
     */
    protected void createTestData(String name, Resource eResource) throws Exception {
        
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
                //
                // Persist resource to file?
                //
                if(!delete) eResource.save(Collections.EMPTY_MAP);                
                //
                // Unload resource?
                //
                if (eResource.isLoaded()) {
                    eResource.unload();
                }
                //
                // Remove resource?
                //
                if (eResource.getResourceSet() != null) {
                    eResource.getResourceSet().getResources().remove(eResource);
                }
            }               
            
            //
            // Reset references
            //
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
     * Finds the object in the test model having the specified qualified name.
     * 
     * @param id - a slash-delimited qualified id, relative to the provided <code>offset</code>
     * @return the matching object, or <code>null</code> if not found
     */
    protected EObject find(String id) {
        return find(eResource, id);
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
