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
package org.geotools.feature.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.NameImpl;
import org.geotools.util.Converters;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;
import org.opengis.filter.Filter;
import org.xml.sax.helpers.NamespaceSupport;


/**
 * This is a set of utility methods used when <b>implementing</b> types.
 * <p>
 * This set of classes captures the all important how does it work questions,
 * particularly with respect to super types.
 * </p>
 * FIXME: These methods need a Q&A check to confirm correct use of Super TODO:
 * Cannot tell the difference in intent from FeatureTypes
 * 
 * @author Jody Garnett, LISAsoft
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class Types {

    /**
     * Ensures an attribute value is withing the restrictions of the AttributeDescriptor and
     * AttributeType.
     * @param attribute
     * @return true if the attribute value is valid
     */      
     public static boolean isValid( Attribute attribute ){
     
        try {
            validate(attribute.getType(), attribute, attribute.getValue(), false );
            return true;
        }
        catch (IllegalAttributeException invalid ){
            return false;
        }
    }
    /**
     * Validates content against an attribute.
     * 
     * @param attribute
     *            The attribute.
     * @param attributeContent
     *            Content of attribute (often attribute.getValue() 
     * 
     * @throws IllegalAttributeException
     *             In the event that content violates any restrictions specified
     *             by the attribute.
     */
    public static void validate(Attribute attribute, Object attributeContent)
            throws IllegalAttributeException {

        validate(attribute.getType(), attribute, attributeContent, false);
    }
    /**
     * 
     * @param type AttributeType (often attribute.getType() )
     * @param attribute Attribute being tested
     * @param attributeContent Content of the attribute (often attribute.getValue() )
     * @throws IllegalAttributeException
     */
    public static void validate(AttributeType type, Attribute attribute, Object attributeContent)
            throws IllegalAttributeException {

        validate(type, attribute, attributeContent, false);
    }

    /**
     * 
     * @param type AttributeType (often attribute.getType() )
     * @param attribute Attribute being tested
     * @param attributeContent Content of the attribute (often attribute.getValue() )
     * @param isSuper True if super type is being checked
     * @throws IllegalAttributeException
     */
    protected static void validate(AttributeType type, Attribute attribute,
            Object attributeContent, boolean isSuper) throws IllegalAttributeException {

        if (type == null) {
            throw new IllegalAttributeException("null type");
        }

        if (attributeContent == null) {
            if (!attribute.isNillable()) {
                throw new IllegalAttributeException(type.getName() + " not nillable");
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
            if (binding != null && binding != clazz && !binding.isAssignableFrom(clazz)) {
                throw new IllegalAttributeException(clazz.getName()
                        + " is not an acceptable class for " + type.getName()
                        + " as it is not assignable from " + binding);
            }
        }

        if (type.getRestrictions() != null) {
            for (Filter f : type.getRestrictions()) {
                if (!f.evaluate(attribute)) {
                    throw new IllegalAttributeException("Attribute instance (" + attribute.getIdentifier() + ")"
                            + "fails to pass filter: " + f);
                }
            }
        }

        // move up the chain,
        if (type.getSuper() != null) {
            validate(type.getSuper(), attribute, attributeContent, true);
        }
    }

    /**
     * Ensure that attributeContent is a good value for descriptor.
     */
    public static void validate(AttributeDescriptor descriptor,
            Object value) throws IllegalAttributeException {

        if (descriptor == null) {
            throw new NullPointerException("Attribute descriptor required for validation");
        }
        
        if (value == null) {
            if (!descriptor.isNillable()) {
                throw new IllegalArgumentException(descriptor.getName() + " requires a non null value");
            }           
        } else {
            validate( descriptor.getType(), value, false );
        }
    }
    
    /**
     * Do our best to make the provided value line up with the needs of descriptor.
     * <p>
     * This helper method uses the Coverters api to convert the provided
     * value into the required class. If the value is null (and the attribute
     * is not nillable) a default value will be returned.
     * @param descriptor Attribute descriptor we need to supply a value for.
     * @param value The provided value
     * @return Our best attempt to make a valid value
     * @throws IllegalArgumentException if we really could not do it.
     */
    public static Object parse(AttributeDescriptor descriptor, Object value) throws IllegalArgumentException {
        if (value == null){
            if( descriptor.isNillable()){
                return descriptor.getDefaultValue();
            }
        }
        else {
            Class target = descriptor.getType().getBinding(); 
            if ( !target.isAssignableFrom( value.getClass() ) ) {
                // attempt to convert
                Object converted = Converters.convert(value,target);
                if ( converted != null ) {
                    return converted;
                }
//                else {
//                    throw new IllegalArgumentException( descriptor.getLocalName()+ " could not convert "+value+" into "+target);
//                }
            }
        }        
        return value;
    }
    
    protected static void validate(final AttributeType type, final Object value, boolean isSuper) throws IllegalAttributeException {
        if (!isSuper) {
            // JD: This is an issue with how the xml simpel type hierarchy
            // maps to our current Java Type hiearchy, the two are inconsitent.
            // For instance, xs:integer, and xs:int, the later extend the
            // former, but their associated java bindings, (BigDecimal, and
            // Integer)
            // dont.
            Class clazz = value.getClass();
            Class binding = type.getBinding();
            if (binding != null && !binding.isAssignableFrom(clazz)) {
                throw new IllegalAttributeException(clazz.getName()
                        + " is not an acceptable class for " + type.getName()
                        + " as it is not assignable from " + binding);
            }
        }

        if (type.getRestrictions() != null && type.getRestrictions().size() > 0) {
            for (Filter filter : type.getRestrictions()) {
                if (!filter.evaluate(value)) {
                    throw new IllegalAttributeException( type.getName() + " restriction "+ filter + " not met by: " + value);
                }
            }
        }

        // move up the chain,
        if (type.getSuper() != null) {
            validate(type.getSuper(), value, true );
        }
    }
    
    /**
     * FeatureType comparison indicating if the description provided by two FeatureTypes is
     * similar to the point data can be exchanged. This comparison is really very focused on the
     * name / value contract and is willing to overlook details like length restrictions.
     * <p>
     * When creating compatible FeatureTypes you will find some systems have different abilities
     * which is reflected in how well they support a given FeatureType.
     * <p>
     * As an example databases traditionally support variable length strings with a
     * limit of 32 k; while a shapefile is limited to 256 characters. When working with data from
     * both these data sources you will need to make adjustments based on these abilities.
     * </p>
     * If true is returned data conforming to the expected FeatureType can be used with the 
     * actual FeatureType.
     * <p>
     * After assertOrderCovered returns without error the following code will work:<pre><code>
     * for( Property property : feature.getProperties() ){
     *     Object value = property.getValue();
     *     
     *     Property target = newFeature.getProperty( property.getName().getLocalPart() );
     *     target.setValue( value );
     * }
     * </code></pre>
     * Specifically this says that between the two feature types data is assignable on a name by name
     * basis.
     * 
     * @param expected Expected FeatureType being used to compare against
     * @param actual Actual FeatureType 
     * @return true if actual is equal to or a subset of the expected feature type.
     */
    public static void assertNameAssignable( FeatureType expected, FeatureType actual){
        // check feature type name
        String expectedName = expected.getName().getLocalPart();
        String actualName = actual.getName().getLocalPart();
        if( !expectedName.equals( actualName ) ){
            throw new IllegalAttributeException("Expected '"+expectedName+"' but was supplied '"+actualName+"'.");            
        }
        // check attributes names
        Set<String> names = new TreeSet<String>();
        for( PropertyDescriptor descriptor : actual.getDescriptors() ){
            names.add( descriptor.getName().getLocalPart() );
        }
        for( PropertyDescriptor descriptor : expected.getDescriptors() ){
            expectedName = descriptor.getName().getLocalPart();
            if( names.contains( expectedName )){
                names.remove( expectedName ); // only use once!
            }
            else {
                throw new IllegalAttributeException("Expected to find a match for '"+expectedName+"' but was not available remaining names: " + names );
            }
        }
        if( !names.isEmpty() ){
            throw new IllegalAttributeException("Expected to find attributes '"+expectedName+"' but was not available remaining names: " + names );
        }
        
        // check attribute bindings
        for( PropertyDescriptor expectedDescriptor : expected.getDescriptors() ){
            expectedName = expectedDescriptor.getName().getLocalPart();            
            PropertyDescriptor actualDescriptor = actual.getDescriptor( expectedName );
            
            Class<?> expectedBinding = expectedDescriptor.getType().getBinding();
            Class<?> actualBinding = actualDescriptor.getType().getBinding();
            if( !actualBinding.isAssignableFrom( expectedBinding )){
                throw new IllegalArgumentException( "Expected "+expectedBinding.getSimpleName()+" for "+expectedName+" but was "+actualBinding.getSimpleName() );
            }
        }
    }
    
    /**
     * SimpleFeatureType comparison indicating that data from one FeatureType can
     * be exchanged with another - specifically ensuring that the order / value is a
     * reasonable match with the expected number of attributes on each side and the 
     * values correctly assignable.
     * <p>
     * After assertOrderCovered returns without error the following code will work:<pre><code>
     * List<Object> values = feature.getAttributes();
     * newFeature.setAttributes( values );
     * </code></pre>
     * 
     * @param expected
     * @param actual
     * @return
     */
    public static void assertOrderAssignable( SimpleFeatureType expected, SimpleFeatureType actual){
        // check feature type name
        String expectedName = expected.getName().getLocalPart();
        String actualName = actual.getName().getLocalPart();
        if( !expectedName.equals( actualName ) ){
            throw new IllegalAttributeException("Expected '"+expectedName+"' but was supplied '"+actualName+"'.");            
        }
        // check attributes names
        if( expected.getAttributeCount() != actual.getAttributeCount() ){
            throw new IllegalAttributeException("Expected "+expected.getAttributeCount()+" attributes, but was supplied "+actual.getAttributeCount() );
        }
        for( int i=0; i< expected.getAttributeCount(); i++){
            Class<?> expectedBinding = expected.getDescriptor(i).getType().getBinding();
            Class<?> actualBinding = actual.getDescriptor(i).getType().getBinding();
            if( !actualBinding.isAssignableFrom( expectedBinding )){
                String name = expected.getDescriptor(i).getLocalName();
                throw new IllegalArgumentException( "Expected "+expectedBinding.getSimpleName()+" for "+name+" but was "+actualBinding.getSimpleName() );
            }
        }
    }
    
    /**
     * Returns The name of attributes defined in the type.
     * 
     * @param type The type.
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
     * @param name The name, may be null
     * 
     * @return The name in which getLocalPart() == name and getNamespaceURI() == null. Or null if name == null.
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
     * @param name The name, may be null
     * @param namespace The scope or namespace, may be null.
     * 
     * @return The name in which getLocalPart() == name and getNamespaceURI() == namespace.
     */
    public static Name typeName(String namespace, String name) {
        return new NameImpl(namespace, name);
    }

    /**
     * Creates a type name from another name.
     * 
     * @param name The other name.
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
     * Convenience method for turning an array of qualified names into a list of non qualified names.
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
     * Convenience method for turning an array of qualified names into a list of non qualified names.
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
     * Takes a prefixed attribute name and returns an {@link Name} by looking which namespace belongs the prefix to in
     * {@link AppSchemaDataAccessDTO#getNamespaces()}.
     * 
     * @param prefixedName , namespaces
     * @return
     * @throws IllegalArgumentException if <code>prefixedName</code> has no declared namespace in app-schema config file.
     */
    public static Name degloseName(String prefixedName, NamespaceSupport namespaces)
            throws IllegalArgumentException {
        Name name = null;

        if (prefixedName == null) {
            return null;
        }

        int prefixIdx = prefixedName.lastIndexOf(':');
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
    

    /**
     * Converts content into a format which is used to store it internally within an attribute of a specific type.
     * 
     * @param value the object to attempt parsing of.
     * 
     * @throws IllegalArgumentException if parsing is attempted and is unsuccessful.
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
     *  &lt;code&gt;
     * validate(attribute.type(), attribute)
     * &lt;/code&gt;
     * </pre>
     * 
     * </p>
     * 
     * @param attribute The attribute.
     * 
     * @throws IllegalAttributeException In the event that content violates any restrictions specified by the attribute.
     */
    public static void validate(Attribute attribute) throws IllegalAttributeException {

        validate(attribute, attribute.getValue());
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
    

    /**
     * Determines if <code>parent</code> is a super type of <code>type</code>
     * 
     * @param type The type in question.
     * @param parent The possible parent type.
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
     * Returns the first descriptor matching the given local name within the given type.
     * 
     * @param type The type, non null.
     * @param name The name, non null.
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
     * Returns the first descriptor matching the given name + namespace within the given type.
     * 
     * @param type The type, non null.
     * @param name The name, non null.
     * @param namespace The namespace, non null.
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
     * @param type The type, non null.
     * @param name The name, non null.
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
     * @param type The type, non null.
     * @param name The name, non null.
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
     * @param type The type, non null.
     * @param name The name, non null.
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
     * Returns the set of all descriptors of a complex type, including from supertypes.
     * 
     * @param type The type, non null.
     * 
     * @return The list of all descriptors.
     */
    public static List<PropertyDescriptor> descriptors(ComplexType type) {
        // get list of descriptors from types and all supertypes
        List<PropertyDescriptor> children = new ArrayList<PropertyDescriptor>();
        ComplexType loopType = type;
        while (loopType != null) {
            children.addAll(loopType.getDescriptors());
            loopType = loopType.getSuper() instanceof ComplexType ? (ComplexType) loopType
                    .getSuper() : null;
        }
        return children;
    }
    
    
    /**
     * Find a descriptor, taking in to account supertypes AND substitution groups
     * 
     * @param parentType type
     * @param name name of descriptor
     * @return descriptor, null if not found
     */
    public static PropertyDescriptor findDescriptor(ComplexType parentType, Name name) {
        // get list of descriptors from types and all supertypes
        List<PropertyDescriptor> descriptors = descriptors(parentType);

        // find matching descriptor
        for (Iterator<PropertyDescriptor> it = descriptors.iterator(); it.hasNext();) {
            PropertyDescriptor d = it.next();
            if (d.getName().equals(name)) {
                return d;
            }
        }

        // nothing found, perhaps polymorphism?? let's loop again
        for (Iterator<PropertyDescriptor> it = descriptors.iterator(); it.hasNext();) {
            List<AttributeDescriptor> substitutionGroup = (List<AttributeDescriptor>) it.next()
                    .getUserData().get("substitutionGroup");
            if (substitutionGroup != null) {
                for (Iterator<AttributeDescriptor> it2 = substitutionGroup.iterator(); it2
                        .hasNext();) {
                    AttributeDescriptor d = it2.next();
                    if (d.getName().equals(name)) { // BINGOOO !!
                        return d;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Find a descriptor, taking in to account supertypes AND substitution groups
     * 
     * @param parentType type
     * @param name name of descriptor
     * @return descriptor, null if not found
     */
    public static PropertyDescriptor findDescriptor(ComplexType parentType, String name) {
        // get list of descriptors from types and all supertypes
        List<PropertyDescriptor> descriptors = descriptors(parentType);

        // find matching descriptor
        for (Iterator<PropertyDescriptor> it = descriptors.iterator(); it.hasNext();) {
            PropertyDescriptor d = it.next();
            if (d.getName().getLocalPart().equals(name)) {
                return d;
            }
        }

        // nothing found, perhaps polymorphism?? let's loop again
        for (Iterator<PropertyDescriptor> it = descriptors.iterator(); it.hasNext();) {
            List<AttributeDescriptor> substitutionGroup = (List<AttributeDescriptor>) it.next()
                    .getUserData().get("substitutionGroup");
            if (substitutionGroup != null) {
                for (Iterator<AttributeDescriptor> it2 = substitutionGroup.iterator(); it2
                        .hasNext();) {
                    AttributeDescriptor d = it2.next();
                    if (d.getName().getLocalPart().equals(name)) { // BINGOOO !!
                        return d;
                    }
                }
            }
        }

        return null;
    }
            
}
