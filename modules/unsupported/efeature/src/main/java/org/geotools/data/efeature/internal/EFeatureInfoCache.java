package org.geotools.data.efeature.internal;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureListener;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.EStructureInfo;
import org.geotools.util.WeakHashSet;

/**
 * {@link EFeatureInfo} cache class.
 * <p>
 * This class caches {@link EFeatureInfo} instances using {@link WeakHashSet}, 
 * allowing the instances to be garbage collected when instances no longer 
 * are hard referenced by internal or external clients.
 * </p>
 * @author kengu, 24. apr. 2011
 * 
 */
public final class EFeatureInfoCache {
    
    public static final int CACHE = 0; 
    
    /**
     * Set of weakly referenced {@link EFeatureInfo} instances
     */
    private final HashMap<String, WeakReference<EFeatureInfo>> 
        eCache = new HashMap<String, WeakReference<EFeatureInfo>>();
    
    /**
     * Set of weakly referenced {@link EStructureInfo} listeners
     */
    @SuppressWarnings("rawtypes")
    protected WeakHashSet<EFeatureListener> 
        eListeners = new WeakHashSet<EFeatureListener>(
            EFeatureListener.class);
    
    /**
     * Cache DENIED status
     */
    private final EFeatureStatus DENIED = 
           EFeatureUtils.newStatus(this, EFeatureStatus.FAILURE, "FeatureInfo was not allowed to be cached", null);

    /**
     * Cache ALLOWED status
     */
    private final EFeatureStatus ALLOWED = 
           EFeatureUtils.newStatus(this, EFeatureStatus.SUCCESS, "FeatureInfo was cached", null);
    

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * EFeatureInfo constructor.
     */
    public EFeatureInfoCache(EFeatureListener<EFeatureInfoCache> eVoter) {
        eListeners.add(eVoter);
    }
    
    // ----------------------------------------------------- 
    //  EFeatureInfoCache methods
    // -----------------------------------------------------
    
    /**
     * Try to add given {@link EFeatureInfo} to cache.
     * <p>
     * Attachment is allowed iff the {@link EFeatureInfo}:
     * <ol>
     *  <li>is not already {@link #contains(EFeatureInfo) contained}</li>
     *  <li>belongs to the same 
     *  {@link EFeatureContext context} as this cache</li>
     * </ol> 
     * </p>
     * @return a {@link EFeatureStatus} instance
     */
    public final EFeatureStatus attach(EFeatureInfo eInfo) {
        if(contains(eInfo)) return DENIED;
        EFeatureStatus eStatus = vote(eInfo);
        if(eStatus.isSuccess()) eCache.put(createKey(eInfo),new WeakReference<EFeatureInfo>(eInfo));
        return eStatus;
    }
    
    /**
     * Try to remove given {@link EFeatureInfo} from cache.
     * <p>
     * Detachment is allowed iff the {@link EFeatureInfo}:
     * <ol>
     *  <li>is {@link #contains(EFeatureInfo) contained}</li>
     *  <li>belongs to the same {@link EFeatureContext context} as this cache</li>
     * </ol> 
     * </p>
     * @return a {@link EFeatureStatus} instance
     */
    public final EFeatureStatus detach(EFeatureInfo eInfo) {
        if(!contains(eInfo)) return DENIED;
        EFeatureStatus eStatus = vote(eInfo);
        if(eStatus.isSuccess()) eCache.remove(createKey(eInfo));
        return eStatus;
    }    
    
    /**
     * Check if given {@link EFeatureInfo} instance is cached.
     * <p>
     * <b>NOTE</b>: This only checks for <i>key equivalence</i>. If two keys are 
     * equal within the same {@link EFeatureContext}, the two structures must 
     * by definition be equal. 
     * </p> 
     * @param eInfo - the {@link EFeatureInfo} instance
     * @return <code>true</code> if given instance is cached
     */
    public final boolean contains(EFeatureInfo eInfo) {
        return contains(createKey(eInfo));
    }

