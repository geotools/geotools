/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.gen;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Repository;
import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.gen.info.GeneralizationInfosProvider;
import org.geotools.data.gen.info.GeneralizationInfosProviderImpl;

/**
 * @author Christian Mueller
 * 
 * Factory for {@link PreGeneralizedDataStore} objects.
 * 
 * Params
 * 
 * 
 * RepositoryClassName (String,mandatory) Name of a class implementing {@link Repository}
 * 
 * 
 * GeneralizationInfosProviderClassName (String,mandatory) Name of a class implementing
 * {@link GeneralizationInfosProvider}
 * 
 * GeneralizationInfosProviderProviderParam (Object,optional) Parameter object for
 * {@link GeneralizationInfosProvider#getGeneralizationInfos(Object)}
 *
 * @source $URL$
 */

public class PreGeneralizedDataStoreFactory implements DataStoreFactorySpi {

    public static final Param REPOSITORY_CLASS = new Param("RepositoryClassName", String.class,
            "Class name for data store repository implementation", true, DSFinderRepository.class
                    .getName());

    public static final Param GENERALIZATION_INFOS_PROVIDER_CLASS = new Param(
            "GeneralizationInfosProviderClassName", String.class,
            "Class name for GeneralizationInfosProvider implementation", true,
            GeneralizationInfosProviderImpl.class.getName());

    public static final Param GENERALIZATION_INFOS_PROVIDER_PARAM = new Param(
            "GeneralizationInfosProviderParam", String.class,
            "Optional config parameter for GeneralizationInfosProvider implementation", false);

    public static final Param NAMESPACEP = new Param("namespace", URI.class,
            "uri to a the namespace", false); // not required

    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {

        String providerClassName = (String) GENERALIZATION_INFOS_PROVIDER_CLASS.lookUp(params);
        String repositoryClassName = (String) REPOSITORY_CLASS.lookUp(params);

        String providerParam = (String) GENERALIZATION_INFOS_PROVIDER_PARAM.lookUp(params);
        URI namespace = (URI) NAMESPACEP.lookUp(params);

        try {
            Class providerClass = Class.forName(providerClassName);
            GeneralizationInfosProvider provider = (GeneralizationInfosProvider) providerClass
                    .newInstance();
            GeneralizationInfos gInfos = provider.getGeneralizationInfos(providerParam);

            Class repositoryClass = Class.forName(repositoryClassName);
            Repository repository = (Repository) repositoryClass.newInstance();

            return new PreGeneralizedDataStore(gInfos, repository, namespace);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }

    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException();
    }

    public boolean canProcess(Map<String, Serializable> params) {
        String repositoryClass = null, providerClass = null;
        try {
            repositoryClass = (String) REPOSITORY_CLASS.lookUp(params);
            providerClass = (String) GENERALIZATION_INFOS_PROVIDER_CLASS.lookUp(params);
        } catch (IOException ex) {
            return false;
        }

        if (repositoryClass == null || providerClass == null) {
            return false;
        }

        return true;
    }

    public String getDescription() {
        return "Data store supporting generalized geometries";

    }

    public String getDisplayName() {
        return "Generalizing data store";

    }

    public Param[] getParametersInfo() {
        return new Param[] { REPOSITORY_CLASS, GENERALIZATION_INFOS_PROVIDER_CLASS,
                GENERALIZATION_INFOS_PROVIDER_PARAM, NAMESPACEP };
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

}
