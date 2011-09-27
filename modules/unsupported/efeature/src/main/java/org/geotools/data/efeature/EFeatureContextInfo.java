package org.geotools.data.efeature;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.geotools.data.efeature.internal.EFeatureDelegate;
import org.geotools.data.efeature.internal.EFeatureInfoCache;

/**
 * 
 * @author kengu
 *
 *
 * @source $URL$
 */
public class EFeatureContextInfo extends EStructureInfo<EFeatureContextInfo> {

    /** 
     * Map of {@link EFeaturePackageInfo} instance
     */
    protected Map<String, EFeaturePackageInfo> 
        ePackageMap = new HashMap<String, EFeaturePackageInfo>();
    
    /**
     * Cached {@link EFeatureInfoCache} for this context.
     */
    protected EFeatureInfoCache eFeatureInfoCache;
    
    /**
     * {@link EFeatureInfoCache} voter
     */
    protected EFeatureListener<EFeatureInfoCache> eFeatureInfoCacheVoter;
    
    // ----------------------------------------------------- 
    //  EFeatureContextInfo methods
    // -----------------------------------------------------

    @Override
    public boolean isAvailable() {
        if(super.isAvailable()){
            for (EFeaturePackageInfo it : synchronize(this).values()) {
                if (it.isAvailable())
                    return true;
            }
        }
        return false;
    }
    
    /**
     * Get {@link EFeaturePackageInfo package information} mapped to 
     * {@link EFeaturePackageInfo#eNsURI() namespace URIs}
     * <p>  
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public Map<String, EFeaturePackageInfo> ePackageMap() {
        verify();
        return synchronize(this);
    }

    /**
     * Get {@link EFeaturePackageInfo} with given 
     * {@link EPackage#getNsURI() namespace URI}.
     * @param eNsURI
     * @throws IllegalArgumentException If not found
     * @throws IllegalStateException If {@link #isDisposed() disposed}.
     */
    public EFeaturePackageInfo eGetPackageInfo(String eNsURI) {
        verify();
        EFeaturePackageInfo eInfo = synchronize(this).get(eNsURI);
        if(eInfo==null) {
            throw new IllegalArgumentException(
                    "EFeaturePackageInfo [" + eNsURI + "] not found");
        }
        return eInfo;
    }
    
    /**
     * Check if a {@link EFeatureDataStore} with with given 
     * {@link EPackage#getNsURI() namespace URI}.
     * </p> 
     * @param eNsURI - EMF model {@link EPackage package} name space URI
     * @throws IllegalStateException If {@link #isDisposed() disposed}.
     */
    public boolean contains(String eNsURI) {
        verify();
        return synchronize(this).get(eNsURI)!=null;
    }
    
    /**
     * Check if a {@link EFeatureInfo} instance is cached for 
     * the {@link EClass} defining given {@link EObject}.
     * <p>
     * This is a convenience method calling {@link #contains(EClass)}
     * </p> 
     * @param eObject - the {@link EObject} instance
     * @return <code>true</code> if given instance is cached
     * @see {@link #contains(EClass)}
     */    
    public boolean contains(EObject eObject) {
        return eFeatureInfoCache().contains(eObject);
    }
    
    /**
     * Get the {@link EFeatureInfo} instance cached for the 
     * {@link EClass} defining given {@link EObject}.
     * </p> 
     * @param eObject - the {@link EObject} instance
     * @return a {@link EFeatureInfo} if found,<code>null</code> otherwise.
     */         
    public EFeatureInfo eGetFeatureInfo(EObject eObject) {
        return eFeatureInfoCache().get(eObject);
    }

    /**
     * Get the {@link EFeatureInfo} instance cached for given {@link EClass}.
     * </p> 
     * @param eClass - the {@link EClass} instance
     * @return a {@link EFeatureInfo} if found, <code>null</code> otherwise.
     */         
    public EFeatureInfo eGetFeatureInfo(EClass eClass) {
        return eFeatureInfoCache().get(eClass);
    }
    
    /**
     * Get the {@link EFeatureInfo} instance cached for  
     * {@link EFeatureFolderInfo folder} and {@link EClass class}.
     * </p> 
     * @param eFolder - the {@link EFeatureFolderInfo} name
     * @param eClass - the {@link EClass} instance
     * @return a {@link EFeatureInfo} if found, <code>null</code> otherwise.
     */         
    public EFeatureInfo eGetFeatureInfo(String eFolder, EClass eClass) {
        return eFeatureInfoCache().get(eFolder, eClass);
    }    
    
