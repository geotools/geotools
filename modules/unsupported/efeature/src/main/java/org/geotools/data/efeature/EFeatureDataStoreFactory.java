package org.geotools.data.efeature;

import static org.geotools.data.efeature.EFeatureDialect.EDITING_DOMAIN_ID;
import static org.geotools.data.efeature.EFeatureDialect.EFEATURE_CONTEXT_ID;
import static org.geotools.data.efeature.EFeatureDialect.EFOLDERS_QUERY;
import static org.geotools.data.efeature.EFeatureDialect.EPACKAGE_NS_URI;
import static org.geotools.data.efeature.EFeatureDialect.ERESOURCE_URI;

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
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A {@link EFeatureDataStore} {@link DataStoreFactorySpi} class implementation.
 * <p>
 * This class adds {@link EFeatureDataStore} instance creation capabilities to the GeoTools framework.
 * </p>
 * 
 * @author kengu
 * 
 */
public class EFeatureDataStoreFactory implements DataStoreFactorySpi {
    
    /** 
     * Static logger for all {@link EFeatureDataStoreFactory} instances 
     */
    private static final Logger LOGGER = Logging.getLogger(EFeatureDataStoreFactory.class);
    
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
    
    public String getDisplayName() {
        return "EGeometryFeature Data Store";
    }

    public String getDescription() {
        return "Allows access to EMF EObject instances containing JTS Geometry data";
    }
    
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
    public boolean isAvailable() {
        return eGetContextFactory().isAvailable();
    }

    public Map<Key, ?> getImplementationHints() {
        // TODO: Add hints for CRS etc.
        //
        return Collections.emptyMap();
    }

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
     *         {@link EFeatureDataStoreInfo} does not match.
     */
    public EFeatureDataStore createDataStore(Map<String, Serializable> params) throws IOException,
            IllegalArgumentException {

        // lookup will throw error message for
        // lack of required parameter or wrong data type
        //
        String eContextID = (String) EFEATURE_CONTEXT_ID_PARAM.lookUp(params);
        String eDomainID = (String) EDITING_DOMAIN_ID_PARAM.lookUp(params);
        String eNsURI = (String) EPACKAGE_NS_URI_PARAM.lookUp(params);
        String eURI = (String) ERESOURCE_URI_PARAM.lookUp(params);
        String eFolders = (String) EFOLDERS_QUERY_PARAM.lookUp(params);

        // Try processing parameters first so we can get real IO
        // error message back to the user
        //
        if (!canProcess(params)) {
            throw new IOException("One or more parameters are " +
            		"missing or invalid. See log for more information.");
        }

        // Construct EFeatureStore instance from given parameters
        //
        return new EFeatureDataStore(eContextID, eDomainID, eNsURI, eURI, eFolders);
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
     *         {@link EFeatureDataStoreInfo} does not match.
     */
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException,
            IllegalArgumentException {
        return createDataStore(params);
    }


    /**
     * Get {@link EFeature} folder names from given {@link EFeatureContext}
     * 
     * @param eContextID - {@link EFeatureContext} instance extension point id
     * @param eDomainID - {@link EFeatureContext} instance extension point id
     * @param eNsURI - {@link EPackage} defining {@link EObject}s containing {@link EFeature}s or
     *        {@link EFeature} compatible data.
     * @param eQuery - query used to select which {@link EFeature} folders to include
     * @return an array of {@link EFeature} folder names if found, <code>empty</code> array
     *         otherwise.
     */
    public static String[] getFolderNames(String eContextID, String eDomainID, String eNsURI,
            String eQuery) {
        EFeatureContextInfo eInfo = eGetContextFactory().eStructure(eContextID);
        if (eInfo != null) {
            EFeatureDataStoreInfo d = eInfo.eGetDataStoreInfo(eDomainID, eNsURI);
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
     * Get {@link EFeatureDataStoreInfo} instance with given {@link EFeatureDomainInfo#eDomainID() name space URL} 
     * from {@link EFeatureContext} with given {@link EFeatureContext#eContextID() ID}. 
     * </p>
     * 
     * @param eContextID - given context {@link EFeatureContext#eContextID() ID}
     * @param eDomainID - given editing domain {@link EFeatureDomainInfo#eDomainID() ID}
     * @param eNsURI - {@link EPackage} defining {@link EObject}s containing {@link EFeature}s or
     *        {@link EFeature} compatible data.
     * @return a {@link EFeatureDataStoreInfo} if found.
     * @throws IllegalArgumentException If no {@link EditingDomain} instance was found.
     */
    public static EFeatureDataStoreInfo eDataStoreInfo(String eContextID, String eDomainID, String eNsURI) {
        EFeatureContextInfo eContextInfo = eGetContextFactory().eStructure(eContextID);
        EFeatureDataStoreInfo eStoreInfo = (eContextInfo != null ? eContextInfo.eGetDataStoreInfo(eDomainID, eNsURI) : null);
        if(eStoreInfo!=null)
        {
            return eStoreInfo;
        }
        // Not found, throw exception
        //
        throw new IllegalArgumentException("No EditingDomain cached for " +
                "eContextID: " + eContextID + ", " +
                "eDomainID: " + eDomainID);
    }
    
    /**
     * Get {@link EditingDomain} instance with given {@link EFeatureDomainInfo#eDomainID() ID} 
     * from {@link EFeatureContext} with given {@link EFeatureContext#eContextID() ID}. 
     * </p>
     * @param eContextID - given context {@link EFeatureContext#eContextID() ID}
     * @param eDomainID - given editing domain {@link EFeatureDomainInfo#eDomainID() ID}
     * @return a {@link EditingDomain} instance if found.
     * @throws IllegalArgumentException If no {@link EditingDomain} instance was found.
     */
    public static EditingDomain eDomain(String eContextID, String eDomainID) {
        EditingDomain eDomain = null;
        EFeatureContext eContext = eGetContextFactory().eContext(eContextID);
        eDomain = (eContext != null ? eContext.eGetDomain(eDomainID) : null);
        if(eDomain!=null)
        {
            return eDomain;
        }
        // Not found, throw exception
        //
        throw new IllegalArgumentException("No EditingDomain cached for " +
                "eContextID: " + eContextID + ", " +
                "eDomainID: " + eDomainID);
            
    }
    
    /**
     * Get {@link EditingDomain} instance with given {@link EFeatureDomainInfo#eDomainID() ID} 
     * from {@link EFeatureContext} with given {@link EFeatureContext#eContextID() ID}. 
     * </p>
     * @param eContextID - given context {@link EFeatureContext#eContextID() ID}
     * @param eDomainID - given editing domain {@link EFeatureDomainInfo#eDomainID() ID}
     * @return a {@link EditingDomain} instance if found.
     * @throws NullPointerException If 'eContext' is <code>null</code>.
     * @throws IllegalArgumentException If no {@link EditingDomain} instance was found.
     */
    public static EditingDomain getEditingDomain(EFeatureContext eContext, String eDomainID) {
        if(eContext==null) {
            throw new NullPointerException("EFeatureContext can not be 'null'");
        }            
        EditingDomain eDomain = null;
        eDomain = (eContext != null ? eContext.eGetDomain(eDomainID) : null);
        if(eDomain!=null)
        {
            return eDomain;
        }
        // Not found, throw exception
        //
        throw new IllegalArgumentException("No EditingDomain cached for " +
                "eContextID: " + eContext.eContextID() + ", " +
                "eDomainID: " + eDomainID);
            
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
                                EFeatureDataStoreInfo info = eDataStoreInfo(eContextID,eDomainID, eNsURI);
                                if (info == null)
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
    

}
