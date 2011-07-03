package org.geotools.data.efeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * Geotools {@link DataStore} for {@link EFeature}s
 * 
 * @author kengu
 * 
 */
public class EFeatureDataStore extends AbstractDataStore {
    
    /**
     * Publisher URI
     */
    public static final String PUBLISHER = "http://www.osgeo.org";
    
    /** 
     * Static logger for all {@link EFeatureDataStore} instances 
     */
    private static final Logger LOGGER = Logging.getLogger(EFeatureDataStore.class); 

    protected final URI eURI;

    protected final String eNsURI;

    protected final String eContextID;

    protected final String eDomainID;

    protected final String eQuery;
    
    protected DefaultServiceInfo serviceInfo;

    /** Cached {@link EFeaturePackageInfo} instance */
    protected final EFeaturePackageInfo ePackageInfo;

    /** Open {@link EFeatureReader}s */
    private Map<String,EFeatureReader> readerMap = Collections
            .synchronizedMap(new HashMap<String,EFeatureReader>());

    /** Open {@link EFeatureWriter}s */
    private Map<String,EFeatureWriter> writerMap = Collections
        .synchronizedMap(new HashMap<String,EFeatureWriter>());

    /**
     * A {@link EFeature} {@link DataStore} implementation class.
     * @param eContextID - {@link EFeatureContext} instance id
     * @param eDomainID - {@link EditingDomain} instance extension id
     * @param eNsURI - {@link EPackage} name space
     * @param eURI - {@link URI} formated string to EMF {@link Resource} containing 
     *  {@link EFeature}s
     * @param eQuery - {@link EFeature} query
     * 
     * @throws IOException 
     * @throws IllegalArgumentException if any component parsed from uri is not valid.
     * @see {@link URI#createURI(String)}
     */
    public EFeatureDataStore(String eContextID, String eDomainID, String eNsURI, 
            String eURI, String eQuery) throws IOException, IllegalArgumentException {
        //
        // Forward using same context factory as EFeatureDataStoreFactory
        //
        this(EFeatureDataStoreFactory.eGetContextFactory().eContext(eContextID),eDomainID,eNsURI,eURI,eQuery);
    }
    
    /**
     * A {@link EFeature} {@link DataStore} implementation class.
     * @param eContextFactory - {@link EFeatureContextFactory} instance 
     * @param eContextID - {@link EFeatureContext} instance id
     * @param eDomainID - {@link EditingDomain} instance extension id
     * @param eNsURI - {@link EPackage} name space
     * @param eURI - {@link URI} formated string to EMF {@link Resource} containing 
     *  {@link EFeature}s
     * @param eQuery - {@link EFeature} query
     * 
     * @throws IOException 
     * @throws IllegalArgumentException if any component parsed from uri is not valid.
     * @see {@link URI#createURI(String)}
     */
    public EFeatureDataStore(EFeatureContext eContext, String eDomainID, String eNsURI, 
            String eURI, String eQuery) throws IOException, IllegalArgumentException {  
        //
        // Cache information
        //
        this.eContextID = eContext.eContextID();
        this.eDomainID = eDomainID;
        this.eQuery = eQuery;
        this.eNsURI = eNsURI;
        this.eURI = URI.createURI(eURI);
        //
        // Get EFeatureStore information
        //
        ePackageInfo = EFeatureDataStoreFactory.ePackageInfo(eContextID, eNsURI);
        if (this.ePackageInfo == null) {
            throw new IOException("EFeatureDataStore structure not " 
                    + "found in context: '" + eContextID + "/"
                    + eDomainID + "/" + eURI );
        }
    }
    
    // ----------------------------------------------------- 
    //  EFeatureDataStore methods
    // -----------------------------------------------------

    /**
     * Get {@link EPackage} names space URI as string.
     */
    public String eNsURI() {
        return eNsURI;
    }
    
    public EFeatureContext eContext() {
        return ePackageInfo.eContext();
    }

    /**
     * Get {@link EditingDomain} instance.
     * 
     * @return an {@link EditingDomain} instance
     */
    public EditingDomain eDomain() {
        return eContext().eGetDomain(eDomainID);
    }
    
    /**
     * Get {@link EPackage} instance.
     * 
     * @return the {@link EPackage} instance.
     */
    public EPackage ePackage() {
        return eContext().eGetPackage(eNsURI);
    }    
    
    /**
     * Get {@link URI} to {@link Resource} containing {@link EFeature}s or {@link EFeature}
     * compatible data.
     * 
     * @return an {@link URI} instance
     */
    public URI eResourceURI() {
        return eURI;
    }
    
    /**
     * Get the {@link Resource} that contains the data.
     */
    public Resource eResource() {
        return eContext().eGetResource(eDomainID, eURI, true);
    }

    /**
     * Information about this service.
     * <p>
     * This method offers access to a summary of header or metadata
     * information describing the service.
     * </p>
     * @return SeviceInfo
     */    
    @Override
    public ServiceInfo getInfo() {
        if(serviceInfo==null) {
            serviceInfo = new DefaultServiceInfo();
            serviceInfo.setTitle("EFeature DataStore");
            serviceInfo.setKeywords(new HashSet<String>(
                    Arrays.asList("EFeature", "EMF", "EMF Query", "EMF Transaction")));
            serviceInfo.setDescription( "The EFeature DataStore module adds support " +
            		"for spatial read and write operations to EMF models." );
            serviceInfo.setSchema( java.net.URI.create( ePackageInfo.eNsURI ) );
            serviceInfo.setPublisher( java.net.URI.create(PUBLISHER) );
        }
        serviceInfo.setSource( java.net.URI.create(eURI.toString()) );
        return serviceInfo;
    }
    
