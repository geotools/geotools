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
package org.geotools.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.Identifier;

public class ComplexAttributeImpl extends AttributeImpl implements ComplexAttribute {

    public ComplexAttributeImpl(
            Collection<Property> properties, AttributeDescriptor descriptor, Identifier id) {
        super(cloneProperties(properties), descriptor, id);
    }

    public ComplexAttributeImpl(Collection<Property> properties, ComplexType type, Identifier id) {
        this(properties, new AttributeDescriptorImpl(type, type.getName(), 1, 1, true, null), id);
    }

    public ComplexType getType() {
        return (ComplexType) super.getType();
    }

    public Collection<? extends Property> getValue() {
        return FeatureImplUtils.unmodifiable((Collection) super.getValue());
    }

    public Collection<Property> getProperties() {
        return FeatureImplUtils.unmodifiable((Collection) super.getValue());
    }

    /**
     * Internal helper method for getting at the properties without wrapping in unmodifiable
     * collection.
     */
    protected Collection properties() {
        return (Collection) super.getValue();
    }

    public Collection<Property> getProperties(Name name) {
        List<Property> matches = new ArrayList<Property>();
        for (Iterator p = getValue().iterator(); p.hasNext(); ) {
            Property property = (Property) p.next();
            if (property.getName().equals(name)) {
                matches.add(property);
            }
        }

        return matches;
    }

    public Collection<Property> getProperties(String name) {
        List<Property> matches = new ArrayList<Property>();
        for (Iterator p = properties().iterator(); p.hasNext(); ) {
            Property property = (Property) p.next();
            if (property.getName().getLocalPart().equals(name)) {
                matches.add(property);
            }
        }

        return matches;
    }

    public Property getProperty(Name name) {
        for (Iterator p = properties().iterator(); p.hasNext(); ) {
            Property property = (Property) p.next();
            if (property.getName().equals(name)) {
                return property;
            }
        }

        return null;
    }

    public Property getProperty(String name) {
        for (Iterator p = getValue().iterator(); p.hasNext(); ) {
            Property property = (Property) p.next();
            if (property.getName().getLocalPart().equals(name)) {
                return property;
            }
        }

        return null;
    }

    public void setValue(Object newValue) throws IllegalArgumentException, IllegalStateException {
        setValue((Collection<Property>) newValue);
    }

    public void setValue(Collection<Property> newValue) {
        super.setValue(cloneProperties(newValue));
    }

