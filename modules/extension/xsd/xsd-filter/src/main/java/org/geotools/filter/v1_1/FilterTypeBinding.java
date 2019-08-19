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
package org.geotools.filter.v1_1;

import java.util.HashSet;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.geotools.xsd.filter.FilterParsingUtils;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.Identifier;

/**
 * Binding object for the type http://www.opengis.net/ogc:FilterType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="FilterType"&gt;
 *      &lt;xsd:choice&gt;
 *          &lt;xsd:element ref="ogc:spatialOps"/&gt;
 *          &lt;xsd:element ref="ogc:comparisonOps"/&gt;
 *          &lt;xsd:element ref="ogc:logicOps"/&gt;
 *          &lt;xsd:element maxOccurs="unbounded" ref="ogc:_Id"/&gt;
 *      &lt;/xsd:choice&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class FilterTypeBinding extends AbstractComplexBinding {
    FilterFactory filterFactory;

    public FilterTypeBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    /** @generated */
    public QName getTarget() {
        return OGC.FilterType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Filter.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // &lt;xsd:element ref="ogc:spatialOps"/&gt;
        // &lt;xsd:element ref="ogc:comparisonOps"/&gt;
        // &lt;xsd:element ref="ogc:logicOps"/&gt;
        if (node.hasChild(Filter.class)) {
            return node.getChildValue(Filter.class);
        }

        // no direct child filter, check for ids
        // &lt;xsd:element maxOccurs="unbounded" ref="ogc:_Id"/&gt;
        List ids = node.getChildValues(Identifier.class);
        if (!ids.isEmpty()) {
            return filterFactory.id(new HashSet(ids));
        }

        // try an extended operator (part of filter/fes 2.0)
        List<Filter> extOps = FilterParsingUtils.parseExtendedOperators(node, filterFactory);
        if (!extOps.isEmpty()) {
            return extOps.get(0);
        }
        return null;
    }

    public Object getProperty(Object object, QName name) throws Exception {
        return FilterParsingUtils.Filter_getProperty(object, name);
    }
}
