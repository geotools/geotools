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

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Interface for creation of EFeature ID attribute values
 *  
 * @author kengu - 26. mai 2011 
 *
 *
 * @source $URL$
 */
public interface EFeatureIDFactory {

    /**
     * Get ID prefix.
     */
    public String getPrefix();
    
    /**
     * Check if this {@link EFeatureIDFactory factory} 
     * contains IDs for given {@link Resource#getURI() URI}.
     * @param eURI - the {@link Resource#getURI() URI}.
     * @return <code>true</code> if IDs exists.
     */
    public boolean contains(URI eURI);
    
    /**
     * Check if this {@link EFeatureIDFactory factory} 
     * contains an ID for given {@link EObject object}.
     * @param eObject - the {@link EObject} instance.
     * @return <code>true</code> if an ID exists.
     */
    public boolean contains(EObject eObject);
    
    /**
     * Get unique {@link EFeature#getID() ID} for given EObject.
     * @param eObject - given {@link EObject} instance.
     * @return an {@link EFeature} {@link EFeature#getID() ID} if found, 
     * <code>null</code> otherwise.
     * @throws IllegalArgumentException If the factory does not create IDs for given {@link EObject}
     * @throws IllegalStateException If given {@link EObject} is not added to an EMF {@link Resource}. 
     */
    public String getID(EObject eObject);
    
    /**
     * Get {@link EObject} to {@link EFeature#getID()} mapping for given {@link URI}
     * @param eURI - given {@link URI}
     * @return a {@link Map} of IDs for given {@link URI}. If this does not contain any
     * IDs for given URI, an empty map is returned.
     */
    public Map<EObject,String> getIDMap(URI eURI);
    
    /**
     * Check if this factory create IDs for given {@link EFeature} instance  
     * @param eObject - a {@link EFeature} instance or a {@link EObject} containing 
     * {@link EFeature} compatible data. 
     * @return <code>true</code> if this factory create IDs for 
     * given {@link EFeature} instance.
     */
    public boolean creates(EObject eObject);
    
    /**
     * Check if this factory create IDs for given {@link EAttribute} instance  
     * @param eAttribute - the {@link EClass} implementing 
     * {@link EFeature}, or contains {@link EFeature} compatible data.
     * @return <code>true</code> if this factory create IDs for 
     * given {@link EClass} instance.
     */
    public boolean creates(EAttribute eAttribute);
    
    /**
     * Check if this factory create IDs for given {@link EClass} instance  
     * @param eClass - the {@link EClass} implementing 
     * {@link EFeature}, or contains {@link EFeature} compatible data.
     * @return <code>true</code> if this factory create IDs for 
     * given {@link EAttribute} instance.
     */
    public boolean creates(EClass eClass);
    
    /**
     * Add {@link EAttribute ID attribute} in {@link EObject} to factory
     * @param eObject - a {@link EFeature} instance or a {@link EObject} containing 
     * {@link EFeature} compatible data.
     * @return the {@link EClass#getEIDAttribute() ID attribute} 
     * @throws IllegalArgumentException If {@link EObject} does not contain an 
     * attribute with {@link String} data.
     */
    public EAttribute add(EObject eObject) throws IllegalArgumentException;
    
    /**
     * Add {@link EAttribute ID attribute} in {@link EClass} to factory.
     * <p>
     * If the class does not contain any,
     * {@link EClass#getEIDAttribute() ID attribute} the first changeable 
     * attribute with {@link String} data is selected.  
     * @param eClass - the {@link EClass} implementing 
     * {@link EFeature}, or contains {@link EFeature} compatible data.
     * @return the {@link EClass#getEIDAttribute() ID attribute} 
     * @throws IllegalArgumentException If {@link EClass} does not contain 
     * any changeable attributes with {@link String} data.
     */
    public EAttribute add(EClass eClass) throws IllegalArgumentException;
    
    /**
     * Add {@link EAttribute} to factory.
     * @param eClass - the {@link EClass} implementing 
     * {@link EFeature}, or contains {@link EFeature} compatible data.
     * @param eID - any {@link EAttribute} instance contained in 'eClass'
     * @return the {@link EClass#getEIDAttribute() ID attribute} 
     * @throws IllegalArgumentException If {@link EAttribute} is 
     * unchangeable or does not contain {@link String} data.
     */
    public void add(EClass eClass, EAttribute eID);      
    
    /**
     * Create a unique {@link EFeature#getID() EFeature ID} for given {@link EObject} instance.
     * <p>
     * @param eObject - a {@link EFeature} instance or a {@link EObject} containing 
     * {@link EFeature} compatible data. 
     * @return a new ID not already created, or the already created value.
     * @throws IllegalArgumentException If the factory does not create IDs for given {@link EObject}
     * @throws IllegalStateException If given {@link EObject} is not added to an EMF {@link Resource} 
     */
    public String createID(EObject eObject) throws IllegalArgumentException, IllegalStateException;
       
    /**
     * Use given ID as a unique {@link EFeature#getID() EFeature ID} for given {@link EObject} instance.
     * @param eObject - a {@link EFeature} instance or a {@link EObject} containing 
     * {@link EFeature} compatible data. 
     * @param eID - the {@link EObject} ID value. 
     * @return same ID if unique, new unique ID otherwise. 
     * @throws IllegalArgumentException If the factory does not create IDs for given {@link EObject}
     */
    public String useID(EObject eObject, String eID) throws IllegalArgumentException, IllegalStateException;

    /**
     * Dispose ID for given {@link EObject} instance.
     * @param eObject - a {@link EFeature} instance or a {@link EObject} containing 
     * {@link EFeature} compatible data. 
     * @return the objects unique ID
     * @throws IllegalArgumentException If the factory contains no ID for given {@link EObject}
     */
    public String disposeID(EObject eObject) throws IllegalArgumentException;
            
}
