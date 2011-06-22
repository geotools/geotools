package org.geotools.data.efeature.internal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureAttribute;
import org.geotools.data.efeature.EFeatureAttributeInfo;
import org.geotools.data.efeature.EFeatureConstants;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.EFeatureGeometry;
import org.geotools.data.efeature.EFeatureHints;
import org.geotools.data.efeature.EFeatureIDFactory;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeaturePackage;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.EFeatureReader;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.ESimpleFeature;
import org.geotools.data.efeature.EStructureInfo;
import org.geotools.data.efeature.EFeatureListener;
import org.geotools.data.efeature.impl.EFeatureImpl;
import org.geotools.data.efeature.util.EFeatureAttributeList;
import org.geotools.data.efeature.util.EFeatureGeometryList;
import org.geotools.feature.NameImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

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
 */
public class EFeatureInternal {

    /**
     * The default value of the '{@link #getSRID() <em>SRID</em>}' attribute.
     */
    protected static final String SRID_EDEFAULT = EFeatureConstants.DEFAULT_SRID;

    /**
     * The default value of the '{@link #getData() <em>Data</em>}' attribute.
     */
    protected static final Feature DATA_EDEFAULT = EFeatureConstants.DEFAULT_FEATURE;

    /**
     * The cached value of the '{@link #getData() <em>Data</em>}' attribute.
     */
    protected Feature data = DATA_EDEFAULT;

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
     * The {@link EFeature} ID. Only in use if this is the {@link #isIDHolder ID holder}
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
    protected WeakReference<InternalEObject> eImpl;

    /**
     * Cached {@link Feature feature} bounds.
     */
    protected ReferencedEnvelope bounds;
    
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
    protected boolean isCheckingIDHolder = false;
    
    /**
     * Flag indicating that the {@link EFeature#getID()} value is hold by this.
     */
    protected boolean isIDHolder = false;

    /**
     * Cached {@link EFeatureProperty} instances.
     */
    protected Map<String, EFeatureProperty<?, ? extends Property>> ePropertyMap;
    
    /**
     * Cached set of validated {@link EFeatureInfo#eUID}s  
     */
    protected static Map<EClass,Set<Long>> eClassValidatedMap = 
        Collections.synchronizedMap(new WeakHashMap<EClass,Set<Long>>());

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
    public EFeatureInternal(InternalEObject eImpl) {
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
    public EFeatureInternal(EFeatureContext eContext, InternalEObject eImpl) 
        throws IllegalArgumentException {
        //
        // Build EFeature structure from EObject instance
        //
        this(EFeatureInfo.create(eContext, eImpl, new EFeatureHints()), eImpl);
    }

    /**
     * Explicit-configuring constructor.
     * <p>
     * <b>Note</b>: This constructor verifies the structure. 
     * If invalid, a {@link IllegalArgumentException} is thrown.
     * </p>
     * @param eStructure - {@link EFeatureInfo structure} instance.
     * @param eImpl - {@link EObject} instance which implements {@link EFeature}.
     * @throws IllegalArgumentException If the {@link EFeatureInfo structure} of this feature
     *         {@link EFeatureInfo#validate(EPackage, EClass) fails to validate}.
     */
    public EFeatureInternal(EFeatureInfo eStructure, InternalEObject eImpl)
            throws IllegalArgumentException {
        //
        // Cache weak reference to implementation
        //
        this.eImpl = new WeakReference<InternalEObject>(eImpl);
        //
        // Set structure
        //
        setStructure(eStructure);        
        //
        // Add listeners that keep cached data in-sync with eImpl and structure
        //
        eImpl.eAdapters().add(getContainerAdapter());
    }

    // ----------------------------------------------------- 
    //  EFeature implementation
    // -----------------------------------------------------

    public String getID() {
        //
        // Verify current state
        //
        verify();
        //
        // Is checking if this is the ID holder?
        //
        if(isCheckingIDHolder) {
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
            isCheckingIDHolder = true;
        }
        
        //
        // Implementation holds the ID value. Get EFeature ID attribute.
        //
        EAttribute eAttribute = getStructure().eIDAttribute();
        //
        // Get current EFeature ID (will recurse into this method if ID holder)
        //
        Object eID = eImpl().eGet(eAttribute);
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
            isCheckingIDHolder = false;
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
            // Notify?
            //
            if (eImpl().eNotificationRequired())
                eImpl().eNotify(
                        new ENotificationImpl(eImpl(), Notification.SET,
                                EFeaturePackage.EFEATURE__SRID, oldSRID, newSRID));
        }
    }

    public Feature getData() {
        verify();
        //
        // Initialize data?
        //
        if (data == null) {
            data = new SimpleFeatureDelegate();
        }
        return data;
    }

    public void setData(Feature newData) {
        verify();
        // Sanity checks
        //
        if (newData == null) {
            throw new NullPointerException("Data can not be set to null");
        }

        // Prepare change delta
        //
        Feature oldData = data;

        // Is changed?
        //
        if (newData != oldData) {
            // Attempt to transform data into a valid structure
            //
            ETransform eTransform = create(newData);

            // Apply transform to delegate
            //
            newData = eTransform.apply();

            // Replace current data
            //
            data = newData;

            // Notify change to adapters
            //
            eTransform.eNotify();

        }
    }

    public boolean isSimple() {
        verify();
        return (getData() instanceof SimpleFeature);
    }

    public String getDefault() {
        verify();
        return getStructure().eGetDefaultGeometryName();
    }

    public void setDefault(String newDefault) {
        verify();
        String oldDefault = getDefault();
        getStructure().eSetDefaultGeometryName(newDefault);
        if (eImpl().eNotificationRequired())
            eImpl().eNotify(
                    new ENotificationImpl(eImpl(), Notification.SET,
                            EFeaturePackage.EFEATURE__DEFAULT, oldDefault, newDefault));
    }

    public EFeatureInfo getStructure() {
        return eStructure;
    }
    
    public void setStructure(EFeatureInfo eStructure) {
        //
        // Do sanity checks
        //
        if(eStructure==null) {
            throw new NullPointerException("EFeatureInfo structure can not be null");
        }
        //
        // Not already set?
        //
        if(this.eStructure!=eStructure) {
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
            if(!eStructure.eEqualTo(this.eStructure)) {
                validate(eStructure, eImpl());
            } 
            //
            // Unregister current?
            //
            if(this.eStructure!=null) {
                this.eStructure.removeListener(getStructureAdapter());
            }
            //
            // Is valid, initialize this instance
            //
            this.eStructure = eStructure;
            //
            // Add adapter that keep cached data in-sync with structure (SRID etc.)
            //
            eStructure.addListener(getStructureAdapter());            
        }
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
    //  Object implementation
    // -----------------------------------------------------

//    @Override
//    public String toString() {
//        StringBuffer result = new StringBuffer(super.toString());
//        result.append(" (data: ");
//        result.append(getData());
//        result.append(", structure: ");
//        result.append(eStructure);
//        result.append(')');
//        return result.toString();
//    }
    
    // ----------------------------------------------------- 
    //  EFeatureInternal methods
    // -----------------------------------------------------
        
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected static final EFeatureContext eInternalContext(InternalEObject eObject) {
        return EFeatureContextHelper.eContext(eObject);
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
            if (eImpl().eNotificationRequired())
                eImpl().eNotify(
                        new ENotificationImpl(eImpl(), Notification.SET,
                                EFeaturePackage.EFEATURE__ID, eOldID, eNewID));            
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
    
    protected static final void validate(EFeatureInfo eStructure, EObject eObject)
            throws IllegalArgumentException {
        //
        // Support multithread access
        //
        synchronized(eClassValidatedMap) {            
            //
            // Prepare
            //
            EClass eClass = eObject.eClass();
            EPackage ePackage = eClass.getEPackage();
            //
            // Get structures already verified for implementing class 
            // 
            Set<Long> eValidSet = eClassValidatedMap.get(eClass);
            //
            // Found no validated structures?
            //
            if( eValidSet == null) {
                eValidSet = new HashSet<Long>();
                eClassValidatedMap.put(eClass, eValidSet);
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
     * Get current list of {@link EFeatureProperty} instances.
     * <p>
     * This method implements lazy creation of {@link EFeatureProperty} instances.
     * </p>
     * 
     * @return list of {@link EFeatureProperty} instances.
     */
    protected Map<String, ? extends EFeatureProperty<?, ? extends Property>> getPropertyMap() {
        if (ePropertyMap == null) {
            // Get all attribute structures
            //
            Map<String, EFeatureAttributeInfo> eMap = getStructure().eGetAttributeInfoMap(true);

            // Initialize map
            //
            ePropertyMap = new HashMap<String, EFeatureProperty<?, ? extends Property>>(eMap.size());
            //
            // Loop over all attributes
            //
            for (EFeatureAttributeInfo it : eMap.values()) {
                EAttribute eAttribute = it.eAttribute();
                Class<?> type = eAttribute.getEAttributeType().getInstanceClass();
                String eName = it.eName();
                ePropertyMap.put(eName, newProperty(it.eName(), type));
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
        // Initialize
        //
        Collection<? extends EFeatureProperty<?, ? extends Property>> eList = getPropertyMap()
                .values();
        List<EFeatureProperty<V, Property>> eSelected = new ArrayList<EFeatureProperty<V, Property>>(
                eList.size());

        // Select EFeatureProperty instances with correct value type
        //
        for (EFeatureProperty<?, ? extends Property> it : eList) {
            // Has correct value type?
            //
            if (type.isAssignableFrom(it.getValueType())) {
                eSelected.add((EFeatureProperty<V, Property>) it);
            }
        }
        // Finished selection
        //
        return eSelected;
    }

    /**
     * Attempts to transform new data into valid form
     * <p>
     * 
     * @param newData - new {@link Feature}
     * @return a new {@link Feature} instance compatible with the structure of this {@link EFeature}
     *         instance.
     */
    protected ETransform create(Feature newData) {
        // Get structure
        //
        EFeatureInfo eStructure = getStructure();

        // Verify that given data is valid
        //
        EFeatureStatus s;
        if (!(s = eStructure.validate(newData)).isSuccess()) {
            throw new IllegalArgumentException(s.getMessage());
        }

        // Prepare transformation
        //
        ETransform eTransform = new ETransform();

        // Get new and old feature types
        //
        eTransform.oldType = getData().getType();
        eTransform.newType = newData.getType();

        // Get old and new CRS
        //
        eTransform.oldCRS = eTransform.oldType.getCoordinateReferenceSystem();
        eTransform.newCRS = eTransform.newType.getCoordinateReferenceSystem();

        // Get transformation
        //
        try {
            eTransform.transform = CRS
                    .findMathTransform(eTransform.newCRS, eTransform.oldCRS, true);
        } catch (FactoryException e) {
            throw new IllegalArgumentException("Tranform from " + "'" + eTransform.newCRS
                    + "' to '" + eTransform.oldCRS + "' not possible");
        }

        // Is identity transform?
        //
        eTransform.isIdentity = (eTransform.transform instanceof IdentityTransform);
        if (!eTransform.isIdentity) {
            // Get new SRID
            //
            eTransform.newSRID = CRS.toSRS(eTransform.newCRS, true);
        }

        // Prepare feature values, catching
        // any transformation errors before any
        // changes are committed to the model
        //
        eTransform.eValueMap = new HashMap<EAttribute, Object>();
        for (Property it : newData.getProperties()) {
            // Get attribute, null indicates that it does not exist
            // in the structure of this EFeature instance. If so,
            // just discard it (in line with using structures as filters)
            //
            String eName = it.getName().getLocalPart();
            EAttribute eAttribute = eStructure.eGetAttribute(eName);

            // EAttribute found in this structure.
            //
            if (eAttribute != null) {
                Object value = it.getValue();
                if (value instanceof Geometry) {
                    try {
                        value = JTS.transform((Geometry) value, eTransform.transform);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to " + "transform geometry: "
                                + it);
                    }
                }
                eTransform.eValueMap.put(eAttribute, value);
            }
        }

        // Finished
        //
        return eTransform;
    }

    protected InternalEObject eImpl() throws NullPointerException {
        InternalEObject eObject = eImpl.get();
        if (eObject == null) {
            throw (NullPointerException)(new NullPointerException("EFeature implementation is "
                    + "finalized (garbage collected).")).fillInStackTrace();
        }
        return eObject;
    }

    @SuppressWarnings("unchecked")
    public EFeatureProperty<?, ? extends Property> newProperty(String eName, Class<?> type) {
        if (Geometry.class.isAssignableFrom(type)) {
            return newGeometry(eName, (Class<? extends Geometry>) type);
        }
        return newAttribute(eName, type);
    }

    public <V> EFeatureAttribute<V> newAttribute(String eName, Class<V> type) {
        return EFeatureAttributeDelegate.create(eImpl(), eName, type, getStructure());
    }

    public <T extends Geometry> EFeatureGeometry<T> newGeometry(String eName, Class<T> type) {
        return EFeatureGeometryDelegate.create(eImpl(), eName, type, getStructure());
    }

    // ----------------------------------------------------- 
    //  Methods for keeping cached data in-sync
    // -----------------------------------------------------

    /**
     * Cached {@link Adapter}.
     * <p>
     * 
     * @see {@link #getContainerAdapter()}
     */
    protected WeakReference<AdapterImpl> eContainerlistener;

    /**
     * Cached {@link EFeatureListener}.
     * <p>
     * 
     * @see {@link #getContainerAdapter()}
     */
    protected WeakReference<EFeatureListener<EStructureInfo<?>>> eStructurelistener;

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
    protected Adapter getContainerAdapter() {
        if (eContainerlistener == null || eContainerlistener.get() == null) {
            eContainerlistener = new WeakReference<AdapterImpl>(new AdapterImpl() {

                @Override
                public void notifyChanged(Notification msg) {

                    Object feature = msg.getFeature();
                    if (msg.getEventType() == Notification.SET
                            && (msg.getNewValue() instanceof Geometry)
                            && (msg.getFeature() instanceof EStructuralFeature)) {
                        // Check if a geometry is changed. If it is, bounds
                        // must be re-calculated...
                        String eName = ((EStructuralFeature) feature).getName();
                        if (getStructure().isGeometry(eName)) {
                            // Reset bounds. This forces bounds of this
                            // feature to be recalculated on next call
                            // to SimpleFeatureDelegate#getBounds()
                            bounds = null;

                        }
                    }
                }
            });
        }
        return eContainerlistener.get();
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
    protected EFeatureListener<EStructureInfo<?>> getStructureAdapter() {
        if (eStructurelistener == null || eStructurelistener.get() == null) {
            eStructurelistener = new WeakReference<EFeatureListener<EStructureInfo<?>>>(
                    new EFeatureListener<EStructureInfo<?>>() {

                        public boolean onChange(EStructureInfo<?> eInfo, int property, Object oldValue,
                                Object newValue) {
                            if (property == EFeatureInfo.SRID) {
                                // Current bounds have wrong CRS.
                                // This forces current bounds
                                // to be recalculated on next call
                                // to getData().getBounds()
                                //
                                bounds = null;

                                // Notify
                                //
                                if (eImpl().eNotificationRequired()) {
                                    eImpl().eNotify(
                                            new ENotificationImpl(eImpl(), Notification.SET,
                                                    EFeaturePackage.EFEATURE__SRID, oldValue,
                                                    newValue));
                                }
                            }
                            return true;
                        }

                    });
        }
        return eStructurelistener.get();
    }

    // ----------------------------------------------------- 
    //  SimpleFeatureDelegate implementation
    // -----------------------------------------------------


    class SimpleFeatureDelegate implements ESimpleFeature {

        private FeatureId eID;

        private Map<Object, Object> userData;

        public String getID() {
            return getIdentifier().getID();
        }
        
        public EObject eObject() {
            EObject eObject = eImpl();
            if(eObject instanceof EFeatureDelegate) {
                eObject = ((EFeatureDelegate)eObject).eDelegate.get();
            }
            return eObject;
        }
        
        public EFeature eFeature() {
            EObject eObject = eImpl();
            if(eObject instanceof EFeature) {
                return (EFeature)eObject;
            }
            return EFeatureDelegate.create(eStructure, eImpl());
        }
        
        public FeatureId getIdentifier() {
            //
            // Create id?
            //
            if (eID == null) {
                //
                // Get EMF id attribute
                //
                EAttribute eIDAttribute = getStructure().eIDAttribute();
                //
                // Get feature id as string
                //
                String fid = (eIDAttribute == null || !eImpl().eIsSet(eIDAttribute) ? null
                        : EcoreUtil.convertToString(eIDAttribute.getEAttributeType(),
                                eImpl().eGet(eIDAttribute)));
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
                for (EFeatureGeometry<Geometry> it : getGeometryList(Geometry.class)) {
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

        public GeometryAttribute getDefaultGeometryProperty() {
            // Get EFeatureGeometry structure
            //
            EFeatureGeometry<Geometry> eGeometry = newGeometry(getDefault(), Geometry.class);
            //
            // Found geometry?
            //
            if (eGeometry != null) {
                // Get attribute
                //
                return eGeometry.getData();
            }

            // Not found, return null;
            //
            return null;
        }

        public void setDefaultGeometryProperty(GeometryAttribute attribute) {
            getStructure().eSetDefaultGeometryName(attribute.getName().getURI());
        }

        public Collection<? extends Property> getValue() {
            return getProperties();
        }

        @SuppressWarnings("unchecked")
        public void setValue(Object newValue) {
            setValue((Collection<Property>) newValue);
        }

        public void setValue(Collection<Property> values) {
            for (Property it : values) {
                EAttribute eAttr = getStructure().eGetAttribute(it.getName().getURI());
                eImpl().eSet(eAttr, it.getValue());
            }
        }

        public Property getProperty(Name name) {
            return getProperties(name.getLocalPart()).iterator().next();
        }

        public Collection<Property> getProperties(String eName) {
            Set<Property> eItems = new HashSet<Property>();
            for (Property it : getProperties()) {
                if (it.getName().getLocalPart().equals(eName)) {
                    eItems.add(it);
                }
            }
            return eItems;
        }

        public Collection<Property> getProperties(Name eName) {
            Set<Property> eItems = new HashSet<Property>();
            for (Property it : getProperties()) {
                if (it.getName().equals(eName)) {
                    eItems.add(it);
                }
            }
            return eItems;
        }

        public List<Property> getProperties() {
            // Initialize
            //
            List<Property> eList = new ArrayList<Property>();
            //
            // Loop over all EFeatureProperty instances,
            // collecting current Property instances.
            //
            for (EFeatureProperty<?, ? extends Property> it : getPropertyMap().values()) {
                eList.add(it.getData());
            }
            // Finished
            //
            return Collections.unmodifiableList(eList);
        }

        public Property getProperty(String eName) {
            // Get instance, returns null if not found
            //
            EFeatureProperty<?, ? extends Property> eProperty = getPropertyMap().get(eName);
            //
            // Get property instance, return null if not found
            //
            return (eProperty != null ? eProperty.getData() : null);
        }

        public void validate() throws IllegalAttributeException {
            // Loop over all property instances,
            // calling validate on attributes
            //
            for (Property it : getProperties()) {
                ((Attribute) it).validate();
            }
        }

        public AttributeDescriptor getDescriptor() {
            // Is top-level attribute (feature)
            return null;
        }

        public Name getName() {
            return new NameImpl(getStructure().eName());
        }

        public boolean isNillable() {
            return false;
        }

        public Map<Object, Object> getUserData() {
            if (userData == null) {
                userData = new HashMap<Object, Object>();
            }
            return userData;
        }

        public SimpleFeatureType getType() {
            return getStructure().getFeatureType();
        }

        public SimpleFeatureType getFeatureType() {
            return getStructure().getFeatureType();
        }

        public List<Object> getAttributes() {
            // Initialize
            //
            List<Object> values = new ArrayList<Object>(getPropertyMap().size());

            // Loop over all property instances
            //
            for (Property it : getProperties()) {
                values.add(it.getValue());
            }
            // Finished
            //
            return values;
        }

        public void setAttributes(List<Object> values) {
            setAttributes(values.toArray(new Object[0]));
        }

        public void setAttributes(Object[] values) {
            // Loop over all property instances,
            // calling validate on attributes
            //
            int i = 0;
            for (Property it : getProperties()) {
                ((Attribute) it).setValue(values[i]);
            }
        }

        public Object getAttribute(String eName) {
            Property p = getProperty(eName);
            return (p != null ? p.getValue() : null);
        }

        public void setAttribute(String eName, Object newValue) {
            Property p = getProperty(eName);
            if (p != null) {
                p.setValue(newValue);
            }
        }

        public Object getAttribute(Name eName) {
            Property p = getProperty(eName);
            return (p != null ? p.getValue() : null);
        }

        public void setAttribute(Name eName, Object newValue) {
            Property p = getProperty(eName);
            if (p != null) {
                p.setValue(newValue);
            }
        }

        public Object getAttribute(int index) throws IndexOutOfBoundsException {
            Property p = getProperties().get(index);
            return p.getValue();
        }

        public void setAttribute(int index, Object newValue) throws IndexOutOfBoundsException {
            Property p = getProperties().get(index);
            if (p != null) {
                p.setValue(newValue);
            }
        }

        public int getAttributeCount() {
            return getPropertyMap().size();
        }

        public Geometry getDefaultGeometry() {
            Property p = getDefaultGeometryProperty();
            return (Geometry) (p != null ? p.getValue() : null);
        }

        public void setDefaultGeometry(Object newGeometry) {
            Property p = getDefaultGeometryProperty();
            if (p != null) {
                p.setValue(newGeometry);
            }
        }
                
    }

    // ----------------------------------------------------- 
    //  ETransform helper class
    // -----------------------------------------------------


    class ETransform {
        String newSRID;

        Feature oldData;

        Feature newData;

        FeatureType oldType;

        FeatureType newType;

        MathTransform transform;

        CoordinateReferenceSystem oldCRS;

        CoordinateReferenceSystem newCRS;

        boolean isIdentity;

        Map<EAttribute, Object> eValueMap;

        /**
         * Apply transform to delegate.
         * <p>
         * Update delegate directly without any additional validation, since it is already
         * established that the data is valid.
         * 
         * @return a new {@link Feature} instance
         */
        public Feature apply() {
            EObject eImpl = eImpl();
            boolean eDeliver = eImpl.eDeliver();
            try {
                eImpl.eSetDeliver(false);

                // Update delegate directly without any
                // additional validation, since it is
                // already established that the data is
                // valid.
                for (Entry<EAttribute, Object> it : eValueMap.entrySet()) {
                    eImpl.eSet(it.getKey(), it.getValue());
                }

                // Update SRID for all instances?
                //
                if (!isIdentity) {
                    getStructure().setSRID(newSRID);
                }

                // Create new delegate
                //
                newData = new SimpleFeatureDelegate();

                // Finished
                //
                return newData;

            } finally {
                eImpl.eSetDeliver(eDeliver);
            }

        }

        public void eNotify() {
            // Notify change listeners?
            //
            if (eImpl().eNotificationRequired()) {
                eImpl().eNotify(
                        new ENotificationImpl(eImpl(), Notification.SET,
                                EFeaturePackage.EFEATURE__DATA, oldData, newData));
            }
        }

    }

}