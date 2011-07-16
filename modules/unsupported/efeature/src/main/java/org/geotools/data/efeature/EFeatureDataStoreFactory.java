package org.geotools.data.efeature;

import static org.geotools.data.efeature.EFeatureDialect.EFEATURE_CONTEXT_ID;
import static org.geotools.data.efeature.EFeatureDialect.EPACKAGE_NS_URI;
import static org.geotools.data.efeature.EFeatureDialect.EDITING_DOMAIN_ID;
import static org.geotools.data.efeature.EFeatureDialect.ERESOURCE_URI;
import static org.geotools.data.efeature.EFeatureDialect.EFOLDERS_QUERY;
import static org.geotools.data.efeature.EFeatureDialect.EFEATURE_WRITABLE;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A state-less {@link EFeatureDataStore} {@link DataStoreFactorySpi} class implementation.
 * <p>
 * This class adds {@link EFeatureDataStore} instance creation capabilities to the 
 * GeoTools framework. The factory keeps no reference to created 
 * {@link EFeatureDataStore}s, it each data store is given a reference to this factory instance.
 * </p>
 * 
 * @author kengu
 * 
 * @see {@link EFeatureDataStore#getDataStoreFactory()}
 * 
 */
public class EFeatureDataStoreFactory implements DataStoreFactorySpi {
    
    // ----------------------------------------------------- 
    //  Public parameters
    // -----------------------------------------------------
        
    /**
     * Statically cached {@link EFeatureDialect} instance.
     */
    public static final EFeatureDialect DIALECT = new EFeatureDialect();

    /**
     * {@link EFeatureContext} instance ID.
     * <p>
     */
    public static final Param EFEATURE_CONTEXT_ID_PARAM = new Param(
            EFEATURE_CONTEXT_ID, String.class, 
            "EFeatureContext instance ID", true);
    
    /**
     * {@link EditingDomain} instance ID.
     * <p>
     * All readers and writers are forces to use given {@link EditingDomain} instance for read/write
     * EMF model access.
     * <p>
     */
    public static final Param EDITING_DOMAIN_ID_PARAM = new Param(
            EDITING_DOMAIN_ID, String.class,
            "Extension point id to an EditingDomain instance", true);

    /**
     * The name space URI of the {@link EPackage} which the {@link EClass} with name
     * {@link #EFOLDERS_QUERY} belongs.
     * <p>
     */
    public static final Param EPACKAGE_NS_URI_PARAM = new Param(
            EPACKAGE_NS_URI, String.class,
            "The namespace URI of the EPackage which "
            + "defines EObjects containing EFeatures or EFeature compatible data", true);

    /**
     * {@link URI} to the {@link Resource} which the {@link EFeatureDataStore} instance fetches
     * {@link SimpleFeature}s from.
     * <p>
     * The {@link URI} points to a {@link Resource} managed by the {@link EditingDomain} specified
     * by {@link #EDITING_DOMAIN_ID}.
     * <p>
     * All readers and writers are forces to use given {@link EditingDomain} instance for read/write
     * access to the {@link Resource}.
     * <p>
     */
    public static final Param ERESOURCE_URI_PARAM = new Param(
            ERESOURCE_URI, String.class,
            "URI to EMF Resource instance containing EFeatures or "
            + "EFeature compatible data managed by the editing domain", true);

    /**
     * A (optional) query that defines which {@link EFeature} folders to 
     * include in a {@link EFeatureDataStore}.
     * <p>
     * This parameter has the following syntax:
     * 
     * <pre>
     * eFolders=&lt;eFolder1&gt;+...+&lt;eFolderN&gt;
     * 
     * where
     * 
     * eFolder = &lt;eName&gt;[:&lt;eQuery&gt;|$&lt;eFragment&gt;]
     * </pre>
     */
    public static final Param EFOLDERS_QUERY_PARAM = new Param(EFOLDERS_QUERY, String.class,
            "A query that defines which EFeature folders to include in a EFeatureStore.", false);

    /**
     * A boolean flag indication if {@link EFeature}s can be 
     * written ({@link EFeatureWriter#UPDATE} | 
     * {@link EFeatureWriter#APPEND}).  
     */
    public static final Param EFEATURE_WRITABLE_PARAM = new Param(EFEATURE_WRITABLE, Boolean.class,
            "A boolean flag indicating if EFeatures can be written (UPDATE | APPEND)", false);
    
    
    // ----------------------------------------------------- 
    //  Other static members
    // -----------------------------------------------------    
    
    /** 
     * Static logger for all {@link EFeatureDataStoreFactory} instances 
     */
    private static final Logger LOGGER = Logging.getLogger(EFeatureDataStoreFactory.class);
    
    /**
     * Cached {@link EFeatureContextFactory} instance
     */
    protected static EFeatureContextFactory eContextFactory;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * Public "no arguments" constructor called by Factory Service Provider
     * (SPI) based on entry in META-INF/services/org.geotools.data.DataStoreFactorySpi
     */
    public EFeatureDataStoreFactory() { /*NOP*/ }

    // ----------------------------------------------------- 
    //  EFeatureDataStoreFactory methods
    // -----------------------------------------------------
    
    @Override
    public String getDisplayName() {
        return "EGeometryFeature Data Store";
    }

    @Override
    public String getDescription() {
        return "Allows access to EMF EObject instances containing JTS Geometry data";
    }
    
    @Override
    public Param[] getParametersInfo() {
        return new Param[] { EFEATURE_CONTEXT_ID_PARAM, EDITING_DOMAIN_ID_PARAM,
                EPACKAGE_NS_URI_PARAM, ERESOURCE_URI_PARAM, EFOLDERS_QUERY_PARAM};
    }

    /**
     * Check to see if {@link EFeatureDataStore}s can be created.
     * <p>
     * This factory is only available if at least one 
     * {@link EFeatureContext} instance is registered.
     * <p>
     * 
     * @return <code>true</code> if and only if this factory is 
     *         able to create {@link EFeatureDataStore}s.
     * 
     */
    @Override
    public boolean isAvailable() {
        return eGetContextFactory().isAvailable();
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        // TODO: Add hints for CRS etc.
        //
        return Collections.emptyMap();
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {
        return canProcess(DIALECT,params);
    }

    /**
     * Construct a live {@link EFeatureDataStore} instance from given parameters.
     * 
     * @param params - the full set of information needed to construct a live {@link EFeatureDataStore}
     *        instance
     * 
     * @return a new {@link EFeatureDataStore} instance, this may be <code>null</code> if the required
     *         resource was not found or if insufficient parameters were given. Note that
     *         canProcess() should have returned false if the problem is to do with insufficient
     *         parameters.
     * 
     * @throws IOException if there were any problems setting up (creating or connecting) the
     *         {@link EFeatureContext}, {@link EditingDomain} or {@link EObject} container
     *         instances.
     * @throws IllegalArgumentException if resolved {@link EObject} container and
     *         {@link EFeaturePackageInfo} does not match.
     */
    @Override
    public EFeatureDataStore createDataStore(Map<String, Serializable> params) throws IOException,
            IllegalArgumentException {
        //
        // lookup will throw error message for
        // lack of required parameter or wrong data type
        //
        String eContextID = (String) EFEATURE_CONTEXT_ID_PARAM.lookUp(params);
        String eDomainID = (String) EDITING_DOMAIN_ID_PARAM.lookUp(params);
        String eNsURI = (String) EPACKAGE_NS_URI_PARAM.lookUp(params);
        String eURI = (String) ERESOURCE_URI_PARAM.lookUp(params);
        String eFolders = (String) EFOLDERS_QUERY_PARAM.lookUp(params);
        boolean eWritable = toValue((Boolean) EFEATURE_WRITABLE_PARAM.lookUp(params),Boolean.class,true);
        //
        // Try processing parameters first so we can get real IO
        // error message back to the user
        //
        if (!canProcess(params)) {
            throw new IOException("One or more parameters are " +
            		"missing or invalid. See log for more information.");
        }
        //
        // Construct EFeatureStore instance from given parameters
        //
        EFeatureDataStore eDataStore = new EFeatureDataStore(eContextID, eDomainID, eNsURI, eURI, eFolders, eWritable);
        //
        // Set filter factory (
        //
        eDataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        //
        // Set reference to this data store factory
        //
        eDataStore.setDataStoreFactory(this);
        //
        // Finished
        //
        return eDataStore;
    }
    
    /**
     * TODO: Create a new {@link EObject} container and construct a live data {@link EFeatureDataStore}
     * instance on it from given parameters.
     * <p>
     * For now, this method is forwarded to {@link #createDataStore(Map)}.
     * <p>
     * 
     * @param params - the full set of information needed to construct a live {@link EFeatureDataStore}
     *        instance
     * 
     * @return a new {@link EFeatureDataStore} instance, this may be <code>null</code> if the required
     *         resource was not found or if insufficient parameters were given. Note that
     *         canProcess() should have returned false if the problem is to do with insufficient
     *         parameters.
     * 
     * @throws IOException if there were any problems setting up (creating or connecting) the
     *         {@link EFeatureContext}, {@link EditingDomain} or {@link EObject} container
     *         instances.
     * @throws IllegalArgumentException if resolved {@link EObject} container and
     *         {@link EFeaturePackageInfo} does not match.
     */
    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException,
            IllegalArgumentException {
        return createDataStore(params);
    }


    /**
     * Get {@link EFeature} folder names from given {@link EFeatureContext}
     * 
     * @param eContextID - {@link EFeatureContext} instance extension point id
     * @param eNsURI - {@link EPackage} defining {@link EObject}s containing {@link EFeature}s or
     *        {@link EFeature} compatible data.
     * @param eQuery - query used to select which {@link EFeature} folders to include
     * @return an array of {@link EFeature} folder names if found, <code>empty</code> array
     *         otherwise.
     */
    public static String[] getFolderNames(String eContextID, String eNsURI, String eQuery) {
        EFeatureContextInfo eInfo = eGetContextFactory().eStructure(eContextID);
        if (eInfo != null) {
            EFeaturePackageInfo d = eInfo.eGetPackageInfo(eNsURI);
            if (d != null) {
                return d.eGetFolderNames(eQuery);
            }
        }
        return new String[] {};
    }
    
    /**
     * Get current {@link EFeature} {@link EFeatureContext context} {@link EFeatureContextFactory factory}
     */
    public static EFeatureContextFactory eGetContextFactory() {
        if(eContextFactory==null) {
            eContextFactory = EFeatureContextFactory.eDefault();
        }
        return eContextFactory;
    }
    
    /**
     * Set current {@link EFeature} {@link EFeatureContext context} {@link EFeatureContextFactory factory}
     */
    public static void eSetContextFactory(EFeatureContextFactory eFactory) {
        eContextFactory = eFactory;
    }
    

    /**
     * Get {@link EFeaturePackageInfo} instance with given 
     * {@link EFeaturePackageInfo#eNsURI() namespace URL} 
     * from {@link EFeatureContext} with given 
     * {@link EFeatureContext#eContextID() ID}. 
     * </p>
     * 
     * @param eContextID - given context {@link EFeatureContext#eContextID() ID}
     * @param eNsURI - {@link EPackage} defining {@link EObject}s containing {@link EFeature}s or
     *        {@link EFeature} compatible data.
     * @return a {@link EFeaturePackageInfo} if found.
     * @throws IllegalArgumentException If no {@link EFeaturePackageInfo} instance was found.
     */
    public static EFeaturePackageInfo ePackageInfo(String eContextID, String eNsURI) {
        EFeatureContextInfo eContextInfo = eGetContextFactory().eStructure(eContextID);
        return eContextInfo.eGetPackageInfo(eNsURI);
    }
    
    /**
     * Get {@link EditingDomain} instance with given ID 
     * from {@link EFeatureContext} with given {@link EFeatureContext#eContextID() ID}. 
     * </p>
     * @param eContextID - given context {@link EFeatureContext#eContextID() ID}
     * @param eDomainID - given {@link EditingDomain} ID
     * @return a {@link EditingDomain} instance if found.
     * @throws IllegalArgumentException If no {@link EditingDomain} instance was found.
     */
    public static EditingDomain eDomain(String eContextID, String eDomainID) {
        EFeatureContext eContext = eGetContextFactory().eContext(eContextID);
        return eContext.eGetDomain(eDomainID);
    }
    
    /**
     * Get {@link EditingDomain} instance with given ID in the {@link EFeatureContext} 
     * with given {@link EFeatureContext#eContextID() ID}. 
     * </p>
     * @param eContextID - given context {@link EFeatureContext#eContextID() ID}
     * @param eDomainID - given {@link EditingDomain} ID
     * @return a {@link EditingDomain} instance if found.
     * @throws NullPointerException If 'eContext' is <code>null</code>.
     * @throws IllegalArgumentException If no {@link EditingDomain} instance was found.
     */
    public static EditingDomain eDomain(EFeatureContext eContext, String eDomainID) {
        if(eContext==null) {
            throw new NullPointerException("EFeatureContext can not be 'null'");
        }            
        return eContext.eGetDomain(eDomainID);
    }    
    
    /**
     * Check if {@link EFeatureDataStoreFactory} can process given parameters
     * 
     * @param params - {@link EFeatureDataStore} connection parameters
     */
    private static boolean canProcess(EFeatureDialect dialect, Map<String, Serializable> params) {
        if (params != null) {
            
            // lookup will throw error message for
            // lack of required parameter or wrong data type
            //
            String eContextID;
            String eDomainID;
            String eNsURI; 
            URI eURI;
            String eFolders;
            try {
                eContextID = (String) EFEATURE_CONTEXT_ID_PARAM.lookUp(params);
                eDomainID = (String) EDITING_DOMAIN_ID_PARAM.lookUp(params);
                eNsURI = (String) EPACKAGE_NS_URI_PARAM.lookUp(params);
                eURI = URI.createURI((String) ERESOURCE_URI_PARAM.lookUp(params));
                eFolders = (String) EFOLDERS_QUERY_PARAM.lookUp(params);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
                return false;
            }            
            //
            // Check if required all exist
            //
            if (params.containsKey(EFEATURE_CONTEXT_ID) 
                    && params.containsKey(EDITING_DOMAIN_ID)
                    && params.containsKey(ERESOURCE_URI)) {
                EditingDomain eDomain = eDomain(eContextID, eDomainID);
                if (eDomain != null) {
                    if(params.containsKey(EFOLDERS_QUERY)) {
                        if( !(eFolders==null || eFolders.length()==0)) {                        
                            for (String eFolder : dialect.toFolderQueries(eFolders)) {
                                EFeaturePackageInfo eInfo = ePackageInfo(eContextID,eNsURI);
                                if (eInfo == null)
                                    return false;
                                String eURIFragment = dialect.getFolderFragment(eFolder);
                                if (!(eURIFragment == null || eURIFragment.length() == 0)) {
                                    URI uri = eURI.appendFragment(eURIFragment);
                                    if (eDomain.getResourceSet().getEObject(uri, true) == null)
                                        return false;
                                }
        
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static <T> T toValue(Object v, Class<T> type, T d) {
        return (v==null ? d : type.cast(v));
    }

    

}
