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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureHints;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.ESimpleFeature;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

/**
 * This class implements {@link ESimpleFeature} by delegating 
 * to a {@link Feature} instance.
 * <p>
 * The references to delegate objects are weak, allowing 
 * the them to be garbage collected when needed.
 * </p>
 * 
 * @author kengu - 28. mai 2011 
 *
 *
 * @source $URL$
 */
public class ESimpleFeatureDelegate implements ESimpleFeature {
    
    private EObject eObject;
    private SimpleFeature feature;
    private EFeatureInfo eStructure;
    private EFeatureHints eHints;


    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * This constructor creates a {@link ESimpleFeature} instance that
     * delegates to given objects.
     */
    public ESimpleFeatureDelegate(EFeatureInfo eStructure, EObject eObject, SimpleFeature feature, EFeatureHints eHints) {
        this.feature = feature;
        this.eObject = eObject;
        this.eStructure = eStructure;
        this.eHints = eHints == null ? new EFeatureHints(eStructure.eHints()) : eHints;
    }

    // ----------------------------------------------------- 
    //  ESimpleFeature implementation
    // -----------------------------------------------------
    
    @Override
    public EObject eObject() {
        if(eObject instanceof EFeatureDelegate) {
            return ((EFeatureDelegate)eObject).eImpl;
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
        return new EFeatureDelegate(eStructure, (InternalEObject)eObject, false, eHints);
    }    

    @Override
    public boolean isDetached() {
        return eHints.eValuesDetached();
    }

    @Override
    public boolean isSingleton() {
        return eHints.eSingletonFeatures();
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
        // Update feature values
        //
        feature.setAttributes(eValues);        
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
        // Get feature values
        //
        List<Object> eValues = feature.getAttributes();
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
    // ----------------------------------------------------- 
    //  SimpleFeature implementation
    // -----------------------------------------------------
    
    @Override
    public FeatureId getIdentifier() {
        return getFeature().getIdentifier();
    }

    @Override
    public AttributeDescriptor getDescriptor() {
        return getFeature().getDescriptor();
    }

    @Override
    public BoundingBox getBounds() {
        return getFeature().getBounds();
    }

    @Override
    public String getID() {
        return getFeature().getID();
    }

    @Override
    public SimpleFeatureType getType() {
        return getFeature().getType();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return getFeature().getFeatureType();
    }

    @Override
    public void setValue(Object newValue) {
        getFeature().setValue(newValue);
    }

    @Override
    public List<Object> getAttributes() {
        return getFeature().getAttributes();
    }

    @Override
    public GeometryAttribute getDefaultGeometryProperty() {
        return getFeature().getDefaultGeometryProperty();
    }

    @Override
    public void setValue(Collection<Property> values) {
        getFeature().setValue(values);
    }

    @Override
    public void setAttributes(List<Object> values) {
        getFeature().setAttributes(values);
    }

    @Override
    public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {
        getFeature().setDefaultGeometryProperty(geometryAttribute);
    }

    @Override
    public Collection<? extends Property> getValue() {
        return getFeature().getValue();
    }

    @Override
    public Collection<Property> getProperties(Name name) {
        return getFeature().getProperties(name);
    }

    @Override
    public void setAttributes(Object[] values) {
        getFeature().setAttributes(values);
    }

    @Override
    public Name getName() {
        return getFeature().getName();
    }

    @Override
    public Property getProperty(Name name) {
        return getFeature().getProperty(name);
    }

    @Override
    public Object getAttribute(String name) {
        return getFeature().getAttribute(name);
    }

    @Override
    public boolean isNillable() {
        return getFeature().isNillable();
    }

    @Override
    public void setAttribute(String name, Object value) {
        getFeature().setAttribute(name, value);
    }

    @Override
    public Map<Object, Object> getUserData() {
        return getFeature().getUserData();
    }

    @Override
    public Object getAttribute(Name name) {
        return getFeature().getAttribute(name);
    }

    @Override
    public Collection<Property> getProperties(String name) {
        return getFeature().getProperties(name);
    }

    @Override
    public void setAttribute(Name name, Object value) {
        getFeature().setAttribute(name, value);
    }

    @Override
    public Collection<Property> getProperties() {
        return getFeature().getProperties();
    }

    @Override
    public Property getProperty(String name) {
        return getFeature().getProperty(name);
    }

    @Override
    public Object getAttribute(int index) throws IndexOutOfBoundsException {
        return getFeature().getAttribute(index);
    }

    @Override
    public void setAttribute(int index, Object value) throws IndexOutOfBoundsException {
        getFeature().setAttribute(index, value);
    }

    @Override
    public void validate() throws IllegalAttributeException {
        getFeature().validate();
    }

    @Override
    public int getAttributeCount() {
        return getFeature().getAttributeCount();
    }

    @Override
    public Object getDefaultGeometry() {
        return getFeature().getDefaultGeometry();
    }

    @Override
    public void setDefaultGeometry(Object geometry) {
        getFeature().setDefaultGeometry(geometry);
    }

    // ----------------------------------------------------- 
    //  Object equality implementation
    // -----------------------------------------------------

    @Override
    public int hashCode() {
        return feature.hashCode();        
    }
               
    @Override
    public boolean equals(Object obj) {
        return feature.equals(obj);        
    }    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected SimpleFeature getFeature() {
        return feature;
    }    

}
