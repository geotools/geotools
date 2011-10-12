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
package org.geotools.wfs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xs.XS;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;

/**
 * Wrapping feature collection used by GetPropertyValue operation.
 * <p>
 * This feature collection pulls only the specified property out of the delegate 
 * feature collection. 
 * </p>
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class PropertyValueCollection extends DecoratingFeatureCollection {

    static FeatureTypeFactory typeFactory = new FeatureTypeFactoryImpl();
    static FeatureFactory factory = CommonFactoryFinder.getFeatureFactory(null);
    
    AttributeDescriptor descriptor;
    List<Schema> typeMappingProfiles = new ArrayList();
    
    public PropertyValueCollection(FeatureCollection delegate, AttributeDescriptor descriptor) {
        super(delegate);
        this.descriptor = descriptor;
        this.typeMappingProfiles.add(XS.getInstance().getTypeMappingProfile());
        this.typeMappingProfiles.add(GML.getInstance().getTypeMappingProfile());
    }

    @Override
    public Iterator iterator() {
        return new PropertyValueIterator(delegate.iterator());
    }
    
    @Override
    public void close(Iterator close) {
        delegate.close(((PropertyValueIterator)close).it);
    }
    
    class PropertyValueIterator implements Iterator {
        
        Iterator it;
        Feature next;
        
        PropertyValueIterator(Iterator it) {
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            if (next == null) {
                while(it.hasNext()) {
                    Feature f = (Feature) it.next();
                    if (f.getProperty(descriptor.getName()).getValue() != null) {
                        next = f;
                        break;
                    }
                }
            }
            
            return next != null;
        }

        @Override
        public Object next() {
            //create a new descriptor based on teh xml type
            AttributeType xmlType = findType(descriptor.getType().getBinding());
            if (xmlType == null) {
                throw new RuntimeException("Unable to map attribute " + descriptor.getName() + 
                    " to xml type");
            }
            
            Object value = next.getProperty(descriptor.getName()).getValue();

            //because simple features don't carry around their namespace, create a descritor name
            // that actually used the feature type schema namespace
            Name name = new NameImpl(next.getType().getName().getNamespaceURI(), descriptor.getLocalName());
            AttributeDescriptor newDescriptor = typeFactory.createAttributeDescriptor(xmlType, 
                name, descriptor.getMinOccurs(), descriptor.getMaxOccurs(), 
                descriptor.isNillable(), descriptor.getDefaultValue());
            
            next = null;
            return factory.createAttribute(value, newDescriptor, null);
        }

        @Override
        public void remove() {
            it.remove();
        }
        
        AttributeType findType(Class binding) {
            for (Schema schema : typeMappingProfiles) {
                for (Map.Entry<Name,AttributeType> e : schema.entrySet()) {
                    AttributeType at = e.getValue();
                    if (at.getBinding() != null && at.getBinding().equals(binding)) {
                        return at;
                    }
                }
                
                
                for (AttributeType at : schema.values()) {
                    if (binding.isAssignableFrom(at.getBinding())) {
                        return at;
                    }
                }
            }
            return null;
        }
    }
}
