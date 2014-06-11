/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;

import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.xml.gml.FCBuffer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

public class Gml2GetFeatureResponseParser implements GetFeatureParser {

    
    public Gml2GetFeatureResponseParser(final InputStream getFeatureResponseStream,
            final SimpleFeatureType targetType, QName featureDescriptorName) throws IOException{
     
    }
    
    @Override
    public int getNumberOfFeatures() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public SimpleFeature parse() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public FeatureType getFeatureType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        // TODO Auto-generated method stub
        
    }

}
