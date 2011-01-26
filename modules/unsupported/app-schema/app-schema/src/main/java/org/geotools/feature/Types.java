/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.opengis.feature.IllegalAttributeException;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.feature.type.AttributeTypeImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.Identifier;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is a set of utility methods used when <b>implementing</b> types.
 * <p>
 * This set of classes captures the all important how does it work questions, particularly with
 * respect to super types.
 * </p>
 * FIXME: These methods need a Q&A check to confirm correct use of Super TODO: Cannot tell the
 * difference in intent from FeatureTypes
 * 
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/feature/Types.java $
 */
public class Types {

    /**
     * Returns The name of attributes defined in the type.
     * 
     * @param type
     *            The type.
     * 
     */
    public static Name[] names(ComplexType type) {
        ArrayList names = new ArrayList();
        for (Iterator itr = type.getDescriptors().iterator(); itr.hasNext();) {
            AttributeDescriptor ad = (AttributeDescriptor) itr.next();
            names.add(ad.getName());
        }

        return (Name[]) names.toArray(new Name[names.size()]);
    }

    /**
     * Creates a type name from a single non-qualified string.
     * 
     * @param name
     *            The name, may be null
     * 
     * @return The name in which getLocalPart() == name and getNamespaceURI() == null. Or null if
     *         name == null.
     */
    public static Name typeName(String name) {
        if (name == null) {
            return null;
        }
        return new NameImpl(name);
    }

    /**
     * Creates an attribute name from a single non-qualified string.
     * 
     * @param name
     *            The name, may be null
     * @param namespace
     *            The scope or namespace, may be null.
     * 
     * @return The name in which getLocalPart() == name and getNamespaceURI() == namespace.
     */
    public static Name typeName(String namespace, String name) {
        return new NameImpl(namespace, name);
    }

    /**
     * Creates a type name from another name.
     * 
     * @param name
     *            The other name.
     */
    public static Name typeName(Name name) {
        return new NameImpl(name.getNamespaceURI(), name.getLocalPart());
    }

    /**
     * Creates a set of attribute names from a set of strings.
     * <p>
     * This method returns null if names == null.
     * </p>
     * <p>
     * The ith name has getLocalPart() == names[i] and getNamespaceURI() == null
     * </p>
     */
    public static Name[] toNames(String[] names) {
        if (names == null) {
            return null;
        }
        Name[] attributeNames = new Name[names.length];

        for (int i = 0; i < names.length; i++) {
            attributeNames[i] = typeName(names[i]);
        }

        return attributeNames;
    }

    /**
     * Creates a set of type names from a set of strings.
     * <p>
     * This method returns null if names == null.
     * </p>
     * <p>
     * The ith name has getLocalPart() == names[i] and getNamespaceURI() == null
     * </p>
     */
    public static Name[] toTypeNames(String[] names) {
        if (names == null) {
            return null;
        }

        Name[] typeNames = new Name[names.length];

        for (int i = 0; i < names.length; i++) {
            typeNames[i] = typeName(names[i]);
        }

        return typeNames;
    }

    /**
     * Convenience method for turning an array of qualified names into a list of non qualified
     * names.
     * 
     */
    public static String[] fromNames(Name[] attributeNames) {
        if (attributeNames == null) {
            return null;
        }

        String[] names = new String[attributeNames.length];
        for (int i = 0; i < attributeNames.length; i++) {
            names[i] = attributeNames[i].getLocalPart();
        }

        return names;
    }

    /**
     * Convenience method for turning an array of qualified names into a list of non qualified
     * names.
     * 
     */
    public static String[] fromTypeNames(Name[] typeNames) {
        if (typeNames == null)
            return null;

        String[] names = new String[typeNames.length];
        for (int i = 0; i < typeNames.length; i++) {
            names[i] = typeNames[i].getLocalPart();
        }

        return names;
    }

    /**
     * Returns the first descriptor matching the given local name within the given type.
     * 
     * @param type
     *            The type, non null.
     * @param name
     *            The name, non null.
     * 
     * @return The first descriptor, or null if no match.
     */
    public static PropertyDescriptor descriptor(ComplexType type, String name) {
        List match = descriptors(type, name);

        if (match.isEmpty())
            return null;

        return (PropertyDescriptor) match.get(0);
    }

