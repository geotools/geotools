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
package org.geotools.data.efeature.tests.unit;

import static org.geotools.data.efeature.EFeatureDialect.EFEATURE_CONTEXT_ID;
import static org.geotools.data.efeature.EFeatureDialect.EDITING_DOMAIN_ID;
import static org.geotools.data.efeature.EFeatureDialect.EPACKAGE_NS_URI;
import static org.geotools.data.efeature.EFeatureDialect.ERESOURCE_URI;
import static org.geotools.data.efeature.EFeatureDialect.EFOLDERS_QUERY;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.efeature.EFeatureDataStoreFactory;
import org.geotools.data.efeature.tests.impl.EFeatureTestsContextHelper;

/**
 * @author kengu
 *
 */
public class ParameterInfoTestData {
    
    /**
     * @see EFeatureDataStoreFactory#EFEATURE_CONTEXT_ID_PARAM
     */
    public static final Param EFEATURE_CONTEXT_ID_PARAM = new Param(
            EFEATURE_CONTEXT_ID, String.class, 
            "Extension point id to an EFeatyreRegistry instance",
            true, EFeatureTestsContextHelper.eCONTEXT_ID);

    /**
     * @see EFeatureDataStoreFactory#EDITING_DOMAIN_ID_PARAM
     */
    public static final Param EDITING_DOMAIN_ID_PARAM = new Param(
            EDITING_DOMAIN_ID, String.class,
            "Extension point id to an EditingDomain instance",
            true, EFeatureTestsContextHelper.eDOMAIN_ID);

    /**
     * @see EFeatureDataStoreFactory#EPACKAGE_NS_URI_PARAM
     */
    public static final Param EPACKAGE_NS_URI_PARAM = new Param(
            EPACKAGE_NS_URI, String.class,
            "The namespace URI of the EPackage which "
            + "defines EObjects containing EFeatures or EFeature compatible data", 
            true, EFeatureTestsContextHelper.eNS_URI);

    /**
     * @see EFeatureDataStoreFactory#ERESOURCE_URI_PARAM
     */
    public static final Param ERESOURCE_URI_PARAM = new Param(
            ERESOURCE_URI, String.class,
            "URI to ENF Resource instance containing EFeatures or "
            + "EFeature compatible data managed by the editing domain", true);

    /**
     * @see EFeatureDataStoreFactory#EFOLDERS_QUERY_PARAM
     */
    public static final Param EFOLDERS_QUERY_PARAM = new Param(
            EFOLDERS_QUERY, String.class,
            "A query that defines which EFeature folders to include in a EFeatureStore.", 
            false);
    
    public Param[] getParametersInfo() {
        return new Param[] { EFEATURE_CONTEXT_ID_PARAM, EDITING_DOMAIN_ID_PARAM,
                EPACKAGE_NS_URI_PARAM, ERESOURCE_URI_PARAM, EFOLDERS_QUERY_PARAM};
    } 
    
    public Map<String, Serializable> createParams(String eURI, String eFolders) {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        for(Param it : getParametersInfo())
        {
            if(it.sample!=null) {
                params.put(it.key,(Serializable)it.sample);
            }
        }
        if(!(eURI==null || eURI.length()==0)) 
            params.put(ERESOURCE_URI_PARAM.key,eURI);
        if(!(eFolders==null || eFolders.length()==0)) 
            params.put(EFOLDERS_QUERY_PARAM.key,eFolders);
        return params;
    }
}
