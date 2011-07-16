/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.efeature;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.impl.ESimpleFeatureImpl;
import org.geotools.data.simple.SimpleFeatureWriter;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author kengu - 23. juni 2011
 * 
 */
public class EFeatureWriter implements SimpleFeatureWriter {

    /**
     * Cached {@link EFeatureReader} instance
     */
    private final EFeatureReader eReader;
    
    /**
     * Next feature in line for a add, update or remove
     */
    private ESimpleFeature eNext;

    /**
     * Copy of feature returned by {@link #eReader}.
     * <p> 
     * It is this instance which is returned by 
     * {@link #next()}, allowing the writer to detect 
     * any changes made to it on the next call to 
     * {@link #next()} {@link #hasNext()} or {@link #write()}
     * by comparing to the {@link #eNext} instance.
     * </p> 
     */
    private ESimpleFeature eLive;
    
    /**
     * Writer mode flags ({@link #UPDATE} | {@link #APPEND})
     */
    private final int flags;
    
    /**
     * Writer append mode flag. 
     * <p>
     * If set, the writer returns a new 
     * {@link EFeature} of given type when the writer has
     * no more updateable features. If this flag can be 
     * bit-wise OR'ed with the {@link #UPDATE} flag.
     * </p>  
     */
    public final static int APPEND = 0x01<<0;

    /**
     * Writer update mode flag. 
     * <p>
     * If set, the writer returns updateable 
     * features as long as there exists {@link EFeature}s
     * matching given query. This flag can be 
     * bit-wise OR'ed with the {@link #UPDATE} flag.
     * </p>  
     */
    public final static int UPDATE = 0x01<<1;
    
    
    // -----------------------------------------------------
    // Constructors
    // -----------------------------------------------------

    /**
     * The {@link EFeatureWriter} constructor.
     * <p>
     * This constructor create a writer that supports both 
     * {@link #UPDATE updates} and {@link #UPDATE appending} of new
     * features. 
     * </p>
     * @param eStore - {@link EFeatureDataStore} instance containing 
     * {@link EFeature} resource information
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
    public EFeatureWriter(EFeatureDataStore eStore, Query query) throws IOException {
        this(eStore, query, Transaction.AUTO_COMMIT, 0);
    }
    
    /**
     * The {@link EFeatureWriter} constructor.
     * 
     * @param eStore - {@link EFeatureDataStore} instance containing 
     * {@link EFeature} resource information
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
     * @param eTx {@link Transaction} instance
     * @param flags - writer move flags ({@link #UPDATE} | {@link #APPEND})
     * @throws IOException
     */    
    public EFeatureWriter(EFeatureDataStore eStore, Query query, Transaction eTx, int flags) throws IOException {
        this.eReader = new EFeatureReader(eStore, query, eTx);
        this.flags = flags;
    }

    // -----------------------------------------------------
    // SimpleFeatureWriter implementation
    // -----------------------------------------------------
    
    /**
     * Check if this writer supports updating. 
     * <p>
     * If set, the writer returns updateable 
     * features as long as there exists {@link EFeature}s
     * matching given query. 
     * </p>  
     */    
    public boolean isUpdating() {
        return (flags & APPEND) == APPEND;
    }
    
    /**
     * Check if this writer supports appending. 
     * <p>
     * If <code>true</code>, the writer returns a new 
     * {@link EFeature} of given type when the writer has
     * no more updateable features. </p>  
     */    
    public boolean isAppending() {
        return (flags & APPEND) == APPEND;
    }
    
    @Override
    public SimpleFeatureType getFeatureType() {
        return eReader.getFeatureType();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (eReader == null) {
            throw new IOException("Writer has been closed");
        }
        if (isModified()) {
            write();
        }
        return eReader.hasNext();
    }

    @Override
    public ESimpleFeature next() throws IOException {
        if (eReader == null) {
            throw new IOException("Writer has been closed");
        }
        String fid = null;
        try {
            //
            // Get flag
            //
            boolean bHasNext = hasNext();
            //
            // Has more features?
            //
            if (isUpdating() && bHasNext) {
                //
                // Grab next feature matching given query
                //
                eNext = eReader.next();
                //
                // Create live copy
                //
                eLive = new ESimpleFeatureImpl(eNext, eReader.eTx);
                //
                // Finished
                //
                return eLive;
                
            } else if(isAppending()) {
                //
                //  No more EObject found, create new from structure
                //
                EFeature eFeature = eNewInstance();
                //
                // Get default values
                //
                Object eValues[] = DataUtilities.defaultValues(getFeatureType());
                //
                // Set default values
                //
                eSetValues(eFeature, Arrays.asList(eValues));
                //
                // Finished
                //
                return (ESimpleFeature)eFeature.getData(eReader.eTx);
            }
            //
            // Illegal writer mode
            //
            if(bHasNext) {
                throw new IOException("EFeatureWriter does allow updates");
            } else {
                throw new IOException("EFeatureWriter does allow appending");                
            }
            
        } catch (IllegalAttributeException e) {
            String message = "Problem creating feature "
                    + (fid != null ? fid : "");
            throw new DataSourceException(message, e);
        }    
    }

    @Override
    public void remove() throws IOException {
        //
        // Do sanity check
        //
        if (eNext  == null) {
            throw new IOException("No current feature to remove");
        }
        //
        // Remove from resource?
        //
        EObject eObject = eNext.eObject();
        if(eObject.eResource()!=null) {            
            EcoreUtil.delete(eObject);
        }
        //
        // Release strong references
        //
        eNext = null;
        eLive = null;
    }

    @Override
    public void write() throws IOException {
        //
        // Do sanity checks
        //
        if (eLive == null) {
            throw new IOException("No current feature to write");
        }
        if (!isModified()) {
            throw new IOException("Feature is not modified");            
        }
        //
        // Write feature values to EObject
        //
        eLive.write(eReader.eTx);
        //
        // Add to resource backing given data store?
        //
        if(eNext==null) {
            EObject eObject = eLive.eObject();
            if(eObject.eResource()==null) {   
                eReader.eDataStore().eResource().getContents().add(eObject);
            }
        }
        //eReader.eDataStore.listenerManager;
        //
        // Release strong references
        //
        eNext = null;
        eLive = null;
    }

    /**
     * Reset current iterator.
     * <p>
     * Any changes made to the feature
     * returned by last call to {@link #next()}
     * is discarded.
     * @throws IOException
     */    
    public void reset() throws IOException {
        //
        // Forward
        //
        eReader.reset();       
        //
        // Release strong references
        //
        eNext = null;        
        eLive = null;
    }
    
    @Override
    public void close() throws IOException {
        //
        // Forward
        //
        eReader.close();
        //
        // Release strong references
        //
        eNext = null;        
        eLive = null;
    }
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected EFeature eNewInstance() {
        EFeatureInfo eStructure = eReader.eStructure;
        EObject eObject = eStructure.eNewInstance();        
        return EFeatureReader.eAdapt(eStructure, eObject,eReader.eHints);
    }
    
    protected void eSetValues(EObject eObject, List<Object> eValues) {
        EFeatureUtils.eSetFeatureValues(eReader.eStructure,eObject,eValues,eReader.eTx);        
    }
    
    protected boolean isModified(){
        return eLive!=null && (eNext == null || !eLive.equals(eNext));
    }

}
