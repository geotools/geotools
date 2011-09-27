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
package org.geotools.data.efeature.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.EFeatureContextFactory;
import org.geotools.data.efeature.EFeatureContextInfo;
import org.geotools.data.efeature.EFeatureIDFactory;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.internal.EFeatureVoidIDFactory;
import org.geotools.util.logging.Logging;

/**
 * Default implementation of {@link EFeatureContext} instance.
 * <p>
 * This implementation generates unique IDs for 
 * each {@link EAttribute} is manages.
 * </p> 
 * 
 * @author kengu - 26. mai 2011
 *
 *
 * @source $URL$
 */
public final class EFeatureContextImpl implements EFeatureContext {

    private static final Logger LOGGER = Logging.getLogger(EFeatureContextImpl.class); 

    // ----------------------------------------------------- 
    //  EFeatureContext implementation
    // -----------------------------------------------------
    
    private final String eContextID;
    private final boolean isPrototype;
    private final EFeatureIDFactory eIDFactory;
    private final WeakReference<EFeatureContextFactory> eContextFactory;
    private final ResourceHandler eResourceHandler = new ResourceHandler();
    private final Map<String, EPackage> ePackageMap = new HashMap<String, EPackage>();
    private final Map<String, EditingDomain> eDomainMap = new HashMap<String, EditingDomain>();
       
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * This constructor implements a the default 
     * {@link EFeatureIDFactoryImpl EFeature ID factory}
     * </p>
     * @param eContextFactory - the {@link EFeatureContext eContextID factory}
     * @param eContextID - the {@link EFeatureContext eContextID} which this is created with
     * @throws NullPointerException - if any argument is <code>null</code>
     * @throws IllegalArgumentException - if 'eContextID' is an empty string.
     */
    public EFeatureContextImpl(EFeatureContextFactory eContextFactory, String eContextID) {
        this(eContextFactory,eContextID,new EFeatureIDFactoryImpl());
    }

    /**
     * This constructor use given 
     * {@link EFeature} ID {@link EFeatureIDFactoryImpl factory} 
     * <p>
     * @param eContextFactory - the {@link EFeatureContext eContextID factory}
     * @param eContextID - the {@link EFeatureContext eContextID} which this is created with
     * @param eIDFactory - the {@link EFeatureIDFactory eFeatureID factory}
     * @throws NullPointerException - if any argument is <code>null</code>
     * @throws IllegalArgumentException - if 'eContextID' is an empty string.
     */
    public EFeatureContextImpl(EFeatureContextFactory eContextFactory, 
            String eContextID, EFeatureIDFactory eIDFactory) 
        throws NullPointerException, IllegalArgumentException {
        //
        // Is valid?
        //
        if(eContextID==null) {
            throw new NullPointerException("Context ID can not be null");
        }
        if(eContextID.length()==0) {
            throw new IllegalArgumentException("Context ID can not be empty");
        }
        if(eContextFactory==null) {
            throw new NullPointerException("Context factory can not be null");
        }
        if(eIDFactory==null) {
            throw new NullPointerException("EFeatureIDFactory can not be null");
        }

        //
        // Cache properties
        //
        this.eContextID = eContextID;
        this.eIDFactory = eIDFactory;
        this.isPrototype = (eIDFactory instanceof EFeatureVoidIDFactory);
        this.eContextFactory = new WeakReference<EFeatureContextFactory>(eContextFactory);
    }

    // ----------------------------------------------------- 
    //  EFeatureContext implementation
    // -----------------------------------------------------
    
    @Override
    public String eContextID() {
        return eContextID;
    }
        
    @Override
    public boolean isPrototype() {
        return isPrototype;
    }

    @Override
    public EFeatureContextInfo eStructure() {
        return eContextFactory().eStructure(eContextID);
    }

    @Override
    public EFeatureIDFactory eIDFactory() {
        return eIDFactory;
    }
    
    @Override
    public EFeatureContextFactory eContextFactory() {
        return eContextFactory.get();
    }
    

    @Override
    public boolean containsDomain(String eDomainID) {
        return eDomainMap.containsKey(eDomainID);
    }

    @Override
    public List<String> eDomainIDs() {
        return Collections.unmodifiableList(new ArrayList<String>(eDomainMap.keySet()));
    }

    @Override
    public EditingDomain eGetDomain(String eDomainID) {
        EditingDomain eDomain = eDomainMap.get(eDomainID);
        if(eDomain==null) {
            throw new IllegalArgumentException("EditingDomain [" + eDomainID + "] not found");
        }
        return eDomain;
    }

    @Override
    public Resource eGetResource(String eDomainID, URI eURI, boolean loadOnDemand) {
        return eGetDomain(eDomainID).getResourceSet().getResource(eURI, loadOnDemand);
    }

    @Override
    public boolean containsPackage(String eNsURI) {
        return ePackageMap.containsKey(eNsURI);
    }

