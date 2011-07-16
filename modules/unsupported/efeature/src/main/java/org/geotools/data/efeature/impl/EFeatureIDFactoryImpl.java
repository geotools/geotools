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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureIDFactory;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.internal.EFeatureDelegate;
import org.geotools.util.WeakHashSet;
import org.geotools.util.logging.Logging;

/**
 * Default implementation of {@link EFeatureIDFactory} instance.
 * <p>
 * This implementation generates unique IDs for 
 * each {@link EAttribute} is manages.
 *  
 * @author kengu - 26. mai 2011 
 *
 */
public class EFeatureIDFactoryImpl implements EFeatureIDFactory {
    
    public static final String DEFAULT_PREFIX = "F";
    
    private static final Logger LOGGER = Logging.getLogger(EFeatureIDFactoryImpl.class);
    
    protected String ePrefix;
    
    /**
     * This map contains a mapping from each EObject to the ID created for it by this factory.
     */
    protected Map<URI,WeakHashMap<EObject,Long>> eInverseIDMap = new HashMap<URI,WeakHashMap<EObject, Long>>();

    /**
     * This map contains a mapping from each ID to each {@link EObject} assigned to it. 
     * <b>NOTE</b>: Only objects already assigned an ID are allowed to have 
     * the same ID. This only happens if multiple {@link EObject}s are constructed
     * from the same EMF {@link Resource}. This occurs when {@link EObject} are queried
     * multiple times from the resource, and the garbage collector has not yet disposed 
     * older {@link EObject} instances not longer referenced by any client code. 
     * A {@link WeakHashSet} is used to ensure that this factory does not prevent the
     * garbage collection.
     */
    protected Map<URI,Map<Long,WeakHashSet<EObject>>> eIDMap = new HashMap<URI,Map<Long, WeakHashSet<EObject>>>();
    
    /**
     * This map contains the next ID for each {@link EAttribute} this factory create IDs for.
     * It allows for faster creation of unique IDs for each {@link EAttribute} by removing
     * the step of calculating maximum ID value among existing IDs. 
     */
    protected Map<URI,Long> eNextIDMap = new HashMap<URI,Long>();
    
    /**
     * This map ensures that only one {@link EAttribute} per {@link EClass} is allowed 
     * to be used as an unique ID. This is introduced to reduce the complexity, but could be 
     * relaxed if necessary. 
     * 
     * TODO: Add option to allow multiple ID attribute registrations per {@link EClass}? 
     */
    protected Map<EClass,EAttribute> eAttributeMap = new HashMap<EClass, EAttribute>();
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /** 
     * Default constructor.
     * <p>
     * This factory only handles {@link EObject} instances which 
     * implements the {@link EFeature} interface.
     * @see {@link EFeaturePackage#getEFeature_ID()}
     * @throws IllegalArgumentException If the 
     * {@link EDataType#getInstanceClass() data type} of one or 
     * more {@link EAttribute#getEAttributeType()}s are not {@link String}. 
     */
    public EFeatureIDFactoryImpl() throws IllegalArgumentException {
        this(DEFAULT_PREFIX, EFeaturePackage.eINSTANCE.getEFeature_ID());
        
        
    }

    /** 
     * Constructor which tells this factory to generate IDs 
     * for given {@link EAttribute} instances.
     * @param ePrefix - custom ID prefix
     * @param eAttributes - array of {@link EAttribute} instances
     * @see {@link EFeaturePackage#getEFeature_ID()}
     * @throws IllegalArgumentException If the 
     * {@link EDataType#getInstanceClass() data type} of one or 
     * more {@link EAttribute#getEAttributeType()}s are not {@link String}. 
     */    
    public EFeatureIDFactoryImpl(String ePrefix, EAttribute... eAttributes) 
        throws IllegalArgumentException {
        this.ePrefix = ePrefix;
        for(EAttribute it : eAttributes) {
            add(it.getEContainingClass(), it);
        }
    }
    
    // ----------------------------------------------------- 
    //  EFeatureIDFactory implementation
    // -----------------------------------------------------
    
    @Override
    public String getPrefix() {
        return ePrefix;
    }
        
