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
    
    protected String ePrefix;
    //protected WeakHashMap<EObject,Long> eUnusedIDMap = new WeakHashMap<EObject, Long>();
    protected HashMap<URI,WeakHashMap<EObject,Long>> eCachedIDMap = new HashMap<URI,WeakHashMap<EObject, Long>>();
    protected HashMap<URI,Map<EAttribute,Long>> eNextIDMap = new HashMap<URI,Map<EAttribute, Long>>();
    protected HashMap<EClass,EAttribute> eAttributeMap = new HashMap<EClass, EAttribute>();
    
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
    
    public String getPrefix() {
        return ePrefix;
    }
        
    public EAttribute add(EObject eObject) throws IllegalArgumentException {
        return add(eObject.eClass());
    }

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
        
    public boolean creates(EObject eObject) {
        return creates(eObject.eClass());
    }

    public boolean creates(EClass eClass) {
        return eAttributeMap.containsKey(eClass);
    }
    
    public boolean creates(EAttribute eAttribute) {
        return eAttributeMap.containsValue(eAttribute);
    }
        
    public boolean contains(URI eURI) {
        return eCachedIDMap.containsKey(eURI);
    }

    public boolean contains(EObject eObject) {
        for(Map<EObject,Long> it : eCachedIDMap.values()) { 
            if(it.containsKey(eObject)) {
                return true;
            }
        }
        return false;
    }
    
    public Map<EObject,String> getIDMap(URI eURI) {
        Map<EObject,Long> eCachedIDs = eCachedIDMap.get(eURI);
        if(eCachedIDs!=null) {
            WeakHashMap<EObject,String> eIDs = new WeakHashMap<EObject, String>(eCachedIDs.size());
            for(Entry<EObject,Long> it : eCachedIDs.entrySet()) {
                eIDs.put(it.getKey(), ePrefix + it.getValue());
            }
        }
        return Collections.emptyMap();
    }
    
    public String getID(EObject eObject) {
        //
        // Verify EObject
        //
        verify("eObject", eObject, true, false, true);
        //
        // Locate ID if exists
        //
        for(Map<EObject,Long> it : eCachedIDMap.values()) {
            Long uniqueID = it.get(eObject);
            if(uniqueID!=null) {
                return ePrefix + uniqueID;
            }
        }       
        return null;
    }
    
    public String getID(URI eURI, EObject eObject) {
        //
        // Verify URI
        //
        verify("eURI", eURI, true);
        //
        // Verify EObject
        //
        verify("eObject", eObject, true, false, true);
        //
        // Locate ID if exists
        //
        for(Map<EObject,Long> it : eCachedIDMap.values()) {
            Long uniqueID = it.get(eObject);
            if(uniqueID!=null) {
                return ePrefix + uniqueID;
            }
        }       
        return null;
    }
    
    public String createID(EObject eObject) throws IllegalArgumentException {
        //
        // Verify EObject
        //
        verify("eObject", eObject, true, false, true);
        //
        // Get ID attribute
        //
        EAttribute eAttribute = eAttributeMap.get(eObject.eClass());
        //
        // Verify
        //
        verify("eAttribute",eAttribute,true);
        //
        // Get resource
        //
        Resource eResource = eObject.eResource();
        //
        // Verify
        //
        verify("eResource",eResource,true);
        //
        // Get URI
        //
        URI eURI = eResource.getURI();        
        //
        // Forward to implementation
        //
        return createID(eURI, eObject, eObject.eClass(), eAttribute);
    }

//    public String createID(EObject eObject, EAttribute eID) 
//        throws IllegalArgumentException, IllegalStateException {
//        //
//        // Verify EObject
//        //
//        verify("EObject", eObject, true, false, true);
//        //
//        // Verify EAttribute
//        //
//        verify("EAttribute", eID, true, false, false);
//        //
//        // Get attribute class
//        //
//        EClass eClass = eID.getEContainingClass(); 
//        //
//        // Verify instance of EClass?
//        //
//        if( !(eObject==null  || eClass.isInstance(eObject)) ) {
//            throw new IllegalArgumentException(eObject + 
//                    " is not an instance of " + eClass);
//        }        
//        //
//        // Get URI
//        //
//        URI eURI = eObject.eResource().getURI();        
//        //
//        // Forward to implementation
//        //
//        return createID(eURI, eObject, eClass, eID);
//    }    
//    
//    public String createID(URI eURI, EObject eObject, EAttribute eID)
//            throws IllegalArgumentException, IllegalStateException {
//        //
//        // Verify URI
//        //
//        verify("URI",eURI,true);
//        //
//        // Verify EObject
//        //
//        verify("EObject", eObject, true, false, true);
//        //
//        // Forward
//        //
//        return createID(eURI, eObject, eObject.eClass(), eID);
//    }

    public String peekID(URI eURI, EClass eClass) throws IllegalArgumentException {
        //
        // Verify URI
        //
        verify("eURI",eURI, true);
        //
        // Verify EObject
        //
        verify("eClass",eClass, true);
        //
        // Get ID attribute
        //
        EAttribute eAttribute = eAttributeMap.get(eClass);
        //
        // Verify
        //
        verify("eAttribute",eAttribute,true);
        //
        // Forward 
        //        
        return peekID(eURI, eClass);
    }

    public String peekID(URI eURI, EAttribute eAttribute)
            throws IllegalArgumentException {
        //
        // Verify URI
        //
        verify("eURI", eURI, true);
        //
        // Verify ID EAttribute
        //
        verify("eAttribute", eAttribute, true);
        //
        // Forward to implementation
        //
        return ePrefix + nextID(eURI, eAttribute);
    }    
    
    public String useID(EObject eObject, String eID) {
        //
        // Verify eObject
        //
        verify("eObject",eObject,true);
        //
        // Get resource
        //
        Resource eResource = eObject.eResource();
        //
        // Verify resource
        //
        verify("eResource",eResource,true);
        //
        // Get ID attribute
        //
        EAttribute eAttribute = eAttributeMap.get(eObject.eClass());
        //
        // Verify
        //
        verify("eAttribute",eAttribute,true);
        //
        // Forward
        //
        return useID(eResource.getURI(), eObject, eAttribute, eID);
    }
    
