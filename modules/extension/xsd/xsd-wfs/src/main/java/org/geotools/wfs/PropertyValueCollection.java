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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.collection.BaseFeatureCollection;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xs.XS;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;

/**
 * Wrapping feature collection used by GetPropertyValue operation.
 * <p>
 * This feature collection pulls only the specified property out of the delegate feature collection.
 * </p>
 * 
 * @author Justin Deoliveira, OpenGeo
 * 
 */
public class PropertyValueCollection extends BaseFeatureCollection {

    static FeatureTypeFactory typeFactory = new FeatureTypeFactoryImpl();

    static FeatureFactory factory = CommonFactoryFinder.getFeatureFactory(null);

    AttributeDescriptor descriptor;

    List<Schema> typeMappingProfiles = new ArrayList<Schema>();

    private SimpleFeatureCollection delegate;

    public PropertyValueCollection(SimpleFeatureCollection delegate, AttributeDescriptor descriptor) {
        super(null); // we will create the schema later
        this.delegate = delegate;
        this.descriptor = descriptor;

        this.typeMappingProfiles.add(XS.getInstance().getTypeMappingProfile());
        this.typeMappingProfiles.add(GML.getInstance().getTypeMappingProfile());

        // create a new descriptor based on the xml type
        Class<?> binding = descriptor.getType().getBinding();
        AttributeType xmlType = findType(binding);
        if (xmlType == null) {
            throw new RuntimeException("Unable to map attribute " + descriptor.getName()
                    + " to xml type");
        }
        // because simple features don't carry around their namespace, create a descriptor name
        // that actually used the feature type schema namespace
        SimpleFeatureType origionalSchema = delegate.getSchema();

        Name name = new NameImpl(origionalSchema.getName().getNamespaceURI(),
                descriptor.getLocalName());
        AttributeDescriptor newDescriptor = typeFactory.createAttributeDescriptor(xmlType, name,
                descriptor.getMinOccurs(), descriptor.getMaxOccurs(), descriptor.isNillable(),
                descriptor.getDefaultValue());

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder(typeFactory);
        builder.setName(origionalSchema.getName());
        builder.add(newDescriptor);

        this.schema = builder.buildFeatureType();
    }

    /**
     * Look in the {@link #typeMappingProfiles} for the official AttributeType for the provided java class.
     * 
     * @param binding Java class being represented
     * @return AttributeType from {@link #typeMappingProfiles} {@link XS} and {@link GML}
     */
    AttributeType findType(Class<?> binding) {
        for (Schema schema : typeMappingProfiles) {
            for (Map.Entry<Name, AttributeType> e : schema.entrySet()) {
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

    @Override
    public SimpleFeatureIterator features() {
        return new PropertyValueIterator(delegate.features());
    }

    class PropertyValueIterator implements SimpleFeatureIterator {
        SimpleFeatureIterator it;

        SimpleFeature next;

        SimpleFeatureBuilder builder;

        PropertyValueIterator(SimpleFeatureIterator featureIterator) {
            this.it = featureIterator;
            this.builder = new SimpleFeatureBuilder(schema);
        }

        @Override
        public boolean hasNext() {
            if (next == null) {
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    if (f.getProperty(descriptor.getName()).getValue() != null) {
                        next = f;
                        break;
                    }
                }
            }
            return next != null;
        }

        @Override
        public SimpleFeature next() {
            // Object value = next.getProperty(descriptor.getName()).getValue();
            // return (F) factory.createAttribute(value, newDescriptor, null);

            Object value = next.getAttribute(descriptor.getName());
            String fid = next.getID();

            next = null;

            builder.add(value);
            SimpleFeature newFeature = builder.buildFeature(fid);
            return newFeature;
        }

        @Override
        public void close() {
            if (it != null) {
                it.close();
            }
            it = null;
        }
    }

}
