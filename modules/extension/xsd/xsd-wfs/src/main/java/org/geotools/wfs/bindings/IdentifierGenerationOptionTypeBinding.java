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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;


/**
 * Binding object for the type http://www.opengis.net/wfs:IdentifierGenerationOptionType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="IdentifierGenerationOptionType"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:enumeration value="UseExisting"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The UseExsiting value indicates that WFS should not
 *                    generate a new feature identifier for the feature
 *                    being inserted into the repositry.  Instead, the WFS
 *                    should use the identifier encoded if the feature.
 *                    If a duplicate exists then the WFS should raise an
 *                    exception.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:enumeration&gt;
 *          &lt;xsd:enumeration value="ReplaceDuplicate"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The ReplaceDuplicate value indicates that WFS should
 *                    not generate a new feature identifier for the feature
 *                    being inserted into the repositry.  Instead, the WFS
 *                    should use the identifier encoded if the feature.
 *                    If a duplicate exists then the WFS should replace the
 *                    existing feature instance with the one encoded in the
 *                    Insert action.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:enumeration&gt;
 *          &lt;xsd:enumeration value="GenerateNew"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The GenerateNew value indicates that WFS should
 *                    generate a new unique feature identifier for the
 *                    feature being inserted into the repositry.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:enumeration&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class IdentifierGenerationOptionTypeBinding extends AbstractSimpleBinding {
    public IdentifierGenerationOptionTypeBinding(WfsFactory factory) {
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.IdentifierGenerationOptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        //TODO: implement and remove call to super
        return super.parse(instance, value);
    }
}
