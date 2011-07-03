package org.geotools.data.efeature;

import static org.geotools.data.efeature.EFeatureHints.EFEATURE_VALUES_DETACHED;
import static org.geotools.data.efeature.EFeatureHints.EFEATURE_SINGLETON_FEATURES;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.internal.EFeatureDelegate;
import org.geotools.data.efeature.internal.ESimpleFeatureDelegate;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.factory.Hints;
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
    protected final EFeatureInfo eStructure;

    /**
     * Cached {@link EFeatureAttributeReader} instance. Must be closed when this reader is closed to
     * prevent memory leakage.
     */
    protected final EFeatureAttributeReader eReader;
    
    /**
     * Cached {@link EFeatureDataStore} instance. Reference must be reset when the this reader is closed
     * to prevent memory leakage.
     */
    protected WeakReference<EFeatureDataStore> eStore;
    
    /**
     * Cached {@link Transaction}. Can contain locking information
     */
    protected final Transaction eTx;
    
    /**
     * Cached Query hints
     */
    protected Hints hints;
    
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
        this.eStructure = eStore.eStructure().eGetFeatureInfo(eType);
        this.eReader = new EFeatureAttributeReader(eStore, eType, query);
        this.eTx = Transaction.AUTO_COMMIT;
        this.hints = query.getHints();
    }
    
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
    protected EFeatureReader(EFeatureDataStore eStore, String eType, Query query, Transaction eTx) throws IOException {
        this.eStore = new WeakReference<EFeatureDataStore>(eStore);
        this.eStructure = eStore.eStructure().eGetFeatureInfo(eType);
        this.eReader = new EFeatureAttributeReader(eStore, eType, query);
        this.eTx = eTx;
        this.hints = query.getHints();
    }
    
    // ----------------------------------------------------- 
    //  EFeatureReader implementation
    // -----------------------------------------------------
    
    public void reset() throws IOException {
        this.eReader.reset();
    }

    @Override
    public void close() throws IOException {
        this.eReader.close();
        EFeatureDataStore eStore = this.eStore.get();
        if(eStore!=null) eStore.onCloseReader(this);
        this.eStore = null;
    }
    
    public EFeatureInfo eStructure() {
        return eStructure;
    }

    public EFeatureDataStore eDataStore() {
        return eStore.get();
    }
    
    @Override
    public SimpleFeatureType getFeatureType() {
        return eReader.getFeatureType();
    }

    @Override
    public boolean hasNext() throws IOException {
        return eReader.hasNext();
    }

    @Override
    public ESimpleFeature next() throws IOException, 
        IllegalArgumentException, NoSuchElementException {
        
        //
        // Sanity check
        //
        if (!hasNext()) { 
            throw new NoSuchElementException();
        }
        
        try {
            //
            // Access to EFeatureInfo hints must be synchronized (thread safe)
            //
            eStructure.eLock();
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
            eObject = eAdapt(eStructure,eObject,hints);
            //
            // Get feature from EFeature
            //
            feature = eData((EFeature)eObject,hints);
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
                LOGGER.log(Level.WARNING,"Non-standard Feature implementation " +
                		"found. Unpredictable behavior may occur.");
                //
                // Forward ESimpleFeature delegate
                //
                return new ESimpleFeatureDelegate(eStructure, eObject,(SimpleFeature)feature);
            }

        } finally {
            //
            // Release lock on structure
            //
            eStructure.eUnlock();
            //
            // Progress to next feature
            //
            eReader.next();
        }        
    }
        
    protected static EFeature eAdapt(EFeatureInfo eStructure, EObject eObject, Hints hints) {
        //
        // Get EFeatureHints for given structure 
        //
        EFeatureHints eHints = eStructure.eHints();
        //
        // Override current hints 
        //
        Object eDetatchedValues = eHints.replace(hints,EFEATURE_VALUES_DETACHED);
        Object eSingletonFeatures = eHints.replace(hints,EFEATURE_SINGLETON_FEATURES);
        try {
            //
            // Adapt directly? 
            //
            if(eObject instanceof EFeature) {
                ((EFeature)eObject).setStructure(eStructure);
                return (EFeature)eObject;
            }
            //
            // Create new delegate and return it
            //
            return EFeatureDelegate.create(eStructure, (InternalEObject)eObject, true);
            
        } finally {
            //
            // Restore old hint states 
            //
            eHints.restore(EFEATURE_VALUES_DETACHED,eDetatchedValues);
            eHints.restore(EFEATURE_SINGLETON_FEATURES,eSingletonFeatures);
        }
    }
    
    protected static ESimpleFeature eData(EFeature eFeature, Hints hints) {
        //
        // Get EFeatureHints for given structure 
        //
        EFeatureHints eHints = eFeature.getStructure().eHints();
        //
        // Override current hints 
        //
        Object eDetatchedValues = eHints.replace(hints,EFEATURE_VALUES_DETACHED);
        Object eSingletonFeatures = eHints.replace(hints,EFEATURE_SINGLETON_FEATURES);
        try {
            //
            // Get ESimpleFeature instance
            //
            Feature data = eFeature.getData();
            //
            // Finished
            //
            return (ESimpleFeature)data;
            
        } finally {
            //
            // Restore old hint states 
            //
            eHints.restore(EFEATURE_VALUES_DETACHED,eDetatchedValues);
            eHints.restore(EFEATURE_SINGLETON_FEATURES,eSingletonFeatures);
        }
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
