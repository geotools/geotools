/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson.feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.DelegatingHandler;
import org.json.simple.parser.ParseException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FeatureCollectionHandler extends DelegatingHandler<SimpleFeature> 
    implements IFeatureCollectionHandler {

    SimpleFeatureBuilder builder;
    AttributeIO attio;
    
    SimpleFeature feature;
    List stack;
   
    public FeatureCollectionHandler(SimpleFeatureType featureType, AttributeIO attio) {
        if (featureType != null) {
            builder = new SimpleFeatureBuilder(featureType);
        }
       
        if (attio == null) {
            if (featureType != null) {
                attio = new FeatureTypeAttributeIO(featureType);
            }
            else {
                attio = new DefaultAttributeIO();
            }
        }
        
        this.attio = attio;
    }
    
    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("features".equals(key)) {
            delegate = UNINITIALIZED;
            
            return true;
        }
        return super.startObjectEntry(key);
    }
    
    @Override
    public boolean startArray() throws ParseException, IOException {
        if (delegate == UNINITIALIZED) {
            delegate = new FeatureHandler(builder, attio);
            
            //maintain a stack to track when the "features" array ends
            stack = new ArrayList();
            
            return true;
        }

        // are we handling a feature collection? stack is null otherwise
        if(stack != null) {
            stack.add(null);
        }
        return super.startArray();
    }
    
    @Override
    public boolean endArray() throws ParseException, IOException {
        // are we handling a feature collection? stack is null otherwise
        if(stack != null) {
            if (stack.isEmpty()) {
                //end of features array, clear the delegate
                delegate = NULL;
                return true;
            }
            
            stack.remove(0);
        }
        return super.endArray();
    }
    
    @Override
    public boolean endObject() throws ParseException, IOException {
        super.endObject();
        
        if (delegate instanceof FeatureHandler) {
            feature = ((FeatureHandler) delegate).getValue();
            if (feature != null) {
                //check for a null builder, if it is null set it with the feature type
                // from this feature
                if (builder == null) {
                    builder = new SimpleFeatureBuilder(feature.getFeatureType());
                }
                
                ((FeatureHandler)delegate).init();
                //we want to pause at this point
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void endJSON() throws ParseException, IOException {
        delegate = null;
        feature = null;
    }
    
//    public boolean hasMoreFeatures() {
//        return delegate != null;
//    }
    
    @Override
    public SimpleFeature getValue() {
        return feature;
    }
    
}
