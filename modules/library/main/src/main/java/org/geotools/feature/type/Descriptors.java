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
package org.geotools.feature.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;

/**
 * Helper methods for dealing with Descriptor.
 *
 * <p>This methods opperate directly on the interfaces provided by geoapi, no actual classes were
 * harmed in the making of these utility methods.
 *
 * @author Jody Garnett
 * @author Justin Deoliveira
 * @since 2.5
 */
public class Descriptors {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Descriptors.class);

    /**
     * Wraps a list of {@link AttributeType} in {@link AttributeDescriptor}.
     *
     * @param typeList The list of attribute types.
     * @return The list of attribute descriptors.
     * @see #wrapAttributeType(AttributeType)
     */
    public static final List wrapAttributeTypes(List /*<AttributeType>*/ typeList) {
        List descriptors = new ArrayList(typeList.size());
        for (Iterator i = typeList.iterator(); i.hasNext(); ) {
            AttributeType attributeType = (AttributeType) i.next();
            descriptors.add(wrapAttributeType(attributeType));
        }
        return descriptors;
    }

    /**
     * Wraps a {@link AttributeType} in {@link AttributeDescriptor}.
     *
     * @param type The attribute type.
     * @return The attribute descriptor.
     */
    public static final AttributeDescriptor wrapAttributeType(AttributeType type) {
        if (type == null) {
            return null;
        }
        return new AttributeDescriptorImpl(type, type.getName(), 1, 1, true, null);
    }

    /**
     * Returns the attribute descriptor from a list which matches the specified name, or <code>null
     * </code> if no such descriptor is found.
     *
     * @param descriptors The list of {@link AttributeDescriptor}.
     * @param name The name to match.
     * @return The matching attribute descriptor, or <code>null</code>.
     */
    public static final AttributeDescriptor find(List descriptors, Name name) {
        if (name == null) return null;
        for (Iterator i = descriptors.iterator(); i.hasNext(); ) {
            AttributeDescriptor attributeType = (AttributeDescriptor) i.next();
            if (name.equals(attributeType.getType().getName())) {
                return attributeType;
            }
        }
        return null; // no default geometry here?
    }

    // /**
    // * Handle subtyping in a "sensible" manner.
    // * <p>
    // * We explored using the XMLSchema of extention and restriction, and have
    // * instead opted for the traditional Java lanaguage notion of an override.
    // * <p>
    // * The concept of an overrided allows both:
    // * <ul>
    // * <li>extention - completly new attribtues are tacked on the "end" of the
    // * list
    // * <li>restriction - attribute with the same qname are used to specify
    // * additional (or replace) information provided by the parent.
    // * </ol>
    // * Note - even <b>removal</b> ( a complicated (and silly) use of
    // * restriction in XMLSchema) is supported. To remove simply override an
    // * attribute mentioned by the parent with multiplicity 0:0.
    // * </p>
    // *
    // * @return Descriptor resulting by extending the provided schema
    // (collisions
    // * on qname are treated as overrides)
    // */
    // public ComplexType subtype(ComplexType parent, ComplexType extend) {
    // /*
    // * if( schema instanceof AllDescriptor && subtype instanceof
    // * AllDescriptor ){ return subtype( (AllDescriptor) schema,
    // * (AllDescriptor) extend); } else if( schema instanceof
    // * ChoiceDescriptor && extend instanceof ChoiceDescriptor ){ return
    // * subtype( (ChoiceDescriptor) schema, (ChoiceDescriptor) extend); }
    // * else if( schema instanceof OrderedDescriptor && extend instanceof
    // * OrderedDescriptor ){ return subtype( (OrderedDescriptor) schema,
    // * (OrderedDescriptor) extend); } else { List<Descriptor> all = new
    // * ArrayList<Descriptor>(); all.add( schema ); all.add( extend );
    // * return factory.ordered( all, 1, 1 ); }
    // */
    // try {
    // return restriction(parent, extend);
    // }
    // catch(IllegalArgumentException structsDontMatch){
    // return extension(parent, extend);
    // }
    // }
    //
    // public ComplexType subtype(
    // ComplexType parent, Collection/*<AttributeDescriptor>*/ schema
    // ) {
    // try {
    // return restriction(parent, schema);
    // }
    // catch(IllegalArgumentException structsDontMatch){
    // return extension(parent,schema);
    // }
    // }

    /**
     * Restriction only works on exact structure match.
     *
     * <p>This is the way XMLSchema handles it ...
     */
    // @SuppressWarnings("unchecked")
    // public ComplexType restriction(ComplexType parent,ComplexType restrict) {
    //
    // ComplexType type = null;
    //
    // if (
    // parent instanceof ChoiceType && restrict instanceof ChoiceType
    // ) {
    //
    // Set choices = (Set) restriction(
    // ((ChoiceType)parent).getAttributes(),
    // ((ChoiceType)restrict).getAttributes(),
    // new HashSet()
    // );
    //
    // type = xmlFactory.createChoiceType(choices);
    // }
    // else if (parent instanceof SequenceType && restrict instanceof
    // SequenceType) {
    // List sequence = (List) restriction(
    // ((SequenceType)parent).getAttributes(),
    // ((SequenceType)restrict).getAttributes(),
    // new ArrayList()
    // );
    //
    // type = xmlFactory.createSequenceType(sequence);
    // }
    // else if (
    // parent instanceof ComplexType && restrict instanceof ComplexType
    // ){
    // List elements = (List) restriction(
    // ((ComplexType)parent).getAttributes(),
    // ((ComplexType)restrict).getAttributes(),
    // new ArrayList()
    // );
    //
    // ComplexType ct = (ComplexType) restrict;
    // type = xmlFactory.createType(
    // ct.getName(),elements,ct.isIdentified(),ct.isNillable().booleanValue(),
    // ct.getRestrictions(),ct,ct.isAbstract());
    //
    // }
    // else {
    // throw new IllegalArgumentException("Cannot restrict provided schema");
    // }
    //
    // return type;
    //
    // }
    // public ComplexType restriction(ComplexType parent,
    // Collection/*<AttributeDescriptor>*/ schema) {
    // ComplexType type = null;
    //
    // if (parent instanceof ChoiceType) {
    // Set choices = (Set) restriction(
    // ((ChoiceType)parent).getAttributes(),schema,new HashSet()
    // );
    // type = xmlFactory.createChoiceType(choices);
    // }
    // else if (parent instanceof SequenceType) {
    // List sequence = (List) restriction(
    // ((SequenceType)parent).getAttributes(), schema, new ArrayList()
    // );
    //
    // type = xmlFactory.createSequenceType(sequence);
    // }
    // else if (parent instanceof ComplexType){
    // List elements = (List) restriction(
    // ((ComplexType)parent).getAttributes(),schema, new ArrayList()
    // );
    //
    // //duplicate parent type with new schema
    // //JD: this is a bit of a hack, creating type manually (ie wihtout
    // // factory, because we have constructed the schema manually
    //
    // ComplexType ct = (ComplexType) parent;
    // type = new ComplexTypeImpl(
    // ct.getName(),elements,ct.isIdentified(),
    // ct.isNillable().booleanValue(),ct.getRestrictions(),ct,
    // ct.isAbstract()
    // );
    // type = xmlFactory.createType(
    // ct.getName(),elements,ct.isIdentified(),ct.isNillable().booleanValue(),
    // ct.getRestrictions(),null,ct.isAbstract());
    // }
    // else {
    // throw new IllegalArgumentException("Cannot restrict provided schema");
    // }
    //
    // return type;
    // }
    //
    // public ComplexType extension(
    // ComplexType parent, Collection/*<AttributeDescriptor>*/ schema
    // ) {
    //
    // //create a dummy type for the schema
    // ComplexType type = null;
    //
    // if (parent instanceof ChoiceType) {
    //
    // Set choices = new HashSet();
    // choices.addAll(((ChoiceType)parent).getAttributes());
    // choices.addAll(schema);
    //
    // type = xmlFactory.createChoiceType(choices);
    // }
    // else if (parent instanceof SequenceType) {
    //
    // List sequence = new ArrayList();
    //
    // sequence.addAll(((SequenceType)parent).getAttributes());
    // sequence.addAll(schema);
    //
    // type = xmlFactory.createSequenceType(sequence);
    // }
    // else if (parent instanceof ComplexType){
    // List elements = new ArrayList();
    // elements.addAll(((ComplexType)parent).getAttributes());
    // elements.addAll(schema);
    //
    // //JD: fix this, passing in null here to avoid recalling this method
    // // this method needs to be factored out somewhere else
    // ComplexType ct = (ComplexType) parent;
    // type = xmlFactory.createType(
    // ct.getName(),elements,ct.isIdentified(),ct.isNillable().booleanValue(),
    // ct.getRestrictions(),null,ct.isAbstract());
    // }
    //
    // return type;
    // }
    //
    //
    // /**
    // * Extending a schema.
    // * <p>
    // * Since we will be creating a new Descriptor we need the factory.
    // */
    // public ComplexType extension(ComplexType parent, ComplexType extend) {
    //
    // ComplexType type = null;
    //
    // if (parent instanceof ChoiceType && extend instanceof ChoiceType) {
    //
    // Set choices = new HashSet();
    // choices.addAll(((ChoiceType)parent).getAttributes());
    // choices.addAll(((ChoiceType)extend).getAttributes());
    //
    // type = xmlFactory.createChoiceType(choices);
    // }
    // else if (
    // parent instanceof SequenceType && extend instanceof SequenceType
    // ) {
    //
    // List sequence = new ArrayList();
    //
    // sequence.addAll(((SequenceType)parent).getAttributes());
    // sequence.addAll(((SequenceType)extend).getAttributes());
    //
    // type = xmlFactory.createSequenceType(sequence);
    // }
    // else if (
    // parent instanceof ComplexType && extend instanceof ComplexType
    // ){
    // List elements = new ArrayList();
    // elements.addAll(((ComplexType)parent).getAttributes());
    // elements.addAll(((ComplexType)extend).getAttributes());
    //
    // ComplexType ct = (ComplexType) extend;
    //
    // //JD: this is a bit of a hack, creating type manually (ie wihtout
    // // factory, because we have constructed the schema manually
    // type = new ComplexTypeImpl(
    // ct.getName(),elements,ct.isIdentified(),
    // ct.isNillable().booleanValue(),ct.getRestrictions(),ct,
    // ct.isAbstract()
    // );
    //
    // }
    //
    // return type;
    //
    // }
    /**
     * We can only restrict node if the restricftion is a subtype that used by node.
     *
     * @return restrict, iff restrict.getType() ISA node.getType()
     */
    AttributeDescriptor restrict(AttributeDescriptor node, AttributeDescriptor restrict) {

        if (node.getType() == restrict.getType()) {
            return restrict;
        }
        for (AttributeType /* <?> */ type = restrict.getType();
                type != null;
                type = type.getSuper()) {
            if (node.getType().equals(type)) {
                return restrict;
            }
        }
        throw new IllegalArgumentException("Cannot restrict provided schema");
    }

    Collection restriction(Collection schema, Collection restrict, Collection restriction) {

        if (schema.size() != restrict.size()) {
            throw new IllegalArgumentException(
                    "You must provide an exact structure match in order to implement restriction");
        }

        Iterator i = schema.iterator();
        Iterator j = restrict.iterator();
        while (i.hasNext() && j.hasNext()) {
            restriction.add(
                    restrict((AttributeDescriptor) i.next(), (AttributeDescriptor) j.next()));
        }
        return restriction;
    }

    /**
     * Locate type associated with provided name, or null if not found.
     *
     * <p>Namespaces are not taken in count, so if two properties share the same local name, the
     * first one that matches will be returned.
     */
    public static AttributeType type(Collection schema, Name name) {
        AttributeDescriptor node = node(schema, name);
        if (node != null) return node.getType();
        return null;
    }

    /**
     * Locate type associated with provided name, or null if not found.
     *
     * <p>Namespaces are not taken in count, so if two properties share the same local name, the
     * first one that matches will be returned.
     */
    public static AttributeType type(ComplexType schema, String name) {
        return type(schema, new NameImpl(name));
    }

    /** Locate type associated with provided name, or null if not found. */
    public static AttributeType type(ComplexType schema, Name name) {
        AttributeDescriptor node = node(schema, name);
        if (node != null) return node.getType();
        return null;
    }

    /** Finds the first node associated with the provided name disregarding namespaces */
    public static AttributeDescriptor node(ComplexType schema, String name) {
        // return node(schema,new org.geotools.feature.Name(name));

        for (Iterator itr = list(schema).iterator(); itr.hasNext(); ) {
            AttributeDescriptor node = (AttributeDescriptor) itr.next();

            if (node.getName() == null) {
                // this may be due to old api usage style, where
                // only types had names
                LOGGER.warning("node has no name set, try to fix! " + node);
                if (node.getType().getName().getLocalPart().equals(name)) {
                    return node;
                }
            } else {
                // this is the correct usage
                if (node.getName().getLocalPart().equals(name)) {
                    return node;
                }
            }
        }
        AttributeType superType = schema.getSuper();
        if (superType instanceof ComplexType) {
            return node((ComplexType) superType, name);
        }
        return null;
    }

    // static public List nodes(Attribute schema) {
    // List nodes = new ArrayList();
    //
    // for (Iterator itr = list(schema).iterator(); itr.hasNext();) {
    // Descriptor child = (Descriptor)itr.next();
    // if (child instanceof AttributeDescriptor) {
    // AttributeDescriptor node = (AttributeDescriptor) child;
    // nodes.add(node);
    // }
    // }
    // return nodes;
    // }

    /**
     * Finds the node associated with the provided name.
     *
     * @return AttributeDescriptor assoicated with provided name, or null if not found.
     */
    public static AttributeDescriptor node(ComplexType schema, Name name) {
        return node(list(schema), name);
    }

    /**
     * Finds the node associated with the provided name.
     *
     * @return AttributeDescriptor assoicated with provided name, or null if not found.
     */
    public static AttributeDescriptor node(Collection schema, Name name) {
        for (Iterator itr = schema.iterator(); itr.hasNext(); ) {

            AttributeDescriptor node = (AttributeDescriptor) itr.next();

            Name nodeName = node.getName();
            if (nodeName == null) {
                // this may be due to old api usage style, where
                // only types had names
                LOGGER.warning("node has no name set, try to fix! " + node);
                Name name2 = node.getType().getName();
                if (null == name.getNamespaceURI()) {
                    if (name.getLocalPart().equals(name2.getLocalPart())) {
                        return node;
                    }
                } else if (name2.getNamespaceURI().equals(name.getNamespaceURI())
                        && name2.getLocalPart().equals(name.getLocalPart())) {
                    return node;
                }
            } else {
                // this is the correct usage
                if (name.getNamespaceURI() != null) {
                    if (name.getLocalPart().equals(nodeName.getLocalPart())) {
                        return node;
                    }
                } else if (name.equals(nodeName)) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * Finds the node associated with the provided type.
     *
     * <p>Note a type may be included in more then one node, in which case this will only find the
     * first one.
     *
     * @return AttributeDescriptor assoicated with provided name, or null if not found.
     */
    public static AttributeDescriptor node(ComplexType schema, AttributeType type) {
        for (Iterator itr = list(schema).iterator(); itr.hasNext(); ) {
            AttributeDescriptor node = (AttributeDescriptor) itr.next();
            if (node.getType() == type) {
                return node;
            }
        }
        return null;
    }

    /**
     * List of nodes matching AttributeType.
     *
     * @return List of nodes for the provided type, or empty.
     */
    public static List /* <AttributeDescriptor> */ nodes(ComplexType schema, AttributeType type) {
        List /* <AttributeDescriptor> */ nodes = new ArrayList /* <AttributeDescriptor> */();
        for (Iterator itr = list(schema).iterator(); itr.hasNext(); ) {
            AttributeDescriptor node = (AttributeDescriptor) itr.next();
            if (node.getType().equals(type)) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    /**
     * List of types described by this schema.
     *
     * <p>On the cases where order matters, the returned list preserves the order of descriptors
     * declared in <code>schema</code>
     *
     * @return List of nodes for the provided type, or empty.
     */
    public static List /* <AttributeType> */ types(AttributeType type) {
        List /* <AttributeType> */ types = new ArrayList /* <AttributeType> */();
        for (Iterator itr = list(type).iterator(); itr.hasNext(); ) {
            AttributeDescriptor node = (AttributeDescriptor) itr.next();
            types.add(node.getType());
        }
        return types;
    }

    /**
     * True if there may be more then one AttributeType in the schema.
     *
     * <p>This may happen if:
     *
     * <ul>
     *   <li>The AttributeType is referenced by more then one node.
     *   <li>The node referencing the type has multiplicy greater then 1
     * </ul>
     */
    public static boolean multiple(ComplexType schema, AttributeType type) {
        // return maxOccurs( schema, type ) != 1;
        return maxOccurs(schema, type) > 1;
    }

    public static int maxOccurs(ComplexType schema, AttributeType type) {
        List /* <AttributeDescriptor> */ nodes = nodes(schema, type);
        if (nodes.isEmpty()) return 0;

        int max = 0;
        for (Iterator itr = nodes.iterator(); itr.hasNext(); ) {
            AttributeDescriptor node = (AttributeDescriptor) itr.next();
            if (max == Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            max += node.getMaxOccurs();
        }
        return max;
    }

    /**
     * Returns the list of descriptors defined in the provided schema, preserving declaration order
     * when relevant.
     */
    // @SuppressWarnings("unchecked")
    public static List /* <? extends Descriptor> */ list(AttributeType type) {

        ArrayList list = new ArrayList();

        if (type instanceof ComplexType) {
            list = new ArrayList(((ComplexType) type).getDescriptors());
        }

        return list;

        // if (schema instanceof OrderedDescriptor) {
        // return ((OrderedDescriptor) schema).sequence();
        // } else if (schema instanceof AllDescriptor) {
        // return new ArrayList/*<AttributeDescriptor>*/(((AllDescriptor)
        // schema)
        // .all());
        // } else if (schema instanceof ChoiceDescriptor) {
        // return new ArrayList/*<Descriptor>*/(((ChoiceDescriptor) schema)
        // .options());
        // }
        //
        // return Collections.EMPTY_LIST;

    }

    /**
     * Determines if a collection of attribute descriptors is "simple".
     *
     * @param schema Collection of attribute descriptors.
     * @return True if schema is simple, otherwise false.
     */
    public static boolean isSimple(Collection /* <AttributeDescriptor> */ schema) {
        for (Iterator itr = schema.iterator(); itr.hasNext(); ) {
            AttributeDescriptor d = (AttributeDescriptor) itr.next();
            if (d.getMinOccurs() != 1 || d.getMaxOccurs() != 1) {
                return false;
            }
            if (d.getType() instanceof ComplexType) {
                return false;
            }
        }

        return true;
    }
}
