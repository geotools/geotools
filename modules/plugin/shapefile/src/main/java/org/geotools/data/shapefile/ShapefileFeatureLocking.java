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
package org.geotools.data.shapefile;

import java.io.IOException;
import java.util.Set;

import org.geotools.data.AbstractFeatureLocking;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureListener;
import org.geotools.data.Query;
import org.geotools.data.ResourceInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;

public class ShapefileFeatureLocking extends AbstractFeatureLocking {
    /**
     * 
     */
    private final ShapefileDataStore shapefile;
    private final SimpleFeatureType featureType;
    public ShapefileFeatureLocking( ShapefileDataStore shapefileDataStore, Set hints, SimpleFeatureType featureType ) {
        super(hints);
        shapefile = shapefileDataStore;
        this.featureType = featureType;
    }
    public DataStore getDataStore() {
        return shapefile;
    }
    public void addFeatureListener(FeatureListener listener) {
        shapefile.listenerManager.addFeatureListener(this, listener);
    }
    public void removeFeatureListener(FeatureListener listener) {
        shapefile.listenerManager.removeFeatureListener(this, listener);
    }
    public SimpleFeatureType getSchema() {
        return featureType;
    }
    public ReferencedEnvelope getBounds(Query query)
            throws IOException {
        return shapefile.getBounds(query);
    }
    public ResourceInfo getInfo(){
        return shapefile.getInfo( featureType.getTypeName() );
    }
}
