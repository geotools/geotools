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
package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.fes20.SortPropertyType;
import org.geotools.filter.v2_0.FES;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;

/**
 * Binding object for the type http://www.opengis.net/ogc:SortPropertyType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="SortPropertyType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element ref="ogc:PropertyName"/&gt;
 *          &lt;xsd:element minOccurs="0" name="SortOrder" type="ogc:SortOrderType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 * </pre>
 *
 * @generated
 */
public class SortPropertyTypeBinding extends org.geotools.filter.v1_1.SortPropertyTypeBinding {

    public SortPropertyTypeBinding(FilterFactory filterfactory) {
        super(filterfactory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return FES.SortPropertyType;
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
        return SortPropertyType.class;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        SortBy sortBy = (SortBy) object;

        if (FES.ValueReference.equals(name)) {
            return sortBy.getPropertyName();
        }

        if ("SortOrder".equals(name.getLocalPart())) {
            return sortBy.getSortOrder();
        }

        return null;
    }
}
