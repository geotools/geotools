package org.geotools.data.efeature;

import static org.geotools.data.efeature.EFeatureConstants.DEFAULT_SRID;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.Adler32;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.impl.ESimpleFeatureImpl;
import org.geotools.data.efeature.internal.EFeatureVoidIDFactory;
import org.geotools.factory.Hints.Key;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author kengu
 *
 */
public class EFeatureInfo extends EStructureInfo<EStructureInfo<?>> {
            
    /**
     * Mutable property: {@link #getSRID()} </p>
     * 
     * @see {@link #setSRID(String)} - invalidates all {@link EFeatureGeometryInfo#getDescriptor()
     *      geometry descriptors}
     * @see {@link EStructureInfo#addListener(EStructureInfoListener)} - listen for SRID changes.
     */
    public static final int SRID = EFeaturePackage.EFEATURE__SRID;

    /**
     * Mutable property: {@link #eGetDefaultGeometryName()} </p>
     * 
     * @see {@link #eSetDefaultGeometryName(String)}
     * @see {@link EStructureInfo#addListener(EStructureInfoListener)} - listen for default geometry
     *      name changes.
     */
    public static final int DEFAULT_GEOMETRY_NAME = 2;
    
    /**
     * @see #eUID()
     */
    protected Long eUID;

    protected String eNsURI;

    protected String eFolderName;

    protected String eClassName;

    protected String eReferenceName;

    protected String srid = DEFAULT_SRID;

    protected CoordinateReferenceSystem crs;

    protected boolean eIsRoot = true;

    protected String eIDAttributeName;

    protected String eSRIDAttributeName;
    
    protected String eDefaultAttributeName;
    
    protected String eDefaultGeometryName;

    protected WeakReference<EClass> eClass;

    protected WeakReference<EClass> eParentClass;

    protected WeakReference<EReference> eReference;

    protected SimpleFeatureType featureType;
    
    protected SimpleFeatureBuilder builder;

    /**
     * Map of name to all geometry {@link EAttribute}s.
     * (optimization)
     */
    protected Map<String,EAttribute> eGeometryMap;
        
    /**
     * Map of name to all non-geometry {@link EAttribute}s.
     * (optimization)
     */
    protected Map<String,EAttribute> eAttributeMap;
        
    /**
     * Map of name to all non-geometry {@link EAttribute}s.
     * (optimization)
     */
    protected Map<String,EAttribute> eAllAttributeMap;
        
    /**
     * Maps {@link #eClass()} attributes to attributes in {@link EFeature}.
     */
    protected Map<EAttribute,EAttribute> eMappingMap = new HashMap<EAttribute, EAttribute>();

    /**
     * {@link EAttribute} id to non-geometry {@link EFeatureAttributeInfo} instance {@link Map}
     */
    protected Map<String, EFeatureAttributeInfo> eAttributeInfoMap;

    /**
     * {@link EFeatureGeometry} id to {@link EFeatureGeometryInfo} instance {@link Map}
     */
    public Map<String, EFeatureGeometryInfo> eGeometryInfoMap;
    
    /**
     * {@link EAttribute} id to all {@link EFeatureAttributeInfo} instances {@link Map}
     * (optimization)
     */
    protected Map<String, EFeatureAttributeInfo> eAllAttributeInfoMap;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * Default constructor
     */
    protected EFeatureInfo() { /*NOP*/ }
    
    /**
     * {@link EFeatureInfo} copy constructor.
     * <p>
     * This method copies the structure into given context.
     * <p> 
     * If the parent structure is not found in the new context,
     * the new instance become a 
     * {@link EStructureInfo#isPrototype() prototype}. 
     * </p>
     * <b>NOTE</b>: This method only adds a one-way reference from 
     * copied instance to given {@link EFeatureContext context}. 
     * No reference is added from the context to this feature. It does not
     * attach the {@link EFeatureInfo} instance with given 
     * {@link EFeatureContext context} {@link EFeatureContextInfo structure}, 
     * nor adds the {@link EClass#getEIDAttribute()} to the 
     * {@link EFeatureContext#eIDFactory()}. This must be done manually.
     * </p>  
     * @param eInfo - copy from this {@link EFeatureInfo} instance
     * @param eContextInfo - copy into this context
     * @see {@link EFeatureContextInfo#doAdapt(EFeatureInfo, boolean)}
     */
    protected EFeatureInfo(EFeatureInfo eInfo, EFeatureContextInfo eContextInfo) {
        //
        // Forward (copies context, state and hints)
        //
        super(eInfo, eContextInfo);        
        //
        // Mark as being structural equal to given structure
        //
        this.eUID = eInfo.eUID;
        //
        // Copy context path
        //
        this.eNsURI = eInfo.eNsURI;
        this.eFolderName = eInfo.eFolderName;
        //
        // Copy EClass information
        //        
        this.eClassName = eInfo.eClassName;
        this.eClass = new WeakReference<EClass>(eInfo.eClass());
        //
        // Copy parent EClass information (folder is an EClass)
        //                
        this.eReferenceName = eInfo.eReferenceName;
        this.eReference = new WeakReference<EReference>(eInfo.eReference());
        this.eParentClass = new WeakReference<EClass>(eInfo.eParentClass());
        //
        // Copy ID attribute information
        //                
        this.eIDAttributeName = eInfo.eIDAttributeName;
        //
        // Copy SRID attribute information
        //                
        this.eSRIDAttributeName = eInfo.eSRIDAttributeName;
        //
        // Copy default geometry attribute information
        //                
        this.eDefaultAttributeName = eInfo.eDefaultAttributeName;
        //
        // Copy other attributes 
        //
        this.isAvailable = eInfo.isAvailable;        
        //
        // Prepare new hash maps
        //
        this.eGeometryInfoMap = EFeatureUtils.newMap(eInfo.eGeometryInfoMap);
        this.eAttributeInfoMap = EFeatureUtils.newMap(eInfo.eAttributeInfoMap);
        //
        // Loop over all attribute structures, copy them and add to maps
        //
        for(EFeatureAttributeInfo it : eInfo.eAttributeInfoMap.values()) {
            it = new EFeatureAttributeInfo(it,this);
            eAttributeInfoMap.put(it.eName,it);
        }
        for(EFeatureGeometryInfo it : eInfo.eGeometryInfoMap.values()) {
            it = new EFeatureGeometryInfo(it,this);
            eGeometryInfoMap.put(it.eName,it);
        }
        //
        // Pass mappings directly (is copied when made immutable below)
        //
        eMappingMap = eInfo.eMappingMap;
        //
        // Optimize structure lookup methods
        //
        eOptimize(this);
        //
        // Make structure immutable (it is allowed to not be modified)
        //
        eImmutable(this);
    }
    