    /**
     * Returns the first descriptor matching the given local name within the given type.
     * 
     * @param type
     *            The type, non null.
     * @param name
     *            The name, non null.
     * 
     * @return The first descriptor, or null if no match.
     */
    public static PropertyDescriptor descriptor(ComplexType type, String name,
            AttributeType actualType) {
        List match = descriptors(type, name);

        if (match.isEmpty()) {
            Collection properties = type.getDescriptors();
            for (Iterator it = properties.iterator(); it.hasNext();) {
                PropertyDescriptor desc = (PropertyDescriptor) it.next();
                if (!(desc instanceof AttributeDescriptor)) {
                    continue;
                }
                AttributeDescriptor attDesc = (AttributeDescriptor) desc;
                AttributeType attType = (AttributeType) attDesc.getType();
                if (isSuperType(actualType, attType)) {
                    return attDesc;
                }
            }
            return null;
        }

        return (PropertyDescriptor) match.get(0);
    }

    public static PropertyDescriptor descriptor(ComplexType type, Name name,
            AttributeType actualType) {
        List match = descriptors(type, name);

        if (match.isEmpty()) {
            Collection properties = type.getDescriptors();
            for (Iterator it = properties.iterator(); it.hasNext();) {
                PropertyDescriptor desc = (PropertyDescriptor) it.next();
                if (!(desc instanceof AttributeDescriptor)) {
                    continue;
                }
                AttributeDescriptor attDesc = (AttributeDescriptor) desc;
                AttributeType attType = (AttributeType) attDesc.getType();
                if (isSuperType(actualType, attType)) {
                    return attDesc;
                }
            }
            return null;
        }

        return (PropertyDescriptor) match.get(0);
    }

    /**
     * Returns the first descriptor matching the given name + namespace within the given type.
     * 
     * @param type
     *            The type, non null.
     * @param name
     *            The name, non null.
     * @param namespace
     *            The namespace, non null.
     * 
     * @return The first descriptor, or null if no match.
     */
    public static PropertyDescriptor descriptor(ComplexType type, String name, String namespace) {
        return descriptor(type, new NameImpl(namespace, name));
    }

    /**
     * Returns the first descriptor matching the given name within the given type.
     * 
     * 
     * @param type
     *            The type, non null.
     * @param name
     *            The name, non null.
     * 
     * @return The first descriptor, or null if no match.
     */
    public static PropertyDescriptor descriptor(ComplexType type, Name name) {
        List match = descriptors(type, name);

        if (match.isEmpty())
            return null;

        return (PropertyDescriptor) match.get(0);
    }

    /**
     * Returns the set of descriptors matching the given local name within the given type.
     * 
     * @param type
     *            The type, non null.
     * @param name
     *            The name, non null.
     * 
     * @return The list of descriptors named 'name', or an empty list if none such match.
     */
    public static List/* <PropertyDescriptor> */descriptors(ComplexType type, String name) {
        if (name == null)
            return Collections.EMPTY_LIST;

        List match = new ArrayList();

        for (Iterator itr = type.getDescriptors().iterator(); itr.hasNext();) {
            PropertyDescriptor descriptor = (PropertyDescriptor) itr.next();
            String localPart = descriptor.getName().getLocalPart();
            if (name.equals(localPart)) {
                match.add(descriptor);
            }
        }

        // only look up in the super type if the descriptor is not found
        // as a direct child definition
        if (match.size() == 0) {
            AttributeType superType = type.getSuper();
            if (superType instanceof ComplexType) {
                List superDescriptors = descriptors((ComplexType) superType, name);
                match.addAll(superDescriptors);
            }
        }
        return match;
    }

    /**
     * Returns the set of descriptors matching the given name.
     * 
     * @param type
     *            The type, non null.
     * @param name
     *            The name, non null.
     * 
     * @return The list of descriptors named 'name', or an empty list if none such match.
     */
    public static List/* <PropertyDescriptor> */descriptors(ComplexType type, Name name) {
        if (name == null)
            return Collections.EMPTY_LIST;

        List match = new ArrayList();

        for (Iterator itr = type.getDescriptors().iterator(); itr.hasNext();) {
            PropertyDescriptor descriptor = (PropertyDescriptor) itr.next();
            Name descriptorName = descriptor.getName();
            if (name.equals(descriptorName)) {
                match.add(descriptor);
            }
        }

        // only look up in the super type if the descriptor is not found
        // as a direct child definition
        if (match.size() == 0) {
            AttributeType superType = type.getSuper();
            if (superType instanceof ComplexType) {
                List superDescriptors = descriptors((ComplexType) superType, name);
                match.addAll(superDescriptors);
            }
        }
        return match;
    }

