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
 *
 */

package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import net.opengis.wcs10.InterpolationMethodType;
import org.geotools.wcs.WCS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wcs:InterpolationMethodType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;simpleType name=&quot;InterpolationMethodType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Codes that identify interpolation methods. The meanings of these codes are defined in Annex B of ISO 19123: Geographic information  Schema for coverage geometry and functions. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base=&quot;string&quot;&gt;
 *          &lt;enumeration value=&quot;nearest neighbor&quot;/&gt;
 *          &lt;enumeration value=&quot;bilinear&quot;/&gt;
 *          &lt;enumeration value=&quot;bicubic&quot;/&gt;
 *          &lt;enumeration value=&quot;lost area&quot;/&gt;
 *          &lt;enumeration value=&quot;barycentric&quot;/&gt;
 *          &lt;enumeration value=&quot;none&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;No interpolation. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt;
 *
 * </code>
 * </pre>
 *
 * @generated
 */
public class InterpolationMethodTypeBinding extends AbstractSimpleBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.InterpolationMethodType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return InterpolationMethodType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {

        return InterpolationMethodType.get((String) value);
    }
}