    /**
     * Check if a given {@link EFeatureInfo} exists in structure.
     * <p>
     * <b>NOTE</b>: This only checks for <i>key equivalence</i>. If two keys are 
     * equal within the same {@link EFeatureContext}, the two structures must 
     * by definition be equal.
     * </p> 
     * @param eClass - the {@link EClass} instance
     * @return <code>true</code> if given instance is cached
     */
    public boolean contains(EFeatureInfo eInfo) {
        return eFeatureInfoCache().contains(
                eInfo.eNsURI,eInfo.eFolderName,eInfo.eName());
    }  
    
    /**
     * Check if a {@link EFeatureInfo#isPrototype() prototype} 
     * instance is cached for given {@link EClass}.
     * <p>
     * Since a {@link EClass}es only contains information 
     * about the {@link EPackage#getNsURI() eNsURI}
     * and EFeature {@link EClass#getName() root name}, this method only checks 
     * for EFeature prototypes (does not belong to any folder).
     * <p>
     * <b>NOTE</b>: This only checks for <i>key equivalence</i>. If two keys are 
     * equal within the same {@link EFeatureContext}, the two structures must 
     * by definition be equal.
     * </p> 
     * @param eClass - the {@link EClass} instance
     * @return <code>true</code> if given instance is cached
     */
    public boolean contains(EClass eClass) {
        return eFeatureInfoCache().contains(eClass);
    }    

    /**
     * Adapt given {@link EObject} into an EFeature.
     * @param eObject - {@link EObject} to be adapted into this context
     * @return new {@link EFeature} instance
     * @throws IllegalArgumentException If adaption failed.
     */
    public EFeature eAdapt(EObject eObject) throws IllegalArgumentException {
        //
        // Get context
        //
        EFeatureContext eContext = eContext();
        //
        // Get package
        //
        EPackage ePackage = eObject.eClass().getEPackage();
        //
        // Verify that package is contained in context
        //
        if(!eContext.contains(ePackage)) {
            throw new IllegalArgumentException("EPackage [" + ePackage.getNsURI() + "] not found");
        }
        //
        // Check if object is in cache
        //
        EFeatureInfo  eStructure = eFeatureInfoCache().get(eObject);
        if(eStructure==null) {
            //
            // Create structure in this context
            //
            eStructure = EFeatureInfo.create(eContext(), eObject, new EFeatureHints());
            //
            // Validate structure
            //
            eStructure.validate(ePackage, null);
        }
        //
        // Adapt directly? 
        //
        if(eObject instanceof EFeature) {
            ((EFeature)eObject).setStructure(eStructure);
            return (EFeature)eObject;
        }
        //
        // Create an delegate
        //
        return EFeatureDelegate.create(eStructure, (InternalEObject)eObject, true);
    }
    
    /**
     * Adapt given {@link EFeatureInfo structure} to this {@link EFeatureContext#eContext() context}.
     * @param eInfo - {@link EFeatureInfo} to be adapted into this context
     * @param copy - if <code>true</code>, a copy of given {@link EFeatureInfo structure} is adapted.
     * If <code>false</code>, the structure is claimed from it's context (moved into to this context). 
     * @return new {@link EFeatureInfo} instance if 'copy' is <code>true</code>, same otherwise
     * @throws IllegalArgumentException If adaption failed.
     */
    public EFeatureInfo eAdapt(EFeatureInfo eInfo, boolean copy) throws IllegalArgumentException {
        //
        // Get contexts 
        //
        EFeatureContext eOldContext = eInfo.eContext(false);
        EFeatureContext eNewContext = eContext(false);
        //
        // Get change flag
        //
        boolean eChanged = (eOldContext!=eNewContext);
        //
        // Update references?
        //
        if(eChanged) {
            //
            // Assume adaptation is required
            //
            boolean bAdapt = true;
            //
            // Check if cached instance exists with same key, use it if it 
            // has same context as this (which is should have)
            //
            EFeatureInfo eEqual = eFeatureInfoCache().get(EFeatureInfoCache.createKey(eInfo));
            if(eEqual!=null) {
                // -----------------------------------------------------------------
                //  Use already attached instance it instead of adapting given?
                // -----------------------------------------------------------------
                //  This is an optimization, ensuring that adaptation only occurs
                //  when absolutely required. It leverages the fact that
                // -----------------------------------------------------------------
                if(eEqual.eEqualTo(eInfo)) {
                    return eEqual;
                }
            }
            //
            // Try to do the adaptation?
            //
            if(bAdapt) {
                return doAdapt(eInfo, copy);
            }
        }
        //
        // Finished
        //
        return eInfo;                
    }
    