    /**
     * Determines if <code>parent</code> is a super type of <code>type</code>
     * 
     * @param type
     *            The type in question.
     * @param parent
     *            The possible parent type.
     * 
     */
    public static boolean isSuperType(PropertyType type, PropertyType parent) {
        while (type.getSuper() != null) {
            type = type.getSuper();
            if (type.equals(parent))
                return true;
        }

        return false;
    }

    /**
     * Converts content into a format which is used to store it internally within an attribute of a
     * specific type.
     * 
     * @param value
     *            the object to attempt parsing of.
     * 
     * @throws IllegalArgumentException
     *             if parsing is attempted and is unsuccessful.
     */
    public static Object parse(AttributeType type, Object content) throws IllegalArgumentException {

        // JD: TODO: this is pretty lame
        if (type instanceof AttributeTypeImpl) {
            AttributeTypeImpl hack = (AttributeTypeImpl) type;
            Object parsed = hack.parse(content);

            if (parsed != null) {
                return parsed;
            }
        }

        return content;
    }

    /**
     * Validates anattribute. <br>
     * <p>
     * Same result as calling:
     * 
     * <pre>
     * 	&lt;code&gt;
     * validate(attribute.type(), attribute)
     * &lt;/code&gt;
     * </pre>
     * 
     * </p>
     * 
     * @param attribute
     *            The attribute.
     * 
     * @throws IllegalAttributeException
     *             In the event that content violates any restrictions specified by the attribute.
     */
    public static void validate(Attribute attribute) throws IllegalAttributeException {

        validate(attribute, attribute.getValue());
    }

    /**
     * Validates content against an attribute.
     * 
     * @param attribute
     *            The attribute.
     * @param attributeContent
     *            Content of attribute.
     * 
     * @throws IllegalAttributeException
     *             In the event that content violates any restrictions specified by the attribute.
     */
    public static void validate(Attribute attribute, Object attributeContent)
            throws IllegalAttributeException {

        validate(attribute.getType(), attribute, attributeContent, false);
    }

    public static void validate(AttributeType type, Attribute attribute, Object attributeContent)
            throws IllegalAttributeException {

        validate(type, attribute, attributeContent, false);
    }

    protected static void validate(AttributeType type, Attribute attribute,
            Object attributeContent, boolean isSuper) throws IllegalAttributeException {

        if (type == null) {
            throw new IllegalAttributeException(attribute.getDescriptor(), "null type");
        }

        if (attributeContent == null) {
            if (!attribute.isNillable()) {
                throw new IllegalAttributeException(attribute.getDescriptor(), type.getName()
                        + " not nillable");
            }
            return;
        }

        if (type.isIdentified() && attribute.getIdentifier() == null) {
            throw new NullPointerException(type.getName() + " is identified, null id not accepted");
        }

        if (!isSuper) {

            // JD: This is an issue with how the xml simpel type hierarchy
            // maps to our current Java Type hiearchy, the two are inconsitent.
            // For instance, xs:integer, and xs:int, the later extend the
            // former, but their associated java bindings, (BigDecimal, and
            // Integer)
            // dont.
            Class clazz = attributeContent.getClass();
            Class binding = type.getBinding();
            if (binding != null && !binding.isAssignableFrom(clazz)) {
                throw new IllegalAttributeException(attribute.getDescriptor(), clazz.getName()
                        + " is not an acceptable class for " + type.getName()
                        + " as it is not assignable from " + binding);
            }
        }

        if (type.getRestrictions() != null && type.getRestrictions().size() > 0) {

            final Attribute fatt = attribute;
            Attribute fake = new Attribute() {

                private Map<Object, Object> userData = new HashMap<Object, Object>();

                public AttributeDescriptor getDescriptor() {
                    return fatt.getDescriptor();
                }

                public PropertyDescriptor descriptor() {
                    return fatt.getDescriptor();
                }

                public org.opengis.feature.type.Name name() {
                    return fatt.getName();
                }

                public AttributeType getType() {
                    return fatt.getType();
                }

                public boolean isNillable() {
                    return fatt.isNillable();
                }

                public Identifier getIdentifier() {
                    return fatt.getIdentifier();
                }

                public Object getValue() {
                    return fatt.getValue();
                }

                public void setValue(Object newValue) throws IllegalArgumentException {
                    throw new UnsupportedOperationException("Modification is not supported");
                }

                public Object operation(Name arg0, List arg1) {
                    throw new UnsupportedOperationException("Operation is not supported");
                }

                public Name getName() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public Map<Object, Object> getUserData() {
                    return userData;
                }

                public void validate() throws IllegalAttributeException {
                    // do not care
                }

            };

            for (Iterator itr = type.getRestrictions().iterator(); itr.hasNext();) {
                Filter f = (Filter) itr.next();
                if (!f.evaluate(fake)) {
                    throw new IllegalAttributeException(attribute.getDescriptor(),
                            "Attribute instance (" + fake.getIdentifier().toString() + ")"
                                    + "fails to pass filter: " + f);
                }
            }
        }

        // move up the chain,
        if (type.getSuper() != null) {
            validate(type.getSuper(), attribute, attributeContent, true);
        }
    }

