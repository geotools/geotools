package org.geotools.data.efeature.internal;

import java.util.HashMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.EFeatureContextFactory;
import org.geotools.data.efeature.EFeatureFolderInfo;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureListener;
import org.geotools.data.efeature.EFeatureStatus;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.EStructureInfo;
import org.geotools.util.WeakHashSet;

/**
 * {@link EFeatureInfo} cache class.
 * <p>
 * Each {@link EFeatureInfo} instance is uniquely identified by a key. 
 * <p>
 * The key is constructed from the structure information and has 
 * following format: 
 * <pre>
 * eKey := &lt;eNsURI&gt;/&lt;eDomainID&gt;/&lt;eFolder&gt;/&lt;eFeature&gt;
 * 
 * where
 * 
 *  {@link EFeatureInfo#eNsURI() &lt;eNsURI&gt;}        := is the {@link EPackage#getNsURI() EMF package namespace URI} 
 *  {@link EFeatureInfo#eDomainID() &lt;eDomainID&gt;}     := is the {@link EFeatureContext#eGetDomain(String) EMF editing domain ID}
 *  {@link EFeatureInfo#eFolderName() &lt;eFolder&gt;}       := is the {@link EFeatureFolderInfo#eName() folder name}
 *  {@link EFeatureInfo#eName() &lt;eFeature&gt;}      := is the {@link EClass#getName() name} of the {@link EFeature} compatible {@link EClass implementation} 
 * </pre>
 * <p>
 * <b>NOTE</b>: {@link EFeatureInfo} instances are cached using hard 
 * references. Each {@link EFeatureInfo} instance is required to call 
 * {@link #detach(EFeatureInfo)} when the context is 
 * {@link EFeatureContextFactory#dispose(EFeatureContext) disposed} 
 * to allow the garbage collector to reclaim allocated memory.
 * </p>
 * @author kengu, 24. apr. 2011
 * 
 */
public final class EFeatureInfoCache {
    
    public static final int CACHE = 0;
    
    /**
     * Set of weakly referenced {@link EFeatureInfo} instances
     */
    private final HashMap<String, EFeatureInfo> 
        eCache = new HashMap<String, EFeatureInfo>();
    
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
        if(eStatus.isSuccess()) eCache.put(createKey(eInfo),eInfo);
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
     * Check if a {@link EFeatureInfo} instance is cached for 
     * the {@link EClass} defining given {@link EObject}
     * <p>
     * This is a convenience method calling {@link #contains(EClass)}
     * </p> 
     * @param eObject - the {@link EObject} instance
     * @return <code>true</code> if given instance is cached
     * @see {@link #contains(EClass)}
     */
    public final boolean contains(EObject eObject) {
        return contains(createKey(eObject));
    }
    
    /**
     * Check if a {@link EFeatureInfo} instance is cached for given {@link EClass}.
     * <p>
     * Since a {@link EClass}es only contains information 
     * about the {@link EPackage#getNsURI() eNsURI}
     * and EFeature {@link EClass#getName() root name}, this method only checks 
     * for {@link EFeatureInfo structures} instances that does not belong to 
     * any domain or folder.
     * <p>
     * <b>NOTE</b>: This only checks for <i>key equivalence</i>. If two keys are 
     * equal within the same {@link EFeatureContext}, the two structures must 
     * by definition be equal.
     * </p> 
     * @param eClass - the {@link EClass} instance
     * @return <code>true</code> if given instance is cached
     */
    public final boolean contains(EClass eClass) {
        return contains(createKey(eClass));
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
    
    /**
     * Get the {@link EFeatureInfo#isPrototype() prototype} 
     * {@link EFeatureInfo} instance cached for the {@link EClass} 
     * defining given {@link EObject}.
     * </p> 
     * @param eObject - the {@link EObject} instance
     * @return a {@link EFeatureInfo} if found,<code>null</code> otherwise.
     */        
    public final EFeatureInfo get(EObject eObject) {
        return get(null,null,eObject.eClass());
    }
    
    /**
     * Get the {@link EFeatureInfo#isPrototype() prototype} 
     * {@link EFeatureInfo} instance cached for given {@link EClass}.
     * </p> 
     * @param eFixture - the {@link EObject} instance
     * @return a {@link EFeatureInfo} if found,<code>null</code> otherwise.
     */        
    public final EFeatureInfo get(EClass eClass) {
        return get(null,null,eClass);
    }
        
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

    public final EFeatureInfo get(String eKey) {
        return eCache.get(eKey);
    }
    
    public final EFeatureInfo get(String eNsURI, String eDomainID, String eFolder, String eFeature) {
        return get(createKey(eNsURI, eDomainID, eFolder, eFeature));
    }
    

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------

    public static final String createKey(EObject eObject) {
        return createKey(eObject.eClass());
    }
    
    public static final String createKey(EClass eClass) {
        String eNsURI = eClass.getEPackage().getNsURI();
        return createKey(eNsURI,null,null, eClass.getName());
    }
    
    public static final String createKey(EFeatureInfo eInfo) {
        return createKey(eInfo.eNsURI(),eInfo.eDomainID(),eInfo.eFolderName(), eInfo.eName());
    }
    
    private static final String createKey(String eNsURI, String eDomainID, String eFolder, String eFeature) {
        String eKey = eNsURI + "/" +eDomainID + "/" + eFolder + "/" + eFeature;
        if(eKey.contains("//null")) {
            System.out.println();
        }
        return eKey;
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
