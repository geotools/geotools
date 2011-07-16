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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureGeometry;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureListener;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.feature.NameImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author kengu - 3. juli 2011
 *
 */
public class ESimpleFeatureInternal implements ESimpleFeature {

    private FeatureId eID;

    private EFeatureInternal eInternal;
    
    private Map<Object, Object> userData;
    
    /**
     * Cached container {@link Adapter}.
     * <p>
     * 
     * @see {@link #getEObjectAdapter()}
     */
    protected AdapterImpl eObjectlistener;
    
    /**
     * Cached {@link EFeatureListener}.
     * <p>
     * 
     * @see {@link #getStructureAdapter()}
     */
    protected EFeatureListener<?> eStructurelistener;
    
    
    /**
     * Cached {@link Feature feature} bounds.
     */
    protected ReferencedEnvelope bounds;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    public ESimpleFeatureInternal(EFeatureInternal eInternal) {
        //
        // -------------------------------------------------
        //  Cache strong reference to implementation
        // -------------------------------------------------
        //
        this.eInternal = eInternal;
        //
        // Add listeners that keep cached data in-sync with eImpl and structure
        //
        eStructure().addListener(getStructureAdapter());
        eFeature().eAdapters().add(getEObjectAdapter());
    }
    
    // ----------------------------------------------------- 
    //  ESimpleFeature implementation
    // -----------------------------------------------------
    
    @Override
    public EObject eObject() {
        return eInternal.eImpl();
    }
    
    @Override
    public EFeature eFeature() {
        return (EFeature)eInternal.eFeature();
    }
            
    @Override
    public boolean isDetached() {
        return eStructure().eHints().eValuesDetached();
    }

    @Override
    public boolean isSingleton() {
        return eStructure().eHints().eSingletonFeatures();
    }
    
    @Override
    public List<Object> read() throws IllegalStateException {
        return read(eInternal().eTx);
    }
    
    @Override
    public List<Object> read(Transaction transaction) throws IllegalStateException {
        //
        // Get properties
        //
        List<EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature>> 
            eList = eInternal.getProperties();
        //
        // Prepare to read values from properties
        //
        List<Object> eValues = new ArrayList<Object>(eList.size());
        //
        // Loop over all properties
        //
        for(EFeatureProperty<?, ? extends Property> it : eList) {
            eValues.add(it.read(eInternal().eTx));
        }
        //
        // Finished
        //
        return eValues;
    }

    @Override
    public List<Object> write() throws IllegalStateException {
        return write(eInternal().eTx);
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
        // Get properties
        //
        List<EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature>> 
            eList = eInternal.getProperties();
        //
        // Prepare to read values from properties
        //
        List<Object> eValues = new ArrayList<Object>(eList.size());
        //
        // Loop over all properties
        //
        for(EFeatureProperty<?, ? extends Property> it : eList) {
            eValues.add(it.write(eInternal().eTx));
        }
        //
        // Finished
        //
        return eValues;            
    }
           
    @Override
    public boolean isReleased() {            
        return eInternal == null;
    }

    @Override
    public void release() {
        //
        // Remove adapters
        //
        eFeature().eAdapters().remove(eObjectlistener);
        //
        // Remove structure listeners
        //
        eStructure().removeListener(eStructurelistener);
        //
        // Release all strong references to external objects
        //
        eInternal = null;
    }

    // ----------------------------------------------------- 
    //  SimpleFeature implementation
    // -----------------------------------------------------
    
    @Override
    public String getID() {
        return getIdentifier().getID();
    }
    
    @Override
    public FeatureId getIdentifier() {
        //
        // Create id?
        //
        if (eID == null) {
            //
            // Get EMF id attribute
            //
            EAttribute eIDAttribute = eStructure().eIDAttribute();
            //
            // Get feature id as string
            //
            String fid = (eIDAttribute == null || !eFeature().eIsSet(eIDAttribute) ? null
                    : EcoreUtil.convertToString(eIDAttribute.getEAttributeType(),
                            eFeature().eGet(eIDAttribute)));
            //
            // Create feature id instance
            //
            eID = new FeatureIdImpl(fid);
        }
        //
        // Finished
        //
        return eID;
    }