    /**
     * Unique ID which enables efficient <i>{@link EFeatureInfo structure} 
     * equivalence</i> checking.
     * <p> 
     * <b>NOTE</b>: This equivalence is not the same as 
     * <i>{@link Object#equals(Object) object equivalence}</i>,
     * it only states that the structure is equal. Since the 
     * structure is immutable, a single unique value is enough to 
     * compare two structures for equivalence. Mutable fields like 
     * {@link #getSRID()} is not part of the structure. It can therefore 
     * not be assumed that if two structure have the same {@link #eUID}, then
     * the {@link #getSRID()} are also the same. 
     * </p>
     * @throws IllegalStateException  If {@link EStructureInfo#isDisposed() disposed} or 
     * {@link EStructureInfo#isValid() not valid}.
     */
    public Long eUID() {
        verify(true);
        return eUID;
    }
    
    @Override
    public boolean isAvailable() {
        // --------------------------------------------------------
        //  Is available and is a prototype or has at least 
        //  one geometry defined?
        // --------------------------------------------------------
        //  Prototypes must be available since prototypes are used 
        //  in queries matching all EFeature implementations. If 
        //  prototypes are unavailable, getFeatureType() returns
        //  null, which breaks the construction of query instances.
        //
        //  See EFeatureUtils#toEFeatureQuery(TreeIterator,Filter)
        // --------------------------------------------------------
        //
        return super.isAvailable() && (isPrototype() || !eGeometryInfoMap.isEmpty());
    }

    /**
     * Check if {@link EFeature} is a root.
     * <p>
     * A {@link EFeature} roots are not referenced (contained).
     * </p>
     * 
     * @return <code>true</code> if EObject root.
     */
    public boolean isRoot() {
        return eIsRoot;
    }
            
    /**
     * Get {@link Feature} name.
     * <p>
     * If this {@link #isRoot() is a root}, the {@link EClass} name is returned, else the name of
     * the {@link EObject#eContainmentFeature() containment reference} to this {@link EFeature} is
     * returned.
     * </p>
     * 
     * @return the {@link EFeature} name.
     */
    public String eName() {
        return eIsRoot ? eClassName : eReferenceName;
    }
        
    public String eNsURI() {
        return eNsURI;
    }

    public String eFolderName() {
        return eFolderName;
    }

    /**
     * Get EFeature {@link EClass}
     */
    public EClass eClass() {
        return (eClass!=null ? eClass.get() : null);
    }

    /**
     * Get EFeature {@link EClass} name
     */
    public String eClassName() {
        return eClassName;
    }

    /**
     * Get name of {@link EReference} referencing this {@link EFeature}.
     * <p>
     * This is only set if the {@link EFeature} is not {@link #isRoot() a root}.
     * </p>
     */
    public EReference eReference() {
        return  (eReference!=null ? eReference.get() : null);
    }

    /**
     * Get name of {@link EReference} referencing this {@link EFeature}.
     * <p>
     * This is only set if the {@link EFeature} is not {@link #isRoot() a root}.
     * </p>
     */
    public String eReferenceName() {
        return eReferenceName;
    }

    /**
     * Get this {@link EClass parent} class.
     */
    public EClass eParentClass() {
        return eParentClass!=null ? eParentClass.get() : null;
    }

    /**
     * Get name of EFeature parent {@link EClass}
     */
    public String eParentClassName() {
        return eParentClass != null ? eParentClass().getName() : null;
    }
    
    /**
     * Get {@link EStructureInfo structure parent} instance.
     */
    @Override
    protected EStructureInfo<?> eParentInfo(boolean checkIsValid) {
        EFeaturePackageInfo eInfo = eContext(checkIsValid).
            eStructure().eGetPackageInfo(eNsURI);
        if(eInfo!=null) {
            return eInfo.eGetFolderInfo(eFolderName);
        }
        return eContext(checkIsValid).eStructure();
    }
    
    /**
     * Get ID {@link EAttribute}
     * 
     * @return an {@link EAttribute} instance
     */
    public EAttribute eIDAttribute() {
        return eGetAttribute(eIDAttributeName);
    }
    
    /**
     * Get SRID {@link EAttribute}
     * 
     * @return an {@link EAttribute} instance
     */
    public EAttribute eSRIDAttribute() {
        return eGetAttribute(eSRIDAttributeName);
    }
    
    /**
     * Get default {@link EFeatureGeometry} attribute.
     * <p>
     * 
     * @return a {@link EAttribute} instance, or <code>null</code> if no {@link EFeatureGeometry
     *         geometries} are defined.
     */
    public EAttribute eDefaultGeometry() {
        return eGetAttribute(eGetDefaultGeometryName());
    }
    
    
    public String eGetDefaultGeometryName() {
        //
        // 1) Try hints first
        //
        //
        if (eDefaultGeometryName == null) {
            for(String it : eGetDefaultGeometryNames(eHints)) {
                if(eGeometryInfoMap.containsKey(it)) {
                    eDefaultGeometryName = it;
                }
            }
        }
        // 2) If no hints was found, try to find a geometry attribute
        //            
        if (eDefaultGeometryName == null) {
            String eFirstFound = null;
            for (Entry<String, EFeatureGeometryInfo> it : eGeometryInfoMap.entrySet()) {
                if (eFirstFound == null) {
                    eFirstFound = it.getKey();
                }
                if (it.getValue().isDefaultGeometry) {
                    eDefaultGeometryName = it.getKey();
                    break;
                }
            }
            if (eDefaultGeometryName == null) {
                if( !(eFirstFound==null || eFirstFound.length()==0) ) { 
                    eGeometryInfoMap.get(eFirstFound).isDefaultGeometry = true;
                } else {
                    eFirstFound = EFeatureConstants.DEFAULT_GEOMETRY_NAME;
                }
                eDefaultGeometryName = eFirstFound;
            }
        }
        return eDefaultGeometryName;
    }