//    public String useID(EObject eObject, EAttribute eAttribute, String eID) {
//        //
//        // Get resource
//        //
//        Resource eResource = eObject.eResource();
//        //
//        // Verify resource
//        //
//        verify("Resource",eResource,true);
//        //
//        // Forward
//        //
//        return useID(eResource.getURI(), eObject, eAttribute,eID);        
//    }
//       

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected Long nextID(URI eURI, EAttribute eID) {
        //
        // Initialize ID
        //
        Long uniqueID = 0L;
        //
        // Get map of next IDs
        //
        Map<EAttribute,Long> eNextIDs = eNextIDMap.get(eURI);
        //
        // Factory is caching IDs?
        // 
        if(eNextIDs!=null) {
            //
            // Get next ID for given attribute
            //
            uniqueID = eNextIDs.get(eID);
            if(uniqueID==null) {
                uniqueID = 1L;
            }
            //
            // Validate against cached IDs
            //
            return uniqueID(eURI, null, uniqueID);        
        }
        //
        // Finished
        //
        return uniqueID++;        
    }
    
    /**
     * Create ID from given information.
     */
    protected String createID(URI eURI, EObject eObject, EClass eClass, EAttribute eID) 
        throws IllegalArgumentException {
        //
        // Initialize unique id to "unknown"
        //
        Long uID = 0L;
        //
        // Try to get unique ID as string
        //
        String sID = getID(eObject);
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
        // No unique ID "unknown"?
        //
        if(uID==0L) {
            //
            // Get map of next IDs
            //
            Map<EAttribute,Long> eNextIDs = eNextIDMap.get(eURI);
            //
            // Start to create IDs for new resource?
            // 
            if(eNextIDs==null) {
                eNextIDs = new WeakHashMap<EAttribute, Long>();
                eNextIDMap.put(eURI, eNextIDs);
            }
            //
            // Get current ID for given class
            //
            uID = eNextIDs.get(eID);
            if(uID==null) {
                uID = 1L;
            }
            //
            // Validate against cached IDs
            //
            uID = uniqueID(eURI, eObject, uID);
            //
            // Add next unique ID to map
            //
            eNextIDs.put(eID, uID+1);
        } 
        //
        // Add to unused IDs map
        //
        return cacheID(eURI,eObject,uID);
        //return ePrefix + uID;
    }    
    
//    protected String unusedID(EObject eObject, Long eID) {
//        //
//        // Add to map of unused IDs 
//        //
//        eUnusedIDMap.put(eObject,eID);
//        //
//        // Finished
//        //
//        return ePrefix+eID;
//    }
    
    protected String useID(URI eURI, EObject eObject, EAttribute eAttribute, String eID) {
        //
        // Verify URI
        //
        verify("eURI",eURI,true);
        //
        // Verify URI
        //
        verify("eObject",eObject,true);
        //
        // Verify ID
        //
        verify("eID",eID,true);
        //
        // Initialize used ID
        //
        String eUsedID = null;
        //
        // Remove prefix
        //
        eID = eID.replace(ePrefix, "");
        //
        // Convert to long
        //
        Long uID = Long.decode(eID);
        //
        // Ensure that is is unique
        //
        uID = uniqueID(eURI, eObject, uID);        
        //
        // Cache the ID as used
        //
        eUsedID = cacheID(eURI, eObject, uID);
        //
        // Finished
        //
        return eUsedID;
    }        
    
    protected String cacheID(URI eURI, EObject eObject, Long eID) {
        //
        // Get IDs cached for given URI, create it if not found
        //
        WeakHashMap<EObject,Long> eCachedIDs = eCachedIDMap.get(eURI);
        if(eCachedIDs==null) {
            eCachedIDs = new WeakHashMap<EObject,Long>();
            eCachedIDMap.put(eURI, eCachedIDs);
        }        
        //
        // Add to ID cache
        //
        eCachedIDs.put(eObject, eID);
        //
        // Finished
        //
        return ePrefix+eID;
    }
    
    /**
     * Ensure that ID is unique. If not, return one that is.
     * <p>
     * This method used the {@link #eCachedIDMap} to ensure that 
     * the next ID is is unique
     * @param eObject - {@link EObject} instance
     * @param uID - proposed ID
     * @param eObject - the object with ID 
     * @return actual unique ID
     */
    protected Long uniqueID(URI eURI, EObject eObject, Long uID) {
        //
        // Get Map of cached ID for given URI
        //
        Map<EObject,Long> eCachedIDs = eCachedIDMap.get(eURI);        
        //
        // Found cached IDs?
        //
        if(eCachedIDs!=null) {            
            //
            // Check if an equal ID value is already cached for given object
            //
            if(uID.equals(eCachedIDs.get(eObject))) 
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

}
