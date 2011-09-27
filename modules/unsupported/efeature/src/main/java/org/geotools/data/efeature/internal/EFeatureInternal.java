package org.geotools.data.efeature.internal;

import static org.geotools.data.efeature.internal.ESimpleFeatureAdapter.create;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureAttributeInfo;
import org.geotools.data.efeature.EFeatureConstants;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.EFeatureHints;
import org.geotools.data.efeature.EFeatureIDFactory;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.EFeatureReader;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.data.efeature.impl.EFeatureImpl;
import org.geotools.data.efeature.util.EFeatureAttributeList;
import org.geotools.data.efeature.util.EFeatureGeometryList;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This is an internal implementation of the {@link EFeature} contract. 
 * <p>
 * It does not implement the {@link EFeature} interfaces, just the interface 
 * methods. This allows for a minimal base class common for both 
 * {@link EFeatureImpl} and {@link EFeatureDelegate} classes.   
 * </p>
 * 
 * @author kengu - 29. mai 2011
 * 
 * @see {@link EFeature} - the interface which this class is a minimal base for  
 * @see {@link EFeatureImpl} - default implementation of {@link EFeature}. All models 
 * which extend the {@link EFeature} EMF model implements this class. 
 * @see {@link EFeatureDelegate} - a class which is able to delegate {@link EFeature} delegate 
 *
 * @source $URL$
 */
public class EFeatureInternal {

    /**
     * The default value of the '{@link #getSRID() <em>SRID</em>}' attribute.
     */
    protected static final String SRID_EDEFAULT = EFeatureConstants.DEFAULT_SRID;

    /**
     * The default value of the '{@link #getData(Transaction) <em>Data</em>}' attribute.
     */
    protected static final Feature DATA_EDEFAULT = EFeatureConstants.DEFAULT_FEATURE;

    /**
     * The default value of the '{@link #isSimple() <em>Simple</em>}' attribute.
     */
    protected static final boolean SIMPLE_EDEFAULT = EFeatureConstants.DEFAULT_IS_SIMPLE;

    /**
     * The default value of the '{@link #getDefault() <em>Default</em>}' attribute.
     */
    protected static final String DEFAULT_EDEFAULT = EFeatureConstants.DEFAULT_GEOMETRY_NAME;

    /**
     * The default value of the '{@link #getStructure() <em>Structure</em>}' attribute.
     */
    protected static final EFeatureInfo STRUCTURE_EDEFAULT = EFeatureConstants.DEFAULT_FEATURE_STRUCTURE;

    /**
     * Current {@link EFeature} ID.
     */
    protected String eID;
    
    /**
     * The cached value of the '{@link #getStructure() <em>Structure</em>}' attribute.
     */
    protected EFeatureInfo eStructure = STRUCTURE_EDEFAULT;
    
    /**
     * Cached {@link EObject} which this delegates to.
     * <p>
     * It is cached using a weak reference which allows cyclic reference between this and the
     * implementation.
     */
    protected WeakReference<EFeature> eImpl;

    /**
     * Flag indicating that the {@link EFeature#getID EFeature ID} 
     * holder check is not performed.
     */
    protected boolean doIDHolderCheck = true;

    /**
     * Flag indicating that the {@link EFeature#getID EFeature ID} 
     * holder check is in progress. If {@link #getID()} is
     * called when this flag is <code>true</code>, then this is the ID 
     * holder. This follows from the fact that the 
     * {@link #eImpl() actual implementation} is delegating its 
     * {@link EAttribute ID attribute} to {@link #getID() this}.
     */
    protected boolean isIDHolderChecking = false;
    
    /**
     * Flag indicating that the {@link EFeature#getID()} value is hold by this.
     */
    protected boolean isIDHolder = false;

    /**
     * Cached list of to {@link EFeatureProperty} instances in same order 
     * as {@link EAttribute}s in current {@link #eStructure EFeatureInfo}.
     */
    protected List<EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature>> eProperties;
    
    /**
     * Cached property name to {@link EFeatureProperty} instances map.
     */
    protected Map<String, EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature>> ePropertyMap;
    
