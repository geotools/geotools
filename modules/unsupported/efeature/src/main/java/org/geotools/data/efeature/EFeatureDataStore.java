package org.geotools.data.efeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.data.store.ContentState;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Geotools {@link DataStore} for {@link EFeature}s
 * 
 * @author kengu
 * 
 */
public class EFeatureDataStore extends ContentDataStore {
    
    /**
     * Publisher URI
     */
    public static final String PUBLISHER = "http://www.osgeo.org";
    
    protected final URI eURI;

    protected final String eNsURI;

    protected final String eContextID;

    protected final String eDomainID;

    protected final String eTypeQuery;
    
    protected final Query eDataStoreQuery;
    
    protected final boolean eWritable;
    
    protected DefaultServiceInfo serviceInfo;

    /** 
     * Cached {@link EFeaturePackageInfo} instance 
     */
    protected final EFeaturePackageInfo ePackageInfo;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * A {@link EFeature} {@link DataStore} implementation class.
     * @param eContextID - {@link EFeatureContext} instance id
     * @param eDomainID - {@link EditingDomain} instance extension id
     * @param eNsURI - {@link EPackage} name space
     * @param eURI - {@link URI} formated string to EMF {@link Resource} containing 
     *  {@link EFeature}s
     * @param eTypes - {@link EFeature} types on the following format:
     * <pre>
     * eTypes:=&lt;eType1&gt;+...+&lt;eTypeN&gt;
     * 
     * where
     *  
     * eType    := &lt;eFolder&gt;.&lt;eFeature&gt;
     * eFolder  := the name of the {@link EFeatureFolderInfo folder} which contains the {@link EFeatureInfo feature}
     * eFeature := the name of the {@link EFeatureInfo feature type}
     * </pre>
     * @param eWritable - if <code>true</code> {@link EFeature}s are writable 
     * ({@link EFeatureWriter#UPDATE} | {@link EFeatureWriter#APPEND})
     * @throws IOException 
     * @throws IllegalArgumentException If any argument is invalid.
     * @see {@link URI#createURI(String)}
     */
    public EFeatureDataStore(String eContextID, String eDomainID, String eNsURI, 
            String eURI, String eTypes, boolean eWritable) throws IOException, IllegalArgumentException {
        //
        // Forward using same context factory as EFeatureDataStoreFactory
        //
        this(EFeatureDataStoreFactory.eGetContextFactory().eContext(eContextID),eDomainID,eNsURI,eURI,eTypes,eWritable);
    }
    
    /**
     * A {@link EFeature} {@link DataStore} implementation class.
     * <p>
     * @param eContext - {@link EFeatureContext} instance
     * @param eDomainID - {@link EditingDomain} instance id
     * @param eNsURI - {@link EPackage} name space
     * @param eURI - {@link URI} formated string to EMF {@link Resource} containing 
     *  {@link EFeature}s
     * @param eTypes - {@link EFeature} type query on the following format:
     * <pre>
     * eTypes:=&lt;eType1&gt;+...+&lt;eTypeN&gt;
     * 
     * where
     *  
     * eType    := &lt;eFolder&gt;.&lt;eFeature&gt;
     * eFolder  := the name of the {@link EFeatureFolderInfo folder} which contains the {@link EFeatureInfo feature}
     * eFeature := the name of the {@link EFeatureInfo feature type}
     * </pre>
     * @param eWritable - if <code>true</code> {@link EFeature}s are writable 
     * ({@link EFeatureWriter#UPDATE} | {@link EFeatureWriter#APPEND})
     * @throws IOException 
     * @throws IllegalArgumentException If any argument is invalid.
     * @see {@link URI#createURI(String)}
     */
    public EFeatureDataStore(EFeatureContext eContext, String eDomainID, String eNsURI, 
            String eURI, String eTypes, boolean eWritable) throws IOException, IllegalArgumentException {
        //
        // Forward to ContentStore constructor
        //
        super();
        //
        // Get context ID
        //
        this.eContextID = eContext.eContextID();
        //
        // Get EFeatureStore information
        //
        this.ePackageInfo = EFeatureDataStoreFactory.ePackageInfo(eContextID, eNsURI);
        if (this.ePackageInfo == null) {
            throw new IOException("EFeatureDataStore structure not " 
                    + "found in context: '" + eContextID + "/"
                    + eDomainID + "/" + eURI );
        }
        //
        // Cache other information
        //
        this.eDomainID = eDomainID;
        this.eTypeQuery = eTypes;
        this.eNsURI = eNsURI;
        this.eURI = URI.createURI(eURI);
        this.eWritable = eWritable;
        //
        // TODO Create EFeature query from 'eQuery' (must be added)
        //        
        this.eDataStoreQuery = Query.ALL;
    }
    
    // ----------------------------------------------------- 
    //  Unsupported ContentDataStore methods
    // -----------------------------------------------------

    /**
     * {@link EFeatureDataStore} does not support {@link FeatureTypeFactory}
     * @throws UnsupportedOperationException Operation not supported
     */
    @Override
    public FeatureTypeFactory getFeatureTypeFactory() {
        throw new UnsupportedOperationException("EFeatureDataStore does not support FeatureTypeFactory");
    }
    
    /**
     * {@link EFeatureDataStore} does not support {@link FeatureTypeFactory}
     * @throws UnsupportedOperationException Operation not supported
     */
    @Override
    public void setFeatureTypeFactory(FeatureTypeFactory typeFactory) {
        throw new UnsupportedOperationException("EFeatureDataStore does not support FeatureTypeFactory");
    }

    /**
     * {@link EFeatureDataStore} does not support {@link FeatureFactory}
     * @throws UnsupportedOperationException Operation not supported
     */
    @Override
    public FeatureFactory getFeatureFactory() {
        throw new UnsupportedOperationException("EFeatureDataStore does not support FeatureFactory");
    }
    
    /**
     * {@link EFeatureDataStore} does not support {@link FeatureFactory}
     * @throws UnsupportedOperationException Operation not supported
     */
    @Override
    public void setFeatureFactory(FeatureFactory featureFactory) {
        throw new UnsupportedOperationException("EFeatureDataStore does not support FeatureFactory");
    }
    
    /**
     * {@link EFeatureDataStore} does not support {@link GeometryFactory}
     * @throws UnsupportedOperationException Operation not supported
     */
    @Override
    public GeometryFactory getGeometryFactory() {
        throw new UnsupportedOperationException("EFeatureDataStore does not support GeometryFactory");
    }

    /**
     * {@link EFeatureDataStore} does not support {@link GeometryFactory}
     * @throws UnsupportedOperationException Operation not supported
     */
    @Override
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        throw new UnsupportedOperationException("EFeatureDataStore does not support GeometryFactory");
    }    
    
    // ----------------------------------------------------- 
    //  Overridden ContentDataStore methods
    // -----------------------------------------------------
    
    @Override
    public FilterFactory getFilterFactory() {
        if(filterFactory==null) {
            filterFactory = CommonFactoryFinder.getFilterFactory(null);
        }
        return filterFactory;
    }
    
    @Override
    public ContentEntry getEntry(Name name) {
        // TODO Auto-generated method stub
        return super.getEntry(name);
    }
    
    // ----------------------------------------------------- 
    //  EFeatureDataStore convenience methods
    // -----------------------------------------------------
    
    /**
     * Returns a {@link EFeatureWriter} for the specified type name and transaction.
     * <p>
     * This is a convenience method for <code>getFeatureWriter(typeName,filter,tx)</code>,
     * which returns a writer capable of both updating and appending {@link EFeature features}
     * </p>
     * @param eType - (required) {@link EFeature} type name on the format:
     * <pre>
     * eType := &lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder  := the name of the {@link EFeatureFolderInfo folder} which contains the {@link EFeatureInfo feature}
     * eFeature := the name of the {@link EFeatureInfo feature}
     * </pre>  
     * @param filter - (Optional) {@link Filter} selecting {@link EFeature features} to be updated. 
     * If <code>null</code>, {@link Filter#INCLUDE} is assumed.
     * @param tx - (Optional) {@link Transaction} controlling modifications. If <code>null</code>, 
     * {@link Transaction#AUTO_COMMIT} is assumed.
     * @return a {@link EFeatureWriter} instance. 
     * @see {@link #getFeatureWriter(String, Filter, Transaction)
     */
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getEFeatureWriter(String eType, Filter filter, 
            Transaction tx) throws IOException {
        tx = ensureTransaction(tx);
        filter = ensureFilter(filter);
        return getFeatureWriter( eType, filter, tx );
    }

    /**
     * Returns a {@link EFeatureWriter} for the specified type name and transaction.
     * <p>
     * This returns a writer only capable of updating {@link EFeature features}
     * </p>
     * @param eType - (required) {@link EFeature} type name on the format:
     * <pre>
     * eType := &lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder  := the name of the {@link EFeatureFolderInfo folder} which contains the {@link EFeatureInfo feature}
     * eFeature := the name of the {@link EFeatureInfo feature}
     * </pre>  
     * @param filter - (Optional) {@link Filter} selecting {@link EFeature features} to be updated. 
     * If <code>null</code>, {@link Filter#INCLUDE} is assumed.
     * @param tx - (Optional) {@link Transaction} controlling modifications. If <code>null</code>, 
     * {@link Transaction#AUTO_COMMIT} is assumed.
     * @return a {@link FeatureWriter} instance. 
     */
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getEFeatureWriterUpdate(String eType, Filter filter, 
            Transaction tx) throws IOException {
        
        tx = ensureTransaction(tx);
        filter = ensureFilter(filter);
        ContentFeatureStore featureStore = ensureFeatureStore(eType,tx);
        return featureStore.getWriter( filter , WRITER_UPDATE );
    }
    
    /**
     * Returns an appending {@link EFeatureWriter} for the specified type name and 
     * transaction.
     * <p>
     * This is a convenience method for <code>getFeatureWriterAppend(typeName,tx)</code>,
     * which returns a writer only capable of appending {@link EFeature features} 
     * (no filter is therefore required).
     * </p>
     * @param eType - (required) {@link EFeature} type name on the format:
     * <pre>
     * eType := &lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder  := the name of the {@link EFeatureFolderInfo folder} which contains the {@link EFeatureInfo feature}
     * eFeature := the name of the {@link EFeatureInfo feature}
     * </pre>  
     * @param tx - (Optional) {@link Transaction} controlling modifications. If <code>null</code>, 
     * {@link Transaction#AUTO_COMMIT} is assumed.
     * @return a {@link FeatureWriter} instance. 
     * @see {@link #getEFeatureWriterAppend(String, Transaction)
     */
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getEFeatureWriterAppend(
            String eType, Transaction tx) throws IOException {                
        tx = ensureTransaction(tx);
        return getFeatureWriterAppend(eType, tx );
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
    public EFeatureReader getFeatureReader(Query query, Transaction transaction) throws IOException {
        return (EFeatureReader)super.getFeatureReader(query, transaction);
    }

    /**
     * Get a {@link EFeatureReader} for given {@link EFeature} type name.
     * </p>
     * @param eType - {@link EFeature} type name on the format:
     * <pre>
     * eType := &lt;eFolder&gt;.&lt;eFeature&gt;
     * 
     * where
     * 
     * eFolder  := the name of the {@link EFeatureFolderInfo folder} which contains the {@link EFeatureInfo feature}
     * eFeature := the name of the {@link EFeatureInfo feature}
     * </pre>  
     * @return an {@link EFeatureReader} of given {@link EFeature} type
     */
    public EFeatureReader getFeatureReader(String eType) throws IOException {
        return getFeatureReader(new Query(eType,Filter.INCLUDE));
    }
    
    // ----------------------------------------------------- 
    //  ContentDataStore implementation
    // -----------------------------------------------------
    
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
    protected List<Name> createTypeNames() throws IOException {
        List<Name> eNames = new ArrayList<Name>();
        for(String eName : ePackageInfo.getTypeNames(eTypeQuery)) {
            eNames.add(new NameImpl(eName));
        }
        return eNames;
    }
    
    @Override
    protected ContentState createContentState(ContentEntry entry) {
        //
        // Forward to default implementation
        //
        ContentState state = super.createContentState(entry);
        //
        // Get EFeature structure info
        //
        EFeatureInfo eStructure = ePackageInfo.eGetFeatureInfo(entry.getTypeName());
        //
        // Set SimpleFeature type definition
        //
        state.setFeatureType(eStructure.getFeatureType());        
        //
        // Finished
        //
        return state;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        //
        // Create FeatureStore containing EFeature instances matching current data store query
        //
        return eWritable ? new EFeatureStore(entry, eDataStoreQuery)
                         : new EFeatureSource(entry, eDataStoreQuery);
    }

    // ----------------------------------------------------- 
    //  Static helper methods
    // -----------------------------------------------------

    protected static Filter ensureFilter(Filter filter) {
        return (filter == null ? Filter.INCLUDE : filter);
    }
    
    protected static Transaction ensureTransaction(Transaction tx) {
        return (tx == null ? Transaction.AUTO_COMMIT : tx);
    }
    
    
}
