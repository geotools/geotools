/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.feature.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.feature.NameImpl;
import org.opengis.feature.Property;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * A replacement for {@link ComplexTypeImpl} with lazy evaluation of descriptors, to support
 * cyclically-defined types. Note that type equality is defined by name, so do not allow different
 * types with the same name to be put in any Collection.
 *
 * <p>Inspired by {@link ComplexTypeImpl}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @see ComplexTypeImpl
 */
public abstract class AbstractLazyComplexTypeImpl extends AbstractLazyAttributeTypeImpl
        implements ComplexType {

    private Collection<PropertyDescriptor> descriptors;

    private Map<Name, PropertyDescriptor> descriptorMap;

    /** Constructor arguments have the same meaning as in {@link ComplexTypeImpl}. */
    public AbstractLazyComplexTypeImpl(
            Name name,
            boolean identified,
            boolean isAbstract,
            List<Filter> restrictions,
            InternationalString description) {
        super(name, Collection.class, identified, isAbstract, restrictions, description);
    }

    /**
     * Subclasses must override this method to return the list of descriptors that define the
     * properties of this type. This method will only be called once at most.
     *
     * <p>If the type has no properties, return either an empty collection or null.
     *
     * @return a collection of descriptors or null if empty
     */
    public abstract Collection<PropertyDescriptor> buildDescriptors();

    /** Check whether descriptors have been built. If not, construct and sanitise them. */
    private void requireDescriptors() {
        if (descriptors == null) {
            Collection<PropertyDescriptor> builtDescriptors = buildDescriptors();
            if (builtDescriptors == null) {
                descriptors = Collections.emptyList();
                descriptorMap = Collections.emptyMap();
            } else {
                Collection<PropertyDescriptor> localDescriptors =
                        new ArrayList<PropertyDescriptor>(builtDescriptors);
                Map<Name, PropertyDescriptor> localDescriptorMap =
                        new HashMap<Name, PropertyDescriptor>();
                for (PropertyDescriptor descriptor : localDescriptors) {
                    localDescriptorMap.put(descriptor.getName(), descriptor);
                }
                descriptors = Collections.unmodifiableCollection(localDescriptors);
                descriptorMap = Collections.unmodifiableMap(localDescriptorMap);
            }
        }
    }

    /** @see org.geotools.feature.type.AbstractLazyAttributeTypeImpl#getBinding() */
    @SuppressWarnings("unchecked")
    @Override
    public Class<Collection<Property>> getBinding() {
        return (Class<Collection<Property>>) super.getBinding();
    }

    /** @see org.opengis.feature.type.ComplexType#getDescriptors() */
    public Collection<PropertyDescriptor> getDescriptors() {
        requireDescriptors();
        return descriptors;
    }

    /** @see org.opengis.feature.type.ComplexType#getDescriptor(org.opengis.feature.type.Name) */
    public PropertyDescriptor getDescriptor(Name name) {
        requireDescriptors();
        return descriptorMap.get(name);
    }

    /** @see org.opengis.feature.type.ComplexType#isInline() */
    public boolean isInline() {
        return false;
    }

    /** @see org.geotools.feature.type.AbstractLazyAttributeTypeImpl#toString() */
    @Override
    public String toString() {
        return "LazyComplexType: " + getName();
    }

    /**
     * The namespace-ignorant version of {@link #getDescriptor(Name)}. Note that we honour the same
     * permissive algorithm as {@link ComplexTypeImpl}: (1) try no-namespace, (2) try
     * container-namespace, (2) search for match ignoring namespace. <b>*Shudder*</b>. Warning: Any
     * code that uses this method instead of {@link #getDescriptor(Name)} is * inherently unsafe.
     *
     * @see org.opengis.feature.type.ComplexType#getDescriptor(java.lang.String)
     */
    public PropertyDescriptor getDescriptor(String name) {
        requireDescriptors();
        PropertyDescriptor result = getDescriptor(new NameImpl(name));
        if (result == null) {
            result = getDescriptor(new NameImpl(getName().getNamespaceURI(), name));
            if (result == null) {
                for (PropertyDescriptor pd : descriptors) {
                    if (pd.getName().getLocalPart().equals(name)) {
                        return pd;
                    }
                }
            }
        }
        return result;
    }
}
