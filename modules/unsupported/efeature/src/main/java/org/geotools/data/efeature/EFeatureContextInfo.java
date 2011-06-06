package org.geotools.data.efeature;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.efeature.internal.EFeatureInfoCache;

/**
 * 
 * @author kengu
 *
 */
public class EFeatureContextInfo extends EStructureInfo<EFeatureContextInfo> {

    /** 
     * Map of {@link EFeatureDomainInfo} instance
     */
    protected Map<String, EFeatureDomainInfo> 
        eDomainMap = new HashMap<String, EFeatureDomainInfo>();
    
    /**
     * Cached {@link EFeatureInfoCache} for this context.
     */
    protected EFeatureInfoCache eFeatureInfoCache;
    
    /**
     * {@link EFeatureInfoCache} voter
     */
    protected EFeatureListener<EFeatureInfoCache> eFeatureInfoCacheVoter;
    
    /**
     * Internal variable to respond to cache vote
     */
    //private EFeatureInfo eFeatureInfoCacheInProgress;
    
    
    // ----------------------------------------------------- 
    //  EFeatureContextInfo methods
    // -----------------------------------------------------

    @Override
    public boolean isAvailable() {
        if(super.isAvailable()){
            for (EFeatureDomainInfo it : synchronize(this).values()) {
                if (it.isAvailable())
                    return true;
            }
        }
        return false;
    }
    
    /**
     * Get {@link EFeatureDomainInfo} from {@link #eContext()} instance counterpart.
     * <p> 
     * @param eDomainID - {@link EFeatureContext#eGetDomain(String) editing domain} id
     * @return a {@link EFeatureDomainInfo}
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public EFeatureDomainInfo eGetDomainInfo(String eDomainID)
    {
        verify();
        return synchronize(this).get(eDomainID);
    }
    
    /**
     * Get {@link EFeatureDomainInfo domain information} mapped to 
     * {@link EFeatureDomainInfo#eDomainID() domain IDs}
     * <p>  
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public Map<String, EFeatureDomainInfo> eDomainInfoMap() {
        verify();
        return synchronize(this);
    }

    /**
     * Check {@link EFeatureDomainInfo domain information} with given 
     * {@link EFeatureDomainInfo#eDomainID() domain ID} exists.
     * <p>  
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */    
    public boolean isDomain(String eDomainID) {
        verify();
        return synchronize(this).containsKey(eDomainID);
    }

