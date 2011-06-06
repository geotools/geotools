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

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.EFeature;
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
 */
public class ESimpleFeatureDelegate implements ESimpleFeature {
    
    private WeakReference<EObject> eObject;
    private WeakReference<SimpleFeature> feature;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * This constructor creates a {@link ESimpleFeature} instance that
     * delegates to given objects.
     */
    public ESimpleFeatureDelegate(EObject eObject, SimpleFeature feature) {
        this.eObject = new WeakReference<EObject>(eObject);
        this.feature = new WeakReference<SimpleFeature>(feature);
    }

    // ----------------------------------------------------- 
    //  ESimpleFeature implementation
    // -----------------------------------------------------
    
    public EObject eObject() {
        EObject eObject = this.eObject.get();
        if(eObject instanceof EFeatureDelegate) {
            eObject = ((EFeatureDelegate)eObject).eDelegate.get();
        }
        return eObject;
    }
    
    public EFeature eFeature() {
        EObject eObject = eObject();
        if(eObject instanceof EFeature) {
            return (EFeature)eObject;
        }
        return null;
    }    
    
    // ----------------------------------------------------- 
    //  SimpleFeature implementation
    // -----------------------------------------------------
    
    public FeatureId getIdentifier() {
        return getFeature().getIdentifier();
    }

    public AttributeDescriptor getDescriptor() {
        return getFeature().getDescriptor();
    }

    public BoundingBox getBounds() {
        return getFeature().getBounds();
    }

    public String getID() {
        return getFeature().getID();
    }

    public SimpleFeatureType getType() {
        return getFeature().getType();
    }

    public SimpleFeatureType getFeatureType() {
        return getFeature().getFeatureType();
    }

    public void setValue(Object newValue) {
        getFeature().setValue(newValue);
    }

    public List<Object> getAttributes() {
        return getFeature().getAttributes();
    }

    public GeometryAttribute getDefaultGeometryProperty() {
        return getFeature().getDefaultGeometryProperty();
    }

    public void setValue(Collection<Property> values) {
        getFeature().setValue(values);
    }

    public void setAttributes(List<Object> values) {
        getFeature().setAttributes(values);
    }

    public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {
        getFeature().setDefaultGeometryProperty(geometryAttribute);
    }

    public Collection<? extends Property> getValue() {
        return getFeature().getValue();
    }

    public Collection<Property> getProperties(Name name) {
        return getFeature().getProperties(name);
    }

    public void setAttributes(Object[] values) {
        getFeature().setAttributes(values);
    }

    public Name getName() {
        return getFeature().getName();
    }

    public Property getProperty(Name name) {
        return getFeature().getProperty(name);
    }

    public Object getAttribute(String name) {
        return getFeature().getAttribute(name);
    }

    public boolean isNillable() {
        return getFeature().isNillable();
    }

    public void setAttribute(String name, Object value) {
        getFeature().setAttribute(name, value);
    }

    public Map<Object, Object> getUserData() {
        return getFeature().getUserData();
    }

    public Object getAttribute(Name name) {
        return getFeature().getAttribute(name);
    }

    public Collection<Property> getProperties(String name) {
        return getFeature().getProperties(name);
    }

    public void setAttribute(Name name, Object value) {
        getFeature().setAttribute(name, value);
    }

    public Collection<Property> getProperties() {
        return getFeature().getProperties();
    }

    public Property getProperty(String name) {
        return getFeature().getProperty(name);
    }

    public Object getAttribute(int index) throws IndexOutOfBoundsException {
        return getFeature().getAttribute(index);
    }

    public void setAttribute(int index, Object value) throws IndexOutOfBoundsException {
        getFeature().setAttribute(index, value);
    }

    public void validate() throws IllegalAttributeException {
        getFeature().validate();
    }

    public int getAttributeCount() {
        return getFeature().getAttributeCount();
    }

    public Object getDefaultGeometry() {
        return getFeature().getDefaultGeometry();
    }

    public void setDefaultGeometry(Object geometry) {
        getFeature().setDefaultGeometry(geometry);
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected SimpleFeature getFeature() {
        return feature.get();
    }    

}
