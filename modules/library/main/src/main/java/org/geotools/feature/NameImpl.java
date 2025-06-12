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

import java.io.Serializable;
import org.geotools.api.feature.type.Name;
import org.geotools.util.Utilities;

/**
 * Simple implementation of Name.
 *
 * <p>This class emulates QName, and is used as the implementation of both AttributeName and TypeName (so when the API
 * settles down we should have a quick fix.
 *
 * <p>Its is advantageous to us to be able to:
 *
 * <ul>
 *   <li>Have a API in agreement with QName - considering our target audience
 *   <li>Strongly type AttributeName and TypeName separately
 * </ul>
 *
 * The ISO interface move towards combining the AttributeName and Attribute classes, and TypeName and Type classes,
 * while we understand the attractiveness of this on a UML diagram it is very helpful to keep these concepts separate
 * when playing with a strongly typed language like java.
 *
 * <p>It case it is not obvious this is a value object and equality is based on namespace and name.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class NameImpl implements org.geotools.api.feature.type.Name, Serializable, Comparable<NameImpl> {
    private static final long serialVersionUID = 4564070184645559899L;

    /** namespace / scope */
    protected String namespace;

    /** local part */
    protected String local;

    private String separator;

    /**
     * Constructs an instance with the local part set. Namespace / scope is set to null.
     *
     * @param local The local part of the name.
     */
    public NameImpl(String local) {
        this(null, local);
    }

    /**
     * Constructs an instance with the local part and namespace set.
     *
     * @param namespace The namespace or scope of the name.
     * @param local The local part of the name.
     */
    public NameImpl(String namespace, String local) {
        this(namespace, ":", local);
    }
    /**
     * Constructs an instance with the local part and namespace set.
     *
     * @param namespace The namespace or scope of the name.
     * @param local The local part of the name.
     */
    public NameImpl(String namespace, String separator, String local) {
        this.namespace = namespace;
        this.separator = separator;
        this.local = local;
    }

    /** Constract an instance from the provided QName. */
    public NameImpl(javax.xml.namespace.QName qName) {
        this(qName.getNamespaceURI(), qName.getLocalPart());
    }

    @Override
    public boolean isGlobal() {
        return getNamespaceURI() == null;
    }

    @Override
    public String getSeparator() {
        return separator;
    }

    @Override
    public String getNamespaceURI() {
        return namespace;
    }

    @Override
    public String getLocalPart() {
        return local;
    }

    @Override
    public String getURI() {
        if ((namespace == null) && (local == null)) {
            return null;
        }
        if (namespace == null) {
            return local;
        }
        if (local == null) {
            return namespace;
        }
        return new StringBuffer(namespace).append(separator).append(local).toString();
    }

    /** Returns a hash code value for this operand. */
    @Override
    public int hashCode() {
        return (namespace == null ? 0 : namespace.hashCode()) + 37 * (local == null ? 0 : local.hashCode());
    }

    /** value object with equality based on name and namespace. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof Name) {
            Name other = (Name) obj;
            if (!Utilities.equals(this.namespace, other.getNamespaceURI())) {
                return false;
            }
            if (!Utilities.equals(this.local, other.getLocalPart())) {
                return false;
            }
            return true;
        }
        return false;
    }

    /** name or namespace:name */
    @Override
    public String toString() {
        return getURI();
    }

    @Override
    public int compareTo(NameImpl other) {
        if (other == null) {
            return 1; // we are greater than null!
        }
        int c = compare(getNamespaceURI(), other.getNamespaceURI());
        return c != 0 ? c : compare(getLocalPart(), other.getLocalPart());
    }

    private int compare(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return s1 == s2 ? 0 : s1 == null ? 1 : -1;
        }
        return s1.compareTo(s2);
    }
}
