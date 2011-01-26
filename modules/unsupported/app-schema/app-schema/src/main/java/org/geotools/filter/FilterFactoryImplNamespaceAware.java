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

package org.geotools.filter;

import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

public class FilterFactoryImplNamespaceAware extends FilterFactoryImpl {

    private NamespaceSupport namespaceContext;

    /**
     * Empty constructor, no namespace context received, behaves exactly like
     * {@link FilterFactoryImpl}
     */
    public FilterFactoryImplNamespaceAware() {
        super();
    }

    public FilterFactoryImplNamespaceAware(NamespaceSupport namespaceContext) {
        setNamepaceContext(namespaceContext);
    }

    // @Override
    public PropertyName property(String name) {
        return property(name, namespaceContext);
    }

    public void setNamepaceContext(NamespaceSupport namespaceContext) {
        this.namespaceContext = namespaceContext;
    }
}
