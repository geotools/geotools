package org.geotools.data.efeature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EPackage;
import org.geotools.data.FeatureStore;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * {@link EPackage} information class implementation.
 * <p>
 * 
 * @author kengu
 * 
 */
public class EFeaturePackageInfo extends EStructureInfo<EFeatureContextInfo> {

    /** Cached {@link EPackage} name space */
    protected String eNsURI;

    /** Map of {@link EFeatureFolderInfo} instances */
    protected Map<String, EFeatureFolderInfo> eFolderInfoMap;
    
    // ----------------------------------------------------- 
    //  EFeaturePackage methods
    // -----------------------------------------------------
    
    @Override
    public boolean isAvailable() {
        if(super.isAvailable()){
            for (EFeatureFolderInfo it : eFolderInfoMap.values()) {
                if (it.isAvailable())
                    return true;
            }
        }
        return false;
    }
    
    public String eNsURI() {
        return eNsURI;
    }
    
    public EPackage ePackage()
    {
        return eContext().eGetPackage(eNsURI);
    }
    @Override
    protected EFeatureContextInfo eParentInfo(boolean checkIsValid) {
        return eContext(checkIsValid).eStructure();
    }    
    
    public EFeatureFolderInfo eGetFolderInfo(String eName) {
        return eFolderInfoMap.get(eName);
    }

    public Map<String, EFeatureFolderInfo> eFolderInfoMap() {
        return eFolderInfoMap;
    }

    /**
     * Check if this data store contains a folder with given name
     * 
     * @param eName - {@link EFeature} folder name
     * @return <code>true</code> if exists, <code>false</code> otherwise
     */
    public boolean isFolder(String eName) {
        return eFolderInfoMap.containsKey(eName);
    }

    /**
     * Get {@link EFeature} folder name from {@link SimpleFeatureType} name
     * <p>
     * {@link SimpleFeatureType} names have the following
     * 
     * <pre>
     * eType=&lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eFeature = {@link EFeature} class | reference name
     * </pre>
     * 
     * @param eType - given {@link SimpleFeatureType} name
     * @return a {@link EFeature} folder name if {@link #isSimpleFeatureType(String)},
     *         <code>null</code> otherwise.
     */
    public String toFolderName(String eType) {
        return EFeatureUtils.toFolderName(eType);
    }

    /**
     * Get {@link EFeature} name from {@link SimpleFeatureType} name
     * <p>
     * {@link SimpleFeatureType} names have the following
     * 
     * <pre>
     * eType=&lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eFeature = {@link EFeature} class | reference name
     * </pre>
     * 
     * @param eType - given {@link SimpleFeatureType} name
     * @return a {@link EFeature} name if {@link #isSimpleFeatureType(String)}, <code>null</code>
     *         otherwise.
     */
    public String toFeatureName(String eType) {
        return EFeatureUtils.toFeatureName(eType);
    }

    /**
     * Get {@link EFeature} folder names in this {@link FeatureStore} definition.
     * <p>
     * 
     * @param eQuery - (optional) query used to select which {@link EFeature} folders to include
     * @return an array of {@link EFeature} folder names if found, <code>empty</code> array
     *         otherwise.
     */
    public String[] eGetFolderNames(String eQuery) {
        Set<String> subSet = new HashSet<String>(eFolderInfoMap.keySet());
        if( !( eQuery==null || eQuery.length()==0) )
        {
            Set<String> eNames = new HashSet<String>();
            for (String eFolder : eQuery.split("+")) {
                int i = eFolder.indexOf(':');
                eNames.add(i == -1 ? eFolder : eFolder.substring(0, i - 1));
            }
            subSet.retainAll(eNames);
        }
        return subSet.toArray(new String[0]);
    }

    /**
     * Check if given eType is a {@link SimpleFeatureType}.
     * <p>
     * {@link SimpleFeatureType} names have the following
     * 
     * <pre>
     * eType=&lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eFeature = {@link EFeature} class | reference name
     * </pre>
     * 
     * @param eType - given {@link SimpleFeatureType} name
     * @return a {@link EFeatureInfo} instance if found, <code>null</code> otherwise.
     * @see {@link EFeatureInfo#getReferenceName()}
     */
    public boolean isSimpleFeatureType(String eType) {
        if (!(eType == null || eType.length() == 0)) {
            String[] eParts = eType.split(".");
            if (eParts.length == 2) {
                EFeatureFolderInfo eFolderInfo = eFolderInfoMap.get(eParts[0]);
                if (eFolderInfo != null) {
                    return eFolderInfo.isFeature(eParts[1]);
                }
            }
        }
        return false;
    }

    /**
     * Check if given eType is a {@link SimpleFeatureType}.
     * <p>
     * {@link SimpleFeatureType} names have the following format:
     * 
     * <pre>
     * eType=&lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eFeature = {@link EFeature} class | reference name
     * </pre>
     * 
     * @param eType - given {@link SimpleFeatureType} name
     * @return a {@link EFeatureInfo} instance if found, <code>null</code> otherwise.
     * @see {@link EFeatureInfo#getReferenceName()}
     * @throws IllegalArgumentException
     */
    public EFeatureInfo eGetFeatureInfo(String eType) throws IllegalArgumentException {
        if (!(eType == null || eType.length() == 0)) {
            String[] eParts = eType.split("\\.");
            if (eParts.length == 2) {
                EFeatureFolderInfo eFolderInfo = eFolderInfoMap.get(eParts[0]);
                if (eFolderInfo != null) {
                    return eFolderInfo.eGetFeatureInfo(eParts[1]);
                }
            }            
            throw new IllegalArgumentException("Type name have illegal format. Expected \"<efolder>.<efeature>\"");
        }
        throw new IllegalArgumentException("Type name can not be null or empty");
    }

