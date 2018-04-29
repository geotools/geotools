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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xs.XS;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;
import org.opengis.filter.expression.PropertyName;

/**
 * Wrapping feature collection used by GetPropertyValue operation.
 *
 * <p>This feature collection pulls only the specified property out of the delegate feature
 * collection.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class PropertyValueCollection extends AbstractCollection<Attribute> {

    static FeatureTypeFactory typeFactory = new FeatureTypeFactoryImpl();

    static FeatureFactory factory = CommonFactoryFinder.getFeatureFactory(null);

    static final Name GML_IDENTIFIER = new NameImpl(GML.NAMESPACE, "identifier");

    static final AttributeDescriptor ID_DESCRIPTOR;

    static {
        AttributeTypeBuilder ab = new AttributeTypeBuilder(typeFactory);
        ab.setName("identifier");
        ab.setBinding(String.class);
        ID_DESCRIPTOR = ab.buildDescriptor("identifier");
    }

    FeatureCollection delegate;

    AttributeDescriptor descriptor;

    List<Schema> typeMappingProfiles = new ArrayList();

    PropertyName propertyName;

    public PropertyValueCollection(
            FeatureCollection delegate, AttributeDescriptor descriptor, PropertyName propName) {
        this.delegate = delegate;
        this.descriptor = descriptor;
        this.typeMappingProfiles.add(XS.getInstance().getTypeMappingProfile());
        this.typeMappingProfiles.add(GML.getInstance().getTypeMappingProfile());
        this.propertyName = propName;

        // fallback for gml:id "property"
        if (descriptor == null) {
            this.descriptor = ID_DESCRIPTOR;
        }
    }

    @Override
    public int size() {
        // JD: this is a lie, since we skip over features without the attribute
        return delegate.size();
    }

    @Override
    public Iterator iterator() {
        return new PropertyValueIterator(delegate.features());
    }

    class PropertyValueIterator implements Iterator {
        FeatureIterator it;

        Feature next;

        Queue values = new LinkedList();

        PropertyValueIterator(FeatureIterator it) {
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            if (it == null) {
                return false;
            }
            if (values.isEmpty()) {
                Object value = null;
                while (it.hasNext()) {
                    Feature f = (Feature) it.next();
                    value = propertyName.evaluate(f);
                    if (value != null
                            && !(value instanceof Collection && ((Collection) value).isEmpty())) {
                        next = f;
                        break;
                    }
                }
                if (value != null) {
                    if (value instanceof Collection) {
                        values.addAll((Collection) value);
                    } else {
                        values.add(value);
                    }
                }
            }

            if (!values.isEmpty()) {
                return true;
            }

            // close the iterator
            it.close();
            it = null;
            return false;
        }

        @Override
        public Object next() {
            Object value = values.remove();

            // create a new descriptor based on teh xml type
            AttributeType xmlType = findType(descriptor.getType().getBinding());
            if (xmlType == null) {
                throw new RuntimeException(
                        "Unable to map attribute " + descriptor.getName() + " to xml type");
            }

            // because simple features don't carry around their namespace, create a descriptor name
            // that actually used the feature type schema namespace
            Name name;
            if (descriptor == ID_DESCRIPTOR) {
                name = GML_IDENTIFIER;
            } else {
                name =
                        new NameImpl(
                                next.getType().getName().getNamespaceURI(),
                                descriptor.getLocalName());
            }
            AttributeDescriptor newDescriptor =
                    typeFactory.createAttributeDescriptor(
                            xmlType,
                            name,
                            descriptor.getMinOccurs(),
                            descriptor.getMaxOccurs(),
                            descriptor.isNillable(),
                            descriptor.getDefaultValue());

            if (next instanceof SimpleFeature) {
                return factory.createAttribute(value, newDescriptor, null);
            } else {
                return factory.createComplexAttribute(
                        Collections.<Property>singletonList((Property) value), newDescriptor, null);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        AttributeType findType(Class binding) {
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
    }
}