    /**
     * Validate structure against the {@link #eContext()}.
     * @throws IllegalStateException If {@link #isDisposed() disposed}.
     */
    public EFeatureStatus validate()
    {
        //
        // 1) Verify that not disposed
        //
        if(isDisposed)
        {
            return failure(this, eContextID, "Is disposed");
        }
        //
        // Invalidate structure
        //
        invalidate(false);
        //
        // 2) Verify that a EFeatureIDFactory instance is given
        //
        if(eContext(false).eIDFactory()==null)
        {
            return failure(this, eContextID, "No EFeatureIDFactory instance found");
        }
        //
        // Synchronize with EFeatureContext counterpart. 
        //
        EChange eChange = new EChange();
        Map<String,EFeaturePackageInfo> ePackageMap = eChange.synchronize(this);        
        //
        // 3) Validate all packages 
        //
        for(EFeaturePackageInfo it : ePackageMap.values())
        {
            EFeatureStatus s;
            if((s = it.validate()).isFailure())
            {
               return s; 
            }
        }
        //
        // Commit changes to structure
        //
        eChange.commit(this);
        //
        // Finished 
        //
        return structureIsValid(eContextID());
    }
    
    // ----------------------------------------------------- 
    //  EStructureInfo implementation
    // -----------------------------------------------------

    @Override
    protected void doDispose() {
        eContext = null;
        ePackageMap.clear();
    }

    @Override
    protected void doInvalidate(boolean deep) { 
        if (deep) {
            for (EStructureInfo<?> it : ePackageMap.values()) {
                it.doInvalidate(true);
            }
        }
    }
    
    @Override
    protected EFeatureContextInfo eParentInfo(boolean checkIsValid) {
        return this;
    }
    
    /**
     * Adapt given {@link EFeatureInfo} to this {@link EFeatureContextInfo#eContext() context}
     * <p>
     * <b>NOTE</b>: This method attaches the {@link EFeatureInfo} instance 
     * with given {@link EFeatureContext context} {@link EFeatureContextInfo structure}, 
     * and adds the {@link EClass#getEIDAttribute()} to the 
     * {@link EFeatureContext#eIDFactory()}.
     * </p>
     * @param eInfo - {@link EFeatureInfo} instance to adapt into this context 
     * @param copy - if <code>true</code>, copy {@link EFeatureInfo} instance
     * into this context. If <code>false</code>, claim it (move into this context). 
     * @return new {@link EFeatureInfo} instance if copy, same otherwise
     * @throws IllegalArgumentException If adaption failed.
     */
    protected EFeatureInfo doAdapt(EFeatureInfo eInfo, boolean copy) {     
        //
        // Clone EFeature structure?
        //
        if(copy) {
            //
            // Do a deep copy.
            //
            eInfo = new EFeatureInfo(eInfo,this);
        } else {            
            //
            // Adapt structure to this context 
            //
            eInfo.eAdapt(this);
        }
        //
        // ----------------------------------------------
        //  Attach to context
        // ----------------------------------------------
        //  This is an important step because:
        //  1) It makes the context structure aware of
        //  2) It allows the structure validation method  
        //     to map it to package structure (folder).
        //  3) If it fails now, nothing is yet added to 
        //     the context
        // ----------------------------------------------
        EFeatureStatus eStatus;
        if((eStatus = doAttach(eInfo)).isFailure()) {
            //
            // Notify 
            //
            throw new IllegalArgumentException("EFeatureInfo " + 
                    eInfo.eName() + " could not be adapted: " + eStatus.getMessage());            
        }
        //
        // Get this context
        //
        EFeatureContext eContext = eContext(false);
        //
        // Get EClass implementing EFeature
        //
        EClass eClass = eInfo.eClass();
        //
        // Add ID attribute to ID factory?
        //
        if(!eContext.eIDFactory().creates(eInfo.eIDAttribute())) {
            eContext.eIDFactory().add(eClass, eInfo.eIDAttribute());
        }
        //
        // Add package if not contained by new context
        //
        EPackage ePackage = eInfo.eClass().getEPackage();
        if(!eContext.contains(ePackage)) {
            //
            // Add package to the structure
            //
            eContext.eAdd(ePackage);
            //
            // ----------------------------------------------
            //  Validate the structure from context down
            // ----------------------------------------------
            //  This is an important step because:
            //  1) It will add create "eURI/eFolder"
            //     structures, which given EFeatureInfo 
            //     instance is added to if possible.
            //  2) More to come...
            // ----------------------------------------------
            validate();
        }
        
        //
        // Finished
        //
        return eInfo;
    }
            