    public static void validate(ComplexAttribute attribute) throws IllegalArgumentException {

    }

    public static void validate(ComplexAttribute attribute, Collection content)
            throws IllegalArgumentException {

    }

    protected static void validate(ComplexType type, ComplexAttribute attribute, Collection content)
            throws IllegalAttributeException {

        // do normal validation
        validate((AttributeType) type, (Attribute) attribute, (Object) content, false);

        if (content == null) {
            // not really much else we can do
            return;
        }

        Collection schema = type.getDescriptors();

        int index = 0;
        for (Iterator itr = content.iterator(); itr.hasNext();) {
            Attribute att = (Attribute) itr.next();

            // att shall not be null
            if (att == null) {
                throw new NullPointerException("Attribute at index " + index
                        + " is null. Attributes "
                        + "can't be null. Do you mean Attribute.get() == null?");
            }

            // and has to be of one of the allowed types
            AttributeType attType = att.getType();
            boolean contains = false;
            for (Iterator sitr = schema.iterator(); sitr.hasNext();) {
                AttributeDescriptor ad = (AttributeDescriptor) sitr.next();
                if (ad.getType().equals(attType)) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                throw new IllegalArgumentException("Attribute of type " + attType.getName()
                        + " found at index " + index
                        + " but this type is not allowed by this descriptor");
            }

            index++;
        }

        // empty is allows, in such a case, content should be empty
        if (type.getDescriptors().isEmpty()) {
            if (!content.isEmpty()) {
                throw new IllegalAttributeException(attribute.getDescriptor(),
                        "Type indicates empty attribute collection, content does not");
            }

            // we are done
            return;
        }

        validateAll(type, attribute, content);

        if (type.getSuper() != null) {
            validate((ComplexType) type.getSuper(), attribute, content);
        }
    }

    private static void validateAll(ComplexType type, ComplexAttribute att, Collection content)
            throws IllegalAttributeException {
        processAll(type.getDescriptors(), content);
    }

    private static void processAll(Collection/* <AttributeDescriptor> */all, Collection/*
                                                                                        * <Attribute>
                                                                                        */content)
            throws IllegalAttributeException {

        // TODO: JD: this can be definitley be optimzed, as written its O(n^2)

        // for each descriptor, count occurences of each matching attribute
        ArrayList remaining = new ArrayList(content);
        for (Iterator itr = all.iterator(); itr.hasNext();) {
            AttributeDescriptor ad = (AttributeDescriptor) itr.next();

            int min = ad.getMinOccurs();
            int max = ad.getMaxOccurs();
            int occurences = 0;

            for (Iterator citr = remaining.iterator(); citr.hasNext();) {
                Attribute a = (Attribute) citr.next();
                if (a.getName().equals(ad.getName())) {
                    occurences++;
                    citr.remove();
                }
            }

            if (occurences < ad.getMinOccurs() || occurences > ad.getMaxOccurs()) {
                throw new IllegalAttributeException(ad, "Found " + occurences + " of "
                        + ad.getName() + " when type" + "specifies between " + min + " and " + max);
            }
        }

        if (!remaining.isEmpty()) {
            throw new IllegalAttributeException((AttributeDescriptor) remaining.iterator().next(),
                    "Extra content found beyond the specified in the schema: " + remaining);
        }

    }

    public static QName toQName(Name featurePath) {
        return toQName(featurePath, null);
    }

    public static QName toQName(Name featurePath, NamespaceSupport ns) {
        if (featurePath == null) {
            return null;
        }
        String namespace = featurePath.getNamespaceURI();
        String localName = featurePath.getLocalPart();
        QName qName;
        if (null == namespace) {
            qName = new QName(localName);
        } else {
            if (ns != null) {
                String prefix = ns.getPrefix(namespace);
                if (prefix != null) {
                    qName = new QName(namespace, localName, prefix);
                    return qName;
                }
            }
            qName = new QName(namespace, localName);
        }
        return qName;
    }