    @Override
    public BoundingBox getBounds() {
        // Calculate bounds?
        //
        if (bounds == null) {
            //
            // Initialize bounds
            //
            bounds = new ReferencedEnvelope(getFeatureType().getCoordinateReferenceSystem());
            //
            // Loop over all geometries
            //
            for (EFeatureGeometry<Geometry> it : eInternal.getGeometryList(Geometry.class)) {
                if (!it.isEmpty()) {
                    Geometry g = it.getValue();
                    if (bounds.isNull()) {
                        bounds.init(g.getEnvelopeInternal());
                    } else {
                        bounds.expandToInclude(g.getEnvelopeInternal());
                    }
                }
            }
        }
        return bounds;
    }

    @Override
    public GeometryAttribute getDefaultGeometryProperty() {
        //
        // Get default Geometry name
        // 
        String eName = eStructure().eGetDefaultGeometryName();
        //
        // Get EFeatureGeometry structure
        //
        EFeatureGeometry<?> eGeometry = (EFeatureGeometry<?>)eInternal.getPropertyMap().get(eName);
        //
        // Found geometry?
        //
        if (eGeometry != null) {
            // Get attribute
            //
            return eGeometry.getData();
        }
        //
        // Not found, return null;
        //
        return null;
    }

    @Override
    public void setDefaultGeometryProperty(GeometryAttribute attribute) {
        eStructure().eSetDefaultGeometryName(attribute.getName().getURI());
    }

    @Override
    public Collection<? extends Property> getValue() {
        return getProperties();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(Object newValue) {
        setValue((Collection<Property>) newValue);
    }

    @Override
    public void setValue(Collection<Property> values) {
        for (Property it : values) {
            EAttribute eAttr = eStructure().eGetAttribute(it.getName().getURI());
            eFeature().eSet(eAttr, it.getValue());
        }
    }

    @Override
    public Property getProperty(Name name) {
        return getProperties(name.getLocalPart()).iterator().next();
    }

    @Override
    public Collection<Property> getProperties(String eName) {
        Set<Property> eItems = new HashSet<Property>();
        for (Property it : getProperties()) {
            if (it.getName().getLocalPart().equals(eName)) {
                eItems.add(it);
            }
        }
        return eItems;
    }

    @Override
    public Collection<Property> getProperties(Name eName) {
        Set<Property> eItems = new HashSet<Property>();
        for (Property it : getProperties()) {
            if (it.getName().equals(eName)) {
                eItems.add(it);
            }
        }
        return eItems;
    }

    @Override
    public List<Property> getProperties() {
        // Initialize
        //
        List<Property> eList = new ArrayList<Property>();
        //
        // Loop over all EFeatureProperty instances,
        // collecting current Property instances.
        //
        for (EFeatureProperty<?, ? extends Property> it : eInternal.getProperties()) {
            eList.add(it.getData());
        }
        // Finished
        //
        return Collections.unmodifiableList(eList);
    }

    @Override
    public Property getProperty(String eName) {
        // Get instance, returns null if not found
        //
        EFeatureProperty<?, ? extends Property> eProperty = eInternal.getPropertyMap().get(eName);
        //
        // Get property instance, return null if not found
        //
        return (eProperty != null ? eProperty.getData() : null);
    }

    @Override
    public void validate() throws IllegalAttributeException {
        // Loop over all property instances,
        // calling validate on attributes
        //
        for (Property it : getProperties()) {
            ((Attribute) it).validate();
        }
    }

    @Override
    public AttributeDescriptor getDescriptor() {
        // Is top-level attribute (feature)
        return null;
    }

    @Override
    public Name getName() {
        return new NameImpl(eStructure().eName());
    }

    @Override
    public boolean isNillable() {
        return false;
    }

    @Override
    public Map<Object, Object> getUserData() {
        if (userData == null) {
            userData = new HashMap<Object, Object>();
        }
        return userData;
    }

    @Override
    public SimpleFeatureType getType() {
        return eStructure().getFeatureType();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return eStructure().getFeatureType();
    }

    @Override
    public List<Object> getAttributes() {
        //
        // Get properties
        //
        List<Property> eList = getProperties();
        //
        // Initialize value list
        //
        List<Object> values = EFeatureUtils.newList(eList.size());
        //
        // Loop over all property instances
        //
        for (Property it : eList) {
            values.add(it.getValue());
        }
        //
        // Finished
        //
        return values;
    }

    @Override
    public void setAttributes(List<Object> values) {
        setAttributes(values.toArray(new Object[0]));
    }

    @Override
    public void setAttributes(Object[] values) {
        //
        // Loop over all property instances,
        // calling validate on attributes
        //
        int i = 0;
        for (Property it : getProperties()) {
            ((Attribute) it).setValue(values[i]);
        }
    }

    @Override
    public Object getAttribute(String eName) {
        Property p = getProperty(eName);
        return (p != null ? p.getValue() : null);
    }

    @Override
    public void setAttribute(String eName, Object newValue) {
        Property p = getProperty(eName);
        if (p != null) {
            p.setValue(newValue);
        }
    }

    @Override
    public Object getAttribute(Name eName) {
        Property p = getProperty(eName);
        return (p != null ? p.getValue() : null);
    }

    @Override
    public void setAttribute(Name eName, Object newValue) {
        Property p = getProperty(eName);
        if (p != null) {
            p.setValue(newValue);
        }
    }

    @Override
    public Object getAttribute(int index) throws IndexOutOfBoundsException {
        Property p = getProperties().get(index);
        return p.getValue();
    }

    @Override
    public void setAttribute(int index, Object newValue) throws IndexOutOfBoundsException {
        Property p = getProperties().get(index);
        if (p != null) {
            p.setValue(newValue);
        }
    }

    @Override
    public int getAttributeCount() {
        return eInternal.getProperties().size();
    }

    @Override
    public Geometry getDefaultGeometry() {
        Property p = getDefaultGeometryProperty();
        return (Geometry) (p != null ? p.getValue() : null);
    }

    @Override
    public void setDefaultGeometry(Object newGeometry) {
        Property p = getDefaultGeometryProperty();
        if (p != null) {
            p.setValue(newGeometry);
        }
    }
    
    // ----------------------------------------------------- 
    //  Object equality implementation
    // -----------------------------------------------------

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getID().hashCode();
        hash = 31 * hash + getFeatureType().hashCode();
        for(Object it : getAttributes()) {
            hash = (null == it ? 0 : it.hashCode());
        }
        return hash;
    }
               
