package org.geotools.data.efeature;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.Query;
import org.geotools.data.efeature.internal.EFeatureDelegate;
import org.geotools.data.efeature.internal.ESimpleFeatureDelegate;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * {@link EFeature} reader implementation.
 * 
 * @author kengu
 * 
 */
public class EFeatureReader implements SimpleFeatureReader {
    
    /**
     * Static LOGGER for all {@link EFEatureReader} instances 
     */
    private static final Logger LOGGER = Logging.getLogger(EFeatureReader.class);
    
    /**
     * Cached {@link EFeatureInfo} instance
     */
    private final EFeatureInfo eFeatureInfo;

    /**
     * Cached {@link EFeatureDataStore} instance. Reference must be reset when the this reader is closed
     * to prevent memory leakage.
     */
    private WeakReference<EFeatureDataStore> eStore;

    /**
     * Cached {@link EFeatureAttributeReader} instance. Must be closed when this reader is closed to
     * prevent memory leakage.
     */
    private EFeatureAttributeReader eReader;
    
//    /**
//     * Static {@link EFeatureDelegate} cache. 
//     */
//    private static final WeakHashMap<EObject, EFeatureDelegate> 
//        eDelegateMap = new WeakHashMap<EObject, EFeatureDelegate>();

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * The {@link EFeatureReader} constructor.
     * 
     * @param eStore - {@link EFeatureDataStore} instance containing {@link EFeature} resource
     *        information
     * @param eType - {@link SimpleFeatureType} name.
     *        <p>
     *        {@link SimpleFeatureType} names have the following
     * 
     *        <pre>
     * eName=&lt;eFolder&gt;.&lt;eReference&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eReference = {@link EFeature} reference name
     * </pre>
     * 
     * @throws IOException
     */
    protected EFeatureReader(EFeatureDataStore eStore, String eType, Query query) throws IOException {
        this.eStore = new WeakReference<EFeatureDataStore>(eStore);
        this.eFeatureInfo = eStore.eStructure().eGetFeatureInfo(eType);
        this.eReader = new EFeatureAttributeReader(eStore, eType, query);
    }
    
    public void reset() throws IOException {
        this.eReader.reset();
    }

    public void close() throws IOException {
        this.eReader.close();
        this.eReader = null;
        EFeatureDataStore eStore = this.eStore.get();
        if(eStore!=null) eStore.onCloseReader(this);
        this.eStore = null;
    }

    public SimpleFeatureType getFeatureType() {
        return eReader.getFeatureType();
    }

    public boolean hasNext() throws IOException {
        return eReader.hasNext();
    }

    public ESimpleFeature next() throws IOException, IllegalArgumentException,
    NoSuchElementException {
        try {
            //
            // Initialize
            //
            Feature feature = null;
            //
            // Get current EFeature or EFeature data compatible EObject
            //
            EObject eObject = eReader.get();
            //
            // Adapt given object to EFeature structure
            //
            eObject = adapt(eFeatureInfo,eObject);
            //
            // Get feature from EFeature
            //
            feature = ((EFeature)eObject).getData();            
            //
            // Implements ESimpleFeature?
            //
            if(feature instanceof ESimpleFeature) {
                //
                // This is always the case if EFeatureInternal is used. 
                //
                return (ESimpleFeature)feature;
            } else {
                //
                // If EFeatureInternal is NOT used, this may be required since 
                // the getData() method of EFeature defines Feature, not 
                // SimpleFeature. Since use of EFeatureInternal is strongly 
                // recommended (solves several general problems, like the context 
                // startup problem etc.), a WARNING message is issued telling them that
                // the this operation may be unsafe. 
                //
                LOGGER.log(Level.WARNING,"Non-standard Feature " +
                		"implementation found. Unpredictable behavior may occur");
                //
                // Forward ESimpleFeature delegate
                //
                return new ESimpleFeatureDelegate(eObject,(SimpleFeature)feature);
            }


        } finally {
            //
            // Progress to next feature
            //
            eReader.next();
        }        
    }
    
    protected static EFeature adapt(EFeatureInfo eInfo, EObject eObject) {
        //
        // Adapt directly? 
        //
        if(eObject instanceof EFeature) {
            ((EFeature)eObject).setStructure(eInfo);
            return (EFeature)eObject;
        }
        //
        // Use EFeatureDelegate
        //
        return EFeatureDelegate.create(eInfo, (InternalEObject)eObject);
//        EFeatureDelegate eDelegate = eDelegateMap.get(eObject);
//        if( eDelegate == null ) {
//            eDelegate = new EFeatureDelegate(eInfo, (InternalEObject)eObject);
//            eDelegateMap.put(eObject,eDelegate);
//        }
//        return eDelegate;
    }

    /**
     * Get current {@link EFeature} id.
     * 
     * @return a {@link EFeature} id.
     * @see {@link EcoreUtil#getID(EObject)}
     */
    protected String getFeatureID() {
        return eReader.getFeatureID();
    }


    protected String getContextID() {
        return getStructure().eContextID;
    }

    protected EFeatureContextFactory getFactory() {
        return getStructure().eFactory();
    }

    protected EFeaturePackageInfo getStructure() {
        return eStore.get().ePackageInfo;
    }
    
}
