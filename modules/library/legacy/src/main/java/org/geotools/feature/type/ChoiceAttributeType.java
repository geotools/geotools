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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.feature.AttributeType;
import org.geotools.feature.DefaultAttributeType;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import java.util.Arrays;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;


/**
 * This represents a Choice of AttributeTypes. That means, an Attribute of this
 * type may be one of any of this AttributeType's children. This attribute  is
 * not valid for Simple Features, and maps to the Choice construct in GML.
 * 
 * <p>
 * Another way to think about the ChoiceAttributeType is as a Union
 * construction from C - it can store a number of different types of value,
 * but it only stores the one value.  The parse and validate methods try out
 * each of the choices to  see if one of them might work, since all are valid.
 * The order that the child attributeTypes (the choices you can use) are
 * specified is important, because  some objects can parse and validate
 * against several types.  The first choice that returns true is the one that
 * will
 * </p>
 *
 * @author dzwiers
 * @author Chris Holmes, TOPP
 * @source $URL$
 * 
 * @deprecated Will be removed in geotools 2.6.
 */
public class ChoiceAttributeType extends AttributeDescriptorImpl implements AttributeType {
    
    private final AttributeType[] children;
    
    /**
     * DOCUMENT ME!
     *
     * @param copy
     */
    public ChoiceAttributeType(ChoiceAttributeType copy) {
    	super( copy.getType(), copy.getName(), copy.getMinOccurs(), copy.getMaxOccurs(), copy.isNillable(), copy.getDefaultValue() );
        
        this.children = copyChildren(copy.getAttributeTypes());
    }

    // The field for 'Class type' should be added when GT has moved to java 1.5
    public ChoiceAttributeType(String name, int min, int max,
        AttributeType[] children, Filter restriction) {
    	super(DefaultAttributeType.createAttributeType(name, Object.class, restriction),
    			new NameImpl(name),min,max,calculateNillableStatic(children),null);
        
    	//ensure immutable.
        this.children = copyChildren(children);
    }

    public ChoiceAttributeType(String name, AttributeType[] children) {
        this(name, 1, 1, children, Filter.EXCLUDE);
    }

    public Filter getRestriction() {
        return DefaultAttributeType.getRestriction(this);
    }

    protected AttributeType[] copyChildren(AttributeType[] attributes) {
	int length = attributes.length;
	AttributeType[] returnArray = new AttributeType[length];
	System.arraycopy(attributes, 0, returnArray, 0, length);
	return returnArray;
    }

    /**
     * {@inheritDoc}
     */
    public String getLocalName() {
    	return DefaultAttributeType.getLocalName(this);
    }

    /**
     * Gets the class of the object.  For a choice this is fairly useless, as
     * it  just returns Object, since we can not tell more than that.
     *
     * @return currently always returns Object.class, since we can't tell more.
     *
     * @task REVISIT: Perhaps we should add a getTypes() method that returns an
     *       array of classes, that would represent the classes that you can
     *       choose from.
     * @task REVISIT: Would also be good if this could dynamically figure out
     *       the broadest class - like Number if the choices were Double and
     *       Integer.
     *
     * @see org.geotools.feature.AttributeType#getBinding()
     */
    public Class getBinding() {
		return DefaultAttributeType.getBinding(this);
    }

    public boolean calculateNillable(AttributeType[] children) {
    	return calculateNillableStatic(children);
    }