    @Override
    public EAttribute add(EObject eObject) throws IllegalArgumentException {
        return add(eImpl(eObject).eClass());
    }

    @Override
    public EAttribute add(EClass eClass) throws IllegalArgumentException {
        // 
        // Check if have ID attribute?        
        //
        EAttribute eAtt = eClass.getEIDAttribute();
        if(eAtt==null) {
            for(EAttribute it : eClass.getEAllAttributes()) {
                String eType = it.getEAttributeType().getInstanceClassName();
                if("java.lang.String".equals(eType)) {
                    eAtt = it;
                    break;
                }
            }
        }
        //
        // verify non-null
        //
        if(eAtt==null) {
            throw new IllegalArgumentException("Did not find " +
            		"any EAttribute ID in " + eClass.getName());
        }
        //
        // Add attribute
        //
        add(eClass,eAtt);
        //
        // Finished 
        //
        return eAtt;
    }

    @Override
    public void add(EClass eClass, EAttribute eID) {
        EAttribute eAtt = eAttributeMap.get(eClass);
        if(eAtt!=null) {
            if(eAtt!=eID) {
                throw new IllegalArgumentException("EAttribute " + 
                        eAtt.getName() + " already as ID added for " + 
                        "EClass " + eClass.getName());
            }
        } 
        else {
            String eType = eID.getEAttributeType().getInstanceClassName();
            if(!"java.lang.String".equals(eType)) {
                throw new IllegalArgumentException("EAttribute " + 
                        eID.getName() + " contains " + eType + ", not java.lang.String"); 
            }            
            eAttributeMap.put(eClass, eID);
        }        
    }    
        
    @Override
    public boolean creates(EObject eObject) {
        return creates(eImpl(eObject).eClass());
    }

    @Override
    public boolean creates(EClass eClass) {
        return eAttributeMap.containsKey(eClass);
    }
    
    @Override
    public boolean creates(EAttribute eAttribute) {
        return eAttributeMap.containsValue(eAttribute);
    }
        
    @Override
    public boolean contains(URI eURI) {
        return eIDMap.containsKey(eURI);
    }

