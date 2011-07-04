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
import org.geotools.data.simple.SimpleFeatureWriter;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author kengu - 23. juni 2011
 * 
 */
public class EFeatureWriter implements SimpleFeatureWriter {

    private final EFeatureReader eReader;
    
    private ESimpleFeature eNext;
    
    // -----------------------------------------------------
    // Constructors
    // -----------------------------------------------------

    public EFeatureWriter(EFeatureDataStore eStore, String eType, Query query) throws IOException {
        this(eStore, eType, query, Transaction.AUTO_COMMIT);
    }
    
    public EFeatureWriter(EFeatureDataStore eStore, String eType, Query query, Transaction eTx) throws IOException {
        this.eReader = new EFeatureReader(eStore, eType, query, eTx);
    }

    // -----------------------------------------------------
    // SimpleFeatureWriter implementation
    // -----------------------------------------------------
    
    @Override
    public SimpleFeatureType getFeatureType() {
        return eReader.getFeatureType();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (eReader == null) {
            throw new IOException("Writer has been closed");
        }
        if (eNext != null) {
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
            // Has more features?
            //
            if (hasNext()) {
                //
                // Grab next feature matching given query
                //
                eNext = eReader.next(); 
                //
                // Finished 
                //
                return eNext;
                
            } else {
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
    }

    @Override
    public void write() throws IOException {
        //
        // Do sanity check
        //
        if (eNext  == null) {
            throw new IOException("No current feature to write");
        }
        //
        // Write feature values to EObject
        //
        eNext.write(eReader.eTx);
        //
        // Add to resource backing given data store ?
        //
        EObject eObject = eNext.eObject();
        if(eObject.eResource()==null) {   
            eReader.eDataStore().eResource().getContents().add(eObject);
        }
        //
        // Release strong references
        //
        eNext = null;
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
    

}
