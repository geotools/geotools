package org.geotools.data.efeature.internal;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Internal;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureAttribute;
import org.geotools.data.efeature.EFeatureGeometry;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.impl.EFeatureImpl;
import org.geotools.data.efeature.util.EFeatureAttributeList;
import org.geotools.data.efeature.util.EFeatureGeometryList;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;

import com.vividsolutions.jts.geom.Geometry;

/**
 * {@link EFeature} delegate class.
 * <p>
 * This class implements {@link EFeature} by delegating to a
 * EMF {@link InternalEObject object}. The object is mapped to
 * {@link EFeature} by applying a {@link EFeatureInfo structure} 
 * to it.
 * <p>
 * It is only able to delegate to a {@link InternalEObject object} 
 * if it's {@link EFeatureInfo#validate(EObject) structure is valid}.  
 * </p>
 */
public class EFeatureDelegate implements EFeature, InternalEObject {

    /**
     * Cached {@link InternalEObject} which this delegates to.
     * It is cached using a weak reference which allows garbage 
     * collections when references objects is no longer in use.
     */
    protected WeakReference<InternalEObject> eDelegate;
    
    /**
     * Cached {@link EFeatureInternal} which this delegates to.
     */
    protected EFeatureInternal eImpl;
    
    /**
     * Cached {@link EFeatureInternal} which this delegates to.
     * <p>
     * It is cached using a weak reference which allows garbage 
     * collections when references objects is no longer in use.
     */
    protected WeakReference<EFeatureInternal> eImplRef;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * Explicit-configuring delegate constructor.
     * <p>
     * This class 
     * <p>
     * <b>Note</b>: This constructor verifies the structure. 
     * If invalid, a {@link IllegalArgumentException} is thrown.
     * </p>
     * @param eStructure - {@link EFeatureInfo structure} instance.
     * @param eDelegate - {@link EObject} instance which implements {@link EFeature}.
     * @throws IllegalArgumentException If the {@link EFeatureInfo structure} of this feature
     *         {@link EFeatureInfo#validate(EPackage, EClass) fails to validate}.
     */
    public EFeatureDelegate(EFeatureInfo eStructure, InternalEObject eDelegate)
            throws IllegalArgumentException {
        //
        // Is known implementation?
        //
        if(eDelegate instanceof EFeatureImpl) {
            //
            //  Get internal from EFeatureImpl
            //                        
            this.eImplRef = new WeakReference<EFeatureInternal>(((EFeatureImpl)eDelegate).eImpl());            
        } 
        else if(eDelegate instanceof EFeatureDelegate) {
            //
            //  Get internal from delegate
            //                        
            this.eImplRef = new WeakReference<EFeatureInternal>(((EFeatureDelegate)eDelegate).eImpl());            
        } 
        else {
            //
            // Save delegate 
            //            
            this.eDelegate = new WeakReference<InternalEObject>(eDelegate);
            //
            // Create new internal (owned by this, use hard reference) 
            //            
            this.eImpl = new EFeatureInternal(eStructure,this);            
        }
    }

    // ----------------------------------------------------- 
    //  EFeatureDelegate methods
    // -----------------------------------------------------
    
    protected InternalEObject eDelegate() {
        return eDelegate!=null ? eDelegate.get() : eImpl().eImpl();
    }
    
    protected EFeatureInternal eImpl() {
        return eImpl!=null ? eImpl : eImplRef.get();
    }
    
    // ----------------------------------------------------- 
    //  EFeature delegation
    // -----------------------------------------------------
    
    
    public String getID() {
        return eImpl().getID();
    }
    
    public void setID(String value) {
        eImpl().setID(value);
    }

    public String getSRID() {
        return eImpl().getSRID();
    }

    public String getDefault() {
        return eImpl().getDefault();
    }

    public EFeatureInfo getStructure() {
        return eImpl().getStructure();
    }

    public <V extends Geometry> EFeatureGeometryList<V> getGeometryList(Class<V> valueType) {
        return eImpl().getGeometryList(valueType);
    }

    @Override
    public boolean equals(Object arg0) {
        return eImpl().equals(arg0);
    }

    public Feature getData() {
        return eImpl().getData();
    }

    public <V> EFeatureAttributeList<V> getAttributeList(Class<V> valueType) {
        return eImpl().getAttributeList(valueType);
    }

    @Override
    public int hashCode() {
        return eImpl().hashCode();
    }

    public void setSRID(String newSRID) {
        eImpl().setSRID(newSRID);
    }

    public void setData(Feature newData) {
        eImpl().setData(newData);
    }

    public boolean isSimple() {
        return eImpl().isSimple();
    }

    public void setDefault(String newDefault) {
        eImpl().setDefault(newDefault);
    }

    public void setStructure(EFeatureInfo eStructure) {
        eImpl().setStructure(eStructure);
    }

    public EFeatureProperty<?, ? extends Property> newProperty(String eName, Class<?> type) {
        return eImpl().newProperty(eName, type);
    }

    public <V> EFeatureAttribute<V> newAttribute(String eName, Class<V> type) {
        return eImpl().newAttribute(eName, type);
    }

    public <T extends Geometry> EFeatureGeometry<T> newGeometry(String eName, Class<T> type) {
        return eImpl().newGeometry(eName, type);
    }

    // ----------------------------------------------------- 
    //  InternalEObject delegate implementation
    // -----------------------------------------------------

    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case EFeaturePackage.EFEATURE__SRID:
            return getSRID();
        case EFeaturePackage.EFEATURE__DATA:
            return getData();
        case EFeaturePackage.EFEATURE__SIMPLE:
            return isSimple();
        case EFeaturePackage.EFEATURE__DEFAULT:
            return getDefault();
        case EFeaturePackage.EFEATURE__STRUCTURE:
            return getStructure();
        }
        return eDelegate().eGet(featureID, resolve, coreType);
    }

    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case EFeaturePackage.EFEATURE__SRID:
            setSRID((String) newValue);
            return;
        case EFeaturePackage.EFEATURE__DATA:
            setData((Feature) newValue);
            return;
        case EFeaturePackage.EFEATURE__DEFAULT:
            setDefault((String) newValue);
            return;
        }
        eDelegate().eSet(featureID, newValue);
    }

    public void eUnset(int featureID) {
        switch (featureID) {
        case EFeaturePackage.EFEATURE__SRID:
            setSRID(EFeatureInternal.SRID_EDEFAULT);
            return;
        case EFeaturePackage.EFEATURE__DATA:
            setData(EFeatureInternal.DATA_EDEFAULT);
            return;
        case EFeaturePackage.EFEATURE__DEFAULT:
            setDefault(EFeatureInternal.DEFAULT_EDEFAULT);
            return;
        }
        eDelegate().eUnset(featureID);
    }

    public boolean eIsSet(int featureID) {
        return eDelegate().eIsSet(featureID);
    }

    public EList<Adapter> eAdapters() {
        return eDelegate().eAdapters();
    }

    public boolean eDeliver() {
        return eDelegate().eDeliver();
    }

    public boolean eNotificationRequired() {
        return eDelegate().eNotificationRequired();
    }

    public void eSetDeliver(boolean deliver) {
        eDelegate().eSetDeliver(deliver);
    }

    public void eNotify(Notification notification) {
        eDelegate().eNotify(notification);
    }

    public String eURIFragmentSegment(EStructuralFeature eFeature, EObject eObject) {
        return eDelegate().eURIFragmentSegment(eFeature, eObject);
    }

    public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
        return eDelegate().eObjectForURIFragmentSegment(uriFragmentSegment);
    }

    public void eSetClass(EClass eClass) {
        eDelegate().eSetClass(eClass);
    }

    public EClass eClass() {
        return eDelegate().eClass();
    }

    public Setting eSetting(EStructuralFeature feature) {
        return eDelegate().eSetting(feature);
    }

    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        return eDelegate().eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    public Resource eResource() {
        return eDelegate().eResource();
    }

    public int eContainerFeatureID() {
        return eDelegate().eContainerFeatureID();
    }

    public EObject eContainer() {
        return eDelegate().eContainer();
    }

    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        return eDelegate().eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
        return eDelegate().eDerivedOperationID(baseOperationID, baseClass);
    }

    public EStructuralFeature eContainingFeature() {
        return eDelegate().eContainingFeature();
    }

    public NotificationChain eSetResource(Internal resource, NotificationChain notifications) {
        return eDelegate().eSetResource(resource, notifications);
    }

    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID,
            Class<?> baseClass, NotificationChain notifications) {
        return eDelegate().eInverseAdd(otherEnd, featureID, baseClass, notifications);
    }

    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID,
            Class<?> baseClass, NotificationChain notifications) {
        return eDelegate().eInverseRemove(otherEnd, featureID, baseClass, notifications);
    }

    public EReference eContainmentFeature() {
        return eDelegate().eContainmentFeature();
    }

    public NotificationChain eBasicSetContainer(InternalEObject newContainer,
            int newContainerFeatureID, NotificationChain notifications) {
        return eDelegate().eBasicSetContainer(newContainer, newContainerFeatureID, notifications);
    }

    public NotificationChain eBasicRemoveFromContainer(NotificationChain notifications) {
        return eDelegate().eBasicRemoveFromContainer(notifications);
    }

    public URI eProxyURI() {
        return eDelegate().eProxyURI();
    }

    public EList<EObject> eContents() {
        return eDelegate().eContents();
    }

    public void eSetProxyURI(URI uri) {
        eDelegate().eSetProxyURI(uri);
    }

    public EObject eResolveProxy(InternalEObject proxy) {
        return eDelegate().eResolveProxy(proxy);
    }

    public InternalEObject eInternalContainer() {
        return eDelegate().eInternalContainer();
    }

    public TreeIterator<EObject> eAllContents() {
        return eDelegate().eAllContents();
    }

    public Internal eInternalResource() {
        return eDelegate().eInternalResource();
    }

    public Internal eDirectResource() {
        return eDelegate().eDirectResource();
    }

    public boolean eIsProxy() {
        return eDelegate().eIsProxy();
    }

    public EStore eStore() {
        return eDelegate().eStore();
    }

    public void eSetStore(EStore store) {
        eDelegate().eSetStore(store);
    }

    public EList<EObject> eCrossReferences() {
        return eDelegate().eCrossReferences();
    }

    public Object eGet(EStructuralFeature feature) {
        return eDelegate().eGet(feature);
    }

    public Object eGet(EStructuralFeature feature, boolean resolve) {
        return eDelegate().eGet(feature, resolve);
    }

    public void eSet(EStructuralFeature feature, Object newValue) {
        eDelegate().eSet(feature, newValue);
    }

    public boolean eIsSet(EStructuralFeature feature) {
        return eDelegate().eIsSet(feature);
    }

    public void eUnset(EStructuralFeature feature) {
        eDelegate().eUnset(feature);
    }

    public Object eGet(EStructuralFeature eFeature, boolean resolve, boolean coreType) {
        return eDelegate().eGet(eFeature, resolve, coreType);
    }

    public Object eInvoke(EOperation operation, EList<?> arguments)
    throws InvocationTargetException {
        return eDelegate().eInvoke(operation, arguments);
    }

    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        return eDelegate().eInvoke(operationID, arguments);
    }

    // ----------------------------------------------------- 
    //  Object implementation
    // -----------------------------------------------------


    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        return eImpl().toString();
    }

} // EFeatureDelegate
