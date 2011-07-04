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

import static org.geotools.data.efeature.EFeatureUtils.eGetFeatureValues;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.data.efeature.internal.EFeatureDelegate;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;

/**
 * @author kengu - 24. juni 2011
 *
 */
public class ESimpleFeatureImpl extends SimpleFeatureImpl implements ESimpleFeature {

    /**
     * Strong cached reference to EFeature {@link EObject data object}
     */
    private EObject eObject;
    
    /**
     * Strong cached reference to EFeature {@link EFeatureInfo structure}
     */
    private EFeatureInfo eStructure;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * Default constructor
     * @param eFeature
     * @param transaction
     */
    public ESimpleFeatureImpl(EFeature eFeature, Transaction transaction) {
        this(eFeature.getStructure(),eFeature,eFeature.getID(),
                eGetFeatureValues(eFeature.getStructure(),eFeature,transaction));
    }
    
    /**
     * Unwrapping constructor
     * @param eStructure
     * @param eObject
     * @param transaction
     */
    public ESimpleFeatureImpl(EFeatureInfo eStructure, EObject eObject, Transaction transaction) {
        this(eStructure,eObject,eStructure.toID(eObject),
                eGetFeatureValues(eStructure, eObject, transaction));
    }
    
    
    /**
     * Trusted constructor
     * @param eStructure
     * @param eObject
     * @param id
     * @param values
     */
    public ESimpleFeatureImpl(EFeatureInfo eStructure, EObject eObject, String eID, List<Object> values) {
        //
        // Forward to SimpleFeatureImpl
        //
        super(values.toArray(), eStructure.getFeatureType(), 
                new FeatureIdImpl(eID), false, eStructure.eGetAttributeIndex());
        //
        // Prepare
        //
        this.eObject = eObject;
        this.eStructure = eStructure;
    }

    // ----------------------------------------------------- 
    //  ESimpleFeature implementation
    // -----------------------------------------------------
    
    @Override
    public EObject eObject() {
        //
        // Return implementation, not delegate
        //
        if(eObject instanceof EFeatureDelegate) {
            return ((EFeatureDelegate)eObject).eImpl();
        }
        return eObject;
    }
    
    @Override
    public EFeature eFeature() {
        //
        // Return delegate if not a EFeature implementation  
        //
        if(eObject instanceof EFeature) {
            return (EFeature)eObject();
        }
        return new EFeatureDelegate(eStructure, (InternalEObject)eObject, true, null);
    }   
    
    @Override
    public boolean isDetached() {
        return eStructure.eHints().eValuesDetached();
    }

    @Override
    public boolean isSingleton() {
        return eStructure.eHints().eSingletonFeatures();
    }
    
    @Override
    public List<Object> read() throws IllegalStateException {
        return read(Transaction.AUTO_COMMIT);
    }
    
    @Override
    public List<Object> read(Transaction transaction) throws IllegalStateException {
        //
        // Decide if feature values is allowed to be updated from backing store
        //
        if(!isDetached()) {
            throw new IllegalStateException("ESimpleFeature " 
                    + getType().getTypeName() + " is not detached");
        }
        //
        // Read values from eImpl()
        //
        List<Object> eValues = EFeatureUtils.eGetFeatureValues(eStructure, eObject, transaction);
        //
        // Update values
        //
        values = eValues.toArray();
        //
        // Finished
        //
        return eValues;
    }

    @Override
    public List<Object> write() throws IllegalStateException {
        return write(Transaction.AUTO_COMMIT);
    }
    
    @Override
    public List<Object> write(Transaction transaction) throws IllegalStateException {            
        //
        // Decide if feature values is allowed to be updated from backing store
        //
        if(!isDetached()) {
            throw new IllegalStateException("ESimpleFeature " 
                    + getType().getTypeName() + " is not detached");
        }
        //
        // Get values as list
        //
        List<Object> eValues = getAttributes();
        //
        // Write values to eImpl()
        //
        EFeatureUtils.eSetFeatureValues(eStructure, eObject, eValues, transaction);
        //
        // Finished
        //
        return eValues;            
    }    
    
    @Override
    public boolean isReleased() {
        return eObject == null;
    }

    @Override
    public void release() {
        eObject = null;
    }
    

}