    @Override
    public List<String> eNsURIs() {
        return Collections.unmodifiableList(new ArrayList<String>(ePackageMap.keySet()));
    }

    @Override
    public EPackage eGetPackage(String eNsURI) {
        EPackage ePackage = ePackageMap.get(eNsURI);
        if(ePackage==null) {
            throw new IllegalArgumentException("EPackage [" + eNsURI + "] not found");
        }
        return ePackage;
    }
        
    @Override
    public boolean contains(EPackage ePackage) {
        return ePackageMap.containsKey(ePackage.getNsURI());
    }

    @Override
    public EPackage eAdd(EPackage ePackage) throws IllegalArgumentException {
        String eNsURI = ePackage.getNsURI();
        if(!(eNsURI==null || eNsURI.length()==0)) {
            return ePackageMap.put(eNsURI, ePackage);            
        }
        throw new IllegalArgumentException("EPackage#getNsURI() must be specified.");
    }
       
    public boolean contains(String eDomainID, EditingDomain eDomain) {
        return eDomainMap.containsKey(eDomainID);
    }

    @Override
    public EditingDomain eAdd(String eDomainID, EditingDomain eDomain) throws IllegalArgumentException {
        if(!(eDomainID==null || eDomainID.length()==0)) {
            ResourceSet eSet = eDomain.getResourceSet(); 
            eSet.eAdapters().add(eResourceHandler);
            return eDomainMap.put(eDomainID, eDomain);            
        }
        throw new IllegalArgumentException("EditingDomain ID must be specified.");
    }
    
    @Override
    public EFeatureInfo eAdapt(EFeature eFeature, boolean copy) {
        //
        // Forward to structure
        //
        EFeatureInfo eInfo = eStructure().eAdapt(eFeature.getStructure(), copy);
        //
        // Update structure
        //
        eFeature.setStructure(eInfo);
        //
        // Finished
        //
        return eInfo;
    }
    
    
    // ----------------------------------------------------- 
    //  Inner classes
    // -----------------------------------------------------
    
    private class ResourceHandler extends AdapterImpl {

        @Override
        public void notifyChanged(Notification msg) {
            //
            // Get ID factory
            //
            EFeatureIDFactory eIDFactory = eIDFactory();
            //
            // Does this context generate IDs?
            //
            if( !(eIDFactory instanceof EFeatureVoidIDFactory) ) {
                //
                // Is added or removed?
                //
                if( msg.getEventType() == Notification.ADD ) {
                    Object value = msg.getNewValue();
                    if(value instanceof EObject) {
                        //
                        // Implements EFeature?
                        //
                        if(value instanceof EFeature) {
                            //
                            // Cast to EFeature
                            //
                            EFeature eFeature = (EFeature)value;
                            //
                            // ----------------------------------------------------------
                            //  Adapt given EFeature (does nothing if already in context)
                            // ----------------------------------------------------------
                            //  This is a very important step: It's part of the context
                            //  startup problem solution (see EFeatureContextHelper), 
                            //  and ensures that context-unaware objects become 
                            //  aware of the context they are added to. Without this 
                            //  step, EFeature stays context-unaware, preventing 
                            //  EFeatureReaders from reading them using a context ID
                            //  known by client code.
                            // ----------------------------------------------------------
                            //
                            eAdapt(eFeature, true);
                            //
                            // Get current ID
                            //
                            String eID = eFeature.getID();
                            //
                            // Resolve unique ID
                            //
                            if( !( eID==null || eID.length()==0 ) ) {
                                eIDFactory.useID(eFeature, eID);
                            } else {
                                eIDFactory.createID(eFeature);
                            }
                            
                        } else if(eIDFactory.creates((EObject)value)) {
                            //
                            // Adapt object directly to context
                            //
                            eStructure().eAdapt((EObject)value);
                        }
                    }
                    else if(value instanceof Resource) {
                        ((Resource)value).eAdapters().add(this);
                    }                
                } else if( msg.getEventType() == Notification.REMOVE ) {
                    Object value = msg.getOldValue();
                    if(value instanceof EObject) {
                        //
                        // Cast to EObject
                        //
                        EObject eObject = (EObject)value;
                        // 
                        // ID created for this object?
                        //
                        if(eIDFactory.contains(eObject)) {
                            try {
                                
                                //
                                // This should never fail...
                                //
                                eIDFactory.disposeID(eObject);
                                
                            } catch (IllegalArgumentException e) {
                                //
                                // ... but if it does, log a warning ...
                                //                                
                                LOGGER.log(Level.WARNING, e.getMessage(), e);
                                //
                                // ... and re-throw it
                                //
                                throw e;
                            }
                        }
                        //eIDFactory.disposeID(eFeature.getID());
                    }
                    else if(value instanceof Resource) {
                        ((Resource)value).eAdapters().remove(this);
                    }                                
                }
            }
        }
    }    
    
}