    public EFeatureGeometryInfo eSetDefaultGeometryName(String eNewName) {
        String eOldName = eGetDefaultGeometryName();
        if (!(eNewName == null || eNewName.length() == 0 && !eNewName.equals(eOldName))) {
            EFeatureGeometryInfo eOldInfo = eGeometryInfoMap.get(eOldName);
            EFeatureGeometryInfo eNewInfo = eGeometryInfoMap.get(eNewName);
            if (eNewInfo != null) {
                //
                // Update
                //
                eDefaultGeometryName = eNewName;
                //
                // Keep structures in-sync
                //
                if (eOldInfo != null) {
                    eOldInfo.setIsDefaultGeometry(false);
                }
                eNewInfo.setIsDefaultGeometry(true);
                //
                // Notify change of mutable property
                //
                fireOnChange(DEFAULT_GEOMETRY_NAME, eOldName, eNewName);
                //
                // Change completed
                //
                return eNewInfo;
            }
        }
        // No change
        //
        return null;
    }
    
    public EFeatureGeometryInfo eDefaultGeometryInfo() {
        return eGeometryInfoMap.get(eDefaultGeometryName);
    }    

    /**
     * Get spatial reference ID.
     * @return a SRID
     * @see {@link CRS} - utility class for converting SRIDs into a 
     * {@link CoordinateReferenceSystem} 
     */
    public String getSRID() {
        return srid;
    }

    /**
     * Set spatial reference ID for all {@link EFeature} instances
     */
    public CoordinateReferenceSystem setSRID(String newSRID) throws IllegalArgumentException {
        final String oldSRID = getSRID();
        if (isAvailable() && !(newSRID == null || newSRID.length() == 0)
                && !newSRID.equals(oldSRID)) {
            // Update Spatial reference id
            //
            this.srid = newSRID;

            // Try create new CRS from SRID
            //
            try {
                CRSCache.decode(this, true);
            } catch (Exception e) {
                // Rollback to old SRID
                //
                srid = oldSRID;

                // Throw expected exception
                //
                throw new IllegalArgumentException("Failed to decode SRID " + "'" + srid + "'", e);
            }

            // Keep structures in-sync
            // (use this.srid since decodeCRS may enforce default value)
            //
            setSRID(oldSRID, this.srid, this.crs);

            // Change completed
            //
            return this.crs;

        }
        // No change
        //
        return null;
    }

