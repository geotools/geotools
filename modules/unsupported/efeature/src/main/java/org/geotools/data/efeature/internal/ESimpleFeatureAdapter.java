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
package org.geotools.data.efeature.internal;

import static org.geotools.data.efeature.internal.EFeatureInternal.eInternal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This class implements a two step algorithm which adapts
 * a {@link SimpleFeature} to a {@link ESimpleFeature}
 * 
 * @author kengu - 30. juni 2011
 *
 *
 * @source $URL$
 */
public final class ESimpleFeatureAdapter {
    
    private String oldSRID;
    
    private String newSRID;

    private FeatureType oldType;

    private FeatureType newType;

    private MathTransform transform;

    private CoordinateReferenceSystem oldCRS;

    private CoordinateReferenceSystem newCRS;

    private boolean isIdentity;

    private List<Object> oldValues = new ArrayList<Object>();
    private List<Object> newValues = new ArrayList<Object>();
    private Map<EAttribute, Object> oldValueMap = new HashMap<EAttribute, Object>();;
    private Map<EAttribute, Object> newValueMap = new HashMap<EAttribute, Object>();;
    
    // ----------------------------------------------------- 
    //  ESimpleFeatureAdapter methods
    // -----------------------------------------------------

    /**
     * Adapt given EFeature {@link EObject data} into a {@link ESimpleFeature} instance.
     * <p>
     * This method writes adapted data to {@link EObject} and then calls {@link EFeature#getData()}.
     * </p>
     * @param eStructure - structure to adapt 
     * @param eObject - EFeature {@link EObject data} to adapt
     * @param transaction - any adaptation is written to this transaction 
     * @return a {@link ESimpleFeature} instance with the given structure.
     */
    public ESimpleFeature eAdapt(EFeatureInfo eStructure, EObject eObject, Transaction transaction) {
        //
        // Prepare
        //
        ESimpleFeature eData = null;
        //
        // Get current notification delivery state
        //
        boolean eDeliver = eObject.eDeliver();
        //
        // Disable notifications
        //
        eObject.eSetDeliver(false);
        //
        // Get internal EFeature implementation
        //
        EFeatureInternal eInternal = eInternal(eStructure, eObject);
        //
        // Enter modification mode
        //
        eInternal.enter(transaction);
        //
        // Try to set values
        //        
        try {                        
            //
            // Update SRID for all instances?
            //
            if (!isIdentity) {
                eStructure.setSRID(newSRID);
            }
            //
            // Update delegate directly without any
            // additional validation, since it is
            // already established that the data is
            // valid.
            //
            for (Entry<EAttribute, Object> it : newValueMap.entrySet()) {
                eObject.eSet(it.getKey(), it.getValue());
            }
            //
            // Finished
            //
            return (eData = eInternal.getData(transaction));

        } finally {
            //
            // Leave modification mode
            //
            eInternal.leave();
            //
            // Restore notification delivery state
            //
           eObject.eSetDeliver(eDeliver);
           //
           // Notify?
           //
           if(eData!=null) {
               eNotify(eObject, eData);
           }
           
        }

    }
    
    /**
     * Adapt given EFeature {@link SimpleFeature data} into a {@link ESimpleFeature} instance.
     * </p>
     * @param eStructure - {@link EFeature} structure of given {@link EObject}
     * @param eObject - {@link EObject} backing given {@link SimpleFeature data}. If null, 
     * @param eData - {@link SimpleFeature data} attached to given {@link EObject}
     * @param transaction - any changes are written to this transaction
     * @return updated {@link ESimpleFeature} instance.
     */
    public ESimpleFeature eAdapt(EFeatureInfo eStructure, 
            ESimpleFeature eData, Transaction transaction) {
        //
        // Get EObject
        //
        EObject eObject = eData.eObject();
        //
        // Get current notification delivery state
        //
        boolean eDeliver = eObject.eDeliver();
        //
        // Get internal EFeature implementation
        //
        EFeatureInternal eInternal = eInternal(eStructure, eObject);
        //
        // Disable notifications
        //
        eObject.eSetDeliver(false);
        //
        // Ensure that any changes are written to given transaction
        //
        eInternal.enter(transaction);
        //
        // Try to set values
        //        
        try {                        
            //
            // Update feature directly without any
            // additional validation, since it is
            // already established that the data is
            // valid.
            //
            eData.setAttributes(newValues);
            //
            // Update SRID for all instances?
            //
            if (!isIdentity) {
                eStructure.setSRID(newSRID);
            }
            //
            // Finished
            //
            return eData;

        } finally {
            //
            // Leave modification mode
            //
            eInternal.leave();
            //
            // Restore notification delivery state
            //
            eObject.eSetDeliver(eDeliver);
            //
            // Notify if changed
            //
            eNotify(eObject, eData);
        }

    }        
    
