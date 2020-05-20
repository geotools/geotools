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
import org.eclipse.emf.common.util.UniqueEList;
import org.geotools.filter.v2_0.FES;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the type http://www.opengis.net/ogc:SortByType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="SortByType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="SortProperty" type="ogc:SortPropertyType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 * </pre>
 *
 * @generated
 */
public class SortByTypeBinding extends org.geotools.filter.v1_1.SortByTypeBinding {

    public SortByTypeBinding(FilterFactory filterfactory) {
        super(filterfactory);
    }

    /** @generated */
    public QName getTarget() {
        return FES.SortByType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return UniqueEList.class;
    }

    public Object getProperty(Object object, QName name) throws Exception {
        if ("SortProperty".equals(name.getLocalPart())) {
            UniqueEList sortBy = (UniqueEList) object;

            return sortBy;
        }

        return null;
    }
}
