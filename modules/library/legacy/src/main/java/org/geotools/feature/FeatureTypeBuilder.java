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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Factory;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A schema builder, because FeatureTypes are meant to be immutable, this
 * object is mutable.
 * 
 * <p>
 * The basic idea for usage is that you configure the builder to whatever state
 * is desired, setting properties and adding AttributeTypes. When the desired
 * state is acheived, the expected FeatureType can be retrieved by calling<br>
 * <code>getFeatureType()</code>
 * </p>
 * <p>
 * Repeated calls to getFeatureType will return the <i>same</i> FeatureType
 * given that no calls which modify the state of the factory are made.
 * </p>
 * 
 * <p>
 * Here's an example of how to use this: 
 * <code><pre> 
 * FeatureTypeBuilder build = FeatureTypeFactory.newInstance();  
 * build.addType(...);
 * build.setName(...);  
 * build.setNamespace(...);  
 * FeatureType type = build.getFeatureType(); 
 * </pre></code>
 * There are also a set of convenience methods for creation of FeatureTypes.
 * These are the various newFeatureType methods.
 * </p>
 * 
 * </p>
 *
 * @author Ian Schneider
 * @source $URL$
 * @version $Id$
 */
public abstract class FeatureTypeBuilder extends FeatureTypes implements Factory {
	
	/** abstract base type for all feature types */
    public final static FeatureType ABSTRACT_FEATURE_TYPE;
    static {
        FeatureType featureType = null;
        try {
            featureType = new DefaultFeatureType("Feature",new URI("http://www.opengis.net/gml"), null, null, null);
        }
        catch(Exception e ) {
            //shold not happen
        }
        ABSTRACT_FEATURE_TYPE = featureType;
    }
    
    /** If the base types have been initialized */
    private static boolean initialized;

    /** The name to give the FeatureType to be created. */
    private String name;

    /** The namespace to give the FeatureType to be created. */
    private URI namespace;

    /** If something in the factory has changed. */
    private boolean dirty = true;

    /** The type created. */
    private FeatureType type = null;

    /** The current defaultGeometry of the FeatureType returned. */
    private GeometryAttributeType defaultGeometry = null;

    /** If the type is abstract. */
    private boolean abstractType = false;

    /** The types that this is derived from. */
    private java.util.Set superTypes;

    /**
     * Implementation hints - since this is a builder all
     * hints are passed onto the FeatureType.
     */
    Map hints;
    
    /**
     * An empty public constructor. Subclasses should not provide a
     * constructor.
     * @deprecated 
     */
    public FeatureTypeBuilder() {
    	this( Collections.EMPTY_MAP );
    }
    
