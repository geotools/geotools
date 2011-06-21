package org.geotools.data.efeature;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.geotools.data.efeature.internal.EFeatureInfoCache;


/**
 * {@link EFeature} folder information class implementation.
 * <p>
 * 
 * @author kengu
 * 
 */
public class EFeatureFolderInfo extends EStructureInfo<EFeaturePackageInfo> {

    /**
     * The {@link EFeature} folder name. An {@link EClass} containing {@link EFeature} or
     * {@link EFeature} compatible data if {@link #eIsObject} is <code>true</code>.
     */
    protected String eName;
    
    /** Cached {@link EPackage#getNsURI() name space URI}  */
    protected String eNsURI;

    /**
     * If <code>true</code>, {@link #eName} is an {@link EClass} containing {@link EFeature}s or
     * {@link EFeature} compatible data
     */
    protected boolean eIsObject;

    /**
     * {@link EFeature} id to {@link EFeatureInfo} instance {@link Map}
     */
    protected Map<String, EFeatureInfo> eFeatureInfoMap;
    
    // ----------------------------------------------------- 
    //  EFeatureFolderInfo methods
    // -----------------------------------------------------

    @Override
    public boolean isAvailable() {
        // Is valid and has at least one EFeature definition?
        //
        return super.isAvailable() && !eFeatureInfoMap.isEmpty();
    }

    public String eName() {
        return eName;
    }
    
    public String eNsURI() {
        return eNsURI;
    }

    @Override
    protected EFeaturePackageInfo eParentInfo(boolean checkIsValid) {
        return eContext(checkIsValid).eStructure().
            eGetPackageInfo(eNsURI);        
    }

    public EFeatureInfo eGetFeatureInfo(String eName) {
        return eFeatureInfoMap.get(eName);
    }

    public Map<String, EFeatureInfo> eFeatureInfoMap() {
        return eFeatureInfoMap;
    }

    public boolean isFeature(String eName) {
        return eFeatureInfoMap.containsKey(eName);
    }

    public String[] eFeatureNames() {
        return eFeatureInfoMap.keySet().toArray(new String[0]);
    }

    /**
     * Validate {@link EFeature} folder information against given {@link EPackage}.
     * <p>
     * 
     * @param ePackage - given {@link EPackage} instance
     * @return <code>true</code> if valid, <code>false</code> otherwise.
     */
    public EFeatureStatus validate(EPackage ePackage) {
        //
        // Invalidate structure
        //
        doInvalidate(false);
        //
        // 1) Verify folder name
        //
        if (eName == null || eName.length() == 0) {
            return failure(this, eName(), "Folder name is missing");
        }

        // 2) Verify that instances of given
        // eClass is an EObject of same class
        // as given folder name?
        //
        EClass eParent = null;
        if (eIsObject) {
            EClassifier eClassifier = ePackage.getEClassifier(eName);
            if (eClassifier instanceof EClass) {
                eParent = (EClass) eClassifier;
            } else
                return failure(this, eName(), "Folder mismatch: Parent " 
                        + eName + " not an EClass");
        }

        // 3) Verify features
        //
        EFeatureStatus s;
        for (EFeatureInfo it : eFeatureInfoMap.values()) {
            if (!(s = it.validate(ePackage, eParent)).isSuccess())
            {
                return s;
            }
        }

        // Confirm that structure is valid
        //
        return structureIsValid(eName());
    }

    // ----------------------------------------------------- 
    //  EStructureInfo implementation
    // -----------------------------------------------------
    
    @Override
    protected void doInvalidate(boolean deep) {
        if (deep) {
            for (EStructureInfo<?> it : eFeatureInfoMap.values()) {
                it.doInvalidate(true);
            }
        }
    }

    @Override
    protected void doDispose() {        
        for (EFeatureInfo it : eFeatureInfoMap.values()) {
            it.dispose();
        }
        eFeatureInfoMap.clear();
        eFeatureInfoMap = null;
    }
    
    // ----------------------------------------------------- 
    //  EStructureInfo implementation
    // -----------------------------------------------------
    
    /**
     * Construct {@link EFeatureFolderInfo} instances from {@link EPackage} instance.
     * <p>     
     * @param eStoreInfo - {@link EFeaturePackageInfo} instance
     * @param ePackage - {@link EPackage} instance containing EFeature and EFeature compatible classes.
     * @throws IllegalArgumentException If a {@link EPackage} instance was not found.
     */
    protected static EFeatureFolderInfo create( 
            EFeaturePackageInfo eStoreInfo, EPackage ePackage) 
        throws IllegalArgumentException {
        EFeatureFolderInfo eInfo = new EFeatureFolderInfo();
        eInfo.eName = ePackage.getName();
        eInfo.eNsURI = eStoreInfo.eNsURI;
        eInfo.eHints = eStoreInfo.eHints;
        eInfo.eContextID = eStoreInfo.eContextID;
        eInfo.eIsObject = false;
        eInfo.eFactory = eStoreInfo.eFactory;
        eInfo.eContext = eStoreInfo.eContext;
        eInfo.eFeatureInfoMap = features(eInfo,ePackage);
        return eInfo;
    }

    protected static Map<String, EFeatureInfo> features(EFeatureFolderInfo eFolderInfo, EPackage ePackage) {
        //
        // Prepare lists
        //
        EList<EClassifier> eList = ePackage.getEClassifiers();
        Map<String, EFeatureInfo> eFeatureMap = 
            new HashMap<String, EFeatureInfo>(eList.size());
        //
        // Get EFeatureInfo cache
        //
        EFeatureInfoCache eCache = eFolderInfo.eContext(false).eStructure().eFeatureInfoCache();
        //
        // Inspect EMF package, adding all EFeature and EFeature compatible classes. 
        //
        for(EClassifier it : eList)
        {
            //
            // Check if it implements EFeature or contains EFeature compatible data
            //
            if(EFeatureUtils.isCompatible(it))
            {
                EClass eClass = (EClass)it;
                EFeatureInfo eFeatureInfo = eCache.get(eClass);
                if(eFeatureInfo==null) {
                    eFeatureInfo = EFeatureInfo.create(eFolderInfo, (EClass)it);
                }
                eFeatureInfo = eCache.get(eClass);
                eFeatureMap.put(eFeatureInfo.eName(), eFeatureInfo);
            }
        }      
        //
        // Finished
        //
        return Collections.unmodifiableMap(eFeatureMap);
    }

}
