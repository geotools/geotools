/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml2.simple;

import javax.xml.namespace.QName;

/**
 * A QName subclass holding the fully qualified name as a field (important performance wise,
 * ContentHandler writes out XML much faster if given the fully qualified name as opposed to
 * namespace URI and local name)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class QualifiedName extends QName {
    private static final long serialVersionUID = -132059274810468074L;

    String qualifiedName;

    String prefix;

    public QualifiedName(String namespaceURI, String localPart) {
        super(namespaceURI, localPart);
    }

    public QualifiedName(String namespaceURI, String localPart, String prefix) {
        super(namespaceURI, localPart, prefix);
        this.prefix = prefix;
        this.qualifiedName = prefix + ":" + localPart;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Derives a new {@link QualifiedName} with the given prefix, or returns itself if the prefix is
     * the same alredy in use
     */
    public QualifiedName derive(String prefix) {
        return derive(prefix, getNamespaceURI());
    }

    public QualifiedName derive(String prefix, String uri) {
        if (prefix.equals(this.prefix) && uri.equals(getNamespaceURI())) {
            return this;
        } else {
            return new QualifiedName(uri, getLocalPart(), prefix);
        }
    }

    /**
     * This method is used to replicate a "feature" of the standard encoder, when no prefix can be
     * found, "null" will be used instead of having the code throw an exception
     */
    public static QualifiedName build(String targetNamespace, String name, String prefix) {
        if (prefix == null) {
            return new QualifiedName(targetNamespace, name, "null");
        } else {
            return new QualifiedName(targetNamespace, name, prefix);
        }
    }
}
