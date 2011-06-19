package org.geotools.data.efeature;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.util.logging.Logging;

/**
 * 
 * @author kengu
 *
 */
public class EFeatureDomainInfo extends EStructureInfo<EFeatureContextInfo> {

    /** The logger for the {@link EFeatureDomainInfo} class */
    protected static final Logger LOGGER = Logging.getLogger(EFeatureDomainInfo.class);

    /** The {@link EditingDomain} instance id */
    protected String eDomainID;
    
    /** Map of {@link EFeatureDataStoreInfo} instances */
    protected Map<String, EFeatureDataStoreInfo> 
        eDataStoreMap = new HashMap<String, EFeatureDataStoreInfo>();
    
    // ----------------------------------------------------- 
    //  EFeatureDomainInfo methods
    // -----------------------------------------------------
    
    @Override
    public boolean isAvailable() {
        if(super.isAvailable()){
            for (EFeatureDataStoreInfo it : synchronize(this).values()) {
                if (it.isAvailable())
                    return true;
            }
        }
        return false;
    }
        
    /**
     * 
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public String eDomainID()
    {
        verify();
        return eDomainID;
    }
    
    @Override
    protected EFeatureContextInfo eParentInfo(boolean checkIsValid) {
        return eFactory(checkIsValid).eStructure(eContextID);
    }        
    
    /**
     * 
     * @param eNsURI
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public EFeatureDataStoreInfo eGetDataStoreInfo(String eNsURI) {
        verify();
        return synchronize(this).get(eNsURI);
    }

    /**
     * 
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public Map<String, EFeatureDataStoreInfo> eDataStoreInfoMap() {
        verify();
        return synchronize(this);
    }
    
    /**
     * 
     * @param eNsURI
     * @throws IllegalStateException If {@link #isDisposed() disposed},
     * or in{@link #isValid() valid}.
     */
    public boolean isDataStore(String eNsURI) {
        verify();
        return synchronize(this).containsKey(eNsURI);
    }
    
    public EFeatureStatus validate() {
        //
        // 1) Verify that not disposed
        //
        if(isDisposed)
        {
            return failure(this, eDomainID, "Is disposed");
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
            return failure(this, eDomainID, 
                    "EFeatureContext with ID: " + eContextID + " not found");
        }
        //
        // Synchronize with EFeatureContext counterpart. 
        //
        EChange eChange = new EChange();
        Map<String,EFeatureDataStoreInfo> eDataStoreMap = eChange.synchronize(this);                
        //
        // 2) Validate all domains 
        //
        for(EFeatureDataStoreInfo it : eDataStoreMap.values())
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
        return structureIsValid(eDomainID());
    }    
    
    // ----------------------------------------------------- 
    //  EStructureInfo implementation
    // -----------------------------------------------------
    
    @Override
    protected void doInvalidate(boolean deep) {
        if (deep) {
            for (EStructureInfo<?> it : eDataStoreMap.values()) {
                it.doInvalidate(true);
            }
        }
    }
    
    @Override
    protected void doDispose(){
        eDataStoreMap.clear();
        eDataStoreMap = null;
    }

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    /**
     * Create a {@link EFeatureDomainInfo} from {@link EFeatureContext} 
     * cached with given id.
     * <p>
     * @param eContextID - {@link EFeatureContext#eContextID() ID} of cached {@link EFeatureContext}
     * @param eDomainID - {@link EFeatureDomainInfo#eDomainID() ID} of managed 
     * @param eHints - construction hints. 
     * {@link EFeatureContext#eGetDomain(String) editing domain}
     * @return a {@link EFeatureDomainInfo} instance
     * @throws IllegalArgumentException If {@link EFeatureContext} 
     * or {@link EditingDomain} instances was not found.
     * @see {@link EFeatureHints} - Hints about attribute mappings etc.
     */
    protected static EFeatureDomainInfo create(
            EFeatureContextInfo eContextInfo, String eDomainID) 
        throws IllegalArgumentException {
        EFeatureDomainInfo eInfo = new EFeatureDomainInfo();
        eInfo.eHints = eContextInfo.eHints;
        eInfo.eContextID = eContextInfo.eContextID;
        eInfo.eDomainID = eDomainID;
        eInfo.eFactory = eContextInfo.eFactory;
        eInfo.eContext = eContextInfo.eContext;
        synchronize(eInfo);
        return eInfo; 
    }    