    /**
     * Flag storing current detached values state. Since {@link #eHints} is mutable,
     * current state change must be cached in order to track actual changes made to hints.
     */
    protected boolean eValuesDetached = false;
    
    /**
     * Cached set of validated {@link EFeatureInfo#eUID}s  
     */
    protected static Map<EClass,Set<Long>> eValidatedClassMap = 
        Collections.synchronizedMap(new WeakHashMap<EClass,Set<Long>>());
    
    /**
     * Transaction used when not explicitly specified
     */
    protected Transaction eTx = Transaction.AUTO_COMMIT;
    
    /**
     * Cached {@link EFeatureHints}
     */
    protected EFeatureHints eHints;

    /**
     * Cached {@link ESimpleFeature} singleton instance. 
     * <p>
     * This is used {@link ThreadLocal thread local} to allow 
     * multiple threads to work on {@link ESimpleFeature} 
     * singletons without concurrent modification problems.
     * </p>
     * @see {@link EFeatureHints#EFEATURE_SINGLETON_FEATURES}
     */
    protected static ThreadLocal<ESimpleFeatureInternal> 
        eSingleton = new ThreadLocal<ESimpleFeatureInternal>();

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * Context-unaware constructor.
     * <p>
     * Use this constructor when the {@link EFeatureContext} is unknown.
     * <p>
     * The implementation is weakly referenced to allow garbage collection 
     * when the implementation is no longer referenced. 
     * <p>
     * {@link EFeatureContext Context} and {@link EFeatureInfo structure} must 
     * be set before it can be read by {@link EFeatureReader}. 
     * </p> 
     * @param eImpl - {@link EObject} instance which implements {@link EFeature}.
     * 
     * @see {@link EFeatureContextHelper} - read more about the context startup problem.
     * @see {@link #setStructure(EFeatureInfo)} - set {@link EFeatureInfo#eContext() context}
     * and {@link EFeatureInfo structure}.
     * 
     */
    public EFeatureInternal(EFeature eImpl) {
        this(eInternalContext(eImpl),eImpl);
    }        

    /**
     * Auto-configuring structure constructor.
     * <p>
     * This constructor builds the {@link EFeatureInfo structure} 
     * from given {@link EObject} instance using EMF reflection.
     * <p>
     * <b>Note</b>: The constructor verifies the structure. 
     * If invalid, a {@link IllegalArgumentException} is thrown.
     * </p>
     * @param eContext - the {@link EFeatureContext} which this belongs
     * @param eImpl - {@link EObject} instance which implements {@link EFeature}.
     * @throws IllegalArgumentException If the {@link EFeatureInfo structure} of this feature
     *         {@link EFeatureInfo#validate(EPackage, EClass) fails to validate}.
     * 
     */
    public EFeatureInternal(EFeatureContext eContext, EFeature eImpl)
        throws IllegalArgumentException {
        //
        // Build EFeature structure from EObject instance
        //
        this(EFeatureInfo.create(eContext, eImpl, new EFeatureHints()), eImpl, null);
    }

    /**
     * Explicit-configuring constructor.
     * <p>
     * <b>Note</b>: This constructor verifies the structure. 
     * If invalid, a {@link IllegalArgumentException} is thrown.
     * </p>
     * @param eStructure - {@link EFeatureInfo structure} instance.
     * @param eImpl - {@link EObject} instance which implements {@link EFeature}.
     * @param eHints - {@link EFeatureHints} instance. If <code>null</code>, 
     * {@link EFeatureInfo#eHints() structure hints} is used.
     * @throws IllegalArgumentException If the {@link EFeatureInfo structure} of this feature
     *         {@link EFeatureInfo#validate(EPackage, EClass) fails to validate}.
     */
    public EFeatureInternal(EFeatureInfo eStructure, EFeature eImpl, EFeatureHints eHints)
            throws IllegalArgumentException {
        //
        // Cache weak reference to implementation
        //
        this.eImpl = new WeakReference<EFeature>(eImpl);
        //
        // Copy structure hints
        //
        this.eHints = (eHints==null ? new EFeatureHints(eStructure.eHints()) : eHints);
        //
        // Set structure
        //
        setStructure(eStructure);
    }

    // ----------------------------------------------------- 
    //  EFeature implementation
    // -----------------------------------------------------

    public String getID() {
        //
        // -----------------------------------------------------
        //  Some EMF GenModel Feature delegating pattern 
        //  choices (f.ex. 'None') will result in recursive 
        //  calls to from EFeature#getID() to 
        //  EFeatureInternal#getID() because of the dynamic
        //  implementation of eGet(EStructuralFeature) methods.
        //
        //  This method implements re-entry guards which 
        //  detects the situation, indication that the EFeature
        //  implementation is abstract, delegating the storage
        //  of the ID value to this EFeatureInternal instance
        //  (hence the 'ID holder' check and flags)
        // -----------------------------------------------------
        //
        // Verify current state
        //
        verify();
        //
        // Is checking if this is the ID holder?
        //
        if(isIDHolderChecking) {
            //
            // The re-entry implies that this is the ID holder
            //
            isIDHolder = true;
        }
        //
        // Get ID held by this?
        //
        if(isIDHolder) 
        {
            return eID;
        }         
        //
        // ------------------------------------
        //  Enter "Check if ID Holder" mode?
        // ------------------------------------
        //  This is only done once per instance
        //
        else if(doIDHolderCheck) {
            //
            // --------------------------------
            //  If this method is called 
            //  recursively, then this must be
            //  an ID holder.
            // --------------------------------
            //
            //  Enable recursive call detection
            //
            isIDHolderChecking = true;
        }
        
        //
        // Implementation holds the ID value. Get EFeature ID attribute.
        //
        EAttribute eAttribute = getStructure().eIDAttribute();
        //
        // Get current EFeature ID (will recurse into this method if ID holder)
        //
        eID = (String)eImpl().eGet(eAttribute);
        //
        // Reset ID holder check flags?
        //
        if(doIDHolderCheck) {
            //
            // Run check only once
            //
            doIDHolderCheck = false;
            //
            // Notify that the check is completed
            //
            isIDHolderChecking = false;
        }
        //
        // Finished
        //
        return (eID!=null ? eID.toString() : null);
    }
    
    public void setID(String eNewID) {
        //
        // Forward
        //
        eSetID(eNewID, true);
    }    

    public String getSRID() {
        return eStructure==null ? SRID_EDEFAULT : getStructure().getSRID();
    }

    public void setSRID(String newSRID) {
        verify();
        //
        // Prepare change delta
        //
        String oldSRID = getSRID();

        // Is changed?
        //
        if (newSRID != oldSRID) {
            //
            // Update SRID for structure.
            // Current bounds is invalidated
            // by callback to eStructureInfoListener
            //
            getStructure().setSRID(newSRID);
            //
            // Forward
            //
            eNotify(EFeaturePackage.EFEATURE__SRID, oldSRID, newSRID);
        }
    }

    public ESimpleFeature getData(Transaction transaction) {
        //
        // Do sanity checks
        //
        verify();
        //
        // Get ESimpleFeature instance
        //
        return eSimpleFeature(this,eHints);
    }

    public ESimpleFeature setData(Feature newData, Transaction transaction) {
        //
        // Sanity checks
        //
        verify();
        if (newData == null) {
            throw new NullPointerException("Data can not be set to null");
        }
        //
        // Prepare adaption of new data to given structure
        //
        ESimpleFeatureAdapter eAdapter = create(eStructure, eImpl(), newData);
        //
        // Adapt data to ESimpleFeature 
        // (EMF notifications are raised if anything changed)
        //
        if(eHints.eValuesDetached()) {
            return eAdapter.eAdapt(eStructure, eSimpleFeature(this,eHints), eTx);
        } else {
            return eAdapter.eAdapt(eStructure, eImpl(), eTx);
        }
    }

    public String getDefault() {
        verify();
        return getStructure().eGetDefaultGeometryName();
    }

    public void setDefault(String newDefault) {
        verify();
        String oldDefault = getDefault();
        getStructure().eSetDefaultGeometryName(newDefault);
        eNotify(EFeaturePackage.EFEATURE__DEFAULT, oldDefault, newDefault);
    }

    public EFeatureInfo getStructure() {
        return eStructure;
    }
    
    public boolean setStructure(EFeatureInfo eNewStructure) {
        //
        // Do sanity checks
        //
        if(eNewStructure==null) {
            throw new NullPointerException("EFeatureInfo structure can not be null");
        }
        //
        // Calculate change
        //
        boolean bFlag = this.eStructure!=eNewStructure;
        //
        // Is changed
        //
        if(bFlag) {
            //
            // Cache reference to old structure
            //
            EFeatureInfo eOldStructure = this.eStructure;
            //
            // ------------------------------------------------------
            //  Validate implementation against structure? 
            // ------------------------------------------------------
            //  This is an optimization exploiting the fact that
            //  structures are immutable once created, and that 
            //  each structure can thus be uniquely identified 
            //  by a unique ID. It is therefore only required to
            //  validate this implementation against each unique 
            //  structure once.
            //
            if(!eNewStructure.eEqualTo(eOldStructure)) {
                validate(eNewStructure, eImpl());
            } 
            //
            // Is valid, initialize this instance
            //
            this.eStructure = eNewStructure;
            //
            // Reset id information, forcing ID holder checks
            //
            eInitID();
            //
            // Reset property collections
            //
            this.eProperties = null;
            this.ePropertyMap = null;
            //
            // Tell listeners in old structure about the change?
            //
            if(eOldStructure!=null) {
                eOldStructure.eNotify(this, 
                        EFeaturePackage.EFEATURE__STRUCTURE, 
                        eOldStructure, eNewStructure);
            }            
        }
        //
        // Finished
        //
        return bFlag;
    }   
    
    public <V> EFeatureAttributeList<V> getAttributeList(Class<V> valueType) {
        verify();
        return new EFeatureAttributeList<V>(getPropertyList(valueType), valueType);
    }

    public <V extends Geometry> EFeatureGeometryList<V> getGeometryList(Class<V> valueType) {
        verify();
        return new EFeatureGeometryList<V>(getPropertyList(valueType), valueType);
    }
    
       
    // ----------------------------------------------------- 
    //  EFeatureInternal methods
    // -----------------------------------------------------
    
    public void enter(Transaction transaction) {
        this.eTx = transaction;
    }
        
    public void leave() {
        this.eTx = Transaction.AUTO_COMMIT;
    }
    
    public EFeatureHints eHints() {
        return eHints;
    }
    
    public void eReplace(EFeatureHints eHints, boolean eSetInitDetachedValues) {
        //
        // Verify state
        //
        if(eStructure==null) {
            throw new IllegalStateException("EFeatureInternal is not created");
        }
        //
        // Replace hints
        //
        this.eHints = eHints;
        //
        // Discover ID? (only possible added to resource and not a proxy)
        //
        if( !(eImpl().eResource()==null || eImpl().eIsProxy()) ) {
            //
            // Structure not change (eID was not reset)
            //
            eInitID();
            //
            // Get EFeature ID (assignment is done by method)
            //
            getID();
        }
        //
        // Tell all EFeatureProperty instances to initialize detached values?
        //
        if(eSetInitDetachedValues) eSetInitDetachedValues();        
    }
    
    public void eReplace(EFeatureInfo eStructure, EFeature eImpl, 
            EFeatureHints eHints, boolean eSetInitDetachedValues) {
        //
        // Replace weak reference?
        //
        if(this.eImpl.get()!=eImpl) {
            this.eImpl = new WeakReference<EFeature>(eImpl);
        }
        //
        // Replace hints
        //
        this.eHints = eHints;
        //
        // Discover ID? (only possible added to resource and not a proxy)
        //
        if( !(setStructure(eStructure) || eImpl.eResource()==null || eImpl.eIsProxy()) ) {
            //
            // Structure not change (eID was not reset)
            //
            eInitID();
            //
            // Get EFeature ID (assignment is done by method)
            //
            getID();
        }
        //
        // Tell all EFeatureProperty instances to initialize detached values?
        //
        if(eSetInitDetachedValues) eSetInitDetachedValues();        
    }
        
    public String eSetID(String eNewID, boolean eSetUsage) {
        //
        // Verify current state
        //
        verify();
        //
        // Tell ID factory of ID usage?
        //
        if(eSetUsage) {
            //
            // Get ID factory
            //
            EFeatureIDFactory eIDFactory = getStructure().eContext().eIDFactory();
            //
            // -------------------------------------------------------
            //  Notify ID usage to factory?
            // -------------------------------------------------------
            //  This is part of the context startup problem solution.
            //  When constructing EFeatures from XMI, the context is
            //  unknown. This comes from the fact that values are 
            //  serialized before instances are added to the resource
            //  (context). Therefore, a internal context is used 
            //  instead. This internal context should not not create 
            //  IDs, since IDs only have meaning in the context that
            //  that they belong. The ID factory in the internal 
            //  context does therefore not support creation and 
            //  usage of IDs (throws OperationUnsupportedException).
            //
            if(!(eIDFactory instanceof EFeatureVoidIDFactory)) {
                //
                // Tell ID factory of ID usage, a new ID is returned if not unique
                //
                eNewID = eIDFactory.useID(eImpl(),eNewID);            
            }
        }
        //
        // Is ID held by this?
        //
        if(isIDHolder)
        {
            //
            // Cache old for later use
            //
            String eOldID = eID;
            //
            // Cache ID in this
            //
            eID = eNewID;
            //
            // Notify?
            //
            eNotify(EFeaturePackage.EFEATURE__ID, eOldID, eNewID);            
        } 
        else {
            //
            // Implementation holds the ID value. Get EFeature ID attribute.
            //
            EAttribute eAttribute = getStructure().eIDAttribute();
            //
            // Since the implementation holds the ID value, the
            // the eAttribute must be changeable. 
            //
            if(!eAttribute.isChangeable()) {
                throw new IllegalStateException("EAttribute must be " +
                                "changeable when the eImpl() is the " +
                                "ID holder.");
            }
            //
            // Update ID attribute value
            //
            eImpl().eSet(eAttribute,eNewID);            
        }
        //
        // Finished
        //
        return eNewID;
    }        
    
    // ----------------------------------------------------- 
    //  Public static helper methods
    // -----------------------------------------------------           
        
    public static final EFeatureInternal eInternal(EFeatureInfo eStructure, 
            EObject eObject) throws IllegalArgumentException {
        
        if(eObject instanceof EFeatureInternal) {
            return (EFeatureInternal)eObject;
        } else if(eObject instanceof EFeatureImpl) {
            return ((EFeatureImpl)eObject).eInternal();
        } else if(eObject instanceof EFeatureDelegate) {
            return ((EFeatureDelegate)eObject).eInternal();
        }
        throw new IllegalArgumentException("EObject " + eObject + " does not implement EFeature");
        
    }
    
    
    // ----------------------------------------------------- 
    //  Protected helper methods
    // -----------------------------------------------------           
    
    /**
     * Reset ID information, forcing ID holder checks
     */
    protected void eInitID() {
        eID = null;
        doIDHolderCheck = true;
        isIDHolder = false;
        isIDHolderChecking = false;
    }
    
//    /**
//     * Set {@link EFeatureHints} instance.
//     * @param eHints - new {@link EFeatureHints} instance.
//     * @return <code>true</code> if {@link EFeatureHints#EFEATURE_VALUES_DETACHED} has changed
//     */
//    protected boolean eSetHints(EFeatureHints eHints) {
//        //
//        // Check if hint is changed
//        //
//        boolean bFlag = eHints.eValuesDetached()!=eValuesDetached;
//        //
//        // Replace hints
//        //
//        this.eHints = eHints;
//        //
//        // Set flag tracking changes to EFEATURE_VALUES_DETACHED flag
//        //
//        eValuesDetached = eHints.eValuesDetached();
//        //
//        // Finished
//        //
//        return bFlag;
//    }
       
    protected void eSetInitDetachedValues() {
        if(eProperties!=null) {
            for(EFeaturePropertyDelegate<?, ?, ?> it : eProperties) {
                it.eSetInitDetachedValues();
            }
        }
    }
    
    /**
     * Verify that state is available
     */
    protected void verify() throws IllegalStateException
    {
        if(eStructure==null)
            throw new IllegalStateException(this + " is not valid. " +
            		"Please specify the structure.");
    }      
 
    /**
     * Get {@link InternalEObject} instance containing {@link EFeature} data.
     * @return a {@link InternalEObject} instance.
     * @throws NullPointerException If garbage collected (is weakly referenced)
     */
    protected InternalEObject eImpl() throws NullPointerException {
        InternalEObject eObject = (InternalEObject)eImpl.get();
        if(eObject instanceof EFeatureDelegate) {
            eObject = ((EFeatureDelegate)eObject).eImpl();
        }
        if (eObject == null) {
            throw (NullPointerException)(new NullPointerException("EFeature implementation " 
                    + eStructure.eClassName() + " is finalized (garbage collected).")).fillInStackTrace();
        }
        return eObject;
    }
    
    /**
     * Get {@link InternalEObject} instance implementing {@link EFeature} data.
     * @return a {@link EFeature} instance.
     * @throws NullPointerException If garbage collected (is weakly referenced)
     */
    protected InternalEObject eFeature() throws NullPointerException {
        InternalEObject eObject = (InternalEObject)this.eImpl.get();
        if (eObject == null) {
            throw (NullPointerException)(new NullPointerException("EFeature implementation " 
                    + eStructure.eClassName() + " is finalized (garbage collected).")).fillInStackTrace();
        } 
        return eObject;
    }
        

    protected void eNotify(int feature, Object oldValue, Object newValue) {
        eNotify(eImpl(), feature, oldValue, newValue);
    }
        
    /**
     * Get current list of {@link EFeatureProperty} instances.
     * <p>
     * This method implements lazy creation of {@link EFeatureProperty} instances.
     * </p>
     * 
     * @return list of {@link EFeatureProperty} instances.
     */
    protected List<EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature>> getProperties() {
        if (eProperties == null) {
            //
            // Get all attributes in structure
            //
            List<EFeatureAttributeInfo> eList = getStructure().eGetAttributeInfoList(true);
            //
            // Initialize map
            //
            eProperties = EFeatureUtils.newList(eList.size());
            //
            // Loop over all attributes
            //
            for (EFeatureAttributeInfo it : eList) {
                EAttribute eAttribute = it.eAttribute();
                Class<?> type = eAttribute.getEAttributeType().getInstanceClass();
                eProperties.add(newProperty(this, it.eName(), type));
            }            
        }
        return eProperties;
    }
    
    /**
     * Get {@link EFeatureProperty} name to instances mappin.
     * <p>
     * This method implements lazy creation of instance map.
     * </p>
     * 
     * @return map of {@link EFeatureProperty} instances.
     */
    protected Map<String, ? extends EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature>> getPropertyMap() {
        if (ePropertyMap == null) {
            //
            // Get all properties in structure
            //
            List<EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature>> 
                eList = getProperties();
            //
            // Initialize map
            //
            ePropertyMap = EFeatureUtils.newMap(eList.size());
            //
            // Loop over all attributes
            //
            for (EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature> it : eList) {
                String eName = it.getName();
                ePropertyMap.put(eName, it);
            }
        }
        return ePropertyMap;
    }

    /**
     * Get list of {@link EFeatureProperty} instances filtered on value type.
     * <p>
     * This method implements lazy creation of {@link EFeatureProperty} instances. After creation,
     * the instances are cached in {@link #ePropertyMap}
     * </p>
     * 
     * @return list of {@link EFeatureProperty} instances.
     */
    @SuppressWarnings("unchecked")
    protected <V> List<? extends EFeatureProperty<V, Property>> getPropertyList(Class<V> type) {
        //
        // Initialize
        //
        Collection<? extends EFeatureProperty<?, ? extends Property>> eList = getProperties();
        List<EFeatureProperty<V, Property>> eSelected = EFeatureUtils.newList(eList.size());
        //
        // Select EFeatureProperty instances with correct value type
        //
        for (EFeatureProperty<?, ? extends Property> it : eList) {
            //
            // Has correct value type?
            //
            if (type.isAssignableFrom(it.getValueType())) {
                eSelected.add((EFeatureProperty<V, Property>) it);
            }
        }
        //
        // Finished selection
        //
        return eSelected;
    }
    
    
    // ----------------------------------------------------- 
    //  Static helper methods
    // -----------------------------------------------------
            
    protected static void eNotify(InternalEObject eObject, int feature, Object oldValue, Object newValue) {
        if (eObject.eNotificationRequired()) {
            eObject.eNotify(
                    new ENotificationImpl(eObject, Notification.SET,
                            feature, oldValue, newValue));
        }
        
    }    
    
    /**
     * Get a {@link ESimpleFeature} instance.
     * <p>
     * This method decides from inspection of
     * {@link EFeatureInfo#eHints()} if a new instance
     * or the singleton instance should be returned
     * </p>
     * @param eInternal EFeatureInternal that contains the ESimpleFeature data
     * @return a {@link ESimpleFeature} instance
     */
    protected static ESimpleFeature eSimpleFeature(EFeatureInternal eInternal, EFeatureHints eHints) {
        //
        // Prepare
        //
        ESimpleFeatureInternal eData = null;
        //
        // Is instance singleton?
        //
        if(eHints.eSingletonFeatures()) {
            //
            // Get singleton instance
            //
            eData = eSingleton.get();
            //
            // Create new singleton instance?
            //
            if(eData==null || eData.isReleased()) {
                //
                // Create instance
                //
                eData = new ESimpleFeatureInternal(eInternal);
                //
                // Update thread local instance
                //
                eSingleton.set(eData);
            } 
            //
            // else, set internal implementation
            //
            else eData.eReplace(eInternal);
                
            
        } else {
            //
            // Create new instance
            //
            eData = new ESimpleFeatureInternal(eInternal);
        }
        //
        // Finished
        //
        return eData;
    }

    
    protected static final EFeatureContext eInternalContext(EFeature eImpl) {
        return EFeatureContextHelper.eContext(eImpl);
    }
    
    protected static final void validate(EFeatureInfo eStructure, EObject eObject)
            throws IllegalArgumentException {
        //
        // Synchronize multiple threads access to map
        //
        synchronized(eValidatedClassMap) {            
            //
            // Prepare
            //
            EClass eClass = eObject.eClass();
            EPackage ePackage = eClass.getEPackage();
            //
            // Get structures already verified for implementing class 
            // 
            Set<Long> eValidSet = eValidatedClassMap.get(eClass);
            //
            // Found no validated structures?
            //
            if( eValidSet == null) {
                eValidSet = new HashSet<Long>();
                eValidatedClassMap.put(eClass, eValidSet);
            } 
            if( !eValidSet.contains(eStructure.eUID())) {
                //
                // Get parent class?
                //
                EClass eParent = null; 
                if (!eStructure.isRoot()) {
                    eParent = EFeatureUtils.eGetContainingClass(eObject);
                }        
                //
                // Validate structure against EObject instance EClass
                //
                EFeatureStatus s;
                if (!(s = eStructure.validate(ePackage, eParent)).isSuccess()) {
                    throw new IllegalArgumentException(s.getMessage(), s.getCause());
                }
                //
                // Add to verified structures
                //
                eValidSet.add(eStructure.eUID());
            }           
        }
    }    
    
    @SuppressWarnings("unchecked")
    protected static EFeaturePropertyDelegate<?, ? extends Property, ? extends EStructuralFeature> newProperty(
            EFeatureInternal eInternal, String eName, Class<?> type) {
        if (Geometry.class.isAssignableFrom(type)) {
            return newGeometry(eInternal, eName, (Class<? extends Geometry>)type);
        }
        return newAttribute(eInternal, eName, type);
    }

    protected static <V> EFeatureAttributeDelegate<V> newAttribute(EFeatureInternal eInternal, String eName, Class<V> type) {
        return new EFeatureAttributeDelegate<V>(eInternal, eName, type);
    }

    protected static <V extends Geometry> EFeatureGeometryDelegate<V> newGeometry(
            EFeatureInternal eInternal, String eName, Class<V> type) {
        return new EFeatureGeometryDelegate<V>(eInternal, eName, type);
    }
    

}