    /**
     * Get {@link SimpleFeatureType} names defined by this {@link FeatureStore} information.
     * <p>
     * {@link SimpleFeatureType} names have the following format:
     * 
     * <pre>
     * eType=&lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eFeature = {@link EFeature} class | reference name
     * </pre>
     * 
     * @param eQuery - query used to select which {@link EFeature} folders to include
     * @return an array of {@link SimpleFeatureType} names if found, <code>empty</code> array
     *         otherwise.
     */
    public String[] getTypeNames(String eQuery) {
        Set<String> eNames = new HashSet<String>();
        for (String eFolder : eGetFolderNames(eQuery)) {
            EFeatureFolderInfo eFolderInfo = eGetFolderInfo(eFolder);
            for (String eName : eFolderInfo.eFeatureNames()) {
                eNames.add(eFolder + "." + eName);
            }
        }
        return eNames.toArray(new String[0]);
    }

    /**
     * Validate given {@link EPackage} instance against 
     * the {@link EFeaturePackageInfo structure} .
     */
    public EFeatureStatus validate() {
        //
        // 1) Verify that not disposed
        //
        if(isDisposed)
        {
            return failure(this, eNsURI, "Is disposed");
        }
        //        
        // Invalidate structure
        //
        invalidate(false);        
        // 
        // 2) Verify that registry is cached
        //
        if(!isCached(eContextID))
        {
            return failure(this, eNsURI, "EFeatureContext with ID: " + eContextID + " not found");
        }
        EFeatureContext eContext = eContext(false);
        //
        // 3) Verify name space URI
        //
        EPackage ePackage = eContext.eGetPackage(eNsURI);
        if (!this.eNsURI.equals(ePackage.getNsURI())) {
            return failure(this, eNsURI, "Package mismatch: namespace (" + ePackage + ")");
        }
        //
        // 4) Verify ALL folders (query == null)
        //
        EFeatureStatus s;
        for (String eName : eGetFolderNames(null)) {
            EFeatureFolderInfo eInfo = eFolderInfoMap.get(eName);
            if (eInfo != null) {
                if (!(s = eInfo.validate(ePackage)).isSuccess()){
                    return s;
                }
            } else {
                return failure(this, eNsURI, "Package mismatch: Folder " + eName + " not found");
            }
        }
        //
        // Confirm that structure is valid
        //
        return structureIsValid(eNsURI());

    }
    
    // ----------------------------------------------------- 
    //  EStructureInfo implementation
    // -----------------------------------------------------
    
    @Override
    protected void doInvalidate(boolean deep) {
        if (deep) {
            for (EStructureInfo<?> it : eFolderInfoMap.values()) {
                it.doInvalidate(true);
            }
        }
    }

    @Override
    protected void doDispose() {
        for (EFeatureFolderInfo it : eFolderInfoMap.values()) {
            it.dispose();
        }
        eFolderInfoMap.clear();
        eFolderInfoMap = null;
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    /**
     * Create a {@link EFeaturePackageInfo} from {@link EFeatureContext} 
     * information cached with given ids.     
     * @param eContextInfo - {@link EFeatureContextInfo} instance
     * @param eNsURI - {@link EPackage#getNsURI() name space URI} 
     */
    protected static EFeaturePackageInfo create(EFeatureContextFactory eFactory, 
            EFeatureContextInfo eContextInfo, String eNsURI) 
        throws IllegalArgumentException {
        EFeaturePackageInfo eInfo = new EFeaturePackageInfo();
        eInfo.eHints = eContextInfo.eHints;
        eInfo.eContextID = eContextInfo.eContextID;
        eInfo.eNsURI = eNsURI;
        eInfo.eFactory = eContextInfo.eFactory;
        eInfo.eContext = eContextInfo.eContext;
        eInfo.eFolderInfoMap = folders(eInfo);
        return eInfo;
    }
    
    /**
     * Construct {@link EFeatureFolderInfo} instances from {@link EFeaturePackageInfo} instance.
     * <p>     
     * @param ePackageInfo - {@link EFeaturePackageInfo} instance
     * @throws IllegalArgumentException If a {@link EPackage} instance was not found.
     */
    protected static Map<String, EFeatureFolderInfo> folders(EFeaturePackageInfo ePackageInfo)
        throws IllegalArgumentException {
        //
        // Prepare
        //
        Map<String, EFeatureFolderInfo> eMap = new HashMap<String, EFeatureFolderInfo>();
        //
        // Get context instance
        //
        EFeatureContext eContext = ePackageInfo.eContext(false);
        //
        // Get EMF package from name space URI
        //
        EPackage ePackage = eContext.eGetPackage(ePackageInfo.eNsURI);
        //
        // Create single folder that contains all EFeature classes.
        //
        EFeatureFolderInfo eFolderInfo = EFeatureFolderInfo.create(ePackageInfo, ePackage);
        //
        // Map name to instance
        //
        eMap.put(eFolderInfo.eName,eFolderInfo);
        //
        // Success
        //
        return eMap;
    }

}
