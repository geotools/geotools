/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureEvent.Type;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

/**
 * Records FeatureEvents and provides a record that we can check.
 *
 * @source $URL$
 */
public class FeatureEventWatcher implements FeatureListener {
    /** Last known type */
    public Type type = null;

    /** last known event source */
    public FeatureSource<? extends FeatureType, ? extends Feature> source;

    /** Total bounds since last reset*/
    public ReferencedEnvelope bounds;

    /** number of events since last reset */
    public int count = 0;

    /** Filter selecting features modified in the last event */
    public Filter filter;
    
    public void changed(FeatureEvent featureEvent) {
        type = featureEvent.getType();
        if( bounds == null ){
            bounds = featureEvent.getBounds();
        }
        else {
            bounds.expandToInclude( featureEvent.getBounds() );
        }
        filter = featureEvent.getFilter();
        source = featureEvent.getFeatureSource();
        count++;
    }
    
    public void reset(){
        type = null;
        bounds = new ReferencedEnvelope();
        source = null;
        count = 0;
    }
}