    protected final void setSRID(String oldSRID, String newSRID, CoordinateReferenceSystem newCRS) {
        // Update Spatial reference id
        //
        this.srid = newSRID;

        // Keep structures in-sync
        //
        for (EFeatureGeometryInfo it : eGeometryInfoMap.values()) {
            it.setSRID(newSRID, newCRS);
        }

        // Notify listener of changes mutable property
        //
        fireOnChange(SRID, oldSRID, newSRID);

    }    

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if(crs==null) {
            try {
                crs = CRSCache.decode(this, true);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
                throw new RuntimeException("Failed to decode SRID",e);
            }
        }
        return crs;
    }

    public SimpleFeatureType getFeatureType() {
        if (isAvailable() && featureType == null) {
            featureType = new InnerSimpleFeatureTypeImpl();
        }
        return featureType;
    }

    public EAttribute eGetAttribute(String eName) {
        EFeatureAttributeInfo eInfo = eGetAttributeInfo(eName, true);
        if (eInfo != null) {
            return eInfo.eAttribute();
        }
        return null;
    }

    public EFeatureAttributeInfo eGetAttributeInfo(String eName, boolean all) {
        EFeatureAttributeInfo eInfo = eAttributeInfoMap.get(eName);
        if (eInfo == null && all) {
            return eGetGeometryInfo(eName);
        }
        return eInfo;
    }

    public EFeatureGeometryInfo eGetGeometryInfo(String eName) {
        return eGeometryInfoMap.get(eName);
    }

    public boolean isGeometry(String eName) {
        return eGeometryInfoMap.containsKey(eName);
    }

    public boolean isDefaultGeometry(String eName) {
        return eDefaultGeometryName != null ? eDefaultGeometryName.equals(eName) : false;
    }

    public List<EAttribute> eGetAttributeList(boolean all) {        
        Collection<EAttribute> eItems =
            (all ? eAllAttributeMap.values() : eAttributeMap.values());
        List<EAttribute> eList = new ArrayList<EAttribute>(eItems);
        return Collections.unmodifiableList(eList);
    }
    
    public List<EAttribute> eGetGeometryList() {        
        Collection<EAttribute> eItems = eGeometryMap.values();
        List<EAttribute> eList = new ArrayList<EAttribute>(eItems);
        return Collections.unmodifiableList(eList);
    }
    
    
    public Map<String, EAttribute> eGetAttributeMap(boolean all) {
        return (all ? eAttributeMap : eAllAttributeMap);
    }

    public Map<String, EAttribute> eGetAttributeMap(String[] eNames, boolean all) {
        Map<String, EAttribute> eFoundMap = new HashMap<String, EAttribute>();
        Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass());
        for (String eName : eNames) {
            if (eAttrMap.containsKey(eName)
                    && (eAttributeInfoMap.containsKey(eName) || all
                            && eGeometryInfoMap.containsKey(eName))) {
                eFoundMap.put(eName, eAttrMap.get(eName));
            }
        }
        return Collections.unmodifiableMap(eFoundMap);
    }
    
    public List<EFeatureAttributeInfo> eGetAttributeInfoList(boolean all) {
        Collection<EFeatureAttributeInfo> eItems =
            (all ? eAllAttributeInfoMap.values() : eAttributeInfoMap.values());
        List<EFeatureAttributeInfo> eList = new ArrayList<EFeatureAttributeInfo>(eItems);
        return Collections.unmodifiableList(eList);
    }
    
    public List<EFeatureGeometryInfo> eGetGeometryInfoList() {
        Collection<EFeatureGeometryInfo> eItems = eGeometryInfoMap.values();
        List<EFeatureGeometryInfo> eList = new ArrayList<EFeatureGeometryInfo>(eItems);
        return Collections.unmodifiableList(eList);
    }
    
    
    public Map<String, EFeatureAttributeInfo> eGetAttributeInfoMap(boolean all) {
        return (all ? eAllAttributeInfoMap : eAttributeInfoMap);
    }

    public Map<String, EFeatureGeometryInfo> eGetGeometryInfoMap() {
        return Collections.unmodifiableMap(eGeometryInfoMap);
    }

    /**
     * Check if a mapping for given {@link EStructuralFeature} exists
     * @param eAttribute - mapped attribute
     * @return an <code>true</code> if a mapping is found
     */
    public final boolean eMappingExists(EStructuralFeature eFeature) {
        return eMappingMap.containsKey(eFeature);
    }
    
    /**
     * Get {@link EAttribute} mapped to given {@link EAttribute}
     * @param eAttribute - mapped attribute
     * @return an {@link EAttribute} if found.
     * @throws IllegalArgumentException If given attribute is not 
     * mapping to anything in this structure
     */
    public final EAttribute eMappedTo(EAttribute eAttribute) 
        throws IllegalArgumentException {
        //
        // Get mapping from given attribute to implementation
        //
        eAttribute = eMappingMap.get(eAttribute);
        //
        // Validate
        //
        if(eAttribute==null) {
            throw new IllegalArgumentException("EAttribute " + 
                    eAttribute + " is not mapped by " + this);
        }
        //
        // Finished
        //
        return eAttribute;
    }
        
    /**
     * Validate given {@link EObject object} against this structure.
     * <p>
     * 
     * </p>
     * @param eObject - the {@link EObject} instance
     * 
     * @return <code>true</code> if valid, <code>false</code> otherwise.
     */
    public EFeatureStatus validate(EObject eObject) {
        //
        // Prepare
        //
        EClass eClass = eObject.eClass();
        EPackage ePackage = eClass.getEPackage();
        //
        // Get parent class?
        //
        EClass eParent = eIsRoot ? null : EFeatureUtils.eGetContainingClass(eObject);
        //
        // Forward
        //
        return validate(ePackage,eParent);
    }

    /**
     * Validate {@link EFeature} structure.
     * <p>
     * If this {@link EFeature} is not {@link #isRoot() a root}, given {@link EClass} instance is
     * validated against the {@link #eReference() reference name} to ensure that such a reference
     * exist.
     * </p>
     * @param ePackage - given {@link EPackage} instance
     * @param eParent - {@link EClass} of given {@link EFeature} parent instance
     * @return <code>true</code> if valid, <code>false</code> otherwise.
     */
    public EFeatureStatus validate(EPackage ePackage, EClass eParent) {
        //
        // Initialize
        //
        EFeatureStatus s;
        //
        // Invalidate structure
        //
        doInvalidate(false);
        //
        // 1) Validate reference?
        //
        EReference eReference = null;
        if (!eIsRoot) {
            if (eParent == null) {
                return failure(this,
                        eName(), "Feature mismatch: Flagged as !eIsRoot() but parent class is null");
            }
            eReference = EFeatureUtils.eGetReference(eParent, this.eReferenceName);
            if (eReference == null) {
                return failure(this, eName(), "Feature mismatch: EReference " + this.eReferenceName
                        + " not found");
            }
        }
        //
        // 2) Validate EClass name
        //
        EClassifier eClassifier = ePackage.getEClassifier(eClassName);
        if (!(eClassifier instanceof EClass)) {
            return failure(this, eName(), "Feature mismatch: EClass " + eClassName + " not found");
        }
        EClass eClass = ((EClass) eClassifier);
        //
        // Get list of all EAttributes
        //
        Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass);
        //
        // 3) Validate ID attribute
        //
        EAttribute eAttribute = eClass.getEIDAttribute();
        if (eAttribute != null) {
            //
            // Get attribute name
            //
            String eName = eAttribute.getName();
            //
            // Get EAttribute ID instance
            //
            EFeatureAttributeInfo eInfo = eGetAttributeInfo(eName, true);
            //
            // Validate attribute
            //
            if(!(s = eInfo.validate(true, eAttribute)).isSuccess()) {
                //
                // Given EClass specified ID attribute was not a valid
                //
                return s;
            }
        } else if (!eAttrMap.containsKey(eIDAttributeName)) {
            return failure(this, eName(), "Feature mismatch: ID EAttribute '" + eIDAttributeName + "' not found");
        }
        //
        // EClass specified ID attribute is already validated
        //
        eAttrMap.remove(eAttribute);
        //
        // 4) Validate attributes
        //
        for (EFeatureAttributeInfo it : eAttributeInfoMap.values()) {
            eAttribute = eAttrMap.get(it.eName);
            if (eAttribute == null) {
                return failure(this, eName(), "Feature mismatch: EAttribute " + it.eName
                        + " not found in EClass");
            }
            boolean isID = eAttribute.getName().equals(eIDAttributeName);
            if (!(s = it.validate(isID, eAttribute)).isSuccess()) {
                return s;
            }
        }
        //
        // 5) Validate geometries
        //
        for (EFeatureGeometryInfo it : eGeometryInfoMap.values()) {
            eAttribute = eAttrMap.get(it.eName);
            if (eAttribute == null) {
                return failure(this, eName(), "Feature mismatch: EGeometry " + it.eName
                        + " not found in EClass");
            }
            boolean isID = eAttribute.getName().equals(eIDAttributeName);
            if (!(s = it.validate(isID, eAttribute)).isSuccess()) {
                return s;
            }
        }
        //
        // Store valid state
        //
        this.isValid = true;
        //
        // Store as weak references. This prevents memory leakage
        // when doDispose() is not called explicitly.
        //
        this.eParentClass = new WeakReference<EClass>(eParent);
        this.eReference = new WeakReference<EReference>(eReference);        
        //
        // Create structure checksum
        //
        eUID = checksum();
        //
        // Confirm that structure is valid
        //
        return structureIsValid(eName());
    }

    /**
     * Validate feature against this structure.
     * 
     * @param feature - feature to be validated
     * @return <code>true</code> if valid.
     */
    public EFeatureStatus validate(Feature feature) {
        //
        // TODO: Does this work?
        //
        if (!getFeatureType().equals(feature.getType())) {
            return featureIsInvalid();
        }
        // Confirm that feature is valid
        //
        return featureIsValid();
    }

    /**
     * Check if given {@link EFeatureInfo structure} is equal to this 
     * @param eInfo - given structure
     * @return <code>true</code> if structures are equal
     */
    public boolean eEqualTo(EFeatureInfo eInfo) {
        return eEqualTo(eInfo,true);
    }
    
    /**
     * Get {@link EFeature#getID() ID} of given EObject
     * @param eObject - an {@link EObject} instance
     */
    public String toID(EObject eObject) {
        return (String)eObject.eGet(eIDAttribute());
    }
    
    /**
     * Create {@link ESimpleFeatureImpl} instance from given object.
     * @param eObject - an {@link EObject} instance
     * @param transaction TODO
     */
    public ESimpleFeatureImpl toFeature(EObject eObject, Transaction transaction) {
        return new ESimpleFeatureImpl(this,eObject, transaction);
    }
    
    public Map<String, Integer> eGetAttributeIndex() {
        return ((InnerSimpleFeatureTypeImpl)getFeatureType()).index;
    }
    
    public EObject eNewInstance() {
        return EcoreUtil.create(eClass());
    }
    
    // ----------------------------------------------------- 
    //  Object methods
    // -----------------------------------------------------
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + eContextID + "]";
    }
    
    // ----------------------------------------------------- 
    //  EStructureInfo implementation
    // -----------------------------------------------------
    
    @Override
    protected void doInvalidate(boolean deep) {
        //
        // Dispose weak references
        //
        this.eParentClass = null;
        this.eReference = null;
        //
        // Do a deep invalidation?
        //
        if (deep) {
            for (EStructureInfo<?> it : eGetAttributeInfoList(true)) {
                it.doInvalidate(true);
            }
        }
        
    }
    
    @Override
    protected void doAdapt() {        
        // 
        // Update ALL attributes (includes geometry structures)
        // 
        for(EFeatureAttributeInfo it : eGetAttributeInfoList(true)) {
            it.eAdapt(this);
        }        
    }
    
    @Override
    protected void doDetach() {
        //
        // Detach from old context
        //
        eContext(false).eStructure().doDetach(this);
    }

    @Override
    protected void doDispose() {
        //
        // Remove from EFeatureInfo cache in given context 
        // (required to prevent memory leakage)
        //
        eContext(false).eStructure().eFeatureInfoCache.detach(this);
        //
        // Forward to sub-structures
        //
        for (EFeatureAttributeInfo it : eGetAttributeInfoList(true)) {
            it.dispose();
        }
        //
        // Clear maps
        //        
        this.eGeometryMap.clear();
        this.eAttributeMap.clear();
        this.eAllAttributeMap.clear();
        this.eGeometryInfoMap.clear();
        this.eAttributeInfoMap.clear();
        this.eAllAttributeInfoMap.clear();
        //
        // Clear cached references to allow garbage collection
        //
        this.crs = null;
        this.eClass = null;
        this.eParentClass = null;
        this.eReference = null;
        this.featureType = null;
        this.eGeometryMap = null;
        this.eAttributeMap = null;
        this.eAllAttributeMap = null;
        this.eGeometryInfoMap = null;
        this.eAttributeInfoMap = null;
        this.eAllAttributeInfoMap = null;
    }
    
    /**
     * Check if given {@link EFeatureInfo structure} is equal to this 
     * @param eInfo - given structure
     * @return <code>true</code> if structures are equal
     */
    protected boolean eEqualTo(EFeatureInfo eInfo, boolean checkIfValid) {
        verify(checkIfValid);
        return !(eUID==null || eInfo==null) ? eUID.equals(eInfo.eUID) : false;
    }    

    /**
     * Calculate structure checksum.
     * <p>
     * This method calculates the checksum by repeatedly applying
     * {@link #eFeatureUID(EStructuralFeature)} on every 
     * {@link EStructuralFeature} in this structure, concatenating 
     * them together and finally calculating a checksum from the 
     * concatenated string of unique feature IDs.
     * @return a {@link Adler32} checksum for given structure
     */
    protected final Long checksum() {
        //
        // Prepare array of IDs
        //
        List<String> eIDs = new ArrayList<String>();
        //
        // Add EFeature ID EAttribute feature ID
        //
        eIDs.add(eFeatureUID(eIDAttribute()));
        //
        // Add all EAttribute feature IDs
        //
        for(EAttribute it : eGetAttributeMap(true).values()) {
            eIDs.add(eFeatureUID(it));
        }
        //
        // Add reference?
        //
        if(!isRoot()){
            eIDs.add(eFeatureUID(eReference()));
        }
        //
        // Sort array (structure is unordered, order it to ensure equal checksum)
        //        
        Collections.sort(eIDs);
        //
        // Calculate checksum (Adler32 is much faster then CRC-32 and almost as accurate)
        //                
        Adler32 a = new Adler32();
        a.update(Arrays.toString(eIDs.toArray()).toString().getBytes());
        //
        // Finished
        //
        return a.getValue();
    }
    
    /**
     * Create EFeature unique ID from given {@link EStructuralFeature feature}
     * @param eEFeature {@link EStructuralFeature} instance
     * @return a unique ID
     */
    private static final String eFeatureUID(EStructuralFeature eEFeature) {
        EClass eClass = eEFeature.getEContainingClass();
        return EFeatureUtils.eGetNsURI(eClass) 
            + "/" + eClass.getClassifierID() 
            + "/" + eEFeature.getFeatureID();
    }
    
    // ----------------------------------------------------- 
    //  Public construction methods
    // -----------------------------------------------------
    
    /**
     * Create EFeature {@link EFeatureInfo structure} from 
     * given EMF {@link EObject object} when the context is known.
     * <p>
     * </p>
     * @param eContext - {@link EFeatureContext context} of given object
     * @param eFeature - {@link EFeature} or EFeature compatible EMF {@link EObject object}. 
     * @param eHints - {@link EFeatureInfo} construction hints (ID attribute name etc.)
     * @throws IllegalArgumentException If anything goes wrong.
     * @see {@link EFeatureHints}
     */
    public static EFeatureInfo create(EFeatureContext eContext, 
            EObject eFeature, EFeatureHints eHints) throws IllegalArgumentException {
        //
        // Get structure information
        //
        EClass eClass = (eFeature instanceof EClass ? (EClass)eFeature : eFeature.eClass());
        EPackage ePackage = eClass.getEPackage();
        String eNsURI = ePackage.getNsURI();
        //
        // Check cache first
        //
        EFeatureInfo eInfo = eContext.eStructure().eGetFeatureInfo(eClass);
        //
        // Does not exist?
        //
        if(eInfo == null) {
            //
            // Create new instance
            //
            eInfo = new EFeatureInfo();
            //
            // Set context
            //
            eInfo.eContextID = eContext.eContextID();
            eInfo.eContext = new WeakReference<EFeatureContext>(eContext);
            eInfo.eFactory = new WeakReference<EFeatureContextFactory>(eContext.eContextFactory());
            //
            // Set construction hints
            //
            eInfo.eHints = (eHints != null ? eHints : new EFeatureHints());
            //
            // Get SRID from hints
            //
            eInfo.srid = eGetSRID(eInfo.eHints);
            //
            // Defined the structure using default values
            //
            eInfo = define(eInfo, eContext, eNsURI, ePackage.getName(), eClass);
        }
        //
        // Finished
        //
        return eInfo;
    }

    // ----------------------------------------------------- 
    //  Static Helper methods
    // -----------------------------------------------------
    
    /**
     * Create EFeature {@link EFeatureInfo structure} from 
     * given EMF {@link EClass class} in given {@link EFeatureFolderInfo folder}.
     * <p>
     * <b>NOTE</b>: This method attaches the {@link EFeatureInfo} instance 
     * with given {@link EFeatureContext context} {@link EFeatureContextInfo structure}, 
     * and adds the {@link EClass#getEIDAttribute()} to the 
     * {@link EFeatureContext#eIDFactory()}.
     * </p>
     * @param eFolderInfo - the {@link EFeatureFolderInfo folder} instance 
     * @param eClass - {@link EFeature} or EFeature compatible EMF {@link EClass class} . 
     * @return a new {@link EFeatureInfo} instance if not defined, 
     * or the existing structure if defined
     */
    protected static EFeatureInfo create(
            EFeatureFolderInfo eFolderInfo, EClass eClass) {
        //
        // Get structure information
        //
        String eNsURI = eFolderInfo.eNsURI;
        String eFolderName = eFolderInfo.eName;
        //
        // Get context information
        //
        EFeatureContext eContext = eFolderInfo.eContext(false);
        //
        // Check cache first
        //
        EFeatureInfo eInfo = eContext.eStructure().eGetFeatureInfo(eFolderName, eClass);
        //
        // Was not cached?
        //
        if (eInfo == null) {
            //
            // Create new instance
            //
            eInfo = new EFeatureInfo();
            //
            // Set context
            //
            eInfo.eFactory = eFolderInfo.eFactory;
            eInfo.eContext = eFolderInfo.eContext;
            eInfo.eContextID = eContext.eContextID();
            //
            // Set construction hints
            //
            eInfo.eHints = (eFolderInfo.eHints != null ? eFolderInfo.eHints : new EFeatureHints());
            //
            // Get SRID from hints
            //
            eInfo.srid = eGetSRID(eInfo.eHints);
            //
            // Forward
            //
            eInfo = define(eInfo, eContext, eNsURI, eFolderName, eClass);
        }
        //
        // Finished
        //
        return eInfo;
    }

    // ----------------------------------------------------- 
    //  Static Helper methods
    // -----------------------------------------------------
        
    /**
     * Define EFeature {@link EFeatureInfo structure} from given information.
     * <p>
     * <b>NOTE</b>: This method assumes that the {@link EFeatureContext} and 
     * {@link EFeatureHints} are already set. This method also attaches the 
     * {@link EFeatureInfo} instance with given context 
     * {@link EFeatureContextInfo structure}, and adds the 
     * {@link EClass#getEIDAttribute()} to the {@link EFeatureContext#eIDFactory()}.
     * </p>
     * @param eInfo
     * @param eContext
     * @param eNsURI
     * @param eFolderName - the {@link EFeatureFolderInfo folder} instance 
     * @param eClass - {@link EFeature} or EFeature compatible EMF {@link EClass class} . 
     * @return a {@link EFeatureInfo} instance
     * @throws IllegalArgumentException If anything goes wrong.
     */
    private static EFeatureInfo define(
            EFeatureInfo eInfo, EFeatureContext eContext, 
            String eNsURI, String eFolderName, EClass eClass) 
        throws IllegalArgumentException {
        //
        // Set context path
        //        
        eInfo.eNsURI = eNsURI;
        eInfo.eFolderName = eFolderName;
        //
        // Set EClass implementing EFeature 
        //                
        eInfo.eClassName = eClass.getName();
        eInfo.eClass = new WeakReference<EClass>(eClass);        
        //
        // Set ID attribute 
        //                
        EAttribute eIDAttribute = eGetIDAttribute(eClass,eInfo.eHints);
        eInfo.eIDAttributeName = eIDAttribute.getName();
        eInfo.eMappingMap.put(EFeaturePackage.eINSTANCE.getEFeature_ID(),eIDAttribute);
        //
        // Set SRID attribute 
        //                
        EAttribute eSRIDAttribute = eGetSRIDAttribute(eClass, eInfo.eHints);
        eInfo.eSRIDAttributeName = eSRIDAttribute.getName();
        eInfo.eMappingMap.put(EFeaturePackage.eINSTANCE.getEFeature_SRID(),eSRIDAttribute);
        //
        // Set default geometry attribute 
        //                
        EAttribute eDefaultAttribute = eGetDefaultAttribute(eClass, eInfo.eHints);
        eInfo.eDefaultAttributeName = eDefaultAttribute.getName();
        eInfo.eMappingMap.put(EFeaturePackage.eINSTANCE.getEFeature_Default(),eDefaultAttribute);
        //
        // Create EFeature attribute structures 
        //                        
        EList<EAttribute> eAttributes = eClass.getEAllAttributes();
        eInfo.eAttributeInfoMap = attributes(eInfo, eAttributes);
        eInfo.eGeometryInfoMap = geometries(eInfo, eAttributes);        
        //
        // Register ID attribute with ID factory?
        //
        if( !(eContext.eIDFactory() instanceof EFeatureVoidIDFactory) ) {
            eContext.eIDFactory().add(eClass,eIDAttribute);
        }
        //
        // Add to cache?
        //
        if(!eContext.eStructure().contains(eInfo)) {
            EFeatureStatus eStatus;
            if((eStatus = eContext.eStructure().doAttach(eInfo)).isFailure()) {
                throw new IllegalArgumentException("Failed to cache " 
                        + eInfo.eName() + ": " + eStatus.getMessage());
            }
        }
        //
        // Optimize structure lookup methods
        //
        eOptimize(eInfo);
        //
        // Make immutable (this method also optimizes lookup)
        // 
        eImmutable(eInfo);
        //
        // Finished
        //
        return eInfo;
    }    
    
    protected static EAttribute eGetIDAttribute(EClass eClass, EFeatureHints eHints) {
        EAttribute eIDAttribute = eGetAttribute(eClass, eHints, EFeatureHints.EFEATURE_ID_ATTRIBUTES);
        if(eIDAttribute == null) {
            eIDAttribute = eClass.getEIDAttribute();            
        }
        return eIDAttribute;
    }
    
    protected static EAttribute eGetSRIDAttribute(EClass eClass, EFeatureHints eHints) {
        EAttribute eIDAttribute = eGetAttribute(eClass, eHints, EFeatureHints.EFEATURE_SRID_ATTRIBUTES);
        if(eIDAttribute == null) {
            eIDAttribute = EFeaturePackage.eINSTANCE.getEFeature_SRID();            
        }
        return eIDAttribute;
    }
    
    protected static EAttribute eGetDefaultAttribute(EClass eClass, EFeatureHints eHints) {
        EAttribute eIDAttribute = eGetAttribute(eClass, eHints, EFeatureHints.EFEATURE_DEFAULT_ATTRIBUTES);
        if(eIDAttribute == null) {
            eIDAttribute = EFeaturePackage.eINSTANCE.getEFeature_Default();            
        }
        return eIDAttribute;
    }
    
    protected static EAttribute eGetAttribute(EClass eClass, EFeatureHints eHints, Key eHint) {
        EAttribute eAttribute = null;
        if(eHints!=null) {
            eAttribute = eHints.eGetAttribute(eClass, eHint);
        } 
        return eAttribute;
    }
    
    protected static String eGetSRID(EFeatureHints eHints) {
        Object eSRID = null;
        if(eHints!=null) {
            eSRID = eHints.get(EFeatureHints.EFEATURE_DEFAULT_SRID_HINT);
        }
        return eSRID !=null ? eSRID.toString() : DEFAULT_SRID;
    }

    @SuppressWarnings("unchecked")
    protected static Set<String> eGetDefaultGeometryNames(EFeatureHints eHints) {
        Object eNames = null;
        if(eHints!=null) {
            eNames = eHints.get(EFeatureHints.EFEATURE_DEFAULT_GEOMETRY_NAMES);
        }
        return eNames !=null ? (Set<String>)eNames : 
            new HashSet<String>(Arrays.asList(EFeatureConstants.DEFAULT_GEOMETRY_NAME));
    }    
    
    protected static Map<String, EFeatureAttributeInfo> attributes(EFeatureInfo eFeatureInfo,
            EList<EAttribute> eAttributes) {
        //
        // Prepare
        //
        Map<String, EFeatureAttributeInfo> eAttributeMap = EFeatureUtils.newMap(eAttributes.size());
        //
        // Get name of EAttribute ID
        //
        String eID = eFeatureInfo.eIDAttributeName;
        //
        // Inspect EMF attributes, adding all with non geometry values. 
        //
        for (EAttribute it : eAttributes) {
            //
            // Does NOT contain geometry data?
            //
            if (!Geometry.class.isAssignableFrom(it.getEAttributeType().getInstanceClass())) {
                String eName = it.getName();
                eAttributeMap.put(eName, EFeatureAttributeInfo.create(
                        eFeatureInfo, eName.equals(eID), it));
            }
        }
        return eAttributeMap;
    }

    protected static Map<String, EFeatureGeometryInfo> geometries(EFeatureInfo eFeatureInfo,
            EList<EAttribute> eAttributes) {
        //
        // Prepare
        //        
        String eDefault = null;
        String srid = eFeatureInfo.srid;
        CoordinateReferenceSystem crs = eFeatureInfo.crs;
        Map<String, EFeatureGeometryInfo> eGeometryMap = EFeatureUtils.newMap(eAttributes.size());
        //
        // Inspect EMF attributes, adding all with geometry values. 
        //                
        for (EAttribute it : eAttributes) {
            //
            // Contains geometry data?
            //
            if (Geometry.class.isAssignableFrom(it.getEAttributeType().getInstanceClass())) {
                //
                // Initialize default geometry name?
                //
                String eName = it.getName();
                if (eDefault == null) {
                    eDefault = eName;
                }
                //
                // Create structure instance
                //
                EFeatureGeometryInfo eGeometryInfo = 
                    EFeatureGeometryInfo.create(eFeatureInfo, 
                            eName.equals(eDefault), srid, crs, it);
                //
                // Add to cache
                //
                eGeometryMap.put(eName, eGeometryInfo);
            }
        }
        return eGeometryMap;
    }

    protected final EFeatureStatus featureIsValid() {
        return success("Feature is valid: " + getClass().getSimpleName() + " [" + eName() + "]");
    }

    protected final EFeatureStatus featureIsInvalid() {
        return failure(this, eName(), "Feature is invalid: " 
                + getClass().getSimpleName() 
                + " [" + eName() + "]");
    }
    
    /**
     * Optimizes structure lookup.
     * <p>
     * @param eStructure
     */
    protected static void eOptimize(EFeatureInfo eStructure) {
        //
        // Get collection sizes
        //
        int gsize = eStructure.eGeometryInfoMap.size();
        int asize = eStructure.eAttributeInfoMap.size();
        int tsize = asize + gsize;
        //
        // ------------------------------------------------
        //  1) Prepare optimized lookup 
        // ------------------------------------------------
        //
        eStructure.eGeometryMap = EFeatureUtils.newMap(gsize);
        eStructure.eAttributeMap = EFeatureUtils.newMap(asize);
        eStructure.eAllAttributeMap = EFeatureUtils.newMap(tsize);
        eStructure.eAllAttributeInfoMap = EFeatureUtils.newMap(tsize);
        //
        // ------------------------------------------------
        //  2) Build lookup maps and collections 
        // ------------------------------------------------
        //
        for(EFeatureGeometryInfo it : eStructure.eGeometryInfoMap.values()) {
            String eName = it.eName;
            EAttribute eAttribute = it.eAttribute();
            eStructure.eGeometryMap.put(eName,eAttribute);
            eStructure.eAllAttributeMap.put(eName,eAttribute);
            eStructure.eAllAttributeInfoMap.put(eName, it);
        }
        for(EFeatureAttributeInfo it : eStructure.eAttributeInfoMap.values()) {
            String eName = it.eName;
            EAttribute eAttribute = it.eAttribute();
            eStructure.eAttributeMap.put(eName,eAttribute);
            eStructure.eAllAttributeMap.put(eName,eAttribute);
            eStructure.eAllAttributeInfoMap.put(eName, it);
        }        
    }
    
    /**
     * Make given {@link EFeatureInfo structure} immutable.
     * <p>
     * {@link EFeature} is highly optimized, which depends on the 
     * structure being immutable. Note that EMF meta models might 
     * change over time (dynamic EMF). Each time a {@link EClass}
     * change (attribute, containment, cross reference etc),
     * the EMF meta model and structure might get out of sync.
     * Change is tracked by the structure. If any change is detected,
     * the structure is invalidated. If {@link #validate(EPackage, EClass)} 
     * fails, the whole structure must be recreated.
     * <p/>
     * TODO Add tracking of EMF meta model changes and implement
     * synchronization mechanisms that update affected structures.
     * Note that this implies that checksums must be recalculated,
     * and that any cached values derived from the structure in live 
     * {@link EFeature} instances must be invalidated.
     */
    protected static void eImmutable(EFeatureInfo eStructure) {                
        //
        // Make copied lists and maps unmodifiable (structure is immutable)
        //
        eStructure.eMappingMap = Collections.unmodifiableMap(eStructure.eMappingMap);    
        eStructure.eGeometryMap = Collections.unmodifiableMap(eStructure.eGeometryMap);
        eStructure.eAttributeMap = Collections.unmodifiableMap(eStructure.eAttributeMap);
        eStructure.eAllAttributeMap = Collections.unmodifiableMap(eStructure.eAllAttributeMap);
        eStructure.eGeometryInfoMap = Collections.unmodifiableMap(eStructure.eGeometryInfoMap);
        eStructure.eAttributeInfoMap = Collections.unmodifiableMap(eStructure.eAttributeInfoMap);
        eStructure.eAllAttributeInfoMap = Collections.unmodifiableMap(eStructure.eAllAttributeInfoMap);
        //
        // TODO Add invalidation adapter tracking changes in the EMF meta model 
        //
    }
    
    // ----------------------------------------------------- 
    //  SimpleFeatureDescriptor implementation
    // -----------------------------------------------------

    private class InnerSimpleFeatureTypeImpl implements SimpleFeatureType {
        
        private Map<String, Integer> index;

        private Map<Object, Object> userData;

        private List<PropertyDescriptor> descriptors;

        private Class<?> binding = Collection.class;

        public InnerSimpleFeatureTypeImpl() {
            index = buildIndex(this);
        }

        @Override
        public boolean isIdentified() {
            // Features are always identified
            //
            return true;
        }

        @Override
        public Name getName() {
            return new NameImpl(EFeatureInfo.this.eName());
        }

        @Override
        public boolean isAbstract() {
            return false;
        }

        @Override
        public GeometryDescriptor getGeometryDescriptor() {
            EFeatureGeometryInfo eInfo = eGeometryInfoMap.get(eGetDefaultGeometryName());
            return eInfo != null ? eInfo.getDescriptor() : null;
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return EFeatureInfo.this.getCoordinateReferenceSystem();
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<Collection<Property>> getBinding() {
            return (Class<Collection<Property>>) binding;
        }

        @Override
        public Collection<PropertyDescriptor> getDescriptors() {
            if(descriptors==null) {
                List<EFeatureAttributeInfo> eList = eGetAttributeInfoList(true);
                descriptors = new ArrayList<PropertyDescriptor>(eList.size());
                for (EFeatureAttributeInfo it : eList) {
                    descriptors.add(it.getDescriptor());
                }
                descriptors = Collections.unmodifiableList(descriptors);
            }
            return descriptors;
        }

        @Override
        public boolean isInline() {
            // Apparently not in use, return false
            //
            return false;
        }

        @Override
        public AttributeType getSuper() {
            return null;
        }

        @Override
        public List<Filter> getRestrictions() {
            return Collections.emptyList();
        }

        @Override
        public InternationalString getDescription() {
            return null;
        }

        @Override
        public Map<Object, Object> getUserData() {
            if (userData == null) {
                userData = new HashMap<Object, Object>();
            }
            return userData;
        }

        @Override
        public String getTypeName() {
            return getName().getLocalPart();
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public List<AttributeDescriptor> getAttributeDescriptors() {
            return (List) getDescriptors();
        }

        @Override
        public List<AttributeType> getTypes() {
            synchronized (this) {
                List<AttributeType> types = new ArrayList<AttributeType>();
                for (EFeatureAttributeInfo it : eGetAttributeInfoList(true)) {
                    types.add(it.getDescriptor().getType());
                }
                return types;
            }
        }

        @Override
        public AttributeType getType(Name name) {
            AttributeDescriptor attribute = getDescriptor(name);
            if (attribute != null) {
                return attribute.getType();
            }
            return null;
        }

        @Override
        public AttributeType getType(String name) {
            AttributeDescriptor attribute = getDescriptor(name);
            if (attribute != null) {
                return attribute.getType();
            }
            return null;
        }

        @Override
        public AttributeType getType(int index) {
            return getTypes().get(index);
        }

        @Override
        public AttributeDescriptor getDescriptor(Name eName) {
            return getDescriptor(eName.getURI());
        }

        @Override
        public AttributeDescriptor getDescriptor(String eName) {
            EFeatureAttributeInfo eInfo = eGetAttributeInfo(eName, true);
            return (eInfo != null ? eInfo.getDescriptor() : null);
        }

        @Override
        public AttributeDescriptor getDescriptor(int index) {
            return getAttributeDescriptors().get(index);
        }

        @Override
        public int indexOf(Name name) {
            if (name.getNamespaceURI() == null) {
                return indexOf(name.getLocalPart());
            }
            // otherwise do a full scan
            int index = 0;
            for (AttributeDescriptor descriptor : getAttributeDescriptors()) {
                if (descriptor.getName().equals(name)) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        @Override
        public int indexOf(String name) {
            Integer idx = index.get(name);
            if (idx != null) {
                return idx.intValue();
            } else {
                return -1;
            }
        }

        @Override
        public int getAttributeCount() {
            return getAttributeDescriptors().size();
        }
        
        /**
         * Builds the name -> position index used by simple features for fast attribute lookup
         * 
         * @param featureType
         * @return
         */
        private Map<String, Integer> buildIndex(InnerSimpleFeatureTypeImpl featureType) {
            // build an index of attribute name to index
            Map<String, Integer> index = new HashMap<String, Integer>();
            int i = 0;
            for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
                index.put(ad.getLocalName(), i++);
            }
            if (featureType.getGeometryDescriptor() != null) {
                index.put(null, index.get(featureType.getGeometryDescriptor().getLocalName()));
            }
            return Collections.unmodifiableMap(index);
        }
    }    

}
