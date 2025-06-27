/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.filter.v2_0.FES;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:ValueReference.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="ValueReference" substitutionGroup="fes:expression" type="xsd:string"/&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class ValueReferenceBinding extends AbstractSimpleBinding {

    FilterFactory filterFactory;

    NamespaceSupport namespaceSupport;

    public ValueReferenceBinding(FilterFactory filterFactory, NamespaceSupport namespaceSupport) {
        this.filterFactory = filterFactory;
        this.namespaceSupport = namespaceSupport;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return FES.ValueReference;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return PropertyName.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        return filterFactory.property((String) value, GML3EncodingUtils.copyNamespaceSupport(namespaceSupport));
    }
}