    /**
     * Synchronize given {@link EFeatureDomainInfo structure}  
     * with it's {@link EFeatureContextInfo context} counterpart
     * <p> 
     * This is required since {@link EPackage packages} are 
     * allowed to be added to, and removed from the context. 
     * </p>
     * @param eDomainInfo - {@link EFeatureDomainInfo structure}
     * @return a {@link Map} from {@link EFeatureDomainInfo#eDomainID() eDomainID} to 
     * {@link EFeatureDomainInfo} instances.
     * @see {@link EFeatureDomainInfo#eGetDataStoreInfo(String)}
     */    
    protected static final Map<String, EFeatureDataStoreInfo> synchronize(EFeatureDomainInfo eDomainInfo)
    {    
        EChange eChange  = new EChange();
        eChange.synchronize(eDomainInfo);
        return eChange.commit(eDomainInfo);
    }
    
    /**
     * @author kengu - 29. mai 2011
     */
    private static class EChange {
        
        /**
         * Cached {@link EFeatureDomainInfo} changes  
         */
        private Map<String, EFeatureDataStoreInfo> eMap;
        
        /**
        * Synchronize given {@link EFeatureDomainInfo structure}  
        * with it's {@link EFeatureContextInfo context} counterpart
        * <p> 
        * This is required since {@link EPackage packages} are 
        * allowed to be added to, and removed from the context. 
        * </p>
        * @param eDomainInfo - {@link EFeatureDomainInfo structure}
        * @return a {@link Map} from {@link EFeatureDomainInfo#eDomainID() eDomainID} to 
        * {@link EFeatureDomainInfo} instances.
        * @see {@link EFeatureDomainInfo#eGetDataStoreInfo(String)}
        */    
       protected final Map<String, EFeatureDataStoreInfo> synchronize(EFeatureDomainInfo eDomainInfo)
       {
           //
           // Ensure thread safe access to domain map 
           //
           synchronized(eDomainInfo.eDataStoreMap) {
               //
               // Get information
               //
               eMap = eDomainInfo.eDataStoreMap;
               //
               // Get factory instance and context id 
               //
               EFeatureContextFactory eFactory = eDomainInfo.eFactory(false);
               //
               // Get context instance
               //
               EFeatureContext eContext = eDomainInfo.eContext(false);
               //
               // Get current set of EPackage NsURIs 
               //
               List<String> eNsURIs = eContext.eNsURIs();
               
               // Initialize map of new store structures
               //
               Map<String, EFeatureDataStoreInfo> 
                   eNewMap = new HashMap<String, EFeatureDataStoreInfo>(eNsURIs.size());        
               
               // Add all domains 
               //
               for(String eNsURI : eNsURIs)
               {
                   // Get structure information
                   //
                   EFeatureDataStoreInfo eStoreInfo = eMap.get(eNsURI);
                   
                   // Not found or overwrite?
                   //
                   if(eStoreInfo==null)
                   {
                       // Create the domain structure
                       //
                       eStoreInfo = EFeatureDataStoreInfo.create(
                               eFactory, eDomainInfo, eNsURI);
                       
                       /// Put to map of new structures 
                       //
                       eNewMap.put(eNsURI, eStoreInfo);
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
        * Commit cached changes to domain structure
        *  
        * @param eDomainInfo
        */
       public Map<String, EFeatureDataStoreInfo> commit(EFeatureDomainInfo eDomainInfo) {
           synchronized(eDomainInfo.eDataStoreMap) {
               eDomainInfo.eDataStoreMap = eMap;
               return eMap;
           }
       }
       
    }
            

}