    /**
     * static verison of {@link #calculateNillable(AttributeType[])} which can 
     * be called from constructor.
     */
    private static boolean calculateNillableStatic(AttributeType[] children) {
        for (int i = 0, ii = children.length; i < ii; i++) {
            if (children[i].isNillable()) {
                return true;
            }
        }

        //none of the children can take a null, so no nulls are allowed.
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.geotools.feature.AttributeType#isGeometry()
     */
    public boolean isGeometry() {
        return false;
    }

    /**
     * Goes through the children, and searches for a parser that works. This
     * method  searches in the order in which the children are specified ...
     * please keep  this in mind when creating these objects if you care about
     * precedence.
     *
     * @param value The object to parse.
     *
     * @return The object parsed into the appropriate form for the Attribute.
     *
     * @throws IllegalArgumentException If the object could not be parsed by
     *         any of the child attribute Types.
     */
    public Object parse(Object value) throws IllegalArgumentException {
        for (int i = 0; i < children.length; i++) {
            try {
                return children[i].parse(value);
            } catch (IllegalArgumentException e) {
                // ignore ... try the next
            }
        }

        throw new IllegalArgumentException("Could not be parsed :(");
    }

    /**
     * Goes through the children, and searches for a validator that works. This
     * method  searches in the order in which the children are specified ...
     * please keep  this in mind when creating these objects if you care about
     * precedence.
     *
     * @param obj The object to validate.
     *
     * @throws IllegalArgumentException If none of the children can validate.
     */
    public void validate(Object obj) throws IllegalArgumentException {
        for (int i = 0; i < children.length; i++) {
            try {
                children[i].validate(obj);

                return; // validates
            } catch (IllegalArgumentException e) {
                // ignore ... try the next
            }
        }

        throw new IllegalArgumentException("Could not be validated :(");
    }

    /**
     * Goes through the children, and searches for a duplicator that works.
     * This method  searches in the order in which the children are specified
     * ... please keep  this in mind when creating these objects if you care
     * about precedence.
     *
     * @param src The object to be duplicated.
     *
     * @return A deep copy of the original object.
     *
     * @throws IllegalAttributeException For any attribute errors.
     * @throws IllegalArgumentException If the object could not be duplicated.
     */
    public Object duplicate(Object src) throws IllegalAttributeException {
        for (int i = 0; i < children.length; i++) {
            try {
                return children[i].duplicate(src);
            } catch (IllegalArgumentException e) {
                // ignore ... try the next
            }
        }

        throw new IllegalArgumentException("Could not be duplicated :(");
    }

    /**
     * Returns the default value for the first child which does not  throw an
     * exception, null otherwise.
     *
     * @return The default value of the first choice that does not throw an
     *         exception.
     */
    public Object createDefaultValue() {
        for (int i = 0; i < children.length; i++) {
            try {
                return children[i].createDefaultValue();
            } catch (IllegalArgumentException e) {
                // ignore ... try the next
            }
        }

        return null;
    }

    /**
     * This is only used twice in the whole geotools code base, and  one of
     * those is for a test, so we're removing it from the interface. If
     * getAttributeType does not have the AttributeType it will just return
     * null.  Gets the number of occurrences of this attribute.
     *
     * @param xPath XPath pointer to attribute type.
     *
     * @return Number of occurrences.
     */
    public boolean hasAttributeType(String xPath) {
        return getAttributeType(xPath) != null;
    }

    /**
     * Returns the number of attributes at the first 'level' of the schema.
     *
     * @return equivalent value to getAttributeTypes().length
     */
    public int getAttributeCount() {
        return children.length;
    }

    /**
     * Gets the attributeType at this xPath, if the specified attributeType
     * does not exist then null is returned.
     *
     * @param xPath XPath pointer to attribute type.
     *
     * @return True if attribute exists.
     */
    public AttributeType getAttributeType(String xPath) {
        AttributeType attType = null;
        int idx = find(xPath);

        if (idx >= 0) {
            attType = children[idx];
        }

        return attType;
    }

    /**
     * Find the position of a given AttributeType.
     *
     * @param type The type to search for.
     *
     * @return -1 if not found, a zero-based index if found.
     */
    public int find(AttributeType type) {
        if (type == null) {
            return -1;
        }

        int idx = find(type.getLocalName());

        if ((idx < 0) || !children[idx].equals(type)) {
            idx = -1;
        }

        return idx;
    }

    /**
     * Find the position of an AttributeType which matches the given String.
     *
     * @param attName the name to look for
     *
     * @return -1 if not found, zero-based index otherwise
     */
    public int find(String attName) {
        int i = 0;

        while ((i < children.length) && !attName.equals(children[i].getLocalName()))
            i++;

        return (i == children.length) ? (-1) : i;
    }

    /**
     * Gets the attributeType at the specified index.
     *
     * @param position the position of the attribute to check.
     *
     * @return The attribute type at the specified position.
     */
    public AttributeType getAttributeType(int position) {
        return children[position];
    }

    public AttributeType[] getAttributeTypes() {
        return (AttributeType[]) children.clone();
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof ChoiceAttributeType)) {
            return false;
        }

        ChoiceAttributeType att = (ChoiceAttributeType) other;

        if (!super.equals(att)) {
        	return false;
        }
       
        //hmmm... This makes the assumption that the order of the choices
        //matters - not sure if that's true.  Though the order does matter a
        //a bit for our parse method, so this is probably right, since two
        //with different orders could have diff. behaviors for that method.
        if (!Arrays.equals(children, att.getAttributeTypes())) {
            return false;
        }

        return true;
    }

    /**
     * Override of hashCode.
     *
     * @return hashCode for this object.
     */
    public int hashCode() {
        int hash = super.hashCode();

        for (int i = 0, ii = children.length; i < ii; i++) {
            hash ^= children[i].hashCode();
        }

        return hash;
    }

    /**
     * Gets a representation of this object as a string.
     *
     * @return A representation of this object as a string
     */
    public String toString() {
        String details = "name=" + name;
        details += ((" , nillable=" + isNillable()) + ", min=" + getMinOccurs() + ", max=" + getMaxOccurs());
        details += (", choices: " + Arrays.asList(children));

        return "ChoiceAttributeType [" + details + "]";
    }

    /**
     * A special class that is made so a Choice can serve as the Default
     * Geometry in a FeatureType, by implementing GeometryAttributeType. It
     * must be a choice between other GeometryAttributeTypes.
     *
     * @author Chris Holmes, TOPP
     *
     * @task TODO: Need to write code to check that all the geometry attributes
     *       are in the same crs.  Right now we just blindly assume they are
     *       and return the first.
     */
    public static final class Geometric extends ChoiceAttributeType
        implements GeometryAttributeType {
        public Geometric(Geometric copy) {
            super(copy);
        }

        // The field for 'Class type' should be added when GT has moved to java 1.5
        public Geometric(String name, int min, int max,
            GeometryAttributeType[] children, Filter restriction) {
            super(name, min, max, children, restriction);
        }

        public Geometric(String name, GeometryAttributeType[] children) {
            super(name, children);
        }

        public GeometryType getType() {
           return new GeometryTypeImpl( getName(), Geometry.class, getCoordinateSystem(), false, false, null, null, null);
        }
        
        
        public CoordinateReferenceSystem getCoordinateSystem() {
            //Hack - this is not guaranteed to be right, since right now we
            //don't check in the constructors that all crses are the same.
            GeometryAttributeType first = (GeometryAttributeType) getAttributeType(0);

            return first.getCoordinateSystem();
        }
        
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        	return getCoordinateSystem();
        }

        public GeometryFactory getGeometryFactory() {
            //Hack - this is not guaranteed to be right, since right now we
            //don't check in the constructors that all crses are the same.
            GeometryAttributeType first = (GeometryAttributeType) getAttributeType(0);

            return first.getGeometryFactory();
        }

        /* (non-Javadoc)
         * @see org.geotools.feature.AttributeType#isGeometry()
         */
        public boolean isGeometry() {
            return true;
        }
    }
}