    /**
     * 
     * @param name
     * @return
     * @deprecated use {@link #toTypeName(QName}
     */
    public static Name toName(QName name) {
        return toTypeName(name);
    }

    public static Name toTypeName(QName name) {
        if (XMLConstants.NULL_NS_URI.equals(name.getNamespaceURI())) {
            return typeName(name.getLocalPart());
        }
        return typeName(name.getNamespaceURI(), name.getLocalPart());
    }

    public static boolean equals(Name name, QName qName) {
        if (name == null && qName != null) {
            return false;
        }
        if (qName == null && name != null) {
            return false;
        }
        if (XMLConstants.NULL_NS_URI.equals(qName.getNamespaceURI())) {
            if (null != name.getNamespaceURI()) {
                return false;
            } else {
                return name.getLocalPart().equals(qName.getLocalPart());
            }
        }
        if (null == name.getNamespaceURI()
                && !XMLConstants.NULL_NS_URI.equals(qName.getNamespaceURI())) {
            return false;
        }

        return name.getNamespaceURI().equals(qName.getNamespaceURI())
                && name.getLocalPart().equals(qName.getLocalPart());
    }

    /**
     * Takes a prefixed attribute name and returns an {@link Name} by looking which namespace
     * belongs the prefix to in {@link AppSchemaDataAccessDTO#getNamespaces()}.
     * 
     * @param prefixedName
     *            , namespaces
     * @return
     * @throws IllegalArgumentException
     *             if <code>prefixedName</code> has no declared namespace in app-schema config file.
     */
    public static Name degloseName(String prefixedName, NamespaceSupport namespaces)
            throws IllegalArgumentException {
        Name name = null;

        if (prefixedName == null) {
            return null;
        }

        int prefixIdx = prefixedName.indexOf(':');
        if (prefixIdx == -1) {
            return Types.typeName(prefixedName);
            // throw new IllegalArgumentException(prefixedName + " is not
            // prefixed");
        }

        String nsPrefix = prefixedName.substring(0, prefixIdx);
        String localName = prefixedName.substring(prefixIdx + 1);
        String nsUri = namespaces.getURI(nsPrefix);

        // handles undeclared namespaces in the app-schema mapping file
        if (nsUri == null) {
            throw new IllegalArgumentException("No namespace set: The namespace has not"
                    + " been declared in the app-schema mapping file for name: " + nsPrefix + ":"
                    + localName + " [Check the Namespaces section in the config file] ");

        }

        name = Types.typeName(nsUri, localName);

        return name;
    }

