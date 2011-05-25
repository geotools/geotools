/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.util.CheckedArrayList;
import org.geotools.util.CheckedHashMap;
import org.geotools.util.CheckedHashSet;

/**
 * Configuration object for a {@link org.geotools.data.complex.AppSchemaDataAccess}.
 * <p>
 * This configuration object contains all the needed elements for a AppSchemaDataAccess to aquire the
 * source and target FeatureTypes, and apply the mappings between attributes to serve community
 * schemas.
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class AppSchemaDataAccessDTO implements Serializable {
    private static final long serialVersionUID = -8649884546130644668L;

    /**
     * Url used as base path to resolve relative file names in {@link #targetSchemasUris}
     */
    private String baseSchemasUrl;

    /** Mapping of prefix/namespace used in the target schema */
    private Map namespaces = Collections.EMPTY_MAP;

    /**
     * List of configuration objects used to aquire the datastores that provides the source
     * FeatureTypes. Source feature types are those internally defined whose Feature instances are
     * converted to features of the target schemas by applying the FeatureTypeMappings.
     */
    private List /* <SourceDataStore> */sourceDataStores = Collections.EMPTY_LIST;

    private Set /* <TypeMapping> */typeMappings = Collections.EMPTY_SET;

    /**
     * List of file names, that may be fully qualified URL's, or paths relative to
     * {@link #baseSchemasUrl}
     */
    private List targetSchemasUris = Collections.EMPTY_LIST;
    
    /**
     * List of the paths of other related types that are mapped separately that shouldn't be visible
     * on their own, thus included in "include" statement
     */
    private List<String> includes = Collections.emptyList();

    private String oasisCatalogUri;

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public List getTargetSchemasUris() {
        return new ArrayList(targetSchemasUris);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param targetSchemasUris
     *                DOCUMENT ME!
     */
    public void setTargetSchemasUris(List targetSchemasUris) {
        this.targetSchemasUris = new CheckedArrayList(String.class);

        if (targetSchemasUris != null) {
            this.targetSchemasUris.addAll(targetSchemasUris);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param nameSpaces
     *                DOCUMENT ME!
     */
    public void setNamespaces(Map nameSpaces) {
        if (nameSpaces == null) {
            this.namespaces = Collections.EMPTY_MAP;
        } else {
            this.namespaces = new CheckedHashMap(String.class, String.class);
            this.namespaces.putAll(nameSpaces);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public Map getNamespaces() {
        return new HashMap(namespaces);
    }

    /**
     * Set the path of other related types that are mapped separately
     * @param includes
     */
    public void setIncludedTypes(ArrayList<String> includes) {
        if (includes != null) {
            this.includes = new CheckedArrayList(String.class);
            this.includes.addAll(includes);
        }       
    }
    
    /**
     * Return the list of paths of related types that are mapped separately
     * @return
     */
    public List<String> getIncludes() {
        return includes;
    }
    
    /**
     * DOCUMENT ME!
     * 
     * @param dataStores
     *                DOCUMENT ME!
     */
    public void setSourceDataStores(List /* <SourceDataStore> */dataStores) {
        if (dataStores == null) {
            this.sourceDataStores = Collections.EMPTY_LIST;
        } else {
            this.sourceDataStores = new CheckedArrayList(SourceDataStore.class);
            this.sourceDataStores.addAll(dataStores);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public List getSourceDataStores() {
        return new ArrayList(sourceDataStores);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param typeMappings
     *                DOCUMENT ME!
     */
    public void setTypeMappings(Set typeMappings) {
        this.typeMappings = new CheckedHashSet(TypeMapping.class);

        if (typeMappings != null) {
            this.typeMappings.addAll(typeMappings);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public Set getTypeMappings() {
        return new HashSet(typeMappings);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return a non null URL for the base location of the resource files in order to serve as the
     *         base to resolve relative configuration paths.
     */
    public String getBaseSchemasUrl() {
        return baseSchemasUrl;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param baseSchemasUrl
     *                URL of a resource which's going to be taken as the base location to resolve
     *                configuration path elements expressed as relative paths.
     */
    public void setBaseSchemasUrl(final String baseSchemasUrl) {
        this.baseSchemasUrl = baseSchemasUrl;
    }

    public String getCatalog() {
        return oasisCatalogUri;
    }

    public void setCatalog(final String oasisCatalogUri) {
        this.oasisCatalogUri = oasisCatalogUri;
    }
}
