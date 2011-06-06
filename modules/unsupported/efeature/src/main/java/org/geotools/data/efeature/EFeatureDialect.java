package org.geotools.data.efeature;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.opengis.feature.simple.SimpleFeature;

public class EFeatureDialect {

    /**
     * {@link EFeatureContext} instance ID.
     * <p>
     */
    public static final String EFEATURE_CONTEXT_ID = "eContextID";

    /**
     * Extension point id to an {@link EditingDomain} instance.
     * <p>
     * All readers and writers are forces to use given {@link EditingDomain} instance for read/write
     * EMF model access.
     * <p>
     */
    public static final String EDITING_DOMAIN_ID = "eDomainID";

    /**
     * The name space URI of the {@link EPackage} which the {@link EClass} with name
     * {@link #EFOLDERS_QUERY} belongs.
     * <p>
     */
    public static final String EPACKAGE_NS_URI = "eNsURI";

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
    public static final String ERESOURCE_URI = "eURI";
    
    /**
     * A query that defines which {@link EFeature} folders to include in a {@link EFeatureDataStore}.
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
    public static final String EFOLDERS_QUERY = "eFolders";

    /**
     * Create connection parameter {@link Map} from required parameter values
     * 
     * @param eContextID - id of {@link EFeatureContext} instance
     * @param eDomainID - id of {@link EditingDomain} instance
     * @param eNsURI TODO
     * @param eURI - {@link URI} to {@link Resource} containing {@link SimpleFeature} data
     * @param eFolders - {@link EFeature} folders query
     * @return a {@link Map} of connection parameters
     */
    public Map<String, Serializable> toParams(String eContextID, String eDomainID, 
            String eNsURI, String eURI, String eFolders) {
        Map<String, Serializable> params = new HashMap<String, Serializable>(1);
        params.put(EFEATURE_CONTEXT_ID, eContextID);
        params.put(EDITING_DOMAIN_ID, eDomainID);
        params.put(EPACKAGE_NS_URI, eNsURI);        
        params.put(ERESOURCE_URI, eURI);
        params.put(EFOLDERS_QUERY, eFolders);
        return params;
    }

    /**
     * Parse {@link #EFOLDERS_QUERY} parameter into folders
     */
    public String[] toFolderQueries(String eFolders) {
        return (isSane(eFolders) ? eFolders.split("+") : null);
    }

    /**
     * Get folder eName element in {@link #EFOLDERS_QUERY} specification
     */
    public String getFolderName(String eFolder) {
        int i = eFolder.indexOf(':');
        if (i == -1)
            i = eFolder.indexOf('$');
        return i == -1 ? eFolder : eFolder.substring(0, i);
    }

    /**
     * Get folder eQuery element in {@link #EFOLDERS_QUERY} specification
     */
    public String getFolderQuery(String eFolder) {
        int i = eFolder.indexOf(':');
        return i == -1 ? null : eFolder.substring(i + 1);
    }

    /**
     * Get folder eFragment element in {@link #EFOLDERS_QUERY} specification
     */
    public String getFolderFragment(String eFolder) {
        int i = eFolder.indexOf('$');
        return i == -1 ? null : eFolder.substring(i + 1);
    }

    /**
     * Concatenate folders into a {@link #EFOLDERS_QUERY} syntax
     */
    public String toFoldersQuery(String[] eFolders) {
        StringBuilder builder = new StringBuilder();
        for (String it : eFolders) {
            if (builder.length() > 0) {
                builder.append("+");
            }
            builder.append(it);
        }
        return builder.toString();
    }

    /**
     * Get folder query specification
     * 
     * @param eName - name of {@link EFeature} folder
     * @param eQuery - {@link EFeature} query
     * @param eFragment - {@link EFeature} {@link EObject parent} fragment
     * @return a {@link #EFOLDERS_QUERY} folder query specification
     */
    public String toFolderQuery(String eName, String eQuery, String eFragment) {
        String eFolder = eName;
        if (isSane(eQuery)) {
            eFolder += ":" + eQuery;
        } else if (isSane(eFragment)) {
            eFolder += "$" + eFragment;
        }
        return eFolder;
    }

    public boolean isSane(String spec) {
        return !(spec == null || spec.length() == 0);
    }

}
