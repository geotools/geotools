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
import net.opengis.wcs10.CapabilitiesSectionType;
import org.geotools.wcs.WCS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wcs:CapabilitiesSectionType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;simpleType name=&quot;CapabilitiesSectionType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Identification of desired part of full Capabilities XML document to be returned. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base=&quot;string&quot;&gt;
 *          &lt;enumeration value=&quot;/&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *          &lt;enumeration value=&quot;/WCS_Capabilities/Service&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *          &lt;enumeration value=&quot;/WCS_Capabilities/Capability&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *          &lt;enumeration value=&quot;/WCS_Capabilities/ContentMetadata&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;TBD. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class CapabilitiesSectionTypeBinding extends AbstractSimpleBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.CapabilitiesSectionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return CapabilitiesSectionType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        return CapabilitiesSectionType.get((String) value);
    }
}
