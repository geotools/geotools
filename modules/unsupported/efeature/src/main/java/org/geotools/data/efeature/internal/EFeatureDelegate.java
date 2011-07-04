package org.geotools.data.efeature.internal;

import static org.geotools.data.efeature.internal.EFeatureInternal.DATA_EDEFAULT;
import static org.geotools.data.efeature.internal.EFeatureInternal.DEFAULT_EDEFAULT;
import static org.geotools.data.efeature.internal.EFeatureInternal.SRID_EDEFAULT;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
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
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureHints;
import org.geotools.data.efeature.EFeatureIDFactory;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.data.efeature.impl.EFeatureImpl;
import org.geotools.data.efeature.util.EFeatureAttributeList;
import org.geotools.data.efeature.util.EFeatureGeometryList;
import org.opengis.feature.Feature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * {@link EFeature} delegate class.
 * <p>
 * This class implements {@link EFeature} by delegating to a
 * EMF {@link InternalEObject object}. The object is mapped to 
 * {@link EFeature} by applying a {@link EFeatureInfo structure} to it.
 * <p>
 * <b>Limitations</b>
 * <p>
 * <nl>
 *      <li>It is only able to delegate to a {@link InternalEObject object} 
 *              if it's {@link EFeatureInfo#validate(EObject) structure is valid}.
 *      </li> 
 *      <li>Getter and setter methods unique to {@link InternalEObject} interface,
 *              delegates to {@link EFeature} only (see below). 
 *      </li> 
 * </nl> 
 * <p>
 * <b>InternalEObject</b>
 * <p>
 * The methods:
 * <nl>
 *      <li>{@link InternalEObject#eGet(int, boolean, boolean)} </li> 
 *      <li>{@link InternalEObject#eIsSet(int)} </li> 
 *      <li>{@link InternalEObject#eSet(int, Object)} </li> 
 *      <li>{@link InternalEObject#eUnset(int)} </li> 
 * </nl>
 * <p>
 * do not supply any {@link EClass} information. Because the {@link EClass} of 
 * {@link #eImpl()} can not in general be assumed to be a subtype of 
 * {@link EFeaturePackage#EFEATURE}, it is impossible to decide which {@link EClass} 
 * given feature ID belongs to. As a result, all these methods assume
 * that feature IDs belong to {@link EFeature} only.
 * </p>
 * @author kengu - 3. juli 2011 
 */
public class EFeatureDelegate implements EFeature, InternalEObject {

    /**
     * Cached {@link InternalEObject} which this delegates to.
     * It is cached using a strong reference prevents garbage 
     * collection to occur until all references to this delegate 
     * is released.
     */
    protected InternalEObject eImpl;
    
    /**
     * Cached {@link EFeatureInternal} which this delegates to.
     */
    protected EFeatureInternal eInternal;
    
    /**
     * Cached {@link EFeatureDelegate} singleton instance. 
     * <p>
     * This {@link ThreadLocal thread local variable} allow 
     * multiple threads to work on {@link EFeatureDelegate} 
     * singletons without the risk of any memory inconsistencies.
     * </p>
     * @see {@link EFeatureHints#EFEATURE_SINGLETON_FEATURES}
     */
    protected static ThreadLocal<EFeatureDelegate> 
        eSingleton = new ThreadLocal<EFeatureDelegate>();

    
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
     * @param eImpl - {@link EObject} instance which implements {@link EFeature}.
     * @param eTrusted - if <code>true</code>, the constructor trusts that 'eStructure'
     * is a valid structure for 'eImpl'. Use this when option to optimize construction
     * when 'eImpl' is already validated against given 'eStructure'.  
     * @param eHints TODO
     * @throws IllegalArgumentException If the {@link EFeatureInfo structure} of this feature
     *         {@link EFeatureInfo#validate(EPackage, EClass) fails to validate}.
     */
    public EFeatureDelegate(EFeatureInfo eStructure, InternalEObject eImpl, 
            boolean eTrusted, EFeatureHints eHints) throws IllegalArgumentException {
        //
        // Forward
        //
        eReplace(eStructure, eImpl, eTrusted, eHints);
    }

    // ----------------------------------------------------- 
    //  EFeatureDelegate methods
    // -----------------------------------------------------
    
    public InternalEObject eImpl() {
        return eImpl;
    }
    
    public EFeatureInternal eInternal() {
        return eInternal;
    }
    
    // ----------------------------------------------------- 
    //  EFeature delegation
    // -----------------------------------------------------
    
    
    @Override
    public String getID() {
        return eInternal().getID();
    }
    
    @Override
    public void setID(String value) {
        eInternal().eSetID(value,true);
    }

    @Override
    public String getSRID() {
        return eInternal().getSRID();
    }

    @Override
    public String getDefault() {
        return eInternal().getDefault();
    }

    @Override
    public EFeatureInfo getStructure() {
        return eInternal().getStructure();
    }

    @Override
    public <V extends Geometry> EFeatureGeometryList<V> getGeometryList(Class<V> valueType) {
        return eInternal().getGeometryList(valueType);
    }

    @Override
    public ESimpleFeature getData() {
        return eInternal().getData(Transaction.AUTO_COMMIT);
    }

    @Override
    public ESimpleFeature getData(Transaction transaction) {
        return eInternal().getData(transaction);
    }
    
    @Override
    public <V> EFeatureAttributeList<V> getAttributeList(Class<V> valueType) {
        return eInternal().getAttributeList(valueType);
    }

    @Override
    public void setSRID(String newSRID) {
        eInternal().setSRID(newSRID);
    }

    @Override
    public void setData(Feature newData) {
        eInternal().setData(newData, Transaction.AUTO_COMMIT);
    }
    
    @Override
    public ESimpleFeature setData(Feature newData, Transaction transaction) {
        return eInternal().setData(newData, transaction);
    }
    

    @Override
    public void setDefault(String newDefault) {
        eInternal().setDefault(newDefault);
    }

    @Override
    public void setStructure(EFeatureInfo eStructure) {
        eInternal().setStructure(eStructure);
    }

    // ----------------------------------------------------- 
    //  InternalEObject delegate implementation
    // -----------------------------------------------------

    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case EFeaturePackage.EFEATURE__ID:
            return getID();
        case EFeaturePackage.EFEATURE__SRID:
            return getSRID();
        case EFeaturePackage.EFEATURE__DATA:
            return getData();
        case EFeaturePackage.EFEATURE__DEFAULT:
            return getDefault();
        case EFeaturePackage.EFEATURE__STRUCTURE:
            return getStructure();
        }
        return eImpl().eGet(featureID, resolve, coreType);
    }

    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case EFeaturePackage.EFEATURE__ID:
            setID((String) newValue);
            return;
        case EFeaturePackage.EFEATURE__SRID:
            setSRID((String) newValue);
            return;
        case EFeaturePackage.EFEATURE__DATA:
            setData((Feature) newValue);
            return;
        case EFeaturePackage.EFEATURE__DEFAULT:
            setDefault((String) newValue);
            return;
        case EFeaturePackage.EFEATURE__STRUCTURE:
            setStructure((EFeatureInfo) newValue);
            return;
        }
        eImpl().eSet(featureID, newValue);
    }

    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case EFeaturePackage.EFEATURE__ID:
            setID(null);
        case EFeaturePackage.EFEATURE__SRID:
            setSRID(SRID_EDEFAULT);
            return;
        case EFeaturePackage.EFEATURE__DATA:
            setData(DATA_EDEFAULT);
            return;
        case EFeaturePackage.EFEATURE__DEFAULT:
            setDefault(DEFAULT_EDEFAULT);
            return;
        case EFeaturePackage.EFEATURE__STRUCTURE:
            setStructure(eInternal().eStructure.eContext().eStructure().eGetFeatureInfo(eImpl()));
            return;
        }
        eImpl().eUnset(featureID);
    }

    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case EFeaturePackage.EFEATURE__ID:
            return getID() != null;
        case EFeaturePackage.EFEATURE__DATA:
            return DATA_EDEFAULT == null ? getData() != null : !DATA_EDEFAULT.equals(getData());
        case EFeaturePackage.EFEATURE__SRID:
            return SRID_EDEFAULT == null ? getSRID() != null : !SRID_EDEFAULT.equals(getSRID());
        case EFeaturePackage.EFEATURE__DEFAULT:
            return DEFAULT_EDEFAULT == null ? getDefault() != null : !DEFAULT_EDEFAULT.equals(getDefault());
        case EFeaturePackage.EFEATURE__STRUCTURE:
            return getStructure() != null;
        }
        return eImpl().eIsSet(featureID);
    }

    @Override
    public EList<Adapter> eAdapters() {
        return eImpl().eAdapters();
    }

    @Override
    public boolean eDeliver() {
        return eImpl().eDeliver();
    }

    @Override
    public boolean eNotificationRequired() {
        return eImpl().eNotificationRequired();
    }

    @Override
    public void eSetDeliver(boolean deliver) {
        eImpl().eSetDeliver(deliver);
    }

    @Override
    public void eNotify(Notification notification) {
        eImpl().eNotify(notification);
    }

    @Override
    public String eURIFragmentSegment(EStructuralFeature eFeature, EObject eObject) {
        return eImpl().eURIFragmentSegment(eFeature, eObject);
    }

    @Override
    public EObject eObjectForURIFragmentSegment(String uriFragmentSegment) {
        return eImpl().eObjectForURIFragmentSegment(uriFragmentSegment);
    }

    @Override
    public void eSetClass(EClass eClass) {
        eImpl().eSetClass(eClass);
    }

    @Override
    public EClass eClass() {
        return eImpl().eClass();
    }

    @Override
    public Setting eSetting(EStructuralFeature feature) {
        return eImpl().eSetting(feature);
    }

    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        return eImpl().eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    @Override
    public Resource eResource() {
        return eImpl().eResource();
    }

    @Override
    public int eContainerFeatureID() {
        return eImpl().eContainerFeatureID();
    }

    @Override
    public EObject eContainer() {
        return eImpl().eContainer();
    }

    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        return eImpl().eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    @Override
    public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
        return eImpl().eDerivedOperationID(baseOperationID, baseClass);
    }

    @Override
    public EStructuralFeature eContainingFeature() {
        return eImpl().eContainingFeature();
    }

    @Override
    public NotificationChain eSetResource(Internal resource, NotificationChain notifications) {
        return eImpl().eSetResource(resource, notifications);
    }

    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID,
            Class<?> baseClass, NotificationChain notifications) {
        return eImpl().eInverseAdd(otherEnd, featureID, baseClass, notifications);
    }

    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID,
            Class<?> baseClass, NotificationChain notifications) {
        return eImpl().eInverseRemove(otherEnd, featureID, baseClass, notifications);
    }

    @Override
    public EReference eContainmentFeature() {
        return eImpl().eContainmentFeature();
    }

    @Override
    public NotificationChain eBasicSetContainer(InternalEObject newContainer,
            int newContainerFeatureID, NotificationChain notifications) {
        return eImpl().eBasicSetContainer(newContainer, newContainerFeatureID, notifications);
    }

    @Override
    public NotificationChain eBasicRemoveFromContainer(NotificationChain notifications) {
        return eImpl().eBasicRemoveFromContainer(notifications);
    }

    @Override
    public URI eProxyURI() {
        return eImpl().eProxyURI();
    }

    @Override
    public EList<EObject> eContents() {
        return eImpl().eContents();
    }

    @Override
    public void eSetProxyURI(URI uri) {
        eImpl().eSetProxyURI(uri);
    }

    @Override
    public EObject eResolveProxy(InternalEObject proxy) {
        return eImpl().eResolveProxy(proxy);
    }

    @Override
    public InternalEObject eInternalContainer() {
        return eImpl().eInternalContainer();
    }

    @Override
    public TreeIterator<EObject> eAllContents() {
        return eImpl().eAllContents();
    }

    @Override
    public Internal eInternalResource() {
        return eImpl().eInternalResource();
    }

    @Override
    public Internal eDirectResource() {
        return eImpl().eDirectResource();
    }

    @Override
    public boolean eIsProxy() {
        return eImpl().eIsProxy();
    }

    @Override
    public EStore eStore() {
        return eImpl().eStore();
    }

    @Override
    public void eSetStore(EStore store) {
        eImpl().eSetStore(store);
    }

    @Override
    public EList<EObject> eCrossReferences() {
        return eImpl().eCrossReferences();
    }

    @Override
    public Object eGet(EStructuralFeature feature) {
        return eGet(feature, true);
    }

    @Override
    public Object eGet(EStructuralFeature feature, boolean resolve) {
        //
        // 1) Check unmappable features first
        //
        if(feature == EFeaturePackage.eINSTANCE.getEFeature_Data()) {
            return getData();
        } else if(feature == EFeaturePackage.eINSTANCE.getEFeature_Structure()) {
            return getData();
        } 
        //
        // 2) Then check structure mappings, replacing feature with mapped 
        //
        EFeatureInfo eStructure = eInternal().eStructure;
        if(eStructure.eMappingExists(feature))  {
            feature = eStructure.eMappedTo((EAttribute)feature);
        }
        //
        // Forward to implementation
        //
        return eImpl().eGet(feature, resolve);
    }

    @Override
    public Object eGet(EStructuralFeature feature, boolean resolve, boolean coreType) {
        //
        // 1) Check unmappable features first
        //
        if(feature == EFeaturePackage.eINSTANCE.getEFeature_Data()) {
            return getData();
        } else if(feature == EFeaturePackage.eINSTANCE.getEFeature_Structure()) {
            return getData();
        } 
        //
        // 2) Then check structure mappings, replacing feature with mapped 
        //
        EFeatureInfo eStructure = eInternal().eStructure;
        if(eStructure.eMappingExists(feature))  {
            feature = eStructure.eMappedTo((EAttribute)feature);
        }
        //
        // Forward to implementation
        //
        return eImpl().eGet(feature, resolve, coreType);
    }

    
    @Override
    public void eSet(EStructuralFeature feature, Object newValue) {
        //
        // 1) Check unmappable features first
        //
        if(feature == EFeaturePackage.eINSTANCE.getEFeature_Data()) {
            setData((Feature)newValue);
        } else if(feature == EFeaturePackage.eINSTANCE.getEFeature_Structure()) {
            setStructure((EFeatureInfo)newValue);
        } 
        //
        // 2) Then check structure mappings, replacing feature with mapped 
        //
        EFeatureInfo eStructure = eInternal().eStructure;
        if(eStructure.eMappingExists(feature))  {
            feature = eStructure.eMappedTo((EAttribute)feature);
        }
        //
        // Forward to implementation
        //        
        eImpl().eSet(feature, newValue);
    }

    @Override
    public boolean eIsSet(EStructuralFeature feature) {
        //
        // 1) Check unmappable features first
        //
        if(feature == EFeaturePackage.eINSTANCE.getEFeature_Data()) {
            return getData()!=null;
        } else if(feature == EFeaturePackage.eINSTANCE.getEFeature_Structure()) {
            return getStructure()!=null;
        } 
        //
        // 2) Then check structure mappings, replacing feature with mapped 
        //
        EFeatureInfo eStructure = eInternal().eStructure;
        if(eStructure.eMappingExists(feature))  {
            feature = eStructure.eMappedTo((EAttribute)feature);
        }
        //
        // Forward to implementation
        //
        return eImpl().eIsSet(feature);
    }

    @Override
    public void eUnset(EStructuralFeature feature) {
        //
        // 1) Check unmappable features first
        //
        if(feature == EFeaturePackage.eINSTANCE.getEFeature_Data()) {
            throw new IllegalStateException("'Data' can not be unset");
        } else if(feature == EFeaturePackage.eINSTANCE.getEFeature_Structure()) {
            throw new IllegalStateException("'Structure' can not be unset");
        } 
        //
        // 2) Then check structure mappings, replacing feature with mapped 
        //
        EFeatureInfo eStructure = eInternal().eStructure;
        if(eStructure.eMappingExists(feature))  {
            feature = eStructure.eMappedTo((EAttribute)feature);
        }
        //
        // Forward to implementation
        //
        eImpl().eUnset(feature);
    }

    @Override
    public Object eInvoke(EOperation operation, EList<?> arguments)
        throws InvocationTargetException {
        return eImpl().eInvoke(operation, arguments);
    }

    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        return eImpl().eInvoke(operationID, arguments);
    }

    // ----------------------------------------------------- 
    //  Object implementation
    // -----------------------------------------------------


    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        return eInternal().toString();
    }
    
    // ----------------------------------------------------- 
    //  Public helper methods
    // -----------------------------------------------------

    /**
     * Create new {@link EFeatureDelegate} instance
     * <p>
     * If structure {@link EFeatureInfo#eHints() hints} indicate
     * {@link EFeatureHints#EFEATURE_SINGLETON_FEATURES singletons} should be 
     * used, a {@link ThreadLocal thread local} singleton {@link EFeatureDelegate}
     * instance is returned. Otherwise, a new instance is returned.
     * <p> 
     * This hint optimize memory usage and runtime speed, but should only be used
     * with read operations from {@link #eImpl()}.
     * </p>
     * @param eStructure - EFeature {@link EFeatureInfo structure}.
     * @param eImpl - new {@link InternalEObject} implementation which this delegates to 
     * @param eTrusted - if <code>true</code>, the method will not validate structure against implementation.
     * @throws IllegalArgumentException If implementation does not validate 
     * against given structure
     */
    public static final EFeatureDelegate create(EFeatureInfo eStructure, InternalEObject eImpl, boolean eTrusted) {
        return create(eStructure, eImpl, eTrusted, eStructure.eHints());
    }
    
    /**
     * Create new {@link EFeatureDelegate} instance
     * </p>
     * @param eStructure - EFeature {@link EFeatureInfo structure}.
     * @param eImpl - new {@link InternalEObject} implementation which this delegates to 
     * @param eTrusted - if <code>true</code>, the method will not validate structure against implementation.
     * @param eHints - if {@link EFeatureHints#EFEATURE_SINGLETON_FEATURES} is <code>true</code>, 
     * a {@link ThreadLocal thread local} singleton instance is returned
     * @throws IllegalArgumentException If implementation does not validate 
     * against given structure
     */
    public static final EFeatureDelegate create(EFeatureInfo eStructure, 
            InternalEObject eImpl, boolean eTrusted, EFeatureHints eHints) {
        //
        // Initialize
        //
        EFeatureDelegate eDelegate;
        //
        // Is EFeatureDelegate a singleton?
        //
        if(eHints.eSingletonFeatures()) {
            //
            // Get singleton instance
            //
            eDelegate = eSingleton.get();
            //
            // No singleton instance found?
            //
            if(eDelegate==null) {
                //
                // Create new delegate
                //
                eDelegate = new EFeatureDelegate(eStructure, eImpl, eTrusted, eHints);
                //
                // Update thread local variable
                //
                eSingleton.set(eDelegate);
            } 
            //
            // Adapt directly? (replaces current delegate) 
            //
            else if(eImpl instanceof EFeatureDelegate) {
                //
                // Cast to EFeatureDelegate
                //
                eDelegate = (EFeatureDelegate)eImpl;
                //
                // Replace structure
                //
                eDelegate.eReplace(eStructure, eDelegate.eImpl, false, eHints);
            } 
            //
            // Replace implementation of singleton delegate
            //
            else {
                //
                // Replace implementation
                //
                eDelegate.eReplace(eStructure, (InternalEObject)eImpl, eTrusted, eHints);
            }
        } 
        //
        // Adapt directly? (returns same current delegate) 
        //
        else if(eImpl instanceof EFeatureDelegate) {
            //
            // Cast to EFeatureDelegate
            //
            eDelegate = (EFeatureDelegate)eImpl;
            //
            // Replace structure
            //
            eDelegate.eReplace(eStructure, eDelegate.eImpl, false, eHints);
        }
        //
        // Construct new instance
        //
        else {        
            eDelegate = new EFeatureDelegate(eStructure, eImpl, eTrusted, eHints);
        }        
        //
        // Get current ID if set
        //
        String eSetID = eDelegate.getID();
        //
        // Get ID factory from context
        //
        EFeatureIDFactory eIDFactory = eStructure.eContext().eIDFactory();
        //
        // Set ID as used?
        //
        if(!(eSetID==null || eSetID.length()==0)) {
            //
            // Set ID as used for this delegate
            //
            eIDFactory.useID(eDelegate, eSetID);
        } else {
            //
            // Create new ID for this delegate
            //
            eSetID = eIDFactory.createID(eDelegate);
        }
        //
        // Finished
        //
        return eDelegate;
    }
    
    // ----------------------------------------------------- 
    //  Protected helper methods
    // -----------------------------------------------------
    
    /**
     * Replace current hints
     * <p>
     * @param eHints - {@link EFeatureHints} instance. If <code>null</code>,
     * {@link EFeatureInfo#eHints() structure hints} is used instead.
     */
    protected void eReplace(EFeatureHints eHints) {
        //
        // Verify state
        //
        if(eInternal==null) {
            throw new IllegalStateException("EFeatureDelegate is not created");
        }
        //
        // Ensure hints exists
        //
        eHints = (eHints==null ? new EFeatureHints(eInternal.eStructure.eHints()) : eHints);        
        //
        // Replace hints
        //
        this.eInternal.eReplace(eHints, true);     
    }
    /**
     * Replace current delegation
     * <p>
     * @param eStructure - EFeature {@link EFeatureInfo structure}.
     * @param eImpl - new {@link EObject} implementation which this delegates to 
     * @param eTrusted - if <code>true</code>, the method will not validate 
     * structure against implementation.
     * @param eHints - {@link EFeatureHints} instance. If <code>null</code>,
     * {@link EFeatureInfo#eHints() structure hints} is used instead.
     * @throws IllegalArgumentException If implementation does not validate 
     * against given structure
     */
    protected void eReplace(EFeatureInfo eStructure, InternalEObject eImpl, boolean eTrusted, EFeatureHints eHints) {
        //
        // Validate structure?
        //
        if(!eTrusted) {
            EFeatureStatus eStatus;
            if((eStatus = eStructure.validate(eImpl)).isFailure()) {
                throw new IllegalArgumentException(
                        "EObject implementation is not valid. " +
                        eStatus.getMessage(), eStatus.getCause());
            }
        }
        //
        // Ensure hints exists
        //
        eHints = (eHints==null ? new EFeatureHints(eStructure.eHints()) : eHints);
        //
        // Is known implementation?
        //
        if(eImpl instanceof EFeatureImpl) {
            //
            // --------------------------------------------
            //  This is a replace (copy) operation.
            // --------------------------------------------
            //
            // Cache strong reference to implementation, 
            // ensuring that it is not garbage collected 
            // until this delegate is.  
            //            
            this.eImpl = eImpl;
            //
            // Get internal EFeature implementation from EFeatureImpl
            //                        
            this.eInternal = ((EFeatureImpl)eImpl).eInternal();            
            //
            // Replace implementation
            //
            this.eInternal.eReplace(eStructure, this, eHints, true);     
        } 
        else if(eImpl instanceof EFeatureDelegate) {
            //
            // --------------------------------------------
            //  This is a replace (copy) operation.
            // --------------------------------------------
            //
            // Cache strong reference to EObject implementation, 
            // ensuring that it is not garbage collected until 
            // this delegate is. This acts like a copy constructor.
            //            
            this.eImpl = ((EFeatureDelegate)eImpl).eImpl();
            //
            // Get internal EFeature implementation from delegate
            //                        
            this.eInternal = ((EFeatureDelegate)eImpl).eInternal();
            //
            // Replace implementation
            //
            this.eInternal.eReplace(eStructure, this, eHints, true);
        } 
        else {
            //
            // Cache strong reference to EObject implementation, 
            // ensuring that it is not garbage collected until 
            // this delegate is 
            //
            // (EFeatureInternal only has a weak reference).
            //            
            this.eImpl = eImpl;
            //
            // --------------------------------------------
            //  This is the replace (or create) operation.
            // --------------------------------------------
            //
            if(this.eInternal==null) {
                //
                // Create internal implementation
                //
                this.eInternal = new EFeatureInternal(eStructure, this, eHints);
            } 
            else {
                //
                // Replace implementation
                //
                this.eInternal.eReplace(eStructure, this, eHints, true);
            }                
        }
    }
    
    

} // EFeatureDelegate