    /**
     * Get {@link EFeatureInfoCache} instance.
     * <p>
     * This enables reuse of {@link EFeatureInfo} 
     * instances, and ensures that only one 
     * instance exists for each uniquely different 
     * {@link EFeatureInfo structure}.
     * </p>
     */
    protected EFeatureInfoCache eFeatureInfoCache() {
        if(eFeatureInfoCache==null) {
            eFeatureInfoCache = new EFeatureInfoCache(eFeatureInfoCacheVoter());
        }
        return eFeatureInfoCache;
    }    
    
    /**
     * Add {@link EFeatureInfo} to cache.
     * @return an {@link EFeatureStatus} instance.
     */
    protected EFeatureStatus doAttach(EFeatureInfo eInfo) {
        return eFeatureInfoCache().attach(eInfo);
    }
    
    /**
     * Detach {@link EFeatureInfo} from context.
     * </p>
     * @return <code>true</code> if released was allowed.
     * @throws IllegalArgumentException If release unexpectedly fails.
     */
    protected boolean doDetach(EFeatureInfo eInfo) {
        //
        // Try to release from cache.
        //
        EFeatureStatus eStatus = eFeatureInfoCache().detach(eInfo);
        //
        // Allowed?
        //
        if(eStatus.isSuccess()) {
            //
            // Get parent structure
            //
            EStructureInfo<?> eStructure = eInfo.eParentInfo(false);
            //
            // Found (not dangling)?
            //
            if(eStructure instanceof EFeatureFolderInfo) {
                //
                // Cast to EFeatureFolderInfo
                //
                EFeatureFolderInfo eFolderInfo = (EFeatureFolderInfo)eStructure;
                //
                // Remove object from folder's feature map
                //
                EFeatureInfo eRemoved = eFolderInfo.eFeatureInfoMap.remove(eInfo.eName()); 
                if(eInfo!=eRemoved) {
                    //
                    // Successfully removed structure from context 
                    //
                    return true;
                }
                //
                // Remove object does not match given, add it again?
                //
                if(eRemoved!=null) {
                    eFolderInfo.eFeatureInfoMap.put(eInfo.eName(), eInfo);
                }
                //
                // Break out, try to re-attach to cache...
                //
            } else {
                //
                // Successfully removed dangling structure from context 
                //
                return true;                
            }
        }
        //
        // Try to add again
        //
        eStatus = eFeatureInfoCache().attach(eInfo);
        //
        // Validate state
        //
        if(eStatus.isFailure()) {
            throw new IllegalStateException("Release failed. EFeatureInfoCache is inconsistent.");
        }
        //
        // Not allowed
        //
        return false;
    }    
    