    @Override
    public boolean equals(Object obj) {
        //
        // Sanity checks
        //
        if (obj == null) {
            return false;
        }
        //
        // Same as this?
        //
        if (obj == this) {
            return true;
        }
        //
        // Not same implementation?
        //        
        if (!(obj instanceof ESimpleFeatureInternal)) {
            return false;
        }
        //
        // Cast to ESimpleFeatureInternal
        //
        ESimpleFeatureInternal eFeature = (ESimpleFeatureInternal)obj;
        //
        // Get this feature ID
        //
        String eID = getID();
        //
        // This check shouldn't really be necessary since
        // by contract, all features should have an ID.
        //
        if (getID() == null) {
            if (eFeature.getIdentifier() != null) {
                return false;
            }
        }
        //
        // Is not same feature ID?
        //
        if (!eID.equals(eFeature.getIdentifier())) {
            return false;
        }
        //
        // Is not same feature type. 
        //
        if (!eFeature.getFeatureType().equals(getFeatureType())) {
            return false;
        }
        //
        // Get attribute values
        //
        List<Object> values = getAttributes();
        //
        // Check all values for inequality
        //
        for (int i = 0, count = values.size(); i < count; i++) {
            //
            // Get attribute values
            //
            Object v1 = values.get(i);
            Object v2 = eFeature.getAttribute(i);
            //
            // Do a guarded check
            //
            if (v1 == null) {
                if (v2 != null) {
                    return false;
                }
            } else {
                if (!v1.equals(v2)) {
                    return false;
                }
            }
        }
        //
        // All values are equal
        //
        return true;        
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    /**
     * Verify that state is available
     */
    protected void verify() throws IllegalStateException
    {
        if(eStructure()==null)
            throw new IllegalStateException(this + " is not valid. " +
                        "Please specify the structure.");
        if(eFeature()==null)
            throw new IllegalStateException(this + " is released.");
    }  
    
    protected EFeatureInternal eInternal() {
        return eInternal;
    }
    
    protected EFeatureInfo eStructure() {
        return eInternal.eStructure;
    }
    
    protected void eReplace(EFeatureInternal eInternal) {
        //
        // Remove structure listener from current structure?
        //
        if(!eStructure().eEqualTo(eInternal.eStructure)) {
            eStructure().removeListener(getStructureAdapter());
        }
        //
        // Remove data adapter structure
        //
        eFeature().eAdapters().remove(getEObjectAdapter());
        //
        //  Replace strong reference.
        //
        this.eInternal = eInternal;
        //
        // Add call-backs that keep caches in-sync with structure and data
        //
        eStructure().addListener(getStructureAdapter());
        eFeature().eAdapters().add(getEObjectAdapter()); 
    }

    
    // ----------------------------------------------------- 
    //  Methods for keeping cached data in-sync
    // -----------------------------------------------------
    
    /**
     * Cached {@link EFeatureListener} which monitors changes made to {@link EFeatureInfo}.
     * <p>
     * On each change, this adapter determines if any data cached by this instance should be
     * invalidated. The following conditions invoke invalidation of data:
     * <ol>
     * <li>{@link #bounds} is invalidated after {@link #setSRID(String) spatial reference ID} is
     * changed</li>
     * </ol>
     * 
     * @see {@link #setSRID(String)} - forwarded to {@link EFeatureInfo#setSRID(String)}
     * @see {@link EFeatureInfo#setSRID(String)} - invalidates the
     *      {@link FeatureType#getCoordinateReferenceSystem() CRS} of all {@link Feature} instances
     *      contained by {@link EFeature}s with the same structure as this.
     * @return a lazily cached {@link Adapter} instance.
     */
    protected Adapter getEObjectAdapter() {
        if (eObjectlistener == null) {
            eObjectlistener = new AdapterImpl() {

                @Override
                public void notifyChanged(Notification msg) {

                    Object feature = msg.getFeature();
                    if (msg.getEventType() == Notification.SET
                            && (msg.getNewValue() instanceof Geometry)
                            && (msg.getFeature() instanceof EStructuralFeature)) {
                        // Check if a geometry is changed. If it is, bounds
                        // must be re-calculated...
                        String eName = ((EStructuralFeature) feature).getName();
                        if (eStructure().isGeometry(eName)) {
                            // Reset bounds. This forces bounds of this
                            // feature to be recalculated on next call
                            // to SimpleFeatureDelegate#getBounds()
                            bounds = null;

                        }
                    }
                }
            };
        }
        return eObjectlistener;
    }        

    /**
     * Cached {@link Adapter} which monitors changes made to the {@link EObject} instance this
     * delegates to.
     * <p>
     * On each change, this adapter determines if any data cached by this instance should be
     * invalidated. The following conditions invoke invalidation of data:
     * <ol>
     * <li>{@link #bounds} is invalidated after a {@link #getValue() geometry} change</li>
     * </ol>
     * 
     * @return a lazily cached {@link EStructuralFeature} instance.
     */
    protected EFeatureListener<?> getStructureAdapter() {
        if (eStructurelistener == null) {
            eStructurelistener = new EFeatureListener<Object>() {

                        @Override
                        public boolean onChange(Object source, 
                                int property, Object oldValue, Object newValue) {
                            //
                            // Dispatch on property type
                            //
                            if (property == EFeaturePackage.EFEATURE__SRID) {
                                //
                                // ---------------------------------------
                                //  Current bounds have wrong CRS.
                                // ---------------------------------------
                                //  This forces current bounds
                                //  to be recalculated on next call
                                //  to getData().getBounds()
                                //
                                bounds = null;
                                //
                                // Notify
                                //
                                EFeatureInternal.eNotify((InternalEObject)eObject(),EFeaturePackage.EFEATURE__SRID, oldValue, newValue);
                                
                            }
                            //
                            // Referenced EObject structure changed?
                            //
                            else if ((source == eFeature()) 
                                    && (property == EFeaturePackage.EFEATURE__STRUCTURE)) {
                                //
                                // ---------------------------------------
                                //  Current structure has been switched
                                // ---------------------------------------
                                //
                                //  1) Remove listener from structure 
                                //
                                ((EFeatureInfo)oldValue).removeListener(this);
                                //
                                // Add listener to new structure
                                //
                                ((EFeatureInfo)newValue).addListener(this);
                                //
                                //  This forces current bounds
                                //  to be recalculated on next call
                                //  to getData().getBounds()
                                //
                                bounds = null;
                            }
                            //
                            // Always allowed
                            //
                            return true;
                        }

                    };
        }
        return eStructurelistener;
    }
    
            
}