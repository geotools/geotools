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
package org.geotools.data.efeature;

import java.awt.RenderingHints.Key;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.spi.ServiceRegistry;

import org.geotools.data.efeature.impl.EFeatureContextImpl;
import org.geotools.data.efeature.impl.EFeatureIDFactoryImpl;
import org.geotools.factory.BufferedFactory;
import org.geotools.util.logging.Logging;

/**
 * This class implements a cache of {@link EFeatureContext} instances.
 * <p>
 * Each {@link EFeatureContext instance} is associated with a factory ID. The 
 * reference to each cached {@link EFeatureContext is} is strong (ordinary Java
 * object reference), ensuring that instances can not be garbage collected, even
 * if more memory is needed.   
 * </p>
 * 
 * @author kengu
 *
 */
public class EFeatureContextFactory implements BufferedFactory {

    /**
     * Cached {@link Logger} instance for this class
     */
    private static final Logger LOGGER = Logging.getLogger(EFeatureContextFactory.class);
    
    /**
     * Weak reference to instance cached by the 
     * {@link ServiceRegistry service registry}.
     */
    private static WeakReference<EFeatureContextFactory> eInstance;
    
    /**
     * {@link Map} containing {@link EFeatureContext} instances.
     */
    private final Map<String, EFeatureContext> 
        eContextMap = new HashMap<String, EFeatureContext>();
    
    /**
     * {@link Map} containing {@link EFeatureContextInfo} instances.
     */
    private final Map<String, EFeatureContextInfo> 
        eContextInfoMap = new HashMap<String, EFeatureContextInfo>();

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * Default construction (required by SPI)
     */
    public EFeatureContextFactory() { /*NOP*/ }
    
    
    // ----------------------------------------------------- 
    //  Static helper methods
    // -----------------------------------------------------

    /**
     * Get instance cached by {@link EFeatureFactoryFinder}
     */
    public static EFeatureContextFactory eDefault(){
        if(eInstance==null || eInstance.get()==null)
        {
            eInstance = new WeakReference<EFeatureContextFactory>(
                    EFeatureFactoryFinder.getContextFactory());
        }
        return eInstance.get();
    }
    
    /**
     * Check to see if {@link EFeatureDataStore}s can be created.
     * <p>
     * This factory is only available if at least one 
     * {@link EFeatureContext} instance is registered.
     * <p>
     * 
     * @return <code>true</code> if and only if this factory is available to create
     *         {@link EFeatureDataStore}s.
     * 
     */
    public boolean isAvailable() {
        return EFeatureUtils.isAvailable(eContextInfoMap);
    }
    
    /**
     * Check if given {@link EFeatureContext} instance is created by this factory.
     * <p>
     * This method checks for {@link Map#containsValue(Object) value equality}
     * between given instance and cached instances. 
     * </p>
     * @param eContext - a {@link EFeatureContext} instance
     * @return <code>true</code> if found.
     * @see {@link #contains(String)}
     */
    public boolean contains(EFeatureContext eContext)
    {
        return eContextMap.containsValue(eContext);
    }
    
    /**
     * Check if a {@link EFeatureContext} instance with given ID is created by this factory.
     * <p>
     * This method checks for {@link Map#containsKey(Object) key equality}
     * between given ID and cached IDs. 
     * </p>
     * @param eContextID - {@link EFeature} context {@link EFeatureContext#get id}
     * @return <code>true</code> if found.
     */
    public boolean contains(String eContextID)
    {
        return eContextMap.containsKey(eContextID);
    }
    
    /**
     * Create a {@link EFeatureContext} instance with given ID.
     * </p>
     * @return a {@link EFeatureContext} instance.
     * @throws IllegalArgumentException If an another instance with 
     * same eContextID already exist. 
     * {@link EFeatureContext instance}. 
     */
    public EFeatureContext create(String eContextID) throws IllegalArgumentException
    {
        return create(eContextID,new EFeatureIDFactoryImpl(), new EFeatureHints());
    }
    
    /**
     * Create a {@link EFeatureContextInfo} instance from given 
     * {@link EFeatureContext} instance and cache both instances.
     * <p>
     * If instance has already joined this factory, this method returns the cached 
     * {@link EFeatureContextInfo} instance. 
     * </p>
     * @param eContextID
     * @param eHints - {@link EFeatureHints} instance
     * @return a {@link EFeatureContextInfo} instance.
     * @throws IllegalArgumentException If an another instance with 
     * same eContextID already exist. 
     * {@link EFeatureContext instance}. 
     */
    public EFeatureContext create(String eContextID, EFeatureHints eHints) throws IllegalArgumentException
    {
        return create(eContextID,new EFeatureIDFactoryImpl(),eHints);
    }
    
