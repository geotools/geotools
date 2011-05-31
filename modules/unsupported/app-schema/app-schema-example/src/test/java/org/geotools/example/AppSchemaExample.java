/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.gml3.GML;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * An example of how to use the app-schema module.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 * 
 * @source $URL$
 */
public class AppSchemaExample {

    public static void main(String[] args) throws Exception {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("dbtype", "app-schema");
        params.put("url", AppSchemaExample.class.getResource("/gsml_MappedFeature.xml").toURI()
                .toString());
        DataAccess<FeatureType, Feature> dataAccess = null;
        try {
            dataAccess = DataAccessFinder.getDataStore(params);
            FeatureSource<FeatureType, Feature> source = dataAccess.getFeatureSource(new NameImpl(
                    "urn:cgi:xmlns:CGI:GeoSciML:2.0", "MappedFeature"));
            FeatureCollection<FeatureType, Feature> features = source.getFeatures();
            FeatureIterator<Feature> iterator = features.features();
            try {
                while (iterator.hasNext()) {
                    Feature f = iterator.next();
                    System.out.println("Feature "
                            + f.getIdentifier().toString()
                            + " has gml:name = "
                            + ((ComplexAttribute) f.getProperty(new NameImpl(GML.name)))
                                    .getProperty("simpleContent").getValue());
                }
            } finally {
                iterator.close();
            }
        } finally {
            if (dataAccess != null) {
                dataAccess.dispose();
            }
        }
    }

}
