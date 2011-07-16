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
import org.geotools.data.Transaction;
import org.geotools.data.efeature.impl.EFeatureImpl;
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
    protected WeakReference<EFeatureDataStore> eDataStore;
    
    /**
     * Cached {@link Transaction}. Can contain locking information
     */
    protected final Transaction eTx;
    
    /**
     * Cached Query hints
     */
    protected EFeatureHints eHints;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * The {@link EFeatureReader} constructor.
     * 
     * @param eDataStore - {@link EFeatureDataStore} instance containing {@link EFeature} resource
     *        information
     * @param query - {@link Query} instance. Note that {@link Query#getTypeName()}
     * is expected to be a name of a {@link SimpleFeatureType} in given data store. 
     * <p>
     * {@link SimpleFeatureType} names have the following format:
     * 
     * <pre>
     * eName=&lt;eFolder&gt;.&lt;eReference&gt;
     * 
     * where
     * 
     * eFolder = {@link EFeature} folder name
     * eReference = {@link EFeature} reference name
     * </pre>
     * @throws IOException
     */
    protected EFeatureReader(EFeatureDataStore eDataStore, Query query) throws IOException {
        this(eDataStore, query, Transaction.AUTO_COMMIT);
    }
    
    /**
     * The {@link EFeatureReader} constructor.
     * 
     * @param eDataStore - {@link EFeatureDataStore} instance containing {@link EFeature} resource
     *        information
     * @param query - {@link Query} instance. Note that {@link Query#getTypeName()}
     * is expected to be a name of a {@link SimpleFeatureType} in given data store. 
     * <p>
     * {@link SimpleFeatureType} names have the following format:
     * 
     * <pre>
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
    protected EFeatureReader(EFeatureDataStore eDataStore, Query query, Transaction eTx) throws IOException {
        //
        // Cache references
        //
        this.eDataStore = new WeakReference<EFeatureDataStore>(eDataStore);
        this.eStructure = eDataStore.eStructure().eGetFeatureInfo(query.getTypeName());
        this.eReader = new EFeatureAttributeReader(eDataStore, query);
        this.eTx = eTx;
        //
        // Copy query hints
        //
        if(query.getHints() instanceof EFeatureHints) {
            this.eHints = new EFeatureHints(query.getHints());
        }else {
            this.eHints = new EFeatureHints(this.eStructure.eHints);            
            this.eHints.add(query.getHints());
        }
    }
    
    // ----------------------------------------------------- 
    //  EFeatureReader implementation
    // -----------------------------------------------------
    
    /**
     * Get {@link EFeatureHints}.
     */
    public EFeatureHints eHints() {
        return eHints;
    }
    
    /**
     * Reset current iterator.
     * @throws IOException
     */
    public void reset() throws IOException {
        this.eReader.reset();
    }

    @Override
    public void close() throws IOException {
        this.eReader.close();
        this.eDataStore = null;
    }
    
    public EFeatureInfo eStructure() {
        return eStructure;
    }

    public EFeatureDataStore eDataStore() {
        return eDataStore.get();
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
            EFeature eFeature = eAdapt(eStructure, eObject, eHints);
            //
            // Get feature from EFeature
            //
            feature = eFeature.getData(eTx);
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
                return new ESimpleFeatureDelegate(eStructure, eFeature, (SimpleFeature)feature, eHints);
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
        
    protected static EFeature eAdapt(EFeatureInfo eStructure, EObject eObject, EFeatureHints eHints) {
        //
        // Adapt directly? 
        //
        if(eObject instanceof EFeature) {
            //
            // Replace
            //
            if(eObject instanceof EFeatureImpl) {
                ((EFeatureImpl)eObject).eInternal().eReplace(eStructure,(EFeature)eObject,eHints, true);
            }
            //
            // TODO: Do we need this? Is never called in current implementation...
            //
            else if(eObject instanceof EFeatureDelegate) {
                ((EFeatureDelegate)eObject).eInternal().eReplace(eStructure,(EFeature)eObject,eHints, true);
            }
            //
            // Finished
            //
            return (EFeature)eObject;
        }
        //
        // Create new delegate and return it
        //
        return EFeatureDelegate.create(eStructure, (InternalEObject)eObject, true, eHints);
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
        return eDataStore.get().ePackageInfo;
    }
    
}
