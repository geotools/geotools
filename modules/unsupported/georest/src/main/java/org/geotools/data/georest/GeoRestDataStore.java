/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.georest;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

/**
 * <p>
 * DataStore that connects to a GeoJson service URL. Requires the actual URL of the service and a
 * comma separated list of type-names (layers).<br/>
 * This DataStore should be able to connect to a rest-like service as defined in the following
 * projects:
 * <ul>
 * <li>Geomajas GeoJson plug-in</li>
 * <li>FeatureServer</li>
 * <li>MapFish server</li>
 * </ul>
 * For a more detailed description, look up the projects documentation.
 * </p>
 * 
 * @author Pieter De Graef, Geosparc
 */
public class GeoRestDataStore extends ContentDataStore {

    private URL url;

    private List<Name> typeNames;

    GeoRestDataStore(Map<String, Serializable> params) throws MalformedURLException {
        url = new URL((String) params.get(GeoRestDataStoreFactory.PARAM_URL));
        String layers = (String) params.get(GeoRestDataStoreFactory.PARAM_LAYERS);
        String[] layerNames = layers.split(",");
        typeNames = new ArrayList<Name>();
        for (int i = 0; i < layerNames.length; i++) {
            typeNames.add(new NameImpl(layerNames[i].trim()));
        }
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        return typeNames;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new GeoRestFeatureSource(entry, null);
    }

    /**
     * @return Returns the base URL for the online GeoJson rest service.
     */
    protected URL getUrl() {
        return url;
    }
}