    // /** Wander up getSuper gathering all memberTypes */
    // public static Set/*<FeatureType>*/ memberTypes(
    // FeatureCollectionType collectionType) {
    // Set/*<FeatureType>*/ all = new HashSet/*<FeatureType>*/();
    // memberTypes(collectionType, all);
    // return all;
    // }
    //
    // /**
    // * Collection memberType contributions.
    // * <p>
    // * Tail recursion is used so that subclasses "override" contributions made
    // * by the parent. (This depends on FeatureType identity being completly
    // * defined by their Name.
    // * </p>
    // *
    // * @param collection
    // * @param all
    // */
    // static void memberTypes(FeatureCollectionType/*<?>*/ collection,
    // Set/*<FeatureType>*/ all) {
    // if (collection == null)
    // return;
    //
    // ComplexType superType = (ComplexType) collection.getSuper();
    // if (superType instanceof FeatureCollectionType) {
    // memberTypes((FeatureCollectionType) superType, all);
    // }
    // featureTypes(collection.getMemberDescriptor(), all); // tail
    // // recursion
    // }
    //
    // /**
    // * Process a descriptor for indicated FeatureTypes.
    // * <p>
    // * This method should not be used directly, please use memberTypes(
    // * collection ) as it will also consider types contributed by the
    // * superclass.
    // * </p>
    // *
    // * @param descriptor
    // * @param all
    // */
    // static void featureTypes(Descriptor descriptor, Set/*<FeatureType>*/ all)
    // {
    // if (descriptor instanceof AttributeDescriptor) {
    // AttributeDescriptor attribute = (AttributeDescriptor) descriptor;
    // if (attribute.getType() instanceof FeatureType) {
    // FeatureType type = (FeatureType) attribute.getType();
    // all.add(type);
    // }
    // } else if (descriptor instanceof ChoiceDescriptor) {
    // ChoiceDescriptor choice = (ChoiceDescriptor) descriptor;
    // for (Iterator itr = choice.options().iterator(); itr.hasNext();) {
    // Descriptor option = (Descriptor) itr.next();
    // featureTypes(option, all);
    // }
    // } else if (descriptor instanceof OrderedDescriptor) {
    // OrderedDescriptor list = (OrderedDescriptor) descriptor;
    // for (Iterator itr = list.sequence().iterator(); itr.hasNext();) {
    // Descriptor option = (Descriptor) itr.next();
    // featureTypes(option, all);
    // }
    // } else if (descriptor instanceof OrderedDescriptor) {
    // OrderedDescriptor list = (OrderedDescriptor) descriptor;
    // for (Iterator itr = list.sequence().iterator(); itr.hasNext();) {
    // Descriptor option = (Descriptor) itr.next();
    // featureTypes(option, all);
    // }
    // } else {
    // // should not occur
    // }
    // }
    //
    // /**
    // * This method is about as bad as it gets, we need to wander through
    // * Descriptor detecting overrides by AttributeType.
    // * <p>
    // * Almost makes me thing Descriptor should have the attrribute name, and
    // the
    // * Name stuff should be left on AttributeType.
    // * </p>
    // *
    // * @param complex
    // * @return Descriptor that actually describes what is valid for the
    // * ComplexType.
    // */
    // public static Descriptor schema(ComplexType complex) {
    // // We need to do this with tail recursion:
    // // - and sequence, any, choice gets hacked differently ...
    // return complex.getDescriptor();
    // }
    //
    // /**
    // * Returns the ancestors of <code>type</code>
    // *
    // * @param type
    // * @return
    // */
    // public static List/*<AttributeType>*/ ancestors(AttributeType type) {
    // List/*<AttributeType>*/ ancestors = new ArrayList();
    // if (type.getSuper() != null) {
    // ancestors = new LinkedList/*<AttributeType>*/();
    // while (type.getSuper() != null) {
    // AttributeType ancestor = type.getSuper();
    // ancestors.add(ancestor);
    // type = ancestor;
    // }
    // }
    // return ancestors;
    // }
    //
    // /**
    // * Checks wether the ancestors of <code>type</code> have a member named as
    // * indicated by <code>superType</code>.
    // * <p>
    // * WARNING: checking against non qualified <code>superType</code> is
    // * allowed, though it may result in false positives. This is needed not to
    // * break compatibility with existing code which checks against Feature
    // * supertype without GML namespace
    // * </p>
    // *
    // * @param type
    // * the <code>AttributeType</code> to check its ancestors
    // * against
    // * @param superType
    // * the <code>AttributeType</code> to see if its an ancestor of
    // * <code>type</code>
    // * @return <code>true</code> if <code>superType</code> is an ancestor of
    // * <code>type</code>, false otherwise
    // */
    // public static boolean isDescendedFrom(AttributeType type, Name superType)
    // {
    // String ns = superType.getNamespaceURI();
    //				
    // if (XMLConstants.NULL_NS_URI.equals(ns) || ns == null) {
    // // WARNING: checking against non qualified type name. This
    // // is needed not to break compatibility with existing code
    // // which checks against Feature supertype without GML namespace
    // for (Iterator itr = ancestors(type).iterator(); itr.hasNext();) {
    // AttributeType ancestor = (AttributeType) itr.next();
    // if (ancestor.getName().getLocalPart().equals(superType.getLocalPart())) {
    // return true;
    // }
    // }
    // } else {
    // for (Iterator itr = ancestors(type).iterator(); itr.hasNext();) {
    // AttributeType ancestor = (AttributeType) itr.next();
    // if (ancestor.getName().equals(superType)) {
    // return true;
    // }
    // }
    // }
    // return false;
    // }
    //
    // /**
    // * Checks wether <code>superType</code> is an ancestor of
    // * <code>type</code>.
    // *
    // * @param type
    // * the <code>AttributeType</code> to check its ancestors
    // * against
    // * @param superType
    // * the <code>AttributeType</code> to see if its an ancestor of
    // * <code>type</code>
    // * @return <code>true</code> if <code>superType</code> is an ancestor of
    // * <code>type</code>, false otherwise
    // */
    // public static boolean isDescendedFrom(AttributeType type,
    // AttributeType superType) {
    // for (Iterator itr = ancestors(type).iterator(); itr.hasNext();) {
    // AttributeType ancestor = (AttributeType) itr.next();
    // if (ancestor.equals(superType)) {
    // return true;
    // }
    // }
    // return false;
    // }
}