    /**
     * Create a {@link EFeatureContextInfo} instance from given 
     * {@link EFeatureContext} instance and cache both instances.
     * <p>
     * If instance has already joined this factory, this method returns the cached 
     * {@link EFeatureContextInfo} instance. 
     * </p>
     * @param eContextID
     * @param eIDFactory
     * @param eHints - {@link EFeatureHints} instance
     * @return a {@link EFeatureContextInfo} instance.
     * @throws IllegalArgumentException If an another instance with 
     * same eContextID already exist. 
     * {@link EFeatureContext instance}. 
     */
    public EFeatureContext create(String eContextID, EFeatureIDFactory eIDFactory, EFeatureHints eHints) throws IllegalArgumentException
    {
        try {
            //
            // Use default factory?
            //
            if(eIDFactory==null) eIDFactory = new EFeatureIDFactoryImpl();
            //
            // Use default hints?
            //
            if(eHints==null) eHints = new EFeatureHints();
            //
            // Another instance already registered with given ID?
            //
            EFeatureContextInfo eContextInfo = eContextInfoMap.get(eContextID);
            if(eContextInfo != null)
            {
                throw new IllegalArgumentException(
                        "EFeatureContext " + eContextID + "' already exists");
            }
            //
            // All OK, go ahead and create context and structure.
            //
            EFeatureContext eContext = new EFeatureContextImpl(this, eContextID, eIDFactory);
            //
            // Map context to id (required by subsequent create methods)
            //
            eContextMap.put(eContextID, eContext);
            //
            // Create structure info object
            //
            eContextInfo = EFeatureContextInfo.create(this,eContextID, null);
            //
            // Map structure to id
            //
            eContextInfoMap.put(eContextID, eContextInfo);
            //
            // Success!
            //
            return eContext;
        } 
        catch (IllegalArgumentException e) {
            //
            // Remove context and structure from cache
            //
            eContextMap.remove(eContextID);
            eContextInfoMap.remove(eContextID);
            //
            // Log the error
            //
            LOGGER.throwing(getClass().getName(), "create(EFeatureContext)", e);
            //
            // Notify again
            //
            throw e;
        }
    }
    
    /**
     * Dispose given {@link EFeatureContext context}.
     * </p>
     * @param eContext - the {@link EFeatureContext} instance.
     * @throws IllegalArgumentException If given context is not
     * created by this factory.
     */
    public void dispose(EFeatureContext eContext) {
        if(!contains(eContext)) {
            throw new IllegalArgumentException("Context " 
                    + (eContext !=null ? eContext.eContextID() : "null") 
                    + " is not created by this factory");
        }
        //
        // Get ID
        //
        String eContextID = eContext.eContextID();
        //
        // Dispose structure
        //
        eStructure(eContextID).dispose();
        //
        // Remove context from cache, allowing garbage collections
        //
        eContextMap.remove(eContextID);
        //
        // Tell garbage collector that memory can possible be reclaimed
        //
        Runtime.getRuntime().gc();
    }
    
    /**
     * Dispose {@link EFeatureContext context} with given id.
     * </p>
     * @param eContextID - the {@link EFeatureContext} id.
     * @throws IllegalArgumentException If given context is not
     * created by this factory.
     */
    public void dispose(String eContextID) {
        if(!contains(eContextID)) {
            throw new IllegalArgumentException("Context " 
                    + eContextID + " is not created by this factory");
        }
        dispose(eContextMap.get(eContextID));        
    }

    /**
     * Get a EFeature {@link EFeatureContextInfo registry info} instance.
     * <p>
     * <strong>NOTE</strong>: If no instance was found, a 
     * {@link IllegalArgumentException} it thrown.    
     * </p>
     * @param eContextID - id of requested {@link EFeatureContextInfo} instance  
     * @return a {@link EFeatureContextInfo} instance.
     * @throws IllegalArgumentException If no {@link EFeatureContext} instance with
     * given ID was found.
     */
    public EFeatureContextInfo eStructure(String eContextID) throws IllegalArgumentException
    {
        EFeatureContextInfo eContextInfo =  eContextInfoMap.get(eContextID);
        if(eContextInfo == null)
        {
            throw new IllegalArgumentException("EFeatureContext instance with ID " 
                    + eContextID + " was found");
        }
        return eContextInfo;
        
    }
    
    /**
     * Get a EFeature {@link EFeatureContext context} instance. 
     * </p>
     * @param eContextID - id of requested {@link EFeatureContext} instance 
     * @return a {@link EFeatureContextInfo} instance.
     * @throws IllegalArgumentException If no {@link EFeatureContext} 
     * instance was found.
     */
    public EFeatureContext eContext(String eContextID)
    {
        EFeatureContext eContext = eContextMap.get(eContextID);
        if(eContext == null)
        {
            throw new IllegalArgumentException(
                    "EFeatureContext '" + eContextID + "' does not exist");
        }
        return eContext;
    }

    public Map<Key, ?> getImplementationHints() {
        //
        // TODO: Add hints for controlling cache size etc.
        //
        return Collections.emptyMap();
    }
    

}
