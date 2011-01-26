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

import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortOrder;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.Binding;
import org.geotools.xml.InstanceComponent;


/**
 * Binding object for the type http://www.opengis.net/ogc:SortOrderType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="SortOrderType"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:enumeration value="DESC"/&gt;
 *          &lt;xsd:enumeration value="ASC"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class SortOrderTypeBinding extends AbstractSimpleBinding {
    FilterFactory filterfactory;

    public SortOrderTypeBinding(FilterFactory filterfactory) {
        this.filterfactory = filterfactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.SortOrderType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SortOrder.class;
    }

    public int getExecutionMode() {
        return Binding.OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        if ("ASC".equals(value)) {
            return SortOrder.ASCENDING;
        }

        if ("DESC".equals(value)) {
            return SortOrder.DESCENDING;
        }

        return null;
    }

    public String encode(Object object, String value) throws Exception {
        SortOrder sortOrder = (SortOrder) object;

        if (sortOrder == SortOrder.ASCENDING) {
            return "ASC";
        }

        if (sortOrder == SortOrder.DESCENDING) {
            return "DESC";
        }

        return null;
    }
}