    @Override
    public boolean contains(EObject eObject) {
        EObject eImpl = eImpl(eObject);
        for(Map<EObject,Long> it : eInverseIDMap.values()) { 
            if(it.containsKey(eImpl)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Map<EObject,String> getIDMap(URI eURI) {
        Map<EObject,Long> eCachedIDs = eInverseIDMap.get(eURI);
        if(eCachedIDs!=null) {
            WeakHashMap<EObject,String> eIDs = new WeakHashMap<EObject, String>(eCachedIDs.size());
            for(Entry<EObject,Long> it : eCachedIDs.entrySet()) {
                eIDs.put(it.getKey(), ePrefix + it.getValue());
            }
            return eIDs;
        }
        return Collections.emptyMap();
    }
    
    @Override
    public String getID(EObject eObject) {
        //
        // Get implementation (strips away EFeatureDelegates)
        //
        EObject eImpl = eImpl(eObject);
        //
        // Verify EObject
        //
        verify("EObject", eImpl, true, false, true);
        //
        // Get URI
        //
        URI eURI = eImpl.eResource().getURI();
        //
        // Forward
        //
        return eGetID(eURI,eImpl);
    }
    
    @Override
    public String createID(EObject eObject) throws IllegalArgumentException {
        //
        // Get implementation (strips away EFeatureDelegates)
        //
        EObject eImpl = eImpl(eObject);
        //
        // Verify EObject
        //
        verify("eObject", eImpl, true, true, true);
        //
        // Get ID attribute
        //
        EAttribute eAttribute = eAttributeMap.get(eImpl.eClass());
        //
        // Verify
        //
        verify("eAttribute",eAttribute,true);
        //
        // Get URI
        //
        URI eURI = eImpl.eResource().getURI();        
        //
        // Verify
        //
        verify("eURI",eURI,true);
        //
        // Get set value
        //
        String eSetID = eGetID(eURI, eImpl);
        //
        // Forward to implementation
        //
        String eNewID = (eSetID==null || eSetID.length()==0 ?
                eCreateID(eURI, eImpl, eImpl.eClass(), eAttribute) : 
                    eUseID(eURI, eImpl, eAttribute, eSetID, true));
        //
        // If EObject is an EFeature, then set ID. 
        // Otherwise, throw an IllegalArgumentException
        //
        return eInverseSet(eObject,eNewID,eSetID);
        
    }

    @Override
    public String useID(EObject eObject, String eID) {
        //
        // Get implementation (strips away EFeatureDelegates)
        //
        EObject eImpl = eImpl(eObject);
        //
        // Verify eObject
        //
        verify("eObject",eImpl,true,true,true);
        //
        // Get URI
        //
        URI eURI = eImpl.eResource().getURI();
        //
        // Verify eURI
        //
        verify("eURI",eURI,true);
        //
        // Get ID attribute
        //
        EAttribute eAttribute = eAttributeMap.get(eImpl.eClass());
        //
        // Get current value
        // 
        String eSetID = (String)eImpl.eGet(eAttribute);
        //
        // Check if set
        //
        boolean eIsSet = !(eSetID==null || eSetID.length()==0);
        //
        // ----------------------------------------------------------
        //  Cache ID as used
        // ----------------------------------------------------------
        //  If ID is not set in given object, make sure that ID is 
        //  unique, if not unique a unique ID is returned. If ID is 
        //  set in given object, then just mark it as used and return
        //  it unchanged. See eIDMap for more information.
        //
        eID = eUseID(eURI, eImpl, eAttribute, eID, eIsSet);
        //
        // If EObject is an EFeature, then set ID. 
        // Otherwise, throw an IllegalArgumentException
        //
        return eInverseSet(eObject,eID,eSetID);        
    }

    @Override
    public String disposeID(EObject eObject) {
        //
        // Get implementation (strips away EFeatureDelegates)
        //
        EObject eImpl = eImpl(eObject);
        //
        // Verify eObject
        //
        verify("eObject",eImpl,true,true,false);
//        //
//        // Get URI
//        //
//        URI eURI = eImpl.eResource().getURI();
//        //
//        // Verify URI
//        //
//        verify("eURI",eURI,true);
        //
        // Get ID attribute
        //
        EAttribute eAttribute = eAttributeMap.get(eImpl.eClass());
        //
        // Get current ID
        //
        String eID = (String)eImpl.eGet(eAttribute);
        //
        // Verify
        //
        verify("eID",eID,true);
        //
        // Forward
        //
        return eDisposeID(eImpl, eID);
    }    
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected String eGetID(URI eURI, EObject eImpl) {
        //
        // Get map
        //
        Map<EObject,Long> eIDs = eInverseIDMap.get(eURI);
        //
        // Locate ID if exists?
        //
        if(eIDs!=null) {
            Long uniqueID = eIDs.get(eImpl);
            if(uniqueID!=null) {
                return ePrefix + uniqueID;
            }
        }       
        return null;
    }    
    
    protected Long eNextID(URI eURI) {
        //
        // Get next unique ID from map
        //
        Long uID = eNextIDMap.get(eURI);
        //
        // First ID in given resource?
        // 
        if(uID==null) uID = 1L;
        //
        // Finished
        //
        return uID;
    }
    
    /**
     * Ensure that ID is unique. If not, return one that is.
     * <p>
     * This method used the {@link #eInverseIDMap} to ensure that 
     * the next ID is is unique
     * @param eImpl - {@link EObject} instance
     * @param eImpl - the object with ID 
     * @param uID - proposed ID
     * @return actual unique ID
     */
    protected Long eUniqueID(URI eURI, EObject eImpl, Long uID) {
        //
        // Get Map of cached ID for given URI
        //
        Map<EObject,Long> eCachedIDs = eInverseIDMap.get(eURI);
        //
        // Found cached IDs?
        //
        if(eCachedIDs!=null) {            
            //
            // Check if an equal ID value is already cached for given object
            //
            if(uID.equals(eCachedIDs.get(eImpl))) 
                return uID;
            //
            // Initialize upper bounds for safe exit
            //
            int size = eCachedIDs.size();
            int count = 0;
            // 
            // Continue until unique is found or end of map reached
            //
            while(eCachedIDs.containsValue(uID) && count<size) {
                uID++;
                count++;
            }
        }
        //
        // Finished
        //
        return uID;
    }
    
    /**
     * Create ID from given information.
     */
    protected String eCreateID(URI eURI, EObject eImpl, EClass eClass, EAttribute eID) 
        throws IllegalArgumentException {
        //
        // Initialize unique id to "unknown"
        //
        Long uID = 0L;
        //
        // Try to get unique ID as string
        //
        String sID = getID(eImpl);
        //
        // Get current ID
        //
        if( !(sID==null || sID.length()==0) ) {
            //
            // Remove prefix
            //
            sID = sID.replace(ePrefix, "");
            //
            // Convert to long
            //
            uID = Long.decode(sID);
        }
        //
        // Current ID is "unknown"?
        //
        if(uID==0L) {
            //
            // Get next ID from map
            //
            uID = eNextIDMap.get(eURI);
            //
            // Start to create IDs for new resource?
            // 
            if(uID==null) {
                //
                // Set first ID in this series
                //
                uID = 1L;
            } else {
                //
                // Validate against cached IDs
                //
                uID = eUniqueID(eURI, eImpl, uID);
            }
        } 
        //
        // Add to ID maps
        //
        return eCacheID(eURI,eImpl,uID);
    }    
    
    protected String eUseID(URI eURI, EObject eImpl, 
            EAttribute eAttribute, String eID, boolean eIsSet) {
        //
        // Remove prefix
        //
        eID = eID.replace(ePrefix, "");
        //
        // Convert to long
        //
        Long uID = Long.decode(eID);
        //
        // Ensure that is is unique?
        //
        if(!eIsSet) {
            uID = eUniqueID(eURI, eImpl, uID);
        }
        //
        // Cache the ID as used
        //
        return eCacheID(eURI, eImpl, uID);
    }        
    
    protected String eCacheID(URI eURI, EObject eImpl, Long uID) {
        //
        // Ensure that EFeatureDelegates are not cached
        //
        if(eImpl instanceof EFeatureDelegate) {
            throw new IllegalArgumentException("IDs can not be associated with EFeatureDelegate instances");
        }        
        //
        // Get Object cached for given ID, create it if not found
        //
        Map<Long,WeakHashSet<EObject>> eCachedIDSets = eIDMap.get(eURI);
        if(eCachedIDSets==null) {
            eCachedIDSets = new HashMap<Long,WeakHashSet<EObject>>();
            eIDMap.put(eURI, eCachedIDSets);
        } 
        WeakHashSet<EObject> eObjectSet = eCachedIDSets.get(uID);
        if(eObjectSet==null) {
            eObjectSet = new WeakHashSet<EObject>(EObject.class);
            eCachedIDSets.put(uID, eObjectSet);            
        }                
        // 
        // Add object to hash set
        //
        eObjectSet.add(eImpl);                
        //
        // Get EObjects cached for given URI, create it if not found
        //
        WeakHashMap<EObject,Long> eInverseIDs = eInverseIDMap.get(eURI);
        if(eInverseIDs==null) {
            eInverseIDs = new WeakHashMap<EObject,Long>();
            eInverseIDMap.put(eURI, eInverseIDs);
        }        
        //
        // Add to ID cache
        //
        eInverseIDs.put(eImpl, uID);
        //
        // Update next ID?
        //
        if(eNextID(eURI)<=uID) {
            eNextIDMap.put(eURI,uID+1);
        }
        //
        // Finished
        //
        return ePrefix+uID;
    }
    
    protected String eInverseSet(EObject eObject, String eNewID, String eSetID) {
        if((eObject instanceof EFeature)) {
            if(!eNewID.equals(eSetID)) {
                if(eObject instanceof EFeatureImpl) {
                    return ((EFeatureImpl)eObject).eInternal().eSetID(eNewID, false);
                } else if(eObject instanceof EFeatureDelegate) {
                    return ((EFeatureDelegate)eObject).eInternal().eSetID(eNewID, false);
                }
                //
                // ------------------------------------------------------
                //  !!! EFeature not implemented !!!
                // ------------------------------------------------------
                //  Just as implementors or EObject are expected to 
                //  implement InternalEObject, implementors of EFeature
                //  are expected to implement EFeatureInternal or 
                //  EFeatureDelegate. These implementations have guards 
                //  that break the recursive call sequence 
                //  EFeature.setID() -> EFeatureIDFactory.useID() -> 
                //  EFeature.setID(), handles the context startup 
                //  problem and much more which direct implementors of 
                //  EFeature must handle themselves. Try
                //
                //
                //  This will probably fail, but let's try it out anyway ...
                //
                try {
                    ((EFeature)eObject).setID(eNewID);
                } catch (Exception e) {
                    //
                    // Notify that this is a unrecoverable errors
                    //
                    String msg = "EFeature is implemented correctly. " +
                    		 "Extend EFeatureImpl or use a EFeatureDelegate instead. ";
                    //
                    // Log message as severe. This should give the implementor some additional hints...
                    //
                    LOGGER.log(Level.SEVERE, msg + e.getMessage(), e);
                    //
                    // Throw it again
                    //
                    throw ((IllegalArgumentException)(new IllegalArgumentException(msg).initCause(e)));
                }
            }
            return eNewID;
        }
        //
        // This should never happen...
        //
        throw new IllegalArgumentException("'" + eObject + " does not implement EFeature");
    }
    
    protected String eDisposeID(EObject eObject, String eID) 
        throws IllegalArgumentException {
        //
        // Remove prefix
        //
        eID = eID.replace(ePrefix, "");
        //
        // Convert to long
        //
        Long uID = Long.decode(eID);        
        //
        // Find URI
        //
        URI eURI = null;
        for(Entry<URI,Map<Long,WeakHashSet<EObject>>> it : eIDMap.entrySet()) {
            WeakHashSet<EObject> eSet = it.getValue().get(uID);
            if(eSet!=null && eSet.contains(eObject)) {
                eURI = it.getKey();
            }
        }
        //
        // Verify that URI was found
        //
        if(eURI==null) {
            //
            // No IDs created for given resource
            //
            throw new IllegalArgumentException("Object " 
                    + eObject.getClass().getSimpleName() 
                    + "[" + eID + " ] not found");
        }    
        //
        // Verify that URI was 
        //
        WeakHashMap<EObject,Long> eCachedIDs = eInverseIDMap.get(eURI);
        if(eCachedIDs==null) {
            //
            // No IDs created for given resource
            //
            throw new IllegalArgumentException("No IDs created for " +
            		"resource [" + eURI + " ]");
        }        
        //
        // Get ID from cache
        //
        uID = eCachedIDs.get(eObject);
        //
        // ID not found?
        if(uID == null) {
            //
            // No ID created for given object
            //
            throw new IllegalArgumentException("No ID created for " + eObject);            
        }
        if(eID.equals(ePrefix+uID)) {
            //
            // Not same as ID created for given object
            //
            throw new IllegalArgumentException("Expected ID " + eID + "," + 
                    "found " + ePrefix + uID);                        
        }
        //
        // Dispose ID
        //
        eCachedIDs.remove(eObject);
        //
        // Finished
        //
        return eID;
    }    
        
    protected void verify(String eType, Object object, boolean isNonNull) {
        if(isNonNull && object==null) {
            throw new NullPointerException(eType + " can not be null");
        }
        
    }
    
    protected void verify(String eType, EObject eObject, boolean isNonNull, boolean isValid, boolean isLoaded) {        
        verify(eType, eObject,isNonNull);
        if(isValid && !creates(eObject)) {
            throw new IllegalArgumentException(eType + " " + getName(eObject) + " is not valid");
        }
        if(isLoaded && eObject.eResource()==null) {
            throw new IllegalStateException(eType + " " + getName(eObject) + " is not loaded into a resource");            
        }
    }
    
    protected static String getName(EObject eObject) {
        if(eObject==null) return "null";
        if(eObject instanceof ENamedElement) {
            return ((ENamedElement)eObject).getName();
        }
        return eObject.toString();
    }
    
    protected EObject eImpl(EObject eObject) {
        if(eObject instanceof EFeatureDelegate) {
            eObject = ((EFeatureDelegate)eObject).eImpl();
        }
        return eObject;
    }

}