    /**
     * Get {@link EFeaturePackageInfo structure information}.
     * </p>
     * @return a {@link EFeaturePackageInfo} instance.
     */
    public EFeaturePackageInfo eStructure() {
        return ePackageInfo;
    }
    
    /**
     * Gets the names of {@link FeatureType feature types} available 
     * in this {@code EFeatureDataStore store}.
     * <p>
     * Since {@link EFeatureDataStore}s only have one name space by design, 
     * this method is guaranteed to return a list of unique 
     * names since the all unqualified type names are present in the same 
     * name space.
     * </p>
     * The following naming convention is used:
     * <pre>
     * name := &lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder  := the name of the {@link EFeatureFolderInfo folder} which contains the {@link EFeatureInfo feature}
     * eFeature := the name of the {@link EFeatureInfo feature}
     * </pre>  
     *
     * @return names of EFeature types available in this {@code DataStore}
     *
     * @see EFeatureUtils#toFolderName(String) - parse type name into folder name
     * @see EFeatureUtils#toFeatureName(String) - parse type name into feature name
     */
    @Override
    public String[] getTypeNames() {
        return ePackageInfo.getTypeNames(eQuery);
    }
        
    /**
     * Retrieve schema information for given {@link SimpleFeatureType}.
     * <p> 
     * This method delegates to {@link #getSchema(String)} with {@code name.getLocalPart()}
     * </p>
     * @param name - the feature type {@link Name name}.
     * @return a {@link SimpleFeatureType} instance
     * @see {@link #getSchema(String)}
     * @see {@link DataAccess#getSchema(Name)}
     */
    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }
    
    /**
     * Retrieve schema information for given {@link SimpleFeatureType}
     * <p>
     * @param name - the feature type name.
     * @return a {@link SimpleFeatureType} if EClass was found, <code>null</code> otherwise.
     * @see {@link #getTypeNames()} - the eType naming convention is described here 
     */
    @Override
    public SimpleFeatureType getSchema(String eType) {
        EFeatureInfo eFeatureInfo = ePackageInfo.eGetFeatureInfo(eType);
        if (eFeatureInfo != null) {
            return eFeatureInfo.getFeatureType();
        }
        return null;
    }
    
    /**
     * Convenience method for calling {@link #getFeatureReader(Query, Transaction)}
     * with {@link Transaction#AUTO_COMMIT} as the transaction.
     * 
     * @param query - the {@link EFeature} query
     * @return a {@link EFeatureReader} instance.
     * @throws IOException
     */
    public EFeatureReader getFeatureReader(Query query) throws IOException {
        return getFeatureReader(query, Transaction.AUTO_COMMIT);
    }    

    @Override
    public EFeatureReader getFeatureReader(Query query,
            Transaction transaction) throws IOException {
        return (EFeatureReader)super.getFeatureReader(query, transaction);
    }

    /**
     * Get a {@link EFeatureReader} for given {@link EFeature} type name.
     * <p>
     * The type name have the following format
     * <pre>
     * name := &lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder  := the name of the {@link EFeatureFolderInfo folder} which contains the {@link EFeatureInfo feature}
     * eFeature := the name of the {@link EFeatureInfo feature}
     * </pre>  
     * @param eType - {@link EFeature} type name
     * @return an {@link EFeatureReader} of given {@link EFeature} type
     */
    @Override
    public EFeatureReader getFeatureReader(String eType) throws IOException {
        return getFeatureReader(eType, Query.ALL);
    }
    
    public final Collection<EFeatureReader> getReaders() {
        return Collections.unmodifiableCollection(readerMap.values());
    }
    
    public final Collection<EFeatureWriter> getWriters() {
        return Collections.unmodifiableCollection(writerMap.values());
    }    

    /**
     * Dispose all open {@link EFeatureReader readers} and 
     * {@link EFeatureReader writers} opened by this 
     * {@link EFeatureDataStore data store}.
     */
    @Override
    public void dispose() {
        //
        // Dispose all readers
        //
        Collection<EFeatureReader> readers = new ArrayList<EFeatureReader>(this.readerMap.values());
        for(EFeatureReader it : readers) {
            try {
                it.close();
            } catch (IOException e) {
                SimpleFeatureType type = it.getFeatureType();
                LOGGER.log(Level.WARNING, "Failed to close EFeatureReader for type" + 
                        (type!=null ? type.getTypeName() : "unknown"), e);
            }
        }
        //
        // Dispose all writers
        //
        Collection<EFeatureWriter> writers = Collections.unmodifiableCollection(this.writerMap.values());
        for(EFeatureWriter it : writers) {
            try {
                it.close();
            } catch (IOException e) {
                SimpleFeatureType type = it.getFeatureType();
                LOGGER.log(Level.WARNING, "Failed to close EFeatureWriter for type" + 
                        (type!=null ? type.getTypeName() : "unknown"), e);
            }
        }
        //
        // Forward
        //
        super.dispose();
    }

    // ----------------------------------------------------- 
    //  AbstractDataStore implementation
    // -----------------------------------------------------
    
    @Override
    protected EFeatureReader getFeatureReader(String eType, Query query) throws IOException {
        synchronized (readerMap) {
            String eKey = eType+query;
            EFeatureReader reader = readerMap.get(eKey);
            if(reader==null) {
                reader = new EFeatureReader(this, eType, query);
                readerMap.put(eKey,reader);
            }
            return reader;
        }
    }    
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected void onCloseReader(EFeatureReader eReader) {
        synchronized (readerMap) {
            readerMap.values().remove(eReader);
        }
    }

    protected void onCloseWriter(EFeatureReader eWriter) {
        synchronized (writerMap) {
            writerMap.values().remove(eWriter);
        }
    }

}
