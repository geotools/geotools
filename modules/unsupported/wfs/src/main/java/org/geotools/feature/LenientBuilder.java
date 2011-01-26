/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.util.ArrayList;
import java.util.List;

import org.opengis.feature.Attribute;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
/**
 * A build that can be used as a replacement for SimpleFeatureBuilder in order
 * to avoid validation.
 * <p>
 * The normal SimpleFeatureBuilder performs validation (rather than leaving that up
 * to the factory implementation). 
 * <p>
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class LenientBuilder {
    private SimpleFeatureType schema;
    private FeatureFactory factory;
    private Object[] properties;
    
    public LenientBuilder(SimpleFeatureType schmea ){
        this.schema = schmea;
        this.factory = new LenientFeatureFactory();
        reset();
    }
    
    public static SimpleFeature build( SimpleFeatureType ft, Object atts[], String fid ){
        LenientFeatureFactory featureFactory = new LenientFeatureFactory();
        List<Attribute> properties = new ArrayList<Attribute>();
        for( int i=0; i<atts.length;i++){
            Object value = atts[i];
            Attribute property = featureFactory.createAttribute(value, ft.getDescriptor(i), null);
            properties.add(property);            
        }
        return featureFactory.createSimpleFeature(properties, ft, fid );
    }

    /** You can inject another Factory; this builder will still not do validation */
    public void setFeatureFactory(FeatureFactory featureFactory) {
        factory = featureFactory;
    }

    public void addAll(Object[] values) {
        System.arraycopy(values, 0, properties, 0, properties.length);
    }

    public SimpleFeature buildFeature(String fid) {
        return factory.createSimpleFeature( properties, schema, fid );
    }

    public void reset(){
        properties = new Object[schema.getAttributeCount()];
    }

    public static SimpleFeature copy(SimpleFeature f) {
        if( f == null ) return null;
        
        LenientBuilder builder = new LenientBuilder(f.getFeatureType());        
        builder.addAll( f.getAttributes().toArray() );
        return builder.buildFeature(f.getID());
    }
}