    /**
     * An empty public constructor. Subclasses should not provide a
     * constructor.
     */
    public FeatureTypeBuilder( Map hints) {
    	this.hints = hints;
    }
    
    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     * <p>
     * Since the building of a FeatureType involves the collaboration of may
     * Factory classes (that may be discovered over the course of the build process)
     * we are forced to indicate that *all* hints are used.
     * </p>
     * <p>
     * Strictly this is a Builder (not a factory) and has no need declair which
     * hints are used (as one can never *keep* this builder in a factory registery.
     * (It is stateful and cannot be used concurrently for example).
     */
    public Map getImplementationHints() {
        return hints;
    }    
    /**
     * Create a new FeatureTypeFactory with the given typeName.
     *
     * @param name The typeName of the feature to create.
     *
     * @return A new FeatureTypeFactory instance.
     *
     * @throws FactoryRegistryException If there exists a configuration error.
     */
    public static FeatureTypeFactory newInstance(String typeName)
        throws FactoryRegistryException {
        
        // warning not sure if CommonFactoryFinder is going to cache the instance or not?
        //
        Hints hints = GeoTools.getDefaultHints();
        if( hints == null ){
            hints = new Hints( Hints.FEATURE_TYPE_FACTORY_NAME, typeName );
        }
        else {
            hints.put( Hints.FEATURE_TYPE_FACTORY_NAME, typeName );
        }
        hints.put( Hints.FEATURE_TYPE_FACTORY_NAME, typeName );
        return new DefaultFeatureTypeFactory();
    }
    
    /**
     * Import all of the AttributeTypes from the given FeatureType into this
     * factory.
     * 
     * <p>
     * If strict is true, non-uniquely named AttributeTypes will throw an
     * exception.
     * </p>
     * 
     * <p>
     * If strict is false, these will be silently ignored, but not added.
     * </p>
     * 
     * <p>
     * No other information is imported.
     * </p>
     *
     * @param type The FeatureType to import from.
     * @param strict Enforce namespace restrictions.
     *
     * @throws IllegalArgumentException If strict is true and there are naming
     *         problems.
     */
    public void importType(FeatureType type, boolean strict)
        throws IllegalArgumentException {
        for (int i = 0, ii = type.getAttributeCount(); i < ii; i++) {
            try {
                addType(type.getAttributeType(i));
            } catch (IllegalArgumentException iae) {
                if (strict) {
                    throw iae;
                }
            }
        }
    }

    /**
     * Set the super types of this factory. The types will be copied into a
     * Set.
     *
     * @param types A Collection of types.
     */
    public final void setSuperTypes(java.util.Collection types) {
        superTypes = new java.util.LinkedHashSet(types);
    }

    /**
     * Obtain the super types of this factory. Any user types will be appended
     * to the built in types of this factory.
     *
     * @return A Collection representing the super types of the FeatureType
     *         this factory will create.
     */
    public final java.util.Collection getSuperTypes() {
        Set supers = (superTypes == null) ? new HashSet() : superTypes;
        
        boolean add = true;
        for ( java.util.Iterator s = supers.iterator(); s.hasNext(); ) {
            FeatureType superType = (FeatureType) s.next();
            if ( superType.isDescendedFrom(ABSTRACT_FEATURE_TYPE) ) {
                add = false;
            }
        }
        if ( add ) {
            supers.add(ABSTRACT_FEATURE_TYPE);
        }

        return supers;
    }

    /**
     * A convienence method for importing AttributeTypes, simply calls<br>
     * <code> importType(type,false) </code>
     *
     * @param type The type to import.
     */
    public void importType(FeatureType type) {
        importType(type, false);
    }

    /**
     * Set the name of the FeatureType this factory will produce.
     *
     * @param name The new name. May be null.
     */
    public void setName(String name) {
        dirty |= isDifferent(name, this.name);
        this.name = name;
    }

    /**
     * Get the current configuration of the name of this factory.
     *
     * @return The current name. May be null.
     */
    public final String getName() {
        return name;
    }

    /**
     * Set the namespace of the FeatureType this factory will produce.
     *
     * @param namespace The new namespace. May be null.
     */
    public void setNamespace(URI namespace) {
        dirty |= isDifferent(namespace, this.namespace);
        this.namespace = namespace;
    }

    /**
     * Get the current configuration of the namespace of this factory.
     *
     * @return The current namespace. May be null.
     */
    public final URI getNamespace() {
        return namespace;
    }

    /**
     * Is this factory configured to be abstract?
     *
     * @return True if it is, false if it aint.
     */
    public final boolean isAbstract() {
        return abstractType;
    }

    /**
     * Configure this factory to produce an abstract type.
     *
     * @param a True or false.
     */
    public final void setAbstract(boolean a) {
        dirty = true;
        this.abstractType = a;
    }

    private boolean isDifferent(String s1, String s2) {
        if (s1 != null) {
            return !s1.equals(s2);
        }

        if (s2 != null) {
            return !s2.equals(s1);
        }

        return s1 != s2;
    }
    private boolean isDifferent(URI u1, URI u2) {
        if (u1 != null) {
            return !u1.equals(u2);
        }

        if (u2 != null) {
            return !u2.equals(u1);
        }

        return u1 != u2;
    }
    /**
     * Remove all the AttributeTypes in this factory.
     */
    public final void removeAll() {
        int cnt = getAttributeCount();

        for (int i = cnt; i > 0; i++) {
            removeType(i - 1);
        }
    }

    /**
     * Add an array of AttributeTypes to this factory.
     *
     * @param types The types or a null array.
     *
     * @throws NullPointerException If any of the types are null.
     * @throws IllegalArgumentException If there are naming problems.
     */
    public final void addTypes(AttributeType[] types)
        throws NullPointerException, IllegalArgumentException {
        if (types == null) {
            return;
        }

        for (int i = 0; i < types.length; i++) {
            addType(types[i]);
        }
    }

    /**
     * A the given AttributeType to this factory.
     *
     * @param type The type to add.
     *
     * @throws NullPointerException If the type is null.
     * @throws IllegalArgumentException If another type exists with the same
     *         name.
     */
    public final void addType(AttributeType type)
        throws NullPointerException, IllegalArgumentException {
        if (type == null) {
            throw new NullPointerException("type");
        }

        dirty = true;
        check(type);
        add(type);
    }

    /**
     * Remove the given type from this factory.
     *
     * @param type The type to remove.
     *
     * @throws NullPointerException If the type is null.
     */
    public final void removeType(AttributeType type)
        throws NullPointerException {
        if (type == null) {
            throw new NullPointerException("type");
        }

        dirty = true;

        AttributeType removed = remove(type);

        if (removed == defaultGeometry) {
            defaultGeometry = null;
        }
    }

    /**
     * Insert the given type at the index specified.
     *
     * @param idx The index to insert at.
     * @param type The AttributeType to insert.
     *
     * @throws NullPointerException If the type is null.
     * @throws IllegalArgumentException If the AttributeType is not allowed.
     * @throws ArrayIndexOutOfBoundsException If the index is out of range.
     */
    public final void addType(int idx, AttributeType type)
        throws NullPointerException, IllegalArgumentException, 
            ArrayIndexOutOfBoundsException {
        if (type == null) {
            throw new NullPointerException("type");
        }

        dirty = true;
        check(type);
        add(idx, type);
    }

    /**
     * Remove the AttributeType at the given index.
     *
     * @param idx The index to remove at.
     *
     * @throws ArrayIndexOutOfBoundsException If the index is out of bounds.
     */
    public final void removeType(int idx) throws ArrayIndexOutOfBoundsException {
        dirty = true;

        AttributeType removed = remove(idx);

        if (removed == defaultGeometry) {
            defaultGeometry = null;
        }
    }

    /**
     * Set the AttributeType at the given index. Overwrites the existing type.
     *
     * @param idx The index to use.
     * @param type The type to use.
     *
     * @throws IllegalArgumentException If the type is not good.
     * @throws NullPointerException if they type passed in is null
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds.
     */
    public final void setType(int idx, AttributeType type)
        throws IllegalArgumentException, NullPointerException, 
            ArrayIndexOutOfBoundsException {
        if (type == null) {
            throw new NullPointerException("type");
        }

        dirty = true;
        check(type);

        AttributeType removed = set(idx, type);

        if (removed == defaultGeometry) {
            defaultGeometry = null;
        }
    }

    /**
     * Swap the AttributeTypes at the given locations.
     *
     * @param idx1 The index of the first.
     * @param idx2 The index of the second.
     *
     * @throws ArrayIndexOutOfBoundsException if either index is not in the
     *         array bounds.
     */
    public final void swap(int idx1, int idx2)
        throws ArrayIndexOutOfBoundsException {
        // implementation note:
        // we must rely on the subclass implementation, which, hopefully does
        // not do any checking. If we used setType, there is a name overlap.
        AttributeType tmp = get(idx1);
        set(idx1, get(idx2));
        set(idx2, tmp);

        // must do this!
        dirty = true;
    }

    /**
     * Return the AttributeType currently used as the defaultGeometry property
     * for the FeatureType this factory will create.
     *
     * @return The AttributeType representing the defaultGeometry or null.
     */
    public final GeometryAttributeType getDefaultGeometry() {
        return defaultGeometry;
    }

    /**
     * Sets the defaultGeometry of this factory. If the defaultGeometry
     * AttributeType does not exist as an AttributeType within this factory,
     * it is added. This will overwrite the existing defaultGeometry, yet not
     * remove it from the existing AttributeTypes.
     *
     * @param defaultGeometry The AttributeType to use as the defaultGeometry.
     *        May be null.
     *
     * @throws IllegalArgumentException if the type is not a geometry.
     */
    public final void setDefaultGeometry(GeometryAttributeType defaultGeometry)
        throws IllegalArgumentException {
        // check if Geometry
        if ((defaultGeometry != null) && !defaultGeometry.isGeometry()) {
            String mess = "Attempted to set a non-geometry type as "
                + "defaultGeometry: ";
            throw new IllegalArgumentException(mess + defaultGeometry);
        }

        dirty = true; // do this!
        this.defaultGeometry = defaultGeometry;

        // if the defaultGeometry hasn't been added, add it!
        if ((defaultGeometry != null) && !contains(defaultGeometry)) {
            addType(defaultGeometry);
        }
    }

    /**
     * Get a FeatureType which reflects the state of this factory. Any
     * modifications to the state of the factory (adding, removing, or
     * reordering any AttributeTypes or changing any other properties -
     * isNillable,name,etc.), will cause the factory to "retool" itself. If
     * the factory has not changed since a call to this method, the return
     * value will be the same FeatureType which the previous method returned.
     * Otherwise, a new FeatureType will be created.
     *
     * @return The featureType reflecting the current factory state.
     *
     * @throws SchemaException if name is null or blank
     */
    public final FeatureType getFeatureType() throws SchemaException {
        // we're dirty, recreate the FeatureType
        if (dirty || (type == null)) {
            // no defaultGeometry assigned, search for one.
            if (defaultGeometry == null) {
                for (int i = 0, ii = getAttributeCount(); i < ii; i++) {
                    if (get(i) instanceof GeometryAttributeType) {
                        defaultGeometry = (GeometryAttributeType) get(i);
                        break;
                    }
                }
            }

            if ((name == null) || (name.trim().length() == 0)) {
                throw new SchemaException(
                    "Cannot create FeatureType with null or blank name");
            }

            type = createFeatureType();

            // oops, the subclass messed up...
            if (type == null) {
                throw new NullPointerException(getClass().getName()
                    + ".createFeatureType()");
            }

            if (isAbstract() && !type.isAbstract()) {
                throw new RuntimeException(
                    "FeatureTypeFactory poorly implemented, "
                    + "expected abstract type, received " + type);
            }

            // not dirty anymore.
            dirty = false;
        }

        return type;
    }

    /**
     * Returns a string representation of this factory.
     *
     * @return The string representing this factory.
     */
    public String toString() {
        String types = "";

        for (int i = 0, ii = getAttributeCount(); i < ii; i++) {
            types += get(i);

            if (i < ii) {
                types += " , ";
            }
        }

        return "FeatureTypeFactory(" + getClass().getName() + ") [ " + types
        + " ]";
    }

    /**
     * Check to see if this factory contains the given AttributeType. The
     * comparison is done by name.
     *
     * @param type The AttributeType to search for by name.
     *
     * @return <tt>true</tt> if a like-named AttributeType exists,
     *         <tt>false</tt> otherwise.
     */
    public final boolean contains(AttributeType type) {
        for (int i = 0, ii = getAttributeCount(); i < ii; i++) {
            if (get(i).getLocalName().equals(type.getLocalName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks to see if this factory already contains the type.
     *
     * @param type
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected void check(AttributeType type) {
        if (contains(type)) {
            throw new IllegalArgumentException("Duplicate AttributeTypes "
                + type);
        }
    }

    protected void addBaseTypes(Set types) {
        // base class hook
    }

    /**
     * DOCUMENT ME!
     *
     */
    protected abstract FeatureType createFeatureType()
        throws SchemaException;

    /**
     * DOCUMENT ME!
     *
     * @param type
     *
     * @throws IllegalArgumentException
     */
    protected abstract void add(AttributeType type)
        throws IllegalArgumentException;

    /**
     * DOCUMENT ME!
     *
     * @param type
     *
     */
    protected abstract AttributeType remove(AttributeType type);

    /**
     * DOCUMENT ME!
     *
     * @param idx
     * @param type
     *
     * @throws ArrayIndexOutOfBoundsException
     * @throws IllegalArgumentException
     */
    protected abstract void add(int idx, AttributeType type)
        throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    /**
     * DOCUMENT ME!
     *
     * @param idx
     *
     *
     * @throws ArrayIndexOutOfBoundsException
     */
    protected abstract AttributeType remove(int idx)
        throws ArrayIndexOutOfBoundsException;

    /**
     * DOCUMENT ME!
     *
     * @param idx
     *
     *
     * @throws ArrayIndexOutOfBoundsException
     */
    public abstract AttributeType get(int idx)
        throws ArrayIndexOutOfBoundsException;

    /**
     * DOCUMENT ME!
     *
     * @param idx
     * @param type
     *
     *
     * @throws ArrayIndexOutOfBoundsException
     * @throws IllegalArgumentException
     */
    protected abstract AttributeType set(int idx, AttributeType type)
        throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    /**
     * DOCUMENT ME!
     *
     */
    public abstract int getAttributeCount();

}