    /**
     * 
     * @param eDomainID
     * @param eNsURI
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public EFeatureDataStoreInfo eGetDataStoreInfo(String eDomainID, String eNsURI) {
        verify();
        EFeatureDomainInfo info = synchronize(this).get(eDomainID);
        if (info != null) {
            return info.eGetDataStoreInfo(eNsURI);
        }
        return null;
    }
    
    /**
     * 
     * @param eDomainID
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public Map<String, EFeatureDataStoreInfo> eGetDataStoreMap(String eDomainID) {
        verify();
        return synchronize(this).get(eDomainID).eDataStoreInfoMap();
    }

    /**
     * Check if a {@link EFeatureDataStore} is given name space URI
     * and given editing domain id exists.
     * </p> 
     * @param eNsURI - EMF model {@link EPackage package} name space URI
     * @param eDomainID - EMF model {@link EditingDomain editing domain} id  
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public boolean isDataStore(String eNsURI, String eDomainID) {
        verify();
        EFeatureDomainInfo info = synchronize(this).get(eDomainID);
        if (info != null) {
            return info.isDataStore(eNsURI);
        }
        return false;
    }

    /**
     * Adapt given {@link EFeatureInfo structure} to this {@link EFeatureContext#eContext() context}.
     * @param eInfo - {@link EFeatureInfo} to be adapted into this context
     * @param copy - if <code>true</code>, a copy of given {@link EFeatureInfo structure} is adapted.
     * If <code>false</code>, the structure is claimed from it's context (moved into to this context). 
     * @return new {@link EFeatureInfo} instance if 'copy' is <code>true</code>, same otherwise
     * @throws IllegalArgumentException If adaption failed.
     */
    public EFeatureInfo eAdapt(EFeatureInfo eInfo, boolean copy) {
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
            EFeatureInfo eEqual = eFeatureInfoCache.get(EFeatureInfoCache.createKey(eInfo));
            if(eEqual!=null) {
                //
                // Sanity check: Verify that, if found, it belongs to this context.
                //
                EFeatureContext eContext = eEqual.eContext(false);
                if(eContext!=eNewContext) {
                    //
                    // This is an indication that something is wrong. Should really 
                    // not happen. EFeatureInfo instances should always belong to the
                    // same context as the cache is it attached to. 
                    //
                    LOGGER.log(Level.WARNING,"Found EFeatureInfo attached to context " +
                            eContext.eContextID() + " in EFeatureInfoCache in context " + 
                            eContextID + ". Is will be adapted to the latter context.");
                }
                // -----------------------------------------------------------------
                //  Use already attached instance it instead of adapting given?
                // -----------------------------------------------------------------
                //  This is an optimization, ensuring that adaptation only occurs
                //  when absolutely required. It leverages the fact that
                // -----------------------------------------------------------------
                if(eEqual.eUID == eInfo.eUID) {
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
        Map<String,EFeatureDomainInfo> eDomainMap = eChange.synchronize(this);        
        //
        // 3) Validate all domains 
        //
        for(EFeatureDomainInfo it : eDomainMap.values())
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
        eDomainMap.clear();
    }

    @Override
    protected void doInvalidate(boolean deep) { 
        if (deep) {
            for (EStructureInfo<?> it : eDomainMap.values()) {
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
        if(!doAttach(eInfo)) {
            //
            // Notify 
            //
            throw new IllegalArgumentException("EFeatureInfo " + 
                    eInfo.eName() + " could not be adapted (not allowed to cache)");            
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
            //  1) It will add create "eURI/eDomain/eFolder"
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
     * @return <code>true</code> if cached.
     */
    protected boolean doAttach(EFeatureInfo eInfo) {
        //eFeatureInfoCacheInProgress = eInfo;
        EFeatureStatus eStatus = eFeatureInfoCache.attach(eInfo);
        //eFeatureInfoCacheInProgress = null;
        return eStatus.isSuccess();
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
        //eFeatureInfoCacheInProgress = eInfo;
        EFeatureStatus eStatus = eFeatureInfoCache.detach(eInfo);
        //eFeatureInfoCacheInProgress = null;
        //
        // Allowed?
        //
        if(eStatus.isSuccess()) {
            //
            // Get parent structure
            //
            EFeatureFolderInfo eFolderInfo = eInfo.eParentInfo(false);
            //
            // Found (not dangling)?
            //
            if(eFolderInfo!=null) {
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
        //eFeatureInfoCacheInProgress = eInfo;
        eStatus = eFeatureInfoCache.attach(eInfo);
        //eFeatureInfoCacheInProgress = null;
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
     * This is required since {@link EditingDomain editing domains} are 
     * allowed to be added to and removed from the context.
     * </p>
     * @param eContextInfo - {@link EFeatureContextInfo structure}
     * @return a {@link Map} from {@link EFeatureContextInfo#eContextID() eContexID} to 
     * {@link EFeatureContextInfo} instances.
     */    
    protected static final Map<String, EFeatureDomainInfo> synchronize(EFeatureContextInfo eContextInfo)
    {
        EChange eChange  = new EChange();
        eChange.synchronize(eContextInfo);
        return eChange.commit(eContextInfo);
    }
    
    /**
     * @author kengu - 29. mai 2011
     */
    private static class EChange {
        
        /**
         * Cached {@link EFeatureDomainInfo} changes  
         */
        private Map<String, EFeatureDomainInfo> eMap;
        
        /**
         * Synchronize given {@link EFeatureContextInfo structure}  
         * with it's {@link EFeatureContextFactory factory} counterpart.
         * <p> 
         * This is required since {@link EditingDomain editing domains} are 
         * allowed to be added to and removed from the context.
         * </p>
         * @param eContextInfo - {@link EFeatureContextInfo structure}
         * @return a {@link Map} from {@link EFeatureContextInfo#eContextID() eContexID} to 
         * {@link EFeatureContextInfo} instances.
         */    
        public final Map<String, EFeatureDomainInfo> synchronize(EFeatureContextInfo eContextInfo)
        {
            //
            // Ensure thread safe access to domain map 
            //
            synchronized(eContextInfo.eDomainMap) {
                //
                // Get copy of current domain structure map
                //
                eMap = new HashMap<String, EFeatureDomainInfo>(eContextInfo.eDomainMap);
                //
                // Get context instance
                //
                EFeatureContext eContext = eContextInfo.eContext(false);
                //
                // Get current set of editing domains
                //
                List<String> eDomainIDs = eContext.eDomainIDs();
                //
                // Initialize
                //
                HashMap<String, EFeatureDomainInfo> 
                    eNewMap = new HashMap<String, EFeatureDomainInfo>(eDomainIDs.size());           
                
                // Add all domains 
                //
                for(String eDomainID : eDomainIDs)
                {
                    // Get structure information
                    //
                    EFeatureDomainInfo eDomainInfo = eMap.get(eDomainID);
                    
                    // Not found or overwrite?
                    //
                    if(eDomainInfo==null)
                    {
                        // Create the domain structure
                        //
                        eDomainInfo = EFeatureDomainInfo.create(eContextInfo, eDomainID);
                        
                    }
                    /// Put to map of new structures 
                    //
                    eNewMap.put(eDomainID, eDomainInfo);
                }
                
                // Remove disposed editing domains
                //
                eMap.keySet().retainAll(eDomainIDs);
                
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
        public final Map<String, EFeatureDomainInfo> commit(EFeatureContextInfo eContextInfo) {
            synchronized(eContextInfo.eDomainMap) {
                eContextInfo.eDomainMap = eMap;
                return eMap;
            }
        }
    }


}