    /** helper method to clone the property collection. */
    private static Collection cloneProperties(Collection original) {
        if (original == null) {
            return null;
        }

        Collection clone = null;
        try {
            clone = original.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            clone = new ArrayList();
        }

        clone.addAll(original);
        return clone;
    }
    //    public List/* <Property> */get(Name name) {
    //        // JD: this is a farily lenient check, should we be stricter about
    //        // matching up the namespace
    //        List/* <Property> */childs = new LinkedList/* <Property> */();
    //
    //        for (Iterator itr = this.properties.iterator(); itr.hasNext();) {
    //            Property prop = (Property) itr.next();
    //            PropertyDescriptor node = prop.descriptor();
    //            Name propName = node.getName();
    //			if (name.getNamespaceURI() != null) {
    //                if (propName.equals(name)) {
    //                    childs.add(prop);
    //                }
    //            } else {
    //                // just do a local part compare
    //                String localName = propName.getLocalPart();
    //                if (localName.equals(name.getLocalPart())) {
    //                    childs.add(prop);
    //                }
    //            }
    //
    //        }
    //        return childs;
    //    }
    //
    //    /**
    //     * Represents just enough info to convey the idea of this being a "view"
    //     * into getAttribtues.
    //     */
    //    protected synchronized List/* <AttributeType> */types() {
    //        if (types == null) {
    //            types = createTypesView((List) getValue());
    //        }
    //        return types;
    //    }
    //
    //    /** Factory method so subclasses can optimize */
    //    protected List/* <AttributeType> */createTypesView(
    //            final List/* <Attribute> */source) {
    //        if (source == null)
    //            return Collections.EMPTY_LIST;
    //
    //        return new AbstractList/* <AttributeType> */() {
    //            // @Override
    //            public Object /* AttributeType */get(int index) {
    //                return ((Attribute) source.get(index)).getType();
    //            }
    //
    //            // @Override
    //            public int size() {
    //                return source.size();
    //            }
    //
    //            // @Override
    //            public Object /* AttributeType */remove(int index) {
    //                Attribute removed = (Attribute) source.remove(index);
    //                if (removed != null) {
    //                    return removed.getType();
    //                }
    //                return null;
    //            }
    //
    //            /**
    //             * Unsupported.
    //             * <p>
    //             * We may be able to do this for nilable types, or types that have a
    //             * default value.
    //             * </p>
    //             *
    //             */
    //            // @Override
    //            public void add(int index, Object o) {
    //                throw new UnsupportedOperationException(
    //                        "Cannot add directly to types");
    //            }
    //        };
    //    }
    //
    //    public synchronized List/* <Object> */values() {
    //        if (values == null) {
    //            values = createValuesView((List) getValue());
    //        }
    //        return values;
    //    }
    //
    //    /** Factory method so subclasses can optimize */
    //    protected List/* <Object> */createValuesView(
    //            final List/* <Attribute> */source) {
    //        return new AbstractList/* <Object> */() {
    //            // @Override
    //            public Object get(int index) {
    //                return ((Attribute) source.get(index)).getValue();
    //            }
    //
    //            // @Override
    //            public Object set(int index, Object value) {
    //                Object replaced = ((Attribute) source.get(index)).getValue();
    //                ((Attribute) source.get(index)).setValue(value);
    //                return replaced;
    //            }
    //
    //            // @Override
    //            public int size() {
    //                return source.size();
    //            }
    //
    //            // @Override
    //            public Object /* AttributeType */remove(int index) {
    //                Attribute removed = (Attribute) source.remove(index);
    //                if (removed != null) {
    //                    return removed.getValue();
    //                }
    //                return null;
    //            }
    //
    //            /**
    //             * Unsupported, we can support this for flat schema.
    //             * <p>
    //             * We may be able to do this after walking the schema and figuring
    //             * out that there is only one binding for the provided object.
    //             * </p>
    //             *
    //             */
    //            // @Override
    //            public void add(int index, Object value) {
    //                throw new UnsupportedOperationException(
    //                        "Cannot add directly to values");
    //            }
    //        };
    //    }
    //
    //    public void setValue(Object newValue) {
    //
    //        if (newValue == null) {
    //            properties.clear();
    //        } else {
    //            properties = new ArrayList( (Collection) newValue );
    //        }
    //
    //        // reset "views"
    //        attributes = null;
    //        associations = null;
    //        types = null;
    //        values = null;
    //    }
    //
    //    protected Object get(AttributeType type) {
    //        if (type == null) {
    //            throw new NullPointerException("type");
    //        }
    //
    //        // JD: Is this crazy or is it just me? This method returns an object
    //        // in one case, and collection in the other?
    //        ComplexType ctype = TYPE;
    //        if (Descriptors.multiple(ctype, type)) {
    //            List/* <Object> */got = new ArrayList/* <Object> */();
    //            for (Iterator itr = properties.iterator(); itr.hasNext();) {
    //                Attribute attribute = (Attribute) itr.next();
    //                if (attribute.getType().equals(type)) {
    //                    got.add(attribute.getValue());
    //                }
    //            }
    //            return got;
    //        } else {
    //            for (Iterator itr = properties.iterator(); itr.hasNext();) {
    //                Attribute attribute = (Attribute) itr.next();
    //                if (attribute.getType().equals(type)) {
    //                    return attribute.getValue();
    //                }
    //            }
    //            return null;
    //        }
    //    }
    //
    //    public boolean equals(Object o) {
    //        if (!(o instanceof ComplexAttributeImpl)) {
    //            return false;
    //        }
    //        ComplexAttributeImpl c = (ComplexAttributeImpl) o;
    //
    //        if (!Utilities.equals(ID, c.ID))
    //            return false;
    //
    //        if (!Utilities.equals(TYPE, c.TYPE))
    //            return false;
    //
    //        if (!Utilities.equals(DESCRIPTOR, c.DESCRIPTOR)) {
    //            return false;
    //        }
    //
    //        return this.properties.equals(c.properties);
    //    }
    //
    //    public int hashCode() {
    //        if (HASHCODE == -1) {
    //            HASHCODE = 23 + (TYPE == null ? 1 : TYPE.hashCode())
    //                    * (DESCRIPTOR == null ? 1 : DESCRIPTOR.hashCode())
    //                    * properties.hashCode() * (ID == null ? 1 : ID.hashCode());
    //        }
    //        return HASHCODE;
    //    }
    //
    //    public String toString() {
    //        StringBuffer sb = new StringBuffer(getClass().getName());
    //        List/* <Attribute> */atts = this.properties;
    //        sb.append("[id=").append(this.ID).append(", name=").append(
    //                DESCRIPTOR != null ? DESCRIPTOR.getName().toString() : "null")
    //                .append(", type=").append(getType().getName()).append('\n');
    //        for (Iterator itr = atts.iterator(); itr.hasNext();) {
    //            Attribute att = (Attribute) itr.next();
    //            sb.append(att);
    //            sb.append('\n');
    //        }
    //        sb.append("]");
    //        return sb.toString();
    //    }
    //
    //    public Object operation(Name arg0, List arg1) {
    //        throw new UnsupportedOperationException("operation not supported yet");
    //    }

}