    protected EFeatureListener<EFeatureInfoCache> eFeatureInfoCacheVoter() {
        if(eFeatureInfoCacheVoter==null) {
            eFeatureInfoCacheVoter = new EFeatureListener<EFeatureInfoCache>() {
                
                @Override
                public boolean onChange(EFeatureInfoCache source, int property, Object oldValue,
                        Object newValue) {
                    // 
                    // Only allow structures in the same context as this 
                    // by EFeatureCacheInfo#cache(EFeatureInfo)
                    //
                    if(newValue instanceof EFeatureInfo) {
                        return eContext(false)==((EFeatureInfo)newValue).eContext(false);
                    }
                    return false;
                }
            };
        }
        return eFeatureInfoCacheVoter;
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    /**
     * Create a {@link EFeatureContext} instance from cached 
     * {@link EFeatureContext} instance.
     * <p>
     * @param eFactory - {@link EFeatureContextFactory} instance
     * @param eContextID - {@link EFeatureContext} instance id.
     * @param eHints - construction hints. 
     * @throws IllegalArgumentException If the instance for some reason could 
     * not be constructed.
     * @see {@link EFeatureHints} - Hints about attribute mappings etc.
     */
    protected static final EFeatureContextInfo create(
            EFeatureContextFactory eFactory, String eContextID, EFeatureHints eHints)
        throws IllegalArgumentException
    {        
        EFeatureContext eContext = eFactory.eContext(eContextID);
        EFeatureContextInfo eInfo = new EFeatureContextInfo();
        eInfo.eHints = eHints;
        eInfo.eContextID = eContextID;        
        eInfo.eFactory = new WeakReference<EFeatureContextFactory>(eFactory);
        eInfo.eContext = new WeakReference<EFeatureContext>(eContext);
        synchronize(eInfo);
        return eInfo;
    }
    
    /**
     * Synchronize given {@link EFeatureContextInfo structure}  
     * with it's {@link EFeatureContextFactory factory} counterpart.
     * <p> 
     * This is required since {@link EPackage packages} are 
     * allowed to be added to and removed from the context.
     * </p>
     * @param eContextInfo - {@link EFeatureContextInfo structure}
     * @return a {@link Map} from {@link EFeatureContextInfo#eContextID() eContexID} to 
     * {@link EFeatureContextInfo} instances.
     */    
    protected static final Map<String, EFeaturePackageInfo> synchronize(EFeatureContextInfo eContextInfo)
    {
        EChange eChange  = new EChange();
        eChange.synchronize(eContextInfo);
        return eChange.commit(eContextInfo);
    }
    
    // ----------------------------------------------------- 
    //  Inner classes
    // -----------------------------------------------------
    
    /**
     * @author kengu - 29. mai 2011
     */
    private static class EChange {
        
        /**
         * Cached {@link EFeaturePackageInfo} changes  
         */
        private Map<String, EFeaturePackageInfo> eMap;
        
        /**
         * Synchronize given {@link EFeatureContextInfo structure}  
         * with it's {@link EFeatureContextFactory factory} counterpart.
         * <p> 
         * This is required since {@link EPackage packages} are 
         * allowed to be added to and removed from the context.
         * </p>
         * @param eContextInfo - {@link EFeatureContextInfo structure}
         * @return a {@link Map} from {@link EFeatureContextInfo#eContextID() eContexID} to 
         * {@link EFeatureContextInfo} instances.
         */    
        public final Map<String, EFeaturePackageInfo> synchronize(EFeatureContextInfo eContextInfo)
        {
            //
            // Ensure thread safe access to package map 
            //
            synchronized(eContextInfo.ePackageMap) {
                //
                // Get information
                //
                eMap = eContextInfo.ePackageMap;
                //
                // Get factory instance and context id 
                //
                EFeatureContextFactory eFactory = eContextInfo.eFactory(false);
                //
                // Get context instance
                //
                EFeatureContext eContext = eContextInfo.eContext(false);
                //
                // Get current set of EPackage NsURIs 
                //
                List<String> eNsURIs = eContext.eNsURIs();
                
                // Initialize map of new store structures
                //
                Map<String, EFeaturePackageInfo> 
                    eNewMap = new HashMap<String, EFeaturePackageInfo>(eNsURIs.size());        
                
                // Add all packages 
                //
                for(String eNsURI : eNsURIs)
                {
                    // Get structure information
                    //
                    EFeaturePackageInfo ePackageInfo = eMap.get(eNsURI);
                    
                    // Not found or overwrite?
                    //
                    if(ePackageInfo==null)
                    {
                        // Create the package structure
                        //
                        ePackageInfo = EFeaturePackageInfo.create(
                                eFactory, eContextInfo, eNsURI);
                        
                        /// Put to map of new structures 
                        //
                        eNewMap.put(eNsURI, ePackageInfo);
                    }
                }
                
                // Remove disposed packages
                //
                eMap.keySet().retainAll(eNsURIs);
                
                // Put new to current map
                //
                eMap.putAll(eNewMap);
                
                // Finished
                // 
                return Collections.unmodifiableMap(eMap);
            }
        }

        /**
         * Commit cached changes to context structure
         *  
         * @param eContextInfo
         */
        public Map<String, EFeaturePackageInfo> commit(EFeatureContextInfo eContextInfo) {
            synchronized(eContextInfo.ePackageMap) {
                eContextInfo.ePackageMap = eMap;
                return eMap;
            }
        }
    }

}