    /**
     * Check if a {@link EFeatureInfo} instance with given key is cached.
     * <p>
     * <b>NOTE</b>: If two keys are equal within the same 
     * {@link EFeatureContext}, the two structures must by definition be 
     * equal. 
     * </p> 
     * @param eInfo - the {@link EFeatureInfo} instance
     * @return <code>true</code> if instance with given key is cached
     */    
    public final boolean contains(String eKey) {
        return eCache.containsKey(eKey);
    }

//    /**
//     * Check if a {@link EFeatureInfo} instance with given key fragments is cached.
//     * <p>
//     * <b>NOTE</b>: If two keys are equal within the same 
//     * {@link EFeatureContext}, the two structures must by definition be 
//     * equal. 
//     * </p> 
//     * @param eInfo - the {@link EFeatureInfo} instance
//     * @return <code>true</code> if instance with given key fragments is cached
//     */        
//    public final boolean contains(String eNsURI, String eDomainID, String eType) {
//        return contains(eNsURI,eDomainID,
//                EFeatureUtils.toFolderName(eType),
//                EFeatureUtils.toFeatureName(eType));
//    }
//    
    
    /**
     * Check if a {@link EFeatureInfo} instance with given key fragments is cached.
     * <p>
     * <b>NOTE</b>: If two keys are equal within the same 
     * {@link EFeatureContext}, the two structures must by definition be 
     * equal. 
     * </p> 
     * @param eInfo - the {@link EFeatureInfo} instance
     * @return <code>true</code> if instance with given key fragments is cached
     */        
    public final boolean contains(String eNsURI, String eDomainID, String eFolder, String eFeature) {
        return eCache.containsKey(createKey(eNsURI, eDomainID, eFolder, eFeature));
    }
    
//    
//    public final boolean contains(String eDomainID, String eFolder, EObject eObject) {
//        return get(eDomainID,eFolder,eObject)!=null;
//    }    
//    
//    public final EFeatureInfo get(String eDomainID, String eFolder, EObject eObject) {
//        //
//        // Get EClass instance
//        //
//        EClass eClass = eObject.eClass();
//        //
//        // forward
//        //
//        return get(eDomainID, eFolder, eClass);
//    }

    public final EFeatureInfo get(String eDomainID, String eFolder, EClass eClass) {
        //
        // Get name space URI string
        //
        String eNsURI = EFeatureUtils.eGetNsURI(eClass);
        //
        // Try to get cached structure
        //
        EFeatureInfo eInfo = get(eNsURI,eDomainID,eFolder,eClass.getName());
        if(eInfo!=null && EcoreUtil.equals(eInfo.eClass(),eClass)) {
            return eInfo;
        }
        //
        // Not found
        //
        return null;
    }
    
//    public final EFeatureInfo get(String eDomainID, String eFolder, EReference eReference) {
//        //
//        // Get name space URI string
//        //
//        String eNsURI = EFeatureUtils.eGetNsURI(eReference,true);
//        //
//        // Try to get cached structure
//        //
//        EFeatureInfo eInfo = get(eNsURI, eDomainID, eFolder, eReference.getName());
//        if(eInfo!=null && EcoreUtil.equals(eInfo.eReference(),eReference)) {
//            return eInfo;
//        }
//        //
//        // Not found
//        //
//        return null;
//    }

    public final EFeatureInfo get(String eKey) {
        WeakReference<EFeatureInfo> eInfo = eCache.get(eKey);
        if( eInfo != null) {
            return eInfo.get();
        }
        return null;
    }
    
//    public final EFeatureInfo get(String eNsURI, String eDomainID, String eType) {
//        return get(eNsURI,eDomainID,
//                EFeatureUtils.toFolderName(eType),
//                EFeatureUtils.toFeatureName(eType));
//    }
//    
    public final EFeatureInfo get(String eNsURI, String eDomainID, String eFolder, String eFeature) {
        return get(createKey(eNsURI, eDomainID, eFolder, eFeature));
    }
    

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    public static final String createKey(EFeatureInfo eInfo) {
        return createKey(eInfo.eNsURI(),eInfo.eDomainID(),eInfo.eFolderName(), eInfo.eName());
    }
    
    private static final String createKey(String eNsURI, String eDomainID, String eFolder, String eFeature) {
        return eNsURI + "/" +eDomainID + "/" + eFolder + "/" + eFeature;
    }
    
    /**
     * Check if all voters agree to cache given {@link EFeatureInfo structure}. 
     * @param eInfo - the structure
     * @return {@link #ALLOWED} if all agree, {@link #DENIED} else. 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final EFeatureStatus vote(EFeatureInfo eInfo) {
        for (EFeatureListener it : eListeners) {
            if(!it.onChange(this, CACHE, null, eInfo)) {
                return DENIED;
            }
        }
        return ALLOWED;        
    }
    

}