    // ----------------------------------------------------- 
    //  Construction methods
    // -----------------------------------------------------
    
    /**
     * Attempts to transform new data into valid form
     * </p>
     * @param eStructure - given EFeature {@link EFeatureInfo structure}
     * @param eImpl - {@link EObject} containing EFeature data
     * @param eData - {@link Feature} data to prepare for adaption into given structure
     * @return a new {@link ESimpleFeatureAdapter} instance
     */
    public static ESimpleFeatureAdapter create(
            EFeatureInfo eStructure, 
            EObject eImpl, Feature eData) {
        //
        // Verify that given data is valid
        //
        EFeatureStatus s;
        if (!(s = eStructure.validate(eData)).isSuccess()) {
            throw new IllegalArgumentException(s.getMessage());
        }
        //
        // Prepare transformation
        //
        ESimpleFeatureAdapter eAdapter = new ESimpleFeatureAdapter();
        //
        // Get new and old feature types
        //
        eAdapter.oldType = eStructure.getFeatureType();
        eAdapter.newType = eData.getType();
        //
        // Get old and new CRS
        //
        eAdapter.oldCRS = eAdapter.oldType.getCoordinateReferenceSystem();
        eAdapter.newCRS = eAdapter.newType.getCoordinateReferenceSystem();
        //
        // Get transformation
        //
        try {
            eAdapter.transform = CRS.findMathTransform(
                    eAdapter.newCRS, eAdapter.oldCRS, true);
        } catch (FactoryException e) {
            throw new IllegalArgumentException("Tranform from " + "'" 
                    + eAdapter.newCRS + "' to '" + eAdapter.oldCRS 
                    + "' not possible");
        }
        //
        // Is identity transform?
        //
        eAdapter.isIdentity = (eAdapter.transform instanceof IdentityTransform);
        if (!eAdapter.isIdentity) {
            //
            // Get SRIDs
            //
            eAdapter.oldSRID = CRS.toSRS(eAdapter.oldCRS, true);
            eAdapter.newSRID = CRS.toSRS(eAdapter.newCRS, true);
        }
        //
        // Prepare feature values, catching
        // any transformation errors before any
        // changes are committed to the model
        //
        for (Property it : eData.getProperties()) {
            //
            // Get attribute, null indicates that it does not exist
            // in the structure of this EFeature instance. If so,
            // just discard it (in line with using structures as filters)
            //
            String eName = it.getName().getLocalPart();
            EAttribute eAttribute = eStructure.eGetAttribute(eName);
            //
            // EAttribute found in this structure.
            //
            if (eAttribute != null) {
                //
                // Get value
                //
                Object value = it.getValue();
                //
                // Cache old value
                //
                eAdapter.oldValues.add(value);
                eAdapter.oldValueMap.put(eAttribute, value);
                //
                // Adapt value?
                //
                if (value instanceof Geometry) {
                    try {
                        value = JTS.transform((Geometry) value, eAdapter.transform);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to " 
                                + "transform geometry: " + it);
                    }
                }
                //
                // Cache new value
                //
                eAdapter.newValues.add(value);
                eAdapter.newValueMap.put(eAttribute, value);
            }
        }
        //
        // Finished
        //
        return eAdapter;
    }        
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected void eNotify(EObject eObject, ESimpleFeature eData) {
        //
        // Any values changed?
        //
        if(!oldValues.equals(newValues)) {
            eNotify((InternalEObject)eObject, EFeaturePackage.EFEATURE__DATA, oldValues, eData);
        }
        if (!isIdentity) {
            eNotify((InternalEObject)eObject, EFeaturePackage.EFEATURE__SRID, oldSRID, newSRID);                    
        }
    }

    protected void eNotify(InternalEObject eObject, int feature, Object oldValue, Object newValue) {
        if (eObject.eNotificationRequired()) {
            eObject.eNotify(
                    new ENotificationImpl(eObject, Notification.SET,
                            feature, oldValue, newValue));
        }
        
    }    
    
}
